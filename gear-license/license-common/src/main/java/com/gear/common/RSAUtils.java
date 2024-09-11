package com.gear.common;

import com.gear.model.RsaKeyPair;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

@Slf4j
public class RSAUtils {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 245;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 256;

    public static final String PARAM_EQUAL = "=";

    public static final String PARAM_AND = "&";


    /**
     * 私钥解密
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 公钥解密
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 公钥加密
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 私钥加密
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 获取私钥
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return java.util.Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return java.util.Base64.getEncoder().encodeToString(key.getEncoded());
    }


    /**
     * 私钥加密
     *
     * @param data 加密数据
     * @return privateKey
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data, String privateKey) throws Exception {
        byte[] encryptArr = encryptByPrivateKey(java.util.Base64.getEncoder().encode(data.getBytes("UTF-8")), privateKey);
        return java.util.Base64.getEncoder().encodeToString(encryptArr);
    }

    /**
     * 私钥解密
     *
     * @param encryptedData 加密数据
     * @return privateKey
     * @throws Exception
     */
    public static String decryptByPrivateKey(String encryptedData, String privateKey)
            throws Exception {
        byte[] decryptArr = decryptByPrivateKey(java.util.Base64.getDecoder().decode(encryptedData), privateKey);
        return new String(java.util.Base64.getDecoder().decode(decryptArr), "UTF-8");
    }

    /**
     * 公钥加密
     *
     * @param data      源数据
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, String publicKey)
            throws Exception {
        byte[] encryptArr = encryptByPublicKey(java.util.Base64.getEncoder().encode(data.getBytes("UTF-8")), publicKey);
        return java.util.Base64.getEncoder().encodeToString(encryptArr);
    }

    /**
     * 公钥解密
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String encryptedData, String publicKey)
            throws Exception {
        byte[] decryptArr = decryptByPublicKey(java.util.Base64.getDecoder().decode(encryptedData), publicKey);
        return new String(java.util.Base64.getDecoder().decode(decryptArr), "UTF-8");
    }

    public static void main(String[] args) throws Exception {

        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKeA4ysPqkGbkbHKjM2E/n/ZMpTeqrcx9yIH/91o6x2+l6EzJnnT6eFETpJWN8Tek9rPaPKpeYG3gY4HxVhm+Syjc251wyAsiFWBxASk39qgFr+uE6XAyM0tqo/zftQbWRa/VSsJew7zfrc6jScWEqDYAZ4mjPc+QGcbolpuAfWtAgMBAAECgYBFz4/eOI8q/N2CDfsVBOLVAf700MCxzV9EjbTz4HBtWyvzAVB94fZN7pwYnVps8J8KyPrieAOuLn8OZOq452HdrbfDbX+Zly5pm9HFLouJyk7KpSLSkfqJzoZvgIofBUKltg1fmh7txTPBvo26CVXou4QJsWHvJJTvFQatyX8cgQJBAN/TOhcZ0ygrmWraa5ulXI6Act+EIAqwGdxYBUIVLwEWT6fcu9iq3xhWeKCVU9j3VaCFx1AowLPFgITfGo50m/ECQQC/lQWW2V8ZPn3fbucRgcuP1Dl2dttP43d+FUhwbWqy9eBycsbPNAYNt4Dhd2ENm31wnHZ+Z/UFvwq1+a1hM+F9AkAWKVryGJuAubhqDRBki937OhqlqPZnOIKG/6wdm+1YhTYD3+Y1kM2gIke1VrPDotG2oChY9oAGDMMp5NFDU6ZxAkBFopf2faoYVeOQrBHnBiOEcuI0Ef2jKw3K0VeULeEjjUV4tAlZVRKCN9nrmeW3+XV90hEr3wNrhEYTYN5JP39NAkEAyjM18FCWAYd1lXEEJuF5/uSqMBC2PLIOTGWzqGXpKT+ly7fIktEYLPjgWdq9zdI4mGSYa6afhsZGom5O35X95g==";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCngOMrD6pBm5GxyozNhP5/2TKU3qq3MfciB//daOsdvpehMyZ50+nhRE6SVjfE3pPaz2jyqXmBt4GOB8VYZvkso3NudcMgLIhVgcQEpN/aoBa/rhOlwMjNLaqP837UG1kWv1UrCXsO8363Oo0nFhKg2AGeJoz3PkBnG6JabgH1rQIDAQAB";

        /*byte[] jiami =  RSAUtils.encryptByPublicKey(Base64.getEncoder().encode(s.getBytes("UTF-8")), publicKey);
        byte[] jiemi = RSAUtils.decryptByPrivateKey(jiami, privateKey);
        log.info("公钥加密，私钥解密----解密后：" + new String(Base64.getDecoder().decode(jiemi), "UTF-8"));*/


        /*byte[] jiami1 =  RSAUtils.encryptByPrivateKey(Base64.getEncoder().encode(s.getBytes("UTF-8")), privateKey);
        byte[] jiemi1 = RSAUtils.decryptByPublicKey(jiami1, publicKey);
        log.info("私钥加密，公钥解密----解密后：" + new String(Base64.getDecoder().decode(jiemi1), "UTF-8"));*/


        String s = "[{\"needPdf\":\"1\",\"nsrsbh\":\"913201023532898821\"}]";
        log.info("明文：" + s);

        byte[] jiami1 = RSAUtils.encryptByPrivateKey(java.util.Base64.getEncoder().encode(s.getBytes("UTF-8")), privateKey);
        String str = java.util.Base64.getEncoder().encodeToString(jiami1);
        log.info(str);
        // 模拟接到请求参数进行解密
        byte[] jiemi1 = RSAUtils.decryptByPublicKey(jiami1, publicKey);
        log.info("私钥加密，公钥解密----解密后：" + new String(Base64.getDecoder().decode(jiemi1), "UTF-8"));
    }


    public static RsaKeyPair generateKeyPair() {
        KeyPairGenerator keygen;
        try {
            keygen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("RSA初始化密钥出现错误,算法异常");
        }
        SecureRandom secrand = new SecureRandom();
        //初始化随机产生器
        secrand.setSeed(String.valueOf(System.currentTimeMillis()).getBytes());
        //初始化密钥生成器
        keygen.initialize(2048, secrand);
        KeyPair keyPair = keygen.genKeyPair();
        //获取公钥并转成base64编码
        byte[] pub_key = keyPair.getPublic().getEncoded();
        String publicKeyStr = Base64.getEncoder().encodeToString(pub_key);
        //获取私钥并转成base64编码
        byte[] pri_key = keyPair.getPrivate().getEncoded();
        String privateKeyStr = Base64.getEncoder().encodeToString(pri_key);
        //创建一个Map返回结果
        return new RsaKeyPair(publicKeyStr, privateKeyStr);
    }
}
