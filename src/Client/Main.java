package Client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View/Login.fxml"));
        primaryStage.setTitle("Game World");
        Scene login = new Scene(root, 300, 420);
        primaryStage.setResizable(false);
        primaryStage.setScene(login);
        primaryStage.show();
    }




    public static void main(String[] args) {
        launch(args);
    }
}
