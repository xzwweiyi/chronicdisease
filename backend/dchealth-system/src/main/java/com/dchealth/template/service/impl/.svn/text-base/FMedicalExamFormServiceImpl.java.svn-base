package com.dchealth.template.service.impl;

import static com.dchealth.common.utils.DateUtils.YYYY_MM_DD_HH_MM_SS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dchealth.cms.domain.CmsArticle;
import com.dchealth.cms.mapper.CmsArticleMapper;
import com.dchealth.cms.service.impl.CmsArticleServiceImpl;
import com.dchealth.common.utils.CommunalUtils;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.dynamic.templete.DynamicArticle;
import com.dchealth.medical.domain.MpPerson;
import com.dchealth.medical.domain.PatientExamRecord;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.PatientRecordFiles;
import com.dchealth.medical.mapper.MpPersonMapper;
import com.dchealth.medical.mapper.PatientExamRecordMapper;
import com.dchealth.medical.mapper.PatientInfoMapper;
import com.dchealth.medical.mapper.PatientRecordFilesMapper;
import com.dchealth.research.domain.constant.PropertyType;
import com.dchealth.template.domain.FMedicalExamForm;
import com.dchealth.template.domain.FMedicalExamItem;
import com.dchealth.template.domain.FMedicalExamSubject;
import com.dchealth.template.domain.ResArticle;
import com.dchealth.template.domain.vo.FMedicalExamFormVO;
import com.dchealth.template.domain.vo.FMedicalInfoReq;
import com.dchealth.template.domain.vo.FMedicalValueVo;
import com.dchealth.template.mapper.FMedicalExamFormMapper;
import com.dchealth.template.mapper.FMedicalExamItemMapper;
import com.dchealth.template.mapper.FMedicalExamSubjectMapper;
import com.dchealth.template.mapper.FMedicalPropertyMapper;
import com.dchealth.template.service.IFMedicalExamFormService;

/**
 * 单模板-问卷Service业务层处理
 *
 * @author lchbu
 * @date 2022-11-04
 */
@Service
public class FMedicalExamFormServiceImpl implements IFMedicalExamFormService {

    private static final String EXCEL_HEADER_FORMID = "formId";
    private static final String EXCEL_HEADER_PATIENT = "patientId";
    private static final String EXCEL_HEADER_PATIENTNAME = "patientName";
    private static final String EXCEL_HEADER_RECORDID = "recordId";
    private static final String EXCEL_HEADER_SUBMITTIME = "submitTime";
    @Autowired
    private FMedicalExamFormMapper fMedicalExamFormMapper;
    @Autowired
    private FMedicalExamSubjectMapper fMedicalExamSubjectMapper;
    @Autowired
    private FMedicalExamItemMapper fMedicalExamItemMapper;
    @Autowired
    private CmsArticleMapper cmsArticleMapper;

    @Autowired
    private PatientExamRecordMapper patientExamRecordMapper;

    @Autowired
    private MpPersonMapper mpPersonMapper;

    @Autowired
    private CmsArticleServiceImpl cmsArticleService;

    @Autowired
    private DynamicArticle dynamicArticle;

    @Autowired
    private FMedicalPropertyMapper fMedicalPropertyMapper;

    @Autowired
    private PatientRecordFilesMapper patientRecordFilesMapper;

    @Autowired
    private PatientInfoMapper patientInfoMapper;

    /**
     * 查询单模板-问卷
     *
     * @param medicalFormId 单模板-问卷主键
     * @return 单模板-问卷
     */
    @Override
    public FMedicalExamForm selectFMedicalExamFormByMedicalFormId(Long medicalFormId) {
        return fMedicalExamFormMapper.selectFMedicalExamFormByMedicalFormId(medicalFormId);
    }

    /**
     * 查询单模板-问卷列表
     *
     * @param fMedicalExamForm 单模板-问卷
     * @return 单模板-问卷
     */
    @Override
    public List<FMedicalExamForm> selectFMedicalExamFormList(FMedicalExamForm fMedicalExamForm) {
        return fMedicalExamFormMapper.selectFMedicalExamFormList(fMedicalExamForm);
    }

