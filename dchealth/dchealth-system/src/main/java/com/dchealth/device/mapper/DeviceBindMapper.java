package com.dchealth.device.mapper;

import java.util.List;
import com.dchealth.device.domain.DeviceBind;

public interface DeviceBindMapper {
    List<DeviceBind> selectDeviceBindList(DeviceBind entity);
    DeviceBind selectDeviceBindById(Long id);
    int insertDeviceBind(DeviceBind entity);
    int updateDeviceBind(DeviceBind entity);
    int deleteDeviceBindById(Long id);
    int deleteDeviceBindByIds(Long[] ids);
}
