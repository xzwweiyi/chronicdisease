package com.dchealth.cro.minprogram.domain;

import lombok.Data;

import java.util.Date;

/**
 * 患者小程序信息对象 patient_mininfo
 *
 * @author ruoyi
 * @date 2022-06-23
 */
@Data
public class PatientMinInfo
{
    private static final long serialVersionUID = 1L;

    /** 患者ID */
    private Long patientId;

    /** $column.columnComment */
    private Long appId;

    /** 患者头像 */
    private String userHead;

    /** OPEN_ID */
    private String openId;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;
}
