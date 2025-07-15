package com.dchealth.cro.medical.service;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONArray;
import com.dchealth.cro.account.domain.PatientAccount;
import com.dchealth.cro.account.mapper.AccountNoticeMapper;
import com.dchealth.cro.account.mapper.PatientAccountMapper;
import com.dchealth.cro.common.base.CommonResult;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.common.utils.bean.BeanUtils;
import com.dchealth.cro.enums.HealthManageEnum;
import com.dchealth.cro.form.common.DynamicSql;
import com.dchealth.cro.form.constant.PropertyType;
import com.dchealth.cro.form.domain.DynamicCommonProperty;
import com.dchealth.cro.form.domain.FMedicalForm;
import com.dchealth.cro.form.domain.form.DynamicForm;
import com.dchealth.cro.form.domain.form.FMedicalPropertyForm;
import com.dchealth.cro.form.mapper.DynamicCommonMapper;
import com.dchealth.cro.form.mapper.FMedicalFormMapper;
import com.dchealth.cro.form.service.MedicalFormService;
import com.dchealth.cro.form.vo.MedicalFormVO;
import com.dchealth.cro.form.vo.MedicalPropertyVO;
import com.dchealth.cro.health.domain.MpPerson;
import com.dchealth.cro.health.mapper.MpPersonMapper;
import com.dchealth.cro.health.utils.HealthUtils;
import com.dchealth.cro.medical.domain.PatientInfo;
import com.dchealth.cro.medical.domain.PatientRecord;
import com.dchealth.cro.medical.domain.PatientRecordFiles;
import com.dchealth.cro.medical.domain.PatientRecordHealth;
import com.dchealth.cro.medical.form.PatientRecordDetailForm;
import com.dchealth.cro.medical.form.PatientRecordFilesForm;
import com.dchealth.cro.medical.form.PatientRecordSaveUpdateInfoForm;
import com.dchealth.cro.medical.mapper.PatientInfoMapper;
import com.dchealth.cro.medical.mapper.PatientRecordFilesMapper;
import com.dchealth.cro.medical.mapper.PatientRecordHealthMapper;
import com.dchealth.cro.medical.mapper.PatientRecordMapper;
import com.dchealth.cro.medical.vo.health.HealthTypeResp;
import com.dchealth.cro.medical.vo.health.PatientRecordHealthReq;
import com.dchealth.cro.research.domain.ResearchInfo;
import com.dchealth.cro.research.mapper.ResearchInfoMapper;
import com.dchealth.cro.research.mapper.ResearchUserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
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

    @Autowired
    private ResearchInfoMapper researchInfoMapper;

    @Autowired
    private MedicalFormService medicalFormService;

    @Autowired
    private Executor executor;

    @Resource
    private FMedicalFormMapper fMedicalFormMapper;

    @Resource
    private PatientAccountMapper patientAccountMapper;

    @Resource
    private MpPersonMapper mpPersonMapper;




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
        health.setHealthId(String.valueOf(healthId));
        health.setRecordId(infoForm.getRecordId());
        health.setFollowId(patientRecord.getFollowId());
        health.setResearchId(patientRecord.getResearchId());
        health.setPatientId(patientRecord.getPatientId());
        health.setSubmitTime(DateUtils.getNowDate());
        health.setCreateTime(DateUtils.getNowDate());
        health.setHealthManageType(infoForm.getHealthManageType());
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

    public Object patientRecordHealthDetail(PatientRecordHealthReq healthReq) {

        DchealthCloudResponseEntity<PatientRecordDetailForm> result = new DchealthCloudResponseEntity<>();
        PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByRecordId(healthReq.getRecordId());
        if (patientRecord == null) {//档案不存在
            result.setCode(CroApiCodeConstant.PATIENT_RECORD_NOT_EXIT.getCode());
            return result;
        }
        Long researchId = patientRecord.getResearchId();
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(researchId);
        if (Objects.isNull(researchInfo) || "1".equals(researchInfo.getDelFlag())) {
            result.setCode(CroApiCodeConstant.ARTICLE_NOT_EXIST.getCode());
            return result;
        }
        PatientRecordDetailForm patientRecordDetailForm = new PatientRecordDetailForm();
        patientRecordDetailForm.setRecordId(healthReq.getRecordId());
        patientRecordDetailForm.setIsSubmit(0);
        patientRecordDetailForm.setStageName(patientRecord.getName());
        patientRecordDetailForm.setOverTimeStatus(patientRecord.getOverTimeStatus());
        patientRecordDetailForm.setWarnStatus(patientRecord.getWarnStatus());
        patientRecordDetailForm.setCreateTimeStr(DateUtils.dateTime(patientRecord.getCreateTime()));
        patientRecordDetailForm.setResearchId(patientRecord.getResearchId());
        patientRecordDetailForm.setFollowTipStatus(patientRecord.getFollowTipStatus());
        if(patientRecord.getVisitTime()!= null) {
            patientRecordDetailForm.setVisitTime(DateUtils.parseDateToStrFull(patientRecord.getVisitTime()));
        }

        List<MedicalFormVO> datas = medicalFormService.followHealthForms(patientRecord.getFollowId(),
                patientRecord.getRecordId(), patientRecord.getFollowStatus(), (formVOS, propertyVOS, propertyTypes) -> {
                    List<PatientRecordFiles> patientRecordFiles =
                            patientRecordFilesMapper.selectPatientRecordFilesByRecordId(healthReq.getRecordId());
                    if (healthReq.getHealthId() != null) {
                        CompletableFuture<Void> pcf = getPcf(healthReq.getHealthId(), patientRecord, propertyVOS, propertyTypes,
                                patientRecordFiles);
                        CompletableFuture<Void> fcf = getFcf(healthReq.getHealthId(), patientRecord, formVOS, propertyVOS, patientRecordFiles);
                        CompletableFuture.allOf(pcf, fcf).join();
                    }
                }, healthReq.getHealthManageType());
        patientRecordDetailForm.setMedicalFormVOS(datas);

        //获取患者信息
        PatientInfo patientInfo = patientInfoMapper.selectPatientInfoById(patientRecord.getPatientId());
        patientRecordDetailForm.setPatientId(patientInfo.getPatientId());
        patientRecordDetailForm.setPersonId(patientInfo.getPersonId());
        patientRecordDetailForm.setPatientName(patientInfo.getPatientName());
        PatientAccount patientAccount = patientAccountMapper.selectPatientAccountByAccountId(patientInfo.getAccountId());
        if(patientAccount!=null){
            patientRecordDetailForm.setPatientMobile(patientAccount.getMobile());
        }

        MpPerson pb = mpPersonMapper.selectPersonByAccountId(patientInfo.getAccountId());
        if(pb!=null){
            patientRecordDetailForm.setSex(HealthUtils.getSex(pb.getCardId()));
        }
        result.setData(patientRecordDetailForm);
        return result;
    }

    private Boolean checkFillInFlag(Integer healthManageType, Long recordId, Long followId) {
        // 限制每天填写一次，需要不返回详情填写信息查询表中含有提交时间含有小于当前最后时间的信息则回显信息。如果没有则只显示表单
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        PatientRecordHealth patientRecordHealth = new PatientRecordHealth();
        patientRecordHealth.setStartTime(Date.from(start.atZone(ZoneId.systemDefault()).toInstant()));
        patientRecordHealth.setEndTime(Date.from(end.atZone(ZoneId.systemDefault()).toInstant()));
        patientRecordHealth.setHealthManageType(healthManageType);
        patientRecordHealth.setPatientId(recordId);
        patientRecordHealth.setFollowId(followId);
        List<PatientRecordHealth> healthList = patientRecordHealthMapper.selectPatientRecordHealthList(patientRecordHealth);
        return !healthList.isEmpty();
//        return healthList != null && !healthList.isEmpty();
    }


    private CompletableFuture<Void> getFcf(Long healthId, PatientRecord patientRecord, List<MedicalFormVO> formVOS,
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
                                f.setImageUrls(filesMap.get(f.getMedicalFormId()));
                            }
                        });
                    }
                }
