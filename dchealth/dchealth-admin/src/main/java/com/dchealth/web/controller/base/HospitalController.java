package com.dchealth.web.controller.base;

import java.util.List;

import com.dchealth.base.domain.Hospital;
import com.dchealth.base.service.IHospitalService;
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
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 机构管理Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/hospital")
public class HospitalController extends BaseController
{
    @Autowired
    private IHospitalService hospitalService;

    /**
     * 查询机构管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:hospital:list')")
    @GetMapping("/list")
    public TableDataInfo list(Hospital hospital)
    {
        startPage();
        List<Hospital> list = hospitalService.selectHospitalList(hospital);
        return getDataTable(list);
    }

    /**
     * 获取机构管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:hospital:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(hospitalService.selectHospitalById(id));
    }

    /**
     * 新增机构管理
     */
    @PreAuthorize("@ss.hasPermi('system:hospital:add')")
    @Log(title = "机构管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Hospital hospital)
    {
        hospital.setCreateBy(getUsername());
        return toAjax(hospitalService.insertHospital(hospital));
    }

    /**
     * 修改机构管理
     */
    @PreAuthorize("@ss.hasPermi('system:hospital:edit')")
    @Log(title = "机构管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Hospital hospital)
    {
        hospital.setUpdateBy(getUsername());
        return toAjax(hospitalService.updateHospital(hospital));
    }

    /**
     * 删除机构管理
     */
    @PreAuthorize("@ss.hasPermi('system:hospital:remove')")
    @Log(title = "机构管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(hospitalService.deleteHospitalByIds(ids));
    }
}
