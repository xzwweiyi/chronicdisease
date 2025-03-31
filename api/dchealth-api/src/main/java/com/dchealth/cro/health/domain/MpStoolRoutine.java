package com.dchealth.cro.health.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 便常规对象 mp_stool_routine
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpStoolRoutine {
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

    /** 大便颜色 */
    private String fecesColour;

    /** 大便性状 */
    private String fecesCharacter;

    /** 便红细胞 */
    private String fecesRbc;

    /** 便白细胞 */
    private String fecesWbc;

    /** 便隐血 */
    private String fecesOb;

    /** 蛔虫卵 */
    private String fecesAscarid;

    /** 鞭虫卵 */
    private String fecesWhipworm;

    /** 蛲虫卵 */
    private String fecesPinworm;

    /** 绦虫卵 */
    private String fecesTapeworm;

  
}
