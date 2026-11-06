package com.dchealth.compatibility.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用药信息对象 mp_drug_exposure
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
@Accessors(chain = true)
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
    private String drugSctid;

    /** 药品名称 */
    private String drugValue;

    /** 剂型名称 */
    private String dosageForm;

    /** 用药时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date drugExposureStartDate;

    /** 用药天数 */
    private Long daysSupply;

    /** 开单科室名称 */
    private String departmentName;


    /**
     * 就诊时间
     */
    private Date visitStartTime;

    /** 药品代码 */
    private String drugCode;

    /** 药品名称 */
    private String drugName;

    /** 反映药品的含量信息 */
    private String drugSpec;

    /** 如片、支等 */
    private String drugUnit;

    /** 如针剂、片剂等，使用规范描述 */
    private String drugForm;

    /** 按药品的毒理分类，如普通、毒麻、精神等 */
    private String pharmacologyType;

    /** 剂量的单位，如MG、ML等 */
    private String doseUnit;

    /** 反映是否药品及何类药品：1-西药2-中草药 3-中成药 5-辅料 6-试剂 9-其它 */
    private String drugMaterialType;

    /**
     * 就诊时间
     */
    private Date visitDate;

    /**
     * 订单科室名称
     */
    private String orderedByDept;

    /**
     * 订单医生名称
     */
    private String orderedByDoctor;



   
}
