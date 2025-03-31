package com.dchealth.cro.common.crypto;
 
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithID;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;

import lombok.extern.slf4j.Slf4j;
 
/**
 * @Description 国密公私钥对 工具类
 * @date 2021/11/24 16:10
 */
@Slf4j
public class Sm2Utils {
	public static final String PUBLIC_KEY_NAME = "publicKey";
	public static final String PRIVATE_KEY_NAME = "privateKey";
	/**
	 * 生成公私钥
	 * @return
	 */
	public  static  Map<String, String> createKeys(){
		Map<String, String> keyPairMap = new HashMap<String, String>();
		 try {
			 SMKeyPair sMKeyPair = genKeyPair(false);
			keyPairMap.put(PUBLIC_KEY_NAME, sMKeyPair.getPubKey());
			keyPairMap.put(PRIVATE_KEY_NAME, sMKeyPair.getPriKey());
		} catch (Exception e) {
		}
		 return keyPairMap;
	}
	
	/**
     * 生成SM2公私钥对
     * @return
     */
    private static AsymmetricCipherKeyPair genKeyPair0() {
        //获取一条SM2曲线参数
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");

        //构造domain参数
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(),
                sm2ECParameters.getG(), sm2ECParameters.getN());

        //1.创建密钥生成器
        ECKeyPairGenerator keyPairGenerator = new ECKeyPairGenerator();

        //2.初始化生成器,带上随机数
        try {
            keyPairGenerator.init(new ECKeyGenerationParameters(domainParameters, SecureRandom.getInstance("SHA1PRNG")));
        } catch (NoSuchAlgorithmException e) {
            log.error("生成公私钥对时出现异常:", e);
//            e.printStackTrace();
        }

