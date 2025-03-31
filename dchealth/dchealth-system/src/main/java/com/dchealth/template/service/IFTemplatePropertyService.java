package com.dchealth.template.service;

import java.util.List;

import com.dchealth.template.domain.FTemplate;
import com.dchealth.template.domain.FTemplateForm;
import com.dchealth.template.domain.FTemplateProperty;

/**
 * 属性Service接口
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
public interface IFTemplatePropertyService 
{
    /**
     * 查询属性
     * 
     * @param id 属性主键
     * @return 属性
     */
    public FTemplateProperty selectFTemplatePropertyById(Long id);

    /**
     * 查询属性列表
     * 
     * @param fTemplateProperty 属性
     * @return 属性集合
     */
    public List<FTemplateProperty> selectFTemplatePropertyList(FTemplateProperty fTemplateProperty);

    /**
     * 新增属性
     * 
     * @param fTemplateProperty 属性
     * @return 结果
     */
    public int insertFTemplateProperty(FTemplateProperty fTemplateProperty);

    /**
     * 表单提交时使用
     * @param fTemplateForm
     * @return
     */
    public int insertFTemplateProperty(FTemplateForm fTemplateForm);

    /**
     * 新增属性
     * @param fTemplate
     * @return
     */
    public int insertFTemplateProperty(FTemplate fTemplate);

    /**
     * 修改属性
     * 
     * @param fTemplateProperty 属性
     * @return 结果
     */
    public int updateFTemplateProperty(FTemplateProperty fTemplateProperty);

    /**
     * 表单提交修改
     * @param fTemplateForm
     * @return
     */
    public int updateFTemplateProperty(FTemplateForm fTemplateForm);

    /**
     * 批量
     * @param fTemplate
     * @return
     */
    public int updateFTemplateProperty(FTemplate fTemplate);

    /**
     * 批量删除属性
     * 
     * @param ids 需要删除的属性主键集合
     * @return 结果
     */
    public int deleteFTemplatePropertyByIds(Long[] ids);

    /**
     * 删除属性信息
     * 
     * @param id 属性主键
     * @return 结果
     */
    public int deleteFTemplatePropertyById(Long id);
}
