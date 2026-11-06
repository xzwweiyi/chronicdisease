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
import com.dchealth.template.domain.FMedicalExamForm;
import com.dchealth.template.domain.vo.FMedicalInfoReq;
import com.dchealth.template.service.IFMedicalExamFormService;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 单模板-问卷Controller
 *
 * @author lchbu
 * @date 2022-11-04
 */
@RestController
@RequestMapping("/template/dept/f_medical_exam_form")
public class FMedicalExamDeptFormController extends BaseController {
    @Autowired
    private IFMedicalExamFormService fMedicalExamFormService;

    @Autowired
    private ISysRoleDeptService sysRoleDeptService;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询单模板-问卷列表
     */
    @PreAuthorize("@ss.hasPermi('templateDept:form:list')")
    @GetMapping("/list")
    public TableDataInfo list(FMedicalExamForm fMedicalExamForm) {
//        if(getUserId()>1){
//            fMedicalExamForm.setCreateId(getUserId());
//            String deptIds = sysRoleDeptService.getDeptIdsByUserId(fMedicalExamForm.getCreateId());
//            fMedicalExamForm.setDeptIds(deptIds);
//        }
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        fMedicalExamForm.setDeptId(sysUser.getDeptId());
        startPage();
        List<FMedicalExamForm> list = fMedicalExamFormService.selectFMedicalExamFormList(fMedicalExamForm);
        return getDataTable(list);
    }

    /**
     * 导出单模板-问卷列表
     */
    @PreAuthorize("@ss.hasPermi('templateDept:form:export')")
    @Log(title = "单模板-问卷", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FMedicalExamForm fMedicalExamForm) {
//        if(getUserId()>1){
//            fMedicalExamForm.setCreateId(getUserId());
//            String deptIds = sysRoleDeptService.getDeptIdsByUserId(fMedicalExamForm.getCreateId());
//            fMedicalExamForm.setDeptIds(deptIds);
//        }
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        fMedicalExamForm.setDeptId(sysUser.getDeptId());
        List<FMedicalExamForm> list = fMedicalExamFormService.selectFMedicalExamFormList(fMedicalExamForm);
        ExcelUtil<FMedicalExamForm> util = new ExcelUtil<FMedicalExamForm>(FMedicalExamForm.class);
        util.exportExcel(response, list, "单模板-问卷数据");
    }

    /**
     * 获取单模板-问卷详细信息
     */
    @PreAuthorize("@ss.hasPermi('templateDept:form:query')")
    @GetMapping(value = "/{medicalFormId}")
    public AjaxResult getInfo(@PathVariable("medicalFormId") Long medicalFormId) {
        return AjaxResult.success(fMedicalExamFormService.selectFMedicalExamFormByMedicalFormId(medicalFormId));
    }

    /**
     * 新增单模板-问卷
     */
    @PreAuthorize("@ss.hasPermi('templateDept:form:add')")
    @Log(title = "单模板-问卷", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FMedicalExamForm fMedicalExamForm) {
//        if(getUserId()>1){
//            fMedicalExamForm.setCreateId(getUserId());
//            String deptIds = sysRoleDeptService.getDeptIdsByUserId(fMedicalExamForm.getCreateId());
//            fMedicalExamForm.setDeptIds(deptIds);
//        }
        return AjaxResult.error("科室不允许新增单模板-问卷");
    }

    /**
     * 修改单模板-问卷
     */
    @PreAuthorize("@ss.hasPermi('templateDept:form:edit')")
    @Log(title = "单模板-问卷", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FMedicalExamForm fMedicalExamForm) {
        fMedicalExamForm.setUpdateBy(getUsername());
        return  AjaxResult.error("科室不允许修改单模板-问卷");
    }

    /**
     * 删除单模板-问卷
     */
    @PreAuthorize("@ss.hasPermi('templateDept:form:remove')")
    @Log(title = "单模板-问卷", businessType = BusinessType.DELETE)
    @DeleteMapping("/{medicalFormIds}")
    public AjaxResult remove(@PathVariable Long[] medicalFormIds) {
        return  AjaxResult.error("科室不允许删除单模板-问卷");
    }

    /**
     * 获取问卷模板详细信息
     *
     * @param fMedicalInfoReq
     * @return
     */
    @GetMapping(value = "/fMedicalFormExamInfo")
    public AjaxResult getFMedicalFormExamInfo(FMedicalInfoReq fMedicalInfoReq) {
        return AjaxResult.success(fMedicalExamFormService.getFMedicalFormExamInfo(fMedicalInfoReq));
    }

    /**
     * 导出问卷
     */
    @PreAuthorize("@ss.hasPermi('templateDept:form:export')")
    @Log(title = "模板-问卷-答案", businessType = BusinessType.EXPORT)
    @PostMapping("/exportForm")
    public AjaxResult exportForm(HttpServletResponse response, FMedicalExamForm fMedicalExamForm) {
        List<Map<String, String>> maps = fMedicalExamFormService.exportForm(fMedicalExamForm);
        ExcelUtil<LinkedList> util = new ExcelUtil<>(LinkedList.class);
        util.exportMapListExcel(response, maps, "模板-问卷-答案");
        return AjaxResult.success();
    }

    /**
     * 获取问卷模板填报详细信息
     *
     * @param fMedicalInfoReq
     * @return
     */
    @GetMapping(value = "/fMedicalFormExamFillInfo")
    public AjaxResult getFMedicalFormExamFillInfo(FMedicalInfoReq fMedicalInfoReq) {
        return AjaxResult.success(fMedicalExamFormService.getFMedicalFormExamFillInfo(fMedicalInfoReq));
    }

    /**
     * 查询调查问卷详情
     *
     * @param fMedicalInfoReq
     * @return
     */
    @GetMapping(value = "/queryMedicalExamForm")
    public AjaxResult queryMedicalExamForm(FMedicalInfoReq fMedicalInfoReq) {
        return AjaxResult.success(fMedicalExamFormService.queryMedicalExamForm(fMedicalInfoReq));
    }

}
