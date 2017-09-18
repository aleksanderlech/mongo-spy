package ae.teletronics.mongospy.db.model;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

/**
 * Corresponds to the db.profile Mongo Db documents to hold the information concerning a single query intercepted while profiling.
 *
 * @author aleksanderlech
 */
@Value
public class ProfiledQuery {

    private Date time;
    private String operation;
    private String collection;
    private long duration;
    private Query query;
    private ExecutionStage stats;

    @Builder
    public ProfiledQuery(Date time, String operation, String collection, long duration, Query query, ExecutionStage stats) {
        this.time = time;
        this.operation = operation;
        this.collection = collection;
        this.duration = duration;
        this.query = query;
        this.stats = stats;
    }
}
