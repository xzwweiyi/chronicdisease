package com.dchealth.cms.mapper;

import com.dchealth.cms.domain.CmsComment;

import java.util.List;

/**
 * 资讯评论Mapper接口
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
public interface CmsCommentMapper 
{
    /**
     * 查询资讯评论
     * 
     * @param id 资讯评论主键
     * @return 资讯评论
     */
    public CmsComment selectCmsCommentById(Long id);

    /**
     * 查询资讯评论列表
     * 
     * @param cmsComment 资讯评论
     * @return 资讯评论集合
     */
    public List<CmsComment> selectCmsCommentList(CmsComment cmsComment);

    /**
     * 新增资讯评论
     * 
     * @param cmsComment 资讯评论
     * @return 结果
     */
    public int insertCmsComment(CmsComment cmsComment);

    /**
     * 修改资讯评论
     * 
     * @param cmsComment 资讯评论
     * @return 结果
     */
    public int updateCmsComment(CmsComment cmsComment);

    /**
     * 删除资讯评论
     * 
     * @param id 资讯评论主键
     * @return 结果
     */
    public int deleteCmsCommentById(Long id);

    /**
     * 批量删除资讯评论
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCmsCommentByIds(Long[] ids);
}
