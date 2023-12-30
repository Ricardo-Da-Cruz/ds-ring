package ds.ring.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionThread implements Runnable {
    String clientAddress;
    Socket clientSocket;

    public ConnectionThread(String clientAddress, Socket clientSocket) {
        this.clientAddress = clientAddress;
        this.clientSocket  = clientSocket;
    }

    @Override
    public void run() {
        /*
         * prepare socket I/O channels
         */
        try {
            System.out.println("new connection from " + clientAddress);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            while(true) {
                /*
                 * receive command
                 */
                System.out.println("reading command");
                String command = in.readLine();
                if( command == null)
                    break;
                else
                    System.out.printf("message from %s : %s\n", clientAddress, command);		      	                    /*
                     * process command
                     */
                Scanner sc = new Scanner(command).useDelimiter(":");
                String  op = sc.next();
                double  x  = Double.parseDouble(sc.next());
                double  y  = Double.parseDouble(sc.next());
                double  result = 0.0;
                switch(op) {
                    case "add": result = x + y; break;
                    case "sub": result = x - y; break;
                    case "mul": result = x * y; break;
                    case "div": result = x / y; break;
                }
                /*
                 * send result
                 */
                out.println(String.valueOf(result));
                out.flush();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}
