package ae.teletronics.mongospy.db;

import ae.teletronics.mongospy.db.model.ExecutionStage;
import org.bson.Document;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StatsExtractor implements ValueExtractor<ExecutionStage> {

    @Override
    public Optional<ExecutionStage> apply(Document document) {
        return Optional.ofNullable((Document) document.get("execStats"))
                .filter(statsDocument -> !statsDocument.isEmpty())
                .map(this::createExecutionStage);

    }

    private ExecutionStage createExecutionStage(Document inputStage) {

        return new ExecutionStage(inputStage.getString("stage"),
                inputStage.getInteger("millis", 0),
                inputStage.getInteger("nReturned"),
                inputStage.getInteger("processedDocuments", 0),
                inputStage.getString("indexName"),
                Optional.ofNullable((Document) inputStage.get("inputStage"))
                        .map(Arrays::asList)
                        .orElse(Optional.ofNullable((List<Document>) inputStage.get("inputStages")).orElse(Collections.emptyList()))
                        .stream().map(this::createExecutionStage).collect(Collectors.toList()));

    }


}
