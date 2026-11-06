package com.dchealth.device.domain;

import java.util.Date;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 设备绑定记录对象 device_bind
 *
 * @author dchealth
 * @date 2026-06-27
 */
public class DeviceBind extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 设备编码 */
    private String deviceCode;

    /** 居民ID */
    private Long residentId;

    /** 居民姓名 */
    private String residentName;

    /** 绑定日期 */
    private Date bindDate;

    /** 解绑日期 */
    private Date unbindDate;

    /** 绑定状态 */
    private String bindStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public Long getResidentId() {
        return residentId;
    }

    public void setResidentId(Long residentId) {
        this.residentId = residentId;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public Date getBindDate() {
        return bindDate;
    }

    public void setBindDate(Date bindDate) {
        this.bindDate = bindDate;
    }

    public Date getUnbindDate() {
        return unbindDate;
    }

    public void setUnbindDate(Date unbindDate) {
        this.unbindDate = unbindDate;
    }

    public String getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(String bindStatus) {
        this.bindStatus = bindStatus;
    }

    @Override
    public String toString() {
        return "DeviceBind{id=" + id + "}";
    }
}
