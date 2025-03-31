package com.dchealth.cms.mapper;

import com.dchealth.cms.domain.CmsArticleData;

import java.util.List;

/**
 * 资讯详情Mapper接口
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
public interface CmsArticleDataMapper 
{
    /**
     * 查询资讯详情
     * 
     * @param id 资讯详情主键
     * @return 资讯详情
     */
    public CmsArticleData selectCmsArticleDataById(Long id);

    /**
     * 查询资讯详情列表
     * 
     * @param cmsArticleData 资讯详情
     * @return 资讯详情集合
     */
    public List<CmsArticleData> selectCmsArticleDataList(CmsArticleData cmsArticleData);

    /**
     * 新增资讯详情
     * 
     * @param cmsArticleData 资讯详情
     * @return 结果
     */
    public int insertCmsArticleData(CmsArticleData cmsArticleData);

    /**
     * 修改资讯详情
     * 
     * @param cmsArticleData 资讯详情
     * @return 结果
     */
    public int updateCmsArticleData(CmsArticleData cmsArticleData);

    /**
     * 删除资讯详情
     * 
     * @param id 资讯详情主键
     * @return 结果
     */
    public int deleteCmsArticleDataById(Long id);

    /**
     * 批量删除资讯详情
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCmsArticleDataByIds(Long[] ids);
}