        //3.生成密钥对
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = keyPairGenerator.generateKeyPair();
        return asymmetricCipherKeyPair;
    }

    /**
     * 生成公私钥对(默认压缩公钥)
     * @return
     */
    public static SMKeyPair genKeyPair() {
        return genKeyPair(true);
    }

    /**
     * 生成公私钥对
     * @param compressedPubKey  是否压缩公钥
     * @return
     */
    public static SMKeyPair genKeyPair(boolean compressedPubKey) {
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = genKeyPair0();

        //提取公钥点
        ECPoint ecPoint = ((ECPublicKeyParameters) asymmetricCipherKeyPair.getPublic()).getQ();
        //公钥前面的02或者03表示是压缩公钥,04表示未压缩公钥,04的时候,可以去掉前面的04
        String pubKey = Hex.toHexString(ecPoint.getEncoded(compressedPubKey));

        BigInteger privatekey = ((ECPrivateKeyParameters) asymmetricCipherKeyPair.getPrivate()).getD();
        String priKey = privatekey.toString(16);

        SMKeyPair keyPair = new SMKeyPair(priKey, pubKey);
        return keyPair;
    }

    /**
     * 私钥签名
     * @param privateKey    私钥
     * @param content       待签名内容
     * @return
     */
    public static String sign(String privateKey, String content) throws CryptoException {
        //待签名内容转为字节数组
        byte[] message = Hex.decode(content);

        //获取一条SM2曲线参数
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        //构造domain参数
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(),
                sm2ECParameters.getG(), sm2ECParameters.getN());

        BigInteger privateKeyD = new BigInteger(privateKey, 16);
        ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(privateKeyD, domainParameters);

        //创建签名实例
        SM2Signer sm2Signer = new SM2Signer();

        //初始化签名实例,带上ID,国密的要求,ID默认值:1234567812345678
        try {
            sm2Signer.init(true, new ParametersWithID(new ParametersWithRandom(privateKeyParameters, SecureRandom.getInstance("SHA1PRNG")), Strings.toByteArray("1234567812345678")));
        } catch (NoSuchAlgorithmException e) {
            log.error("签名时出现异常:", e);
        }
        sm2Signer.update(message, 0, message.length);
        //生成签名,签名分为两部分r和s,分别对应索引0和1的数组
        byte[] signBytes = sm2Signer.generateSignature();

        String sign = Hex.toHexString(signBytes);

        return sign;
    }


    /**
     * 验证签名
     * @param publicKey     公钥
     * @param content       待签名内容
     * @param sign          签名值
     * @return
     */
    public static boolean verify(String publicKey, String content, String sign) {
        //待签名内容
        byte[] message = Hex.decode(content);
        byte[] signData = Hex.decode(sign);

        // 获取一条SM2曲线参数
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        // 构造domain参数
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(),
                sm2ECParameters.getG(),
                sm2ECParameters.getN());
        //提取公钥点
        ECPoint pukPoint = sm2ECParameters.getCurve().decodePoint(Hex.decode(publicKey));
        // 公钥前面的02或者03表示是压缩公钥，04表示未压缩公钥, 04的时候，可以去掉前面的04
        ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(pukPoint, domainParameters);
        //创建签名实例
        SM2Signer sm2Signer = new SM2Signer();
        ParametersWithID parametersWithID = new ParametersWithID(publicKeyParameters, Strings.toByteArray("1234567812345678"));
        sm2Signer.init(false, parametersWithID);
        sm2Signer.update(message, 0, message.length);
        //验证签名结果
        boolean verify = sm2Signer.verifySignature(signData);
        return verify;
    }

    /**
     * SM2加密算法
     * @param publicKey     公钥
     * @param data          数据
     * @return
     */
    public static String encrypt(String publicKey, String data){
        // 获取一条SM2曲线参数
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        // 构造domain参数
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(),
                sm2ECParameters.getG(),
                sm2ECParameters.getN());
        //提取公钥点
        ECPoint pukPoint = sm2ECParameters.getCurve().decodePoint(Hex.decode(publicKey));
        // 公钥前面的02或者03表示是压缩公钥，04表示未压缩公钥, 04的时候，可以去掉前面的04
        ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(pukPoint, domainParameters);

        SM2Engine sm2Engine = new SM2Engine();
        sm2Engine.init(true, new ParametersWithRandom(publicKeyParameters, new SecureRandom()));

        byte[] arrayOfBytes = null;
        try {
            byte[] in = data.getBytes("utf-8");
            arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
        } catch (Exception e) {
            log.error("SM2加密时出现异常:", e);
        }
        return Hex.toHexString(arrayOfBytes);
    }

    /**
     * SM2加密算法
     * @param publicKey     公钥
     * @param data          明文数据
     * @return
     */
    public static String encrypt(PublicKey publicKey, String data) {

        ECPublicKeyParameters ecPublicKeyParameters = null;
        if (publicKey instanceof BCECPublicKey) {
            BCECPublicKey bcecPublicKey = (BCECPublicKey) publicKey;
            ECParameterSpec ecParameterSpec = bcecPublicKey.getParameters();
            ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(),
                    ecParameterSpec.getG(), ecParameterSpec.getN());
            ecPublicKeyParameters = new ECPublicKeyParameters(bcecPublicKey.getQ(), ecDomainParameters);
        }

        SM2Engine sm2Engine = new SM2Engine();
        sm2Engine.init(true, new ParametersWithRandom(ecPublicKeyParameters, new SecureRandom()));

        byte[] arrayOfBytes = null;
        try {
            byte[] in = data.getBytes("utf-8");
            arrayOfBytes = sm2Engine.processBlock(in,0, in.length);
        } catch (Exception e) {
            log.error("SM2加密时出现异常:", e);
        }
        return Hex.toHexString(arrayOfBytes);
    }

    /**
     * SM2解密算法
     * @param privateKey    私钥
     * @param cipherData    密文数据
     * @return
     */
    public static String decrypt(String privateKey, String cipherData) {
        byte[] cipherDataByte = Hex.decode(cipherData);

        //获取一条SM2曲线参数
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        //构造domain参数
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(),
                sm2ECParameters.getG(), sm2ECParameters.getN());

        BigInteger privateKeyD = new BigInteger(privateKey, 16);
        ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(privateKeyD, domainParameters);

        SM2Engine sm2Engine = new SM2Engine();
        sm2Engine.init(false, privateKeyParameters);

        String result = null;
        try {
            byte[] arrayOfBytes = sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length);
            return new String(arrayOfBytes, "utf-8");
        } catch (Exception e) {
            log.error("SM2解密时出现异常:", e);
        }
        return result;

    }

    /**
     * SM2解密算法
     * @param privateKey        私钥
     * @param cipherData        密文数据
     * @return
     */
    public static String decrypt(PrivateKey privateKey, String cipherData) {
        byte[] cipherDataByte = Hex.decode(cipherData);

        BCECPrivateKey bcecPrivateKey = (BCECPrivateKey) privateKey;
        ECParameterSpec ecParameterSpec = bcecPrivateKey.getParameters();

        ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(),
                ecParameterSpec.getG(), ecParameterSpec.getN());

        ECPrivateKeyParameters ecPrivateKeyParameters = new ECPrivateKeyParameters(bcecPrivateKey.getD(),
                ecDomainParameters);

        SM2Engine sm2Engine = new SM2Engine();
        sm2Engine.init(false, ecPrivateKeyParameters);

        String result = null;
        try {
            byte[] arrayOfBytes = sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length);
            return new String(arrayOfBytes, "utf-8");
        } catch (Exception e) {
            log.error("SM2解密时出现异常:", e);
        }
        return result;
    }

    /**
     * 将未压缩公钥压缩成压缩公钥
     * 公钥前面的02或者03表示是压缩公钥，04表示未压缩公钥, 04的时候，可以去掉前面的04
     * @param pubKey    未压缩公钥(16进制,不要带头部04)
     * @return
     */
    public static String compressPubKey(String pubKey) {
        pubKey = "04" + pubKey;    //将未压缩公钥加上未压缩标识.
        // 获取一条SM2曲线参数
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        //提取公钥点
        ECPoint pukPoint = sm2ECParameters.getCurve().decodePoint(Hex.decode(pubKey));
        String compressPubKey = Hex.toHexString(pukPoint.getEncoded(Boolean.TRUE));

        return compressPubKey;
    }

    /**
     * 将压缩的公钥解压为非压缩公钥
     * @param compressKey   压缩公钥
     * @return
     */
    public static String unCompressPubKey(String compressKey) {
        // 获取一条SM2曲线参数
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        //提取公钥点
        ECPoint pukPoint = sm2ECParameters.getCurve().decodePoint(Hex.decode(compressKey));
        String pubKey = Hex.toHexString(pukPoint.getEncoded(Boolean.FALSE));
        //去掉前面的04   (04的时候，可以去掉前面的04)
        pubKey = pubKey.substring(2);
        return pubKey;
    }
 
}