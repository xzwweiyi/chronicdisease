package com.dchealth.template.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.template.mapper.FTemplateWarnConfigMapper;
import com.dchealth.template.domain.FTemplateWarnConfig;
import com.dchealth.template.service.IFTemplateWarnConfigService;

/**
 * 模板预警信息Service业务层处理
 * 
 * @author lchbu
 * @date 2022-08-31
 */
@Service
public class FTemplateWarnConfigServiceImpl implements IFTemplateWarnConfigService 
{
    @Autowired
    private FTemplateWarnConfigMapper fTemplateWarnConfigMapper;

    /**
     * 查询模板预警信息
     * 
     * @param id 模板预警信息主键
     * @return 模板预警信息
     */
    @Override
    public FTemplateWarnConfig selectFTemplateWarnConfigById(Long id)
    {
        return fTemplateWarnConfigMapper.selectFTemplateWarnConfigById(id);
    }

    /**
     * 查询模板预警信息列表
     * 
     * @param fTemplateWarnConfig 模板预警信息
     * @return 模板预警信息
     */
    @Override
    public List<FTemplateWarnConfig> selectFTemplateWarnConfigList(FTemplateWarnConfig fTemplateWarnConfig)
    {
        return fTemplateWarnConfigMapper.selectFTemplateWarnConfigList(fTemplateWarnConfig);
    }

    /**
     * 新增模板预警信息
     * 
     * @param fTemplateWarnConfig 模板预警信息
     * @return 结果
     */
    @Override
    public int insertFTemplateWarnConfig(FTemplateWarnConfig fTemplateWarnConfig)
    {
        fTemplateWarnConfig.setCreateTime(DateUtils.getNowDate());
        return fTemplateWarnConfigMapper.insertFTemplateWarnConfig(fTemplateWarnConfig);
    }

    /**
     * 修改模板预警信息
     * 
     * @param fTemplateWarnConfig 模板预警信息
     * @return 结果
     */
    @Override
    public int updateFTemplateWarnConfig(FTemplateWarnConfig fTemplateWarnConfig)
    {
        fTemplateWarnConfig.setUpdateTime(DateUtils.getNowDate());
        return fTemplateWarnConfigMapper.updateFTemplateWarnConfig(fTemplateWarnConfig);
    }

    /**
     * 批量删除模板预警信息
     * 
     * @param ids 需要删除的模板预警信息主键
     * @return 结果
     */
    @Override
    public int deleteFTemplateWarnConfigByIds(Long[] ids)
    {
        return fTemplateWarnConfigMapper.deleteFTemplateWarnConfigByIds(ids);
    }

    /**
     * 删除模板预警信息信息
     * 
     * @param id 模板预警信息主键
     * @return 结果
     */
    @Override
    public int deleteFTemplateWarnConfigById(Long id)
    {
        return fTemplateWarnConfigMapper.deleteFTemplateWarnConfigById(id);
    }
}
