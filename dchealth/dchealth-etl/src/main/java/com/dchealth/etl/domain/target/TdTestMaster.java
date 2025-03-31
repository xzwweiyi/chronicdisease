package com.dchealth.etl.domain.target;

import com.dchealth.etl.domain.BaseEntity;

import java.util.Date;
import java.io.Serializable;

/**
 * 检验主记录(TdTestMaster)实体类
 *
 * @author makejava
 * @since 2023-03-30 11:23:00
 */
public class TdTestMaster extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -47059270817749806L;
    
    private Integer id;
    /**
     * 身份证号码
     */
    private String idCardNo;
    /**
     * 检验序号
     */
    private String testNo;
    /**
     * 患者Id
     */
    private String patientId;
    /**
     * 检验时间
     */
    private Date testDateTime;
     /**
     * 检验日期
     */
    private Date testDate;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 申请医生
     */
    private String orderingProvider;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getTestNo() {
        return testNo;
    }

    public void setTestNo(String testNo) {
        this.testNo = testNo;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Date getTestDateTime() {
        return testDateTime;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setTestDateTime(Date testDateTime) {
        this.testDateTime = testDateTime;
    }

    public String getOrderingProvider() {
        return orderingProvider;
    }

    public void setOrderingProvider(String orderingProvider) {
        this.orderingProvider = orderingProvider;
    }
}

