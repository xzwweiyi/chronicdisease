package com.dchealth.cms.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.cms.mapper.CmsStatMapper;
import com.dchealth.cms.domain.CmsStat;
import com.dchealth.cms.service.ICmsStatService;

/**
 * 资讯统计Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Service
public class CmsStatServiceImpl implements ICmsStatService 
{
    @Autowired
    private CmsStatMapper cmsStatMapper;

    /**
     * 查询资讯统计
     * 
     * @param id 资讯统计主键
     * @return 资讯统计
     */
    @Override
    public CmsStat selectCmsStatById(Long id)
    {
        return cmsStatMapper.selectCmsStatById(id);
    }

    /**
     * 查询资讯统计列表
     * 
     * @param cmsStat 资讯统计
     * @return 资讯统计
     */
    @Override
    public List<CmsStat> selectCmsStatList(CmsStat cmsStat)
    {
        return cmsStatMapper.selectCmsStatList(cmsStat);
    }

    /**
     * 新增资讯统计
     * 
     * @param cmsStat 资讯统计
     * @return 结果
     */
    @Override
    public int insertCmsStat(CmsStat cmsStat)
    {
        return cmsStatMapper.insertCmsStat(cmsStat);
    }

    /**
     * 修改资讯统计
     * 
     * @param cmsStat 资讯统计
     * @return 结果
     */
    @Override
    public int updateCmsStat(CmsStat cmsStat)
    {
        return cmsStatMapper.updateCmsStat(cmsStat);
    }

    /**
     * 批量删除资讯统计
     * 
     * @param ids 需要删除的资讯统计主键
     * @return 结果
     */
    @Override
    public int deleteCmsStatByIds(Long[] ids)
    {
        return cmsStatMapper.deleteCmsStatByIds(ids);
    }

    /**
     * 删除资讯统计信息
     * 
     * @param id 资讯统计主键
     * @return 结果
     */
    @Override
    public int deleteCmsStatById(Long id)
    {
        return cmsStatMapper.deleteCmsStatById(id);
    }
}
