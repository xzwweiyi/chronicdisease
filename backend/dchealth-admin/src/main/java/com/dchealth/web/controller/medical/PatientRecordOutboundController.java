package com.dchealth.web.controller.medical;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.medical.domain.PatientRecordOutbound;
import com.dchealth.medical.domain.vo.OutboundWorkListVO;
import com.dchealth.medical.service.IPatientRecordOutboundService;
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
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 患者参与队列信息Controller
 * 
 * @author lchbu
 * @date 2023-08-17
 */
@RestController
@RequestMapping("/medical/outbound")
public class PatientRecordOutboundController extends BaseController
{
    @Autowired
    private IPatientRecordOutboundService patientRecordOutboundService;

    /**
     * 查询患者参与队列信息列表
     */
//    @PreAuthorize("@ss.hasPermi('medical:outbound:list')")
    @GetMapping("/list")
    public TableDataInfo list(PatientRecordOutbound patientRecordOutbound)
    {
        startPage();
        List<PatientRecordOutbound> list = patientRecordOutboundService.selectPatientRecordOutboundList(patientRecordOutbound);
        return getDataTable(list);
    }

//    @PreAuthorize("@ss.hasPermi('medical:outbound:selectOutboundList')")
    @GetMapping("/selectOutboundList")
    public TableDataInfo selectOutboundList(PatientRecordOutbound patientRecordOutbound)
    {
        startPage();
        List<OutboundWorkListVO> list = patientRecordOutboundService.selectOutboundList(patientRecordOutbound);
        return getDataTable(list);
    }

    /**
     * 导出患者参与队列信息列表
     */
    @PreAuthorize("@ss.hasPermi('medical:outbound:export')")
    @Log(title = "患者参与队列信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PatientRecordOutbound patientRecordOutbound)
    {
        List<PatientRecordOutbound> list = patientRecordOutboundService.selectPatientRecordOutboundList(patientRecordOutbound);
        ExcelUtil<PatientRecordOutbound> util = new ExcelUtil<PatientRecordOutbound>(PatientRecordOutbound.class);
        util.exportExcel(response, list, "患者参与队列信息数据");
    }

    /**
     * 获取患者参与队列信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('medical:outbound:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(patientRecordOutboundService.selectPatientRecordOutboundById(id));
    }

    /**
     * 新增患者参与队列信息
     */
    @PreAuthorize("@ss.hasPermi('medical:outbound:add')")
    @Log(title = "患者参与队列信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PatientRecordOutbound patientRecordOutbound)
    {
        return toAjax(patientRecordOutboundService.insertPatientRecordOutbound(patientRecordOutbound));
    }

    /**
     * 修改患者参与队列信息
     */
    @PreAuthorize("@ss.hasPermi('medical:outbound:edit')")
    @Log(title = "患者参与队列信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PatientRecordOutbound patientRecordOutbound)
    {
        return toAjax(patientRecordOutboundService.updatePatientRecordOutbound(patientRecordOutbound));
    }

    /**
     * 删除患者参与队列信息
     */
    @PreAuthorize("@ss.hasPermi('medical:outbound:remove')")
    @Log(title = "患者参与队列信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(patientRecordOutboundService.deletePatientRecordOutboundByIds(ids));
    }


    /**
     * 外呼管理
     */
    @PreAuthorize("@ss.hasPermi('medical:outbound:outboundTree')")
    @GetMapping("/tree")
    public AjaxResult outboundTree(){
        Long userId = getUserId();
        return AjaxResult.success(patientRecordOutboundService.outboundTree(userId));
    }


    //生成需要拨打超窗的数据
    @GetMapping(value = "/followRecordByResearchId/{researchId}")
    public AjaxResult followRecordByResearchId(@PathVariable("researchId") Long researchId){
        return AjaxResult.success(patientRecordOutboundService.followRecordByResearchId(researchId));
    }

    //站内信随访和超窗
    @GetMapping(value = "/appNoticeByResearch/{researchId}")
    public AjaxResult appNoticeByResearch(@PathVariable("researchId") Long researchId){
        return AjaxResult.success(patientRecordOutboundService.appNoticeByResearch(researchId));
    }


    //单次队列外呼任务
    @GetMapping(value = "/callbackByResearchId/{researchId}")
    public AjaxResult callbackByResearchId(@PathVariable("researchId") Long researchId){
        return AjaxResult.success(patientRecordOutboundService.callbackByResearchId(researchId));
    }


    //获取外呼结果
    @GetMapping(value = "/getResult/{researchId}")
    public AjaxResult getResult(@PathVariable("researchId") Long researchId){
        return AjaxResult.success(patientRecordOutboundService.getResult(researchId));
    }

    //根据周期id发送预警外呼
    @GetMapping(value = "/callByRecordId/{recordId}")
    public AjaxResult callByRecordId(@PathVariable("recordId") Long recordId){
        return AjaxResult.success(patientRecordOutboundService.callByRecordId(recordId));
    }

    //根据id下载语音文件到本地服务器，并更新语音文件地址
    @GetMapping(value = "/downloadWav/{id}")
    public AjaxResult downloadWav(@PathVariable("id") Long id){
        return AjaxResult.success(patientRecordOutboundService.downloadWav(id));
    }

    //查询七天未处理的质疑
    @GetMapping(value = "/selectSevenDayList/{researchId}")
    public AjaxResult selectSevenDayList(@PathVariable("researchId") Long researchId){
        return AjaxResult.success(patientRecordOutboundService.selectSevenDayList(researchId));
    }

}
