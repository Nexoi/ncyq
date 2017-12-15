package com.seeu.system.aliyunSdk;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 阿里云OSS工具类
 *
 * @author Scary
 */
@Service
public class AliyunOSSUtil {

    private static Logger logger = Logger.getLogger(AliyunOSSUtil.class);

    /**
     * 阿里云API外网域名
     */
    @Value("${endpoint}")
    private String ENDPOINT;

    /**
     * 阿里云API的密钥Access Key ID
     */
    @Value("${access.key.id}")
    private String ACCESS_KEY_ID;

    /**
     * 阿里云API的密钥Access Key Secret
     */
    @Value("${accessKeySecret}")
    private String ACCESS_KEY_SECRET;

    /**
     * URL过期时间
     */
    @Value("${expiration.date}")
    private String EXPIRATION_DATE;

    //处理图片的样式
    private String STYLE_A = "image/auto-orient,1/sharpen,120";//正常(锐化120)

    private String STYLE_B = "image/auto-orient,1/sharpen,100/quality,q_90/blur,r_40,s_15";//模糊

    private String STYLE_C = "image/auto-orient,1/sharpen,150/quality,q_100/format,jpg/interlace,1/bright,30/contrast,10";//自己DIY的效果

    private String STYLE_ORIGINAL = "image/quality,Q_100";
    private String STYLE_WIDTH_100 = "image/resize,w_100";
    private String STYLE_WIDTH_200 = "image/resize,w_200";
    private String STYLE_WIDTH_300 = "image/resize,w_300";
    private String STYLE_WIDTH_500 = "image/resize,w_500";

    private String STYLE_HAZY_ORIGINAL = "image/blur,r_40,s_15/quality,Q_100";
    private String STYLE_HAZY_WIDTH_100 = "image/blur,r_40,s_15/resize,w_100";
    private String STYLE_HAZY_WIDTH_200 = "image/blur,r_40,s_15/resize,w_200";
    private String STYLE_HAZY_WIDTH_300 = "image/blur,r_40,s_15/resize,w_300";
    private String STYLE_HAZY_WIDTH_500 = "image/blur,r_40,s_15/resize,w_500";

    private String STYLE_E = "";


    /**
     * 获取阿里云OSS客户端对象
     * 调用此方法一定要在最后ossClient.shutdown()释放资源！
     *
     * @return ossClient
     */
    public OSSClient getOSSClient() {
        return new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }


    /**
     * 创建存储空间
     *
     * @param ossClient  OSS连接
     * @param bucketName 存储空间
     * @return
     */
    public String createBucketName(OSSClient ossClient, String bucketName) {
        //存储空间  
        final String bucketNames = bucketName;
        if (!ossClient.doesBucketExist(bucketName)) {
            //创建存储空间  
            Bucket bucket = ossClient.createBucket(bucketName);
            logger.info("创建存储空间成功");
            return bucket.getName();
        }
        return bucketNames;
    }


    /**
     * 删除存储空间buckName
     *
     * @param ossClient  oss对象
     * @param bucketName 存储空间
     */
    public void deleteBucket(OSSClient ossClient, String bucketName) {
        ossClient.deleteBucket(bucketName);
        logger.info("删除 " + bucketName + " Backet成功");
    }


    /**
     * 创建模拟文件夹
     *
     * @param ossClient  oss连接
     * @param bucketName 存储空间
     * @param folder     模拟文件夹名如"qj_nanjing/"
     * @return 文件夹名
     */
    public String createFolder(OSSClient ossClient, String bucketName, String folder) {
        //文件夹名   
        final String keySuffixWithSlash = folder;
        //判断文件夹是否存在，不存在则创建  
        if (!ossClient.doesObjectExist(bucketName, keySuffixWithSlash)) {
            //创建文件夹  
            ossClient.putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
            logger.info("创建文件夹成功");
            //得到文件夹名  
            OSSObject object = ossClient.getObject(bucketName, keySuffixWithSlash);
            String fileDir = object.getKey();
            return fileDir;
        }
        return keySuffixWithSlash;
    }


    /**
     * 根据key删除OSS服务器上的文件
     *
     * @param ossClient  oss连接
     * @param bucketName 存储空间
     * @param folder     模拟文件夹名 如"qj_nanjing/"
     * @param key        Bucket下的文件的路径名+文件名 如："upload/cake.jpg"
     */
    public void deleteFile(OSSClient ossClient, String bucketName, String folder, String key) {
        ossClient.deleteObject(bucketName, folder + key);
        logger.info("删除" + bucketName + "下的文件" + folder + key + "成功");
    }


