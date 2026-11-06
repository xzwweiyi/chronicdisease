package com.dchealth.cms.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.cms.mapper.CmsGuestImagesMapper;
import com.dchealth.cms.domain.CmsGuestImages;
import com.dchealth.cms.service.ICmsGuestImagesService;

/**
 * 资讯图片Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Service
public class CmsGuestImagesServiceImpl implements ICmsGuestImagesService 
{
    @Autowired
    private CmsGuestImagesMapper cmsGuestImagesMapper;

    /**
     * 查询资讯图片
     * 
     * @param id 资讯图片主键
     * @return 资讯图片
     */
    @Override
    public CmsGuestImages selectCmsGuestImagesById(Long id)
    {
        return cmsGuestImagesMapper.selectCmsGuestImagesById(id);
    }

    /**
     * 查询资讯图片列表
     * 
     * @param cmsGuestImages 资讯图片
     * @return 资讯图片
     */
    @Override
    public List<CmsGuestImages> selectCmsGuestImagesList(CmsGuestImages cmsGuestImages)
    {
        return cmsGuestImagesMapper.selectCmsGuestImagesList(cmsGuestImages);
    }

    /**
     * 新增资讯图片
     * 
     * @param cmsGuestImages 资讯图片
     * @return 结果
     */
    @Override
    public int insertCmsGuestImages(CmsGuestImages cmsGuestImages)
    {
        cmsGuestImages.setCreateTime(DateUtils.getNowDate());
        return cmsGuestImagesMapper.insertCmsGuestImages(cmsGuestImages);
    }

    /**
     * 修改资讯图片
     * 
     * @param cmsGuestImages 资讯图片
     * @return 结果
     */
    @Override
    public int updateCmsGuestImages(CmsGuestImages cmsGuestImages)
    {
        return cmsGuestImagesMapper.updateCmsGuestImages(cmsGuestImages);
    }

    /**
     * 批量删除资讯图片
     * 
     * @param ids 需要删除的资讯图片主键
     * @return 结果
     */
    @Override
    public int deleteCmsGuestImagesByIds(Long[] ids)
    {
        return cmsGuestImagesMapper.deleteCmsGuestImagesByIds(ids);
    }

    /**
     * 删除资讯图片信息
     * 
     * @param id 资讯图片主键
     * @return 结果
     */
    @Override
    public int deleteCmsGuestImagesById(Long id)
    {
        return cmsGuestImagesMapper.deleteCmsGuestImagesById(id);
    }
}
