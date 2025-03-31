package com.dchealth.cms.mapper;

import com.dchealth.cms.domain.CmsLink;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
public interface CmsLinkMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public CmsLink selectCmsLinkById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param cmsLink 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CmsLink> selectCmsLinkList(CmsLink cmsLink);

    /**
     * 新增【请填写功能名称】
     * 
     * @param cmsLink 【请填写功能名称】
     * @return 结果
     */
    public int insertCmsLink(CmsLink cmsLink);

    /**
     * 修改【请填写功能名称】
     * 
     * @param cmsLink 【请填写功能名称】
     * @return 结果
     */
    public int updateCmsLink(CmsLink cmsLink);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteCmsLinkById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCmsLinkByIds(Long[] ids);
}
