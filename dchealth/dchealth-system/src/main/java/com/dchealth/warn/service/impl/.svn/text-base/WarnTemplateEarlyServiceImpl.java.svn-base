package com.dchealth.warn.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.warn.mapper.WarnTemplateEarlyMapper;
import com.dchealth.warn.domain.WarnTemplateEarly;
import com.dchealth.warn.service.IWarnTemplateEarlyService;

/**
 * 预警模板Service业务层处理
 * 
 * @author lchbu
 * @date 2023-01-09
 */
@Service
public class WarnTemplateEarlyServiceImpl implements IWarnTemplateEarlyService 
{
    @Autowired
    private WarnTemplateEarlyMapper warnTemplateEarlyMapper;

    /**
     * 查询预警模板
     * 
     * @param id 预警模板主键
     * @return 预警模板
     */
    @Override
    public WarnTemplateEarly selectWarnTemplateEarlyById(Long id)
    {
        return warnTemplateEarlyMapper.selectWarnTemplateEarlyById(id);
    }

    /**
     * 查询预警模板列表
     * 
     * @param warnTemplateEarly 预警模板
     * @return 预警模板
     */
    @Override
    public List<WarnTemplateEarly> selectWarnTemplateEarlyList(WarnTemplateEarly warnTemplateEarly)
    {
        return warnTemplateEarlyMapper.selectWarnTemplateEarlyList(warnTemplateEarly);
    }

    /**
     * 新增预警模板
     * 
     * @param warnTemplateEarly 预警模板
     * @return 结果
     */
    @Override
    public int insertWarnTemplateEarly(WarnTemplateEarly warnTemplateEarly)
    {
        warnTemplateEarly.setCreateTime(DateUtils.getNowDate());
        return warnTemplateEarlyMapper.insertWarnTemplateEarly(warnTemplateEarly);
    }

    /**
     * 修改预警模板
     * 
     * @param warnTemplateEarly 预警模板
     * @return 结果
     */
    @Override
    public int updateWarnTemplateEarly(WarnTemplateEarly warnTemplateEarly)
    {
        return warnTemplateEarlyMapper.updateWarnTemplateEarly(warnTemplateEarly);
    }

    /**
     * 批量删除预警模板
     * 
     * @param ids 需要删除的预警模板主键
     * @return 结果
     */
    @Override
    public int deleteWarnTemplateEarlyByIds(Long[] ids)
    {
        return warnTemplateEarlyMapper.deleteWarnTemplateEarlyByIds(ids);
    }

    /**
     * 删除预警模板信息
     * 
     * @param id 预警模板主键
     * @return 结果
     */
    @Override
    public int deleteWarnTemplateEarlyById(Long id)
    {
        return warnTemplateEarlyMapper.deleteWarnTemplateEarlyById(id);
    }
}
