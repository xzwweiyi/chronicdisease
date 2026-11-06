package com.dchealth.web.controller.cms;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.cms.domain.CmsArticle;
import com.dchealth.cms.domain.CmsCategory;
import com.dchealth.cms.domain.CmsCategoryTree;
import com.dchealth.cms.domain.vo.CmsArticleCateGoryVo;
import com.dchealth.cms.service.ICmsArticleService;
import com.dchealth.cms.service.ICmsCategoryService;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.system.service.ISysRoleDeptService;
import com.dchealth.system.service.ISysUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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

import static com.dchealth.base.service.BaseRef.list;

/**
 * 资讯分类Controller
 *
 * @author ruoyi
 * @date 2022-03-16
 */
@RestController
@RequestMapping("/cms/category")
public class CmsCategoryController extends BaseController {
    @Autowired
    private ICmsCategoryService cmsCategoryService;
    @Autowired
    private ISysRoleDeptService sysRoleDeptService;
    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ICmsArticleService cmsArticleService;


    /**
     * 新增资讯分类
     */
    @PreAuthorize("@ss.hasPermi('system:category:add')")
    @Log(title = "资讯分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add( @RequestBody CmsCategory cmsCategory) {
        cmsCategory.setCreateId(getUserId());
        cmsCategory.setCreateBy(getUsername());
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        cmsCategory.setDeptId(sysUser.getDeptId());
        return toAjax(cmsCategoryService.insertCmsCategory(cmsCategory));
    }

    /**
     * 查询资讯分类列表(包含公共和科室)
     */
    @PreAuthorize("@ss.hasPermi('system:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(CmsCategory cmsCategory) {
        Integer commonFlag = cmsCategory.getCommonFlag() == null ? 1:cmsCategory.getCommonFlag();
        if (commonFlag == 0) {
            SysUser sysUser = sysUserService.selectUserById(getUserId());
            Long deptId = cmsCategory.getDeptId();
            if (deptId == null){
                deptId = sysUser.getDeptId();
            }
            cmsCategory.setDeptId(deptId);
        }
        startPage();
        cmsCategory.setCommonFlag(commonFlag);
        List<CmsCategory> list = cmsCategoryService.selectCmsCategoryList(cmsCategory);
        return getDataTable(list);
    }





    /**
     * 查询科室分类树列表结构(包含资讯)
     */
//    @PreAuthorize("@ss.hasPermi('system:category:treeList')")
    @GetMapping("/treeList")
    public AjaxResult selectTree(CmsCategory cmsCategory) {
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        cmsCategory.setDeptId(sysUser.getDeptId());
        cmsCategory.setCommonFlag(1);
        //查询科室下所有文章
        CmsArticle cmsArticleParam = new CmsArticle();
        cmsArticleParam.setDeptId(sysUser.getDeptId());
        cmsArticleParam.setCategoryId(cmsCategory.getId());
        cmsArticleParam.setTitle(cmsCategory.getTitle());
        List<CmsArticleCateGoryVo> cmsArticlesList = cmsArticleService.selectCmsDeptCategoryArticleList(cmsArticleParam);
        Map<String, List<CmsArticleCateGoryVo>> categotyArticleMap = cmsArticlesList.stream().collect(Collectors.groupingBy(cmsArticleCateGoryVo -> cmsArticleCateGoryVo.getCategoryId()+"," + cmsArticleCateGoryVo.getParentIds()));
        List<CmsCategoryTree> list = cmsCategoryService.selectCmsCategoryTree(cmsCategory,categotyArticleMap);
        return AjaxResult.success(list);
    }


    /**
     * 查询科室分类树结构(不包含资讯)
     */
    @GetMapping("/tree")
    public AjaxResult tree(CmsCategory cmsCategory) {
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        cmsCategory.setDeptId(sysUser.getDeptId());
        cmsCategory.setCommonFlag(0);
        List<CmsCategoryTree> list = cmsCategoryService.selectCmsCategoryTree(cmsCategory);
        return AjaxResult.success(list);
    }


