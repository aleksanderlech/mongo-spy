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

        String stage = inputStage.getString("stage");
        Integer nReturned = inputStage.getInteger("nReturned");
        Integer millis = inputStage.getInteger("millis", 0);

        List<Document> parentInputStages = Optional.ofNullable((Document) inputStage.get("inputStage"))
                .map(Arrays::asList)
                .orElse(Optional.ofNullable((List<Document>) inputStage.get("inputStages")).orElse(Collections.emptyList()));

        return new ExecutionStage(stage, millis, nReturned, parentInputStages.stream().map(this::createExecutionStage).collect(Collectors.toList()));

    }


}
