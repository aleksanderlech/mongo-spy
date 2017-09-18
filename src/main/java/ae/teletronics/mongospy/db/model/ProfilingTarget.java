package ae.teletronics.mongospy.db.model;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a connection info for the target database that is to be profiled.
 *
 * @author aleksanderlech
 */
@Data
public class ProfilingTarget {

    private String host;
    private int port;
    private String database;

    @Builder
    public ProfilingTarget(String host, int port, String database) {
        this.host = host;
        this.port = port;
        this.database = database;
    }

    public String getMongoUrl() {
        return "mongodb://" + host + ":" + port;
    }
}
