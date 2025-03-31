package com.dchealth.web.controller.cms;

import com.dchealth.cms.domain.CmsArticle;
import com.dchealth.cms.domain.CmsArticleData;
import com.dchealth.cms.domain.CmsArticleRecord;
import com.dchealth.cms.domain.CmsCategory;
import com.dchealth.cms.domain.CmsDeptArticle;
import com.dchealth.cms.domain.FMedicalExamRes;
import com.dchealth.cms.domain.vo.CmsArticleCateGoryVo;
import com.dchealth.cms.domain.vo.CmsArticleVo;
import com.dchealth.cms.mapper.CmsDeptArticleMapper;
import com.dchealth.cms.service.ICmsArticleDataService;
import com.dchealth.cms.service.ICmsArticleService;
import com.dchealth.cms.service.ICmsCategoryService;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.research.domain.ResearchInfoArticle;
import com.dchealth.research.service.IResearchInfoArticleService;
import com.dchealth.system.service.ISysRoleDeptService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 资讯管理Controller
 *
 * @author ruoyi
 * @date 2022-03-16
 */
@RestController
@RequestMapping("/cms/dept/article")
public class CmsArticleDeptController extends BaseController {
    @Autowired
    private ICmsArticleService cmsArticleService;

    @Autowired
    private ICmsCategoryService cmsCategoryService;

    @Autowired
    private ICmsArticleDataService cmsArticleDataService;

    @Autowired
    private IResearchInfoArticleService researchInfoArticleService;

    @Autowired
    private IFMedicalExamFormService fMedicalExamFormService;

    @Autowired
    private ISysRoleDeptService sysRoleDeptService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private CmsDeptArticleMapper cmsDeptArticleMapper;


