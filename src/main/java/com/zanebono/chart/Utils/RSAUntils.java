package com.zanebono.chart.Utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.naming.ConfigurationException;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RSAUntils {
    public static String privatePath = "C:\\pem\\privateKey.txt";
    public static String publicPath = "C:\\pem\\publicKey.txt";
    /** *//**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /** *//**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /** *//**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /** *//**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /** *//**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /** *//**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    public byte[] encrypt(PublicKey publicKey,byte[] srcBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, BadPaddingException {
        if(publicKey!=null){
            //Cipher负责完成加密或解密工作，基于RSA
            Cipher cipher = Cipher.getInstance("RSA");
            //根据公钥，对Cipher对象进行初始化
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] resultBytes = cipher.doFinal(srcBytes);
            return resultBytes;
        }
        return null;
    }

    /**
     * 解密
     * @param privateKey
     * @param srcBytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public byte[] decrypt(PrivateKey privateKey,byte[] srcBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        if(privateKey!=null){
            //Cipher负责完成加密或解密工作，基于RSA
            Cipher cipher = Cipher.getInstance("RSA");
            //根据公钥，对Cipher对象进行初始化
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            int inputLen = srcBytes.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] resultBytes = cipher.doFinal(srcBytes);
            return resultBytes;
//            int offSet = 0;
//            byte[] cache;
//            int i = 0;
//            // 对数据分段解密
//            while (inputLen - offSet > 0) {
//                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
//                    cache = cipher.doFinal(srcBytes, offSet, MAX_DECRYPT_BLOCK);
//                } else {
//                    cache = cipher.doFinal(srcBytes, offSet, inputLen - offSet);
//                }
//                out.write(cache, 0, cache.length);
//                i++;
//                offSet = i * MAX_DECRYPT_BLOCK;
//            }
//            byte[] decryptedData = out.toByteArray();
//            out.close();
//            return decryptedData;
        }
        return null;
    }

    public void saveFile(String path,String key)throws Exception{
        File file = new File(path);
        if (file.exists()){
            System.out.println("文件已存在");
        }else {
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(key.getBytes());
            fos.flush();
            fos.close();
        }
    }
    public PrivateKey LoadPrivateKey(String path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        FileInputStream fis = new FileInputStream(path);
        byte[] keyBytes = new byte[1024];
        fis.read(keyBytes);
        fis.close();
        //Base64解码
        keyBytes=Base64.decodeBase64(keyBytes);
        // 实例化PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        // 实例化KeyFactory对象，并指定DSA算法
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // 获得PrivateKey对象
        PrivateKey privateKey = keyFactory.generatePrivate (keySpec);
        return privateKey;
    }
    public PublicKey LoadPublicKey(String path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        FileInputStream fis = new FileInputStream(path);
        byte[] keyBytes = new byte[1024];
        fis.read(keyBytes);
        fis.close();
        //Base64解码
        keyBytes=Base64.decodeBase64(keyBytes);
        // 实例化PKCS8EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        // 实例化KeyFactory对象，并指定DSA算法
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // 获得PrivateKey对象
        PublicKey publicKey = keyFactory.generatePublic (keySpec);
        return publicKey;
    }
    /**
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     */
    public void initialize() throws Exception {

        String msg = "郭XX-精品相声";
        //KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        //初始化密钥对生成器，密钥大小为1024位
        keyPairGen.initialize(1024);
        //生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        //得到私钥
        PrivateKey privateKey = keyPair.getPrivate();
        //得到公钥
        PublicKey publicKey = keyPair.getPublic();

        byte[] privateBT = privateKey.getEncoded();
        // base64
        this.saveFile(privatePath, Base64.encodeBase64String(privateBT));
        //============================================//
        byte[] publicBT = publicKey.getEncoded();
        // base64
        this.saveFile(publicPath, Base64.encodeBase64String(publicBT));

        PrivateKey temp = this.LoadPrivateKey(privatePath);
        PublicKey temp_pub =  this.LoadPublicKey(publicPath);
        byte[] srcBytes = msg.getBytes();
        byte[] resultBytes = this.encrypt(temp_pub, srcBytes);

        //用私钥解密
        byte[] decBytes = this.decrypt(temp, resultBytes);

        System.out.println("明文是:" + msg);
        System.out.println("加密后是:" + new String(resultBytes));
        System.out.println("解密后是:" + new String(decBytes));
    }
}
