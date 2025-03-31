package com.dchealth.etl.domain.record;

import com.dchealth.etl.domain.BaseEntity;

import java.util.Date;
import java.io.Serializable;

/**
 * 医嘱(Orders)实体类
 *
 * @author makejava
 * @since 2023-04-24 17:53:20
 */
public class Orders extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 667071281230283246L;
    /**
     * 病人标识号
     */
    private String patientId;
    /**
     * 病人本次住院标识
     */
    private String visitId;
    /**
     * 医嘱序号
     */
    private Integer orderNo;
    /**
     * 医嘱子序号
     */
    private String orderSubNo;
    /**
     * 长期医嘱标志
     */
    private Integer repeatIndicator;
    /**
     * 医嘱类别
     */
    private String orderClass;
    /**
     * 医嘱正文
     */
    private String orderText;
    /**
     * 起始日期及时间
     */
    private Date startDateTime;
    /**
     * 停止日期及时间
     */
    private Date stopDateTime;
    /**
     * 开医嘱时间
     */
    private Date enterDateTime;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderSubNo() {
        return orderSubNo;
    }

    public void setOrderSubNo(String orderSubNo) {
        this.orderSubNo = orderSubNo;
    }

    public Integer getRepeatIndicator() {
        return repeatIndicator;
    }

    public void setRepeatIndicator(Integer repeatIndicator) {
        this.repeatIndicator = repeatIndicator;
    }

    public String getOrderClass() {
        return orderClass;
    }

    public void setOrderClass(String orderClass) {
        this.orderClass = orderClass;
    }

    public String getOrderText() {
        return orderText;
    }

    public void setOrderText(String orderText) {
        this.orderText = orderText;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getStopDateTime() {
        return stopDateTime;
    }

    public void setStopDateTime(Date stopDateTime) {
        this.stopDateTime = stopDateTime;
    }

    public Date getEnterDateTime() {
        return enterDateTime;
    }

    public void setEnterDateTime(Date enterDateTime) {
        this.enterDateTime = enterDateTime;
    }
}

