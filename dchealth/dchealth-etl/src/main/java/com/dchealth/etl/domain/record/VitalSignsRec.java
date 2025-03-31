package com.dchealth.etl.domain.record;

import com.dchealth.etl.domain.BaseEntity;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 病人体症记录(VitalSignsRec)实体类
 *
 * @author makejava
 * @since 2023-03-27 13:58:50
 */
public class VitalSignsRec extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 322591615046547352L;

    @Id
    private String patientId;
    /**
     * 病人本次住院标识
     */
    private String visitId;
    /**
     * 记录日期
     */
    private Date recordingDate;
    /**
     * 时间点
     */
    private Date timePoint;
    /**
     * 记录项目
     */
    private String vitalSigns;
    /**
     * 项目数值
     */
    private Float vitalSignsValues;
    /**
     * 项目单位 如千克、次、度等
     */
    private String units;
    /**
     * 备注
     */
    private String memo;


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

    public Date getRecordingDate() {
        return recordingDate;
    }

    public void setRecordingDate(Date recordingDate) {
        this.recordingDate = recordingDate;
    }

    public Date getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(Date timePoint) {
        this.timePoint = timePoint;
    }

    public String getVitalSigns() {
        return vitalSigns;
    }

    public void setVitalSigns(String vitalSigns) {
        this.vitalSigns = vitalSigns;
    }

    public Float getVitalSignsValues() {
        return vitalSignsValues;
    }

    public void setVitalSignsValues(Float vitalSignsValues) {
        this.vitalSignsValues = vitalSignsValues;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

}

