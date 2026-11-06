package com.dchealth.etl.domain.check;

import com.dchealth.etl.domain.BaseEntity;

import java.io.Serializable;

/**
 * 化验结果表（HYB）(Hyb)实体类
 *
 * @author makejava
 * @since 2023-03-27 10:46:29
 */
public class Hyb extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 745320538738340954L;
    
    private Integer rowid;
    
    private String studyid;
    /**
     * 收费项目代码
     */
    private String sfxmdm;
    /**
     * 项目代码
     */
    private String xxdm;
    /**
     * 化验日期
     */
    private String checkdate;
    /**
     * 化验时间
     */
    private String checktime;
    /**
     * 化验结果
     */
    private String itemresult;
    /**
     * 单位
     */
    private String itemunit;
    /**
     * 抽样号
     */
    private Integer sampleno;
    /**
     * 高低标志
     */
    private Integer flag;
    /**
     * 参考值
     */
    private String defvalue;
    /**
     * 检验医生
     */
    private String doctor;
    /**
     * 打印序号
     */
    private String ordercode;
    /**
     * 传送标记
     */
    private String sendflag;
    /**
     * 审核医生
     */
    private String shdoctor;
    /**
     * 试验方法
     */
    private String checkmethod;
    /**
     * 试验材料
     */
    private String checkstuff;
    /**
     * 备注
     */
    private String bz;
    /**
     * 体检分部代码
     */
    private String deptcode;


    public Integer getRowid() {
        return rowid;
    }

    public void setRowid(Integer rowid) {
        this.rowid = rowid;
    }

    public String getStudyid() {
        return studyid;
    }

    public void setStudyid(String studyid) {
        this.studyid = studyid;
    }

    public String getSfxmdm() {
        return sfxmdm;
    }

    public void setSfxmdm(String sfxmdm) {
        this.sfxmdm = sfxmdm;
    }

    public String getXxdm() {
        return xxdm;
    }

    public void setXxdm(String xxdm) {
        this.xxdm = xxdm;
    }

    public String getCheckdate() {
        return checkdate;
    }

    public void setCheckdate(String checkdate) {
        this.checkdate = checkdate;
    }

    public String getChecktime() {
        return checktime;
    }

    public void setChecktime(String checktime) {
        this.checktime = checktime;
    }

    public String getItemresult() {
        return itemresult;
    }

    public void setItemresult(String itemresult) {
        this.itemresult = itemresult;
    }

    public String getItemunit() {
        return itemunit;
    }

    public void setItemunit(String itemunit) {
        this.itemunit = itemunit;
    }

    public Integer getSampleno() {
        return sampleno;
    }

    public void setSampleno(Integer sampleno) {
        this.sampleno = sampleno;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getDefvalue() {
        return defvalue;
    }

    public void setDefvalue(String defvalue) {
        this.defvalue = defvalue;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }

    public String getSendflag() {
        return sendflag;
    }

    public void setSendflag(String sendflag) {
        this.sendflag = sendflag;
    }

    public String getShdoctor() {
        return shdoctor;
    }

    public void setShdoctor(String shdoctor) {
        this.shdoctor = shdoctor;
    }

    public String getCheckmethod() {
        return checkmethod;
    }

    public void setCheckmethod(String checkmethod) {
        this.checkmethod = checkmethod;
    }

    public String getCheckstuff() {
        return checkstuff;
    }

    public void setCheckstuff(String checkstuff) {
        this.checkstuff = checkstuff;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getDeptcode() {
        return deptcode;
    }

    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode;
    }

}

