package com.dchealth.cro.cms.mapper;

import com.dchealth.cro.cms.domain.CmsStat;
import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * 资讯统计Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-06
 */
@Repository
public interface CmsStatMapper 
{
    /**
     * 查询资讯统计
     * 
     * @param id 资讯统计ID
     * @return 资讯统计
     */
    public CmsStat selectCmsStatById(Long id);

    /**
     * 查询资讯统计列表
     * 
     * @param cmsStat 资讯统计
     * @return 资讯统计集合
     */
    public List<CmsStat> selectCmsStatList(CmsStat cmsStat);

    /**
     * 新增资讯统计
     * 
     * @param cmsStat 资讯统计
     * @return 结果
     */
    public int insertCmsStat(CmsStat cmsStat);

    /**
     * 修改资讯统计
     * 
     * @param cmsStat 资讯统计
     * @return 结果
     */
    public int updateCmsStat(CmsStat cmsStat);

    /**
     * 删除资讯统计
     * 
     * @param id 资讯统计ID
     * @return 结果
     */
    public int deleteCmsStatById(Long id);

    /**
     * 批量删除资讯统计
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCmsStatByIds(String[] ids);
}
