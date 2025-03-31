package com.dchealth.cms.service.impl;

import java.util.List;

import com.dchealth.cms.domain.CmsArticleData;
import com.dchealth.cms.mapper.CmsArticleDataMapper;
import com.dchealth.cms.service.ICmsArticleDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 资讯详情Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Service
public class CmsArticleDataServiceImpl implements ICmsArticleDataService
{
    @Autowired
    private CmsArticleDataMapper cmsArticleDataMapper;

    /**
     * 查询资讯详情
     * 
     * @param id 资讯详情主键
     * @return 资讯详情
     */
    @Override
    public CmsArticleData selectCmsArticleDataById(Long id)
    {
        return cmsArticleDataMapper.selectCmsArticleDataById(id);
    }

    /**
     * 查询资讯详情列表
     * 
     * @param cmsArticleData 资讯详情
     * @return 资讯详情
     */
    @Override
    public List<CmsArticleData> selectCmsArticleDataList(CmsArticleData cmsArticleData)
    {
        return cmsArticleDataMapper.selectCmsArticleDataList(cmsArticleData);
    }

    /**
     * 新增资讯详情
     * 
     * @param cmsArticleData 资讯详情
     * @return 结果
     */
    @Override
    public int insertCmsArticleData(CmsArticleData cmsArticleData)
    {
        return cmsArticleDataMapper.insertCmsArticleData(cmsArticleData);
    }

    /**
     * 修改资讯详情
     * 
     * @param cmsArticleData 资讯详情
     * @return 结果
     */
    @Override
    public int updateCmsArticleData(CmsArticleData cmsArticleData)
    {
        return cmsArticleDataMapper.updateCmsArticleData(cmsArticleData);
    }

    /**
     * 批量删除资讯详情
     * 
     * @param ids 需要删除的资讯详情主键
     * @return 结果
     */
    @Override
    public int deleteCmsArticleDataByIds(Long[] ids)
    {
        return cmsArticleDataMapper.deleteCmsArticleDataByIds(ids);
    }

    /**
     * 删除资讯详情信息
     * 
     * @param id 资讯详情主键
     * @return 结果
     */
    @Override
    public int deleteCmsArticleDataById(Long id)
    {
        return cmsArticleDataMapper.deleteCmsArticleDataById(id);
    }
}
