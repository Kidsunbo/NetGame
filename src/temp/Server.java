package temp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
    private int port;
    private HashMap<String,Socket> clients = new HashMap<>();

    public Server(int port){
        this.port = port;
    }

    public void Start(){
        try {
            ServerSocket ss = new ServerSocket(port);
            while(true){
                new SocketHandler(ss.accept()).start();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server(4333).Start();
    }

    static class SocketHandler extends Thread{
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
                    System.out.println(line);
                    out.println(line.toUpperCase());
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }


    }
}


