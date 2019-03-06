package Client;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class LoginController {
    private static Client client = null;
    private Stage stage;
    public static Scene loginScene = null;
    private ExecutorService es = Executors.newFixedThreadPool(5, r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
    });

    @FXML
    private Pane root;

    @FXML
    private TextField usernameInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private Button signupBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private Circle connectCircle;

    @FXML
    public void initialize(){
        Timer time = new Timer(true);
        TimerTask ts = new TimerTask() {
            @Override
            public void run() {
                try {
                    if(client ==null)
                        client = new Client("localhost",4399);
                    else if(!client.checkConnect()) {
                        client = null;
                        throw new IOException("connect failed");
                    }
                    loginBtn.setDisable(false);
                    signupBtn.setDisable(false);
                    connectCircle.setFill(Color.GREEN);
                    connectCircle.setStroke(Color.GREEN);
                } catch (IOException e) {
                    loginBtn.setDisable(true);
                    signupBtn.setDisable(true);
                    connectCircle.setFill(Color.RED);
                    connectCircle.setStroke(Color.RED);
                }
            }
        };
        time.scheduleAtFixedRate(ts,0,10);
    }


    @FXML
    void signUpAction(ActionEvent e){
        try {
            stage = (Stage)(root.getScene().getWindow());
            loginScene=root.getScene();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("View/Signup.fxml")),300,450));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    @FXML
    void loginAction(ActionEvent e) {
        if (usernameInput.getText().isEmpty()) {
            LoginControllerHelper.empyInput(usernameInput, loginBtn);
        } else if (passwordInput.getText().isEmpty()) {
            LoginControllerHelper.empyInput(passwordInput, loginBtn);
        } else {
            String pwd = LoginControllerHelper.encrypt(passwordInput.getText());
            passwordInput.setText(pwd);
            afterLogin(client.login(usernameInput.getText(), pwd));
        }
    }

    void afterLogin(Future<JSONObject> future) {
        es.execute(() -> {
            try {
                JSONObject jsonObject = future.get();
                System.out.println(jsonObject.toString());
                if (jsonObject.get("success").equals("no")) {
                    Platform.runLater(() -> {
//                        Alert alert = new Alert(Alert.AlertType.INFORMATION, jsonObject.getString("reply"));
//                        alert.setHeaderText("Login Failed!");
//                        alert.showAndWait();
                        MsgBoxController.display("Login Failed",jsonObject.getString("reply"));
                    });
                } else {
                    Platform.runLater(() -> {
//                        Alert alert = new Alert(Alert.AlertType.INFORMATION, jsonObject.getString("reply"));
//                        alert.setHeaderText("Login Succeed!");
//                        alert.showAndWait();
                        MsgBoxController.display("Login Succeed",jsonObject.getString("reply"));
                        try {
                            stage = (Stage) (root.getScene().getWindow());
                            loginScene = root.getScene();
                            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("View/chatroom.fxml"))));
                        }catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void keyPress(KeyEvent e){
        if(!usernameInput.getText().isEmpty() && !passwordInput.getText().isEmpty()){
            loginBtn.setDisable(false);
        }
    }

    public static Client getClient() {
        return client;
    }
}
