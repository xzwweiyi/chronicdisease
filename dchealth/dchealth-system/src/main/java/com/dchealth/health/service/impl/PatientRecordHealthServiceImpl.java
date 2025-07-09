package com.dchealth.health.service.impl;

import com.dchealth.cms.domain.CmsArticle;
import com.dchealth.cms.mapper.CmsArticleMapper;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.health.domain.PatientRecordHealth;
import com.dchealth.health.mapper.PatientRecordHealthMapper;
import com.dchealth.health.service.IPatientRecordHealthService;
import com.dchealth.health.vo.req.RecordHealthReq;
import com.dchealth.health.vo.req.RecordHealthTree;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.ResearchPatient;
import com.dchealth.medical.mapper.PatientInfoMapper;
import com.dchealth.medical.mapper.PatientRecordFilesMapper;
import com.dchealth.medical.mapper.PatientRecordMapper;
import com.dchealth.medical.mapper.ResearchPatientMapper;
import com.dchealth.medical.service.common.CommonService;
import com.dchealth.research.domain.vo.ResearchDiseasesVO;
import com.dchealth.research.domain.vo.ResearchInfoVo;
import com.dchealth.research.mapper.ResearchDiseasesMapper;
import com.dchealth.template.domain.FMedicalForm;
import com.dchealth.template.mapper.FMedicalExamSubjectMapper;
import com.dchealth.template.mapper.FMedicalFormMapper;
import com.dchealth.template.mapper.FMedicalPropertyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 患者档案健康管理Service业务层处理
 *
 * @author lchbu
 * @date 2025-06-24
 */
@Service
public class PatientRecordHealthServiceImpl extends CommonService implements IPatientRecordHealthService {
    @Autowired
    private PatientRecordHealthMapper patientRecordHealthMapper;

    @Resource
    private PatientRecordMapper patientRecordMapper;

    @Resource
    private PatientRecordFilesMapper patientRecordFilesMapper;

    @Resource
    private FMedicalFormMapper fMedicalFormMapper;

    @Resource
    private FMedicalPropertyMapper fMedicalPropertyMapper;

    @Resource
    private FMedicalExamSubjectMapper fMedicalExamSubjectMapper;

    @Resource
    private PatientInfoMapper patientInfoMapper;

    @Resource
    private ResearchPatientMapper researchPatientMapper;

    @Resource
    private ResearchDiseasesMapper researchDiseasesMapper;

    @Resource
    private CmsArticleMapper cmsArticleMapper;


