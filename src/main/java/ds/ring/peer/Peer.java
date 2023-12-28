package ds.ring.peer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
public class Peer{

    public static void main(String[] args) throws IOException {
        BlockingQueue<String> messageQueue = new LinkedBlockingQueue<String>();

        InetAddress nextPeer = InetAddress.getByName(args[0]);
        InetAddress server = InetAddress.getByName(args[1]);

        new Thread(new Mutex(server,nextPeer, messageQueue)).start();
        new Thread(new MessageGenerator(messageQueue)).start();

    }

}
