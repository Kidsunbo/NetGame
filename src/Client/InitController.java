package Client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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


public class InitController {
    private static Client client = null;
    private ExecutorService es = Executors.newFixedThreadPool(5, r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
    });


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
                    loginBtn.setDisable(false);
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

    }

    @FXML
    void loginAction(ActionEvent e){
        if(usernameInput.getText().isEmpty()){
            InitControllerHelper.empyInput(usernameInput,loginBtn);
        }
        else if(passwordInput.getText().isEmpty()){
            InitControllerHelper.empyInput(passwordInput,loginBtn);
        }
        else{
            try {
                byte[] salt = "This is dummy salt".getBytes();
                KeySpec spec = new PBEKeySpec(passwordInput.getText().toCharArray(), salt, 65536, 128);
                SecretKeyFactory fac = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                byte[] hash = fac.generateSecret(spec).getEncoded();
                Base64.Encoder encoder = Base64.getEncoder();
                String pwd = encoder.encodeToString(hash);
                passwordInput.setText(pwd);
                afterLogin(client.login(usernameInput.getText(),pwd));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e1) {
                e1.printStackTrace();
            }
        }
    }

    void afterLogin(Future<JSONObject> future){
        es.execute(()->{
            try {
                JSONObject jsonObject = future.get();
                if(jsonObject.get("success").equals("no")){
                    
                }
                else{

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





}