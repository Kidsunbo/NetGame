package Client;

import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Client {

    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    public Client(String ip, int port){
        try {
            socket=new Socket(ip,port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Future<JSONObject> send(JSONObject message){
        out.println(message.toString());
        return Executors.newSingleThreadExecutor().submit(()->{
            JSONObject result = null;
            try {
                String line = in.readLine();
                result = new JSONObject(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        });
    }

    public Future<JSONObject> login(String username,String password){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","login");
        jsonObject.put("username",username);
        jsonObject.put("password",password);
        return send(jsonObject);
    }

    public Future<JSONObject> logout(String username){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","logout");
        jsonObject.put("username",username);
        return send(jsonObject);
    }

    public Future<JSONObject> signup(String username, String password){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","signup");
        jsonObject.put("username",username);
        jsonObject.put("password",password);
        return send(jsonObject);
    }




}
