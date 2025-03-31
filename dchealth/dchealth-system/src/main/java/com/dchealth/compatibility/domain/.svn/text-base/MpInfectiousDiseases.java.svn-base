package com.dchealth.compatibility.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 常见传染病（代替乙肝）对象 mp_infectious_diseases
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpInfectiousDiseases {
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

    /** 乙肝表面抗原 */
    private String hbvSag;

    /** 乙肝表面抗体 */
    private String hbvSab;

    /** 乙肝e抗原 */
    private String hbvEag;

    /** 乙肝e抗体 */
    private String hbvEab;

    /** 乙肝核心抗体 */
    private String hbvCab;

    /** 乙型肝炎病毒dna(hbv-dna) */
    private String hbvDna;

    /** 丙型肝炎病毒dna(hcv-rna) */
    private String hcvRna;

    /** 丙肝抗体 */
    private String hcvAb;

    /** 艾滋病抗体 */
    private String hivAg;

    /** 梅毒抗体 */
    private String syphilisAb;

}
