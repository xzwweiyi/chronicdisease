package com.dchealth.research.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 讯飞智能外呼模板话术变量对象 research_xunfei_template_var
 * 
 * @date 2023-10-30
 */
@Data
public class ResearchXunfeiTemplateVar extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 电话模板id */
    @Excel(name = "电话模板id")
    private Long speechId;

    /** 变量名 */
    @Excel(name = "变量名")
    private String remarks;

    /** 变量内容样例 */
    @Excel(name = "变量内容样例")
    private String content;

    /** 变量所在的话术内容 */
    @Excel(name = "变量所在的话术内容")
    private String original;

    /** 变量内容长度限制 */
    @Excel(name = "变量内容长度限制")
    private Integer limits;

    /** 排序 */
    @Excel(name = "排序")
    private Long sortNum;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("speechId", getSpeechId())
            .append("remarks", getRemarks())
            .append("content", getContent())
            .append("original", getOriginal())
            .append("limits", getLimits())
            .append("sortNum", getSortNum())
            .append("delFlag", getDelFlag())
            .append("updateBy", getUpdateBy())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
