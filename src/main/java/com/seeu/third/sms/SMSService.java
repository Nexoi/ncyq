package com.seeu.third.sms;

import com.seeu.third.exception.SMSSendFailureException;

public interface SMSService {

    void send(String phone, String message) throws SMSSendFailureException;

    void sendTemplate(String phone, String templatesId, String... parameters);
}
