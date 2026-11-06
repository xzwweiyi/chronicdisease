package com.dchealth.cms.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.cms.mapper.CmsCommentTipMapper;
import com.dchealth.cms.domain.CmsCommentTip;
import com.dchealth.cms.service.ICmsCommentTipService;

/**
 * 举报Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Service
public class CmsCommentTipServiceImpl implements ICmsCommentTipService 
{
    @Autowired
    private CmsCommentTipMapper cmsCommentTipMapper;

    /**
     * 查询举报
     * 
     * @param id 举报主键
     * @return 举报
     */
    @Override
    public CmsCommentTip selectCmsCommentTipById(Long id)
    {
        return cmsCommentTipMapper.selectCmsCommentTipById(id);
    }

    /**
     * 查询举报列表
     * 
     * @param cmsCommentTip 举报
     * @return 举报
     */
    @Override
    public List<CmsCommentTip> selectCmsCommentTipList(CmsCommentTip cmsCommentTip)
    {
        return cmsCommentTipMapper.selectCmsCommentTipList(cmsCommentTip);
    }

    /**
     * 新增举报
     * 
     * @param cmsCommentTip 举报
     * @return 结果
     */
    @Override
    public int insertCmsCommentTip(CmsCommentTip cmsCommentTip)
    {
        cmsCommentTip.setCreateTime(DateUtils.getNowDate());
        return cmsCommentTipMapper.insertCmsCommentTip(cmsCommentTip);
    }

    /**
     * 修改举报
     * 
     * @param cmsCommentTip 举报
     * @return 结果
     */
    @Override
    public int updateCmsCommentTip(CmsCommentTip cmsCommentTip)
    {
        return cmsCommentTipMapper.updateCmsCommentTip(cmsCommentTip);
    }

    /**
     * 批量删除举报
     * 
     * @param ids 需要删除的举报主键
     * @return 结果
     */
    @Override
    public int deleteCmsCommentTipByIds(Long[] ids)
    {
        return cmsCommentTipMapper.deleteCmsCommentTipByIds(ids);
    }

    /**
     * 删除举报信息
     * 
     * @param id 举报主键
     * @return 结果
     */
    @Override
    public int deleteCmsCommentTipById(Long id)
    {
        return cmsCommentTipMapper.deleteCmsCommentTipById(id);
    }
}
