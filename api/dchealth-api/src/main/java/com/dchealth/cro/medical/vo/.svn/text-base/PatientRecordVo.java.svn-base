package com.dchealth.cro.medical.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 患者档案对象 vo
 * 
 * @author lchbu
 * @date 2022-09-15
 */
@Data
public class PatientRecordVo
{

    /** 档案id */
    private Long recordId;

    /** 患者ID */
    private Long patientId;

    /** 研究项目ID */
    private Long researchId;

    /** 随访节点id */
    private Long followId;

    /** $column.columnComment */
    private String name;

    /** 随访次数 */
    private Integer stageNum;

    /** 质疑状态0无质疑1待处理1进行中2已完成 */
    private String questionStatus;

    /** 预警状态0无预警1待处理2进行中3已完成 */
    private String warnStatus;

    /** 超窗状态0无超窗1待处理2完成 */
    private String overTimeStatus;

    /** 超窗处理状态0无1更新随访时间2忽略本次3失访 */
    private String overTimeHandleStatus;

    /** 填写时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fillTime;

    /** 随访时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date visitTime;

    /**
     * 入组时间
     */
    private Date enrollmentTime;

    /**
     * 锁定的随访时间
     */
    private Date lockVisitTime;

    /**
     * 患者名
     */
    private String patientName;

    /**
     * 随访名
     */
    private String followName;

    /**
     * edc状态0无状态1待处理2进行中3已完成
     */
    private String edcStatus;

    public PatientRecordVo(){}

}
