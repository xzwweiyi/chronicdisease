package com.dchealth.compatibility.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 唐都-辅助检查-核磁项目相关对象 mp_auxiliary_examination_nmr
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpAuxiliaryExaminationNmr {
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

    /** mri检查 */
    private String mri;

    /** 头颅血管成像 */
    private String cranialVascularImaging;

    /** 头颅静脉血管成像mrv */
    private String cranialVenousMrv;

    /** 头颅动脉血管成像mra */
    private String cranialArteryMra;

    /** 心脏冠脉血管成像 */
    private String cardiacCoronaryAngiography;

    /** mr1.5t平扫头颅 */
    private String headFlatScan15;

    /** mr1.5t平扫颈椎 */
    private String csFlatScan15;

    /** mr1.5t平扫腰椎 */
    private String lsFlatScan15;

    /** mr3t平扫头颅 */
    private String headFlatScan3;

    /** mr3t平扫颈椎 */
    private String csFlatScan3;

    /** mr3t平扫腰椎 */
    private String lsFlatScan3;

    /** 磁共振弥散成像（dwi） */
    private String dwi;

    /** 磁共振功能成像（3d-asl) */
    private String asl;

    /** 胸透 */
    private String chestXray;

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

    /** 报告类型1健康, 2体检 */
    private String reportType;

    
}
