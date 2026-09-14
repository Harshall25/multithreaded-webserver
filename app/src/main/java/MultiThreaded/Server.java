package MultiThreaded;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class Server {

    public Consumer<Socket> getConsumer(){
        return (clientSocket) -> {
            try{
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true);
                String msg = fromClient.readLine();
                System.out.println("Message from client: "+msg);
                toClient.println("Hello from server");
                clientSocket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        };
    }

    public static void main(String[] args) throws Exception {
        int port=  8010;
        Server server = new Server();
        try{
            ServerSocket socket = new ServerSocket(port);
            socket.setSoTimeout(20000);
            System.out.println("Serever is Listening on port :"+ port);
            while(true){
                Socket acceptSocket = socket.accept();
                /**for virtual thread in java 21
                 * Thread.startVirtualThread(new Thread(()->server.getConsumer().accept(acceptSocket)));
                 * no need to thread.start();
                 */
                Thread.startVirtualThread(() -> server.getConsumer().accept(acceptSocket));
            }
        }catch (Exception e) {
            throw new Exception(e);
        }
    }
}
