package com.dchealth.web.controller.patient;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import org.springframework.scheduling.annotation.Async;
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
import com.dchealth.system.domain.PatientCardInfo;
import com.dchealth.system.service.IPatientCardInfoService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 患者主号管理Controller
 * 
 * @author a
 * @date 2024-10-30
 */
@RestController
@RequestMapping("/system/patientCard")
public class PatientCardInfoController extends BaseController
{
    @Autowired
    private IPatientCardInfoService patientCardInfoService;

    /**
     * 查询患者主号管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:patientCard:list')")
    @GetMapping("/list")
    public TableDataInfo list(PatientCardInfo patientCardInfo)
    {
        startPage();
        List<PatientCardInfo> list = patientCardInfoService.selectPatientCardInfoList(patientCardInfo);
        return getDataTable(list);
    }

    /**
     * 导出患者主号管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:patientCard:export')")
    @Log(title = "患者主号管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PatientCardInfo patientCardInfo)
    {
        List<PatientCardInfo> list = patientCardInfoService.selectPatientCardInfoList(patientCardInfo);
        ExcelUtil<PatientCardInfo> util = new ExcelUtil<PatientCardInfo>(PatientCardInfo.class);
        util.exportExcel(response, list, "患者主号管理数据");
    }

    /**
     * 获取患者主号管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:patientCard:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(patientCardInfoService.selectPatientCardInfoById(id));
    }

    /**
     * 新增患者主号管理
     */
    @PreAuthorize("@ss.hasPermi('system:patientCard:add')")
    @Log(title = "患者主号管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PatientCardInfo patientCardInfo)
    {
        return toAjax(patientCardInfoService.insertPatientCardInfo(patientCardInfo));
    }

    /**
     * 修改患者主号管理
     */
    @PreAuthorize("@ss.hasPermi('system:patientCard:edit')")
    @Log(title = "患者主号管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PatientCardInfo patientCardInfo)
    {
//        patientCardInfoService.updatePatientCardInfo(patientCardInfo);
//        return AjaxResult.success();
        Integer patientMasterNo = patientCardInfo.getPatientMasterNo();
        if (Integer.valueOf(0).equals(patientMasterNo)){
            return error("请设置成主号!");
        }
        patientCardInfoService.updateMasterNumber(patientCardInfo);

        //发布时间, 更新患者id, 调用更新服务
        new Thread(()->{
            Map<String, Integer> stringIntegerMap = patientCardInfoService.updatePatientAllPersonId();
            logger.info("更新表影响行统计_{}",JSON.toJSONString(stringIntegerMap));
        }).start();

        return AjaxResult.success("后台任务正在执行, 请等待更新状态变为已更新!");

    }


    /**
     * 设置主号
     */
    @PreAuthorize("@ss.hasPermi('system:patientCard:setMaster')")
    @Log(title = "患者主号管理", businessType = BusinessType.UPDATE)
    @PostMapping("setMaster")
    public AjaxResult setMaster(@RequestBody PatientCardInfo patientCardInfo)
    {
        Integer patientMasterNo = patientCardInfo.getPatientMasterNo();
        if (Integer.valueOf(0).equals(patientMasterNo)){
            return error("请设置成主号!");
        }
        patientCardInfoService.updateMasterNumber(patientCardInfo);
        //发布时间, 更新患者id, 调用更新服务
        Map<String, Integer> stringIntegerMap = patientCardInfoService.updatePatientAllPersonId();

        return AjaxResult.success(JSON.toJSONString(stringIntegerMap));
    }

    /**
     * 删除患者主号管理
     */
    @PreAuthorize("@ss.hasPermi('system:patientCard:remove')")
    @Log(title = "患者主号管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(patientCardInfoService.deletePatientCardInfoByIds(ids));
    }
}
