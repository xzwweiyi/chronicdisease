package com.dchealth.web.controller.template;

import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.system.service.ISysRoleDeptService;
import com.dchealth.system.service.ISysUserService;
import com.dchealth.template.domain.FTemplate;
import com.dchealth.template.domain.vo.FTemplateVo;
import com.dchealth.template.service.IFTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 模板Controller
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
@RestController
@RequestMapping("/template/f_template")
public class FTemplateController extends BaseController
{
    @Autowired
    private IFTemplateService fTemplateService;
    @Autowired
    private ISysRoleDeptService sysRoleDeptService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 导入更新模板
     */
    @PostMapping("/importOrUpdate")
    public AjaxResult importOrUpdate(Long templateId, Long followId)
    {
        return toAjax(fTemplateService.importOrUpdate(templateId, followId));
    }


    /**
     * 查询模板列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FTemplate fTemplate){
//        if(getUserId()>1){
//            String deptIds = sysRoleDeptService.getDeptIdsByUserId(getUserId());
//            fTemplate.setDeptIds(deptIds);
//        }
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        fTemplate.setDeptId(sysUser.getDeptId());
        List<FTemplate> list = fTemplateService.selectFTemplateList(fTemplate);
        return getDataTable(list);
    }
    @PostMapping("/copy/{templateId}/{templateCategoryId}")
    public AjaxResult copy(@PathVariable("templateId") Long templateId,@PathVariable("templateCategoryId") Long templateCategoryId){
        fTemplateService.copy(templateId,templateCategoryId);
        return AjaxResult.success();
    }
    /**
     * 导出模板列表
     */
    @PreAuthorize("@ss.hasPermi('template:f_template:export')")
    @Log(title = "模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FTemplate fTemplate){
//        if(getUserId()>1){
//            String deptIds = sysRoleDeptService.getDeptIdsByUserId(getUserId());
//            fTemplate.setDeptIds(deptIds);
//        }
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        fTemplate.setDeptId(sysUser.getDeptId());
        List<FTemplate> list = fTemplateService.selectFTemplateList(fTemplate);
        ExcelUtil<FTemplate> util = new ExcelUtil<FTemplate>(FTemplate.class);
        util.exportExcel(response, list, "模板数据");
    }

    /**
     * 获取模板详细信息
     */
    @PreAuthorize("@ss.hasPermi('template:f_template:query')")
    @GetMapping(value = "/{templateId}")
    public AjaxResult getInfo(@PathVariable("templateId") Long templateId)
    {
        return AjaxResult.success(fTemplateService.selectFTemplateByTemplateId(templateId));
    }

    /**
     * 获取模板表单详细内容
     */
    @PreAuthorize("@ss.hasPermi('template:f_template:query')")
    @GetMapping(value = "/detail/{templateId}")
    public AjaxResult detail(@PathVariable("templateId") Long templateId)
    {
        return AjaxResult.success(fTemplateService.detail(templateId));
    }

    /**
     * 新增模板
     */
    @PreAuthorize("@ss.hasPermi('template:f_template:add')")
    @Log(title = "模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FTemplate fTemplate){
        fTemplate.setCreateId(getUserId());
        fTemplate.setCreateBy(getUsername());
//        if(getUserId()>1){
//            String deptIds = sysRoleDeptService.getDeptIdsByUserId(getUserId());
//            fTemplate.setDeptIds(deptIds);
//        }
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        fTemplate.setDeptId(sysUser.getDeptId());
        return AjaxResult.success(fTemplateService.insertFTemplate(fTemplate));
    }

    /**
     * 修改模板
     */
    @PreAuthorize("@ss.hasPermi('template:f_template:edit')")
    @Log(title = "模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FTemplate fTemplate){
        fTemplate.setUpdateBy(getUsername());
        return toAjax(fTemplateService.updateFTemplate(fTemplate));
    }

    /**
     * 删除模板
     */
    @PreAuthorize("@ss.hasPermi('template:f_template:remove')")
    @Log(title = "模板", businessType = BusinessType.DELETE)
	@DeleteMapping("/{templateIds}")
    public AjaxResult remove(@PathVariable Long[] templateIds)
    {
        return toAjax(fTemplateService.deleteFTemplateByTemplateIds(templateIds));
    }

    /**
     * 模板保存及表单、表单属性保存
     */
    @PreAuthorize("@ss.hasPermi('template:f_template:add')")
    @Log(title = "模板", businessType = BusinessType.INSERT)
    @PostMapping("/addTemplate")
    public AjaxResult addTemplate(@RequestBody FTemplateVo vo){
        vo.setCreateBy(getUsername());
        vo.setCreateTime(new Date());
        fTemplateService.addTemplate(vo);
        return toAjax(true);
    }

    /**
     * 删除模板（逻辑删除）
     */
    @PreAuthorize("@ss.hasPermi('template:f_template:remove')")
    @Log(title = "模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{templateIds}")
    public AjaxResult delete(@PathVariable Long[] templateIds)
    {
        return toAjax(fTemplateService.deleteFTemplateByTempIds(templateIds));
    }

}
