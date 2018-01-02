package com.seeu.system.yunxinsms.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.seeu.system.yunxinsms.service.ISmsSV;
import com.seeu.system.util.YunXinUtil;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "deprecation", "resource"})
@Service("iSmsSV")
@PropertySource("classpath:application.properties")
public class smsSVImpl implements ISmsSV {

    /**
     * 发送验证码的请求路径URL(验证类短信)
     */
    @Value("{yunxinsms.notify.url}")
    private String VALIDATE_SMS_URL;

    /**
     * 网易云信分配的账号
     */
    @Value("{yunxinsms.app.key}")
    private String APP_KEY;

    /**
     * 网易云信分配的密钥
     */
    @Value("{yunxinsms.app.secret}")
    private String APP_SECRET;

    /**
     * 随机数
     */
    @Value("{yunxinsms.app.nonce}")
    private String NONCE;

    /**
     * 验证码长度
     */
    @Value("{yunxinsms.app.codelen}")
    private String CODELEN;

    /**
     * 短信模板ID
     */
    @Value("{yunxinsms.app.templateid}")
    private String TEMPLATEID;


    @Override
    public String sendSMS(String phone) throws Exception {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(VALIDATE_SMS_URL);
        String curTime = String.valueOf((new Date()).getTime() / 1000L);

        /*
         * 参考计算CheckSum的java代码，在上述文档的参数列表中，有CheckSum的计算文档示例
         */
        String checkSum = YunXinUtil.getCheckSum(APP_SECRET, NONCE, curTime);

        // 设置请求的header
        httpPost.addHeader("AppKey", APP_KEY);
        httpPost.addHeader("Nonce", NONCE);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的的参数，requestBody参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        /*
         * 1.如果是模板短信，请注意参数mobile是有s的，详细参数配置请参考“发送模板短信文档”
         * 2.参数格式是jsonArray的格式，例如 "['13888888888','13666666666']"
         * 3.params是根据你模板里面有几个参数，那里面的参数也是jsonArray格式
         */
        JSONArray mobiles = new JSONArray();
        mobiles.add(phone);
        nvps.add(new BasicNameValuePair("templateid", TEMPLATEID));
        nvps.add(new BasicNameValuePair("mobiles", mobiles.toJSONString()));
        nvps.add(new BasicNameValuePair("codeLen", CODELEN));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        /****************************************************************/
        /** 执行请求
        HttpResponse response = httpClient.execute(httpPost);

//         * 1.打印执行结果，打印结果一般会200、315、403、404、413、414、500
//         * 2.具体的code有问题的可以参考官网的Code状态表

        String smsStr = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println("短信发送信息:" + smsStr);

        Gson gs = new Gson();
        Map<String, String> map = gs.fromJson(smsStr, Map.class);
        return map.get("obj"); // 这是 6 位验证码
        /****************************************************************/
		return "123456";
    }

    public int sendSMSWithCode(String phone, String code) throws Exception {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(VALIDATE_SMS_URL);
        String curTime = String.valueOf((new Date()).getTime() / 1000L);

        /*
         * 参考计算CheckSum的java代码，在上述文档的参数列表中，有CheckSum的计算文档示例
         */
        String checkSum = YunXinUtil.getCheckSum(APP_SECRET, NONCE, curTime);

        // 设置请求的header
        httpPost.addHeader("AppKey", APP_KEY);
        httpPost.addHeader("Nonce", NONCE);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的的参数，requestBody参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        /*
         * 1.如果是模板短信，请注意参数mobile是有s的，详细参数配置请参考“发送模板短信文档”
         * 2.参数格式是jsonArray的格式，例如 "['13888888888','13666666666']"
         * 3.params是根据你模板里面有几个参数，那里面的参数也是jsonArray格式
         */
        nvps.add(new BasicNameValuePair("templateid", TEMPLATEID));
        nvps.add(new BasicNameValuePair("mobile", phone));
        nvps.add(new BasicNameValuePair("code", code));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        /****************************************************************/
        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);

//         * 1.打印执行结果，打印结果一般会200、315、403、404、413、414、500
//         * 2.具体的code有问题的可以参考官网的Code状态表

        String smsStr = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println("短信发送信息:" + smsStr);

        Gson gs = new Gson();
        Map<String, String> map = gs.fromJson(smsStr, Map.class);
        return Integer.parseInt(map.get("code")); // 这是返回的状态码
    }

}
