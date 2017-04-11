/**
 * $LastChangedBy: Andre $
 * $LastChangedDate: 2016-07-07 09:50:33 +0200 (Thu, 07 Jul 2016) $
 * $LastChangedRevision: 994 $
 */

package com.stellr.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stuart Kemp
 */
public class Password {

    private static SecureRandom random;
    private static final String CHARSET = "UTF-8";
    private static final String ENCRYPTION_ALGORITHM = "MD5";
    private static final int SALT_LENGTH = 64;

     public static byte[] getSalt() {
        random = new SecureRandom();
        byte bytes[] = new byte[SALT_LENGTH];
        random.nextBytes(bytes);
        return bytes;
    }
    
     public static String hashToBase16String(String password) {
        String hash = null;
        try {
            byte[] bytesOfMessage = password.getBytes(CHARSET);
            MessageDigest md;
            md = MessageDigest.getInstance(ENCRYPTION_ALGORITHM);
            md.reset();
            md.update(bytesOfMessage,0,bytesOfMessage.length);
            hash = new BigInteger(1, md.digest()).toString(16);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(Password.class.getName()).log(Level.SEVERE, "Encoding Problem", ex);
        }
        return hash;
    }

    public static byte[] hashWithSalt(String password, byte[] salt) {
        byte[] hash = null;
        try {
            byte[] bytesOfMessage = password.getBytes(CHARSET);
            MessageDigest md;
            md = MessageDigest.getInstance(ENCRYPTION_ALGORITHM);
            md.reset();
            md.update(salt);
            md.update(bytesOfMessage);
            hash = md.digest();
                     

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(Password.class.getName()).log(Level.SEVERE, "Encoding Problem", ex);
        }
        return hash;
    }
}
