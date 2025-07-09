package com.dchealth.cro.form.service;

import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.common.utils.bean.BeanUtils;
import com.dchealth.cro.enums.FollowTipStatusEnum;
import com.dchealth.cro.form.domain.FMedicalExamSubject;
import com.dchealth.cro.form.domain.FMedicalForm;
import com.dchealth.cro.form.domain.FMedicalProperty;
import com.dchealth.cro.form.function.FormConsumer;
import com.dchealth.cro.form.mapper.FMedicalExamSubjectMapper;
import com.dchealth.cro.form.mapper.FMedicalFormMapper;
import com.dchealth.cro.form.mapper.FMedicalPropertyMapper;
import com.dchealth.cro.form.vo.MedicalFormVO;
import com.dchealth.cro.form.vo.MedicalPropertyVO;
import com.dchealth.cro.medical.domain.PatientRecord;
import com.dchealth.cro.medical.mapper.PatientRecordMapper;
import com.dchealth.cro.warn.domain.WarnConfig;
import com.dchealth.cro.warn.mapper.WarnConfigMapper;
import com.dchealth.cro.warn.vo.WarnConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 随访表单service
 */
@Service
public class MedicalFormService {

    @Autowired
    private FMedicalFormMapper fMedicalFormMapper;
    @Autowired
    private FMedicalPropertyMapper fMedicalPropertyMapper;
    @Autowired
    private WarnConfigMapper warnConfigMapper;
    @Autowired
    private PatientRecordMapper patientRecordMapper;
    @Autowired
    private FMedicalExamSubjectMapper fMedicalExamSubjectMapper;


