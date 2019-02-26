package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by bxs863 on 26/02/19.
 */
public class SocketHandler extends Thread {

    private Socket socket;

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
                if(line==null) break;
                String responce = MessageHandler.getMessageHandler(line).process();
                out.println(responce);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
