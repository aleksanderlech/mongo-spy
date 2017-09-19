package ae.teletronics.mongospy.db;

import ae.teletronics.mongospy.db.model.*;
import com.mongodb.CursorType;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;

import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Provides an observable collection of {@link ProfiledQuery} instances.
 *
 * @author aleksanderlech
 * @see ProfiledQuery
 */
public class ProfiledQueryListProvider implements Supplier<ObservableList<ProfiledQuery>>, AutoCloseable {

    private ProfilingTarget profilingTarget;
    private Supplier<ProfilingFilter> filterProvider;
    private ValueExtractor<Query> queryExtractor;
    private ValueExtractor<ExecutionStage> statsExtractor;

    private MongoClient mongoClient;

    public ProfiledQueryListProvider(ProfilingTarget profilingTarget, Supplier<ProfilingFilter> filterProvider) {
        this.profilingTarget = profilingTarget;
        this.filterProvider = filterProvider;
        //TODO extend in future?
        queryExtractor = new QueryExtractor();
        statsExtractor = new StatsExtractor();
    }

    @Override
    public ObservableList<ProfiledQuery> get() {

        ProfilingFilter filter = filterProvider.get();
        ObservableList<ProfiledQuery> results = FXCollections.observableArrayList();

        //start tailing thread

        new Thread(() -> {

            //ensure profiling enabled
            toggleProfilingMode(true);

            //tail the system.profile collection for changes
            getDatabase().getCollection("system.profile")
                    .find(
                            Filters.and(
                                    filter.getOperation().isEmpty() ? Filters.exists("op") : Filters.eq("op", filter.getOperation()),
                                    filter.getCollection().isEmpty() ? Filters.exists("ns") : Filters.eq("ns", filter.getCollection()),
                                    Filters.ne("ns", "meteor.system.profile"),
                                    Filters.gte("millis", filter.getMillisThreshold())
                            ))
                    .cursorType(CursorType.TailableAwait)
                    .noCursorTimeout(true)
                    .forEach((Consumer<? super Document>) result -> {



                        results.add(new ProfiledQuery(
                                result.getDate("ts"),
                                result.getString("op"),
                                result.getString("ns"),
                                result.getInteger("millis"),
                                queryExtractor.apply(result).orElse(null),
                                statsExtractor.apply(result).orElse(null)
                        ));

                        Collections.sort(results);

                    });

        }).start();

        return results;
    }

    private void toggleProfilingMode(boolean enable) {
        int desiredLevel = enable ? 2 : 0;
        Document response = getDatabase().runCommand(new Document("profile", desiredLevel));
        if (!response.getDouble("ok").equals(1.0d)) {
            throw new RuntimeException("Unable to change profiling level. Check user permissions.");
        }
    }

    @Override
    public void close() throws Exception {
        if (mongoClient != null) {
            toggleProfilingMode(false);
            mongoClient.close(); //will cause tailing to stop with exception (maybe should be handled the better way ;))
            mongoClient = null;
        }
    }

    private MongoDatabase getDatabase() {
        if (mongoClient == null) {
            mongoClient = new MongoClient(new MongoClientURI(profilingTarget.getMongoUrl()));
        }
        return mongoClient.getDatabase(profilingTarget.getDatabase());
    }
}
