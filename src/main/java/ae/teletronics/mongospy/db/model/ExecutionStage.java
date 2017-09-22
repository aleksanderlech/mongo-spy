package ae.teletronics.mongospy.db.model;

import lombok.Builder;
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
    private Integer processedDocuments;
    private String indexName;
    private List<ExecutionStage> parentStages;

    @Builder
    public ExecutionStage(String name, long millis, Integer nReturned, Integer processedDocuments, String indexName, List<ExecutionStage> parentStages) {
        this.name = name;
        this.millis = millis;
        this.nReturned = nReturned;
        this.processedDocuments = processedDocuments;
        this.indexName = indexName;
        this.parentStages = parentStages;
    }
}
