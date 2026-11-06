package com.dchealth.common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class AESUtils {
    //可配置到Constant中，并读取配置文件注入,16位,自己定义
    private static final String KEY = "1ds487465482649d";
    //参数分别代表 算法名称/加密模式/数据填充方式
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    private static final String CHARACTER = "utf-8";
    private static final String AES = "AES";

    private static final int KEY_LEN = 16;

    public static final String BASE_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~!@#$%^&*()_+{}:,./?";

    /**
     * 加密
     *
     * @param content    加密的字符串
     * @param encryptKey key值
     * @return
     * @throws Exception
     */
    private static String encrypt(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(AES);
        kgen.init(new SecureRandom());
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), AES));
        byte[] b = cipher.doFinal(content.getBytes(CHARACTER));
        // 采用base64算法进行转码,避免出现中文乱码
        return Base64.encodeBase64String(b);
    }

    /**
     * 解密
     *
     * @param encryptStr 解密的字符串
     * @param decryptKey 解密的key值
     * @return
     * @throws Exception
     */
    private static String decrypt(String encryptStr, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(AES);
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), AES));
        // 采用base64算法进行转码,避免出现中文乱码
        byte[] encryptBytes = Base64.decodeBase64(encryptStr);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    public static String encrypt(String content) throws Exception {
        return encrypt(content, KEY);
    }

    public static String decrypt(String encryptStr) throws Exception {
        return decrypt(encryptStr, KEY);
    }

    private static final String getKey() {
        return randomString(BASE_CHAR, KEY_LEN);
    }

    public static String randomString(String baseString, int length) {
        if (StringUtils.isEmpty(baseString)) {
            return "";
        }
        StringBuilder sb = new StringBuilder(length);
        if (length < 1) {
            length = 16;
        }
        while (length > 0) {
            sb.append(baseString.charAt(randomInt(baseString.length())));
            length--;
        }
        return sb.toString();
    }

    public static int randomInt(int limit) {
        return (int) (Math.random() * limit);
    }
}
