package com.seeu.system.yunxinsms.service;


public interface ISmsSV {
	
	/**
	 * 发送验证码到指定号码的移动端
	 * @param phone
	 * @throws Exception
	 */
	public String sendSMS(String phone) throws Exception;
	public int sendSMSWithCode(String phone, String code) throws Exception;
}
