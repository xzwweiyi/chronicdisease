package com.dchealth.template.mapper;

import com.dchealth.template.domain.FDeptCrfTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FDeptCrfTemplateMapper
{

    /**
     * 查询科室crf模板
     * @param fDeptCrfTemplate
     * @return
     */
    public List<FDeptCrfTemplate> selectDeptCrfTemplateByTemplateList(FDeptCrfTemplate fDeptCrfTemplate);

    /**
     * 科室新增CRF模板
     * @param fDeptCrfTemplate
     * @return
     */
    public int insertDeptCrfTemplate(FDeptCrfTemplate fDeptCrfTemplate);

    /**
     * 科室移除Crf模板
     * @param deptTemplateIds
     * @return
     */
    public int deleteDeptCrfTemplateByTemplateId(@Param("deptTemplateIds") Long[] deptTemplateIds,@Param("templateIds")Long[] templateIds);
}
