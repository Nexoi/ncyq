package com.seeu.third.filestore.impl;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.seeu.third.filestore.FileUploadService;
import com.seeu.ywq.resource.model.Image;
import com.seeu.ywq.resource.model.Video;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class QiniuUploadServiceImpl implements FileUploadService {
    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Override
    public Image uploadImage(MultipartFile file) throws IOException {
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
    public List<Image> uploadImages(MultipartFile[] files) throws IOException {
        if (files == null) return new ArrayList<>();
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file == null) continue;
            images.add(uploadImage(file));
        }
        return images;
    }


    @Override
    public Video uploadVideo(MultipartFile videoFile, MultipartFile coverImage) throws IOException {
        String url = upload(videoFile);
        String coverUrl = upload(coverImage);
        Video video = new Video();
        // 封面要用户自己传，不支持截屏取图，此处已经存储至 OSS 上
        video.setSrcUrl(url);
        video.setCoverUrl(coverUrl);
        video.setId(null);
        return video;
    }

    @Override
    public Video uploadVideo(MultipartFile videoFile) throws IOException {
        String url = upload(videoFile);
        Video video = new Video();
        // 封面要用户自己传，不支持截屏取图
        video.setSrcUrl(url);
        video.setCoverUrl(null);
        video.setId(null);
        return video;
    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        String key = "ywq" + UUID.randomUUID();
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(file.getBytes());
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        Response response = uploadManager.put(byteInputStream, key, upToken, null, null);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//        System.out.println(putRet.key);
//        System.out.println(putRet.hash);
        return "http://o7k6tx0fl.bkt.clouddn.com/" + key;
    }
}
