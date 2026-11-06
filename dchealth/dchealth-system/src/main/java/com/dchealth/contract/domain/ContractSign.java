package com.dchealth.contract.domain;

import java.util.Date;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 签约记录对象 contract_sign
 *
 * @author dchealth
 * @date 2026-06-27
 */
public class ContractSign extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 居民ID */
    private Long residentId;

    /** 居民姓名 */
    private String residentName;

    /** 团队ID */
    private Long teamId;

    /** 服务包ID */
    private Long packageId;

    /** 签约日期 */
    private Date signDate;

    /** 服务开始日期 */
    private Date startDate;

    /** 服务结束日期 */
    private Date endDate;

    /** 签约状态 */
    private String contractStatus;

    /** 签约类型 */
    private String signType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    @Override
    public String toString() {
        return "ContractSign{id=" + id + "}";
    }
}
