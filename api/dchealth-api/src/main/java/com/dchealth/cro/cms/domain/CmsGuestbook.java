package com.dchealth.cro.cms.domain;

import java.util.Date;

import lombok.Data;

/**
 * 留言板对象 cms_guestbook
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-06
 */
@Data
public class CmsGuestbook 
{

    /** 编号 */
    private Long id;


    /** 留言内容 */
    private String content;

    /** 姓名 */
    private String userName;

    /** 电话 */
    private String mobile;


    /** 留言时间 */
    private Date createTime;

    /** 删除标记 */
    private String delFlag;
    
    private Long accountId;
    
    private String tenantId;
    
    private String email;

}