    /**
     * 查询资讯及子文件列表
     */
    @PreAuthorize("@ss.hasPermi('systemDept:article:list')")
    @GetMapping("/list")
    public TableDataInfo list(CmsArticle cmsArticleParam) {
        startPage();
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        cmsArticleParam.setDeptId(sysUser.getDeptId());
        List<CmsArticle> list;
        Integer commonFlag = cmsArticleParam.getCommonFlag() == null ? 1 : cmsArticleParam.getCommonFlag();
        if (commonFlag == 1) {
            list = cmsArticleService.selectCmsArticleList(cmsArticleParam);
        } else {
            list = cmsArticleService.selectCmsDeptArticleList(cmsArticleParam);
        }
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
                result.add(cmsArticle);
            }
        }
        tableDataInfo.setRows(result);
        return tableDataInfo;
    }


    /**
     * 查询科室分类资讯列表
     */
    @PreAuthorize("@ss.hasPermi('systemDept:deptArticle:list')")
    @GetMapping("/deptArticleList")
    public TableDataInfo deptCategoryArticleList(CmsArticle cmsArticleParam) {
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        cmsArticleParam.setDeptId(sysUser.getDeptId());
        List<CmsArticleCateGoryVo> list = cmsArticleService.selectCmsDeptCategoryArticleList(cmsArticleParam);
        TableDataInfo tableDataInfo = getDataTable(list);
        return tableDataInfo;
    }

    /**
     * 导出资讯管理列表
     */
    @PreAuthorize("@ss.hasPermi('systemDept:article:export')")
    @Log(title = "资讯管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CmsArticle cmsArticle) {
        List<CmsArticle> list = cmsArticleService.selectCmsArticleList(cmsArticle);
        ExcelUtil<CmsArticle> util = new ExcelUtil<CmsArticle>(CmsArticle.class);
        util.exportExcel(response, list, "资讯管理数据");
    }

    /**
     * 获取资讯管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('systemDept:article:query')")
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
    @PreAuthorize("@ss.hasPermi('systemDept:article:add')")
    @Log(title = "资讯管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CmsArticle cmsArticle) {
        return AjaxResult.error("科室不允许新增资讯");
    }

    /**
     * 修改资讯管理
     */
    @PreAuthorize("@ss.hasPermi('systemDept:article:edit')")
    @Log(title = "资讯管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CmsArticle cmsArticle) {
        cmsArticle.setUpdateBy(getUsername());
        return AjaxResult.error("科室不允许编辑资讯");
    }

    /**
     * 删除资讯管理
     */
    @PreAuthorize("@ss.hasPermi('systemDept:article:remove')")
    @Log(title = "资讯管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return AjaxResult.error("科室不允许删除资讯");
    }

    /**
     * 队列慢病选择患教列表
     *
     * @param vo
     * @return
     */
    @GetMapping("/listEdu")
    public AjaxResult listEdu(CmsArticle vo) {
        vo.setPositionType("2");
        List<CmsArticleVo> articleVo = new ArrayList<>();
        Map<Long, List<CmsArticle>> map = new HashMap<>();
//        if(getUserId()>1){
//            vo.setCreateId(getUserId());
//            String deptIds = sysRoleDeptService.getDeptIdsByUserId(getUserId());
//            vo.setDeptIds(deptIds);
//        }
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        vo.setDeptId(sysUser.getDeptId());
        List<CmsArticle> list = cmsArticleService.selectCmsArticleList(vo);
        if (list != null && list.size() > 0) {
            for (CmsArticle cmsArticle : list) {
                //资讯文章内容
                CmsArticleData cmsArticleData = cmsArticleDataService.selectCmsArticleDataById(cmsArticle.getId());
                if (cmsArticleData != null) {
                    cmsArticle.setCopyFrom(cmsArticleData.getCopyFrom());
                    cmsArticle.setContent(cmsArticleData.getContent());
                    cmsArticle.setRelation(cmsArticleData.getRelation());
                    cmsArticle.setAllowComment(cmsArticleData.getAllowComment());
                }
                //资讯分类
                if (cmsArticle.getCategoryId() != null) {
                    CmsCategory cmsCategory = cmsCategoryService.selectCmsCategoryById(cmsArticle.getCategoryId());
                    if (cmsCategory != null) {
                        cmsArticle.setCategoryName(cmsCategory.getName());
                    }
                }
            }

            //获取患教对应分类id
            List<Long> categoryIds = list.stream().filter(art -> art.getCategoryId() != null).map(art -> art.getCategoryId()).distinct().collect(Collectors.toList());
            //获取分类底下的患教
            map = list.stream().filter(art -> art.getCategoryId() != null).collect(Collectors.groupingBy(CmsArticle::getCategoryId));
            if (categoryIds != null && categoryIds.size() > 0 && map != null) {
                for (Long categoryId : categoryIds) {
                    CmsArticleVo cmsArticleVo = new CmsArticleVo();
                    List<CmsArticle> cmsArticles = map.get(categoryId);
                    cmsArticleVo.setCategoryName(cmsArticles.get(0).getCategoryName());
                    cmsArticleVo.setArticleList(cmsArticles);
                    //回显内容需要队列id
                    if (vo.getResearchId() != null) {
                        ResearchInfoArticle researchInfoArticle = new ResearchInfoArticle();
                        researchInfoArticle.setResearchId(vo.getResearchId());
                        researchInfoArticle.setCategoryId(categoryId);
                        //获取病种已选择的患教数量
                        List<ResearchInfoArticle> articleList = researchInfoArticleService.selectResearchInfoArticleList(researchInfoArticle);
                        cmsArticleVo.setArticleNum(articleList != null ? articleList.size() : 0);
                    } else {
                        cmsArticleVo.setArticleNum(0);
                    }
                    articleVo.add(cmsArticleVo);
                }
            }

        }
        return AjaxResult.success(articleVo);
    }



    /**
     * 逻辑删除
     */
    @PreAuthorize("@ss.hasPermi('systemDept:article:remove')")
    @Log(title = "逻辑删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/edu/{id}")
    public AjaxResult updateDelById(@PathVariable Long id) {
//        CmsDeptArticle cmsDeptArticle = cmsDeptArticleMapper.selectCmsDeptArticle(id);
//        if (cmsDeptArticle != null){
//            return AjaxResult.error("文章已经加入科室库,请先删除科室库内容");
//        }

        return AjaxResult.error("科室不允许删除资讯");
    }

    /**
     * 获取资讯管理详细信息
     */
    @GetMapping(value = "/eduInfo/{id}")
    public AjaxResult getInfoEdu(@PathVariable("id") Long id) {
        //资讯详情
        CmsArticle cmsArticle = cmsArticleService.selectCmsArticleById(id);
        if (cmsArticle != null) {
            //文章信息
            CmsArticleData cmsArticleData = cmsArticleDataService.selectCmsArticleDataById(cmsArticle.getId());
            if (cmsArticleData != null) {
                cmsArticle.setCopyFrom(cmsArticleData.getCopyFrom());
                cmsArticle.setContent(cmsArticleData.getContent());
                cmsArticle.setRelation(cmsArticleData.getRelation());
                cmsArticle.setAllowComment(cmsArticleData.getAllowComment());
            }
        }
        return AjaxResult.success(cmsArticle);
    }


    /**
     * 问卷发布
     *
     * @param id
     * @return
     */
    @Log(title = "问卷发布", businessType = BusinessType.UPDATE)
    @GetMapping("/questionnaire/{id}")
    public AjaxResult releaseScale(@PathVariable Long id) {
        return toAjax(cmsArticleService.updateCmsArticleStatusById(id));
    }


    /**
     * 患教问卷记录
     */
    @PreAuthorize("@ss.hasPermi('systemDept:article:record')")
    @GetMapping("/record")
    public TableDataInfo record(CmsArticleRecord cmsArticleRecord) {
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        startPage();
        cmsArticleRecord.setUserId(sysUser.getUserId());
        cmsArticleRecord.setUserName(sysUser.getUserName());
        List<CmsArticleRecord> cmsArticleRecordList = cmsArticleService.selectCmsArticleRecord(cmsArticleRecord);
        TableDataInfo tableDataInfo = getDataTable(cmsArticleRecordList);
        return tableDataInfo;
    }


    /**
     * 问卷详情
     *
     * @param fMedicalExamRes
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/examFromInfo")
    public AjaxResult examFromDetailInfo(FMedicalExamRes fMedicalExamRes) {
        return AjaxResult.success(cmsArticleService.selectExamFromDetailInfo(fMedicalExamRes));
    }
}
