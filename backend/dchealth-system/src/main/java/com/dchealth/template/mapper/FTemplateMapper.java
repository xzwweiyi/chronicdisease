package com.dchealth.template.mapper;

import java.util.List;
import com.dchealth.template.domain.FTemplate;

/**
 * 模板Mapper接口
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
public interface FTemplateMapper 
{
    /**
     * 查询模板
     * 
     * @param templateId 模板主键
     * @return 模板
     */
    public FTemplate selectFTemplateByTemplateId(Long templateId);

    /**
     * 查询模板列表
     * 
     * @param fTemplate 模板
     * @return 模板集合
     */
    public List<FTemplate> selectFTemplateList(FTemplate fTemplate);

    /**
     * 新增模板
     * 
     * @param fTemplate 模板
     * @return 结果
     */
    public int insertFTemplate(FTemplate fTemplate);

    /**
     * 根据分类统计模板数量
     * @param templateCategoryId
     * @return
     */
    public int statisticsTemplateByTemplateCategoryId(Long templateCategoryId);

    /**
     * 修改模板
     * 
     * @param fTemplate 模板
     * @return 结果
     */
    public int updateFTemplate(FTemplate fTemplate);

    /**
     * 删除模板
     * 
     * @param templateId 模板主键
     * @return 结果
     */
    public int deleteFTemplateByTemplateId(Long templateId);

    /**
     * 批量删除模板
     * 
     * @param templateIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFTemplateByTemplateIds(Long[] templateIds);

    public int updateDelFlagByTemplateIds(Long[] templateIds);

    /**
     * 逻辑删除模板信息
     * @param templateIds
     * @return
     */
    int deleteFTemplateByTempIds(Long[] templateIds);

    List<FTemplate> selectDeptFTemplateList(FTemplate fTemplate);
}
