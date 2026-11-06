package com.dchealth.web.controller.template;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.system.service.ISysRoleDeptService;
import com.dchealth.system.service.ISysUserService;
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
import com.dchealth.template.domain.FTemplateCategory;
import com.dchealth.template.service.IFTemplateCategoryService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * CRF库模板分类Controller
 * 
 * @author lchbu
 * @date 2022-08-30
 */
@RestController
@RequestMapping("/template/category")
public class FTemplateCategoryController extends BaseController
{
    @Autowired
    private IFTemplateCategoryService fTemplateCategoryService;
    @Autowired
    private ISysRoleDeptService sysRoleDeptService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询CRF库模板分类列表
     */
    @PreAuthorize("@ss.hasPermi('template:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(FTemplateCategory fTemplateCategory){
//        if(getUserId()>1){
//            String deptIds = sysRoleDeptService.getDeptIdsByUserId(getUserId());
//            fTemplateCategory.setDeptIds(deptIds);
//        }

        Integer commonFlag = fTemplateCategory.getCommonFlag() == null ? 1 : fTemplateCategory.getCommonFlag();
        if (commonFlag == 0){
            Long deptId = fTemplateCategory.getDeptId();
            if (deptId == null){
                SysUser sysUser = sysUserService.selectUserById(getUserId());
                deptId = sysUser.getDeptId();
            }
            fTemplateCategory.setDeptId(deptId);
        }
        fTemplateCategory.setCommonFlag(commonFlag);
        startPage();
        List<FTemplateCategory> list = fTemplateCategoryService.selectFTemplateCategoryList(fTemplateCategory);
        return getDataTable(list);
    }

    /**
     * 导出CRF库模板分类列表
     */
    @PreAuthorize("@ss.hasPermi('template:category:export')")
    @Log(title = "CRF库模板分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FTemplateCategory fTemplateCategory){
//        if(getUserId()>1){
//            String deptIds = sysRoleDeptService.getDeptIdsByUserId(getUserId());
//            fTemplateCategory.setDeptIds(deptIds);
//        }
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        fTemplateCategory.setDeptId(sysUser.getDeptId());
        List<FTemplateCategory> list = fTemplateCategoryService.selectFTemplateCategoryList(fTemplateCategory);
        ExcelUtil<FTemplateCategory> util = new ExcelUtil<FTemplateCategory>(FTemplateCategory.class);
        util.exportExcel(response, list, "CRF库模板分类数据");
    }

    /**
     * 获取CRF库模板分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('template:category:query')")
    @GetMapping(value = "/{templateCategoryId}")
    public AjaxResult getInfo(@PathVariable("templateCategoryId") Long templateCategoryId)
    {
        return AjaxResult.success(fTemplateCategoryService.selectFTemplateCategoryByTemplateCategoryId(templateCategoryId));
    }

    /**
     * 新增CRF库模板分类
     */
    @PreAuthorize("@ss.hasPermi('template:category:add')")
    @Log(title = "CRF库模板分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FTemplateCategory fTemplateCategory){
        fTemplateCategory.setCreateId(getUserId());
        fTemplateCategory.setCreateBy(getUsername());
//        if(getUserId()>1){
//            String deptIds = sysRoleDeptService.getDeptIdsByUserId(getUserId());
//            fTemplateCategory.setDeptIds(deptIds);
//        }
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        fTemplateCategory.setDeptId(sysUser.getDeptId());
        Integer commonFlag = fTemplateCategory.getCommonFlag() == null ? 1 : fTemplateCategory.getCommonFlag();
        fTemplateCategory.setCommonFlag(commonFlag);
        return toAjax(fTemplateCategoryService.insertFTemplateCategory(fTemplateCategory));
    }

    /**
     * 修改CRF库模板分类
     */
    @PreAuthorize("@ss.hasPermi('template:category:edit')")
    @Log(title = "CRF库模板分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FTemplateCategory fTemplateCategory){
        fTemplateCategory.setUpdateBy(getUsername());
        return toAjax(fTemplateCategoryService.updateFTemplateCategory(fTemplateCategory));
    }

    /**
     * 删除CRF库模板分类
     */
    @PreAuthorize("@ss.hasPermi('template:category:remove')")
    @Log(title = "CRF库模板分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{templateCategoryId}")
    public AjaxResult remove(@PathVariable Long templateCategoryId)
    {
        return fTemplateCategoryService.deleteFTemplateCategoryByTemplateCategoryId(templateCategoryId);
    }
}
