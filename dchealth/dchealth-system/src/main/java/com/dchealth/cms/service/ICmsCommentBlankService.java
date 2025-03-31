package com.dchealth.cms.service;

import com.dchealth.cms.domain.CmsCommentBlank;

import java.util.List;

/**
 * 评价黑名单Service接口
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
public interface ICmsCommentBlankService 
{
    /**
     * 查询评价黑名单
     * 
     * @param id 评价黑名单主键
     * @return 评价黑名单
     */
    public CmsCommentBlank selectCmsCommentBlankById(Long id);

    /**
     * 查询评价黑名单列表
     * 
     * @param cmsCommentBlank 评价黑名单
     * @return 评价黑名单集合
     */
    public List<CmsCommentBlank> selectCmsCommentBlankList(CmsCommentBlank cmsCommentBlank);

    /**
     * 新增评价黑名单
     * 
     * @param cmsCommentBlank 评价黑名单
     * @return 结果
     */
    public int insertCmsCommentBlank(CmsCommentBlank cmsCommentBlank);

    /**
     * 修改评价黑名单
     * 
     * @param cmsCommentBlank 评价黑名单
     * @return 结果
     */
    public int updateCmsCommentBlank(CmsCommentBlank cmsCommentBlank);

    /**
     * 批量删除评价黑名单
     * 
     * @param ids 需要删除的评价黑名单主键集合
     * @return 结果
     */
    public int deleteCmsCommentBlankByIds(Long[] ids);

    /**
     * 删除评价黑名单信息
     * 
     * @param id 评价黑名单主键
     * @return 结果
     */
    public int deleteCmsCommentBlankById(Long id);
}
