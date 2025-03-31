package com.dchealth.web.controller.template;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.template.domain.vo.FMedicalExamVo;
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
import com.dchealth.template.domain.FMedicalExamSubject;
import com.dchealth.template.service.IFMedicalExamSubjectService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 属性题目Controller
 * 
 * @author lchbu
 * @date 2022-10-31
 */
@RestController
@RequestMapping("/template/subject")
public class FMedicalExamSubjectController extends BaseController
{
    @Autowired
    private IFMedicalExamSubjectService fMedicalExamSubjectService;

    /**
     * 查询属性题目列表
     */
    @PreAuthorize("@ss.hasPermi('template:subject:list')")
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
    @PreAuthorize("@ss.hasPermi('template:subject:export')")
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
    @PreAuthorize("@ss.hasPermi('template:subject:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(fMedicalExamSubjectService.selectFMedicalExamSubjectById(id));
    }

    /**
     * 新增属性题目(问卷)
     */
    @PreAuthorize("@ss.hasPermi('template:subject:add')")
    @Log(title = "属性题目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FMedicalExamVo fMedicalExamVo)
    {
        return toAjax(fMedicalExamSubjectService.insertFMedicalExamSubject(fMedicalExamVo));
    }

    /**
     * 修改属性题目(问卷)
     */
    @PreAuthorize("@ss.hasPermi('template:subject:edit')")
    @Log(title = "属性题目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FMedicalExamSubject fMedicalExamSubject)
    {
        return toAjax(fMedicalExamSubjectService.updateFMedicalExamSubject(fMedicalExamSubject));
    }

    /**
     * 删除属性题目
     */
    @PreAuthorize("@ss.hasPermi('template:subject:remove')")
    @Log(title = "属性题目", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fMedicalExamSubjectService.deleteFMedicalExamSubjectByIds(ids));
    }

    /**
     * 新增属性题目(自评量表)
     */
    @PreAuthorize("@ss.hasPermi('template:subject:addScale')")
    @Log(title = "属性题目", businessType = BusinessType.INSERT)
    @PostMapping("/addScale")
    public AjaxResult addScale(@RequestBody FMedicalExamVo fMedicalExamVo)
    {
        return toAjax(fMedicalExamSubjectService.insertFMedicalExamSubjectScale(fMedicalExamVo));
    }
}
