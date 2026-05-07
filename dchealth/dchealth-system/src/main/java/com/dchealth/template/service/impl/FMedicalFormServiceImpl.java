package com.dchealth.template.service.impl;

import com.dchealth.common.utils.CommunalUtils;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.dynamic.templete.DynamicFroms;
import com.dchealth.dynamic.templete.DynamicLargeText;
import com.dchealth.dynamic.templete.DynamicText;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.PatientRecordFiles;
import com.dchealth.medical.domain.vo.PatientRecordVo;
import com.dchealth.medical.mapper.PatientInfoMapper;
import com.dchealth.medical.mapper.PatientRecordFilesMapper;
import com.dchealth.medical.mapper.PatientRecordMapper;
import com.dchealth.research.domain.constant.PropertyType;
import com.dchealth.template.domain.*;
import com.dchealth.template.domain.vo.*;
import com.dchealth.template.mapper.*;
import com.dchealth.template.service.IFMedicalFormService;
import com.dchealth.warn.domain.form.WarnTemplateEarlyForm;
import com.dchealth.warn.domain.vo.WarnMedicalEarlyVO;
import com.dchealth.warn.service.IWarnService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 单模板Service业务层处理
 *
 * @author ruoyi
 * @date 2022-06-08
 */
@Service
public class FMedicalFormServiceImpl implements IFMedicalFormService
{
    @Autowired
    private FMedicalFormMapper fMedicalFormMapper;
    @Autowired
    private FMedicalPropertyMapper fMedicalPropertyMapper;
    @Autowired
    private FTemplateFormMapper fTemplateFormMapper;
    @Autowired
    private FTemplatePropertyMapper fTemplatePropertyMapper;
    /*@Autowired
    private FTemplateWarnConfigMapper fTemplateWarnConfigMapper;*/
    /*@Autowired
    private WarnConfigMapper warnConfigMapper;*/
    @Autowired
    private DynamicFroms dynamicFroms;
    @Autowired
    private DynamicText dynamicText;
    @Autowired
    private DynamicLargeText dynamicLargeText;
    @Autowired
    private PatientInfoMapper patientInfoMapper;
    @Autowired
    private PatientRecordMapper patientRecordMapper;
    /*@Autowired
    private DynamicArticle dynamicArticle;*/
    @Autowired
    private PatientRecordFilesMapper patientRecordFilesMapper;
    @Autowired
    private FMedicalExamSubjectMapper fMedicalExamSubjectMapper;
    @Autowired
    private FMedicalExamItemMapper fMedicalExamItemMapper;
    /*@Autowired
    private CmsArticleMapper cmsArticleMapper;*/

    @Autowired
    private IWarnService warnService;
    @Autowired
    private FTemplateExamSubjectMapper fTemplateExamSubjectMapper;
    @Autowired
    private FTemplateExamItemMapper fTemplateExamItemMapper;
    @Autowired
    private FTemplateFormSetEvaluationMapper fTemplateFormSetEvaluationMapper;
    @Autowired
    private FMedicalExamSetEvaluationMapper fMedicalExamSetEvaluationMapper;

    /**
     * 查询单模板
     *
     * @param medicalFormId 单模板主键
     * @return 单模板
     */
    @Override
    public FMedicalForm selectFMedicalFormByMedicalFormId(Long medicalFormId)
    {
        return fMedicalFormMapper.selectFMedicalFormByMedicalFormId(medicalFormId);
    }

    @Override
    public List<FMedicalForm> detailAll(Long followId) {
        List<FMedicalForm> result = new ArrayList<>();
        List<FMedicalForm> fMedicalForms = fMedicalFormMapper.selectFMedicalFormByMedicalFollowId3(followId);
        if(fMedicalForms!=null && fMedicalForms.size()>0){
            for(FMedicalForm f:fMedicalForms){
                if(f.getParentId()==0){
                    FMedicalForm form = detail(f.getMedicalFormId());
                    result.add(form);
                }
            }
        }
        return result;
    }


    @Override
    public FMedicalForm detail(Long medicalFormId) {
        FMedicalForm params = new FMedicalForm();
        params.setParentId(medicalFormId);
        List<FMedicalForm> fMedicalForms = fMedicalFormMapper.selectFMedicalFormList(params);
        FMedicalForm medicalForm = fMedicalFormMapper.selectFMedicalFormByMedicalFormId(medicalFormId);
        Map<Long, List<WarnMedicalEarlyVO>> warnMap = null;
        Map<Long, List<FMedicalExamItem>> itemMap = null;
        if (medicalForm != null){
            if("4".equals(medicalForm.getFormType())){
                List<WarnMedicalEarlyVO> warnMedicalEarlyVOS = warnService.selectWarnMedicalEarlys(medicalForm.getFollowId());
                if (warnMedicalEarlyVOS != null && !warnMedicalEarlyVOS.isEmpty()){
                    warnMap = warnMedicalEarlyVOS.stream().collect(Collectors.groupingBy(WarnMedicalEarlyVO :: getPropertyId));
                }
            }
            if("3".equals(medicalForm.getFormType())){
                List<FMedicalExamItem> itemList = fMedicalExamItemMapper.selectFMedicalExamItemByFromId(medicalForm.getMedicalFormId());
                if(itemList != null && !itemList.isEmpty()){
                    itemMap = itemList.stream().collect(Collectors.groupingBy(FMedicalExamItem::getPropertyId));
                }
            }
        }
        List<FMedicalForm> childrens = findChildren(fMedicalForms, warnMap, itemMap);
        if(medicalForm==null){
            medicalForm = new FMedicalForm();
        }
        medicalForm.setMedicalFormList(childrens);
        //组件属性
        if("3".equals(medicalForm.getFormType())){
            packageFMedicalProperty(medicalForm, itemMap);
        } else {
            List<FMedicalProperty> fMedicalProperties = fMedicalPropertyMapper.selectFMedicalPropertyByMedicalFormId(medicalFormId);
            if (fMedicalProperties != null){
                if (medicalForm != null && "4".equals(medicalForm.getFormType()) && warnMap != null){
                    for (FMedicalProperty p : fMedicalProperties){
                        if (warnMap.containsKey(p.getId())){
                            List<WarnMedicalEarlyVO> warnMedicalEarlyVOS = warnMap.get(p.getId());
                            if (warnMedicalEarlyVOS != null && !warnMedicalEarlyVOS.isEmpty()){
                                p.setIsCondition(warnMedicalEarlyVOS.get(0).getIsCondition());
                            }
                            p.setWarnMedicalEarlyVOS(warnMedicalEarlyVOS);
                        }
                    }
                }
                medicalForm.setMedicalProperties(fMedicalProperties);
            }
        }
        return medicalForm;
    }

