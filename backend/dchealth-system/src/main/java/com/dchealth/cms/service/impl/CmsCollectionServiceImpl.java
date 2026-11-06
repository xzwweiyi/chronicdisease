package com.dchealth.cms.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.cms.mapper.CmsCollectionMapper;
import com.dchealth.cms.domain.CmsCollection;
import com.dchealth.cms.service.ICmsCollectionService;

/**
 * 用户收藏Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Service
public class CmsCollectionServiceImpl implements ICmsCollectionService 
{
    @Autowired
    private CmsCollectionMapper cmsCollectionMapper;

    /**
     * 查询用户收藏
     * 
     * @param id 用户收藏主键
     * @return 用户收藏
     */
    @Override
    public CmsCollection selectCmsCollectionById(Long id)
    {
        return cmsCollectionMapper.selectCmsCollectionById(id);
    }

    /**
     * 查询用户收藏列表
     * 
     * @param cmsCollection 用户收藏
     * @return 用户收藏
     */
    @Override
    public List<CmsCollection> selectCmsCollectionList(CmsCollection cmsCollection)
    {
        return cmsCollectionMapper.selectCmsCollectionList(cmsCollection);
    }

    /**
     * 新增用户收藏
     * 
     * @param cmsCollection 用户收藏
     * @return 结果
     */
    @Override
    public int insertCmsCollection(CmsCollection cmsCollection)
    {
        cmsCollection.setCreateTime(DateUtils.getNowDate());
        return cmsCollectionMapper.insertCmsCollection(cmsCollection);
    }

    /**
     * 修改用户收藏
     * 
     * @param cmsCollection 用户收藏
     * @return 结果
     */
    @Override
    public int updateCmsCollection(CmsCollection cmsCollection)
    {
        return cmsCollectionMapper.updateCmsCollection(cmsCollection);
    }

    /**
     * 批量删除用户收藏
     * 
     * @param ids 需要删除的用户收藏主键
     * @return 结果
     */
    @Override
    public int deleteCmsCollectionByIds(Long[] ids)
    {
        return cmsCollectionMapper.deleteCmsCollectionByIds(ids);
    }

    /**
     * 删除用户收藏信息
     * 
     * @param id 用户收藏主键
     * @return 结果
     */
    @Override
    public int deleteCmsCollectionById(Long id)
    {
        return cmsCollectionMapper.deleteCmsCollectionById(id);
    }
}
