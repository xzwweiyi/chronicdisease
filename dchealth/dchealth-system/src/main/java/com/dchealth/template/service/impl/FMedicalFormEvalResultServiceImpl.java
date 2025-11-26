package com.dchealth.template.service.impl;

import com.dchealth.common.utils.DateUtils;
import com.dchealth.template.domain.FMedicalFormEvalResult;
import com.dchealth.template.mapper.FMedicalFormEvalResultMapper;
import com.dchealth.template.service.IFMedicalFormEvalResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author wangpf
 * @date 2025-11-26
 */
@Service
public class FMedicalFormEvalResultServiceImpl implements IFMedicalFormEvalResultService
{
    @Autowired
    private FMedicalFormEvalResultMapper fMedicalFormEvalResultMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public FMedicalFormEvalResult selectFMedicalFormEvalResultById(Long id)
    {
        return fMedicalFormEvalResultMapper.selectFMedicalFormEvalResultById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param fMedicalFormEvalResult 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<FMedicalFormEvalResult> selectFMedicalFormEvalResultList(FMedicalFormEvalResult fMedicalFormEvalResult)
    {
        return fMedicalFormEvalResultMapper.selectFMedicalFormEvalResultList(fMedicalFormEvalResult);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param fMedicalFormEvalResult 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertFMedicalFormEvalResult(FMedicalFormEvalResult fMedicalFormEvalResult)
    {
        fMedicalFormEvalResult.setCreateTime(DateUtils.getNowDate());
        return fMedicalFormEvalResultMapper.insertFMedicalFormEvalResult(fMedicalFormEvalResult);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param fMedicalFormEvalResult 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateFMedicalFormEvalResult(FMedicalFormEvalResult fMedicalFormEvalResult)
    {
        return fMedicalFormEvalResultMapper.updateFMedicalFormEvalResult(fMedicalFormEvalResult);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteFMedicalFormEvalResultByIds(Long[] ids)
    {
        return fMedicalFormEvalResultMapper.deleteFMedicalFormEvalResultByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteFMedicalFormEvalResultById(Long id)
    {
        return fMedicalFormEvalResultMapper.deleteFMedicalFormEvalResultById(id);
    }
}