    /**
     * 查询患者档案健康管理
     *
     * @param recordHealthReq 患者档案健康管理主键
     * @return 患者档案健康管理
     */
    @Override
    public PatientRecordHealth selectPatientRecordHealthByRecordId(RecordHealthReq recordHealthReq) {
//        PatientDetailVO patientDetailVO = new PatientDetailVO();
//        patientBaseInfo( recordHealthReq.getPatientId(),patientDetailVO,x->{});
//
//        if(recordHealthReq.getRecordId()==null){
//            PatientRecord pr = patientRecordMapper.selectOldByPatientId(recordHealthReq.getPatientId());
//            recordHealthReq.setRecordId(pr.getRecordId());
//        }
//
//        PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByRecordId(recordHealthReq.getRecordId());
//        //超窗不需要进行中状态，所有的处理均放在待处理
//        /*        if("3".equals(followWorkDetailsForm.getDetailType()) && "1".equals(patientRecord.getOverTimeStatus())){
//            //超窗查看详情且是待处理，需要修改成进行中
//            patientRecord.setOverTimeStatus("2");
//            patientRecordMapper.updatePatientRecord(patientRecord);
//        }*/
//        patientDetailVO.setSubmit(patientRecord.getIsSubmit() != null && Integer.valueOf("1").equals(patientRecord.getIsSubmit()));
//
//
//        List<PatientRecordFiles> patientRecordFiles = patientRecordFilesMapper.selectPatientRecordFilesByHealthId(recordHealthReq.getHealthId());
//        Map<Long, List<String>> filesMap = null;
//        if (patientRecordFiles != null && !patientRecordFiles.isEmpty()){
//            filesMap = patientRecordFiles.stream()
//                    .filter(f-> f.getPropertyId() != null)
//                    .collect(Collectors.groupingBy(PatientRecordFiles :: getPropertyId,
//                            Collectors.mapping(PatientRecordFiles :: getImageUrl, Collectors.toList())));
//        }
//
//        List<FMedicalForm> formList =fMedicalFormMapper.selectHealthFMedicalFormByMedicalFollowId(patientRecord.getFollowId());
//        List<MedicalFormVO> medicalFormVOS = BeanUtils.copyList(formList, MedicalFormVO :: new);
//
//        List<MedicalFormVO> medicalFormVOSResult = new ArrayList<>();
//        List<MedicalFormVO> medicalFormExamVOSResult = new ArrayList<>();
//        //判断是否存在组的情况
//        Map<Long,List<MedicalFormVO>> medicalFormMap = new HashMap<>();
//        if (medicalFormVOS != null && !medicalFormVOS.isEmpty()){
//            medicalFormMap = medicalFormVOS.stream().filter( x-> x.getParentId()>0).collect(Collectors.groupingBy(MedicalFormVO::getParentId));
//        }
//
//        // 获取formid
////        formList.stream().collect()
//        List<FMedicalProperty> properties = fMedicalPropertyMapper.selectFMedicalPropertyByMedicalFormIds();
//        List<MedicalPropertyVO> medicalPropertyVOS = BeanUtils.copyList(properties, MedicalPropertyVO :: new);
//
////        //查询所有的问卷
////        List<FMedicalExamSubject> examSubjects = fMedicalExamSubjectMapper.selectFMedicalExamSubjectByFollowId(patientRecord.getFollowId());
////        List<MedicalPropertyVO> medicalPropertyExamVOS = BeanUtils.copyList(examSubjects, MedicalPropertyVO :: new);
////
//
//        if (medicalFormVOS != null && !medicalFormVOS.isEmpty()){
//            Map<Long,List<MedicalPropertyVO>> map = null;
//            if (medicalPropertyVOS != null && !medicalPropertyVOS.isEmpty()){
//                map = medicalPropertyVOS.stream().collect(Collectors.groupingBy(MedicalPropertyVO :: getMedicalFormId));
//            }
//
////            Map<Long,List<MedicalPropertyVO>> exaMap = new HashMap<>();
////            if (medicalPropertyExamVOS != null && !medicalPropertyExamVOS.isEmpty()){
////                exaMap = medicalPropertyExamVOS.stream().collect(Collectors.groupingBy(MedicalPropertyVO :: getMedicalFormId));
////            }
//
////            QuestionRecordForm questionRecordForm = new QuestionRecordForm();
////            questionRecordForm.setRecordId(followWorkDetailsForm.getRecordId());
////            List<QuestionRecordForm> forms = questionRecordFormMapper.selectQuestionRecordFormList(questionRecordForm);
////            Map<Long,List<QuestionRecordForm>> formMap = new HashMap<>();
////            if (forms != null && !forms.isEmpty()){
////                formMap = forms.stream().collect(Collectors.groupingBy(QuestionRecordForm :: getMedicalFormId));
////            }
//            for (MedicalFormVO medicalFormVO : medicalFormVOS) {
//                if(medicalFormVO.getParentId()>0){
//                    continue;
//                }
//                if (map != null && map.containsKey(medicalFormVO.getMedicalFormId()) && !"1".equals(medicalFormVO.getOpenMultiple())){
//                    medicalFormVO.setPropertyVOS(map.get(medicalFormVO.getMedicalFormId()));
//                }
////                if (exaMap != null && exaMap.containsKey(medicalFormVO.getMedicalFormId()) && !"1".equals(medicalFormVO.getOpenMultiple())){
////                    medicalFormVO.setPropertyExamVOS(exaMap.get(medicalFormVO.getMedicalFormId()));
////                }
//                if(medicalFormMap.containsKey(medicalFormVO.getMedicalFormId())){
//                    List<MedicalFormVO> voList = medicalFormMap.get(medicalFormVO.getMedicalFormId());
//                    if(voList.size()>0){
//                        for(MedicalFormVO v:voList){
//                            if (map != null && map.containsKey(v.getMedicalFormId()) ){
//                                v.setPropertyVOS(map.get(v.getMedicalFormId()));
//                            }
//                            if(exaMap!=null && exaMap.containsKey(v.getMedicalFormId())){
//                                v.setPropertyExamVOS(exaMap.get(v.getMedicalFormId()));
//                            }
//                        }
//                        medicalFormVO.setChildMedicalFormVOS(voList);
//                    }
//                }
//
////                if("3".equals(medicalFormVO.getFormType())){
////                    medicalFormExamVOSResult.add(medicalFormVO);
////                } else{
////                    medicalFormVOSResult.add(medicalFormVO);
////                }
//
//
//                //随访工作站详情页面判断模板id是否默认选中
//                if(formMap.containsKey(medicalFormVO.getMedicalFormId())){
//                    medicalFormVO.setChecked("true");
//                }
//            }
//        }
//
//        // 处理预警源
////        dealWarnSource(followWorkDetailsForm.getRecordId(), medicalFormVOSResult);
//
//        List<DynamicCommonProperty> t = new ArrayList<>();
//        DynamicForm dynamicForm = new DynamicForm();
//        dynamicForm.setRecordId(recordHealthReq.getRecordId());
//        dynamicForm.setType("1");
//        //大文本
//        dynamicForm.setSql(dynamicSql.buildSelectLargeText(patientRecord.getResearchId()));
//        List<DynamicCommonProperty> largeTextList = dynamicCommonMapper.selectBySql(dynamicForm);
//        if (largeTextList != null && !largeTextList.isEmpty()){
//            t.addAll(largeTextList);
//        }
//        //小文本
//        dynamicForm.setSql(dynamicSql.buildSelectText(patientRecord.getResearchId()));
//        List<DynamicCommonProperty> textList = dynamicCommonMapper.selectBySql(dynamicForm);
//        if (textList != null && !textList.isEmpty()){
//            t.addAll(textList);
//        }
//        //动态
//        dynamicForm.setSql(dynamicSql.buildSelectDynamic(patientRecord.getResearchId()));
//        List<DynamicCommonProperty> dynaicList = dynamicCommonMapper.selectBySql(dynamicForm);
//        if (dynaicList != null && !dynaicList.isEmpty()){
//            t.addAll(dynaicList);
//        }
//        t= new ArrayList<>(t.stream().collect(
//                Collectors.collectingAndThen(Collectors.toCollection(() -> new
//                        TreeSet<>(Comparator.comparing(DynamicCommonProperty::getPropertyId))), ArrayList::new)));
//
//        Map<Long, DynamicCommonProperty> dp = t.stream().collect(Collectors.toMap(DynamicCommonProperty :: getPropertyId, item -> item));
//        if (medicalPropertyVOS != null && !medicalPropertyVOS.isEmpty()){
//            for (MedicalPropertyVO p : medicalPropertyVOS) {
//                if (PropertyType.eq(p.getPropertyType(), PropertyType.UPLOAD)){
//                    if (filesMap != null && !filesMap.isEmpty() && filesMap.containsKey(p.getId())){
//                        p.setImageUrls(filesMap.get(p.getId()));
//                    }
//                }else{
//                    if (dp != null && !dp.isEmpty() && dp.containsKey(p.getId())){
//                        DynamicCommonProperty propertyVO = dp.get(p.getId());
//                        if (propertyVO != null){
//                            p.setPropertyValue(propertyVO.getPropertyValue());
//                            p.setOther(propertyVO.getOther());
//                        }
//                    }
//                }
//            }
//        }
//        //问卷值
//        if (medicalPropertyExamVOS != null && !medicalPropertyExamVOS.isEmpty()){
//            for (MedicalPropertyVO p : medicalPropertyExamVOS) {
//                if (PropertyType.eq(p.getPropertyType(), PropertyType.UPLOAD)){
//                    if (filesMap != null && !filesMap.isEmpty() && filesMap.containsKey(p.getId())){
//                        p.setImageUrls(filesMap.get(p.getId()));
//                    }
//                }else{
//                    if (dp != null && !dp.isEmpty() && dp.containsKey(p.getId())){
//                        DynamicCommonProperty propertyVO = dp.get(p.getId());
//                        if (propertyVO != null){
//                            p.setPropertyValue(propertyVO.getPropertyValue());
//                            p.setOther(propertyVO.getOther());
//                        }
//                    }
//                }
//            }
//        }
//
//
//        WarnEarlyRecord warnEarlyRecord = new WarnEarlyRecord();
//        warnEarlyRecord.setRecordId(followWorkDetailsForm.getRecordId());
//        List<WarnEarlyRecord> warnEarlyRecordList = warnEarlyRecordMapper.selectListByRecordId(warnEarlyRecord);
//        patientDetailVO.setWarnEarlyRecordList(warnEarlyRecordList);
//        patientDetailVO.setMedicalFormVOS(medicalFormVOSResult);
//        patientDetailVO.setMedicalFormExamVOS(medicalFormExamVOSResult);
//        return patientDetailVO;
        return patientRecordHealthMapper.selectPatientRecordHealthByHealthId(recordHealthReq.getHealthId());
    }

