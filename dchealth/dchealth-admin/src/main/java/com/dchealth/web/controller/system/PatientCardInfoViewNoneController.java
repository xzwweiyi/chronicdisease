package com.dchealth.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.system.domain.PatientCardInfoViewNone;
import com.dchealth.system.service.IPatientCardInfoViewNoneService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 增量患者号Controller
 * 
 * @author lchbu
 * @date 2024-11-23
 */
@RestController
@RequestMapping("/system/patientCardNotNull")
public class PatientCardInfoViewNoneController extends BaseController
{
    @Autowired
    private IPatientCardInfoViewNoneService patientCardInfoViewNoneService;

    /**
     * 查询增量患者号列表
     */
    @PreAuthorize("@ss.hasPermi('system:patientCardNotNull:list')")
    @GetMapping("/list")
    public TableDataInfo list(PatientCardInfoViewNone patientCardInfoViewNone)
    {
        startPage();
        List<PatientCardInfoViewNone> list = patientCardInfoViewNoneService.selectPatientCardInfoViewNoneList(patientCardInfoViewNone);
        return getDataTable(list);
    }

    /**
     * 导出增量患者号列表
     */
    @PreAuthorize("@ss.hasPermi('system:patientCardNotNull:export')")
    @Log(title = "增量患者号", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PatientCardInfoViewNone patientCardInfoViewNone)
    {
        List<PatientCardInfoViewNone> list = patientCardInfoViewNoneService.selectPatientCardInfoViewNoneList(patientCardInfoViewNone);
        ExcelUtil<PatientCardInfoViewNone> util = new ExcelUtil<PatientCardInfoViewNone>(PatientCardInfoViewNone.class);
        util.exportExcel(response, list, "增量患者号数据");
    }

    /**
     * 获取增量患者号详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:patientCardNotNull:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(patientCardInfoViewNoneService.selectPatientCardInfoViewNoneById(id));
    }

    /**
     * 新增增量患者号
     */
    @PreAuthorize("@ss.hasPermi('system:patientCardNotNull:add')")
    @Log(title = "增量患者号", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PatientCardInfoViewNone patientCardInfoViewNone)
    {
        return toAjax(patientCardInfoViewNoneService.insertPatientCardInfoViewNone(patientCardInfoViewNone));
    }

    /**
     * 修改增量患者号
     */
    @PreAuthorize("@ss.hasPermi('system:patientCardNotNull:edit')")
    @Log(title = "增量患者号", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PatientCardInfoViewNone patientCardInfoViewNone)
    {
        return toAjax(patientCardInfoViewNoneService.updatePatientCardInfoViewNone(patientCardInfoViewNone));
    }

    /**
     * 删除增量患者号
     */
    @PreAuthorize("@ss.hasPermi('system:patientCardNotNull:remove')")
    @Log(title = "增量患者号", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(patientCardInfoViewNoneService.deletePatientCardInfoViewNoneByIds(ids));
    }
}
