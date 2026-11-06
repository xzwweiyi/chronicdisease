package com.dchealth.web.controller.research;

import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.research.domain.ResearchHospital;
import com.dchealth.research.domain.vo.ResearchIdsVo;
import com.dchealth.research.service.IResearchHospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 队列参与中心信息
 */
@RestController
@RequestMapping("/research/hospital")
public class ResearchHospitalController extends BaseController {

    @Autowired
    IResearchHospitalService iResearchHospitalService;

    /**
     * 获取队列参与中心信息
     */
    @PreAuthorize("@ss.hasPermi('researh:hospital:query')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody ResearchHospital researhHospital)
    {
        startPage();
        List<ResearchHospital> researhHospitals = iResearchHospitalService.selectResearchHospital(researhHospital);
        return getDataTable(researhHospitals);
    }

    /**
     * 新增队列参与中心信息
     * @param researchHospital
     * @return
     */
    @PreAuthorize("@ss.hasPermi('researh:hospital:add')")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody ResearchHospital researchHospital)
    {
        return toAjax(iResearchHospitalService.insertResearchHospital(researchHospital));
    }


    /**
     * 修改队列参与中心信息
     * @param researchHospital
     * @return
     */
    @PreAuthorize("@ss.hasPermi('researh:hospital:update')")
    @PostMapping("/update")
    public AjaxResult update(@RequestBody ResearchHospital researchHospital)
    {
        return toAjax(iResearchHospitalService.updateResearchHospital(researchHospital));
    }

    /**
     * 删除队列参与中心信息
     * @param
     * @return
     */
    @PreAuthorize("@ss.hasPermi('researh:hospital:delete')")
    @PostMapping("/delete")
    public AjaxResult remove(@RequestBody ResearchIdsVo researchIdsVo)
    {
        return toAjax(iResearchHospitalService.deleteResearchHospitalByIds(researchIdsVo.getIds()));
    }

    /**
     * 医院中心字典表
     */
    @PreAuthorize("@ss.hasPermi('researh:hospital:query')")
    @PostMapping("/findHospitalDict")
    public AjaxResult findHospitalDict() {
        return AjaxResult.success(iResearchHospitalService.findHospitalDict());
    }
    /**
     * 队列字典表
     */
    @PreAuthorize("@ss.hasPermi('researh:hospital:query')")
    @PostMapping("/findResearchInfoDict")
    public AjaxResult findResearchInfoDict() {
        return AjaxResult.success(iResearchHospitalService.findResearchInfoDict());
    }
}
