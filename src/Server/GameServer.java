package Server;

import javafx.collections.FXCollections;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by bxs863 on 16/03/19.
 */
public class GameServer {

    private static GameServer server = new GameServer();
    private Hashtable<String,User> users = new Hashtable<>();
    private Hashtable<String,Hashtable<String,User>> games = new Hashtable<>();
    private Hashtable<String,Integer> gameIDs = new Hashtable<>();

    class User{
        public InetAddress address;
        public int port;

        public User(InetAddress address,int port){
            this.address = address;
            this.port = port;
        }
    }

    private GameServer(){
        


        Thread t = new Thread(()->{
            try {
                DatagramSocket socket = new DatagramSocket(4399);
                AtomicInteger time = new AtomicInteger(600); // This is just a way to avoid final or effective final
                Timer timer = new Timer(true);
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        time.decrementAndGet();
                    }
                };
                timer.scheduleAtFixedRate(task,10000,1000);
                while(true){
                    if(time.get()<0){
                        break;
                    }
                    byte[] buf = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buf,buf.length);
                    socket.receive(packet);
                    addOrUpdate(packet);
                    sendToOthers(packet,socket);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public static GameServer getGameServer(){
        return server;
    }


    private void addOrUpdate(DatagramPacket packet){
        byte[] buf = packet.getData();
        JSONObject jsonObject = new JSONObject(new String(buf));
        String username = jsonObject.getString("username");
        users.put(username,new User(packet.getAddress(),packet.getPort()));
    }

    private void sendToOthers(DatagramPacket packet, DatagramSocket socket) throws IOException {
        byte[] buf = packet.getData();
        JSONObject jsonObject = new JSONObject(new String(buf));
        String username = jsonObject.getString("username");
        for(Map.Entry<String,User> user : users.entrySet()){
            if(user.getKey().equals(username)) continue;
            packet.setAddress(user.getValue().address);
            packet.setPort(user.getValue().port);
            socket.send(packet);
        }
    }

    public String createGameID(){
        int i = 0;
        while(gameIDs.containsKey(String.valueOf(i))){
            i++;
        }
        games.put(String.valueOf(i),new Hashtable<>());
        gameIDs.put(String.valueOf(i),3600);
        return ""+i;
    }

}
