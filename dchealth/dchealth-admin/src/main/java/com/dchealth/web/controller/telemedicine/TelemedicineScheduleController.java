package com.dchealth.web.controller.telemedicine;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.telemedicine.domain.TelemedicineSchedule;
import com.dchealth.telemedicine.service.ITelemedicineScheduleService;

/**
 * 会诊日程
 *
 * @author dchealth
 */
@RestController
@RequestMapping("/telemedicine/schedule")
public class TelemedicineScheduleController extends BaseController {

    @Autowired
    private ITelemedicineScheduleService service;

    @PreAuthorize("@ss.hasPermi('telemedicine:schedule:list')")
    @GetMapping("/list")
    public TableDataInfo list(TelemedicineSchedule entity) {
        startPage();
        List<TelemedicineSchedule> list = service.selectTelemedicineScheduleList(entity);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('telemedicine:schedule:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(service.selectTelemedicineScheduleById(id));
    }

    @PreAuthorize("@ss.hasPermi('telemedicine:schedule:add')")
    @Log(title = "会诊日程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TelemedicineSchedule entity) {
        entity.setCreateBy(getUsername());
        return toAjax(service.insertTelemedicineSchedule(entity));
    }

    @PreAuthorize("@ss.hasPermi('telemedicine:schedule:edit')")
    @Log(title = "会诊日程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TelemedicineSchedule entity) {
        entity.setUpdateBy(getUsername());
        return toAjax(service.updateTelemedicineSchedule(entity));
    }

    @PreAuthorize("@ss.hasPermi('telemedicine:schedule:remove')")
    @Log(title = "会诊日程", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteTelemedicineScheduleByIds(ids));
    }
}
