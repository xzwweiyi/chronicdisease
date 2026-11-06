package com.dchealth.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Static functions to simplifiy common {@link MessageDigest}
 * tasks. This class is thread safe.
 * 
 * @author 99bill
 */
public final class MD5Util {


    private static final BigInteger PRIVATE_D = new BigInteger(
            "3206586642942415709865087389521403230384599658161226562177807849299468150139");
    private static final BigInteger N = new BigInteger(
            "7318321375709168120463791861978437703461807315898125152257493378072925281977");
    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
    private MD5Util() {
    }

    /**
     * Returns a MessageDigest for the given <code>algorithm</code>.
     *
     *            The MessageDigest algorithm name.
     * @return An MD5 digest instance.
     * @throws RuntimeException
     *             when a {@link NoSuchAlgorithmException} is
     *             caught
     */

    static MessageDigest getDigest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
	public static String sign(String text, String input_charset) {
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }
	
	/* @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException 
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
    /**
     * Calculates the MD5 digest and returns the value as a 16 element
     * <code>byte[]</code>.
     * 
     * @param data
     *            Data to digest
     * @return MD5 digest
     */
    public static byte[] md5(byte[] data) {
        return getDigest().digest(data);
    }

    /**
     * Calculates the MD5 digest and returns the value as a 16 element
     * <code>byte[]</code>.
     * 
     * @param data
     *            Data to digest
     * @return MD5 digest
     */
    public static byte[] md5(String data, String charset) {
        if (charset == null) {
            return md5(data.getBytes());
        } else {
            try {
                return md5(data.getBytes(charset));
            } catch (UnsupportedEncodingException e) {
            }

            return null;
        }
    }

    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex
     * string.
     * 
     * @param data
     *            Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex(byte[] data) {
        return HexUtil.toHexString(md5(data));
    }

    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex
     * string.
     * 
     * @param data
     *            Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex(String data) {
        return HexUtil.toHexString(md5(data, null));
    }

    /**
     * 指定编码加密
     * 
     * @author lihe 2013-7-4 下午5:27:07
     * @param data
     * @param charset
     * @return
     * @see
     * @since
     */
    public static String md5Hex(String data, String charset) {
        return HexUtil.toHexString(md5(data, charset));
    }

    /**
     * 解密登录密码
     * 
     * @author lihe 2013-7-4 下午5:27:24
     * @param str
     * @return
     * @see
     * @since
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
    
    private static final String SALT = "tambor";

	public static String encode(String password) {
		password = password + SALT;
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		char[] charArray = password.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}

			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}
	
	
	  private static String byteArrayToHexString(byte b[]) {
			StringBuffer resultSb = new StringBuffer();
			for (int i = 0; i < b.length; i++)
				resultSb.append(byteToHexString(b[i]));

			return resultSb.toString();
		}
	  
	  private static String byteToHexString(byte b) {
			int n = b;
			if (n < 0)
				n += 256;
			int d1 = n / 16;
			int d2 = n % 16;
			return hexDigits[d1] + hexDigits[d2];
		}


}
