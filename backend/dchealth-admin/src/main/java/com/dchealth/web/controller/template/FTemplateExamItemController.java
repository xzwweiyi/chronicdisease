package com.dchealth.web.controller.template;

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
import com.dchealth.template.domain.FTemplateExamItem;
import com.dchealth.template.service.IFTemplateExamItemService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 属性题目选项-自评量Controller
 * 
 * @author lchbu
 * @date 2022-11-11
 */
@RestController
@RequestMapping("/template/f_template_exam_item")
public class FTemplateExamItemController extends BaseController
{
    @Autowired
    private IFTemplateExamItemService fTemplateExamItemService;

    /**
     * 查询属性题目选项-自评量列表
     */
    @PreAuthorize("@ss.hasPermi('template:examitem:list')")
    @GetMapping("/list")
    public TableDataInfo list(FTemplateExamItem fTemplateExamItem)
    {
        startPage();
        List<FTemplateExamItem> list = fTemplateExamItemService.selectFTemplateExamItemList(fTemplateExamItem);
        return getDataTable(list);
    }

    /**
     * 导出属性题目选项-自评量列表
     */
    @PreAuthorize("@ss.hasPermi('template:examitem:export')")
    @Log(title = "属性题目选项-自评量", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FTemplateExamItem fTemplateExamItem)
    {
        List<FTemplateExamItem> list = fTemplateExamItemService.selectFTemplateExamItemList(fTemplateExamItem);
        ExcelUtil<FTemplateExamItem> util = new ExcelUtil<FTemplateExamItem>(FTemplateExamItem.class);
        util.exportExcel(response, list, "属性题目选项-自评量数据");
    }

    /**
     * 获取属性题目选项-自评量详细信息
     */
    @PreAuthorize("@ss.hasPermi('template:examitem:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(fTemplateExamItemService.selectFTemplateExamItemById(id));
    }

    /**
     * 新增属性题目选项-自评量
     */
    @PreAuthorize("@ss.hasPermi('template:examitem:add')")
    @Log(title = "属性题目选项-自评量", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FTemplateExamItem fTemplateExamItem)
    {
        return toAjax(fTemplateExamItemService.insertFTemplateExamItem(fTemplateExamItem));
    }

    /**
     * 修改属性题目选项-自评量
     */
    @PreAuthorize("@ss.hasPermi('template:examitem:edit')")
    @Log(title = "属性题目选项-自评量", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FTemplateExamItem fTemplateExamItem)
    {
        return toAjax(fTemplateExamItemService.updateFTemplateExamItem(fTemplateExamItem));
    }

    /**
     * 删除属性题目选项-自评量
     */
    @PreAuthorize("@ss.hasPermi('template:examitem:remove')")
    @Log(title = "属性题目选项-自评量", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fTemplateExamItemService.deleteFTemplateExamItemByIds(ids));
    }
}
