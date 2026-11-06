package com.dchealth.etl.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.json.JSONUtil;
import com.dchealth.etl.domain.constant.TdRecordConstant;
import com.dchealth.etl.domain.record.*;
import com.dchealth.etl.domain.target.*;
import com.dchealth.etl.service.record.*;
import com.dchealth.etl.service.target.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zjl
 * @since 2023/3/27
 * 健康档案数据同步
 */
@Service("recordDataSyncService")
@Slf4j
public class RecordDataSyncServiceImpl implements RecordDataSyncService {

    @Autowired
    TdSyncDataInfoService tdSyncDataInfoService;
    @Autowired
    TdSyncErrorInfoService tdSyncErrorInfoService;
    @Autowired
    PatientBaseInfoService patientBaseInfoService;
    @Autowired
    TdGeneralInspectionService tdGeneralInspectionService;
    @Autowired
    TdExamMasterService tdExamMasterService;
    @Autowired
    TdTestMasterService tdTestMasterService;
    @Autowired
    TdAuxiliaryExaminationCtService tdExamCtService;
    @Autowired
    TdAuxiliaryExaminationNmrService tdExamNmrService;
    @Autowired
    TdAuxiliaryExaminationUltrasoundService tdExamUltrasoundService;
    @Autowired
    TdAuxiliaryExaminationOtherService tdExamOtherService;

    @Autowired
    TdBloodBiochemistryService tdBloodBiochemistryService;
    @Autowired
    TdBloodRoutineService tdBloodRoutineService;
    @Autowired
    TdHormoneItemsService tdHormoneItemsService;
    @Autowired
    TdMyocardiaEnzymeService tdMyocardiaEnzymeService;
    @Autowired
    TdStoolRoutineService tdStoolRoutineService;
    @Autowired
    TdThyroidFunctionItemsService tdThyroidFunctionItemsService;
    @Autowired
    TdTumorScreeningItemsService tdTumorScreeningItemsService;
    @Autowired
    TdUrineRoutineService tdUrineRoutineService;
    @Autowired
    TdDiagnosisService tdDiagnosisService;
    @Autowired
    TdOrdersService tdOrdersService;
    @Autowired
    TdDrugPrescMasterService tdDrugPrescMasterService;
    @Autowired
    TdDrugPrescDetailService tdDrugPrescDetailService;
    @Autowired
    TdBloodGasAnalysisService tdBloodGasAnalysisService;
    @Autowired
    TdCoagulationItemsService tdCoagulationItemsService;

    @Autowired
    PatMasterIndexService patMasterIndexService;
    @Autowired
    VitalSignsRecService vitalSignsRecService;
    @Autowired
    ExamMasterService examMasterService;
    @Autowired
    LabTestMasterService labTestMasterService;
    @Autowired
    ExamReportService examReportService;
    @Autowired
    LabResultService labResultService;
    @Autowired
    DiagnosisService diagnosisService;
    @Autowired
    PatVisitService patVisitService;
    @Autowired
    OrdersService ordersService;
    @Autowired
    DrugPrescMasterService drugPrescMasterService;
    @Autowired
    DrugPrescDetailService drugPrescDetailService;
    @Autowired
    OutpMedrecService outpMedrecService;
    /**
     * 患者信息
     */
    private static boolean syncPatMasterIndexNext = true;
    /**
     * 病人体征记录
     */
    private static boolean syncVitalSignsRecNext = true;
    /**
     * 检查主记录
     */
    private static boolean syncExamMasterNext = true;
    /**
     * 检验主记录
     */
    private static boolean syncTestMasterNext = true;
    /**
     * 检查结果
     */
    private static boolean syncExamReportNext = true;
    /**
     * 检验结果
     */
    private static boolean syncLabResultNext = true;
    /**
     * 诊断记录
     */
    private static boolean syncDiagnosisNext = true;
    private static boolean syncPatVisitNext = true;

    public static String formatStrShort = "yyyy-MM-dd";
    public static String formatStrTime = "yyyy-MM-dd HH:mm:ss";

    private static double pageSize = 300.00;
    private static int saveSize = 300;

    /**
     * 是否开启患者信息更新
     * 初始化是不许要更新，初始化完成后需要随诊断记录更新患者信息
     */
    private static boolean isOpenPatientBaseUpd = true;

