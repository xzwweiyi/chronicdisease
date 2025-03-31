package com.dchealth.cro.cms.mapper;

import com.dchealth.cro.cms.domain.CmsCommentTip;
import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * 举报Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2020-04-29
 */
@Repository
public interface CmsCommentTipMapper 
{
    /**
     * 查询举报
     * 
     * @param id 举报ID
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
     * 删除举报
     * 
     * @param id 举报ID
     * @return 结果
     */
    public int deleteCmsCommentTipById(Long id);

    /**
     * 批量删除举报
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCmsCommentTipByIds(String[] ids);
}
