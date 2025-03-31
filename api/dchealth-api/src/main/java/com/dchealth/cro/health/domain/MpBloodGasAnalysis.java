package com.dchealth.cro.health.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 血气分析对象 mp_blood_gas_analysis
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpBloodGasAnalysis {
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

    /** ph值 */
    private String bloodPh;

    /** 动脉血二氧化碳分压 */
    private String bloodPco2;

    /** 动脉血氧分压 */
    private String bloodPo2;

    /** 红细胞压积 */
    private String bloodHct;

    /** 血红蛋白 */
    private String bloodHb;

    /** 剩余碱 */
    private String bloodBe;

    /** 标准碳酸根 */
    private String bloodSb;

    /** 实际碳酸根 */
    private String bloodAb;

    /** 总二氧化碳总量 */
    private String bloodTco2;

    /** 氧饱和度50%时氧分压 */
    private String bloodPo2P50;

    /** 血氧饱和度 */
    private String bloodSato2;

    /** 血氧含量 */
    private String bloodTo2;

    /** 氧容量 */
    private String bloodO2Cap;

}
