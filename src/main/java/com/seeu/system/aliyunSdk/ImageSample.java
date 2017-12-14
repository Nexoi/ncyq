package com.seeu.system.aliyunSdk;

import java.io.File;
import java.io.IOException;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;

/**
 * 阿里云官方文档的代码，仅作参考.
 *
 */
public class ImageSample {
    
    private static String endpoint = "oss-cn-beijing.aliyuncs.com";
    private static String accessKeyId = "LTAIxuV5z3PB4Zk2";
    private static String accessKeySecret = "fcPF3q48O6Ss6atnKRR76gRRlPE9p4";
    private static String bucketName = "ygq-image";
    private static String key = "YGQimageOBJaaa.jpg";
    
    public static void main(String[] args) throws IOException {        

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        
        try {
            // 调整大小
            String style = "image/resize,m_fixed,w_100,h_100";  
            GetObjectRequest request = new GetObjectRequest(bucketName, key);
            request.setProcess(style);
            
            ossClient.getObject(request, new File("example-resize.jpg"));
            
            // crop
            style = "image/crop,w_100,h_100,x_100,y_100,r_1"; 
            request = new GetObjectRequest(bucketName, key);
            request.setProcess(style);
            
            ossClient.getObject(request, new File("example-crop.jpg"));
            
            // 旋转
            style = "image/rotate,90"; 
            request = new GetObjectRequest(bucketName, key);
            request.setProcess(style);
            
            ossClient.getObject(request, new File("example-rotate.jpg"));
            
            // 锐化
            style = "image/sharpen,100"; 
            request = new GetObjectRequest(bucketName, key);
            request.setProcess(style);
            
            ossClient.getObject(request, new File("example-sharpen.jpg"));
            
            // 在图像中添加水印
            style = "image/watermark,text_SGVsbG8g5Zu-54mH5pyN5YqhIQ"; 
            request = new GetObjectRequest(bucketName, key);
            request.setProcess(style);
            
            ossClient.getObject(request, new File("example-watermark.jpg"));
            
            // 转换格式
            style = "image/format,png"; 
            request = new GetObjectRequest(bucketName, key);
            request.setProcess(style);
            
            ossClient.getObject(request, new File("example-format.png"));
            
            // 图像信息
            style = "image/info"; 
            request = new GetObjectRequest(bucketName, key);
            request.setProcess(style);
            
            ossClient.getObject(request, new File("example-info.txt"));
            
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Comment: " + oe.getErrorCode());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Comment: " + ce.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
    }
}
