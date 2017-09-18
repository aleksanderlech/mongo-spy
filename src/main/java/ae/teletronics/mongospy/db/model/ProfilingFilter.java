package ae.teletronics.mongospy.db.model;

import lombok.Builder;
import lombok.Data;

/**
 * Holds the profiling filter info data.
 *
 * @author aleksanderlech
 */
@Data
public class ProfilingFilter {

    private String operation;
    private String collection;
    private long millisThreshold;

    @Builder
    public ProfilingFilter(String operation, String collection, long millisThreshold) {
        this.operation = operation;
        this.collection = collection;
        this.millisThreshold = millisThreshold;
    }
}
