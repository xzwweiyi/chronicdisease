package com.dchealth.referral.domain;

import java.util.Date;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 转诊规则对象 referral_rule
 *
 * @author dchealth
 * @date 2026-06-27
 */
public class ReferralRule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 规则名称 */
    private String ruleName;

    /** 病种类型 */
    private String diseaseType;

    /** 上转条件 */
    private String upwardConditions;

    /** 下转条件 */
    private String downwardConditions;

    /** 急诊转诊条件 */
    private String emergencyConditions;

    /** 优先级 */
    private Integer priority;

    /** 生效日期 */
    private Date effectiveDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }

    public String getUpwardConditions() {
        return upwardConditions;
    }

    public void setUpwardConditions(String upwardConditions) {
        this.upwardConditions = upwardConditions;
    }

    public String getDownwardConditions() {
        return downwardConditions;
    }

    public void setDownwardConditions(String downwardConditions) {
        this.downwardConditions = downwardConditions;
    }

    public String getEmergencyConditions() {
        return emergencyConditions;
    }

    public void setEmergencyConditions(String emergencyConditions) {
        this.emergencyConditions = emergencyConditions;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Override
    public String toString() {
        return "ReferralRule{id=" + id + "}";
    }
}
