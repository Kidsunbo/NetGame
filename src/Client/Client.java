package Client;

import javafx.application.Platform;
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

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    public Client(String ip, int port) throws IOException {
            socket=new Socket(ip,port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
    }

    public boolean checkConnect() throws IOException {
        if ( socket.getInputStream().read() == -1) {
            socket.close();
            return false;
        }
        System.out.println(socket.getInputStream().read());
        return true;
    }


//    public Future<JSONObject> send(JSONObject message){
//        out.println(message.toString());
//        return Executors.newSingleThreadExecutor().submit(()->{
//            JSONObject result = null;
//            try {
//                String line = in.readLine();
//                System.out.println(line);
//                line = line.substring(line.indexOf("{"));
//                System.out.println(line);
//                result = new JSONObject(line);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return result;
//        });
//    }

    public JSONObject send(JSONObject message) {
        System.out.println("#1");
        out.println(message.toString());
        System.out.println("#2");
        JSONObject result = null;
        try {
            System.out.println("#3");
            String line = in.readLine();
            System.out.println(line);
            line = line.substring(line.indexOf("{"));
            System.out.println(line);
            result = new JSONObject(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

//    public Future<JSONObject> login(String username,String password){
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("type","login");
//        jsonObject.put("username",username);
//        jsonObject.put("password",password);
//        return send(jsonObject);
//    }
//
//    public Future<JSONObject> logout(String username){
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("type","logout");
//        jsonObject.put("username",username);
//        return send(jsonObject);
//    }
//
//    public Future<JSONObject> signup(String username, String password){
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("type","signup");
//        jsonObject.put("username",username);
//        jsonObject.put("password",password);
//        return send(jsonObject);
//    }

    public JSONObject login(String username,String password){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","login");
        jsonObject.put("username",username);
        jsonObject.put("password",password);
        return send(jsonObject);
    }

    public JSONObject logout(String username){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","logout");
        jsonObject.put("username",username);
        return send(jsonObject);
    }

    public JSONObject signup(String username, String password){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","signup");
        jsonObject.put("username",username);
        jsonObject.put("password",password);
        return send(jsonObject);
    }




}
