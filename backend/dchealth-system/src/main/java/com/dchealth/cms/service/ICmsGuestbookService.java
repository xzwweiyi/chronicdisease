package com.dchealth.cms.service;

import com.dchealth.cms.domain.CmsGuestbook;

import java.util.List;

/**
 * 反馈管理Service接口
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
public interface ICmsGuestbookService 
{
    /**
     * 查询反馈管理
     * 
     * @param id 反馈管理主键
     * @return 反馈管理
     */
    public CmsGuestbook selectCmsGuestbookById(Long id);

    /**
     * 查询反馈管理列表
     * 
     * @param cmsGuestbook 反馈管理
     * @return 反馈管理集合
     */
    public List<CmsGuestbook> selectCmsGuestbookList(CmsGuestbook cmsGuestbook);

    /**
     * 新增反馈管理
     * 
     * @param cmsGuestbook 反馈管理
     * @return 结果
     */
    public int insertCmsGuestbook(CmsGuestbook cmsGuestbook);

    /**
     * 修改反馈管理
     * 
     * @param cmsGuestbook 反馈管理
     * @return 结果
     */
    public int updateCmsGuestbook(CmsGuestbook cmsGuestbook);

    /**
     * 批量删除反馈管理
     * 
     * @param ids 需要删除的反馈管理主键集合
     * @return 结果
     */
    public int deleteCmsGuestbookByIds(Long[] ids);

    /**
     * 删除反馈管理信息
     * 
     * @param id 反馈管理主键
     * @return 结果
     */
    public int deleteCmsGuestbookById(Long id);
}
