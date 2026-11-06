package com.dchealth.cms.mapper;

import com.dchealth.cms.domain.CmsArticle;
import com.dchealth.cms.domain.CmsArticleRecord;
import com.dchealth.cms.domain.vo.CmsArticleCateGoryVo;

import java.util.List;

/**
 * 资讯管理Mapper接口
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
public interface CmsArticleMapper 
{
    /**
     * 查询资讯管理
     * 
     * @param id 资讯管理主键
     * @return 资讯管理
     */
    public CmsArticle selectCmsArticleById(Long id);

    /**
     * 查询资讯管理列表
     * 
     * @param cmsArticle 资讯管理
     * @return 资讯管理集合
     */
    public List<CmsArticle> selectCmsArticleList(CmsArticle cmsArticle);

    /**
     * 新增资讯管理
     * 
     * @param cmsArticle 资讯管理
     * @return 结果
     */
    public int insertCmsArticle(CmsArticle cmsArticle);

    /**
     * 修改资讯管理
     * 
     * @param cmsArticle 资讯管理
     * @return 结果
     */
    public int updateCmsArticle(CmsArticle cmsArticle);

    /**
     * 删除资讯管理
     * 
     * @param id 资讯管理主键
     * @return 结果
     */
    public int deleteCmsArticleById(Long id);

    /**
     * 批量删除资讯管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCmsArticleByIds(Long[] ids);

    int updateCmsArticleByDiseaseId(Long diseaseId);

    /**
     * 逻辑删除分类底下的患教内容
     * @param id
     */
    void updateCmsArticleDelByCategoryId(Long id);

    /**
     * 逻辑删除
     * @param id
     * @return
     */
    int updateDelById(Long id);

    /**
     * 查询咨询信息
     * @param articleIds
     * @return
     */
    List<CmsArticle> selectCmsArticleByIds(List<Long> articleIds);

    /**
     * 根据患者id查询相关患教信息
     * @param accountId
     * @return
     */
    List<CmsArticle> selectCmsArticleByAccountId(Long accountId);

    /**
     * 自评量表发布
     * @param id
     */
    int updateCmsArticleStatusById(Long id);

    /**
     * 查询问卷最大排序值
     * @return
     */
    Integer selectCmsArticleMaxQuesNum();

    List<CmsArticle> selectCmsDeptArticleList(CmsArticle cmsArticle);

    List<CmsArticleCateGoryVo> selectCmsDeptCategoryArticleList(CmsArticle cmsArticle);

    List<CmsArticleRecord> selectCmsArticleRecord(CmsArticleRecord cmsArticleRecord);

    List<CmsArticle> selectCmsInformationList(CmsArticle cmsArticle);

    int batchUpdateCmsArticleCategory(List<CmsArticle> cmsArticles);
}
