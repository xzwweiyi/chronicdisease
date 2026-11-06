package com.dchealth.research.service;

import java.util.List;

import com.dchealth.cms.domain.CmsArticle;
import com.dchealth.research.domain.ResearchInfoArticle;

/**
 * 队列-患教关系Service接口
 * 
 * @author lchbu
 * @date 2022-08-25
 */
public interface IResearchInfoArticleService 
{
    /**
     * 查询队列-患教关系
     * 
     * @param researchId 队列-患教关系主键
     * @return 队列-患教关系
     */
    public List<ResearchInfoArticle> selectResearchInfoArticleByResearchId(Long researchId);

    /**
     * 查询队列-患教关系列表
     * 
     * @param researchInfoArticle 队列-患教关系
     * @return 队列-患教关系集合
     */
    public List<ResearchInfoArticle> selectResearchInfoArticleList(ResearchInfoArticle researchInfoArticle);

    /**
     * 新增队列-患教关系
     * 
     * @param researchInfoArticles 队列-患教关系
     * @return 结果
     */
    public int insertResearchInfoArticle(List<ResearchInfoArticle> researchInfoArticles);

    /**
     * 修改队列-患教关系
     * 
     * @param researchInfoArticle 队列-患教关系
     * @return 结果
     */
    public int updateResearchInfoArticle(ResearchInfoArticle researchInfoArticle);

    /**
     * 批量删除队列-患教关系
     * 
     * @param researchIds 需要删除的队列-患教关系主键集合
     * @return 结果
     */
    public int deleteResearchInfoArticleByResearchIds(Long[] researchIds);

    /**
     * 删除队列-患教关系信息
     * 
     * @param researchId 队列-患教关系主键
     * @return 结果
     */
    public int deleteResearchInfoArticleByResearchId(Long researchId);

    /**
     * 查询队列-病种下的患教列表
     * @param researchInfoArticle
     * @return
     */
    List<CmsArticle> selectListArticle(ResearchInfoArticle researchInfoArticle);

    /**
     * 移除队列患教关系
     * @param researchInfoArticle
     * @return
     */
    int removeArticle(ResearchInfoArticle researchInfoArticle);


    public int updateResearchArticleType(ResearchInfoArticle researchInfoArticle);
}
