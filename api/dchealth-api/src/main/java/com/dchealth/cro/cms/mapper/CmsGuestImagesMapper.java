package com.dchealth.cro.cms.mapper;

import com.dchealth.cro.cms.domain.CmsGuestImages;
import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * 留言图片Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-10
 */
@Repository
public interface CmsGuestImagesMapper 
{
    /**
     * 查询留言图片
     * 
     * @param id 留言图片ID
     * @return 留言图片
     */
    public CmsGuestImages selectCmsGuestImagesById(Long id);

    /**
     * 查询留言图片列表
     * 
     * @param cmsGuestImages 留言图片
     * @return 留言图片集合
     */
    public List<CmsGuestImages> selectCmsGuestImagesList(CmsGuestImages cmsGuestImages);

    /**
     * 新增留言图片
     * 
     * @param cmsGuestImages 留言图片
     * @return 结果
     */
    public int insertCmsGuestImages(CmsGuestImages cmsGuestImages);

    /**
     * 修改留言图片
     * 
     * @param cmsGuestImages 留言图片
     * @return 结果
     */
    public int updateCmsGuestImages(CmsGuestImages cmsGuestImages);

    /**
     * 删除留言图片
     * 
     * @param id 留言图片ID
     * @return 结果
     */
    public int deleteCmsGuestImagesById(Long id);

    /**
     * 批量删除留言图片
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCmsGuestImagesByIds(String[] ids);
}
