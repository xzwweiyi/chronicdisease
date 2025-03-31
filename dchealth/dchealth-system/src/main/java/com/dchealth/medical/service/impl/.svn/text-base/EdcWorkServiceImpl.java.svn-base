package com.dchealth.medical.service.impl;

import com.dchealth.cms.domain.CmsArticle;
import com.dchealth.cms.mapper.CmsArticleMapper;
import com.dchealth.common.annotation.DataScope;
import com.dchealth.common.constant.StatusConstants;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.utils.CommunalUtils;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.dynamic.templete.DynamicArticle;
import com.dchealth.dynamic.templete.DynamicFroms;
import com.dchealth.dynamic.templete.DynamicLargeText;
import com.dchealth.dynamic.templete.DynamicText;
import com.dchealth.medical.domain.*;
import com.dchealth.medical.domain.vo.EdcWorkTree;
import com.dchealth.medical.domain.vo.FollowMedicalFromTree;
import com.dchealth.medical.domain.vo.PatientRecordVo;
import com.dchealth.medical.mapper.*;
import com.dchealth.medical.service.IEdcWorkService;
import com.dchealth.question.domain.QuestionRecord;
import com.dchealth.question.mapper.QuestionRecordMapper;
import com.dchealth.research.domain.constant.PropertyType;
import com.dchealth.research.domain.vo.ResearchDiseasesVO;
import com.dchealth.research.domain.vo.ResearchInfoVo;
import com.dchealth.research.mapper.ResearchDiseasesMapper;
import com.dchealth.system.mapper.SysUserMapper;
import com.dchealth.template.domain.FMedicalExamSubject;
import com.dchealth.template.domain.FMedicalForm;
import com.dchealth.template.domain.FMedicalProperty;
import com.dchealth.template.domain.vo.*;
import com.dchealth.template.mapper.FMedicalExamFormMapper;
import com.dchealth.template.mapper.FMedicalExamSubjectMapper;
import com.dchealth.template.mapper.FMedicalFormMapper;
import com.dchealth.template.mapper.FMedicalPropertyMapper;
import com.dchealth.template.service.impl.FMedicalFormServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EdcWorkServiceImpl implements IEdcWorkService {

    @Autowired
    private ResearchDiseasesMapper researchDiseasesMapper;
    @Autowired
    private PatientRecordMapper patientRecordMapper;
    @Autowired
    private PatientInfoMapper patientInfoMapper;
    @Autowired
    private FMedicalFormMapper fMedicalFormMapper;
    @Autowired
    private FMedicalPropertyMapper fMedicalPropertyMapper;
    @Autowired
    private DynamicFroms dynamicFroms;
    @Autowired
    private DynamicText dynamicText;
    @Autowired
    private DynamicLargeText dynamicLargeText;
    @Autowired
    private DynamicArticle dynamicArticle;
    @Autowired
    private FMedicalFormServiceImpl fMedicalFormService;
    @Autowired
    private PatientExamRecordMapper patientExamRecordMapper;
    @Autowired
    private FMedicalExamFormMapper fMedicalExamFormMapper;
    @Autowired
    private CmsArticleMapper cmsArticleMapper;
    @Autowired
    private PatientRecordTemplateMapper patientRecordTemplateMapper;

    @Autowired
    private PatientRecordFilesMapper patientRecordFilesMapper;
    @Autowired
    private FMedicalExamSubjectMapper fMedicalExamSubjectMapper;

    @Autowired
    private PatientRecordEdcStatusMapper patientRecordEdcStatusMapper;

    @Autowired
    private QuestionRecordMapper questionRecordMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * edc树状列表
     * @return
     */
//    @DataScope(deptAlias = "d")
    @Override
    public EdcWorkTree edcTree(ResearchInfoVo vo) {
        vo.setResearchStatus("1");//只查询进行中的队列
        SysUser sysUser = sysUserMapper.selectUserById(vo.getUserId());
        if("1".equals(sysUser.getIsAlldeptResearch())){
            vo.setUserId(null);
            vo.setDeptId(sysUser.getDeptId());
        }
        List<ResearchDiseasesVO> researchDiseasesList = researchDiseasesMapper.selectResearchDiseasesListTree(vo);
        List <PatientRecord> patientRecords = patientRecordMapper.selectPatientRecordTodo();
        return new EdcWorkTree(researchDiseasesList, patientRecords);
    }

    /**
     * 查询edc任务列表
     * @param vo
     * @return
     */
    @Override
    public List<PatientRecordVo> selectEcdPatientRecordVoList(PatientRecordVo vo) {
        return patientRecordMapper.selectEcdPatientRecordVoList(vo);
    }

    /**
     * 根据随访id查询随访模块信息
     * 考虑子模块信息
     * @param vo
     * @return
     */
    @Override
    public MedicalDetailInfoVo edcRecordDetailInfo(FMedicalInfoReq vo) {
        MedicalDetailInfoVo mdVo = new MedicalDetailInfoVo();
        List<FMedicalForm> fMedicalForms = new ArrayList<>();
        //表单id
        Long medicalFormId = vo.getMedicalFormId();
        //队列id
        Long researchId = vo.getResearchId();
        //模板信息
        FMedicalForm fMedicalForm = fMedicalFormMapper.selectFMedicalFormByMedicalFormId(medicalFormId);
        //查询随访信息
        Long followId = fMedicalForm.getFollowId();
        //问卷id
        Long articleId = fMedicalForm.getArticleId();
        //模板属性信息
        List<FMedicalProperty> fMedicalProperties = fMedicalPropertyMapper.selectFMedicalPropertyByMedicalFormId(medicalFormId);
        if(fMedicalProperties != null && !fMedicalProperties.isEmpty()){
            String sql = null;
            if(followId != null){
                //判断属性存值类型（大文本、普通文本、动态表单）
                List<String> propertyTypes = fMedicalProperties.stream().map(FMedicalProperty::getPropertyType).collect(Collectors.toList());
                if(propertyTypes != null && (propertyTypes.contains(PropertyType.CASCADER) || propertyTypes.contains(PropertyType.TEXTAREA))){
                    //大文本
                    sql = dynamicLargeText.buildSelectSql(researchId, fMedicalForm.getMedicalFormId(), vo.getRecordId());
                } else {
                    //小文本
                    sql = dynamicText.buildSelectSql(researchId, fMedicalForm.getMedicalFormId(), vo.getRecordId());
                }
                if("1".equals(fMedicalForm.getDynamic())){
                    //动态表单
                    sql = dynamicFroms.buildSelectSql(researchId, fMedicalForm.getMedicalFormId(), vo.getRecordId());
                }
            }
            //将值赋值属性表
            List<FMedicalValueVo> valueVos = fMedicalPropertyMapper.selectFMedicalValues(sql);
            if(valueVos != null && !valueVos.isEmpty()){
                Map<Long, List<FMedicalValueVo>> valuesMap = valueVos.stream().collect(Collectors.groupingBy(FMedicalValueVo::getPropertyId));
                for (FMedicalProperty fMedicalProperty : fMedicalProperties) {
                    if(valuesMap.get(fMedicalProperty.getId()) != null){
                        fMedicalProperty.setPropertyValues(valuesMap.get(fMedicalProperty.getId()));
                    }
                }
            }
        }
        fMedicalForm.setMedicalProperties(fMedicalProperties);

        fMedicalForms.add(fMedicalForm);
        mdVo.setMedicalInfos(fMedicalForms);

        //查询人员信息
        Long patientId = vo.getPatientId();
        //查询患者基本信息
        PatientInfo patientInfo = patientInfoMapper.selectPatientDetailInfoByPatientId(patientId);
        if(patientInfo!=null && patientInfo.getIdCard()!=null){
            if(patientInfo.getIdCard().length()==18){
                patientInfo.setSex(Integer.parseInt(CommunalUtils.getSex(patientInfo.getIdCard())));
            }
        }
        mdVo.setPatientInfo(patientInfo);
        return mdVo;
    }

    /**
     * edc工作站档案详情table页信息
     * @param vo
     * @return
     */
    @Override
    public MedicalDetailInfoVo edcPatientRecordInfo(FMedicalInfoReq vo) {
        MedicalDetailInfoVo medicalDetailInfoVo = new MedicalDetailInfoVo();
        //后台模板数据
        List<FMedicalForm> fMedicalFormInfo = fMedicalFormService.getFMedicalFormInfo(vo);
        medicalDetailInfoVo.setMedicalInfos(fMedicalFormInfo);
        //app模板数据
        List<FMedicalForm> appPatientRecordInfo = fMedicalFormService.getAppPatientRecordInfo(vo);
        medicalDetailInfoVo.setAppMedicalInfos(appPatientRecordInfo);
        //查询患者基本信息
        PatientInfo patientInfo = patientInfoMapper.selectPatientDetailInfoByPatientId(vo.getPatientId());
        if(patientInfo!=null && patientInfo.getIdCard()!=null){
            if(patientInfo.getIdCard().length()==18){
                patientInfo.setSex(Integer.parseInt(CommunalUtils.getSex(patientInfo.getIdCard())));
            }
        }

        medicalDetailInfoVo.setPatientInfo(patientInfo);
        return medicalDetailInfoVo;
    }

    /**
     * edc模板数据保存
     *
     * @param proVo
     */
    @Override
    public void edcInsert(FMedicalPropertyVo proVo) {
        List<FMedicalValueVo> valueVos = proVo.getValueVos();
        //flag保存还是提交
        Boolean flag = proVo.getFlag();
        Long recordId = proVo.getRecordId();
        if(valueVos != null && !valueVos.isEmpty()){
            for (FMedicalValueVo vo : valueVos) {
                String sql = null;
                String sqlLog = null;
                String selectSql = null;
                String delSql = null;
                if(vo.getRecordId() != null){
                    FMedicalForm fMedicalForm = fMedicalFormService.selectFMedicalFormByMedicalFormId(vo.getFromId());
                    FMedicalProperty fMedicalProperty = new FMedicalProperty();
                    if("3".equals(fMedicalForm.getFormType())){
                        //自评量表
                        FMedicalExamSubject fMedicalExamSubject = fMedicalExamSubjectMapper.selectFMedicalExamSubjectById(vo.getPropertyId());
                        BeanUtils.copyProperties(fMedicalExamSubject, fMedicalProperty);
                    }else{
                        fMedicalProperty = fMedicalPropertyMapper.selectFMedicalPropertyById(vo.getPropertyId());
                    }
                    String propertyType = fMedicalProperty.getPropertyType();
                    //构建模板insert语句
                    if("1".equals(fMedicalForm.getDynamic())){
                        //动态表单
                        sql = dynamicFroms.buildInsertSql(vo);
                        // 表单日志存储
                        sqlLog = dynamicFroms.buildInsertLogSql(vo);
                        // 根据属性   筛选 拼接sql
                        selectSql = dynamicFroms.buildSelectSql1(vo.getResearchId(),vo.getFromId(),vo.getRecordId(),vo.getPropertyId(),"2");
                        // 根据属性   删除 拼接sql
                        delSql = dynamicFroms.buildDelSql(vo.getResearchId(),vo.getFromId(),vo.getRecordId(),vo.getPropertyId(),"2");
                    } else if (propertyType != null && PropertyType.largeText(propertyType)) {
                        //大文本
                        sql = dynamicLargeText.buildInsertSql(vo);
                        // 表单日志存储
                        sqlLog = dynamicLargeText.buildInsertLogSql(vo);
                        // 根据属性   筛选 拼接sql
                        selectSql = dynamicLargeText.buildSelectSql1(vo.getResearchId(),vo.getFromId(),vo.getRecordId(),vo.getPropertyId(),"2");
                        // 根据属性   删除 拼接sql
                        delSql = dynamicLargeText.buildDelSql(vo.getResearchId(),vo.getFromId(),vo.getRecordId(),vo.getPropertyId(),"2");
                    } else if(propertyType != null && PropertyType.text(propertyType)){
                        //小文本
                        sql = dynamicText.buildInsertSql(vo);
                        // 表单日志存储
                        sqlLog = dynamicText.buildInsertLogSql(vo);
                        // 根据属性   筛选 拼接sql
                        selectSql = dynamicText.buildSelectSql1(vo.getResearchId(),vo.getFromId(),vo.getRecordId(),vo.getPropertyId(),"2");
                        // 根据属性   删除 拼接sql
                        delSql = dynamicText.buildDelSql(vo.getResearchId(),vo.getFromId(),vo.getRecordId(),vo.getPropertyId(),"2");
                    } else if(propertyType != null && PropertyType.upLoad(propertyType)){
                        //图片保存
                        List<String> imageUrls = vo.getImageUrls();
                        if(imageUrls!=null && imageUrls.size() > 0){
                            //  查询图片是否保存  如果已保存  进行更新操作
                            PatientRecordFiles files = new PatientRecordFiles();
                            files.setPatientId(vo.getPatientId());
                            files.setRecordId(recordId);
                            files.setMedicalFormId(vo.getFromId());
                            files.setPropertyId(vo.getPropertyId());
                            List<PatientRecordFiles> recordFiles = patientRecordFilesMapper.selectPatientRecordFilesList(files);
                            if(recordFiles!=null && recordFiles.size() > 0){
                                for (PatientRecordFiles recordFile : recordFiles) {
                                    patientRecordFilesMapper.deletePatientRecordFilesById(recordFile.getId());
                                }
                            }

                            for (String imageUrl : imageUrls) {
                                PatientRecordFiles patientRecordFiles = new PatientRecordFiles();
                                patientRecordFiles.setImageUrl(imageUrl);
                                patientRecordFiles.setPatientId(vo.getPatientId());
                                patientRecordFiles.setRecordId(recordId);
                                patientRecordFiles.setMedicalFormId(vo.getFromId());
                                patientRecordFiles.setCreateTime(DateUtils.getNowDate());
                                patientRecordFiles.setPropertyId(vo.getPropertyId());
                                patientRecordFilesMapper.insertPatientRecordFiles(patientRecordFiles);
                            }

                        }

                    }
                }
                if(sql != null){
                    if(selectSql!=null){
                       Map map = fMedicalPropertyMapper.runSelectSql(selectSql);
                       if(map!=null && delSql!=null ){
                           fMedicalPropertyMapper.runDelSql(delSql);
                       }
                    }
                    //执行模板数据insert语句
                    fMedicalPropertyMapper.runInsertSql(sql);
                    // 执行模板log数据insert语句
                    if(sqlLog!=null){
                        fMedicalPropertyMapper.runInsertLogSql(sqlLog);
                    }
                }
            }
        }
//        if(flag){
//            //保存并提交-------edc状态：待质检
//            patientRecordMapper.updateEdcStatus(recordId, StatusConstants.STATUS_INSPECTED);
//        }

        PatientRecordEdcStatus pres = patientRecordEdcStatusMapper.selectByTwoId(proVo.getRecordId(),proVo.getMedicalFormId());
        if(flag){
            //保存并提交-------edc状态：待质检
            if(pres==null){
                PatientRecordEdcStatus patientRecordEdcStatus = new PatientRecordEdcStatus();
                patientRecordEdcStatus.setRecordId(proVo.getRecordId());
                patientRecordEdcStatus.setMedicalFormId(proVo.getMedicalFormId());
                patientRecordEdcStatus.setEdcStatus("2");
                patientRecordEdcStatus.setCreateTime(DateUtils.getNowDate());
                patientRecordEdcStatusMapper.insertPatientRecordEdcStatus(patientRecordEdcStatus);
            }else{
                pres.setEdcStatus("2");
                patientRecordEdcStatusMapper.updatePatientRecordEdcStatus(pres);
            }
            FMedicalForm fMedicalForm = fMedicalFormMapper.selectFMedicalFormByMedicalFormId(proVo.getMedicalFormId());
            Integer edcStatus = fMedicalFormMapper.selectMinStatusByRecordIdAndMedicalFormId(proVo.getRecordId(),fMedicalForm.getFollowId());
            patientRecordMapper.updateEdcStatus(recordId, String.valueOf(edcStatus));

        }else{
            if(pres==null){
                PatientRecordEdcStatus patientRecordEdcStatus = new PatientRecordEdcStatus();
                patientRecordEdcStatus.setRecordId(proVo.getRecordId());
                patientRecordEdcStatus.setMedicalFormId(proVo.getMedicalFormId());
                patientRecordEdcStatus.setEdcStatus("1");
                patientRecordEdcStatus.setCreateTime(DateUtils.getNowDate());
                patientRecordEdcStatusMapper.insertPatientRecordEdcStatus(patientRecordEdcStatus);
            }
        }
        if(proVo.getEdcQuestionVo()!=null){
            EdcQuestionVo edcQuestionVo = proVo.getEdcQuestionVo();
            if(edcQuestionVo.getQuestionId() != null){
                QuestionRecord qr = questionRecordMapper.selectQuestionRecordByQuestionId(edcQuestionVo.getQuestionId());
                if(qr != null){
                    qr.setReplyUserId(edcQuestionVo.getReplyUserId());
                    qr.setReplyContent(edcQuestionVo.getReplyContent());
                    qr.setReplyTime(DateUtils.getNowDate());
                    questionRecordMapper.updateQuestionRecord(qr);
                }
            }
        }
    }

    /**
     * edc工作站档案详情左侧树状图
     * @param vo
     * @return
     */
    @Override
    public FollowMedicalFromTree followMedicalFromTree(FMedicalInfoReq vo) {
        //随访节点信息
        PatientRecordTemplate template = patientRecordTemplateMapper.selectPatientRecordTemplateByFollowId(vo.getFollowId());
        //随访节点模板基础信息
        Optional<List<FMedicalForm>> fMedicalForms = Optional.ofNullable(fMedicalFormMapper.selectFMedicalFormByFollowId(vo.getFollowId()));
        FollowMedicalFromTree fromTree = new FollowMedicalFromTree(template,fMedicalForms,vo.getPatientId());
        if(vo.getRecordId()!=null){
            PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByRecordId(vo.getRecordId());
            if(patientRecord!=null){
                fromTree.setNodeName(patientRecord.getName());
            }
        }
        return fromTree;
    }

    /**
     * 查询档案详情
     * @param vo
     * @return
     */
    @Override
    public FromDetailInfoVo edcPatientRecordDetailInfo(FMedicalInfoReq vo) {
        FromDetailInfoVo mdInfoVo = new FromDetailInfoVo();
        //患者信息
        PatientInfo patientInfo = patientInfoMapper.selectPatientDetailInfoByPatientId(vo.getPatientId());
        if(patientInfo!=null && patientInfo.getIdCard()!=null){
            if(patientInfo.getIdCard().length()==18){
                patientInfo.setSex(Integer.parseInt(CommunalUtils.getSex(patientInfo.getIdCard())));
            }
        }

        mdInfoVo.setPatientInfo(patientInfo);
        //App模板信息
        FMedicalForm appPatientRecordInfo = fMedicalFormService.getAppPatientRecordDetailInfo(vo);
        mdInfoVo.setAppMedicalInfo(appPatientRecordInfo);
        //后台模板信息
        FMedicalForm fMedicalFormInfo = fMedicalFormService.getFMedicalFormDetailInfo(vo);

        PatientRecord pr = patientRecordMapper.selectPatientRecordByRecordId(vo.getRecordId());
        PatientRecordEdcStatus pres = patientRecordEdcStatusMapper.selectByTwoId(vo.getRecordId(),vo.getMedicalFormId());
        if("3".equals(pr.getEdcStatus())){
            if("3".equals(pres.getEdcStatus())){
                mdInfoVo.setShowSaveSubmit(true);
                mdInfoVo.setShowReplay(false);
                mdInfoVo.setShowQueryLock(false);
            }else{
                mdInfoVo.setShowSaveSubmit(false);
                mdInfoVo.setShowReplay(false);
                mdInfoVo.setShowQueryLock(false);
            }
        }else if("4".equals(pr.getEdcStatus())){
            mdInfoVo.setShowSaveSubmit(false);
            mdInfoVo.setShowReplay(false);
            mdInfoVo.setShowQueryLock(false);
        }else{
            //开始查询按钮是否显示
            if(pres==null || "1".equals(pres.getEdcStatus())){
                mdInfoVo.setShowSaveSubmit(true);
                mdInfoVo.setShowReplay(false);
                mdInfoVo.setShowQueryLock(false);
            }else{
                if("2".equals(pres.getEdcStatus())){
                    mdInfoVo.setShowSaveSubmit(false);
                    mdInfoVo.setShowReplay(true);
                    mdInfoVo.setShowQueryLock(true);
                }else if("3".equals(false)){
                    mdInfoVo.setShowSaveSubmit(false);
                    mdInfoVo.setShowReplay(false);
                    mdInfoVo.setShowQueryLock(false);
                }else if("4".equals(false)){
                    mdInfoVo.setShowSaveSubmit(false);
                    mdInfoVo.setShowReplay(false);
                    mdInfoVo.setShowQueryLock(false);
                }
            }
        }
        mdInfoVo.setMedicalInfo(fMedicalFormInfo);
        return mdInfoVo;
    }

    @Override
    public int lock(FMedicalInfoReq vo) {
        FMedicalForm fMedicalForm = new FMedicalForm();
        fMedicalForm.setMedicalFormId(vo.getMedicalFormId());
        fMedicalForm.setLockFlag("1");
        return fMedicalFormService.updateFMedicalForm(fMedicalForm);
    }

}
