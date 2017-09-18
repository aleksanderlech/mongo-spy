package ae.teletronics.mongospy.db;

import ae.teletronics.mongospy.db.model.ExecutionStage;
import org.bson.BsonArray;
import org.bson.Document;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

        Document originInputStage = (Document) inputStage.get("inputStage");
        BsonArray originInputStages = (BsonArray) inputStage.get("inputStages");

        List<ExecutionStage> parentInputStages;
        if (originInputStage != null) {
            parentInputStages = Collections.singletonList(createExecutionStage(originInputStage));
        } else if (originInputStages != null) {
            //TODO handle that!
            throw new RuntimeException("");
        } else {
            parentInputStages = Collections.emptyList();
        }

        return new ExecutionStage(stage, millis, nReturned, parentInputStages);

    }


}
