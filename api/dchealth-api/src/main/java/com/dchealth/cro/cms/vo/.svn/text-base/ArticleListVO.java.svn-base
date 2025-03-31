package com.dchealth.cro.cms.vo;

import lombok.Data;
@Data
public class ArticleListVO {
	private Long articleId;
	private String imageUrl;//图片地址
	   /** 标题 */
    private String title;//标题

    private String link;//链接

    //上传方式1本地上传2转发3云视频
    private String uploadType;
    //转发路径
    private String forwardLink;
    
    private String createTime;
    
    private String description;//描述、摘要
    
    private Integer viewNum;//浏览数

    private Integer commentNum;//评论数

    private Long score;//分数
//0待填写1进行中2已完成
    private String fillStatus;
    /** 患者填报记录id */
    private Long examRecordId;
    /**
     * 科室ID集合 [废弃]
     */
    private String deptIds;
    private Long deptId;

    /**
     *  队列ID
     */
    private Long researchId;
    /**
     * 患者ID
     */
    private Long accountId;
    /**
     * 是否通过再来一次的方式填写的问卷
     */
    private Boolean isFillAgain;

    private String type;
    private Long dataId;

    private String isCollection = "N"; //Y是 N否
    /**
     *  队列名称
     */
    private String projectName;
}
