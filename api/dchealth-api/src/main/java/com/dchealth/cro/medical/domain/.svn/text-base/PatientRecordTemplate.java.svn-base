package com.dchealth.cro.medical.domain;

import com.dchealth.cro.common.base.BaseEntity;
import com.dchealth.cro.form.domain.FMedicalForm;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 周期配置对象 patient_record_template
 * 
 * @author lchbu
 * @date 2022-08-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class PatientRecordTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long followId;

    /** 研究项目ID */
    private Long researchId;

    /** 随访开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date followStartDate;

    /** 随访结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date followEndDate;

    /** 与上一个次随访间隔数 */
    private Integer followInterval;

    /** 与上一个次随访间隔单位（1-天，2-周，3-月） */
    private String followIntervalUnit;

    /** 随访频率 1-限制次数  2-不限次数 */
    private String followFrequency;

    /** 随访总次数 */
    private Integer followTotal;

    /** 第几次随访 */
    private Integer followNum;

    /** 随访名称 */
    private String followName;

    /** 随访对应的模板信息 */
    private List<FMedicalForm> fMedicalForms;

}
