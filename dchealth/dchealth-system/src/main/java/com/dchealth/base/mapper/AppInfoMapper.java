package com.dchealth.base.mapper;

import com.dchealth.base.domain.AppInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2022-06-16
 */
public interface AppInfoMapper
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
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteAppInfoById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAppInfoByIds(Long[] ids);
}
