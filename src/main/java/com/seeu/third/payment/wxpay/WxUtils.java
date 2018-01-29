package com.seeu.third.payment.wxpay;

import com.seeu.ywq.utils.MD5Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by suneo.
 * User: neo
 * Date: 29/01/2018
 * Time: 5:21 PM
 * Describe:
 */
@Service
public class WxUtils {

    @Autowired
    private MD5Service md5Service;

    private String Key = "";


    public String createSign(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + Key);
        return md5Service.encode(sb.toString());
    }

    public String gen32RandomString() {
        char[] dict = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < 32; i++) {
            sb.append(dict[random.nextInt(36)]);
        }
        return sb.toString();
    }
}
