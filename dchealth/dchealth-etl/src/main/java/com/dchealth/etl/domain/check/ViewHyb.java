package com.dchealth.etl.domain.check;

import com.dchealth.etl.domain.BaseEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * 检验结果(ViewHyb)实体类
 *
 * @author makejava
 * @since 2023-06-06 11:34:37
 */
public class ViewHyb extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 693218381198346617L;
    /**
     * 预约id
     */
    private String studyid;
    /**
     * 检查日期
     */
    private Date checkdate;
    /**
     * 结论日期
     */
    private Date resultdate;
    /**
     * 诊疗项目代码
     */
    private String zlxmdm;
    /**
     * 诊疗项目名称
     */
    private String zlxmmc;
    /**
     * 细项代码
     */
    private String xxdm;
    /**
     * 细项名称
     */
    private String xxmc;
    /**
     * 检查时间
     */
    private Date checktime;
    /**
     * 检查结果
     */
    private String itemresult;
    /**
     * 单位
     */
    private String itemunit;
    /**
     * 抽样号
     */
    private String sampleno;
    /**
     * 高低标记
     */
    private String flag;
    /**
     * 参考范围
     */
    private String defvalue;
    /**
     * 检查医生
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
     * 部门编码
     */
    private String deptcode;


    public String getStudyid() {
        return studyid;
    }

    public void setStudyid(String studyid) {
        this.studyid = studyid;
    }

    public Date getCheckdate() {
        return checkdate;
    }

    public void setCheckdate(Date checkdate) {
        this.checkdate = checkdate;
    }

    public Date getResultdate() {
        return resultdate;
    }

    public void setResultdate(Date resultdate) {
        this.resultdate = resultdate;
    }

    public String getZlxmdm() {
        return zlxmdm;
    }

    public void setZlxmdm(String zlxmdm) {
        this.zlxmdm = zlxmdm;
    }

    public String getZlxmmc() {
        return zlxmmc;
    }

    public void setZlxmmc(String zlxmmc) {
        this.zlxmmc = zlxmmc;
    }

    public String getXxdm() {
        return xxdm;
    }

    public void setXxdm(String xxdm) {
        this.xxdm = xxdm;
    }

    public String getXxmc() {
        return xxmc;
    }

    public void setXxmc(String xxmc) {
        this.xxmc = xxmc;
    }

    public Date getChecktime() {
        return checktime;
    }

    public void setChecktime(Date checktime) {
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

    public String getSampleno() {
        return sampleno;
    }

    public void setSampleno(String sampleno) {
        this.sampleno = sampleno;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
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

