package Server;

import Database.Database;
import org.json.JSONObject;

import javax.xml.crypto.Data;

/**
 * Created by bxs863 on 26/02/19.
 */
public class DataMessageHandler extends MessageHandler {
    public DataMessageHandler(String message) {
        super(message);
    }

    @Override
    String process() {
        JSONObject response = new JSONObject();
        response.put("type","data_response");
        if(jsonObject.get("sub_type").equals("check_user")){
            checkUserExist(response);
        }
        return response.toString();
    }

    private void checkUserExist(JSONObject response) {
        response.put("sub_type", "check_user_response");
        if (jsonObject.has("username")) {
            boolean result = Database.getInstance().checkExist("user_info", "username", jsonObject.getString("username"));
            response.put("success","yes");
            response.put("reply",result);
        }
        else{
            response.put("success","no");
            response.put("reply","You haven't provide the username");
        }
    }

}
