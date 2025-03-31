package com.dchealth.cro.medical.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 用户统计操作记录对象 patient_record_oper_log
 * 
 * @author lchbu
 * @date 2023-04-14
 */
@Data
public class PatientRecordOperLog{

    /** $column.columnComment */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 档案id */
    private Long recordId;

    /** 1入组审批2处理预警3处理超窗 */
    private String formType;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public PatientRecordOperLog(){

    }

    public PatientRecordOperLog(Long userId, Long recordId, String formType, Date createTime){
        this.userId=userId;
        this.recordId=recordId;
        this.formType=formType;
        this.createTime=createTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("recordId", getRecordId())
            .append("formType", getFormType())
            .append("createTime", getCreateTime())
            .toString();
    }
}
