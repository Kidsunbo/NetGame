package myGame.Frames;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import myGame.Objects.Node;
import myGame.Objects.Snake;
import myGame.Objects.SnakeBody;


/**
 *
 * @author Shengdong Yan
 * @version 2019-3-9
 */
public class MyCanvas extends Canvas {

    private Snake snakeA;
    private Snake snakeB;
    private Node node;
    private GraphicsContext gc;
    private Timeline timeline;
    private KeyFrame keyFrame;
    private SnakeBody snakeBodyA;
    private SnakeBody snakeBodyB;
    private TimeCounter timeCounter;
    private Constants.GameState gameState;
    private Group root;
    private Label label;


    public MyCanvas(Group root){
         this.root = root;
    }


    public void  initial(){
        node = new Node();
        this.setHeight(Constants.HEIGHT);
        this.setWidth(Constants.WIDTH);
        gc = this.getGraphicsContext2D();
        timeCounter = new TimeCounter(Constants.GameDuration);

        snakeA = new Snake(Constants.userAX,Constants.userAY,node);
        snakeB = new Snake(Constants.userBX,Constants.userBY,node);
        snakeBodyA = new SnakeBody(snakeA);
        snakeBodyB = new SnakeBody(snakeB);

        snakeA.setColor(Color.BLUE);
        snakeA.setUserName("DongdongSnake");  // Update the userName here
      //  snakeB.setUserName("ChangeCode!");
        snakeBodyA.initBody();
        snakeA.setSnakeBody(snakeBodyA);

        snakeB.setColor(Color.YELLOW);
        snakeB.setUserName("SunBoSnake");  // Update the userName here
        //  snakeB.setUserName("ChangeCode!");
        snakeBodyB.initBody();
        snakeB.setSnakeBody(snakeBodyB);

        initTimeLine(gc);
//        drawStart();
        setGameState(Constants.GameState.RUN);
        start();
    }


    public void onKeyPressed(KeyEvent event){
        KeyCode key = event.getCode();
        // change the gamestate into Puase when player press SPACE key and back when user press SPACE key again
        if(this.getGameState() == Constants.GameState.RUN){
            if(key==KeyCode.SPACE){
                setGameState(Constants.GameState.PAUSE);
                timeCounter.stopTimer();
            }
            else if(key == KeyCode.ESCAPE){
                    setGameState(Constants.GameState.TIMEOUT);
                    timeCounter.setTime(0);
            }
            else{
                snakeA.onKeyPressed(event);
            }
        }

        else if(this.getGameState() == Constants.GameState.PAUSE) {   // 在暂停状态，再一次空格回到run状态
            if (key == KeyCode.SPACE) {
                setGameState(Constants.GameState.RUN);
                timeCounter.startTimer();
            }
        }
    }

/*

    public void onKeyReleased(KeyEvent event) {
       snakeA.onKeyReleased(event);
    }
   public void onMouseMoved(MouseEvent event) {
        for (SObject wObject : mObjects) {
            wObject.onMouseMoved(event);
        }
    }
*/


    public void initTimeLine( GraphicsContext gc) {

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        keyFrame = new KeyFrame(Duration.millis(Constants.duration), event -> {
                gc.setEffect(null);
                gc.clearRect(0, 0, getWidth(), getHeight());
                drawGridding(gc);

                if (getGameState() == Constants.GameState.RUN){
                    draw(gc);
                    update();
                    drawScoreBoard();
                } else if (getGameState() == Constants.GameState.PAUSE){
                    drawPause(gc);
                } else if (getGameState() == Constants.GameState.TIMEOUT){
                   drawTimeout(gc);
                }
        });
        timeline.getKeyFrames().add(keyFrame);
    }


    /**
     * start the update timeline
     */
    public void start() {
        timeline.play();
        timeCounter.start();
    }

    /**
     * pause the update timeline
     */
    public void pause() {
        timeline.pause();
    }

    /**
     * stop the update timeline
     */
    public void stop() {
        timeline.stop();
    }



    public void drawGridding(GraphicsContext gc){

        for (int i = 0; i < Constants.WIDTH; i += 18) {
            gc.setStroke(Color.web("#111", 0.5));
            gc.strokeLine(i,0, i, Constants.HEIGHT);
        }
        for (int i = 0; i < Constants.HEIGHT; i += 18) {
            gc.setStroke(Color.web("#111", 0.5));
            gc.strokeLine(0, i, Constants.WIDTH, i);

        }
    }

//    public  void drawStart() {
//
//        Label label = new Label("Are You Ready?");
//        label.setLayoutX(Constants.WIDTH / 2 - 300);
//        label.setLayoutY(Constants.HEIGHT / 2 - 250);
//        label.setStyle("-fx-font: 80 arial; -fx-font-weight: bold; -fx-text-fill: Gray;  ");
//        root.getChildren().add(label);
//
//        Button button = new Button();
//        button.setText("Start Now!");
//        button.setLayoutX(Constants.WIDTH / 2 - 75);
//        button.setLayoutY(Constants.HEIGHT / 2 - 50);
//        button.setMinWidth(150);
//        button.setMinHeight(70);
//        button.setStyle("-fx-font: 24 arial; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: #ff4e4e; -fx-background-radius: 20; ");
//        root.getChildren().add(button);
//
//        Label label2 = new Label(" In the Game you can: \n Use SPACE to pause the game \n And SPACE again to back");
//        label2.setLayoutX(Constants.WIDTH / 2 - 150);
//        label2.setLayoutY(Constants.HEIGHT  - 250);
//        label2.setStyle("-fx-font: 20 arial; -fx-font-weight: bold; -fx-text-fill: Gray;  ");
//        root.getChildren().add(label2);
//
//        button.setOnMouseClicked(event ->{
//                setGameState(Constants.GameState.READY);
//                setGameState(Constants.GameState.RUN);
//                root.getChildren().remove(button);
//                root.getChildren().remove(label);
//                root.getChildren().remove(label2);
//                start();
//
//        });
//
//
//
//    }

