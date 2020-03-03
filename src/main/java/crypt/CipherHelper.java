package crypt;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CipherHelper {
    private static final String ALGORITHM = "DES";
    private static String secretKey = new SimpleDateFormat("DdDdD").format(Calendar.getInstance().getTime());

    public static String cipher(String data){
        SecretKey key = new SecretKeySpec(secretKey.getBytes(),ALGORITHM);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE,key);
            return toHex(cipher.doFinal(data.getBytes()));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static String decipher(String data){
        SecretKey key = new SecretKeySpec(secretKey.getBytes(),ALGORITHM);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE,key);
            return new String(cipher.doFinal(toByte(data)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    private static byte[] toByte(String hexString) {
        int len = hexString.length()/2;

        byte[] result = new byte[len];

        try {
            for (int i = 0; i < len; i++)
                result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
        } catch (NumberFormatException ignored){

        }
        return result;
    }

    public static String toHex(byte[] stringBytes) {
        StringBuffer result = new StringBuffer(2*stringBytes.length);

        try {
            for (int i = 0; i < stringBytes.length; i++) {
                result.append(HEX.charAt((stringBytes[i]>>4)&0x0f)).append(HEX.charAt(stringBytes[i]&0x0f));
            }
        } catch (NumberFormatException ignored){

        }
        return result.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

}