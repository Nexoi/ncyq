package com.seeu.ywq.bean;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 留言表
 * @author Scary
 *
 */
@Entity
@Table(name = "comment",indexes = {
		@Index(name = "comment_index1",columnList = "uid"),
		@Index(name = "comment_index2",columnList = "leave_uid")
})
public class Comment implements Serializable {
	public static enum MESSAGE_HASREAD{
		read,
		unread
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;//主键
	
	private Long uid;//

	private String userName;//发布评论的人的昵称

	private String userHeadUrl;//发布评论的人的头像地址

	@Column(name = "leave_uid")
	private Long leaveUid;//被留言用户号
	
	private Long publishId;//对应的发布信息ID
	
	private String text;//留言内容

	@Enumerated
	private MESSAGE_HASREAD hasRead;//是否阅读

	private Date createTime;//创建时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserHeadUrl() {
		return userHeadUrl;
	}

	public void setUserHeadUrl(String userHeadUrl) {
		this.userHeadUrl = userHeadUrl;
	}

	public Long getLeaveUid() {
		return leaveUid;
	}

	public void setLeaveUid(Long leaveUid) {
		this.leaveUid = leaveUid;
	}

	public Long getPublishId() {
		return publishId;
	}

	public void setPublishId(Long publishId) {
		this.publishId = publishId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public MESSAGE_HASREAD getHasRead() {
		return hasRead;
	}

	public void setHasRead(MESSAGE_HASREAD hasRead) {
		this.hasRead = hasRead;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
