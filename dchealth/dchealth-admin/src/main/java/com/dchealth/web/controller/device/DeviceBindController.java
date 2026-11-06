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
import com.dchealth.device.domain.DeviceBind;
import com.dchealth.device.service.IDeviceBindService;

/**
 * 设备绑定
 *
 * @author dchealth
 */
@RestController
@RequestMapping("/device/bind")
public class DeviceBindController extends BaseController {

    @Autowired
    private IDeviceBindService service;

    @PreAuthorize("@ss.hasPermi('device:bind:list')")
    @GetMapping("/list")
    public TableDataInfo list(DeviceBind entity) {
        startPage();
        List<DeviceBind> list = service.selectDeviceBindList(entity);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('device:bind:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(service.selectDeviceBindById(id));
    }

    @PreAuthorize("@ss.hasPermi('device:bind:add')")
    @Log(title = "设备绑定", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DeviceBind entity) {
        entity.setCreateBy(getUsername());
        return toAjax(service.insertDeviceBind(entity));
    }

    @PreAuthorize("@ss.hasPermi('device:bind:edit')")
    @Log(title = "设备绑定", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DeviceBind entity) {
        entity.setUpdateBy(getUsername());
        return toAjax(service.updateDeviceBind(entity));
    }

    @PreAuthorize("@ss.hasPermi('device:bind:remove')")
    @Log(title = "设备绑定", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteDeviceBindByIds(ids));
    }
}
