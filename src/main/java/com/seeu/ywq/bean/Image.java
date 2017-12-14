package com.seeu.ywq.bean;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 相册表
 * @author Scary
 *
 */
@Table(name = "image",indexes = {
		@Index(name = "image_index1",columnList = "publish_id")
})
public class Image implements Serializable {
	public static enum IMAGE_TYPE{
		open,
		close
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;//主键

	@Column(name = "publish_id")
	private String publishId;//发布表id
	
	private String albumId;//相册表id

	@Enumerated
	private IMAGE_TYPE type;//图片类型，2表示公开，3表示私密，与对应的相册的类型应该一致
		
	private String imageUrl;//图片地址
		
	private String thumbImageUrl;//缩略图片地址
		
	private Date createTime;//创建时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPublishId() {
		return publishId;
	}

	public void setPublishId(String publishId) {
		this.publishId = publishId;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public IMAGE_TYPE getType() {
		return type;
	}

	public void setType(IMAGE_TYPE type) {
		this.type = type;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getThumbImageUrl() {
		return thumbImageUrl;
	}

	public void setThumbImageUrl(String thumbImageUrl) {
		this.thumbImageUrl = thumbImageUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
