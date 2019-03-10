import com.sun.javafx.font.freetype.HBGlyphLayout;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

public class test extends Application {

    ListView<String> list = new ListView<String>();
    ObservableList<String> data = FXCollections.observableArrayList(
            "chocolate", "salmon", "gold", "coral", "darkorchid",
            "darkgoldenrod", "lightsalmon", "black", "rosybrown", "blue",
            "blueviolet", "brown");
    final Label label = new Label();

    @Override
    public void start(Stage stage) {
        VBox box = new VBox();
        Scene scene = new Scene(box, 200, 200);
        stage.setScene(scene);
        stage.setTitle("ListViewSample");
        box.getChildren().addAll(list, label);
        VBox.setVgrow(list, Priority.ALWAYS);

        label.setLayoutX(10);
        label.setLayoutY(115);
        label.setFont(Font.font("Verdana", 20));

        list.setItems(data);
        list.setCellFactory(list -> new ColorRectCell());
        list.getSelectionModel().selectedItemProperty().addListener(
                (ov, old_val, new_val) -> {
                    label.setText(new_val);
                    label.setTextFill(Color.web(new_val));
                });
        stage.show();
    }

    static class ColorRectCell extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            Rectangle rect = new Rectangle(100, 50);
            if (item != null) {
                rect.setFill(Color.web(item));
                VBox box = new VBox();
                box.setSpacing(5);
                HBox bb = new HBox();
                Label l = new Label(item);
                l.setTextFill(Color.RED);
                box.getChildren().addAll(new Label("xiaohuang"),l);
                bb.getChildren().addAll(rect,box);
                setGraphic(bb);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
