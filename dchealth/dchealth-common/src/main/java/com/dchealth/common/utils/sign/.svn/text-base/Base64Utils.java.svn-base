package com.dchealth.common.utils.sign;

import org.apache.commons.codec.binary.Base64;

/**
 * Base64工具类
 * 
 * @author ruoyi
 */
public final class Base64Utils
{
	
    /**
     * Encodes hex octects into Base64
     *
     * @param binaryData Array containing binaryData
     * @return Encoded Base64 array
     */
    public static String encode(byte[] binaryData)
    {
    	
    	
        if (binaryData == null)
        {
            return null;
        }
        
        return Base64.encodeBase64String(binaryData);

       
    }

    /**
     * Decodes Base64 data into octects
     *
     * @param encoded string containing Base64 data
     * @return Array containind decoded data.
     */
    public static byte[] decode(String encoded)
    {
        if (encoded == null)
        {
            return null;
        }

        return Base64.decodeBase64(encoded);
    }

}