    /**
     * 同步病人主索引
     *
     * @param patientId
     * @param startDate    yyyy-MM-dd
     * @param endDate      yyyy-MM-dd
     * @param endPatientId
     * @return
     */
    @Override
    public String syncPatMasterIndex(String patientId, String startDate, String endDate, String endPatientId) {
        log.info("syncPatMasterIndex同步开始 patientId={}", patientId);
        //记录异常信息
        TdSyncErrorInfo tdSyncErrorInfo = new TdSyncErrorInfo();
        try {
            //先从同步信息表中查询出已经同步的最新数据
            PatMasterIndex patMasterIndex = new PatMasterIndex();
            TdSyncDataInfo tdSyncDataInfo = tdSyncDataInfoService.queryByTableName("pat_master_index");
            if (tdSyncDataInfo == null || StringUtils.isBlank(tdSyncDataInfo.getDataRecords())) {
                //如果不存在pat_master_index表的记录，则创建并保存
                tdSyncDataInfo = new TdSyncDataInfo();
                //为空默认从0开始
                tdSyncDataInfo.setDataRecords("0");
                tdSyncDataInfo.setTableName("pat_master_index");
                tdSyncDataInfo.setCreateTime(new Date());
            }
            //如果传入的patientId不为空，则以传入参数为准
            if (StringUtils.isNotBlank(patientId)) {
                tdSyncDataInfo.setDataRecords(patientId);
            }

            log.info("syncPatMasterIndex同步进行中 tdSyncDataInfo={}", JSONUtil.toJsonStr(tdSyncDataInfo));
            //分页查询病人主索引
            PatientBaseInfo patientBaseInfo = null;
            while (syncPatMasterIndexNext) {

                //判断是否结束同步
                if (StringUtils.isNotBlank(endPatientId) &&
                        Integer.parseInt(tdSyncDataInfo.getDataRecords()) > Integer.parseInt(endPatientId)) {
                    syncPatMasterIndexNext = false;
                    continue;
                }

                patMasterIndex.setPatientId(tdSyncDataInfo.getDataRecords());

                PageRequest pageRequest = PageRequest.of(0, (int) pageSize);
                List<PatMasterIndex> list = patMasterIndexService.querySyncInfoByPage(patMasterIndex, pageRequest);

                //存放目标数据
                List<PatientBaseInfo> targetPatientBaseInfos = new ArrayList<>();

                if (list != null && list.size() > 0) {
                    //赋值目标表
                    for (PatMasterIndex index : list) {
                        //每次循环都记录同步数据，异常时能定位到
                        tdSyncErrorInfo.setDataRecords(index.getPatientId());

                        patientBaseInfo = new PatientBaseInfo();
                        patientBaseInfo.setPatientId(index.getPatientId());
                        patientBaseInfo.setRegistrySigns(index.getPatientId());
                        patientBaseInfo.setName(index.getName());
                        //年龄，根据身份证号计算
                        patientBaseInfo.setBirthday(IdcardUtil.getBirthDate(index.getIdNo()).toString(formatStrShort));
                        patientBaseInfo.setAge(IdcardUtil.getAgeByIdCard(index.getIdNo()));
                        if ("男".equals(index.getSex())) {
                            patientBaseInfo.setSex(1);
                        } else if ("女".equals(index.getSex())) {
                            patientBaseInfo.setSex(2);
                        } else {
                            patientBaseInfo.setSex(0);
                        }
                        //是否婚姻  可以从 病人住院主记录  PAT_VISIT里的婚姻状况,还未读取，需后期关联表查询处理
                        PatVisit patVisit = patVisitService.queryPatVisit(index.getPatientId());
                        if (patVisit != null) {
                            patientBaseInfo.setMedicalRecordNo(patVisit.getVisitId());
                            patientBaseInfo.setProfession(patVisit.getOccupation());
                            patientBaseInfo.setAdmissionHospitalDate(patVisit.getAdmissionDateTime());
                            patientBaseInfo.setMaritalStatus(patVisit.getMaritalStatus());

                            patientBaseInfo.setAdmissionHospitalDepartment(patVisit.getDeptAdmissionTo());
                            patientBaseInfo.setDeptDischargeFrom(patVisit.getDeptDischargeFrom());
                            patientBaseInfo.setDischargeDate(patVisit.getDischargeDateTime());
                        }
                        patientBaseInfo.setIdCard(index.getIdNo());
                        patientBaseInfo.setMobile(index.getPhoneNumberHome());
                        patientBaseInfo.setLinkMan(index.getNextOfKin());
                        patientBaseInfo.setLinkManMobile(index.getNextOfKinPhone());
                        patientBaseInfo.setRelationship(index.getRelationship());
                        patientBaseInfo.setNation(index.getNation());
                        patientBaseInfo.setPatientStatus("1");
                        patientBaseInfo.setCreateTime(new Date());
                        patientBaseInfo.setLastVisitDate(index.getLastVisitDate());
                        patientBaseInfo.setPatientClinicTime(index.getLastVisitDate());
                        targetPatientBaseInfos.add(patientBaseInfo);
                        //记录当期同步的数据
                        tdSyncDataInfo.setDataRecords(index.getPatientId());

                    }
                    //批量插入
                    int num = patientBaseInfoService.insertBatch(targetPatientBaseInfos);
                } else {
                    // 无数据跳出循环
                    syncPatMasterIndexNext = false;
                }
            }

            //插入之后保存同步记录
            if (tdSyncDataInfo.getId() == null) {
                tdSyncDataInfoService.insert(tdSyncDataInfo);
            } else {
                tdSyncDataInfo.setUpdateTime(new Date());
                tdSyncDataInfoService.update(tdSyncDataInfo);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            tdSyncErrorInfo.setTableName("pat_master_index");
            tdSyncErrorInfo.setErrorInfo(e.getMessage());
            tdSyncErrorInfo.setCreateTime(new Date());
            tdSyncErrorInfo.setUpdateTime(new Date());
            tdSyncErrorInfoService.insert(tdSyncErrorInfo);
            throw e;
        } finally {
            syncPatMasterIndexNext = true;
        }

        return "同步成功";
    }

    private Map checkNextInfo(String tableName, String startDate, String endDate) {
        Map rtMap = new HashMap();

        TdSyncDataInfo tdSyncDataInfo = tdSyncDataInfoService.queryByTableName(tableName);
        if (tdSyncDataInfo == null || StringUtils.isBlank(tdSyncDataInfo.getRecordDate())) {
            //如果不存在pat_master_index表的记录，则创建并保存
            tdSyncDataInfo = new TdSyncDataInfo();
            //为空默认从0开始
            tdSyncDataInfo.setDataRecords("0");
            tdSyncDataInfo.setRecordDate("2001-01-01");
            tdSyncDataInfo.setTableName(tableName);
            tdSyncDataInfo.setCreateTime(new Date());
        }
        if (StringUtils.isNotBlank(startDate)) {
            tdSyncDataInfo.setRecordDate(startDate);
        }
        rtMap.put("tdSyncDataInfo", tdSyncDataInfo);

        if (StringUtils.isBlank(tdSyncDataInfo.getRecordDate()) || StringUtils.isBlank(endDate)) {
            log.info("参数错误，无数据同步开始或结束日期，tableName={}", tableName);
            rtMap.put("nextFlag", "false");
        }

        if (endDate.length() != 10) {
            log.info("参数错误，开始或结束日期格式不正确，tableName={}", tableName);
            rtMap.put("nextFlag", "false");
        }

        return rtMap;
    }

    /**
     * 病人体症记录 按日期维度同步，按每天的数据批量同步
     *
     * @param patientId
     * @param startDate yyyy-MM-dd
     * @param endDate   yyyy-MM-dd
     * @return
     */
    @Override
    public String syncVitalSignsRec(String patientId, String startDate, String endDate) throws Exception {
        log.info("syncVitalSignsRec patientId={},startDate={},endDate={}", patientId, startDate, endDate);
        //记录异常信息
        TdSyncErrorInfo tdSyncErrorInfo = new TdSyncErrorInfo();
        try {
            String tableName = "vital_signs_rec";
            //先从同步信息表中查询出已经同步的最新数据
            Map checkNextInfo = this.checkNextInfo(tableName, startDate, endDate);
            if ("false".equals(checkNextInfo.get("nextFlag"))) {
                log.error("参数错误，无数据同步开始或结束日期，tableName={}", tableName);
                return "参数错误，无数据同步开始或结束日期";
            }

            TdSyncDataInfo tdSyncDataInfo = (TdSyncDataInfo) checkNextInfo.get("tdSyncDataInfo");
            //取数据同步记录表中记录的下一天日期
            String recordDateStr = tdSyncDataInfo.getRecordDate();

            log.info("syncVitalSignsRec同步进行中 tdSyncDataInfo={}", JSONUtil.toJsonStr(tdSyncDataInfo));
            VitalSignsRec vitalSignsRec = new VitalSignsRec();
            Map<String, Object> params = new HashMap<>();

            //存放临时数据
            //一般检查表
            Map<String, TdGeneralInspection> vsMap = null;
            while (syncVitalSignsRecNext) {
                //查询当天数据总数
                params.put("recordingDate", recordDateStr);
                vitalSignsRec.setParams(params);

                Date rDate = DateUtil.parse(recordDateStr, formatStrShort);
                if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || recordDateStr.equals(endDate)) {
                    //如果rDate大于等于当前时间，则不再进行循环
                    syncVitalSignsRecNext = false;
                    continue;
                }

                long count = vitalSignsRecService.querySyncInfoCount(vitalSignsRec);
                if (count == 0) {
                    log.info("syncVitalSignsRec当天无数据vitalSignsRec={}", JSONUtil.toJsonStr(vitalSignsRec));
                    //循环进行下一天的数据
                    recordDateStr = getDateStr(recordDateStr, 1);
                    continue;
                }

                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();

                vsMap = new HashMap<>();
                for (int i = 0; i < totalPage; i++) {

                    List<VitalSignsRec> list = vitalSignsRecService.querySyncInfoByPage(vitalSignsRec, PageRequest.of(i, (int) pageSize));
                    log.info("List<VitalSignsRec> list.size()={},vitalSignsRec={}", list.size(), JSONUtil.toJsonStr(vitalSignsRec));
                    if (list != null && list.size() > 0) {
                        for (VitalSignsRec vs : list) {

                            String key = vs.getPatientId() + "_" + DateUtil.format(vs.getTimePoint(), formatStrTime);
                            TdGeneralInspection ti = vsMap.get(key);
                            if (ti == null) {
                                ti = new TdGeneralInspection();
                            }
                            //记录是否往实体中存值，没有不保存
                            boolean mapIsNotNull = false;
                            if ("体温".equals(vs.getVitalSigns())) {
                                ti.setBodyTemperature(vs.getVitalSignsValues());
                                mapIsNotNull = true;
                            } else if ("脉搏".equals(vs.getVitalSigns())) {
                                ti.setPulse(vs.getVitalSignsValues());
                                mapIsNotNull = true;
                            } else if ("呼吸".equals(vs.getVitalSigns())) {
                                ti.setPulseRate(vs.getVitalSignsValues());
                                mapIsNotNull = true;
                            } else if ("血压low".equals(vs.getVitalSigns())) {
                                ti.setSystolicBloodPressure(vs.getVitalSignsValues());
                                mapIsNotNull = true;
                            } else if ("血压high".equals(vs.getVitalSigns())) {
                                ti.setDiastolicBloodPressure(vs.getVitalSignsValues());
                                mapIsNotNull = true;
                            } else if ("身高".equals(vs.getVitalSigns())) {
                                ti.setHeight(vs.getVitalSignsValues());
                                mapIsNotNull = true;
                            } else if ("体重".equals(vs.getVitalSigns())) {
                                ti.setWeight(vs.getVitalSignsValues());
                                mapIsNotNull = true;
                            } else if ("呼吸".equals(vs.getVitalSigns())) {
                                ti.setWeight(vs.getVitalSignsValues());
                                mapIsNotNull = true;
                            } else if ("BMI".equals(vs.getVitalSigns())) {
                                ti.setBmi(String.valueOf(vs.getVitalSignsValues()));
                                mapIsNotNull = true;
                            }
                            if (mapIsNotNull) {
                                ti.setPatientId(vs.getPatientId());
                                ti.setCreateTime(new Date());
                                ti.setUpdateTime(new Date());
                                ti.setRecordingDate(vs.getTimePoint());
                                ti.setResultDate(vs.getTimePoint());
                                vsMap.put(key, ti);
                            }
                        }
                    }
                }
                //查询出目标表当天的数据
                List<TdGeneralInspection> giList = vsMap.values().stream().collect(Collectors.toList());
                log.info("保存组装好的数据列表 giList={}", giList.size());

                //此处需根据表中一天的数据量确定是否需要分批保存
                if (giList.size() > 0) {
                    this.batchSaveInfos(tdGeneralInspectionService, "insertBatch", giList);
                }

                //记录中间表的同步信息
                tdSyncDataInfo.setRecordDate(recordDateStr);
                tdSyncDataInfo.setDataRecords(giList.get(giList.size() - 1).getPatientId());
                tdSyncErrorInfo.setDataRecords(recordDateStr);
                //获取下一天日期
                recordDateStr = getDateStr(recordDateStr, 1);

            }

            //插入之后保存同步记录
            if (tdSyncDataInfo.getId() == null) {
                tdSyncDataInfoService.insert(tdSyncDataInfo);
            } else {
                tdSyncDataInfo.setUpdateTime(new Date());
                tdSyncDataInfoService.update(tdSyncDataInfo);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            tdSyncErrorInfo.setTableName("vital_signs_rec");
            tdSyncErrorInfo.setErrorInfo(e.getMessage());
            tdSyncErrorInfo.setCreateTime(new Date());
            tdSyncErrorInfo.setUpdateTime(new Date());
            tdSyncErrorInfoService.insert(tdSyncErrorInfo);
            throw e;
        } finally {
            syncPatMasterIndexNext = true;
        }

        return "同步成功";
    }

    /**
     * 检查主记录
     *
     * @param examNo
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public String syncExamMaster(String examNo, String startDate, String endDate) {
        String tableName = "exam_master";
        log.info("syncExamMaster examNo={},startDate={},endDate={}", examNo, startDate, endDate);
        //记录异常信息
        TdSyncErrorInfo tdSyncErrorInfo = new TdSyncErrorInfo();
        try {
            //先从同步信息表中查询出已经同步的最新数据
            Map checkNextInfo = this.checkNextInfo(tableName, startDate, endDate);
            if ("false".equals(checkNextInfo.get("nextFlag"))) {
                log.error("参数错误，无数据同步开始或结束日期，tableName={}", tableName);
                return "参数错误，无数据同步开始或结束日期";
            }

            TdSyncDataInfo tdSyncDataInfo = (TdSyncDataInfo) checkNextInfo.get("tdSyncDataInfo");
            //取数据同步记录表中记录的下一天日期
            String recordDateStr = tdSyncDataInfo.getRecordDate();
            log.info("syncExamMaster同步进行中 tdSyncDataInfo={}", JSONUtil.toJsonStr(tdSyncDataInfo));
            ExamMaster examMaster = new ExamMaster();
            Map<String, Object> params = new HashMap<>();

            //存放临时数据
            while (syncExamMasterNext) {
                //查询当天数据总数
                params.put("recordingDate", recordDateStr);
                examMaster.setParams(params);

                Date rDate = DateUtil.parse(recordDateStr, formatStrShort);
                if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || recordDateStr.equals(endDate)) {
                    //如果rDate大于等于当前时间，则不再进行循环
                    //无数据且已是最新日期
                    syncExamMasterNext = false;
                    continue;
                }

                long count = examMasterService.querySyncInfoCount(examMaster);
                if (count == 0) {
                    log.info("syncExamMaster当天无数据examMaster={}", JSONUtil.toJsonStr(examMaster));
                    //循环进行下一天的数据
                    recordDateStr = getDateStr(recordDateStr, 1);
                    continue;
                }
                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();

                List<TdExamMaster> tdExamMasters = new ArrayList<>();
                TdExamMaster tdExamMaster = null;
                //循环分批取数
                for (int i = 0; i < totalPage; i++) {
                    List<ExamMaster> examMasters = examMasterService.querySyncInfoByPage(examMaster, PageRequest.of(i, (int) pageSize), count).getContent();
                    if (examMasters != null && examMasters.size() > 0) {
                        //循环处理源数据
                        for (ExamMaster em : examMasters) {
                            tdExamMaster = new TdExamMaster();
                            tdExamMaster.setExamNo(em.getExamNo());
                            tdExamMaster.setPatientId(em.getPatientId());
                            tdExamMaster.setExamDateTime(em.getExamDateTime());
                            tdExamMaster.setExamDate(DateUtil.parse(recordDateStr, formatStrShort));
                            tdExamMaster.setCreateTime(new Date());
                            tdExamMaster.setUpdateTime(new Date());
                            tdExamMaster.setReqPhysician(em.getReqPhysician());
                            tdExamMasters.add(tdExamMaster);
                        }
                        //组装完数据后，分批插入目标表
                        if (tdExamMasters != null && tdExamMasters.size() > 0) {
                            tdExamMasterService.insertBatch(tdExamMasters);
                        }
                        tdExamMasters.clear();
                    } else {
                        //当天已无数据
                        break;
                    }
                }
                //记录同步数据日期
                tdSyncDataInfo.setRecordDate(recordDateStr);
                tdSyncErrorInfo.setDataRecords(recordDateStr);
                //获取下一天日期
                recordDateStr = getDateStr(recordDateStr, 1);

            }

            //插入之后保存同步记录
            if (tdSyncDataInfo.getId() == null) {
                tdSyncDataInfoService.insert(tdSyncDataInfo);
            } else {
                tdSyncDataInfo.setUpdateTime(new Date());
                tdSyncDataInfoService.update(tdSyncDataInfo);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            tdSyncErrorInfo.setTableName(tableName);
            tdSyncErrorInfo.setErrorInfo(e.getMessage());
            tdSyncErrorInfo.setCreateTime(new Date());
            tdSyncErrorInfo.setUpdateTime(new Date());
            tdSyncErrorInfoService.insert(tdSyncErrorInfo);
            throw e;
        } finally {
            syncExamMasterNext = true;
        }
        return "同步成功";
    }


    /**
     * 检验主记录
     *
     * @param testNo
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public String syncTestMaster(String testNo, String startDate, String endDate) {
        String tableName = "lab_test_master";
        log.info("syncTestMaster testNo={},startDate={},endDate={}", testNo, startDate, endDate);
        //记录异常信息
        TdSyncErrorInfo tdSyncErrorInfo = new TdSyncErrorInfo();
        try {
            //先从同步信息表中查询出已经同步的最新数据
            Map checkNextInfo = this.checkNextInfo(tableName, startDate, endDate);
            if ("false".equals(checkNextInfo.get("nextFlag"))) {
                log.error("参数错误，无数据同步开始或结束日期，tableName={}", tableName);
                return "参数错误，无数据同步开始或结束日期";
            }

            TdSyncDataInfo tdSyncDataInfo = (TdSyncDataInfo) checkNextInfo.get("tdSyncDataInfo");
            //取数据同步记录表中记录的下一天日期
            String recordDateStr = tdSyncDataInfo.getRecordDate();
            log.info("syncTestMaster同步进行中 tdSyncDataInfo={}", JSONUtil.toJsonStr(tdSyncDataInfo));

            LabTestMaster testMaster = new LabTestMaster();
            Map<String, Object> params = new HashMap<>();

            //存放临时数据
            while (syncTestMasterNext) {
                //查询当天数据总数
                params.put("recordingDate", recordDateStr);
                testMaster.setParams(params);

                Date rDate = DateUtil.parse(recordDateStr, formatStrShort);
                if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || recordDateStr.equals(endDate)) {
                    //如果rDate大于等于当前时间，则不再进行循环
                    //无数据且已是最新日期
                    syncTestMasterNext = false;
                    continue;
                }

                long count = labTestMasterService.querySyncInfoCount(testMaster);
                if (count == 0) {
                    log.info("syncTestMaster当天无数据examMaster={}", JSONUtil.toJsonStr(testMaster));
                    //循环进行下一天的数据
                    recordDateStr = recordDateStr = getDateStr(recordDateStr, 1);
                    continue;
                }
                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();

                List<TdTestMaster> tdTestMasters = new ArrayList<>();
                TdTestMaster tdTestMaster = null;
                //循环分批取数
                for (int i = 0; i < totalPage; i++) {
                    List<LabTestMaster> testMasters = labTestMasterService.querySyncInfoByPage(testMaster, PageRequest.of(i, (int) pageSize), count).getContent();
                    if (testMasters != null && testMasters.size() > 0) {
                        //循环处理源数据
                        for (LabTestMaster tm : testMasters) {
                            tdTestMaster = new TdTestMaster();
                            tdTestMaster.setTestNo(tm.getTestNo());
                            tdTestMaster.setPatientId(tm.getPatientId());
                            tdTestMaster.setTestDateTime(tm.getExecuteDate());
                            tdTestMaster.setTestDate(DateUtil.parse(recordDateStr, formatStrShort));
                            tdTestMaster.setCreateTime(new Date());
                            tdTestMaster.setUpdateTime(new Date());
                            tdTestMaster.setOrderingProvider(tm.getOrderingProvider());
                            tdTestMasters.add(tdTestMaster);
                        }
                        //组装完数据后，分批插入目标表
                        if (tdTestMasters != null && tdTestMasters.size() > 0) {
                            tdTestMasterService.insertBatch(tdTestMasters);
                        }
                        tdTestMasters.clear();
                    } else {
                        //当天已无数据
                        break;
                    }
                }

                //记录同步数据日期
                tdSyncDataInfo.setRecordDate(recordDateStr);
                tdSyncErrorInfo.setDataRecords(recordDateStr);
                //获取下一天日期
                recordDateStr = recordDateStr = getDateStr(recordDateStr, 1);
            }

            //插入之后保存同步记录
            if (tdSyncDataInfo.getId() == null) {
                tdSyncDataInfoService.insert(tdSyncDataInfo);
            } else {
                tdSyncDataInfo.setUpdateTime(new Date());
                tdSyncDataInfoService.update(tdSyncDataInfo);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            tdSyncErrorInfo.setTableName(tableName);
            tdSyncErrorInfo.setErrorInfo(e.getMessage());
            tdSyncErrorInfo.setCreateTime(new Date());
            tdSyncErrorInfo.setUpdateTime(new Date());
            tdSyncErrorInfoService.insert(tdSyncErrorInfo);
            throw e;
        } finally {
            syncTestMasterNext = true;
        }

        return "同步成功";
    }

    /**
     * 辅助检查
     *
     * @param examNo
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public String syncExamReport(String examNo, String startDate, String endDate) throws Exception {
        String tableName = "exam_report";
        log.info("syncExamReport testNo={},startDate={},endDate={}", examNo, startDate, endDate);
        //记录异常信息
        TdSyncErrorInfo tdSyncErrorInfo = new TdSyncErrorInfo();
        try {
            //先从同步信息表中查询出已经同步的最新数据
            Map checkNextInfo = this.checkNextInfo(tableName, startDate, endDate);
            if ("false".equals(checkNextInfo.get("nextFlag"))) {
                log.error("参数错误，无数据同步开始或结束日期，tableName={}", tableName);
                return "参数错误，无数据同步开始或结束日期";
            }

            TdSyncDataInfo tdSyncDataInfo = (TdSyncDataInfo) checkNextInfo.get("tdSyncDataInfo");
            //取数据同步记录表中记录的下一天日期
            String recordDateStr = tdSyncDataInfo.getRecordDate();
            log.info("syncExamReport同步进行中 tdSyncDataInfo={}", JSONUtil.toJsonStr(tdSyncDataInfo));
            ExamReport examReport = new ExamReport();
            Map<String, Object> params = new HashMap<>();

            //存放临时数据
            while (syncExamReportNext) {
                //查询当天数据总数
                params.put("recordingDate", recordDateStr);
                examReport.setParams(params);

                Date rDate = DateUtil.parse(recordDateStr, formatStrShort);
                if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || recordDateStr.equals(endDate)) {
                    //如果rDate大于等于当前时间，则不再进行循环
                    //无数据且已是最新日期
                    syncExamReportNext = false;
                    continue;
                }

                long count = examReportService.querySyncInfoCount(examReport);
                if (count == 0) {
                    log.info("syncTestMaster当天无数据examReport={}", JSONUtil.toJsonStr(examReport));
                    //循环进行下一天的数据
                    recordDateStr = getDateStr(recordDateStr, 1);
                    continue;
                }
                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();

                //ct相关
                Map<String, TdAuxiliaryExaminationCt> tdexamCtMap = new HashMap<>();
                TdAuxiliaryExaminationCt tdexamCt = null;
                List<TdAuxiliaryExaminationCt> tdexamCts = new ArrayList<>();
                //核磁相关
                Map<String, TdAuxiliaryExaminationNmr> tdexamNmrMap = new HashMap<>();
                TdAuxiliaryExaminationNmr tdexamNmr = null;
                List<TdAuxiliaryExaminationNmr> tdexamNmrs = new ArrayList<>();
                //超声相关
                Map<String, TdAuxiliaryExaminationUltrasound> tdexamUltrasoundMap = new HashMap<>();
                TdAuxiliaryExaminationUltrasound tdexamUltrasound = null;
                List<TdAuxiliaryExaminationUltrasound> tdexamUltrasounds = new ArrayList<>();
                //其他，如心电图
                Map<String, TdAuxiliaryExaminationOther> tdexamOtherMap = new HashMap<>();
                TdAuxiliaryExaminationOther tdexamOther = null;
                List<TdAuxiliaryExaminationOther> tdexamOthers = new ArrayList<>();
                //循环分批取数
                for (int i = 0; i < totalPage; i++) {
                    List<ExamReport> examReports = examReportService.querySyncInfoByPage(examReport, PageRequest.of(i, (int) pageSize), count).getContent();

                    if (examReports != null && examReports.size() > 0) {
                        for (ExamReport er : examReports) {
                            String key = er.getExamNo();
                            tdexamCt = tdexamCtMap.get(key);
                            if (tdexamCt == null) {
                                tdexamCt = new TdAuxiliaryExaminationCt();
                            }
                            tdexamNmr = tdexamNmrMap.get(key);
                            if (tdexamNmr == null) {
                                tdexamNmr = new TdAuxiliaryExaminationNmr();
                            }
                            tdexamUltrasound = tdexamUltrasoundMap.get(key);
                            if (tdexamUltrasound == null) {
                                tdexamUltrasound = new TdAuxiliaryExaminationUltrasound();
                            }
                            tdexamOther = tdexamOtherMap.get(key);
                            if (tdexamOther == null) {
                                tdexamOther = new TdAuxiliaryExaminationOther();
                            }

                            //循环匹配检查结果到不同的数据表
                            this.getAuxiliaryExamination(er, tdexamUltrasound, tdexamOther, tdexamCt, tdexamNmr);

                            //如果循环的实体有值，则放入对应的map集合中
                            if (StringUtils.isNotBlank(tdexamCt.getExamNo())) {
                                tdexamCtMap.put(key, tdexamCt);
                            }
                            if (StringUtils.isNotBlank(tdexamNmr.getExamNo())) {
                                tdexamNmrMap.put(key, tdexamNmr);
                            }
                            if (StringUtils.isNotBlank(tdexamUltrasound.getExamNo())) {
                                tdexamUltrasoundMap.put(key, tdexamUltrasound);
                            }
                            if (StringUtils.isNotBlank(tdexamOther.getExamNo())) {
                                tdexamOtherMap.put(key, tdexamOther);
                            }
                        }
                        //同一个examNo理论上只会对应有一条检验结果，所以可以在每次分页后立即保存数据
                        //将每天对应map里的数据转为list并保存
                        tdexamCts = tdexamCtMap.values().stream().collect(Collectors.toList());
                        tdexamNmrs = tdexamNmrMap.values().stream().collect(Collectors.toList());
                        tdexamUltrasounds = tdexamUltrasoundMap.values().stream().collect(Collectors.toList());
                        tdexamOthers = tdexamOtherMap.values().stream().collect(Collectors.toList());
                        if (tdexamCts.size() > 0) {
                            this.batchSaveInfos(tdExamCtService, "insertBatch", tdexamCts);
                            tdexamCts.clear();
                        }
                        if (tdexamNmrs.size() > 0) {
                            this.batchSaveInfos(tdExamNmrService, "insertBatch", tdexamNmrs);
                            tdexamNmrs.clear();
                        }
                        if (tdexamUltrasounds.size() > 0) {
                            this.batchSaveInfos(tdExamUltrasoundService, "insertBatch", tdexamUltrasounds);
                            tdexamUltrasounds.clear();
                        }
                        if (tdexamOthers.size() > 0) {
                            this.batchSaveInfos(tdExamOtherService, "insertBatch", tdexamOthers);
                            tdexamOthers.clear();
                        }

                    }
                }

                //记录数据日期到同步记录表
                tdSyncDataInfo.setRecordDate(recordDateStr);
                tdSyncErrorInfo.setDataRecords(recordDateStr);

                //循环进行下一天的数据
                recordDateStr = getDateStr(recordDateStr, 1);
            }

            //插入之后保存同步记录
            if (tdSyncDataInfo.getId() == null) {
                tdSyncDataInfoService.insert(tdSyncDataInfo);
            } else {
                tdSyncDataInfo.setUpdateTime(new Date());
                tdSyncDataInfoService.update(tdSyncDataInfo);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            tdSyncErrorInfo.setTableName(tableName);
            tdSyncErrorInfo.setErrorInfo(e.getMessage());
            tdSyncErrorInfo.setCreateTime(new Date());
            tdSyncErrorInfo.setUpdateTime(new Date());
            tdSyncErrorInfoService.insert(tdSyncErrorInfo);
            throw e;
        } finally {
            syncExamReportNext = true;
        }

        return "同步成功";
    }

    /**
     * 获取对应的辅助检查项目并放入对应的实体中
     *
     * @param er
     * @param tdexamUltrasound
     * @param tdexamOther
     * @param tdexamCt
     * @param tdexamNmr
     */
    public void getAuxiliaryExamination(ExamReport er, TdAuxiliaryExaminationUltrasound tdexamUltrasound, TdAuxiliaryExaminationOther tdexamOther
            , TdAuxiliaryExaminationCt tdexamCt, TdAuxiliaryExaminationNmr tdexamNmr) {

        //循环匹配检查结果到不同的数据表
        if ("平片".equals(er.getExamClass())) {
            if ("胸部".equals(er.getExamSubClass())) {
                //胸片报告
                tdexamUltrasound.setCr(StringUtils.isNotBlank(er.getDescription()) ? er.getDescription() : er.getImpression());
            } else {
                //X线（胸部以外）
                tdexamUltrasound.setCrOther(StringUtils.isNotBlank(er.getDescription()) ? er.getDescription() : er.getImpression());
            }
            tdexamUltrasound.setExamNo(er.getExamNo());
            tdexamUltrasound.setResultTime(er.getExamDateTime());
        } else if ("心电图".equals(er.getExamClass()) && "心电图".equals(er.getExamSubClass())) {
            //心电图报告
            tdexamOther.setEcg(StringUtils.isNotBlank(er.getDescription()) ? er.getDescription() : er.getImpression());
            tdexamOther.setExamNo(er.getExamNo());
            tdexamOther.setResultTime(er.getExamDateTime());
        } else if ("CT".equals(er.getExamClass())) {
            //CT检查
            tdexamCt.setCt(StringUtils.isNotBlank(er.getDescription()) ? er.getDescription() : er.getImpression());
            tdexamCt.setExamNo(er.getExamNo());
            tdexamCt.setResultTime(er.getExamDateTime());
        } else if ("核医学".equals(er.getExamClass())) {
            if ("PET/MR".equals(er.getExamSubClass())) {
                //MRI检查
                tdexamNmr.setMri(StringUtils.isNotBlank(er.getDescription()) ? er.getDescription() : er.getImpression());
                tdexamNmr.setExamNo(er.getExamNo());
                tdexamNmr.setResultTime(er.getExamDateTime());
            } else if ("PET/CT".equals(er.getExamSubClass())) {
                //PET—CT检查
                tdexamCt.setPetCt(StringUtils.isNotBlank(er.getDescription()) ? er.getDescription() : er.getImpression());
                tdexamCt.setExamNo(er.getExamNo());
                tdexamCt.setResultTime(er.getExamDateTime());
            }
        } else if ("超声".equals(er.getExamClass())) {
            if ("心脏".equals(er.getExamSubClass())) {
                //心脏超声
                tdexamUltrasound.setCardiacUltrasound(StringUtils.isNotBlank(er.getDescription()) ? er.getDescription() : er.getImpression());
                tdexamUltrasound.setExamNo(er.getExamNo());
                tdexamUltrasound.setResultTime(er.getExamDateTime());
            } else if ("腹部".equals(er.getExamSubClass())) {
                //腹部超声
                tdexamUltrasound.setAbdominalUltrasound(StringUtils.isNotBlank(er.getDescription()) ? er.getDescription() : er.getImpression());
                tdexamUltrasound.setExamNo(er.getExamNo());
                tdexamUltrasound.setResultTime(er.getExamDateTime());
            }
        }
        tdexamUltrasound.setCreateTime(new Date());
        tdexamCt.setCreateTime(new Date());
        tdexamNmr.setCreateTime(new Date());
        tdexamOther.setCreateTime(new Date());
    }

    /**
     * 获取日期偏移后日期字符串
     *
     * @param recordDateStr yyyy-MM-dd
     * @param offset
     * @return
     */
    private String getDateStr(String recordDateStr, int offset) {
        Date rDate = DateUtil.parse(recordDateStr, formatStrShort);
        return DateUtil.format(DateUtil.offsetDay(rDate, offset), formatStrShort);
    }

    /**
     * 检验结果
     *
     * @param testNo
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public String syncTestResult(String testNo, String startDate, String endDate) throws Exception {
        String tableName = "lab_result";
        log.info("syncTestResult testNo={},startDate={},endDate={}", testNo, startDate, endDate);
        //记录异常信息
        TdSyncErrorInfo tdSyncErrorInfo = new TdSyncErrorInfo();
        try {
            //先从同步信息表中查询出已经同步的最新数据
            Map checkNextInfo = this.checkNextInfo(tableName, startDate, endDate);
            if ("false".equals(checkNextInfo.get("nextFlag"))) {
                log.error("参数错误，无数据同步开始或结束日期，tableName={}", tableName);
                return "参数错误，无数据同步开始或结束日期";
            }

            TdSyncDataInfo tdSyncDataInfo = (TdSyncDataInfo) checkNextInfo.get("tdSyncDataInfo");
            //取数据同步记录表中记录的下一天日期
            String recordDateStr = tdSyncDataInfo.getRecordDate();
            log.info("syncTestResult同步进行中 tdSyncDataInfo={}", JSONUtil.toJsonStr(tdSyncDataInfo));

            Map<String, Object> params = new HashMap<>();
            //处理同步数据
            recordDateStr = this.getLabResultInfos(params, recordDateStr, endDate);

            //记录数据日期到同步记录表
            tdSyncDataInfo.setRecordDate(recordDateStr);
            tdSyncErrorInfo.setDataRecords(recordDateStr);

            //插入之后保存同步记录
            if (tdSyncDataInfo.getId() == null) {
                tdSyncDataInfoService.insert(tdSyncDataInfo);
            } else {
                tdSyncDataInfo.setUpdateTime(new Date());
                tdSyncDataInfoService.update(tdSyncDataInfo);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            tdSyncErrorInfo.setTableName(tableName);
            tdSyncErrorInfo.setErrorInfo(e.getMessage());
            tdSyncErrorInfo.setCreateTime(new Date());
            tdSyncErrorInfo.setUpdateTime(new Date());
            tdSyncErrorInfoService.insert(tdSyncErrorInfo);
            throw e;
        } finally {
            syncLabResultNext = true;
        }

        return "同步成功";
    }

    /**
     * 处理同步数据
     *
     * @param params
     * @param recordDateStr
     * @param endDate
     * @return
     */
    private String getLabResultInfos(Map params, String recordDateStr, String endDate) throws Exception {
        while (syncLabResultNext) {
            //查询当天数据总数
            params.put("recordingDate", recordDateStr);
            LabResult labResult = new LabResult();
            labResult.setParams(params);

            Date rDate = DateUtil.parse(recordDateStr, formatStrShort);
            if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || recordDateStr.equals(endDate)) {
                //如果rDate大于等于当前时间，则不再进行循环
                //无数据且已是最新日期
                syncLabResultNext = false;
                continue;
            }

            long count = labResultService.querySyncInfoCount(labResult);
            if (count == 0) {
                log.info("syncTestMaster当天无数据labResult={}", JSONUtil.toJsonStr(labResult));
                //循环进行下一天的数据
                recordDateStr = getDateStr(recordDateStr, 1);
                continue;
            }
            //计算总页数
            Double num = Math.ceil(count / pageSize);
            int totalPage = num.intValue();

            log.info("同步检验结果：recordDateStr={},totalPage={},count={}", recordDateStr, totalPage, count);
            // 处理具体的检验项到对应的表及字段中 and save
            this.getLabResultMaps(labResult, totalPage, (int) pageSize, (int) count);

            log.info("同步检验结果一天结束：recordDateStr={}", recordDateStr);
            //循环进行下一天的数据
            recordDateStr = getDateStr(recordDateStr, 1);
        }

        return recordDateStr;
    }

