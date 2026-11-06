package com.dchealth.cms.service;

import com.dchealth.cms.domain.CmsEduCategory;

import java.util.List;

/**
 * 宣教分类Service接口
 *
 * @author dchealth
 * @date 2026-09-02
 */
public interface ICmsEduCategoryService {

    /**
     * 查询宣教分类
     *
     * @param id 宣教分类主键
     * @return 宣教分类
     */
    CmsEduCategory selectCmsEduCategoryById(Long id);

    /**
     * 查询宣教分类列表
     *
     * @param cmsEduCategory 宣教分类
     * @return 宣教分类集合
     */
    List<CmsEduCategory> selectCmsEduCategoryList(CmsEduCategory cmsEduCategory);

    /**
     * 新增宣教分类
     *
     * @param cmsEduCategory 宣教分类
     * @return 结果
     */
    int insertCmsEduCategory(CmsEduCategory cmsEduCategory);

    /**
     * 修改宣教分类
     *
     * @param cmsEduCategory 宣教分类
     * @return 结果
     */
    int updateCmsEduCategory(CmsEduCategory cmsEduCategory);

    /**
     * 批量删除宣教分类
     *
     * @param ids 需要删除的宣教分类主键
     * @return 结果
     */
    int deleteCmsEduCategoryByIds(Long[] ids);

    /**
     * 删除宣教分类信息
     *
     * @param id 宣教分类主键
     * @return 结果
     */
    int deleteCmsEduCategoryById(Long id);
}
