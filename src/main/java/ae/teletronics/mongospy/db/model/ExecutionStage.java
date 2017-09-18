package ae.teletronics.mongospy.db.model;

import lombok.Value;

import java.util.List;

/**
 * Represents a mongo db execution stats (plan) single stage.
 */
@Value
public class ExecutionStage {

    private String name;
    private long millis;
    private Integer nReturned;
    private List<ExecutionStage> parentStages;

    public ExecutionStage(String name, long millis, Integer nReturned, List<ExecutionStage> parentStages) {
        this.name = name;
        this.millis = millis;
        this.nReturned = nReturned;
        this.parentStages = parentStages;
    }
}
