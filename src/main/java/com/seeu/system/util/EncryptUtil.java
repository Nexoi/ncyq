package com.seeu.system.util;

import java.security.MessageDigest;
import org.apache.commons.codec.binary.Base64;

/**
 * 加密解密工具类
 * 
 * @author mengbin
 * 
 */
public class EncryptUtil
{
	private static String toHex(byte[] bytes)
	{
		StringBuffer buf = new StringBuffer();
		for (int offset = 0; offset < bytes.length; offset++)
		{
			int i = bytes[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}

	/**
	 * MD5加密
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public static String md5(String msg) throws Exception
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(msg.getBytes());
		byte b[] = md.digest();
		return toHex(b);
	}
	
	/**
	 * BASE64加密
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(String msg)
	{
		byte[] b = msg.getBytes();
		Base64 base64 = new Base64();
		b = base64.encode(b);
		String s = new String(b);
		return s;
	}

	/**
	 * BASE64解密
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public static String decryptBASE64(String msg)
	{
		byte[] b = msg.getBytes();
		Base64 base64 = new Base64();
		b = base64.decode(b);
		String s = new String(b);
		return s;
	}

	/*public static void main(String[] args) throws Exception
	{
		System.out.println(encryptBASE64("XXX"));
		
		System.out.println(decryptBASE64("WFhY"));
	}*/
}
