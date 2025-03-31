package com.dchealth.etl.domain.check;

import com.dchealth.etl.domain.BaseEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * 患者基本信息(ViewJcxx)实体类
 *
 * @author makejava
 * @since 2023-06-06 11:34:37
 */
public class ViewJcxx extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 861819239268081223L;
    /**
     * 预约id
     */
    private String id;
    /**
     * 检查日期
     */
    private Date checkdate;
    /**
     * 预约日期
     */
    private Date studydate;
    /**
     * 身份证号
     */
    private String sfzh;
    /**
     * 姓名
     */
    private String xm;
    /**
     * 出生日期
     */
    private Date csny;
    /**
     * 年龄
     */
    private Integer nl;
    /**
     * 性别
     */
    private String xb;
    /**
     * 电话
     */
    private String lxdh;
    /**
     * 民族
     */
    private String mz;
    /**
     * 婚姻状况
     */
    private String hf;
    /**
     * 联系人姓名
     */
    private String lxrxm;
    /**
     * 联系人电话
     */
    private String lxrdh;
    /**
     * 通信地址
     */
    private String txdz;
    /**
     * 结论日期
     */
    private Date resultdate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCheckdate() {
        return checkdate;
    }

    public void setCheckdate(Date checkdate) {
        this.checkdate = checkdate;
    }

    public Date getStudydate() {
        return studydate;
    }

    public void setStudydate(Date studydate) {
        this.studydate = studydate;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public Date getCsny() {
        return csny;
    }

    public void setCsny(Date csny) {
        this.csny = csny;
    }

    public Integer getNl() {
        return nl;
    }

    public void setNl(Integer nl) {
        this.nl = nl;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getMz() {
        return mz;
    }

    public void setMz(String mz) {
        this.mz = mz;
    }

    public String getHf() {
        return hf;
    }

    public void setHf(String hf) {
        this.hf = hf;
    }

    public String getLxrxm() {
        return lxrxm;
    }

    public void setLxrxm(String lxrxm) {
        this.lxrxm = lxrxm;
    }

    public String getLxrdh() {
        return lxrdh;
    }

    public void setLxrdh(String lxrdh) {
        this.lxrdh = lxrdh;
    }

    public String getTxdz() {
        return txdz;
    }

    public void setTxdz(String txdz) {
        this.txdz = txdz;
    }

    public Date getResultdate() {
        return resultdate;
    }

    public void setResultdate(Date resultdate) {
        this.resultdate = resultdate;
    }

}

