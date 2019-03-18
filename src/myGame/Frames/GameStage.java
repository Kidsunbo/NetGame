package myGame.Frames;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.Light;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.LinkedList;

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


        primaryStage.show();
    }


    public static void main(String[] args) { launch(args); }
}
