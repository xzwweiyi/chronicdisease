package com.dchealth.medical.service.impl;

import com.dchealth.common.function.FormConsumer;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.common.utils.bean.BeanUtils;
import com.dchealth.medical.domain.vo.MedicalFormVO;
import com.dchealth.medical.domain.vo.MedicalPropertyVO;
import com.dchealth.medical.domain.vo.WarnConfigVO;
import com.dchealth.template.domain.FMedicalExamItem;
import com.dchealth.template.domain.FMedicalExamSubject;
import com.dchealth.template.domain.FMedicalForm;
import com.dchealth.template.domain.FMedicalProperty;
import com.dchealth.template.mapper.FMedicalExamItemMapper;
import com.dchealth.template.mapper.FMedicalExamSubjectMapper;
import com.dchealth.template.mapper.FMedicalFormMapper;
import com.dchealth.template.mapper.FMedicalPropertyMapper;
import com.dchealth.warn.domain.WarnConfig;
import com.dchealth.warn.mapper.WarnConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MedicalFormServiceImpl {

    @Autowired
    private FMedicalFormMapper fMedicalFormMapper;
    @Autowired
    private FMedicalPropertyMapper fMedicalPropertyMapper;
    @Autowired
    private WarnConfigMapper warnConfigMapper;

    @Autowired
    private FMedicalExamItemMapper fMedicalExamItemMapper;

    @Autowired
    private FMedicalExamSubjectMapper fMedicalExamSubjectMapper;

    /**
     * 获取随访表单内容
     * @param followId
     */
    public List<MedicalFormVO> followForms(Long followId, Long recordId, String followStatus, FormConsumer<List<MedicalFormVO>, List<MedicalPropertyVO>, Set<String>> fun){
        List<FMedicalForm> formList = null;
        if ("2".equals(followStatus)){
            formList = fMedicalFormMapper.selectFMedicalFormJoinByMedicalFollowId(followId, recordId);
        }else{
            formList = fMedicalFormMapper.selectFMedicalFormByMedicalFollowId(followId);
        }
        if (formList == null || formList.isEmpty()){
            return null;
        }
        List<FMedicalProperty> properties = fMedicalPropertyMapper.selectFMedicalPropertyByFollowId(followId);
        List<WarnConfig> warnConfigs = warnConfigMapper.selectWarnConfigByFollowId(followId);
        List<MedicalFormVO> medicalFormVOS = BeanUtils.copyList(formList, MedicalFormVO :: new);
        List<MedicalPropertyVO> medicalPropertyVOS = BeanUtils.copyList(properties, MedicalPropertyVO :: new);
        List<WarnConfigVO> warnConfigVOS = BeanUtils.copyList(warnConfigs, WarnConfigVO::new);
        fun.accept(medicalFormVOS, medicalPropertyVOS, Optional.ofNullable(medicalPropertyVOS)
                .orElseGet(()->new ArrayList<>())
                .stream()
                .filter(x-> StringUtils.isNotBlankAndNotEmpty(x.getPropertyType()))
                .map(MedicalPropertyVO :: getPropertyType)
                .collect(Collectors.toSet()));
        if (medicalFormVOS != null && !medicalFormVOS.isEmpty()){
            Map<Long,List<MedicalPropertyVO>> map = null;
            if (medicalPropertyVOS != null && !medicalPropertyVOS.isEmpty()){
                map = medicalPropertyVOS.stream().collect(Collectors.groupingBy(MedicalPropertyVO :: getMedicalFormId));
            }
            Map<Long, List<WarnConfigVO>> warnConfigMaps = null;
            if (warnConfigVOS != null && !warnConfigVOS.isEmpty()){
                warnConfigMaps = warnConfigVOS.stream().collect(Collectors.groupingBy(WarnConfigVO :: getMedicalFormId));
            }
            for (MedicalFormVO x : medicalFormVOS) {
                if (map != null && map.containsKey(x.getMedicalFormId())){
                    x.setPropertyVOS(map.get(x.getMedicalFormId()));
                }
                if (warnConfigMaps != null && warnConfigMaps.containsKey(x.getMedicalFormId())){
                    x.setWarnConfigVOS(warnConfigMaps.get(x.getMedicalFormId()));
                }
            }
        }

        return recurrenceTreeData(0l,medicalFormVOS);
    }


    /**
     * 数结构
     * @param id
     * @param medicalFormVOS
     * @return
     */
    private List<MedicalFormVO> recurrenceTreeData(long id, List<MedicalFormVO> medicalFormVOS){
        if (medicalFormVOS == null || medicalFormVOS.isEmpty()){
            return null;
        }
        List<MedicalFormVO> result = new ArrayList<>();
        medicalFormVOS.forEach(x->{
            if (x.getParentId() != null){
                if (id == x.getParentId()){
                    result.add(x);
                }
            }

        });
        for (MedicalFormVO fMedicalForm : result) {
            List<MedicalFormVO> childs = recurrenceTreeData(fMedicalForm.getMedicalFormId(), medicalFormVOS);
            if (childs != null && !childs.isEmpty()){
                fMedicalForm.setChildrens(childs);
            }
        }
        return result;
    }

    /**
     * 获取随访表单内容
     * @param followId
     */
    public List<MedicalFormVO>  followForm(Long followId,FormConsumer<List<MedicalFormVO>, List<MedicalPropertyVO>, Set<String>> fun){
        List<FMedicalForm> formList = fMedicalFormMapper.selectFMedicalFormByMedicalFollowId(followId);
        if (formList == null || formList.isEmpty()){
            return null;
        }
        List<FMedicalProperty> properties = packageProperty(followId);
        //预警
//        List<WarnConfig> warnConfigs = warnConfigMapper.selectWarnConfigByFollowId(followId);
        //表单
        List<MedicalFormVO> medicalFormVOS = BeanUtils.copyList(formList, MedicalFormVO :: new);
        List<MedicalPropertyVO> medicalPropertyVOS = BeanUtils.copyList(properties, MedicalPropertyVO :: new);
//        List<WarnConfigVO> warnConfigVOS = BeanUtils.copyList(warnConfigs, WarnConfigVO::new);

        Set<String> collect = Optional.ofNullable(medicalPropertyVOS)
                .orElseGet(() -> new ArrayList<>())
                .stream()
                .filter(x -> StringUtils.isNotBlankAndNotEmpty(x.getPropertyType()))
                .map(MedicalPropertyVO::getPropertyType)
                .collect(Collectors.toSet());

        if(!CollectionUtils.isEmpty(collect)){
            fun.accept(medicalFormVOS, medicalPropertyVOS, collect);
        }
        if (medicalFormVOS != null && !medicalFormVOS.isEmpty()){
            Map<Long,List<MedicalPropertyVO>> map = null;
            if (medicalPropertyVOS != null && !medicalPropertyVOS.isEmpty()){
                map = medicalPropertyVOS.stream().collect(Collectors.groupingBy(MedicalPropertyVO :: getMedicalFormId));
            }
//            Map<Long, List<WarnConfigVO>> warnConfigMaps = null;
//            if (warnConfigVOS != null && !warnConfigVOS.isEmpty()){
//                warnConfigMaps = warnConfigVOS.stream().collect(Collectors.groupingBy(WarnConfigVO :: getMedicalFormId));
//            }
            for (MedicalFormVO x : medicalFormVOS) {
                if (map != null && map.containsKey(x.getMedicalFormId()) && !"1".equals(x.getOpenMultiple())){
                    x.setPropertyVOS(map.get(x.getMedicalFormId()));
                }
//                if (warnConfigMaps != null && warnConfigMaps.containsKey(x.getMedicalFormId())){
//                    x.setWarnConfigVOS(warnConfigMaps.get(x.getMedicalFormId()));
//                }
            }
        }

        return recurrenceTreeData(0l, medicalFormVOS);
    }

    /**
     * 组装property信息,自评量表转化
     * @param followId
     */
    public List<FMedicalProperty> packageProperty(Long followId){
        List<FMedicalProperty> fMedicalProperties = new ArrayList<>();
        //自平量表---题干信息
        List<FMedicalExamSubject> subjectList = fMedicalExamSubjectMapper.selectFMedicalExamSubjectByFollowId(followId);
        if(subjectList != null && !subjectList.isEmpty()){
            for (FMedicalExamSubject subject : subjectList) {
                FMedicalProperty property = new FMedicalProperty();
                org.springframework.beans.BeanUtils.copyProperties(subject, property);
                property.setFollowId(followId);
                //选项信息
                List<FMedicalExamItem> items = fMedicalExamItemMapper.selectFMedicalExamItemByProId(subject.getId());
                property.setItemList(items);
                fMedicalProperties.add(property);
            }
        }
        //其它
        List<FMedicalProperty> properties = fMedicalPropertyMapper.selectFMedicalPropertyByFollowId(followId);
        fMedicalProperties.addAll(properties);
        return fMedicalProperties;
    }
}
