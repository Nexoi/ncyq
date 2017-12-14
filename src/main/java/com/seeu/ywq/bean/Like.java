package com.seeu.ywq.bean;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 点赞关系表
 * @author Scary
 *
 */
@Entity
@Table(name = "likes",indexes = {
		@Index(name = "like_index1",columnList = "uid"),
		@Index(name = "like_index2",columnList = "liked_uid"),
		@Index(name = "like_index3",columnList = "publish_id")
})
public class Like implements Serializable {

	public enum LIKE_HASREAD{
		read,
		unread
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;//主键
	@NotNull
	private Long uid;//用户号
	@NotNull
	@Column(name = "liked_uid")
	private Long likedUid;//被点赞的用户号

	@Column(name = "publish_id")
	private Long publishId;//对应的发布信息ID
	@Enumerated
	private LIKE_HASREAD hasRead;//是否阅读(0否，1是)
	
	private Date createTime;//创建时间
	
	private String userName;//发布评论的人的昵称（不与数据库交互）
	
	private String userHeadUrl;//发布评论的人的头像地址（不与数据库交互）

	
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

	public Long getLikedUid() {
		return likedUid;
	}

	public void setLikedUid(Long likedUid) {
		this.likedUid = likedUid;
	}

	public Long getPublishId() {
		return publishId;
	}

	public void setPublishId(Long publishId) {
		this.publishId = publishId;
	}

	public LIKE_HASREAD getHasRead() {
		return hasRead;
	}

	public void setHasRead(LIKE_HASREAD hasRead) {
		this.hasRead = hasRead;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
}
