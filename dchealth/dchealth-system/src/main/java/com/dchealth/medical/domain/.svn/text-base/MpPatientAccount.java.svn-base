package com.dchealth.medical.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 mp_patient_account
 * 
 * @date 2023-11-08
 */
@Data
public class MpPatientAccount extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String personId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long accountId;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("personId", getPersonId())
            .append("accountId", getAccountId())
            .toString();
    }
}
