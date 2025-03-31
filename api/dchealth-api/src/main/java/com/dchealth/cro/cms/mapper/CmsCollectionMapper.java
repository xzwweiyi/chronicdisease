package com.dchealth.cro.cms.mapper;

import com.dchealth.cro.cms.domain.CmsCollection;
import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * 资讯收藏Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-17
 */
@Repository
public interface CmsCollectionMapper 
{
    /**
     * 查询资讯收藏
     * 
     * @param id 资讯收藏ID
     * @return 资讯收藏
     */
    public CmsCollection selectCmsCollectionById(Long id);

    /**
     * 查询资讯收藏列表
     * 
     * @param cmsCollection 资讯收藏
     * @return 资讯收藏集合
     */
    public List<CmsCollection> selectCmsCollectionList(CmsCollection cmsCollection);

    /**
     * 新增资讯收藏
     * 
     * @param cmsCollection 资讯收藏
     * @return 结果
     */
    public int insertCmsCollection(CmsCollection cmsCollection);

    /**
     * 修改资讯收藏
     * 
     * @param cmsCollection 资讯收藏
     * @return 结果
     */
    public int updateCmsCollection(CmsCollection cmsCollection);

    /**
     * 删除资讯收藏
     * 
     * @param id 资讯收藏ID
     * @return 结果
     */
    public int deleteCmsCollectionById(Long id);

    /**
     * 批量删除资讯收藏
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCmsCollectionByIds(String[] ids);
}