    /**
     * 处理具体的检验项到对应的表及字段中
     *
     * @param labResult
     * @param totalPage
     * @param pageSize
     * @param count
     */
    private void getLabResultMaps(LabResult labResult, int totalPage, int pageSize, int count) throws Exception {
        //血常规
        Map<String, Map> bloodRoutineMap = new HashMap<>();
        //血液生化
        Map<String, Map> bloodBiochemistryMap = new HashMap<>();
        //激素
        Map<String, Map> hormoneItemsMap = new HashMap<>();
        //心肌酶
        Map<String, Map> myocardiaEnzymeMap = new HashMap<>();
        //便常规
        Map<String, Map> stoolRoutineMap = new HashMap<>();
        //甲状腺功能
        Map<String, Map> thyroidItemsMap = new HashMap<>();
        //肿瘤项目
        Map<String, Map> tumorItemsMap = new HashMap<>();
        //尿常规
        Map<String, Map> urineRoutineMap = new HashMap<>();
        //凝血项目
        Map<String, Map> coagulationItemsMap = new HashMap<>();
        //血气分析
        Map<String, Map> bloodGasAnalysisMap = new HashMap<>();
        //循环分批取数
        for (int i = 0; i < totalPage; i++) {
            //血常规
            Map routineMap = null;
            //血液生化
            Map biochemistryMap = null;
            //激素
            Map hormoneMap = null;
            //心肌酶
            Map myocardiaMap = null;
            //便常规
            Map stoolMap = null;
            //甲状腺功能
            Map thyroidMap = null;
            //肿瘤项目
            Map tumorMap = null;
            //尿常规
            Map urineMap = null;
            //凝血项目
            Map coagulationMap = null;
            //血气分析
            Map bloodGasMap = null;
            log.info("同步检验结果分页查询：labResult={},totalPage={},pageNo={}", JSONUtil.toJsonStr(labResult), totalPage, i);
            List<LabResult> labResults = labResultService.querySyncInfoByPage(labResult, PageRequest.of(i, pageSize), count).getContent();
            if (labResults != null && labResults.size() > 0) {
                log.info("同步检验结果分页查询结果：labResults.size()={}", labResults.size());
                for (LabResult lr : labResults) {
                    String key = lr.getTestNo();
                    //组装map信息
                    if (TdRecordConstant.bloodRoutineKeys.containsKey(lr.getReportItemCode())) {
                        //血常规
                        routineMap = bloodRoutineMap.get(key) == null ? new HashMap() : bloodRoutineMap.get(key);
                        bloodRoutineMap.put(key, routineMap);
                        this.getMapInfo(lr, routineMap, TdRecordConstant.bloodRoutineKeys);
                    } else if (TdRecordConstant.urineRoutineKeys.containsKey(lr.getReportItemCode())) {
                        //尿常规
                        urineMap = urineRoutineMap.get(key) == null ? new HashMap() : urineRoutineMap.get(key);
                        urineRoutineMap.put(key, urineMap);
                        this.getMapInfo(lr, urineMap, TdRecordConstant.urineRoutineKeys);
                    } else if (TdRecordConstant.stoolRoutineKeys.containsKey(lr.getReportItemCode())) {
                        //便常规
                        stoolMap = stoolRoutineMap.get(key) == null ? new HashMap() : stoolRoutineMap.get(key);
                        stoolRoutineMap.put(key, stoolMap);
                        this.getMapInfo(lr, stoolMap, TdRecordConstant.stoolRoutineKeys);
                    } else if (TdRecordConstant.bloodBiochemistryKeys.containsKey(lr.getReportItemCode())) {
                        //血液生化
                        biochemistryMap = bloodBiochemistryMap.get(key) == null ? new HashMap() : bloodBiochemistryMap.get(key);
                        bloodBiochemistryMap.put(key, biochemistryMap);
                        this.getMapInfo(lr, biochemistryMap, TdRecordConstant.bloodBiochemistryKeys);
                    } else if (TdRecordConstant.myocardiaKeys.containsKey(lr.getReportItemCode())) {
                        //心肌酶
                        myocardiaMap = myocardiaEnzymeMap.get(key) == null ? new HashMap() : myocardiaEnzymeMap.get(key);
                        myocardiaEnzymeMap.put(key, myocardiaMap);
                        this.getMapInfo(lr, myocardiaMap, TdRecordConstant.myocardiaKeys);
                    } else if (TdRecordConstant.tumorKeys.containsKey(lr.getReportItemCode())) {
                        //肿瘤十二项
                        tumorMap = tumorItemsMap.get(key) == null ? new HashMap() : tumorItemsMap.get(key);
                        tumorItemsMap.put(key, tumorMap);
                        this.getMapInfo(lr, tumorMap, TdRecordConstant.tumorKeys);
                    } else if (TdRecordConstant.thyroidKeys.containsKey(lr.getReportItemCode())) {
                        //甲功五项
                        thyroidMap = thyroidItemsMap.get(key) == null ? new HashMap() : thyroidItemsMap.get(key);
                        thyroidItemsMap.put(key, thyroidMap);
                        this.getMapInfo(lr, thyroidMap, TdRecordConstant.thyroidKeys);
                    } else if (TdRecordConstant.hormoneKeys.containsKey(lr.getReportItemCode())) {
                        //激素六项
                        hormoneMap = hormoneItemsMap.get(key) == null ? new HashMap() : hormoneItemsMap.get(key);
                        hormoneItemsMap.put(key, hormoneMap);
                        this.getMapInfo(lr, hormoneMap, TdRecordConstant.hormoneKeys);
                    }else if (TdRecordConstant.coagulationKeys.containsKey(lr.getReportItemCode())) {
                        //凝血项目
                        coagulationMap = coagulationItemsMap.get(key) == null ? new HashMap() : hormoneItemsMap.get(key);
                        coagulationItemsMap.put(key, coagulationMap);
                        this.getMapInfo(lr, coagulationMap, TdRecordConstant.coagulationKeys);
                    }else if (TdRecordConstant.bloodGasKeys.containsKey(lr.getReportItemCode())) {
                        //血气分析
                        bloodGasMap = bloodGasAnalysisMap.get(key) == null ? new HashMap() : hormoneItemsMap.get(key);
                        bloodGasAnalysisMap.put(key, bloodGasMap);
                        this.getMapInfo(lr, bloodGasMap, TdRecordConstant.bloodGasKeys);
                    }

                }
            }
            log.info("同步检验结果分页查询后循环处理map,map转实体");

        }
        log.info("同步检验结果同一天的数据处理完成，开始保存！");
        //处理完后保存数据
        if (bloodRoutineMap.size() > 0) {
            log.info("同步检验结果同一天的数据bloodRoutines处理完成，开始保存！size={}", bloodRoutineMap.size());
            this.batchSaveInfos(tdBloodRoutineService, "insertBatch", bloodRoutineMap.values().stream().collect(Collectors.toList()));
        }
        if (urineRoutineMap.size() > 0) {
            log.info("同步检验结果同一天的数据urineRoutines处理完成，开始保存！size={}", urineRoutineMap.size());
            this.batchSaveInfos(tdUrineRoutineService, "insertBatch", urineRoutineMap.values().stream().collect(Collectors.toList()));
        }
        if (stoolRoutineMap.size() > 0) {
            log.info("同步检验结果同一天的数据stoolRoutines处理完成，开始保存！size={}", stoolRoutineMap.size());
            this.batchSaveInfos(tdStoolRoutineService, "insertBatch", stoolRoutineMap.values().stream().collect(Collectors.toList()));
        }
        if (bloodBiochemistryMap.size() > 0) {
            log.info("同步检验结果同一天的数据bloodBiochemistrys处理完成，开始保存！size={}", bloodBiochemistryMap.size());
            this.batchSaveInfos(tdBloodBiochemistryService, "insertBatch", bloodBiochemistryMap.values().stream().collect(Collectors.toList()));
        }
        if (myocardiaEnzymeMap.size() > 0) {
            log.info("同步检验结果同一天的数据myocardias处理完成，开始保存！size={}", myocardiaEnzymeMap.size());
            this.batchSaveInfos(tdMyocardiaEnzymeService, "insertBatch", myocardiaEnzymeMap.values().stream().collect(Collectors.toList()));
        }
        if (tumorItemsMap.size() > 0) {
            log.info("同步检验结果同一天的数据tumors处理完成，开始保存！size={}", tumorItemsMap.size());
            this.batchSaveInfos(tdTumorScreeningItemsService, "insertBatch", tumorItemsMap.values().stream().collect(Collectors.toList()));
        }
        if (thyroidItemsMap.size() > 0) {
            log.info("同步检验结果同一天的数据thyroids处理完成，开始保存！size={}", thyroidItemsMap.size());
            this.batchSaveInfos(tdThyroidFunctionItemsService, "insertBatch", thyroidItemsMap.values().stream().collect(Collectors.toList()));
        }
        if (hormoneItemsMap.size() > 0) {
            log.info("同步检验结果同一天的数据hormones处理完成，开始保存！size={}", hormoneItemsMap.size());
            this.batchSaveInfos(tdHormoneItemsService, "insertBatch", hormoneItemsMap.values().stream().collect(Collectors.toList()));
        }
        if (coagulationItemsMap.size() > 0) {
            log.info("同步检验结果同一天的数据hormones处理完成，开始保存！size={}", coagulationItemsMap.size());
            this.batchSaveInfos(tdCoagulationItemsService, "insertBatch", coagulationItemsMap.values().stream().collect(Collectors.toList()));
        }
        if (bloodGasAnalysisMap.size() > 0) {
            log.info("同步检验结果同一天的数据hormones处理完成，开始保存！size={}", bloodGasAnalysisMap.size());
            this.batchSaveInfos(tdBloodGasAnalysisService, "insertBatch", bloodGasAnalysisMap.values().stream().collect(Collectors.toList()));
        }


    }

