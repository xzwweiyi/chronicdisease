package com.dchealth.cro.health.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 用药信息对象 mp_drug_exposure
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpDrugExposure
{
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

    /** 医疗机构id */
    private String careSiteId;

    /** 患者id */
    private String personId;

    /** 就诊id */
    private String visitOccurrenceId;

    /** 处方id */
    private String drugExposureId;

    /** 药品类型 */
    private String drugType;

    /** 药品sctid */
    private Long drugSctid;

    /** 药品名称 */
    private String drugValue;

    /** 剂型名称 */
    private String dosageForm;

    /** 用药时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date drugExposureStartDate;

    /** 用药天数 */
    private Long daysSupply;

    /** 开单科室名称 */
    private String departmentName;

   
}
