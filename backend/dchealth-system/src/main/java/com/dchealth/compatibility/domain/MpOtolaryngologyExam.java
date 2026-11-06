package com.dchealth.compatibility.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 耳鼻喉科检查对象 mp_otolaryngology_exam
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpOtolaryngologyExam 
{

    /** 自增主键 */
    private Long id;

    /** 医疗机构id */
    private String careSiteId;

    /** 患者id */
    private String personId;

    /** 就诊id */
    private String visitOccurrenceId;

    /** 听力 */
    private String hearing;

    /** 耳廓 */
    private String auricle;

    /** 外耳道 */
    private String earExternalMeatus;

    /** 鼓膜 */
    private String earDrum;

    /** 外鼻 */
    private String noseExternal;

    /** 鼻腔 */
    private String noseCavity;

    /** 口咽 */
    private String oropharynx;

    /** 喉咽 */
    private String throat;

    /** 扁桃体 */
    private String tonsil;

    /** 体检日期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date healthExamDate;

    /** 结果日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date resultDate;

   
}
