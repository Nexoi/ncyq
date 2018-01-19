package com.seeu.third.sms.impl;

import com.seeu.third.exception.SMSSendFailureException;
import com.seeu.third.sms.SMSService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SMSMaiXunTongServiceImpl implements SMSService {

    @Value("${sms.url}")
    private String url;

    @Value("${sms.userId}")
    private String userId;

    @Value("${sms.account}")
    private String account;

    @Value("${sms.password}")
    private String password;


    @Override
    public void send(String phone, String message) throws SMSSendFailureException {
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        PostMethod getMethod = new PostMethod(url);
        String Content = null;
        try {
            Content = java.net.URLEncoder.encode(message, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        NameValuePair[] data = {
                new NameValuePair("UserID", userId),
                new NameValuePair("Account", account),
                new NameValuePair("Password", password),
                new NameValuePair("Phones", phone),
                new NameValuePair("SendType", "1"),
//                new NameValuePair("SendTime", df.format(date)),
                new NameValuePair("SendTime", ""),
                new NameValuePair("PostFixNumber", ""),
                new NameValuePair("Content", Content)};
        getMethod.setRequestBody(data);
        getMethod.addRequestHeader("Connection", "close");
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + getMethod.getStatusLine());
            }
            byte[] responseBody = getMethod.getResponseBody();
            String str = new String(responseBody, "utf-8");
            if (str.contains("GBK")) {
                str = str.replaceAll("GBK", "utf-8");
            }
            int beginPoint = str.indexOf("<RetCode>");
            int endPoint = str.indexOf("</RetCode>");
            String result = "RetCode=";
            str = str.substring(beginPoint + 9, endPoint);
//            System.out.println(result + str);
//            System.out.println(str);
            // return getMethod.getResponseBodyAsString();
            if (!str.contains("Sucess"))
                throw new SMSSendFailureException(phone, message);
        } catch (HttpException e) {
            throw new SMSSendFailureException(phone, message);
        } catch (IOException e) {
            throw new SMSSendFailureException(phone, message);
        } finally {
            getMethod.releaseConnection();
        }
    }

    @Override
    public void sendTemplate(String phone, String templatesId, String... parameters) {

    }
}
