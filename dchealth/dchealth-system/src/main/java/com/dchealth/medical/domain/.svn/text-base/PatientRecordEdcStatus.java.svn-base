package com.dchealth.medical.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 随访edc模板状态对象 patient_record_edc_status
 * 
 */
@Data
public class PatientRecordEdcStatus extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 随访表单id */
    @Excel(name = "随访表单id")
    private Long recordId;

    /** 随访表单id */
    @Excel(name = "随访表单id")
    private Long medicalFormId;

    /** edc状态，1待提交，2待质检，3质疑待回复，4已锁库 */
    @Excel(name = "edc状态，1待提交，2待质检，3质疑待回复，4已锁库")
    private String edcStatus;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("recordId", getRecordId())
            .append("medicalFormId", getMedicalFormId())
            .append("edcStatus", getEdcStatus())
            .append("createTime", getCreateTime())
            .toString();
    }

    public PatientRecordEdcStatus(){}

    public PatientRecordEdcStatus(Long recordId,Long medicalFormId,String edcStatus){
        this.recordId=recordId;
        this.medicalFormId=medicalFormId;
        this.edcStatus=edcStatus;
    }

}
