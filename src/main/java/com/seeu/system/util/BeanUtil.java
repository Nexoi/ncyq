package com.seeu.system.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;

public class BeanUtil
{
	private static transient Log log = LogFactory.getLog(BeanUtil.class);
	
	/**
	 * 获取参数信息完整度,即获取非默认值字段所占比率 * 100
	 * @param obj
	 * @return
	 */
	public static int getBeanCompleteRate(Object obj)
	{
		Class<?> clazz = obj.getClass();
		Method[] methods = clazz.getDeclaredMethods();
		double totalGet = 0;
		double totalNotEmptyGet = 0;
		for(Method method : methods)
		{
			if(method.getName().startsWith("get"))
			{
				try
				{
					Object result = method.invoke(obj);
					totalGet += 1d;
					if(result == null)
					{
						continue;
					}
					if(result instanceof Number)
					{
						Number num = (Number)result;
						if(num.byteValue() == 0)
						{
							continue;
						}
					}
					totalNotEmptyGet += 1d;
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		System.out.println(totalNotEmptyGet + "/" + totalGet);
		return (int)(totalNotEmptyGet / totalGet * 100);
	}
	
	/**
	 * 拷贝对象属性到另一个对象中(如果属性存在)
	 * @author mengbin
	 * @date Feb 16, 201411:29:14 AM
	 */
	public static void copyProperties(Object source,Object target,String[] ignoreProperties)throws Exception
	{
		BeanUtils.copyProperties(source, target, ignoreProperties);
	}
	
	public static void copyProperties(Object source,Object target)throws Exception
	{
		copyProperties(source, target, null);
	}
	
	public static JSON ehchance(JSON json,Map<String, Map<String, String>> map)throws Exception
	{
		return enchance(json, map, null);
	}
	
	/**
	 * 增强JSON数据
	 * json可以为JsonObject或者JsonArray
	 * map键为需要增强的字段名,值为该字段枚举值映射,键为value值为text
	 * suffix 为增强字段后缀
	 * @version: v1.0.0
	 * @author: mengbin
	 * @date: 2013-11-6下午08:13:46
	 */
	public static JSON enchance(JSON json,Map<String, Map<String, String>> map,String suffix)throws Exception
	{
		JSONArray array = null;
		boolean isArray = false;
		if(json instanceof JSONObject)
		{
			array = new JSONArray();
			array.add(json);
		}
		else if(json instanceof JSONArray)
		{
			array = (JSONArray)json;
			isArray = true;
		}
		else
		{
			return json;
		}
		if(map == null || map.size() < 1)
		{
			return json;
		}
		if(StringUtil.isEmptyString(suffix))
		{
			suffix = "text";
		}
		for(Iterator<String> iter = map.keySet().iterator();iter.hasNext();)
		{
			String key = iter.next();
			Map<String, String> _map = map.get(key);
			if(_map == null || _map.size() < 1)
			{
				continue;
			}
			for(int i=0;i<array.size();i++)
			{
				JSONObject obj = array.getJSONObject(i);
				if(obj.has(key))
				{
					String val = obj.get(key).toString();
					String text = _map.get(val);
					obj.put(key + "_" + suffix, text);
				}
			}
		}
		if(!isArray)
		{
			return array.getJSONObject(0);
		}
		return array;
	}
	
	/**
	 * 设置属性
	 * @author mengbin
	 * @date 2013-11-18 下午09:50:12
	 */
	public static void setProperty(Object obj,String name,Object value)throws Exception
	{
		PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(obj, name);
		if(pd == null)
		{
			log.error("未获取到属性:" + name + "的set方法!");
		}
		if(value == null)
		{
			Class<?> type = pd.getPropertyType();
			if(!BeanUtils.isSimpleProperty(type))
			{
				pd.getWriteMethod().invoke(obj, value);
			}
		}
		else
		{
			pd.getWriteMethod().invoke(obj, value);
		}
	}
}
