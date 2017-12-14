package com.seeu.ywq.bean;

import java.io.Serializable;

/**
 * 发布标签中间表
 * @author zho
 *
 */
public class ReleaseLabel implements Serializable {
	
	private Long id;//主键
	
	private Long releaseId;//发布id
	
	private Long labelNo;//标签id

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReleaseId() {
		return releaseId;
	}

	public void setReleaseId(Long releaseId) {
		this.releaseId = releaseId;
	}

	public Long getLabelNo() {
		return labelNo;
	}

	public void setLabelNo(Long labelNo) {
		this.labelNo = labelNo;
	}


	
}
