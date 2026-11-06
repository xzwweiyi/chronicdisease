package com.dchealth.common.utils.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class CalcMD5 {
	private static final Logger L = LoggerFactory.getLogger(CalcMD5.class);
    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    /** 
     * 获取上传文件的md5 
     *  
     * @param file 
     * @return 
     * @throws NoSuchAlgorithmException 
     * @throws IOException 
     */  
    public static String calcMD5(MultipartFile file) {  
  
        try {  
            byte[] uploadBytes = file.getBytes();  
            MessageDigest md5 = MessageDigest.getInstance("MD5");  
            byte[] digest = md5.digest(uploadBytes);  
            String hashString =  new BigInteger(1, digest).toString(16); 
            return hashString;  
        } catch (Exception e) {  
        	 L.error(e.getMessage());
           return "";
        }  
  
    }  
    

    /**
     * 计算文件 MD5
     * @param file
     * @return 返回文件的md5字符串，如果计算过程中任务的状态变为取消或暂停，返回null， 如果有其他异常，返回空字符串
     */
    public  static String calcMD5(File file) {
        try (InputStream stream = Files.newInputStream(file.toPath(), StandardOpenOption.READ)) {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] buf = new byte[8192];
            int len;
            while ((len = stream.read(buf)) > 0) {
                digest.update(buf, 0, len);
            }
            return toHexString(digest.digest());
        } catch (IOException e) {
            L.error(e.getMessage());
            return "";
        } catch (NoSuchAlgorithmException e) {
            L.error(e.getMessage());
            return "";
        }
    }
    
    public static String toHexString(byte[] data) {
          StringBuilder r = new StringBuilder(data.length * 2);
          for (byte b : data) {
              r.append(hexCode[(b >> 4) & 0xF]);
              r.append(hexCode[(b & 0xF)]);
          }
          return r.toString();
      }

}