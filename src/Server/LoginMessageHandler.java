package Server;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bxs863 on 26/02/19.
 */
public class LoginMessageHandler extends MessageHandler {
    public LoginMessageHandler(String message) {
        super(message);
    }

    @Override
    String process() {
        JSONObject response = new JSONObject();
        try{
            response.put("type","login_response");
            check(jsonObject,response);
        }
        catch (Exception e){

        }

        return response.toString();
    }

    /**
     * Check if the message contains username and password
     * @param jsonObject
     * @param response
     * @return
     */
    private static boolean check(JSONObject jsonObject,JSONObject response){
        if(jsonObject.has("username") && jsonObject.has("password")){
            return checkIfLoggedIn(jsonObject, response);
        }
        else{
            try {
                response.put("success","no");
                response.put("reply","You should provide the username and the password");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Check if the user has logged in.
     * @param jsonObject
     * @param response
     * @return
     */
    private static boolean checkIfLoggedIn(JSONObject jsonObject,JSONObject response){
        try {
            if(!Server.getInstance().getClients().containsKey(jsonObject.getString("username"))){
                return tryToLogin(jsonObject,response);
            }
            else{
                response.put("success","no");
                response.put("reply","You have logged in!");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;

    }


    private static boolean tryToLogin(JSONObject jsonObject, JSONObject response) {
        // Connect with Database to check if logged in

        return true;
    }

}