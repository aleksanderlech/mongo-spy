package ae.teletronics.mongospy.ui.components;

import ae.teletronics.mongospy.db.model.ExecutionStage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
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
                createAndInsertStageBox(container, queryFinalExecutionStage);
                setGraphic(container);
            }
        };
    }

    private void createAndInsertStageBox(Pane parent, ExecutionStage executionStage) {
        try {
            final AnchorPane template = FXMLLoader.load(getClass().getResource("/fxml/table/StageFragment.fxml"));

            //Name
            ((Text) template.lookup("#name")).setText(executionStage.getName());

            //Millis
            ((Text) template.lookup("#millis")).setText(executionStage.getMillis() + " ms");

            //nReturned
            Optional.ofNullable(executionStage.getNReturned()).ifPresent(v -> ((Text) template.lookup("#nReturned")).setText(v.toString()));


            parent.getChildren().add(template);

            //parents
            List<ExecutionStage> parentStages = executionStage.getParentStages();
            if(parentStages.size() == 1) {
                // one parent

                parentStages.stream().findFirst().ifPresent(stage -> createAndInsertStageBox(parent, stage));
            } else if(parentStages.size() > 1) {

                //multiple parents
                VBox vBox = new VBox();
                parent.getChildren().add(vBox);

                parentStages.forEach(stage -> {
                    HBox hBox = new HBox();
                    vBox.getChildren().add(hBox);
                    createAndInsertStageBox(hBox, stage);
                });

            }


        } catch (IOException e) {
            throw new RuntimeException("Template load error.", e);
        }
    }

}
