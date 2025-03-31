package com.dchealth.research.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 队列详情对象 research_detail
 * 
 * @author lchbu
 * @date 2022-08-22
 */
public class ResearchDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long researchId;

    /** 研究介绍 */
    @Excel(name = "研究介绍")
    private String researchIntro;

    /** 治疗方案 */
    @Excel(name = "治疗方案")
    private String researchScheme;

    /** 入组标准 */
    @Excel(name = "入组标准")
    private String researchStandard;

    public void setResearchId(Long researchId) 
    {
        this.researchId = researchId;
    }

    public Long getResearchId() 
    {
        return researchId;
    }
    public void setResearchIntro(String researchIntro) 
    {
        this.researchIntro = researchIntro;
    }

    public String getResearchIntro() 
    {
        return researchIntro;
    }
    public void setResearchScheme(String researchScheme) 
    {
        this.researchScheme = researchScheme;
    }

    public String getResearchScheme() 
    {
        return researchScheme;
    }
    public void setResearchStandard(String researchStandard) 
    {
        this.researchStandard = researchStandard;
    }

    public String getResearchStandard() 
    {
        return researchStandard;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("researchId", getResearchId())
            .append("researchIntro", getResearchIntro())
            .append("researchScheme", getResearchScheme())
            .append("researchStandard", getResearchStandard())
            .toString();
    }
}
