
package com.seeu.system.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**  
 * ClassName: HttpUrlStreamUtil.java
 * @Description: 根据URL得到网络上的文件流（图片、文件等）
 *
 */
public class HttpUrlStreamUtil {
	
	private static transient Log log = LogFactory.getLog(HttpUrlStreamUtil.class);
	 
	/**  
     * 根据地址获取文件的输入流  
     * @param fileUrl 网络连接地址  
     * @return  
     */  
    public static InputStream getFileStreamFromNetByUrl(String fileUrl){   
        try {   
            URL url = new URL(fileUrl);   
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();   
            conn.setRequestMethod("GET");   
            conn.setConnectTimeout(5 * 1000);   
            InputStream inStream = conn.getInputStream();//通过输入流获取文件数据   
            return inStream;
        } catch (Exception e) {   
        	log.error(e.getMessage(), e);   
        }   
        return null;   
    }
    
    /**  
     * 根据地址获取文件的字节流  
     * @param fileUrl 网络连接地址  
     * @return  
     */  
    public static byte[] getFileBytesFromNetByUrl(String fileUrl){   
        try {   
            URL url = new URL(fileUrl);   
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();   
            conn.setRequestMethod("GET");   
            conn.setConnectTimeout(5 * 1000);   
            InputStream inStream = conn.getInputStream();//通过输入流获取文件数据   
            byte[] btImg = readInputStream(inStream);//得到文件的二进制数据   
            return btImg;   
        } catch (Exception e) {   
        	log.error(e.getMessage(), e);   
        }   
        return null;   
    }   
    
    /**  
     * 从输入流中获取数据  
     * @param inStream 输入流  
     * @return  
     * @throws Exception  
     */  
    public static byte[] readInputStream(InputStream inStream) throws Exception{   
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();   
        byte[] buffer = new byte[1024];   
        int len = 0;   
        while( (len=inStream.read(buffer)) != -1 ){   
            outStream.write(buffer, 0, len);   
        }   
        inStream.close();   
        return outStream.toByteArray();   
    }   

    /**  
     * 将图片写入到磁盘  
     * @param img 图片数据流  
     * @param fileName 文件保存时的名称  
     */  
    public static void writeImageToDisk(byte[] img, String fileName){   
        try {   
            File file = new File("C:\\Users\\User\\Desktop\\素材\\" + fileName);   
            FileOutputStream fops = new FileOutputStream(file);   
            fops.write(img);   
            fops.flush();   
            fops.close();   
            System.out.println("图片已经写入到C盘");   
        } catch (Exception e) {   
        	log.error(e.getMessage(), e);      
        }   
    }   
    
    /**  
     * 测试  
     * @param args  
     */  
    public static void main(String[] args) {
    	/**
        String url = "http://latex.codecogs.com/gif.latex?%5Cbigcap%20%5Cbigcap_%7Ba%7D%5E%7Bb%7D";   
        byte[] btImg = getFileBytesFromNetByUrl(url);   
        if(null != btImg && btImg.length > 0){   
            System.out.println("读取到：" + btImg.length + " 字节");   
            String fileName = "公式符号.gif";   
            writeImageToDisk(btImg, fileName);   
        }else{   
            System.out.println("没有从该连接获得内容");   
        } 
        */
    	
        String preString = "AAA<p><img alt=\"\\bigcap \\bigcap_{a}^{b}\" src=\"http://latex.codecogs.com/gif.latex?%5Cbigcap%20%5Cbigcap_%7Ba%7D%5E%7Bb%7D\"/></p>";
        preString += "BBB<p><img alt=\"\\bigcap \\bigcap_{a}^{b}\" src=\"http://latex.codecogs.com/gif.latex?%5Cbigcap%20%5Cbigcap_%7Ba%7D%5E%7Bb%7D\"/></p>ssxx</p>";
        preString += "DDD<p><img alt=\"\\bigcap \\bigcap_{a}^{b}\" src=\"http://ss.codecogs.com/gif.latex?%5Cbigcap%20%5Cbigcap_%7Ba%7D%5E%7Bb%7D\"/></p>FFF</p>";
        String patternStr = "(?is)<img[^>]*?src=(['\"\"\\s]?)(http://latex.codecogs.com/(gif|svg)\\.latex[^'\"\"\\s]*)\\2[^>]*?>";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcherSrc = pattern.matcher(preString);
        String newString = "";
        List<String> matchs = new ArrayList<String>();
        while(matcherSrc.find()) {
        	System.out.println("group1="+matcherSrc.group(1));
        	System.out.println("group2="+matcherSrc.group(2));
        	System.out.println("group3="+matcherSrc.group(3));
        	String replaceString = matcherSrc.group(2).replaceAll("\\?", "\\\\?");
        	System.out.println(replaceString);
        	matchs.add(replaceString);
        	//Matcher matcher2 = pattern.matcher(matcherSrc.group(2));
        	//newString = matcher2.replaceAll("http://www.baidu.com?UUID=123");
        	//newString = preString.replaceFirst(matcherSrc.group(2), "http://www.baidu.com?UUID=123");
        }
        for (String match : matchs) {
        	newString = preString.replaceAll(match, "http://www.baidu.com?UUID=123");
		}
        System.out.println(newString);
    }   
}
