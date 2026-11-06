package com.dchealth.template.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;


@Data
public class SmsTemplate extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 模板名称 */
    @Excel(name = "模板名称")
    private String templateName;

    /** 状态0失效1生效 */
    @Excel(name = "状态0失效1生效")
    private String status;

    /** 通知内容 */
    @Excel(name = "通知内容")
    private String content;

    /** 创建人id */
    @Excel(name = "创建人id")
    private Long createId;

    /** 更新人id */
    @Excel(name = "更新人id")
    private Long updateId;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("templateName", getTemplateName())
            .append("status", getStatus())
            .append("content", getContent())
            .append("createId", getCreateId())
            .append("createTime", getCreateTime())
            .append("updateId", getUpdateId())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
