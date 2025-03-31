package com.dchealth.template.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.template.mapper.FTemplateFormSetEvaluationMapper;
import com.dchealth.template.domain.FTemplateFormSetEvaluation;
import com.dchealth.template.service.IFTemplateFormSetEvaluationService;

/**
 * 单模板设置评测结果Service业务层处理
 * 
 * @author lchbu
 * @date 2023-04-04
 */
@Service
public class FTemplateFormSetEvaluationServiceImpl implements IFTemplateFormSetEvaluationService 
{
    @Autowired
    private FTemplateFormSetEvaluationMapper fTemplateFormSetEvaluationMapper;

    /**
     * 查询单模板设置评测结果
     * 
     * @param id 单模板设置评测结果主键
     * @return 单模板设置评测结果
     */
    @Override
    public FTemplateFormSetEvaluation selectFTemplateFormSetEvaluationById(Long id)
    {
        return fTemplateFormSetEvaluationMapper.selectFTemplateFormSetEvaluationById(id);
    }

    /**
     * 查询单模板设置评测结果列表
     * 
     * @param fTemplateFormSetEvaluation 单模板设置评测结果
     * @return 单模板设置评测结果
     */
    @Override
    public List<FTemplateFormSetEvaluation> selectFTemplateFormSetEvaluationList(FTemplateFormSetEvaluation fTemplateFormSetEvaluation)
    {
        return fTemplateFormSetEvaluationMapper.selectFTemplateFormSetEvaluationList(fTemplateFormSetEvaluation);
    }

    /**
     * 新增单模板设置评测结果
     * 
     * @param fTemplateFormSetEvaluation 单模板设置评测结果
     * @return 结果
     */
    @Override
    public int insertFTemplateFormSetEvaluation(FTemplateFormSetEvaluation fTemplateFormSetEvaluation)
    {
        fTemplateFormSetEvaluation.setCreateTime(DateUtils.getNowDate());
        return fTemplateFormSetEvaluationMapper.insertFTemplateFormSetEvaluation(fTemplateFormSetEvaluation);
    }

    /**
     * 修改单模板设置评测结果
     * 
     * @param fTemplateFormSetEvaluation 单模板设置评测结果
     * @return 结果
     */
    @Override
    public int updateFTemplateFormSetEvaluation(FTemplateFormSetEvaluation fTemplateFormSetEvaluation)
    {
        return fTemplateFormSetEvaluationMapper.updateFTemplateFormSetEvaluation(fTemplateFormSetEvaluation);
    }

    /**
     * 批量删除单模板设置评测结果
     * 
     * @param ids 需要删除的单模板设置评测结果主键
     * @return 结果
     */
    @Override
    public int deleteFTemplateFormSetEvaluationByIds(Long[] ids)
    {
        return fTemplateFormSetEvaluationMapper.deleteFTemplateFormSetEvaluationByIds(ids);
    }

    /**
     * 删除单模板设置评测结果信息
     * 
     * @param id 单模板设置评测结果主键
     * @return 结果
     */
    @Override
    public int deleteFTemplateFormSetEvaluationById(Long id)
    {
        return fTemplateFormSetEvaluationMapper.deleteFTemplateFormSetEvaluationById(id);
    }
}
