package com.dchealth.cms.service;

import com.dchealth.cms.domain.CmsArticle;
import com.dchealth.cms.domain.CmsArticleMoveParam;
import com.dchealth.cms.domain.CmsArticleRecord;
import com.dchealth.cms.domain.CmsDeptArticle;
import com.dchealth.cms.domain.FMedicalExamRes;
import com.dchealth.cms.domain.vo.CmsArticleCateGoryVo;
import com.dchealth.template.domain.FMedicalExamForm;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 资讯管理Service接口
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
public interface ICmsArticleService 
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


    void edcExportData(HttpServletResponse response, List<Long> examRecordIds);

    List<CmsArticle> selectCmsInformationList(CmsArticle cmsArticleParam);

    /**
     * 查询资讯管理列表
     *
     * @param cmsArticle 资讯管理
     * @return 资讯管理集合
     */
    public List<CmsArticle> selectCmsDeptArticleList(CmsArticle cmsArticle);

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
     * 批量删除资讯管理
     * 
     * @param ids 需要删除的资讯管理主键集合
     * @return 结果
     */
    public int deleteCmsArticleByIds(Long[] ids);

    /**
     * 删除资讯管理信息
     * 
     * @param id 资讯管理主键
     * @return 结果
     */
    public int deleteCmsArticleById(Long id);

    int deleteCmsArticleByDiseaseId(Long diseaseId);

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
     * 问卷发布
     * @param id
     * @return
     */
    int updateCmsArticleStatusById(Long id);


    /**
     * 查询科室分类资讯列表
     * @param cmsArticle
     * @return
     */
    public List<CmsArticleCateGoryVo> selectCmsDeptCategoryArticleList(CmsArticle cmsArticle);

    List<CmsArticleRecord> selectCmsArticleRecord(CmsArticleRecord cmsArticleRecord);

    FMedicalExamForm selectExamFromDetailInfo(FMedicalExamRes fMedicalExamRes);

    Integer fileMove(CmsArticleMoveParam articleMoveParam);

    Integer deptfileMove(CmsArticleMoveParam articleMoveParam);

    List<CmsDeptArticle> selectCmsDeptArticleByArticleIdList(List<Long> articleIdList);
}
