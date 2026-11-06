package com.dchealth.web.controller.template;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.template.domain.FLibraryForm;
import com.dchealth.template.domain.FLibraryProperty;
import com.dchealth.template.service.IFLibraryPropertyService;

/**
 * 属性库属性Controller
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
@RestController
@RequestMapping("/template/f_library_property")
public class FLibraryPropertyController extends BaseController
{
    public static final Logger log = LoggerFactory.getLogger(FLibraryPropertyController.class);
    @Autowired
    private IFLibraryPropertyService fLibraryPropertyService;

    /**
     * 查询属性库属性列表
     */
    @PreAuthorize("@ss.hasPermi('base:library:list')")
    @GetMapping("/list")
    public TableDataInfo list(FLibraryProperty fLibraryProperty)
    {
        startPage();
        List<FLibraryProperty> list = fLibraryPropertyService.selectFLibraryPropertyList(fLibraryProperty);
        return getDataTable(list);
    }

    /**
     * 查询全部
     * @param fLibraryProperty
     * @return
     */
    @GetMapping("/listAll")
    public AjaxResult listAll(FLibraryProperty fLibraryProperty)
    {
        List<FLibraryProperty> list = fLibraryPropertyService.selectFLibraryPropertyList(fLibraryProperty);
        return AjaxResult.success(list);
    }

    /**
     * 导出属性库属性列表
     */
    @PreAuthorize("@ss.hasPermi('base:library:export')")
    @Log(title = "属性库属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FLibraryProperty fLibraryProperty)
    {
        List<FLibraryProperty> list = fLibraryPropertyService.selectFLibraryPropertyList(fLibraryProperty);
        ExcelUtil<FLibraryProperty> util = new ExcelUtil<FLibraryProperty>(FLibraryProperty.class);
        util.exportExcel(response, list, "属性库属性数据");
    }

    /**
     * 获取属性库属性详细信息
     */
    @PreAuthorize("@ss.hasPermi('library:property:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(fLibraryPropertyService.selectFLibraryPropertyById(id));
    }

    /**
     * 新增属性库属性
     */
    /*@PreAuthorize("@ss.hasPermi('library:property:add')")
    @Log(title = "属性库属性", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FLibraryForm fLibraryForm)
    {
        log.info(JSON.toJSONString(fLibraryForm));
        return toAjax(fLibraryPropertyService.insertFLibraryProperty(fLibraryForm));
    }*/

    /**
     * 修改属性库属性
     */
    @PreAuthorize("@ss.hasPermi('base:library:edit')")
    @Log(title = "属性库属性", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FLibraryForm fLibraryForm)
    {
        return toAjax(fLibraryPropertyService.updateFLibraryProperty(fLibraryForm));
    }

    /**
     * 修改属性库属性
     */
    @PreAuthorize("@ss.hasPermi('base:library:edit')")
    @Log(title = "属性库属性", businessType = BusinessType.UPDATE)
    @PutMapping("/only")
    public AjaxResult edit(@RequestBody FLibraryProperty fLibraryProperty)
    {
        return toAjax(fLibraryPropertyService.updateFLibraryProperty(fLibraryProperty));
    }

    /**
     * 删除属性库属性
     */
    @PreAuthorize("@ss.hasPermi('base:library:remove')")
    @Log(title = "属性库属性", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fLibraryPropertyService.deleteFLibraryPropertyByIds(ids));
    }
}
