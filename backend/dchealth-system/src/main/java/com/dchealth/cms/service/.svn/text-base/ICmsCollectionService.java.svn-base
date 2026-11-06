package com.dchealth.cms.service;

import com.dchealth.cms.domain.CmsCollection;

import java.util.List;

/**
 * 用户收藏Service接口
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
public interface ICmsCollectionService 
{
    /**
     * 查询用户收藏
     * 
     * @param id 用户收藏主键
     * @return 用户收藏
     */
    public CmsCollection selectCmsCollectionById(Long id);

    /**
     * 查询用户收藏列表
     * 
     * @param cmsCollection 用户收藏
     * @return 用户收藏集合
     */
    public List<CmsCollection> selectCmsCollectionList(CmsCollection cmsCollection);

    /**
     * 新增用户收藏
     * 
     * @param cmsCollection 用户收藏
     * @return 结果
     */
    public int insertCmsCollection(CmsCollection cmsCollection);

    /**
     * 修改用户收藏
     * 
     * @param cmsCollection 用户收藏
     * @return 结果
     */
    public int updateCmsCollection(CmsCollection cmsCollection);

    /**
     * 批量删除用户收藏
     * 
     * @param ids 需要删除的用户收藏主键集合
     * @return 结果
     */
    public int deleteCmsCollectionByIds(Long[] ids);

    /**
     * 删除用户收藏信息
     * 
     * @param id 用户收藏主键
     * @return 结果
     */
    public int deleteCmsCollectionById(Long id);
}
