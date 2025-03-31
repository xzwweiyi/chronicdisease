package com.dchealth.web.controller.medical;

import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.service.IPatientRecordService;
import com.dchealth.web.job.MedicalJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 患者档案Controller
 * 
 * @author lchbu
 * @date 2022-09-15
 */
@RestController
@RequestMapping("/medical/record")
public class PatientRecordController extends BaseController
{
    @Autowired
    private IPatientRecordService patientRecordService;
    @Autowired
    private MedicalJob medicalJob;

    /**
     * 查询患者档案列表
     */
    @PreAuthorize("@ss.hasPermi('medical:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(PatientRecord patientRecord)
    {
        startPage();
        List<PatientRecord> list = patientRecordService.selectPatientRecordList(patientRecord);
        return getDataTable(list);
    }

    /**
     * 导出患者档案列表
     */
    @PreAuthorize("@ss.hasPermi('medical:record:export')")
    @Log(title = "患者档案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PatientRecord patientRecord)
    {
        List<PatientRecord> list = patientRecordService.selectPatientRecordList(patientRecord);
        ExcelUtil<PatientRecord> util = new ExcelUtil<PatientRecord>(PatientRecord.class);
        util.exportExcel(response, list, "患者档案数据");
    }

    /**
     * 获取患者档案详细信息
     */
    @PreAuthorize("@ss.hasPermi('medical:record:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId)
    {
        return AjaxResult.success(patientRecordService.selectPatientRecordByRecordId(recordId));
    }

    /**
     * 新增患者档案
     */
    @PreAuthorize("@ss.hasPermi('medical:record:add')")
    @Log(title = "患者档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PatientRecord patientRecord)
    {
        return toAjax(patientRecordService.insertPatientRecord(patientRecord));
    }

    /**
     * 修改患者档案
     */
    @PreAuthorize("@ss.hasPermi('medical:record:edit')")
    @Log(title = "患者档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PatientRecord patientRecord)
    {
        return toAjax(patientRecordService.updatePatientRecord(patientRecord));
    }

    /**
     * 删除患者档案
     */
    @PreAuthorize("@ss.hasPermi('medical:record:remove')")
    @Log(title = "患者档案", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(patientRecordService.deletePatientRecordByRecordIds(recordIds));
    }


    /**
     * 查询患者档案详情左侧树状图
     */
    @PreAuthorize("@ss.hasPermi('medical:record:list')")
    @GetMapping("/leftDetailTree")
    public AjaxResult leftDetailTree(PatientRecord patientRecord)
    {
        return AjaxResult.success(patientRecordService.leftDetailTree(patientRecord));
    }


    @GetMapping(value = "/batchRecordByResearchId/{researchId}")
    public AjaxResult batchRecordByResearchId(@PathVariable("researchId") Long researchId)
    {
        return AjaxResult.success(patientRecordService.batchRecordByResearchId(researchId));
    }

//    @GetMapping(value = "/batchRecordAdd")
//    public AjaxResult batchRecordAdd() throws Exception {
//        return AjaxResult.success(medicalJob.followPlanJobHandler());
//    }
}
