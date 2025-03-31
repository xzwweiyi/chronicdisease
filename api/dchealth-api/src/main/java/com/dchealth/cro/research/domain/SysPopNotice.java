package com.dchealth.cro.research.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 消息弹框对象 sys_pop_notice
 * 
 * @author lchbu
 * @date 2024-01-23
 */
@Data
public class SysPopNotice
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 队列id */
    private Long researchId;

    /** 队列名称 */
    private String researchName;

    /** 患者id */
    private String userId;

    private Date createTime;
    private Date updateTime;

    private String remark;
    private String createBy;
    private String updateBy;

    private Integer isread;

    /**
     * 队列介绍
     */
    private String researchDescription;

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }
}
