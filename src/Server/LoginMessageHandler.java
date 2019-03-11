package Server;

import Database.Database;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;

/**
 * Created by bxs863 on 26/02/19.
 * process->check->checkIfLoggedIn->tryToLogin
 *
 */
public class LoginMessageHandler extends MessageHandler {
    Socket socket = null;

    public LoginMessageHandler(String message,Socket socket) {
        super(message);
        this.socket = socket;
    }

    @Override
    String process() {
        JSONObject response = new JSONObject();
        try{
            response.put("type","login_response");
            if(check(jsonObject,response)){
                Server.getInstance().getClients().put(this.jsonObject.getString("username"),socket);
            }
            System.out.println(response);
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
        if(Database.getInstance().userMatch("user_info",jsonObject.getString("username"),jsonObject.getString("password"))){
            response.put("success","yes");
            response.put("reply","Log in successfully");
            return true;
        }
        else{
            response.put("success","no");
            response.put("reply","You username or password is in correct");
        }
        return false;
    }

}
