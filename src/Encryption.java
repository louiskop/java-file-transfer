import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * A class that provides encryption and decryption functionality using symmetric
 * and asymmetric encryption algorithms.
 */
public class Encryption {

    Cipher cipher;
    SecretKey aesKey;

    /**
     * Constructs a new instance of Encryption and generates a symmetric AES key.
     *
     * @throws Exception if an error occurs during key generation
     */
    public Encryption() throws Exception {

        // generate symmetric AES key
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        this.aesKey = generator.generateKey();

    }

    /**
     * Encrypts the AES key with the provided public key.
     *
     * @param key the public key used for encryption
     * @return the encrypted AES key
     * @throws Exception if an error occurs during encryption
     */
    public byte[] getAES(PublicKey key) throws Exception {

        // initialize cipher to RSA
        this.cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        // encrypt AES key
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] aesEncrypted = cipher.doFinal(aesKey.getEncoded());

        return aesEncrypted;
    }

    /**
     * Encrypts the provided filename using the AES key.
     *
     * @param filename the filename to encrypt
     * @return the encrypted filename
     * @throws Exception if an error occurs during encryption
     */
    public byte[] encrypt(String filename) throws Exception {

        // store encrypted string in byte array
        byte[] text = filename.getBytes("UTF8");

        // initialize cipher to AES
        cipher = Cipher.getInstance("AES");

        // encrypt text with AES key
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] textEncrypted = cipher.doFinal(text);

        return textEncrypted;
    }

    /**
     * Decrypts the encrypted string and AES key using the provided private key.
     *
     * @param encryptedString the encrypted string to decrypt
     * @param encryptedAES    the encrypted AES key to decrypt
     * @param privateKey      the private key used for decryption
     * @return the decrypted string
     */
    public String decrypt(byte[] encryptedString, byte[] encryptedAES, PrivateKey privateKey) {

        String s = "";

        try {

            // decrypt AES with private key
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.PRIVATE_KEY, privateKey);
            byte[] decryptedAESBytes = cipher.doFinal(encryptedAES);
            SecretKey decryptedAESKey = new SecretKeySpec(decryptedAESBytes, 0, decryptedAESBytes.length, "AES");

            // decrypt string with AES key
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, decryptedAESKey);
            byte[] textDecrypted = cipher.doFinal(encryptedString);

            // convert decrypted byte array to string
            s = new String(textDecrypted);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;
    }
}
