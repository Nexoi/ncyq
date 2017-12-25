package com.seeu.third.sms.impl;

import com.seeu.third.exception.SMSSendFailureException;
import com.seeu.third.sms.SMSService;
import org.springframework.stereotype.Service;

@Service
public class SMSServiceImpl implements SMSService {
    @Override
    public void send(String phone, String message) throws SMSSendFailureException {

    }

    @Override
    public void sendTemplate(String phone, String templatesId, String... parameters) {

    }
}