    /**
     * 处理检验结果异常项
     *
     * @param map
     */
    private void getAbnormalIndicator(Map map) {
        String abnormalIndicatorHigh = map.get("abnormalIndicatorHigh") == null ? "" : map.get("abnormalIndicatorHigh").toString();
        String abnormalIndicatorLow = map.get("abnormalIndicatorLow") == null ? "" : map.get("abnormalIndicatorLow").toString();
        if (abnormalIndicatorHigh.startsWith("null,")) {
            map.put("abnormalIndicatorHigh", abnormalIndicatorHigh.substring(5));
        }
        if (abnormalIndicatorLow.startsWith("null,")) {
            map.put("abnormalIndicatorLow", abnormalIndicatorLow.substring(5));
        }
    }

    /**
     * 组装map信息
     *
     * @param lr   唐都检验结果
     * @param map  当前表map
     * @param keys 对应的字段映射关系
     */
    private void getMapInfo(LabResult lr, Map map, Map keys) {
        map.put(keys.get(lr.getReportItemCode()), lr.getResult());
        if (StringUtils.isBlank(String.valueOf(map.get("testNo")))) {
            map.put("testNo", lr.getTestNo());
            map.put("resultTime", lr.getResultDateTime());
            map.put("createTime", new Date());
        }
        if ("H".equals(lr.getAbnormalIndicator())) {
            //检验结果偏高
            String highVals = String.valueOf(map.get("abnormalIndicatorHigh"));
            if (StringUtils.isBlank(highVals)) {
                map.put("abnormalIndicatorHigh", keys.get(lr.getReportItemCode()));
            } else {
                map.put("abnormalIndicatorHigh", keys.get(lr.getReportItemCode()) + "," + highVals);
            }
        } else if ("L".equals(lr.getAbnormalIndicator())) {
            String lowVals = String.valueOf(map.get("abnormalIndicatorLow"));
            if (StringUtils.isBlank(lowVals)) {
                map.put("abnormalIndicatorLow", keys.get(lr.getReportItemCode()));
            } else {
                map.put("abnormalIndicatorLow", keys.get(lr.getReportItemCode()) + "," + lowVals);
            }
        }
    }

