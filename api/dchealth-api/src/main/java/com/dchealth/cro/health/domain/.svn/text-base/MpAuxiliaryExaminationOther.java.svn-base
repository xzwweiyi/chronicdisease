package com.dchealth.cro.health.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 唐都-辅助检查-心电图等其他项目对象 mp_auxiliary_examination_other
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpAuxiliaryExaminationOther {
	  /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;

    /** 自增主键 */
    private Long id;

    /** 体检申请id */
    private String visitOccurrenceId;

    /** 病人标识id */
    private String personId;

    /** 健康档案检查申请号 */
    private String examNo;

    /** 健康档案检验申请号 */
    private String testNo;

    /** 报告时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date resultTime;

    /** 心电图 */
    private String ecg;

    /** 普通肠镜 */
    private String conventionalEnteroscope;

    /** 无痛胃镜 */
    private String painlessGastroscope;

    /** 无痛胃肠镜 */
    private String painlessGastroenteroscopy;

    /** 浅表器官（包块） */
    private String superficialOrgan;

    /** 骨密度测定（全身骨密） */
    private String boneMineralDensity;

    /** c13呼气试验检测 */
    private String c13BreathTest;

    /** 普通胃镜 */
    private String generalGastroscope;

    /** 无痛肠镜 */
    private String painlessColonoscopy;

    /** 无痛胃肠镜加 */
    private String painlessGastroscopyPlus;

    /** 其他检查项,包括检查类别technology_group: 2,4,5,6,9,10,11,12,13,15 */
    private String otherExaminationItem;

    /** 医疗机构id */
    private String careSiteId;

    /** 检查类别 */
    private String technologyGroup;

    /** 身体部位【body_position_dict】 */
    private String positionSctid;

    /** 检查部位名称 */
    private String position;

    /** 检查方式（增强，平扫） */
    private String testMethod;

    /** 检查报告结果--主观提示 */
    private String subjectiveHints;

    /** 检查诊断 */
    private String relevantClinicDiagnosis;

    /** 检查项目名称 */
    private String technologyName;

    /** 报告类型1健康, 2体检 */
    private Long reportType;

 
}
