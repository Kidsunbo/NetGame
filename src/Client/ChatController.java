package Client;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.json.JSONObject;


import java.util.List;
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
                setText(item);
            }
        }
    }

    @FXML
    private ListView<String> contactList;
    @FXML
    private ListView<String> gameList;
    @FXML
    private ListView<String> chatArea;
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
        jsonObject.put("subtype", "contacts");
        LoginController.getClient().sendMessage(jsonObject);
        contactList.setCellFactory(list -> new MyListViewCell());
        es.execute(() -> {
            while (true) {
                String response_str = LoginController.getClient().receiveMessage();
                if (response_str == null || response_str.equals("")) continue;
                JSONObject response = new JSONObject(response_str);
                if (response.getString("type").equals("forward")) {
                    processNewMessage(response);
                } else if (response.getString("type").equals("forward_response")) {
                    processResponse(response);
                }


            }
        });
    }

    private void processNewMessage(JSONObject jsonObject){

    }

    private void processResponse(JSONObject jsonObject){
        if(jsonObject.getString("subtype").equals("contact_response")){
            List<String> nameList = jsonObject.getJSONArray("contact_names").toList().stream().map(Object::toString).collect(Collectors.toList());
//            String[] name = new String[nameList.size()];
            String[] name = {"hello","hello1","hello2"};
            name = nameList.toArray(name);
            contactList.getItems().addAll(name);
        }
    }


}

