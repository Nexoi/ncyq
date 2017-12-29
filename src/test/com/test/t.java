package com.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class t {
    static List<String> arrayList = new ArrayList<>();

    static String token = "5_1F5ZY3OXsGIRLyKPt83-lXzv14EZq4rWc-3_UQ5W5atuAgaeBQkuQnIKS8vCYzIj2fGaDZtt_DXSRaUPjvZEptQM8_M-T9Oe9Mhzp1TL0dqSwYweBuFNR8YNpp1-O4kLxlIeWkwDN2NkHnY7HYYiAEAARR";

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = new HttpPost("https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + token);
        CloseableHttpResponse response = httpclient.execute(post);
        HttpEntity entity = response.getEntity();
        JSONObject object = JSON.parseObject(EntityUtils.toString(entity, "UTF-8"));
        JSONArray array = object.getJSONObject("data").getJSONArray("openid");
        for (int i = 0; i < array.size(); i++) {
            String openid = array.get(i).toString();
            // 做新的请求
            runOpenId(httpclient, openid);
        }

        for (String str : arrayList) {
            System.out.println(str);
        }
    }

    public static void runOpenId(CloseableHttpClient client, String openid) throws IOException {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + token + "&openid=" + openid;
        HttpPost post = new HttpPost(url);
        CloseableHttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();
        JSONObject object = JSON.parseObject(EntityUtils.toString(entity, "UTF-8"));
        String nickname = object.getString("nickname");
        String city = object.getString("city");
        String province = object.getString("province");
        String openId = object.getString("openid");
        arrayList.add("[][][][][]" + province + "\t" + city + "\t\t" + nickname + "________" + openId);
    }
}