    /**
     * 上传图片至OSS
     *
     * @param ossClient  oss连接
     * @param file       上传文件（文件全路径如：D:\\image\\cake.jpg）
     * @param bucketName 存储空间
     * @param folder     模拟文件夹名 如"qj_nanjing/"
     * @param fileName   文件名
     * @return urls 返回对应文件url
     */
    public List<String> uploadObject2OSS(OSSClient ossClient, File file, String bucketName, String folder, String fileName) {

        String fileNameNow = null;
        try {
            //以输入流的形式上传文件  
            InputStream is = new FileInputStream(file);
            //文件大小  
            Long fileSize = file.length();
            //创建上传Object的Metadata    
            ObjectMetadata metadata = new ObjectMetadata();
            //上传的文件的长度  
            metadata.setContentLength(is.available());
            //指定该Object被下载时的网页的缓存行为  
            metadata.setCacheControl("no-cache");
            //指定该Object下设置Header  
            metadata.setHeader("Pragma", "no-cache");
            //指定该Object被下载时的内容编码格式  
            metadata.setContentEncoding("utf-8");
            //文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，  
            //如果没有扩展名则填默认值application/octet-stream  
            metadata.setContentType(getContentType(fileName));
            //指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）  
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
            //上传文件   (上传文件流的形式)  
            ossClient.putObject(bucketName, folder + fileName, is, metadata);
            //上传文件的文件名
            fileNameNow = folder + fileName;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("****************************************");
            logger.error("上传阿里云OSS服务器异常:" + e.getMessage(), e);
            logger.error("****************************************");
        }

        return getUrl(ossClient, bucketName, fileNameNow);
    }


    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     *
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public String getContentType(String fileName) {
        //文件的后缀名  
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if (".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension) || ".png".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        }
        if (".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if (".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if (".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if (".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if (".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if (".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        //默认返回类型  
        return "image/jpeg";
    }


    /**
     * 根据key获取url
     *
     * @param client
     * @param bucketName 储存空间
     * @param key        图片文件名称
     * @return
     */
    public List<String> getUrl(OSSClient client, String bucketName, String key) {

        List<String> urlList = new ArrayList<String>();
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, key, HttpMethod.GET);

        // 设置URL过期时间为1年
        Date expiration = new Date(new Date().getTime() + Integer.parseInt(EXPIRATION_DATE));
        req.setExpiration(expiration);

        // 正常图片
        req.setProcess(STYLE_ORIGINAL);
        URL url_0 = client.generatePresignedUrl(req);
        req.setProcess(STYLE_WIDTH_100);
        URL url_1 = client.generatePresignedUrl(req);
        req.setProcess(STYLE_WIDTH_200);
        URL url_2 = client.generatePresignedUrl(req);
        req.setProcess(STYLE_WIDTH_300);
        URL url_3 = client.generatePresignedUrl(req);
        req.setProcess(STYLE_WIDTH_500);
        URL url_5 = client.generatePresignedUrl(req);
        urlList.add(url_0.toString());
        urlList.add(url_1.toString());
        urlList.add(url_2.toString());
        urlList.add(url_3.toString());
        urlList.add(url_5.toString());
        // 模糊图片
        req.setProcess(STYLE_HAZY_ORIGINAL);
        URL url_0_hazy = client.generatePresignedUrl(req);
        req.setProcess(STYLE_HAZY_WIDTH_100);
        URL url_1_hazy = client.generatePresignedUrl(req);
        req.setProcess(STYLE_HAZY_WIDTH_200);
        URL url_2_hazy = client.generatePresignedUrl(req);
        req.setProcess(STYLE_HAZY_WIDTH_300);
        URL url_3_hazy = client.generatePresignedUrl(req);
        req.setProcess(STYLE_HAZY_WIDTH_500);
        URL url_5_hazy = client.generatePresignedUrl(req);
        urlList.add(url_0_hazy.toString());
        urlList.add(url_1_hazy.toString());
        urlList.add(url_2_hazy.toString());
        urlList.add(url_3_hazy.toString());
        urlList.add(url_5_hazy.toString());

        // 生成URL，10 张图
        return urlList;
    }


    /**
     * 获取OSS下所有的存储空间名称
     *
     * @param client
     * @return
     */
    public List<String> getBucketNames(OSSClient client) {
        List<String> list = new ArrayList<>();
        List<Bucket> buckets = client.listBuckets();
        // 遍历Bucket
        for (Bucket bucket : buckets) {
            System.out.println(bucket.getName());
            list.add(bucket.getName());
        }
        return list;
    }


}
