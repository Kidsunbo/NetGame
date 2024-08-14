package org.xsource.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/**
 * Created by bxs863 on 02/03/19.
 */
public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        var path = getClass().getResource("/view/Login.fxml");
        Parent root = FXMLLoader.load(path);
        primaryStage.setTitle("Game World");
        Scene login = LoginController.addDragFunction(new Scene(root, 800, 450));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setResizable(false);
        primaryStage.setScene(login);
        primaryStage.show();
    }




    public static void main(String[] args) {
        launch(args);
    }
}
