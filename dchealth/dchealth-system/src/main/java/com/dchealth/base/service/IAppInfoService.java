package com.dchealth.base.service;

import com.dchealth.base.domain.AppInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2022-06-16
 */
public interface IAppInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public AppInfo selectAppInfoById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param appInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<AppInfo> selectAppInfoList(AppInfo appInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param appInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertAppInfo(AppInfo appInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param appInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateAppInfo(AppInfo appInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteAppInfoByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteAppInfoById(Long id);
}
