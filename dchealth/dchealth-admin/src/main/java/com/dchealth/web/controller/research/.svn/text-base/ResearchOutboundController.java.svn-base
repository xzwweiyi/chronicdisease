package com.dchealth.web.controller.research;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.medical.vo.xunfei.SpeechListRes;
import com.dchealth.medical.vo.xunfei.Var;
import com.dchealth.research.domain.ResearchOutbound;
import com.dchealth.research.domain.vo.ResearchOutboundVo;
import com.dchealth.research.service.IResearchOutboundService;
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


@RestController
@RequestMapping("/research/outbound")
public class ResearchOutboundController extends BaseController{
    @Autowired
    private IResearchOutboundService researchOutboundService;

    /**
     * 查询队列智能外呼设置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ResearchOutbound researchOutbound)
    {
        startPage();
        List<ResearchOutbound> list = researchOutboundService.selectResearchOutboundList(researchOutbound);
        return getDataTable(list);
    }


    /**
     * 查询队列智能外呼设置列表
     */
    @GetMapping("/speechList")
    public TableDataInfo speechList(){
        List<SpeechListRes> list = researchOutboundService.speechList();
//        List<SpeechListRes> list = new ArrayList<>();
        return getDataTable(list);
    }

    /**
     * 查询队列智能外呼设置列表
     */
//    @GetMapping("/speechDetailBySpeechId/{speechId}")
//    public TableDataInfo speechDetailBySpeechId(@PathVariable("speechId") String speechId){
//        List<Var> list = researchOutboundService.speechDetailBySpeechId(speechId);
//
//        return getDataTable(list);
//    }

    @GetMapping("/speechDetailBySpeechId/{speechId}")
    public AjaxResult speechDetailBySpeechId(@PathVariable("speechId") String speechId){
        return AjaxResult.success(researchOutboundService.speechTemplateImage(speechId));
    }


    /**
     * 导出队列智能外呼设置列表
     */
    @PreAuthorize("@ss.hasPermi('research:outbound:export')")
    @Log(title = "队列智能外呼设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResearchOutbound researchOutbound)
    {
        List<ResearchOutbound> list = researchOutboundService.selectResearchOutboundList(researchOutbound);
        ExcelUtil<ResearchOutbound> util = new ExcelUtil<ResearchOutbound>(ResearchOutbound.class);
        util.exportExcel(response, list, "队列智能外呼设置数据");
    }

    /**
     * 获取队列智能外呼设置详细信息
     */
    @PreAuthorize("@ss.hasPermi('research:outbound:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(researchOutboundService.selectResearchOutboundById(id));
    }

//    /**
//     * 新增队列智能外呼设置
//     */
//    @PreAuthorize("@ss.hasPermi('research:outbound:add')")
//    @Log(title = "队列智能外呼设置", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody ResearchOutbound researchOutbound)
//    {
//        return toAjax(researchOutboundService.insertResearchOutbound(researchOutbound));
//    }

    /**
     * 队列智能外呼设置
     */
//    @PreAuthorize("@ss.hasPermi('research:outbound:add')")
    @Log(title = "队列智能外呼设置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody ResearchOutboundVo researchOutboundVo)
    {
        researchOutboundVo.setUserId(getUserId());
        researchOutboundVo.setUpdateBy(getUsername());
        return toAjax(researchOutboundService.add(researchOutboundVo));
    }

    /**
     * 修改队列智能外呼设置
     */
    @PreAuthorize("@ss.hasPermi('research:outbound:edit')")
    @Log(title = "队列智能外呼设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResearchOutbound researchOutbound)
    {
        return toAjax(researchOutboundService.updateResearchOutbound(researchOutbound));
    }

    /**
     * 删除队列智能外呼设置
     */
    @PreAuthorize("@ss.hasPermi('research:outbound:remove')")
    @Log(title = "队列智能外呼设置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(researchOutboundService.deleteResearchOutboundByIds(ids));
    }




}
