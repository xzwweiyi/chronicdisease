package com.dchealth.template.service.impl;

import com.dchealth.common.utils.DateUtils;
import com.dchealth.template.domain.*;
import com.dchealth.template.domain.vo.FTemplateVo;
import com.dchealth.template.mapper.*;
import com.dchealth.template.service.IFTemplateFormService;
import com.dchealth.template.service.IFTemplateService;
import com.dchealth.warn.domain.form.WarnMedicalEarlyForm;
import com.dchealth.warn.domain.vo.WarnTemplateEarlyVO;
import com.dchealth.warn.service.IWarnService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 模板Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
@Service
public class FTemplateServiceImpl implements IFTemplateService 
{
    @Autowired
    private FTemplateMapper fTemplateMapper;
    @Autowired
    private FTemplateFormMapper fTemplateFormMapper;
    @Autowired
    private FTemplatePropertyMapper fTemplatePropertyMapper;
    @Autowired
    private FMedicalFormMapper fMedicalFormMapper;
    @Autowired
    private FMedicalPropertyMapper fMedicalPropertyMapper;
    /*@Autowired
    private WarnConfigMapper warnConfigMapper;*/
    @Autowired
    private IWarnService warnService;
//    private FTemplateWarnConfigMapper fTemplateWarnConfigMapper;
    @Autowired
    private FTemplateExamSubjectMapper fTemplateExamSubjectMapper;
    @Autowired
    private FMedicalExamItemMapper fMedicalExamItemMapper;
    @Autowired
    private FMedicalExamSubjectMapper fMedicalExamSubjectMapper;
    @Autowired
    private FTemplateExamItemMapper fTemplateExamItemMapper;
    @Autowired
    private FTemplateFormSetEvaluationMapper fTemplateFormSetEvaluationMapper;
    @Autowired
    private FMedicalExamSetEvaluationMapper fMedicalExamSetEvaluationMapper;

    @Autowired
    private FDeptCrfTemplateMapper fDeptCrfTemplateMapper;
    @Autowired
    private IFTemplateFormService fTemplateFormService;


    /**
     * 查询模板
     * 
     * @param templateId 模板主键
     * @return 模板
     */
    @Override
    public FTemplate selectFTemplateByTemplateId(Long templateId)
    {
        return fTemplateMapper.selectFTemplateByTemplateId(templateId);
    }

    @Override
    public FTemplate detail(Long templateId) {
        FTemplate fTemplate = new FTemplate();
        FTemplateForm params = new FTemplateForm();
        params.setTemplateId(templateId);
        List<FTemplateForm> fTemplateForms = fTemplateFormMapper.selectFTemplateFormList(params);
        FTemplateProperty propertyParams = new FTemplateProperty();
        propertyParams.setTemplateId(templateId);
        List<FTemplateProperty> fTemplateProperties = fTemplatePropertyMapper.selectFTemplatePropertyList(propertyParams);
        List<WarnTemplateEarlyVO> warnTemplateEarlyVOS = warnService.selectWarnTemplateEarlys(templateId);
        Map<Long, List<WarnTemplateEarlyVO>> teMap = Optional.ofNullable(warnTemplateEarlyVOS).map(m->m.stream().collect(Collectors.groupingBy(WarnTemplateEarlyVO :: getPropertyId))).orElse(null);
        if (fTemplateForms != null && !fTemplateForms.isEmpty() && fTemplateProperties != null && !fTemplateProperties.isEmpty()){
            Map<Long,List<FTemplateProperty>> map = fTemplateProperties.stream().collect(Collectors.groupingBy(FTemplateProperty :: getTemplateFormId));
            fTemplateForms.forEach(x->{
                if (map.containsKey(x.getTemplateFormId()) && !"2".equals(x.getFormType())){
                    x.setTemplateProperties(map.get(x.getTemplateFormId()).stream().map(m->{
                        if (teMap != null && !teMap.isEmpty()
                                && teMap.containsKey(m.getId())){
                            m.setWarnTemplateEarlyVOS(teMap.get(m.getId()));
                        }
                        return m;
                    }).collect(Collectors.toList()));
                }
            });
        }
        if (fTemplateForms != null && !fTemplateForms.isEmpty()){
            fTemplate.setTemplateForms(templateForm(0l,fTemplateForms));
        }
        fTemplate.setTemplateId(templateId);
        return fTemplate;
    }

