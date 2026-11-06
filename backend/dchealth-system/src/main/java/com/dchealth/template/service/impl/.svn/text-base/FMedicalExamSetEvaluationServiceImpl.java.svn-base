package com.dchealth.template.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.template.domain.FMedicalExamSetEvaluation;
import com.dchealth.template.mapper.FMedicalExamSetEvaluationMapper;
import com.dchealth.template.service.IFMedicalExamSetEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 属性题目设置评价结果Service业务层处理
 * 
 * @author lchbu
 * @date 2023-04-03
 */
@Service
public class FMedicalExamSetEvaluationServiceImpl implements IFMedicalExamSetEvaluationService
{
    @Autowired
    private FMedicalExamSetEvaluationMapper fMedicalExamSetEvaluationMapper;

    /**
     * 查询属性题目设置评价结果
     * 
     * @param id 属性题目设置评价结果主键
     * @return 属性题目设置评价结果
     */
    @Override
    public FMedicalExamSetEvaluation selectFMedicalExamSetEvaluationById(Long id)
    {
        return fMedicalExamSetEvaluationMapper.selectFMedicalExamSetEvaluationById(id);
    }

    /**
     * 查询属性题目设置评价结果列表
     * 
     * @param fMedicalExamSetEvaluation 属性题目设置评价结果
     * @return 属性题目设置评价结果
     */
    @Override
    public List<FMedicalExamSetEvaluation> selectFMedicalExamSetEvaluationList(FMedicalExamSetEvaluation fMedicalExamSetEvaluation)
    {
        return fMedicalExamSetEvaluationMapper.selectFMedicalExamSetEvaluationList(fMedicalExamSetEvaluation);
    }

    /**
     * 新增属性题目设置评价结果
     * 
     * @param fMedicalExamSetEvaluation 属性题目设置评价结果
     * @return 结果
     */
    @Override
    public int insertFMedicalExamSetEvaluation(FMedicalExamSetEvaluation fMedicalExamSetEvaluation)
    {
        fMedicalExamSetEvaluation.setCreateTime(DateUtils.getNowDate());
        return fMedicalExamSetEvaluationMapper.insertFMedicalExamSetEvaluation(fMedicalExamSetEvaluation);
    }

    /**
     * 修改属性题目设置评价结果
     * 
     * @param fMedicalExamSetEvaluation 属性题目设置评价结果
     * @return 结果
     */
    @Override
    public int updateFMedicalExamSetEvaluation(FMedicalExamSetEvaluation fMedicalExamSetEvaluation)
    {
        return fMedicalExamSetEvaluationMapper.updateFMedicalExamSetEvaluation(fMedicalExamSetEvaluation);
    }

    /**
     * 批量删除属性题目设置评价结果
     * 
     * @param ids 需要删除的属性题目设置评价结果主键
     * @return 结果
     */
    @Override
    public int deleteFMedicalExamSetEvaluationByIds(Long[] ids)
    {
        return fMedicalExamSetEvaluationMapper.deleteFMedicalExamSetEvaluationByIds(ids);
    }

    /**
     * 删除属性题目设置评价结果信息
     * 
     * @param id 属性题目设置评价结果主键
     * @return 结果
     */
    @Override
    public int deleteFMedicalExamSetEvaluationById(Long id)
    {
        return fMedicalExamSetEvaluationMapper.deleteFMedicalExamSetEvaluationById(id);
    }
}
