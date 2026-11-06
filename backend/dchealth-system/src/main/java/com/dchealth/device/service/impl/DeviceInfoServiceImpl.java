package com.dchealth.device.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.device.domain.DeviceInfo;
import com.dchealth.device.mapper.DeviceInfoMapper;
import com.dchealth.device.service.IDeviceInfoService;

/**
 * 设备信息Service业务层处理
 *
 * @author ruoyi
 * @date 2025-06-27
 */
@Service
public class DeviceInfoServiceImpl implements IDeviceInfoService
{
    @Autowired
    private DeviceInfoMapper deviceInfoMapper;

    /**
     * 查询设备信息列表
     *
     * @param entity 设备信息
     * @return 设备信息集合
     */
    @Override
    public List<DeviceInfo> selectDeviceInfoList(DeviceInfo entity)
    {
        return deviceInfoMapper.selectDeviceInfoList(entity);
    }

    /**
     * 查询设备信息
     *
     * @param id 设备信息主键
     * @return 设备信息
     */
    @Override
    public DeviceInfo selectDeviceInfoById(Long id)
    {
        return deviceInfoMapper.selectDeviceInfoById(id);
    }

    /**
     * 新增设备信息
     *
     * @param entity 设备信息
     * @return 结果
     */
    @Override
    public int insertDeviceInfo(DeviceInfo entity)
    {
        return deviceInfoMapper.insertDeviceInfo(entity);
    }

    /**
     * 修改设备信息
     *
     * @param entity 设备信息
     * @return 结果
     */
    @Override
    public int updateDeviceInfo(DeviceInfo entity)
    {
        return deviceInfoMapper.updateDeviceInfo(entity);
    }

    /**
     * 删除设备信息信息
     *
     * @param id 设备信息主键
     * @return 结果
     */
    @Override
    public int deleteDeviceInfoById(Long id)
    {
        return deviceInfoMapper.deleteDeviceInfoById(id);
    }

    /**
     * 批量删除设备信息
     *
     * @param ids 需要删除的设备信息主键集合
     * @return 结果
     */
    @Override
    public int deleteDeviceInfoByIds(Long[] ids)
    {
        return deviceInfoMapper.deleteDeviceInfoByIds(ids);
    }
}
