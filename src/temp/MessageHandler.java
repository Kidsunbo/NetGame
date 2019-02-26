package temp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;


public class MessageHandler {

    public enum MessageType{
        Login,Message,Exit,None
    }

    JSONObject jsonObject;
    MessageType type = MessageType.None;

    public MessageHandler(String message){
        try {
            jsonObject = new JSONObject(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public MessageType getType(){
        try {
            switch (jsonObject.getString("type")) {
                case "login":
                    type = MessageType.Login;
                    break;
                case "message":
                    type = MessageType.Message;
                    break;
                case "exit":
                    type=MessageType.Exit;
                    break;
                default:
                    break;
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return type;
    }

    public JSONObject process(HashMap<String, Socket> clients,Socket socket) {
        MessageType type = getType();
        JSONObject result = null;
        switch (type) {
            case Login:
                result = processLogin(clients,socket);
                break;
            case Message:
                result = processMessage(clients);
                break;
            case Exit:
                result = processExit(clients);
                break;
            case None:
                result = processNone();
                break;
            default:
                result = new JSONObject();
                break;
        }
        return result;
    }

    private JSONObject processNone() {
        JSONObject response = new JSONObject();
        try {
            response.put("type","none_response");
            response.put("content","You have sent wrong type of message");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    private JSONObject processExit(HashMap<String, Socket> clients) {
        JSONObject response = new JSONObject();
        try {
            response.put("type","exit_response");
            response.put("content","You have exited");
            clients.get(jsonObject.getString("username")).close();
            clients.remove(jsonObject.getString("username"));

        } catch (JSONException| IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private JSONObject processMessage(HashMap<String,Socket> clients) {
        JSONObject response = new JSONObject();
        try{
            response.put("type","message_response");
            String toUser = jsonObject.getString("to_user");
            if(toUser.equals("")){
                response.put("content","temp.Server has received your message");
            }
            else if(clients.containsKey(toUser)){
                String message = jsonObject.getString("content");
                Socket socket = clients.get(toUser);
                PrintWriter print = new PrintWriter(socket.getOutputStream(),true);
                JSONObject forward = new JSONObject();
                forward.put("type","message");
                forward.put("from_user",jsonObject.getString("username"));
                forward.put("content",jsonObject.getString("content"));
                print.println(forward);
                response.put("content","Message has been sent");
            }
            else{
                response.put("content","The other user is not online");
            }
        }
        catch (JSONException | IOException e){
            e.printStackTrace();
        }
        return response;
    }

    private JSONObject processLogin(HashMap<String, Socket> clients,Socket socket) {
        JSONObject response = new JSONObject();
        try {
            response.put("type","login_response");
            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");
            boolean rightPassword = testPassword(username,password);
            if(rightPassword) {
                response.put("content", "You have logged in");
                clients.put(username,socket);
            }
            else{
                response.put("content","The password is wrong");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    private boolean testPassword(String username, String password){
        //To be continued
        return false;
    }


}