    /**
     * 新增单模板-问卷
     *
     * @param fMedicalExamForm 单模板-问卷
     * @return 结果
     */
    @Transactional
    @Override
    public FMedicalExamForm insertFMedicalExamForm(FMedicalExamForm fMedicalExamForm) {
        CmsArticle cmsArticle = new CmsArticle();
        cmsArticle.setCategoryId(fMedicalExamForm.getCategoryId());
        cmsArticle.setTitle(fMedicalExamForm.getMedicalName());
        cmsArticle.setCreateTime(DateUtils.getNowDate());
        cmsArticle.setUpdateTime(DateUtils.getNowDate());
        cmsArticle.setType("2");
        cmsArticle.setArticleStatus("0");
        cmsArticle.setPositionType("2");
        cmsArticle.setImage(fMedicalExamForm.getImageUrl());
        cmsArticle.setDescription(fMedicalExamForm.getDescription());

        //查询问卷最大排序值
        Integer quesNum = cmsArticleMapper.selectCmsArticleMaxQuesNum();
        cmsArticle.setQuesNum(quesNum != null ? (quesNum + 1) : 1);
        //初始化cmsArticle表数据
        if(StringUtils.isNotBlankAndNotEmpty(fMedicalExamForm.getDeptIds())){
            cmsArticle.setDeptIds(fMedicalExamForm.getDeptIds());
        }
        cmsArticle.setDeptId(fMedicalExamForm.getDeptId());
        cmsArticle.setCreateId(fMedicalExamForm.getCreateId());
        cmsArticleMapper.insertCmsArticle(cmsArticle);
        fMedicalExamForm.setArticleId(cmsArticle.getId());
        fMedicalExamForm.setCreateTime(DateUtils.getNowDate());
        fMedicalExamForm.setFormSource("2");
        fMedicalExamFormMapper.insertFMedicalExamForm(fMedicalExamForm);
        //问卷发布
        cmsArticleService.updateCmsArticleStatusById(cmsArticle.getId());
        return fMedicalExamForm;
    }

    /**
     * 修改单模板-问卷
     *
     * @param fMedicalExamForm 单模板-问卷
     * @return 结果
     */
    @Transactional
    @Override
    public int updateFMedicalExamForm(FMedicalExamForm fMedicalExamForm) {
        CmsArticle cmsArticle = new CmsArticle();
        cmsArticle.setId(fMedicalExamForm.getArticleId());
        cmsArticle.setCategoryId(fMedicalExamForm.getCategoryId());
        cmsArticle.setTitle(fMedicalExamForm.getMedicalName());
        cmsArticle.setUpdateTime(DateUtils.getNowDate());

        cmsArticle.setImage(fMedicalExamForm.getImageUrl());
        cmsArticle.setDescription(fMedicalExamForm.getDescription());
        //更新问卷名称
        cmsArticleMapper.updateCmsArticle(cmsArticle);
        fMedicalExamForm.setUpdateTime(DateUtils.getNowDate());
        return fMedicalExamFormMapper.updateFMedicalExamFormByArticleId(fMedicalExamForm);
    }

    /**
     * 批量删除单模板-问卷
     *
     * @param medicalFormIds 需要删除的单模板-问卷主键
     * @return 结果
     */
    @Override
    public int deleteFMedicalExamFormByMedicalFormIds(Long[] medicalFormIds) {
        return fMedicalExamFormMapper.deleteFMedicalExamFormByMedicalFormIds(medicalFormIds);
    }

    /**
     * 删除单模板-问卷信息
     *
     * @param medicalFormId 单模板-问卷主键
     * @return 结果
     */
    @Override
    public int deleteFMedicalExamFormByMedicalFormId(Long medicalFormId) {
        return fMedicalExamFormMapper.deleteFMedicalExamFormByMedicalFormId(medicalFormId);
    }

