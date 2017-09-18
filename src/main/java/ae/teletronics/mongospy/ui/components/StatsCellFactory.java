package ae.teletronics.mongospy.ui.components;

import ae.teletronics.mongospy.db.model.ExecutionStage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Optional;

public class StatsCellFactory implements Callback<TableColumn<String, ExecutionStage>, TableCell<String, ExecutionStage>> {

    @Override
    public TableCell<String, ExecutionStage> call(TableColumn<String, ExecutionStage> param) {

        final HBox container = new HBox();
        return new TableCell<String, ExecutionStage>() {
            @Override
            protected void updateItem(ExecutionStage queryFinalExecutionStage, boolean b) {

                super.updateItem(queryFinalExecutionStage, b);
                if (null == queryFinalExecutionStage) {
                    setGraphic(null);
                    return;
                }

                container.getChildren().clear();
                for (ExecutionStage stage = queryFinalExecutionStage; stage != null; stage = stage.getParentStages().stream().findFirst().orElse(null)) {
                    container.getChildren().add(buildBox(stage));
                }


                setGraphic(container);
            }
        };
    }

    private Node buildBox(ExecutionStage executionStage) {
        try {
            final AnchorPane template = FXMLLoader.load(getClass().getResource("/fxml/table/StageFragment.fxml"));

            //Name
            ((Text) template.lookup("#name")).setText(executionStage.getName());

            //Millis
            ((Text) template.lookup("#millis")).setText(executionStage.getMillis() + " ms");

            //nReturned
            Optional.ofNullable(executionStage.getNReturned()).ifPresent(v -> {
                ((Text) template.lookup("#nReturned")).setText(v.toString());
            });


            return template;

        } catch (IOException e) {
            throw new RuntimeException("Template load error.", e);
        }
    }

}
