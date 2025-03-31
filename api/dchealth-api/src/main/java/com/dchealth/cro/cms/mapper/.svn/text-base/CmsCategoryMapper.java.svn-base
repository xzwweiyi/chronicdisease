package com.dchealth.cro.cms.mapper;

import com.dchealth.cro.cms.domain.CmsCategory;
import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * 栏目Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-06
 */
@Repository
public interface CmsCategoryMapper 
{
    /**
     * 查询栏目
     * 
     * @param id 栏目ID
     * @return 栏目
     */
    public CmsCategory selectCmsCategoryById(Long id);

    /**
     * 查询栏目列表
     * 
     * @param cmsCategory 栏目
     * @return 栏目集合
     */
    public List<CmsCategory> selectCmsCategoryList(CmsCategory cmsCategory);

    /**
     * 新增栏目
     * 
     * @param cmsCategory 栏目
     * @return 结果
     */
    public int insertCmsCategory(CmsCategory cmsCategory);

    /**
     * 修改栏目
     * 
     * @param cmsCategory 栏目
     * @return 结果
     */
    public int updateCmsCategory(CmsCategory cmsCategory);

    /**
     * 删除栏目
     * 
     * @param id 栏目ID
     * @return 结果
     */
    public int deleteCmsCategoryById(Long id);

    /**
     * 批量删除栏目
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCmsCategoryByIds(String[] ids);
}
