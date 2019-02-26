package Database;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

/**
 * Created by bxs863 on 26/02/19.
 */
public class DatabaseOperation {
    private static JSONObject setting = null;
    static {
        try {
            setting = new JSONObject(new BufferedReader(new InputStreamReader(new FileInputStream(new File("db_set.json")))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkConnection(){
        String username = setting.getString("username");
        String password = setting.getString("password");
        String url = setting.getString("url");
        return false;
    }

}