    /**
     * 获取问卷模板信息
     *
     * @param fMedicalInfoReq
     * @return
     */
    @Override
    public FMedicalExamForm getFMedicalFormExamInfo(FMedicalInfoReq fMedicalInfoReq) {
        //获取模板信息
        FMedicalExamForm fMedicalExamForm = fMedicalExamFormMapper.selectFMedicalExamFormByArticleId(fMedicalInfoReq.getArticleId());
        if (fMedicalExamForm != null) {
            //获取题目信息
            List<FMedicalExamSubject> subjects = fMedicalExamSubjectMapper.selectFMedicalExamSubjectByFromId(fMedicalExamForm.getMedicalFormId());
            if (subjects != null && !subjects.isEmpty()) {
                subjects.forEach(subject -> {
                    //获取选项信息
                    List<FMedicalExamItem> items = fMedicalExamItemMapper.selectFMedicalExamItemByProId(subject.getId());
                    subject.setItemList(items);
                });
            }
            fMedicalExamForm.setSubjectList(subjects);
        }
        return fMedicalExamForm;
    }

    @Override
    public List<Map<String, String>> exportForm(FMedicalExamForm fMedicalExamForm) {
        // 1.查询问卷下的题目
        List<FMedicalExamSubject> fMedicalExamSubjects = fMedicalExamSubjectMapper.selectFMedicalExamSubjectByFromId(fMedicalExamForm.getMedicalFormId());
        // 2.查询问卷对应的答案
        PatientExamRecord patientExamRecord = new PatientExamRecord();
        patientExamRecord.setMedicalFormId(fMedicalExamForm.getMedicalFormId());
        List<PatientExamRecord> patientExamRecords = patientExamRecordMapper.selectPatientExamRecordList(patientExamRecord);

        FMedicalExamSubject subject = new FMedicalExamSubject();
        if (CollectionUtils.size(patientExamRecords) > 0) {
            subject.setTableName(patientExamRecords.get(0).getExamTable());
        } else {
            return null;
        }
        // 3.查询全部答案
        List<ResArticle> resArticles = fMedicalExamSubjectMapper.selectFMedicalExamSubjectResArticle(subject);
        List<Map<String, String>> res = new ArrayList<>();
        res.add(buildExcelHeader(fMedicalExamSubjects));
        // 4.查询所有有患者信息 这块逻辑是否需要调整，患者库患者非常多
    //    List<PatientBaseInfo> patientBaseInfos = patientBaseInfoMapper.selectBasePatientInfoList(null); 
        MpPerson  mpPersonSearch = new MpPerson();
        List<MpPerson> patientBaseInfos =  mpPersonMapper.selectMpPersonList(mpPersonSearch);
        // 答案全部查询
        Map<Long, List<ResArticle>> resArticleMap = resArticles.stream().collect(Collectors.groupingBy(ResArticle::getExamRecordId));
        for (long recordId : resArticleMap.keySet()) {
            Map<String, String> map = new HashMap<>();
            map.put(EXCEL_HEADER_FORMID, String.valueOf(fMedicalExamForm.getMedicalFormId()));
            map.put(EXCEL_HEADER_RECORDID, String.valueOf(recordId));

            List<ResArticle> resArticles1 = resArticleMap.get(recordId);
            String patientName = "";
            String submitTime = "";
            if (CollectionUtils.size(resArticles1) > 0) {
                Long patientId = resArticles1.get(0).getPatientId();
                
                if (patientId != null) {
                    map.put(EXCEL_HEADER_PATIENT, String.valueOf(patientId));
                    PatientInfo  patientInfo =   patientInfoMapper.selectPatientInfoByPatientId(patientId);
                    Optional<MpPerson> first = patientBaseInfos.stream().filter(base -> base.getPersonId().equals(patientInfo.getPersonId())).findFirst();
                    if (first.isPresent()) {
                        patientName = first.get().getPersonName();
                    }
                }
                if (Objects.nonNull(resArticles1.get(0))) {
                    submitTime = DateUtils.getTimeStr(resArticles1.get(0).getCreateTime(), YYYY_MM_DD_HH_MM_SS);
                }

            }
            map.put(EXCEL_HEADER_PATIENTNAME, patientName);
            map.put(EXCEL_HEADER_SUBMITTIME, submitTime);
            fMedicalExamSubjects.stream().forEach(su -> {
                resArticles1.stream().filter(resArticle -> StringUtils.equals(resArticle.getPropertyCode(), su.getPropertyCode())).forEach(resArticle -> {
                    map.put(resArticle.getPropertyCode(), resArticle.getPropertyValue());
                });
            });
            res.add(map);
        }
        return res;
    }

