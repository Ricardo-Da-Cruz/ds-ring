package ds.ring.peer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
public class Peer{

    public static void main(String[] args) throws IOException {


        InetAddress nextPeer = InetAddress.getByName(args[0]);
        InetAddress server = InetAddress.getByName(args[1]);

        System.out.println(Boolean.parseBoolean(args[2]));

        BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
        new Thread(new MessageGenerator(messageQueue)).start();
        new Thread(new Mutex(server,nextPeer, messageQueue,Boolean.parseBoolean(args[2]))).start();

    }

}
