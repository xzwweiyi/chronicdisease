package com.dchealth.template.service;

import java.util.List;

import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.template.domain.FTemplateCategory;

/**
 * CRF库模板分类Service接口
 * 
 * @author lchbu
 * @date 2022-08-30
 */
public interface IFTemplateCategoryService 
{
    /**
     * 查询CRF库模板分类
     * 
     * @param templateCategoryId CRF库模板分类主键
     * @return CRF库模板分类
     */
    public FTemplateCategory selectFTemplateCategoryByTemplateCategoryId(Long templateCategoryId);

    /**
     * 查询CRF库模板分类列表
     * 
     * @param fTemplateCategory CRF库模板分类
     * @return CRF库模板分类集合
     */
    public List<FTemplateCategory> selectFTemplateCategoryList(FTemplateCategory fTemplateCategory);

    /**
     * 新增CRF库模板分类
     * 
     * @param fTemplateCategory CRF库模板分类
     * @return 结果
     */
    public int insertFTemplateCategory(FTemplateCategory fTemplateCategory);

    /**
     * 修改CRF库模板分类
     * 
     * @param fTemplateCategory CRF库模板分类
     * @return 结果
     */
    public int updateFTemplateCategory(FTemplateCategory fTemplateCategory);

    /**
     * 批量删除CRF库模板分类
     * 
     * @param templateCategoryIds 需要删除的CRF库模板分类主键集合
     * @return 结果
     */
    public int deleteFTemplateCategoryByTemplateCategoryIds(Long[] templateCategoryIds);

    /**
     * 删除CRF库模板分类信息
     * 
     * @param templateCategoryId CRF库模板分类主键
     * @return 结果
     */
    public AjaxResult deleteFTemplateCategoryByTemplateCategoryId(Long templateCategoryId);
}
