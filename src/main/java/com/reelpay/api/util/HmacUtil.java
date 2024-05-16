package com.reelpay.api.util;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class HmacUtil {
   //加密算法
   public static final String HMAC_SHA1 = "HmacSHA1";
   public static final String HMAC_MD5 = "HmacMD5";
   public static final String HMAC_SHA256 = "HmacSHA256";
   public static final String HMAC_SHA512 = "HmacSHA512";


  /**
   * 实现Hmac系列的加密算法HmacSHA1、HmacMD5等
   *
   * @param input 需要加密的输入参数
   * @param key 密钥
   * @param algorithm 选择加密算法
   * @return 加密后的值
   **/
   public static String encrypt(String input, String key, String algorithm) {
       String cipher = "";
       try {
           byte[] data = key.getBytes(StandardCharsets.UTF_8);
           //根据给定的字节数组构造一个密钥，第二个参数指定一个密钥的算法名称，生成HmacSHA1专属密钥
           SecretKey secretKey = new SecretKeySpec(data, algorithm);

           //生成一个指定Mac算法的Mac对象
           Mac mac = Mac.getInstance(algorithm);
           //用给定密钥初始化Mac对象
           mac.init(secretKey);
           byte[] text = input.getBytes(StandardCharsets.UTF_8);
           byte[] encryptByte = mac.doFinal(text);
           cipher = bytesToHexStr(encryptByte);
       } catch (NoSuchAlgorithmException | InvalidKeyException e) {
           e.printStackTrace();
       }
       return cipher;
   }


   /**
    * byte数组转16进制字符串
    *
    * @param  bytes byte数组
    * @return hex字符串
    */
   public static String bytesToHexStr(byte[] bytes) {
       StringBuilder hexStr = new StringBuilder();
       for (byte b : bytes) {
           String hex = Integer.toHexString(b & 0xFF);
           if (hex.length() == 1) {
               hex = '0' + hex;
           }
           hexStr.append(hex);
       }
       return hexStr.toString();
   }
   
   public static void Test(String[] args) {
       String valMD5 = HmacUtil.encrypt("abc", "123456", HmacUtil.HMAC_MD5);
       System.out.println(valMD5);

       String valSha1 = HmacUtil.encrypt("abc", "123456", HmacUtil.HMAC_SHA1);
       System.out.println(valSha1);

       String valSha256 = HmacUtil.encrypt("abc", "123456", HmacUtil.HMAC_SHA256);
       System.out.println(valSha256);

       String valSha512 = HmacUtil.encrypt("abc", "123456", HmacUtil.HMAC_SHA512);
       System.out.println(valSha512);

   }
}