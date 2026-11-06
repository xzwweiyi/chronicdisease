package com.dchealth.cms.mapper;

import java.util.List;
import com.dchealth.cms.domain.CmsArticleSetEvaluation;

/**
 * 患教设置评测结果Mapper接口
 * 
 * @author lchbu
 * @date 2023-04-07
 */
public interface CmsArticleSetEvaluationMapper 
{
    /**
     * 查询患教设置评测结果
     * 
     * @param id 患教设置评测结果主键
     * @return 患教设置评测结果
     */
    public CmsArticleSetEvaluation selectCmsArticleSetEvaluationById(Long id);

    /**
     * 查询患教设置评测结果列表
     * 
     * @param cmsArticleSetEvaluation 患教设置评测结果
     * @return 患教设置评测结果集合
     */
    public List<CmsArticleSetEvaluation> selectCmsArticleSetEvaluationList(CmsArticleSetEvaluation cmsArticleSetEvaluation);

    /**
     * 新增患教设置评测结果
     * 
     * @param cmsArticleSetEvaluation 患教设置评测结果
     * @return 结果
     */
    public int insertCmsArticleSetEvaluation(CmsArticleSetEvaluation cmsArticleSetEvaluation);

    /**
     * 修改患教设置评测结果
     * 
     * @param cmsArticleSetEvaluation 患教设置评测结果
     * @return 结果
     */
    public int updateCmsArticleSetEvaluation(CmsArticleSetEvaluation cmsArticleSetEvaluation);

    /**
     * 删除患教设置评测结果
     * 
     * @param id 患教设置评测结果主键
     * @return 结果
     */
    public int deleteCmsArticleSetEvaluationById(Long id);

    /**
     * 批量删除患教设置评测结果
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCmsArticleSetEvaluationByIds(Long[] ids);
}
