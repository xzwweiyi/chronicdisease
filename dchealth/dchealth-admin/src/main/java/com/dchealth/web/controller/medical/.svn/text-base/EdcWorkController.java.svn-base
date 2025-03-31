package com.dchealth.web.controller.medical;

import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.medical.domain.vo.PatientRecordVo;
import com.dchealth.medical.service.IEdcWorkService;
import com.dchealth.medical.service.IExportTableService;
import com.dchealth.medical.service.IPatientInfoService;
import com.dchealth.research.domain.vo.ResearchInfoVo;
import com.dchealth.system.service.ISysUserService;
import com.dchealth.template.domain.vo.FMedicalInfoReq;
import com.dchealth.template.domain.vo.FMedicalPropertyVo;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * EDC工作台
 */
@RestController
@RequestMapping("/edc/work")
public class EdcWorkController extends BaseController {

    @Autowired
    private IEdcWorkService iEdcWorkService;

    @Autowired
    private IExportTableService iExportTableService;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * EDC工作台任务列表树
     * @return
     */
    @GetMapping("/edcTree")
    public AjaxResult edcTree(ResearchInfoVo vo){
//        SysUser sysUser = sysUserService.selectUserById(getUserId());
//        vo.setDeptId(sysUser.getDeptId());
        vo.setUserId(getUserId());
        return AjaxResult.success(iEdcWorkService.edcTree(vo));
    }

    /**
     * EDC任务列表
     * @param vo
     * @return
     */
    @GetMapping("/edcList")
    public AjaxResult edcList(PatientRecordVo vo){
        return AjaxResult.success(iEdcWorkService.selectEcdPatientRecordVoList(vo));
    }

    /**
     * 导出edc信息列表
     */
    @Log(title = "edc任务导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PatientRecordVo vo)
    {
        List<PatientRecordVo> list = iEdcWorkService.selectEcdPatientRecordVoList(vo);
        ExcelUtil<PatientRecordVo> util = new ExcelUtil<>(PatientRecordVo.class);
        util.exportExcel(response, list, "edc任务列表");
    }

    /**
     * 根据随访id查询模板信息以及模板对应属性值
     * (弃用)
     * @param vo
     * @return
     */
    @GetMapping(value = "/recordDetailInfo")
    public AjaxResult edcRecordDetailInfo(FMedicalInfoReq vo){
        return AjaxResult.success(iEdcWorkService.edcRecordDetailInfo(vo));
    }

    /**
     * edc工作台档案详情table页
     * @param vo
     * @return
     */
    @GetMapping(value = "/edcPatientRecordInfo")
    public AjaxResult edcPatientRecordInfo(FMedicalInfoReq vo){
        return AjaxResult.success(iEdcWorkService.edcPatientRecordInfo(vo));
    }

    /**
     * 模板数据入库-edc档案详情保存/保存提交
     * @param proVo
     * @return
     */
    @PostMapping("/edcInsert")
    public AjaxResult edcInsert(@RequestBody FMedicalPropertyVo proVo){
        iEdcWorkService.edcInsert(proVo);
        return AjaxResult.success();
    }

    /**
     * EDC工作台档案详情左侧树状图
     * @return
     */
    @GetMapping("/followMedicalFromTree")
    public AjaxResult followMedicalFromTree(FMedicalInfoReq vo)
    {
        return AjaxResult.success(iEdcWorkService.followMedicalFromTree(vo));
    }

    /**
     * edc工作台档案单模块详情
     * @param vo
     * @return
     */
    @GetMapping(value = "/edcPatientRecordDetail")
    public AjaxResult edcPatientRecordDetail(FMedicalInfoReq vo){
        return AjaxResult.success(iEdcWorkService.edcPatientRecordDetailInfo(vo));
    }

    /**
     * 锁库
     */
    @GetMapping(value = "/lock")
    public AjaxResult lock(FMedicalInfoReq vo){
        return AjaxResult.success(iEdcWorkService.lock(vo));
    }

    /**
     * 工作档案导出
     */
    @Log(title = "工作档案导出", businessType = BusinessType.EXPORT)
    @PostMapping("/recordExport")
    public void recordExport(HttpServletResponse response, PatientRecordVo vo)
    {
        //档案信息
        List<PatientRecordVo> list = iEdcWorkService.selectEcdPatientRecordVoList(vo);
        ExcelUtil util = new ExcelUtil<>();
        Workbook workbook = util.createAndReturnWorkbook();
        iExportTableService.exportTable(workbook, "工作档案导出列表", list, vo.getResearchId());
        util.exportExcel(response, "工作档案导出列表");
    }

}
