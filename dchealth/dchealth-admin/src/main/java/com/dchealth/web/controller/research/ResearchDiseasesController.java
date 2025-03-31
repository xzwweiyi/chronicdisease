package com.dchealth.web.controller.research;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.core.domain.model.LoginUser;
import com.dchealth.research.domain.DiseasesGroups;
import com.dchealth.research.domain.vo.ResearchInfoVo;
import com.dchealth.research.service.IDiseasesGroupsService;
import com.dchealth.system.service.ISysUserService;
import org.apache.commons.collections4.CollectionUtils;
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
import com.dchealth.research.domain.ResearchDiseases;
import com.dchealth.research.service.IResearchDiseasesService;
import com.dchealth.common.utils.poi.ExcelUtil;

/**
 * 病种Controller
 * 
 * @author lchbu
 * @date 2022-08-24
 */
@RestController
@RequestMapping("/research/diseases")
public class ResearchDiseasesController extends BaseController
{
    @Autowired
    private IResearchDiseasesService researchDiseasesService;
    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IDiseasesGroupsService diseasesGroupsService;

    /**
     * 查询病种下面队列信息，没有队列的也要展示病种信息
     */
//    @PreAuthorize("@ss.hasPermi('research:diseases:list')")
    @GetMapping("/list")
    public AjaxResult list(ResearchInfoVo vo)
    {
//        SysUser sysUser = sysUserService.selectUserById(getUserId());
//        vo.setDeptId(sysUser.getDeptId());
        vo.setUserId(getUserId());
        return AjaxResult.success(researchDiseasesService.selectDiseasesAndResearchInfo(vo));
    }

    /**
     * 根据当前登录用户所在科室查询
     */
    @PreAuthorize("@ss.hasPermi('research:diseases:list')")
    @GetMapping("/list2")
    public AjaxResult list2(ResearchInfoVo vo)
    {
        // 获取当前登录用户信息
        SysUser sysUser = getLoginUser().getUser();

        // 如果当前用户不是管理员，则限制查询范围到用户所在科室
        if(!sysUser.isAdmin()) {
            vo.setDeptId(sysUser.getDeptId());

            // 如果用户没有权限查询所有科室的研究疾病列表，则进一步限制查询范围到用户本身
            if (!"1".equals(sysUser.getIsAlldeptResearch())) {
                vo.setUserId(sysUser.getUserId());
            }
        }

        // 调用服务方法获取研究疾病列表，并返回成功结果
        return AjaxResult.success(researchDiseasesService.selectResearchDiseasesListTree(vo));
    }

    /**
     * 查询病种列表
     */
    @PreAuthorize("@ss.hasPermi('research:diseases:list')")
    @GetMapping("/select")
    public AjaxResult selectList(ResearchInfoVo vo)
    {
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        vo.setDeptId(sysUser.getDeptId());
        return AjaxResult.success(researchDiseasesService.selectList(vo));
    }

    /**
     * 导出病种列表
     */
    @PreAuthorize("@ss.hasPermi('research:diseases:export')")
    @Log(title = "病种", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResearchDiseases researchDiseases)
    {
        List<ResearchDiseases> list = researchDiseasesService.selectResearchDiseasesList(researchDiseases);
        ExcelUtil<ResearchDiseases> util = new ExcelUtil<ResearchDiseases>(ResearchDiseases.class);
        util.exportExcel(response, list, "病种数据");
    }

    /**
     * 获取病种详细信息
     */
    @PreAuthorize("@ss.hasPermi('research:diseases:query')")
    @GetMapping(value = "/{diseaseId}")
    public AjaxResult getInfo(@PathVariable("diseaseId") Long diseaseId)
    {
        return AjaxResult.success(researchDiseasesService.selectResearchDiseasesByDiseaseId(diseaseId));
    }

    /**
     * 新增病种
     */
    @PreAuthorize("@ss.hasPermi('research:diseases:add')")
    @Log(title = "病种", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResearchDiseases researchDiseases){

        DiseasesGroups diseasesGroups = new DiseasesGroups();
        diseasesGroups.setDiseasesGroupsName(researchDiseases.getDiseaseName());
        List<DiseasesGroups> diseasesGroups1 = diseasesGroupsService.selectDiseasesGroupsList(diseasesGroups);
        if (CollectionUtils.isEmpty(diseasesGroups1)){
            return AjaxResult.error("创建失败,基础病重不存在");
        }
        researchDiseases.setDiseasesGroupsId(String.valueOf(diseasesGroups1.get(0).getDiseasesGroupsId()));

        ResearchDiseases r = new ResearchDiseases();
        r.setDiseaseName(researchDiseases.getDiseaseName());
        r.setDeptId(researchDiseases.getDeptId());
        List<ResearchDiseases> list = researchDiseasesService.selectResearchDiseasesList(r);
        if(list!=null && list.size()>0){
            return AjaxResult.error("创建失败,病种已存在");
        }
        return toAjax(researchDiseasesService.insertResearchDiseases(researchDiseases));
    }

    /**
     * 修改病种
     */
    @PreAuthorize("@ss.hasPermi('research:diseases:edit')")
    @Log(title = "病种", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResearchDiseases researchDiseases)
    {
        return toAjax(researchDiseasesService.updateResearchDiseases(researchDiseases));
    }

    /**
     * 删除病种
     */
    @PreAuthorize("@ss.hasPermi('research:diseases:remove')")
    @Log(title = "病种", businessType = BusinessType.DELETE)
	@DeleteMapping("/{diseaseId}")
    public AjaxResult remove(@PathVariable Long diseaseId)
    {
        return researchDiseasesService.deleteResearchDiseasesByDiseaseId(diseaseId);
    }

    /**
     * 查询患者信息列表
     */
    @PreAuthorize("@ss.hasPermi('research:diseases:treeList')")
    @GetMapping("/treeList")
    public AjaxResult treeList(ResearchInfoVo vo)
    {
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        vo.setUserId(sysUser.getUserId());
        return AjaxResult.success(researchDiseasesService.selectPatientResearchDiseasesTree(vo));
    }
}
