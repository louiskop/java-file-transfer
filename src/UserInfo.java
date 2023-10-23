import java.io.ObjectOutputStream;

/**
 * A class that represents user information including an IP address and an
 * ObjectOutputStream for communication.
 */
public class UserInfo {

    String ip;
    ObjectOutputStream out;

    /**
     * Constructs a new instance of UserInfo with the specified IP address and
     * ObjectOutputStream.
     *
     * @param ip  the IP address of the user
     * @param out the ObjectOutputStream for communication with the user
     */
    public UserInfo(String ip, ObjectOutputStream out) {
        this.ip = ip;
        this.out = out;
    }
}
