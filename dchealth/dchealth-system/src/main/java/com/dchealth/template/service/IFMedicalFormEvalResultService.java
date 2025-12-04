package com.dchealth.template.service;

import com.dchealth.template.domain.FMedicalFormEvalResult;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author wangpf
 * @date 2025-11-26
 */
public interface IFMedicalFormEvalResultService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public FMedicalFormEvalResult selectFMedicalFormEvalResultById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param fMedicalFormEvalResult 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<FMedicalFormEvalResult> selectFMedicalFormEvalResultList(FMedicalFormEvalResult fMedicalFormEvalResult);

    /**
     * 新增【请填写功能名称】
     * 
     * @param fMedicalFormEvalResult 【请填写功能名称】
     * @return 结果
     */
    public int insertFMedicalFormEvalResult(FMedicalFormEvalResult fMedicalFormEvalResult);

    /**
     * 修改【请填写功能名称】
     * 
     * @param fMedicalFormEvalResult 【请填写功能名称】
     * @return 结果
     */
    public int updateFMedicalFormEvalResult(FMedicalFormEvalResult fMedicalFormEvalResult);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteFMedicalFormEvalResultByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteFMedicalFormEvalResultById(Long id);
}
