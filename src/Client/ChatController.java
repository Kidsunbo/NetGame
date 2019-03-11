package Client;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


/**
 * Created by bxs863 on 06/03/19.
 */
public class ChatController {


    //INNER CLASS
    class MyListViewCell extends ListCell<Profile> {
        @Override
        public void updateItem(Profile item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setGraphic(item.getItem());
            } else {
                setGraphic(null);
            }
        }
    }

    class Profile{
        private final String username;
        private ImageView icon;
        private HBox item = new HBox(5);
        private ListView<String> listView = new ListView<>();

        public Profile(String username){
            this.username = username;
            Random random = new Random();
            this.icon = new ImageView();
            icon.setImage(new Image(String.format("Client/View/icon/%d.png",random.nextInt(9)+1),30,30,false,false));
            item.getChildren().add(icon);
            VBox vBox = new VBox();
            vBox.setSpacing(0);
            Label label = new Label(username);
            label.setStyle("-fx-font-weight: bold");
            vBox.getChildren().add(label);
            item.getChildren().add(vBox);
        }

        public HBox getItem() {
            return item;
        }

        public String getUsername() {
            return username;
        }

        public ListView<String> getListView() {
            return listView;
        }
    }
    //---------------------------------------------------


    public static String username;
    private static Client client = LoginController.getClient();


    private ExecutorService es = Executors.newFixedThreadPool(10, r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
    });


    @FXML
    private ListView<Profile> contactList;
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
        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                jsonObject.put("type", "forward");
                jsonObject.put("subtype", "contact");
                LoginController.getClient().sendMessage(jsonObject);
            }
        },0,5000);


        contactList.setCellFactory(list -> new MyListViewCell());
        contactList.getSelectionModel().selectedItemProperty().addListener(
                (ov, old_val, new_val) -> {
                    if (old_val != null) {
                        old_val.getListView().setItems(chatArea.getItems());
                        chatArea.setItems(new_val.getListView().getItems());
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
        usernameLabel.setText(username);
    }

    private void processNewMessage(JSONObject jsonObject){
        String fromUser = jsonObject.getString("from_user");
        String message = jsonObject.getString("message");
        for(Profile p : contactList.getItems()){
            if(p.getUsername().equals(fromUser)){
                p.getListView().getItems().add(message);
                if(p.getUsername().equals(contactList.getSelectionModel().getSelectedItem().getUsername())){
                    contactList.getItems().remove(p);
                    contactList.getItems().add(0, p);
                    contactList.getSelectionModel().selectFirst();
                }
                else {
                    contactList.getItems().remove(p);
                    contactList.getItems().add(0, p);
                }
                break;
            }
        }
    }

    private void processResponse(JSONObject jsonObject){
        if(jsonObject.getString("subtype").equals("contact_response")){
            boolean isEmpty = false;
            if(contactList.getItems().isEmpty()){
                isEmpty = true;
            }
            List<String> nameList = jsonObject.getJSONArray("contact_names").toList().stream().map(Object::toString).collect(Collectors.toList());
            Profile[] offlineList = filterOffline(nameList);
            nameList = filterExist(nameList);
            String[] name = new String[nameList.size()];
//            String[] name = {"tom","mary","lucy","jenny"};
            name = nameList.toArray(name);
            contactList.getItems().addAll(stringsToProfiles(name));
            contactList.getItems().removeAll(offlineList);
            if(isEmpty) contactList.getSelectionModel().selectFirst();
            contactList.refresh();
        }
    }



    public void sendByKeyboard(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER){
            sendMessage();
        }
    }

    public void sendMessage(MouseEvent mouseEvent) {
        sendMessage();
    }



    private void sendMessage(){
        String msg = inputArea.getText();
        Profile p = contactList.getSelectionModel().getSelectedItem();
        chatArea.getItems().add(msg);
        inputArea.clear();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","forward");
        jsonObject.put("subtype","forward");
        jsonObject.put("to_user",contactList.getSelectionModel().getSelectedItem().getUsername());
        jsonObject.put("message",msg);
        jsonObject.put("from_user",username);
        es.execute(()->LoginController.getClient().sendMessage(jsonObject));

        contactList.getItems().remove(p);
        contactList.getItems().add(0,p);
        contactList.getSelectionModel().selectFirst();

    }

    private Profile[] stringsToProfiles(String[] args){
        Profile[] profiles = new Profile[args.length];
        for(int i = 0;i<args.length;i++){
            profiles[i]=new Profile(args[i]);
        }
        return profiles;
    }

    private List<String> filterExist(List<String> names){
        List<String> existName = new ArrayList<>();
        for(Profile i : contactList.getItems()){
            existName.add(i.getUsername());
        }
        List<String> result = new ArrayList<>();
        for(String name:names){
            if(existName.contains(name)) continue;
            result.add(name);
        }
        return result;
    }

    private Profile[] filterOffline(List<String> nameList) {
        List<Profile> offline = new ArrayList<Profile>();
        for(Profile p : contactList.getItems()){
            if(!nameList.contains(p.getUsername())){
                offline.add(p);
            }
        }
        return offline.toArray(new Profile[0]);
    }

}

