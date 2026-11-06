package com.dchealth.compatibility.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 就诊信息对象 mp_visit_occurrence
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpVisitOccurrence {
	  /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;

    /** 自增住建 */
    private String visitOccurrenceId;

    /** 患者id */
    private String personId;

    /** 医疗机构id医疗机构id */
    private String careSiteId;

    /** 就诊类型名称, 就诊类型名称【diagnosis_type】 */
    private String visitType;

    /** 入院时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date visitStartDate;

    /** 出院日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date visitEndDate;

    /** 病区名称 */
    private String ward;

    /** 出院科室名称 */
    private String dischargeDepartment;

    /** 离院方式名称 */
    private String dischargeType;
    
    private Date summaryDate;//结论日期
    private String examSummary;//终检结论
    private String healthGuide;//体检提示及建议

   
}