    public void drawScoreBoard(){
        String InfoA = String.format("%-14s", snakeA.getUserName())+": "+ snakeA.getScore();
        String InfoB = String.format("%-14s", snakeB.getUserName())+": "+ snakeB.getScore();
        // Username 从网络得到，顺便加成固定长度(空格大小和字体大小不一样，所以显示的长度还是不一样)

        label = new Label(InfoA+"\n"+ InfoB);
        label.setLayoutX(Constants.WIDTH - 300);
        label.setLayoutY(Constants.HEIGHT - 800);
        label.setStyle("-fx-font: 30 arial; -fx-font-weight: bold; -fx-text-fill: Gray;  ");
        root.getChildren().add(label);
    }

    public void drawPause(GraphicsContext gc){
        gc.setFont(Font.font("Impact", FontWeight.BOLD, 120));
        gc.setFill(Color.YELLOW);
        gc.fillText("Game Paused", Constants.WIDTH/2 - 320, Constants.HEIGHT/2 -100);
        gc.setFont(Font.font("Impact", FontWeight.BOLD, 60));
        gc.setFill(Color.GREEN);
        gc.fillText("Press Space again to back", Constants.WIDTH / 2 - 300, Constants.HEIGHT / 2 + 20);
        gc.setFont(Font.font("Impact", FontWeight.BOLD, 60));
        gc.setFill(Color.RED);
        gc.fillText("Press Esc to Give up", Constants.WIDTH/2 - 220, Constants.HEIGHT/2 + 120);

    }

    public  void drawTimeout(GraphicsContext gc){
        String winningMessage = "End of Game";
        if (snakeA.getScore() > snakeB.getScore()){
            winningMessage += "\n" + snakeA.getUserName() + " is Winner";
            winningMessage += "\n Score       " + snakeA.getScore();
        } else {
            winningMessage +="\n"+ snakeB.getUserName() + " is Winner";
            winningMessage += "\n Score       " + snakeB.getScore();
        }

        gc.setFont(Font.font("Times", FontWeight.BOLD, 40));
        gc.setFill(Color.YELLOW);
        gc.fillText(winningMessage, Constants.WIDTH / 2 - 300, Constants.HEIGHT / 2);

        Button button = new Button();
        button.setText("Ok,Return");
        button.setLayoutX(Constants.WIDTH / 2 - 75);
        button.setLayoutY(Constants.HEIGHT - 150);
        button.setMinWidth(150);
        button.setMinHeight(70);
        button.setStyle("-fx-font: 24 arial; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: #ff4e4e; -fx-background-radius: 20; ");
        root.getChildren().add(button);

        button.setOnMouseClicked(event -> { });

    }


    /*
    * draw everything into the canvas
    * */
    public void draw(GraphicsContext gc){
        snakeBodyA.draw(gc);
        snakeA.drawSnake(gc);

        snakeBodyB.draw(gc);
        snakeB.drawSnake(gc);

        node.draw(gc);
        timeCounter.draw(gc);

        if(snakeA.isCollisionWithSnake(snakeB)){snakeA.rebirth();snakeBodyA.initBody();}
        if(snakeB.isCollisionWithSnake(snakeA)){snakeB.rebirth();snakeBodyB.initBody();}
    }
  /*update all the objects information
  * */
    public  void  update(){
        if (timeCounter.getTime()<=0) {this.setGameState(Constants.GameState.TIMEOUT); }

        snakeA.update();
        snakeBodyA.update();
        if (snakeA.isGetNode()) {node.update();}
        if (snakeA.isReachBorder()){snakeA.rebirth(); snakeBodyA.initBody();}



//        snakeB.update();
//        snakeBodyB.update();
//        if (snakeB.isGetNode()) {node.update();}
//        if (snakeB.isReachBorder()){snakeB.rebirth(); snakeBodyB.initBody();} // 后期把两条蛇和食物的信息更新放在这
        
//        snakeB.setX();
//        snakeB.setY();
//        snakeB.setDirection();
//        snakeBodyB.setPointlist();
        root.getChildren().remove(label);

    }



    public Constants.GameState getGameState() {
        return gameState;
    }

    public void setGameState(Constants.GameState gameState) {
        this.gameState = gameState;

    }

    public Snake getSnakeA() {
        return snakeA;
    }

    public Snake getSnakeB() {
        return snakeB;
    }

    public Node getNode() {
        return node;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public void setSnakeA(Snake snakeA) {
        this.snakeA = snakeA;
    }

    public void setSnakeB(Snake snakeB) {
        this.snakeB = snakeB;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }
    public SnakeBody getSnakeBody() {
        return snakeBodyA;
    }

    public void setSnakeBody(SnakeBody snakeBody) {
        this.snakeBodyA = snakeBody;
    }
}
