package com.dchealth.web.controller.medical;

import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.medical.domain.PatientDrugInfo;
import com.dchealth.medical.service.IPatientDrugInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用药信息Controller
 * 
 * @author wangpf
 * @date 2025-08-06
 */
@RestController
@RequestMapping("/drug/info")
public class PatientDrugInfoController extends BaseController
{
    @Autowired
    private IPatientDrugInfoService patientDrugInfoService;

    /**
     * 查询用药信息列表
     */
    @PreAuthorize("@ss.hasPermi('drug:info:list')")
    @GetMapping("/list")
    public AjaxResult list(PatientDrugInfo patientDrugInfo)
    {
        //startPage();
        return patientDrugInfoService.selectPatientDrugInfoList(patientDrugInfo);
        //return getDataTable(list);
    }

    /**
     * 导出用药信息列表
     */
    @PreAuthorize("@ss.hasPermi('drug:info:export')")
    @Log(title = "用药信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PatientDrugInfo patientDrugInfo)
    {
        List<PatientDrugInfo> list = patientDrugInfoService.getPatientDrugInfoList(patientDrugInfo);
        ExcelUtil<PatientDrugInfo> util = new ExcelUtil<PatientDrugInfo>(PatientDrugInfo.class);
        util.exportExcel(response, list, "用药信息数据");
    }

    /**
     * 获取用药信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('drug:info:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(patientDrugInfoService.selectPatientDrugInfoById(id));
    }

    /**
     * 新增用药信息
     */
    @PreAuthorize("@ss.hasPermi('drug:info:add')")
    @Log(title = "用药信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PatientDrugInfo patientDrugInfo)
    {
        return toAjax(patientDrugInfoService.insertPatientDrugInfo(patientDrugInfo));
    }

    /**
     * 修改用药信息
     */
    @PreAuthorize("@ss.hasPermi('drug:info:edit')")
    @Log(title = "用药信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PatientDrugInfo patientDrugInfo)
    {
        return toAjax(patientDrugInfoService.updatePatientDrugInfo(patientDrugInfo));
    }

    /**
     * 删除用药信息
     */
    @PreAuthorize("@ss.hasPermi('drug:info:remove')")
    @Log(title = "用药信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(patientDrugInfoService.deletePatientDrugInfoByIds(ids));
    }
}
