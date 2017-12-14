package com.seeu.ywq.bean;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 关注表
 * @author zho
 *
 */
@Entity
@Table(name = "follow", indexes = {
		@Index(name = "follow_index1", columnList = "uid")
})
public class Follow implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;//主键

	@NotNull
	private Long uid;//用户账号

	@NotNull
	private Long followUid;//被关注的用户账号
	
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

	public Long getFollowUid() {
		return followUid;
	}

	public void setFollowUid(Long followUid) {
		this.followUid = followUid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
