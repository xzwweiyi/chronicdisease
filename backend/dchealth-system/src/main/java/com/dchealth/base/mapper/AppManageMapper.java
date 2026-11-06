package com.dchealth.base.mapper;

import com.dchealth.base.domain.AppManage;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2022-06-16
 */
public interface AppManageMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public AppManage selectAppManageById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param appManage 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<AppManage> selectAppManageList(AppManage appManage);

    /**
     * 新增【请填写功能名称】
     *
     * @param appManage 【请填写功能名称】
     * @return 结果
     */
    public int insertAppManage(AppManage appManage);

    /**
     * 修改【请填写功能名称】
     *
     * @param appManage 【请填写功能名称】
     * @return 结果
     */
    public int updateAppManage(AppManage appManage);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteAppManageById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAppManageByIds(Long[] ids);
}
