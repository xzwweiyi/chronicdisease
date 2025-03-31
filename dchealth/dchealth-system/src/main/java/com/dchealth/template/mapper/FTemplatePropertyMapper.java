package com.dchealth.template.mapper;

import java.util.List;
import com.dchealth.template.domain.FTemplateProperty;

/**
 * 属性Mapper接口
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
public interface FTemplatePropertyMapper 
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


    public List<FTemplateProperty> selectPropertyNumsList(FTemplateProperty fTemplateProperty);

    /**
     * 根据模板id查询
     * @param templateId
     * @return
     */
    public List<FTemplateProperty> selectFTemplatePropertyByTemplateId(Long templateId);

    /**
     * 新增属性
     * 
     * @param fTemplateProperty 属性
     * @return 结果
     */
    public int insertFTemplateProperty(FTemplateProperty fTemplateProperty);

    /**
     * 修改属性
     * 
     * @param fTemplateProperty 属性
     * @return 结果
     */
    public int updateFTemplateProperty(FTemplateProperty fTemplateProperty);

    /**
     * 删除属性
     * 
     * @param id 属性主键
     * @return 结果
     */
    public int deleteFTemplatePropertyById(Long id);

    /**
     * 根据模板id删除
     * @param id
     * @return
     */
    public int deleteFTemplatePropertyByTemplateId(Long id);

    /**
     * 根据模板表单id删除
     * @param id
     * @return
     */
    public int deleteFTemplatePropertyByFormId(Long id);

    /**
     * 批量删除属性
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFTemplatePropertyByIds(Long[] ids);

    /**
     * 根据表单id查询表单属性信息
     * @param templateFormId
     * @return
     */
    List<FTemplateProperty> selectFTemplatePropertyByTemFromId(Long templateFormId);
}
