package com.dchealth.cro.common.utils.file;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 文件处理工具类
 * 
 * @author genemax
 */
public class FileUtils
{
	public static final String ENCODING = "UTF-8";
    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";
    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    protected static MessageDigest messagedigest = null;
	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsaex) {
			System.err.println(FileUtils.class.getName() + "初始化失败，MessageDigest不支持MD5Util。");
			nsaex.printStackTrace();
		}
	}
	
	  /**
		 * 生成文件的md5校验值
		 * 
		 * @param file
		 * @return
		 * @throws IOException
		 */
		public static String getFileMD5String(InputStream fis) throws IOException {
			byte[] buffer = new byte[1024];
			int numRead = 0;
			while ((numRead = fis.read(buffer)) > 0) {
				messagedigest.update(buffer, 0, numRead);
			}
			fis.close();
			return bufferToHex(messagedigest.digest());
		}

    /**
	 * 生成文件的md5校验值
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileMD5String(File file) throws IOException {
		InputStream fis;
		fis = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		int numRead = 0;
		while ((numRead = fis.read(buffer)) > 0) {
			messagedigest.update(buffer, 0, numRead);
		}
		fis.close();
		return bufferToHex(messagedigest.digest());
	}
	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}
 
	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}
 
	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换, >>>
												// 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
		char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

    /**
     * 输出指定文件的byte数组
     * 
     * @param filePath 文件路径
     * @param os 输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException
    {
        FileInputStream fis = null;
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0)
            {
                os.write(b, 0, length);
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     * 
     * @param filePath 文件
     * @return
     */
    public static boolean deleteFile(String filePath)
    {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists())
        {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 文件名称验证
     * 
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename)
    {
        return filename.matches(FILENAME_PATTERN);
    }
    

	/**
	 * 获取文件扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtend(String filename) {
		return getExtend(filename, "");
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtend(String filename, String defExt) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');

			if ((i > 0) && (i < (filename.length() - 1))) {
				return (filename.substring(i+1)).toLowerCase();
			}
		}
		return defExt.toLowerCase();
	}
	
	/**
	 * 流转二进制
	 * 用于 统一 传到服务器端的均为二进制文件
	 * @param inStream
	 * @return
	 */
	public static byte[] getFileBuffer(InputStream inStream){  
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();  
		try {
			byte[] buff = new byte[1024];  
	        int rc = 0;  
	    	while ((rc = inStream.read(buff)) != -1) {
	            swapStream.write(buff, 0, rc);  
	        }  
		} catch (Exception e) {
			e.printStackTrace();
		}
        return swapStream.toByteArray();  
    }  
	
	public static String readFile(InputStream in) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder("");
		try {
			br = new BufferedReader(new InputStreamReader(in, ENCODING));
			 
			String line = null;
			while ((line = br.readLine()) != null) {
			    sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
}
