package com.seeu.system.util;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Token工具类
 * @author Administrator
 *
 */
public class TokenUtil{

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
	public static String md5(String msg) throws Exception{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(msg.getBytes());
		byte b[] = md.digest();
		return toHex(b);
	}
	
	
	/**
	 * 获取token
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	public static String getToken(String userNo) throws Exception{
		
		Date dt = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String nowTime = df.format(dt);
		//java.sql.Timestamp buyDate = java.sql.Timestamp.valueOf(nowTime);
		
		return md5(userNo + "-" + nowTime);
	}

}
