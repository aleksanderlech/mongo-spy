package ae.teletronics.mongospy.db;

import ae.teletronics.mongospy.db.model.Query;
import org.bson.Document;

import java.util.Optional;

public class QueryExtractor implements ValueExtractor<Query> {

    @Override
    public Optional<Query> apply(Document document) {

        return extractQueryDocument(document).map(queryDocument -> {

            Document queryFilter = (Document) queryDocument.get("filter");
            String filter = queryFilter == null ? queryDocument.toJson() : queryFilter.toJson();
            String sort = ((Document) queryDocument.get("sort")).toJson();
            Integer skip = queryDocument.getInteger("skip");
            Integer limit = queryDocument.getInteger("limit");

            return new Query(filter, sort, skip, limit);
        });
    }

    private Optional<Document> extractQueryDocument(Document document) {
        Optional<Document> query = Optional.ofNullable((Document) document.get("query"));
        if (!query.isPresent()) {
            query = Optional.ofNullable((Document) document.get("command")).map(command -> command.get("query", Document.class));
        }
        return query;
    }
}
