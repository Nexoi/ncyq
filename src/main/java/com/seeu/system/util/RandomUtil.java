package com.seeu.system.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 随机数工具类
 * @author mengbin
 *
 */
public class RandomUtil {
	public static String getRandomeCode(int length)
	{
		if(length < 1)
		{
			length = 1;
		}
		Random rnd = new Random(new Date().getTime());
		int code = rnd.nextInt((int)Math.pow(10, length));
		return code + "";
	}
	
	/**
	 * 获取UUID,用于设置上传文件名
	 * @return
	 */
	public static String getUUID()
	{
		UUID uid = UUID.randomUUID();
		String id = uid.toString();
		return id.replaceAll("-", "");
	}
	
	public static void main(String[] args) {
		System.out.println(getUUID());
	}
	
public static String getDateString(){
		
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMddHHmmss" );
		String date = sdf.format( new Date() );
		
		return date;
	}
}
