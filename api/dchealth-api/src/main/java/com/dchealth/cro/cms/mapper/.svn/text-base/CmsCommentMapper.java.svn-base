package com.dchealth.cro.cms.mapper;

import com.dchealth.cro.cms.domain.CmsComment;
import com.dchealth.cro.cms.vo.CommentListVO;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 评论Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-18
 */
@Repository
public interface CmsCommentMapper 
{
    /**
     * 查询评论
     * 
     * @param id 评论ID
     * @return 评论
     */
    public CmsComment selectCmsCommentById(Long id);

    /**
     * 查询评论列表
     * 
     * @param cmsComment 评论
     * @return 评论集合
     */
    public List<CmsComment> selectCmsCommentList(CmsComment cmsComment);

    /**
     * 新增评论
     * 
     * @param cmsComment 评论
     * @return 结果
     */
    public int insertCmsComment(CmsComment cmsComment);

    /**
     * 修改评论
     * 
     * @param cmsComment 评论
     * @return 结果
     */
    public int updateCmsComment(CmsComment cmsComment);

    /**
     * 删除评论
     * 
     * @param id 评论ID
     * @return 结果
     */
    public int deleteCmsCommentById(Long id);

    /**
     * 批量删除评论
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCmsCommentByIds(String[] ids);
    
    List<CommentListVO> selectCommentListByArticleId(@Param("articleId")Long articleId,@Param("pageNo")Integer pageNo,@Param("pageSize")Integer pageSize);
}
