import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * A server class that listens for client connections and manages clients on
 * separate threads.
 */
public class Server {

    // server variables

    // user list <USERNAME, <IP, OUTPUTSTREAM> >
    static HashMap<String, UserInfo> userList = new HashMap<String, UserInfo>();
    static int p2pPort = 2561;

    /**
     * The main method that starts the server and waits for clients to connect.
     *
     * @param args command line arguments (unused)
     * @throws Exception if an error occurs during server initialization or client
     *                   handling
     */
    public static void main(String[] args) throws Exception {

        // create server socket
        ServerSocket ss = new ServerSocket(2560);
        System.out.println("\n\n[server] Server has started ...");

        // wait for clients to connect
        while (!ss.isClosed()) {

            Socket s = ss.accept();

            // handle each client on a new thread
            ClientManager clientManager = new ClientManager(userList, s);
            Thread thread = new Thread(clientManager);
            thread.start();

        }

        ss.close();
    }
}
