package com.seeu.ywq.bean;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 发布信息表
 * @author Scary
 *
 */
//@Table(name = "publish",indexes = {
//		@Index(name = "publish_id",columnList = "uid")
//})
//@Entity
public class Publish implements Serializable {
	public static enum PUBLISH_TYPE{
		word,
		picture,
		video,
		shot
	}
	public static enum PUBLIC_STATUS{
		ok,
		limit
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;//主键
	
	private Long uid;//用户账号

	@Enumerated
	private PUBLISH_TYPE type;//发布类型，1表示文字，2表示图片，3表示视屏，4表示拍摄
		
	private String title;//标题
		
	private Date createTime;//创建时间
	
	private String unlockPrice;//解锁需要金额

	private String unlockUserNo;//解锁过此信息的用户号(逗号隔开)

	@Enumerated
	private PUBLIC_STATUS state;//状态，0表示正常，1表示封禁
	
	private int messageNum;//留言数(不与数据库做交互)
	
	private String label;//标签(不与数据库做交互)
	
	private int likeNum;//点赞数(不与数据库做交互)
	
	private List<Image> images;//图片(不与数据库做交互)

	private String coverImageUrl;//动态广场中动态封面图片(不与数据库做交互)

	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "publish_id")
	private List<PublishOpenImage> OpenImages;//公开图片(不与数据库做交互)

	
	private List<Image> privateImages;//私密图片(不与数据库做交互)
	
	private List<String> videoUrls;//
	
}
