package com.seeu.system.aliyunSdk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SimpleTimeZone;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import sun.misc.BASE64Encoder;


/**
 * 阿里云视频服务工具类
 *
 * @author Scary
 */
public class VideoUtil {

    @Value("vo.access.key.id")
    private String ACCESS_KEY_ID;
    @Value("vo.accessKey.secre")
    private String ACCESSKEY_SECRET;

    //以下参数不需要修改
    private String VOD_DOMAIN = "http://vod.cn-shanghai.aliyuncs.com/";
    private String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private String HTTP_METHOD = "GET";
    private String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private String UTF_8 = "utf-8";

    /**
     * 请求阿里云视频服务
     *
     * @param videoId 视频ID
     * @param api     请求服务对应的api关键字，详情可查看阿里云官方文档
     * @return
     * @throws Exception
     */
    public String requestVideoService(String videoId, String api) throws Exception {

        //生成私有参数，不同API需要修改
        Map<String, String> privateParams = generatePrivateParamters(videoId, api);
        //生成公共参数，不需要修改
        Map<String, String> publicParams = generatePublicParamters();
        //生成OpenAPI地址，不需要修改
        String URL = generateOpenAPIURL(publicParams, privateParams);
        //发送HTTP GET 请求
        String responseJson = httpGet(URL);

        return responseJson;
    }


    private String httpGet(String url) throws IOException {

        @SuppressWarnings("resource")
        Scanner s = new Scanner(new URL(url).openStream(), UTF_8).useDelimiter("\\A");
        try {
            String resposne = s.hasNext() ? s.next() : "true";
            System.out.println("阿里云视频服务返回参数: " + resposne);
            return resposne;
        } finally {
            s.close();
        }

    }


    /**
     * 生成视频点播OpenAPI私有参数
     * 不同API需要修改此方法中的参数
     *
     * @return
     */
    private Map<String, String> generatePrivateParamters(String videoId, String api) {
        // 接口私有参数列表, 不同API请替换相应参数
        Map<String, String> privateParams = new HashMap<>();
        // 视频ID
        privateParams.put("VideoId", videoId);
        // API名称
        privateParams.put("Action", api);
        return privateParams;
    }


    /**
     * 生成视频点播OpenAPI公共参数
     * 不需要修改
     *
     * @return
     */
    private Map<String, String> generatePublicParamters() {
        Map<String, String> publicParams = new HashMap<>();
        publicParams.put("Format", "JSON");
        publicParams.put("Version", "2017-03-21");
        publicParams.put("AccessKeyId", ACCESS_KEY_ID);
        publicParams.put("SignatureMethod", "HMAC-SHA1");
        publicParams.put("Timestamp", generateTimestamp());
        publicParams.put("SignatureVersion", "1.0");
        publicParams.put("SignatureNonce", generateRandom());
        return publicParams;
    }


    /**
     * 生成OpenAPI地址
     *
     * @param privateParams
     * @return
     * @throws Exception
     */
    private String generateOpenAPIURL(Map<String, String> publicParams, Map<String, String> privateParams) {
        return generateURL(VOD_DOMAIN, HTTP_METHOD, publicParams, privateParams);
    }


    /**
     * @param domain        请求地址
     * @param httpMethod    HTTP请求方式GET，POST等
     * @param publicParams  公共参数
     * @param privateParams 接口的私有参数
     * @return 最后的url
     */
    private String generateURL(String domain, String httpMethod, Map<String, String> publicParams, Map<String, String> privateParams) {
        List<String> allEncodeParams = getAllParams(publicParams, privateParams);
        String cqsString = getCQS(allEncodeParams);
        String stringToSign = httpMethod + "&" + percentEncode("/") + "&" + percentEncode(cqsString);
        String signature = hmacSHA1Signature(ACCESSKEY_SECRET, stringToSign);
        return domain + "?" + cqsString + "&" + percentEncode("Signature") + "=" + percentEncode(signature);
    }


    private List<String> getAllParams(Map<String, String> publicParams, Map<String, String> privateParams) {
        List<String> encodeParams = new ArrayList<String>();
        if (publicParams != null) {
            for (String key : publicParams.keySet()) {
                String value = publicParams.get(key);
                //将参数和值都urlEncode一下。
                String encodeKey = percentEncode(key);
                String encodeVal = percentEncode(value);
                encodeParams.add(encodeKey + "=" + encodeVal);
            }
        }
        if (privateParams != null) {
            for (String key : privateParams.keySet()) {
                String value = privateParams.get(key);
                //将参数和值都urlEncode一下。
                String encodeKey = percentEncode(key);
                String encodeVal = percentEncode(value);
                encodeParams.add(encodeKey + "=" + encodeVal);
            }
        }
        return encodeParams;
    }


    private String hmacSHA1Signature(String accessKeySecret, String stringtoSign) {

        try {

            String key = accessKeySecret + "&";
            try {
                SecretKeySpec signKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
                Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
                mac.init(signKey);
                byte[] rawHmac = mac.doFinal(stringtoSign.getBytes());
                //按照Base64 编码规则把上面的 HMAC 值编码成字符串，即得到签名值（Signature）
                return new String(new BASE64Encoder().encode(rawHmac));
            } catch (Exception e) {
                throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
            }

        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 参数urlEncode
     *
     * @param value
     * @return
     */
    private String percentEncode(String value) {

        try {

            String urlEncodeOrignStr = URLEncoder.encode(value, "UTF-8");
            String plusReplaced = urlEncodeOrignStr.replace("+", "%20");
            String starReplaced = plusReplaced.replace("*", "%2A");
            String waveReplaced = starReplaced.replace("%7E", "~");
            return waveReplaced;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }


    /**
     * 获取CQS 的字符串
     *
     * @param allParams
     * @return
     */
    private String getCQS(List<String> allParams) {
        ParamsComparator paramsComparator = new ParamsComparator();
        Collections.sort(allParams, paramsComparator);
        String cqString = "";
        for (int i = 0; i < allParams.size(); i++) {
            cqString += allParams.get(i);
            if (i != allParams.size() - 1) {
                cqString += "&";
            }
        }
        return cqString;
    }


    private class ParamsComparator implements Comparator<String> {
        @Override
        public int compare(String lhs, String rhs) {
            return lhs.compareTo(rhs);
        }
    }


    /**
     * 生成当前UTC时间戳
     *
     * @return
     */
    public String generateTimestamp() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat df = new SimpleDateFormat(ISO8601_DATE_FORMAT);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }


    /**
     * 生成随机数
     *
     * @return
     */
    private String generateRandom() {
        String signatureNonce = UUID.randomUUID().toString();
        return signatureNonce;
    }

}
