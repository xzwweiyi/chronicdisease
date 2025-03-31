package com.dchealth.cro.health.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 激素测定对象 mp_hormone_items
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpHormoneItems {
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

    /** 卵泡刺激素（fsh） */
    private String hormoneFsh;

    /** 黄体生成素（lh） */
    private String hormoneLh;

    /** 雌二醇（e2） */
    private String hormoneE2;

    /** 孕酮（p） */
    private String hormoneP;

    /** 睾酮（t） */
    private String hormoneT;

    /** 催乳素（prl） */
    private String hormonePrl;

    
}
