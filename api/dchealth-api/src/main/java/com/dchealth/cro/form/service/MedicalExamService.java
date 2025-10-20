package com.dchealth.cro.form.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dchealth.cro.cms.domain.CmsArticle;
import com.dchealth.cro.cms.mapper.CmsArticleMapper;
import com.dchealth.cro.common.base.CommonResult;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.form.common.DynamicSql;
import com.dchealth.cro.form.constant.PropertyType;
import com.dchealth.cro.form.domain.*;
import com.dchealth.cro.form.domain.form.DynamicForm;
import com.dchealth.cro.form.mapper.*;
import com.dchealth.cro.form.vo.form.FormItemVo;
import com.dchealth.cro.form.vo.form.ProblemFromVo;
import com.dchealth.cro.form.vo.form.ProblemOptionVo;
import com.dchealth.cro.medical.contants.MedicalContants;
import com.dchealth.cro.medical.domain.PatientRecordFiles;
import com.dchealth.cro.medical.form.PatientRecordFilesForm;
import com.dchealth.cro.medical.mapper.PatientRecordFilesMapper;
import com.dchealth.cro.question.domain.CmsArticleSetEvaluation;
import com.dchealth.cro.question.mapper.CmsArticleSetEvaluationMapper;
import com.dchealth.cro.research.domain.ResearchInfoArticle;
import com.dchealth.cro.research.domain.ResearchInfoArticlePatient;
import com.dchealth.cro.research.mapper.ResearchInfoArticleMapper;
import com.dchealth.cro.research.mapper.ResearchInfoArticlePatientMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 问卷相关
 */
@Service
public class MedicalExamService {
    @Autowired
    private FMedicalExamFromMapper fMedicalExamFromMapper;

    @Autowired
    private FMedicalExamSubjectMapper fMedicalExamSubjectMapper;

    @Autowired
    private FMedicalExamItemMapper fMedicalExamItemMapper;

    @Autowired
    private DynamicSql dynamicSql;

    @Autowired
    private DynamicCommonMapper dynamicCommonMapper;

    @Autowired
    private PatientExamRecordMapper patientExamRecordMapper;

    @Autowired
    private PatientRecordFilesMapper patientRecordFilesMapper;

    @Autowired
    private CmsArticleMapper cmsArticleMapper;

    @Autowired
    private ResearchInfoArticlePatientMapper infoArticlePatientMapper;

    @Autowired
    private ResearchInfoArticleMapper researchInfoArticleMapper;
    @Autowired
    private CmsArticleSetEvaluationMapper cmsArticleSetEvaluationMapper;

