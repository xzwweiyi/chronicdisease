package com.dchealth.cro.warn.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 预警指导会话对象 warn_guide_conver
 *
 * @author lchbu
 * @date 2022-10-08
 */
@Data
public class WarnGuideConver
{
    private static final long serialVersionUID = 1L;

    /** 预警指导会话表 */
    private Long warnGuideConverId;

    /** 档案id */
    private Long recordId;

    /** 科室id */
    private Long deptId;

    /** $column.columnComment */
    private Long createUserId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
