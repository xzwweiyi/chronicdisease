package com.dchealth.web.controller.cms;

import com.dchealth.cms.domain.CmsArticle;
import com.dchealth.cms.domain.CmsArticleData;
import com.dchealth.cms.domain.CmsCategory;
import com.dchealth.cms.service.ICmsArticleDataService;
import com.dchealth.cms.service.ICmsArticleService;
import com.dchealth.cms.service.ICmsCategoryService;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.research.service.IResearchInfoArticleService;
import com.dchealth.system.service.ISysUserService;
import com.dchealth.template.domain.FMedicalExamForm;
import com.dchealth.template.domain.vo.FMedicalInfoReq;
import com.dchealth.template.service.IFMedicalExamFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * APP资讯管理Controller
 *
 * @author ruoyi
 * @date 2022-03-16
 */
@RestController
@RequestMapping("/cms/default/appArticle")
public class CmsDefaultArticleAppRotationController extends BaseController {
    @Autowired
    private ICmsArticleService cmsArticleService;

    @Autowired
    private ICmsCategoryService cmsCategoryService;

    @Autowired
    private ICmsArticleDataService cmsArticleDataService;


    @Autowired
    private IFMedicalExamFormService fMedicalExamFormService;


    @Autowired
    private ISysUserService sysUserService;





    /**
     * 查询资讯列表
     */
    @PreAuthorize("@ss.hasPermi('systemDefault:information:list')")
    @GetMapping("/informationList")
    public TableDataInfo informationList(CmsArticle cmsArticleParam) {
        startPage();
        List<CmsArticle> list = cmsArticleService.selectCmsInformationList(cmsArticleParam);
        TableDataInfo tableDataInfo = getDataTable(list);
        List<CmsArticle> result = new ArrayList<CmsArticle>();
        @SuppressWarnings("unchecked")
        List<CmsArticle> cmsArticleList = (List<CmsArticle>) tableDataInfo.getRows();
        if (cmsArticleList != null && cmsArticleList.size() > 0) {
            for (CmsArticle cmsArticle : list) {
                CmsArticleData cmsArticleData = cmsArticleDataService.selectCmsArticleDataById(cmsArticle.getId());
                if (cmsArticleData != null) {
                    cmsArticle.setCopyFrom(cmsArticleData.getCopyFrom());
                    cmsArticle.setContent(cmsArticleData.getContent());
                    cmsArticle.setRelation(cmsArticleData.getRelation());
                    cmsArticle.setAllowComment(cmsArticleData.getAllowComment());
                }
                if (cmsArticle.getCategoryId() != null) {
                    CmsCategory cmsCategory = cmsCategoryService.selectCmsCategoryById(cmsArticle.getCategoryId());
                    if (cmsCategory != null) {
                        cmsArticle.setCategoryName(cmsCategory.getName());
                    }
                }
                cmsArticle.setUpdateTime(cmsArticle.getUpdateTime());
                result.add(cmsArticle);
            }
        }
        tableDataInfo.setRows(result);
        return tableDataInfo;
    }


    /**
     * 获取资讯管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('systemDefault:AppArticle:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        //资讯详情
        CmsArticle cmsArticle = cmsArticleService.selectCmsArticleById(id);
        String type = cmsArticle.getType();
        if ("1".equals(type)) {
            //文章信息
            CmsArticleData cmsArticleData = cmsArticleDataService.selectCmsArticleDataById(cmsArticle.getId());
            cmsArticle.setCopyFrom(cmsArticleData.getCopyFrom());
            cmsArticle.setContent(cmsArticleData.getContent());
            cmsArticle.setRelation(cmsArticleData.getRelation());
            cmsArticle.setAllowComment(cmsArticleData.getAllowComment());
        } else if ("2".equals(type)) {
            //问卷信息
            FMedicalInfoReq fMedicalInfoReq = new FMedicalInfoReq();
            fMedicalInfoReq.setArticleId(id);
            FMedicalExamForm fMedicalFormExamInfo = fMedicalExamFormService.getFMedicalFormExamInfo(fMedicalInfoReq);
            if (fMedicalFormExamInfo != null) {
                cmsArticle.setFmedicalExamForm(fMedicalFormExamInfo);
            }
        }
        return AjaxResult.success(cmsArticle);
    }

    /**
     * 新增资讯管理
     */
    @PreAuthorize("@ss.hasPermi('systemDefault:AppArticle:add')")
    @Log(title = "资讯管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CmsArticle cmsArticle) {
        String userName = getUsername();
        cmsArticle.setCreateBy(userName);
        cmsArticle.setCreateId(getUserId());
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        cmsArticle.setDeptId(sysUser.getDeptId());
        return toAjax(cmsArticleService.insertCmsArticle(cmsArticle));
    }

    /**
     * 修改资讯管理
     */
    @PreAuthorize("@ss.hasPermi('systemDefault:AppArticle:edit')")
    @Log(title = "资讯管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CmsArticle cmsArticle) {
        cmsArticle.setUpdateBy(getUsername());
        return toAjax(cmsArticleService.updateCmsArticle(cmsArticle));
    }

    /**
     * 删除资讯管理
     */
    @PreAuthorize("@ss.hasPermi('systemDefault:AppArticle:remove')")
    @Log(title = "资讯管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(cmsArticleService.deleteCmsArticleByIds(ids));
    }

}
