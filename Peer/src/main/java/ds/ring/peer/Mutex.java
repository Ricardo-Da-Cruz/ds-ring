package ds.ring.peer;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class Mutex implements Runnable {

    private final ServerSocket listener;
    private final Socket server;
    private final Socket nextClient;
    private final BlockingQueue<String> messages;

    private boolean hasMutex;

    Mutex(InetAddress server,InetAddress nextClient, BlockingQueue<String> messagesQueue,boolean hasMutex) throws IOException {
        this.listener = new ServerSocket(5000);
        System.out.println("connecting to server");
        this.server = connect(server);
        System.out.println("connecting to next client");
        this.nextClient = connect(nextClient);
        messages = messagesQueue;
        this.hasMutex = hasMutex;
    }

    private Socket connect(InetAddress address){
        boolean connected = false;
        Socket socket;
        while (!connected) {
            try {
                System.out.println("Attempting to connect to the server...");
                socket = new Socket(address, 5000);
                connected = true;
                System.out.println("Connected to the server successfully!");
                return socket;
            } catch (UnknownHostException e) {
                System.err.println("Error: Unknown host " + address);
            } catch (IOException e) {
                System.err.println("Error: Failed to connect to the server");
                try {
                    Thread.sleep(3000); // Wait for 3 seconds before retrying
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return null;
    }

    public void run() {
        try {
            Socket listenerSocket = listener.accept();
            System.out.println("received connection from: " + listenerSocket.getInetAddress());
            BufferedReader tokenIn = new BufferedReader(new InputStreamReader(listenerSocket.getInputStream()));
            PrintWriter tokenOut = new PrintWriter(nextClient.getOutputStream(), true);
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
            PrintWriter serverOut = new PrintWriter(server.getOutputStream(), true);
            while (true){
                Thread.sleep(1000);
                if (hasMutex){
                    System.out.println("has mutex");
                    System.out.println("sending " + messages.size() +" messages");
                    while (!messages.isEmpty()){
                        String message = messages.remove();
                        System.out.println("sending message: " + message);
                        serverOut.write(message);
                        serverOut.flush();
                        message = serverIn.readLine();
                        if (message == null){
                            System.out.println("Server disconnected");
                            break;
                        }
                        System.out.println("Server response: " + message);
                    }
                    System.out.println("sending token");
                    tokenOut.write("token\n");
                    tokenOut.flush();
                    hasMutex = false;
                }else{
                    System.out.println("waiting for token");
                    String message = tokenIn.readLine();
                    if (message == null){
                        System.out.println("Server disconnected");
                        break;
                    }else if (message.equals("token")) {
                        System.out.println("received token");
                        hasMutex = true;
                    }else{
                        System.out.println("received token: " + message);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
