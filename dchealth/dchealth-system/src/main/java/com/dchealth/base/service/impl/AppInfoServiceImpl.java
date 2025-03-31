package com.dchealth.base.service.impl;

import java.util.List;

import com.dchealth.base.domain.AppInfo;
import com.dchealth.base.mapper.AppInfoMapper;
import com.dchealth.base.service.IAppInfoService;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2022-06-16
 */
@Service
public class AppInfoServiceImpl implements IAppInfoService
{
    @Autowired
    private AppInfoMapper appInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public AppInfo selectAppInfoById(Long id)
    {
        return appInfoMapper.selectAppInfoById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param appInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<AppInfo> selectAppInfoList(AppInfo appInfo)
    {
        return appInfoMapper.selectAppInfoList(appInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param appInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertAppInfo(AppInfo appInfo)
    {
        appInfo.setCreateTime(DateUtils.getNowDate());
        return appInfoMapper.insertAppInfo(appInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param appInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateAppInfo(AppInfo appInfo)
    {
        appInfo.setUpdateTime(DateUtils.getNowDate());
        return appInfoMapper.updateAppInfo(appInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteAppInfoByIds(Long[] ids)
    {
        return appInfoMapper.deleteAppInfoByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteAppInfoById(Long id)
    {
        return appInfoMapper.deleteAppInfoById(id);
    }
}