    private List<FTemplateForm> templateForm(Long parentId, List<FTemplateForm> fTemplateForms){
        List<FTemplateForm> list = new ArrayList<>();
        if (fTemplateForms != null && !fTemplateForms.isEmpty()){
            fTemplateForms.forEach(x->{
                if (x.getParentId().equals(parentId)){
                    List<FTemplateForm> childrends = templateForm(x.getTemplateFormId(),fTemplateForms);
                    if (!childrends.isEmpty()){
                        x.setTemplateForms(childrends);
                    }
                    list.add(x);
                }
            });
        }
        return list;
    }

    /**
     * 查询模板列表
     * 
     * @param fTemplate 模板
     * @return 模板
     */
    @Override
    public List<FTemplate> selectFTemplateList(FTemplate fTemplate){
        List<FTemplate> resultList = fTemplateMapper.selectFTemplateList(fTemplate);
        for(FTemplate p:resultList){
            FTemplateForm fTemplateForm = new FTemplateForm();
            fTemplateForm.setTemplateId(p.getTemplateId());
            fTemplateForm.setParentId(0l);
            List<FTemplateForm> list = fTemplateFormMapper.selectFTemplateFormList(fTemplateForm);

            FTemplateProperty fTemplateProperty = new FTemplateProperty();
            fTemplateProperty.setTemplateId(p.getTemplateId());
            List<FTemplateProperty> fTemplatePropertyList = fTemplatePropertyMapper.selectPropertyNumsList(fTemplateProperty);
            Map<Long,List<FTemplateProperty>> propertyMaps = new HashMap<>();
            if(fTemplatePropertyList.size()>0 && list.size()>0){
                propertyMaps = fTemplatePropertyList.stream().collect(Collectors.groupingBy(FTemplateProperty::getParentId));
            }
            int num=0;
            for(FTemplateForm f:list){
                if(propertyMaps.containsKey(f.getTemplateFormId())){
                    num = num + f.getPropertyNum()+propertyMaps.get(f.getTemplateFormId()).size()+f.getEdcNum();
                }else{
                    num = num + f.getPropertyNum() + f.getEdcNum();
                }
                if(f.getFormType().equals("3")){
                    FTemplateExamSubject fTemplateExamSubject = new FTemplateExamSubject();
                    fTemplateExamSubject.setTemplateFormId(f.getTemplateFormId());
                    List<FTemplateExamSubject> examSubjectList = fTemplateExamSubjectMapper.selectFTemplateExamSubjectList(fTemplateExamSubject);
                    num = num + examSubjectList.size();
                }
            }
            p.setPropertyNum(num);
        }
        return resultList;
    }