//                    if (formVOS.stream().anyMatch(m->"1".equals(m.getDynamic()))){
                if (formVOS.stream().anyMatch(m -> "1".equals(m.getOpenMultiple()))) {
                    DynamicForm dynamicForm = new DynamicForm();
                    dynamicForm.setRecordId(healthId);
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

    private CompletableFuture<Void> getPcf(Long healthId, PatientRecord patientRecord,
                                           List<MedicalPropertyVO> propertyVOS, Set<String> propertyTypes,
                                           List<PatientRecordFiles> patientRecordFiles) {
        CompletableFuture<Void> pcf = CompletableFuture.runAsync(() -> {
            List<DynamicCommonProperty> t = new ArrayList<>();
            if (propertyTypes != null && !propertyTypes.isEmpty()) {
                DynamicForm dynamicForm = new DynamicForm();
                dynamicForm.setRecordId(healthId);
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
                            p.setImageUrls(filesMap.get(p.getId()));
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

    public DchealthCloudResponseEntity<List<HealthTypeResp>> healthType(PatientRecordHealthReq healthListReq) {
        DchealthCloudResponseEntity<List<HealthTypeResp>> responseEntity = new DchealthCloudResponseEntity<>();

        List<FMedicalForm> fMedicalForms = fMedicalFormMapper.selectFMedicalHealthFormJoinByMedicalFollowId(healthListReq.getFollowId(), null);
        if (fMedicalForms != null && !fMedicalForms.isEmpty()) {

            List<HealthTypeResp> healthTypeResps = new ArrayList<>();
            fMedicalForms.forEach(item -> {
                HealthTypeResp healthTypeResp = new HealthTypeResp();
                healthTypeResp.setHealthManageType(item.getHealthManageType());
                healthTypeResp.setHealthManageTypeName(HealthManageEnum.getName(item.getHealthManageType()));

                // 如果当天存在值则不可以进行编辑
                Boolean b = checkFillInFlag(item.getHealthManageType(), healthListReq.getRecordId(), item.getFollowId());
                if (b) {
                    healthTypeResp.setIsEditable(false);
                }

                healthTypeResps.add(healthTypeResp);
            });
            responseEntity.setData(healthTypeResps);
            return responseEntity;
        }
       return responseEntity;
    }
}
