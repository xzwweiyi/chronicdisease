package com.dchealth.web.controller.research;

import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.research.domain.DiseasesGroups;
import com.dchealth.research.service.IDiseasesGroupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diseases/groups")
public class DiseasesGroupsController extends BaseController {

    @Autowired
    private IDiseasesGroupsService iDiseasesGroupsService;


    /**
     * 查询病种列表
     */
    @PreAuthorize("@ss.hasPermi('diseases:groups:list')")
    @PostMapping("/list")
    public AjaxResult list(@RequestBody DiseasesGroups groups){
//        Long userId = getUserId();
//        if(userId>1){
//            groups.setCreateUserId(getUserId());
//        }
        return AjaxResult.success(iDiseasesGroupsService.selectDiseasesGroupsList(groups));
    }

    /**
     * 分页
     * @param groups
     * @return
     */
    @PreAuthorize("@ss.hasPermi('diseases:groups:list')")
    @GetMapping("/listPage")
    public TableDataInfo listPage(DiseasesGroups groups) {
//        Long userId = getUserId();
//        if(userId>1){
//            groups.setCreateUserId(getUserId());
//        }
        startPage();
        List<DiseasesGroups> list = iDiseasesGroupsService.selectDiseasesGroupsListPage(groups);
        return getDataTable(list);
    }

    /**
     * 新增病种
     */
    @PreAuthorize("@ss.hasPermi('diseases:groups:add')")
    //@Log(title = "病种", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody DiseasesGroups groups){
//        Long userId = getUserId();
//        if(userId>1){
//            groups.setCreateUserId(getUserId());
//        }
        DiseasesGroups d = iDiseasesGroupsService.selectBydiseasesGroupsName(groups.getDiseasesGroupsName());
        if(d!=null){
            return AjaxResult.error("病种已存在");
        }
        return toAjax(iDiseasesGroupsService.insertDiseasesGroups(groups));
    }

    /**
     * 修改病种
     */
    @PreAuthorize("@ss.hasPermi('diseases:groups:edit')")
    //@Log(title = "病种", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult update(@RequestBody DiseasesGroups groups){
        DiseasesGroups d = iDiseasesGroupsService.selectBydiseasesGroupsName(groups.getDiseasesGroupsName());
        if(d!=null){
            if(groups.getDiseasesGroupsId().intValue()!= d.getDiseasesGroupsId().intValue())
            return AjaxResult.error("修改失败,修改病种已存在");
        }
        return toAjax(iDiseasesGroupsService.updateDiseasesGroups(groups));
    }

    /**
     * 删除病种
     */
    @PreAuthorize("@ss.hasPermi('diseases:groups:remove')")
    //@Log(title = "病种", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody DiseasesGroups groups)
    {
        return iDiseasesGroupsService.deleteDiseasesGroupsByDiseasesGroupsId(groups);
    }
}