    private static final Logger log = LoggerFactory.getLogger(MedicalExamService.class);
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 查询问卷详情
     *
     * @param fMedicalExamRes
     * @return
     */
    public FMedicalExamForm selectExamFromDetailInfo(FMedicalExamRes fMedicalExamRes) {
        Long articleId = fMedicalExamRes.getArticleId();
        String status = fMedicalExamRes.getStatus();
        Long examRecordId = fMedicalExamRes.getExamRecordId();
        //查询问卷模板信息
        Optional<FMedicalExamForm> examForm =
                Optional.ofNullable(fMedicalExamFromMapper.selectFMedicalExamFormByArticleId(articleId));
        if (examForm.isPresent()) {
            FMedicalExamForm fMedicalExamForm = examForm.get();
            Long medicalFormId = fMedicalExamForm.getMedicalFormId();
            //查询问卷题干信息
            Optional<List<FMedicalExamSubject>> fMedicalExamSubjects =
                    Optional.ofNullable(fMedicalExamSubjectMapper.selectFMedicalExamSubjectByFromId(medicalFormId));
            if (fMedicalExamSubjects.isPresent()) {
                List<FMedicalExamSubject> examSubjects = fMedicalExamSubjects.get();
                examSubjects.forEach(subject -> {
                    //查询问卷选项信息
                    List<FMedicalExamItem> items =
                            fMedicalExamItemMapper.selectFMedicalExamItemByProId(subject.getId());
                    subject.setItemList(items);
                    //查询问卷选项值信息
                    if (MedicalContants.EXAM_STATUS_1.equals(status) || MedicalContants.EXAM_STATUS_2.equals(status)) {
                        //题目类型是图片、文件
                        if (PropertyType.eq(PropertyType.UPLOAD, subject.getPropertyType())) {
                            List<PatientRecordFiles> recordFiles =
                                    patientRecordFilesMapper.selectPatientRecordFiles(subject.getId(),
                                            fMedicalExamRes.getExamRecordId());
                            List<String> imageUrls =
                                    recordFiles.stream().map(PatientRecordFiles::getImageUrl).collect(Collectors.toList());
                            subject.setImageUrls(imageUrls);
                            subject.setPropertyValue("");
                        } else {
                            //题目类型是单选、多选、文本等
                            PatientExamRecord patientExamRecord =
                                    patientExamRecordMapper.selectPatientExamRecordById(examRecordId);
                            String sql = dynamicSql.buildSelectArticleByTableName(patientExamRecord.getExamTable());
                            DynamicForm dynamicForm = new DynamicForm();
                            dynamicForm.setArticleId(articleId);
                            dynamicForm.setExamRecordId(examRecordId);
                            dynamicForm.setSql(sql);
                            List<DynamicCommonProperty> properties = dynamicCommonMapper.selectBySql(dynamicForm);
                            if ("form".equalsIgnoreCase(subject.getPropertyType())) {
                                String attribute = subject.getAttribute();
                                JSONObject jsonObject1 = JSON.parseObject(attribute);
                                List<JSONObject> children = (List<JSONObject>) jsonObject1.get("children");
                                List<PatientRecordFiles> recordFiles = patientRecordFilesMapper.selectPatientRecordFiles(null, examRecordId);
                                log.info("查询出来问题组图片文件：" + JSON.toJSONString(recordFiles));
                                Map<String, PatientRecordFiles> recordFilesByProCode = recordFiles.stream().filter(item -> item.getPropertyCode() != null)
                                        .collect(Collectors.toMap(PatientRecordFiles::getPropertyCode, item -> item));
                                for (JSONObject jsonObject : children) {
                                    //问题组单选、多选、文本等
                                    for (DynamicCommonProperty property : properties) {
                                        if (jsonObject.get("propertyCode").equals(property.getPropertyCode())) {
                                            jsonObject.put("propertyValue", property.getPropertyValue());
                                            jsonObject.put("other", property.getOther());
                                        }
                                    }
                                    //问题组图片处理
                                    if ((!recordFilesByProCode.isEmpty()) && recordFilesByProCode.keySet().contains(jsonObject.get("propertyCode"))) {
                                        PatientRecordFiles recordFile = recordFilesByProCode.get(jsonObject.get("propertyCode"));
                                        jsonObject.put("propertyValue", recordFile.getImageUrl());
                                    }
                                }
                                subject.setAttribute(JSON.toJSONString(jsonObject1));
                            } else {
                                if (properties != null && !properties.isEmpty()) {
//                                Map<Long, List<DynamicCommonProperty>> collect = properties.stream().collect
//                                (Collectors.groupingBy(DynamicCommonProperty::getPropertyId));
                                    try {
                                        Map<Long, DynamicCommonProperty> collect =
                                                properties.stream().filter(x -> !Objects.isNull(x.getPropertyId())).collect(Collectors.toMap(DynamicCommonProperty::getPropertyId, item -> item));
                                        if (collect != null && !collect.isEmpty() && collect.containsKey(subject.getId())) {
                                            DynamicCommonProperty commonProperties = collect.get(subject.getId());
                                            subject.setPropertyValue(commonProperties.getPropertyValue());
                                            subject.setOther(commonProperties.getOther());
                                        }
                                    } catch (Exception e) {
                                        for (DynamicCommonProperty property : properties) {
                                            if (subject.getPropertyCode().equals(property.getPropertyCode())) {
                                                subject.setPropertyValue(property.getPropertyValue());
                                                subject.setOther(property.getOther());
                                            }
                                        }
                                    }


                                }
                                if (PropertyType.eq(PropertyType.CHECKBOX, subject.getPropertyType())) {
                                    if (subject.getOther() == null) subject.setOther("");
                                }
                            }

                        }
                    }
                });
                //分数统计
                if (MedicalContants.EXAM_STATUS_2.equals(status)) {
                    ResearchInfoArticlePatient articlePatient = new ResearchInfoArticlePatient();
                    articlePatient.setExamRecordId(examRecordId);
                    articlePatient.setArticleId(articleId);
                    ResearchInfoArticlePatient riap =
                            infoArticlePatientMapper.selectResearchInfoArticlePatient(articlePatient);
                    if (riap != null && StringUtils.isNotBlank(riap.getScores())) {
                        fMedicalExamForm.setScores(Long.valueOf(riap.getScores()));
                        fMedicalExamForm.setEvaluation(riap.getEvaluationResult());
                    }
                    //题干分数
//                    long subjectScores = examSubjects.stream()
//                            .filter(subject -> subject.getSubjectScores() != null && "1".equals(subject.getIfScore
//                            ()) && (subject.getPropertyValue() != null || subject.getImageUrls() != null))
//                            .mapToLong(FMedicalExamSubject::getSubjectScores).sum();
//                    //选项分数
//                    //单选、多选、下拉框、级联分数
//                    long radioScores = 0;
//                   // AtomicLong radioScores = new AtomicLong();
//                    List<FMedicalExamSubject> subjects = examSubjects.stream().filter(subject -> (PropertyType
//                    .RADIO.equals(subject.getPropertyType())
//                            || PropertyType.CHECKBOX.equals(subject.getPropertyType())
//                            || PropertyType.SELECT.equals(subject.getPropertyType())
//                            || PropertyType.CASCADER.equals(subject.getPropertyType()))
//                            && "1".equals(subject.getIfScore()) && subject.getPropertyValue() != null).collect
//                            (Collectors.toList());
//                    for (FMedicalExamSubject subject : subjects) {
//                        //选项信息
//                        List<FMedicalExamItem> items = fMedicalExamItemMapper.selectFMedicalExamItemByProId(subject
//                        .getId());
//                        //题所选答案
//                        List<String> propertyValues = Arrays.stream(subject.getPropertyValue().split(",")).collect
//                        (Collectors.toList());
//                        long scores = items.stream().filter(item -> propertyValues.contains(item.getItemValue()))
//                        .mapToLong(FMedicalExamItem::getItemScores).sum();
//                        radioScores += scores;
//                    }
//                    fMedicalExamForm.setScores(subjectScores + radioScores);
//
//
//                    CmsArticleSetEvaluation cmsArticleSetEvaluation = cmsArticleSetEvaluationMapper
//                    .selectCmsArticleSetEvaluationByCmsArticleId(articleId,fMedicalExamForm.getScores());
//                      if(cmsArticleSetEvaluation != null){
//                          fMedicalExamForm.setEvaluation(cmsArticleSetEvaluation.getEvaluationResult());
//                      }
                }
                for (FMedicalExamSubject examSubject : examSubjects) {
                    if ("form".equalsIgnoreCase(examSubject.getPropertyType())) {
                        String attribute = examSubject.getAttribute();
                        JSONObject jsonObject = JSON.parseObject(attribute);
                        List<JSONObject> children = (List<JSONObject>) jsonObject.get("children");
                        for (JSONObject child : children) {
                            Object o = child.get("propertyValue");
                            if (Objects.isNull(o)) {
                                child.put("propertyValue", "");
                            }
                        }
                        jsonObject.put("children", children);
                        examSubject.setAttribute(JSON.toJSONString(jsonObject));
                    } else {
                        if (StringUtils.isEmpty(examSubject.getPropertyValue())) examSubject.setPropertyValue("");
                    }
                }
//                log.info("问卷详情：" + JSON.toJSONString(examSubjects));
                fMedicalExamForm.setSubjectList(examSubjects);
                //"再来一次"按钮是否显示处理
                handleIsShowAgain(articleId, fMedicalExamRes.getAccountId(), fMedicalExamForm);
            }
            return fMedicalExamForm;
        }
        return null;
    }

