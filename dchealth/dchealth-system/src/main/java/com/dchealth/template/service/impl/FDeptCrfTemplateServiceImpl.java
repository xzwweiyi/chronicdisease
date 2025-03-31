package com.dchealth.template.service.impl;

import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.system.service.ISysUserService;
import com.dchealth.template.domain.FDeptCrfTemplate;
import com.dchealth.template.domain.FTemplate;
import com.dchealth.template.domain.FTemplateForm;
import com.dchealth.template.domain.FTemplateFormSetEvaluation;
import com.dchealth.template.mapper.FDeptCrfTemplateMapper;
import com.dchealth.template.mapper.FTemplateFormSetEvaluationMapper;
import com.dchealth.template.service.IFDeptCrfTemplateService;
import com.dchealth.template.service.IFTemplateFormService;
import com.dchealth.template.service.IFTemplatePropertyService;
import com.dchealth.template.service.IFTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.dchealth.common.utils.SecurityUtils.getUserId;

@Service
public class FDeptCrfTemplateServiceImpl implements IFDeptCrfTemplateService {

    @Autowired
    private FDeptCrfTemplateMapper fDeptCrfTemplateMapper;

    @Autowired
    private ISysUserService sysUserService;


    @Autowired
    private IFTemplateService fTemplateService;

    @Autowired
    private IFTemplateFormService fTemplateFormService;
    @Autowired
    private IFTemplatePropertyService fTemplatePropertyService;

    @Autowired
    private FTemplateFormSetEvaluationMapper fTemplateFormSetEvaluationMapper;


    @Override
    @Transactional
    public int insertDeptCrfTemplate(FDeptCrfTemplate fDeptCrfTemplate) {
        SysUser sysUser = sysUserService.selectUserById(getUserId());
        Long deptId = fDeptCrfTemplate.getDeptId();
        FDeptCrfTemplate fDeptCrfTemplateParam = new FDeptCrfTemplate();
        if (deptId == null){
            deptId = sysUser.getDeptId();
            fDeptCrfTemplateParam.setDeptId(deptId);
        }
        fDeptCrfTemplateParam.setTemplateId(fDeptCrfTemplate.getTemplateId());
        List<FDeptCrfTemplate> fDeptCrfTemplates = fDeptCrfTemplateMapper.selectDeptCrfTemplateByTemplateList(fDeptCrfTemplateParam);
        if (!CollectionUtils.isEmpty(fDeptCrfTemplates)) {
            throw new RuntimeException("模板已导入当前科室,导入的分类为："+fDeptCrfTemplates.get(0).getTemplateCategoryName());
        }
        FTemplate fTemplate = fTemplateService.selectFTemplateByTemplateId(fDeptCrfTemplate.getTemplateId());
        fTemplate.setCreateBy(sysUser.getUserName());
        fTemplate.setUpdateBy(sysUser.getUserName());
        fTemplate.setCreateTime(DateUtils.getNowDate());
        fTemplate.setCreateId(sysUser.getUserId());
        fTemplate.setDeptId(deptId);
        fTemplate.setTemplateCategoryId(fDeptCrfTemplate.getTemplateCategoryId());
        fTemplateService.insertFTemplate(fTemplate);
        Long deptTemplateId = fTemplate.getTemplateId();
        FTemplateForm fTemplateForm = new FTemplateForm();
        fTemplateForm.setParentId(0l);
        fTemplateForm.setTemplateId(fDeptCrfTemplate.getTemplateId());
        List<FTemplateForm> fTemplateForms = fTemplateFormService.selectFTemplateFormList(fTemplateForm);
        for (FTemplateForm templateForm : fTemplateForms) {
            FTemplateForm templateFormDetail = fTemplateFormService.detail(templateForm.getTemplateFormId());
            templateFormDetail.setTemplateId(deptTemplateId);
            templateFormDetail.setCreateBy(sysUser.getUserName());
            templateFormDetail.setUpdateBy(sysUser.getUserName());
            templateFormDetail.setCreateTime(DateUtils.getNowDate());
            // 2拿到当前模块信息
            fTemplateFormService.insertFTemplateForm(templateFormDetail);
            List<FTemplateForm> templateForms = templateFormDetail.getTemplateForms();
            for (FTemplateForm fTemplateFormVo : templateForms) {
                fTemplateFormVo.setTemplateFormId(null);
                fTemplateFormVo.setTemplateId(templateFormDetail.getTemplateId());
            }
            // 3对应条目
            fTemplatePropertyService.updateFTemplateProperty(templateFormDetail);
            //4设置测评结果
            if (templateFormDetail.getTemplateCode().equals("modelb")) {
                FTemplateFormSetEvaluation fTemplateFormSetEvaluation = new FTemplateFormSetEvaluation();
                fTemplateFormSetEvaluation.setTemplateFormId(templateForm.getTemplateFormId());
                List<FTemplateFormSetEvaluation> fTemplateFormSetEvaluations = fTemplateFormSetEvaluationMapper.selectFTemplateFormSetEvaluationList(fTemplateFormSetEvaluation);
                for (FTemplateFormSetEvaluation fTemplateFormSetEvaluationVo : fTemplateFormSetEvaluations) {
                    fTemplateFormSetEvaluationVo.setTemplateFormId(templateFormDetail.getTemplateFormId());
                    fTemplateFormSetEvaluationVo.setCreateBy(sysUser.getUserName());
                    fTemplateFormSetEvaluationVo.setCreateTime(DateUtils.getNowDate());
                    fTemplateFormSetEvaluationMapper.insertFTemplateFormSetEvaluation(fTemplateFormSetEvaluationVo);
                }
            }
        }
        fDeptCrfTemplate.setCreateBy(sysUser.getUserName());
        fDeptCrfTemplate.setUpdateBy(sysUser.getUserName());
        fDeptCrfTemplate.setCreateTime(DateUtils.getNowDate());
        fDeptCrfTemplate.setDeptId(deptId);
        fDeptCrfTemplate.setDeptTemplateId(deptTemplateId);
        return fDeptCrfTemplateMapper.insertDeptCrfTemplate(fDeptCrfTemplate);
    }
}