    /**
     * 查询患者档案健康管理列表
     *
     * @param patientRecordHealth 患者档案健康管理
     * @return 患者档案健康管理
     */
    @Override
    public List<PatientRecordHealth> selectPatientRecordHealthList(PatientRecordHealth patientRecordHealth) {
        return patientRecordHealthMapper.selectPatientRecordHealthList(patientRecordHealth);
    }

    /**
     * 新增患者档案健康管理
     *
     * @param patientRecordHealth 患者档案健康管理
     * @return 结果
     */
    @Override
    public int insertPatientRecordHealth(PatientRecordHealth patientRecordHealth) {
        patientRecordHealth.setCreateTime(DateUtils.getNowDate());
        return patientRecordHealthMapper.insertPatientRecordHealth(patientRecordHealth);
    }

    /**
     * 修改患者档案健康管理
     *
     * @param patientRecordHealth 患者档案健康管理
     * @return 结果
     */
    @Override
    public int updatePatientRecordHealth(PatientRecordHealth patientRecordHealth) {
        patientRecordHealth.setUpdateTime(DateUtils.getNowDate());
        return patientRecordHealthMapper.updatePatientRecordHealth(patientRecordHealth);
    }

    /**
     * 批量删除患者档案健康管理
     *
     * @param healthIds 需要删除的患者档案健康管理主键
     * @return 结果
     */
    @Override
    public int deletePatientRecordHealthByHealthIds(Long[] healthIds) {
        return patientRecordHealthMapper.deletePatientRecordHealthByHealthIds(healthIds);
    }

