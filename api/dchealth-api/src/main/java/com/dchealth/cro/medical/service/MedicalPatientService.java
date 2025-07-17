package com.dchealth.cro.medical.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.cro.account.constant.BaseConstant;
import com.dchealth.cro.account.domain.AccountNotice;
import com.dchealth.cro.account.domain.PatientAccount;
import com.dchealth.cro.account.domain.SysUser;
import com.dchealth.cro.account.mapper.AccountNoticeMapper;
import com.dchealth.cro.account.mapper.PatientAccountMapper;
import com.dchealth.cro.account.mapper.SysUserMapper;
import com.dchealth.cro.account.vo.PatientAccountInfoVO;
import com.dchealth.cro.common.base.CommonResult;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.cache.RedisPublishService;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.common.utils.XfServiceHttpClientUtil;
import com.dchealth.cro.common.utils.bean.BeanUtils;
import com.dchealth.cro.core.config.CroCoreConfig;
import com.dchealth.cro.enums.FollowTipStatusEnum;
import com.dchealth.cro.form.common.DynamicSql;
import com.dchealth.cro.form.constant.PropertyType;
import com.dchealth.cro.form.domain.DynamicCommonProperty;
import com.dchealth.cro.form.domain.FMedicalForm;
import com.dchealth.cro.form.domain.form.DynamicForm;
import com.dchealth.cro.form.domain.form.FMedicalPropertyForm;
import com.dchealth.cro.form.mapper.DynamicCommonMapper;
import com.dchealth.cro.form.mapper.FMedicalFormMapper;
import com.dchealth.cro.form.service.MedicalFormService;
import com.dchealth.cro.form.vo.MedicalFormVO;
import com.dchealth.cro.form.vo.MedicalPropertyVO;
import com.dchealth.cro.health.domain.MpPatientAccount;
import com.dchealth.cro.health.domain.MpPerson;
import com.dchealth.cro.health.mapper.MpPatientAccountMapper;
import com.dchealth.cro.health.mapper.MpPersonMapper;
import com.dchealth.cro.health.utils.HealthUtils;
import com.dchealth.cro.his.domain.PatMasterIndex;
import com.dchealth.cro.his.mapper.PatMasterIndexMapper;
import com.dchealth.cro.medical.domain.*;
import com.dchealth.cro.medical.dto.DiseaseResearchPatientDto;
import com.dchealth.cro.medical.form.*;
import com.dchealth.cro.medical.mapper.*;
import com.dchealth.cro.medical.vo.PatientRecordListVo;
import com.dchealth.cro.medical.vo.xunfei.*;
import com.dchealth.cro.question.domain.QuestionQuick;
import com.dchealth.cro.question.domain.QuestionRecord;
import com.dchealth.cro.question.mapper.QuestionQuickMapper;
import com.dchealth.cro.question.mapper.QuestionRecordMapper;
import com.dchealth.cro.question.vo.QuestionRecordVO;
import com.dchealth.cro.research.domain.*;
import com.dchealth.cro.research.mapper.ResearchInfoMapper;
import com.dchealth.cro.research.mapper.ResearchOutboundMapper;
import com.dchealth.cro.research.mapper.ResearchPatientMapper;
import com.dchealth.cro.research.mapper.ResearchUserRoleMapper;
import com.dchealth.cro.warn.domain.WarnEarlyRecord;
import com.dchealth.cro.warn.domain.WarnLeaveMessage;
import com.dchealth.cro.warn.domain.WarnMedicalEarlyCondition;
import com.dchealth.cro.warn.mapper.WarnEarlyRecordMapper;
import com.dchealth.cro.warn.mapper.WarnLeaveMessageMapper;
import com.dchealth.cro.warn.mapper.WarnMedicalEarlyConditionMapper;
import com.dchealth.cro.warn.vo.OptionsVo;
import com.dchealth.cro.warn.vo.WarnConditionValueVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MedicalPatientService {

    private static final Logger L = LoggerFactory.getLogger(MedicalPatientService.class);
    @Autowired
    private MedicalFormService medicalFormService;
    @Autowired
    private MpPersonMapper mpPersonMapper;
    @Autowired
    private PatientAccountMapper patientAccountMapper;
    @Autowired
    private PatientInfoMapper patientInfoMapper;
    @Autowired
    private ResearchInfoMapper researchInfoMapper;
    @Autowired
    private PatientRecordMapper patientRecordMapper;
    @Autowired
    private DynamicCommonMapper dynamicCommonMapper;
    @Autowired
    private DynamicSql dynamicSql;
    @Autowired
    private PatientRecordFilesMapper patientRecordFilesMapper;
    @Autowired
    private Executor executor;
    @Autowired
    private QuestionRecordMapper questionRecordMapper;
    @Autowired
    private QuestionQuickMapper questionQuickMapper;
    @Autowired
    private WarnMedicalEarlyConditionMapper warnMedicalEarlyConditionMapper;
    @Autowired
    private WarnEarlyRecordMapper warnEarlyRecordMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private WarnLeaveMessageMapper warnLeaveMessageMapper;
    @Autowired
    private PatientRecordTemplateMapper patientRecordTemplateMapper;

    @Autowired
    private WarnDoctorPatientMapper warnDoctorPatientMapper;

    @Autowired
    private ResearchOutboundMapper researchOutboundMapper;
    @Autowired
    private PatientRecordOutboundMapper patientRecordOutboundMapper;

    @Autowired
    private PatMasterIndexMapper patMasterIndexMapper;

    @Autowired
    private AccountNoticeMapper accountNoticeMapper;

    @Autowired
    private ResearchUserRoleMapper researchUserRoleMapper;

    @Value("${xunfei.requestUrl}")
    private String baseUrl ;
    @Autowired
	private CroCoreConfig croCoreConfig;
    @Autowired
    private MpPatientAccountMapper mpPatientAccountMapper;

    @Autowired
    private ResearchPatientMapper researchPatientMapper;

    @Resource
    private RedisPublishService redisPublishService;

    @Resource
    private FMedicalFormMapper fMedicalFormMapper;

    @Value("${image.oldDomain}")
    private String oldDomain;

    @Value("${image.newDomain}")
    private String newDomain;



    /**
     * 调试动态表单使用
     *
     * @param followId
     * @return
     */
    public List<MedicalFormVO> testFrom(Long followId) {
        return medicalFormService.followForms(followId, null, null, (formVOS, propertyVOS, propertyTypes) -> {

        });
    }

    /**
     * 保存或更新患者档案
     *
     * @param infoForm
     * @return
     */
    @Transactional
    public DchealthCloudResponseEntity<CommonResult> saveOrUpdatePatientRecord(PatientRecordSaveUpdateInfoForm infoForm) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CommonResult commonResult = new CommonResult();
        L.info("保存接口表单数据{}", infoForm);


        PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByRecordId(infoForm.getRecordId());
        if (patientRecord == null) {//档案不存在
            result.setCode(CroApiCodeConstant.PATIENT_RECORD_NOT_EXIT.getCode());
            return result;
        }
        PatientRecord record = new PatientRecord();
        //质疑处理  患者回复质疑只处理质疑的逻辑不修改提交的逻辑
        if(infoForm.getQuestionRecordForm() != null && !Objects.isNull(infoForm.getQuestionRecordForm().getQuestionId())) {
            handleRequestionRecordFormInfo(infoForm, record);
            record.setRecordId(patientRecord.getRecordId());
            record.setFollowTipStatus(FollowTipStatusEnum.FOLLOW_UP_VISIT_SUBMISSION_REMINDER.getIndex());
            patientRecordMapper.updatePatientRecord(record);
            commonResult.setResult(true);
            result.setData(commonResult);
            return result;
        }

        PatientInfo patientInfo = patientInfoMapper.selectPatientInfoById(patientRecord.getPatientId());
        patientRecord.setAccountId(patientInfo.getAccountId());


        // 属性值集合处理
        handlePropertyFormsInfo(infoForm, patientRecord);
        //上传文件[图片]
        handleFileImagesInfo(infoForm, patientRecord);

        // 随访记录
        record.setIsSubmit(infoForm.getIsSubmit());
        record.setName(patientRecord.getName());
        record.setFormComplentRate(infoForm.getFormComplentRate());
        handlePatientRecordInfo(patientRecord, record);

        // 修改患者队列随访状态
        handlePatientInfo(patientRecord, record);

        // 处理 patient_info 表省市县字段
        handleArea(patientRecord.getPatientId(), infoForm.getPropertyForms());

        //处理 patient_info  表就诊信息
        handleVisit(patientRecord.getPatientId(), infoForm.getPropertyForms());

        commonResult.setResult(true);
        result.setData(commonResult);
        return result;
    }

    private void handleVisit(Long patientId, List<FMedicalPropertyForm> propertyForms) {
        try {
            List<FMedicalPropertyForm> visitSourceValue = propertyForms.stream().filter(fMedicalPropertyForm -> fMedicalPropertyForm.getPropertyCode().contains("VISIT_SOURCE_VALUE"))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(visitSourceValue)){
                FMedicalPropertyForm fMedicalPropertyForm = visitSourceValue.get(0);
                String propertyValue = fMedicalPropertyForm.getPropertyValue();
                PatientInfo pi = new PatientInfo();
                pi.setPatientId(patientId);
                pi.setVisitType(propertyValue);
                patientInfoMapper.updatePatientInfo(pi);
            }
        } catch (Exception e) {
            L.error("handleVisit error is {}", e.getMessage());
        }

    }


    private void handleArea(Long patientId, List<FMedicalPropertyForm> propertyForms) {
        try {
            Map<String, FMedicalPropertyForm> collect = propertyForms.stream().collect(Collectors.toMap(
                    FMedicalPropertyForm::getPropertyType, Function.identity(), (oldvalue, newvalue) -> oldvalue));
            FMedicalPropertyForm fMedicalPropertyForm = collect.get("province");

            if (fMedicalPropertyForm != null && StringUtils.isNotEmpty(fMedicalPropertyForm.getPropertyValue())) {
                String propertyValue = fMedicalPropertyForm.getPropertyValue();
                JSONArray jsonArray = JSONArray.parse(propertyValue);
                String cityId = jsonArray.size() == 2 ? jsonArray.getString(0) : jsonArray.getString(1);

                PatientInfo pi = new PatientInfo();
                pi.setPatientId(patientId);
                pi.setProvinceId(Long.parseLong(jsonArray.getString(0)));
                pi.setCityId(Long.parseLong(cityId));
                pi.setAreaId(Long.parseLong(jsonArray.getString(jsonArray.size()-1)));
                patientInfoMapper.updatePatientInfo(pi);
            }
        } catch (Exception e) {
            L.error("handleArea error is {}", e.getMessage());
        }

    }

    private void handlePatientInfo(PatientRecord patientRecord, PatientRecord record){

        if (record.getIsSubmit() == 1) {
            String patientStatus = "1";
            if(!"基线".equals(patientRecord.getName())){
                patientStatus = "2";
            }
            PatientInfo pi = new PatientInfo();
            pi.setPatientId(patientRecord.getPatientId());
            pi.setPatientStatus(patientStatus);
            patientInfoMapper.updatePatientInfo(pi);
        }
    }

    private void handlePatientRecordInfo(PatientRecord patientRecord, PatientRecord record) {
        record.setRecordId(patientRecord.getRecordId());
        if ("0".equals(patientRecord.getFillStatus())) {
            record.setFillStatus("1");
            record.setFillTime(new Date());
        }
        record.setUpdateTime(new Date());
        record.setQuestionStatus("0");//回复后质疑状态  无预警
        if (record.getIsSubmit() == 1) {
            record.setFollowStatus("1");
            record.setFollowTipStatus(patientRecord.getFollowTipStatus());
            record = setFollowTipStatusInfo(record);
            record.setFollowSubmitTime(new Date());
            if(!"基线".equals(record.getName())){
                saveAccountNotice(patientRecord);
            } else if ("基线".equals(record.getName())) {
                record.setFollowStatus("3");
            }
        }
        if (record.getIsSubmit() == 0) {
            record.setFollowStatus("0");
        }
        patientRecordMapper.updatePatientRecord(record);
    }

    private void saveAccountNotice(PatientRecord patientRecord) {
        List<ResearchUserRole> researchUserRoleList = researchUserRoleMapper.findUserRoleByResearchId(patientRecord.getResearchId());
        if(researchUserRoleList.size()>0){
            String doctorMsgContent = "患者提交新的随访请尽快处理";
            for(ResearchUserRole r:researchUserRoleList){
                AccountNotice an = new AccountNotice(patientRecord,doctorMsgContent,"16",r.getUserId());
                accountNoticeMapper.insertAccountNotice(an);
            }
        }
    }

    private void handleRequestionRecordFormInfo(PatientRecordSaveUpdateInfoForm infoForm, PatientRecord record) {
        if (infoForm.getQuestionRecordForm() != null && !Objects.isNull(infoForm.getQuestionRecordForm().getQuestionId())) {
            QuestionRecord questionRecord = new QuestionRecord();
            questionRecord.setReplyContent(infoForm.getQuestionRecordForm().getReplyContent());
            questionRecord.setReplyTime(new Date());
            questionRecord.setQuestionId(infoForm.getQuestionRecordForm().getQuestionId());
            questionRecordMapper.updateQuestionRecord(questionRecord);
            record.setFollowStatus("1");
        }
    }

    private void handleFileImagesInfo(PatientRecordSaveUpdateInfoForm infoForm, PatientRecord patientRecord) {
        patientRecordFilesMapper.deletePatientRecordFilesByRecordId(patientRecord.getRecordId());
        List<PatientRecordFilesForm> patientRecordFilesList = infoForm.getImageUrls();
        if (CollectionUtils.isEmpty(patientRecordFilesList)) {
            return;
        }
        List<PatientRecordFiles> recordFiles = new ArrayList<>();
        for (PatientRecordFilesForm patientRecordFile : patientRecordFilesList) {
            Date d = new Date();
            patientRecordFile.getImageUrl().forEach(img -> {
                PatientRecordFiles recordFile = new PatientRecordFiles();
                recordFile.setMedicalFormId(patientRecordFile.getMedicalFormId());
                recordFile.setPropertyId(patientRecordFile.getPropertyId());
                recordFile.setPropertyCode(patientRecordFile.getPropertyCode());
                recordFile.setImageUrl(img);
                recordFile.setPatientId(patientRecord.getPatientId());
                recordFile.setRecordId(patientRecord.getRecordId());
                recordFile.setCreateTime(d);
                recordFile.setExamRecordId(null);
                recordFiles.add(recordFile);
            });
        }
        if (!CollectionUtils.isEmpty(recordFiles)) {
            patientRecordFilesMapper.batchInsertPatientRecordFiles(recordFiles);
        }

    }

    private void handlePropertyFormsInfo(PatientRecordSaveUpdateInfoForm infoForm, PatientRecord patientRecord) {
        if (infoForm.getPropertyForms() != null && !infoForm.getPropertyForms().isEmpty()) {
            DynamicForm dynamicForm = new DynamicForm();
            dynamicForm.setType("1");
            dynamicForm.setRecordId(infoForm.getRecordId());
            dynamicForm.setFollowId(patientRecord.getFollowId());
            dynamicForm.setPatientId(patientRecord.getPatientId());
            dynamicForm.setResearchId(patientRecord.getResearchId());
            Map<String, List<FMedicalPropertyForm>> dyMap =
                    infoForm.getPropertyForms().stream().collect(Collectors.groupingBy(FMedicalPropertyForm::getDynamic));
            Optional<List<FMedicalPropertyForm>> dyList = Optional.ofNullable(dyMap.get("1"));//动态表格数据
            if (dyList.isPresent()) {
                dynamicCommonMapper.executeDeleteSql(dynamicSql.buildDeleteDynamic(patientRecord.getResearchId(),
                        patientRecord.getRecordId()));
                String dynamicSqls = dynamicSql.buildBatchDynamicSql(dynamicForm, dyList.get());
                dynamicCommonMapper.executeInsertSql(dynamicSqls);
            }
            Optional<List<FMedicalPropertyForm>> otList = Optional.ofNullable(dyMap.get("0"));//其他数据

            // 大文本
            if (otList.isPresent()) {
                Optional<List<FMedicalPropertyForm>> ltList = otList.map(lt -> {
                    List<FMedicalPropertyForm> l =
                            lt.stream().filter(f -> PropertyType.largeText(f.getPropertyType())).collect(Collectors.toList());
                    return l.isEmpty() ? null : l;
                });

                // 小文本
                Optional<List<FMedicalPropertyForm>> tList = otList.map(t -> {
                    List<FMedicalPropertyForm> l =
                            t.stream().filter(f -> PropertyType.text(f.getPropertyType())).collect(Collectors.toList());
                    return l.isEmpty() ? null : l;
                });
                if (ltList.isPresent()) {
                    String delSql = dynamicSql.buildDeleteLargeText(patientRecord.getResearchId(),
                            patientRecord.getRecordId());
                    dynamicCommonMapper.executeDeleteSql(delSql);
                    String largeTextSql = dynamicSql.buildBatchLargeTextSql(dynamicForm, ltList.get());
                    dynamicCommonMapper.executeInsertSql(largeTextSql);
                }
                if (tList.isPresent()) {
                    dynamicCommonMapper.executeDeleteSql(dynamicSql.buildDeleteText(patientRecord.getResearchId(),
                            patientRecord.getRecordId()));
                    String textSql = dynamicSql.buildBatchTextSql(dynamicForm, tList.get());
                    dynamicCommonMapper.executeInsertSql(textSql);
                }

                List<FMedicalPropertyForm> fMedicalPropertyForms = otList.get();
                //预警逻辑处理，不符合预警配置规则的就会触发一条预警记录: warn_early_record 就会插入一条记录
                handleWarnsInfo(patientRecord, fMedicalPropertyForms, infoForm);
            }
        }
    }

    public PatientRecord setFollowTipStatusInfo(PatientRecord record) {
        if (!Objects.isNull(record.getFollowTipStatus()) && Objects.equals(record.getFollowTipStatus(),
                FollowTipStatusEnum.BASELINE_REMINDER.getIndex())) {
            // 基线节点，患者端在填写完基本的患者信息后，交互结束
            record.setFollowTipStatus(FollowTipStatusEnum.NO_STATUS.getIndex());
        }
        if (!Objects.isNull(record.getFollowTipStatus()) && Objects.equals(record.getFollowTipStatus(),
                FollowTipStatusEnum.FOLLOW_UP_VISIT_DUE_REMINDER.getIndex())) {
            record.setFollowTipStatus(FollowTipStatusEnum.FOLLOW_UP_VISIT_SUBMISSION_REMINDER.getIndex());
        }
        if (!Objects.isNull(record.getFollowTipStatus()) && Objects.equals(record.getFollowTipStatus(),
                FollowTipStatusEnum.FOLLOW_UP_QUESTIONING_REMINDER.getIndex())) {
            record.setFollowTipStatus(FollowTipStatusEnum.FOLLOW_UP_VISIT_SUBMISSION_REMINDER.getIndex());
        }
        return record;
    }

    public void handleWarnsInfo(PatientRecord patientRecord, List<FMedicalPropertyForm> formList,
                                PatientRecordSaveUpdateInfoForm patientRecordSaveUpdateInfoForm) {
        if (patientRecordSaveUpdateInfoForm.getIsSubmit() == 0) {
            return;
        }
        PatientInfo patientInfo = patientInfoMapper.selectPatientInfoById(patientRecord.getPatientId());
        MpPerson patientBaseInfo = mpPersonMapper.selectPersonByPersonId(patientInfo.getPersonId());
        WarnMedicalEarlyCondition warnMedicalEarlyCondition = new WarnMedicalEarlyCondition();
        warnMedicalEarlyCondition.setFollowId(patientRecord.getFollowId());
        warnMedicalEarlyCondition.setIsEffect("1");
        List<WarnMedicalEarlyCondition> conditionList =
                warnMedicalEarlyConditionMapper.selectConditionAndWarnMedicalEarlyList(warnMedicalEarlyCondition);
        if (!(CollectionUtils.isEmpty(conditionList))) {
            Map<Long, List<WarnMedicalEarlyCondition>> conditionListMaps =
                    conditionList.stream().collect(Collectors.groupingBy(WarnMedicalEarlyCondition::getPropertyId));
            Map<Long, FMedicalPropertyForm> formListMaps =
                    formList.stream().collect(Collectors.toMap(FMedicalPropertyForm::getPropertyId,
                            Function.identity(), (key1, key2) -> key2));
            List<WarnEarlyRecord> insertList = new ArrayList<>();
            for (Long key : conditionListMaps.keySet()) {
                if (formListMaps.containsKey(key)) {
                    FMedicalPropertyForm fMedicalPropertyForm = formListMaps.get(key);
                    List<WarnMedicalEarlyCondition> conditions = conditionListMaps.get(key);
                    for (WarnMedicalEarlyCondition wec : conditions) {
                        if (StringUtils.isEmpty(wec.getIsCondition())) {

                            if (StringUtils.isNotBlankAndNotEmpty(fMedicalPropertyForm.getPropertyValue()) && StringUtils.isNotBlankAndNotEmpty(wec.getEarlyWarnValue()) && StringUtils.isNotBlankAndNotEmpty(wec.getAttribute())) {
                                JSONObject jsonObject = JSONObject.parseObject(wec.getAttribute());
                                //获取多选配置,例如:[{"isShowInputValue":false,"label":"选项一","value":1},
                                // {"isShowInputValue":false,"label":"选项二","value":2},{"id":3,"label":"选项三","value":3}]
                                List<OptionsVo> optionsVoList =
                                        JSON.parseArray(jsonObject.getString("options").toString(),
                                                OptionsVo.class);
                                boolean isChecked = getExitValue(fMedicalPropertyForm.getPropertyValue(),
                                        wec.getEarlyWarnValue());
                                if (isChecked) {
                                    //true说明患者填写的值需要触发预警
                                    String warnDesc = getWarnSelectDesc(wec.getEarlyWarnValue(), optionsVoList);
                                    WarnEarlyRecord warnEarlyRecord = new WarnEarlyRecord();
                                    warnEarlyRecord.setRecordId(patientRecord.getRecordId());
                                    warnEarlyRecord.setWarnMedicalEarlyConditionId(wec.getId());
                                    if(StringUtils.isNotBlankAndNotEmpty(wec.getWarnPreDesc())){
                                        warnEarlyRecord.setWarnDesc(wec.getWarnPreDesc());
                                    }else {
                                        warnEarlyRecord.setWarnDesc(warnDesc);
                                    }
                                    String propertyDesc = getWarnSelectDesc(fMedicalPropertyForm.getPropertyValue(),
                                            optionsVoList);
                                    warnEarlyRecord.setPropertyValue(propertyDesc);
                                    warnEarlyRecord.setCreateTime(DateUtils.getNowDate());
                                    insertList.add(warnEarlyRecord);
                                }
                            }

                        } else if ("1".equals(wec.getIsCondition())) {
                            //分类预警,先判断性别和年龄,在判断参考值范围
                            if (patientBaseInfo != null && StringUtils.isNotBlankAndNotEmpty(fMedicalPropertyForm.getPropertyValue())) {
                                if (HealthUtils.getSex(patientBaseInfo.getCardId()) == Integer.parseInt(wec.getSexCondition())) {
                                    boolean ageResult = getWarnCondition(wec.getAgeConditionType(),
                                            String.valueOf(HealthUtils.IdNOToAge(patientBaseInfo.getCardId())), wec.getAgeEarlyWarnValue(),
                                            wec.getAgeEarlyWarnStart(), wec.getAgeEarlyWarnEnd());
                                    if (ageResult) {
                                        //年龄判断符合条件的进行参考范围判断，参考范围设置的为异常值
                                        boolean conditionResult = getWarnCondition(wec.getConditionType(),
                                                fMedicalPropertyForm.getPropertyValue(), wec.getEarlyWarnValue(),
                                                wec.getEarlyWarnStart(), wec.getEarlyWarnEnd());
                                        if (conditionResult) {
                                            //符合预警参考范围的值,入库预警表
                                            WarnEarlyRecord warnEarlyRecord = new WarnEarlyRecord();
                                            warnEarlyRecord.setRecordId(patientRecord.getRecordId());
                                            warnEarlyRecord.setWarnMedicalEarlyConditionId(wec.getId());
                                            String conditionStr = getWarnConditionDesc(wec.getConditionType(),
                                                    wec.getEarlyWarnValue(), wec.getEarlyWarnStart(),
                                                    wec.getEarlyWarnEnd());
                                            if(StringUtils.isNotBlankAndNotEmpty(wec.getWarnPreDesc())){
                                                warnEarlyRecord.setWarnDesc(wec.getWarnPreDesc());
                                            }else {
                                                warnEarlyRecord.setWarnDesc(conditionStr);
                                            }
                                            warnEarlyRecord.setPropertyValue(fMedicalPropertyForm.getPropertyValue());
                                            warnEarlyRecord.setCreateTime(DateUtils.getNowDate());
                                            insertList.add(warnEarlyRecord);
                                        }
                                    }
                                }
                            }
                        } else if ("2".equals(wec.getIsCondition())) {
                            //无分类预警,只需判断异常值参考范围
                            boolean conditionResult = getWarnCondition(wec.getConditionType(),
                                    fMedicalPropertyForm.getPropertyValue(), wec.getEarlyWarnValue(),
                                    wec.getEarlyWarnStart(), wec.getEarlyWarnEnd());
                            if (conditionResult) {
                                //符合预警参考范围的值,入库预警表
                                WarnEarlyRecord warnEarlyRecord = new WarnEarlyRecord();
                                warnEarlyRecord.setRecordId(patientRecord.getRecordId());
                                warnEarlyRecord.setWarnMedicalEarlyConditionId(wec.getId());
                                String conditionStr = getWarnConditionDesc(wec.getConditionType(),
                                        wec.getEarlyWarnValue(), wec.getEarlyWarnStart(), wec.getEarlyWarnEnd());
                                if(StringUtils.isNotBlankAndNotEmpty(wec.getWarnPreDesc())){
                                    warnEarlyRecord.setWarnDesc(wec.getWarnPreDesc());
                                }else {
                                    warnEarlyRecord.setWarnDesc(conditionStr);
                                }
                                warnEarlyRecord.setPropertyValue(fMedicalPropertyForm.getPropertyValue());
                                warnEarlyRecord.setCreateTime(DateUtils.getNowDate());
                                insertList.add(warnEarlyRecord);
                            }
                        }
                    }
                }
            }

            //批量插入预警表
            warnEarlyRecordMapper.deleteByRecordId(patientRecord.getRecordId());
            if (insertList.size() > 0) {
                warnEarlyRecordMapper.batchInsert(insertList);
                patientRecord.setWarnStatus("1");
                patientRecordMapper.updatePatientRecord(patientRecord);
                //触发预警发送推送和站内信 患者端
                String msgContent = "您有一条随访预警记录请及时查看";
                AccountNotice accountNotice = new AccountNotice(patientRecord,msgContent,"8");
                accountNoticeMapper.insertAccountNotice(accountNotice);
//                umpushService.sendCustomizedcastMessage(2,String.valueOf(accountNotice.getAccountId()),accountNotice.getContent(),
//                        accountNotice.getExtendJson());

                //医生端应该收到的通知
                List<ResearchUserRole> researchUserRoleList = researchUserRoleMapper.findUserRoleByResearchId(patientInfo.getResearchId());
                if(researchUserRoleList.size()>0){
                    String doctorMsgContent = "患者有新的预警请尽快处理";
                    for(ResearchUserRole r:researchUserRoleList){
                        AccountNotice an = new AccountNotice(patientRecord,doctorMsgContent,"18",r.getUserId());
                        accountNoticeMapper.insertAccountNotice(an);
                    }

                }

                //触发预警，发送电话外呼
                try{
                    callByRecordId(patientRecord.getRecordId());
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }


    //optValue符合条件返回true，否则false
    public boolean getWarnCondition(String type, String optValue, String value, String valueStart, String valueEnd) {
        boolean isFit = false;//是否符合条件
        if (StringUtils.isEmpty(optValue)) return isFit;
        BigDecimal b = new BigDecimal(optValue);
        //1等于2大于3大于等于4小于5小于等于6区间
        switch (type) {
            case "1"://等于
                BigDecimal v = new BigDecimal(value);
                if (b.compareTo(v) == 0) {
                    isFit = true;
                }
                break;
            case "2"://大于
                BigDecimal c = new BigDecimal(value);
                if (b.compareTo(c) == 1) {
                    isFit = true;
                }
                break;
            case "3"://大于等于
                BigDecimal l = new BigDecimal(value);
                if (b.compareTo(l) >= 0) {
                    isFit = true;
                }
                break;
            case "4"://小于
                BigDecimal n = new BigDecimal(value);
                if (b.compareTo(n) == -1) {
                    isFit = true;
                }
                break;
            case "5"://小于等于
                BigDecimal a = new BigDecimal(value);
                if (b.compareTo(a) <= 0) {
                    isFit = true;
                }
                break;
            case "6"://区间
                BigDecimal start = new BigDecimal(valueStart);
                BigDecimal end = new BigDecimal(valueEnd);
                if ((b.compareTo(start) >= 0) && (b.compareTo(end) <= 0)) {
                    isFit = true;
                }
                break;
            default:
                break;
        }
        return isFit;
    }

    //返回正常范围的描述
    public String getWarnConditionDesc(String type, String value, String valueStart, String valueEnd) {
        StringBuffer resultStr = new StringBuffer();
        //1等于2大于3大于等于4小于5小于等于6区间
        switch (type) {
            case "1"://等于
                resultStr.append("异常参考范围【=").append(value).append("】");
                break;
            case "2"://大于
                resultStr.append("异常参考范围【>").append(value).append("】");
                break;
            case "3"://大于等于
                resultStr.append("异常参考范围【>=").append(value).append("】");
                break;
            case "4"://小于
                resultStr.append("异常参考范围【<").append(value).append("】");
                break;
            case "5"://小于等于
                resultStr.append("异常参考范围【<=").append(value).append("】");
                break;
            case "6"://区间
                resultStr.append("异常参考范围【").append(valueStart).append("-").append(valueEnd).append("】");
                break;
            default:
                break;
        }
        return resultStr.toString();
    }

    //根据配置的自定义单选多选返回描述文案
    public String getWarnSelectDesc(String warnValue, List<OptionsVo> voList) {
        StringBuffer sb = new StringBuffer();
        List<String> strings = Arrays.asList(warnValue.split(","));
        Map<String, String> map = new HashMap<>();
        for (OptionsVo v : voList) {
            map.put(v.getValue(), v.getLabel());
        }
        for (String s : strings) {
            if (map.containsKey(s)) {
                sb.append(map.get(s)).append(",");
            }
        }
        if (sb.length() > 1) {
            return sb.substring(0, sb.length() - 1);
        } else {
            return "";
        }
    }

    //根据患者填的值和自定义预警值做比较，判断是否触发预警
    public boolean getExitValue(String value, String checkValue) {
        boolean isResult = false;
        List<String> valueStr = Arrays.asList(value.split(","));
        List<String> checkValueStr = Arrays.asList(checkValue.split(","));
        for (String s : valueStr) {
            if (checkValueStr.contains(s)) {
                isResult = true;
            }
            if (isResult) {
                break;
            }
        }
        return isResult;
    }


    public void callByRecordId(Long recordId) {
        PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByRecordId(recordId);
        PatientInfo patientInfo = patientInfoMapper.selectPatientInfoById(patientRecord.getPatientId());
        MpPerson patientBaseInfo = mpPersonMapper.selectPersonByPersonId(patientInfo.getPersonId());
        ResearchOutbound researchOutbound = researchOutboundMapper.selectByResearchIdAndType(patientRecord.getResearchId(),"1");
        if(researchOutbound!=null && patientBaseInfo!=null){
            PatientRecordOutbound patientRecordOutbound = patientRecordOutboundWarn(patientRecord,researchOutbound,"1");
            ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(patientInfo.getResearchId());
            patientRecordOutbound.setProjectName(researchInfo.getProjectName());
//            sendXunfeiCallWarn(patientRecordOutbound,patientRecord,researchOutbound,patientBaseInfo);
            patientRecordOutboundMapper.insertPatientRecordOutbound(patientRecordOutbound);
        }
        PatientRecordOutbound patientRecordOutboundOver = patientRecordOutboundWarn(patientRecord,null,"2");
//            sendXunfeiSmsOver(patientRecordOutboundOver,patientRecord,researchOutbound,patientBaseInfo);
        patientRecordOutboundMapper.insertPatientRecordOutbound(patientRecordOutboundOver);

    }

    public PatientRecordOutbound patientRecordOutboundWarn(PatientRecord patientRecord,ResearchOutbound researchOutbound,String outboundType){
        PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
        patientRecordOutbound.setRecordId(patientRecord.getRecordId());
        patientRecordOutbound.setResearchId(patientRecord.getResearchId());
        if(researchOutbound!=null){
            patientRecordOutbound.setOutboundId(researchOutbound.getId());
            patientRecordOutbound.setSpeechId(researchOutbound.getWarnSpeechId());
        }
        patientRecordOutbound.setOutboundType(outboundType);//1智能外呼2短信提醒3app站内信
        patientRecordOutbound.setRemindType("3");//预警
        patientRecordOutbound.setStatus("1");
        patientRecordOutbound.setCreateTime(DateUtils.getNowDate());
        patientRecordOutbound.setName("随访"+String.format("%02d", patientRecord.getStageAddNum()));
        patientRecordOutbound.setStageNum(patientRecord.getStageAddNum());
        patientRecordOutbound.setOutboundTime(DateUtils.getNowDate());
        return patientRecordOutbound;
    }

    public void sendXunfeiCallWarn(PatientRecordOutbound patientRecordOutbound,PatientRecord patientRecord,ResearchOutbound researchOutbound,MpPerson patientBaseInfo){
        XunfeiReq xunfeiReq = new XunfeiReq();
        List<Dweller> dwellerList = new ArrayList<>();

        //居民信息
        Dweller dweller = new Dweller();
        dweller.setDwellerName(patientBaseInfo.getPersonName());
        dweller.setTelephone(patientBaseInfo.getPhoneNumber());
        dweller.setIdCard(patientBaseInfo.getCardId());
        dweller.setRelationId(patientRecord.getRecordId()+"");
        dwellerList.add(dweller);

        xunfeiReq.setDwellers(dwellerList);
        xunfeiReq.setSpeechId(Long.parseLong(researchOutbound.getWarnSpeechId()));
//                xunfeiReq.setPlanName("");//任务名称，非必填
        xunfeiReq.setType(1);//1即呼
        if(!"0".equals(researchOutbound.getWarnReplay()) && researchOutbound.getWarnReplay()!=null){
            Recall recall = new Recall(Integer.parseInt(researchOutbound.getWarnReplay()),researchOutbound.getWarnReplaySpaceMinute());
            xunfeiReq.setRecall(recall);
        }
        List<Var> vars = getVarList(researchOutbound.getWarnSpeechId(),patientRecordOutbound);
        xunfeiReq.setVars(vars);
        L.info("讯飞呼叫随访请求参数{}", JSONObject.toJSONString(xunfeiReq));
//        String result = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(Constants.XUNFEI_CALL_TASK,xunfeiReq);
        String result = XfServiceHttpClientUtil.getInstance().sendSzDefaultHttpPostJson(baseUrl+"xunfei/callTask",xunfeiReq);
        L.info("讯飞呼叫随访返回结果{}",result);
        JSONObject jsonObject = JSON.parseObject(result);
        String code = jsonObject.getString("code");
        if(result!=null && ("1000".equals(code) || "0".equals(code))){
            XunfeiRes xunfeiRes = JSONObject.parseObject(jsonObject.getString("data"),XunfeiRes.class);
            String batch = xunfeiRes.getBatch()[0];
            List<TaskRelationVo> taskRelationVoList = xunfeiRes.getTaskRelation();
            Map<String,TaskRelationVo> taskRelationVoMap = new HashMap<>();
            if(taskRelationVoList.size()>0){
                taskRelationVoMap = taskRelationVoList.stream().collect(Collectors.toMap(TaskRelationVo::getRelationId,Function.identity(), (key1, key2) -> key2));
            }

            patientRecordOutbound.setStatus("2");//执行中
            patientRecordOutbound.setBatch(batch);
            if(taskRelationVoMap.containsKey(String.valueOf(patientRecord.getRecordId()))){
                TaskRelationVo relationVo = taskRelationVoMap.get(String.valueOf(patientRecord.getRecordId()));
                patientRecordOutbound.setTaskId(relationVo.getTaskId());
            }
            patientRecordOutbound.setUpdateTime(DateUtils.getNowDate());
            patientRecordOutboundMapper.insertPatientRecordOutbound(patientRecordOutbound);
        }
    }

    public List<Var> getVarList(String followSpeechId,PatientRecordOutbound pro){
        List<Var> varList = new ArrayList<>();
        if("772939646279680".equals(followSpeechId)){
            Var var1 = new Var("主治医生","唐都医院");
            Var var2 = new Var("单位名称","唐都医院");
            Var var3 = new Var("医院名称","唐都医院");
            varList.add(var1);
            varList.add(var2);
            varList.add(var3);
        }else if("216442646339584".equals(followSpeechId)){
            Var var1 = new Var("期间","住院");
            Var var2 = new Var("单位名称","唐都医院");
            varList.add(var1);
            varList.add(var2);
        }else if("214084533133312".equals(followSpeechId)){
            Var var1 = new Var("自我介绍","唐都医院");
            varList.add(var1);
        }else if("74459".equals(followSpeechId)){
            Var var1 = new Var("体检单位","唐都医院");
            Var var2 = new Var("体检项目","身高、体重、视力和血常规等检查");
            Var var3 = new Var("携带物品","背包");
            Var var4 = new Var("体检时间","每周一至周五全天");
            Var var5 = new Var("自我介绍","唐都医院");
            varList.add(var1);
            varList.add(var2);
            varList.add(var3);
            varList.add(var4);
            varList.add(var5);
        }else if("213577374679040".equals(followSpeechId)){
            Var var1 = new Var("自我介绍","唐都医院");
            Var var2 = new Var("未体检宣教","未体检宣教");
            Var var3 = new Var("体检类型","体检");
            varList.add(var1);
            varList.add(var2);
            varList.add(var3);
        }else if("1530318155456512".equals(followSpeechId)){
            //医院-随访提醒
            Var var1 = new Var("队列名称",pro.getProjectName());
            Var var2 = new Var("具体日期",DateUtils.dateTimeDays(pro.getPlanVisitTime()));
            Var var3 = new Var("外呼机构","唐都医院");
            varList.add(var1);
            varList.add(var2);
            varList.add(var3);
        }else if("1530319925452800".equals(followSpeechId)){
            //医院-超窗提醒
            Var var1 = new Var("外呼机构","唐都医院");
            Var var2 = new Var("队列名称",pro.getProjectName());
            varList.add(var1);
            varList.add(var2);
        }else if("1530309037039616".equals(followSpeechId)){
            //预警提醒
            Var var1 = new Var("外呼机构","唐都医院");
            Var var2 = new Var("具体日期",DateUtils.dateTimeDays(DateUtils.getNowDate()));
            varList.add(var1);
            varList.add(var2);
        }
        return varList;
    }


    public void sendXunfeiSmsOver(PatientRecordOutbound patientRecordOutbound,PatientRecord patientRecord,ResearchOutbound researchOutbound,MpPerson patientBaseInfo){
        XunfeiReq xunfeiReq = new XunfeiReq();
        List<Dweller> dwellerList = new ArrayList<>();

        //居民信息
        Dweller dweller = new Dweller();
        dweller.setDwellerName(patientBaseInfo.getPersonName());
        dweller.setTelephone(patientBaseInfo.getPhoneNumber());
        dweller.setIdCard(patientBaseInfo.getCardId());
        dweller.setRelationId(patientRecord.getRecordId()+"");
        dwellerList.add(dweller);

        xunfeiReq.setDwellers(dwellerList);
        xunfeiReq.setSmsId("16606");
        xunfeiReq.setType(1);//1即呼
        List<Var> vars = getSmsVarList(patientBaseInfo.getPhoneNumber());
        xunfeiReq.setVars(vars);
        L.info("讯飞短信提醒随访请求参数{}", JSONObject.toJSONString(xunfeiReq));
//        String result = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(Constants.XUNFEI_CALL_TASK,xunfeiReq);
        String result = XfServiceHttpClientUtil.getInstance().sendSzDefaultHttpPostJson(baseUrl+"xunfei/smsTask",xunfeiReq);
        L.info("讯飞短信提醒随访返回结果{}",result);
        JSONObject jsonObject = JSON.parseObject(result);
        String code = jsonObject.getString("code");
        if(result!=null && ("1000".equals(code) || "0".equals(code))){
            XunfeiRes xunfeiRes = JSONObject.parseObject(jsonObject.getString("data"),XunfeiRes.class);
            String batch = xunfeiRes.getBatch()[0];
            List<TaskRelationVo> taskRelationVoList = xunfeiRes.getTaskRelation();
            Map<String,TaskRelationVo> taskRelationVoMap = new HashMap<>();
            if(taskRelationVoList.size()>0){
                taskRelationVoMap = taskRelationVoList.stream().collect(Collectors.toMap(TaskRelationVo::getRelationId,Function.identity(), (key1, key2) -> key2));
            }

            patientRecordOutbound.setStatus("2");//执行中
            patientRecordOutbound.setBatch(batch);
            if(taskRelationVoMap.containsKey(String.valueOf(patientRecord.getRecordId()))){
                TaskRelationVo relationVo = taskRelationVoMap.get(String.valueOf(patientRecord.getRecordId()));
                patientRecordOutbound.setTaskId(relationVo.getTaskId());
            }
            patientRecordOutbound.setUpdateTime(DateUtils.getNowDate());
            patientRecordOutboundMapper.insertPatientRecordOutbound(patientRecordOutbound);
        }
    }

    public List<Var> getSmsVarList(String patientName){
        List<Var> varList = new ArrayList<>();
        Var var1 = new Var("dwellerName",patientName);
        Var var2 = new Var("content",DateUtils.dateTimeDays(DateUtils.getNowDate()));
        Var var3 = new Var("center","唐都医院");
        varList.add(var1);
        varList.add(var2);
        varList.add(var3);
        return varList;
    }


    /**
     * 患者查看档案详情
     *
     * @param recordId
     * @return
     */
    public DchealthCloudResponseEntity<PatientRecordDetailForm> patientRecordDetail(Long recordId,String detailType,
                                                                                    HttpServletRequest request) {
        DchealthCloudResponseEntity<PatientRecordDetailForm> result = new DchealthCloudResponseEntity<>();
        PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByRecordId(recordId);
        if (patientRecord == null) {//档案不存在
            result.setCode(CroApiCodeConstant.PATIENT_RECORD_NOT_EXIT.getCode());
            return result;
        }
        Long researchId = patientRecord.getResearchId();
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(researchId);
        if (Objects.isNull(researchInfo) || "1".equals(researchInfo.getDelFlag())) {
            result.setCode(CroApiCodeConstant.ARTICLE_NOT_EXIST.getCode());
            return result;
        }
        PatientRecordDetailForm patientRecordDetailForm = new PatientRecordDetailForm();
        patientRecordDetailForm.setRecordId(recordId);
        patientRecordDetailForm.setIsSubmit(patientRecord.getIsSubmit());
        patientRecordDetailForm.setStageName(patientRecord.getName());
        patientRecordDetailForm.setOverTimeStatus(patientRecord.getOverTimeStatus());
        patientRecordDetailForm.setWarnStatus(patientRecord.getWarnStatus());
        patientRecordDetailForm.setCreateTimeStr(DateUtils.dateTime(patientRecord.getCreateTime()));
        patientRecordDetailForm.setResearchId(patientRecord.getResearchId());
        patientRecordDetailForm.setFollowTipStatus(patientRecord.getFollowTipStatus());
        if(patientRecord.getVisitTime()!= null) {
            patientRecordDetailForm.setVisitTime(DateUtils.parseDateToStrFull(patientRecord.getVisitTime()));
        }
        List<MedicalFormVO> datas = medicalFormService.followForms(patientRecord.getFollowId(),
                patientRecord.getRecordId(), patientRecord.getFollowStatus(), (formVOS, propertyVOS, propertyTypes) -> {
            List<PatientRecordFiles> patientRecordFiles =
                    patientRecordFilesMapper.selectPatientRecordFilesByRecordId(recordId);
            CompletableFuture<Void> pcf = getPcf(recordId, patientRecord, propertyVOS, propertyTypes,
                    patientRecordFiles);
            CompletableFuture<Void> fcf = getFcf(recordId, patientRecord, formVOS, propertyVOS, patientRecordFiles);
            CompletableFuture.allOf(pcf, fcf).join();
        });

        // 处理预警源
        dealWarnSource(recordId, datas);

        patientRecordDetailForm.setMedicalFormVOS(datas);
        //随访质疑
        patientRecordDetailForm.setQuestionRecordVOS(questionList(patientRecord.getRecordId()));
        /* if ("1".equals(patientRecord.getQuestionStatus())){
            patientRecordDetailForm.setQuestionRecordVOS(questionList(patientRecord.getRecordId()));
        }*/
        //随访意见
        patientRecordDetailForm.setFollowOpinion(patientRecord.getFollowOpinion());
        patientRecordDetailForm.setFollowStatus(patientRecord.getFollowStatus());
        //获取患者信息
        PatientInfo patientInfo = patientInfoMapper.selectPatientInfoById(patientRecord.getPatientId());
        patientRecordDetailForm.setPatientId(patientInfo.getPatientId());
        patientRecordDetailForm.setPersonId(patientInfo.getPersonId());
        patientRecordDetailForm.setPatientName(patientInfo.getPatientName());
        PatientAccount patientAccount = patientAccountMapper.selectPatientAccountByAccountId(patientInfo.getAccountId());
        if(patientAccount!=null){
            patientRecordDetailForm.setPatientMobile(patientAccount.getMobile());
        }

        //设置详情页是否可编辑
        if (!Objects.isNull(patientInfo.getPlanResearchId()) && !Objects.equals(patientInfo.getPlanResearchId(),
                patientInfo.getResearchId())) {
            patientRecordDetailForm.setIsEditable(false);
        }

        MpPerson pb = mpPersonMapper.selectPersonByAccountId(patientInfo.getAccountId());
        if(pb!=null){
            patientRecordDetailForm.setSex(HealthUtils.getSex(pb.getCardId()));
        }

        //快捷回复内容
        QuestionQuick questionQuick = new QuestionQuick();
        questionQuick.setCreateUserId(patientInfo.getAccountId());
        List<QuestionQuick> questionQuickList = questionQuickMapper.selectQuestionQuickList(questionQuick);
        if (questionQuickList != null) {
            patientRecordDetailForm.setQuestionQuickList(questionQuickList);
        }
        //随访评价
        patientRecordDetailForm.setEvaluatePrescription(patientRecord.getEvaluatePrescription());
        patientRecordDetailForm.setEvaluateQuality(patientRecord.getEvaluateQuality());
        patientRecordDetailForm.setEvaluateReply(patientRecord.getEvaluateReply());

        //随访质效评价
        patientRecordDetailForm.setEvaluatePrescriptionBack(patientRecord.getEvaluatePrescriptionBack());
        patientRecordDetailForm.setEvaluateQualityBack(patientRecord.getEvaluateQualityBack());
        patientRecordDetailForm.setEvaluateReplyBack(patientRecord.getEvaluateReplyBack());

        //预警质效评价
        patientRecordDetailForm.setWarnEvaluatePrescriptionBack(patientRecord.getWarnEvaluatePrescriptionBack());
        patientRecordDetailForm.setWarnEvaluateQualityBack(patientRecord.getWarnEvaluateQualityBack());
        patientRecordDetailForm.setWarnEvaluateReplyBack(patientRecord.getWarnEvaluateReplyBack());


        //超窗质效评价
        patientRecordDetailForm.setOverEvaluatePrescriptionBack(patientRecord.getOverEvaluatePrescriptionBack());
        patientRecordDetailForm.setOverEvaluateQualityBack(patientRecord.getOverEvaluateQualityBack());
        patientRecordDetailForm.setOverEvaluateReplyBack(patientRecord.getOverEvaluateReplyBack());




        //预警
        WarnEarlyRecord warnEarlyRecord = new WarnEarlyRecord();
        warnEarlyRecord.setRecordId(patientRecord.getRecordId());
        List<WarnEarlyRecord> warnEarlyRecordList = warnEarlyRecordMapper.selectListByRecordId(warnEarlyRecord);
        if(warnEarlyRecordList!=null && warnEarlyRecordList.size()>0){
            Integer wer = warnEarlyRecordList.stream().filter(f -> f.getEarlyWarnLevel()!="").mapToInt(w -> dictToValue(w.getEarlyWarnLevel())).max().getAsInt();
            patientRecordDetailForm.setEarlyWarnLevel(String.valueOf(wer));
        }
        patientRecordDetailForm.setWarnEarlyRecordList(warnEarlyRecordList);
        //预警指导
        List<WarnLeaveMessage> list =
                warnLeaveMessageMapper.selectWarnLeaveMessageByRecordId(recordId);
        if (list != null && list.size() > 0) {
            list.forEach(x -> {
                if ("1".equals(x.getUserType())) {
                    SysUser sysUser = sysUserMapper.selectSysUserByUserId(x.getAccountId());
                    if (sysUser != null) {
                        x.setHeadImage(sysUser.getHeadImage() == null ? "https://hzfw.dchealthclinical" + ".com/files" +
                                "/admin.png" : sysUser.getHeadImage());
                        x.setUserName(sysUser.getNickName() == null ? "" : sysUser.getNickName());
                    }
                } else if ("2".equals(x.getUserType())) {
                    MpPerson patientBaseInfo = mpPersonMapper.selectPersonByAccountId(x.getAccountId());
                    if (patientBaseInfo != null) {
                        x.setHeadImage(croCoreConfig.getUserHead());
                        x.setUserName(patientBaseInfo.getPersonName());
                    }
                }
                String content = x.getContent();
                if (StrUtil.isNotEmpty(content) && content.contains(oldDomain)) {
                    String image = content.replace(oldDomain, newDomain);
                    x.setContent(image);
                }
            });
        }
        patientRecordDetailForm.setFirstDoctorId(getFirstDoctorId(patientRecord, patientInfo.getAccountId()));
        patientRecordDetailForm.setWarnLeaveMessageList(list);

        result.setData(patientRecordDetailForm);
        //设置随访记录状态信息
        setFollowTipAndGetNextNodeInfo(patientRecord, patientInfo, request);

        if("3".equals(detailType) && "1".equals(patientRecord.getOverTimeStatus())){
            //超窗查看详情且是待处理，需要修改成进行中
            patientRecord.setOverTimeStatus("2");
            patientRecordMapper.updatePatientRecord(patientRecord);
        }
        return result;
    }

    private Integer dictToValue(String dictName) {
        if(StringUtils.isBlank(dictName)) {
            return 0;
        }
        if(dictName.equals("一级")) {
            return 1;
        } else if(dictName.equals("二级")) {
            return 2;
        } else if(dictName.equals("三级")) {
            return 3;
        } else if(dictName.equals("轻度")) {
            return 1;
        } else if(dictName.equals("中度")) {
            return 2;
        } else if(dictName.equals("重度")) {
            return 3;
        } else if(dictName.equals("弱阳性")) {
            return 1;
        } else if(dictName.equals("阳性")) {
            return 3;
        } else if(dictName.equals("异常")) {
            return 1;
        } else if(dictName.equals("危急")) {
            return 3;
        } else {
            return 0;
        }

    }

    private void dealWarnSource(Long recordId, List<MedicalFormVO> medicalFormVOSResult) {
        try {
            // 查询预警信息
            List<WarnConditionValueVO> warnConditionValueVOS = warnEarlyRecordMapper.selectConditionAndValueByRecordId(recordId);
            Set<Long> collect = warnConditionValueVOS.stream().map(WarnConditionValueVO::getMedicalFormId).collect(Collectors.toSet());

            // 标记 预警源
            if (!CollectionUtils.isEmpty(warnConditionValueVOS)) {
                for (MedicalFormVO x : medicalFormVOSResult) {
                    if(!x.getFormType().equals("4")){ // 4：预警填报
                        continue;
                    }
                    List<MedicalFormVO> childMedicalFormVOS = x.getChildrens();
                    boolean isWarnSource = false;
                    if(!CollectionUtils.isEmpty(childMedicalFormVOS)){
                        isWarnSource = markWarnSource(collect, x, childMedicalFormVOS);
                    }else {
                        List<MedicalPropertyVO> propertyVOS = x.getPropertyVOS();
                        for(MedicalPropertyVO medicalPropertyVO : propertyVOS){
                            if(collect.contains(medicalPropertyVO.getMedicalFormId().longValue())){
                                isWarnSource = true;
                            }
                        }
                    }
                    x.setWarnSource(isWarnSource);
                }
            }
        } catch (Exception e) {
            L.error("api dealWarnSource error, recordId = {}, error is {}", recordId, e.getMessage());
        }
    }

    private boolean markWarnSource(Set<Long> collect, MedicalFormVO x, List<MedicalFormVO> childMedicalFormVOS) {
        for (MedicalFormVO y : childMedicalFormVOS) {
            List<MedicalFormVO> yChildMedicalFormVOS = y.getChildrens();
            if(!CollectionUtils.isEmpty(yChildMedicalFormVOS)){
                markWarnSource(collect, y, yChildMedicalFormVOS);
            }else {
                List<MedicalPropertyVO> propertyVOS = y.getPropertyVOS();
                for(MedicalPropertyVO medicalPropertyVO : propertyVOS){
                    if(collect.contains(medicalPropertyVO.getMedicalFormId().longValue())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 医生查看患者档案详情
     * @param recordId
     * @param
     * @return
     */
    public DchealthCloudResponseEntity<PatientRecordDetailForm> doctorSeePatientRecordDetail(Long recordId) {
        DchealthCloudResponseEntity<PatientRecordDetailForm> result = new DchealthCloudResponseEntity<>();
        PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByRecordId(recordId);
        if (patientRecord == null) {//档案不存在
            result.setCode(CroApiCodeConstant.PATIENT_RECORD_NOT_EXIT.getCode());
            return result;
        }
        PatientRecordDetailForm patientRecordDetailForm = new PatientRecordDetailForm();
        patientRecordDetailForm.setRecordId(recordId);
        patientRecordDetailForm.setIsSubmit(patientRecord.getIsSubmit());
        List<MedicalFormVO> datas = medicalFormService.followForms(patientRecord.getFollowId(),
                patientRecord.getRecordId(), patientRecord.getFollowStatus(), (formVOS, propertyVOS, propertyTypes) -> {
                    List<PatientRecordFiles> patientRecordFiles =
                            patientRecordFilesMapper.selectPatientRecordFilesByRecordId(recordId);
                    CompletableFuture<Void> pcf = getPcf(recordId, patientRecord, propertyVOS, propertyTypes,
                            patientRecordFiles);
                    CompletableFuture<Void> fcf = getFcf(recordId, patientRecord, formVOS, propertyVOS, patientRecordFiles);
                    CompletableFuture.allOf(pcf, fcf).join();
                });
        patientRecordDetailForm.setMedicalFormVOS(datas);
        //随访质疑
        patientRecordDetailForm.setQuestionRecordVOS(questionList(patientRecord.getRecordId()));
        /* if ("1".equals(patientRecord.getQuestionStatus())){
            patientRecordDetailForm.setQuestionRecordVOS(questionList(patientRecord.getRecordId()));
        }*/
        //随访意见
        patientRecordDetailForm.setFollowOpinion(patientRecord.getFollowOpinion());
        patientRecordDetailForm.setFollowStatus(patientRecord.getFollowStatus());
        //获取患者信息
        PatientInfo patientInfo = patientInfoMapper.selectPatientInfoById(patientRecord.getPatientId());
        patientRecordDetailForm.setPatientId(patientInfo.getPatientId());
        if(StringUtils.equals(patientInfo.getAdjustType(),"2")){
            patientRecordDetailForm.setIsEditable(false);
        }
        patientRecordDetailForm.setPatientName(patientInfo.getPatientName());
        patientRecordDetailForm.setPatientMobile(patientInfo.getPatientMobile());
        //设置详情页是否可编辑
        if (!Objects.isNull(patientInfo.getPlanResearchId()) && !Objects.equals(patientInfo.getPlanResearchId(),
                patientInfo.getResearchId())) {
            patientRecordDetailForm.setIsEditable(false);
        }
        //快捷回复内容
        QuestionQuick questionQuick = new QuestionQuick();
        questionQuick.setCreateUserId(patientInfo.getAccountId());
        List<QuestionQuick> questionQuickList = questionQuickMapper.selectQuestionQuickList(questionQuick);
        if (questionQuickList != null) {
            patientRecordDetailForm.setQuestionQuickList(questionQuickList);
        }
        //随访评价
        patientRecordDetailForm.setEvaluatePrescription(patientRecord.getEvaluatePrescription());
        patientRecordDetailForm.setEvaluateQuality(patientRecord.getEvaluateQuality());
        patientRecordDetailForm.setEvaluateReply(patientRecord.getEvaluateReply());
        //预警
        WarnEarlyRecord warnEarlyRecord = new WarnEarlyRecord();
        warnEarlyRecord.setRecordId(patientRecord.getRecordId());
        List<WarnEarlyRecord> warnEarlyRecordList = warnEarlyRecordMapper.selectListByRecordId(warnEarlyRecord);
        patientRecordDetailForm.setWarnEarlyRecordList(warnEarlyRecordList);
        //预警指导
        List<WarnLeaveMessage> list =
                warnLeaveMessageMapper.selectWarnLeaveMessageByRecordId(patientRecord.getRecordId());
        if (list != null && list.size() > 0) {
            list.forEach(x -> {
                if ("1".equals(x.getUserType())) {
                    SysUser sysUser = sysUserMapper.selectSysUserByUserId(x.getAccountId());
                    if (sysUser != null) {
                        x.setHeadImage(sysUser.getHeadImage() == null ? "https://hzfw.dchealthclinical" + ".com/files" +
                                "/admin.png" : sysUser.getHeadImage());
                        x.setUserName(sysUser.getNickName() == null ? "" : sysUser.getNickName());
                    }
                } else if ("2".equals(x.getUserType())) {
                	MpPerson patientBaseInfo = mpPersonMapper.selectPersonByAccountId(x.getAccountId());
                    if (patientBaseInfo != null) {
                        x.setHeadImage(croCoreConfig.getUserHead());
                        x.setUserName(patientBaseInfo.getPersonName());
                    }
                }
            });
        }
        patientRecordDetailForm.setFirstDoctorId(getFirstDoctorId(patientRecord, patientInfo.getAccountId()));
        patientRecordDetailForm.setWarnLeaveMessageList(list);

        result.setData(patientRecordDetailForm);
        //设置随访记录状态信息
//        setFollowTipAndGetNextNodeInfo(patientRecord, patientInfo, request);
        return result;
    }
    private String getFirstDoctorId(PatientRecord patientRecord, Long accountId) {
        WarnDoctorPatient warnDoctorPatient = new WarnDoctorPatient();
        warnDoctorPatient.setPatientId(patientRecord.getPatientId());
        warnDoctorPatient.setResearchId(patientRecord.getResearchId());
        List<WarnDoctorPatient> warnDoctorPatientList =
                warnDoctorPatientMapper.selectWarnDoctorPatient(warnDoctorPatient);
        if (!CollectionUtils.isEmpty(warnDoctorPatientList)) {
            Optional<WarnDoctorPatient> firstDoctorOptional =
                    warnDoctorPatientList.stream().filter(wp -> Objects.equals(1, wp.getIsFirstDoctor())).findAny();
            return firstDoctorOptional.map(doctorPatient -> doctorPatient.getDoctorId().toString()).orElse("");
        } else {
            return "";
        }
    }

    private CompletableFuture<Void> getFcf(Long recordId, PatientRecord patientRecord, List<MedicalFormVO> formVOS,
                                           List<MedicalPropertyVO> propertyVOS,
                                           List<PatientRecordFiles> patientRecordFiles) {
        CompletableFuture<Void> fcf = CompletableFuture.runAsync(() -> {
            if (formVOS != null && !formVOS.isEmpty()) {
                if (patientRecordFiles != null && !patientRecordFiles.isEmpty()) {
                    Map<Long, List<String>> filesMap =
                            patientRecordFiles.stream().filter(f -> f.getMedicalFormId() != null).collect(Collectors.groupingBy(PatientRecordFiles::getMedicalFormId, Collectors.mapping(PatientRecordFiles::getImageUrl, Collectors.toList())));
                    if (filesMap != null && !filesMap.isEmpty()) {//图片
                        formVOS.stream().filter(f -> "2".equals(f.getFormType())).forEach(f -> {
                            if (filesMap.containsKey(f.getMedicalFormId())) {
                                f.setImageUrls(filesMap.get(f.getMedicalFormId()));
                            }
                        });
                    }
                }
//                    if (formVOS.stream().anyMatch(m->"1".equals(m.getDynamic()))){
                if (formVOS.stream().anyMatch(m -> "1".equals(m.getOpenMultiple()))) {
                    DynamicForm dynamicForm = new DynamicForm();
                    dynamicForm.setRecordId(recordId);
                    dynamicForm.setType("1");
                    dynamicForm.setSql(dynamicSql.buildSelectDynamic(patientRecord.getResearchId()));
                    List<DynamicCommonProperty> dynamicList = dynamicCommonMapper.selectBySql(dynamicForm);
                    if (dynamicList != null && !dynamicList.isEmpty()) {
                        Map<Long, List<MedicalPropertyVO>> pMap =
                                propertyVOS.stream().collect(Collectors.groupingBy(MedicalPropertyVO::getMedicalFormId));
                        Map<Long, List<DynamicCommonProperty>> listMap =
                                dynamicList.stream().collect(Collectors.groupingBy(DynamicCommonProperty::getFormId));
                        formVOS.stream()
//                                    .filter(f->"1".equals(f.getDynamic()))
                                .filter(f -> "1".equals(f.getOpenMultiple())).forEach(f -> {
                                    if (listMap.containsKey(f.getMedicalFormId()) && pMap.containsKey(f.getMedicalFormId())) {
                                /*Map<Long, Integer> p = pMap.get(f.getMedicalFormId()).stream()
                                        .collect(Collectors.toMap(MedicalPropertyVO :: getId,
                                        MedicalPropertyVO::getSortNum));
                                List<DynamicCommonProperty> commonProperties = listMap.get(f.getMedicalFormId());*/

                                        Map<String, List<MedicalPropertyVO>> dynamicTable =
                                                listMap.get(f.getMedicalFormId()).stream().collect(Collectors.groupingBy(DynamicCommonProperty::getNumber)).entrySet().stream().collect(Collectors.toMap(key -> String.valueOf(key.getKey()), item -> {
                                            List<DynamicCommonProperty> ds = item.getValue();
                                            if (ds == null || ds.isEmpty()) return new ArrayList<>();

                                            List<MedicalPropertyVO> ps = pMap.get(f.getMedicalFormId());
                                            List<MedicalPropertyVO> nps = BeanUtils.copyList(ps,
                                                    MedicalPropertyVO::new);
                                            return nps.stream().map(pv -> {
                                                ds.stream().filter(m -> Objects.equals(pv.getId(), m.getPropertyId())).forEach(m2 -> {
                                                    pv.setPropertyValue(m2.getPropertyValue());
                                                    pv.setOther(m2.getOther());
                                                });
                                                return pv;
                                            }).collect(Collectors.toList());
                                        }, (oldValue, newValue) -> newValue, LinkedHashMap::new));
                                        f.setDynamicTable(dynamicTable);
                                    } else {
                                        if (pMap.containsKey(f.getMedicalFormId())) {
                                            List<MedicalPropertyVO> ds = pMap.get(f.getMedicalFormId());
                                            Map<String, List<MedicalPropertyVO>> map = new HashMap<>();
                                            map.put("1", ds);
                                            f.setDynamicTable(map);
                                        }
                                    }
                                });
                    } else {
                        Map<Long, List<MedicalPropertyVO>> pMap =
                                propertyVOS.stream().collect(Collectors.groupingBy(MedicalPropertyVO::getMedicalFormId));
                        formVOS.stream().filter(f -> "1".equals(f.getOpenMultiple())).forEach(f -> {
                            if (pMap.containsKey(f.getMedicalFormId())) {
                                List<MedicalPropertyVO> ds = pMap.get(f.getMedicalFormId());
                                Map<String, List<MedicalPropertyVO>> map = new HashMap<>();
                                map.put("1", ds);
                                f.setDynamicTable(map);
                            }
                        });
                    }
                }
            }
        }, executor);
        return fcf;
    }

    private CompletableFuture<Void> getPcf(Long recordId, PatientRecord patientRecord,
                                           List<MedicalPropertyVO> propertyVOS, Set<String> propertyTypes,
                                           List<PatientRecordFiles> patientRecordFiles) {
        CompletableFuture<Void> pcf = CompletableFuture.runAsync(() -> {
            List<DynamicCommonProperty> t = new ArrayList<>();
            if (propertyTypes != null && !propertyTypes.isEmpty()) {
                DynamicForm dynamicForm = new DynamicForm();
                dynamicForm.setRecordId(recordId);
                dynamicForm.setType("1");
                if (PropertyType.largeText(propertyTypes)) {//大文本
                    dynamicForm.setSql(dynamicSql.buildSelectLargeText(patientRecord.getResearchId()));
                    List<DynamicCommonProperty> largeTextList = dynamicCommonMapper.selectBySql(dynamicForm);
                    if (largeTextList != null && !largeTextList.isEmpty()) t.addAll(largeTextList);
                }
                if (PropertyType.text(propertyTypes)) {//小文本
                    dynamicForm.setSql(dynamicSql.buildSelectText(patientRecord.getResearchId()));
                    List<DynamicCommonProperty> textList = dynamicCommonMapper.selectBySql(dynamicForm);
                    if (textList != null && !textList.isEmpty()) t.addAll(textList);
                }
            }

            Map<Long, List<String>> filesMap = null;
            if (patientRecordFiles != null && !patientRecordFiles.isEmpty()) {
                filesMap =
                        patientRecordFiles.stream().filter(f -> f.getPropertyId() != null).collect(Collectors.groupingBy(PatientRecordFiles::getPropertyId, Collectors.mapping(PatientRecordFiles::getImageUrl, Collectors.toList())));
            }
            // 通过propertyId去重
            t = t.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(DynamicCommonProperty::getPropertyId))), ArrayList::new));
            Map<Long, DynamicCommonProperty> dp =
                    t.stream().distinct().collect(Collectors.toMap(DynamicCommonProperty::getPropertyId, item -> item));
            if (propertyVOS != null && !propertyVOS.isEmpty()) {
                for (MedicalPropertyVO p : propertyVOS) {
                    if (PropertyType.eq(p.getPropertyType(), PropertyType.UPLOAD)) {
                        if (filesMap != null && !filesMap.isEmpty() && filesMap.containsKey(p.getId())) {
                            p.setImageUrls(filesMap.get(p.getId()));
                        }
                    } else {
                        if (dp != null && !dp.isEmpty() && dp.containsKey(p.getId())) {
                            DynamicCommonProperty propertyVO = dp.get(p.getId());
                            if (propertyVO != null) {
                                p.setPropertyValue(propertyVO.getPropertyValue());
                                p.setOther(propertyVO.getOther());
                            }
                        }
                    }
                }
            }
        }, executor);
        return pcf;
    }


    /**
     * 绑定患者
     *
     * @param bindPatientForm
     * @return
     */
    @Transactional
    public DchealthCloudResponseEntity<PatientAccountInfoVO> bindPatient(BindPatientForm bindPatientForm) {
        DchealthCloudResponseEntity<PatientAccountInfoVO> result = new DchealthCloudResponseEntity<>();
        PatientAccount patientAccount = patientAccountMapper.selectPatientAccountByAccountId(bindPatientForm.getAccountId());
        if (patientAccount == null) {
            result.setCode(CroApiCodeConstant.USER_NOT_EXIST.getCode());
            return result;
        }
        //停用
        if ("1".equals(patientAccount.getStatus())) {
            result.setCode(CroApiCodeConstant.BLOCK_UP_ERROR.getCode());
            return result;
        }
        //绑定且输入的身份证
        if ("1".equals(patientAccount.getBindStatus()) && bindPatientForm.getIdCardNo().equalsIgnoreCase(patientAccount.getIdCard())) {
            result.setCode(CroApiCodeConstant.ALREADY_BINDING.getCode());
            String personId = patientAccount.getRegistrySigns();
            // 返回已绑定id
            PatientAccountInfoVO patientAccountInfoVO = new PatientAccountInfoVO();
            patientAccountInfoVO.setPersonId(personId);
            result.setData(patientAccountInfoVO);
            return result;
        }
        if ("1".equals(patientAccount.getBindStatus()) && !bindPatientForm.getIdCardNo().equalsIgnoreCase(patientAccount.getIdCard())) {
            result.setCode(CroApiCodeConstant.ALREADY_BINDING_OTHER.getCode());
            String personId = patientAccount.getRegistrySigns();
            // 返回已绑定id
            PatientAccountInfoVO patientAccountInfoVO = new PatientAccountInfoVO();
            patientAccountInfoVO.setPersonId(personId);
            patientAccountInfoVO.setIdCard(patientAccount.getIdCard());
            result.setData(patientAccountInfoVO);
            return result;
        }
        MpPatientAccount mpPatientAccount =  mpPatientAccountMapper.selectMpPatientAccountByAccountId(bindPatientForm.getAccountId());
       if(mpPatientAccount != null) {
    	   result.setCode(CroApiCodeConstant.ALREADY_BINDING_ERROR.getCode());
           return result; 
       }
        // 只处理未绑定的状态
       PatientAccountInfoVO patientAccountInfoVO = new PatientAccountInfoVO();
       MpPerson patientBaseInfos = mpPersonMapper.selectMpPersonByPersonIdCardId(bindPatientForm.getRegistrySigns(),bindPatientForm.getIdCardNo());
        if(patientBaseInfos!=null){
            MpPatientAccount mpa = mpPatientAccountMapper.selectMpPatientAccountBypersonId(patientBaseInfos.getPersonId());
            if(mpa!=null && !(mpa.getAccountId().longValue()==bindPatientForm.getAccountId().longValue())){
                result.setCode(CroApiCodeConstant.ALREADY_BINDING_ERROR.getCode());
                return result;
            }
        }

       if (patientBaseInfos == null  ) {
           PatMasterIndex patMasterIndex = patMasterIndexMapper.selectByIdNoAndPatientId(bindPatientForm.getIdCardNo(), bindPatientForm.getRegistrySigns());
           if(patMasterIndex == null ) {
        	   result.setCode(CroApiCodeConstant.PATIENT_NOT_EXIST_PLEASE_FILL_CORRECT_ID_NUMBER_AND_REGISTRY_SIGNS.getCode());
               return result;
           }
           //此处因为患者多手机多就诊号暂时加入手机号查询
           MpPerson patientBaseInfo = mpPersonMapper.selectMpPersonByCardId(bindPatientForm.getIdCardNo(), patientAccount.getMobile());
           if (Objects.nonNull(patientBaseInfo)) {
               // 多个身份证~ 发布事件后, 设置主号, 然后
               Map<String, String> json = new HashMap<>();
               json.put("idCard",bindPatientForm.getIdCardNo());
               json.put("personId",bindPatientForm.getRegistrySigns());
               redisPublishService.convertAndSend("patientCard", JSON.toJSONString(json));
//               if(bindPatientForm.getAccountId().equals()) {
//
//               }
               patientBaseInfos = patientBaseInfo;

//               L.info("患者存在多个就诊号={}", JSON.toJSONString(patientBaseInfos));
//               result.setCode(CroApiCodeConstant.PATIENT_ALREADY_EXIST.getCode());
//               // 返回已绑定id
//               PatientAccountInfoVO patientAccountInfoVO2 = new PatientAccountInfoVO();
//               patientAccountInfoVO2.setPersonId(patientBaseInfo.getPersonId());
//               patientAccountInfoVO2.setIdCard(bindPatientForm.getIdCardNo());
//               result.setData(patientAccountInfoVO2);
//               return result;
           }else{
               patientBaseInfos = insertMpPerson(patMasterIndex);
           }
       }
        
    	MpPatientAccount  patientAccount2 = new MpPatientAccount();
    	patientAccount2.setPersonId(String.valueOf(patientBaseInfos.getPersonId()));
    	patientAccount2.setAccountId(bindPatientForm.getAccountId());
    	mpPatientAccountMapper.insertMpPatientAccount(patientAccount2);
        patientAccount.setBindStatus("1");
        patientAccount.setRegistrySigns(bindPatientForm.getRegistrySigns());
        patientAccount.setIdCard(bindPatientForm.getIdCardNo());
        patientAccountMapper.updatePatientAccount(patientAccount);
        patientAccountInfoVO.setPersonId(patientBaseInfos.getPersonId());
        patientAccountInfoVO.setPersonName(patientBaseInfos.getPersonName());
        patientAccountInfoVO.setBindStatus("1");
        patientAccountInfoVO.setSex(HealthUtils.getSex(patientBaseInfos.getCardId()));
        patientAccountInfoVO.setIdCard(patientBaseInfos.getCardId());
        patientAccountInfoVO.setAccountId(bindPatientForm.getAccountId());


        //patient_info表中accountId是null的做绑定处理
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setPersonId(patientBaseInfos.getPersonId());
//        patientInfo.setEnrollmentStatus("0");
        List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoList(patientInfo);
        List<PatientInfo> filterPatientInfos = patientInfos.stream().filter(info -> info.getAccountId() == 0).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(filterPatientInfos)){
            for (PatientInfo info : filterPatientInfos) {
                PatientInfo patientInfoVo = new PatientInfo();
                patientInfoVo.setPatientId(info.getPatientId());
                patientInfoVo.setAccountId(bindPatientForm.getAccountId());
                patientInfoVo.setPatientMobile(patientAccount.getMobile());
                patientInfoMapper.updatePatientInfo(patientInfoVo);
            }
        }
        result.setData(patientAccountInfoVO);
        return result;
    }
    
  

    /**
     * 申请队列
     *
     * @param applyResearchForm
     * @return
     */
    @Transactional
    public DchealthCloudResponseEntity<CommonResult> applyResearch(ApplyResearchForm applyResearchForm) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CommonResult commonResult = new CommonResult();
        PatientAccount patientAccount = patientAccountMapper.selectPatientAccountByAccountId(applyResearchForm.getAccountId());
        if (patientAccount == null) {
            result.setCode(CroApiCodeConstant.USER_NOT_EXIST.getCode());
            return result;
        }
        if ("1".equals(patientAccount.getStatus())) {
            result.setCode(CroApiCodeConstant.BLOCK_UP_ERROR.getCode());
            return result;
        }
        PatientInfo patientInfo = new PatientInfo();
        MpPerson patientBaseInfo = mpPersonMapper.selectPersonByAccountId(patientAccount.getAccountId());
        if (patientBaseInfo != null) {
            patientInfo.setPatientName(patientBaseInfo.getPersonName());
            patientInfo.setPersonId(patientBaseInfo.getPersonId());
            patientInfo.setPatientMobile(patientBaseInfo.getPhoneNumber());
        }
        PatientInfo info = new PatientInfo();
        info.setPersonId(patientInfo.getPersonId());
        info.setResearchId(applyResearchForm.getResearchId());
        info.setAccountId(applyResearchForm.getAccountId());
        List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoList(info);
        if (patientInfos.size() == 0) {
            patientInfo.setCreateTime(new Date());
            patientInfo.setAccountId(applyResearchForm.getAccountId());
            patientInfo.setResearchId(applyResearchForm.getResearchId());
            patientInfo.setPlanScanResearchId(applyResearchForm.getResearchId());
            patientInfo.setChannelType("2");//来源渠道  2扫码
            patientInfo.setDeptId(applyResearchForm.getDeptId());
            patientInfo.setPlanResearchId(applyResearchForm.getResearchId());
            patientInfo.setPatientMobile(patientBaseInfo.getPhoneNumber());
            patientInfoMapper.insertPatientInfo(patientInfo);

            ResearchPatient researchPatient = new ResearchPatient();
            researchPatient.setResearchId(patientInfo.getResearchId());
            researchPatient.setPersonId(patientInfo.getPersonId());
            researchPatient.setPatientId(patientInfo.getPatientId());
            researchPatient.setCreateTime(DateUtils.getNowDate());
            researchPatientMapper.insertResearchPatient(researchPatient);

            genBasePatientRecord(applyResearchForm, patientInfo);
        }else {
            PatientInfo patient = patientInfos.get(0);
            String enrollmentHandlerStatus = patient.getEnrollmentHandlerStatus(); //1,2,3
            String enrollmentStatus = patient.getEnrollmentStatus();//0,1
            //邀请变主动扫码, 待处理1, 渠道2
//            if ("2".equals(enrollmentHandlerStatus)) {
//                enrollmentHandlerStatus = "1";
//            }
            if ("3".equals(enrollmentHandlerStatus) && "1".equals(enrollmentStatus)) {
                //已入组
                result.setCode(CroApiCodeConstant.ALREADY_IN_GROUP.getCode());
                return result;
            }
            //待处理
            patient.setEnrollmentHandlerStatus("1");
            //扫码
            patient.setChannelType("2");
            patientInfoMapper.updatePatientInfo(patient);
        }
        commonResult.setResult(true);
        result.setData(commonResult);
        // 当前队列owner
        Long userId = getResearchOwnerId(applyResearchForm.getResearchId());
        // 申请入组after, 发送加载随访树事件
        if (!Objects.isNull(userId)) {
            redisPublishService.convertAndSend("followTree", Long.toString(userId));
        }
        return result;
    }

    private Long getResearchOwnerId(Long researchId) {
        ResearchInfo researchInfo = researchInfoMapper.selectResearchOwnerById(researchId);
        L.info("队列owner={}",JSON.toJSONString(researchInfo));
        return researchInfo.getUserId();
    }

    public DchealthCloudResponseEntity<MpPerson> selectPatientBaseInfoByAccountId(Long accountId) {
        DchealthCloudResponseEntity<MpPerson> result = new DchealthCloudResponseEntity<>();
        MpPerson patientBaseInfo = mpPersonMapper.selectPersonByAccountId(accountId);
        if(patientBaseInfo==null){
//        	 result.setCode(CroApiCodeConstant.ERROR_PARAM.getCode());
            result.setData(new MpPerson());
            return result;
        }
        List<PatientInfo> newPatientInfoList = new ArrayList<>();
        PatientInfo pio = new PatientInfo();
        pio.setAccountId(accountId);
        pio.setEnrollmentHandlerStatus("3");
        pio.setEnrollmentStatus("1");
        pio.setIsShowApp("1");
        List<PatientInfo> patientInfoList = patientInfoMapper.selectPatientInfoList(pio);
        Map<Long, List<PatientInfo>> researchIdPatientMap = patientInfoList.stream().collect(Collectors.groupingBy(PatientInfo::getResearchId));
        if (!CollectionUtils.isEmpty(patientInfoList)) {
            researchIdPatientMap.keySet().forEach(researchId -> {
                Optional<PatientInfo> maxPatientIdPatient =
                        researchIdPatientMap.get(researchId).stream().max(Comparator.comparingLong(PatientInfo::getPatientId));
                PatientInfo patientInfo = maxPatientIdPatient.get();
                ResearchInfo researchInfo = new ResearchInfo();
                researchInfo.setId(patientInfo.getResearchId());
//                researchInfo.setResearchStatus("1");
                PatientInfo patientInf = handleResearchInfo(patientInfo, researchInfo);
                ResearchInfo researchInfo2 = researchInfoMapper.selectByIdAndStatus(researchInfo);
                //此处因为需要小程序看到未发布的队列暂时去掉此判断 ||"0".equals(researchInfo2.getResearchStatus())
                if ("1".equals(researchInfo2.getDelFlag())) {
                    //跳过
                } else if("0".equals(researchInfo2.getResearchStatus())) {
                    //切换了队列
                    if (!Objects.isNull(patientInfo.getPlanResearchId()) && !Objects.equals(patientInfo.getPlanResearchId(), patientInfo.getResearchId())) {
                        ResearchInfo researchInfo1 = patientInfo.getResearchInfo();
                        researchInfo1.setPendingTasksNum(0);
                        patientInf.setResearchInfo(researchInfo1);
                        patientInf.setPatientStatus("3");
                        newPatientInfoList.add(patientInf);
                    } else {
                        newPatientInfoList.add(patientInf);
                    }
                }
                 else if ("1".equals(researchInfo2.getResearchStatus())||"2".equals(researchInfo2.getResearchStatus())) {
                    //切换了队列
                    if (!Objects.isNull(patientInfo.getPlanResearchId()) && !Objects.equals(patientInfo.getPlanResearchId(), patientInfo.getResearchId())) {
                        ResearchInfo researchInfo1 = patientInfo.getResearchInfo();
                        researchInfo1.setPendingTasksNum(0);
                        patientInf.setResearchInfo(researchInfo1);
                        patientInf.setPatientStatus("3");
                        newPatientInfoList.add(patientInf);
                    } else {
                        newPatientInfoList.add(patientInf);
                    }
                }
            });
        }
        patientBaseInfo.setPatientInfoList(newPatientInfoList);
        result.setData(patientBaseInfo);
        return result;
    }

   
    private PatientInfo handleResearchInfo(PatientInfo patientInfo, ResearchInfo info) {

        ResearchInfo researchInfo = researchInfoMapper.selectByIdAndStatus(info);
        int pendingTasksNum = getPendingTasksNum(patientInfo);
        //此处因为需要小程序看到未发布的队列暂时去掉此判断 ||"0".equals(researchInfo.getResearchStatus())
        if (Objects.isNull(researchInfo)||"1".equals(researchInfo.getDelFlag())) {
            L.error("队列信息查询为空:{}", JSON.toJSONString(researchInfo));
        } else {
            researchInfo.setPendingTasksNum(pendingTasksNum);
            patientInfo.setResearchInfo(researchInfo);
        }
        return patientInfo;
    }

  

    private int getPendingTasksNum(PatientInfo patientInfo) {
//        有新的随访到期，有新的随访质疑，有新的随访意见 这三个数量和
        // 通过patientInfo.getResearchId  .patientId   -->filter( )
        PatientRecord patientRecord = new PatientRecord();
        patientRecord.setPatientId(patientInfo.getPatientId());
        patientRecord.setResearchId(patientInfo.getResearchId());
        List<PatientRecord> patientRecordList = patientRecordMapper.selectPatientRecordList(patientRecord);
        int pendingTasksNum = patientRecordList.stream().filter(p -> {
            return (p.getFollowTipStatus() == FollowTipStatusEnum.FOLLOW_UP_VISIT_DUE_REMINDER.getIndex() || p.getFollowTipStatus() == FollowTipStatusEnum.FOLLOW_UP_QUESTIONING_REMINDER.getIndex() || p.getFollowTipStatus() == FollowTipStatusEnum.FOLLOW_UP_OPINION_REMINDER.getIndex());
        }).collect(Collectors.toList()).size();
        return pendingTasksNum;
    }

    @Transactional
    public DchealthCloudResponseEntity<PatientRecordListVo> selectPatientRecordList(PatientRecord record) {
        AtomicReference<PatientRecord> nextNodeAtomic = new AtomicReference<>(new PatientRecord());
        DchealthCloudResponseEntity<PatientRecordListVo> result = new DchealthCloudResponseEntity<>();
        PatientRecordListVo resultVo = new PatientRecordListVo();

        List<PatientRecord> patientRecords = patientRecordMapper.selectPatientRecordList(record);
        // 唐都新需求（20250703）添加新的健康管理填写的入口
        // 查询队列下有没有健康管理的表单信息，如果有的话则需要放开填写的权限
        PatientRecord rp = patientRecords.get(0);
        List<FMedicalForm> fMedicalForms = fMedicalFormMapper.selectFMedicalHealthFormJoinByMedicalFollowId(rp.getFollowId(), null);
        if (fMedicalForms != null && !fMedicalForms.isEmpty()) {
            rp.setHealthEditable(true);
        }

        //对于A-》B->A 的情况递归查询同一个队列中旧队列的数据
        DchealthCloudResponseEntity<PatientRecordListVo> result1 = addOldFollowNodes(record, result,
                patientRecords);
        if (result1 != null) return result1;

        Optional<PatientRecord> maxFollowIdPr =
                patientRecords.stream().max(Comparator.comparingLong(PatientRecord::getRecordId));
        List<PatientRecordTemplate> patientRecordTemplates =
                patientRecordTemplateMapper.selectPatientRecordTemplateByResearchId(record.getResearchId());
        Map<Long, PatientRecordTemplate> templateMap =
                patientRecordTemplates.stream().collect(Collectors.toMap(PatientRecordTemplate::getFollowId,
                        Function.identity(), (key1, key2) -> key2));

        patientRecords.forEach(s -> {
            PatientInfo patientInfo = patientInfoMapper.selectPatientInfoById(s.getPatientId());
            s.setPatientStatus(patientInfo.getPatientStatus());
            if (templateMap.containsKey(s.getFollowId())) {
                PatientRecordTemplate pt = templateMap.get(s.getFollowId());
                if (pt.getFollowNum() == 0) {
                    //基线
                    s.setRecordNum(1);
                } else {
                    //随访
                    s.setRecordNum(2);
                }
            }
            //后端失访和前端状态对应不上，后台1,2,3，api接口用0,1,2
            if("1".equals(s.getLoseType())){
                s.setLoseType("0");
            }else if("2".equals(s.getLoseType())){
                s.setLoseType("1");
            }else if("3".equals(s.getLoseType())){
                s.setLoseType("2");
            }else if("4".equals(s.getLoseType())){
                s.setLoseType("3");
            }
            PatientRecord patientRecord = setFollowTipAndGetNextNodeInfo(s, patientInfo, patientRecords,
                    maxFollowIdPr, patientRecordTemplates);

            if (!Objects.isNull(patientRecord)&&!"4".equals(patientInfo.getPatientStatus())) {
                nextNodeAtomic.set(patientRecord);
            }
        });
        ResearchInfo ri = researchInfoMapper.selectResearchInfoById(record.getResearchId());
        if (!Objects.isNull(nextNodeAtomic.get()) && nextNodeAtomic.get().getResearchId() != null && !"0".equals(ri.getResearchStatus())) {
            patientRecords.add(nextNodeAtomic.get());
        }
        List<PatientRecord> patientRecordList =
                patientRecords.stream().sorted(Comparator.comparing(PatientRecord::getRecordId).reversed()).collect(Collectors.toList());


        PatientInfo pi = patientInfoMapper.selectPatientInfoById(record.getPatientId());
        resultVo.setPatientName(pi.getPatientName());
        resultVo.setPatientId(pi.getPatientId());
        resultVo.setPersonId(pi.getPersonId());

        resultVo.setResearchName(ri.getProjectName());
        resultVo.setList(patientRecordList);
        result.setData(resultVo);
        return result;
    }

    private DchealthCloudResponseEntity<PatientRecordListVo> addOldFollowNodes(PatientRecord record,
                                                                               DchealthCloudResponseEntity<PatientRecordListVo> result,
                                                                               List<PatientRecord> patientRecords) {
        PatientInfo p = patientInfoMapper.selectPatientInfoById(record.getPatientId());
        if (Objects.isNull(p)) {
            return result;
        }
        ;
        if (Objects.isNull(p.getAccountId())) {
            L.error("患者accountID为空:{}", p);
            return result;
        }
        PatientInfo patient = new PatientInfo();
        patient.setResearchId(record.getResearchId());
        patient.setAccountId(p.getAccountId());
        List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoList(patient);
        if (!CollectionUtils.isEmpty(patientInfos) && patientInfos.size() > 1) {
            queryOldResearchNodes(record, patientRecords);
        }
        return null;
    }

    private void queryOldResearchNodes(PatientRecord patientRecord, List<PatientRecord> patientRecords) {
        PatientInfo patientInfo = patientInfoMapper.selectPatientInfoById(patientRecord.getPatientId());
        if (!Objects.isNull(patientInfo) && !Objects.isNull(patientInfo.getOldPatientId())) {
            PatientRecord record = new PatientRecord();
            record.setResearchId(patientRecord.getResearchId());
            record.setPatientId(patientInfo.getOldPatientId());
            List<PatientRecord> patientRecordList = patientRecordMapper.selectPatientRecordList(record);
            if(!CollectionUtils.isEmpty(patientRecordList)){
                for (PatientRecord patientRecord1 : patientRecordList) {
                    patientRecord1.setIsEditable(false);
                    changeResearchTip(patientRecord1, patientInfo);
                    patientRecords.add(patientRecord1);
                }
            }

            PatientInfo info = patientInfoMapper.selectPatientInfoById(patientInfo.getOldPatientId());
            if (!Objects.isNull(info) && !Objects.isNull(info.getOldPatientId())) {
                PatientRecord pr = new PatientRecord();
                pr.setResearchId(patientRecord.getResearchId());
                pr.setPatientId(info.getPatientId());
                queryOldResearchNodes(pr, patientRecords);
            }
        }

    }

    public void setFollowTipAndGetNextNodeInfo(PatientRecord patientRecord, PatientInfo patientInfo,
                                               HttpServletRequest request) {
        if (!Objects.isNull(request) && StringUtils.equals(request.getHeader("appType"), BaseConstant.PATIENT_POINT)) {
            PatientRecord patientRecord1 = new PatientRecord();
            if (Objects.equals(patientRecord.getFollowTipStatus(),
                    FollowTipStatusEnum.FOLLOW_UP_OPINION_REMINDER.getIndex()) && StringUtils.equals(patientRecord.getFollowStatus(), "3") && StringUtils.equals(patientRecord.getEvaluateStatus(), "1")) {
                patientRecord1.setRecordId(patientRecord.getRecordId());
                patientRecord1.setFollowTipStatus(-1);
//                patientRecord1.setEvaluateStatus("2");
                patientRecordMapper.updatePatientRecord(patientRecord1);
            }
            if (Objects.equals(patientRecord.getFollowTipStatus(), FollowTipStatusEnum.CHANGE_QUEUE_ALERT.getIndex()) && StringUtils.equals(patientInfo.getAdjustType(), "2")) {
//            if (Objects.equals(patientRecord.getFollowTipStatus(), FollowTipStatusEnum.CHANGE_QUEUE_ALERT.getIndex
//            ()) ) {
                patientRecord1.setRecordId(patientRecord.getRecordId());
                patientRecord1.setFollowTipStatus(-1);
                patientRecordMapper.updatePatientRecord(patientRecord1);
            }

        }
    }

    public PatientRecord setFollowTipAndGetNextNodeInfo(PatientRecord patientRecord, PatientInfo patientInfo,
                                                        List<PatientRecord> patientRecords,
                                                        Optional<PatientRecord> maxFollowIdPr,
                                                        List<PatientRecordTemplate> patientRecordTemplates) {
        Date currentTime = new Date();
        // 0基线创建提醒
        if (Objects.equals(patientRecord.getFollowTipStatus(), FollowTipStatusEnum.BASELINE_REMINDER.getIndex())) {
            patientRecord.setFollowTipStatusDesc(FollowTipStatusEnum.getStatusDesc(patientRecord.getFollowTipStatus()));
        }
        // 当前时间小于随访时间[基线创建完随访时间还没到 || 变更了随访时间新的随访时间还没到 || 定时任务生成的随访节点,随访时间还没到]
        if (DateUtils.dateCompare(currentTime, patientRecord.getVisitTime())) {
            beforeVisitTime(patientRecord, currentTime, patientRecords, patientInfo, patientRecordTemplates);
        } else {
            afterVisitTime(patientRecord, patientInfo, patientRecords, patientRecordTemplates);
        }
        //对于切换了队列的队列，旧队列不展示随访提醒信息
        if (!Objects.isNull(patientInfo.getPlanResearchId()) && !Objects.equals(patientInfo.getPlanResearchId(),
                patientInfo.getResearchId()) && !Objects.equals(patientRecord.getFollowTipStatus(),
                FollowTipStatusEnum.CHANGE_QUEUE_ALERT.getIndex())) {
            patientRecord.setFollowTipStatusDesc(null);
            patientRecord.setIsEditable(false);
        }
        return getNextFollowNodeInfo(patientInfo, patientRecord, maxFollowIdPr, patientRecordTemplates);

    }

    private PatientRecord getNextFollowNodeInfo(PatientInfo patientInfo, PatientRecord patientRecord,
                                                Optional<PatientRecord> maxFollowIdPatientRecOpt,
                                                List<PatientRecordTemplate> patientRecordTemplates) {
        //对于已经切换了队列的，不再展示虚拟节点
        if (!Objects.isNull(patientInfo.getPlanResearchId()) && !Objects.equals(patientInfo.getPlanResearchId(),
                patientInfo.getResearchId())) {
            return null;
        }
        if (!CollectionUtils.isEmpty(patientRecordTemplates)) {
            //  对大于目前patientRecord有的最大节点的followId 紧接着的随访节点设置下提示信息
            if (maxFollowIdPatientRecOpt.isPresent() && Objects.equals(maxFollowIdPatientRecOpt.get().getRecordId(),
                    patientRecord.getRecordId())) {
                // 对不限制频次的周期设置，直接就设置下一个节点
                Optional<PatientRecordTemplate> patientRecordTemplate =
                        patientRecordTemplates.stream().filter(prt -> Objects.equals(prt.getFollowId(),
                                patientRecord.getFollowId())).findFirst();
                boolean noLimitFrequency = false;
                if (patientRecordTemplate != null && patientRecordTemplate.isPresent()) {
                    noLimitFrequency = StringUtils.equals(patientRecordTemplate.get().getFollowFrequency(), "2");
                }
                if (noLimitFrequency) {
                    return newNextPatientRecord(patientRecord, patientRecordTemplate);
                }
                // 对限制频次的周期设置,要考虑边界点
                else {
                    // [设置的虚拟节点需要满足随访周期设置的节点边界]
                    Stream<PatientRecordTemplate> patientRecordTemplateStream =
                            patientRecordTemplates.stream().filter(prt -> prt.getFollowId() >= patientRecord.getFollowId());
                    Optional<PatientRecordTemplate> minPatRecTemplateOP =
                            patientRecordTemplateStream.min(Comparator.comparingLong(PatientRecordTemplate::getFollowId));
                    if (minPatRecTemplateOP.isPresent()) {
                        //   周期配置设置了随访->当最大随访节点不超过周期配置边界时，继续设置一个虚拟随访节点
                        if (minPatRecTemplateOP.get().getFollowNum() >= 0) {
                            // PatientRecordTemplate::getFollowNum  第几次随访，基线是0，随访从1开始]   [ patientRecord.getStageNum
                            // ()基线是1
                            // 随访从1开始]
                            //只配置了基线，就不设置
                            if (minPatRecTemplateOP.get().getFollowNum() == 0 && patientRecordTemplates.size() == 1) {
                                return null;
                            }
                            //基线直接设置
                            if (!Objects.isNull(patientRecord.getRecordNum())) {
                                if (StringUtils.equals("基线", patientRecord.getName()))  //  minPatRecTemplateOP.get()
                                // .getFollowNum()==0
                                {
                                    Optional<PatientRecordTemplate> min =
                                            patientRecordTemplates.stream().filter(prt -> prt.getFollowId() > patientRecord.getFollowId()).min(Comparator.comparingLong(PatientRecordTemplate::getFollowId));
                                    minPatRecTemplateOP = min;
                                    return newNextPatientRecord(patientRecord, minPatRecTemplateOP);
                                } else {
                                    PatientRecord patientRecord1 = getNextFollowNode(patientRecord,
                                            minPatRecTemplateOP);
                                    if (patientRecord1 != null) return patientRecord1;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private PatientRecord getNextFollowNode(PatientRecord patientRecord,
                                            Optional<PatientRecordTemplate> maxPatRecTemplateOP) {
        // PatientRecordTemplate::getFollowNum 算上基线从0开始]   [ patientRecord.getStageNum()基线是1 随访从1开始]
        //随访保证边界
        if (maxPatRecTemplateOP.isPresent() && maxPatRecTemplateOP.get().getFollowId() > patientRecord.getFollowId()) {
            return newNextPatientRecord(patientRecord, maxPatRecTemplateOP);

        } else if (maxPatRecTemplateOP.isPresent() && Objects.equals(maxPatRecTemplateOP.get().getFollowId(),
                patientRecord.getFollowId()) && (maxPatRecTemplateOP.get().getFollowTotal() == null || (maxPatRecTemplateOP.get().getFollowTotal() > patientRecord.getStageNum()))) {
            return newNextPatientRecord(patientRecord, maxPatRecTemplateOP);
        }
        return null;
    }

    private PatientRecord newNextPatientRecord(PatientRecord patientRecord,
                                               Optional<PatientRecordTemplate> maxPatRecTemplateOP) {
        PatientRecord nextNode = new PatientRecord();
        nextNode.setResearchId(patientRecord.getResearchId());
        nextNode.setRecordId(patientRecord.getRecordId() + 1000000);
        nextNode.setPatientId(patientRecord.getPatientId());
        setFollowName(patientRecord, nextNode);
        Date nextNodeVisitTime = getNextNodeVisitTime(patientRecord, maxPatRecTemplateOP);
        nextNode.setVisitTime(nextNodeVisitTime);
        nextNode.setFollowTipStatusDesc(FollowTipStatusEnum.getStatusDesc(FollowTipStatusEnum.FOLLOW_UP_TIME_REMINDER.getIndex(), DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nextNodeVisitTime)));
//        if (DateUtils.dateCompare(nextNodeVisitTime, new Date())) {
//            nextNode.setFollowTipStatusDesc(FollowTipStatusEnum.FOLLOW_UP_VISIT_DUE_REMINDER.getName()
//            );
//        }

        nextNode.setFollowTipStatus(FollowTipStatusEnum.FOLLOW_UP_TIME_REMINDER.getIndex());
        return nextNode;
    }

    private static void setFollowName(PatientRecord patientRecord, PatientRecord nextNode) {
        //基线
        if (patientRecord.getRecordNum() == 1) {
            nextNode.setName("随访01");
        }
        //随访
        else {
            if (patientRecord.getStageAddNum() < 9) {
                nextNode.setName("随访0" + (patientRecord.getStageAddNum() + 1));
            } else {
                nextNode.setName("随访" + (patientRecord.getStageAddNum() + 1));
            }
        }
    }

    private void afterVisitTime(PatientRecord patientRecord, PatientInfo patientInfo,
                                List<PatientRecord> patientRecords,
                                List<PatientRecordTemplate> patientRecordTemplates) {
        // 10 所有随访完成后提示
        Optional<PatientRecord> maxFollowStatusOpt =
                patientRecords.stream().max(Comparator.comparingLong(PatientRecord::getRecordId));
        Optional<PatientRecordTemplate> maxPatRecTemplateOP =
                patientRecordTemplates.stream().max(Comparator.comparingLong(PatientRecordTemplate::getFollowId));
        if (maxFollowStatusOpt.isPresent() && StringUtils.equals("3", maxFollowStatusOpt.get().getFollowStatus())
                && Objects.equals(maxFollowStatusOpt.get().getRecordId(), patientRecord.getRecordId())
                && maxPatRecTemplateOP.isPresent() && Objects.equals(maxPatRecTemplateOP.get().getFollowId(), patientRecord.getFollowId())
                && Objects.equals(maxPatRecTemplateOP.get().getFollowTotal(), patientRecord.getStageNum())) {
            patientRecord.setFollowTipStatus(FollowTipStatusEnum.REMINDER_OF_ALL_FOLLOW_UP_VISITS_COMPLETED.getIndex());
            patientRecord.setFollowTipStatusDesc(FollowTipStatusEnum.getStatusDesc(patientRecord.getFollowTipStatus()));
            PatientRecord patientRecord1 = new PatientRecord();
            patientRecord1.setRecordId(patientRecord.getRecordId());
            patientRecord1.setFollowTipStatus(FollowTipStatusEnum.REMINDER_OF_ALL_FOLLOW_UP_VISITS_COMPLETED.getIndex());
            patientRecordMapper.updatePatientRecord(patientRecord1);
            return;
        }
        // 2随访到期提醒
        if (Objects.equals(patientRecord.getFollowTipStatus(),
                FollowTipStatusEnum.FOLLOW_UP_TIME_REMINDER.getIndex())) {
            patientRecord.setFollowTipStatus(2);
            patientRecord.setFollowTipStatusDesc(FollowTipStatusEnum.getStatusDesc(FollowTipStatusEnum.FOLLOW_UP_VISIT_DUE_REMINDER.getIndex()));
            patientRecord.setIsSeeDetail(1);
            patientRecordMapper.updatePatientRecord(patientRecord);
            return;
        }
        if (Objects.equals(patientRecord.getFollowTipStatus(),
                FollowTipStatusEnum.FOLLOW_UP_VISIT_DUE_REMINDER.getIndex())) {
            patientRecord.setFollowTipStatusDesc(FollowTipStatusEnum.getStatusDesc(FollowTipStatusEnum.FOLLOW_UP_VISIT_DUE_REMINDER.getIndex()));
            return;
        }
        // 3随访提交提醒
        if (Objects.equals(patientRecord.getFollowTipStatus(),
                FollowTipStatusEnum.FOLLOW_UP_VISIT_SUBMISSION_REMINDER.getIndex())) {
            patientRecord.setFollowTipStatusDesc(FollowTipStatusEnum.getStatusDesc(patientRecord.getFollowTipStatus()));
            return;
        }
        // 4 随访质疑提醒
        if (Objects.equals(patientRecord.getFollowTipStatus(),
                FollowTipStatusEnum.FOLLOW_UP_QUESTIONING_REMINDER.getIndex()) && StringUtils.equals(patientRecord.getQuestionStatus(), "1")) {
            patientRecord.setFollowTipStatusDesc(FollowTipStatusEnum.getStatusDesc(patientRecord.getFollowTipStatus()));
            return;

        }
        //5 随访意见提醒
        if (Objects.equals(patientRecord.getFollowTipStatus(),
                FollowTipStatusEnum.FOLLOW_UP_OPINION_REMINDER.getIndex()) && StringUtils.equals(patientRecord.getFollowStatus(), "3")
                && StringUtils.equals(patientRecord.getEvaluateStatus(), "1")) {
            patientRecord.setFollowTipStatusDesc(FollowTipStatusEnum.getStatusDesc(patientRecord.getFollowTipStatus()));
            return;
        }
        // 6变更队列提醒
        if (changeResearchTip(patientRecord, patientInfo)) return;
        // 7失访提醒
        if (Objects.equals(patientRecord.getFollowTipStatus(), FollowTipStatusEnum.MISSING_VISIT_REMINDER.getIndex()) && StringUtils.equals(patientRecord.getOverTimeHandleStatus(), "3")) {
            String loseReason = patientRecord.getLoseReason();
            if(StringUtils.isBlank(loseReason) && "0".equals(patientRecord.getLoseType())){
                loseReason = "不再参与";
            }else if(StringUtils.isBlank(loseReason) && "1".equals(patientRecord.getLoseType())){
                loseReason = "失联";
            }
            patientRecord.setFollowTipStatusDesc(FollowTipStatusEnum.getStatusDesc(patientRecord.getFollowTipStatus()
                    , loseReason));
            return;

        }
        //8 变更随访时间后新的随访时间到期
        if (Objects.equals(patientRecord.getFollowTipStatus(),
                FollowTipStatusEnum.CHANGE_OF_FOLLOW_UP_TIME_REMINDER.getIndex()) && StringUtils.equals(patientRecord.getOverTimeHandleStatus(), "1")) {
            // 设为2
            patientRecord.setFollowTipStatus(FollowTipStatusEnum.FOLLOW_UP_VISIT_DUE_REMINDER.getIndex());
            patientRecord.setFollowTipStatusDesc(FollowTipStatusEnum.getStatusDesc(patientRecord.getFollowTipStatus()));

            PatientRecord patientRecord1 = new PatientRecord();
            patientRecord1.setRecordId(patientRecord.getRecordId());
            patientRecord1.setOverTimeHandleStatus("0");
            patientRecord1.setFollowTipStatus(FollowTipStatusEnum.FOLLOW_UP_VISIT_DUE_REMINDER.getIndex());
            patientRecordMapper.updatePatientRecord(patientRecord1);
            return;
        }
        // 9 忽略本次随访提醒
        if (Objects.equals(patientRecord.getFollowTipStatus(),
                FollowTipStatusEnum.IGNORE_THIS_FOLLOW_UP_REMINDER.getIndex()) && StringUtils.equals(patientRecord.getOverTimeHandleStatus(), "2")) {
            patientRecord.setFollowTipStatusDesc(FollowTipStatusEnum.getStatusDesc(patientRecord.getFollowTipStatus()));
        }
    }

    private boolean changeResearchTip(PatientRecord patientRecord, PatientInfo patientInfo) {
        if (Objects.equals(patientRecord.getFollowTipStatus(), FollowTipStatusEnum.CHANGE_QUEUE_ALERT.getIndex())) {
            String patientAndResearchStr = "";
            //通过旧的patient_id 查出新的patient_id
            // 通过新的patient_id 查出新的patient_info ,从而获得 patient_id and research_id
            //通过patient_id and research_id，获取List<patientRecord> patientRecords.get(0).getRecordId()
            PatientInfo newPatientInfo = patientInfoMapper.selectPatientInfoByOldId(patientInfo.getPatientId());
            if (!Objects.isNull(newPatientInfo) && !Objects.isNull(newPatientInfo.getPatientId())) {
                DiseaseResearchPatientDto diseaseResearchPatient =
                        researchInfoMapper.selectDiseaseResearchPatientByPatientId(newPatientInfo.getPatientId());
                if (!Objects.isNull(diseaseResearchPatient)) {
                    patientAndResearchStr =
                            diseaseResearchPatient.getDiseaseName() + "病种-" + diseaseResearchPatient.getResearchName() + "队列";
                }
                patientRecord.setFollowTipStatusDesc(FollowTipStatusEnum.getStatusDesc(patientRecord.getFollowTipStatus(), patientAndResearchStr));
                return true;
            }

        }
        return false;
    }


    private void beforeVisitTime(PatientRecord patientRecord, Date currentTime, List<PatientRecord> patientRecords,
                                 PatientInfo patientInfo, List<PatientRecordTemplate> patientRecordTemplates) {
        // 为了以应对当前时间小于随访时间，但是是同一天，在这一天中，继续走剩下的的随访状态。
        if (DateUtils.judgeTwoTimeIsOneDay(currentTime, patientRecord.getVisitTime())) {
            afterVisitTime(patientRecord, patientInfo, patientRecords, patientRecordTemplates);
        }
        // 1随访时间提醒
        if (Objects.equals(patientRecord.getFollowTipStatus(),
                FollowTipStatusEnum.FOLLOW_UP_TIME_REMINDER.getIndex())) {
            // 区分是定时任务生成的随访节点，还是基线刚创建完随访时间还没到
            //定时任务生成的随访节点
            if (patientRecords.size() > 1) {
                patientRecord.setFollowTipStatusDesc(FollowTipStatusEnum.getStatusDesc(FollowTipStatusEnum.FOLLOW_UP_TIME_REMINDER.getIndex(), DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, patientRecord.getVisitTime())));
                //对于虽然当前时间小于随访时间，但是都是同一天的时间。提示信息展示为：
                if (DateUtils.judgeTwoTimeIsOneDay(currentTime, patientRecord.getVisitTime())) {
                    patientRecord.setFollowTipStatusDesc(FollowTipStatusEnum.getStatusDesc(FollowTipStatusEnum.FOLLOW_UP_VISIT_DUE_REMINDER.getIndex()));
                }
            } else {
                if (!Objects.isNull(patientRecord.getVisitTime())) {
                    Optional<PatientRecordTemplate> maxPatRecTemplateOP =
                            patientRecordTemplates.stream().max(Comparator.comparingLong(PatientRecordTemplate::getFollowId));
                    Date firstFollowUpTime = getNextNodeVisitTime(patientRecord, maxPatRecTemplateOP);
                    if (DateUtils.dateCompare(currentTime, firstFollowUpTime)) {
                        patientRecord.setFollowTipStatusDesc(FollowTipStatusEnum.getStatusDesc(FollowTipStatusEnum.FOLLOW_UP_TIME_REMINDER.getIndex(), DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, firstFollowUpTime)));
                        return;
                    }
                    //最后一次随访，就直接设置 -1
                    Optional<PatientRecord> maxFollowStatusOption =
                            patientRecords.stream().max(Comparator.comparing(PatientRecord::getFollowStatus));
                    if (maxFollowStatusOption.isPresent() && StringUtils.equals("3",
                            maxFollowStatusOption.get().getFollowStatus()) && StringUtils.equals(patientInfo.getIsEndCreateRecord(), "1")) {
                        patientRecord.setFollowTipStatusDesc(FollowTipStatusEnum.NO_STATUS.getName());
                    }
                }
            }
        }
        // 8变更了随访时间，新的随访时间还没到
        if (Objects.equals(patientRecord.getFollowTipStatus(),
                FollowTipStatusEnum.CHANGE_OF_FOLLOW_UP_TIME_REMINDER.getIndex()) && StringUtils.equals(patientRecord.getOverTimeHandleStatus(), "1")) {
            {
                patientRecord.setFollowTipStatusDesc(FollowTipStatusEnum.getStatusDesc(patientRecord.getFollowTipStatus()));
            }
        }
    }


    private Date getNextNodeVisitTime(PatientRecord patientRecord,
                                      Optional<PatientRecordTemplate> maxPatRecTemplateOP) {

        try {
            if (!Objects.isNull(patientRecord.getFollowId()) && maxPatRecTemplateOP.isPresent()) {
                PatientRecordTemplate patientRecordTemplate = maxPatRecTemplateOP.get();
                if (!Objects.isNull(patientRecordTemplate)) {

                    Integer followInterval = patientRecordTemplate.getFollowInterval();
                    String followIntervalUnit = patientRecordTemplate.getFollowIntervalUnit();
                    int calendarUnit = 0;
                    //日
                    if (Objects.equals(Integer.parseInt(followIntervalUnit), 1)) {
                        calendarUnit = DateUtils.CALENDAR_DATE;
                    }
                    //周
                    if (Objects.equals(Integer.parseInt(followIntervalUnit), 2)) {
                        calendarUnit = DateUtils.CALENDAR_WEEK_OF_MONTH;
                    }
                    //月
                    if (Objects.equals(Integer.parseInt(followIntervalUnit), 3)) {
                        calendarUnit = DateUtils.CALENDAR_MONTH;
                    }
                    return DateUtils.addDateCalculate(patientRecord.getVisitTime(), DateUtils.YYYY_MM_DD_HH_MM_SS,
                            calendarUnit, followInterval);
                }
            }
        } catch (ParseException e) {
            L.error("patientRecord:{}", patientRecord);
            L.error("获取下一个节点的随访时间异常:{}", e.toString());
            throw new RuntimeException(e);
        }
        return null;
    }

    public DchealthCloudResponseEntity<List<ResearchInfo>> selectPatientResearchList(ResearchInfo researchInfo) {
        DchealthCloudResponseEntity<List<ResearchInfo>> result = new DchealthCloudResponseEntity<>();
        ResearchInfo researchInfo1 = researchInfoMapper.selectResearchInfoById(researchInfo.getId());
        if (Objects.isNull(researchInfo1) || Objects.isNull(researchInfo1.getDiseaseTypeId())) {
            result.setData(new ArrayList<>());
        } else {
            ResearchInfo research = new ResearchInfo();
            research.setDiseaseTypeId(researchInfo1.getDiseaseTypeId());
            result.setData(researchInfoMapper.selectResearchInfoList(research));
        }
        return result;
    }

    public List<QuestionRecordVO> questionList(Long recordId) {
        List<QuestionRecord> questionRecords = questionRecordMapper.selectQuestionRecordByRecordId(recordId);
        if (questionRecords != null && !questionRecords.isEmpty()) {
            return questionRecords.stream().map(QuestionRecordVO::new).collect(Collectors.toList());
        }
        return null;
    }

//    /**
//     * 查询患者健康档案和体检报告列表
//     *
//     * @param patientRecord
//     * @return
//     */
//    public PatientHealthRecordVO patientHealthList(PatientHealthRecord patientRecord) {
//        PatientHealthRecordVO patientHealthRecordVO = new PatientHealthRecordVO();
//        PatientInfo patientInfo = patientInfoMapper.selectPatientInfoById(patientRecord.getPatientId());
//        if (patientInfo != null) {
//            patientRecord.setPersonId(patientInfo.getPersonId());
//            patientHealthRecordVO.setPatientId(patientInfo.getPatientId());
//            patientHealthRecordVO.setPatientName(patientInfo.getPatientName());
//        }
//        //健康随访列表
//        List<PatientHealthRecord> patientHealthRecordList =
//                patientHealthRecordMapper.selectPatientHealthRecordByBaseIdList(patientRecord);
//        if (patientHealthRecordList != null && patientHealthRecordList.size() > 0) {
//            patientHealthRecordVO.setPatientHealthRecordList(patientHealthRecordList);
//        }
//        return patientHealthRecordVO;
//    }

   

    @Transactional
    public DchealthCloudResponseEntity<PatientAccountInfoVO> bindPatientDetail(BindPatientDetailForm bindPatientForm) {
        DchealthCloudResponseEntity<PatientAccountInfoVO> result = new DchealthCloudResponseEntity<>();
        PatientAccount patientAccount =
                patientAccountMapper.selectPatientAccountByAccountId(bindPatientForm.getAccountId());
        if (patientAccount == null) {
            result.setCode(CroApiCodeConstant.USER_NOT_EXIST.getCode());
            return result;
        }
        if ("1".equals(patientAccount.getStatus())) {
            result.setCode(CroApiCodeConstant.BLOCK_UP_ERROR.getCode());
            return result;
        }
        PatientAccountInfoVO patientAccountInfoVO = new PatientAccountInfoVO();
        MpPerson patientBaseInfo = mpPersonMapper.selectPersonByAccountId(bindPatientForm.getAccountId());
        if (patientBaseInfo != null ) {
            patientAccountInfoVO.setPersonId(patientBaseInfo.getPersonId());
            patientAccountInfoVO.setPersonName(patientBaseInfo.getPersonName());
            patientAccountInfoVO.setBindStatus(patientAccount.getBindStatus());
            patientAccountInfoVO.setSex(HealthUtils.getSex(patientBaseInfo.getCardId()));
            patientAccountInfoVO.setIdCard(patientBaseInfo.getCardId());
            patientAccountInfoVO.setMobile(patientBaseInfo.getPhoneNumber());
        }
        patientAccountInfoVO.setAccountId(bindPatientForm.getAccountId());

        result.setData(patientAccountInfoVO);
        return result;
    }

    /**
     * 扫码申请队列
     *
     * @param applyResearchForm
     * @return
     */
    @Transactional
    public DchealthCloudResponseEntity<PatientAccountInfoVO> scanCodeResearch(ApplyResearchForm applyResearchForm) {
        DchealthCloudResponseEntity<PatientAccountInfoVO> result =
                new DchealthCloudResponseEntity<PatientAccountInfoVO>();
        PatientAccountInfoVO patientAccountInfoVO = new PatientAccountInfoVO();
        PatientAccount patientAccount =
                patientAccountMapper.selectPatientAccountByAccountId(applyResearchForm.getAccountId());
        if (patientAccount == null) {
            result.setCode(CroApiCodeConstant.USER_NOT_EXIST.getCode());
            return result;
        }
        if ("1".equals(patientAccount.getStatus())) {
            result.setCode(CroApiCodeConstant.BLOCK_UP_ERROR.getCode());
            return result;
        }
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setCreateTime(new Date());
        patientInfo.setAccountId(applyResearchForm.getAccountId());
        //patientInfo.setBaseId(applyResearchForm.getPatientBaseAccountId());
        patientInfo.setResearchId(applyResearchForm.getResearchId());
        patientInfo.setDeptId(applyResearchForm.getDeptId());
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(applyResearchForm.getResearchId());
        if (researchInfo == null) {
            result.setCode(CroApiCodeConstant.PROJECT_NOT_EXIST.getCode());
            return result;
        }
        patientInfo.setEnrollmentStatus("1");
        List<PatientInfo> patientInfoList = patientInfoMapper.selectPatientInfoList(patientInfo);
        if (patientInfoList.size() > 0) {
            PatientInfo patientInfo1 = patientInfoList.get(0);
            patientAccountInfoVO.setPersonName(patientInfo1.getPatientName());
            patientAccountInfoVO.setEnrollmentHandlerStatus(patientInfo1.getEnrollmentHandlerStatus());
            patientAccountInfoVO.setEnrollmentStatus(patientInfo1.getEnrollmentStatus());
        } else {
            MpPerson patientBaseInfo = mpPersonMapper.selectPersonByAccountId(applyResearchForm.getAccountId());
            if (patientBaseInfo != null) {
                patientAccountInfoVO.setPersonName(patientBaseInfo.getPersonName());
            }
            patientAccountInfoVO.setEnrollmentHandlerStatus("0");//未入组
            patientAccountInfoVO.setEnrollmentStatus("0");
        }

//        genBasePatientRecord(applyResearchForm, patientInfo);

        patientAccountInfoVO.setBindStatus(patientAccount.getBindStatus());//是否绑定
        result.setData(patientAccountInfoVO);
        return result;
    }

    @Transactional
    public DchealthCloudResponseEntity<PatientAccountInfoVO> researchStatusInfo(ReSearchForm reSearchForm) {
        DchealthCloudResponseEntity<PatientAccountInfoVO> result =
                new DchealthCloudResponseEntity<PatientAccountInfoVO>();
        PatientAccountInfoVO patientAccountInfoVO = new PatientAccountInfoVO();
        PatientAccount patientAccount =
                patientAccountMapper.selectPatientAccountByAccountId(reSearchForm.getAccountId());
        if (patientAccount == null) {
            result.setCode(CroApiCodeConstant.USER_NOT_EXIST.getCode());
            return result;
        }
        if ("1".equals(patientAccount.getStatus())) {
            result.setCode(CroApiCodeConstant.BLOCK_UP_ERROR.getCode());
            return result;
        }
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setCreateTime(new Date());
        patientInfo.setAccountId(reSearchForm.getAccountId());
        //patientInfo.setBaseId(applyResearchForm.getPatientBaseAccountId());
        patientInfo.setResearchId(reSearchForm.getResearchId());
//        patientInfo.setDeptId(applyResearchForm.getDeptId());
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(reSearchForm.getResearchId());
        if (researchInfo == null||"1".equals(researchInfo.getDelFlag())) {
            result.setCode(CroApiCodeConstant.PROJECT_NOT_EXIST.getCode());
            return result;
        }
        patientInfo.setEnrollmentStatus("1");
        List<PatientInfo> patientInfoList = patientInfoMapper.selectPatientInfoList(patientInfo);
        if (patientInfoList.size() > 0) {
            PatientInfo patientInfo1 = patientInfoList.get(0);
            patientAccountInfoVO.setPersonName(patientInfo1.getPatientName());
            patientAccountInfoVO.setEnrollmentHandlerStatus(patientInfo1.getEnrollmentHandlerStatus());
            patientAccountInfoVO.setEnrollmentStatus(patientInfo1.getEnrollmentStatus());
        } else {
            MpPerson patientBaseInfo = mpPersonMapper.selectPersonByAccountId(reSearchForm.getAccountId());
            if (patientBaseInfo != null) {
                patientAccountInfoVO.setPersonName(patientBaseInfo.getPersonName());
            }
            patientAccountInfoVO.setEnrollmentHandlerStatus("0");//未入组
            patientAccountInfoVO.setEnrollmentStatus("0");
        }

//        genBasePatientRecord(applyResearchForm, patientInfo);

        patientAccountInfoVO.setBindStatus(patientAccount.getBindStatus());//是否绑定
        result.setData(patientAccountInfoVO);
        return result;
    }

    private void genBasePatientRecord(ApplyResearchForm applyResearchForm, PatientInfo patientInfo) {
        List<PatientInfo> patientInfoList = patientInfoMapper.selectPatientInfoList(patientInfo);
        if (patientInfoList.size() > 0) {
            PatientInfo compeletetPatientInfo = patientInfoList.get(0);
            //生成基线
            Long followId = null;
            List<PatientRecordTemplate> patientRecordTemplates =
                    patientRecordTemplateMapper.selectPatientRecordTemplateByResearchId(applyResearchForm.getResearchId());
            PatientRecord patientRecord = new PatientRecord();
            if (patientRecordTemplates.size() > 0) {
                followId = patientRecordTemplates.get(0).getFollowId();
                patientRecord.setLockVisitTime(compeletetPatientInfo.getCreateTime());
            }
            patientRecord.setFollowId(followId);
            patientRecord.setName("基线");
            patientRecord.setCreateTime(DateUtils.getNowDate());
            patientRecord.setResearchId(applyResearchForm.getResearchId());
            patientRecord.setPatientId(compeletetPatientInfo.getPatientId());
            patientRecord.setVisitTime(DateUtils.getNowDate());
            patientRecord.setFollowTipStatus(FollowTipStatusEnum.BASELINE_REMINDER.getIndex());
            patientRecord.setUpdateBy(applyResearchForm.getAccountId().toString());
            patientRecordMapper.insertPatientRecord(patientRecord);
        } else {
            L.error("patientInfoList 查询失败 ， patientInfo：{}", patientInfo);
        }

    }
    
    
    private MpPerson insertMpPerson(PatMasterIndex patMasterIndex) {
    	MpPerson  mpPerson  = new MpPerson();
    	mpPerson.setLocationId(dealOracleString(patMasterIndex.getMailingAddress()));
    	mpPerson.setCardId(patMasterIndex.getIdNo());
    	mpPerson.setCardType("1");
    	mpPerson.setCareSiteId("TDYY");
    	mpPerson.setContactsName(dealOracleString(patMasterIndex.getNextOfKin()));
    	
    	mpPerson.setContactsPhone(patMasterIndex.getNextOfKinPhone());
    	mpPerson.setPhoneNumber(patMasterIndex.getPhoneNumberHome());
    	mpPerson.setCreateTime(DateUtils.getNowDate());
    	mpPerson.setPersonName(dealOracleString(patMasterIndex.getName()));
    	mpPerson.setAddress(dealOracleString(patMasterIndex.getNextOfKinAddr()));
    	mpPerson.setPersonId(patMasterIndex.getPatientId());
    	if(StringUtils.isNotBlankAndNotEmpty(patMasterIndex.getIdNo())) {
            if(HealthUtils.getSex(patMasterIndex.getIdNo()).intValue() == 1){
                mpPerson.setGenderSourceValue("男");
            }else {
                mpPerson.setGenderSourceValue("女");
            }
    	}
    	mpPersonMapper.insertMpPerson(mpPerson);
    	return mpPerson;
    }
    
    
    private String dealOracleString(String s ) {
    	if(StringUtils.isNotBlankAndNotEmpty(s)) {
			try {
				String newStr = new String(s.getBytes("ISO-8859-1"),"GBK");
				L.error("newStr{} ", newStr);
	    		return newStr;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
    		
    	}
    	return null;
    }

    /**
     * 更新用户的手机号码
     * 此方法同时更新患者信息和MP个人信息中的手机号码
     *
     * @param form 包含人员ID和新手机号码的表单对象
     * @return 更新操作的结果，固定返回"success"
     */
    @Transactional(rollbackFor = Exception.class)
    public String updateMobile(MobileForm form) {
        // 更新患者手机号
        patientInfoMapper.updatePatientMobile(form.getPersonId(), form.getMobile());
        // 更新MP个人信息手机号
        mpPersonMapper.updateMobile(form.getPersonId(), form.getMobile());
        // 返回成功信息
        return "联系手机修改成功！";
    }

    public Object selectHealthManageList(PatientRecord patientRecord) {

        return null;
    }
}
