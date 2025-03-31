package com.dchealth.cro.base.mapper;

import com.dchealth.cro.base.domain.AppBanner;
import com.dchealth.cro.base.vo.AppBannerVO;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 首页轮播设置Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-16
 */
@Repository
public interface AppBannerMapper 
{
    /**
     * 查询首页轮播设置
     * 
     * @param id 首页轮播设置ID
     * @return 首页轮播设置
     */
    public AppBanner selectAppBannerById(Integer id);

    /**
     * 查询首页轮播设置列表
     * 
     * @param appBanner 首页轮播设置
     * @return 首页轮播设置集合
     */
    public List<AppBanner> selectAppBannerList(AppBanner appBanner);

    /**
     * 新增首页轮播设置
     * 
     * @param appBanner 首页轮播设置
     * @return 结果
     */
    public int insertAppBanner(AppBanner appBanner);

    /**
     * 修改首页轮播设置
     * 
     * @param appBanner 首页轮播设置
     * @return 结果
     */
    public int updateAppBanner(AppBanner appBanner);

    /**
     * 删除首页轮播设置
     * 
     * @param id 首页轮播设置ID
     * @return 结果
     */
    public int deleteAppBannerById(Integer id);

    /**
     * 批量删除首页轮播设置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAppBannerByIds(String[] ids);
    
    public List<AppBannerVO>  selectAppBanner(@Param("tenantId")String tenantId,@Param("pageSize")int pageSize,@Param("positionType")String positionType);
    public List<AppBannerVO>  selectAppBannerByDeptId(@Param("deptId")long deptId,@Param("tenantId")String tenantId,@Param("pageSize")int pageSize,@Param("positionType")String positionType);

    public List<AppBannerVO>  selectMobileBanner(@Param("tenantId")String tenantId,@Param("pageSize")int pageSize,@Param("positionType")String positionType);
}
