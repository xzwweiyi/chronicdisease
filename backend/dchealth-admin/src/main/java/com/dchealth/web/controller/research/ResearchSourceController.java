package com.dchealth.web.controller.research;

import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.research.domain.ResearchSource;
import com.dchealth.research.domain.vo.ResearchIdsVo;
import com.dchealth.research.service.IResearchSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单
 */
@RestController
@RequestMapping("/researh/source")
public class ResearchSourceController extends BaseController {

    @Autowired
    IResearchSourceService iResearchSourceService;

    /**
     * 查询菜单
     *
     * @param researchSource
     * @return
     */
    @PreAuthorize("@ss.hasPermi('researh:source:query')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody ResearchSource researchSource) {
        startPage();
        List<ResearchSource> list = iResearchSourceService.selectResearchSourceList(researchSource);
        return getDataTable(list);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('researh:source:query')")
    @PostMapping(value = "/findOneResearchSource")
    public AjaxResult findOneResearchSource(@RequestBody ResearchSource researchSource) {
        return AjaxResult.success(iResearchSourceService.findOneResearchSource(researchSource.getSourceId()));
    }

    /**
     * 新增菜单
     *
     * @param researchSource
     * @return
     */
    @PreAuthorize("@ss.hasPermi('researh:source:add')")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody ResearchSource researchSource) {
        return toAjax(iResearchSourceService.insertResearchSource(researchSource));
    }

    /**
     * 修改菜单
     *
     * @param researchSource
     * @return
     */
    @PreAuthorize("@ss.hasPermi('researh:source:update')")
    @PostMapping("/update")
    public AjaxResult update(@RequestBody ResearchSource researchSource) {
        return toAjax(iResearchSourceService.updateResearchSource(researchSource));
    }


    /**
     * 删除菜单
     */
    @PreAuthorize("@ss.hasPermi('researh:source:delete')")
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody ResearchSource researchSource) {
        if (iResearchSourceService.isHaveChildResearchSource(researchSource)) {
            return AjaxResult.error("存在子菜单,不允许删除");
        }
        if (iResearchSourceService.isDistributionResearchSource(researchSource)) {
            return AjaxResult.error("菜单已分配,不允许删除");
        }
        return toAjax(iResearchSourceService.deleteResearchSource(researchSource));
    }


    /**
     * 根据用户id查询菜单
     */
    @PreAuthorize("@ss.hasPermi('researh:source:query')")
    @PostMapping("/findResearchSourceByUserId")
    public AjaxResult findResearchSourceByUserId(@RequestBody ResearchIdsVo researchIdsVo) {
        return  AjaxResult.success(iResearchSourceService.findResearchSourceByUserId(researchIdsVo));
    }

    /**
     * 根据角色id查询菜单
     */
    @PreAuthorize("@ss.hasPermi('researh:source:query')")
    @PostMapping("/findResearchSourceByRoleId")
    public AjaxResult findResearchSourceByRoleId(@RequestBody ResearchIdsVo researchIdsVo) {
        return  AjaxResult.success(iResearchSourceService.findResearchSourceByRoleId(researchIdsVo));
    }


    /**
     * 加载角色菜单列表树
     */
    @PreAuthorize("@ss.hasPermi('researh:source:query')")
    @PostMapping("/treeResearchSource")
    public AjaxResult treeResearchSource() {
        List<ResearchSource> researchSources = iResearchSourceService.treeResearchSource();
        return AjaxResult.success(researchSources);
    }


}
