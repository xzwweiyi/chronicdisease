package com.dchealth.web.controller.research;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.dchealth.research.domain.ResearchModule;
import com.dchealth.research.service.IResearchModuleService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 队列模块Controller
 * 
 * @author lchbu
 * @date 2022-08-25
 */
@RestController
@RequestMapping("/research/module")
public class ResearchModuleController extends BaseController
{
    @Autowired
    private IResearchModuleService researchModuleService;

    /**
     * 查询队列模块列表
     */
    @PreAuthorize("@ss.hasPermi('research:module:list')")
    @GetMapping("/list")
    public TableDataInfo list(ResearchModule researchModule)
    {
        startPage();
        List<ResearchModule> list = researchModuleService.selectResearchModuleList(researchModule);
        return getDataTable(list);
    }

    /**
     * 导出队列模块列表
     */
    @PreAuthorize("@ss.hasPermi('research:module:export')")
    @Log(title = "队列模块", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResearchModule researchModule)
    {
        List<ResearchModule> list = researchModuleService.selectResearchModuleList(researchModule);
        ExcelUtil<ResearchModule> util = new ExcelUtil<ResearchModule>(ResearchModule.class);
        util.exportExcel(response, list, "队列模块数据");
    }

    /**
     * 获取队列模块详细信息
     */
    @PreAuthorize("@ss.hasPermi('research:module:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(researchModuleService.selectResearchModuleById(id));
    }

    /**
     * 新增队列模块
     */
    @PreAuthorize("@ss.hasPermi('research:module:add')")
    @Log(title = "队列模块", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResearchModule researchModule)
    {
        return toAjax(researchModuleService.insertResearchModule(researchModule));
    }

    /**
     * 修改队列模块
     */
    @PreAuthorize("@ss.hasPermi('research:module:edit')")
    @Log(title = "队列模块", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResearchModule researchModule)
    {
        return toAjax(researchModuleService.updateResearchModule(researchModule));
    }

    /**
     * 删除队列模块
     */
    @PreAuthorize("@ss.hasPermi('research:module:remove')")
    @Log(title = "队列模块", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(researchModuleService.deleteResearchModuleByIds(ids));
    }
}
