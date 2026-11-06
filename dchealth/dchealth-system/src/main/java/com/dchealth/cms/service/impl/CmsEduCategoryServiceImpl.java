package com.dchealth.cms.service.impl;

import com.dchealth.cms.domain.CmsEduCategory;
import com.dchealth.cms.mapper.CmsEduCategoryMapper;
import com.dchealth.cms.service.ICmsEduCategoryService;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 宣教分类Service业务层处理
 *
 * @author dchealth
 * @date 2026-09-02
 */
@Service
public class CmsEduCategoryServiceImpl implements ICmsEduCategoryService {

    @Autowired
    private CmsEduCategoryMapper cmsEduCategoryMapper;

    /**
     * 查询宣教分类
     *
     * @param id 宣教分类主键
     * @return 宣教分类
     */
    @Override
    public CmsEduCategory selectCmsEduCategoryById(Long id) {
        return cmsEduCategoryMapper.selectCmsEduCategoryById(id);
    }

    /**
     * 查询宣教分类列表
     *
     * @param cmsEduCategory 宣教分类
     * @return 宣教分类
     */
    @Override
    public List<CmsEduCategory> selectCmsEduCategoryList(CmsEduCategory cmsEduCategory) {
        return cmsEduCategoryMapper.selectCmsEduCategoryList(cmsEduCategory);
    }

    /**
     * 新增宣教分类
     *
     * @param cmsEduCategory 宣教分类
     * @return 结果
     */
    @Override
    public int insertCmsEduCategory(CmsEduCategory cmsEduCategory) {
        cmsEduCategory.setCreateTime(DateUtils.getNowDate());
        cmsEduCategory.setUpdateTime(DateUtils.getNowDate());
        cmsEduCategory.setDelFlag("N");
        return cmsEduCategoryMapper.insertCmsEduCategory(cmsEduCategory);
    }

    /**
     * 修改宣教分类
     *
     * @param cmsEduCategory 宣教分类
     * @return 结果
     */
    @Override
    public int updateCmsEduCategory(CmsEduCategory cmsEduCategory) {
        cmsEduCategory.setUpdateTime(DateUtils.getNowDate());
        return cmsEduCategoryMapper.updateCmsEduCategory(cmsEduCategory);
    }

    /**
     * 批量删除宣教分类（逻辑删除）
     *
     * @param ids 需要删除的宣教分类主键
     * @return 结果
     */
    @Override
    public int deleteCmsEduCategoryByIds(Long[] ids) {
        return cmsEduCategoryMapper.deleteCmsEduCategoryByIds(ids);
    }

    /**
     * 删除宣教分类信息（逻辑删除）
     *
     * @param id 宣教分类主键
     * @return 结果
     */
    @Override
    public int deleteCmsEduCategoryById(Long id) {
        return cmsEduCategoryMapper.deleteCmsEduCategoryById(id);
    }
}
