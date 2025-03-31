package com.dchealth.etl.domain.target;

import java.util.Date;
import java.io.Serializable;

/**
 * 药品处方明细记录(TdDrugPrescDetail)实体类
 *
 * @author makejava
 * @since 2023-04-24 17:43:08
 */
public class TdDrugPrescDetail implements Serializable {
    private static final long serialVersionUID = -91902298071408280L;
    
    private Integer id;
    /**
     * 处方日期
     */
    private Date prescDate;
    /**
     * 处方号
     */
    private String prescNo;
    /**
     * 项目序号
     */
    private Integer itemNo;
    /**
     * 药品代码
     */
    private String drugCode;
    /**
     * 药品规格
     */
    private String drugSpec;
    /**
     * 药品名称
     */
    private String drugName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getPrescDate() {
        return prescDate;
    }

    public void setPrescDate(Date prescDate) {
        this.prescDate = prescDate;
    }

    public String getPrescNo() {
        return prescNo;
    }

    public void setPrescNo(String prescNo) {
        this.prescNo = prescNo;
    }

    public Integer getItemNo() {
        return itemNo;
    }

    public void setItemNo(Integer itemNo) {
        this.itemNo = itemNo;
    }

    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public String getDrugSpec() {
        return drugSpec;
    }

    public void setDrugSpec(String drugSpec) {
        this.drugSpec = drugSpec;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

}

