package com.seeu.ywq.utils.impl;

import com.seeu.ywq.page_video.model.HomePageVideo;
import com.seeu.ywq.utils.AppVOUtils;
import com.seeu.ywq.trend.model.Publish;
import com.seeu.ywq.userlogin.model.UserLogin;
import com.seeu.ywq.uservip.model.UserVIP;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class AppVOUtilsImpl implements AppVOUtils {

    public List<Long> parseBytesToLongList(Object object) {
        if (object == null) return new ArrayList<>();
        if (object instanceof byte[]) {
            byte[] bytes = (byte[]) object;
            if (bytes.length == 0) return new ArrayList<>();
            List<Long> longs = new ArrayList<>();
            String temp = "";
            for (int i = 0; i < bytes.length; i++) {
                byte id = bytes[i];
                if (id == 44) {
                    // 这是逗号
                    longs.add(parseLong(temp)); // 转成对应的 int 值
                    temp = "";
                } else if (i + 1 == bytes.length) {
                    // 这是句末
                    temp += Byte.toUnsignedLong(id) - 48;
                    longs.add(parseLong(temp));
                    temp = "";
                } else {
                    temp += Byte.toUnsignedLong(id) - 48;
                }
            }
        }
        if (object instanceof String) {
            String longStr = object.toString();
            List<Long> longs = new ArrayList<>();
            String[] ids = longStr.split(",");
            for (String id : ids) {
                longs.add(parseLong(id));
            }
            return longs;
        }
        return new ArrayList<>();
    }

    public Integer parseInt(Object object) {
        if (object == null) return 0;
        return Integer.parseInt(object.toString());
    }


    public Long parseLong(Object object) {
        if (object == null) return 0l;
        return Long.parseLong(object.toString());
    }

    public String parseString(Object object) {
        if (object == null) return null;
        return object.toString();
    }

    public Double parseDouble(Object object) {
        if (object == null) return null;
        return Double.parseDouble(object.toString());
    }

    private static DateFormatter formatter = new DateFormatter("yyyy-MM-dd HH:mm");

    public Date parseDate(Object object) {
        if (object == null) return null;
        // TODO
        String dateStr = object.toString();
        try {
            return formatter.parse(dateStr, Locale.CHINESE);
        } catch (ParseException e) {
            return null;
        }
    }

    public HomePageVideo.CATEGORY parseCATEGORY(Object object) {
        if (object == null) return null;
        int categoryIndex = Integer.parseInt(object.toString());
        return (categoryIndex == 0) ? HomePageVideo.CATEGORY.hd : HomePageVideo.CATEGORY.vr;
    }


    public Publish.PUBLISH_TYPE paresPUBLISH_TYPE(Object object) {
        if (object == null) return null;
        int categoryIndex = Integer.parseInt(object.toString());
        switch (categoryIndex) {
            case 0:
                return Publish.PUBLISH_TYPE.word;
            case 1:
                return Publish.PUBLISH_TYPE.picture;
            case 2:
                return Publish.PUBLISH_TYPE.video;
            default:
                return Publish.PUBLISH_TYPE.word;
        }
    }

    @Override
    public UserLogin.GENDER parseGENDER(Object object) {
        if (object == null) return null;
        int gender = parseInt(object);
        return 0 == gender ? UserLogin.GENDER.male : UserLogin.GENDER.female;
    }

    @Override
    public UserVIP.VIP parseVIP(Object object) {
        if (object == null) return null;
        int vip = parseInt(object);
        switch (vip) {
            case 0:
                return UserVIP.VIP.none;
            case 1:
                return UserVIP.VIP.vip;
            default:
                return UserVIP.VIP.none;
        }
    }
}
