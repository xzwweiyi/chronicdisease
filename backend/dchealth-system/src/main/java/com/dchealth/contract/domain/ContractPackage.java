package com.dchealth.contract.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 签约服务包对象 contract_package
 *
 * @author dchealth
 * @date 2026-06-27
 */
public class ContractPackage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 服务包名称 */
    private String packageName;

    /** 服务包类型 */
    private String packageType;

    /** 价格 */
    private BigDecimal price;

    /** 服务包描述 */
    private String description;

    /** 包含服务项目ID列表（逗号分隔） */
    private String serviceIds;

    /** 适用病种（逗号分隔） */
    private String applicableDiseases;

    /** 状态（0停用 1启用） */
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(String serviceIds) {
        this.serviceIds = serviceIds;
    }

    public String getApplicableDiseases() {
        return applicableDiseases;
    }

    public void setApplicableDiseases(String applicableDiseases) {
        this.applicableDiseases = applicableDiseases;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "ContractPackage{id=" + id + "}";
    }
}
