package com.dchealth.template.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.template.mapper.FLibraryFormSetEvaluationMapper;
import com.dchealth.template.domain.FLibraryFormSetEvaluation;
import com.dchealth.template.service.IFLibraryFormSetEvaluationService;

/**
 * 标准量设置评价结果Service业务层处理
 * 
 * @author lchbu
 * @date 2023-04-03
 */
@Service
public class FLibraryFormSetEvaluationServiceImpl implements IFLibraryFormSetEvaluationService 
{
    @Autowired
    private FLibraryFormSetEvaluationMapper fLibraryFormSetEvaluationMapper;

    /**
     * 查询标准量设置评价结果
     * 
     * @param id 标准量设置评价结果主键
     * @return 标准量设置评价结果
     */
    @Override
    public FLibraryFormSetEvaluation selectFLibraryFormSetEvaluationById(Long id)
    {
        return fLibraryFormSetEvaluationMapper.selectFLibraryFormSetEvaluationById(id);
    }

    /**
     * 查询标准量设置评价结果列表
     * 
     * @param fLibraryFormSetEvaluation 标准量设置评价结果
     * @return 标准量设置评价结果
     */
    @Override
    public List<FLibraryFormSetEvaluation> selectFLibraryFormSetEvaluationList(FLibraryFormSetEvaluation fLibraryFormSetEvaluation)
    {
        return fLibraryFormSetEvaluationMapper.selectFLibraryFormSetEvaluationList(fLibraryFormSetEvaluation);
    }

    /**
     * 新增标准量设置评价结果
     * 
     * @param fLibraryFormSetEvaluation 标准量设置评价结果
     * @return 结果
     */
    @Override
    public int insertFLibraryFormSetEvaluation(FLibraryFormSetEvaluation fLibraryFormSetEvaluation)
    {
        fLibraryFormSetEvaluation.setCreateTime(DateUtils.getNowDate());
        return fLibraryFormSetEvaluationMapper.insertFLibraryFormSetEvaluation(fLibraryFormSetEvaluation);
    }

    /**
     * 修改标准量设置评价结果
     * 
     * @param fLibraryFormSetEvaluation 标准量设置评价结果
     * @return 结果
     */
    @Override
    public int updateFLibraryFormSetEvaluation(FLibraryFormSetEvaluation fLibraryFormSetEvaluation)
    {
        return fLibraryFormSetEvaluationMapper.updateFLibraryFormSetEvaluation(fLibraryFormSetEvaluation);
    }

    /**
     * 批量删除标准量设置评价结果
     * 
     * @param ids 需要删除的标准量设置评价结果主键
     * @return 结果
     */
    @Override
    public int deleteFLibraryFormSetEvaluationByIds(Long[] ids)
    {
        return fLibraryFormSetEvaluationMapper.deleteFLibraryFormSetEvaluationByIds(ids);
    }

    /**
     * 删除标准量设置评价结果信息
     * 
     * @param id 标准量设置评价结果主键
     * @return 结果
     */
    @Override
    public int deleteFLibraryFormSetEvaluationById(Long id)
    {
        return fLibraryFormSetEvaluationMapper.deleteFLibraryFormSetEvaluationById(id);
    }
}
