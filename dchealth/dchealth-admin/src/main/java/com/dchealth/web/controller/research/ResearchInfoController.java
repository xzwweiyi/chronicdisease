package com.dchealth.web.controller.research;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.date.DateUtil;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.es.service.SyncRecordDataESService;
import com.dchealth.listener.FollowTreeEvent;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.vo.ResearchPatientVO;
import com.dchealth.medical.service.IPatientInfoService;
import com.dchealth.research.domain.vo.ResearchInfoVo;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.research.service.IResearchInfoService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 队列基础信息Controller
 * 
 * @author lchbu
 * @date 2022-08-22
 */
@RestController
@RequestMapping("/research/info")
public class ResearchInfoController extends BaseController
{
    @Autowired
    private IResearchInfoService researchInfoService;

    @Autowired
    private IPatientInfoService patientInfoService;

    @Autowired
    private SyncRecordDataESService syncRecordDataESService;

    @Autowired
    private ApplicationContext applicationContext;
    /**
     * 查询队列基础信息列表
     */
    @PreAuthorize("@ss.hasPermi('research:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(ResearchInfoVo vo)
    {
        startPage();
        List<ResearchInfoVo> list = researchInfoService.selectResearchInfoDetailList(vo);
        return getDataTable(list);
    }

    /**
     * 导出队列基础信息列表
     */
    @PreAuthorize("@ss.hasPermi('research:info:export')")
    @Log(title = "队列基础信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResearchInfoVo vo)
    {
        List<ResearchInfoVo> list = researchInfoService.selectResearchInfoDetailList(vo);
        ExcelUtil<ResearchInfoVo> util = new ExcelUtil<ResearchInfoVo>(ResearchInfoVo.class);
        util.exportExcel(response, list, "队列基础信息数据");
    }

    /**
     * 获取队列基础信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('research:info:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(researchInfoService.selectResearchInfoDetailById(id));
    }

    /**
     * 获取队列下患者信息
     */
    @PreAuthorize("@ss.hasPermi('research:patient:query')")
    @GetMapping(value = "/getPatient/{id}")
    public TableDataInfo getPatientByResearchId(@PathVariable("id") Long id,
                                                @RequestParam(required = false) String patientId,
                                                @RequestParam(required = false) String patientName)
    {
        startPage();
        List<ResearchPatientVO> researchPatientVOS = patientInfoService.selectResearchPatientList(id,patientId,patientName);
        return getDataTable(researchPatientVOS);
    }

    /**
     * 获取队列下患者信息
     */
    @PreAuthorize("@ss.hasPermi('research:patient:query')")
    @GetMapping(value = "/getResearchPatientList")
    public TableDataInfo getResearchPatientList(PatientInfo patientInfo){
        patientInfo.setEnrollmentHandlerStatus("3");
        patientInfo.setEnrollmentStatus("1");
        startPage();
        List<PatientInfo> list = patientInfoService.selectPatientInfoList(patientInfo);
        return getDataTable(list);
    }


    /**
     * 新增队列基础信息
     */
    @PreAuthorize("@ss.hasPermi('research:info:add')")
    @Log(title = "队列基础信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody ResearchInfoVo vo)
    {
        if(vo.getProjectName() == null || "".equals(vo.getProjectName())){
            return AjaxResult.error("队列名称不能为空");
        }
        vo.setCreateBy(getUsername());
        vo.setUserId(getUserId());
        vo.setCreateTime(new Date());
        return AjaxResult.success(researchInfoService.insertResearchInfo(vo));
    }

    /**
     * 修改队列基础信息
     */
    @PreAuthorize("@ss.hasPermi('research:info:edit')")
    @Log(title = "队列基础信息", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody ResearchInfoVo vo)
    {
        if(vo.getProjectName() == null || "".equals(vo.getProjectName())){
            return AjaxResult.error("队列名称不能为空");
        }
        return toAjax(researchInfoService.updateResearchInfo(vo));
    }

    /**
     * 删除队列基础信息
     */
    @PreAuthorize("@ss.hasPermi('research:info:remove')")
    @Log(title = "队列基础信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(researchInfoService.deleteResearchInfoByIds(ids));
    }

    /**
     * 删除队列基础信息（逻辑删除）
     */
    @PreAuthorize("@ss.hasPermi('research:info:remove')")
    @Log(title = "队列基础信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/updateDelFlag/{id}")
    public AjaxResult updateDelFlag(@PathVariable Long id)
    {
        // 判断权限
        if (!patientInfoService.canDeleteById(getUserId(),id)){
            return AjaxResult.error("当前患者只能由队列创建者删除或者管理员删除");
        }
        int i = researchInfoService.updateInfoDelFlagById(id);
        applicationContext.publishEvent(new FollowTreeEvent(this,getUserId()));
        return toAjax(i);
    }

    /**
     * 删除队列基础信息（逻辑删除）
     */
    @PreAuthorize("@ss.hasPermi('research:info:remove')")
    @Log(title = "队列基础信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable Long id)
    {
        return toAjax(researchInfoService.delete(id));
    }

    /**
     * 更新队列阶段信息
     * id队列id
     * stage阶段（0-简介，1-随访周期，2-CRF，3-患教，4-外呼，5-随访患者，6-随访医生，7-队列状态）
     */
    @Log(title = "更新队列阶段信息", businessType = BusinessType.UPDATE)
    @GetMapping("/updateStage/{id}/{stage}")
    public AjaxResult updateResearchInfoStage(@PathVariable Long id, @PathVariable String stage)
    {
        return toAjax(researchInfoService.updateResearchInfoStage(id,stage));
    }

    /**
     * 队列发布、关闭
     * id队列id
     * 队列发布状态 status（0待发布,1进行中,2已关闭）
     */
    @Log(title = "队列发布、关闭", businessType = BusinessType.UPDATE)
    @GetMapping("/publishResearch/{id}/{status}")
    public AjaxResult publishResearch(@PathVariable Long id, @PathVariable String status)
    {
        AjaxResult ajax = toAjax(researchInfoService.updateResearchInfoStatus(id, status));
        applicationContext.publishEvent(new FollowTreeEvent(this,getUserId()));
        return ajax;
    }


    @GetMapping("/followUpPersonImportHandler")
    public void followUpPersonImportHandler(String id,String startDate,String endDate) throws Exception {
        syncRecordDataESService.syncMpDiagnosisInfos(id, startDate, endDate);
    }
}
