package com.dchealth.compatibility.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 外科检查对象 mp_surgical_exam
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpSurgicalExam {
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

    /** 皮肤 */
    private String skin;

    /** 肛门 */
    private String anus;

    /** 表浅淋巴结 */
    private String superficialLynph;

    /** 甲状腺 */
    private String thyroid;

    /** 脊柱 */
    private String spine;

    /** 四肢关节 */
    private String limbJoint;

    /** 体检日期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date healthExamDate;

    /** 结果日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date resultDate;

   
}
