package com.dchealth.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 便常规对象 mp_stool_routine_col
 * 
 * @author lchbu
 * @date 2024-06-03
 */
public class MpStoolRoutineCol extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 自增主键 */
    private Long id;

    /** 医疗机构id */
    @Excel(name = "医疗机构id")
    private String careSiteId;

    /** 患者id */
    @Excel(name = "患者id")
    private String personId;

    /** 就诊id */
    @Excel(name = "就诊id")
    private String visitOccurrenceId;

    /** 就诊类型名称 */
    @Excel(name = "就诊类型名称")
    private String visitType;

    /** 报告时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报告时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date resultTime;

    /** 数据提取日期 */
    @Excel(name = "数据提取日期")
    private String insertDate;

    /** 提示 */
    @Excel(name = "提示")
    private String tip;

    /** 单位 */
    @Excel(name = "单位")
    private String unit;

    /** 最小值 */
    @Excel(name = "最小值")
    private String minValue;

    /** 最大值 */
    @Excel(name = "最大值")
    private String maxValue;

    /** 检查结果 */
    @Excel(name = "检查结果")
    private String resultValue;

    /** 检验项 */
    @Excel(name = "检验项")
    private String measurementSourceValue;

    /** 检验类型 */
    @Excel(name = "检验类型")
    private String measurementType;
    /** 参考值范围 */
    @Excel(name = "参考值范围")
    private String rangeValue;

    public String getRangeValue() {
        return rangeValue;
    }

    public void setRangeValue(String rangeValue) {
        this.rangeValue = rangeValue;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCareSiteId(String careSiteId) 
    {
        this.careSiteId = careSiteId;
    }

    public String getCareSiteId() 
    {
        return careSiteId;
    }
    public void setPersonId(String personId) 
    {
        this.personId = personId;
    }

    public String getPersonId() 
    {
        return personId;
    }
    public void setVisitOccurrenceId(String visitOccurrenceId) 
    {
        this.visitOccurrenceId = visitOccurrenceId;
    }

    public String getVisitOccurrenceId() 
    {
        return visitOccurrenceId;
    }
    public void setVisitType(String visitType) 
    {
        this.visitType = visitType;
    }

    public String getVisitType() 
    {
        return visitType;
    }
    public void setResultTime(Date resultTime) 
    {
        this.resultTime = resultTime;
    }

    public Date getResultTime() 
    {
        return resultTime;
    }
    public void setInsertDate(String insertDate) 
    {
        this.insertDate = insertDate;
    }

    public String getInsertDate() 
    {
        return insertDate;
    }
    public void setTip(String tip) 
    {
        this.tip = tip;
    }

    public String getTip() 
    {
        return tip;
    }
    public void setUnit(String unit) 
    {
        this.unit = unit;
    }

    public String getUnit() 
    {
        return unit;
    }
    public void setMinValue(String minValue) 
    {
        this.minValue = minValue;
    }

    public String getMinValue() 
    {
        return minValue;
    }
    public void setMaxValue(String maxValue) 
    {
        this.maxValue = maxValue;
    }

    public String getMaxValue() 
    {
        return maxValue;
    }
    public void setResultValue(String resultValue) 
    {
        this.resultValue = resultValue;
    }

    public String getResultValue() 
    {
        return resultValue;
    }
    public void setMeasurementSourceValue(String measurementSourceValue) 
    {
        this.measurementSourceValue = measurementSourceValue;
    }

    public String getMeasurementSourceValue() 
    {
        return measurementSourceValue;
    }
    public void setMeasurementType(String measurementType) 
    {
        this.measurementType = measurementType;
    }

    public String getMeasurementType() 
    {
        return measurementType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("careSiteId", getCareSiteId())
            .append("personId", getPersonId())
            .append("visitOccurrenceId", getVisitOccurrenceId())
            .append("visitType", getVisitType())
            .append("resultTime", getResultTime())
            .append("insertDate", getInsertDate())
            .append("tip", getTip())
            .append("unit", getUnit())
            .append("minValue", getMinValue())
            .append("maxValue", getMaxValue())
            .append("resultValue", getResultValue())
            .append("measurementSourceValue", getMeasurementSourceValue())
            .append("measurementType", getMeasurementType())
            .toString();
    }
}
