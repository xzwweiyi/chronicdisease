package com.dchealth.research.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 队列模块对象 research_module
 * 
 * @author lchbu
 * @date 2022-08-25
 */
public class ResearchModule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 研究项目ID */
    @Excel(name = "研究项目ID")
    private Long researchId;

    /** 模块名称 */
    @Excel(name = "模块名称")
    private String moduleName;

    /** 是否删除 0未删除 1 删除 */
    @Excel(name = "是否删除 0未删除 1 删除")
    private String isDeleted;

    /** 排序 */
    @Excel(name = "排序")
    private Long sortNum;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setResearchId(Long researchId) 
    {
        this.researchId = researchId;
    }

    public Long getResearchId() 
    {
        return researchId;
    }
    public void setModuleName(String moduleName) 
    {
        this.moduleName = moduleName;
    }

    public String getModuleName() 
    {
        return moduleName;
    }
    public void setIsDeleted(String isDeleted) 
    {
        this.isDeleted = isDeleted;
    }

    public String getIsDeleted() 
    {
        return isDeleted;
    }
    public void setSortNum(Long sortNum) 
    {
        this.sortNum = sortNum;
    }

    public Long getSortNum() 
    {
        return sortNum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("researchId", getResearchId())
            .append("moduleName", getModuleName())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("sortNum", getSortNum())
            .toString();
    }
}
