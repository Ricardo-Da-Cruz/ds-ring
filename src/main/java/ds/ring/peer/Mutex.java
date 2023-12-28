package ds.ring.peer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class Mutex implements Runnable {

    private final ServerSocket listener;
    private final Socket server;
    private final Socket nextClient;
    private final BlockingQueue<String> messages;

    Mutex(InetAddress server,InetAddress nextClient, BlockingQueue<String> messagesQueue) throws IOException {
        this.listener = new ServerSocket(5000);
        this.server = new Socket(server,5000);
        this.nextClient = new Socket(nextClient,5000);
        messages = messagesQueue;
    }

    public void run() {
        boolean hasMutex = false;
        ByteBuffer m = ByteBuffer.allocate(1024);
        try {
            listener.accept();
            listener.getChannel().configureBlocking(true);
            SocketChannel a = listener.getChannel().accept();

            while (true){
                if (hasMutex){
                    while (!messages.isEmpty()){
                        server.getChannel().write(m.put(messages.remove().getBytes()));
                        m.clear();
                        server.getChannel().read(m);
                        System.out.println(Arrays.toString(m.array()));
                    }
                    nextClient.getChannel().write(ByteBuffer.wrap("token".getBytes()));
                    hasMutex = false;
                }else {
                    a.read(m);
                    byte [] message = new byte[1024];
                    m.get(message);
                    if (Arrays.equals(message, "token".getBytes())){
                        hasMutex = true;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
