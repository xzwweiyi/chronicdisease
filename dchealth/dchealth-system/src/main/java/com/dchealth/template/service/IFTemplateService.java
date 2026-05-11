package com.dchealth.template.service;

import java.util.List;
import com.dchealth.template.domain.FTemplate;
import com.dchealth.template.domain.vo.FTemplateVo;

/**
 * 模板Service接口
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
public interface IFTemplateService 
{
    /**
     * 查询模板
     * 
     * @param templateId 模板主键
     * @return 模板
     */
    public FTemplate selectFTemplateByTemplateId(Long templateId);

    /**
     * 模板表单组件详情
     * @param templateId
     * @return
     */
    public FTemplate detail(Long templateId);

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
    public Long insertFTemplate(FTemplate fTemplate);

    /**
     * 导入更新模板
     * @param templateId
     * @param followId
     * @return
     */
    public int importOrUpdate(Long templateId, Long followId);

    /**
     * 修改模板
     * 
     * @param fTemplate 模板
     * @return 结果
     */
    public int updateFTemplate(FTemplate fTemplate);

    /**
     * 批量删除模板
     * 
     * @param templateIds 需要删除的模板主键集合
     * @return 结果
     */
    public int deleteFTemplateByTemplateIds(Long[] templateIds);

    public int deleteDeptFTemplateByTemplateIds(Long[] templateIds);

    /**
     * 删除模板信息
     * 
     * @param templateId 模板主键
     * @return 结果
     */
    public int deleteFTemplateByTemplateId(Long templateId);

    /**
     * 保存模板相关信息,模板、表单、表单属性
     * @param vo
     */
    void addTemplate(FTemplateVo vo);

    /**
     * 逻辑删除模板信息
     * @param templateIds
     * @return
     */
    int deleteFTemplateByTempIds(Long[] templateIds);

    List<FTemplate> selectDeptFTemplateList(FTemplate fTemplate);

    void copy(Long templateId,Long templateCategoryId);
}
