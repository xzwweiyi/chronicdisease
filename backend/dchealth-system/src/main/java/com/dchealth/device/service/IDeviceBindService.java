package com.dchealth.device.service;

import java.util.List;
import com.dchealth.device.domain.DeviceBind;

/**
 * 设备绑定Service接口
 *
 * @author ruoyi
 * @date 2025-06-27
 */
public interface IDeviceBindService
{
    /**
     * 查询设备绑定列表
     *
     * @param entity 设备绑定
     * @return 设备绑定集合
     */
    public List<DeviceBind> selectDeviceBindList(DeviceBind entity);

    /**
     * 查询设备绑定
     *
     * @param id 设备绑定主键
     * @return 设备绑定
     */
    public DeviceBind selectDeviceBindById(Long id);

    /**
     * 新增设备绑定
     *
     * @param entity 设备绑定
     * @return 结果
     */
    public int insertDeviceBind(DeviceBind entity);

    /**
     * 修改设备绑定
     *
     * @param entity 设备绑定
     * @return 结果
     */
    public int updateDeviceBind(DeviceBind entity);

    /**
     * 删除设备绑定信息
     *
     * @param id 设备绑定主键
     * @return 结果
     */
    public int deleteDeviceBindById(Long id);

    /**
     * 批量删除设备绑定
     *
     * @param ids 需要删除的设备绑定主键集合
     * @return 结果
     */
    public int deleteDeviceBindByIds(Long[] ids);
}
