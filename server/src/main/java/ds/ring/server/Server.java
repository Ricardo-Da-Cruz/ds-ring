package ds.ring.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket server;

    public Server() throws Exception {
        this.server = new ServerSocket(5000);
    }

    private void listen() throws Exception {
        while(true) {
            Socket client = this.server.accept();
            String clientAddress = client.getInetAddress().getHostAddress();
            System.out.printf("\r\nnew connection from %s\n", clientAddress);
            new Thread(new ConnectionThread(clientAddress, client)).start();
        }
    }

    public InetAddress getSocketAddress() {
        return this.server.getInetAddress();
    }

    public int getPort() {
        return this.server.getLocalPort();
    }

    public static void main(String[] args) throws Exception {
        Server app = new Server();
        System.out.printf("\r\nrunning server: host=%s @ port=%d\n",
                app.getSocketAddress().getHostAddress(), app.getPort());
        app.listen();
    }
}
