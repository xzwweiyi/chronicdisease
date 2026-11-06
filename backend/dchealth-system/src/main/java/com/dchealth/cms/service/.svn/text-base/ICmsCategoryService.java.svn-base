package com.dchealth.cms.service;

import com.dchealth.cms.domain.CmsCategory;
import com.dchealth.cms.domain.CmsCategoryTree;
import com.dchealth.cms.domain.vo.CmsArticleCateGoryVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 资讯分类Service接口
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
public interface ICmsCategoryService 
{
    /**
     * 查询资讯分类
     * 
     * @param id 资讯分类主键
     * @return 资讯分类
     */
    public CmsCategory selectCmsCategoryById(Long id);


    /**
     * 查询资讯分类(批量)
     *
     * @param ids
     * @return 资讯分类
     */
    public List<CmsCategory> selectCmsCategoryListByIds(Set<String> ids);

    /**
     * 查询资讯分类列表
     * 
     * @param cmsCategory 资讯分类
     * @return 资讯分类集合
     */
    public List<CmsCategory> selectCmsCategoryList(CmsCategory cmsCategory);

    /**
     * 查询分类树
     * @param cmsCategory
     * @return
     */
    public List<CmsCategoryTree> selectCmsCategoryTree(CmsCategory cmsCategory, Map<String, List<CmsArticleCateGoryVo>> categotyArticleMap);

    /**
     * 查询分类树
     * @param cmsCategory
     * @return
     */
    public List<CmsCategoryTree> selectCmsCategoryTree(CmsCategory cmsCategory);

    /**
     * 新增资讯分类
     * 
     * @param cmsCategory 资讯分类
     * @return 结果
     */
    public int insertCmsCategory(CmsCategory cmsCategory);

    /**
     * 修改资讯分类
     * 
     * @param cmsCategory 资讯分类
     * @return 结果
     */
    public int updateCmsCategory(CmsCategory cmsCategory);

    /**
     * 批量删除资讯分类
     * 
     * @param ids 需要删除的资讯分类主键集合
     * @return 结果
     */
    public int deleteCmsCategoryByIds(Long[] ids);

    /**
     * 删除资讯分类信息
     * 
     * @param id 资讯分类主键
     * @return 结果
     */
    public int deleteCmsCategoryById(Long id);

    /**
     * 逻辑删除分类
     * @param id
     * @return
     */
    int updateCmsCategoryById(Long id);

    public int updateDeptCmsCategoryById(Long id);

    /**
     * 根据科室id查询分类
     * @param deptId
     * @return
     */
    List<CmsCategory> selectCmsCategoryByDeptId(Long deptId);


    List<CmsCategory> selectAllChildCategoryById(Long deptCategoryId);
}
