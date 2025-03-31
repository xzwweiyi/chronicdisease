package com.dchealth.cro.health.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 糖耐量试验对象 mp_glucose_tolerance
 * 
 * @author lchbu
 * @date 2024-01-22
 */
public class MpGlucoseTolerance
{

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

    /** 孕周 */
    private String gestationalWeeks;

    /** 身高 */
    private String height;

    /** 体重 */
    private String weight;

    /** 葡萄糖在进餐之后 */
    private String eatAfter;

    /** 葡萄糖在进餐之后30分钟 */
    private String eatAfter30min;

    /** 葡萄糖在进餐之后1小时 */
    private String eatAfter1h;

    /** 葡萄糖在进餐之后2小时 */
    private String eatAfter2h;

    /** 葡萄糖在进餐之后3小时 */
    private String eatAfter3h;

    /** 葡萄糖在进餐之后4小时 */
    private String eatAfter4h;

    /** 葡萄糖在进餐之后5小时 */
    private String eatAfter5h;

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
    public void setGestationalWeeks(String gestationalWeeks) 
    {
        this.gestationalWeeks = gestationalWeeks;
    }

    public String getGestationalWeeks() 
    {
        return gestationalWeeks;
    }
    public void setHeight(String height) 
    {
        this.height = height;
    }

    public String getHeight() 
    {
        return height;
    }
    public void setWeight(String weight) 
    {
        this.weight = weight;
    }

    public String getWeight() 
    {
        return weight;
    }
    public void setEatAfter(String eatAfter) 
    {
        this.eatAfter = eatAfter;
    }

    public String getEatAfter() 
    {
        return eatAfter;
    }
    public void setEatAfter30min(String eatAfter30min) 
    {
        this.eatAfter30min = eatAfter30min;
    }

    public String getEatAfter30min() 
    {
        return eatAfter30min;
    }
    public void setEatAfter1h(String eatAfter1h) 
    {
        this.eatAfter1h = eatAfter1h;
    }

    public String getEatAfter1h() 
    {
        return eatAfter1h;
    }
    public void setEatAfter2h(String eatAfter2h) 
    {
        this.eatAfter2h = eatAfter2h;
    }

    public String getEatAfter2h() 
    {
        return eatAfter2h;
    }
    public void setEatAfter3h(String eatAfter3h) 
    {
        this.eatAfter3h = eatAfter3h;
    }

    public String getEatAfter3h() 
    {
        return eatAfter3h;
    }
    public void setEatAfter4h(String eatAfter4h) 
    {
        this.eatAfter4h = eatAfter4h;
    }

    public String getEatAfter4h() 
    {
        return eatAfter4h;
    }
    public void setEatAfter5h(String eatAfter5h) 
    {
        this.eatAfter5h = eatAfter5h;
    }

    public String getEatAfter5h() 
    {
        return eatAfter5h;
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
            .append("gestationalWeeks", getGestationalWeeks())
            .append("height", getHeight())
            .append("weight", getWeight())
            .append("eatAfter", getEatAfter())
            .append("eatAfter30min", getEatAfter30min())
            .append("eatAfter1h", getEatAfter1h())
            .append("eatAfter2h", getEatAfter2h())
            .append("eatAfter3h", getEatAfter3h())
            .append("eatAfter4h", getEatAfter4h())
            .append("eatAfter5h", getEatAfter5h())
            .toString();
    }
}
