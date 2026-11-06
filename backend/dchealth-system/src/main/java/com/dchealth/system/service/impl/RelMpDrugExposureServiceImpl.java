package com.dchealth.system.service.impl;

import com.dchealth.common.utils.DateUtils;
import com.dchealth.system.domain.RelMpDrugExposure;
import com.dchealth.system.mapper.RelMpDrugExposureMapper;
import com.dchealth.system.service.IRelMpDrugExposureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author wangpf
 * @date 2025-08-20
 */
@Service
public class RelMpDrugExposureServiceImpl implements IRelMpDrugExposureService 
{
    @Autowired
    private RelMpDrugExposureMapper relMpDrugExposureMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public RelMpDrugExposure selectRelMpDrugExposureById(Long id)
    {
        return relMpDrugExposureMapper.selectRelMpDrugExposureById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param relMpDrugExposure 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<RelMpDrugExposure> selectRelMpDrugExposureList(RelMpDrugExposure relMpDrugExposure)
    {
        return relMpDrugExposureMapper.selectRelMpDrugExposureList(relMpDrugExposure);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param relMpDrugExposure 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertRelMpDrugExposure(RelMpDrugExposure relMpDrugExposure)
    {
        return relMpDrugExposureMapper.insertRelMpDrugExposure(relMpDrugExposure);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param relMpDrugExposure 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateRelMpDrugExposure(RelMpDrugExposure relMpDrugExposure)
    {
        relMpDrugExposure.setUpdateTime(String.valueOf(DateUtils.getNowDate()));
        return relMpDrugExposureMapper.updateRelMpDrugExposure(relMpDrugExposure);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteRelMpDrugExposureByIds(Long[] ids)
    {
        return relMpDrugExposureMapper.deleteRelMpDrugExposureByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteRelMpDrugExposureById(Long id)
    {
        return relMpDrugExposureMapper.deleteRelMpDrugExposureById(id);
    }
}
