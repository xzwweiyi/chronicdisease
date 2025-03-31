package com.dchealth.cro.cms.mapper;

import com.dchealth.cro.cms.domain.CmsArticle;
import com.dchealth.cro.cms.form.ActicleListSearchForm;
import com.dchealth.cro.cms.form.CollectionActicleSearchForm;
import com.dchealth.cro.cms.vo.ArticleListVO;
import com.dchealth.cro.research.domain.ResearchInfoArticlePatient;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * 文章Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-06
 */
@Repository
public interface CmsArticleMapper 
{
    /**
     * 查询文章
     * 
     * @param id 文章ID
     * @return 文章
     */
    public CmsArticle selectCmsArticleById(Long id);
    
    public CmsArticle   selectArticleTitleById(Long id);

    /**
     * 查询文章列表
     * 
     * @param cmsArticle 文章
     * @return 文章集合
     */
    public List<ArticleListVO> selectCmsArticleList(ActicleListSearchForm cmsArticle);

    public List<ArticleListVO> selectCmsArticlePatientList(ResearchInfoArticlePatient researchInfoArticlePatient);

    public List<ArticleListVO> selectAllCmsArticlePatientList(ResearchInfoArticlePatient researchInfoArticlePatient);


    public List<ArticleListVO>  selectAllDefaultCmsArticlePatientList(ResearchInfoArticlePatient researchInfoArticlePatient);

    public List<ArticleListVO> selectOwnQuestionnaireList(ResearchInfoArticlePatient researchInfoArticlePatient);

    public List<ArticleListVO> selectAllQuestionnaireList(ResearchInfoArticlePatient researchInfoArticlePatient);


    /**
     * 新增文章
     * 
     * @param cmsArticle 文章
     * @return 结果
     */
    public int insertCmsArticle(CmsArticle cmsArticle);

    /**
     * 修改文章
     * 
     * @param cmsArticle 文章
     * @return 结果
     */
    public int updateCmsArticle(CmsArticle cmsArticle);

    /**
     * 删除文章
     * 
     * @param id 文章ID
     * @return 结果
     */
    public int deleteCmsArticleById(Long id);

    /**
     * 批量删除文章
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCmsArticleByIds(String[] ids);

	public List<ArticleListVO> selectCollectArticleList(CollectionActicleSearchForm collectionActicleSearchForm);

    /**
     * 待填写的问卷list
     * @param researchInfoArticlePatient
     * @return
     */
    public  List<ArticleListVO> selectWriteQuestionnaireList(ResearchInfoArticlePatient researchInfoArticlePatient);

    public List<ArticleListVO> selectProgressQuestionnaireList(ResearchInfoArticlePatient researchInfoArticlePatient);

    public List<ArticleListVO> selectProgressQuestionnaireListByPage(ResearchInfoArticlePatient researchInfoArticlePatient);
    public int selectProgressQuestionnaireCountByPage(ResearchInfoArticlePatient researchInfoArticlePatient);

    public CmsArticle selectCmsArticleByArticleId(Long id);
}
