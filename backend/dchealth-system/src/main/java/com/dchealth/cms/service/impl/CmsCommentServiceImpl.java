package com.dchealth.cms.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.cms.mapper.CmsCommentMapper;
import com.dchealth.cms.domain.CmsComment;
import com.dchealth.cms.service.ICmsCommentService;

/**
 * 资讯评论Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Service
public class CmsCommentServiceImpl implements ICmsCommentService 
{
    @Autowired
    private CmsCommentMapper cmsCommentMapper;

    /**
     * 查询资讯评论
     * 
     * @param id 资讯评论主键
     * @return 资讯评论
     */
    @Override
    public CmsComment selectCmsCommentById(Long id)
    {
        return cmsCommentMapper.selectCmsCommentById(id);
    }

    /**
     * 查询资讯评论列表
     * 
     * @param cmsComment 资讯评论
     * @return 资讯评论
     */
    @Override
    public List<CmsComment> selectCmsCommentList(CmsComment cmsComment)
    {
        return cmsCommentMapper.selectCmsCommentList(cmsComment);
    }

    /**
     * 新增资讯评论
     * 
     * @param cmsComment 资讯评论
     * @return 结果
     */
    @Override
    public int insertCmsComment(CmsComment cmsComment)
    {
        cmsComment.setCreateTime(DateUtils.getNowDate());
        return cmsCommentMapper.insertCmsComment(cmsComment);
    }

    /**
     * 修改资讯评论
     * 
     * @param cmsComment 资讯评论
     * @return 结果
     */
    @Override
    public int updateCmsComment(CmsComment cmsComment)
    {
        return cmsCommentMapper.updateCmsComment(cmsComment);
    }

    /**
     * 批量删除资讯评论
     * 
     * @param ids 需要删除的资讯评论主键
     * @return 结果
     */
    @Override
    public int deleteCmsCommentByIds(Long[] ids)
    {
        return cmsCommentMapper.deleteCmsCommentByIds(ids);
    }

    /**
     * 删除资讯评论信息
     * 
     * @param id 资讯评论主键
     * @return 结果
     */
    @Override
    public int deleteCmsCommentById(Long id)
    {
        return cmsCommentMapper.deleteCmsCommentById(id);
    }
}
