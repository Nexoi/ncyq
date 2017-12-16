package com.seeu.file.aliyun_storage.impl;

import com.aliyun.oss.OSSClient;
import com.seeu.file.aliyun_storage.StorageImageService;
import com.seeu.system.aliyunSdk.AliyunOSSUtil;
import com.seeu.ywq.release.model.Image;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AliImageStorgeServiceImpl implements StorageImageService {

    @Value("${bucketName}")
    private String BACKET_NAME;

    @Override
    public Result saveImages(MultipartFile[] files) throws Exception {

        OSSClient client = aliyunOSSUtil.getOSSClient();
        List<List<String>> urlList = new ArrayList<>();
        List<int[]> imagePXList = new ArrayList<>();
        for (MultipartFile file : files) {
            // 获取宽高
            BufferedImage bufferedImg = ImageIO.read(file.getInputStream());
            int imgWidth = bufferedImg.getWidth();
            int imgHeight = bufferedImg.getHeight();
            imagePXList.add(new int[]{imgWidth, imgHeight});

            // 上传阿里云
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String dir = dateFormat.format(new Date());
            String folder = "ywq" + "/" + dir + "/";
            String fileName = file.getOriginalFilename();
            String suffix = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf('.')) : "";
            fileName = "YGQImage_" + UUID.randomUUID() + suffix;
            //保存图片
            List<String> urls = aliyunOSSUtil.uploadObject2OSS(client, file, BACKET_NAME, folder, fileName);
            urlList.add(urls);
        }
        client.shutdown();//释放资源

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

    @Autowired
    AliyunOSSUtil aliyunOSSUtil;

}
