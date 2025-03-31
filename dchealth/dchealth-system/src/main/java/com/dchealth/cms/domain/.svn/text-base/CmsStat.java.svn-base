package com.dchealth.cms.domain;

import java.util.Map;

import com.dchealth.common.annotation.Excel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 【请填写功能名称】对象 cms_stat
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class CmsStat 
{

    /** 主键 */
    private Long id;

    /** 查看数 */
    @Excel(name = "查看数")
    private Long viewNum;

    /** 收藏数 */
    @Excel(name = "收藏数")
    private Long collectNum;

    /** 评论数 */
    @Excel(name = "评论数")
    private Long commentNum;

    @Excel(name = "资讯标题")
    private String title;

    private String tenantId;
   

    /** 请求参数 */
    private Map<String, Object> params;

   
}
