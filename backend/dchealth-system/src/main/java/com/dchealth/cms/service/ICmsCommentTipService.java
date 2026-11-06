package com.dchealth.cms.service;

import com.dchealth.cms.domain.CmsCommentTip;

import java.util.List;

/**
 * 举报Service接口
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
public interface ICmsCommentTipService 
{
    /**
     * 查询举报
     * 
     * @param id 举报主键
     * @return 举报
     */
    public CmsCommentTip selectCmsCommentTipById(Long id);

    /**
     * 查询举报列表
     * 
     * @param cmsCommentTip 举报
     * @return 举报集合
     */
    public List<CmsCommentTip> selectCmsCommentTipList(CmsCommentTip cmsCommentTip);

    /**
     * 新增举报
     * 
     * @param cmsCommentTip 举报
     * @return 结果
     */
    public int insertCmsCommentTip(CmsCommentTip cmsCommentTip);

    /**
     * 修改举报
     * 
     * @param cmsCommentTip 举报
     * @return 结果
     */
    public int updateCmsCommentTip(CmsCommentTip cmsCommentTip);

    /**
     * 批量删除举报
     * 
     * @param ids 需要删除的举报主键集合
     * @return 结果
     */
    public int deleteCmsCommentTipByIds(Long[] ids);

    /**
     * 删除举报信息
     * 
     * @param id 举报主键
     * @return 结果
     */
    public int deleteCmsCommentTipById(Long id);
}
