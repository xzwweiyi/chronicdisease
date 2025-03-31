package com.dchealth.cro.cms.vo;

import java.util.Date;

import lombok.Data;

@Data
public class CommentListVO {
	private String realName;
	private String userHead;
	private String content;
	private Date createTime;
    private String showTime;
    private Long commentId;
	private String appType;
    private boolean isShow = true;
    private Long accountId;


}
