/**
 * 融云 Server API java 客户端
 * create by kitName
 * create datetime : 2017-03-13 
 * 
 * v2.0.1
 */
package com.seeu.system.rongcloud.io.rong;
import java.util.concurrent.ConcurrentHashMap;

import com.seeu.system.rongcloud.io.rong.methods.Chatroom;
import com.seeu.system.rongcloud.io.rong.methods.Group;
import com.seeu.system.rongcloud.io.rong.methods.Message;
import com.seeu.system.rongcloud.io.rong.methods.Push;
import com.seeu.system.rongcloud.io.rong.methods.SMS;
import com.seeu.system.rongcloud.io.rong.methods.User;
import com.seeu.system.rongcloud.io.rong.methods.Wordfilter;

public class RongCloud {

	private static ConcurrentHashMap<String, RongCloud> rongCloud = new ConcurrentHashMap<String,RongCloud>();
	
	public User user;
	public Message message;
	public Wordfilter wordfilter;
	public Group group;
	public Chatroom chatroom;
	public Push push;
	public SMS sms;

	private RongCloud(String appKey, String appSecret) {
		user = new User(appKey, appSecret);
		message = new Message(appKey, appSecret);
		wordfilter = new Wordfilter(appKey, appSecret);
		group = new Group(appKey, appSecret);
		chatroom = new Chatroom(appKey, appSecret);
		push = new Push(appKey, appSecret);
		sms = new SMS(appKey, appSecret);

	}

	public static RongCloud getInstance(String appKey, String appSecret) {
		if (null == rongCloud.get(appKey)) {
			rongCloud.putIfAbsent(appKey, new RongCloud(appKey, appSecret));
		}
		return rongCloud.get(appKey);
	}
	 
}