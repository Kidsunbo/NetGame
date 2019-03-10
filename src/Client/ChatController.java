package Client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


/**
 * Created by bxs863 on 06/03/19.
 */
public class ChatController {

    private ExecutorService es = Executors.newFixedThreadPool(5, r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
    });

    class MyListViewCell extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {

            }
        }
    }

    @FXML
    private ListView<String> contactList;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button sendBtn;
    @FXML
    private Button startBtn;

    @FXML
    public void initialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "forward");
        jsonObject.put("sub_type", "contacts");
        contactList.setCellFactory(list -> new MyListViewCell());
        es.execute(() -> {
            while (true) {
                try {
                    JSONObject result = LoginController.getClient().send(jsonObject).get();
                    String[] name = new String[0];
                    if(result.getString("type").equals("forward_response") && result.getString("subtype").equals("contact_response")){
                        List<String> nameList = result.getJSONArray("contact_names").toList().stream().map(Object::toString).collect(Collectors.toList());
                        name = new String[nameList.size()];
                        name = nameList.toArray(name);
                    }
                    contactList.getItems().addAll(name);


                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

