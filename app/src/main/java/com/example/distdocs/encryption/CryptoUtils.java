package com.example.distdocs.encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    public static byte[] encrypt(String key,  InputStream is)
            throws CryptoException {
        return doCrypto(Cipher.ENCRYPT_MODE, key, is);
    }

    public static byte[] decrypt(String key,InputStream is)
            throws CryptoException {
        return doCrypto(Cipher.DECRYPT_MODE, key, is);
    }

    private static byte[] doCrypto(int cipherMode, String key, InputStream is) throws CryptoException {
        byte[] outputBytes = null;
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);

           // FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) is.available()];
            is.read(inputBytes);

            outputBytes = cipher.doFinal(inputBytes);

            //FileOutputStream outputStream = new FileOutputStream(outputFile);
            //os.write(outputBytes);

            is.close();
            //os.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
        return outputBytes;
    }
}
