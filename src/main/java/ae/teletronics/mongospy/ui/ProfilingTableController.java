package ae.teletronics.mongospy.ui;


import ae.teletronics.mongospy.db.ProfiledQueryListProvider;
import ae.teletronics.mongospy.db.model.*;
import ae.teletronics.mongospy.db.model.ExecutionStage;
import ae.teletronics.mongospy.ui.components.QueryCellFactory;
import ae.teletronics.mongospy.ui.components.StatsCellFactory;
import com.jfoenix.controls.*;
import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javax.annotation.PostConstruct;

/**
 * A controler for the profiling perspective containing filter and results.
 *
 * @author aleksanderlech
 */
@SuppressWarnings("ambogous")
@ViewController(value = "/fxml/table/ProfilingView.fxml", title = "Mongo profiler")
public class ProfilingTableController {

    // readonly components view
    @FXML
    private TableView<ProfiledQuery> tableView;

    @FXML
    private TableColumn<String, Query> queryColumn;
    @FXML
    private TableColumn<String, ExecutionStage> statsColumn;

    @FXML
    private JFXToggleButton profilingToggleButton;

    @FXML
    private JFXButton clearButton;

    @FXML
    private JFXSlider millisThreshold;
    @FXML
    private JFXTextField collectionField;
    @FXML
    private JFXTextField operationField;
    @FXML
    private JFXProgressBar progressBar;

    private ProfilingTarget profilingTarget;
    private ProfiledQueryListProvider profiledQueryListProvider;


    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {

        //TODO should be passed from side menu
        profilingTarget = ProfilingTarget.builder()
                .host("localhost")
                .port(3001)
                .database("meteor")
                .build();

        queryColumn.setCellFactory(new QueryCellFactory());
        statsColumn.setCellFactory(new StatsCellFactory());
        setupReadOnlyTableView();
    }

    private void setupReadOnlyTableView() {

        profilingToggleButton.setOnAction(e -> toggleProfiling());
        clearButton.setOnAction(e -> tableView.getItems().clear());
        toggleProfiling();

    }

    private void toggleProfiling() {
        progressBar.setVisible(profilingToggleButton.isSelected());

        if (profilingToggleButton.isSelected()) {
            // ask the list provider for an observable collection to be bound to the table

            profiledQueryListProvider = new ProfiledQueryListProvider(profilingTarget,
                    () -> ProfilingFilter.builder()
                            .collection(collectionField.getText())
                            .operation(operationField.getText())
                            .millisThreshold((long) millisThreshold.getValue())
                            .build()
            );

            tableView.setItems(profiledQueryListProvider.get());
        } else if (profiledQueryListProvider != null) {

            // close the provider resource (disable profiling and close DB connection)
            try {
                profiledQueryListProvider.close();
                tableView.getItems().clear();
                profiledQueryListProvider = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

}


