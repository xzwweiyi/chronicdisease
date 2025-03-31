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
import com.dchealth.medical.domain.PatientExamRecord;
import com.dchealth.medical.service.IPatientExamRecordService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 患者问卷填报关系Controller
 * 
 * @author lchbu
 * @date 2022-11-17
 */
@RestController
@RequestMapping("/medical/patient_exam_record")
public class PatientExamRecordController extends BaseController
{
    @Autowired
    private IPatientExamRecordService patientExamRecordService;

    /**
     * 查询患者问卷填报关系列表
     */
    @PreAuthorize("@ss.hasPermi('medical:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(PatientExamRecord patientExamRecord)
    {
        startPage();
        List<PatientExamRecord> list = patientExamRecordService.selectPatientExamRecordList(patientExamRecord);
        return getDataTable(list);
    }

    /**
     * 导出患者问卷填报关系列表
     */
    @PreAuthorize("@ss.hasPermi('medical:record:export')")
    @Log(title = "患者问卷填报关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PatientExamRecord patientExamRecord)
    {
        List<PatientExamRecord> list = patientExamRecordService.selectPatientExamRecordList(patientExamRecord);
        ExcelUtil<PatientExamRecord> util = new ExcelUtil<PatientExamRecord>(PatientExamRecord.class);
        util.exportExcel(response, list, "患者问卷填报关系数据");
    }

    /**
     * 获取患者问卷填报关系详细信息
     */
    @PreAuthorize("@ss.hasPermi('medical:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(patientExamRecordService.selectPatientExamRecordById(id));
    }

    /**
     * 新增患者问卷填报关系
     */
    @PreAuthorize("@ss.hasPermi('medical:record:add')")
    @Log(title = "患者问卷填报关系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PatientExamRecord patientExamRecord)
    {
        return toAjax(patientExamRecordService.insertPatientExamRecord(patientExamRecord));
    }

    /**
     * 修改患者问卷填报关系
     */
    @PreAuthorize("@ss.hasPermi('medical:record:edit')")
    @Log(title = "患者问卷填报关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PatientExamRecord patientExamRecord)
    {
        return toAjax(patientExamRecordService.updatePatientExamRecord(patientExamRecord));
    }

    /**
     * 删除患者问卷填报关系
     */
    @PreAuthorize("@ss.hasPermi('medical:record:remove')")
    @Log(title = "患者问卷填报关系", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(patientExamRecordService.deletePatientExamRecordByIds(ids));
    }
}
