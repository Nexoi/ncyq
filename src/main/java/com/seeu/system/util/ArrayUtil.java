package com.seeu.system.util;

/**
 * 数组工具类
 * 
 * @author mengbin
 * 
 */
public class ArrayUtil {
	public static String join(Object[] objs) {
		return join(objs, ",", "`");
	}

	public static String join(Object[] objs, String separator, String quote) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < objs.length; i++) {
			if (i < objs.length - 1) {
				buf.append(quote).append(objs[i].toString()).append(quote)
						.append(separator);
			} else {
				buf.append(quote).append(objs[i].toString()).append(quote);
			}
		}
		return buf.toString();
	}
	/***元素在数组中第一次出现的位置***/
	public static int indexOf(String[] objs, String value){
		int position = -1;
		for (int i = 0; i < objs.length; i++) {
			if (objs[i].equals(value)) {
				position = i;
				break;
			}
		}
		return position;
	}
}
