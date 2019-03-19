package myGame;

import org.json.JSONObject;

import java.io.IOException;
import java.net.*;

/**
 * Created by sxy777 on 18/03/19.
 */
public class Client {

    class Message{
        public long time=0;
        public String message = "{}";

        public void setNewMessage(long time,String message){
            if(time>this.time){
                this.time = time;
                this.message = message;
            }
        }
    }


    DatagramSocket socket;
    private Message newMessage = new Message();
    private InetAddress serverIP;
    private int serverPort;
    private static Client client = null;

    public static Client getClient(){
        if(client==null){
            client = new Client("localhost",4399);
        }
        return client;
    }

    private Client(String ip,int serverPort) {
        try {
            socket = new DatagramSocket();
            serverIP = InetAddress.getByName(ip);
            this.serverPort = serverPort;

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        Thread t = new Thread(() -> {
            while(true){
                String msg = receive();
                JSONObject jsonObject = new JSONObject(msg);
                long timeStamp = Long.valueOf(jsonObject.getString("time"));
                newMessage.setNewMessage(timeStamp,msg);
            }
        });
        t.setDaemon(true);
        t.start();

    }

    public String receive(){
        byte[] buf = new byte[5*1024];
        DatagramPacket packet = new DatagramPacket(buf,buf.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(buf);
    }

    public void sendMessage(String msg){
        Thread t = new Thread(() -> {
            DatagramPacket packet = new DatagramPacket(msg.getBytes(),msg.getBytes().length,serverIP, serverPort);
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.setDaemon(true);
        t.start();
    }


}