    private void handleIsShowAgain(Long articleId, Long accountId, FMedicalExamForm fMedicalExamForm) {
        // 该患者在哪几个队列中能看到该问卷，只要有一个队列能看到该问卷，“再来一次”按钮就显示
        // 至于当后台有队列删该问卷，App上就应该把删除该问卷的队列对应的问卷不展示，这个 别的逻辑 会处理。
        if (!Objects.isNull(accountId)) {
            List<ResearchInfoArticle> researchInfoArticleList =
                    researchInfoArticleMapper.queryByArticleIdAndAccountId(articleId, accountId);
            if (!CollectionUtils.isEmpty(researchInfoArticleList) && researchInfoArticleList.size() > 0) {
                fMedicalExamForm.setIsShowAgain(true);
            } else {
                fMedicalExamForm.setIsShowAgain(false);
            }
        }

    }


    /**
     * 新保存问卷结果逻辑方法
     *
     * @param examForm
     * @return
     */
    @Transactional
    public DchealthCloudResponseEntity<CommonResult> newSaveOrUpdateExamFrom2(FMedicalExamForm examForm) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<>();
        List<FMedicalExamSubject> subjectList = examForm.getSubjectList();
        if (CollectionUtils.isEmpty(subjectList)) {
            result.setCode(CroApiCodeConstant.PATIENT_RECORD_DATA_NULL.getCode());
            return result;
        }
        Long examRecordId = null;
        // 历史原因，故意写死
        examForm.setResearchId(examForm.getResearchId() == null ? 221l : Long.valueOf(examForm.getResearchId()));
        //获取表名称
        CmsArticle cmsArticle = cmsArticleMapper.selectCmsArticleByArticleId(examForm.getArticleId());
        int i = Math.floorDiv(cmsArticle.getQuesNum(), 10);
        String examTable = "res_article_" + i;
        //提交逻辑
        if (StringUtils.equals(examForm.getStatus(), "2")) {
            examRecordId = newDealSubmitQuestionInfo(examForm, examTable);
        }
        //保存逻辑
        if (StringUtils.equals(examForm.getStatus(), "1")) {
            examRecordId = dealSaveQuestionInfo(examForm, examTable);
        }
        if (Objects.isNull(examForm.getExamRecordId())) {
            //图片和问卷填写文本内容处理逻辑
            handleImagesAndExamSubjectList(examForm, examRecordId, examTable);
        } else {
            //图片和问卷填写文本内容处理逻辑
            handleImagesAndExamSubjectList(examForm, examForm.getExamRecordId(), examTable);
        }

