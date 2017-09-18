package ae.teletronics.mongospy.ui.components;

import ae.teletronics.mongospy.db.model.Query;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.IOException;

public class QueryCellFactory implements Callback<TableColumn<String, Query>, TableCell<String, Query>> {

    @Override
    public TableCell<String, Query> call(TableColumn<String, Query> param) {

        try {

            final AnchorPane template = FXMLLoader.load(getClass().getResource("/fxml/table/QueryCell.fxml"));

            return new TableCell<String, Query>() {
                @Override
                protected void updateItem(Query q, boolean b) {
                    super.updateItem(q, b);
                    if (null == q) {
                        setGraphic(null);
                        return;
                    }

                    //Filter
                    template.lookup("#filter").setVisible(!q.getFilter().isEmpty());
                    ((Text) template.lookup("#filter")).setText(q.getFilter());

                    //Sort
                    template.lookup("#sort").setVisible(q.getSort() != null);
                    ((Text) template.lookup("#sort")).setText(q.getSort());

                    setGraphic(template);
                }
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
