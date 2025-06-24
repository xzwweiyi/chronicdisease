package com.dchealth.web.controller.health;

import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.health.domain.PatientRecordHealth;
import com.dchealth.health.service.IPatientRecordHealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 患者档案健康管理Controller
 * 
 * @author lchbu
 * @date 2025-06-24
 */
@RestController
@RequestMapping("/system/health")
public class PatientRecordHealthController extends BaseController
{
    @Autowired
    private IPatientRecordHealthService patientRecordHealthService;

    /**
     * 查询患者档案健康管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:health:list')")
    @GetMapping("/list")
    public TableDataInfo list(PatientRecordHealth patientRecordHealth)
    {
        startPage();
        List<PatientRecordHealth> list = patientRecordHealthService.selectPatientRecordHealthList(patientRecordHealth);
        return getDataTable(list);
    }

    /**
     * 导出患者档案健康管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:health:export')")
    @Log(title = "患者档案健康管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PatientRecordHealth patientRecordHealth)
    {
        List<PatientRecordHealth> list = patientRecordHealthService.selectPatientRecordHealthList(patientRecordHealth);
        ExcelUtil<PatientRecordHealth> util = new ExcelUtil<PatientRecordHealth>(PatientRecordHealth.class);
        util.exportExcel(response, list, "患者档案健康管理数据");
    }

    /**
     * 获取患者档案健康管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:health:query')")
    @GetMapping(value = "/{healthId}")
    public AjaxResult getInfo(@PathVariable("healthId") Long healthId)
    {
        return AjaxResult.success(patientRecordHealthService.selectPatientRecordHealthByHealthId(healthId));
    }

    /**
     * 新增患者档案健康管理
     */
    @PreAuthorize("@ss.hasPermi('system:health:add')")
    @Log(title = "患者档案健康管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PatientRecordHealth patientRecordHealth)
    {
        return toAjax(patientRecordHealthService.insertPatientRecordHealth(patientRecordHealth));
    }

    /**
     * 修改患者档案健康管理
     */
    @PreAuthorize("@ss.hasPermi('system:health:edit')")
    @Log(title = "患者档案健康管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PatientRecordHealth patientRecordHealth)
    {
        return toAjax(patientRecordHealthService.updatePatientRecordHealth(patientRecordHealth));
    }

    /**
     * 删除患者档案健康管理
     */
    @PreAuthorize("@ss.hasPermi('system:health:remove')")
    @Log(title = "患者档案健康管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{healthIds}")
    public AjaxResult remove(@PathVariable Long[] healthIds)
    {
        return toAjax(patientRecordHealthService.deletePatientRecordHealthByHealthIds(healthIds));
    }
}
