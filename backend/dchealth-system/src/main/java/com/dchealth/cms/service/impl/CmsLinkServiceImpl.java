package com.dchealth.cms.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.cms.mapper.CmsLinkMapper;
import com.dchealth.cms.domain.CmsLink;
import com.dchealth.cms.service.ICmsLinkService;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Service
public class CmsLinkServiceImpl implements ICmsLinkService 
{
    @Autowired
    private CmsLinkMapper cmsLinkMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public CmsLink selectCmsLinkById(Long id)
    {
        return cmsLinkMapper.selectCmsLinkById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param cmsLink 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CmsLink> selectCmsLinkList(CmsLink cmsLink)
    {
        return cmsLinkMapper.selectCmsLinkList(cmsLink);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param cmsLink 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCmsLink(CmsLink cmsLink)
    {
        return cmsLinkMapper.insertCmsLink(cmsLink);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param cmsLink 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCmsLink(CmsLink cmsLink)
    {
        return cmsLinkMapper.updateCmsLink(cmsLink);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteCmsLinkByIds(Long[] ids)
    {
        return cmsLinkMapper.deleteCmsLinkByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteCmsLinkById(Long id)
    {
        return cmsLinkMapper.deleteCmsLinkById(id);
    }
}
