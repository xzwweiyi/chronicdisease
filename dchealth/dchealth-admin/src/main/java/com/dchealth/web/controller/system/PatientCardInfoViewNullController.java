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
import com.dchealth.system.domain.PatientCardInfoViewNull;
import com.dchealth.system.service.IPatientCardInfoViewNullService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 空时患者号Controller
 * 
 * @author lchbu
 * @date 2024-11-23
 */
@RestController
@RequestMapping("/system/patientCardNull")
public class PatientCardInfoViewNullController extends BaseController
{
    @Autowired
    private IPatientCardInfoViewNullService patientCardInfoViewNullService;

    /**
     * 查询空时患者号列表
     */
    @PreAuthorize("@ss.hasPermi('system:patientCardNull:list')")
    @GetMapping("/list")
    public TableDataInfo list(PatientCardInfoViewNull patientCardInfoViewNull)
    {
        startPage();
        List<PatientCardInfoViewNull> list = patientCardInfoViewNullService.selectPatientCardInfoViewNullList(patientCardInfoViewNull);
        return getDataTable(list);
    }

    /**
     * 导出空时患者号列表
     */
    @PreAuthorize("@ss.hasPermi('system:patientCardNull:export')")
    @Log(title = "空时患者号", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PatientCardInfoViewNull patientCardInfoViewNull)
    {
        List<PatientCardInfoViewNull> list = patientCardInfoViewNullService.selectPatientCardInfoViewNullList(patientCardInfoViewNull);
        ExcelUtil<PatientCardInfoViewNull> util = new ExcelUtil<PatientCardInfoViewNull>(PatientCardInfoViewNull.class);
        util.exportExcel(response, list, "空时患者号数据");
    }

    /**
     * 获取空时患者号详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:patientCardNull:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(patientCardInfoViewNullService.selectPatientCardInfoViewNullById(id));
    }

    /**
     * 新增空时患者号
     */
    @PreAuthorize("@ss.hasPermi('system:patientCardNull:add')")
    @Log(title = "空时患者号", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PatientCardInfoViewNull patientCardInfoViewNull)
    {
        return toAjax(patientCardInfoViewNullService.insertPatientCardInfoViewNull(patientCardInfoViewNull));
    }

    /**
     * 修改空时患者号
     */
    @PreAuthorize("@ss.hasPermi('system:patientCardNull:edit')")
    @Log(title = "空时患者号", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PatientCardInfoViewNull patientCardInfoViewNull)
    {
        return toAjax(patientCardInfoViewNullService.updatePatientCardInfoViewNull(patientCardInfoViewNull));
    }

    /**
     * 删除空时患者号
     */
    @PreAuthorize("@ss.hasPermi('system:patientCardNull:remove')")
    @Log(title = "空时患者号", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(patientCardInfoViewNullService.deletePatientCardInfoViewNullByIds(ids));
    }
}
