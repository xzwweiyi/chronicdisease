package com.dchealth.cro.health.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 一般检查对象 mp_general_inspection
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpGeneralInspection {
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

    /** 就诊类型名称 */
    private String visitType;

    /** 报告时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date resultTime;

    /** 身高(cm) */
    private String height;

    /** 体重(kg) */
    private String weight;

    /** 体温℃ */
    private String bodyTemperature;

    /** 呼吸（次/分） */
    private String respiratoryRate;

    /** 脉搏（次/分） */
    private String pulseRate;

    /** 收缩压（mmhg） */
    private String systolicBloodPressure;

    /** 舒张压（mmhg） */
    private String diastolicBloodPressure;

    /** 尿量(ml) */
    private String urine;

    /** 结果日期 */
    private Date resultDate;
    
    private String age;
    private String vision;//视力(体检)
    private String waist;//腰围(体检)

   
}
