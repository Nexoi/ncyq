package com.seeu.system.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * ClassName: HttpClientUtil.java
 * Description: 使用HTTP协议进行远程调用的客户端，请求参数和返回结果可以是JSON串
 *
 */
public class HttpClientUtil {
	
	private static transient Log log = LogFactory.getLog(HttpClientUtil.class);

	public static String httpPost(String httpUrl, Map<String, String> jsonParamsMap) {
		//构造HttpClient的实例
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(httpUrl);

		// 将表单的值放入postMethod中
		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
		Iterator<Entry<String, String>> paramIt = jsonParamsMap.entrySet().iterator();
		for(; paramIt.hasNext();) {
			Entry<String, String> paramEntry = paramIt.next();
			NameValuePair pair = new NameValuePair(paramEntry.getKey(), paramEntry.getValue());
			nameValuePairList.add(pair);
		}
		postMethod.setRequestBody(nameValuePairList.toArray(new NameValuePair[nameValuePairList.size()]));
		
		// 使用系统提供的默认的恢复策略
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
						new DefaultHttpMethodRetryHandler());
		
		try {
			long startTime = System.currentTimeMillis();
			int statusCode = httpClient.executeMethod(postMethod); // 执行postMethod
			long endTime = System.currentTimeMillis();
			log.info("statusCode:" + statusCode);
			log.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));
             
			// HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
			// 301或者302
			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
					|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
				// 从头中取出转向的地址
				Header locationHeader = postMethod.getResponseHeader("location");
				String location = null;
				if (locationHeader != null) {
					location = locationHeader.getValue();
					log.error("The page was redirected to:" + location);
				} else {
					log.error("Location field value is null.");
				}
				return null;
			}
			
			// 读取内容
			String responseBody = postMethod.getResponseBodyAsString();
			log.info(responseBody);
			return responseBody;
		} catch (HttpException e) { // 发生致命的异常，可能是协议不对或者返回的内容有问题
			log.error(e.getMessage(), e);
			return null;
		} catch (IOException e) { // 发生网络异常
			log.error(e.getMessage(), e);
			return null;
		} finally { // 释放连接
			postMethod.releaseConnection();
		}

	}

	public static String httpGet(String httpUrl, Map<String, String> jsonParamsMap) {
		// 构造HttpClient的实例
		HttpClient httpClient = new HttpClient();
		// 创建GET方法的实例
		GetMethod getMethod = new GetMethod(httpUrl);
		
		// 将表单的值放入GetMethod中
		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
		if(jsonParamsMap!=null){
			Iterator<Entry<String, String>> paramIt = jsonParamsMap.entrySet().iterator();
			for(; paramIt.hasNext();) {
				Entry<String, String> paramEntry = paramIt.next();
				NameValuePair pair = new NameValuePair(paramEntry.getKey(), paramEntry.getValue());
				nameValuePairList.add(pair);
			}	
		}
		
		getMethod.setQueryString(nameValuePairList.toArray(new NameValuePair[nameValuePairList.size()]));
		
		// 使用系统提供的默认的恢复策略
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		try {
			long startTime = System.currentTimeMillis();
			// 执行getMethod
			int statusCode = httpClient.executeMethod(getMethod);
			long endTime = System.currentTimeMillis();
			log.info("statusCode:" + statusCode);
            log.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));
            
			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: "
						+ getMethod.getStatusLine());
			}
			// 读取内容
			String responseBody = getMethod.getResponseBodyAsString();
			//log.info(responseBody);
			
			return responseBody;
		} catch (HttpException e) { // 发生致命的异常，可能是协议不对或者返回的内容有问题
			log.error(e.getMessage(), e);
			return null;
		} catch (IOException e) { // 发生网络异常
			log.error(e.getMessage(), e);
			return null;
		} finally { // 释放连接
			getMethod.releaseConnection();
		}
	}
	
	public static void main(String[] args) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("loginName", "admin");
		paramsMap.put("password", "123456");
		String url = "http://localhost:9080/mclsys/userLogin";

		String retString = HttpClientUtil.httpPost(url, paramsMap);

		System.out.println("远程返回：" + retString);
		
	}
}
