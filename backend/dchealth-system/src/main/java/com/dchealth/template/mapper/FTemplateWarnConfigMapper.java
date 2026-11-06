package com.dchealth.template.mapper;

import java.util.List;
import com.dchealth.template.domain.FTemplateWarnConfig;

/**
 * 模板预警信息Mapper接口
 * 
 * @author lchbu
 * @date 2022-08-31
 */
public interface FTemplateWarnConfigMapper 
{
    /**
     * 查询模板预警信息
     * 
     * @param id 模板预警信息主键
     * @return 模板预警信息
     */
    public FTemplateWarnConfig selectFTemplateWarnConfigById(Long id);

    /**
     * 查询模板预警信息列表
     * 
     * @param fTemplateWarnConfig 模板预警信息
     * @return 模板预警信息集合
     */
    public List<FTemplateWarnConfig> selectFTemplateWarnConfigList(FTemplateWarnConfig fTemplateWarnConfig);


    public List<FTemplateWarnConfig> selectFTemplateWarnConfigByTemplateId(Long templateId);

    /**
     * 新增模板预警信息
     * 
     * @param fTemplateWarnConfig 模板预警信息
     * @return 结果
     */
    public int insertFTemplateWarnConfig(FTemplateWarnConfig fTemplateWarnConfig);

    /**
     * 批量增加
     * @param list
     * @return
     */
    public int batchInsertFTemplateWarnConfig(List<FTemplateWarnConfig> list);

    /**
     * 修改模板预警信息
     * 
     * @param fTemplateWarnConfig 模板预警信息
     * @return 结果
     */
    public int updateFTemplateWarnConfig(FTemplateWarnConfig fTemplateWarnConfig);

    /**
     * 删除模板预警信息
     * 
     * @param id 模板预警信息主键
     * @return 结果
     */
    public int deleteFTemplateWarnConfigById(Long id);

    /**
     * 根据模板id删除
     * @param templateId
     * @return
     */
    public int deleteFTemplateWarnConfigByTemplateId(Long templateId);

    /**
     * 批量删除模板预警信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFTemplateWarnConfigByIds(Long[] ids);
}
