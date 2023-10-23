import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 * A class that manages a client connection on a separate thread.
 */
public class ClientManager implements Runnable {

    // packet types
    static final int CONNECT = 0;
    static final int SEARCH = 1;
    static final int RESULTS = 2;
    static final int KEY = 3;
    static final int CLIENT = 4;
    static final int INFO = 5;

    // server variables
    HashMap<String, UserInfo> userList;
    Socket s;
    String username;
    ObjectOutputStream out;
    ObjectInputStream in;

    /**
     * Constructs a new ClientManager instance.
     *
     * @param userList the user list mapping usernames to UserInfo objects
     * @param s        the client socket
     * @throws Exception if an error occurs while initializing the input/output
     *                   streams
     */
    public ClientManager(HashMap<String, UserInfo> userList, Socket s) throws Exception {
        this.userList = userList;
        this.s = s;
        this.out = new ObjectOutputStream(s.getOutputStream());
        this.in = new ObjectInputStream(s.getInputStream());
    }

    /**
     * Broadcasts a packet to all connected users except for the specified user.
     *
     * @param packet     the packet to broadcast
     * @param removeUser the username of the user to exclude from broadcasting
     * @throws Exception if an error occurs while sending the packet to the users
     */
    public void broadcast(Packet packet, String removeUser) throws Exception {

        String nicknames[] = userList.keySet().toArray(new String[userList.size()]);

        // send packet to all users except removeuser
        for (int i = 0; i < userList.size(); i++) {
            if (nicknames[i].equals(removeUser)) {
                continue;
            }

            userList.get(nicknames[i]).out.writeObject(packet);
        }

    }

    @Override
    public void run() {
        System.out.println("Handling client ...");

        // listen for incoming packets
        while (s.isConnected()) {

            try {

                // read packet
                Packet packet = (Packet) in.readObject();

                switch (packet.type) {

                    // add user to userlist
                    case CONNECT:

                        username = packet.username;

                        // check if user disconnects
                        if (packet.disconnect) {
                            System.out.println("[clientmanager " + username + "] Disconnected !");
                            userList.remove(username);
                            s.close();
                            return;
                        }

                        // check for duplicates
                        if (userList.containsKey(username)) {
                            System.out.println("[clientmanager] Duplicate username detected");
                            packet = new Packet(INFO, true, "Duplicate username, please try again. ");
                            out.writeObject(packet);
                            break;
                        }

                        System.out.println("[clientmanager " + username + "] Connected !");
                        userList.put(username, new UserInfo(s.getInetAddress().toString(), out));

                        // send back confirmation packet
                        packet = new Packet(INFO, false, "Successful connection!");
                        out.writeObject(packet);

                        break;

                    // send search packets to all clients except requester
                    case SEARCH:
                        System.out.println("Received a search request , searching ...");
                        broadcast(packet, username);
                        break;

                    // receive results from client and send to requester
                    case RESULTS:
                        System.out.println("Received some search results, sending to requester ...");
                        userList.get(packet.username).out.writeObject(packet);
                        break;

                    // pass on key or initiate p2p connection
                    case KEY:

                        if (packet.isRequest) {

                            // pass on key
                            System.out.println("[clientmanager] Passing on key packet to requestee");
                            userList.get(packet.requestee).out.writeObject(packet);

                        } else if (!packet.confirmed) {

                            // tell requester request is denied
                            userList.get(packet.username).out.writeObject(packet);

                        } else {

                            // confirmed, initiate p2p connection
                            String sender = packet.requestee;
                            String receiver = packet.username;

                            // notify sender(host)
                            packet = new Packet(CLIENT, userList.get(receiver).ip, packet.encryptedName,
                                    packet.encryptedAESKey, true, Server.p2pPort);
                            userList.get(sender).out.writeObject(packet);

                            // notify receiver
                            packet = new Packet(CLIENT, userList.get(sender).ip, packet.encryptedName,
                                    packet.encryptedAESKey,
                                    false, Server.p2pPort);
                            userList.get(receiver).out.writeObject(packet);

                            // increment p2p port
                            Server.p2pPort++;

                        }

                        break;

                    default:
                        System.out.println("[clientmanager " + username + "] Invalid Packet.type received");
                }

            } catch (Exception e) {
                // e.printStackTrace();
                // System.out.println("[clientmanager " + username + "] Error receiving Packet
                // from Client");
            }

        }

    }

}
