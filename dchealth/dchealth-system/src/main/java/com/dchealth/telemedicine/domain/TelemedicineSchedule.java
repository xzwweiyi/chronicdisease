package com.dchealth.telemedicine.domain;

import java.util.Date;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 远程会诊排班对象 telemedicine_schedule
 *
 * @author dchealth
 * @date 2026-06-27
 */
public class TelemedicineSchedule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 医生ID */
    private Long doctorId;

    /** 医生姓名 */
    private String doctorName;

    /** 科室名称 */
    private String deptName;

    /** 排班日期 */
    private Date scheduleDate;

    /** 时间段 */
    private String timeSlot;

    /** 最大会诊数 */
    private Integer maxConsultations;

    /** 已预约数 */
    private Integer bookedCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Integer getMaxConsultations() {
        return maxConsultations;
    }

    public void setMaxConsultations(Integer maxConsultations) {
        this.maxConsultations = maxConsultations;
    }

    public Integer getBookedCount() {
        return bookedCount;
    }

    public void setBookedCount(Integer bookedCount) {
        this.bookedCount = bookedCount;
    }

    @Override
    public String toString() {
        return "TelemedicineSchedule{id=" + id + "}";
    }
}