    private List<FMedicalForm> findChildren(List<FMedicalForm> fMedicalForms,Map<Long, List<WarnMedicalEarlyVO>> warnMap, Map<Long, List<FMedicalExamItem>> itemMap){
        List<FMedicalForm> list = new ArrayList<>();
        if (fMedicalForms != null && !fMedicalForms.isEmpty()){
            fMedicalForms.forEach(x->{
                FMedicalForm params = new FMedicalForm();
                params.setParentId(x.getMedicalFormId());
                List<FMedicalForm> childrens = fMedicalFormMapper.selectFMedicalFormList(params);
                if (childrens != null && !childrens.isEmpty()){
                    x.setMedicalFormList(findChildren(childrens, warnMap, itemMap));
                }
                if ("0".equals(x.getFormType()) || "1".equals(x.getFormType()) || "2".equals(x.getFormType()) || "4".equals(x.getFormType()) || "5".equals(x.getFormType())){
                    List<FMedicalProperty> fMedicalProperties = fMedicalPropertyMapper.selectFMedicalPropertyByMedicalFormId(x.getMedicalFormId());
                    if (fMedicalProperties != null){
                        if("4".equals(x.getFormType()) || "1".equals(x.getFormType())){
                            for (FMedicalProperty p : fMedicalProperties){
                                if (warnMap != null && warnMap.containsKey(p.getId())){
                                    List<WarnMedicalEarlyVO> warnMedicalEarlyVOS = warnMap.get(p.getId());
                                    if (warnMedicalEarlyVOS != null && !warnMedicalEarlyVOS.isEmpty()){
                                        p.setIsCondition(warnMedicalEarlyVOS.get(0).getIsCondition());
                                    }
                                    p.setWarnMedicalEarlyVOS(warnMedicalEarlyVOS);
                                }
                            }
                        }
                        x.setMedicalProperties(fMedicalProperties);
                    }
                }
                if("3".equals(x.getFormType())){
                    packageFMedicalProperty(x, itemMap);
                }
                list.add(x);
            });
        }
        return list;
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
     * 查询单模板列表
     *
     * @param fMedicalForm 单模板
     * @return 单模板
     */
    @Override
    public List<FMedicalForm> selectFMedicalFormList(FMedicalForm fMedicalForm){
        List<FMedicalForm> list = fMedicalFormMapper.selectFMedicalFormList(fMedicalForm);
        FMedicalProperty fMedicalProperty = new FMedicalProperty();
        fMedicalProperty.setFollowId(fMedicalForm.getFollowId());
        List<FMedicalProperty> fMedicalPropertyList = fMedicalPropertyMapper.selectPropertyNumsList(fMedicalProperty);
        if(fMedicalPropertyList.size()>0 && list.size()>0){
            Map<Long,List<FMedicalProperty>> propertyMaps = fMedicalPropertyList.stream().collect(Collectors.groupingBy(FMedicalProperty::getParentId));
            for(FMedicalForm f:list){
                if(propertyMaps.containsKey(f.getMedicalFormId())){
                    f.setPropertyNum(f.getPropertyNum()+propertyMaps.get(f.getMedicalFormId()).size()+f.getEdcNum());
                }else{
                    f.setPropertyNum(f.getPropertyNum()+f.getEdcNum());
                }
//                if(f.getFormType().equals("3")){
//                    //自评
//                    FMedicalExamSubject fMedicalExamSubject = new FMedicalExamSubject();
//                    fMedicalExamSubject.setMedicalFormId(f.getMedicalFormId());
//                    List<FMedicalExamSubject> fMedicalExamSubjectList = fMedicalExamSubjectMapper.selectFMedicalExamSubjectList(fMedicalExamSubject);
//                    f.setPropertyNum(fMedicalExamSubjectList.size());
//                }
            }
        }else if(fMedicalPropertyList.size()==0 && list.size()>0){
            for(FMedicalForm f:list){
                f.setPropertyNum(f.getPropertyNum()+f.getEdcNum());
            }
        }
        if(list.size()>0){
            for(FMedicalForm f:list){
                if(f.getFormType().equals("3")){
                    //自评
                    FMedicalExamSubject fMedicalExamSubject = new FMedicalExamSubject();
                    fMedicalExamSubject.setMedicalFormId(f.getMedicalFormId());
                    fMedicalExamSubject.setPageSize(Integer.MAX_VALUE);
                    List<FMedicalExamSubject> fMedicalExamSubjectList = fMedicalExamSubjectMapper.selectFMedicalExamSubjectList(fMedicalExamSubject);
                    f.setPropertyNum(fMedicalExamSubjectList.size());


                    FMedicalForm fTemplateForm1 = new FMedicalForm();
                    fTemplateForm1.setParentId(f.getMedicalFormId());
                    List<FMedicalForm> fMedicalForms = fMedicalFormMapper.selectFMedicalFormList(fTemplateForm1);
                    for (FMedicalForm medicalForm : fMedicalForms) {
                        FMedicalExamSubject fTemplateExamSubject2 = new FMedicalExamSubject();
                        fTemplateExamSubject2.setMedicalFormId(medicalForm.getMedicalFormId());
                        List<FMedicalExamSubject> fMedicalExamSubjects = fMedicalExamSubjectMapper.selectFMedicalExamSubjectList(fTemplateExamSubject2);
                        f.setPropertyNum(f.getPropertyNum()+fMedicalExamSubjects.size());
                    }
                }
            }
        }
        return list;
    }

    /**
     * 新增单模板
     *
     * @param fMedicalForm 单模板
     * @return 结果
     */
    @Override
    public int insertFMedicalForm(FMedicalForm fMedicalForm)
    {
        checkHealthType(fMedicalForm);

        fMedicalForm.setCreateTime(new Date());
        return fMedicalFormMapper.insertFMedicalForm(fMedicalForm);
    }

    private void checkHealthType(FMedicalForm fMedicalForm) {
        // 对表单类型为5的健康管理方案类的进行限制。不可添加重名的，不可同时添加超过4个类型方案。考虑到小程序端的展示
        if ("5".equals(fMedicalForm.getFormType())) {
            List<FMedicalForm> fMedicalForms = fMedicalFormMapper.selectFMedicalFormByFollowId(fMedicalForm.getFollowId());

            if (fMedicalForms != null && !fMedicalForms.isEmpty()) {
                List<FMedicalForm> list = fMedicalForms.stream().filter(x -> x.getHealthManageType() != 0).collect(Collectors.toList());
                list.forEach(x -> {
                    if (x.getHealthManageType().equals(fMedicalForm.getHealthManageType())) {
                        throw new RuntimeException("不可重复添加健康管理模板");
                    }
                });
                if (list.size() > 4) {
                    throw new RuntimeException("健康管理方案类不可添加超过4个");
                }
            }


        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importOrUpdate(Long templateId, Long followId, Integer operateType) {
        //构建模板信息
        List<FTemplateForm> forms = builtFTemplateFromInfo(templateId, followId, operateType, null);
        recurrenceSave(followId, 0l, forms);
        return 1;
    }

    /**
     * 递归保存
     * @param followId
     * @param parentId
     * @param forms
     */
    private void recurrenceSave(Long followId, Long parentId, List<FTemplateForm> forms) {
        if (forms == null) {
            return;
        }
        Date date = DateUtils.getNowDate();
        for (FTemplateForm form : forms) {
            long templateFormId = form.getTemplateFormId();
            FMedicalForm medicalForm = new FMedicalForm();
            medicalForm.setFollowId(followId);
            medicalForm.setParentId(parentId);
            medicalForm.setAttribute(form.getAttribute());
            medicalForm.setFormSource("1");
            medicalForm.setFormType(form.getFormType());
            medicalForm.setMedicalCode(form.getTemplateCode());
            medicalForm.setMedicalName(form.getTemplateName());
            medicalForm.setOpenImage(form.getOpenImage());
            medicalForm.setOpenMultiple(form.getOpenMultiple());
            medicalForm.setOpenProperty(form.getOpenProperty());
            medicalForm.setDynamic(form.getDynamic());
            medicalForm.setHealthManageType(form.getHealthManageType());
            medicalForm.setScoresTotalVal(form.getScoresTotalVal());
            medicalForm.setSortNum(form.getSortNum());

            // 导入 之前进行校验
            checkHealthType(medicalForm);
            fMedicalFormMapper.insertFMedicalForm(medicalForm);
            if (form.getTemplateForms() != null && !form.getTemplateForms().isEmpty()) {
                recurrenceSave(followId, medicalForm.getMedicalFormId(), form.getTemplateForms());
            }
            if (form.getTemplateProperties() != null && !form.getTemplateProperties().isEmpty()) {
//                List<FMedicalProperty> medicalProperties = new ArrayList<>();
                if("3".equals(form.getFormType())){
                    form.getTemplateProperties().forEach(y-> {
                        FMedicalExamSubject subject = new FMedicalExamSubject();
                        BeanUtils.copyProperties(y, subject);
                        Long medicalFormId = medicalForm.getMedicalFormId();
                        subject.setCreateTime(date);
                        subject.setMedicalFormId(medicalFormId);
                        subject.setIfScore("1");
                        subject.setFollowId(medicalForm.getFollowId());
                        subject.setSortNum(medicalForm.getSortNum());
                        //存储每一个题目
                        fMedicalExamSubjectMapper.insertFMedicalExamSubject(subject);
                        //保存题目的选项
                        Optional<List<FTemplateExamItem>> itemList = Optional.ofNullable(y.getItemList());
                        if (itemList.isPresent()) {
                            itemList.get().forEach(item -> {
                                FMedicalExamItem fItem = new FMedicalExamItem();
                                BeanUtils.copyProperties(item, fItem);
                                fItem.setPropertyId(subject.getId());
                                fItem.setMedicalFormId(medicalFormId);
                                fItem.setCreateTime(date);
                                fMedicalExamItemMapper.insertFMedicalExamItem(fItem);
                            });
                        }
                    });
                    //同步测评结果
                    saveMedicalFormSetEvaluation(templateFormId,medicalForm);
                }else{
                    for (FTemplateProperty templateProperty : form.getTemplateProperties()) {
                        FMedicalProperty property = new FMedicalProperty();
                        property.setMedicalFormId(medicalForm.getMedicalFormId());
                        property.setFollowId(followId);
                        property.setAttribute(templateProperty.getAttribute());
                        property.setOpenProperty(templateProperty.getOpenProperty());
                        property.setPropertyCode(templateProperty.getPropertyCode());
                        property.setPropertyName(templateProperty.getPropertyName());
                        property.setPropertyType(templateProperty.getPropertyType());
                        property.setSortNum(templateProperty.getSortNum());
                        property.setUpdateBy(templateProperty.getUpdateBy());
                        property.setUpdateTime(templateProperty.getUpdateTime());
//                    medicalProperties.add(property);
                        fMedicalPropertyMapper.insertFMedicalProperty(property);
                        if (templateProperty.getWarnTemplateEarlyForms() != null && !templateProperty.getWarnTemplateEarlyForms().isEmpty()){
                            warnService.importWarnMedical(followId, medicalForm.getMedicalFormId(), property.getId(), templateProperty.getWarnTemplateEarlyForms());
                        }
                    }
                }
                //fMedicalPropertyMapper.batchInsertFMedicalProperty(medicalProperties);
            }
        }
    }

    /**
     * 同步测评结果
     * @param templateFormId
     * @param medicalForm
     */
    private void saveMedicalFormSetEvaluation(long templateFormId,FMedicalForm medicalForm){
        FTemplateFormSetEvaluation fTemplateFormSetEvaluation = new FTemplateFormSetEvaluation();
        fTemplateFormSetEvaluation.setTemplateFormId(templateFormId);
        List<FTemplateFormSetEvaluation> tfses = fTemplateFormSetEvaluationMapper.selectFTemplateFormSetEvaluationList(fTemplateFormSetEvaluation);
        for(FTemplateFormSetEvaluation tfse:tfses){
            FMedicalExamSetEvaluation fmese = new FMedicalExamSetEvaluation();
            BeanUtils.copyProperties(tfse,fmese);
            fmese.setMedicalFormId(medicalForm.getMedicalFormId());
            fMedicalExamSetEvaluationMapper.insertFMedicalExamSetEvaluation(fmese);
        }
    }

    /**
     * 保存预警
     * @param followId
     * @param medicalFormId
     * @param templateWarnConfigs
     */
    /*private int saveWarnConfig(Long followId, Long medicalFormId, List<FTemplateWarnConfig> templateWarnConfigs){
        if (templateWarnConfigs == null || templateWarnConfigs.isEmpty()){
            return 0;
        }
        List<WarnConfig> warnConfigs = new ArrayList<>();
        templateWarnConfigs.forEach(x->{
            WarnConfig warnConfig = new WarnConfig();
            warnConfig.setFollowId(followId);
            warnConfig.setMedicalFormId(medicalFormId);
            warnConfig.setMedicalPropertyCode(x.getTemplatePropertyCode());
            warnConfig.setResearchId(x.getResearchId());
            warnConfig.setStandardValue(x.getStandardValue());
            warnConfig.setWarnFormula(x.getWarnFormula());
            warnConfig.setWarnFormulaTitle(x.getWarnFormulaTitle());
            warnConfig.setUpdateBy(x.getUpdateBy());
            warnConfig.setUpdateTime(x.getUpdateTime());
            warnConfigs.add(warnConfig);
        });

        warnConfigMapper.batchInsertWarnConfig(warnConfigs);
        return 1;
    }*/

    /**
     * 数结构
     * @param id
     * @param templateForms
     * @return
     */
    private List<FTemplateForm> recurrenceTreeData(long id, List<FTemplateForm> templateForms){
        if (templateForms == null || templateForms.isEmpty()){
            return null;
        }
        List<FTemplateForm> result = new ArrayList<>();
        templateForms.forEach(x->{
            if (id == x.getParentId()){
                result.add(x);
            }
        });
        for (FTemplateForm templateForm : result) {
            List<FTemplateForm> childs = recurrenceTreeData(templateForm.getTemplateFormId(), templateForms);
            if (childs != null && !childs.isEmpty()){
                templateForm.setTemplateForms(childs);
            }
        }
        return result;
    }

    /**
     * 修改单模板
     *
     * @param fMedicalForm 单模板
     * @return 结果
     */
    @Override
    public int updateFMedicalForm(FMedicalForm fMedicalForm)
    {
        fMedicalForm.setUpdateTime(new Date());
        checkHealthType(fMedicalForm);
        return fMedicalFormMapper.updateFMedicalForm(fMedicalForm);
    }

    /**
     * 批量删除单模板
     *
     * @param medicalFormIds 需要删除的单模板主键
     * @return 结果
     */
    @Override
    public int deleteFMedicalFormByMedicalFormIds(Long[] medicalFormIds)
    {
        return fMedicalFormMapper.deleteFMedicalFormByMedicalFormIds(medicalFormIds);
    }

    /**
     * 迭代删除
     * @param fMedicalForms
     * @return
     */
    private int deleteIter(List<FMedicalForm> fMedicalForms){
        if (fMedicalForms != null && !fMedicalForms.isEmpty()){
            fMedicalForms.forEach(x->{
                FMedicalForm params = new FMedicalForm();
                params.setParentId(x.getMedicalFormId());
                List<FMedicalForm> childrens = fMedicalFormMapper.selectFMedicalFormList(params);
                if (childrens != null && !childrens.isEmpty()){
                    deleteIter(childrens);
                }
                fMedicalFormMapper.deleteFMedicalFormByMedicalFormId(x.getMedicalFormId());
                fMedicalPropertyMapper.deleteFMedicalPropertyByMedicalFormId(x.getMedicalFormId());
            });
        }
        return 1;
    }

    /**
     * 删除单模板信息
     *
     * @param medicalFormId 单模板主键
     * @return 结果
     */
    @Override
    public int deleteFMedicalFormByMedicalFormId(Long medicalFormId)
    {
        FMedicalForm params = new FMedicalForm();
        params.setParentId(medicalFormId);
        List<FMedicalForm> fMedicalForms = fMedicalFormMapper.selectFMedicalFormList(params);
        if (fMedicalForms != null && !fMedicalForms.isEmpty()){
            deleteIter(fMedicalForms);
        }
        fMedicalPropertyMapper.deleteFMedicalPropertyByMedicalFormId(medicalFormId);
        return fMedicalFormMapper.deleteFMedicalFormByMedicalFormId(medicalFormId);
    }

    /**
     * 根据随访id查询随访详细信息
     * @param vo
     * @return
     */
    @Override
    public MedicalDetailInfoVo selectFollowFMedicalFormInfo(FMedicalInfoReq vo) {
        MedicalDetailInfoVo result = new MedicalDetailInfoVo();
        Long patientId = vo.getPatientId();
        List<FMedicalForm> fMedicalFormInfo = getFMedicalFormInfo(vo);
        if(fMedicalFormInfo != null && !fMedicalFormInfo.isEmpty()){
            result.setMedicalInfos(fMedicalFormInfo);
            //查询患者基本信息
            PatientInfo patientInfo = patientInfoMapper.selectPatientDetailInfoByPatientId(patientId);
            if(patientInfo!=null && patientInfo.getIdCard()!=null){
                if(patientInfo.getIdCard().length()==18){
                    patientInfo.setSex(Integer.parseInt(CommunalUtils.getSex(patientInfo.getIdCard())));
                }
            }
            //此处新增随访提交时间 应唐都老师要求
            if(vo.getRecordId() != null) {
                PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByRecordId(vo.getRecordId());
                if (patientRecord != null && patientRecord.getUpdateTime() != null) {
                    if (patientInfo != null && patientRecord.getIsSubmit() != null && Integer.valueOf("1").equals(patientRecord.getIsSubmit())) {
                        patientInfo.setCommitTime(patientRecord.getUpdateTime());
                    }
                }
            }

            result.setPatientInfo(patientInfo);
        }
        return result;
    }

    /**
     * 查询问卷信息
     * @param fMedicalInfoReq
     * @return
     */
    @Override
    public FMedicalForm getFMedicalFormExamInfo(FMedicalInfoReq fMedicalInfoReq) {
        FMedicalForm fMedicalForm = fMedicalFormMapper.selectFMedicalFormByMedicalFormId(fMedicalInfoReq.getMedicalFormId());
        if(fMedicalForm != null){
            List<FMedicalExamSubject> subjects = fMedicalExamSubjectMapper.selectFMedicalExamSubjectByFromId(fMedicalForm.getMedicalFormId());
            if(subjects != null && !subjects.isEmpty()){
                subjects.forEach(subject ->{
                    List<FMedicalExamItem> items = fMedicalExamItemMapper.selectFMedicalExamItemByProId(subject.getId());
                    subject.setItemList(items);
                });
            }
            fMedicalForm.setSubjectList(subjects);
        }
        return fMedicalForm;
    }

    /**
     * CRF导入模板模块信息
     * @param templateId
     * @param followId
     * @param templateFromIds
     * @return
     */
    @Override
    public int getFMedicalFormIsRepeat(Long templateId, Long followId, List<String> templateFromIds) {
        //查询导入
        List<FTemplateForm> templateForms = fTemplateFormMapper.selectFTemplateFormByTemplateId(templateId);
        List<FMedicalForm> fMedicalForms = fMedicalFormMapper.selectFMedicalFormByFollowId(followId);
        //提取重复项id
        List<String> tFormParentNames = templateForms.stream().filter(form -> form.getParentId() == 0).map(FTemplateForm::getTemplateName)
                .collect(Collectors.toList());
        List<Long> medicalFormParentIds = fMedicalForms.stream().filter(form -> 0 == form.getParentId() && tFormParentNames.contains(form.getMedicalName()))
                .map(FMedicalForm::getMedicalFormId).collect(Collectors.toList());
        List<Long> medicalFormIds = new ArrayList<>(medicalFormParentIds);
        List<Long> medicalFormItemIds = fMedicalForms.stream().filter(form -> medicalFormParentIds.contains(form.getParentId()))
                .map(FMedicalForm::getMedicalFormId).collect(Collectors.toList());
        medicalFormIds.addAll(medicalFormItemIds);
        //无值即为无重复、有值即为重复
        if(medicalFormIds == null || medicalFormIds.size() == 0) return 0;
        return 1;
    }

    /**
     * CRF导入模板模块信息
     * @param templateId
     * @param followId
     * @param templateFromIds
     * @return
     */
    @Override
    public int importOrUpdateByTemplateFromIds(Long templateId, Long followId, List<String> templateFromIds, Integer operateType) {
        //构建模板信息 0 为添加  1 为替换
        List<FTemplateForm> forms = builtFTemplateFromInfo(templateId, followId, operateType, templateFromIds);
        if(!templateFromIds.isEmpty()){
            //筛选所选模块
            List<FTemplateForm> fTemplateForms = forms.stream().filter(fTemplateForm -> templateFromIds.contains(String.valueOf(fTemplateForm.getTemplateFormId()))).collect(Collectors.toList());
            recurrenceSave(followId, 0l, fTemplateForms);
        }
        return 1;
    }

    @Override
    public int batchUpdateSortNum(FMedicalFormSortReq fMedicalFormSortReq) {
        if(CollectionUtils.isEmpty(fMedicalFormSortReq.getFormSortVos())){
            return 0;
        }
        for(FMedicalFormSortVo f:fMedicalFormSortReq.getFormSortVos()){
            FMedicalForm medicalForm = new FMedicalForm();
            medicalForm.setMedicalFormId(f.getMedicalFormId());
            medicalForm.setSortNum(f.getSortNum());
            fMedicalFormMapper.updateFMedicalForm(medicalForm);

            FMedicalForm ff = new FMedicalForm();
            ff.setParentId(f.getMedicalFormId());
            List<FMedicalForm> forms = fMedicalFormMapper.selectFMedicalFormList(ff);
            if(forms!=null && forms.size()>0){
                ff.setSortNum(f.getSortNum());
                fMedicalFormMapper.updateFMedicalFormParentSortNum(ff);
            }
        }
        return 1;
    }

    /**
     * 构建模板信息
     * @param templateId
     * @param followId
     * @return
     */
    public List<FTemplateForm> builtFTemplateFromInfo(Long templateId, Long followId, Integer operateType,
                                                      List<String> templateFromIds){
 //       fMedicalFormMapper.deleteFMedicalFormByMedicalFollowId(followId);
 //       fMedicalPropertyMapper.deleteFMedicalPropertyByMedicalFollowId(followId);
  //      warnService.deleteWarnMedicalFollowId(followId);
//        warnConfigMapper.deleteWarnConfigByByFollowId(followId);
        //查询导入
        List<FTemplateForm> templateForms = fTemplateFormMapper.selectFTemplateFormByTemplateId(templateId);
        List<FTemplateProperty> templateProperties = fTemplatePropertyMapper.selectFTemplatePropertyByTemplateId(templateId);

        FTemplateExamSubject fTemplateExamSubject = new FTemplateExamSubject();
        fTemplateExamSubject.setTemplateId(templateId);
        List<FTemplateExamSubject> fTemplateExamSubjects = fTemplateExamSubjectMapper.selectFTemplateExamSubjectList(fTemplateExamSubject);

        List<FTemplateProperty> templateProperties2 = new ArrayList<>();
        for (FTemplateExamSubject templateExamSubject : fTemplateExamSubjects) {
            FTemplateProperty fTemplateProperty = new FTemplateProperty();
            BeanUtils.copyProperties(templateExamSubject,fTemplateProperty);
            templateProperties2.add(fTemplateProperty);
        }
        templateProperties.addAll(templateProperties2);

//        List<FTemplateWarnConfig> templateWarnConfigs = fTemplateWarnConfigMapper.selectFTemplateWarnConfigByTemplateId(templateId);
        List<WarnTemplateEarlyForm> warnTemplateEarlyForms = warnService.selectWarnTemplateEarlysForm(templateId);
        //精确到模板里面某个条目
        if(templateFromIds != null && !templateFromIds.isEmpty()){
            //筛选所选模块
            templateForms = templateForms.stream().filter(fTemplateForm -> templateFromIds.contains(String.valueOf(fTemplateForm.getTemplateFormId()))).collect(Collectors.toList());
        }
            //替换先删除重复，再入新，否则添加
        if(operateType == 1)this.distinctFTemplateForms(followId, templateForms);
        if (templateForms != null && !templateForms.isEmpty()){
            Map<Long, List<FTemplateProperty>> templatePropertyMaps = null;
            Map<Long, List<FTemplateExamItem>> itemMap = null;
            if (templateProperties != null && !templateProperties.isEmpty()){
                Map<Long, List<WarnTemplateEarlyForm>> wLongListMap = Optional.ofNullable(warnTemplateEarlyForms)
                        .map(n->n.stream().collect(Collectors.groupingBy(WarnTemplateEarlyForm :: getPropertyId))).orElse(null);
                templatePropertyMaps = templateProperties.stream().map(m->{
                    if (wLongListMap != null && !wLongListMap.isEmpty()
                            && wLongListMap.containsKey(m.getId())){
                        m.setWarnTemplateEarlyForms(wLongListMap.get(m.getId()));
                    }
                    return m;
                }).collect(Collectors.groupingBy(FTemplateProperty :: getTemplateFormId));
            }
            for (FTemplateForm templateForm : templateForms) {
                if (templatePropertyMaps != null && templatePropertyMaps.containsKey(templateForm.getTemplateFormId())){
                    templateForm.setTemplateProperties(templatePropertyMaps.get(templateForm.getTemplateFormId()));
                }
                // 子表单
                FTemplateForm queryForm = new FTemplateForm();
                queryForm.setParentId(templateForm.getTemplateFormId());
                List<FTemplateForm> templateFormsChild = fTemplateFormMapper.selectFTemplateFormList(queryForm);
                if (!CollectionUtils.isEmpty(templateFormsChild)) {
                    templateForm.setTemplateForms(templateFormsChild);
                    for (FTemplateForm fTemplateForm : templateFormsChild) {
                        if (templatePropertyMaps != null && templatePropertyMaps.containsKey(fTemplateForm.getTemplateFormId())){
                            fTemplateForm.setTemplateProperties(templatePropertyMaps.get(fTemplateForm.getTemplateFormId()));
                        }
                    }
                }

                if("3".equals(templateForm.getFormType())){
                    packageFTemplateProperty(templateForm);
                }
            }
        }
        return recurrenceTreeData(0l, templateForms);
    }

    /**
     * 去重，当条目名称相同，删除原有，导入新
     * @param followId 随访项目id
     * @param templateForms 导入模板数据
     */
    private void distinctFTemplateForms(Long followId, List<FTemplateForm> templateForms) {
        List<FMedicalForm> fMedicalForms = fMedicalFormMapper.selectFMedicalFormByFollowId(followId);
        //提取重复项id
        List<String> tFormParentNames = templateForms.stream().filter(form -> form.getParentId() == 0).map(FTemplateForm::getTemplateName)
                .collect(Collectors.toList());
        List<Long> medicalFormParentIds = fMedicalForms.stream().filter(form -> 0 == form.getParentId() && tFormParentNames.contains(form.getMedicalName()))
                .map(FMedicalForm::getMedicalFormId).collect(Collectors.toList());
        List<Long> medicalFormIds = new ArrayList<>(medicalFormParentIds);
        List<Long> medicalFormItemIds = fMedicalForms.stream().filter(form -> medicalFormParentIds.contains(form.getParentId()))
                .map(FMedicalForm::getMedicalFormId).collect(Collectors.toList());
        medicalFormIds.addAll(medicalFormItemIds);
        //删除原有条目
        if(medicalFormIds.size() == 0) return;
        fMedicalFormMapper.deleteFMedicalFormByMedicalFormIds(medicalFormIds.toArray(new Long[0]));
        fMedicalPropertyMapper.deleteFMedicalPropertyByMedicalFormIds(medicalFormIds);
        List<FMedicalProperty> fMedicalProperties = fMedicalPropertyMapper.selectFMedicalPropertyByMedicalFormIds(medicalFormIds);
        List<Long> propertyIds = fMedicalProperties.stream().map(FMedicalProperty::getId).collect(Collectors.toList());
        warnService.deleteWarnMedicalByPropertyIds(propertyIds);
    }

    /**
     * 封装自评量表题干、选项信息
     * @param fTemplateForm
     */
    public void packageFTemplateProperty(FTemplateForm fTemplateForm){
        List<FTemplateExamSubject> subjectList = fTemplateExamSubjectMapper.selectFTemplateExamSubjectByTemFromId(fTemplateForm.getTemplateFormId());
        if(subjectList != null && !subjectList.isEmpty()){
            List<FTemplateProperty> properties = new ArrayList<>();
            for (FTemplateExamSubject subject : subjectList) {
                FTemplateProperty property = new FTemplateProperty();
                BeanUtils.copyProperties(subject, property);
                List<FTemplateExamItem> itemList = fTemplateExamItemMapper.selectExamItemByPropertyId(subject.getId());
                property.setItemList(itemList);
                properties.add(property);
            }
            fTemplateForm.setTemplateProperties(properties);
        }
    }

    /**
     * app模板某模块详情查询
     * @param vo
     * @return
     */
    public FMedicalForm getAppPatientRecordDetailInfo(FMedicalInfoReq vo) {
        List<FMedicalForm> fMedicalForms = null;
        List<FMedicalProperty> fMedicalProperties = null;
        Long followId = vo.getFollowId();
        //获取随访模块信息（需要筛选APP端模板信息）
        if(followId != null){
            fMedicalForms = fMedicalFormMapper.selectFMedicalFormByFollowId(followId);
            //查询随访模块属相信息
            fMedicalProperties = fMedicalPropertyMapper.selectFMedicalPropertyByFollowId(followId);

            //查询问卷信息
            List<FMedicalExamSubject> subjects = fMedicalExamSubjectMapper.selectFMedicalExamSubjectByFollowId(followId);
            List<FMedicalProperty> copyPropertys = com.dchealth.common.utils.bean.BeanUtils.copyList(subjects, FMedicalProperty:: new);
            fMedicalProperties.addAll(copyPropertys);

        }
        if(fMedicalForms != null && !fMedicalForms.isEmpty()) {
            //判断表单id是否存在属性分组表中，存在则将属性信息赋值表单
            for (FMedicalForm fMedicalForm : fMedicalForms) {
                //拍照项
                if("2".equals(fMedicalForm.getFormType())){
                    List<PatientRecordFiles> patientRecordFiles = patientRecordFilesMapper.selectFilesByRecordIdAndMedicalFormId(fMedicalForm.getMedicalFormId(),vo.getRecordId());
                    fMedicalForm.setPatientRecordFiles(patientRecordFiles);
                } else {
                    //属性值赋值
                    packageMedicalProperties(fMedicalProperties,fMedicalForm,vo,"1");
                }
            }
            //构建树状结构-子父关系
            fMedicalForms = rebuildTreeData(0l, fMedicalForms);
        }
        List<FMedicalForm> medicalForms = fMedicalForms.stream().filter(fMedicalForm -> fMedicalForm.getMedicalFormId().equals(vo.getMedicalFormId())).collect(Collectors.toList());
        return medicalForms != null && medicalForms.size()>0 ? medicalForms.get(0) : null;
    }

    /**
     * 后台模板模块详情
     * @param vo
     * @return
     */
    public FMedicalForm getFMedicalFormDetailInfo(FMedicalInfoReq vo) {
        List<FMedicalForm> fMedicalForms = null;
        List<FMedicalProperty> fMedicalProperties = null;
        Long followId = vo.getFollowId();
        //获取随访模块信息
        if(followId != null){
            fMedicalForms = fMedicalFormMapper.selectFMedicalFormByFollowId(followId);
            //查询随访模块属相信息
            fMedicalProperties = fMedicalPropertyMapper.selectFMedicalPropertyByFollowId(followId);

            //查询问卷信息
            List<FMedicalExamSubject> subjects = fMedicalExamSubjectMapper.selectFMedicalExamSubjectByFollowId(followId);
            List<FMedicalProperty> copyPropertys = com.dchealth.common.utils.bean.BeanUtils.copyList(subjects, FMedicalProperty:: new);
            fMedicalProperties.addAll(copyPropertys);

        }
        if(fMedicalForms != null && !fMedicalForms.isEmpty()) {
            //判断表单id是否存在属性分组表中，存在则将属性信息赋值表单
            for (FMedicalForm fMedicalForm : fMedicalForms) {
                //属性值赋值
                packageMedicalProperties(fMedicalProperties,fMedicalForm,vo,"2");
            }
            //构建树状结构-子父关系
            fMedicalForms = rebuildTreeData(0l, fMedicalForms);
        }
        List<FMedicalForm> medicalForms = fMedicalForms.stream().filter(fMedicalForm -> fMedicalForm.getMedicalFormId().equals(vo.getMedicalFormId())).collect(Collectors.toList());
        return medicalForms != null && medicalForms.size()>0 ? medicalForms.get(0) : null;
    }

    public List<FMedicalForm> getFMedicalFormInfo(FMedicalInfoReq vo){
        List<FMedicalForm> fMedicalForms = null;
        List<FMedicalProperty> fMedicalProperties = null;
        Long followId = vo.getFollowId();
        Long articleId = vo.getArticleId();
        //获取随访模块信息
        if(followId != null){
            fMedicalForms = fMedicalFormMapper.selectFMedicalForm(followId,vo.getMedicalFormId());
            FMedicalForm ff = new FMedicalForm();
            ff.setParentId(fMedicalForms.get(0).getMedicalFormId());
            List<FMedicalForm> childForm = fMedicalFormMapper.selectFMedicalFormList(ff);
            if(childForm.size()>0){
                fMedicalForms.addAll(childForm);
            }

            //查询随访模块属相信息
//            fMedicalProperties = fMedicalPropertyMapper.selectFMedicalProperty(followId,vo.getMedicalFormId());
            List<FMedicalExamSubject> fMedicalExamSubjects = fMedicalExamSubjectMapper.selectFMedicalExamSubjectByFromId(vo.getMedicalFormId());

            fMedicalProperties = fMedicalPropertyMapper.selectFMedicalPropertyByFollowId(followId);
            if (!CollectionUtils.isEmpty(fMedicalExamSubjects)) {

                List<FMedicalProperty> fMedicalPropertyList = fMedicalExamSubjects.stream().map(x-> {
                    FMedicalProperty fMedicalProperty = new FMedicalProperty();
                    BeanUtils.copyProperties(x,fMedicalProperty);
                    return fMedicalProperty;
                }).collect(Collectors.toList());

                if (CollectionUtils.isEmpty(fMedicalProperties)) {
                    fMedicalProperties = new ArrayList<>();
                }
                fMedicalProperties.addAll(fMedicalPropertyList);
            }

        } else if(articleId != null){
            //TODO:由于患教表拆成独立的表，和随访的表单不在共用，此处的代码无用了
//            //获取患教相关信息
//            fMedicalForms = fMedicalFormMapper.selectFMedicalFormByArticleId(articleId);
//            //查询患教模块属性信息
//            fMedicalProperties = fMedicalPropertyMapper.selectFMedicalPropertyByArticleId(articleId);
        }
        if(fMedicalForms != null && !fMedicalForms.isEmpty()) {
            //判断表单id是否存在属性分组表中，存在则将属性信息赋值表单
            for (FMedicalForm fMedicalForm : fMedicalForms) {
                //属性值赋值
                packageMedicalProperties(fMedicalProperties,fMedicalForm,vo,"1");
            }
            //构建树状结构-子父关系
            fMedicalForms = rebuildTreeData(0l, fMedicalForms);
        }
        return fMedicalForms;
    }

    /**
     * app模板数据
     * @param vo
     * @return
     */
    public List<FMedicalForm> getAppPatientRecordInfo(FMedicalInfoReq vo){
        List<FMedicalForm> fMedicalForms = null;
        List<FMedicalProperty> fMedicalProperties = null;
        Long followId = vo.getFollowId();
        Long articleId = vo.getArticleId();
        //获取随访模块信息（需要筛选APP端模板信息）
        if(followId != null){
            fMedicalForms = fMedicalFormMapper.selectFMedicalFormByFollowId(followId);
            //查询随访模块属相信息
            fMedicalProperties = fMedicalPropertyMapper.selectFMedicalPropertyByFollowId(followId);
        } else if(articleId != null){
            //获取患教相关信息
            fMedicalForms = fMedicalFormMapper.selectFMedicalFormByArticleId(articleId);
            //查询患教模块属性信息
            fMedicalProperties = fMedicalPropertyMapper.selectFMedicalPropertyByArticleId(articleId);
        }
        if(fMedicalForms != null && !fMedicalForms.isEmpty()) {
            //判断表单id是否存在属性分组表中，存在则将属性信息赋值表单
            for (FMedicalForm fMedicalForm : fMedicalForms) {
                //拍照项
                if("2".equals(fMedicalForm.getFormType())){
                    List<PatientRecordFiles> patientRecordFiles = patientRecordFilesMapper.selectPatientRecordFilesByMedicalFormId(fMedicalForm.getMedicalFormId());
                    fMedicalForm.setPatientRecordFiles(patientRecordFiles);
                } else {
                    //属性值赋值
                    packageMedicalProperties(fMedicalProperties,fMedicalForm,vo);
                }
            }
            //构建树状结构-子父关系
            fMedicalForms = rebuildTreeData(0l, fMedicalForms);
        }
        return fMedicalForms;
    }

    /**
     * 封装属性值
     * @param fMedicalProperties
     * @param fMedicalForm
     * @param vo
     */
    public void packageMedicalProperties(List<FMedicalProperty> fMedicalProperties, FMedicalForm fMedicalForm, FMedicalInfoReq vo){
        Long followId = vo.getFollowId();
        Long researchId = vo.getResearchId();
        Map<Long, List<FMedicalProperty>> fMedicalPropertyMaps = null;
        //根据表单id分组
        if (fMedicalProperties != null && !fMedicalProperties.isEmpty()) {
            fMedicalPropertyMaps = fMedicalProperties.stream().collect(Collectors.groupingBy(FMedicalProperty::getMedicalFormId));
        }
        if (fMedicalPropertyMaps != null && fMedicalPropertyMaps.containsKey(fMedicalForm.getMedicalFormId())) {
            //获取属性值
            List<FMedicalProperty> medicalProperties = fMedicalPropertyMaps.get(fMedicalForm.getMedicalFormId());
            List<FMedicalValueVo> fMedicalValueVos = new ArrayList<>();
            List<PatientRecordFiles> patientRecordFiles = new ArrayList<>();
            if (medicalProperties != null && !medicalProperties.isEmpty()) {
                String sql = null;
                if (followId != null) {
                    //判断属性存值类型（大文本、普通文本、动态表单）
                    Set<String> propertyTypes = medicalProperties.stream().filter(fMedicalProperty -> fMedicalProperty.getPropertyType() != null).map(FMedicalProperty::getPropertyType).collect(Collectors.toSet());
                    if (propertyTypes != null && PropertyType.largeText(propertyTypes)) {
                        //大文本
                        sql = dynamicLargeText.buildSelectSql(researchId, fMedicalForm.getMedicalFormId(), vo.getRecordId());
                        List<FMedicalValueVo> valueVos = fMedicalPropertyMapper.selectFMedicalValues(sql);
                        fMedicalValueVos.addAll(valueVos);
                    }
                    if(PropertyType.text(propertyTypes)) {
                        //小文本
                        sql = dynamicText.buildSelectSql(researchId, fMedicalForm.getMedicalFormId(), vo.getRecordId());
                        List<FMedicalValueVo> valueVos = fMedicalPropertyMapper.selectFMedicalValues(sql);
                        fMedicalValueVos.addAll(valueVos);
                    }
                    if("1".equals(fMedicalForm.getDynamic())){
                        //动态表单
                        sql = dynamicFroms.buildSelectSql(researchId, fMedicalForm.getMedicalFormId(), vo.getRecordId());
                        List<FMedicalValueVo> valueVos = fMedicalPropertyMapper.selectFMedicalValues(sql);
                        fMedicalValueVos.addAll(valueVos);
                    }
                    if(PropertyType.upLoad(propertyTypes)){
                        //图片
                        List<PatientRecordFiles> recordFiles = patientRecordFilesMapper.selectPatientRecordFilesByMedicalFormId(fMedicalForm.getMedicalFormId());
                        patientRecordFiles.addAll(recordFiles);
                    }
                }
                Map<Long, FMedicalValueVo> valuesMap = null;
                Map<Long, List<PatientRecordFiles>> fileMap = null;
                //将值赋值属性表
                if (fMedicalValueVos != null && !fMedicalValueVos.isEmpty()) {
                    valuesMap = fMedicalValueVos.stream().collect(Collectors.toMap(FMedicalValueVo::getPropertyId,item -> item));
                }
                //图片赋值
                if(patientRecordFiles != null && !patientRecordFiles.isEmpty()){
                    fileMap = patientRecordFiles.stream().collect(Collectors.groupingBy(PatientRecordFiles::getPropertyId));
                }
                for (FMedicalProperty fMedicalProperty : medicalProperties) {
                    if (valuesMap != null && valuesMap.get(fMedicalProperty.getId()) != null) {
                        FMedicalValueVo fMedicalValueVo = valuesMap.get(fMedicalProperty.getId());
                        fMedicalProperty.setPropertyValue(fMedicalValueVo.getPropertyValue());
                        fMedicalProperty.setOther(fMedicalValueVo.getOther());
                    }
                    if(fileMap != null && fileMap.get(fMedicalProperty.getId()) != null){
                        List<PatientRecordFiles> files = fileMap.get(fMedicalProperty.getId());
                        List<String> list = files.stream().map(PatientRecordFiles::getImageUrl).collect(Collectors.toList());
                        fMedicalProperty.setImageUrls(list);
                    }
                }
            }
            fMedicalForm.setMedicalProperties(medicalProperties);
        }
    }

    //区分app和后台
    public void packageMedicalProperties(List<FMedicalProperty> fMedicalProperties, FMedicalForm fMedicalForm, FMedicalInfoReq vo,String type){
        Long followId = vo.getFollowId();
        Long researchId = vo.getResearchId();
        Map<Long, List<FMedicalProperty>> fMedicalPropertyMaps = null;
        //根据表单id分组
        if (fMedicalProperties != null && !fMedicalProperties.isEmpty()) {
            fMedicalPropertyMaps = fMedicalProperties.stream().collect(Collectors.groupingBy(FMedicalProperty::getMedicalFormId));
        }
        if (fMedicalPropertyMaps != null && fMedicalPropertyMaps.containsKey(fMedicalForm.getMedicalFormId())) {
            //获取属性值
            List<FMedicalProperty> medicalProperties = fMedicalPropertyMaps.get(fMedicalForm.getMedicalFormId());
            List<FMedicalValueVo> fMedicalValueVos = new ArrayList<>();
            List<PatientRecordFiles> patientRecordFiles = new ArrayList<>();
            if (medicalProperties != null && !medicalProperties.isEmpty()) {
                String sql = null;
                if (followId != null) {
                    //判断属性存值类型（大文本、普通文本、动态表单）
                    Set<String> propertyTypes = medicalProperties.stream().filter(fMedicalProperty -> fMedicalProperty.getPropertyType() != null).map(FMedicalProperty::getPropertyType).collect(Collectors.toSet());
                    if (propertyTypes != null && PropertyType.largeText(propertyTypes)) {
                        //大文本
                        sql = dynamicLargeText.buildSelectSql(researchId, fMedicalForm.getMedicalFormId(), vo.getRecordId(),type);
                        List<FMedicalValueVo> valueVos = fMedicalPropertyMapper.selectFMedicalValues(sql);
                        fMedicalValueVos.addAll(valueVos);
                    }
                    if(PropertyType.text(propertyTypes)) {
                        //小文本
                        sql = dynamicText.buildSelectSql(researchId, fMedicalForm.getMedicalFormId(), vo.getRecordId(),type);
                        List<FMedicalValueVo> valueVos = fMedicalPropertyMapper.selectFMedicalValues(sql);
                        fMedicalValueVos.addAll(valueVos);
                    }
                    if(true){//"1".equals(fMedicalForm.getDynamic())
                        //动态表单
                        sql = dynamicFroms.buildSelectSql(researchId, fMedicalForm.getMedicalFormId(), vo.getRecordId(),type);
                        List<FMedicalValueVo> valueVos = fMedicalPropertyMapper.selectFMedicalValues(sql);
                        fMedicalValueVos.addAll(valueVos);
                    }
                    if(PropertyType.upLoad(propertyTypes)){
                        //图片
                        List<PatientRecordFiles> recordFiles = patientRecordFilesMapper.selectFilesByRecordIdAndMedicalFormId(fMedicalForm.getMedicalFormId(),vo.getRecordId());
                        patientRecordFiles.addAll(recordFiles);
                    }
                }
                Map<String, FMedicalValueVo> valuesMap = null;
                Map<Long, List<PatientRecordFiles>> fileMap = null;
                //将值赋值属性表
                if (fMedicalValueVos != null && !fMedicalValueVos.isEmpty()) {
                    valuesMap = fMedicalValueVos.stream().collect(Collectors.toMap(FMedicalValueVo::getPropertyCode,item -> item));
                }
                //图片赋值
                if(patientRecordFiles != null && !patientRecordFiles.isEmpty()){
                    fileMap = patientRecordFiles.stream().collect(Collectors.groupingBy(PatientRecordFiles::getPropertyId));
                }
                for (FMedicalProperty fMedicalProperty : medicalProperties) {
                    if (valuesMap != null && valuesMap.get(fMedicalProperty.getPropertyCode()) != null) {
                        FMedicalValueVo fMedicalValueVo = valuesMap.get(fMedicalProperty.getPropertyCode());
                        fMedicalProperty.setPropertyValue(fMedicalValueVo.getPropertyValue());
                        fMedicalProperty.setOther(fMedicalValueVo.getOther());
                    }
                    if(fileMap != null && fileMap.get(fMedicalProperty.getId()) != null){
                        List<PatientRecordFiles> files = fileMap.get(fMedicalProperty.getId());
                        List<String> list = files.stream().map(PatientRecordFiles::getImageUrl).collect(Collectors.toList());
                        fMedicalProperty.setImageUrls(list);
                    }
                }
            }
            fMedicalForm.setMedicalProperties(medicalProperties);
        }
    }


    /**
     * 构建表单树结构
     * @param id
     * @param fMedicalForms
     * @return
     */
    private List<FMedicalForm> rebuildTreeData(long id, List<FMedicalForm> fMedicalForms) {
        List<FMedicalForm> result = new ArrayList<>();
        fMedicalForms.forEach(x->{
            if (x.getParentId() != null && id == x.getParentId()){
                result.add(x);
            }
        });
        for (FMedicalForm fMedicalForm : result) {
            List<FMedicalForm> childs = rebuildTreeData(fMedicalForm.getMedicalFormId(), fMedicalForms);
            if (childs != null && !childs.isEmpty()){
                fMedicalForm.setMedicalFormList(childs);
            }
        }
        return result;
    }

    /**
     * 封装property信息
     * @param medicalProperties
     * @param fMedicalForm
     * @param vo
     * @param propertyId
     * @return
     */
    public String packageMedicalPropertyValues(List<FMedicalProperty> medicalProperties, FMedicalForm fMedicalForm, PatientRecordVo vo, Long propertyId){
        String propertyValue = "";
        Long followId = vo.getFollowId();
        Long researchId = vo.getResearchId();
        //获取属性值
        List<FMedicalValueVo> fMedicalValueVos = new ArrayList<>();
        List<PatientRecordFiles> patientRecordFiles = new ArrayList<>();
        if (medicalProperties != null && !medicalProperties.isEmpty()) {
            String sql = null;
            if (followId != null) {
                //判断属性存值类型（大文本、普通文本、动态表单）
                Set<String> propertyTypes = medicalProperties.stream().filter(fMedicalProperty -> fMedicalProperty.getPropertyType() != null).map(FMedicalProperty::getPropertyType).collect(Collectors.toSet());
                if (propertyTypes != null && PropertyType.largeText(propertyTypes)) {
                    //大文本
                    sql = dynamicLargeText.buildSelectSql(researchId, vo.getRecordId());
                    List<FMedicalValueVo> valueVos = fMedicalPropertyMapper.selectFMedicalValues(sql);
                    fMedicalValueVos.addAll(valueVos);
                }
                if(PropertyType.text(propertyTypes)) {
                    //小文本
                    sql = dynamicText.buildSelectSql(researchId, vo.getRecordId());
                    List<FMedicalValueVo> valueVos = fMedicalPropertyMapper.selectFMedicalValues(sql);
                    fMedicalValueVos.addAll(valueVos);
                }
                if("1".equals(fMedicalForm.getDynamic())){
                    //动态表单
                    sql = dynamicFroms.buildSelectSql(researchId, vo.getRecordId());
                    List<FMedicalValueVo> valueVos = fMedicalPropertyMapper.selectFMedicalValues(sql);
                    fMedicalValueVos.addAll(valueVos);
                }
                if(PropertyType.upLoad(propertyTypes)){
                    //图片
                    List<PatientRecordFiles> recordFiles = patientRecordFilesMapper.selectPatientRecordFilesByMedicalFormId(fMedicalForm.getMedicalFormId());
                    patientRecordFiles.addAll(recordFiles);
                }
            }
            Map<Long, FMedicalValueVo> valuesMap = null;
            Map<Long, List<PatientRecordFiles>> fileMap = null;
            //将值赋值属性表
            if (fMedicalValueVos != null && !fMedicalValueVos.isEmpty()) {
                valuesMap = fMedicalValueVos.stream().collect(Collectors.toMap(FMedicalValueVo::getPropertyId,item -> item));
            }
            //图片赋值
            if(patientRecordFiles != null && !patientRecordFiles.isEmpty()){
                fileMap = patientRecordFiles.stream().collect(Collectors.groupingBy(PatientRecordFiles::getPropertyId));
            }
            if (valuesMap != null && valuesMap.get(propertyId) != null) {
                FMedicalValueVo fMedicalValueVo = valuesMap.get(propertyId);
                if(fMedicalValueVo.getPropertyValue() != null){
                    propertyValue = fMedicalValueVo.getPropertyValue();
                } else if(fMedicalValueVo.getOther() != null){
                    propertyValue = fMedicalValueVo.getOther();
                }
            }
            if(fileMap != null && fileMap.get(propertyId) != null){
                List<PatientRecordFiles> files = fileMap.get(propertyId);
                if(files != null){
                    List<String> list = files.stream().map(PatientRecordFiles::getImageUrl).collect(Collectors.toList());
                    propertyValue =  String.join(",",list);
                }
            }
        }
        return propertyValue;
    }

    @Override
    public List<FMedicalForm> queryFMedicalFormList(List<Long> followIds) {
        return fMedicalFormMapper.queryFMedicalFormList(followIds);
    }
}
