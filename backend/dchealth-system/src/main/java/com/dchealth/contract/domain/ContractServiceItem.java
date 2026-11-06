package com.dchealth.contract.domain;

import java.util.Date;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 签约服务项目对象 contract_service_item
 *
 * @author dchealth
 * @date 2026-06-27
 */
public class ContractServiceItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 服务项目名称 */
    private String itemName;

    /** 服务项目类型 */
    private String itemType;

    /** 服务频次 */
    private String frequency;

    /** 服务描述 */
    private String description;

    /** 执行标准 */
    private String executionStandard;

    /** 状态（0停用 1启用） */
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExecutionStandard() {
        return executionStandard;
    }

    public void setExecutionStandard(String executionStandard) {
        this.executionStandard = executionStandard;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "ContractServiceItem{id=" + id + "}";
    }
}
