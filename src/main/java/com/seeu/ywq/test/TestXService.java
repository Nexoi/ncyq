package com.seeu.ywq.test;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by suneo.
 * User: neo
 * Date: 05/02/2018
 * Time: 11:02 AM
 * Describe:
 */
@Service
public class TestXService {
    @Resource
    private TestXRepository repository;

    public void info(String info) {
        TestX x = new TestX();
        x.setInfo(info);
        repository.save(x);
    }
}
