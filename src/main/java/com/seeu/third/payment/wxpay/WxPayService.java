package com.seeu.third.payment.wxpay;

import com.seeu.ywq.exception.ActionParameterException;
import com.seeu.ywq.pay.model.WxPayTradeModel;
import com.seeu.ywq.pay.service.OrderService;
import com.seeu.ywq.pay.service.WxPayTradeService;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 2:10 PM
 * Describe:
 * <p>
 * update: 2018-02-03 使用 Apache Http 进行请求
 */

@Service
public class WxPayService {

    @Value("${wxpay.appid}")
    private String appid;

    @Value("${wxpay.mchid}")
    private String mch_id;

    @Value("${wxpay.notify_url}")
    private String notifyUrl;

    @Value("${wxpay.key}")
    private String key;

    @Autowired
    private WxUtils wxUtils;
    @Autowired
    private WxPayTradeService wxPayTradeService;
    @Autowired
    private OrderService orderService;


    public String createOrder(String oid, BigDecimal price, String body, String ipAddress, String deviceInfo) throws ActionParameterException, IOException {
        String placeUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        SortedMap<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put("appid", appid);
        parameters.put("mch_id", mch_id);
        parameters.put("device_info", "WEB");
        parameters.put("body", body);
        parameters.put("nonce_str", wxUtils.gen32RandomString());
        parameters.put("notify_url", notifyUrl);
        parameters.put("out_trade_no", oid);
//        parameters.put("total_fee", price.multiply(BigDecimal.valueOf(100)).intValue());
        parameters.put("total_fee", 1);
        parameters.put("spbill_create_ip", ipAddress);
        parameters.put("trade_type", "APP");
        parameters.put("sign", wxUtils.createSign(parameters)); // 必须在最后
//        String result = restTemplate.postForObject(placeUrl, transferToXml(parameters), String.class);
        return wxUtils.executePost(placeUrl, parameters);
    }

    public String callBack(WxPayTradeModel model) {
        String sign = model.getSign();
        // 验证签名
        boolean signSucess = false;
        if (signSucess) {
            if (model.getReturn_code().equals("SUCCESS")) {
                if (model.getResult_code().equals("SUCCESS")) {
                    wxPayTradeService.save(model);
                    orderService.finishOrder(model.getOut_trade_no());
                } else {
                    wxPayTradeService.save(model);
                    orderService.failOrder(model.getOut_trade_no());
                }
            }
            return "<xml>\n" +
                    "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                    "</xml>";
        }
        return "<xml>\n" +
                "  <return_code><![CDATA[FAIL]]></return_code>\n" +
                "  <return_msg><![CDATA[]]></return_msg>\n" +
                "</xml>";
    }

    public String callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 读取参数
        InputStream inputStream;
        StringBuffer sb = new StringBuffer();
        inputStream = request.getInputStream();

        String str;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((str = in.readLine()) != null) {
            sb.append(str);
        }

        in.close();
        inputStream.close();

        // 解析Xml为map
        Map<String, String> map = new HashMap<String, String>();
        map = doXMLParse(sb.toString());

        // 过滤空  设置TreeMap
        SortedMap<Object, Object> sortMap = new TreeMap<Object, Object>();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String paramK = (String) it.next();
            String paramV = map.get(paramK);

            String value = "";
            if (null != paramV) {
                value = paramV.trim();
            }

            sortMap.put(paramK, value);
        }
        // 判断签名是否正确
        boolean isSignSuccess = isTenpaySign("UTF-8", sortMap);
        response.setHeader("Content-type", "application/xml");
        if (isSignSuccess) {
            return "<xml>\n" +
                    "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                    "</xml>";
        } else {
            return "<xml>\n" +
                    "  <return_code><![CDATA[FAIL]]></return_code>\n" +
                    "  <return_msg><![CDATA[]]></return_msg>\n" +
                    "</xml>";
        }
    }


    /**
     * xml解析为map
     *
     * @param strxml
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    private Map doXMLParse(String strxml) throws JDOMException, IOException {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        if (null == strxml || "".equals(strxml)) {
            return null;
        }
        Map m = new HashMap();
        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }
        //关闭流
        in.close();
        return m;
    }

    private String getChildrenText(List<Element> children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator<Element> it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List<Element> list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        return sb.toString();
    }

    /**
     * 是否签名正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名
     *
     * @return boolean
     * @throws Exception
     */
    private boolean isTenpaySign(String characterEncoding, SortedMap<Object, Object> packageParams) throws Exception {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<Object, Object>> es = packageParams.entrySet();
        Iterator<Map.Entry<Object, Object>> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (
                    null != k
                            && !"sign".equals(k)
                            && !"key".equals(k)
                            && null != v
                            && !"".equals(v)
                    ) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);

        // 算出摘要
        String mysign = HMACSHA256(sb.toString(), key.toUpperCase());
        String tenpaySign = ((String) packageParams.get("sign"));
        System.out.println("==============");
        System.out.println("mysign" + mysign);
        System.out.println("tenpaySign" + tenpaySign);
        System.out.println("==============");
        // System.out.println("tenpaySign:[" + tenpaySign + "] mysign:[" + mysign + "]");
        return tenpaySign.equals(mysign);
    }

    /**
     * 生成 HMACSHA256
     *
     * @param data 待处理数据
     * @param key  密钥
     * @return 加密结果
     * @throws Exception
     */
    private String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

}
