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
    public MyCanvas gameCanvas;
    public InfoPane gameInfo;
    Scene waitingScene, StartGameScene;
    public static boolean isMaster;
    public static Thread t;
    public static AtomicBoolean isReady = new AtomicBoolean(false);
    @Override
    public void start(Stage primaryStage)  {
        /*Canvas canvas = new Canvas(Constants.WIDTH, Constants.HEIGHT);
        GraphicsContext graphic = canvas.getGraphicsContext2D();*/

        this.stage = primaryStage;
        stage.setResizable(false);
        primaryStage.setTitle("Greedy Snake!");
        root = new Group();


        // Game Setup
        gameCanvas = new MyCanvas(root);
        root.getChildren().add(gameCanvas);
        StartGameScene = new Scene(root);
        StartGameScene.setFill(Color.rgb(60,60,60));
        StartGameScene.setOnKeyPressed(event -> gameCanvas.onKeyPressed(event));
        gameCanvas.initial();

        // Waiting Screen Setup
        gameInfo = new InfoPane(stage,StartGameScene);
        gameInfo.setUser1Info("Greg","10", "5");
        gameInfo.setUser2Info("ShengDong","10","5");
        gameInfo.initialize();
        waitingScene = new Scene(gameInfo.gameInfo);
        primaryStage.setScene(waitingScene);

        t = new Thread(()->{
            while(true){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("isReady",isReady.get());
                myGame.Client.getClient().sendMessage(jsonObject.toString());
                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
        primaryStage.show();
    }


    public static void main(String[] args) {
//        myGame.Client.getClient().setGameID(args[0]);
//        myGame.Client.getClient().setUsername(args[1]);
//        isMaster = args[1].equals(args[2]);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","register");
        myGame.Client.getClient().sendMessage(jsonObject.toString());
        launch(args);
    }
}
