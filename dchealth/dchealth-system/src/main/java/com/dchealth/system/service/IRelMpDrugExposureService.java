package com.dchealth.system.service;

import java.util.List;
import com.dchealth.system.domain.RelMpDrugExposure;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author wangpf
 * @date 2025-08-20
 */
public interface IRelMpDrugExposureService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public RelMpDrugExposure selectRelMpDrugExposureById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param relMpDrugExposure 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<RelMpDrugExposure> selectRelMpDrugExposureList(RelMpDrugExposure relMpDrugExposure);

    /**
     * 新增【请填写功能名称】
     * 
     * @param relMpDrugExposure 【请填写功能名称】
     * @return 结果
     */
    public int insertRelMpDrugExposure(RelMpDrugExposure relMpDrugExposure);

    /**
     * 修改【请填写功能名称】
     * 
     * @param relMpDrugExposure 【请填写功能名称】
     * @return 结果
     */
    public int updateRelMpDrugExposure(RelMpDrugExposure relMpDrugExposure);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteRelMpDrugExposureByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteRelMpDrugExposureById(Long id);
}
