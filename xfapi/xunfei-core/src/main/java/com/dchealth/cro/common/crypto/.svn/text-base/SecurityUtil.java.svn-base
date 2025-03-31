package com.dchealth.cro.common.crypto;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码安全帮助类
 *
 * @see
 */
public final class SecurityUtil {

    private static final BigInteger PRIVATE_D = new BigInteger(
            "3206586642942415709865087389521403230384599658161226562177807849299468150139");

    private static final BigInteger N = new BigInteger(
            "7318321375709168120463791861978437703461807315898125152257493378072925281977");

    private SecurityUtil() {

    }
    
    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 解析前台传送的加密字符
     *
     * @param str
     * @return
     * @author lihe 2012-9-26 上午10:51:28
     * @see
     */
    public static String getDecryptLoginPassword(String str) {
        byte ptext[] = HexUtil.toByteArray(str);
        BigInteger encryC = new BigInteger(ptext);

        BigInteger variable = encryC.modPow(PRIVATE_D, N);
        // 计算明文对应的字符串
        byte[] mt = variable.toByteArray();
        StringBuffer buffer = new StringBuffer();
        for (int i = mt.length - 1; i > -1; i--) {
            buffer.append((char) mt[i]);
        }
        return buffer.substring(0, buffer.length() - 10).toString();
    }

    /**
     * 生成密码安全码
     *
     * @return
     * @author lihe 2012-9-27 上午9:40:51
     * @see
     */
    public static String getNewPsw() {
        String s1 = MD5Util.md5Hex(String.valueOf(System.currentTimeMillis()));
        String s2 = UUIDUtil.getUUID();
        return s1 + s2;
    }

    /**
     * 生成加密后的密码
     *
     * @param usercode
     * @param logpwd
     * @param saltVal 
     * @return
     * @author lihe 2012-9-27 上午9:58:10
     * @see
     */
    public static String getStoreLogpwd(String usercode, String logpwd, String saltVal) {
        return MD5Util.md5Hex(usercode + MD5Util.md5Hex(logpwd) + saltVal);
    }

    /**
     * 生成随机的MD5密文
     *
     * @return
     */
    public static String getNewToken() {
        return MD5Util.md5Hex(java.util.UUID.randomUUID().toString());
    }

    /**
     * 生成随机正整数
     *
     * @param maxInt
     * @return
     */
    public static int getRandomInt(int maxInt) {
        Random random = new Random();
        return random.nextInt(maxInt);
    }

    /**
     * 生成签名
     *
     * @param srcStr
     * @return
     */
    public static String getDigest(String srcStr) {
        return MD5Util.md5Hex(MD5Util.md5Hex(srcStr) + (srcStr.hashCode() + srcStr.length()) + "19880322");
    }

    /**
     * 生成指定长度的验证码
     * @param len
     * @return
     */
    public static String getVerifyCode(int len){
        StringBuilder sb = new StringBuilder(len );
        for (int i=0;i<len;i++){
            sb.append(getRandomInt(10));
        }
        return sb.toString();
    }


    /**
     * SHA256散列
     * @param str
     * @return
     */
    public static String getSHA256Str(String str){
    	 MessageDigest messageDigest;
    	 String encdeStr = "";
    	 try {
    	  messageDigest = MessageDigest.getInstance("SHA-256");
    	  byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
    	  encdeStr = Hex.encodeHexString(hash);
    	 } catch (NoSuchAlgorithmException e) {
    	  e.printStackTrace();
    	 } catch (UnsupportedEncodingException e) {
    	  e.printStackTrace();
    	 }
    	 return encdeStr;
    	}

    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
