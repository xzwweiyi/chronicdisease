package com.dchealth.web.job;

import com.dchealth.common.DynamicSql;
import com.dchealth.dynamic.mapper.DynamicCommonMapper;
import com.dchealth.medical.domain.MedicalFormProperty;
import com.dchealth.medical.domain.form.DynamicCommonProperty;
import com.dchealth.medical.domain.form.DynamicForm;
import com.dchealth.medical.mapper.MedicalFormPropertyMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.mysql.cj.util.TimeUtil.DATE_FORMATTER;

@lombok.extern.slf4j.Slf4j
@Slf4j
@Component
public class FollowPatientInfoListJob {

    @Autowired
    private DynamicSql dynamicSql;

    @Autowired
    private DynamicCommonMapper dynamicCommonMapper;

    @Autowired
    private MedicalFormPropertyMapper medicalFormPropertyMapper;


    @XxlJob("followPatientInfoJobHandler")
    public void followPatientInfoList() {

//        List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoList(new PatientInfo());

        List<MedicalFormProperty> result = null;

        String jobParam = XxlJobHelper.getJobParam();
        // 空值校验，如果是空值全量处理
        if (jobParam == null || jobParam.trim().isEmpty()) {
            result = medicalFormPropertyMapper.selectFollowPatientInfoList(new MedicalFormProperty());
        } else {
            // 通过sql查询到所有的随访信息
            MedicalFormProperty medicalFormProperty = new MedicalFormProperty();
            LocalDateTime targetDate = LocalDateTime.parse(jobParam.trim(), DATE_FORMATTER);
            LocalDateTime startOfDay = targetDate.with(LocalTime.MIN);
            medicalFormProperty.setCreateTime(startOfDay);

            result = medicalFormPropertyMapper.selectFollowPatientInfoList(medicalFormProperty);
        }


        Map<Long, List<MedicalFormProperty>> listMap = result.stream().collect(Collectors.groupingBy(MedicalFormProperty::getResearchId));

        for (Map.Entry<Long, List<MedicalFormProperty>> item : listMap.entrySet()) {

            Long researchId = item.getKey();
            List<MedicalFormProperty> dtoList = item.getValue();

            List<Long> recordIds = dtoList.stream().map(MedicalFormProperty::getRecordId).collect(Collectors.toList());
            // 解析从动态表单获取的属性值
            List<DynamicCommonProperty> t = new ArrayList<>();
            DynamicForm dynamicForm = new DynamicForm();
            dynamicForm.setRecordIds(recordIds);
            dynamicForm.setType("1");
            //大文本
            dynamicForm.setSql(dynamicSql.buildSelectLargeText(researchId));
            List<DynamicCommonProperty> largeTextList = dynamicCommonMapper.selectBySql(dynamicForm);
            if (largeTextList != null && !largeTextList.isEmpty()) {
                t.addAll(largeTextList);
            }
            //小文本
            dynamicForm.setSql(dynamicSql.buildSelectText(researchId));
            List<DynamicCommonProperty> textList = dynamicCommonMapper.selectBySql(dynamicForm);
            if (textList != null && !textList.isEmpty()) {
                t.addAll(textList);
            }
            //动态
            dynamicForm.setSql(dynamicSql.buildSelectDynamic(researchId));
            List<DynamicCommonProperty> dynaicList = dynamicCommonMapper.selectBySql(dynamicForm);
            if (dynaicList != null && !dynaicList.isEmpty()) {
                t.addAll(dynaicList);
            }
            t = new ArrayList<>(t.stream().collect(
                    Collectors.collectingAndThen(Collectors.toCollection(() -> new
                            TreeSet<>(Comparator.comparing(DynamicCommonProperty::getPropertyId))), ArrayList::new)));

            Map<Long, DynamicCommonProperty> dp = t.stream().collect(Collectors.toMap(DynamicCommonProperty::getPropertyId, x -> x));

            if (dtoList != null && !dtoList.isEmpty()) {
                dtoList.forEach(dto -> {

                    if (dto.getPropertyType().equals("Checkbox") || dto.getPropertyType().equals("Radio") || dto.getPropertyType().equals("Select")) {

                        String propertyAttribute = dto.getPropertyAttribute();

                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            JsonNode jsonNode = objectMapper.readTree(propertyAttribute);

                            List<String> list = new ArrayList<>();
                            DynamicCommonProperty dynamicCommonProperty = dp.get(dto.getPropertyId());
                            jsonNode.get("options").forEach(option -> {
                                String label = option.get("label").asText();
                                String value = option.get("value").asText();

                                if (dynamicCommonProperty != null) {
                                    String propertyValue = dynamicCommonProperty.getPropertyValue();

                                    List<String> s = Arrays.asList(propertyValue.split(","));
                                    s.forEach(m -> {
                                        if (m.equals(value)) {
                                            list.add(label);
                                        }
                                    });
                                    dto.setOther(dynamicCommonProperty.getOther());
                                }


                            });

                            dto.setValue(String.join(",", list));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        if (dp.containsKey(dto.getPropertyId())) {
                            dto.setValue(dp.get(dto.getPropertyId()).getPropertyValue());
                        }
                    }

                });
                List<Long> collect = dtoList.stream().map(MedicalFormProperty::getPropertyId).collect(Collectors.toList());

                MedicalFormProperty property = new MedicalFormProperty();
                property.setPropertyIds(collect);
                List<MedicalFormProperty> medicalFormProperties = medicalFormPropertyMapper.selectMedicalFormPropertyList(property);
                List<Long> propertyIds = medicalFormProperties.stream().map(MedicalFormProperty::getPropertyId).collect(Collectors.toList());

                List<MedicalFormProperty> collected = dtoList.stream().filter(dto -> !propertyIds.contains(dto.getPropertyId())).collect(Collectors.toList());

                if (!collected.isEmpty()) {
                    medicalFormPropertyMapper.batchInsertMedicalFormProperty(collected);
                }
                log.info("处理之后的数据{}", collected);
            }
        }
    }
}
