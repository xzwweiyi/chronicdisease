package com.dchealth.web.controller.cms;

import com.dchealth.cms.domain.CmsArticle;
import com.dchealth.cms.domain.CmsArticleMoveParam;
import com.dchealth.cms.domain.CmsCategory;
import com.dchealth.cms.domain.CmsDeptArticle;
import com.dchealth.cms.domain.CmsDeptArticleParam;
import com.dchealth.cms.service.ICmsArticleService;
import com.dchealth.cms.service.ICmsCategoryService;
import com.dchealth.cms.service.ICmsDeptArticleService;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/cms/deptArticle")
public class CmsDeptArticleController extends BaseController
{
    @Autowired
    private ICmsArticleService cmsArticleService;

    @Autowired
    private ICmsDeptArticleService cmsDeptArticleService;

    @Autowired
    private ISysUserService sysUserService;


    @Autowired
    private ICmsCategoryService cmsCategoryService;

    /**
     * 加入科室资讯
     */
    @PreAuthorize("@ss.hasPermi('system:deptArticle:add')")
    @Log(title = "加入科室资讯", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CmsDeptArticle cmsDeptArticle){
        Long deptId = cmsDeptArticle.getDeptId();
        CmsDeptArticle cmsDeptArticleParam = new CmsDeptArticle();
        if (deptId == null){
            SysUser sysUser = sysUserService.selectUserById(getUserId());
            deptId = sysUser.getDeptId();
            cmsDeptArticleParam.setDeptId(sysUser.getDeptId());
            cmsDeptArticle.setDeptId(deptId);
        }
        cmsDeptArticleParam.setArticleId(cmsDeptArticle.getArticleId());
        List<CmsDeptArticle> cmsDeptArticles = cmsDeptArticleService.selectCmsDeptArticle(cmsDeptArticle);
        if (!CollectionUtils.isEmpty(cmsDeptArticles)){
            throw new RuntimeException("文章已加入当前科室,已加入的分类为:"+cmsDeptArticles.get(0).getCategoryName());
        }
        String userName = getUsername();
        cmsDeptArticle.setCreateBy(userName);
        cmsDeptArticle.setDeptId(deptId);
        cmsDeptArticle.setArticleId(cmsDeptArticle.getArticleId());
        cmsDeptArticle.setCategoryId(cmsDeptArticle.getCategoryId());
        cmsDeptArticle.setCreateTime(DateUtils.getNowDate());
        cmsDeptArticle.setUpdateTime(DateUtils.getNowDate());
        cmsDeptArticle.setUpdateBy(userName);
        return toAjax(cmsDeptArticleService.insertCmsDeptArticle(cmsDeptArticle));
    }



    /**
     * 移除科室资讯
     */
    @PreAuthorize("@ss.hasPermi('system:deptArticle:remove')")
    @Log(title = "移除科室资讯", businessType = BusinessType.DELETE)
	@DeleteMapping("/{articleId}")
    public AjaxResult remove(@PathVariable Long articleId)
    {
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        return toAjax(cmsDeptArticleService.deleteCmsDeptArticleById(sysUser.getDeptId(),articleId));
    }


    /**
     * 移除科室资讯
     */
    @PreAuthorize("@ss.hasPermi('system:deptArticle:remove')")
    @Log(title = "移除科室资讯", businessType = BusinessType.DELETE)
    @PostMapping("/removeArticle")
    public AjaxResult remove(@RequestBody CmsArticle cmsArticle)
    {
        Long deptId = cmsArticle.getDeptId();
        if (deptId == null){
            SysUser sysUser = sysUserService.selectUserById(getUserId());
            deptId = sysUser.getDeptId();
        }
        return toAjax(cmsDeptArticleService.deleteCmsDeptArticleById(deptId,cmsArticle.getArticleId()));
    }


    /**
     * 文件移动
     *
     * @param articleMoveParam
     * @return
     */
    @ResponseBody
    @PreAuthorize("@ss.hasPermi('system:deptArticle:fileMove')")
    @PostMapping(value = "/fileMove")
    public AjaxResult fileMove(@RequestBody CmsArticleMoveParam articleMoveParam) {
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        Long deptId = sysUser.getDeptId();
        if (deptId != 100){
            return AjaxResult.error("没有操作权限，只允许唐都医院账号操作");
        }
        Long categoryId = articleMoveParam.getCategoryId();
        Long newCategoryId = articleMoveParam.getNewCategoryId();
        if (Objects.isNull(categoryId) && Objects.isNull(newCategoryId) ){
            return AjaxResult.error("分类ID或新分类ID不能为空");
        }
        return AjaxResult.success(cmsArticleService.deptfileMove(articleMoveParam));

    }


    /**
     * 批量加入科室资讯
     */
    @PreAuthorize("@ss.hasPermi('system:deptArticle:batchAdd')")
    @Log(title = "批量加入科室资讯", businessType = BusinessType.INSERT)
    @PostMapping("batchAdd")
    public AjaxResult addByCategoryId(@RequestBody CmsDeptArticleParam cmsDeptArticleParam) {
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        Long deptId = sysUser.getDeptId();
        if (deptId != 100){
            return AjaxResult.error("没有操作权限，只允许唐都医院账号操作");
        }
        CmsCategory cmsCategory = cmsCategoryService.selectCmsCategoryById(cmsDeptArticleParam.getDeptCategoryId());
        CmsArticle cmsArticle = new CmsArticle();
        if (Objects.nonNull(cmsDeptArticleParam.getCommonCategoryId())) {
            Long commonCategoryId = cmsDeptArticleParam.getCommonCategoryId();
            List<CmsCategory> cmsCategoryList = cmsCategoryService.selectAllChildCategoryById(commonCategoryId);
            List<Long> categoryIdList = cmsCategoryList.stream().map(CmsCategory::getId).collect(Collectors.toList());
            cmsArticle.setCategoryIdList(categoryIdList);
        }else {
            cmsArticle.setArticleId(cmsDeptArticleParam.getArticleId());
        }
        List<CmsArticle> cmsArticles = cmsArticleService.selectCmsInformationList(cmsArticle);
        if (CollectionUtils.isEmpty(cmsArticles)){
            return AjaxResult.error("没有需要加入科室的文件");
        }
        return AjaxResult.success(cmsDeptArticleService.batchInsertCmsDeptArticle(cmsDeptArticleParam,cmsCategory,cmsArticles));
    }
}
