package com.seeu.ywq.utils.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seeu.third.push.PushService;
import com.seeu.ywq.resource.model.Image;
import com.seeu.ywq.trend.model.Picture;
import com.seeu.ywq.trend.model.Publish;
import com.seeu.ywq.trend.service.PublishPictureService;
import com.seeu.ywq.trend.service.PublishService;
import com.seeu.ywq.utils.PictureYellowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suneo.
 * User: neo
 * Date: 19/01/2018
 * Time: 5:43 PM
 * Describe:
 */
@Service
public class PictureYellowServiceImpl implements PictureYellowService {

    @Autowired
    private PublishService publishService;
    @Autowired
    private PublishPictureService publishPictureService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PushService pushService;

    @Value("${ywq.validate.picture}")
    private Boolean validate;

    @Async
    @Override
    public void validated(Long publishId) {
        if (!validate) return;
        Publish publish = publishService.findOne(publishId);
        if (publish == null) return;
        List<Picture> pictures = publish.getPictures();
        if (pictures == null || pictures.size() == 0) return;
        Long uid = publish.getUid();
        if (publish.getType() != Publish.PUBLISH_TYPE.picture)
            return; // 只验证图片类动态
        try {

            boolean flag = false; // 是否有黄图
            double score = 0.0;
            for (Picture picture : pictures) {
                if (picture == null) continue;
                Image image = picture.getImageOpen();
                if (image == null) continue;
                String imgUrl = image.getImageUrl();
                // start 鉴定
                String responStr = restTemplate.getForObject(imgUrl + "?qpulp", String.class);
                JSONObject jo = JSON.parseObject(responStr);
                if (0 == (jo.getInteger("code"))) {
                    JSONObject result = jo.getJSONObject("result");
                    if (result != null) {
                        Integer label = result.getInteger("label");
                        if (0 == label) {
                            flag = true;
                            score = result.getDouble("score");
                        }
                    }
                }
            }

            if (!flag) return; // flag 为 false 则表示一切正常

            // 删除！
            for (Picture picture : pictures) {
                picture.setDeleteFlag(Picture.DELETE_FLAG.delete);
            }
            publish.setStatus(Publish.STATUS.delete);

            // 通知
            if (uid == null) return;
            Map extraData = new HashMap();
            extraData.put("message", "动态图片含有色情信息");
            extraData.put("score", score);
            pushService.singlePush(uid, "您的图片含有色情信息，已被删除！", null, extraData);
            publishPictureService.save(pictures);
            publishService.save(publish);
        } catch (Exception e) {
            //...
        }
    }
}
