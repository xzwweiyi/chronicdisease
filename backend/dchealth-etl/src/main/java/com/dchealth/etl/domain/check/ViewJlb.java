package com.dchealth.etl.domain.check;

import com.dchealth.etl.domain.BaseEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * 体检结论(ViewJlb)实体类
 *
 * @author makejava
 * @since 2023-06-06 11:34:38
 */
public class ViewJlb extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -86388588305181380L;
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
    private Date jlrq;
    /**
     * 检查结果
     */
    private String jcjg;
    /**
     * 终检结论
     */
    private String zjjl;
    /**
     * 结论医生
     */
    private String jlys;
    /**
     * 提示建议
     */
    private String tsjy;
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

    public Date getJlrq() {
        return jlrq;
    }

    public void setJlrq(Date jlrq) {
        this.jlrq = jlrq;
    }

    public String getJcjg() {
        return jcjg;
    }

    public void setJcjg(String jcjg) {
        this.jcjg = jcjg;
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

    public String getTsjy() {
        return tsjy;
    }

    public void setTsjy(String tsjy) {
        this.tsjy = tsjy;
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

    public Date getZjrq() {
        return zjrq;
    }

    public void setZjrq(Date zjrq) {
        this.zjrq = zjrq;
    }

}

