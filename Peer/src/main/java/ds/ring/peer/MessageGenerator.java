package ds.ring.peer;

import ds.ring.peer.poisson.PoissonProcess;

import java.util.concurrent.BlockingQueue;
import java.lang.Thread;
import java.util.Random;
public class MessageGenerator implements Runnable{

    BlockingQueue<String> messageQueue;

    public MessageGenerator(BlockingQueue<String> messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void run() {
        PoissonProcess pp = new PoissonProcess(4, new Random(0) );
        while (true) {
            double t = pp.timeForNextEvent() * 60.0 * 1000.0;
            System.out.println("next event in -> " + (int)t + " ms");
            try {
                Thread.sleep((int)t);
                int op = (int)(Math.random() * 4);
                switch (op) {
                    case 0:
                        messageQueue.add(String.format("add:%d:%d\n", (int)(Math.random() * 100), (int)(Math.random() * 100)));
                        break;
                    case 1:
                        messageQueue.add(String.format("sub:%d:%d\n", (int)(Math.random() * 100), (int)(Math.random() * 100)));
                        break;
                    case 2:
                        messageQueue.add(String.format("mul:%d:%d\n", (int)(Math.random() * 100), (int)(Math.random() * 100)));
                        break;
                    case 3:
                        messageQueue.add(String.format("div:%d:%d\n", (int)(Math.random() * 100), (int)(Math.random() * 100)));
                        break;
                }
            }
            catch (InterruptedException e) {
                System.out.println("thread interrupted");
                e.printStackTrace(System.out);
            }
        }
    }
}
