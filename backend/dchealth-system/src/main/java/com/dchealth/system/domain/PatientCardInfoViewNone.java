package com.dchealth.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 增量患者号对象 patient_card_info_view_none
 * 
 * @author lchbu
 * @date 2024-11-23
 */
public class PatientCardInfoViewNone extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Excel(name = "id")
    private Long id;

    /** 身份证 */
    @Excel(name = "身份证")
    private String idCard;

    /** 患者id */
    @Excel(name = "患者id")
    private String personId;

    /** 1主患者号 0 不是主患者号 */
    private Integer patientMasterNo;

    /** 更新状态, 更新主号患者id 0 未处理, 1已更新 */
    private String updateStatus;

    /** 患者id生成时间 */
    private Date personIdGenerateTime;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 性别 */
    @Excel(name = "性别")
    private String sex;

    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dateOfBirth;

    /** 出生地 */
    @Excel(name = "出生地")
    private String birthPlace;

    /** 手机 */
    @Excel(name = "手机")
    private String phoneNumberHome;

    /** 电话 */
    @Excel(name = "电话")
    private String nextOfKinPhone;

    /** 最后访问 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后访问", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastVisitDate;

    /** 源数据库创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "源数据库创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createDate;

    public Date getSyncDate() {
        return syncDate;
    }

    public void setSyncDate(Date syncDate) {
        this.syncDate = syncDate;
    }

    /**
     * 同步日期
     */
    private Date syncDate;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setIdCard(String idCard) 
    {
        this.idCard = idCard;
    }

    public String getIdCard() 
    {
        return idCard;
    }
    public void setPersonId(String personId) 
    {
        this.personId = personId;
    }

    public String getPersonId() 
    {
        return personId;
    }
    public void setPatientMasterNo(Integer patientMasterNo) 
    {
        this.patientMasterNo = patientMasterNo;
    }

    public Integer getPatientMasterNo() 
    {
        return patientMasterNo;
    }
    public void setUpdateStatus(String updateStatus) 
    {
        this.updateStatus = updateStatus;
    }

    public String getUpdateStatus() 
    {
        return updateStatus;
    }
    public void setPersonIdGenerateTime(Date personIdGenerateTime) 
    {
        this.personIdGenerateTime = personIdGenerateTime;
    }

    public Date getPersonIdGenerateTime() 
    {
        return personIdGenerateTime;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setSex(String sex) 
    {
        this.sex = sex;
    }

    public String getSex() 
    {
        return sex;
    }
    public void setDateOfBirth(Date dateOfBirth) 
    {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getDateOfBirth() 
    {
        return dateOfBirth;
    }
    public void setBirthPlace(String birthPlace) 
    {
        this.birthPlace = birthPlace;
    }

    public String getBirthPlace() 
    {
        return birthPlace;
    }
    public void setPhoneNumberHome(String phoneNumberHome) 
    {
        this.phoneNumberHome = phoneNumberHome;
    }

    public String getPhoneNumberHome() 
    {
        return phoneNumberHome;
    }
    public void setNextOfKinPhone(String nextOfKinPhone) 
    {
        this.nextOfKinPhone = nextOfKinPhone;
    }

    public String getNextOfKinPhone() 
    {
        return nextOfKinPhone;
    }
    public void setLastVisitDate(Date lastVisitDate) 
    {
        this.lastVisitDate = lastVisitDate;
    }

    public Date getLastVisitDate() 
    {
        return lastVisitDate;
    }
    public void setCreateDate(Date createDate) 
    {
        this.createDate = createDate;
    }

    public Date getCreateDate() 
    {
        return createDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("idCard", getIdCard())
            .append("personId", getPersonId())
            .append("patientMasterNo", getPatientMasterNo())
            .append("updateStatus", getUpdateStatus())
            .append("personIdGenerateTime", getPersonIdGenerateTime())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("name", getName())
            .append("sex", getSex())
            .append("dateOfBirth", getDateOfBirth())
            .append("birthPlace", getBirthPlace())
            .append("phoneNumberHome", getPhoneNumberHome())
            .append("nextOfKinPhone", getNextOfKinPhone())
            .append("lastVisitDate", getLastVisitDate())
            .append("createDate", getCreateDate())
            .toString();
    }


}
