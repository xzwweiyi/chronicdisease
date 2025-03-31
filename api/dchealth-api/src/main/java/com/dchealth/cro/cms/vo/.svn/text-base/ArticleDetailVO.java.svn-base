package com.dchealth.cro.cms.vo;

import java.util.List;

import lombok.Data;

@Data
public class ArticleDetailVO {

	private Long articleId;
	private String imageUrl;// 图片地址
	/** 标题 */
	private String title;// 标题

	private String createTime;

	/** 文章内容 */
	private String content;// 文章内容
	
	private String copyFrom;// 文章来源
	
	private List<ArticleRelationVO> relationArticles;//相关资讯
	private String isCollection = "N"; //Y是 N否
	
	private String videoImage;
	private String videoUrl;
	private Integer commentNum;
	private Integer viewNum;

	//上传方式1本地上传2转发3云视频
	private String uploadType;
	//转发路径
	private String forwardLink;

}