    /**
     * 获取随访表单内容
     * @param followId
     */
    public List<MedicalFormVO>  followForms(Long followId, Long recordId, String followStatus, FormConsumer<List<MedicalFormVO>, List<MedicalPropertyVO>, Set<String>> fun){
        List<FMedicalForm> formList = fMedicalFormMapper.selectFMedicalFormJoinByMedicalFollowId(followId, recordId);
      /*  if ("2".equals(followStatus)){
            formList = fMedicalFormMapper.selectFMedicalFormJoinByMedicalFollowId(followId, recordId);
        }else{
            formList = fMedicalFormMapper.selectFMedicalFormByMedicalFollowId(followId);
        }*/
        if (formList == null || formList.isEmpty()){
            return null;
        }
        //如果是否开启动态添加属性为空  默认为0未开启
        formList.stream().forEach(x -> {
            if(StringUtils.isBlank(x.getOpenMultiple())) {
                x.setOpenMultiple("0");
            }
        });
//        modifyCheckField(recordId, formList);

        List<FMedicalProperty> properties = fMedicalPropertyMapper.selectFMedicalPropertyByFollowId(followId);
        //患者自评量表的数据查出来，增加到properties中
        List<FMedicalExamSubject> questionnaireList =
                fMedicalExamSubjectMapper.selectFMedicalPropertyByFollowId(followId);
        List<FMedicalProperty> fMedicalProperties = BeanUtils.copyList(questionnaireList,
                FMedicalProperty::new);
        if(fMedicalProperties!=null){
            properties.addAll(fMedicalProperties);
        }

        List<WarnConfig> warnConfigs = warnConfigMapper.selectWarnConfigByFollowId(followId);
        List<MedicalFormVO> medicalFormVOS = BeanUtils.copyList(formList, MedicalFormVO :: new);
        List<MedicalPropertyVO> medicalPropertyVOS = BeanUtils.copyList(properties, MedicalPropertyVO :: new);
        List<WarnConfigVO> warnConfigVOS = BeanUtils.copyList(warnConfigs, WarnConfigVO::new);
        // 属性集合
        Set<String> collect = Optional.ofNullable(medicalPropertyVOS)
                .orElseGet(() -> new ArrayList<>())
                .stream()
                .filter(x -> StringUtils.isNotBlankAndNotEmpty(x.getPropertyType()))
                .map(MedicalPropertyVO::getPropertyType)
                .collect(Collectors.toSet());
        // 动态表单相关
        if(!CollectionUtils.isEmpty(collect)){
            fun.accept(medicalFormVOS, medicalPropertyVOS, collect);
        }
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
//                if (map != null && map.containsKey(x.getMedicalFormId()) && !"1".equals(x.getDynamic())){
                if (map != null && map.containsKey(x.getMedicalFormId()) && !"1".equals(x.getOpenMultiple())){
                    x.setPropertyVOS(map.get(x.getMedicalFormId()));
                }
                if (warnConfigMaps != null && warnConfigMaps.containsKey(x.getMedicalFormId())){
                    x.setWarnConfigVOS(warnConfigMaps.get(x.getMedicalFormId()));
                }
            }
        }

        return recurrenceTreeData(0l,medicalFormVOS);
    }

    private void modifyCheckField(Long recordId, List<FMedicalForm> formList) {
        //     如果医生给出了意见，checked 全部修改为 0
        PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByRecordId(recordId);
        if (!Objects.isNull(patientRecord) &&
                (patientRecord.getFollowTipStatus() == FollowTipStatusEnum.FOLLOW_UP_OPINION_REMINDER.getIndex()
                        || patientRecord.getFollowTipStatus() == FollowTipStatusEnum.NO_STATUS.getIndex())) {
            formList.stream().forEach(fm -> {
                fm.setChecked("0");
            });
        }
    }

    public List<MedicalFormVO>  followHealthForms(Long followId, Long recordId, String followStatus, FormConsumer<List<MedicalFormVO>, List<MedicalPropertyVO>, Set<String>> fun, Integer healthType){
        List<FMedicalForm> formList = fMedicalFormMapper.selectFMedicalHealthFormJoinByMedicalFollowId(followId, healthType);
      /*  if ("2".equals(followStatus)){
            formList = fMedicalFormMapper.selectFMedicalFormJoinByMedicalFollowId(followId, recordId);
        }else{
            formList = fMedicalFormMapper.selectFMedicalFormByMedicalFollowId(followId);
        }*/
        if (formList == null || formList.isEmpty()){
            return null;
        }
        //如果是否开启动态添加属性为空  默认为0未开启
        formList.stream().forEach(x -> {
            if(StringUtils.isBlank(x.getOpenMultiple())) {
                x.setOpenMultiple("0");
            }
        });
//        modifyCheckField(recordId, formList);

        List<FMedicalProperty> properties = fMedicalPropertyMapper.selectFMedicalPropertyByFollowId(followId);
        //患者自评量表的数据查出来，增加到properties中
        List<FMedicalExamSubject> questionnaireList =
                fMedicalExamSubjectMapper.selectFMedicalPropertyByFollowId(followId);
        List<FMedicalProperty> fMedicalProperties = BeanUtils.copyList(questionnaireList,
                FMedicalProperty::new);
        if(fMedicalProperties!=null){
            properties.addAll(fMedicalProperties);
        }

        List<WarnConfig> warnConfigs = warnConfigMapper.selectWarnConfigByFollowId(followId);
        List<MedicalFormVO> medicalFormVOS = BeanUtils.copyList(formList, MedicalFormVO :: new);
        List<MedicalPropertyVO> medicalPropertyVOS = BeanUtils.copyList(properties, MedicalPropertyVO :: new);
        List<WarnConfigVO> warnConfigVOS = BeanUtils.copyList(warnConfigs, WarnConfigVO::new);
        // 属性集合
        Set<String> collect = Optional.ofNullable(medicalPropertyVOS)
                .orElseGet(() -> new ArrayList<>())
                .stream()
                .filter(x -> StringUtils.isNotBlankAndNotEmpty(x.getPropertyType()))
                .map(MedicalPropertyVO::getPropertyType)
                .collect(Collectors.toSet());
        // 动态表单相关
        if(!CollectionUtils.isEmpty(collect)){
            fun.accept(medicalFormVOS, medicalPropertyVOS, collect);
        }
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
//                if (map != null && map.containsKey(x.getMedicalFormId()) && !"1".equals(x.getDynamic())){
                if (map != null && map.containsKey(x.getMedicalFormId()) && !"1".equals(x.getOpenMultiple())){
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
            if (id == x.getParentId()){
                result.add(x);
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


}
