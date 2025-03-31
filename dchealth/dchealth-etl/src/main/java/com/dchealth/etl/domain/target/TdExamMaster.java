package com.dchealth.etl.domain.target;

import com.dchealth.etl.domain.BaseEntity;

import java.util.Date;
import java.io.Serializable;

/**
 * 检查主记录表(TdExamMaster)实体类
 *
 * @author makejava
 * @since 2023-03-29 18:04:02
 */
public class TdExamMaster extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 650512178792694355L;
    
    private Integer id;
    /**
     * 身份证号码
     */
    private String idCardNo;
    /**
     * 检查序号
     */
    private String examNo;
    /**
     * 患者Id
     */
    private String patientId;
    /**
     * 检查时间
     */
    private Date examDateTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 检查日期
     */
    private Date examDate;
    /**
     * 申请医生
     */
    private String reqPhysician;

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

    public String getExamNo() {
        return examNo;
    }

    public void setExamNo(String examNo) {
        this.examNo = examNo;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Date getExamDateTime() {
        return examDateTime;
    }

    public void setExamDateTime(Date examDateTime) {
        this.examDateTime = examDateTime;
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

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public String getReqPhysician() {
        return reqPhysician;
    }

    public void setReqPhysician(String reqPhysician) {
        this.reqPhysician = reqPhysician;
    }
}

