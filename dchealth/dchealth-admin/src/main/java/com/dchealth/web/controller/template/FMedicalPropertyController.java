package com.dchealth.web.controller.template;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.template.domain.FMedicalForm;
import com.dchealth.template.domain.vo.FMedicalVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.template.domain.FMedicalProperty;
import com.dchealth.template.service.IFMedicalPropertyService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 属性Controller
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
@RestController
@RequestMapping("/template/f_medical_property")
public class FMedicalPropertyController extends BaseController
{
    @Autowired
    private IFMedicalPropertyService fMedicalPropertyService;

    /**
     * 查询属性列表
     */
    /*@PreAuthorize("@ss.hasPermi('medical:property:list')")
    @GetMapping("/list")
    public TableDataInfo list(FMedicalProperty fMedicalProperty)
    {
        startPage();
        List<FMedicalProperty> list = fMedicalPropertyService.selectFMedicalPropertyList(fMedicalProperty);
        return getDataTable(list);
    }*/


    /**
     * 获取属性详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:medical:query')")
    @GetMapping(value = "/{medicalFormId}")
    public AjaxResult getInfo(@PathVariable("medicalFormId") Long medicalFormId)
    {
        FMedicalProperty params = new FMedicalProperty();
        params.setMedicalFormId(medicalFormId);
        return AjaxResult.success(fMedicalPropertyService.selectFMedicalPropertyList(params));
    }

    /**
     * 保存
     */
    @PreAuthorize("@ss.hasPermi('base:medical:add')")
    @Log(title = "属性", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult save(@RequestBody FMedicalForm fMedicalForm)
    {
        return toAjax(fMedicalPropertyService.save(fMedicalForm));
    }

    /**
     * 修改属性
     */
    @PreAuthorize("@ss.hasPermi('base:medical:edit')")
    @Log(title = "属性", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FMedicalProperty fMedicalProperty)
    {
        return toAjax(fMedicalPropertyService.updateFMedicalProperty(fMedicalProperty));
    }


    /**
     * 删除属性
     */
    @PreAuthorize("@ss.hasPermi('base:medical:remove')")
    @Log(title = "属性", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fMedicalPropertyService.deleteFMedicalPropertyByIds(ids));
    }

    @GetMapping(value = "/findValueChangeRecord")
    public AjaxResult findValueChangeRecord(FMedicalVo fMedicalVo)
    {
        return AjaxResult.success(fMedicalPropertyService.findValueChangeRecord(fMedicalVo));
    }

    /**
         * formType 0模块1标准项2拍照项3自评量表4预警填报
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public AjaxResult delete(@RequestParam Long id,@RequestParam Integer formType){
    fMedicalPropertyService.deletePropertyById(id,formType);
        return AjaxResult.success();
    }
}
