package com.dchealth.cms.mapper;

import com.dchealth.cms.domain.CmsDeptArticle;
import com.dchealth.cms.domain.CmsDeptArticleMoveDo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资讯详情Mapper接口
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
public interface CmsDeptArticleMapper
{

    /**
     * 新增部门资讯关系
     * 
     * @param cmsArticleData 资讯详情
     * @return 结果
     */
    public int insertCmsDeptArticle(CmsDeptArticle cmsArticleData);

    /**
     * 移除部门资讯关系
     * 
     * @param deptId 科室ID
     * @param articleId 资讯ID
     * @return 结果
     */
    public int deleteCmsDeptArticleById(@Param("deptId") Long deptId, @Param("articleId")Long articleId);


    public int deleteCmsDeptArticleByCategoryId(@Param("categoryId") Long categoryId);




    List<CmsDeptArticle> selectCmsDeptArticle(CmsDeptArticle cmsDeptArticle);


    int updateCmsDeptArticleCategoryAndDeptId(@Param("list") List<CmsDeptArticleMoveDo> cmsDeptArticleMoveDos);

    List<CmsDeptArticle> batchSelectCmsDeptArticle(@Param("deptId") Long deptId, @Param("list")List<Long> articleIdList);


    int batchInsertCmsDeptArticle(List<CmsDeptArticle> insertCmsDeptArticelList);

    int updateCmsDeptArticleCategory(@Param("list") List<CmsDeptArticle> cmsDeptArticleList);

    List<CmsDeptArticle> selectCmsDeptArticleByArticleIdList(@Param("list") List<Long> articleIdList);
}
