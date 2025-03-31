package com.dchealth.cms.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.cms.mapper.CmsArticleSetEvaluationMapper;
import com.dchealth.cms.domain.CmsArticleSetEvaluation;
import com.dchealth.cms.service.ICmsArticleSetEvaluationService;

/**
 * 患教设置评测结果Service业务层处理
 * 
 * @author lchbu
 * @date 2023-04-07
 */
@Service
public class CmsArticleSetEvaluationServiceImpl implements ICmsArticleSetEvaluationService 
{
    @Autowired
    private CmsArticleSetEvaluationMapper cmsArticleSetEvaluationMapper;

    /**
     * 查询患教设置评测结果
     * 
     * @param id 患教设置评测结果主键
     * @return 患教设置评测结果
     */
    @Override
    public CmsArticleSetEvaluation selectCmsArticleSetEvaluationById(Long id)
    {
        return cmsArticleSetEvaluationMapper.selectCmsArticleSetEvaluationById(id);
    }

    /**
     * 查询患教设置评测结果列表
     * 
     * @param cmsArticleSetEvaluation 患教设置评测结果
     * @return 患教设置评测结果
     */
    @Override
    public List<CmsArticleSetEvaluation> selectCmsArticleSetEvaluationList(CmsArticleSetEvaluation cmsArticleSetEvaluation)
    {
        return cmsArticleSetEvaluationMapper.selectCmsArticleSetEvaluationList(cmsArticleSetEvaluation);
    }

    /**
     * 新增患教设置评测结果
     * 
     * @param cmsArticleSetEvaluation 患教设置评测结果
     * @return 结果
     */
    @Override
    public int insertCmsArticleSetEvaluation(CmsArticleSetEvaluation cmsArticleSetEvaluation)
    {
        cmsArticleSetEvaluation.setCreateTime(DateUtils.getNowDate());
        return cmsArticleSetEvaluationMapper.insertCmsArticleSetEvaluation(cmsArticleSetEvaluation);
    }

    /**
     * 修改患教设置评测结果
     * 
     * @param cmsArticleSetEvaluation 患教设置评测结果
     * @return 结果
     */
    @Override
    public int updateCmsArticleSetEvaluation(CmsArticleSetEvaluation cmsArticleSetEvaluation)
    {
        return cmsArticleSetEvaluationMapper.updateCmsArticleSetEvaluation(cmsArticleSetEvaluation);
    }

    /**
     * 批量删除患教设置评测结果
     * 
     * @param ids 需要删除的患教设置评测结果主键
     * @return 结果
     */
    @Override
    public int deleteCmsArticleSetEvaluationByIds(Long[] ids)
    {
        return cmsArticleSetEvaluationMapper.deleteCmsArticleSetEvaluationByIds(ids);
    }

    /**
     * 删除患教设置评测结果信息
     * 
     * @param id 患教设置评测结果主键
     * @return 结果
     */
    @Override
    public int deleteCmsArticleSetEvaluationById(Long id)
    {
        return cmsArticleSetEvaluationMapper.deleteCmsArticleSetEvaluationById(id);
    }
}
