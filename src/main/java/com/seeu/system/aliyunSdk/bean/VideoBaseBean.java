package com.seeu.system.aliyunSdk.bean;

public class VideoBaseBean {

	private String VideoId;
	
	private String Title;
	
	private String Duration;
	
	private String CoverURL;
	
	private String Status;
	
	private String MediaType;
	
	private String CreationTime;

	
	
	public String getVideoId() {
		return VideoId;
	}

	public void setVideoId(String videoId) {
		VideoId = videoId;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDuration() {
		return Duration;
	}

	public void setDuration(String duration) {
		Duration = duration;
	}

	public String getCoverURL() {
		return CoverURL;
	}

	public void setCoverURL(String coverURL) {
		CoverURL = coverURL;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getMediaType() {
		return MediaType;
	}

	public void setMediaType(String mediaType) {
		MediaType = mediaType;
	}

	public String getCreationTime() {
		return CreationTime;
	}

	public void setCreationTime(String creationTime) {
		CreationTime = creationTime;
	}
	
}
