package com.dchealth.cro.core.domain;

import java.util.Date;

import com.dchealth.cro.common.annotation.Excel;

import lombok.Data;

/**
 * 应用设置对象 app_set
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-30
 */
@Data
public class AppSet
{

    /** null */
    private Long id;

    /** null */
    @Excel(name = "null")
    private Long appId;

    /** 配置键 */
    @Excel(name = "配置键")
    private String configKey;

    /** 配置值 */
    @Excel(name = "配置值")
    private String configValue;
    private Date createTime;
    private Date updateTime;
    /**
     * 消息发送的App类型 1 医生端 2 患者端
     */
    private Integer appType;

   
}
