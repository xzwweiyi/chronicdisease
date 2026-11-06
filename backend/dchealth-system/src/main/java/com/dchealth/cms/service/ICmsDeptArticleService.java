package com.dchealth.cms.service;

import com.dchealth.cms.domain.CmsArticle;
import com.dchealth.cms.domain.CmsCategory;
import com.dchealth.cms.domain.CmsDeptArticle;
import com.dchealth.cms.domain.CmsDeptArticleParam;

import java.util.List;

/**
 * 资讯管理Service接口
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
public interface ICmsDeptArticleService
{
     List<CmsDeptArticle> batchSelectCmsDeptArticle(Long deptId,List<Long> articleIdList);
     List<CmsDeptArticle> selectCmsDeptArticle(CmsDeptArticle cmsDeptArticle);

     int insertCmsDeptArticle(CmsDeptArticle cmsDeptArticle);


     int deleteCmsDeptArticleById(Long deptId,Long articleId);

     int batchInsertCmsDeptArticle(CmsDeptArticleParam cmsDeptArticleParam, CmsCategory cmsCategory, List<CmsArticle> cmsArticles);
}
