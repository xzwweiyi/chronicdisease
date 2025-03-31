package com.dchealth.cro.health.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 凝血功能对象 mp_coagulation_function
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpCoagulationFunction {
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

    /** 凝血酶原时间 */
    private String coagulationPt;

    /** 活化部分凝血活酶时间 */
    private String coagulationAptt;

    /** 纤维蛋白原含量 */
    private String coagulationFib;

    /** 凝血酶时间 */
    private String coagulationTt;

    /** 纤维蛋白(原)降解产物 */
    private String coagulationFdp;

    /** d-二聚体定量 */
    private String coagulationDdimer;

    /** 国际标准化比值 */
    private String coagulationInr;

    /** 凝血酶原活动度 */
    private String coagulationPta;

 
}
