package com.dchealth.web.controller.template;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.template.domain.vo.FTemplateExamVo;
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
import com.dchealth.template.domain.FTemplateExamSubject;
import com.dchealth.template.service.IFTemplateExamSubjectService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 属性-自评量Controller
 * 
 * @author lchbu
 * @date 2022-11-11
 */
@RestController
@RequestMapping("/template/f_template_exam_subject")
public class FTemplateExamSubjectController extends BaseController
{
    @Autowired
    private IFTemplateExamSubjectService fTemplateExamSubjectService;

    /**
     * 查询属性-自评量列表
     */
    @PreAuthorize("@ss.hasPermi('template:examsubject:list')")
    @GetMapping("/list")
    public TableDataInfo list(FTemplateExamSubject fTemplateExamSubject)
    {
        startPage();
        List<FTemplateExamSubject> list = fTemplateExamSubjectService.selectFTemplateExamSubjectList(fTemplateExamSubject);
        return getDataTable(list);
    }

    /**
     * 导出属性-自评量列表
     */
    @PreAuthorize("@ss.hasPermi('template:examsubject:export')")
    @Log(title = "属性-自评量", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FTemplateExamSubject fTemplateExamSubject)
    {
        List<FTemplateExamSubject> list = fTemplateExamSubjectService.selectFTemplateExamSubjectList(fTemplateExamSubject);
        ExcelUtil<FTemplateExamSubject> util = new ExcelUtil<FTemplateExamSubject>(FTemplateExamSubject.class);
        util.exportExcel(response, list, "属性-自评量数据");
    }

    /**
     * 获取属性-自评量详细信息
     */
    @PreAuthorize("@ss.hasPermi('template:examsubject:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(fTemplateExamSubjectService.selectFTemplateExamSubjectById(id));
    }

    /**
     * 新增属性-自评量
     */
    @PreAuthorize("@ss.hasPermi('template:examsubject:add')")
    @Log(title = "属性-自评量", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FTemplateExamVo vo)
    {
        return toAjax(fTemplateExamSubjectService.insertFTemplateExamSubject(vo));
    }

    /**
     * 修改属性-自评量
     */
    @PreAuthorize("@ss.hasPermi('template:examsubject:edit')")
    @Log(title = "属性-自评量", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FTemplateExamSubject fTemplateExamSubject)
    {
        return toAjax(fTemplateExamSubjectService.updateFTemplateExamSubject(fTemplateExamSubject));
    }

    /**
     * 删除属性-自评量
     */
    @PreAuthorize("@ss.hasPermi('template:examsubject:remove')")
    @Log(title = "属性-自评量", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fTemplateExamSubjectService.deleteFTemplateExamSubjectByIds(ids));
    }
}
