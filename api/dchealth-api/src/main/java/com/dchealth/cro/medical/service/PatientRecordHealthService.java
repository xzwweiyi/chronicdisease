package com.dchealth.cro.medical.service;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONArray;
import com.dchealth.cro.account.domain.AccountNotice;
import com.dchealth.cro.account.mapper.AccountNoticeMapper;
import com.dchealth.cro.common.base.CommonResult;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.enums.FollowTipStatusEnum;
import com.dchealth.cro.form.common.DynamicSql;
import com.dchealth.cro.form.constant.PropertyType;
import com.dchealth.cro.form.domain.form.DynamicForm;
import com.dchealth.cro.form.domain.form.FMedicalPropertyForm;
import com.dchealth.cro.form.mapper.DynamicCommonMapper;
import com.dchealth.cro.medical.domain.PatientInfo;
import com.dchealth.cro.medical.domain.PatientRecord;
import com.dchealth.cro.medical.domain.PatientRecordFiles;
import com.dchealth.cro.medical.domain.PatientRecordHealth;
import com.dchealth.cro.medical.form.PatientRecordFilesForm;
import com.dchealth.cro.medical.form.PatientRecordSaveUpdateInfoForm;
import com.dchealth.cro.medical.mapper.PatientInfoMapper;
import com.dchealth.cro.medical.mapper.PatientRecordFilesMapper;
import com.dchealth.cro.medical.mapper.PatientRecordHealthMapper;
import com.dchealth.cro.medical.mapper.PatientRecordMapper;
import com.dchealth.cro.research.domain.ResearchUserRole;
import com.dchealth.cro.research.mapper.ResearchUserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 患者档案健康管理Service业务层处理
 *
 * @author lchbu
 * @date 2025-06-24
 */
@Slf4j
@Service
public class PatientRecordHealthService {
    private static final Logger L = LoggerFactory.getLogger(PatientRecordHealthService.class);


    @Autowired
    private PatientRecordHealthMapper patientRecordHealthMapper;

    @Autowired
    private PatientRecordMapper patientRecordMapper;

    @Autowired
    private PatientInfoMapper patientInfoMapper;

    @Resource
    private ResearchUserRoleMapper researchUserRoleMapper;

    @Autowired
    private DynamicCommonMapper dynamicCommonMapper;

    @Autowired
    private DynamicSql dynamicSql;

    @Autowired
    private PatientRecordFilesMapper patientRecordFilesMapper;

    @Autowired
    private AccountNoticeMapper accountNoticeMapper;


    /**
     * 查询患者档案健康管理
     *
     * @param healthId 患者档案健康管理主键
     * @return 患者档案健康管理
     */
    public PatientRecordHealth selectPatientRecordHealthByHealthId(Long healthId) {
        return patientRecordHealthMapper.selectPatientRecordHealthByHealthId(healthId);
    }

    /**
     * 查询患者档案健康管理列表
     *
     * @param patientRecordHealth 患者档案健康管理
     * @return 患者档案健康管理
     */
    public List<PatientRecordHealth> selectPatientRecordHealthList(PatientRecordHealth patientRecordHealth) {
        return patientRecordHealthMapper.selectPatientRecordHealthList(patientRecordHealth);
    }

    /**
     * 新增患者档案健康管理
     *
     * @param patientRecordHealth 患者档案健康管理
     * @return 结果
     */
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
    public int deletePatientRecordHealthByHealthIds(Long[] healthIds) {
        return patientRecordHealthMapper.deletePatientRecordHealthByHealthIds(healthIds);
    }

    /**
     * 删除患者档案健康管理信息
     *
     * @param healthId 患者档案健康管理主键
     * @return 结果
     */
    public int deletePatientRecordHealthByHealthId(Long healthId) {
        return patientRecordHealthMapper.deletePatientRecordHealthByHealthId(healthId);
    }

    /**
     * 新增或修改患者档案健康管理
     *
     * @param infoForm
     * @return
     */
    @Transactional
    public DchealthCloudResponseEntity<CommonResult> saveOrUpdatePatientRecordHealth(PatientRecordSaveUpdateInfoForm infoForm) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CommonResult commonResult = new CommonResult();
        log.info("保存接口表单数据{}", infoForm);


        PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByRecordId(infoForm.getRecordId());
        if (patientRecord == null) {//档案不存在
            result.setCode(CroApiCodeConstant.PATIENT_RECORD_NOT_EXIT.getCode());
            return result;
        }
        PatientRecord record = new PatientRecord();

        PatientInfo patientInfo = patientInfoMapper.selectPatientInfoById(patientRecord.getPatientId());
        patientRecord.setAccountId(patientInfo.getAccountId());

        // 动态表单的record_id 应该是health_id
        Snowflake snowflake = IdUtil.getSnowflake();
        long healthId = snowflake.nextId();
        PatientRecordHealth health = new PatientRecordHealth();
        health.setHealthId(healthId);
        health.setFollowId(patientRecord.getFollowId());
        health.setResearchId(patientRecord.getResearchId());
        health.setPatientId(patientRecord.getPatientId());
        health.setSubmitTime(DateUtils.getNowDate());
        health.setCreateTime(DateUtils.getNowDate());
        health.setHealthManageType(1);
        int i = patientRecordHealthMapper.insertPatientRecordHealth(health);
        if (i < 0) {
            L.error("保存患者档案健康管理失败");
        }
        infoForm.setHealthId(healthId);

        // 属性值集合处理
        handlePropertyFormsInfo(infoForm, patientRecord);

        //上传文件[图片]
        handleFileImagesInfo(infoForm, patientRecord);

//        // 随访记录
//        record.setIsSubmit(infoForm.getIsSubmit());
//        record.setName(patientRecord.getName());
//        handlePatientRecordInfo(patientRecord, record);

        // 修改患者队列随访状态
//        handlePatientInfo(patientRecord, record);

        // 处理 patient_info 表省市县字段
        handleArea(patientRecord.getPatientId(), infoForm.getPropertyForms());

        //处理 patient_info  表就诊信息
//        handleVisit(patientRecord.getPatientId(), infoForm.getPropertyForms());

        commonResult.setResult(true);
        result.setData(commonResult);
        return result;
    }


    private void handlePropertyFormsInfo(PatientRecordSaveUpdateInfoForm infoForm, PatientRecord patientRecord) {
        if (infoForm.getPropertyForms() != null && !infoForm.getPropertyForms().isEmpty()) {
            DynamicForm dynamicForm = new DynamicForm();
            dynamicForm.setType("1");
            dynamicForm.setRecordId(infoForm.getHealthId());
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
            }
        }
    }

    private void handleFileImagesInfo(PatientRecordSaveUpdateInfoForm infoForm, PatientRecord patientRecord) {
        patientRecordFilesMapper.deletePatientRecordFilesByHealthId(infoForm.getHealthId());
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
                recordFile.setHealthId(infoForm.getHealthId());
                recordFile.setCreateTime(d);
                recordFile.setExamRecordId(null);
                recordFiles.add(recordFile);
            });
        }
        if (!CollectionUtils.isEmpty(recordFiles)) {
            patientRecordFilesMapper.batchInsertPatientRecordFiles(recordFiles);
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
                pi.setAreaId(Long.parseLong(jsonArray.getString(jsonArray.size() - 1)));
                patientInfoMapper.updatePatientInfo(pi);
            }
        } catch (Exception e) {
            L.error("handleArea error is {}", e.getMessage());
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
                AccountNotice an = new AccountNotice(patientRecord,doctorMsgContent,"16",r.getUserId());
                accountNoticeMapper.insertAccountNotice(an);
            }
        }
    }

}
