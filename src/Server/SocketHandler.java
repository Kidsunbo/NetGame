package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bxs863 on 26/02/19.
 */
public class SocketHandler extends Thread {

    private Socket socket;
    private Timer timer;
    public SocketHandler(Socket socket){

        this.socket = socket;
    }

    @Override
    public void run(){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            while (true) {
                String line = in.readLine();
                if(line==null || line.equals("")){ //Handle the situation when client quits
                    closeSocket();
                    break;
                }
                String responce = MessageHandler.getMessageHandler(line,socket).process();
                out.println("THIS IS FOR ONLINE TEST   |-|-|"+responce);
                //out.println("Response From Server:"+line.toUpperCase());
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void closeSocket() throws IOException {
        socket.close();
        for(Map.Entry<String,Socket> e:Server.getInstance().getClients().entrySet()){
            if(e.getValue().isClosed())
                Server.getInstance().getClients().remove(e.getKey());
        }
    }

}
