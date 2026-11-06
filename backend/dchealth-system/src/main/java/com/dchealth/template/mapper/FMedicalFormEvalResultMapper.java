package com.dchealth.template.mapper;

import com.dchealth.template.domain.FMedicalFormEvalResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author wangpf
 * @date 2025-11-26
 */
@Mapper
public interface FMedicalFormEvalResultMapper 
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
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteFMedicalFormEvalResultById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFMedicalFormEvalResultByIds(Long[] ids);
}
