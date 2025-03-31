package com.dchealth.cms.service.impl;

import java.util.List;

import com.dchealth.cms.domain.CmsGuestImages;
import com.dchealth.cms.mapper.CmsGuestImagesMapper;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.cms.mapper.CmsGuestbookMapper;
import com.dchealth.cms.domain.CmsGuestbook;
import com.dchealth.cms.service.ICmsGuestbookService;

/**
 * 反馈管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Service
public class CmsGuestbookServiceImpl implements ICmsGuestbookService 
{
    @Autowired
    private CmsGuestbookMapper cmsGuestbookMapper;

    @Autowired
    private CmsGuestImagesMapper cmsGuestImagesMapper;

    /**
     * 查询反馈管理
     * 
     * @param id 反馈管理主键
     * @return 反馈管理
     */
    @Override
    public CmsGuestbook selectCmsGuestbookById(Long id)
    {
        CmsGuestbook  cmsGuestbook = cmsGuestbookMapper.selectCmsGuestbookById(id);
        if(cmsGuestbook != null ) {
            List<CmsGuestImages>  list = cmsGuestImagesMapper.selectCmsGuestImagesByGuestbookId(id);
            cmsGuestbook.setCmsGuestImagesList(list);
        }

        return cmsGuestbook;
    }

    /**
     * 查询反馈管理列表
     * 
     * @param cmsGuestbook 反馈管理
     * @return 反馈管理
     */
    @Override
    public List<CmsGuestbook> selectCmsGuestbookList(CmsGuestbook cmsGuestbook)
    {
        return cmsGuestbookMapper.selectCmsGuestbookList(cmsGuestbook);
    }

    /**
     * 新增反馈管理
     * 
     * @param cmsGuestbook 反馈管理
     * @return 结果
     */
    @Override
    public int insertCmsGuestbook(CmsGuestbook cmsGuestbook)
    {
        cmsGuestbook.setCreateTime(DateUtils.getNowDate());
        return cmsGuestbookMapper.insertCmsGuestbook(cmsGuestbook);
    }

    /**
     * 修改反馈管理
     * 
     * @param cmsGuestbook 反馈管理
     * @return 结果
     */
    @Override
    public int updateCmsGuestbook(CmsGuestbook cmsGuestbook)
    {
        insertGuestImages(cmsGuestbook);
        return cmsGuestbookMapper.updateCmsGuestbook(cmsGuestbook);
    }

    /**
     * 插入图片
     *
     */
    private int insertGuestImages( CmsGuestbook  cmsGuestbook) {
        List<String>  guestImagesList = cmsGuestbook.getGuestImages();
        if(guestImagesList != null && guestImagesList.size() >0 ) {
            for (String guestImages : guestImagesList) {
                if(StringUtils.isNotBlankAndNotEmpty(guestImages)) {
                    CmsGuestImages cmsGuestImages = new CmsGuestImages();
                    cmsGuestImages.setCreateTime(DateUtils.getNowDate());
                    cmsGuestImages.setGuestbookId(cmsGuestbook.getId());
                    cmsGuestImages.setImageUrl(guestImages);
                    cmsGuestImagesMapper.insertCmsGuestImages(cmsGuestImages);
                }
            }
        }

        return 1;
    }

    /**
     * 批量删除反馈管理
     * 
     * @param ids 需要删除的反馈管理主键
     * @return 结果
     */
    @Override
    public int deleteCmsGuestbookByIds(Long[] ids)
    {
        return cmsGuestbookMapper.updateDeleteByIds(ids);
    }

    /**
     * 删除反馈管理信息
     * 
     * @param id 反馈管理主键
     * @return 结果
     */
    @Override
    public int deleteCmsGuestbookById(Long id)
    {
        return cmsGuestbookMapper.deleteCmsGuestbookById(id);
    }
}
