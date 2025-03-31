package com.dchealth.research.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 讯飞智能外呼模板对象 research_xunfei_template
 * 
 * @date 2023-10-30
 */
@Data
public class ResearchXunfeiTemplate extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 电话模板id */
    @Excel(name = "电话模板id")
    private Long speechId;

    /** 模板名称 */
    @Excel(name = "模板名称")
    private String name;

    /** 话术简介 */
    @Excel(name = "话术简介")
    private String intro;

    /** 排序 */
    @Excel(name = "排序")
    private Long sortNum;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    private String imageUrl;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("speechId", getSpeechId())
            .append("name", getName())
            .append("intro", getIntro())
            .append("sortNum", getSortNum())
            .append("delFlag", getDelFlag())
            .append("updateBy", getUpdateBy())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
