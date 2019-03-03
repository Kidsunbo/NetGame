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
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("View/InitWindow.fxml"));
        primaryStage.setTitle("Game World");
        Scene login = new Scene(root, 300, 275);
        login.getStylesheets().add(getClass().getResource("View/style.css").toExternalForm());
        primaryStage.setResizable(false);
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(login);
        primaryStage.show();
    }




    public static void main(String[] args) {
        launch(args);
    }
}