    /**
     * 诊断记录
     *
     * @param patientId
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public String syncDiagnosis(String patientId, String startDate, String endDate) throws Exception {
        String tableName = "diagnosis";
        log.info("syncDiagnosis patientId={},startDate={},endDate={}", patientId, startDate, endDate);
        //记录异常信息
        TdSyncErrorInfo tdSyncErrorInfo = new TdSyncErrorInfo();
        try {
            //先从同步信息表中查询出已经同步的最新数据
            Map checkNextInfo = this.checkNextInfo(tableName, startDate, endDate);
            if ("false".equals(checkNextInfo.get("nextFlag"))) {
                log.error("参数错误，无数据同步开始或结束日期，tableName={}", tableName);
                return "参数错误，无数据同步开始或结束日期";
            }

            TdSyncDataInfo tdSyncDataInfo = (TdSyncDataInfo) checkNextInfo.get("tdSyncDataInfo");
            //取数据同步记录表中记录的下一天日期
            String recordDateStr = tdSyncDataInfo.getRecordDate();
            log.info("syncDiagnosis同步进行中 tdSyncDataInfo={}", JSONUtil.toJsonStr(tdSyncDataInfo));

            Map<String, Object> params = new HashMap<>();

            Diagnosis diagnosis = new Diagnosis();
            //存放临时数据
            while (syncDiagnosisNext) {
                //查询当天数据总数
                params.put("recordingDate", recordDateStr);
                diagnosis.setParams(params);
                Date rDate = DateUtil.parse(recordDateStr, formatStrShort);
                if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || recordDateStr.equals(endDate)) {
                    //如果rDate大于等于当前时间，则不再进行循环
                    //无数据且已是最新日期
                    syncDiagnosisNext = false;
                    continue;
                }

                long count = diagnosisService.querySyncInfoCount(diagnosis);
                if (count == 0) {
                    log.info("syncDiagnosis当天无数据examReport={}", JSONUtil.toJsonStr(diagnosis));
                    //循环进行下一天的数据
                    recordDateStr = getDateStr(recordDateStr, 1);
                    continue;
                }
                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();

                List<TdDiagnosis> tdDiagnosisList = new ArrayList<>();
                TdDiagnosis tdDiagnosis = null;
                //循环分批取数
                for (int i = 0; i < totalPage; i++) {
                    List<Diagnosis> diagnosises = diagnosisService.querySyncInfoByPage(diagnosis, PageRequest.of(i, (int) pageSize), count).getContent();
                    log.info("syncDiagnosis 循环recordDateStr={}日期的数据", recordDateStr);
                    if (diagnosises != null && diagnosises.size() > 0) {
                        List<PatientBaseInfo> updateList = new ArrayList<>();
                        log.info("syncDiagnosis 循环recordDateStr={}日期的数据,count={}", recordDateStr, diagnosises.size());
                        for (Diagnosis d : diagnosises) {
                            tdDiagnosis = new TdDiagnosis();
                            tdDiagnosis.setPatientId(d.getPatientId());
                            tdDiagnosis.setDiagnosisDate(d.getDiagnosisDate());
                            tdDiagnosis.setResultDate(d.getDiagnosisDate());
                            tdDiagnosis.setDiagnosisType(d.getDiagnosisType());
                            tdDiagnosis.setDiagnosisNo(Integer.parseInt(d.getDiagnosisNo()));
                            tdDiagnosis.setDiagnosisDesc(d.getDiagnosisDesc());
                            tdDiagnosis.setCreateTime(new Date());
                            tdDiagnosisList.add(tdDiagnosis);

                            // 需处理最近一次门诊时间，更新到患者信息表
                            if (isOpenPatientBaseUpd) {
                                if ("1".equals(d.getDiagnosisType())) {
                                    PatientBaseInfo p = new PatientBaseInfo();
                                    p.setPatientId(d.getPatientId());
                                    p.setPatientClinicTime(d.getDiagnosisDate());
                                    updateList.add(p);
                                }
                            }
                            //记录同步信息
                            tdSyncDataInfo.setDataRecords(tdDiagnosis.getPatientId());
                        }
                        if (updateList != null && updateList.size() > 0) {
                            //更新患者门诊时间
                            patientBaseInfoService.updateBatch(updateList);
                        }
                    }
                    //分批次保存
                    this.batchSaveInfos(tdDiagnosisService, "insertBatch", tdDiagnosisList);
                }

                //记录数据日期到同步记录表
                tdSyncDataInfo.setRecordDate(recordDateStr);
                tdSyncErrorInfo.setDataRecords(recordDateStr);
                log.info("syncDiagnosis 循环recordDateStr={}日期的数据处理完毕，循环下一天的数据", recordDateStr);
                //循环进行下一天的数据
                recordDateStr = getDateStr(recordDateStr, 1);
            }

            //插入之后保存同步记录
            if (tdSyncDataInfo.getId() == null) {
                tdSyncDataInfoService.insert(tdSyncDataInfo);
            } else {
                tdSyncDataInfo.setUpdateTime(new Date());
                tdSyncDataInfoService.update(tdSyncDataInfo);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            tdSyncErrorInfo.setTableName(tableName);
            tdSyncErrorInfo.setErrorInfo(e.getMessage());
            tdSyncErrorInfo.setCreateTime(new Date());
            tdSyncErrorInfo.setUpdateTime(new Date());
            tdSyncErrorInfoService.insert(tdSyncErrorInfo);
            throw e;
        } finally {
            syncDiagnosisNext = true;
        }

        return "同步成功";
    }


    /**
     * 按固定长度条数批量保存数据
     *
     * @param obj        调用的类
     * @param methodName 调用方法
     * @param list       保存的数据集
     * @throws Exception
     */
    private void batchSaveInfos(Object obj, String methodName, List list) throws Exception {
        try {
            log.info("batchSaveInfos 保存数据：obj={},list.size()={}", obj.getClass().getName(), list.size());
            if (list != null && list.size() > 0) {
                Method method = obj.getClass().getDeclaredMethod(methodName, List.class);
                List<List> partition = Lists.partition(list, saveSize);
                for (List ls : partition) {
                    method.invoke(obj, ls);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public String syncPatVisit(String patientId, String startDate, String endDate) throws Exception {
        String tableName = "pat_visit";
        log.info("syncPatVisit patientId={},startDate={},endDate={}", patientId, startDate, endDate);
        //记录异常信息
        TdSyncErrorInfo tdSyncErrorInfo = new TdSyncErrorInfo();
        try {
            //先从同步信息表中查询出已经同步的最新数据
            Map checkNextInfo = this.checkNextInfo(tableName, startDate, endDate);
            if ("false".equals(checkNextInfo.get("nextFlag"))) {
                log.error("参数错误，无数据同步开始或结束日期，tableName={}", tableName);
                return "参数错误，无数据同步开始或结束日期";
            }

            TdSyncDataInfo tdSyncDataInfo = (TdSyncDataInfo) checkNextInfo.get("tdSyncDataInfo");
            //取数据同步记录表中记录的下一天日期
            String recordDateStr = tdSyncDataInfo.getRecordDate();
            log.info("syncPatVisit同步进行中 tdSyncDataInfo={}", JSONUtil.toJsonStr(tdSyncDataInfo));

            Map<String, Object> params = new HashMap<>();

            PatVisit patVisit = new PatVisit();
            //存放临时数据
            while (syncPatVisitNext) {
                //查询当天数据总数
                params.put("recordingDate", recordDateStr);
                patVisit.setParams(params);
                Date rDate = DateUtil.parse(recordDateStr, formatStrShort);
                if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || recordDateStr.equals(endDate)) {
                    //如果rDate大于等于当前时间，则不再进行循环
                    //无数据且已是最新日期
                    syncPatVisitNext = false;
                    continue;
                }

                long count = patVisitService.querySyncInfoCount(patVisit);
                if (count == 0) {
                    log.info("syncPatVisit当天无数据examReport={}", JSONUtil.toJsonStr(patVisit));
                    //循环进行下一天的数据
                    recordDateStr = getDateStr(recordDateStr, 1);
                    continue;
                }
                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();
                //循环分批取数
                for (int i = 0; i < totalPage; i++) {
                    List<PatientBaseInfo> updateList = new ArrayList<>();
                    List<PatVisit> patVisits = patVisitService.querySyncInfoByPage(patVisit, PageRequest.of(i, (int) pageSize), count).getContent();
                    log.info("syncPatVisit 循环recordDateStr={}日期的数据", recordDateStr);
                    if (patVisits != null && patVisits.size() > 0) {
                        log.info("syncPatVisit 循环recordDateStr={}日期的数据,count={}", recordDateStr, patVisits.size());
                        for (PatVisit pv : patVisits) {
                            PatientBaseInfo pb = new PatientBaseInfo();
                            pb.setPatientId(pv.getPatientId());
                            pb.setAdmissionHospitalDate(pv.getAdmissionDateTime());
                            pb.setAdmissionHospitalDepartment(pv.getDeptAdmissionTo());
                            pb.setMaritalStatus(pv.getMaritalStatus());
                            pb.setDischargeDate(pv.getDischargeDateTime());
                            pb.setDeptDischargeFrom(pv.getDeptDischargeFrom());
                            updateList.add(pb);
                        }
                        //保存修改数据
                        patientBaseInfoService.updateBatch(updateList);
                    }
                }
                //记录数据日期到同步记录表
                tdSyncDataInfo.setRecordDate(recordDateStr);
                tdSyncErrorInfo.setDataRecords(recordDateStr);
                //循环进行下一天的数据
                recordDateStr = getDateStr(recordDateStr, 1);
            }

            //插入之后保存同步记录
            if (tdSyncDataInfo.getId() == null) {
                tdSyncDataInfoService.insert(tdSyncDataInfo);
            } else {
                tdSyncDataInfo.setUpdateTime(new Date());
                tdSyncDataInfoService.update(tdSyncDataInfo);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            tdSyncErrorInfo.setTableName(tableName);
            tdSyncErrorInfo.setErrorInfo(e.getMessage());
            tdSyncErrorInfo.setCreateTime(new Date());
            tdSyncErrorInfo.setUpdateTime(new Date());
            tdSyncErrorInfoService.insert(tdSyncErrorInfo);
            throw e;
        } finally {
            syncPatVisitNext = true;
        }

        return "同步成功";
    }

    /**
     * 医嘱信息
     *
     * @param patientId
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public String syncOrders(String patientId, String startDate, String endDate) throws Exception {
        boolean syncOrdersNext =true;
        String tableName = "orders";
        log.info("syncOrders patientId={},startDate={},endDate={}", patientId, startDate, endDate);
        //记录异常信息
        TdSyncErrorInfo tdSyncErrorInfo = new TdSyncErrorInfo();
        try {
            //先从同步信息表中查询出已经同步的最新数据
            Map checkNextInfo = this.checkNextInfo(tableName, startDate, endDate);
            if ("false".equals(checkNextInfo.get("nextFlag"))) {
                log.error("参数错误，无数据同步开始或结束日期，tableName={}", tableName);
                return "参数错误，无数据同步开始或结束日期";
            }

            TdSyncDataInfo tdSyncDataInfo = (TdSyncDataInfo) checkNextInfo.get("tdSyncDataInfo");
            //取数据同步记录表中记录的下一天日期
            String recordDateStr = tdSyncDataInfo.getRecordDate();
            log.info("syncOrders同步进行中 tdSyncDataInfo={}", JSONUtil.toJsonStr(tdSyncDataInfo));

            Map<String, Object> params = new HashMap<>();
            Orders orders = new Orders();
            //存放临时数据
            while (syncOrdersNext) {
                //查询当天数据总数
                params.put("recordingDate", recordDateStr);
                orders.setParams(params);
                Date rDate = DateUtil.parse(recordDateStr, formatStrShort);
                if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || recordDateStr.equals(endDate)) {
                    //如果rDate大于等于当前时间，则不再进行循环
                    //无数据且已是最新日期
                    syncOrdersNext = false;
                    continue;
                }

                long count = ordersService.querySyncInfoCount(orders);
                if (count == 0) {
                    log.info("syncOrders当天无数据examReport={}", JSONUtil.toJsonStr(orders));
                    //循环进行下一天的数据
                    recordDateStr = getDateStr(recordDateStr, 1);
                    continue;
                }
                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();
                //循环分批取数
                for (int i = 0; i < totalPage; i++) {
                    List<TdOrders> insertList = new ArrayList<>();
                    List<Orders> ordersList = ordersService.querySyncInfoByPage(orders, PageRequest.of(i, (int) pageSize), count).getContent();
                    log.info("syncOrders 循环recordDateStr={}日期的数据", recordDateStr);
                    if (ordersList != null && ordersList.size() > 0) {
                        log.info("syncOrders 循环recordDateStr={}日期的数据,count={}", recordDateStr, ordersList.size());
                        ordersList.stream().forEach(o->{
                            TdOrders tdOrders = new TdOrders();
                            tdOrders.setOrderClass(o.getOrderClass());
                            tdOrders.setOrderNo(o.getOrderNo());
                            tdOrders.setOrderSubNo(o.getOrderNo());
                            tdOrders.setOrderText(o.getOrderText());
                            tdOrders.setPatientId(o.getPatientId());
                            tdOrders.setRepeatIndicator(o.getRepeatIndicator());
                            tdOrders.setVisitId(o.getVisitId());
                            tdOrders.setEnterDateTime(o.getEnterDateTime());
                            tdOrders.setStartDateTime(o.getStartDateTime());
                            tdOrders.setStopDateTime(o.getStopDateTime());
                            tdOrders.setOrdersDate(o.getEnterDateTime());
                            insertList.add(tdOrders);
                        });
                    }
                    tdOrdersService.insertBatch(insertList);
                }
                //记录数据日期到同步记录表
                tdSyncDataInfo.setRecordDate(recordDateStr);
                tdSyncErrorInfo.setDataRecords(recordDateStr);
                //循环进行下一天的数据
                recordDateStr = getDateStr(recordDateStr, 1);
            }

            //插入之后保存同步记录
            if (tdSyncDataInfo.getId() == null) {
                tdSyncDataInfoService.insert(tdSyncDataInfo);
            } else {
                tdSyncDataInfo.setUpdateTime(new Date());
                tdSyncDataInfoService.update(tdSyncDataInfo);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            tdSyncErrorInfo.setTableName(tableName);
            tdSyncErrorInfo.setErrorInfo(e.getMessage());
            tdSyncErrorInfo.setCreateTime(new Date());
            tdSyncErrorInfo.setUpdateTime(new Date());
            tdSyncErrorInfoService.insert(tdSyncErrorInfo);
            throw e;
        }

        return "同步成功";
    }

    /**
     * 药品处方主信息
     *
     * @param patientId
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public String syncDrugPrescMaster(String patientId, String startDate, String endDate) throws Exception {
        boolean syncDrugMasterNext =true;
        String tableName = "drug_presc_master";
        log.info("syncDrugPrescMaster patientId={},startDate={},endDate={}", patientId, startDate, endDate);
        //记录异常信息
        TdSyncErrorInfo tdSyncErrorInfo = new TdSyncErrorInfo();
        try {
            //先从同步信息表中查询出已经同步的最新数据
            Map checkNextInfo = this.checkNextInfo(tableName, startDate, endDate);
            if ("false".equals(checkNextInfo.get("nextFlag"))) {
                log.error("参数错误，无数据同步开始或结束日期，tableName={}", tableName);
                return "参数错误，无数据同步开始或结束日期";
            }

            TdSyncDataInfo tdSyncDataInfo = (TdSyncDataInfo) checkNextInfo.get("tdSyncDataInfo");
            //取数据同步记录表中记录的下一天日期
            String recordDateStr = tdSyncDataInfo.getRecordDate();
            log.info("syncDrugPrescMaster同步进行中 tdSyncDataInfo={}", JSONUtil.toJsonStr(tdSyncDataInfo));

            Map<String, Object> params = new HashMap<>();
            DrugPrescMaster drugPrescMaster = new DrugPrescMaster();
            //存放临时数据
            while (syncDrugMasterNext) {
                //查询当天数据总数
                params.put("recordingDate", recordDateStr);
                drugPrescMaster.setParams(params);
                Date rDate = DateUtil.parse(recordDateStr, formatStrShort);
                if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || recordDateStr.equals(endDate)) {
                    //如果rDate大于等于当前时间，则不再进行循环
                    //无数据且已是最新日期
                    syncDrugMasterNext = false;
                    continue;
                }

                long count = drugPrescMasterService.querySyncInfoCount(drugPrescMaster);
                if (count == 0) {
                    log.info("syncDrugPrescMaster当天无数据examReport={}", JSONUtil.toJsonStr(drugPrescMaster));
                    //循环进行下一天的数据
                    recordDateStr = getDateStr(recordDateStr, 1);
                    continue;
                }
                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();
                //循环分批取数
                for (int i = 0; i < totalPage; i++) {
                    List<TdDrugPrescMaster> insertList = new ArrayList<>();
                    List<DrugPrescMaster> drugList = drugPrescMasterService.querySyncInfoByPage(drugPrescMaster, PageRequest.of(i, (int) pageSize), count).getContent();
                    log.info("syncDrugPrescMaster 循环recordDateStr={}日期的数据", recordDateStr);
                    if (drugList != null && drugList.size() > 0) {
                        log.info("syncDrugPrescMaster 循环recordDateStr={}日期的数据,count={}", recordDateStr, drugList.size());
                        drugList.stream().forEach(o->{
                            TdDrugPrescMaster tdDrug = new TdDrugPrescMaster();
                            tdDrug.setPatientId(o.getPatientId());
                            tdDrug.setPrescDate(o.getPrescDate());
                            tdDrug.setPrescNo(o.getPrescNo());
                            tdDrug.setPrescSource(o.getPrescSource());
                            tdDrug.setPrescType(o.getPrescType());
                            insertList.add(tdDrug);
                        });
                    }
                    tdDrugPrescMasterService.insertBatch(insertList);
                }
                //记录数据日期到同步记录表
                tdSyncDataInfo.setRecordDate(recordDateStr);
                tdSyncErrorInfo.setDataRecords(recordDateStr);
                //循环进行下一天的数据
                recordDateStr = getDateStr(recordDateStr, 1);
            }
            //插入之后保存同步记录
            if (tdSyncDataInfo.getId() == null) {
                tdSyncDataInfoService.insert(tdSyncDataInfo);
            } else {
                tdSyncDataInfo.setUpdateTime(new Date());
                tdSyncDataInfoService.update(tdSyncDataInfo);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            tdSyncErrorInfo.setTableName(tableName);
            tdSyncErrorInfo.setErrorInfo(e.getMessage());
            tdSyncErrorInfo.setCreateTime(new Date());
            tdSyncErrorInfo.setUpdateTime(new Date());
            tdSyncErrorInfoService.insert(tdSyncErrorInfo);
            throw e;
        }
        return "同步成功";
    }
    /**
     * 药品处方明细信息
     *
     * @param patientId
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public String syncDrugPrescDetail(String patientId, String startDate, String endDate) throws Exception {
        boolean syncDrugDetailNext =true;
        String tableName = "orders";
        log.info("syncDrugPrescDetail patientId={},startDate={},endDate={}", patientId, startDate, endDate);
        //记录异常信息
        TdSyncErrorInfo tdSyncErrorInfo = new TdSyncErrorInfo();
        try {
            //先从同步信息表中查询出已经同步的最新数据
            Map checkNextInfo = this.checkNextInfo(tableName, startDate, endDate);
            if ("false".equals(checkNextInfo.get("nextFlag"))) {
                log.error("参数错误，无数据同步开始或结束日期，tableName={}", tableName);
                return "参数错误，无数据同步开始或结束日期";
            }

            TdSyncDataInfo tdSyncDataInfo = (TdSyncDataInfo) checkNextInfo.get("tdSyncDataInfo");
            //取数据同步记录表中记录的下一天日期
            String recordDateStr = tdSyncDataInfo.getRecordDate();
            log.info("syncDrugPrescDetail 同步进行中 tdSyncDataInfo={}", JSONUtil.toJsonStr(tdSyncDataInfo));

            Map<String, Object> params = new HashMap<>();
            DrugPrescDetail dDetail = new DrugPrescDetail();
            //存放临时数据
            while (syncDrugDetailNext) {
                //查询当天数据总数
                params.put("recordingDate", recordDateStr);
                dDetail.setParams(params);
                Date rDate = DateUtil.parse(recordDateStr, formatStrShort);
                if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || recordDateStr.equals(endDate)) {
                    //如果rDate大于等于当前时间，则不再进行循环
                    //无数据且已是最新日期
                    syncDrugDetailNext = false;
                    continue;
                }

                long count = drugPrescDetailService.querySyncInfoCount(dDetail);
                if (count == 0) {
                    log.info("syncDrugPrescDetail 当天无数据examReport={}", JSONUtil.toJsonStr(dDetail));
                    //循环进行下一天的数据
                    recordDateStr = getDateStr(recordDateStr, 1);
                    continue;
                }
                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();
                //循环分批取数
                for (int i = 0; i < totalPage; i++) {
                    List<TdDrugPrescDetail> insertList = new ArrayList<>();
                    List<DrugPrescDetail> detailList = drugPrescDetailService.querySyncInfoByPage(dDetail, PageRequest.of(i, (int) pageSize), count).getContent();
                    log.info("syncDrugPrescDetail 循环recordDateStr={}日期的数据", recordDateStr);
                    if (detailList != null && detailList.size() > 0) {
                        log.info("syncDrugPrescDetail 循环recordDateStr={}日期的数据,count={}", recordDateStr, detailList.size());
                        detailList.stream().forEach(o->{
                            TdDrugPrescDetail detail = new TdDrugPrescDetail();
                            detail.setPrescDate(o.getPrescDate());
                            detail.setPrescNo(o.getPrescNo());
                            detail.setDrugCode(o.getDrugCode());
                            detail.setDrugName(o.getDrugName());
                            detail.setDrugSpec(o.getDrugSpec());
                            detail.setItemNo(o.getItemNo());
                            insertList.add(detail);
                        });
                    }
                    tdDrugPrescDetailService.insertBatch(insertList);
                }
                //记录数据日期到同步记录表
                tdSyncDataInfo.setRecordDate(recordDateStr);
                tdSyncErrorInfo.setDataRecords(recordDateStr);
                //循环进行下一天的数据
                recordDateStr = getDateStr(recordDateStr, 1);
            }
            //插入之后保存同步记录
            if (tdSyncDataInfo.getId() == null) {
                tdSyncDataInfoService.insert(tdSyncDataInfo);
            } else {
                tdSyncDataInfo.setUpdateTime(new Date());
                tdSyncDataInfoService.update(tdSyncDataInfo);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            tdSyncErrorInfo.setTableName(tableName);
            tdSyncErrorInfo.setErrorInfo(e.getMessage());
            tdSyncErrorInfo.setCreateTime(new Date());
            tdSyncErrorInfo.setUpdateTime(new Date());
            tdSyncErrorInfoService.insert(tdSyncErrorInfo);
            throw e;
        }
        return "同步成功";
    }
    /**
     * 同步门诊诊断信息
     * @param patientId
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    @Override
    public String syncOutpMedrec(String patientId, String startDate, String endDate) throws Exception {
        boolean syncOutpMedrecNext =true;
        String tableName = "orders";
        log.info("syncOutpMedrec patientId={},startDate={},endDate={}", patientId, startDate, endDate);
        //记录异常信息
        TdSyncErrorInfo tdSyncErrorInfo = new TdSyncErrorInfo();
        try {
            //先从同步信息表中查询出已经同步的最新数据
            Map checkNextInfo = this.checkNextInfo(tableName, startDate, endDate);
            if ("false".equals(checkNextInfo.get("nextFlag"))) {
                log.error("参数错误，无数据同步开始或结束日期，tableName={}", tableName);
                return "参数错误，无数据同步开始或结束日期";
            }

            TdSyncDataInfo tdSyncDataInfo = (TdSyncDataInfo) checkNextInfo.get("tdSyncDataInfo");
            //取数据同步记录表中记录的下一天日期
            String recordDateStr = tdSyncDataInfo.getRecordDate();
            log.info("syncOutpMedrec 同步进行中 tdSyncDataInfo={}", JSONUtil.toJsonStr(tdSyncDataInfo));

            Map<String, Object> params = new HashMap<>();
            OutpMedrec outpMedrec = new OutpMedrec();
            while (syncOutpMedrecNext) {
                //查询当天数据总数
                params.put("recordingDate", recordDateStr);
                outpMedrec.setParams(params);
                Date rDate = DateUtil.parse(recordDateStr, formatStrShort);
                if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || recordDateStr.equals(endDate)) {
                    //如果rDate大于等于当前时间，则不再进行循环
                    //无数据且已是最新日期
                    syncOutpMedrecNext = false;
                    continue;
                }
                long count = outpMedrecService.querySyncInfoCount(outpMedrec);
                if (count == 0) {
                    log.info("syncOutpMedrec 当天无数据examReport={}", JSONUtil.toJsonStr(outpMedrec));
                    //循环进行下一天的数据
                    recordDateStr = getDateStr(recordDateStr, 1);
                    continue;
                }
                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();
                //循环分批取数
                for (int i = 0; i < totalPage; i++) {
                    List<TdDiagnosis> instList = new ArrayList<>();
                    List<PatientBaseInfo> updateList = new ArrayList<>();
                    List<OutpMedrec> outpMedrecList = outpMedrecService.querySyncInfoByPage(outpMedrec,PageRequest.of(i, (int) pageSize), count).getContent();
                    outpMedrecList.stream().forEach(om ->{
                        TdDiagnosis tdDiagnosis = new TdDiagnosis();
                        tdDiagnosis = new TdDiagnosis();
                        tdDiagnosis.setPatientId(om.getPatientId());
                        tdDiagnosis.setDiagnosisDate(om.getVisitDate());
                        tdDiagnosis.setResultDate(om.getVisitDate());
                        tdDiagnosis.setDiagnosisType("1");
                        tdDiagnosis.setDiagnosisNo(om.getOutpId());
                        tdDiagnosis.setDiagnosisDesc(om.getResult());
                        tdDiagnosis.setCreateTime(new Date());
                        instList.add(tdDiagnosis);

                        // 需处理最近一次门诊时间，更新到患者信息表
                        if (isOpenPatientBaseUpd) {
                            PatientBaseInfo p = new PatientBaseInfo();
                            p.setPatientId(om.getPatientId());
                            p.setPatientClinicTime(om.getVisitDate());
                            updateList.add(p);
                        }
                        //记录同步信息
                        tdSyncDataInfo.setDataRecords(tdDiagnosis.getPatientId());
                    });

                    //保存数据
                    if (updateList != null && updateList.size() > 0) {
                        //更新患者门诊时间
                        patientBaseInfoService.updateBatch(updateList);
                    }

                    //分批次保存
                    this.batchSaveInfos(tdDiagnosisService, "insertBatch", instList);
                }

                //记录数据日期到同步记录表
                tdSyncDataInfo.setRecordDate(recordDateStr);
                tdSyncErrorInfo.setDataRecords(recordDateStr);
                //循环进行下一天的数据
                recordDateStr = getDateStr(recordDateStr, 1);
            }
            //插入之后保存同步记录
            if (tdSyncDataInfo.getId() == null) {
                tdSyncDataInfoService.insert(tdSyncDataInfo);
            } else {
                tdSyncDataInfo.setUpdateTime(new Date());
                tdSyncDataInfoService.update(tdSyncDataInfo);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            tdSyncErrorInfo.setTableName(tableName);
            tdSyncErrorInfo.setErrorInfo(e.getMessage());
            tdSyncErrorInfo.setCreateTime(new Date());
            tdSyncErrorInfo.setUpdateTime(new Date());
            tdSyncErrorInfoService.insert(tdSyncErrorInfo);
            throw e;
        }
        return "同步成功";
    }





}