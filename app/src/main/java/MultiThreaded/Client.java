package MultiThreaded;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    public Runnable getRunnable(){
        return new Runnable() {
            @Override
            public void run() {
                try{
                    int port = 8010;
                    InetAddress address = InetAddress.getByName("localhost");
                    Socket socket = new Socket(address, port);
                    // SEND to server
                    PrintWriter toSocket = new PrintWriter(socket.getOutputStream(), true);
                    // RECEIVE from server
                    BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    // Client → Server
                    toSocket.println("Hello from the client");

                    // Server → Client
                    String line = fromServer.readLine();
                    System.out.println("Response from server: " + line);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
    }
    public static void main(String[] args) {
        Client client = new Client();
        for(int i=0;i<5;i++){
            try{
                Thread thread = new Thread(client.getRunnable());
                thread.start();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
