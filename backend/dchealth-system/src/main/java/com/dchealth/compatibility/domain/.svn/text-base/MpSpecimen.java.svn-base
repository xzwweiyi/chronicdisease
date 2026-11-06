package com.dchealth.compatibility.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 病理信息对象 mp_specimen
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpSpecimen {
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

    /** 对应检查名称 */
    private String technologyName;

    /** 病理id */
    private Long specimenRecordId;

    /** 病理样本名称 */
    private String specimenSourceValue;

    /** 肉眼所见 */
    private String nakedEyeView;

    /** 镜下所见 */
    private String microscopicView;

    /** 病理诊断 */
    private String pathologicDiagnosis;

    /** 报告日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reportDate;

  
}
