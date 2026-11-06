package com.dchealth.template.mapper;

import com.dchealth.template.domain.FMedicalExamSetEvaluation;

import java.util.List;

/**
 * 属性题目设置评价结果Mapper接口
 * 
 * @author lchbu
 * @date 2023-04-03
 */
public interface FMedicalExamSetEvaluationMapper 
{
    /**
     * 查询属性题目设置评价结果
     * 
     * @param id 属性题目设置评价结果主键
     * @return 属性题目设置评价结果
     */
    public FMedicalExamSetEvaluation selectFMedicalExamSetEvaluationById(Long id);

    /**
     * 查询属性题目设置评价结果列表
     * 
     * @param fMedicalExamSetEvaluation 属性题目设置评价结果
     * @return 属性题目设置评价结果集合
     */
    public List<FMedicalExamSetEvaluation> selectFMedicalExamSetEvaluationList(FMedicalExamSetEvaluation fMedicalExamSetEvaluation);

    /**
     * 新增属性题目设置评价结果
     * 
     * @param fMedicalExamSetEvaluation 属性题目设置评价结果
     * @return 结果
     */
    public int insertFMedicalExamSetEvaluation(FMedicalExamSetEvaluation fMedicalExamSetEvaluation);

    /**
     * 修改属性题目设置评价结果
     * 
     * @param fMedicalExamSetEvaluation 属性题目设置评价结果
     * @return 结果
     */
    public int updateFMedicalExamSetEvaluation(FMedicalExamSetEvaluation fMedicalExamSetEvaluation);

    /**
     * 删除属性题目设置评价结果
     * 
     * @param id 属性题目设置评价结果主键
     * @return 结果
     */
    public int deleteFMedicalExamSetEvaluationById(Long id);

    /**
     * 批量删除属性题目设置评价结果
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFMedicalExamSetEvaluationByIds(Long[] ids);
}
