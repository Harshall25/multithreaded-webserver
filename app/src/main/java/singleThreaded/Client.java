package singleThreaded;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    public void run() throws IOException {

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
        socket.close();
        fromServer.close();
        toSocket.close();
    }

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}