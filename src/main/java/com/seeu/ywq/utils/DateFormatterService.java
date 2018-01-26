package com.seeu.ywq.utils;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DateFormatterService {

    private SimpleDateFormat yyyyMMddHHmmssS;
    private SimpleDateFormat yyyyMMddHHmmss;
    private SimpleDateFormat yyyyMMdd;

    public SimpleDateFormat getyyyyMMddHHmmssS() {
        if (yyyyMMddHHmmssS == null)
            yyyyMMddHHmmssS = new SimpleDateFormat("yyyyMMddHHmmssS");
        return yyyyMMddHHmmssS;
    }


    public SimpleDateFormat getyyyyMMddHHmmss() {
        if (yyyyMMddHHmmss == null)
            yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return yyyyMMddHHmmss;
    }


    public SimpleDateFormat getyyyyMMdd() {
        if (yyyyMMdd == null)
            yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        return yyyyMMdd;
    }

    public Long getyyyyMMdd(Date date) {
        return Long.parseLong(getyyyyMMdd().format(date));
    }
}