        CommonResult commonResult = new CommonResult();
        commonResult.setResult(true);
        result.setData(commonResult);
        return result;
    }


    @Transactional
    public DchealthCloudResponseEntity<CommonResult> saveOrUpdateExamFrom2(FMedicalExamForm examForm) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<>();
        List<FMedicalExamSubject> subjectList = examForm.getSubjectList();
        if (CollectionUtils.isEmpty(subjectList)) {
            result.setCode(CroApiCodeConstant.PATIENT_RECORD_DATA_NULL.getCode());
            return result;
        }
        Long examRecordId = null;
        // 历史原因，故意写死
        examForm.setResearchId(Long.valueOf("216"));
        //获取表名称
        CmsArticle cmsArticle = cmsArticleMapper.selectCmsArticleByArticleId(examForm.getArticleId());
        int i = Math.floorDiv(cmsArticle.getQuesNum(), 10);
        String examTable = "res_article_" + i;
        //提交逻辑
        if (StringUtils.equals(examForm.getStatus(), "2")) {
            examRecordId = dealSubmitQuestionInfo(examForm, examTable);
        }
        //保存逻辑
        if (StringUtils.equals(examForm.getStatus(), "1")) {
            examRecordId = dealSaveQuestionInfo(examForm, examTable);
        }
        if (Objects.isNull(examForm.getExamRecordId())) {
            //图片和问卷填写文本内容处理逻辑
            handleImagesAndExamSubjectList(examForm, examRecordId, examTable);
        } else {
            //图片和问卷填写文本内容处理逻辑
            handleImagesAndExamSubjectList(examForm, examForm.getExamRecordId(), examTable);
        }
        CommonResult commonResult = new CommonResult();
        commonResult.setResult(true);
        result.setData(commonResult);
        return result;
    }

    private void dealScoresInfo(FMedicalExamForm examForm) {
        List<FMedicalExamSubject> subjectList = examForm.getSubjectList();
        if (!subjectList.isEmpty()) {
            //查询设置的选项分数
            FMedicalExamItem fMedicalExamItem = new FMedicalExamItem();
            fMedicalExamItem.setMedicalFormId(examForm.getMedicalFormId());
            List<FMedicalExamItem> examItemList = fMedicalExamItemMapper.selectFMedicalExamItemList(fMedicalExamItem);
            AtomicReference<Long> scores = new AtomicReference<>(0L);
            subjectList.forEach(subject -> {
                //题目类型判断--是否图片、文件
                if (!PropertyType.eq(PropertyType.UPLOAD, subject.getPropertyType())) {
                    if ("Checkbox".equalsIgnoreCase(subject.getPropertyType()) && StringUtils.isNotBlank(subject.getPropertyValue())) {
                        String[] values = subject.getPropertyValue().split(",");
                        //循环处理每个选项的得分
                        for (String val : values) {
                            for (FMedicalExamItem examItem : examItemList) {
                                if (subject.getId().equals(examItem.getPropertyId()) && val.equals(examItem.getItemValue()) && examItem.getItemScores() != null) {
                                    //如果是同一个属性，且有分值
                                    scores.updateAndGet(v -> v + examItem.getItemScores());
                                }
                            }
                        }
                    } else if ("Radio".equalsIgnoreCase(subject.getPropertyType())) {
                        for (FMedicalExamItem examItem : examItemList) {
                            if (subject.getId() != null && subject.getId().equals(examItem.getPropertyId()) && subject.getPropertyValue() != null && subject.getPropertyValue().equals(examItem.getItemValue()) && examItem.getItemScores() != null) {
                                //如果是同一个属性，且有分值
                                scores.updateAndGet(v -> v + examItem.getItemScores());
                            }
                        }
                    }
                }
                if (PropertyType.eq(PropertyType.FORM, subject.getPropertyType())) {
                    try {
                        // 获取原始的 attribute 字符串
                        String attributeJson = subject.getAttribute();

                        if (attributeJson == null || attributeJson.trim().isEmpty()) {
                            log.warn("attribute 为空");
                            return;
                        }

                        // 处理可能的引号包裹和转义字符
                        attributeJson = attributeJson.trim();
                        if (attributeJson.startsWith("\"") && attributeJson.endsWith("\"")) {
                            attributeJson = attributeJson.substring(1, attributeJson.length() - 1);
                            // 处理转义的双引号
                            attributeJson = attributeJson.replace("\\\"", "\"");
                        }
                        Gson gson = new Gson();
                        ProblemFromVo problemFromVo = gson.fromJson(attributeJson, ProblemFromVo.class);
                        List<FormItemVo> children = problemFromVo.getChildren();
                        children.forEach(formItem -> {
//                            if ("Checkbox".equalsIgnoreCase(formItem.getPropertyType())) {

                            List<ProblemOptionVo> options = formItem.getOptions();
                            options.forEach(option -> {
                                if (formItem.getPropertyValue().equals(option.getValue()) && option.getItemScores() != null) {
                                    scores.updateAndGet(v -> v + option.getItemScores());
                                }
                            });

//                            } else if ("Radio".equalsIgnoreCase(formItem.getPropertyType())) {
//                                List<ProblemOptionVo> options = formItem.getOptions();
//                                options.forEach(option -> {
//                                    if (formItem.getPropertyValue().equals(option.getValue()) && option.getItemScores() != null) {
//                                        scores.updateAndGet(v -> v + option.getItemScores());
//                                    }
//                                });
//                            }
                        });


                    } catch (Exception e) {
                        log.error("doAfterReturing obj to json exception obj={},msg={}", subject, e.getMessage());
                    }
                }
            });
            examForm.setScores(scores.get());
            //计算评价结果
            CmsArticleSetEvaluation cmsArticleSetEvaluation = new CmsArticleSetEvaluation();
            cmsArticleSetEvaluation.setCmsArticleId(examForm.getArticleId());
            List<CmsArticleSetEvaluation> cmsArticleSetEvaluations =
                    cmsArticleSetEvaluationMapper.selectCmsArticleSetEvaluationList(cmsArticleSetEvaluation);
            //最高档评价结果，处理极端情况
            CmsArticleSetEvaluation setEvaluationMax = null;
            for (CmsArticleSetEvaluation setEvaluation : cmsArticleSetEvaluations) {
                if (examForm.getScores() >= setEvaluation.getMinValue() && examForm.getScores() < setEvaluation.getMaxValue()) {
                    examForm.setEvaluation(setEvaluation.getEvaluationResult());
                    break;
                }
                if (setEvaluationMax == null) {
                    setEvaluationMax = new CmsArticleSetEvaluation();
                    setEvaluationMax.setMaxValue(setEvaluation.getMaxValue());
                    setEvaluationMax.setEvaluationResult(setEvaluation.getEvaluationResult());
                } else {
                    if (setEvaluation.getMaxValue() > setEvaluationMax.getMaxValue()) {
                        setEvaluationMax.setMaxValue(setEvaluation.getMaxValue());
                        setEvaluationMax.setEvaluationResult(setEvaluation.getEvaluationResult());
                    }
                }
                if (examForm.getScores() >= setEvaluationMax.getMaxValue()) {
                    examForm.setEvaluation(setEvaluationMax.getEvaluationResult());
                }

            }

        }
    }

    /**
     * 新问卷提交逻辑，计算了分数以及评价结果
     *
     * @param examForm
     * @param examTable
     * @return
     */
    private Long newDealSubmitQuestionInfo(FMedicalExamForm examForm, String examTable) {
        //保存提交记录前计算分数等信息
        dealScoresInfo(examForm);
        //第一次提交
        if (!examForm.getFillAgain()) {
            // 之前没有保存过，就新增
            if (Objects.isNull(examForm.getExamRecordId())) {
                Long newExamRecordId = insertPatientExaMAndGetId(examForm, examTable);
                insertResearchInfoArticlePatient(examForm, newExamRecordId);
                return newExamRecordId;
            }
            //更新
            else {
                updatePatientExamRecord(examForm);
                updateResearchInfoArticlePatient(examForm);
            }
        }
        // 再来一次：提交
        else {
            // 之前没有保存过
            if (Objects.isNull(examForm.getExamRecordId())) {
                Long newExamRecordId = insertPatientExaMAndGetId(examForm, examTable);
                insertResearchInfoArticlePatient(examForm, newExamRecordId);
                return newExamRecordId;
            }
            //已经保存过了
            else {
                updatePatientExamRecord(examForm);
                updateResearchInfoArticlePatient(examForm);
            }
        }
        return null;
    }

    private Long dealSaveQuestionInfo(FMedicalExamForm examForm, String examTable) {
        // 旧的保存
        if (!examForm.getIsFillAgain()) {
            //第1次保存
            if (Objects.isNull(examForm.getExamRecordId())) {
                Long newExamRecordId = insertPatientExaMAndGetId(examForm, examTable);
                insertResearchInfoArticlePatient(examForm, newExamRecordId);
                return newExamRecordId;
            }
            //第2+次保存
            else {
                //数据相同不用再更新
            }
        }
        //再来一次的保存
        else {
            //第一次保存
            if (Objects.isNull(examForm.getExamRecordId()) && examForm.getIsFillAgain()) {
                Long newExamRecordId = insertPatientExaMAndGetId(examForm, examTable);
                insertResearchInfoArticlePatient(examForm, newExamRecordId);
                return newExamRecordId;
            }
            //第N次保存
            if (!Objects.isNull(examForm.getExamRecordId()) && examForm.getIsFillAgain()) {
                // 字段都一样，不用再更新
            }
        }
        return null;
    }

    private Long dealSubmitQuestionInfo(FMedicalExamForm examForm, String examTable) {
        //第一次提交
        if (!examForm.getFillAgain()) {
            // 之前没有保存过，就新增
            if (Objects.isNull(examForm.getExamRecordId())) {
                Long newExamRecordId = insertPatientExaMAndGetId(examForm, examTable);
                insertResearchInfoArticlePatient(examForm, newExamRecordId);
                return newExamRecordId;
            }
            // 之前没有保存过，就更新
            else {
                updatePatientExamRecord(examForm);
                updateResearchInfoArticlePatient(examForm);
            }
        }
        // 再来一次：提交
        else {
            // 之前没有保存过
            if (Objects.isNull(examForm.getExamRecordId())) {
                Long newExamRecordId = insertPatientExaMAndGetId(examForm, examTable);
                insertResearchInfoArticlePatient(examForm, newExamRecordId);
                return newExamRecordId;
            }
            //已经保存过了
            else {
                updatePatientExamRecord(examForm);
                updateResearchInfoArticlePatient(examForm);
            }
        }
        return null;
    }

    private void insertResearchInfoArticlePatient(FMedicalExamForm examForm, Long newExamRecordId) {
        ResearchInfoArticlePatient articlePatient = new ResearchInfoArticlePatient();
        articlePatient.setArticleId(examForm.getArticleId());
        articlePatient.setAccountId(examForm.getPatientId());
        articlePatient.setResearchId(examForm.getResearchId());
        articlePatient.setExamRecordId(newExamRecordId);
        articlePatient.setFillStatus(examForm.getStatus());
        articlePatient.setIsFillAgain(examForm.getIsFillAgain());
        if (examForm.getScores() != null) {
            articlePatient.setScores(String.valueOf(examForm.getScores()));
            articlePatient.setEvaluationResult(examForm.getEvaluation());
        }
        infoArticlePatientMapper.insertResearchInfoArticlePatient(articlePatient);
    }

    private void updateResearchInfoArticlePatient(FMedicalExamForm examForm) {
        ResearchInfoArticlePatient articlePatient = new ResearchInfoArticlePatient();
        articlePatient.setExamRecordId(examForm.getExamRecordId());
        articlePatient.setFillStatus(examForm.getStatus());
        articlePatient.setIsFillAgain(examForm.getIsFillAgain());
        articlePatient.setScores(String.valueOf(examForm.getScores()));
        articlePatient.setEvaluationResult(examForm.getEvaluation());
        infoArticlePatientMapper.updateResearchInfoArticlePatientByExamRecordId(articlePatient);
    }

    private Long insertPatientExaMAndGetId(FMedicalExamForm examForm, String examTable) {
        PatientExamRecord examRecord = new PatientExamRecord();
        examRecord.setArticleId(examForm.getArticleId());
        examRecord.setMedicalFormId(examForm.getMedicalFormId());
        examRecord.setPatientId(examForm.getPatientId());

        examRecord.setExamTable(examTable);
        examRecord.setCreateTime(DateUtils.getNowDate());
        examRecord.setResearchId(examForm.getResearchId());
        // 患者的问卷填报关系
        examRecord.setFillStatus(examForm.getStatus());
        patientExamRecordMapper.insertPatientExamRecord(examRecord);
        return examRecord.getId();
    }

    private void updatePatientExamRecord(FMedicalExamForm examForm) {
        PatientExamRecord examRecord = new PatientExamRecord();
        examRecord.setId(examForm.getExamRecordId());
        // 患者的问卷填报关系
        examRecord.setFillStatus(examForm.getStatus());
        patientExamRecordMapper.updatePatientExamRecord(examRecord);
    }


    private void handleImagesAndExamSubjectList(FMedicalExamForm examForm, Long examRecordId, String examTable) {
        examForm.setExamRecordId(examRecordId);
        List<FMedicalExamSubject> subjectList = examForm.getSubjectList();
        List<FMedicalExamSubject> examSubjects = new ArrayList<>();
        subjectList.forEach(subject -> {

            if (PropertyType.eq(PropertyType.FORM, subject.getPropertyType())) {
                JSONObject jsonObject = JSON.parseObject(subject.getAttribute());
                Object o = jsonObject.get("children");
                if (o instanceof List) {
                    List<JSONObject> jsonObjects = (List<JSONObject>) o;
                    for (JSONObject json : jsonObjects) {
                        FMedicalExamSubject fMedicalExamSubject = new FMedicalExamSubject();
                        BeanUtils.copyProperties(json, fMedicalExamSubject);
                        FMedicalExamSubject user = JSON.parseObject(json.toString(), FMedicalExamSubject.class);
                        user.setMedicalFormId(subject.getMedicalFormId());
                        user.setId(subject.getId());
                        //题目类型判断--是否图片、文件
                        if (PropertyType.eq(PropertyType.UPLOAD, user.getPropertyType())) {
                            if (Objects.nonNull(examForm) && Objects.nonNull(examForm.getImageUrls())) {
                                List<PatientRecordFilesForm> list =
                                        examForm.getImageUrls().stream().filter(x -> Objects.equals(x.getId(), user.getId())).collect(Collectors.toList());
                                if (!CollectionUtils.isEmpty(list)) {
                                    List<String> imageUrls = list.get(0).getImageUrl();
                                    if (!CollectionUtils.isEmpty(imageUrls)) {
                                        handleImages(examForm, subject, imageUrls);
                                    }
                                }
                            }
                        } else {
                            examSubjects.add(user);
                        }
                    }
                }
            } else {
                //题目类型判断--是否图片、文件
                if (PropertyType.eq(PropertyType.UPLOAD, subject.getPropertyType())) {
                    if (Objects.nonNull(examForm) && Objects.nonNull(examForm.getImageUrls())) {
                        List<PatientRecordFilesForm> list =
                                examForm.getImageUrls().stream().filter(x -> Objects.equals(x.getId(), subject.getId())).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(list)) {
                            List<String> imageUrls = list.get(0).getImageUrl();
                            if (!CollectionUtils.isEmpty(imageUrls)) {
                                handleImages(examForm, subject, imageUrls);
                            }
                        }
                    }
                } else {
                    examSubjects.add(subject);
                }
            }
        });
        if (!examSubjects.isEmpty()) {
            // 先删除，后增加
            PatientExamRecord patientExamRecord = patientExamRecordMapper.selectPatientExamRecordById(examRecordId);
            examTable = patientExamRecord.getExamTable();
            String sql = dynamicSql.buildDeleteArticle(patientExamRecord.getExamTable(), examForm.getArticleId(),
                    examRecordId);
            dynamicCommonMapper.executeDeleteSql(sql);

            //重新储存值
            DynamicForm dynamicForm = new DynamicForm();
            dynamicForm.setPatientId(examForm.getPatientId());
            dynamicForm.setArticleId(examForm.getArticleId());
            dynamicForm.setFollowId(examForm.getFollowId());
            dynamicForm.setResearchId(examForm.getResearchId());
            dynamicForm.setExamRecordId(examRecordId);
            dynamicForm.setExamTable(examTable);
            String sql2 = dynamicSql.buildInsertArticleSql(dynamicForm, examSubjects);
            dynamicCommonMapper.executeInsertSql(sql2);
        }
    }

    private void handleImages(FMedicalExamForm examForm, FMedicalExamSubject subject, List<String> imageUrls) {
        //删除原文件
        patientRecordFilesMapper.deletePatientRecordFiles(subject.getId(), examForm.getExamRecordId());
        List<PatientRecordFiles> patientRecordFilesList = new ArrayList<>();
        imageUrls.forEach(imageUrl -> {
            PatientRecordFiles patientRecordFiles = new PatientRecordFiles();
            patientRecordFiles.setPatientId(examForm.getPatientId());
            patientRecordFiles.setMedicalFormId(examForm.getMedicalFormId());
            patientRecordFiles.setPropertyId(subject.getId());
            patientRecordFiles.setPropertyCode(subject.getPropertyCode());
            patientRecordFiles.setExamRecordId(examForm.getExamRecordId());
            patientRecordFiles.setCreateTime(DateUtils.getNowDate());
            patientRecordFiles.setImageUrl(imageUrl);
            patientRecordFilesList.add(patientRecordFiles);
        });
        patientRecordFilesMapper.batchInsertPatientRecordFiles(patientRecordFilesList);
    }

}
