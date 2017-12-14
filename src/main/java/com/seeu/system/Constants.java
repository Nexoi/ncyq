package com.seeu.system;

import java.text.SimpleDateFormat;

/** 系统常量 */
public class Constants {
	
	public static final String SYSTEM_PROPS_PATH = "/properties/cf/system.properties";
	
	public static final String DATE_MASK = "yyyy-MM-dd";

	public static final String DATETIME_MASK = "yyyy-MM-dd HH:mm:ss";

	public static final String TIMESTAMP_MASK = "yyyyMMddHHmmss"; // 时间戳
	
	/*public static final SimpleDateFormat DATE_MASK_SDF = new SimpleDateFormat(DATE_MASK);
	
	public static final SimpleDateFormat DATETIME_MASK_SDF = new SimpleDateFormat(DATETIME_MASK);
	
	public static final SimpleDateFormat TIMESTAMP_MASK_SDF = new SimpleDateFormat(TIMESTAMP_MASK);*/
	

	// 本地应用服务器地址
	public static final String APPLICATION_SERVICE_URL = "http://localhost:8088";//应用服务器地址

	public static final String APPLICATION_ERROR_FLAG = "error";

	public static final String APPLICATION_ERROR_LOG = "应用服务器异常，请查看对应的webservice接口方法！";

	/** Session Key定义 **/
	public interface SessionKey {

		public static final String SESSION_KEY_USER = "SESSION_KEY_USER";

	}


	public interface Boolean {
		
		public static final String YES = "1";

		public static final String NO = "0";
	}

	public interface CustomerStatus {

		public static final String NORMAL = "N";// 正常状态

		public static final String GREY = "G";// 灰名单

		public static final String BLACK = "B";// 黑名单
	}
	
	public interface Role {

		public static final String REDNET = "R";// 网红

		public static final String PERFORMER = "P";// 演员

		public static final String ORDINARY = "O";// 普通
	}

}