    /**
     * 问卷填报信息
     *
     * @param fMedicalInfoReq
     * @return
     */
    @Override
    public FMedicalExamForm getFMedicalFormExamFillInfo(FMedicalInfoReq fMedicalInfoReq) {
        Long articleId = fMedicalInfoReq.getArticleId();
        Long examRecordId = fMedicalInfoReq.getExamRecordId();
        //获取模板信息
        FMedicalExamForm fMedicalExamForm = fMedicalExamFormMapper.selectFMedicalExamFormByArticleId(articleId);
        if (fMedicalExamForm != null) {
            //获取题目信息
            List<FMedicalExamSubject> subjects = fMedicalExamSubjectMapper.selectFMedicalExamSubjectByFromId(fMedicalExamForm.getMedicalFormId());
            if (subjects != null && !subjects.isEmpty()) {
                subjects.forEach(subject -> {
                    //获取选项信息
                    List<FMedicalExamItem> items = fMedicalExamItemMapper.selectFMedicalExamItemByProId(subject.getId());
                    subject.setItemList(items);
                    //获取选项值信息
                    if (PropertyType.UPLOAD.toLowerCase().equals(subject.getPropertyType().toLowerCase())) {
                        //题目类型是图片或者文件类型
                        List<PatientRecordFiles> recordFiles = patientRecordFilesMapper.selectPatientRecordFilesByProId(subject.getId());
                        List<String> imageUrls = recordFiles.stream().map(PatientRecordFiles::getImageUrl).collect(Collectors.toList());
                        subject.setImageUrls(imageUrls);
                    } else {
                        //问卷表后缀名
                        CmsArticle cmsArticle = cmsArticleMapper.selectCmsArticleById(articleId);
                        int i = Math.floorDiv(cmsArticle.getQuesNum(), 10);
                        String sql = dynamicArticle.buildSelectSql(articleId, examRecordId, i);
                        List<FMedicalValueVo> valueVos = fMedicalPropertyMapper.selectFMedicalValues(sql);
                        if (valueVos != null && !valueVos.isEmpty()) {
                            Map<Long, FMedicalValueVo> valuesMap = valueVos.stream().collect(Collectors.toMap(FMedicalValueVo::getPropertyId, item -> item));
                            if (valuesMap.get(subject.getId()) != null) {
                                FMedicalValueVo fMedicalValueVo = valuesMap.get(subject.getId());
                                subject.setPropertyValue(fMedicalValueVo.getPropertyValue());
                                subject.setOther(fMedicalValueVo.getOther());
                            }
                        }
                    }
                });
            }
//            List<FMedicalExamSubject> examSubjects = recurrenceTreeData(0l, subjects);
            fMedicalExamForm.setSubjectList(subjects);
        }
        return fMedicalExamForm;
    }

    private List<FMedicalExamSubject> recurrenceTreeData(long id, List<FMedicalExamSubject> examSubjects) {
        if (examSubjects == null || examSubjects.isEmpty()) {
            return null;
        }
        List<FMedicalExamSubject> result = new ArrayList<>();
        examSubjects.forEach(x -> {
            if (id == x.getParentId()) {
                result.add(x);
            }
        });
        for (FMedicalExamSubject examSubject : result) {
            List<FMedicalExamSubject> fExamSubjects = recurrenceTreeData(examSubject.getId(), examSubjects);
            if (fExamSubjects != null && !fExamSubjects.isEmpty()) {
                examSubject.setExamSubjects(fExamSubjects);
            }
        }
        return result;
    }