    /**
     * 新增模板
     * 
     * @param fTemplate 模板
     * @return 结果
     */
    @Override
    @Transactional
    public Long insertFTemplate(FTemplate fTemplate)
    {
        fTemplate.setCreateTime(DateUtils.getNowDate());
        fTemplateMapper.insertFTemplate(fTemplate);
        return fTemplate.getTemplateId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importOrUpdate(Long templateId, Long followId) {
        fTemplateFormMapper.deleteFTemplateFormByTemplateId(templateId);
        fTemplatePropertyMapper.deleteFTemplatePropertyByTemplateId(templateId);
        warnService.deleteWarnTemplate(templateId);
        List<FMedicalForm> fMedicalForms = fMedicalFormMapper.selectFMedicalFormByFollowId(followId);
        List<FMedicalProperty> fMedicalProperties = fMedicalPropertyMapper.selectFMedicalPropertyByFollowId(followId);
        List<WarnMedicalEarlyForm> warnMedicalEarlyForms = warnService.selectWarnMedicalEarlysForm(followId);
        if (fMedicalForms != null && !fMedicalForms.isEmpty()){
            Map<Long,List<FMedicalProperty>> map = null;
            Map<Long, List<FMedicalExamItem>> itemMap = null;
            if (fMedicalProperties != null && !fMedicalProperties.isEmpty()){
                Map<Long, List<WarnMedicalEarlyForm>> wLongListMap = Optional.ofNullable(warnMedicalEarlyForms)
                        .map(n->n.stream().collect(Collectors.groupingBy(WarnMedicalEarlyForm :: getPropertyId))).orElse(null);
                map = fMedicalProperties.stream().map(m->{
                    if (wLongListMap != null && !wLongListMap.isEmpty() 
                            && wLongListMap.containsKey(m.getId())){
                        m.setWarnMedicalEarlyForms(wLongListMap.get(m.getId()));
                    }
                    return m;
                }).collect(Collectors.groupingBy(FMedicalProperty :: getMedicalFormId));
            }
            for (FMedicalForm x : fMedicalForms) {
                if (map != null && map.containsKey(x.getMedicalFormId())){
                    x.setMedicalProperties(map.get(x.getMedicalFormId()));
                }
                //处理自评问卷
                if("3".equals(x.getFormType())){
                    List<FMedicalExamItem> itemList = fMedicalExamItemMapper.selectFMedicalExamItemByFromId(x.getMedicalFormId());
                    if(itemList != null && !itemList.isEmpty()){
                        itemMap = itemList.stream().collect(Collectors.groupingBy(FMedicalExamItem::getPropertyId));
                    }
                    packageFMedicalProperty(x, itemMap);
                }
            }
        }
        List<FMedicalForm> forms = recurrenceTreeData(0l,fMedicalForms);
        recurrenceSave(templateId, 0l, forms);
        return 1;
    }

    /**
     * 封装自评量表题干、选项信息
     * @param fMedicalForm
     * @param itemMap
     */
    public void packageFMedicalProperty(FMedicalForm fMedicalForm, Map<Long, List<FMedicalExamItem>> itemMap){
        List<FMedicalExamSubject> subjectList = fMedicalExamSubjectMapper.selectFMedicalExamSubjectByFromId(fMedicalForm.getMedicalFormId());
        if(subjectList != null && !subjectList.isEmpty()){
            List<FMedicalProperty> fMedicalProperties = new ArrayList<>();
            for (FMedicalExamSubject subject : subjectList) {
                FMedicalProperty property = new FMedicalProperty();
                BeanUtils.copyProperties(subject, property);
                property.setFollowId(fMedicalForm.getFollowId());
                property.setArticleId(fMedicalForm.getArticleId());
                if(itemMap != null && itemMap.containsKey(subject.getId())){
                    List<FMedicalExamItem> items = itemMap.get(subject.getId());
                    property.setItemList(items);
                }
                fMedicalProperties.add(property);
            }
            fMedicalForm.setMedicalProperties(fMedicalProperties);
        }
    }

    /**
     * 保存预警
     * @param templateId
     * @param warnConfigs
     * @return
     */
    /*private int saveTemplateWarnConfig(Long templateId,Long templateFormId, List<WarnConfig> warnConfigs){
        if (warnConfigs == null || warnConfigs.isEmpty()){
            return 0;
        }

        List<FTemplateWarnConfig> templateWarnConfigs = new ArrayList<>();
        warnConfigs.forEach(x->{
            FTemplateWarnConfig fTemplateWarnConfig = new FTemplateWarnConfig();
            fTemplateWarnConfig.setTemplateId(templateId);
            fTemplateWarnConfig.setTemplateFormId(templateFormId);
            fTemplateWarnConfig.setResearchId(x.getResearchId());
            fTemplateWarnConfig.setStandardValue(x.getStandardValue());
            fTemplateWarnConfig.setTemplatePropertyCode(x.getMedicalPropertyCode());
            fTemplateWarnConfig.setWarnFormula(x.getWarnFormula());
            fTemplateWarnConfig.setWarnFormulaTitle(x.getWarnFormulaTitle());
            fTemplateWarnConfig.setUpdateTime(x.getUpdateTime());
            fTemplateWarnConfig.setUpdateBy(x.getUpdateBy());
            templateWarnConfigs.add(fTemplateWarnConfig);
        });

        fTemplateWarnConfigMapper.batchInsertFTemplateWarnConfig(templateWarnConfigs);
        return 1;
    }*/

    /**
     * 递归保存
     * @param templateId
     * @param forms
     */
    private void recurrenceSave(Long templateId,Long parentId, List<FMedicalForm> forms){
        if (forms == null){
            return;
        }
        Date date = DateUtils.getNowDate();
        for (FMedicalForm form : forms) {
            long medicalFormId = form.getMedicalFormId();
            FTemplateForm templateForm = new FTemplateForm();
            templateForm.setParentId(parentId);
            templateForm.setTemplateId(templateId);
            templateForm.setCreateTime(form.getCreateTime());
            templateForm.setFormType(form.getFormType());
            templateForm.setOpenImage(form.getOpenImage());
            templateForm.setOpenMultiple(form.getOpenMultiple());
            templateForm.setOpenProperty(form.getOpenProperty());
            templateForm.setTemplateCode(form.getMedicalCode());
            templateForm.setTemplateName(form.getMedicalName());
            templateForm.setAttribute(form.getAttribute());
            templateForm.setHealthManageType(form.getHealthManageType());
            templateForm.setUpdateBy(form.getUpdateBy());
            templateForm.setUpdateTime(form.getUpdateTime());
            templateForm.setDynamic(form.getDynamic());
            fTemplateFormMapper.insertFTemplateForm(templateForm);
            if (form.getMedicalFormList() != null && !form.getMedicalFormList().isEmpty()){
                recurrenceSave(templateId, templateForm.getTemplateFormId(), form.getMedicalFormList());
            }
            if (form.getMedicalProperties() != null && !form.getMedicalProperties().isEmpty()){
                if("3".equals(form.getFormType())){
                    form.getMedicalProperties().forEach(y-> {
                        //自评量表处理
                        FTemplateExamSubject subject = new FTemplateExamSubject();
                        BeanUtils.copyProperties(y, subject);
                        subject.setCreateTime(date);
                        subject.setTemplateFormId(templateForm.getTemplateFormId());
                        subject.setTemplateId(templateForm.getTemplateId());
                        fTemplateExamSubjectMapper.insertFTemplateExamSubject(subject);
                        Optional<List<FMedicalExamItem>> itemList = Optional.ofNullable(y.getItemList());
                        if (itemList.isPresent()) {
                            itemList.get().forEach(item -> {
                                FTemplateExamItem tItem = new FTemplateExamItem();
                                BeanUtils.copyProperties(item, tItem);
                                tItem.setPropertyId(subject.getId());
                                tItem.setTemplateFormId(subject.getTemplateFormId());
                                tItem.setCreateTime(date);
                                fTemplateExamItemMapper.insertFTemplateExamItem(tItem);
                            });
                        }
                    });
                    //同步测评结果
                    saveTemplateFormSetEvaluation(medicalFormId,templateForm);
                }else{
                    for (FMedicalProperty medicalProperty : form.getMedicalProperties()){
                        FTemplateProperty property = new FTemplateProperty();
                        property.setTemplateId(templateId);
                        property.setTemplateFormId(templateForm.getTemplateFormId());
                        property.setAttribute(medicalProperty.getAttribute());
                        property.setOpenProperty(medicalProperty.getOpenProperty());
                        property.setPropertyCode(medicalProperty.getPropertyCode());
                        property.setPropertyName(medicalProperty.getPropertyName());
                        property.setSortNum(medicalProperty.getSortNum());
                        property.setPropertyType(medicalProperty.getPropertyType());
                        property.setUpdateTime(medicalProperty.getUpdateTime());
                        property.setUpdateBy(medicalProperty.getUpdateBy());
                        fTemplatePropertyMapper.insertFTemplateProperty(property);
                        if (medicalProperty.getWarnMedicalEarlyForms() != null && !medicalProperty.getWarnMedicalEarlyForms().isEmpty()){
                            //导入预警
                            warnService.importWarnTemplate(templateId, templateForm.getTemplateFormId(), property.getId(), medicalProperty.getWarnMedicalEarlyForms());
                        }
                    }
                }
            }
        }
    }

    /**
     * 同步测评结果
     * @param medicalFormId
     * @param templateForm
     */
    private void saveTemplateFormSetEvaluation(long medicalFormId,FTemplateForm templateForm){
        FMedicalExamSetEvaluation fmese = new FMedicalExamSetEvaluation();
        fmese.setMedicalFormId(medicalFormId);
        List<FMedicalExamSetEvaluation> fmeses = fMedicalExamSetEvaluationMapper.selectFMedicalExamSetEvaluationList(fmese);
        for(FMedicalExamSetEvaluation fmese1:fmeses){
            FTemplateFormSetEvaluation fTemplateFormSetEvaluation = new FTemplateFormSetEvaluation();
            BeanUtils.copyProperties(fmese1,fTemplateFormSetEvaluation);
            fTemplateFormSetEvaluation.setTemplateFormId(templateForm.getTemplateFormId());
            fTemplateFormSetEvaluationMapper.insertFTemplateFormSetEvaluation(fTemplateFormSetEvaluation);
        }
    }

    /**
     * 数结构
     * @param id
     * @param fMedicalForms
     * @return
     */
    private List<FMedicalForm> recurrenceTreeData(long id, List<FMedicalForm> fMedicalForms){
        if (fMedicalForms == null || fMedicalForms.isEmpty()){
            return null;
        }
        List<FMedicalForm> result = new ArrayList<>();
        fMedicalForms.forEach(x->{
            if (id == x.getParentId()){
                result.add(x);
            }
        });
        for (FMedicalForm fMedicalForm : result) {
            List<FMedicalForm> childs = recurrenceTreeData(fMedicalForm.getMedicalFormId(), fMedicalForms);
            if (childs != null && !childs.isEmpty()){
                fMedicalForm.setMedicalFormList(childs);
            }
        }
        return result;
    }

    /**
     * 修改模板
     * 
     * @param fTemplate 模板
     * @return 结果
     */
    @Override
    public int updateFTemplate(FTemplate fTemplate)
    {
        fTemplate.setUpdateTime(DateUtils.getNowDate());
        return fTemplateMapper.updateFTemplate(fTemplate);
    }

    /**
     * 批量删除模板
     * 
     * @param templateIds 需要删除的模板主键
     * @return 结果
     */

    @Transactional
    @Override
    public int deleteFTemplateByTemplateIds(Long[] templateIds)
    {
        fTemplateMapper.updateDelFlagByTemplateIds(templateIds);
        FDeptCrfTemplate fDeptCrfTemplate = new FDeptCrfTemplate();
        fDeptCrfTemplate.setTemplateId(templateIds[0]);
        List<FDeptCrfTemplate> fDeptCrfTemplates = fDeptCrfTemplateMapper.selectDeptCrfTemplateByTemplateList(fDeptCrfTemplate);
        if (!CollectionUtils.isEmpty(fDeptCrfTemplates)){
            fDeptCrfTemplateMapper.deleteDeptCrfTemplateByTemplateId(null,templateIds);
        }
        return 1;
    }


    @Transactional
    @Override
    public int deleteDeptFTemplateByTemplateIds(Long[] templateIds)
    {
        fTemplateMapper.updateDelFlagByTemplateIds(templateIds);
        FDeptCrfTemplate fDeptCrfTemplate = new FDeptCrfTemplate();
        fDeptCrfTemplate.setDeptTemplateId(templateIds[0]);
        List<FDeptCrfTemplate> fDeptCrfTemplates = fDeptCrfTemplateMapper.selectDeptCrfTemplateByTemplateList(fDeptCrfTemplate);
        if (!CollectionUtils.isEmpty(fDeptCrfTemplates)){
            fDeptCrfTemplateMapper.deleteDeptCrfTemplateByTemplateId(templateIds,null);
        }
        return 1;
    }


    /**
     * 删除模板信息
     * 
     * @param templateId 模板主键
     * @return 结果
     */
    @Override
    public int deleteFTemplateByTemplateId(Long templateId)
    {
        return fTemplateMapper.deleteFTemplateByTemplateId(templateId);
    }

    /**
     * 添加模板信息、表单信息、表单属性信息
     * @param vo
     */
    @Override
    public void addTemplate(FTemplateVo vo) {
        //模板保存处理
        FTemplate fTemplate = new FTemplate();
        fTemplate.setTemplateName(vo.getTemplateName());
        fTemplate.setTemplateType(vo.getTemplateType());
        fTemplate.setIsDeleted(vo.getIsDeleted());
        fTemplate.setShareType(vo.getShareType());
        fTemplate.setCreateBy(vo.getCreateBy());
        fTemplate.setCreateTime(vo.getCreateTime());
        fTemplateMapper.insertFTemplate(fTemplate);
        //模板相关表单保存处理
        List<FTemplateForm> fTemplateForms = vo.getFTemplateForms();
        for (FTemplateForm fTemplateForm : fTemplateForms) {
            fTemplateForm.setTemplateId(fTemplate.getTemplateId());
            fTemplateForm.setCreateBy(vo.getCreateBy());
            fTemplateForm.setCreateTime(vo.getCreateTime());
            fTemplateFormMapper.insertFTemplateForm(fTemplateForm);
            //表单属性表相关保存处理
            List<FTemplateProperty> fTemplateProperties = fTemplateForm.getTemplateProperties();
            for (FTemplateProperty fTemplateProperty : fTemplateProperties) {
                fTemplateProperty.setTemplateFormId(fTemplateForm.getTemplateFormId());
                fTemplateProperty.setCreateBy(vo.getCreateBy());
                fTemplateProperty.setCreateTime(vo.getCreateTime());
                fTemplatePropertyMapper.insertFTemplateProperty(fTemplateProperty);
            }
        }

    }

    /**
     * 逻辑删除模板信息
     * @param templateIds
     * @return
     */
    @Override
    public int deleteFTemplateByTempIds(Long[] templateIds) {
        return fTemplateMapper.deleteFTemplateByTempIds(templateIds);
    }


    /**
     * 部门模板列表
     * @param fTemplate
     * @return
     */
    @Override
    public List<FTemplate> selectDeptFTemplateList(FTemplate fTemplate) {
        return fTemplateMapper.selectDeptFTemplateList(fTemplate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copy(Long templateId,Long templateCategoryId) {
        FTemplate fTemplate = fTemplateMapper.selectFTemplateByTemplateId(templateId);
        FTemplate copyFTemplate = new FTemplate();
        BeanUtils.copyProperties(fTemplate,copyFTemplate);
        copyFTemplate.setTemplateCategoryId(templateCategoryId);
        copyFTemplate.setTemplateId(null);
        copyFTemplate.setTemplateName(fTemplate.getTemplateName()+"_copy");
        fTemplateMapper.insertFTemplate(copyFTemplate);
        FTemplateForm fTemplateForm = new FTemplateForm();
        fTemplateForm.setTemplateId(templateId);
        fTemplateForm.setParentId(0L);
        List<FTemplateForm> fTemplateForms = fTemplateFormService.selectFTemplateFormList(fTemplateForm);
        if(CollectionUtils.isEmpty(fTemplateForms)){
            return;
        }
        // 内部线程池
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        try {

            List<CompletableFuture<Void>> futures =
                    fTemplateForms.stream()
                            .map(templateForm ->
                                    CompletableFuture.runAsync(() -> {

                                        fTemplateFormService.copyFTemplateForm(
                                                copyFTemplate.getTemplateId(),
                                                templateForm.getTemplateFormId()
                                        );

                                    }, executorService)
                            )
                            .collect(Collectors.toList());

        } finally {
            // 必须关闭
            executorService.shutdown();
        }
    }

}
