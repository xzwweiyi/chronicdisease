package com.dchealth.base.service.impl;

import java.util.List;

import com.dchealth.base.domain.AppBanner;
import com.dchealth.base.mapper.AppBannerMapper;
import com.dchealth.base.service.IAppBannerService;
import com.dchealth.cms.domain.CmsArticle;
import com.dchealth.cms.mapper.CmsArticleMapper;
import com.dchealth.common.core.domain.model.LoginUser;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.SecurityUtils;
import com.dchealth.common.utils.StringUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 轮播管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Service
public class AppBannerServiceImpl implements IAppBannerService
{
    @Autowired
    private AppBannerMapper appBannerMapper;

    @Autowired
    private CmsArticleMapper cmsArticleMapper;

    /**
     * 查询轮播管理
     * 
     * @param id 轮播管理主键
     * @return 轮播管理
     */
    @Override
    public AppBanner selectAppBannerById(String id)
    {
        return appBannerMapper.selectAppBannerById(id);
    }

    /**
     * 查询轮播管理列表
     * 
     * @param appBanner 轮播管理
     * @return 轮播管理
     */
    @Override
    public List<AppBanner> selectAppBannerList(AppBanner appBanner)
    {
        return appBannerMapper.selectAppBannerList(appBanner);
    }

    /**
     * 新增轮播管理
     * 
     * @param appBanner 轮播管理
     * @return 结果
     */
    @Override
    public int insertAppBanner(AppBanner appBanner)
    {
        appBanner.setPositionType("1");
        appBanner.setCreateTime(DateUtils.getNowDate());
        return appBannerMapper.insertAppBanner(appBanner);
    }

    /**
     * 修改轮播管理
     * 
     * @param appBanner 轮播管理
     * @return 结果
     */
    @Override
    public int updateAppBanner(AppBanner appBanner)
    {
        appBanner.setUpdateTime(DateUtils.getNowDate());
        return appBannerMapper.updateAppBanner(appBanner);
    }

    /**
     * 批量删除轮播管理
     * 
     * @param ids 需要删除的轮播管理主键
     * @return 结果
     */
    @Override
    public int deleteAppBannerByIds(String[] ids)
    {
        return appBannerMapper.deleteAppBannerByIds(ids);
    }

    /**
     * 删除轮播管理信息
     * 
     * @param id 轮播管理主键
     * @return 结果
     */
    @Override
    public int deleteAppBannerById(String id)
    {
        return appBannerMapper.deleteAppBannerById(id);
    }

    @Override
    public int saveArticleAppBanner(String[] articleIds, String tenantId, String positionType) {
        if (articleIds != null && articleIds.length > 0) {
            for (String articleId : articleIds) {
            	saveDbAppBanner(articleId, tenantId, positionType);
            }
        }
        return 1;
    }
    
    
    private  void saveDbAppBanner(String articleId,String tenantId,String positionType) {
    	if (StringUtils.isNotBlankAndNotEmpty(articleId)) {
            AppBanner appBannerSearch = new AppBanner();
            appBannerSearch.setTenantId(tenantId);
            appBannerSearch.setDataId(articleId);
            List<AppBanner> list = appBannerMapper.selectAppBannerList(appBannerSearch);
            if (CollectionUtils.isEmpty(list)) {
                CmsArticle cmsArticle = cmsArticleMapper.selectCmsArticleById(Long.valueOf(articleId));
                if(cmsArticle != null ) {
                    AppBanner appBanner = new AppBanner();
                    appBanner.setBannerName(cmsArticle.getTitle());
                    appBanner.setDataId(String.valueOf(cmsArticle.getId()));
                    appBanner.setType("1");
                    appBanner.setTenantId(tenantId);
                    appBanner.setPositionType(positionType);
                    appBanner.setImageUrl(cmsArticle.getImage());
                    appBanner.setOrderNum(Long.valueOf(99));
                    appBanner.setCreateTime(DateUtils.getNowDate());
                    appBanner.setUpdateTime(DateUtils.getNowDate());

                    LoginUser user = SecurityUtils.getLoginUser();
                    appBanner.setDeptId(user.getDeptId());
                    appBannerMapper.insertAppBanner(appBanner);
                }
            }
        }
    }
}
