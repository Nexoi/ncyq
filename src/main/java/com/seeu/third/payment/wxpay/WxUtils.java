package com.seeu.third.payment.wxpay;

import com.seeu.ywq.utils.MD5Service;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 5:21 PM
 * Describe:
 */
@Service
public class WxUtils {

    @Autowired
    private MD5Service md5Service;

    @Value("${wxpay.key}")
    private String Key;

    public String executePost(String url, SortedMap<String, Object> parameters) throws IOException {
        HttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);
        request.setHeader("Content-type", "application/xml");
        request.setHeader("Accept", "application/xml");
        request.setEntity(new StringEntity(transferToXml(parameters), "UTF-8"));
        HttpResponse response = client.execute(request);
        return readResponse(response);
    }

    /* 读取 response body 内容为字符串 */
    private String readResponse(HttpResponse response) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        String result = new String();
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        return result;
    }

    private String transferToXml(SortedMap<String, Object> map) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (String key : map.keySet()) {
            sb.append("<").append(key).append(">")
                    .append(map.get(key))
                    .append("</").append(key).append(">");
        }
        return sb.append("</xml>").toString();
    }

    public String createSign(SortedMap<String, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + Key);
        return md5Service.encode(sb.toString());
    }

    public String gen32RandomString() {
        char[] dict = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
                , 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < 32; i++) {
            sb.append(String.valueOf(dict[(int) (Math.random() * 36)]));
        }
        return sb.toString();
    }
}
