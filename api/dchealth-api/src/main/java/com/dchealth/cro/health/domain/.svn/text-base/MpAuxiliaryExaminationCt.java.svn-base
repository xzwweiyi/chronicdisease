package com.dchealth.cro.health.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 唐都-辅助检查-ct项目相关对象 mp_auxiliary_examination_ct
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpAuxiliaryExaminationCt 
{

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

    /** ct检查 */
    private String ct;

    /** pet—ct检查 */
    private String petCt;

    /** 头颅平扫ct（轴位） */
    private String headPlainScanCt;

    /** 胸部平扫ct（轴位+冠位） */
    private String chestPlainScanCt;

    /** 胸部平扫ct+薄层扫描+肺结节 */
    private String clpCt;

    /** 上腹部ct平扫（肝胆胰脾） */
    private String upperAbdominalCtPs;

    /** 下腹部ct平扫（轴位+冠位） */
    private String lowerAbdominalCtPs;

    /** 上腹部ct增强 */
    private String upperAbdominalCt;

    /** 下腹部ct增强 */
    private String lowerAbdominalCt;

    /** 上腹部+下腹部ct增强 */
    private String ulAbdominalCt;

    /** 盆腔ct增强 */
    private String pelvicCt;

    /** 全身骨静态显像 */
    private String boneStaticImaging;

    /** 全身pet/mr */
    private String petMr;

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

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;
}
