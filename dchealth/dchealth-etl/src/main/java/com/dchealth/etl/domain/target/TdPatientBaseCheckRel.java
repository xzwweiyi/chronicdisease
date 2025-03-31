package com.dchealth.etl.domain.target;

import java.util.Date;
import java.io.Serializable;

/**
 * 基础信息表和体检关系表(TdPatientBaseCheckRel)实体类
 *
 * @author makejava
 * @since 2023-04-06 17:24:12
 */
public class TdPatientBaseCheckRel implements Serializable {
    private static final long serialVersionUID = 449183008552639804L;
    
    private Integer id;
    /**
     * 身份证号
     */
    private String idCardNo;
    /**
     * 体检Id
     */
    private String studyId;
    /**
     * 检查日期
     */
    private Date studyDate;
    /**
     * 检查医生
     */
    private String examiningDoctor;
    /**
     * 审核医生
     */
    private String auditCoctor;
    /**
     * 体检分部代码
     */
    private String deptCode;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 体检报告日期
     */
    private Date checkDate;


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

    public String getStudyId() {
        return studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public Date getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(Date studyDate) {
        this.studyDate = studyDate;
    }

    public String getExaminingDoctor() {
        return examiningDoctor;
    }

    public void setExaminingDoctor(String examiningDoctor) {
        this.examiningDoctor = examiningDoctor;
    }

    public String getAuditCoctor() {
        return auditCoctor;
    }

    public void setAuditCoctor(String auditCoctor) {
        this.auditCoctor = auditCoctor;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
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

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

}

