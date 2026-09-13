package singleThreaded;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public void run() throws IOException {

        int port = 8010;

        ServerSocket serverSocket = new ServerSocket(port);

        System.out.println("Server is running on port " + port);

        while (true) {

            // Wait for client
            Socket clientSocket = serverSocket.accept();
            System.out.println(
                    "Connection accepted from "
                            + clientSocket.getRemoteSocketAddress()
            );

            // RECEIVE from client
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // SEND to client
            PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true);

            // Read client's message
            String message = fromClient.readLine();
            System.out.println("Client says: " + message);
            // Send response
            toClient.println("Hello from the server");
            clientSocket.close();
            fromClient.close();
            toClient.close();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}