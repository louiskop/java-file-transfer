import java.io.Serializable;
import java.security.PublicKey;

import javax.crypto.SecretKey;

/**
 * Represents a packet used for communication between a client and a server.
 */
public class Packet implements Serializable {

    // packet variables
    int type;
    String username;
    boolean disconnect;
    String searchString;
    String[] files;
    PublicKey publicKey;
    byte[] encryptedAESKey;
    String requestee;
    byte[] encryptedName;
    SecretKey key;
    boolean isRequest;
    boolean confirmed;
    String ip;
    boolean isHost;
    boolean error;
    String message;
    String fileOwner;
    int port;

    /**
     * Creates a Packet for connecting to the server or disconnecting from it.
     *
     * @param type       the type of the packet
     * @param username   the username associated with the packet
     * @param disconnect indicates whether the user wants to disconnect
     */
    public Packet(int type, String username, boolean disconnect) {
        this.type = type;
        this.username = username;
        this.disconnect = disconnect;
    }

    /**
     * Creates a Packet for searching files.
     *
     * @param type         the type of the packet
     * @param username     the username associated with the packet
     * @param searchString the string used for searching files
     */
    public Packet(int type, String username, String searchString) {
        this.type = type;
        this.username = username;
        this.searchString = searchString;
    }

    /**
     * Creates a Packet for sending search results.
     *
     * @param type      the type of the packet
     * @param username  the username associated with the packet
     * @param fileOwner the owner of the files
     * @param files     the array of files matched in the search
     * @param publicKey the public key associated with the files
     */
    public Packet(int type, String username, String fileOwner, String[] files, PublicKey publicKey) {
        this.type = type;
        this.username = username;
        this.fileOwner = fileOwner;
        this.files = files;
        this.publicKey = publicKey;
    }

    /**
     * Creates a Packet for handling key-related operations.
     *
     * @param type            the type of the packet
     * @param username        the username associated with the packet
     * @param requestee       the username of the user requesting the file
     * @param encryptedName   the encrypted filename
     * @param publicKey       the public key of the requester
     * @param encryptedAESKey the encrypted AES key
     * @param isRequest       indicates whether it is a request packet
     * @param confirmed       indicates whether the request is confirmed
     */
    public Packet(int type, String username, String requestee, byte[] encryptedName, PublicKey publicKey,
            byte[] encryptedAESKey,
            boolean isRequest,
            boolean confirmed) {
        this.type = type;
        this.username = username;
        this.requestee = requestee;
        this.encryptedName = encryptedName;
        this.publicKey = publicKey;
        this.encryptedAESKey = encryptedAESKey;
        this.isRequest = isRequest;
        this.confirmed = confirmed;
    }

    /**
     * Creates a Packet for notifying clients to initiate a peer-to-peer connection.
     *
     * @param type            the type of the packet
     * @param ip              the IP address of the host
     * @param encryptedName   the encrypted filename
     * @param encryptedAESKey the encrypted AES key
     * @param isHost          indicates whether the client is the host
     * @param port            the port number for the connection
     */
    public Packet(int type, String ip, byte[] encryptedName, byte[] encryptedAESKey, boolean isHost, int port) {
        this.type = type;
        this.ip = ip;
        this.encryptedName = encryptedName;
        this.encryptedAESKey = encryptedAESKey;
        this.isHost = isHost;
        this.port = port;
    }

    /**
     * Creates a Packet for sending information or error messages to clients.
     *
     * @param type    the type of the packet
     * @param error   indicates whether it is an error message
     * @param message the information or error message
     */
    public Packet(int type, boolean error, String message) {
        this.type = type;
        this.error = error;
        this.message = message;
    }

}
