package com.dchealth.device.service;

import java.util.List;
import com.dchealth.device.domain.DeviceInfo;

/**
 * 设备信息Service接口
 *
 * @author ruoyi
 * @date 2025-06-27
 */
public interface IDeviceInfoService
{
    /**
     * 查询设备信息列表
     *
     * @param entity 设备信息
     * @return 设备信息集合
     */
    public List<DeviceInfo> selectDeviceInfoList(DeviceInfo entity);

    /**
     * 查询设备信息
     *
     * @param id 设备信息主键
     * @return 设备信息
     */
    public DeviceInfo selectDeviceInfoById(Long id);

    /**
     * 新增设备信息
     *
     * @param entity 设备信息
     * @return 结果
     */
    public int insertDeviceInfo(DeviceInfo entity);

    /**
     * 修改设备信息
     *
     * @param entity 设备信息
     * @return 结果
     */
    public int updateDeviceInfo(DeviceInfo entity);

    /**
     * 删除设备信息信息
     *
     * @param id 设备信息主键
     * @return 结果
     */
    public int deleteDeviceInfoById(Long id);

    /**
     * 批量删除设备信息
     *
     * @param ids 需要删除的设备信息主键集合
     * @return 结果
     */
    public int deleteDeviceInfoByIds(Long[] ids);
}
