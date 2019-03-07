package Client;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Client {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ExecutorService es = Executors.newFixedThreadPool(5,r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
    });


    public Client(String ip, int port) throws IOException {
            socket=new Socket(ip,port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
    }

    public boolean checkConnect() throws IOException {
        if ( socket.getInputStream().read() == -1) {
            return false;
        }
        return true;
    }


    public Future<JSONObject> send(JSONObject message){
        out.println(message.toString());
        return es.submit(()->{
            JSONObject result = null;
            try {
                String line = in.readLine();
                if(line!=null && line.contains("{")) {
                    line = line.substring(line.indexOf("{"));
                    result = new JSONObject(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result==null?new JSONObject("{}"):result;
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


    public String receiveMessage(){
        try {
            String message = in.readLine();
            if(message==null) return "";
            return message;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void sendMessage(String message){
        out.println(message);
    }

    public void sendMessage(JSONObject jsonObject){
        sendMessage(jsonObject.toString());
    }

}
