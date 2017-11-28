package com.bilibili.aesencipher;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by zhangyakun on 17/11/27.
 *
 * @since Version
 */

public class AESUtil {
    private static final String IV_STRING = "A-16-Byte-String";
    private static final String charset = "UTF-8";

    /**
     * 加密
     *
     * @param key
     * @param content
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws UnsupportedEncodingException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String encryptAES(String key, String content)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, UnsupportedEncodingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        // 注意，为了能与 iOS 统一
        // 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
        byte[] keyBytes = key.getBytes(charset);
        byte[] byteContent = content.getBytes(charset);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        byte[] initParam = IV_STRING.getBytes(charset);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
        // 指定加密的算法、工作模式和填充方式
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encryptedBytes = cipher.doFinal(byteContent);
        return toHex(encryptedBytes);
    }

    /**
     * 解密
     *
     * @param key
     * @param content
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws UnsupportedEncodingException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String decryptAES(String key, String content)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, UnsupportedEncodingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] keyBytes = key.getBytes(charset);
        byte[] encryptedBytes = toBinary(content);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        byte[] initParam = IV_STRING.getBytes(charset);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //mode Cipher.DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes,charset);
    }

    /**
     * 将二进制转换为十六进制字符
     *
     * @param buf
     * @return
     */
    private static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            result.append(HEX.charAt((buf[i] >> 4) & 0x0f)).append(HEX.charAt(buf[i] & 0x0f));
        }
        return result.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

    /**
     * 将十六进制转换为字节数组
     *
     * @param hexString
     * @return
     */
    private static byte[] toBinary(String hexString) {
        //hexString的长度对2取整，作为bytes的长度
        int len = hexString.length() / 2;
        byte[] bytes = new byte[len];
        byte high = 0;//字节高四位
        byte low = 0;//字节低四位

        for (int i = 0; i < len; i++) {
            //右移四位得到高位
            high = (byte) ((HEX.indexOf(hexString.charAt(2 * i))) << 4);
            low = (byte) HEX.indexOf(hexString.charAt(2 * i + 1));
            bytes[i] = (byte) (high | low);//高地位做或运算
        }
        return bytes;
    }
}
