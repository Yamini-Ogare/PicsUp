package yamini.picsup.Other;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;



public class FileEncrpt {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    public static byte[] encrypt(byte[] key, byte[] input)
            throws CryptoException {
        return doCrypto(Cipher.ENCRYPT_MODE, key, input);
    }

    public static byte[] decrypt(byte[] key, byte[] input)
            throws CryptoException {
        return doCrypto(Cipher.DECRYPT_MODE, key, input);
    }

    private static byte[] doCrypto(int cipherMode, byte[] key, byte[] input) throws CryptoException {
        try {
            Key secretKey = new SecretKeySpec(key, ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);



            byte[] outputBytes = cipher.doFinal(input);

            return outputBytes;

        } catch (Exception e) {
            throw new CryptoException("Error encrypting/decrypting file",e);
        }
    }
}
