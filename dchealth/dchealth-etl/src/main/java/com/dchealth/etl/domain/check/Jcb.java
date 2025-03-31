package com.dchealth.etl.domain.check;

import com.dchealth.etl.domain.BaseEntity;

import java.io.Serializable;

/**
 * 检查表(Jcb)实体类
 *
 * @author makejava
 * @since 2023-03-22 17:25:00
 */
public class Jcb extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -82713222557617168L;
    
    private String studyid;
    /**
     * 检查日期
     */
    private String studydate;
    /**
     * 明细项目代码
     */
    private String xxdm;
    /**
     * 字符型值
     */
    private String cvalue;
    /**
     * 数字型值
     */
    private String nvalue;
    /**
     * 检查医生
     */
    private String doctor;
    /**
     * 审核医生
     */
    private String shdoctor;
    /**
     * 体检分部代码
     */
    private String deptcode;
    
    private Integer id;
    
    private String dvalue;
    
    private String mvalue;
    
    private String doctordm;


    public String getStudyid() {
        return studyid;
    }

    public void setStudyid(String studyid) {
        this.studyid = studyid;
    }

    public String getStudydate() {
        return studydate;
    }

    public void setStudydate(String studydate) {
        this.studydate = studydate;
    }

    public String getXxdm() {
        return xxdm;
    }

    public void setXxdm(String xxdm) {
        this.xxdm = xxdm;
    }

    public String getCvalue() {
        return cvalue;
    }

    public void setCvalue(String cvalue) {
        this.cvalue = cvalue;
    }

    public String getNvalue() {
        return nvalue;
    }

    public void setNvalue(String nvalue) {
        this.nvalue = nvalue;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getShdoctor() {
        return shdoctor;
    }

    public void setShdoctor(String shdoctor) {
        this.shdoctor = shdoctor;
    }

    public String getDeptcode() {
        return deptcode;
    }

    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDvalue() {
        return dvalue;
    }

    public void setDvalue(String dvalue) {
        this.dvalue = dvalue;
    }

    public String getMvalue() {
        return mvalue;
    }

    public void setMvalue(String mvalue) {
        this.mvalue = mvalue;
    }

    public String getDoctordm() {
        return doctordm;
    }

    public void setDoctordm(String doctordm) {
        this.doctordm = doctordm;
    }

}

