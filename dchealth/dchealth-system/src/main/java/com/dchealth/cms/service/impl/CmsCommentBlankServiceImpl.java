package com.dchealth.cms.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.cms.mapper.CmsCommentBlankMapper;
import com.dchealth.cms.domain.CmsCommentBlank;
import com.dchealth.cms.service.ICmsCommentBlankService;

/**
 * 评价黑名单Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Service
public class CmsCommentBlankServiceImpl implements ICmsCommentBlankService 
{
    @Autowired
    private CmsCommentBlankMapper cmsCommentBlankMapper;

    /**
     * 查询评价黑名单
     * 
     * @param id 评价黑名单主键
     * @return 评价黑名单
     */
    @Override
    public CmsCommentBlank selectCmsCommentBlankById(Long id)
    {
        return cmsCommentBlankMapper.selectCmsCommentBlankById(id);
    }

    /**
     * 查询评价黑名单列表
     * 
     * @param cmsCommentBlank 评价黑名单
     * @return 评价黑名单
     */
    @Override
    public List<CmsCommentBlank> selectCmsCommentBlankList(CmsCommentBlank cmsCommentBlank)
    {
        return cmsCommentBlankMapper.selectCmsCommentBlankList(cmsCommentBlank);
    }

    /**
     * 新增评价黑名单
     * 
     * @param cmsCommentBlank 评价黑名单
     * @return 结果
     */
    @Override
    public int insertCmsCommentBlank(CmsCommentBlank cmsCommentBlank)
    {
        cmsCommentBlank.setCreateTime(DateUtils.getNowDate());
        return cmsCommentBlankMapper.insertCmsCommentBlank(cmsCommentBlank);
    }

    /**
     * 修改评价黑名单
     * 
     * @param cmsCommentBlank 评价黑名单
     * @return 结果
     */
    @Override
    public int updateCmsCommentBlank(CmsCommentBlank cmsCommentBlank)
    {
        return cmsCommentBlankMapper.updateCmsCommentBlank(cmsCommentBlank);
    }

    /**
     * 批量删除评价黑名单
     * 
     * @param ids 需要删除的评价黑名单主键
     * @return 结果
     */
    @Override
    public int deleteCmsCommentBlankByIds(Long[] ids)
    {
        return cmsCommentBlankMapper.deleteCmsCommentBlankByIds(ids);
    }

    /**
     * 删除评价黑名单信息
     * 
     * @param id 评价黑名单主键
     * @return 结果
     */
    @Override
    public int deleteCmsCommentBlankById(Long id)
    {
        return cmsCommentBlankMapper.deleteCmsCommentBlankById(id);
    }
}
