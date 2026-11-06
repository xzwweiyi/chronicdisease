package com.dchealth.base.service;

import com.dchealth.base.domain.AppBanner;

import java.util.List;

/**
 * 轮播管理Service接口
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
public interface IAppBannerService 
{
    /**
     * 查询轮播管理
     * 
     * @param id 轮播管理主键
     * @return 轮播管理
     */
    public AppBanner selectAppBannerById(String id);

    /**
     * 查询轮播管理列表
     * 
     * @param appBanner 轮播管理
     * @return 轮播管理集合
     */
    public List<AppBanner> selectAppBannerList(AppBanner appBanner);

    /**
     * 新增轮播管理
     * 
     * @param appBanner 轮播管理
     * @return 结果
     */
    public int insertAppBanner(AppBanner appBanner);

    /**
     * 修改轮播管理
     * 
     * @param appBanner 轮播管理
     * @return 结果
     */
    public int updateAppBanner(AppBanner appBanner);

    /**
     * 批量删除轮播管理
     * 
     * @param ids 需要删除的轮播管理主键集合
     * @return 结果
     */
    public int deleteAppBannerByIds(String[] ids);

    /**
     * 删除轮播管理信息
     * 
     * @param id 轮播管理主键
     * @return 结果
     */
    public int deleteAppBannerById(String id);

    int saveArticleAppBanner(String[] articleIds, String tenantId, String positionType);
}
