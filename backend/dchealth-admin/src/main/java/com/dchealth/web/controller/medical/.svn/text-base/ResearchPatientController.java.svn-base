package com.dchealth.web.controller.medical;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.medical.domain.MpPerson;
import com.dchealth.medical.domain.ResearchPatient;
import com.dchealth.medical.service.IResearchPatientService;

/**
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2022-06-15
 */
@RestController
@RequestMapping("/system/researchPatient")
public class ResearchPatientController extends BaseController
{
    @Autowired
    private IResearchPatientService researchPatientService;

    /**
     * 查询【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:patient:list')")
    @GetMapping("/list")
    public TableDataInfo list(ResearchPatient researchPatient)
    {
        startPage();
        List<ResearchPatient> list = researchPatientService.selectResearchPatientList(researchPatient);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:patient:list')")
    @GetMapping("/listByDisease")
    public TableDataInfo listByDiseaseId(@RequestParam("id") Long id)
    {
        ResearchPatient researchPatient = new ResearchPatient();
        researchPatient.setResearchId(id);
        startPage();
        List<MpPerson> list = researchPatientService.listByDiseaseId(researchPatient);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:patient:export')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResearchPatient researchPatient)
    {
        List<ResearchPatient> list = researchPatientService.selectResearchPatientList(researchPatient);
        ExcelUtil<ResearchPatient> util = new ExcelUtil<ResearchPatient>(ResearchPatient.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:patient:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(researchPatientService.selectResearchPatientById(id));
    }

    /**
     * 病种批量关联患者
     */
    @PreAuthorize("@ss.hasPermi('system:patient:add')")
    @Log(title = "病种批量关联患者", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResearchPatient researchPatient)
    {
        return toAjax(researchPatientService.insertResearchPatient(researchPatient));
    }

    @PreAuthorize("@ss.hasPermi('system:patient:remove')")
    @Log(title = "病种批量去关联患者", businessType = BusinessType.DELETE)
    @PostMapping("/delBatch")
    public AjaxResult delBatch(@RequestBody List<ResearchPatient> researchPatients)
    {
        return toAjax(researchPatientService.batchDelResearchPatient(researchPatients));
    }

    /**
     * 修改【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:patient:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResearchPatient researchPatient)
    {
        return toAjax(researchPatientService.updateResearchPatient(researchPatient));
    }

    /**
     * 删除【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:patient:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(researchPatientService.deleteResearchPatientByIds(ids));
    }
}

