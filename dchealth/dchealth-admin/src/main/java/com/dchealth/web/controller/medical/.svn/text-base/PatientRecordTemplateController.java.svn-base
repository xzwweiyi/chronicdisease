package com.dchealth.web.controller.medical;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.dchealth.medical.domain.PatientRecordTemplate;
import com.dchealth.medical.service.IPatientRecordTemplateService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 周期配置Controller
 * 
 * @author lchbu
 * @date 2022-08-23
 */
@RestController
@RequestMapping("/medical/recordTemplate")
public class PatientRecordTemplateController extends BaseController
{
    @Autowired
    private IPatientRecordTemplateService patientRecordTemplateService;

    /**
     * 查询周期配置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PatientRecordTemplate patientRecordTemplate)
    {
        startPage();
        List<PatientRecordTemplate> list = patientRecordTemplateService.selectPatientRecordTemplateList(patientRecordTemplate);
        return getDataTable(list);
    }

    /**
     * 导出周期配置列表
     */
    @Log(title = "周期配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PatientRecordTemplate patientRecordTemplate)
    {
        List<PatientRecordTemplate> list = patientRecordTemplateService.selectPatientRecordTemplateList(patientRecordTemplate);
        ExcelUtil<PatientRecordTemplate> util = new ExcelUtil<PatientRecordTemplate>(PatientRecordTemplate.class);
        util.exportExcel(response, list, "周期配置数据");
    }

    /**
     * 获取周期配置详细信息
     */
    @GetMapping(value = "/{followId}")
    public AjaxResult getInfo(@PathVariable("followId") Long followId)
    {
        return AjaxResult.success(patientRecordTemplateService.selectPatientRecordTemplateByFollowId(followId));
    }

    /**
     * 新增周期配置
     */
    @Log(title = "周期配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody PatientRecordTemplate patientRecordTemplate)
    {
        patientRecordTemplate.setCreateBy(getUsername());
        patientRecordTemplate.setCreateTime(new Date());
        return toAjax(patientRecordTemplateService.insertPatientRecordTemplate(patientRecordTemplate));
    }

    /**
     * 修改周期配置
     */
    @Log(title = "周期配置", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody PatientRecordTemplate patientRecordTemplate)
    {
        return toAjax(patientRecordTemplateService.updatePatientRecordTemplate(patientRecordTemplate));
    }

    /**
     * 删除周期配置
     */
    @Log(title = "周期配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{followIds}")
    public AjaxResult remove(@PathVariable Long[] followIds)
    {
        return toAjax(patientRecordTemplateService.deletePatientRecordTemplateByFollowIds(followIds));
    }
}