    /**
     * 删除患者档案健康管理信息
     *
     * @param healthId 患者档案健康管理主键
     * @return 结果
     */
    @Override
    public int deletePatientRecordHealthByHealthId(Long healthId) {
        return patientRecordHealthMapper.deletePatientRecordHealthByHealthId(healthId);
    }

    @Override
    public List<RecordHealthTree> typeList(RecordHealthReq recordHealthReq) {
        List<RecordHealthTree> result = new ArrayList<>();
        //查询患者信息
        PatientInfo info = patientInfoMapper.selectPatientInfoByPatientId(recordHealthReq.getPatientId());
        //查询患者相关队列
        List<ResearchPatient> researchPatients = researchPatientMapper.selectResearchPatientByPatId(info.getPersonId());
        if (researchPatients != null && !researchPatients.isEmpty()) {
            //患者所在所有队列id
            List<Long> researchIds = researchPatients.stream().map(ResearchPatient::getResearchId).collect(Collectors.toList());
            ResearchInfoVo vo = new ResearchInfoVo();
            vo.setResearchIds(researchIds);
            //根据队列id查询相关病种队列信息
            List<ResearchDiseasesVO> researchDiseases = researchDiseasesMapper.selectResearchDiseasesListTreeByIds(vo);
            //根据患者id查询随访相关信息
            List<PatientRecord> patientRecords = patientRecordMapper.selectPatientRecordByPatientId(info.getPatientId());
            //根据患者id查询患教信息
            List<CmsArticle> cmsArticles = cmsArticleMapper.selectCmsArticleByAccountId(info.getAccountId());
            //通过followId查询所有的表单模版
            List<Long> followIds = patientRecords.stream().map(PatientRecord::getFollowId).collect(Collectors.toList());
            List<FMedicalForm> fMedicalForms = null;
            if (!CollectionUtils.isEmpty(followIds)) {
                fMedicalForms = fMedicalFormMapper.queryFMedicalFormHealthList(followIds);
            }


            //树状图处理
            if (researchDiseases != null && !researchDiseases.isEmpty()) {
                Map<Long, List<ResearchDiseasesVO>> map = researchDiseases.stream().collect(Collectors.groupingBy(ResearchDiseasesVO::getDeptId));
                List<FMedicalForm> finalFMedicalForms = fMedicalForms;
                map.forEach((k, v) -> result.add(new RecordHealthTree(v, patientRecords, cmsArticles, recordHealthReq.getPatientId(), finalFMedicalForms)));
            }
        }

        return result;
    }



}
