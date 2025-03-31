package com.dchealth.cro.medical.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Data;

@Data
public class DoctorSatisfactionEvaluation {

    /** $column.columnComment */
    private Long id;

    /** 账号id */
    private Long accountId;

    /** 分值 */
    private Integer score;

    /** 意见 */
    private String suggestion;

    private Date createTime;
    private String createTimeStr;


    private String nickName;
    private String deptName;
    private Long deptId;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("accountId", getAccountId())
            .append("score", getScore())
            .append("suggestion", getSuggestion())
            .append("createTime", getCreateTime())
            .toString();
    }
}
