package com.seeu.system.util;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class AESUtil {
   
   
   /**
    * AES加密
    * @param sSrc	需要加密的内容
    * @param sKey	密钥
    * @return
    * @throws Exception
    */
   public static String Encrypt(String sSrc, String sKey) throws Exception {
       if (sKey == null) {
           System.out.print("Key为空null");
           return null;
       }
       // 判断Key是否为16位
       if (sKey.length() != 16) {
           System.out.print("Key长度不是16位");
           return null;
       }
       byte[] raw = sKey.getBytes("utf-8");
       SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
       Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
       cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
       byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

       return new Base64().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
   
   }
   
   
   /**
    * AES解密
    * @param sSrc	需要解密的内容
    * @param sKey	密钥
    * @return
    * @throws Exception
    */
   public static String Decrypt(String sSrc, String sKey) throws Exception {
       try {
           // 判断Key是否正确
           if (sKey == null) {
               System.out.print("Key为空null");
               return null;
           }
           // 判断Key是否为16位
           if (sKey.length() != 16) {
               System.out.print("Key长度不是16位");
               return null;
           }
           byte[] raw = sKey.getBytes("utf-8");
           SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
           Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
           cipher.init(Cipher.DECRYPT_MODE, skeySpec);
           byte[] encrypted1 = new Base64().decode(sSrc);//先用base64解密
           try {
               byte[] original = cipher.doFinal(encrypted1);
               String originalString = new String(original,"utf-8");
               return originalString;
           } catch (Exception e) {
               System.out.println(e.toString());
               return null;
           }
       } catch (Exception ex) {
           System.out.println(ex.toString());
           return null;
       }
   }
   
   
}
