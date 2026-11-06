package com.dchealth.compatibility.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 尿常规对象 mp_urine_routine
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpUrineRoutine {
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

    /** 尿比重 */
    private String urineSg;

    /** 尿酸碱度 */
    private String urinePh;

    /** 尿白细胞 */
    private String urineWbcCount;

    /** 尿红细胞 */
    private String urineRbcCount;

    /** 尿亚硝酸盐 */
    private String urineNit;

    /** 尿蛋白质 */
    private String urinePro;

    /** 尿糖 */
    private String urineGlu;

    /** 尿酮体 */
    private String urineKet;

    /** 尿胆原 */
    private String urineUro;

    /** 胆红素 */
    private String urineBil;

    /** 尿隐血 */
    private String urineBlo;

}
