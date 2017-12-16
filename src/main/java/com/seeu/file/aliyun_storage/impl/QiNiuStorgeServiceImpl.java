package com.seeu.file.aliyun_storage.impl;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.seeu.file.aliyun_storage.StorageImageService;
import com.seeu.ywq.release.model.Image;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class QiNiuStorgeServiceImpl implements StorageImageService {
    @Override
    public Result saveImages(MultipartFile[] files) throws Exception {
        List<List<String>> urlList = new ArrayList<>();
        List<int[]> imagePXList = new ArrayList<>();

        for (MultipartFile file : files) {
            // 获取宽高
            BufferedImage bufferedImg = ImageIO.read(file.getInputStream());
            int imgWidth = bufferedImg.getWidth();
            int imgHeight = bufferedImg.getHeight();
            imagePXList.add(new int[]{imgWidth, imgHeight});

            // 上传QiNiu
            List<String> urls = upload(file.getBytes());
            urlList.add(urls);
        }
        // 存储图片 URL 等基本信息到数据库
        List<Image> images = new ArrayList<>();
        for (int i = 0; i < urlList.size(); i++) {
            // px
            int width = imagePXList.get(i)[0];
            int height = imagePXList.get(i)[1];

            // URLs
            List<String> urls = urlList.get(i);
            Image image = new Image();
            image.setCreateDate(new Date());
            image.setImageUrl(urls.get(0)); //
            image.setThumbImage100pxUrl(urls.get(1));
            image.setThumbImage200pxUrl(urls.get(2));
            image.setThumbImage300pxUrl(urls.get(3));
            image.setThumbImage500pxUrl(urls.get(4));
            image.setWidth(width);
            image.setHeight(height);

            Image imageHazy = new Image();
            imageHazy.setCreateDate(new Date());
            imageHazy.setImageUrl(urls.get(5)); //
            imageHazy.setThumbImage100pxUrl(urls.get(6));
            imageHazy.setThumbImage200pxUrl(urls.get(7));
            imageHazy.setThumbImage300pxUrl(urls.get(8));
            imageHazy.setThumbImage500pxUrl(urls.get(9));
            imageHazy.setWidth(width);
            imageHazy.setHeight(height);
            // add-
            images.add(image);
            images.add(imageHazy);
        }
        // ~
        Result result = new Result();
        result.setImageList(images);
        result.setImageNum(images.size() / 2);
        result.setStatus(Result.STATUS.success);
        return result;
    }

    private List<String> upload(byte[] inputBytes) throws Exception {
        String key = "ywq" + UUID.randomUUID();
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        String accessKey = "ceLw-DN7neXckVebuUE6eOJC1GPBuXkXS4mkyOzb";
        String secretKey = "pg3QL2EuJY9LOjSayhBWGHvAlwfdxEk9UXrx9zT9";
        String bucket = "zspwork";
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(inputBytes);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        Response response = uploadManager.put(byteInputStream, key, upToken, null, null);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        System.out.println(putRet.key);
        System.out.println(putRet.hash);
        List<String> urls = new ArrayList<>();
        String host = "http://o7k6tx0fl.bkt.clouddn.com/";
        urls.add(host + key);// 原图
        urls.add(host + key +"?imageView2/2/w/100");// 缩略图100
        urls.add(host + key +"?imageView2/2/w/200");// 缩略图200
        urls.add(host + key +"?imageView2/2/w/300");// 缩略图300
        urls.add(host + key +"?imageView2/2/w/500");// 缩略图500
        urls.add(host + key +"?imageMogr2/blur/40x50");// 原图 hazy
        urls.add(host + key +"?imageMogr2/blur/40x50&imageView2/2/w/100");// 缩略图100
        urls.add(host + key +"?imageMogr2/blur/40x50&imageView2/2/w/200");// 缩略图200
        urls.add(host + key +"?imageMogr2/blur/40x50&imageView2/2/w/300");// 缩略图300
        urls.add(host + key +"?imageMogr2/blur/40x50&imageView2/2/w/500");// 缩略图500
        return urls;
    }
}
