package com.dchealth.warn.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.warn.mapper.WarnMedicalEarlyMapper;
import com.dchealth.warn.domain.WarnMedicalEarly;
import com.dchealth.warn.service.IWarnMedicalEarlyService;

/**
 * 预警表单Service业务层处理
 * 
 * @author lchbu
 * @date 2023-01-09
 */
@Service
public class WarnMedicalEarlyServiceImpl implements IWarnMedicalEarlyService 
{
    @Autowired
    private WarnMedicalEarlyMapper warnMedicalEarlyMapper;

    /**
     * 查询预警表单
     * 
     * @param id 预警表单主键
     * @return 预警表单
     */
    @Override
    public WarnMedicalEarly selectWarnMedicalEarlyById(Long id)
    {
        return warnMedicalEarlyMapper.selectWarnMedicalEarlyById(id);
    }

    /**
     * 查询预警表单列表
     * 
     * @param warnMedicalEarly 预警表单
     * @return 预警表单
     */
    @Override
    public List<WarnMedicalEarly> selectWarnMedicalEarlyList(WarnMedicalEarly warnMedicalEarly)
    {
        return warnMedicalEarlyMapper.selectWarnMedicalEarlyList(warnMedicalEarly);
    }

    /**
     * 新增预警表单
     * 
     * @param warnMedicalEarly 预警表单
     * @return 结果
     */
    @Override
    public int insertWarnMedicalEarly(WarnMedicalEarly warnMedicalEarly)
    {
        warnMedicalEarly.setCreateTime(DateUtils.getNowDate());
        return warnMedicalEarlyMapper.insertWarnMedicalEarly(warnMedicalEarly);
    }

    /**
     * 修改预警表单
     * 
     * @param warnMedicalEarly 预警表单
     * @return 结果
     */
    @Override
    public int updateWarnMedicalEarly(WarnMedicalEarly warnMedicalEarly)
    {
        return warnMedicalEarlyMapper.updateWarnMedicalEarly(warnMedicalEarly);
    }

    /**
     * 批量删除预警表单
     * 
     * @param ids 需要删除的预警表单主键
     * @return 结果
     */
    @Override
    public int deleteWarnMedicalEarlyByIds(Long[] ids)
    {
        return warnMedicalEarlyMapper.deleteWarnMedicalEarlyByIds(ids);
    }

    /**
     * 删除预警表单信息
     * 
     * @param id 预警表单主键
     * @return 结果
     */
    @Override
    public int deleteWarnMedicalEarlyById(Long id)
    {
        return warnMedicalEarlyMapper.deleteWarnMedicalEarlyById(id);
    }
}
