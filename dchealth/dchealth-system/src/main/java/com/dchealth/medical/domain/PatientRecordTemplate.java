package com.dchealth.medical.domain;

import java.util.Date;
import java.util.List;

import com.dchealth.template.domain.FMedicalForm;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 周期配置对象 patient_record_template
 * 
 * @author lchbu
 * @date 2022-08-23
 */
@Data
public class PatientRecordTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long followId;

    /** 研究项目ID */
    @Excel(name = "研究项目ID")
    private Long researchId;

    /** 随访开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "随访开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date followStartDate;

    /** 随访结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "随访结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date followEndDate;

    /** 与上一个次随访间隔数 */
    @Excel(name = "与上一个次随访间隔数")
    private Integer followInterval;

    /** 与上一个次随访间隔单位（1-天，2-周，3-月） */
    @Excel(name = "与上一个次随访间隔单位", readConverterExp = "1=-天，2-周，3-月")
    private String followIntervalUnit;

    /** 随访频率 1-限制次数  2-不限次数 */
    @Excel(name = "随访频率 1-限制次数  2-不限次数")
    private String followFrequency;

    /** 当前节点对应周期配置的随访总次数 */
    @Excel(name = "随访总次数")
    private Integer followTotal;

    /** 第几次随访
     * 第几次随访，基线是0，随访从1开始
     * */
    @Excel(name = "第几次随访")
    private Integer followNum;

    /** 随访名称 */
    @Excel(name = "随访名称")
    private String followName;

    /** 随访对应的模板信息 */
    private List<FMedicalForm> fMedicalForms;

}
