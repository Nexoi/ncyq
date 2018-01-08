package com.seeu.ywq.utils;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
public class DateFormatterService {

    public SimpleDateFormat getyyyyMMddHHmmssS(){
        return new SimpleDateFormat("yyyyMMddHHmmssS");
    }
}
