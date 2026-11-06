package com.dchealth.resident.domain;

import java.util.Date;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 跨机构调取日志
 */
public class CrossOrgAccess extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String personId;
    private String personName;
    private String cardId;
    private String sourceInstitution;
    private String sourceDoctor;
    private String targetInstitution;
    private String targetDoctor;
    private Date accessTime;
    private String accessReason;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPersonId() { return personId; }
    public void setPersonId(String personId) { this.personId = personId; }
    public String getPersonName() { return personName; }
    public void setPersonName(String personName) { this.personName = personName; }
    public String getCardId() { return cardId; }
    public void setCardId(String cardId) { this.cardId = cardId; }
    public String getSourceInstitution() { return sourceInstitution; }
    public void setSourceInstitution(String sourceInstitution) { this.sourceInstitution = sourceInstitution; }
    public String getSourceDoctor() { return sourceDoctor; }
    public void setSourceDoctor(String sourceDoctor) { this.sourceDoctor = sourceDoctor; }
    public String getTargetInstitution() { return targetInstitution; }
    public void setTargetInstitution(String targetInstitution) { this.targetInstitution = targetInstitution; }
    public String getTargetDoctor() { return targetDoctor; }
    public void setTargetDoctor(String targetDoctor) { this.targetDoctor = targetDoctor; }
    public Date getAccessTime() { return accessTime; }
    public void setAccessTime(Date accessTime) { this.accessTime = accessTime; }
    public String getAccessReason() { return accessReason; }
    public void setAccessReason(String accessReason) { this.accessReason = accessReason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
