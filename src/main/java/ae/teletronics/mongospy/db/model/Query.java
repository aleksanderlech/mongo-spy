package ae.teletronics.mongospy.db.model;

import lombok.Builder;
import lombok.Value;

/**
 * Represents a single mongo find() execution data.
 *
 * @author aleksanderlech
 */
@Value
public class Query {

    String filter;
    String sort;
    Integer skip;
    Integer limit;

    @Builder
    public Query(String filter, String sort, Integer skip, Integer limit) {
        this.filter = filter;
        this.sort = sort;
        this.skip = skip;
        this.limit = limit;
    }
}
