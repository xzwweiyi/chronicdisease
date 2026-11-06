package com.dchealth.web.controller.template;

import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.template.domain.FMedicalExamSubject;
import com.dchealth.template.domain.vo.FMedicalExamVo;
import com.dchealth.template.service.IFMedicalExamSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 属性题目Controller
 * 
 * @author lchbu
 * @date 2022-10-31
 */
@RestController
@RequestMapping("/template/dept/subject")
public class FMedicalExamSubjectDeptController extends BaseController
{
    @Autowired
    private IFMedicalExamSubjectService fMedicalExamSubjectService;

    /**
     * 查询属性题目列表
     */
    @PreAuthorize("@ss.hasPermi('templateDept:subject:list')")
    @GetMapping("/list")
    public TableDataInfo list(FMedicalExamSubject fMedicalExamSubject)
    {
        startPage();
        List<FMedicalExamSubject> list = fMedicalExamSubjectService.selectFMedicalExamSubjectList(fMedicalExamSubject);
        return getDataTable(list);
    }

    /**
     * 导出属性题目列表
     */
    @PreAuthorize("@ss.hasPermi('templateDept:subject:export')")
    @Log(title = "属性题目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FMedicalExamSubject fMedicalExamSubject)
    {
        List<FMedicalExamSubject> list = fMedicalExamSubjectService.selectFMedicalExamSubjectList(fMedicalExamSubject);
        ExcelUtil<FMedicalExamSubject> util = new ExcelUtil<FMedicalExamSubject>(FMedicalExamSubject.class);
        util.exportExcel(response, list, "属性题目数据");
    }

    /**
     * 获取属性题目详细信息
     */
    @PreAuthorize("@ss.hasPermi('templateDept:subject:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(fMedicalExamSubjectService.selectFMedicalExamSubjectById(id));
    }

    /**
     * 新增属性题目(问卷)
     */
    @PreAuthorize("@ss.hasPermi('templateDept:subject:add')")
    @Log(title = "属性题目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FMedicalExamVo fMedicalExamVo)
    {
        return AjaxResult.error("科室不允许新增属性题目(问卷)");
    }

    /**
     * 修改属性题目(问卷)
     */
    @PreAuthorize("@ss.hasPermi('templateDept:subject:edit')")
    @Log(title = "属性题目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FMedicalExamSubject fMedicalExamSubject)
    {
        return  AjaxResult.error("科室不允许修改属性题目(问卷)");
    }

    /**
     * 删除属性题目
     */
    @PreAuthorize("@ss.hasPermi('templateDept:subject:remove')")
    @Log(title = "属性题目", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return AjaxResult.error("科室不允许删除属性题目");
    }

    /**
     * 新增属性题目(自评量表)
     */
    @PreAuthorize("@ss.hasPermi('templateDept:subject:addScale')")
    @Log(title = "属性题目", businessType = BusinessType.INSERT)
    @PostMapping("/addScale")
    public AjaxResult addScale(@RequestBody FMedicalExamVo fMedicalExamVo)
    {
        return AjaxResult.error("科室不允许删除属性题目(自评量表)");
    }
}
