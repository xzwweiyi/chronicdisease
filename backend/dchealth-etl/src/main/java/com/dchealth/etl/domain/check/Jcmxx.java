package com.dchealth.etl.domain.check;

import com.dchealth.etl.domain.BaseEntity;

import java.io.Serializable;

/**
 * 查明细项目(Jcmxx)实体类
 *
 * @author makejava
 * @since 2023-03-22 17:39:51
 */
public class Jcmxx extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 207480971710435327L;
    /**
     * 项目ID
     */
    private String xxid;
    /**
     * 顺序代码
     */
    private String ordercode;
    /**
     * 项目代码
     */
    private String xxdm;
    /**
     * 项目名称
     */
    private String xxmc;
    /**
     * 英文名称
     */
    private String exxmc;
    /**
     * 单位
     */
    private String unit;
    /**
     * 是否打印
     */
    private String ifprint;
    /**
     * 分类代码
     */
    private String lbdm;
    /**
     * 参考值男上限
     */
    private String mdefvalueup;
    /**
     * 参考值男下限
     */
    private String mdefvaluedown;
    /**
     * 参考值女上限
     */
    private String wdefvalueup;
    /**
     * 参考值女下限
     */
    private String wdefvaluedown;
    /**
     * 参考值1
     */
    private String defvalue1;
    /**
     * 参考值2
     */
    private String defvalue2;
    /**
     * 参考值3
     */
    private String defvalue3;
    /**
     * 参考值4
     */
    private String defvalue4;
    /**
     * 参考值5
     */
    private String defvalue5;
    /**
     * 拼音注记符
     */
    private String zjf;
    /**
     * 项目值字段类型
     */
    private String xxvaluetype;
    /**
     * 关系代码
     */
    private String gxdm;
    /**
     * 结论生成不关联参考值
     */
    private String ifxj;
    /**
     * 输出标记
     */
    private String outflag;
    /**
     * 输出名称
     */
    private String outname;
    /**
     * 定性值
     */
    private String ifdx;
    /**
     * 备注
     */
    private String jcmxxbz;
    /**
     * 不做危险值分析
     */
    private String nofx;
    /**
     * 男性危险值下线
     */
    private String mdsdefvaluedown;
    /**
     * 男性危险值上线
     */
    private String mdsdefvalueup;
    /**
     * 男性危险值
     */
    private String mdsdefvalue;
    /**
     * 女性危险值下线
     */
    private String wdsdefvaluedown;
    /**
     * 女性危险值上线
     */
    private String wdsdefvalueup;
    /**
     * 女性危险值
     */
    private String wdsdefvalue;
    /**
     * 项目性别
     */
    private String xxxb;
    
    private Integer xmlen;
    
    private Integer btflag;


    public String getXxid() {
        return xxid;
    }

    public void setXxid(String xxid) {
        this.xxid = xxid;
    }

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
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

    public String getExxmc() {
        return exxmc;
    }

    public void setExxmc(String exxmc) {
        this.exxmc = exxmc;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIfprint() {
        return ifprint;
    }

    public void setIfprint(String ifprint) {
        this.ifprint = ifprint;
    }

    public String getLbdm() {
        return lbdm;
    }

    public void setLbdm(String lbdm) {
        this.lbdm = lbdm;
    }

    public String getMdefvalueup() {
        return mdefvalueup;
    }

    public void setMdefvalueup(String mdefvalueup) {
        this.mdefvalueup = mdefvalueup;
    }

    public String getMdefvaluedown() {
        return mdefvaluedown;
    }

    public void setMdefvaluedown(String mdefvaluedown) {
        this.mdefvaluedown = mdefvaluedown;
    }

    public String getWdefvalueup() {
        return wdefvalueup;
    }

    public void setWdefvalueup(String wdefvalueup) {
        this.wdefvalueup = wdefvalueup;
    }

    public String getWdefvaluedown() {
        return wdefvaluedown;
    }

    public void setWdefvaluedown(String wdefvaluedown) {
        this.wdefvaluedown = wdefvaluedown;
    }

    public String getDefvalue1() {
        return defvalue1;
    }

    public void setDefvalue1(String defvalue1) {
        this.defvalue1 = defvalue1;
    }

    public String getDefvalue2() {
        return defvalue2;
    }

    public void setDefvalue2(String defvalue2) {
        this.defvalue2 = defvalue2;
    }

    public String getDefvalue3() {
        return defvalue3;
    }

    public void setDefvalue3(String defvalue3) {
        this.defvalue3 = defvalue3;
    }

    public String getDefvalue4() {
        return defvalue4;
    }

    public void setDefvalue4(String defvalue4) {
        this.defvalue4 = defvalue4;
    }

    public String getDefvalue5() {
        return defvalue5;
    }

    public void setDefvalue5(String defvalue5) {
        this.defvalue5 = defvalue5;
    }

    public String getZjf() {
        return zjf;
    }

    public void setZjf(String zjf) {
        this.zjf = zjf;
    }

    public String getXxvaluetype() {
        return xxvaluetype;
    }

    public void setXxvaluetype(String xxvaluetype) {
        this.xxvaluetype = xxvaluetype;
    }

    public String getGxdm() {
        return gxdm;
    }

    public void setGxdm(String gxdm) {
        this.gxdm = gxdm;
    }

    public String getIfxj() {
        return ifxj;
    }

    public void setIfxj(String ifxj) {
        this.ifxj = ifxj;
    }

    public String getOutflag() {
        return outflag;
    }

    public void setOutflag(String outflag) {
        this.outflag = outflag;
    }

    public String getOutname() {
        return outname;
    }

    public void setOutname(String outname) {
        this.outname = outname;
    }

    public String getIfdx() {
        return ifdx;
    }

    public void setIfdx(String ifdx) {
        this.ifdx = ifdx;
    }

    public String getJcmxxbz() {
        return jcmxxbz;
    }

    public void setJcmxxbz(String jcmxxbz) {
        this.jcmxxbz = jcmxxbz;
    }

    public String getNofx() {
        return nofx;
    }

    public void setNofx(String nofx) {
        this.nofx = nofx;
    }

    public String getMdsdefvaluedown() {
        return mdsdefvaluedown;
    }

    public void setMdsdefvaluedown(String mdsdefvaluedown) {
        this.mdsdefvaluedown = mdsdefvaluedown;
    }

    public String getMdsdefvalueup() {
        return mdsdefvalueup;
    }

    public void setMdsdefvalueup(String mdsdefvalueup) {
        this.mdsdefvalueup = mdsdefvalueup;
    }

    public String getMdsdefvalue() {
        return mdsdefvalue;
    }

    public void setMdsdefvalue(String mdsdefvalue) {
        this.mdsdefvalue = mdsdefvalue;
    }

    public String getWdsdefvaluedown() {
        return wdsdefvaluedown;
    }

    public void setWdsdefvaluedown(String wdsdefvaluedown) {
        this.wdsdefvaluedown = wdsdefvaluedown;
    }

    public String getWdsdefvalueup() {
        return wdsdefvalueup;
    }

    public void setWdsdefvalueup(String wdsdefvalueup) {
        this.wdsdefvalueup = wdsdefvalueup;
    }

    public String getWdsdefvalue() {
        return wdsdefvalue;
    }

    public void setWdsdefvalue(String wdsdefvalue) {
        this.wdsdefvalue = wdsdefvalue;
    }

    public String getXxxb() {
        return xxxb;
    }

    public void setXxxb(String xxxb) {
        this.xxxb = xxxb;
    }

    public Integer getXmlen() {
        return xmlen;
    }

    public void setXmlen(Integer xmlen) {
        this.xmlen = xmlen;
    }

    public Integer getBtflag() {
        return btflag;
    }

    public void setBtflag(Integer btflag) {
        this.btflag = btflag;
    }

}

