package com.dchealth.device.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.device.domain.DeviceBind;
import com.dchealth.device.mapper.DeviceBindMapper;
import com.dchealth.device.service.IDeviceBindService;

/**
 * 设备绑定Service业务层处理
 *
 * @author ruoyi
 * @date 2025-06-27
 */
@Service
public class DeviceBindServiceImpl implements IDeviceBindService
{
    @Autowired
    private DeviceBindMapper deviceBindMapper;

    /**
     * 查询设备绑定列表
     *
     * @param entity 设备绑定
     * @return 设备绑定集合
     */
    @Override
    public List<DeviceBind> selectDeviceBindList(DeviceBind entity)
    {
        return deviceBindMapper.selectDeviceBindList(entity);
    }

    /**
     * 查询设备绑定
     *
     * @param id 设备绑定主键
     * @return 设备绑定
     */
    @Override
    public DeviceBind selectDeviceBindById(Long id)
    {
        return deviceBindMapper.selectDeviceBindById(id);
    }

    /**
     * 新增设备绑定
     *
     * @param entity 设备绑定
     * @return 结果
     */
    @Override
    public int insertDeviceBind(DeviceBind entity)
    {
        return deviceBindMapper.insertDeviceBind(entity);
    }

    /**
     * 修改设备绑定
     *
     * @param entity 设备绑定
     * @return 结果
     */
    @Override
    public int updateDeviceBind(DeviceBind entity)
    {
        return deviceBindMapper.updateDeviceBind(entity);
    }

    /**
     * 删除设备绑定信息
     *
     * @param id 设备绑定主键
     * @return 结果
     */
    @Override
    public int deleteDeviceBindById(Long id)
    {
        return deviceBindMapper.deleteDeviceBindById(id);
    }

    /**
     * 批量删除设备绑定
     *
     * @param ids 需要删除的设备绑定主键集合
     * @return 结果
     */
    @Override
    public int deleteDeviceBindByIds(Long[] ids)
    {
        return deviceBindMapper.deleteDeviceBindByIds(ids);
    }
}
