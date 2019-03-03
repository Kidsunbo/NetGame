package Client;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bxs863 on 02/03/19.
 */
public class InitControllerHelper {

    public static void empyInput(TextField textField, Control control){
        Timer time = new Timer();
        String origin = textField.getStyle();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                textField.setStyle(origin);
                control.setDisable(true);
                Platform.runLater(textField::requestFocus);
                time.cancel();
                time.purge();
            }
        },200);
        textField.setStyle("-fx-background-color:#ffcc00");
    }

    public static JSONObject createJsonobject(String... args){
        if(args.length%2==0) throw new IllegalArgumentException("Please pass even number arguments");
        JSONObject jsonObject = new JSONObject();
        for(int i =0;i<args.length-1;i+=2){
            jsonObject.put(args[i],args[i+1]);
        }
        return jsonObject;
    }


}
