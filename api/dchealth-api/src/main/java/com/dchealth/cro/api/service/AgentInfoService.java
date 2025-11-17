package com.dchealth.cro.api.service;


import cn.hutool.core.util.ObjectUtil;
import com.dchealth.cro.api.vo.BaseInfoVo;
import com.dchealth.cro.api.vo.ResearchInfoVo;
import com.dchealth.cro.common.utils.bean.BeanUtils;
import com.dchealth.cro.core.domain.SysDept;
import com.dchealth.cro.core.mapper.SysDeptMapper;
import com.dchealth.cro.form.common.DynamicSql;
import com.dchealth.cro.form.constant.PropertyType;
import com.dchealth.cro.form.domain.DynamicCommonProperty;
import com.dchealth.cro.form.domain.form.DynamicForm;
import com.dchealth.cro.form.mapper.DynamicCommonMapper;
import com.dchealth.cro.form.mapper.FMedicalFormMapper;
import com.dchealth.cro.form.mapper.FMedicalPropertyMapper;
import com.dchealth.cro.form.service.MedicalFormService;
import com.dchealth.cro.form.vo.MedicalFormVO;
import com.dchealth.cro.form.vo.MedicalPropertyVO;
import com.dchealth.cro.medical.domain.PatientInfo;
import com.dchealth.cro.medical.domain.PatientRecord;
import com.dchealth.cro.medical.domain.PatientRecordFiles;
import com.dchealth.cro.medical.mapper.PatientInfoMapper;
import com.dchealth.cro.medical.mapper.PatientRecordFilesMapper;
import com.dchealth.cro.medical.mapper.PatientRecordMapper;
import com.dchealth.cro.research.mapper.ResearchInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/10/28 14:29
 * @Version: 1.0
 */
@Service
public class AgentInfoService {

    @Resource
    private PatientInfoMapper patientInfoMapper;

    @Resource
    private ResearchInfoMapper researchInfoMapper;

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private PatientRecordMapper patientRecordMapper;

    @Resource
    private FMedicalFormMapper fMedicalFormMapper;

    @Resource
    private MedicalFormService medicalFormService;

    @Resource
    private DynamicSql dynamicSql;

    @Resource
    private DynamicCommonMapper dynamicCommonMapper;

    @Autowired
    private Executor executor;

    @Resource
    private PatientRecordFilesMapper patientRecordFilesMapper;

    @Resource
    private FMedicalPropertyMapper fMedicalPropertyMapper;

    public BaseInfoVo  getBaseInfoBases(String personId) {

        List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoByPersonId(personId);

        if (ObjectUtil.isEmpty(patientInfos)) {
            return new BaseInfoVo();
        }
        List<SysDept> sysDepts = sysDeptMapper.selectSysDeptList(new SysDept());
        Map<Long, String> deptMap = sysDepts.stream().collect(Collectors.toMap(SysDept::getDeptId, SysDept::getDeptName));

        BaseInfoVo baseInfoVo = new BaseInfoVo();

        List<ResearchInfoVo> researchInfoVos = new ArrayList<>();

//        patientInfos.forEach(patientInfo -> {
//            ResearchInfoVo researchInfoVo = new ResearchInfoVo();
//            ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(patientInfo.getResearchId());
//
//
//            if (ObjectUtil.isNotNull(researchInfo)) {
//
//                PatientRecord patientRecord = new PatientRecord();
//                patientRecord.setResearchId(researchInfo.getId());
//                patientRecord.setPatientId(patientInfo.getPatientId());
//
//                List<PatientRecord> patientRecords = patientRecordMapper.selectPatientRecordList(patientRecord);
////                if (!ObjectUtil.isEmpty(patientRecords)) {
////                    patientRecords.stream().filter(item -> item.getStageNum() == 1).findFirst().ifPresent(item -> {
////                        researchInfoVo.setFollowId(item.getFollowId().toString());
////                        researchInfoVo.setRecordId(item.getRecordId().toString());
////
////                        org.springframework.beans.BeanUtils.copyProperties(item, patientRecord);
////                    });
////
////                }
//
//                researchInfoVo.setPatientId(String.valueOf(patientInfo.getPatientId()));
//                researchInfoVo.setResearchId(String.valueOf(researchInfo.getId()));
//                researchInfoVo.setResearchName(researchInfo.getProjectName());
//                researchInfoVo.setDeptId(String.valueOf(researchInfo.getDeptId()));
//                researchInfoVo.setDeptName(deptMap.get(researchInfo.getDeptId()));
//
//                // 获取基线信息
////                FMedicalForm fMedicalForm = new FMedicalForm();
////                fMedicalForm.setFollowId(Long.valueOf(researchInfoVo.getFollowId()));
//
////                List<FMedicalForm> fMedicalForms = fMedicalFormMapper.selectFMedicalFormList(fMedicalForm);
////                List<FMedicalProperty> fMedicalProperties = fMedicalPropertyMapper.selectFMedicalPropertyByFollowId(Long.valueOf(researchInfoVo.getFollowId()));
////
////
//
//
//                // 查询动态表单
////                List<MedicalFormVO> datas = medicalFormService.followForms(patientRecord.getFollowId(),
////                        patientRecord.getRecordId(), patientRecord.getFollowStatus(), (formVOS, propertyVOS, propertyTypes) -> {
////                            List<PatientRecordFiles> patientRecordFiles =
////                                    patientRecordFilesMapper.selectPatientRecordFilesByRecordId(Long.valueOf(researchInfoVo.getResearchId()));
////                            CompletableFuture<Void> pcf = getPcf(Long.valueOf(researchInfoVo.getResearchId()), patientRecord, propertyVOS, propertyTypes,
////                                    patientRecordFiles);
////                            CompletableFuture<Void> fcf = getFcf(Long.valueOf(researchInfoVo.getResearchId()), patientRecord, formVOS, propertyVOS, patientRecordFiles);
////                            CompletableFuture.allOf(pcf, fcf).join();
////                        });
//
////                researchInfoVo.setData(datas);
//
//                researchInfoVos.add(researchInfoVo);
//            }
//        });

        baseInfoVo.setPatientName(patientInfos.get(0).getPatientName());
        baseInfoVo.setAccountId(String.valueOf(patientInfos.get(0).getAccountId()));
        baseInfoVo.setPersonId(personId);
        baseInfoVo.setMobile(patientInfos.get(0).getPatientMobile());
        baseInfoVo.setResearchInfoVos(researchInfoVos);

        return baseInfoVo;
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


                                List<String> urlBasedViewResolver = filesMap.get(f.getMedicalFormId());

                                List<String> images = new ArrayList<>();
//                                for (String url : urlBasedViewResolver) {
//                                    if (StrUtil.isNotEmpty(url) && url.contains(oldDomain)) {
//                                        String image = url.replace(oldDomain, newDomain);
//                                        images.add(image);
//                                    }
//                                }

                                if (images.isEmpty()) {
                                    f.setImageUrls(urlBasedViewResolver);
                                } else {
                                    f.setImageUrls(images);
                                }
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

                            List<String> urlBasedViewResolver = filesMap.get(p.getId());

                            List<String> images = new ArrayList<>();
//                            for (String url : urlBasedViewResolver) {
//                                if (StrUtil.isNotEmpty(url) && url.contains(oldDomain)) {
//                                    String image = url.replace(oldDomain, newDomain);
//                                    images.add(image);
//                                }
//                            }
                            if (images.isEmpty()) {
                                p.setImageUrls(urlBasedViewResolver);
                            } else {
                                p.setImageUrls(images);
                            }


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
}
