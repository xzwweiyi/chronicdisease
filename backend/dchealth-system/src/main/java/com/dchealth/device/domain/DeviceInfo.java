package com.dchealth.device.domain;

import java.util.Date;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 设备信息对象 device_info
 *
 * @author dchealth
 * @date 2026-06-27
 */
public class DeviceInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 设备编码 */
    private String deviceCode;

    /** 设备名称 */
    private String deviceName;

    /** 设备类型 */
    private String deviceType;

    /** 生产厂商 */
    private String manufacturer;

    /** 设备型号 */
    private String model;

    /** 采购日期 */
    private Date purchaseDate;

    /** 保修截止日期 */
    private Date warrantyExpire;

    /** 设备状态 */
    private String deviceStatus;

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

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getWarrantyExpire() {
        return warrantyExpire;
    }

    public void setWarrantyExpire(Date warrantyExpire) {
        this.warrantyExpire = warrantyExpire;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    @Override
    public String toString() {
        return "DeviceInfo{id=" + id + "}";
    }
}