    /**
     * 查询科室一级分类
     */
    @GetMapping("/oneLevel")
    public AjaxResult oneLevelCategory(CmsCategory cmsCategory) {
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        //查询科室下所有文章
        CmsArticle cmsArticleParam = new CmsArticle();
        cmsArticleParam.setDeptId(sysUser.getDeptId());
        cmsArticleParam.setCategoryId(cmsCategory.getId());
        cmsArticleParam.setTitle(cmsCategory.getTitle());
        List<CmsArticleCateGoryVo> cmsArticlesList = cmsArticleService.selectCmsDeptCategoryArticleList(cmsArticleParam);
        Set<String> ids = new HashSet<>();
        for (CmsArticleCateGoryVo cateGoryVo:cmsArticlesList) {
            String[] split = cateGoryVo.getParentIds().split(",");
            if (split.length > 1){
                ids.add(split[1]);
            }else {
                ids.add(String.valueOf(cateGoryVo.getCategoryId()));
            }
        }
        List<CmsCategory> cmsCategories = cmsCategoryService.selectCmsCategoryListByIds(ids);
        return AjaxResult.success(cmsCategories);
    }



    /**
     * 修改资讯分类
     */
    @PreAuthorize("@ss.hasPermi('system:category:edit')")
    @Log(title = "资讯分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CmsCategory cmsCategory) {
        cmsCategory.setUpdateBy(getUsername());
        return toAjax(cmsCategoryService.updateCmsCategory(cmsCategory));
    }

    /**
     * 删除分类
     */
    @PreAuthorize("@ss.hasPermi('system:category:remove')")
    @Log(title = "删除分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/edu/{id}")
    public AjaxResult removeEdu(@PathVariable Long id) {
        return toAjax(cmsCategoryService.updateCmsCategoryById(id));
    }


    /**
     * 删除资讯分类
     */
    @PreAuthorize("@ss.hasPermi('system:category:remove')")
    @Log(title = "资讯分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(cmsCategoryService.deleteCmsCategoryByIds(ids));
    }

    /**
     * 获取资讯分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:category:deptQuery')")
    @GetMapping(value = "/dept/{deptId}")
    public AjaxResult getCategoryInfo(@PathVariable("deptId") Long deptId) {
        return AjaxResult.success(cmsCategoryService.selectCmsCategoryByDeptId(deptId));
    }

    /**
     * 导出资讯分类列表
     */
    @PreAuthorize("@ss.hasPermi('system:category:export')")
    @Log(title = "资讯分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CmsCategory cmsCategory) {
//        if(getUserId()>1){
//            cmsCategory.setCreateId(getUserId());
//            String deptIds = sysRoleDeptService.getDeptIdsByUserId(cmsCategory.getCreateId());
//            cmsCategory.setDeptIds(deptIds);
//        }
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        cmsCategory.setDeptId(sysUser.getDeptId());
        List<CmsCategory> list = cmsCategoryService.selectCmsCategoryList(cmsCategory);
        ExcelUtil<CmsCategory> util = new ExcelUtil<CmsCategory>(CmsCategory.class);
        util.exportExcel(response, list, "资讯分类数据");
    }

    /**
     * 获取资讯分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:category:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(cmsCategoryService.selectCmsCategoryById(id));
    }

    /**
     * 查询公共分类树结构(不包含当前文章)
     */
    @GetMapping("filterTree")
    public AjaxResult commonTree(CmsCategory cmsCategory) {
        cmsCategory.setCommonFlag(cmsCategory.getCommonFlag() == null ? 1 : cmsCategory.getCommonFlag());
        if (cmsCategory.getCommonFlag() == 0 &&  Objects.isNull(cmsCategory.getDeptId()) && Objects.isNull(cmsCategory.getCategoryId())){
            return  AjaxResult.error("部门ID或分类ID不可为空");
        }
        List<CmsCategoryTree> list = cmsCategoryService.selectCmsCategoryTree(cmsCategory);
        return AjaxResult.success(list);
    }


}
