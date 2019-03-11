package Client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


/**
 * Created by bxs863 on 06/03/19.
 */
public class ChatController {
    public static String username;
    private static Client client = LoginController.getClient();


    private ExecutorService es = Executors.newFixedThreadPool(5, r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
    });

    private Hashtable<String,ListView<String>> table = new Hashtable<>();

    class MyListViewCell extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                if(!table.containsKey(item)) table.put(item,new ListView<>());
                setText(item);
            }
        }
    }

    @FXML
    private ListView<String> contactList;
    @FXML
    private ListView<String> gameList;
    @FXML
    private TextArea inputArea;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button sendBtn;
    @FXML
    private Button startBtn;
    @FXML
    public ListView<String> chatArea;


    @FXML
    public void initialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "forward");
        jsonObject.put("subtype", "contact");
        LoginController.getClient().sendMessage(jsonObject);
        contactList.setCellFactory(list->new MyListViewCell());
        contactList.getSelectionModel().selectedItemProperty().addListener(
                (ov, old_val, new_val) -> {
                    if(old_val!=null){
                        table.get(old_val).setItems(chatArea.getItems());
                        chatArea.setItems(table.get(new_val).getItems());
                    }
                });
        es.execute(() -> {
            while (true) {
                String response_str = LoginController.getClient().receiveMessage();
                System.out.println(response_str);

                if (response_str == null || response_str.equals("")) Platform.exit();
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
            String[] name = {"tom","mary","lucy","jenny"};
//            name = nameList.toArray(name);
            contactList.getItems().addAll(name);
            contactList.getSelectionModel().selectFirst();
        }
    }

    public void sendMessage(MouseEvent mouseEvent) {
        String msg = inputArea.getText();
        chatArea.getItems().add(msg);
        inputArea.clear();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","forward");
        jsonObject.put("subtype","forward");
        jsonObject.put("to_user",contactList.getSelectionModel().getSelectedItem());
        jsonObject.put("message",msg);
        jsonObject.put("from_user",username);
        es.execute(()->LoginController.getClient().sendMessage(jsonObject));
    }


}

