package com.seeu.third.filestore.impl;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.seeu.third.filestore.FileUploadService;
import com.seeu.third.util.gaussion.GaussianFilter;
import com.seeu.ywq.resource.model.Image;
import com.seeu.ywq.resource.model.Video;
import com.seeu.ywq.trend.model.Picture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

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
    public List<Map<Picture.ALBUM_TYPE, Image>> uploadImagesWithDetialMap(MultipartFile[] files, Picture.ALBUM_TYPE[] types) throws IOException {
        if (files == null || types == null) return new ArrayList<>();
        if (files.length != types.length) return new ArrayList<>();
        List<Map<Picture.ALBUM_TYPE, Image>> list = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            // 获取宽高
            BufferedImage bufferedImg = ImageIO.read(files[i].getInputStream());
            list.add(uploadWithJudgeHazyJPEGWithMap(bufferedImg, types[i]));
        }
        return list;
    }

    @Override
    public List<Image> uploadImagesWithSeqList(MultipartFile[] files, Picture.ALBUM_TYPE[] types) throws IOException {
        if (files == null || types == null) return new ArrayList<>();
        if (files.length != types.length) return new ArrayList<>();
        List<Image> list = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            // 获取宽高
            BufferedImage bufferedImg = ImageIO.read(files[i].getInputStream());
            list.addAll(uploadWithJudgeHazyJPEGWithList(bufferedImg, types[i]));
        }
        return list;
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
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(file.getBytes());
        return upload(byteInputStream);
    }

    /**
     * 七牛专用方法
     *
     * @param byteInputStream
     * @return
     * @throws IOException
     */
    private String upload(ByteArrayInputStream byteInputStream) throws IOException {
        String key = "ywq" + UUID.randomUUID();
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        Response response = uploadManager.put(byteInputStream, key, upToken, null, null);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//        System.out.println(putRet.key);
//        System.out.println(putRet.hash);
        return "http://o7k6tx0fl.bkt.clouddn.com/" + key;
    }

    private Map<Picture.ALBUM_TYPE, Image> uploadWithJudgeHazyJPEGWithMap(BufferedImage bufferedImage, Picture.ALBUM_TYPE album_type) throws IOException {
        Map<Picture.ALBUM_TYPE, Image> map = new HashMap<>();
        // upload open image
        Image openImage = uploadImage(bufferedImage);
        map.put(Picture.ALBUM_TYPE.open, openImage);
        if (album_type == Picture.ALBUM_TYPE.close) {
            // upload close image
            BufferedImage imageContainer = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
            BufferedImage hazyImage = new GaussianFilter(80).filter(bufferedImage, imageContainer);
            Image closeImage = uploadImage(hazyImage);
            map.put(Picture.ALBUM_TYPE.close, closeImage);
        }
        return map;
    }

    private List<Image> uploadWithJudgeHazyJPEGWithList(BufferedImage bufferedImage, Picture.ALBUM_TYPE album_type) throws IOException {
        List<Image> images = new ArrayList<>();
        // upload open image
        Image openImage = uploadImage(bufferedImage);
        images.add(openImage);
        if (album_type == Picture.ALBUM_TYPE.close) {
            // upload close image
            BufferedImage imageContainer = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
            BufferedImage hazyImage = new GaussianFilter(80).filter(bufferedImage, imageContainer);
            Image closeImage = uploadImage(hazyImage);
            images.add(closeImage);
        }
        return images;
    }

    private Image uploadImage(BufferedImage bufferedImg) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImg, "jpeg", os);
        String url = upload(new ByteArrayInputStream(os.toByteArray()));
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
}
