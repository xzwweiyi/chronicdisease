package com.dchealth.medical.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

@Data
public class DoctorSatisfactionEvaluation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 账号id */
    @Excel(name = "账号id")
    private Long accountId;

    /** 分值 */
    @Excel(name = "分值")
    private Long score;

    /** 意见 */
    @Excel(name = "意见")
    private String suggestion;

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
