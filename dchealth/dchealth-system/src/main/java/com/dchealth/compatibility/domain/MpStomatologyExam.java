package com.dchealth.compatibility.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 口腔科检查对象 mp_stomatology_exam
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpStomatologyExam {
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

    /** 牙体组织疾病 */
    private String dentalTissue;

    /** 牙周组织疾病 */
    private String dentalPeriTissue;

    /** 口腔黏膜疾病 */
    private String oralMucosa;

    /** 口腔颌面外科疾病 */
    private String oralMaxillofacial;

    /** 口腔修复科 */
    private String prosthodontics;

    /** 正畸科 */
    private String orthodontics;

    /** 体检日期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date healthExamDate;

    /** 结果日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date resultDate;

  
}
