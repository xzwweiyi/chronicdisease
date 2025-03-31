package com.dchealth.research.mapper;

import com.dchealth.cms.domain.ResearchInfoArticlePatient;
import com.dchealth.research.domain.ResearchInfoArticle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 队列-患教关系Mapper接口
 * 
 * @author lchbu
 * @date 2022-08-25
 */
public interface ResearchInfoArticleMapper 
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

    public List<ResearchInfoArticle> selectResearchInfoArticleList2(ResearchInfoArticle researchInfoArticle);

    /**
     * 新增队列-患教关系
     * 
     * @param researchInfoArticle 队列-患教关系
     * @return 结果
     */
    public int insertResearchInfoArticle(ResearchInfoArticle researchInfoArticle);

    /**
     * 修改队列-患教关系
     * 
     * @param researchInfoArticle 队列-患教关系
     * @return 结果
     */
    public int updateResearchInfoArticle(ResearchInfoArticle researchInfoArticle);

    /**
     * 删除队列-患教关系
     * 
     * @param researchId 队列-患教关系主键
     * @return 结果
     */
    public int deleteResearchInfoArticleByResearchId(Long researchId);

    /**
     * 批量删除队列-患教关系
     * 
     * @param researchIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResearchInfoArticleByResearchIds(Long[] researchIds);

    /**
     * 移除患教关系
     * @param researchInfoArticle
     * @return
     */
    int deleteResearchArticle(ResearchInfoArticle researchInfoArticle);



    int updateResearchArticleType(ResearchInfoArticle researchInfoArticle);

    List<ResearchInfoArticle> queryByArticleIdAndAccountId(@Param("articleId") Long articleId,@Param("accountId")  Long accountId);

    ResearchInfoArticlePatient selectResearchInfoArticlePatient(ResearchInfoArticlePatient articlePatient);


    int deleteResearchInfoArticleByArticleId(@Param("articleId") Long articleId);

    int deleteResearchInfoArticleByCategoryId(@Param("categoryId") Long categoryId);
}
