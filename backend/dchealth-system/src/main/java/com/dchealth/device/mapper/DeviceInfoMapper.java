package com.dchealth.device.mapper;

import java.util.List;
import com.dchealth.device.domain.DeviceInfo;

public interface DeviceInfoMapper {
    List<DeviceInfo> selectDeviceInfoList(DeviceInfo entity);
    DeviceInfo selectDeviceInfoById(Long id);
    int insertDeviceInfo(DeviceInfo entity);
    int updateDeviceInfo(DeviceInfo entity);
    int deleteDeviceInfoById(Long id);
    int deleteDeviceInfoByIds(Long[] ids);
}
