package ds.ring.peer;

import java.util.concurrent.BlockingQueue;
import ds.poisson.*;

import java.lang.Thread;
import java.util.Random;
public class MessageGenerator implements Runnable{

    static final int SAMPLES = 100;
    BlockingQueue<String> messageQueue;

    public MessageGenerator(BlockingQueue<String> messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void run() {
        PoissonProcess pp = new PoissonProcess(20, new Random(0) );
        for (int i = 1; i <= SAMPLES; i++) {
            double t = pp.timeForNextEvent() * 60.0 * 1000.0;
            System.out.println("next event in -> " + (int)t + " ms");
            try {
                Thread.sleep((int)t);
                messageQueue.add("message " + i);
            }
            catch (InterruptedException e) {
                System.out.println("thread interrupted");
                e.printStackTrace(System.out);
            }
        }
    }
}
