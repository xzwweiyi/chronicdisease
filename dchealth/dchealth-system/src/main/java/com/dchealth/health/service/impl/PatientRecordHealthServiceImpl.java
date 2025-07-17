package com.dchealth.health.service.impl;

import com.dchealth.cms.domain.CmsArticle;
import com.dchealth.cms.mapper.CmsArticleMapper;
import com.dchealth.common.DynamicSql;
import com.dchealth.common.utils.CommunalUtils;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.dynamic.mapper.DynamicCommonMapper;
import com.dchealth.dynamic.templete.DynamicFroms;
import com.dchealth.dynamic.templete.DynamicLargeText;
import com.dchealth.dynamic.templete.DynamicText;
import com.dchealth.health.domain.PatientRecordHealth;
import com.dchealth.health.mapper.PatientRecordHealthMapper;
import com.dchealth.health.service.IPatientRecordHealthService;
import com.dchealth.health.vo.req.RecordHealthReq;
import com.dchealth.health.vo.req.RecordHealthTree;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.PatientRecordFiles;
import com.dchealth.medical.domain.ResearchPatient;
import com.dchealth.medical.mapper.PatientInfoMapper;
import com.dchealth.medical.mapper.PatientRecordFilesMapper;
import com.dchealth.medical.mapper.PatientRecordMapper;
import com.dchealth.medical.mapper.ResearchPatientMapper;
import com.dchealth.medical.service.common.CommonService;
import com.dchealth.research.domain.constant.PropertyType;
import com.dchealth.research.domain.vo.ResearchDiseasesVO;
import com.dchealth.research.domain.vo.ResearchInfoVo;
import com.dchealth.research.mapper.ResearchDiseasesMapper;
import com.dchealth.template.domain.FMedicalForm;
import com.dchealth.template.domain.FMedicalProperty;
import com.dchealth.template.domain.vo.FMedicalValueVo;
import com.dchealth.template.domain.vo.MedicalDetailInfoVo;
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
import java.util.Set;
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

    @Resource
    private DynamicSql dynamicSql;

    @Resource
    private DynamicCommonMapper dynamicCommonMapper;

    @Resource
    private DynamicLargeText dynamicLargeText;

    @Resource
    private DynamicText dynamicText;

    @Resource
    private DynamicFroms dynamicFroms;


    /**
     * 查询患者档案健康管理
     *
     * @param recordHealthReq 患者档案健康管理主键
     * @return 患者档案健康管理
     */
    @Override
    public MedicalDetailInfoVo selectPatientRecordHealthByRecordId(RecordHealthReq recordHealthReq) {
        MedicalDetailInfoVo result = new MedicalDetailInfoVo();
        Long patientId = recordHealthReq.getPatientId();
        PatientRecordHealth patientRecordHealth = patientRecordHealthMapper.selectPatientRecordHealthByHealthId(recordHealthReq.getHealthId());

        List<FMedicalForm> fMedicalFormInfo = getFMedicalFormInfo(recordHealthReq, patientRecordHealth);
        if (fMedicalFormInfo != null && !fMedicalFormInfo.isEmpty()) {
            result.setMedicalInfos(fMedicalFormInfo);
            //查询患者基本信息
            PatientInfo patientInfo = patientInfoMapper.selectPatientDetailInfoByPatientId(patientId);
            if (patientInfo != null && patientInfo.getIdCard() != null) {
                if (patientInfo.getIdCard().length() == 18) {
                    patientInfo.setSex(Integer.parseInt(CommunalUtils.getSex(patientInfo.getIdCard())));
                }
            }

            //此处新增随访提交时间 应唐都老师要求
            if (patientRecordHealth != null && patientRecordHealth.getUpdateTime() != null) {
                patientInfo.setCommitTime(patientRecordHealth.getSubmitTime());
            }


            result.setPatientInfo(patientInfo);
        }
        return result;
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

            // 健康管理列表获取
            PatientRecordHealth patientRecordHealth = new PatientRecordHealth();
            patientRecordHealth.setPatientId(recordHealthReq.getPatientId());
            List<PatientRecordHealth> patientRecordHealths = patientRecordHealthMapper.selectPatientRecordHealthList(patientRecordHealth);


            //树状图处理
            if (researchDiseases != null && !researchDiseases.isEmpty()) {
                Map<Long, List<ResearchDiseasesVO>> map = researchDiseases.stream().collect(Collectors.groupingBy(ResearchDiseasesVO::getDeptId));
                List<FMedicalForm> finalFMedicalForms = fMedicalForms;
                map.forEach((k, v) -> result.add(new RecordHealthTree(v, patientRecords, cmsArticles, recordHealthReq.getPatientId(), finalFMedicalForms, patientRecordHealths)));
            }
        }

        return result;
    }


    public List<FMedicalForm> getFMedicalFormInfo(RecordHealthReq vo, PatientRecordHealth patientRecordHealth) {
        List<FMedicalForm> fMedicalForms = null;
        List<FMedicalProperty> fMedicalProperties = null;
        Long followId = vo.getFollowId();
        //获取随访模块信息
        if (followId != null) {
            FMedicalForm fMedicalForm = new FMedicalForm();
            fMedicalForm.setHealthManageType(patientRecordHealth.getHealthManageType());
            fMedicalForm.setFollowId(patientRecordHealth.getFollowId());
            fMedicalForm.setFormType("5");
            fMedicalForms = fMedicalFormMapper.selectFMedicalFormList(fMedicalForm);
            FMedicalForm ff = new FMedicalForm();
            if (fMedicalForms != null && !fMedicalForms.isEmpty()) {
                ff.setParentId(fMedicalForms.get(0).getMedicalFormId());
                List<FMedicalForm> childForm = fMedicalFormMapper.selectFMedicalFormList(ff);
                if (!childForm.isEmpty()) {
                    fMedicalForms.addAll(childForm);
                }
            }


            //查询随访模块属相信息
//            fMedicalProperties = fMedicalPropertyMapper.selectFMedicalProperty(followId,vo.getMedicalFormId());
//            List<FMedicalExamSubject> fMedicalExamSubjects = fMedicalExamSubjectMapper.selectFMedicalExamSubjectByFromId(vo.getMedicalFormId());

            fMedicalProperties = fMedicalPropertyMapper.selectFMedicalPropertyByFollowId(followId);
//            if (!CollectionUtils.isEmpty(fMedicalExamSubjects)) {
//
//                List<FMedicalProperty> fMedicalPropertyList = fMedicalExamSubjects.stream().map(x-> {
//                    FMedicalProperty fMedicalProperty = new FMedicalProperty();
//                    BeanUtils.copyProperties(x,fMedicalProperty);
//                    return fMedicalProperty;
//                }).collect(Collectors.toList());
//
//                if (CollectionUtils.isEmpty(fMedicalProperties)) {
//                    fMedicalProperties = new ArrayList<>();
//                }
//                fMedicalProperties.addAll(fMedicalPropertyList);
//            }

        }
        if (fMedicalForms != null && !fMedicalForms.isEmpty()) {
            //判断表单id是否存在属性分组表中，存在则将属性信息赋值表单
            for (FMedicalForm fMedicalForm : fMedicalForms) {
                //属性值赋值
                packageMedicalProperties(fMedicalProperties, fMedicalForm, vo, "1");
            }
            //构建树状结构-子父关系
            fMedicalForms = rebuildTreeData(0l, fMedicalForms);
        }
        return fMedicalForms;
    }


    public void packageMedicalProperties(List<FMedicalProperty> fMedicalProperties, FMedicalForm fMedicalForm, RecordHealthReq vo, String type) {
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
                        sql = dynamicLargeText.buildSelectSql(researchId, fMedicalForm.getMedicalFormId(), vo.getHealthId(), type);
                        List<FMedicalValueVo> valueVos = fMedicalPropertyMapper.selectFMedicalValues(sql);
                        fMedicalValueVos.addAll(valueVos);
                    }
                    if (PropertyType.text(propertyTypes)) {
                        //小文本
                        sql = dynamicText.buildSelectSql(researchId, fMedicalForm.getMedicalFormId(), vo.getHealthId(), type);
                        List<FMedicalValueVo> valueVos = fMedicalPropertyMapper.selectFMedicalValues(sql);
                        fMedicalValueVos.addAll(valueVos);
                    }
                    if (true) {//"1".equals(fMedicalForm.getDynamic())
                        //动态表单
                        sql = dynamicFroms.buildSelectSql(researchId, fMedicalForm.getMedicalFormId(), vo.getHealthId(), type);
                        List<FMedicalValueVo> valueVos = fMedicalPropertyMapper.selectFMedicalValues(sql);
                        fMedicalValueVos.addAll(valueVos);
                    }
                    if (PropertyType.upLoad(propertyTypes)) {
                        //图片
                        List<PatientRecordFiles> recordFiles = patientRecordFilesMapper.selectPatientRecordFilesByHealthId(vo.getHealthId());
                        patientRecordFiles.addAll(recordFiles);
                    }
                }
                Map<String, FMedicalValueVo> valuesMap = null;
                Map<Long, List<PatientRecordFiles>> fileMap = null;
                //将值赋值属性表
                if (fMedicalValueVos != null && !fMedicalValueVos.isEmpty()) {
                    valuesMap = fMedicalValueVos.stream().collect(Collectors.toMap(FMedicalValueVo::getPropertyCode, item -> item));
                }
                //图片赋值
                if (patientRecordFiles != null && !patientRecordFiles.isEmpty()) {
                    fileMap = patientRecordFiles.stream().collect(Collectors.groupingBy(PatientRecordFiles::getPropertyId));
                }
                for (FMedicalProperty fMedicalProperty : medicalProperties) {
                    if (valuesMap != null && valuesMap.get(fMedicalProperty.getPropertyCode()) != null) {
                        FMedicalValueVo fMedicalValueVo = valuesMap.get(fMedicalProperty.getPropertyCode());
                        fMedicalProperty.setPropertyValue(fMedicalValueVo.getPropertyValue());
                        fMedicalProperty.setOther(fMedicalValueVo.getOther());
                    }
                    if (fileMap != null && fileMap.get(fMedicalProperty.getId()) != null) {
                        List<PatientRecordFiles> files = fileMap.get(fMedicalProperty.getId());
                        List<String> list = files.stream().map(PatientRecordFiles::getImageUrl).collect(Collectors.toList());
                        fMedicalProperty.setImageUrls(list);
                    }
                }
            }
            fMedicalForm.setMedicalProperties(medicalProperties);
        }
    }

    private List<FMedicalForm> rebuildTreeData(long id, List<FMedicalForm> fMedicalForms) {
        List<FMedicalForm> result = new ArrayList<>();
        fMedicalForms.forEach(x -> {
            if (x.getParentId() != null && id == x.getParentId()) {
                result.add(x);
            }
        });
        for (FMedicalForm fMedicalForm : result) {
            List<FMedicalForm> childs = rebuildTreeData(fMedicalForm.getMedicalFormId(), fMedicalForms);
            if (childs != null && !childs.isEmpty()) {
                fMedicalForm.setMedicalFormList(childs);
            }
        }
        return result;
    }

}
