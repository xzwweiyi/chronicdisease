package com.dchealth.web.controller.medical;

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
import com.dchealth.medical.domain.PatientRecordFiles;
import com.dchealth.medical.service.IPatientRecordFilesService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 患者档案图片Controller
 * 
 * @author lchbu
 * @date 2022-09-15
 */
@RestController
@RequestMapping("/medical/files")
public class PatientRecordFilesController extends BaseController
{
    @Autowired
    private IPatientRecordFilesService patientRecordFilesService;

    /**
     * 查询患者档案图片列表
     */
    @PreAuthorize("@ss.hasPermi('medical:files:list')")
    @GetMapping("/list")
    public TableDataInfo list(PatientRecordFiles patientRecordFiles)
    {
        startPage();
        List<PatientRecordFiles> list = patientRecordFilesService.selectPatientRecordFilesList(patientRecordFiles);
        return getDataTable(list);
    }

    /**
     * 导出患者档案图片列表
     */
    @PreAuthorize("@ss.hasPermi('medical:files:export')")
    @Log(title = "患者档案图片", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PatientRecordFiles patientRecordFiles)
    {
        List<PatientRecordFiles> list = patientRecordFilesService.selectPatientRecordFilesList(patientRecordFiles);
        ExcelUtil<PatientRecordFiles> util = new ExcelUtil<PatientRecordFiles>(PatientRecordFiles.class);
        util.exportExcel(response, list, "患者档案图片数据");
    }

    /**
     * 获取患者档案图片详细信息
     */
    @PreAuthorize("@ss.hasPermi('medical:files:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(patientRecordFilesService.selectPatientRecordFilesById(id));
    }

    /**
     * 新增患者档案图片
     */
    @PreAuthorize("@ss.hasPermi('medical:files:add')")
    @Log(title = "患者档案图片", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PatientRecordFiles patientRecordFiles)
    {
        return toAjax(patientRecordFilesService.insertPatientRecordFiles(patientRecordFiles));
    }

    /**
     * 修改患者档案图片
     */
    @PreAuthorize("@ss.hasPermi('medical:files:edit')")
    @Log(title = "患者档案图片", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PatientRecordFiles patientRecordFiles)
    {
        return toAjax(patientRecordFilesService.updatePatientRecordFiles(patientRecordFiles));
    }

    /**
     * 删除患者档案图片
     */
    @PreAuthorize("@ss.hasPermi('medical:files:remove')")
    @Log(title = "患者档案图片", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(patientRecordFilesService.deletePatientRecordFilesByIds(ids));
    }
}
