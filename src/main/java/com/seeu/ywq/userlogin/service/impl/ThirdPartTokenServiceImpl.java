package com.seeu.ywq.userlogin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.seeu.ywq.userlogin.dvo.QQLoginVO;
import com.seeu.ywq.userlogin.dvo.WxLoginVO;
import com.seeu.ywq.userlogin.model.ThirdUserLogin;
import com.seeu.ywq.userlogin.service.ThirdPartTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ThirdPartTokenServiceImpl implements ThirdPartTokenService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${qq.appid}")
    private String qqAppId;


    @Override
    public void validatedInfo(ThirdUserLogin.TYPE type, String username, String token, Processor processor) {
        if (type == null)
            processor.process(false, null, null, null);
        if (type == ThirdUserLogin.TYPE.QQ)
            qqValidate(username, token, processor);
        if (type == ThirdUserLogin.TYPE.Weibo)
            weiboValidate(token, processor);
        if (type == ThirdUserLogin.TYPE.WeChat)
            wxValidate(username, token, processor);
    }


    private void qqValidate(String openId, String token, Processor processor) {
        String url = "https://graph.qq.com/user/get_simple_userinfo" +
                "?access_token=" + token +
                "&oauth_consumer_key=" + qqAppId +
                "&openid=" + openId +
                "&format=json";
        String voStr = restTemplate.getForObject(url, String.class);
        if (voStr == null) return;
        try {
            JSONObject jo = JSON.parseObject(voStr);
            if (null != jo.getString("nickname"))
                processor.process(true, openId, jo.getString("nickname"), jo.getString("figureurl_qq_1"));
            else
                processor.process(false, null, null, null);
            return;
        } catch (JSONException e) {
        }
        // default
        processor.process(false, null, null, null);
    }

    private void weiboValidate(String token, Processor processor) {
        String url = "https://api.weibo.com/oauth2/get_token_info" +
                "?access_token=" + token;
        String voStr = restTemplate.getForObject(url, String.class);
        if (voStr == null) return;
        try {
            JSONObject object = JSONObject.parseObject(voStr);
            if (null != object.getString("uid"))
                processor.process(true, object.getString("uid"), null, null);
            else
                processor.process(false, null, null, null);
        } catch (JSONException e) {
        }
        // default
        processor.process(false, null, null, null);
    }

    private void wxValidate(String openId, String token, Processor processor) {
        String url = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=" + token +
                "&openid=" + openId;
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
        if (entity == null) return;
        if (entity.getStatusCode() != HttpStatus.OK) return;
        if (entity.getBody() == null) return;
        JSONObject vo = null;
        try {
            vo = JSON.parseObject(entity.getBody());
            if (null != vo.getString("openid"))
                processor.process(true, vo.getString("openid"), vo.getString("nickname"), vo.getString("headimgurl"));
            else
                processor.process(false, null, null, null);
            return;
        } catch (JSONException e) {
        }
        // default
        processor.process(false, null, null, null);
    }
}
