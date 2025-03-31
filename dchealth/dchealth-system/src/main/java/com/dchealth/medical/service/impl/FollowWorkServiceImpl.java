package com.dchealth.medical.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.account.domain.AccountNotice;
import com.dchealth.account.mapper.AccountNoticeMapper;
import com.dchealth.base.service.TencentService;
import com.dchealth.cms.domain.PatientRecordFilesForm;
import com.dchealth.common.DynamicSql;
import com.dchealth.common.FollowTipStatusEnum;
import com.dchealth.common.constant.Constants;
import com.dchealth.common.constant.FollowTipStatusConstants;
import com.dchealth.common.constant.HttpStatus;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.core.domain.model.LoginUser;
import com.dchealth.common.exception.ServiceException;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.common.utils.bean.BeanUtils;
import com.dchealth.compatibility.utils.HealthUtils;
import com.dchealth.dynamic.mapper.DynamicCommonMapper;
import com.dchealth.medical.domain.*;
import com.dchealth.medical.domain.form.*;
import com.dchealth.medical.domain.vo.*;
import com.dchealth.medical.mapper.*;
import com.dchealth.medical.service.IFollowWorkService;
import com.dchealth.medical.service.common.CommonService;
import com.dchealth.question.domain.QuestionRecord;
import com.dchealth.question.domain.QuestionRecordForm;
import com.dchealth.question.mapper.QuestionRecordFormMapper;
import com.dchealth.question.mapper.QuestionRecordMapper;
import com.dchealth.research.domain.*;
import com.dchealth.research.domain.constant.PropertyType;
import com.dchealth.research.domain.constant.ResearchContants;
import com.dchealth.research.mapper.ResearchDiseasesMapper;
import com.dchealth.research.mapper.ResearchInfoMapper;
import com.dchealth.research.mapper.ResearchOutboundMapper;
import com.dchealth.research.mapper.ResearchUserRoleMapper;
import com.dchealth.research.service.ISysPopNoticeService;
import com.dchealth.system.mapper.SysUserMapper;
import com.dchealth.template.domain.FMedicalExamSubject;
import com.dchealth.template.domain.FMedicalForm;
import com.dchealth.template.domain.FMedicalProperty;
import com.dchealth.template.mapper.FMedicalExamSubjectMapper;
import com.dchealth.template.mapper.FMedicalFormMapper;
import com.dchealth.template.mapper.FMedicalPropertyMapper;
import com.dchealth.warn.domain.WarnEarlyRecord;
import com.dchealth.warn.domain.WarnMedicalEarlyCondition;
import com.dchealth.warn.domain.vo.WarnConditionValueVO;
import com.dchealth.warn.mapper.WarnEarlyRecordMapper;
import com.dchealth.warn.mapper.WarnMedicalEarlyConditionMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class FollowWorkServiceImpl extends CommonService implements IFollowWorkService {

    private static final Logger log = LoggerFactory.getLogger(FollowWorkServiceImpl.class);

    @Autowired
    private ResearchDiseasesMapper researchDiseasesMapper;
    @Autowired
    private ResearchInfoMapper researchInfoMapper;
    @Autowired
    private ResearchUserRoleMapper researchUserRoleMapper;
    @Autowired
    private PatientInfoMapper patientInfoMapper;
    @Autowired
    private PatientRecordMapper patientRecordMapper;
    @Autowired
    private PatientRecordTemplateMapper patientRecordTemplateMapper;
    @Autowired
    private PatientRecordFilesMapper patientRecordFilesMapper;
    @Autowired
    private DynamicCommonMapper dynamicCommonMapper;
    @Autowired
    private DynamicSql dynamicSql;
    @Autowired
    private FMedicalFormMapper fMedicalFormMapper;
    @Autowired
    private FMedicalPropertyMapper fMedicalPropertyMapper;
    @Autowired
    private QuestionRecordFormMapper questionRecordFormMapper;
    @Autowired
    private QuestionRecordMapper questionRecordMapper;
    @Autowired
    private WarnEarlyRecordMapper warnEarlyRecordMapper;
    @Autowired
    private PatientRecordOperLogMapper patientRecordOperLogMapper;
    @Autowired
    private FMedicalExamSubjectMapper fMedicalExamSubjectMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private MpPersonMapper mpPersonMapper;
    @Autowired
    private PatientRecordOutboundMapper patientRecordOutboundMapper;


    @Autowired
    private AccountNoticeMapper accountNoticeMapper;

//    @Autowired
//    private UmpushService umpushService;

    @Autowired
    private ResearchPatientMapper researchPatientMapper;

    @Autowired
    private ResearchOutboundMapper researchOutboundMapper;

    @Autowired
    private WarnMedicalEarlyConditionMapper warnMedicalEarlyConditionMapper;

//    @Autowired
//    private TencentSmsSevice tencentSmsSevice;

    @Autowired
    private ISysPopNoticeService sysPopNoticeService;

    @Autowired
    private TencentService tencentService;

    ExecutorService executorService = new ThreadPoolExecutor(0, 6,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());


    @Override
    public PatientDetailVO details(FollowWorkDetailsForm followWorkDetailsForm) {
        PatientDetailVO patientDetailVO = new PatientDetailVO();
        patientBaseInfo(followWorkDetailsForm.getPatientId(),patientDetailVO,x->{});

        if(followWorkDetailsForm.getRecordId()==null){
            PatientRecord pr = patientRecordMapper.selectOldByPatientId(followWorkDetailsForm.getPatientId());
            followWorkDetailsForm.setRecordId(pr.getRecordId());
        }

        PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByRecordId(followWorkDetailsForm.getRecordId());
        //超窗不需要进行中状态，所有的处理均放在待处理
        /*        if("3".equals(followWorkDetailsForm.getDetailType()) && "1".equals(patientRecord.getOverTimeStatus())){
            //超窗查看详情且是待处理，需要修改成进行中
            patientRecord.setOverTimeStatus("2");
            patientRecordMapper.updatePatientRecord(patientRecord);
        }*/
        patientDetailVO.setSubmit(patientRecord.getIsSubmit() != null && Integer.valueOf("1").equals(patientRecord.getIsSubmit()));

        patientDetailVO.setFollowOpinion(patientRecord.getFollowOpinion());
        //app随访评价
        patientDetailVO.setEvaluateStatus(patientRecord.getEvaluateStatus());
        patientDetailVO.setEvaluatePrescription(patientRecord.getEvaluatePrescription());
        patientDetailVO.setEvaluateQuality(patientRecord.getEvaluateQuality());
        patientDetailVO.setEvaluateReply(patientRecord.getEvaluateReply());
        //后台质效评价
        //随访
        patientDetailVO.setEvaluateStatusBack(patientRecord.getEvaluateStatusBack());
        patientDetailVO.setEvaluatePrescriptionBack(patientRecord.getEvaluatePrescriptionBack());
        patientDetailVO.setEvaluateQualityBack(patientRecord.getEvaluateQualityBack());
        patientDetailVO.setEvaluateReplyBack(patientRecord.getEvaluateReplyBack());
        //超窗
        patientDetailVO.setOverEvaluateStatusBack(patientRecord.getOverEvaluateStatusBack());
        patientDetailVO.setOverEvaluatePrescriptionBack(patientRecord.getOverEvaluatePrescriptionBack());
        patientDetailVO.setOverEvaluateQualityBack(patientRecord.getOverEvaluateQualityBack());
        patientDetailVO.setOverEvaluateReplyBack(patientRecord.getOverEvaluateReplyBack());
        //告警
        patientDetailVO.setWarnEvaluateStatusBack(patientRecord.getWarnEvaluateStatusBack());
        patientDetailVO.setWarnEvaluatePrescriptionBack(patientRecord.getWarnEvaluatePrescriptionBack());
        patientDetailVO.setWarnEvaluateQualityBack(patientRecord.getWarnEvaluateQualityBack());
        patientDetailVO.setWarnEvaluateReplyBack(patientRecord.getWarnEvaluateReplyBack());

        patientDetailVO.setPatientId(patientRecord.getPatientId());
        patientDetailVO.setIgnoreType(patientRecord.getIgnoreType());
        patientDetailVO.setOverTimeHandleStatus(patientRecord.getOverTimeHandleStatus());
        if("3".equals(patientRecord.getOverTimeHandleStatus())){
            PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(patientRecord.getPatientId());
            patientDetailVO.setLossCause(patientInfo.getLossCause());
            patientDetailVO.setLossCauseOther(patientInfo.getLossCauseOther());
        }


        List<PatientRecordFiles> patientRecordFiles = patientRecordFilesMapper.selectPatientRecordFilesByRecordId(followWorkDetailsForm.getRecordId());
        Map<Long, List<String>> filesMap = null;
        if (patientRecordFiles != null && !patientRecordFiles.isEmpty()){
            filesMap = patientRecordFiles.stream()
                    .filter(f-> f.getPropertyId() != null)
                    .collect(Collectors.groupingBy(PatientRecordFiles :: getPropertyId,
                            Collectors.mapping(PatientRecordFiles :: getImageUrl, Collectors.toList())));
        }

        List<FMedicalForm> formList =fMedicalFormMapper.selectFMedicalFormByMedicalFollowId2(patientRecord.getFollowId());
        List<MedicalFormVO> medicalFormVOS = BeanUtils.copyList(formList, MedicalFormVO :: new);

        List<MedicalFormVO> medicalFormVOSResult = new ArrayList<>();
        List<MedicalFormVO> medicalFormExamVOSResult = new ArrayList<>();
        //判断是否存在组的情况
        Map<Long,List<MedicalFormVO>> medicalFormMap = new HashMap<>();
        if (medicalFormVOS != null && !medicalFormVOS.isEmpty()){
            medicalFormMap = medicalFormVOS.stream().filter( x-> x.getParentId()>0).collect(Collectors.groupingBy(MedicalFormVO::getParentId));
        }

        List<FMedicalProperty> properties = fMedicalPropertyMapper.selectFMedicalPropertyByFollowId(patientRecord.getFollowId());
        List<MedicalPropertyVO> medicalPropertyVOS = BeanUtils.copyList(properties, MedicalPropertyVO :: new);

        //查询所有的问卷
        List<FMedicalExamSubject> examSubjects = fMedicalExamSubjectMapper.selectFMedicalExamSubjectByFollowId(patientRecord.getFollowId());
        List<MedicalPropertyVO> medicalPropertyExamVOS = BeanUtils.copyList(examSubjects, MedicalPropertyVO :: new);


        if (medicalFormVOS != null && !medicalFormVOS.isEmpty()){
            Map<Long,List<MedicalPropertyVO>> map = null;
            if (medicalPropertyVOS != null && !medicalPropertyVOS.isEmpty()){
                map = medicalPropertyVOS.stream().collect(Collectors.groupingBy(MedicalPropertyVO :: getMedicalFormId));
            }

            Map<Long,List<MedicalPropertyVO>> exaMap = new HashMap<>();
            if (medicalPropertyExamVOS != null && !medicalPropertyExamVOS.isEmpty()){
                exaMap = medicalPropertyExamVOS.stream().collect(Collectors.groupingBy(MedicalPropertyVO :: getMedicalFormId));
            }

            QuestionRecordForm questionRecordForm = new QuestionRecordForm();
            questionRecordForm.setRecordId(followWorkDetailsForm.getRecordId());
            List<QuestionRecordForm> forms = questionRecordFormMapper.selectQuestionRecordFormList(questionRecordForm);
            Map<Long,List<QuestionRecordForm>> formMap = new HashMap<>();
            if (forms != null && !forms.isEmpty()){
                formMap = forms.stream().collect(Collectors.groupingBy(QuestionRecordForm :: getMedicalFormId));
            }
            for (MedicalFormVO medicalFormVO : medicalFormVOS) {
                if(medicalFormVO.getParentId()>0){
                    continue;
                }
                if (map != null && map.containsKey(medicalFormVO.getMedicalFormId()) && !"1".equals(medicalFormVO.getOpenMultiple())){
                    medicalFormVO.setPropertyVOS(map.get(medicalFormVO.getMedicalFormId()));
                }
                if (exaMap != null && exaMap.containsKey(medicalFormVO.getMedicalFormId()) && !"1".equals(medicalFormVO.getOpenMultiple())){
                    medicalFormVO.setPropertyExamVOS(exaMap.get(medicalFormVO.getMedicalFormId()));
                }
                if(medicalFormMap.containsKey(medicalFormVO.getMedicalFormId())){
                    List<MedicalFormVO> voList = medicalFormMap.get(medicalFormVO.getMedicalFormId());
                    if(voList.size()>0){
                        for(MedicalFormVO v:voList){
                            if (map != null && map.containsKey(v.getMedicalFormId()) ){
                                v.setPropertyVOS(map.get(v.getMedicalFormId()));
                            }
                            if(exaMap!=null && exaMap.containsKey(v.getMedicalFormId())){
                                v.setPropertyExamVOS(exaMap.get(v.getMedicalFormId()));
                            }
                        }
                        medicalFormVO.setChildMedicalFormVOS(voList);
                    }
                }

                if("3".equals(medicalFormVO.getFormType())){
                    medicalFormExamVOSResult.add(medicalFormVO);
                } else{
                    medicalFormVOSResult.add(medicalFormVO);
                }


                //随访工作站详情页面判断模板id是否默认选中
                if(formMap.containsKey(medicalFormVO.getMedicalFormId())){
                    medicalFormVO.setChecked("true");
                }
            }
        }

        // 处理预警源
        dealWarnSource(followWorkDetailsForm.getRecordId(), medicalFormVOSResult);

        List<DynamicCommonProperty> t = new ArrayList<>();
        DynamicForm dynamicForm = new DynamicForm();
        dynamicForm.setRecordId(followWorkDetailsForm.getRecordId());
        dynamicForm.setType("1");
        //大文本
        dynamicForm.setSql(dynamicSql.buildSelectLargeText(patientRecord.getResearchId()));
        List<DynamicCommonProperty> largeTextList = dynamicCommonMapper.selectBySql(dynamicForm);
        if (largeTextList != null && !largeTextList.isEmpty()){
            t.addAll(largeTextList);
        }
        //小文本
        dynamicForm.setSql(dynamicSql.buildSelectText(patientRecord.getResearchId()));
        List<DynamicCommonProperty> textList = dynamicCommonMapper.selectBySql(dynamicForm);
        if (textList != null && !textList.isEmpty()){
            t.addAll(textList);
        }
        //动态
        dynamicForm.setSql(dynamicSql.buildSelectDynamic(patientRecord.getResearchId()));
        List<DynamicCommonProperty> dynaicList = dynamicCommonMapper.selectBySql(dynamicForm);
        if (dynaicList != null && !dynaicList.isEmpty()){
            t.addAll(dynaicList);
        }
         t= new ArrayList<>(t.stream().collect(
                 Collectors.collectingAndThen(Collectors.toCollection(() -> new
                         TreeSet<>(Comparator.comparing(DynamicCommonProperty::getPropertyId))), ArrayList::new)));

        Map<Long, DynamicCommonProperty> dp = t.stream().collect(Collectors.toMap(DynamicCommonProperty :: getPropertyId, item -> item));
        if (medicalPropertyVOS != null && !medicalPropertyVOS.isEmpty()){
            for (MedicalPropertyVO p : medicalPropertyVOS) {
                if (PropertyType.eq(p.getPropertyType(), PropertyType.UPLOAD)){
                    if (filesMap != null && !filesMap.isEmpty() && filesMap.containsKey(p.getId())){
                        p.setImageUrls(filesMap.get(p.getId()));
                    }
                }else{
                    if (dp != null && !dp.isEmpty() && dp.containsKey(p.getId())){
                        DynamicCommonProperty propertyVO = dp.get(p.getId());
                        if (propertyVO != null){
                            p.setPropertyValue(propertyVO.getPropertyValue());
                            p.setOther(propertyVO.getOther());
                        }
                    }
                }
            }
        }
        //问卷值
        if (medicalPropertyExamVOS != null && !medicalPropertyExamVOS.isEmpty()){
            for (MedicalPropertyVO p : medicalPropertyExamVOS) {
                if (PropertyType.eq(p.getPropertyType(), PropertyType.UPLOAD)){
                    if (filesMap != null && !filesMap.isEmpty() && filesMap.containsKey(p.getId())){
                        p.setImageUrls(filesMap.get(p.getId()));
                    }
                }else{
                    if (dp != null && !dp.isEmpty() && dp.containsKey(p.getId())){
                        DynamicCommonProperty propertyVO = dp.get(p.getId());
                        if (propertyVO != null){
                            p.setPropertyValue(propertyVO.getPropertyValue());
                            p.setOther(propertyVO.getOther());
                        }
                    }
                }
            }
        }


        WarnEarlyRecord warnEarlyRecord = new WarnEarlyRecord();
        warnEarlyRecord.setRecordId(followWorkDetailsForm.getRecordId());
        List<WarnEarlyRecord> warnEarlyRecordList = warnEarlyRecordMapper.selectListByRecordId(warnEarlyRecord);
        patientDetailVO.setWarnEarlyRecordList(warnEarlyRecordList);
        patientDetailVO.setMedicalFormVOS(medicalFormVOSResult);
        patientDetailVO.setMedicalFormExamVOS(medicalFormExamVOSResult);
        return patientDetailVO;
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
                    List<MedicalFormVO> childMedicalFormVOS = x.getChildMedicalFormVOS();
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
            log.error("pc dealWarnSource error, recordId = {}, error is {}", recordId, e.getMessage());
        }
    }

    private boolean markWarnSource(Set<Long> collect, MedicalFormVO x, List<MedicalFormVO> childMedicalFormVOS) throws Exception{
        for (MedicalFormVO y : childMedicalFormVOS) {
            List<MedicalFormVO> yChildMedicalFormVOS = y.getChildMedicalFormVOS();
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

    @Override
    public PatientRecordDetailVo getRecordDetails(FollowWorkDetailsForm followWorkDetailsForm) {
        PatientRecordDetailVo recordDetailVo = new PatientRecordDetailVo();
//        PatientDetailVO patientDetailVO = new PatientDetailVO();
//        patientBaseInfo(followWorkDetailsForm.getPatientId(),patientDetailVO,x->{});
        recordDetailVo.setPatientInfo(patientInfoMapper.selectPatientInfoByPatientId(followWorkDetailsForm.getPatientId()));

        if(followWorkDetailsForm.getRecordId()==null){
            PatientRecord pr = patientRecordMapper.selectOldByPatientId(followWorkDetailsForm.getPatientId());
            followWorkDetailsForm.setRecordId(pr.getRecordId());
        }
        PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByRecordId(followWorkDetailsForm.getRecordId());

        List<PatientRecordFiles> patientRecordFiles = patientRecordFilesMapper.selectPatientRecordFilesByRecordId(followWorkDetailsForm.getRecordId());
        Map<Long, List<String>> filesMap = null;
        if (patientRecordFiles != null && !patientRecordFiles.isEmpty()){
            filesMap = patientRecordFiles.stream()
                    .filter(f-> f.getPropertyId() != null)
                    .collect(Collectors.groupingBy(PatientRecordFiles :: getPropertyId,
                            Collectors.mapping(PatientRecordFiles :: getImageUrl, Collectors.toList())));
        }

        List<FMedicalForm> formList =fMedicalFormMapper.selectFMedicalFormByMedicalFollowId2(patientRecord.getFollowId());
        List<MedicalFormVO> medicalFormVOS = BeanUtils.copyList(formList, MedicalFormVO :: new);

        List<MedicalFormVO> medicalFormVOSResult = new ArrayList<>();
        List<MedicalFormVO> medicalFormExamVOSResult = new ArrayList<>();
        //判断是否存在组的情况
        Map<Long,List<MedicalFormVO>> medicalFormMap = new HashMap<>();
        if (medicalFormVOS != null && !medicalFormVOS.isEmpty()){
            medicalFormMap = medicalFormVOS.stream().filter( x-> x.getParentId()>0).collect(Collectors.groupingBy(MedicalFormVO::getParentId));
        }

        List<FMedicalProperty> properties = fMedicalPropertyMapper.selectFMedicalPropertyByFollowId(patientRecord.getFollowId());
        List<MedicalPropertyVO> medicalPropertyVOS = BeanUtils.copyList(properties, MedicalPropertyVO :: new);

        //查询所有的问卷
        List<FMedicalExamSubject> examSubjects = fMedicalExamSubjectMapper.selectFMedicalExamSubjectByFollowId(patientRecord.getFollowId());
        List<MedicalPropertyVO> medicalPropertyExamVOS = BeanUtils.copyList(examSubjects, MedicalPropertyVO :: new);


        if (medicalFormVOS != null && !medicalFormVOS.isEmpty()){
            Map<Long,List<MedicalPropertyVO>> map = null;
            if (medicalPropertyVOS != null && !medicalPropertyVOS.isEmpty()){
                map = medicalPropertyVOS.stream().collect(Collectors.groupingBy(MedicalPropertyVO :: getMedicalFormId));
            }

            Map<Long,List<MedicalPropertyVO>> exaMap = new HashMap<>();
            if (medicalPropertyExamVOS != null && !medicalPropertyExamVOS.isEmpty()){
                exaMap = medicalPropertyExamVOS.stream().collect(Collectors.groupingBy(MedicalPropertyVO :: getMedicalFormId));
            }

            QuestionRecordForm questionRecordForm = new QuestionRecordForm();
            questionRecordForm.setRecordId(followWorkDetailsForm.getRecordId());
            List<QuestionRecordForm> forms = questionRecordFormMapper.selectQuestionRecordFormList(questionRecordForm);
            Map<Long,List<QuestionRecordForm>> formMap = new HashMap<>();
            if (forms != null && !forms.isEmpty()){
                formMap = forms.stream().collect(Collectors.groupingBy(QuestionRecordForm :: getMedicalFormId));
            }
            for (MedicalFormVO medicalFormVO : medicalFormVOS) {
                if(medicalFormVO.getParentId()>0){
                    continue;
                }
                if (map != null && map.containsKey(medicalFormVO.getMedicalFormId()) && !"1".equals(medicalFormVO.getOpenMultiple())){
                    medicalFormVO.setPropertyVOS(map.get(medicalFormVO.getMedicalFormId()));
                }
                if (exaMap != null && exaMap.containsKey(medicalFormVO.getMedicalFormId()) && !"1".equals(medicalFormVO.getOpenMultiple())){
                    medicalFormVO.setPropertyExamVOS(exaMap.get(medicalFormVO.getMedicalFormId()));
                }
                if(medicalFormMap.containsKey(medicalFormVO.getMedicalFormId())){
                    List<MedicalFormVO> voList = medicalFormMap.get(medicalFormVO.getMedicalFormId());
                    if(voList.size()>0){
                        for(MedicalFormVO v:voList){
                            if (map != null && map.containsKey(v.getMedicalFormId()) ){
                                v.setPropertyVOS(map.get(v.getMedicalFormId()));
                            }
                            if(exaMap!=null && exaMap.containsKey(v.getMedicalFormId())){
                                v.setPropertyExamVOS(exaMap.get(v.getMedicalFormId()));
                            }
                        }
                        medicalFormVO.setChildMedicalFormVOS(voList);
                    }
                }

                if("3".equals(medicalFormVO.getFormType())){
                    medicalFormExamVOSResult.add(medicalFormVO);
                } else{
                    medicalFormVOSResult.add(medicalFormVO);
                }


                //随访工作站详情页面判断模板id是否默认选中
                if(formMap.containsKey(medicalFormVO.getMedicalFormId())){
                    medicalFormVO.setChecked("true");
                }
            }
        }

        // 处理预警源
        dealWarnSource(followWorkDetailsForm.getRecordId(), medicalFormVOSResult);

        List<DynamicCommonProperty> t = new ArrayList<>();
        DynamicForm dynamicForm = new DynamicForm();
        dynamicForm.setRecordId(followWorkDetailsForm.getRecordId());
        dynamicForm.setType("1");
        //大文本
        dynamicForm.setSql(dynamicSql.buildSelectLargeText(patientRecord.getResearchId()));
        List<DynamicCommonProperty> largeTextList = dynamicCommonMapper.selectBySql(dynamicForm);
        if (largeTextList != null && !largeTextList.isEmpty()){
            t.addAll(largeTextList);
        }
        //小文本
        dynamicForm.setSql(dynamicSql.buildSelectText(patientRecord.getResearchId()));
        List<DynamicCommonProperty> textList = dynamicCommonMapper.selectBySql(dynamicForm);
        if (textList != null && !textList.isEmpty()){
            t.addAll(textList);
        }
        //动态
        dynamicForm.setSql(dynamicSql.buildSelectDynamic(patientRecord.getResearchId()));
        List<DynamicCommonProperty> dynaicList = dynamicCommonMapper.selectBySql(dynamicForm);
        if (dynaicList != null && !dynaicList.isEmpty()){
            t.addAll(dynaicList);
        }
        t= new ArrayList<>(t.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(() -> new
                        TreeSet<>(Comparator.comparing(DynamicCommonProperty::getPropertyId))), ArrayList::new)));

        Map<Long, DynamicCommonProperty> dp = t.stream().collect(Collectors.toMap(DynamicCommonProperty :: getPropertyId, item -> item));
        if (medicalPropertyVOS != null && !medicalPropertyVOS.isEmpty()){
            for (MedicalPropertyVO p : medicalPropertyVOS) {
                if (PropertyType.eq(p.getPropertyType(), PropertyType.UPLOAD)){
                    if (filesMap != null && !filesMap.isEmpty() && filesMap.containsKey(p.getId())){
                        p.setImageUrls(filesMap.get(p.getId()));
                    }
                }else{
                    if (dp != null && !dp.isEmpty() && dp.containsKey(p.getId())){
                        DynamicCommonProperty propertyVO = dp.get(p.getId());
                        if (propertyVO != null){
                            p.setPropertyValue(propertyVO.getPropertyValue());
                            p.setOther(propertyVO.getOther());
                        }
                    }
                }
            }
        }
        //问卷值
        if (medicalPropertyExamVOS != null && !medicalPropertyExamVOS.isEmpty()){
            for (MedicalPropertyVO p : medicalPropertyExamVOS) {
                if (PropertyType.eq(p.getPropertyType(), PropertyType.UPLOAD)){
                    if (filesMap != null && !filesMap.isEmpty() && filesMap.containsKey(p.getId())){
                        p.setImageUrls(filesMap.get(p.getId()));
                    }
                }else{
                    if (dp != null && !dp.isEmpty() && dp.containsKey(p.getId())){
                        DynamicCommonProperty propertyVO = dp.get(p.getId());
                        if (propertyVO != null){
                            p.setPropertyValue(propertyVO.getPropertyValue());
                            p.setOther(propertyVO.getOther());
                        }
                    }
                }
            }
        }


        WarnEarlyRecord warnEarlyRecord = new WarnEarlyRecord();
        warnEarlyRecord.setRecordId(followWorkDetailsForm.getRecordId());
        List<WarnEarlyRecord> warnEarlyRecordList = warnEarlyRecordMapper.selectListByRecordId(warnEarlyRecord);
        recordDetailVo.setWarnEarlyRecordList(warnEarlyRecordList);

        List<MedicalFormVO> medicalFormList = new ArrayList<>();
        medicalFormList.addAll(medicalFormExamVOSResult);
        medicalFormList.addAll(medicalFormVOSResult);
        recordDetailVo.setMedicalFormList(medicalFormList);
        return recordDetailVo;
    }

    @Override
    public FollowWorkTree tree(Long userId) {
        try {
            log.info("加载用户{}的随访树", userId);
            long startTime = System.currentTimeMillis();
            SysUser sysUser = sysUserMapper.selectUserById(userId);
            List<ResearchDiseases> researchDiseasesList = new ArrayList<>();
            if ("1".equals(sysUser.getIsAlldeptResearch())) {
                researchDiseasesList = researchDiseasesMapper.selectResearchDiseasesByAuth2(sysUser.getDeptId());
            } else {
                researchDiseasesList = researchDiseasesMapper.selectResearchDiseasesByUserId(userId);
            }

            List<ResearchInfo> researchInfos = new ArrayList<>();
            TreeForm treeForm = new TreeForm();
            treeForm.setType("1");
            if ("1".equals(sysUser.getIsAlldeptResearch())) {
                treeForm.setDeptId(sysUser.getDeptId());
                researchInfos = researchInfoMapper.selectResearchInfoByAuth(treeForm);
            } else {
                treeForm.setUserId(userId);
                researchInfos = researchInfoMapper.selectResearchInfoByUserId(treeForm);
            }
            log.info("to research handle over cost time: {}ms", (System.currentTimeMillis() - startTime));

            Future<List<FollowWorkListVO>> submit = executorService.submit(() -> {
                log.info(Thread.currentThread().getName());
                //患者入组审核-待处理
                FollowWorkListForm followWorkListForm = new FollowWorkListForm();
                followWorkListForm.setEnrollmentHandlerStatus(1);
                followWorkListForm.setAllocatedStatus(0);//待分配
                List<FollowWorkListVO> follow = patientInfoMapper.enrollmentListNew(followWorkListForm);
                log.info("1-到待处理患者查询完, count: {}条 cost time: {}ms", follow.size(), (System.currentTimeMillis() - startTime));
                return follow;
            });

            Future<List<FollowWorkListVO>> submit1 = executorService.submit(() -> {
                log.info(Thread.currentThread().getName());
                List<FollowWorkListVO> all = selectFollowWorkPatientRecordListNew();
                log.info("2-到查询所有档案完, count: {}条, cost time: {}ms", all.size(), (System.currentTimeMillis() - startTime));
                return all;
            });

            Future<List<FollowWorkListVO>> submit2 = executorService.submit(() -> {
                        log.info(Thread.currentThread().getName());
                        FollowWorkListForm form = new FollowWorkListForm();
                        form.setAllocatedHandlerStatus("1");
                        form.setDeptId(sysUser.getDeptId());
                        List<FollowWorkListVO> allocated = patientInfoMapper.allocatedList(form);
                        log.info("3-到查入组状态待处理完, count: {}条,cost time: {}ms", allocated.size(), (System.currentTimeMillis() - startTime));
                        return allocated;
                    }
            );

            Future<List<FollowWorkListVO>> submit3 = executorService.submit(() -> {
                log.info(Thread.currentThread().getName());
                List<FollowWorkListVO> base = selectFollowWorkBasePatientRecordList();
                log.info(" 查询所有基线, count: {}条, cost time: {}ms", base.size(), (System.currentTimeMillis() - startTime));
                return base;
            });

            List<FollowWorkListVO> base = submit3.get();
            List<FollowWorkListVO> handle = new ArrayList<>();
            List<FollowWorkListVO> over = new ArrayList<>();
            List<FollowWorkListVO> warn = new ArrayList<>();
            List<FollowWorkListVO> all = submit1.get();
            for (FollowWorkListVO followWorkListVO : all) {
                if ("1".equals(followWorkListVO.getFollowStatus())) {
                    handle.add(followWorkListVO);
                }
                if ("1".equals(followWorkListVO.getOverTimeStatus()) && "0".equals(followWorkListVO.getFollowStatus())) {
                    over.add(followWorkListVO);
                }
                if ("1".equals(followWorkListVO.getWarnStatus())) {
                    warn.add(followWorkListVO);
                }
            }
            //申请退出队列未处理
            List<FollowWorkListVO> logoutAudit = patientInfoMapper.findAuditList();
            List<FollowWorkListVO> follow = submit.get();
            List<FollowWorkListVO> allocateds = submit2.get();
            int len = allocateds.size() + all.size() + follow.size();
            Map<String, List<FollowWorkListVO>> map = new HashMap<String, List<FollowWorkListVO>>(len / 2);
            map.put("follow", follow);
            map.put("handle", handle);
            map.put("over", over);
            map.put("warn", warn);
            map.put("allocated", allocateds);
            map.put("baseline", base);
            map.put("logoutAudit", logoutAudit);
            log.info("to map handle over cost time: {}ms", (System.currentTimeMillis() - startTime));

            FollowWorkTree followWorkTree = new FollowWorkTree(researchDiseasesList, researchInfos, map);
            log.info("to generate tree over cost time (all time): {}ms", (System.currentTimeMillis() - startTime));
            return followWorkTree;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FollowWorkListVO> selectFollowWorkBasePatientRecordList(){
        FollowWorkListForm followWorkListForm = new FollowWorkListForm();
        followWorkListForm.setFollowStatus(0);
        followWorkListForm.setEnrollmentStatus(1);
        return patientRecordMapper.selectFollowWorkBasePatientRecordList(followWorkListForm);
    }

    public List<FollowWorkListVO> selectFollowWorkPatientRecordListNew(){
        FollowWorkListForm followWorkListForm = new FollowWorkListForm();
        followWorkListForm.setFollowStatus(1);
        followWorkListForm.setOverTimeStatus(1);
        followWorkListForm.setWarnStatus(1);
        return patientRecordMapper.selectFollowWorkPatientRecordListNew(followWorkListForm);
    }

    public List<FollowWorkListVO> selectFollowWorkPatientRecordList(String type){
        FollowWorkListForm followWorkListForm = new FollowWorkListForm();
        switch (type) {
            case "1" :
                followWorkListForm.setFollowStatus(1);
                break;
            case "2" :
                followWorkListForm.setOverTimeStatus(1);
                break;
            case "3" :
                followWorkListForm.setWarnStatus(1);
                break;
        }
        return patientRecordMapper.selectFollowWorkPatientRecordList2(followWorkListForm);
    }

    @Override
    public EvaluateTree evaluateTree(String type,Long userId) {
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        List<ResearchDiseases> researchDiseasesList = new ArrayList<>();
        if("1".equals(sysUser.getIsAlldeptResearch())){
            researchDiseasesList = researchDiseasesMapper.selectResearchDiseasesByAuth2(sysUser.getDeptId());
        }else{
            researchDiseasesList = researchDiseasesMapper.selectResearchDiseasesByUserId(userId);
        }
        
        
        TreeForm treeForm = new TreeForm();
        treeForm.setType("2");
        treeForm.setUserId(userId);
        List<ResearchInfo> researchInfos = researchInfoMapper.selectResearchInfoByUserId(treeForm);
        List<ResearchInfo>  result =  dealFollowTreeData( researchInfos);
        return new EvaluateTree(researchDiseasesList, result,type);
    }
    
    
	 /**
     * 对统计数据进行合并
     * @param researchInfos
     * @return
     */
    private    List<ResearchInfo>  dealFollowTreeData(List<ResearchInfo> researchInfos){
    
	    	 List<ResearchInfo>  result   = new ArrayList<>();
		    // 需要对researhInfos 进行二次处理
		    if(researchInfos != null &&  researchInfos.size() > 0) {
		     Map<Long, List<ResearchInfo>> groupedResearch= researchInfos.stream().collect(Collectors.groupingBy(ResearchInfo::getId));
		     for (Map.Entry<Long, List<ResearchInfo>> researchGruopKey : groupedResearch.entrySet()) {
		    	Long researchId =  researchGruopKey.getKey();
		    	List<ResearchInfo> researchInfoList = researchGruopKey.getValue();
		    	ResearchInfo  researchInfo = new ResearchInfo();
		    	researchInfo.setId(researchId);
		    	if(researchInfoList != null &&  researchInfoList.size() > 0) {
		    		ResearchInfo  research0 = researchInfoList.get(0);
		    		researchInfo.setDiseaseTypeId(research0.getDiseaseTypeId());
		    		researchInfo.setProjectName(research0.getProjectName());
		    	   int evaluateBackPendingNum = 0;//后台质效待评价数量
		    	   int evaluateBackCompletedNum = 0;//后台质效完成数量
		    	   int warnEvaluateBackPendingNum = 0;//后台预警质效待评价数量
		    	   int warnEvaluateBackCompletedNum = 0;//后台预警质效完成数量
		    	   int overEvaluateBackPendingNum = 0;//后台超窗质效待评价数量
		    	   int overEvaluateBackCompletedNum = 0;//后台超窗质效完成数量
		    	   int evaluatePendingNum = 0;
		    	   int evaluateCompletedNum = 0;
		    		for (ResearchInfo researchDb : researchInfoList) {
						if(ResearchContants.FOLLOW_STATUS_3.equals(researchDb.getFollowStatus()) && ResearchContants.EVALUATE_STATUS_BACK_1.equals(researchDb.getEvaluateStatusBack())) {
						  ++ evaluateBackPendingNum ;	
						}
						if(ResearchContants.FOLLOW_STATUS_4.equals(researchDb.getFollowStatus()) && ResearchContants.EVALUATE_STATUS_BACK_2.equals(researchDb.getEvaluateStatusBack())) {
							++ evaluateBackCompletedNum ;	
						}
						if(ResearchContants.WARN_STATUS_3.equals(researchDb.getWarnStatus()) && ResearchContants.WARN_EVALUATE_STATUS_BACK_1.equals(researchDb.getWarnEvaluateStatusBack())) {
							++ warnEvaluateBackPendingNum ;	
						}
						if(ResearchContants.WARN_STATUS_4.equals(researchDb.getWarnStatus()) && ResearchContants.WARN_EVALUATE_STATUS_BACK_2.equals(researchDb.getWarnEvaluateStatusBack())) {
							++ warnEvaluateBackCompletedNum ;	
						}
						if(ResearchContants.OVER_TIME_STATUS_3.equals(researchDb.getOverTimeStatus()) && ResearchContants.OVER_EVALUATE_STATUS_BACK_1.equals(researchDb.getOverEvaluateStatusBack())) {
							++ overEvaluateBackPendingNum ;	
						}
						if(ResearchContants.OVER_TIME_STATUS_4.equals(researchDb.getOverTimeStatus()) && ResearchContants.OVER_EVALUATE_STATUS_BACK_2.equals(researchDb.getOverEvaluateStatusBack())) {
							++ overEvaluateBackCompletedNum ;	
						}
						
						if(ResearchContants.FOLLOW_STATUS_3.equals(researchDb.getFollowStatus()) && ResearchContants.EVALUATE_STATUS_1.equals(researchDb.getEvaluateStatus())) {
							  ++ evaluatePendingNum ;	
							}
						if(ResearchContants.FOLLOW_STATUS_3.equals(researchDb.getFollowStatus()) && ResearchContants.EVALUATE_STATUS_2.equals(researchDb.getEvaluateStatus())) {
							  ++ evaluateCompletedNum ;	
							}
					}
		    		researchInfo.setEvaluateBackPendingNum(evaluateBackPendingNum);
		    		
		    		researchInfo.setEvaluateBackPendingNum( evaluateBackPendingNum);//后台质效待评价数量
		    		researchInfo.setEvaluateBackCompletedNum(evaluateBackCompletedNum);//后台质效完成数量
		    		researchInfo.setWarnEvaluateBackPendingNum(warnEvaluateBackPendingNum );//后台预警质效待评价数量
		    		researchInfo.setWarnEvaluateBackCompletedNum(warnEvaluateBackCompletedNum);//后台预警质效完成数量
		    		researchInfo.setOverEvaluateBackPendingNum(overEvaluateBackPendingNum);//后台超窗质效待评价数量
		    		researchInfo.setOverEvaluateBackCompletedNum(overEvaluateBackCompletedNum);//后台超窗质效完成数量
		    		researchInfo.setEvaluatePendingNum(evaluatePendingNum);
		    		researchInfo.setEvaluateCompletedNum(evaluateCompletedNum);
		    		
		    		
		    	}
		    	
		    	result.add(researchInfo);
			} 
		    }
		    return result;
    }

    

    @Override
    public List<EvaluateVO> evaluateList(EvaluateForm evaluateForm) {
        return patientRecordMapper.selectEvaluateList(evaluateForm);
    }

    @Override
    public int evaluateWork(EvaluateWorkForm evaluateWorkForm) {
        String belongType = evaluateWorkForm.getBelongType() == null ? "1" : evaluateWorkForm.getBelongType();
        PatientRecord record = new PatientRecord();
        record.setRecordId(evaluateWorkForm.getRecordId());
        record.setUpdateBy(evaluateWorkForm.getUpdateBy());
        if (belongType.equals("1")){
            record.setEvaluatePrescriptionBack(evaluateWorkForm.getEvaluatePrescriptionBack());
            record.setEvaluateQualityBack(evaluateWorkForm.getEvaluateQualityBack());
            record.setEvaluateReplyBack(evaluateWorkForm.getEvaluateReplyBack());
            record.setEvaluateStatusBack("2");
            record.setEvaluateReplyBackTime(DateUtils.getNowDate());
            record.setFollowStatus("4");
//        }else if (belongType.equals("3")){
//            record.setOverEvaluatePrescriptionBack(evaluateWorkForm.getOverEvaluatePrescriptionBack());
//            record.setOverEvaluateQualityBack(evaluateWorkForm.getOverEvaluateQualityBack());
//            record.setOverEvaluateReplyBack(evaluateWorkForm.getOverEvaluateReplyBack());
//            record.setOverEvaluateStatusBack("2");
//            record.setOverEvaluateReplyBackTime(DateUtils.getNowDate());
//            record.setOverTimeStatus("4");
//        }else if (belongType.equals("4")){
//            record.setWarnEvaluatePrescriptionBack(evaluateWorkForm.getWarnEvaluatePrescriptionBack());
//            record.setWarnEvaluateQualityBack(evaluateWorkForm.getWarnEvaluateQualityBack());
//            record.setWarnEvaluateReplyBack(evaluateWorkForm.getWarnEvaluateReplyBack());
//            record.setWarnEvaluateStatusBack("2");
//            record.setWarnEvaluateReplyBackTime(DateUtils.getNowDate());
            record.setWarnStatus("4");
        }

        if(Objects.equals(record.getFollowTipStatus(),FollowTipStatusConstants.FOLLOW_UP_OPINION_REMINDER)){
            record.setFollowTipStatus(FollowTipStatusConstants.NO_STATUS);
            record.setEvaluateStatus("1");
        }
        return patientRecordMapper.updatePatientRecord(record);
    }

    @Override
    public List<FollowWorkListVO> enrollmentList(FollowWorkListForm followWorkListForm) {
        if(Integer.valueOf(0).equals(followWorkListForm.getEnrollmentHandlerStatus())){
            followWorkListForm.setEnrollmentHandlerStatus(1);
        }
        followWorkListForm.setAllocatedStatus(0);

        List<FollowWorkListVO> list = patientInfoMapper.enrollmentList2(followWorkListForm);
        if(list.size()>0){
            for(FollowWorkListVO f:list){
                f.setHaveSubmitted(Boolean.FALSE);
                //是否提交
                PatientRecordVo query = new PatientRecordVo();
                query.setResearchId(f.getResearchId());
                query.setPatientId(f.getPatientId());
                List<PatientRecordVo> patientRecordVos = patientRecordMapper.selectEcdPatientRecordVoList(query);
                if (CollectionUtils.isEmpty(patientRecordVos)) {
                    f.setHaveSubmitted(Boolean.FALSE);
                }
                List<PatientRecordVo> collect = patientRecordVos.stream().filter(x -> !"0".equals(x.getFollowStatus())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(collect)) {
                    f.setHaveSubmitted(Boolean.TRUE);
                }
                if(f.getMobile()==null){
                    f.setMobile(f.getBaseMobile());
                }
                f.setBaseMobile(null);
            }
        }
        return list;
    }

    @Override
    public List<FollowWorkListVO> baseList(FollowWorkListForm followWorkListForm) {
        if(followWorkListForm.getFollowStatus() != null && followWorkListForm.getFollowStatus() == 5){
                followWorkListForm.setFollowStatus(0);
        } else if(followWorkListForm.getFollowStatus() != null && followWorkListForm.getFollowStatus() == 1) {
            //已完成
            followWorkListForm.setFollowStatus(3);
        }
        followWorkListForm.setEnrollmentStatus(1);
        List<FollowWorkListVO> listVOS = patientRecordMapper.selectFollowWorkBasePatientRecordList(followWorkListForm);
        if(listVOS.size()>0){
            //随访工作站为提前提醒，PatientRecordOutbound的stage_num为需要生产记录的
            for(FollowWorkListVO f:listVOS){
                PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
                patientRecordOutbound.setResearchId(followWorkListForm.getResearchId());
                patientRecordOutbound.setOutboundType("1");//1智能外呼2短信提醒3app站内信
                patientRecordOutbound.setRemindType("1");//1随访2超窗3预警
                patientRecordOutbound.setPatientId(f.getPatientId());
                patientRecordOutbound.setStageNum(f.getStageAddNum());
                PatientRecordOutbound p = patientRecordOutboundMapper.selectOneId(patientRecordOutbound);
                if(p!=null){
                    f.setOutboundId(p.getId());
                }
                if(f.getFillStatus() != null && Integer.valueOf("1").equals(f.getFillStatus())) {
                    f.setEditFlag(1);
                } else {
                    f.setEditFlag(0);
                }
            }
        }
        return listVOS;
    }

    @Override
    public List<FollowWorkListVO> handleList(FollowWorkListForm followWorkListForm) {
        if(Integer.valueOf(0).equals(followWorkListForm.getFollowStatus())){
            followWorkListForm.setFollowStatus(1);
        }
        if(followWorkListForm.getFollowStatus() != null && followWorkListForm.getFollowStatus() == 4){
            followWorkListForm.setFollowStatus(3);
            followWorkListForm.setEvaluateStatus(2);
        }
        String followStatusSql = "";

        //在待处理中增加查询未填写的记录，用于医生编辑
        if(followWorkListForm.getFollowStatus() != null && followWorkListForm.getFollowStatus() == 1){
            followStatusSql = " and ( r.follow_status = 1 or ( r.follow_status = 0 and r.over_time_status = 0))  ";
            followWorkListForm.setFollowStatusSql(followStatusSql);
        }

        List<FollowWorkListVO> listVOS = patientRecordMapper.selectFollowWorkPatientRecordList(followWorkListForm);
        Map<String, String> sysUserMap = getPersonInfoMap(listVOS);
        if(listVOS.size()>0){
            //随访工作站为提前提醒，PatientRecordOutbound的stage_num为需要生产记录的
            for(FollowWorkListVO f:listVOS){
                f.setCreateName(sysUserMap.get(f.getCreateBy()));
                f.setEditFlag(0);
                PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
                patientRecordOutbound.setResearchId(followWorkListForm.getResearchId());
                patientRecordOutbound.setOutboundType("1");//1智能外呼2短信提醒3app站内信
                patientRecordOutbound.setRemindType("1");//1随访2超窗3预警
                patientRecordOutbound.setPatientId(f.getPatientId());
                patientRecordOutbound.setStageNum(f.getStageAddNum());
                PatientRecordOutbound p = patientRecordOutboundMapper.selectOneId(patientRecordOutbound);
                if(p!=null){
                    f.setOutboundId(p.getId());
                }
                if(f.getFillStatus() != null && Integer.valueOf("1").equals(f.getFillStatus())) {
                    f.setEditFlag(1);
                }
            }
            //非处理状态，所有数据一律不需要编辑，即填写完成状态为1
//            if(ObjectUtil.isNull(followWorkListForm.getFollowStatus()) || followWorkListForm.getFollowStatus()>1){
//                listVOS.forEach(f -> f.setEditFlag(1));
//            }
        }
        return listVOS;
    }

    private Map<String, String> getPersonInfoMap(List<FollowWorkListVO> listVOS) {
        List<String> createByList = listVOS.stream().map(FollowWorkListVO::getCreateBy).filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> patientIdList = createByList.stream().filter(s -> !s.startsWith(Constants.CREATEOR_DOCTOR_KEY)).distinct().map(Long::valueOf).collect(Collectors.toList());
        List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoByPatientIdList(patientIdList);
        Map<String, String> patientMap = patientInfos.stream().collect(Collectors.toMap(x->String.valueOf(x.getPatientId()), PatientInfo::getPatientName, (k1, k2) -> k1));

        List<Long> userIdList = createByList.stream().filter(s -> s.startsWith(Constants.CREATEOR_DOCTOR_KEY)).distinct().map(x-> Long.valueOf(x.substring(3))).collect(Collectors.toList());
        List<SysUser> sysUsers = sysUserMapper.selectUserByIdList(userIdList);
        Map<String, String> sysUserMap = sysUsers.stream().collect(Collectors.toMap(x->(Constants.CREATEOR_DOCTOR_KEY+x.getUserId()), SysUser::getNickName, (k1, k2) -> k1));
        sysUserMap.putAll(patientMap);
        return sysUserMap;
    }

    @Override
    public List<String> handleStageList(FollowWorkListForm followWorkListForm) {
        if(Integer.valueOf(0).equals(followWorkListForm.getFollowStatus())){
            followWorkListForm.setFollowStatus(1);
        }
        if(followWorkListForm.getFollowStatus() != null && followWorkListForm.getFollowStatus() == 4){
            followWorkListForm.setFollowStatus(3);
            followWorkListForm.setEvaluateStatus(2);
        }
        List<FollowWorkListVO> listVOS = patientRecordMapper.selectFollowWorkPatientRecordList(followWorkListForm);

        return listVOS.stream().map(FollowWorkListVO::getName).distinct().collect(Collectors.toList());
    }

    @Override
    public List<FollowWorkListVO> overList(FollowWorkListForm followWorkListForm) {
        if(Integer.valueOf(0).equals(followWorkListForm.getOverTimeStatus())){
            followWorkListForm.setOverTimeStatus(1);
        } else if (Integer.valueOf(3).equals(followWorkListForm.getOverTimeStatus())){
            //完成超窗包括超窗完成和失访两种情况
            List<Integer> list = Arrays.asList(2, 3);
            followWorkListForm.setOverTimeStates(list);
        }
        followWorkListForm.setFollowStatus(0);
        //应唐都客户需求在超窗已完成的查询列表中展示超窗的数据
        if(Integer.valueOf(3).equals(followWorkListForm.getOverTimeStatus())) {
            followWorkListForm.setPatientStatus("4");
            followWorkListForm.setOverTimeStatus(null);
        }
        List<FollowWorkListVO> listVOS = patientRecordMapper.selectFollowWorkPatientRecordList(followWorkListForm);
        Map<String, String> sysUserMap = getPersonInfoMap(listVOS);
        if(listVOS.size()>0){
            for(FollowWorkListVO f:listVOS){
                f.setCreateName(sysUserMap.get(f.getCreateBy()));
                f.setEditFlag(0);
                PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
                patientRecordOutbound.setResearchId(followWorkListForm.getResearchId());
                patientRecordOutbound.setOutboundType("1");//1智能外呼2短信提醒3app站内信
                patientRecordOutbound.setRemindType("2");//1随访2超窗3预警
                patientRecordOutbound.setRecordId(f.getRecordId());
                PatientRecordOutbound p = patientRecordOutboundMapper.selectOneId(patientRecordOutbound);
                if(p!=null){
                    f.setOutboundId(p.getId());
                }
                if(f.getFillStatus() != null && f.getFillStatus() == 1) {
                    f.setEditFlag(1);
                }
            }
            //非超窗状态，所有数据一律不需要编辑，即填写完成状态为1
            if(ObjectUtil.isNull(followWorkListForm.getOverTimeStatus()) || !followWorkListForm.getOverTimeStatus().equals(1)){
                listVOS.forEach(f -> f.setEditFlag(1));
            }
        }
        return listVOS;
    }

    @Override
    public List<String> overStageList(FollowWorkListForm followWorkListForm) {
        if(Integer.valueOf(0).equals(followWorkListForm.getOverTimeStatus())){
            followWorkListForm.setOverTimeStatus(1);
        }
        List<FollowWorkListVO> listVOS = patientRecordMapper.selectFollowWorkPatientRecordList(followWorkListForm);
        return listVOS.stream().map(FollowWorkListVO::getName).distinct().collect(Collectors.toList());
    }

    @Override
    public List<FollowWorkListVO> warnList(FollowWorkListForm followWorkListForm) {
        if(Integer.valueOf(0).equals(followWorkListForm.getWarnStatus())){
            followWorkListForm.setWarnStatus(1);
        }
        List<FollowWorkListVO> listVOS = patientRecordMapper.selectFollowWorkPatientRecordList(followWorkListForm);
        if(listVOS.size()>0){
            for(FollowWorkListVO f:listVOS){
                PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
                patientRecordOutbound.setResearchId(followWorkListForm.getResearchId());
                patientRecordOutbound.setOutboundType("1");//1智能外呼2短信提醒3app站内信
                patientRecordOutbound.setRemindType("3");//1随访2超窗3预警
                patientRecordOutbound.setRecordId(f.getRecordId());
                PatientRecordOutbound p = patientRecordOutboundMapper.selectOneId(patientRecordOutbound);
                if(p!=null){
                    f.setOutboundId(p.getId());
                }

                WarnEarlyRecord warnEarlyRecord = warnEarlyRecordMapper.selectOldOneByRecordId(f.getRecordId());
                if(warnEarlyRecord!=null){
                    f.setCreateTime(warnEarlyRecord.getCreateTime());
                }
            }
        }
        return listVOS;
    }

    @Override
    public List<String> warnStageList(FollowWorkListForm followWorkListForm) {
        if(Integer.valueOf(0).equals(followWorkListForm.getWarnStatus())){
            followWorkListForm.setWarnStatus(1);
        }
        List<FollowWorkListVO> listVOS = patientRecordMapper.selectFollowWorkPatientRecordList(followWorkListForm);

        return listVOS.stream().map(FollowWorkListVO::getName).distinct().collect(Collectors.toList());
    }

    @Override
    public List<FollowWorkListVO> allocatedList(FollowWorkListForm followWorkListForm) {
        return patientInfoMapper.allocatedList(followWorkListForm);
    }

    @Override
    @Transactional
    public int enrollmentHandle(FollowWorkEnrollmentHandleForm workEnrollmentHandleForm) {
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setPatientId(workEnrollmentHandleForm.getPatientId());
        patientInfo.setEnrollmentResult(workEnrollmentHandleForm.getHandleType());
        patientInfo.setEnrollmentHandlerStatus(workEnrollmentHandleForm.getHandleType());
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(workEnrollmentHandleForm.getResearchId());
        //1待分配2变更3确认入组
        if ("1".equals(workEnrollmentHandleForm.getHandleType())){
            patientInfo.setEnrollmentResultContent("待分配");
            patientInfo.setAllocatedStatus("1");
            patientInfo.setAllocatedHandlerStatus("1");
            patientInfoMapper.updatePatientInfo(patientInfo);
        }else if ("2".equals(workEnrollmentHandleForm.getHandleType())){

            patientInfo.setEnrollmentStatus("1");
            patientInfo.setEnrollmentTime(new Date());
            patientInfo.setResearchId(workEnrollmentHandleForm.getResearchId());
            if (researchInfo != null){
                patientInfo.setEnrollmentResultContent("入组变更到"+researchInfo.getProjectName());
            }

            PatientInfo piOld = patientInfoMapper.selectPatientInfoByPatientId(workEnrollmentHandleForm.getPatientId());
            if("1".equals(piOld.getAllocatedStatus())){
                //是待分配的患者
                patientInfo.setAllocatedHandlerStatus("3");
                patientInfo.setEnrollmentHandlerStatus("3");
                generateUpdateBaseRecord(patientInfo);
                patientInfoMapper.updatePatientInfo(patientInfo);
            }else{

                patientInfo.setEnrollmentHandlerStatus("3");
                patientInfo.setPlanResearchId(workEnrollmentHandleForm.getResearchId());
                patientInfo.setIsEndCreateRecord("1");
                patientInfo.setResearchId(null);
                patientInfo.setIsShowApp("0");
                patientInfoMapper.updatePatientInfo(patientInfo);

                PatientInfo newPatientInfo = new PatientInfo(piOld, researchInfo, workEnrollmentHandleForm.getUpdateBy());
                newPatientInfo.setOldPatientId(piOld.getPatientId());
                newPatientInfo.setChannelType(piOld.getChannelType());
                //患者审核后修改入组直接已入组
                newPatientInfo.setEnrollmentHandlerStatus("3");
                newPatientInfo.setDeptId(researchInfo.getDeptId());
                patientInfoMapper.insertPatientInfo(newPatientInfo);
                //新队列记录初始化
                generateBaseRecord(newPatientInfo);

                ResearchPatient researchPatient = new ResearchPatient();
                researchPatient.setResearchId(newPatientInfo.getResearchId());
                researchPatient.setPersonId(newPatientInfo.getPersonId());
                researchPatient.setPatientId(newPatientInfo.getPatientId());
                researchPatient.setCreateTime(DateUtils.getNowDate());
                researchPatientMapper.insertResearchPatient(researchPatient);
            }
        } else if ("3".equals(workEnrollmentHandleForm.getHandleType())) {
            //3.pc端确认入组, pc自己发起的需要app端去确认; app发起的, pc端此处操作直接入组
            PatientInfo patientInfoDO = patientInfoMapper.selectPatientInfoByPatientId(workEnrollmentHandleForm.getPatientId());
            /*渠道来源是app端, 则直接处理完毕, 入组, 否则入组进行中*/
            //处理状态是3, 则不走此项入组 (重入组逻辑)!!!, 已完成的重入组不走此项
            /**
             * 排列组合状态, col_1 来源渠道(1后台,2扫码) | col_2 处理状态 (1待处理,2进行中,3已完成), col_3 入组状态(0待分配,1已入组)
             * 1,1 发起邀请 1,2 入组中,0
             * 1,2 取消入组
             * 1,3 重新入组
             * 2,1 审核确认, 审核拒绝
             * 2,2
             * 2,3 重新入组
              */

            if ("2".equals(patientInfoDO.getChannelType()) && !"3".equals(patientInfoDO.getEnrollmentHandlerStatus())) { //app端
                /*扫码入组*/
                patientInfo.setEnrollmentStatus("1");
                patientInfo.setEnrollmentHandlerStatus("3");
                patientInfo.setEnrollmentTime(new Date());
                patientInfo.setUpdateTime(new Date());
                patientInfo.setResearchId(workEnrollmentHandleForm.getResearchId());
                patientInfo.setEnrollmentResultContent("确认入组");
//            generateBaseRecord(patientInfo);
                patientInfo.setDeptId(researchInfo.getDeptId());
                patientInfoMapper.updatePatientInfo(patientInfo);

                PatientRecord prd = patientRecordMapper.selectOldByPatientId(workEnrollmentHandleForm.getPatientId());
                prd.setVisitTime(DateUtils.getNowDate());
                prd.setLockVisitTime(DateUtils.getNowDate());
                patientRecordMapper.updatePatientRecord(prd);

                PatientInfo pio = patientInfoMapper.selectPatientInfoByPatientId(workEnrollmentHandleForm.getPatientId());
                prd.setAccountId(pio.getAccountId());
                String msgContent = "您已加入随访队列，可查看队列详情";
                AccountNotice accountNotice = new AccountNotice(prd, msgContent, "11");
                accountNoticeMapper.insertAccountNotice(accountNotice);
            } else /*来源渠道后台,pc发起入组邀请*/ {
                PatientInfo pio = patientInfoMapper.selectPatientInfoByPatientId(workEnrollmentHandleForm.getPatientId());
                if ("2".equals(pio.getEnrollmentHandlerStatus())) {
                    throw new ServiceException("您已重新入组,请稍后!");
                }
                //进行中
                patientInfo.setEnrollmentStatus("0");
                patientInfo.setEnrollmentHandlerStatus("2");
                patientInfo.setEnrollmentTime(new Date());
                patientInfo.setResearchId(workEnrollmentHandleForm.getResearchId());
                patientInfo.setEnrollmentResultContent("入组确认中");
                patientInfo.setUpdateTime(new Date());
//            generateBaseRecord(patientInfo);
                patientInfo.setDeptId(researchInfo.getDeptId());
//                Long userId = SecurityUtils.getUserId();
//                patientInfo.setDoctorId(userId);
                if ("3".equals(patientInfoDO.getEnrollmentHandlerStatus())){
                    /*重新入组*/
                    patientInfo.setChannelType("1");
                    patientInfo.setSmsSendCount(0);
                }
                patientInfoMapper.updatePatientInfo(patientInfo);
                clearFollowRecord(patientInfo);
                PatientRecord prd = patientRecordMapper.selectOldByPatientId(workEnrollmentHandleForm.getPatientId());
                prd.setVisitTime(DateUtils.getNowDate());
                prd.setLockVisitTime(DateUtils.getNowDate());
                patientRecordMapper.updatePatientRecord(prd);

                //未绑定发送短信
                if (patientInfoDO.getAccountId() == null || patientInfoDO.getAccountId() == 0) {
                    patientInfoDO.setProjectName(researchInfo.getProjectName());
                    tencentService.asyncSendSms(patientInfoDO);
                }
                //消息和推送
                prd.setAccountId(patientInfoDO.getAccountId());
                String msgContent = "您被邀请加入随访队列";
                AccountNotice accountNotice = new AccountNotice(prd, msgContent, "19");
                Map<String, Object> extjson = new HashMap<>();
                extjson.put("researchId", researchInfo.getId());
                extjson.put("type", "19");
                extjson.put("patientId", String.valueOf(patientInfoDO.getPatientId()));
                extjson.put("recordId", prd.getRecordId());
                extjson.put("researchName", researchInfo.getProjectName());
                extjson.put("researchDescription", researchInfo.getResearchSynopsis());
                accountNotice.setExtendJson(JSON.toJSONString(extjson));
                //用于消息推送
                accountNoticeMapper.insertAccountNotice(accountNotice);

                //用于主动查询 弹框消息
                createPopNotice(researchInfo, String.valueOf(patientInfoDO.getPatientId()));
            }
        } else if ("4".equals(workEnrollmentHandleForm.getHandleType())) {

            checkCancelResearch(workEnrollmentHandleForm);
            //4.pc端取消入组, pc端发起的直接取消入组, 患者端主动扫码的, pc端不让入组
            PatientInfo patientInfoDO = patientInfoMapper.selectPatientInfoByPatientId(workEnrollmentHandleForm.getPatientId());
            PatientRecord prd = patientRecordMapper.selectOldByPatientId(workEnrollmentHandleForm.getPatientId());

            if ("2".equals(patientInfoDO.getChannelType())) {
                /*来源于app端,pc端不让患者*/
                //取消入组, 新加状态还是置为未处理
                patientInfo.setEnrollmentStatus("0");
                patientInfo.setEnrollmentHandlerStatus("3"); //处理了, 但是入组结果还是0, 也就是没有入
                patientInfo.setEnrollmentTime(new Date());
                patientInfo.setResearchId(workEnrollmentHandleForm.getResearchId());
                patientInfo.setEnrollmentResultContent("取消入组");
                String refuseCode = workEnrollmentHandleForm.getRefuseCode();
                String refuseMsg = workEnrollmentHandleForm.getRefuseReason();
                log.info("取消入组原因code:{}", refuseCode);
                log.info("取消入组原因refuseMsg:{}", refuseMsg);
//            generateBaseRecord(patientInfo);
                patientInfo.setDeptId(researchInfo.getDeptId());
                patientInfoMapper.updatePatientInfo(patientInfo);

                prd.setVisitTime(DateUtils.getNowDate());
                prd.setLockVisitTime(DateUtils.getNowDate());
                patientRecordMapper.updatePatientRecord(prd);

                PatientInfo pio = patientInfoMapper.selectPatientInfoByPatientId(workEnrollmentHandleForm.getPatientId());
                prd.setAccountId(pio.getAccountId());
                String msgContent = "您申请加入队列审核未通过,"+refuseMsg;

                AccountNotice accountNotice = new AccountNotice(prd, msgContent, "20");//20

                accountNoticeMapper.insertAccountNotice(accountNotice);
            } else {
                /*来源于pc端邀请,pc端取消入组*/
                //            todo 能否取消判断一下, 若是已完成(3), 则判断下是否填写了, 没填写可以取消
                //取消入组, 新加状态还是置为未处理 todo
                patientInfo.setEnrollmentStatus("0");
                patientInfo.setEnrollmentHandlerStatus("3");
                patientInfo.setEnrollmentTime(new Date());
                patientInfo.setResearchId(workEnrollmentHandleForm.getResearchId());
                patientInfo.setEnrollmentResultContent("取消入组");
                //todo 取消入组原因
                String refuseCode = workEnrollmentHandleForm.getRefuseCode();
                String refuseMsg = workEnrollmentHandleForm.getRefuseReason();
                log.info("取消入组原因code:{}", refuseCode);
                log.info("取消入组原因refuseMsg:{}", refuseMsg);
//            generateBaseRecord(patientInfo);
                patientInfo.setDeptId(researchInfo.getDeptId());
                patientInfoMapper.updatePatientInfo(patientInfo);

                prd.setVisitTime(DateUtils.getNowDate());
                prd.setLockVisitTime(DateUtils.getNowDate());
                patientRecordMapper.updatePatientRecord(prd);

                PatientInfo pio = patientInfoMapper.selectPatientInfoByPatientId(workEnrollmentHandleForm.getPatientId());
                prd.setAccountId(pio.getAccountId());
                String msgContent = "您被取消邀请加入随访队列,"+refuseMsg;

                AccountNotice accountNotice = new AccountNotice(prd, msgContent, "20");//20
                accountNoticeMapper.insertAccountNotice(accountNotice);

                cancelPopNotice(researchInfo, String.valueOf(patientInfoDO.getPatientId()));
            }
        }
        PatientRecord patientRecord = patientRecordMapper.selectOldByPatientId(workEnrollmentHandleForm.getPatientId());
        if (patientRecord != null) {
            patientRecordOperLogMapper.insertPatientRecordOperLog(new PatientRecordOperLog(workEnrollmentHandleForm.getUserId(), patientRecord.getRecordId(), "1", DateUtils.getNowDate()));
        }
        return 1;
    }

    /**.
     * 清理随访
     *
     * @param patientInfo
     */
    private void clearFollowRecord(PatientInfo patientInfo) {
        List<PatientRecordTemplate> patientRecordTemplates =
                patientRecordTemplateMapper.selectPatientRecordTemplateByResearchId(patientInfo.getResearchId());
        if (patientRecordTemplates != null && !patientRecordTemplates.isEmpty()) {
            for (PatientRecordTemplate patientRecordTemplate : patientRecordTemplates) {
                if (patientRecordTemplate.getFollowNum() != 0) {
                    FollowWorkListCmdForm followWorkListCmdForm = new FollowWorkListCmdForm();
                    followWorkListCmdForm.setFollowId(patientRecordTemplate.getFollowId());
                    followWorkListCmdForm.setResearchId(patientRecordTemplate.getResearchId());
                    followWorkListCmdForm.setPatientId(patientInfo.getPatientId());
                    if (followWorkListCmdForm.getFollowId()!=null
                            && followWorkListCmdForm.getResearchId()!=null
                            && followWorkListCmdForm.getPatientId()!=null){
                        patientRecordMapper.deletePatientRecord(followWorkListCmdForm);
                    }
                }
            }

        }
    }

    private void checkCancelResearch(FollowWorkEnrollmentHandleForm workEnrollmentHandleForm) {
        PatientRecordVo query = new PatientRecordVo();
        query.setResearchId(workEnrollmentHandleForm.getResearchId());
        query.setPatientId(workEnrollmentHandleForm.getPatientId());
        List<PatientRecordVo> patientRecordVos = patientRecordMapper.selectEcdPatientRecordVoList(query);
        if (CollectionUtils.isEmpty(patientRecordVos)) {
            throw new ServiceException("该患者没有入组记录");
        }
        List<PatientRecordVo> collect = patientRecordVos.stream().filter(x -> !"0".equals(x.getFollowStatus())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect)) {
            throw new ServiceException("该患者已经提交数据, 不能取消");
        }
    }

    private void cancelPopNotice(ResearchInfo researchInfo, String patientId) {
        SysPopNotice query = new SysPopNotice();
        query.setResearchId(researchInfo.getId());
        query.setUserId(patientId);
        List<SysPopNotice> sysPopNotices = sysPopNoticeService.selectSysPopNoticeList(query);
        if (CollectionUtils.isEmpty(sysPopNotices)) {
            return;
        }
        sysPopNotices.forEach(x -> {
            x.setIsread(1);
            sysPopNoticeService.updateSysPopNotice(x);
        });
    }

    private void createPopNotice(ResearchInfo researchInfo, String patientId) {
        SysPopNotice sysPopNotice = new SysPopNotice();
        sysPopNotice.setResearchName(researchInfo.getProjectName());
        sysPopNotice.setUserId(patientId);
        sysPopNotice.setResearchId(researchInfo.getId());
        sysPopNotice.setIsread(0);
        sysPopNotice.setResearchDescription(researchInfo.getResearchSynopsis());
        sysPopNoticeService.insertSysPopNotice(sysPopNotice);
    }

    @Override
    public String isCall(Long recordId) {
        PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByRecordId(recordId);
        ResearchOutbound researchOutbound = researchOutboundMapper.selectByResearchIdAndType(patientRecord.getResearchId(),"1");
        if(researchOutbound!=null){
            PatientRecordTemplate patientRecordTemplate = patientRecordTemplateMapper.selectByResearchIdAndNum(patientRecord.getResearchId(), 1);
            if(patientRecordTemplate!=null){
                if(patientRecordTemplate.getFollowInterval()==1){
                    //间隔1天，说明需要提示
                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);


                }
            }
        }
        return null;
    }

    @Override
    @Transactional
    public int allocatedHandle(FollowWorkEnrollmentHandleForm workEnrollmentHandleForm) {
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(workEnrollmentHandleForm.getResearchId());
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setPatientId(workEnrollmentHandleForm.getPatientId());
        patientInfo.setEnrollmentStatus("1");
        patientInfo.setEnrollmentTime(new Date());
        patientInfo.setResearchId(workEnrollmentHandleForm.getResearchId());
        patientInfo.setAllocatedHandlerStatus("3");
        if (researchInfo != null){
            patientInfo.setEnrollmentResultContent("入组变更到"+researchInfo.getProjectName());
        }
        generateBaseRecord(patientInfo);
        patientInfoMapper.updatePatientInfo(patientInfo);
        return 1;
    }

    @Override
    public List<ResearchDiseasesSelect> diseasesResearchSelect(Long userId) {
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        List<ResearchDiseases> researchDiseases = new ArrayList<>();
        if("1".equals(sysUser.getIsAlldeptResearch())){
            researchDiseases = researchDiseasesMapper.selectResearchDiseasesByAuth2(sysUser.getDeptId());
        }else{
            researchDiseases = researchDiseasesMapper.selectResearchDiseasesByUserId(userId);
        }

        if (researchDiseases == null || researchDiseases.isEmpty()){
            return new ArrayList<>();
        }
        TreeForm tf = new TreeForm();
//        tf.setDeptId(sysUser.getDeptId());
        tf.setUserId(userId);
        List<ResearchInfo> researchInfos = researchInfoMapper.selectResearchInfoByUserId(tf);
        Map<Long, List<ResearchInfo>> researchMap = null;
        if (researchInfos != null && !researchInfos.isEmpty()){
            researchMap = researchInfos.stream().collect(Collectors.groupingBy(ResearchInfo :: getDiseaseTypeId));
        }
        Map<Long, List<ResearchInfo>> finalResearchMap = researchMap;
        return researchDiseases.stream().map(x->new ResearchDiseasesSelect(x,
                finalResearchMap != null && !finalResearchMap.isEmpty()
                        ? (finalResearchMap.containsKey(x.getDiseaseId()) ? finalResearchMap.get(x.getDiseaseId()).stream().map(y->new ResearchDiseasesSelect(y)).collect(Collectors.toList()) : null)
                        : null)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int visitTimeHandle(FollowOverTimeHandleForm followOverTimeHandleForm) {
        PatientRecord patientRecord = new PatientRecord();
        patientRecord.setRecordId(followOverTimeHandleForm.getRecordId());
        patientRecord.setOverTimeStatus("3");
        patientRecord.setOverEvaluateStatusBack("1");
        patientRecord.setOverTimeHandleStatus(followOverTimeHandleForm.getHandleType());
        patientRecord.setOverTimeHandleTime(DateUtils.getNowDate());
        //1更新随访时间2忽略本次3失访
        if ("1".equals(followOverTimeHandleForm.getHandleType())){
            patientRecord.setVisitTime(followOverTimeHandleForm.getVisitTime());
            patientRecord.setFollowTipStatus(FollowTipStatusConstants.CHANGE_OF_FOLLOW_UP_TIME_REMINDER);
        }else if ("2".equals(followOverTimeHandleForm.getHandleType())){
            patientRecord.setFollowTipStatus(FollowTipStatusConstants.IGNORE_THIS_FOLLOW_UP_REMINDER);
            patientRecord.setIgnoreType(followOverTimeHandleForm.getIgnoreType());
        }else if ("3".equals(followOverTimeHandleForm.getHandleType())){
            PatientRecord recordDB = patientRecordMapper.selectPatientRecordByRecordId(followOverTimeHandleForm.getRecordId());
            PatientInfo patientInfo = new PatientInfo();
            patientInfo.setPatientId(recordDB.getPatientId());
            patientInfo.setLossCause(followOverTimeHandleForm.getLossCause());
            patientInfo.setLossCauseOther(followOverTimeHandleForm.getLossCauseOther());
            patientInfo.setPatientStatus("4");//失访
            patientInfoMapper.updatePatientInfo(patientInfo);
            patientRecord.setFollowTipStatus(FollowTipStatusConstants.MISSING_VISIT_REMINDER);
            patientRecord.setLoseType(followOverTimeHandleForm.getLossCause());
            patientRecord.setLoseReason(followOverTimeHandleForm.getLossCauseOther());

            PatientRecord patientRecord1 = new PatientRecord();
            patientRecord1.setPatientId(recordDB.getPatientId());
            patientRecord1.setOverTimeStatus("1");
            List<PatientRecord> patientRecords = patientRecordMapper.selectList(patientRecord1);
            for (PatientRecord record : patientRecords) {
                log.info("失访记录{}",record.getRecordId());
                if (Objects.nonNull(recordDB.getPatientId()) && recordDB.getPatientId().equals(record.getPatientId())) {
//                if (Objects.isNull(record.getLockVisitTime())||record.getLockVisitTime().getTime() >= recordDB.getLockVisitTime().getTime()) {
                    record.setFollowTipStatus(FollowTipStatusConstants.MISSING_VISIT_REMINDER);
                    record.setLoseType(followOverTimeHandleForm.getLossCause());
                    record.setLoseReason(followOverTimeHandleForm.getLossCauseOther());
                    record.setOverTimeStatus("3");
                    patientRecordMapper.updateOverTimePatientRecord(record);
                }
//                }
                //将未进行的任务全部置为空
                patientRecordOutboundMapper.updateChangeOutboundStatusByRecordId("1", "0", record.getRecordId());
            }
        }
        patientRecordOperLogMapper.insertPatientRecordOperLog(new PatientRecordOperLog(followOverTimeHandleForm.getUserId(),followOverTimeHandleForm.getRecordId(),"3",DateUtils.getNowDate()));
        patientRecord.setUpdateBy(followOverTimeHandleForm.getUpdateBy());

        return patientRecordMapper.updatePatientRecord(patientRecord);
    }

    @Override
    @Transactional
    public int submitOpinionHandle(FollowWorkHandleForm followWorkHandleForm) {
        PatientRecord patientRecord = new PatientRecord();
        if ("2".equals(followWorkHandleForm.getAdjustType())){//更换队列
            if ("2".equals(followWorkHandleForm.getAdjustType())) {
                PatientInfo old = patientInfoMapper.selectPatientInfoByOldId(followWorkHandleForm.getPatientInfoId());
                if (!Objects.isNull(old)) {
                    return -1;
                }
            }

            PatientInfo patientInfo = new PatientInfo();
            patientInfo.setPatientId(followWorkHandleForm.getPatientInfoId());
            patientInfo.setPatientStatus("3");
            patientInfo.setPatientActivityStatus("2");
            patientInfo.setAdjustType(followWorkHandleForm.getAdjustType());
            patientInfo.setPlanResearchId(followWorkHandleForm.getResearchId());
            patientInfo.setUpdateBy(followWorkHandleForm.getUpdateBy());
            patientInfoMapper.updatePatientInfo(patientInfo);
            PatientInfo old = patientInfoMapper.selectPatientInfoByPatientId(followWorkHandleForm.getPatientInfoId());
            ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(followWorkHandleForm.getResearchId());
            PatientInfo newPatientInfo = new PatientInfo(old, researchInfo, followWorkHandleForm.getUpdateBy());
            newPatientInfo.setOldPatientId(old.getPatientId());
            patientInfoMapper.insertPatientInfo(newPatientInfo);
            //新队列记录初始化
            generateBaseRecord(newPatientInfo);
            ResearchPatient researchPatient = new ResearchPatient();
            researchPatient.setResearchId(newPatientInfo.getResearchId());
            researchPatient.setPersonId(newPatientInfo.getPersonId());
            researchPatient.setPatientId(newPatientInfo.getPatientId());
            researchPatient.setCreateTime(DateUtils.getNowDate());
            researchPatientMapper.insertResearchPatient(researchPatient);

            //旧队列保持原来的记录
            patientRecord.setFollowTipStatus(FollowTipStatusConstants.CHANGE_QUEUE_ALERT);
            patientRecord.setAccountId(old.getAccountId());
        }else if("1".equals(followWorkHandleForm.getAdjustType())){
            PatientInfo patientInfo = new PatientInfo();
            patientInfo.setPatientId(followWorkHandleForm.getPatientInfoId());
            patientInfo.setAdjustType(followWorkHandleForm.getAdjustType());
            patientInfoMapper.updatePatientInfo(patientInfo);
            patientRecord.setFollowTipStatus(FollowTipStatusConstants.FOLLOW_UP_OPINION_REMINDER);

            PatientInfo pi = patientInfoMapper.selectPatientInfoByPatientId(followWorkHandleForm.getPatientInfoId());
            patientRecord.setAccountId(pi.getAccountId());
        }
        patientRecord.setRecordId(followWorkHandleForm.getRecordId());
        patientRecord.setFollowOpinion(followWorkHandleForm.getFollowOpinion());
        patientRecord.setFollowOpinionUserId(followWorkHandleForm.getUpdateUserId());
        patientRecord.setEdcStatus("1");
        patientRecord.setFollowStatus("3");
        patientRecord.setEvaluateStatusBack("1");
        patientRecord.setEvaluateStatus("1");
        patientRecord.setFollowCompleteTime(DateUtils.getNowDate());
        patientRecord.setUpdateBy(followWorkHandleForm.getUpdateBy());
        patientRecord.setUpdateBy(followWorkHandleForm.getUpdateBy());
        patientRecordMapper.updatePatientRecord(patientRecord);

        //保存提醒消息
        PatientRecord prd = patientRecordMapper.selectPatientRecordByRecordId(followWorkHandleForm.getRecordId());
        patientRecord.setName(prd.getName());
        String msgContent = "您有一条随访建议请及时查看";
        AccountNotice accountNotice = new AccountNotice(patientRecord,msgContent,"9");
        accountNoticeMapper.insertAccountNotice(accountNotice);
//        umpushService.sendCustomizedcastMessage(2,String.valueOf(patientRecord.getAccountId()),msgContent,accountNotice.getExtendJson());

        return 1;
    }

    /**
     * 生成基线档案
     */
    private void generateBaseRecord(PatientInfo patientInfo){
        List<PatientRecordTemplate> patientRecordTemplates = patientRecordTemplateMapper.selectPatientRecordTemplateByResearchId(patientInfo.getResearchId());
        if (patientRecordTemplates != null && !patientRecordTemplates.isEmpty()){
            patientRecordTemplates.sort(Comparator.comparing(PatientRecordTemplate :: getFollowNum));
            PatientRecord patientRecord = new PatientRecord(patientInfo, patientRecordTemplates.get(0));
            patientRecordMapper.insertPatientRecord(patientRecord);
        }
    }

    //待分配的患者分配完队列后更新记录
    private void generateUpdateBaseRecord(PatientInfo patientInfo){
        List<PatientRecordTemplate> patientRecordTemplates = patientRecordTemplateMapper.selectPatientRecordTemplateByResearchId(patientInfo.getResearchId());
        if (patientRecordTemplates != null && !patientRecordTemplates.isEmpty()){
            PatientRecord patientRecord = patientRecordMapper.selectOldByPatientId(patientInfo.getPatientId());
            if(patientRecord!=null){
                patientRecord.setResearchId(patientInfo.getResearchId());
                patientRecord.setFollowId(patientRecordTemplates.get(0).getFollowId());
                patientRecordMapper.updatePatientRecord(patientRecord);
            }
        }
    }


    @Override
    public List<String> getResearchInfosOfperson(Long patientId) {
        PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(patientId);
        // 获取所属personId
        PatientInfo patientQuery = new PatientInfo();
        patientQuery.setPersonId(patientInfo.getPersonId());
        List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoList2(patientQuery);
        // patientInfos中的名称组成字符串
        List<String> patientNames = patientInfos.stream()
                .filter(item->"1".equals(item.getEnrollmentStatus()))
                .map(item -> item.getProjectName())
                .collect(Collectors.toList());
        return patientNames;
    }

    @Override
    public AjaxResult saveOrUpdatePatientRecord(PatientRecordSaveUpdateInfoForm infoForm, LoginUser loginUser) {
        if (StringUtils.isNotBlankAndNotEmpty(infoForm.getImageUrlsJson())){
            List<PatientRecordFilesForm> list = com.alibaba.fastjson2.JSON.parseArray(infoForm.getImageUrlsJson(), PatientRecordFilesForm.class);
            infoForm.setImageUrls(list);
        }
        if (StringUtils.isNotBlankAndNotEmpty(infoForm.getPropertyFormsJson())){
            List<FMedicalPropertyForm> list = com.alibaba.fastjson2.JSON.parseArray(infoForm.getPropertyFormsJson(), FMedicalPropertyForm.class);
            infoForm.setPropertyForms(list);
        }

        if (StringUtils.isNotBlankAndNotEmpty(infoForm.getQuestionRecordJson())){
            QuestionRecordForm questionRecordForm = com.alibaba.fastjson2.JSON.parseObject(infoForm.getQuestionRecordJson(), QuestionRecordForm.class);
            infoForm.setQuestionRecordForm(questionRecordForm);
        }

        return saveOrUpdatePatientRecordDetail(infoForm,loginUser);
    }

    public AjaxResult saveOrUpdatePatientRecordDetail(PatientRecordSaveUpdateInfoForm infoForm, LoginUser loginUser) {


        PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByRecordId(infoForm.getRecordId());
        if (patientRecord == null) {//档案不存在
            return new AjaxResult(HttpStatus.ERROR, "档案不存在");
        }
        PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(patientRecord.getPatientId());
        patientRecord.setAccountId(patientInfo.getAccountId());


        // 属性值集合处理
        handlePropertyFormsInfo(infoForm, patientRecord);
        //上传文件[图片]
        handleFileImagesInfo(infoForm, patientRecord);

        PatientRecord record = new PatientRecord();
        record.setCreateBy(Constants.CREATEOR_DOCTOR_KEY+loginUser.getUserId());//创建人可能是医生或患者，为避免id冲突，医生id前加上固定值标识
        record.setUpdateBy(String.valueOf(loginUser.getUserId()));
        record.setIsSubmit(infoForm.getIsSubmit());
        //质疑处理
        handleRequestionRecordFormInfo(infoForm, record);
        // 随访记录

        //变更record状态
        handlePatientRecordInfo(patientRecord, record);

        // 修改患者队列随访状态
        handlePatientInfo(patientRecord, record, patientInfo);

        // 处理 patient_info 表省市县字段
        handleArea(patientRecord.getPatientId(), infoForm.getPropertyForms());

        //处理 patient_info  表就诊信息
        handleVisit(patientRecord.getPatientId(), infoForm.getPropertyForms());


        return AjaxResult.success();
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
                //删除recordId对应的res_dynamic_历史数据
                dynamicCommonMapper.executeDeleteSql(dynamicSql.buildDeleteDynamic(patientRecord.getResearchId(),
                        patientRecord.getRecordId()));
                //新增res_dynamic_数据
                String dynamicSqls = dynamicSql.buildBatchDynamicSql(dynamicForm, dyList.get());
                dynamicCommonMapper.executeInsertSql(dynamicSqls);
            }
            Optional<List<FMedicalPropertyForm>> otList = Optional.ofNullable(dyMap.get("0"));//其他数据
            if (otList.isPresent()) {
                Optional<List<FMedicalPropertyForm>> ltList = otList.map(lt -> {
                    List<FMedicalPropertyForm> l =
                            lt.stream().filter(f -> PropertyType.largeText(f.getPropertyType())).collect(Collectors.toList());
                    return l.isEmpty() ? null : l;
                });
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
                    //todo
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
                recordFiles.forEach(x->patientRecordFilesMapper.insertPatientRecordFiles(x));
            }

        }

    private void handleRequestionRecordFormInfo(PatientRecordSaveUpdateInfoForm infoForm, PatientRecord record) {
        if (!"{}".equals(infoForm.getQuestionRecordJson()) && infoForm.getQuestionRecordForm() != null) {
            QuestionRecord questionRecord = new QuestionRecord();
            questionRecord.setReplyContent(infoForm.getQuestionRecordForm().getReplyContent());
            questionRecord.setReplyTime(new Date());
            questionRecord.setQuestionId(infoForm.getQuestionRecordForm().getQuestionId());
            questionRecordMapper.updateQuestionRecord(questionRecord);

        }
    }

    private void handlePatientRecordInfo(PatientRecord patientRecord, PatientRecord record) {
        Date nowTime = new Date();
        record.setRecordId(patientRecord.getRecordId());
        if ("0".equals(patientRecord.getFillStatus())) {
            record.setFillStatus("1");
            record.setFillTime(nowTime);
        }
        if (record.getIsSubmit() == 1) {
            record.setIsSubmit(1);
            record.setFollowStatus("3");
            record.setEvaluateStatus("1");
            record.setName(patientRecord.getName());


            record.setEdcStatus("1");
            record.setEvaluateStatusBack("1");
            record.setFollowCompleteTime(nowTime);

            record.setUpdateTime(nowTime);
            record.setQuestionStatus("0");//回复后质疑状态  无预警

            record.setFollowTipStatus(FollowTipStatusEnum.NO_STATUS.getIndex());//医生填表，无状态提示
            record.setFollowSubmitTime(nowTime);
        }

        patientRecordMapper.updatePatientRecord(record);
    }

    private void handlePatientInfo(PatientRecord patientRecord, PatientRecord record,PatientInfo patientInfo){

        if (record.getIsSubmit() == 1) {
            //已进入随访状态的人，不因填写基线返回基线状态
            if(Integer.parseInt(patientInfo.getPatientStatus())<2){
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
                //todo
                pi.setProvinceId(Integer.valueOf(jsonArray.getString(0)));
                pi.setCityId(Integer.valueOf(cityId));
                pi.setAreaId(Integer.valueOf(jsonArray.getString(jsonArray.size()-1)));
                patientInfoMapper.updatePatientInfo(pi);
            }
        } catch (Exception e) {
            log.error("handleArea error is {}", e.getMessage());
        }

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
                pi.setVisitType(Integer.valueOf(propertyValue));
                patientInfoMapper.updatePatientInfo(pi);
            }
        } catch (Exception e) {
            log.error("handleVisit error is {}", e.getMessage());
        }

    }

    public void handleWarnsInfo(PatientRecord patientRecord, List<FMedicalPropertyForm> formList,
                                PatientRecordSaveUpdateInfoForm patientRecordSaveUpdateInfoForm) {
        if (patientRecordSaveUpdateInfoForm.getIsSubmit() == 0) {
            return;
        }
        PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(patientRecord.getPatientId());
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
                                        com.alibaba.fastjson2.JSON.parseArray(jsonObject.getString("options").toString(),
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
                        AccountNotice an = new AccountNotice(r.getUserId(),patientRecord,doctorMsgContent,"18");
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

    private void saveAccountNotice(PatientRecord patientRecord) {
        List<ResearchUserRole> researchUserRoleList = researchUserRoleMapper.findUserRoleByResearchId(patientRecord.getResearchId());
        if(researchUserRoleList.size()>0){
            String doctorMsgContent = "患者提交新的随访请尽快处理";
            for(ResearchUserRole r:researchUserRoleList){
                AccountNotice an = new AccountNotice(r.getUserId(),patientRecord,doctorMsgContent,"16");
                accountNoticeMapper.insertAccountNotice(an);
            }
        }
    }

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

    public void callByRecordId(Long recordId) {
        PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByRecordId(recordId);
        PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(patientRecord.getPatientId());
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
}
