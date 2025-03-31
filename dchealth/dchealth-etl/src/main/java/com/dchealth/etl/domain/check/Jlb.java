package com.dchealth.etl.domain.check;

import com.dchealth.etl.domain.BaseEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * 结论表(Jlb)实体类
 *
 * @author makejava
 * @since 2023-03-22 17:46:18
 */
public class Jlb extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 584232622770019307L;
    
    private String studyid;
    /**
     * 结论代码
     */
    private String jldm;
    /**
     * 检查结果
     */
    private String jcjg;
    /**
     * 检查结果续
     */
    private String jcjgx;
    /**
     * 终检结论
     */
    private String zjjl;
    /**
     * 结论日期
     */
    private Date jlrq;
    /**
     * 结论医生
     */
    private String jlys;
    /**
     * 提示医疗保健建议
     */
    private String ylbj;
    /**
     * 提示健康饮食注意事项
     */
    private String jcys;
    /**
     * 提示日常运动、体育锻炼建议
     */
    private String tydl;
    /**
     * 提示治疗建议
     */
    private String zljy;
    /**
     * 终检医生
     */
    private String zjys;
    /**
     * 终检日期
     */
    private Date zjrq;
    /**
     * 评估结果 维特奥用—0=健康 1=基本健康 2=不健康
     */
    private String pgjg;
    /**
     * 职业病结论
     */
    private String zyzjjl;
    /**
     * 职业病建议
     */
    private String zyylbj;
    /**
     * 健康处方名
     */
    private String jkzjjl;
    /**
     * 健康处方描述
     */
    private String jkylbj;
    /**
     * 中医体质
     */
    private String chylbj;
    /**
     * 对比科室
     */
    private String compareks;
    /**
     * 对比时间1
     */
    private String compareid1;
    /**
     * 对比时间2
     */
    private String compareid2;
    /**
     * 对比时间3
     */
    private String compareid3;
    /**
     * 体检分部代码
     */
    private String deptcode;
    
    private String jkxc;
    
    private Integer smsReport;
    
    private String fhys;
    
    private String fhrq;
    
    private String repGrade;
    
    private String checkys;
    
    private String shys;
    
    private String checkrq;
    
    private String shrq;
    
    private String bodypart;
    
    private String jlysdm;
    
    private String zjysdm;
    
    private String shysdm;
    
    private String checkysdm;


    public String getStudyid() {
        return studyid;
    }

    public void setStudyid(String studyid) {
        this.studyid = studyid;
    }

    public String getJldm() {
        return jldm;
    }

    public void setJldm(String jldm) {
        this.jldm = jldm;
    }

    public String getJcjg() {
        return jcjg;
    }

    public void setJcjg(String jcjg) {
        this.jcjg = jcjg;
    }

    public String getJcjgx() {
        return jcjgx;
    }

    public void setJcjgx(String jcjgx) {
        this.jcjgx = jcjgx;
    }

    public String getZjjl() {
        return zjjl;
    }

    public void setZjjl(String zjjl) {
        this.zjjl = zjjl;
    }

    public String getJlys() {
        return jlys;
    }

    public void setJlys(String jlys) {
        this.jlys = jlys;
    }

    public String getYlbj() {
        return ylbj;
    }

    public void setYlbj(String ylbj) {
        this.ylbj = ylbj;
    }

    public String getJcys() {
        return jcys;
    }

    public void setJcys(String jcys) {
        this.jcys = jcys;
    }

    public String getTydl() {
        return tydl;
    }

    public void setTydl(String tydl) {
        this.tydl = tydl;
    }

    public String getZljy() {
        return zljy;
    }

    public void setZljy(String zljy) {
        this.zljy = zljy;
    }

    public String getZjys() {
        return zjys;
    }

    public void setZjys(String zjys) {
        this.zjys = zjys;
    }

    public String getPgjg() {
        return pgjg;
    }

    public void setPgjg(String pgjg) {
        this.pgjg = pgjg;
    }

    public String getZyzjjl() {
        return zyzjjl;
    }

    public void setZyzjjl(String zyzjjl) {
        this.zyzjjl = zyzjjl;
    }

    public String getZyylbj() {
        return zyylbj;
    }

    public void setZyylbj(String zyylbj) {
        this.zyylbj = zyylbj;
    }

    public String getJkzjjl() {
        return jkzjjl;
    }

    public void setJkzjjl(String jkzjjl) {
        this.jkzjjl = jkzjjl;
    }

    public String getJkylbj() {
        return jkylbj;
    }

    public void setJkylbj(String jkylbj) {
        this.jkylbj = jkylbj;
    }

    public String getChylbj() {
        return chylbj;
    }

    public void setChylbj(String chylbj) {
        this.chylbj = chylbj;
    }

    public String getCompareks() {
        return compareks;
    }

    public void setCompareks(String compareks) {
        this.compareks = compareks;
    }

    public String getCompareid1() {
        return compareid1;
    }

    public void setCompareid1(String compareid1) {
        this.compareid1 = compareid1;
    }

    public String getCompareid2() {
        return compareid2;
    }

    public void setCompareid2(String compareid2) {
        this.compareid2 = compareid2;
    }

    public String getCompareid3() {
        return compareid3;
    }

    public void setCompareid3(String compareid3) {
        this.compareid3 = compareid3;
    }

    public String getDeptcode() {
        return deptcode;
    }

    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode;
    }

    public String getJkxc() {
        return jkxc;
    }

    public void setJkxc(String jkxc) {
        this.jkxc = jkxc;
    }

    public Integer getSmsReport() {
        return smsReport;
    }

    public void setSmsReport(Integer smsReport) {
        this.smsReport = smsReport;
    }

    public String getFhys() {
        return fhys;
    }

    public void setFhys(String fhys) {
        this.fhys = fhys;
    }

    public String getFhrq() {
        return fhrq;
    }

    public void setFhrq(String fhrq) {
        this.fhrq = fhrq;
    }

    public String getRepGrade() {
        return repGrade;
    }

    public void setRepGrade(String repGrade) {
        this.repGrade = repGrade;
    }

    public String getCheckys() {
        return checkys;
    }

    public void setCheckys(String checkys) {
        this.checkys = checkys;
    }

    public String getShys() {
        return shys;
    }

    public void setShys(String shys) {
        this.shys = shys;
    }

    public String getCheckrq() {
        return checkrq;
    }

    public void setCheckrq(String checkrq) {
        this.checkrq = checkrq;
    }

    public String getShrq() {
        return shrq;
    }

    public void setShrq(String shrq) {
        this.shrq = shrq;
    }

    public String getBodypart() {
        return bodypart;
    }

    public void setBodypart(String bodypart) {
        this.bodypart = bodypart;
    }

    public String getJlysdm() {
        return jlysdm;
    }

    public void setJlysdm(String jlysdm) {
        this.jlysdm = jlysdm;
    }

    public String getZjysdm() {
        return zjysdm;
    }

    public void setZjysdm(String zjysdm) {
        this.zjysdm = zjysdm;
    }

    public String getShysdm() {
        return shysdm;
    }

    public void setShysdm(String shysdm) {
        this.shysdm = shysdm;
    }

    public String getCheckysdm() {
        return checkysdm;
    }

    public void setCheckysdm(String checkysdm) {
        this.checkysdm = checkysdm;
    }

    public Date getJlrq() {
        return jlrq;
    }

    public void setJlrq(Date jlrq) {
        this.jlrq = jlrq;
    }

    public Date getZjrq() {
        return zjrq;
    }

    public void setZjrq(Date zjrq) {
        this.zjrq = zjrq;
    }
}

