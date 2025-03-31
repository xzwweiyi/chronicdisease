package com.dchealth.template.service;

import java.util.List;
import com.dchealth.template.domain.FTemplateForm;

/**
 * 单模板Service接口
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
public interface IFTemplateFormService 
{
    /**
     * 查询单模板
     * 
     * @param templateFormId 单模板主键
     * @return 单模板
     */
    public FTemplateForm selectFTemplateFormByTemplateFormId(Long templateFormId);

    /**
     * 表单详情
     * @param templateFormId
     * @return
     */
    public FTemplateForm detail(Long templateFormId);

    /**
     * 查询单模板列表
     * 
     * @param fTemplateForm 单模板
     * @return 单模板集合
     */
    public List<FTemplateForm> selectFTemplateFormList(FTemplateForm fTemplateForm);

    /**
     * 新增单模板
     * 
     * @param fTemplateForm 单模板
     * @return 结果
     */
    public int insertFTemplateForm(FTemplateForm fTemplateForm);

    /**
     * 修改单模板
     * 
     * @param fTemplateForm 单模板
     * @return 结果
     */
    public int updateFTemplateForm(FTemplateForm fTemplateForm);

    /**
     * 批量删除单模板
     * 
     * @param templateFormIds 需要删除的单模板主键集合
     * @return 结果
     */
    public int deleteFTemplateFormByTemplateFormIds(Long[] templateFormIds);

    /**
     * 删除单模板信息
     * 
     * @param templateFormId 单模板主键
     * @return 结果
     */
    public int deleteFTemplateFormByTemplateFormId(Long templateFormId);

    /**
     * 根据模板id查询表单信息
     * @param templateId
     * @return
     */
    FTemplateForm queryTemplateFromByTemId(Long templateId);
}
