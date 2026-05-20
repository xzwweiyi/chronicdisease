package com.dchealth.cro.common.utils;

import com.dchealth.cro.common.config.KeyConfig;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class RsaUtil {
    @Autowired
    private KeyConfig keyConfig;
    // 解密密码字段
    public static String decryptValue(String encryptedValues) {
        try {
            // 获取私钥字符串，这里假设私钥存在 privateKeyStr 变量中
            String privateKeyStr = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCmhPESuHMHp2/GJ/HeAaZTboXcZp2/HnKL1TGe2QSci+n19pB9alVNFV80byS2zAlLtLNet4It5vqpQJuCQbJ8EeAFdUPZluzxLSrPajVcIf3GknpLWpfHHVe0h2i/zuChRibnvlp8UuSIqndbtIjRlogtwQ93jRSeokLuzR5HXxptxwzsRGjqxLzRlqBwovitM1e97noHydAgP5fBpEDF5DLlUwH68/mjY4+R/bZJDYyQDUeZDwI14ZIccSCdfTREwKP9e9FEZXjKUzpmDC1Uh2rYJ0g6m8tgVuL3Rd79QskwzSCVERt7C9KFt/tkUx/ICb9htObh0Dor3MZXXBXRAgMBAAECggEBAJREtyrGFZZxHfNAyfm1WlXbiVFwBXGROMkNcXtRHOwPIKkjfBBPx9g0+WqEtBoXnwdLxJLHryAUFIjzwAPBX51dhS0W9u0dl6g/YWkB1rVMhtxyvHWwGojutZNHJpvzJswy3J1oPsWIQXj24ARFwbVs5aBn0M/QTNXbAOIEvbspnRrkYm6bzRX5R0VMwUrdBsFPQzuXLjuzwj2GLj25U3MI7fSkTbBAtXBGpkdcZUSd1zH2g/BwUK15w9/cclaubfYbvio0NHfc+omdsBgD7qeDrUZI6j77CAK7oxzUcZwDq8eWsg6XS3YXgHNpR6KMW3ua+W/2u4HGUyNcdmhdrIECgYEA8OvKuuUPOUsgFTdombuK1qWvOLs3DDEK2yr4H6nxkZF65f7I6pfqJXBtJijQez1IoTe6wxR9EICFXEj15S+wrZ5nxfaBhhlv0G1Ynq8TF/BskJP5yqRpzWSw9JhkB/zW7jEdQExQMQUG1o94ZWmsm5OuJF/QR2RGyc6ULjgojWkCgYEAsPEINBP5E5LyEAK/pXCsRoBlWSTESKaPGEJbctibFftwmtVZlkiFglDCP2jU35S8XY62vm5aZk64N7bS77922n36pogfAFD0HtYcvZM1vEsLd9/dA0bxTjs58yRDyo7ctT9SsEsZXd5bJtGt7Gvw2ZN3Y2b4HMpGevJ/HfmT8CkCgYAJ8pJyWRbh1LpEZf9Jqba32it7+i7xr+y00/dex1OUIR+j83s0RIWEejnng3PoQsWG8pghie0IC2Z2QwT0K53t7UZ3pBABC3fUcU9hbfUp3eF9rr53oYZE1s1/q/MlaUkumJSOnRgKjhyUK2RbO5vG6LgeBF7DM97AYmk351W1UQKBgQCSBRfEAoeWw7nHxLzrhuRk+iipPFJPskR8vKZDTMYPigUPgeoAv7COLp9fPOAP8urQOqpM1Tz9GRGlyNaiy9sFzNlA/Sy4UhZkLEZjnEzIKxY3wyLdRXp2GSJlqRPY4bT6Tluq6Q7yDEIk8v0ZEqscoznvalZvS+6GIAEPd5vCoQKBgA1zict5X+d6IYvHPAJJaCn9fOTOLBicP1Nqm0sAUWm9rCLxcrzrzsxd0AVN/cDoZ++KYFGesrm39nsuHnIA5w98XgDETjog9id1K6swhO45EDosJ1vMe3i3T3QIv7WSnhPezamCpNPxp7rDr5KeP1jEONmTtf4lz9N3h3fGtGEx";
            // 解码私钥字符串为字节数组
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr);
            // 构造 PKCS8EncodedKeySpec 对象
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            // 获取 RSA 密钥工厂实例
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            // 生成私钥对象
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            // 创建解密器
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            // 解密
            byte[] decryptedPasswordBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedValues));
            // 转换为字符串
            String decryptedPassword = new String(decryptedPasswordBytes, StandardCharsets.UTF_8);
            return decryptedPassword;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public String encryptValue(String plainText) {
        try {
            // 1. 获取公钥字符串（请确保 keyConfig 已配置 getPublicKey()）
            String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApoTxErhzB6dvxifx3gGmU26F3Gadvx5yi9UxntkEnIvp9faQfWpVTRVfNG8ktswJS7SzXreCLeb6qUCbgkGyfBHgBXVD2Zbs8S0qz2o1XCH9xpJ6S1qXxx1XtIdov87goUYm575afFLkiKp3W7SI0ZaILcEPd40UnqJC7s0eR18abccM7ERo6sS80ZagcKL4rTNXve56B8nQID+XwaRAxeQy5VMB+vP5o2OPkf22SQ2MkA1HmQ8CNeGSHHEgnX00RMCj/XvRRGV4ylM6ZgwtVIdq2CdIOpvLYFbi90Xe/ULJMM0glREbewvShbf7ZFMfyAm/YbTm4dA6K9zGV1wV0QIDAQAB";
            if (publicKeyStr == null || publicKeyStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Public key is not configured");
            }

            // 2. Base64 解码公钥字符串为字节数组
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);

            // 3. 构造 X509EncodedKeySpec（注意：公钥用 X.509 格式，私钥才用 PKCS#8）
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);

            // 4. 获取 RSA 密钥工厂并生成公钥对象
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // 5. 创建并初始化 Cipher（加密模式）
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            // 6. 加密：字符串 → UTF-8 字节数组 → 加密 → Base64 编码为字符串
            byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedBytes = cipher.doFinal(plainBytes);
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt value with RSA", e);
        }
    }

}
