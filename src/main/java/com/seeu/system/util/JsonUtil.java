package com.seeu.system.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * JSON工具类
 * @author mengbin
 * @date 2014年1月8日 上午11:27:11
 */
public class JsonUtil
{
	private static final JsonValueProcessor dateJsonValueProcessor = new DateJsonValueProcessor();
	
	public static JSON serialize(Object obj)
	{
		return serialize(obj, null);
	}
	
	public static JSON serialize(Object obj,String[] exclueds)
	{
		JsonConfig jsConfig = new JsonConfig();
		jsConfig.registerJsonValueProcessor(Date.class, dateJsonValueProcessor);
		jsConfig.registerJsonValueProcessor(Timestamp.class, dateJsonValueProcessor);
		jsConfig.setExcludes(exclueds);
		return JSONSerializer.toJSON(obj,jsConfig);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> fromJsonStringToList(String jsonStr, Class<T> clazz)
	{
		JSONArray jsonArray = JSONArray.fromObject(jsonStr);
		List<?> jsonList = (List<?>) JSONArray
				.toCollection(jsonArray, clazz);
		return (List<T>)jsonList;
	}
}

class DateJsonValueProcessor implements JsonValueProcessor
{	
	@Override
	public Object processArrayValue(Object values, JsonConfig jsCfg)
	{
		if(values != null)
		{
			String[] retVals = null;
			if(values instanceof Timestamp[])
			{
				Timestamp[] stamps = (Timestamp[])values;
				retVals = new String[stamps.length];
				for(int i=0;i<stamps.length;i++)
				{
					retVals[i] = DateUtil.formatDate2String(stamps[i]);
				}
			}
			else if(values instanceof Date[])
			{
				Date[] dates = (Date[])values;
				retVals = new String[dates.length];
				for(int i=0;i<dates.length;i++)
				{
					String temp = DateUtil.formatDate2String(dates[i]);
					int pos = temp.indexOf(" 00:00:00");
					if(pos >= 0)
					{
						retVals[i] = temp.substring(0,pos);
					}
				}
			}
			if(retVals != null)
			{
				return JSONSerializer.toJSON(retVals);
			}
		}
		
		return null;
	}

	@Override
	public Object processObjectValue(String key, Object value, JsonConfig jsCfg)
	{
		if(value != null)
		{
			String retVal = null;
			if(value instanceof Timestamp)
			{
				retVal = DateUtil.formatDate2String((Timestamp)value);
			}
			else if(value instanceof Date)
			{
				retVal = DateUtil.formatDate2String((Date)value);
				int pos = retVal.indexOf(" 00:00:00");
				if(pos >= 0)
				{
					retVal = retVal.substring(0,pos);
				}
			}
			return retVal;
		}
		return null;
	}
}
