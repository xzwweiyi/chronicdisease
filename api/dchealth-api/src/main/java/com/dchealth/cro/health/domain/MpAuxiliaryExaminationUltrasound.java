package com.dchealth.cro.health.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 唐都-辅助检查-超声项目相关对象 mp_auxiliary_examination_ultrasound
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpAuxiliaryExaminationUltrasound {
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

    /** 胸片报告 */
    private String cr;

    /** x线（胸部以外） */
    private String crOther;

    /** 心脏超声 */
    private String cardiacUltrasound;

    /** 腹部超声 */
    private String abdominalUltrasound;

    /** 专科超声男 */
    private String specializedUltrasoundMale;

    /** 专科超声女 */
    private String specializedUltrasoundFemale;

    /** 甲状腺超声 */
    private String thyroidUltrasound;

    /** 颈动脉超声 */
    private String carotidUltrasound;

    /** 血管多普勒超声 */
    private String vascularDopplerUltrasound;

    /** 乳腺彩超 */
    private String biRads;

    /** 阴式彩超(女) */
    private String tvs;

    /** 子宫附件彩超 */
    private String uterineAppendagesUltrasound;

    /** 肝胆胰脾 */
    private String lgps;

    /** 肝胆胰脾肾 */
    private String lgpsk;

    /** 肝胆胰脾肾前列腺膀胱 */
    private String lgpskpb;

    /** 肝胆胰脾肾子宫附件 */
    private String lgpskua;

    /** 双侧乳腺(钼靶) */
    private String bilateralMammaryGland;

    /** 颈椎正位片 */
    private String cervicalSpine;

    /** 颈椎侧位片 */
    private String cervicalLateralFilm;

    /** 腰椎正位片 */
    private String lumbarSpine;

    /** 腰椎侧位片 */
    private String lumbarSpineFilm;

    /** 胸部正位片 */
    private String chestXray;

    /** 左膝关节正位 */
    private String leftKneeJointAp;

    /** 左膝关节侧位 */
    private String lkjlp;

    /** 右膝关节正位 */
    private String rightKneeJointAp;

    /** 右膝关节侧位 */
    private String rkjlp;

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
    private Long reportType;

    
}
