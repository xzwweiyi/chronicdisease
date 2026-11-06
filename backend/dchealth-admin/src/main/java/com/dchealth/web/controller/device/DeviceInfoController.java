package com.dchealth.web.controller.device;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.device.domain.DeviceInfo;
import com.dchealth.device.service.IDeviceInfoService;

/**
 * 设备管理
 *
 * @author dchealth
 */
@RestController
@RequestMapping("/device/manage")
public class DeviceInfoController extends BaseController {

    @Autowired
    private IDeviceInfoService service;

    @PreAuthorize("@ss.hasPermi('device:manage:list')")
    @GetMapping("/list")
    public TableDataInfo list(DeviceInfo entity) {
        startPage();
        List<DeviceInfo> list = service.selectDeviceInfoList(entity);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('device:manage:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(service.selectDeviceInfoById(id));
    }

    @PreAuthorize("@ss.hasPermi('device:manage:add')")
    @Log(title = "设备管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DeviceInfo entity) {
        entity.setCreateBy(getUsername());
        return toAjax(service.insertDeviceInfo(entity));
    }

    @PreAuthorize("@ss.hasPermi('device:manage:edit')")
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DeviceInfo entity) {
        entity.setUpdateBy(getUsername());
        return toAjax(service.updateDeviceInfo(entity));
    }

    @PreAuthorize("@ss.hasPermi('device:manage:remove')")
    @Log(title = "设备管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteDeviceInfoByIds(ids));
    }
}
