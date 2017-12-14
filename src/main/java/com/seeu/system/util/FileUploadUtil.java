package com.seeu.system.util;

import java.io.File;
import java.util.List;
import java.util.UUID;

import com.seeu.system.Constants;
import com.seeu.system.aliyunSdk.AliyunOSSUtil;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class FileUploadUtil {
	
	/**
	 * 阿里云API的bucket名称
	 */
	private String BACKET_NAME = PropertiesUtil.getProperty(Constants.SYSTEM_PROPS_PATH, "bucketName");

	@Autowired
	AliyunOSSUtil aliyunOSSUtil;

	public List<String> getImageUrls(File file, String userNo, String businType) throws Exception {
				
		//模拟文件夹路径
		//需要设计什么样的路径来保存自己想一下---对应后面的删除文件思路
		String folder = userNo + "/" + "ywq" + "/";
		
		OSSClient client = aliyunOSSUtil.getOSSClient();
		
		//保存图片
		String fileName = "YGQImage_" + UUID.randomUUID()+".jpg";
		List<String> urls = aliyunOSSUtil.uploadObject2OSS(client, file, BACKET_NAME, folder, fileName);
		
		client.shutdown();//释放资源
		return urls;
	}
	

}
