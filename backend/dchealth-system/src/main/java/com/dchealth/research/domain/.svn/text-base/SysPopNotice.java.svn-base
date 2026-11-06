package com.dchealth.research.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 消息弹框对象 sys_pop_notice
 * 
 * @author lchbu
 * @date 2024-01-23
 */
@Data
public class SysPopNotice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 队列id */
    @Excel(name = "队列id")
    private Long researchId;

    /** 队列名称 */
    @Excel(name = "队列名称")
    private String researchName;

    /** 患者id */
    @Excel(name = "患者id")
    private String userId;

    /**
     * 0未读, 1已读
     */
    private Integer isread;

    /** 队列介绍 */
    @Excel(name = "队列介绍")
    private String researchDescription;


}
