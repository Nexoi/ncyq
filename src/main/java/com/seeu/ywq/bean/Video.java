package com.seeu.ywq.bean;

import java.io.Serializable;

/**
 * 视频信息表
 * @author zho
 *
 */
public class Video implements Serializable {

	private Long id;//主键
	
	private String releaseId;//发布id
	
	private String albumId;//相册id
		
	private String type;//图片类型，2表示公开，3表示私密，与对应的相册的类型应该一致
	
	private String videoId;//阿里云上的视频ID
	
	private String createTime;//创建时间
	
	private String state;//状态，0表示正常，1表示封禁

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReleaseId() {
		return releaseId;
	}

	public void setReleaseId(String releaseId) {
		this.releaseId = releaseId;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}
