package myGame.Frames;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.Light;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameStage extends Application {
//    private enum GameState{READY, PAUSE, RUN, TIMEOUT};
//    private Timeline timeline;
//    private KeyFrame keyFrame;
//    public static Snake snake;
//    public  GameState gameState;
    public  Stage stage = new Stage();
    public  Group root;
    public  LinkedList<Light.Point> list = new LinkedList<Light.Point>();
    public static MyCanvas gameCanvas;
    public InfoPane gameInfo;
    Scene waitingScene, StartGameScene;
    public static boolean isMaster;
    public static String username;


    @Override
    public void start(Stage primaryStage)  {

        this.stage = primaryStage;
        stage.setResizable(false);
        primaryStage.setTitle("Greedy Snake!");
        root = new Group();


        // Game Setup
        gameCanvas = new MyCanvas(root);
        gameCanvas.setMaster(isMaster);
        gameCanvas.setUsername(username);
        root.getChildren().add(gameCanvas);
        StartGameScene = new Scene(root);
        StartGameScene.setFill(Color.rgb(60,60,60));
        StartGameScene.setOnKeyPressed(event -> gameCanvas.onKeyPressed(event));
        gameCanvas.initial();
        // Waiting Screen Setup
        gameInfo = new InfoPane(stage,StartGameScene,gameCanvas);
        gameInfo.setUser1Info("","0", "0");
        gameInfo.setUser2Info("","0","0");

        gameInfo.setMaster(isMaster);
        gameInfo.initialize();
        waitingScene = new Scene(gameInfo.gameInfo);
        primaryStage.setScene(waitingScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        myGame.Client.getClient().setGameID(args[0]);
        myGame.Client.getClient().setUsername(args[1]);    // 1 is username; 2 is master name
        isMaster = args[1].equals(args[2]);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","register");
        myGame.Client.getClient().sendMessage(jsonObject.toString());
        username = args[1];

        launch(args);
    }
}
