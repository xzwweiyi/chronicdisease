package com.dchealth.cms.mapper;

import com.dchealth.cms.domain.CmsCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资讯分类Mapper接口
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
public interface CmsCategoryMapper 
{
    /**
     * 查询资讯分类
     * 
     * @param id 资讯分类主键
     * @return 资讯分类
     */
    public CmsCategory selectCmsCategoryById(Long id);

    /**
     * 查询资讯分类列表
     * 
     * @param cmsCategory 资讯分类
     * @return 资讯分类集合
     */
    public List<CmsCategory> selectCmsCategoryList(CmsCategory cmsCategory);


    /**
     * 查询资讯分类统计
     *
     * @param commonFlag 是否公共分类
     * @return 资讯分类统计
     */
    public List<CmsCategory> selectCmsCategoryStatistics(@Param("commonFlag") Integer commonFlag);


    public List<CmsCategory> selectCmsDeptCategoryStatistics(@Param("commonFlag") Integer commonFlag,@Param("deptId") Long deptId);


    /**
     * 新增资讯分类
     * 
     * @param cmsCategory 资讯分类
     * @return 结果
     */
    public int insertCmsCategory(CmsCategory cmsCategory);

    /**
     * 修改资讯分类
     * 
     * @param cmsCategory 资讯分类
     * @return 结果
     */
    public int updateCmsCategory(CmsCategory cmsCategory);

    /**
     * 删除资讯分类
     * 
     * @param id 资讯分类主键
     * @return 结果
     */
    public int deleteCmsCategoryById(Long id);

    /**
     * 批量删除资讯分类
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCmsCategoryByIds(Long[] ids);

    /**
     * 逻辑删除分类
     * @param id
     * @return
     */
    int updateCmsCategoryDelById(Long id);

    /**
     * 根据科室查询分裂
     * @param deptId
     * @return
     */
    List<CmsCategory> selectCmsCategoryByDeptId(Long deptId);

    List<CmsCategory> selectAllChildCategoryById(Long id);
}
