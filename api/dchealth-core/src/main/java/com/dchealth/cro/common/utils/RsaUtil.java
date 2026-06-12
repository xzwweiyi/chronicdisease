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
    public  String decryptValue(String encryptedValues) {
        try {
            // 获取私钥字符串，这里假设私钥存在 privateKeyStr 变量中
            String privateKeyStr = keyConfig.getPrivateKey();
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
            String publicKeyStr = keyConfig.getPublicKey();
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
