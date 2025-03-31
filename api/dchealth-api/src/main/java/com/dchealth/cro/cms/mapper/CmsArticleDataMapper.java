package com.dchealth.cro.cms.mapper;

import com.dchealth.cro.cms.domain.CmsArticleData;
import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * 文章详Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-06
 */
@Repository
public interface CmsArticleDataMapper 
{
    /**
     * 查询文章详
     * 
     * @param id 文章详ID
     * @return 文章详
     */
    public CmsArticleData selectCmsArticleDataById(Long id);

    /**
     * 查询文章详列表
     * 
     * @param cmsArticleData 文章详
     * @return 文章详集合
     */
    public List<CmsArticleData> selectCmsArticleDataList(CmsArticleData cmsArticleData);

    /**
     * 新增文章详
     * 
     * @param cmsArticleData 文章详
     * @return 结果
     */
    public int insertCmsArticleData(CmsArticleData cmsArticleData);

    /**
     * 修改文章详
     * 
     * @param cmsArticleData 文章详
     * @return 结果
     */
    public int updateCmsArticleData(CmsArticleData cmsArticleData);

    /**
     * 删除文章详
     * 
     * @param id 文章详ID
     * @return 结果
     */
    public int deleteCmsArticleDataById(Long id);

    /**
     * 批量删除文章详
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCmsArticleDataByIds(String[] ids);
}
