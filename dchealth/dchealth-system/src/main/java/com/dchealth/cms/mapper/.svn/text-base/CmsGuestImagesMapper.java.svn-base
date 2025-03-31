package com.dchealth.cms.mapper;

import com.dchealth.cms.domain.CmsGuestImages;

import java.util.List;

/**
 * 资讯图片Mapper接口
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
public interface CmsGuestImagesMapper 
{
    /**
     * 查询资讯图片
     * 
     * @param id 资讯图片主键
     * @return 资讯图片
     */
    public CmsGuestImages selectCmsGuestImagesById(Long id);

    /**
     * 查询资讯图片列表
     * 
     * @param cmsGuestImages 资讯图片
     * @return 资讯图片集合
     */
    public List<CmsGuestImages> selectCmsGuestImagesList(CmsGuestImages cmsGuestImages);

    /**
     * 新增资讯图片
     * 
     * @param cmsGuestImages 资讯图片
     * @return 结果
     */
    public int insertCmsGuestImages(CmsGuestImages cmsGuestImages);

    /**
     * 修改资讯图片
     * 
     * @param cmsGuestImages 资讯图片
     * @return 结果
     */
    public int updateCmsGuestImages(CmsGuestImages cmsGuestImages);

    /**
     * 删除资讯图片
     * 
     * @param id 资讯图片主键
     * @return 结果
     */
    public int deleteCmsGuestImagesById(Long id);

    /**
     * 批量删除资讯图片
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCmsGuestImagesByIds(Long[] ids);

    List<CmsGuestImages> selectCmsGuestImagesByGuestbookId(Long guestbookId);
}
