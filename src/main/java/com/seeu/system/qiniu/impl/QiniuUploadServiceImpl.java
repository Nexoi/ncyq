package com.seeu.system.qiniu.impl;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.seeu.system.qiniu.ImageUploadService;
import com.seeu.ywq.release.model.Image;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service 
public class QiniuUploadServiceImpl implements ImageUploadService {
    @Override
    public Image uploadWithGetFullInfo(MultipartFile file) throws IOException {
        String url = upload(file);
        BufferedImage bufferedImg = ImageIO.read(file.getInputStream());
        int imgWidth = bufferedImg.getWidth();
        int imgHeight = bufferedImg.getHeight();
        Image image = new Image();
        image.setCreateDate(new Date());
        image.setHeight(imgHeight);
        image.setWidth(imgWidth);
        image.setImageUrl(url);
        image.setThumbImage100pxUrl(url + "?imageView2/2/w/100");
        image.setThumbImage200pxUrl(url + "?imageView2/2/w/200");
        image.setThumbImage300pxUrl(url + "?imageView2/2/w/300");
        image.setThumbImage500pxUrl(url + "?imageView2/2/w/500");
        return image;
    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        String key = "ywq" + UUID.randomUUID();
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        String accessKey = "ceLw-DN7neXckVebuUE6eOJC1GPBuXkXS4mkyOzb";
        String secretKey = "pg3QL2EuJY9LOjSayhBWGHvAlwfdxEk9UXrx9zT9";
        String bucket = "zspwork";
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(file.getBytes());
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        Response response = uploadManager.put(byteInputStream, key, upToken, null, null);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        System.out.println(putRet.key);
        System.out.println(putRet.hash);
        return "http://o7k6tx0fl.bkt.clouddn.com/" + key;
    }
}
