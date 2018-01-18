package com.seeu.third.im.impl;

import com.seeu.third.exception.IMTokenGetException;
import com.seeu.third.im.IMService;
import com.seeu.third.im.impl.rong.RongCloud;
import com.seeu.third.im.impl.rong.models.TokenResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IMServiceImpl implements IMService {

    @Value("${rong.appKey}")
    private String appKey;

    @Value("${rong.appSecret}")
    private String appSecret;

    @Override
    public String getToken(Long uid, String username, String headIconUrl) throws IMTokenGetException {
        RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
        try {
            if (username == null) username = "defaultName";
            if (headIconUrl == null) headIconUrl = "http://r.com/defaultUrl";
            TokenResult result = rongCloud.user.getToken(String.valueOf(uid), username, headIconUrl);
            if (result == null)
                throw new IMTokenGetException(uid, "通信异常");
            if (result.getCode() != 200)
                throw new IMTokenGetException(uid, "融云服务器状态码异常，statusCode=" + result.getCode());
            return result.getToken();
        } catch (Exception e) {
            throw new IMTokenGetException(uid, e.getMessage());
        }
    }
}
