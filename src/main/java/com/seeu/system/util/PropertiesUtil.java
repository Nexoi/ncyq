package com.seeu.system.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 配置文件工具类
 * 所有配置文件放在config/properties路径下
 * 该路径作为根路径
 * @author mengbin
 * @date 2013年12月11日 下午1:49:02
 */
public class PropertiesUtil
{
	private static transient Log log = LogFactory.getLog(PropertiesUtil.class);
	
	private static final Map<String, Properties> MAPPING = new HashMap<String, Properties>();
	
	/**
	 * 刷新缓存
	 * @author mengbin
	 * @date 2013年12月11日 下午2:05:43
	 */
	public static void reload()
	{
		MAPPING.clear();
	}
	
	public static String getProperty(String path,String key,String defaultValue)
	{
		String value = getProperty(path, key);
		if(StringUtil.isEmptyString(value))
		{
			value = defaultValue;
		}
		return value;
	}
	
	/**
	 * 获取指定的属性
	 * @author mengbin
	 * @date 2013年12月11日 下午2:05:10
	 */
	public static Object getProperty(String path,Object key)
	{
		Properties props = getProperties(path);
		if(props != null)
		{
			return props.get(key);
		}
		return null;
	}
	
	/**
	 * 获取指定的属性
	 * @author mengbin
	 * @date 2013年12月11日 下午2:05:10
	 */
	public static String getProperty(String path,String key)
	{
		Properties props = getProperties(path);
		if(props != null)
		{
			return props.getProperty(key);
		}
		return null;
	}
	
	/**
	 * 获取配置信息
	 * @author mengbin
	 * @date 2013年12月11日 下午2:03:51
	 */
	public static Properties getProperties(String path)
	{
		if(StringUtil.isEmptyString(path))
		{
			return null;
		}
		path = convertPath(path);
		if(MAPPING.containsKey(path))
		{
			return MAPPING.get(path);
		}
		InputStream is = null;
		InputStreamReader isr = null;
		try
		{
			is = PropertiesUtil.class.getClassLoader().getResourceAsStream(path);
			if(is != null)
			{
				isr = new InputStreamReader(is, Charset.forName("utf-8"));
				Properties props = new Properties();
				props.load(isr);
				MAPPING.put(path, props);
				return props;
			}
		} catch (Exception e)
		{
			log.error(PropertiesUtil.class,e);
		}
		finally
		{
			if(isr != null)
			{
				try
				{
					isr.close();
				} catch (Exception e2)
				{
					log.error(PropertiesUtil.class,e2);
				}
			}
			if(is != null)
			{
				try
				{
					is.close();
				} catch (Exception e2)
				{
					log.error(PropertiesUtil.class,e2);
				}
			}
		}
		return null;
	}
	
	/**
	 * 转换PATH路径
	 * @author mengbin
	 * @date 2013年12月11日 下午1:54:58
	 */
	private static String convertPath(String path)
	{
		if(!path.endsWith(".properties"))
		{
			path = path + ".properties";
		}
		if(path.startsWith("properties/"))
		{
			return path;
		}
		if(path.startsWith("/properties/"))
		{
			return path.substring(1);
		}
		if(path.startsWith("/"))
		{
			return "properties" + path;
		}
		return "properties/" + path;
	}
	
	public static void main(String[] args)throws Exception
	{
		System.out.println(getProperty("sec/user.properties", "user.image.upload.path"));
	}
}
