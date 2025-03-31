package com.dchealth.compatibility.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 诊断记录对象 mp_diagnosis
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpDiagnosis {
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

    /** 病人标识/患者id */
    private String personId;

    /** 体检id/就诊id */
    private String visitOccurrenceId;

    /** 就诊类型 */
    private String visitType;

    /** 诊断类别 反映入院、出院、门诊、病理诊断等 */
    private String diagnosisType;

    /** 诊断序号 依重要次序，由小到大排列 */
    private String diagnosisNo;

    /** 诊断描述、终检结论 */
    private String diagnosisDesc;

    /** 诊断日期、终检日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date diagnosisDate;

    /** 终检医生 */
    private String finalCheckDoctor;

    /** 检查结果 */
    private String checkDesc;

    /** 结论医生 */
    private String checkDoctor;

    /** 结论日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkDate;


    /** 诊断日期和终检日期的日期格式 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date resultDate;

    /** 医疗机构id */
    private String careSiteId;

    /** 诊断编码 */
    private String conditionCode;

    /** 诊断名称 */
    private String conditionName;

    /** 诊断类别（主诊断，其他诊断） */
    private String conditionClass;

    /** 诊断科室名称 */
    private String departmentName;

    /** 是否新发肿瘤 */
    private String isNewTumor;

    /** 是否实施过临床路径 */
    private String isClinicalPath;

    /** 肿瘤分期等级 */
    private String tumourStageLevel;

    /** 肿瘤分期类型 */
    private String tumourStageType;

    /** t分期 */
    private String tStages;

    /** n分期 */
    private String nStages;

    /** m分期 */
    private String mStages;

    /** tnm综合分期 */
    private String tnmStages;

    /** 是否手术病人 */
    private String isOperationPerson;

    /** 是否微创手术病例 */
    private String isOperationMini;

  
}