    /**
     * 构建excel表头
     *
     * @param fMedicalExamSubjects
     * @return
     */
    private Map<String, String> buildExcelHeader(List<FMedicalExamSubject> fMedicalExamSubjects) {
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        // key 为编码,value 为表头展示字段
        header.put(EXCEL_HEADER_FORMID, "formId");
        header.put(EXCEL_HEADER_PATIENT, "患者Id");
        header.put(EXCEL_HEADER_PATIENTNAME, "患者姓名");
        header.put(EXCEL_HEADER_RECORDID, "recordId");
        header.put(EXCEL_HEADER_SUBMITTIME, "提交时间");
        fMedicalExamSubjects.stream().forEach(sub -> header.put(sub.getPropertyCode(), sub.getPropertyName()));
        return header;
    }

    @Override
    public FMedicalExamFormVO queryMedicalExamForm(FMedicalInfoReq fMedicalInfoReq) {

        FMedicalExamFormVO vo = new FMedicalExamFormVO();
        Long articleId = fMedicalInfoReq.getArticleId();
        //获取模板信息
        FMedicalExamForm fMedicalExamForm = fMedicalExamFormMapper.selectFMedicalExamFormByArticleId(articleId);
        if (fMedicalExamForm != null) {
            //获取题目信息
            List<FMedicalExamSubject> subjects = fMedicalExamSubjectMapper.selectFMedicalExamSubjectByFromId(fMedicalExamForm.getMedicalFormId());
            if (subjects != null && !subjects.isEmpty()) {
                subjects.forEach(subject -> {
                    //获取选项信息
                    List<FMedicalExamItem> items = fMedicalExamItemMapper.selectFMedicalExamItemByProId(subject.getId());
                    subject.setItemList(items);
                    //获取选项值信息
                    if (PropertyType.UPLOAD.toLowerCase().equals(subject.getPropertyType().toLowerCase())) {
                        //题目类型是图片或者文件类型
                        List<PatientRecordFiles> recordFiles = patientRecordFilesMapper.selectPatientRecordFilesByProId(subject.getId());
                        List<String> imageUrls = recordFiles.stream().map(PatientRecordFiles::getImageUrl).collect(Collectors.toList());
                        subject.setImageUrls(imageUrls);
                    } else {
                        //问卷表后缀名
                        CmsArticle cmsArticle = cmsArticleMapper.selectCmsArticleById(articleId);
                        int i = Math.floorDiv(cmsArticle.getQuesNum(), 10);
                        String sql = dynamicArticle.buildSelectSql(articleId, null, i);
                        List<FMedicalValueVo> valueVos = fMedicalPropertyMapper.selectFMedicalValues(sql);
                        if (valueVos != null && !valueVos.isEmpty()) {
                            Map<Long, FMedicalValueVo> valuesMap = valueVos.stream().collect(Collectors.toMap(FMedicalValueVo::getPropertyId, item -> item));
                            if (valuesMap.get(subject.getId()) != null) {
                                FMedicalValueVo fMedicalValueVo = valuesMap.get(subject.getId());
                                subject.setPropertyValue(fMedicalValueVo.getPropertyValue());
                                subject.setOther(fMedicalValueVo.getOther());
                            }
                        }
                    }
                });
            }
            fMedicalExamForm.setSubjectList(subjects);
        }
        vo.setFMedicalExamForm(fMedicalExamForm);

        //查询患者基础信息
        PatientInfo patientInfo = patientInfoMapper.selectPatientDetailInfoByPatientId(fMedicalInfoReq.getPatientId());
        if(patientInfo!=null && patientInfo.getIdCard()!=null){
            if(patientInfo.getIdCard().length()==18){
                patientInfo.setSex(Integer.parseInt(CommunalUtils.getSex(patientInfo.getIdCard())));
            }
        }
        vo.setPatientInfo(patientInfo);
        return vo;
    }
}
