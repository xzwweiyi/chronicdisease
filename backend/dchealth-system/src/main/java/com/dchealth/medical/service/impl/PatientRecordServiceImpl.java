package com.dchealth.medical.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.common.BaseInfoCodeNameEnum;
import com.dchealth.common.DynamicSql;
import com.dchealth.common.exception.ServiceException;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.dynamic.mapper.DynamicCommonMapper;
import com.dchealth.export.domain.PatientMedicalPropertyResVO;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.PatientRecordFiles;
import com.dchealth.medical.domain.PatientRecordTemplate;
import com.dchealth.medical.domain.form.DynamicCommonProperty;
import com.dchealth.medical.domain.form.DynamicForm;
import com.dchealth.medical.domain.form.Option;
import com.dchealth.medical.domain.form.PatientRecordDetailForm;
import com.dchealth.medical.domain.vo.*;
import com.dchealth.medical.mapper.PatientInfoMapper;
import com.dchealth.medical.mapper.PatientRecordFilesMapper;
import com.dchealth.medical.mapper.PatientRecordMapper;
import com.dchealth.medical.mapper.PatientRecordTemplateMapper;
import com.dchealth.medical.service.IEdcWorkService;
import com.dchealth.medical.service.IPatientInfoService;
import com.dchealth.medical.service.IPatientRecordService;
import com.dchealth.medical.utils.PatientUtils;
import com.dchealth.question.domain.QuestionRecord;
import com.dchealth.question.mapper.QuestionRecordMapper;
import com.dchealth.research.domain.ResearchInfo;
import com.dchealth.research.domain.constant.PropertyType;
import com.dchealth.research.mapper.ResearchInfoMapper;
import com.dchealth.system.domain.vo.SysAreaVo;
import com.dchealth.system.mapper.SysAreaMapper;
import com.dchealth.template.domain.vo.FMedicalInfoReq;
import com.google.gson.Gson;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFDrawing;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 患者档案Service业务层处理
 *
 * @author lchbu
 * @date 2022-09-15
 */
@Service
public class PatientRecordServiceImpl implements IPatientRecordService {
    private static final Logger log = LoggerFactory.getLogger(PatientRecordServiceImpl.class);

    @Autowired
    private PatientRecordMapper patientRecordMapper;

    @Autowired
    private IEdcWorkService iEdcWorkService;

    @Autowired
    private MedicalFormServiceImpl medicalFormService;

    @Autowired
    private DynamicCommonMapper dynamicCommonMapper;

    @Autowired
    private DynamicSql dynamicSql;

    @Autowired
    PatientRecordFilesMapper patientRecordFilesMapper;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private QuestionRecordMapper questionRecordMapper;
    @Autowired
    private ResearchInfoMapper researchInfoMapper;
    @Autowired
    private PatientRecordTemplateMapper patientRecordTemplateMapper;
    @Autowired
    private PatientInfoMapper patientInfoMapper;

    @Autowired
    private SysAreaMapper sysAreaMapper;

    @Autowired
    private IPatientInfoService patientInfoService;

    /**
     * 查询患者档案
     *
     * @param recordId 患者档案主键
     * @return 患者档案
     */
    @Override
    public PatientRecord selectPatientRecordByRecordId(Long recordId) {
        return patientRecordMapper.selectPatientRecordByRecordId(recordId);
    }

    /**
     * 查询患者档案列表
     *
     * @param patientRecord 患者档案
     * @return 患者档案
     */
    @Override
    public List<PatientRecord> selectPatientRecordList(PatientRecord patientRecord) {
        return patientRecordMapper.selectPatientRecordList(patientRecord);
    }

    /**
     * 新增患者档案
     *
     * @param patientRecord 患者档案
     * @return 结果
     */
    @Override
    public int insertPatientRecord(PatientRecord patientRecord) {
        patientRecord.setCreateTime(DateUtils.getNowDate());
        return patientRecordMapper.insertPatientRecord(patientRecord);
    }

    /**
     * 修改患者档案
     *
     * @param patientRecord 患者档案
     * @return 结果
     */
    @Override
    public int updatePatientRecord(PatientRecord patientRecord) {
        patientRecord.setUpdateTime(DateUtils.getNowDate());
        return patientRecordMapper.updatePatientRecord(patientRecord);
    }

    /**
     * 批量删除患者档案
     *
     * @param recordIds 需要删除的患者档案主键
     * @return 结果
     */
    @Override
    public int deletePatientRecordByRecordIds(Long[] recordIds) {
        return patientRecordMapper.deletePatientRecordByRecordIds(recordIds);
    }

    /**
     * 删除患者档案信息
     *
     * @param recordId 患者档案主键
     * @return 结果
     */
    @Override
    public int deletePatientRecordByRecordId(Long recordId) {
        return patientRecordMapper.deletePatientRecordByRecordId(recordId);
    }


    @Override
    public List<FollowMedicalFromTree> leftDetailTree(PatientRecord patientRecord) {

        List<PatientRecord> patientRecordList = patientRecordMapper.selectPatientRecordList(patientRecord);
        List<FollowMedicalFromTree> followMedicalFromTrees = new ArrayList<>();
        if (patientRecordList != null && patientRecordList.size() > 0) {
            for (PatientRecord record : patientRecordList) {
                FMedicalInfoReq vo = new FMedicalInfoReq();
                vo.setPatientId(record.getPatientId());
                vo.setFollowId(record.getFollowId());
                FollowMedicalFromTree followMedicalFromTree = iEdcWorkService.followMedicalFromTree(vo);
                if (followMedicalFromTree != null) {
                    followMedicalFromTrees.add(followMedicalFromTree);
                }
            }
        }
        return followMedicalFromTrees;
    }

    @Override
    public List<PatientRecordDetailForm> patientRecordDetail(List<PatientRecord> patientRecords) {
        List<PatientRecordDetailForm> result = new ArrayList<>();
        for (PatientRecord record : patientRecords) {

            PatientRecordDetailForm patientRecordDetailForm = new PatientRecordDetailForm();
            patientRecordDetailForm.setRecordId(record.getRecordId());
            List<MedicalFormVO> datas = medicalFormService.followForms(record.getFollowId(), record.getRecordId(), record.getFollowStatus(),
                    (formVOS, propertyVOS, propertyTypes) -> {
                        List<PatientRecordFiles> patientRecordFiles = patientRecordFilesMapper.selectPatientRecordFilesByRecordId(record.getRecordId());
                        CompletableFuture<Void> pcf = CompletableFuture.runAsync(() -> {
                            List<DynamicCommonProperty> t = new ArrayList<>();
                            if (propertyTypes != null && !propertyTypes.isEmpty()) {
                                DynamicForm dynamicForm = new DynamicForm();
                                dynamicForm.setRecordId(record.getRecordId());
                                dynamicForm.setType("1");
                                if (PropertyType.largeText(propertyTypes)) {//大文本
                                    dynamicForm.setSql(dynamicSql.buildSelectLargeText(record.getResearchId()));
                                    List<DynamicCommonProperty> largeTextList = dynamicCommonMapper.selectBySql(dynamicForm);
                                    if (largeTextList != null && !largeTextList.isEmpty())
                                        t.addAll(largeTextList);
                                }
                                if (PropertyType.text(propertyTypes)) {//小文本
                                    dynamicForm.setSql(dynamicSql.buildSelectText(record.getResearchId()));
                                    List<DynamicCommonProperty> textList = dynamicCommonMapper.selectBySql(dynamicForm);
                                    if (textList != null && !textList.isEmpty())
                                        t.addAll(textList);
                                }
                            }

                            Map<Long, List<String>> filesMap = null;
                            if (patientRecordFiles != null && !patientRecordFiles.isEmpty()) {
                                filesMap = patientRecordFiles.stream()
                                        .filter(f -> f.getPropertyId() != null)
                                        .collect(Collectors.groupingBy(PatientRecordFiles::getPropertyId,
                                                Collectors.mapping(PatientRecordFiles::getImageUrl, Collectors.toList())));
                            }

                            Map<Long, DynamicCommonProperty> dp = t.stream().collect(Collectors.toMap(DynamicCommonProperty::getPropertyId, item -> item));
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
                                            }
                                        }
                                    }
                                }
                            }
                        }, threadPoolTaskExecutor);

                        CompletableFuture<Void> fcf = CompletableFuture.runAsync(() -> {
                            if (formVOS != null && !formVOS.isEmpty()) {
                                if (patientRecordFiles != null && !patientRecordFiles.isEmpty()) {
                                    Map<Long, List<String>> filesMap = patientRecordFiles.stream()
                                            .filter(f -> f.getMedicalFormId() != null)
                                            .collect(Collectors.groupingBy(PatientRecordFiles::getMedicalFormId,
                                                    Collectors.mapping(PatientRecordFiles::getImageUrl, Collectors.toList())));
                                    if (filesMap != null && !filesMap.isEmpty()) {//图片
                                        formVOS.stream()
                                                .filter(f -> "2".equals(f.getFormType()))
                                                .forEach(f -> {
                                                    if (filesMap.containsKey(f.getMedicalFormId())) {
                                                        f.setImageUrls(filesMap.get(f.getMedicalFormId()));
                                                    }
                                                });
                                    }
                                }
                                if (formVOS.stream().anyMatch(m -> "1".equals(m.getDynamic()))) {
                                    DynamicForm dynamicForm = new DynamicForm();
                                    dynamicForm.setRecordId(record.getRecordId());
                                    dynamicForm.setType("1");
                                    dynamicForm.setSql(dynamicSql.buildSelectDynamic(record.getResearchId()));
                                    List<DynamicCommonProperty> dynamicList = dynamicCommonMapper.selectBySql(dynamicForm);
                                    if (dynamicList != null && !dynamicList.isEmpty()) {
                                        Map<Long, List<MedicalPropertyVO>> pMap = propertyVOS.stream().collect(Collectors.groupingBy(MedicalPropertyVO::getMedicalFormId));
                                        Map<Long, List<DynamicCommonProperty>> listMap = dynamicList.stream().collect(Collectors.groupingBy(DynamicCommonProperty::getFormId));
                                        formVOS.stream()
                                                .filter(f -> "1".equals(f.getDynamic()))
                                                .forEach(f -> {
                                                    if (listMap.containsKey(f.getMedicalFormId()) && pMap.containsKey(f.getMedicalFormId())) {
                                                        Map<Long, Integer> p = pMap.get(f.getMedicalFormId()).stream()
                                                                .collect(Collectors.toMap(MedicalPropertyVO::getId, MedicalPropertyVO::getSortNum));
                                                        List<DynamicCommonProperty> commonProperties = listMap.get(f.getMedicalFormId());
                                                        Map<Integer, List<String>> dynamicTable = commonProperties.stream()
                                                                .map(m -> {
                                                                    if (p.containsKey(m.getPropertyId())) {
                                                                        m.setColumnNumber(p.get(m.getPropertyId()));
                                                                    }
                                                                    return m;
                                                                })
                                                                .collect(Collectors.groupingBy(DynamicCommonProperty::getNumber))
                                                                .entrySet().stream()
                                                                .sorted(Map.Entry.comparingByKey())
                                                                .collect(Collectors.toMap(Map.Entry::getKey, item -> {
                                                                            List<DynamicCommonProperty> ds = item.getValue();
                                                                            if (ds == null || ds.isEmpty())
                                                                                return new ArrayList<>();
                                                                            ds.sort(Comparator.comparing(DynamicCommonProperty::getColumnNumber));
                                                                            return ds.stream().map(DynamicCommonProperty::getPropertyValue).collect(Collectors.toList());
                                                                        },
                                                                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                                                        f.setDynamicTable(dynamicTable);
                                                    }
                                                });
                                    }
                                }
                            }
                        }, threadPoolTaskExecutor);

                        CompletableFuture.allOf(pcf, fcf).join();
                    });
            patientRecordDetailForm.setMedicalFormVOS(datas);
            if ("1".equals(record.getQuestionStatus())) {
                patientRecordDetailForm.setQuestionRecordVOS(questionList(record.getRecordId()));
            }
            result.add(patientRecordDetailForm);
        }
        return result;
    }

    @Override
    public String batchRecordByResearchId(Long researchId) {
        String resultStr = "操作成功";
        List<PatientRecord> insertRecord = new ArrayList<>();
        insertRecordJob(researchId, insertRecord);
        if (!insertRecord.isEmpty()) {
//            patientRecordMapper.batchInsertPatientRecord(insertRecord);
            resultStr = "操作成功,共插入" + insertRecord.size() + "条";
        }
        return resultStr;
    }


//    @Override
//    public String batchRecordByResearchId(Long researchId) {
//        String resultStr = "操作成功";
//        List<PatientRecord> insertRecord = new ArrayList<>();
//
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 50, 4, TimeUnit.SECONDS,
//                new ArrayBlockingQueue<>(10), new ThreadPoolExecutor.CallerRunsPolicy());
//        ResearchInfo ri = new ResearchInfo();
//        ri.setResearchStatus("1");
//        List<ResearchInfo> researchInfoList = researchInfoMapper.selectResearchInfoList(ri);
//        List<List<ResearchInfo>> splitList = SplitListUtils.splitList(researchInfoList, 10);
//        //记录单个任务的执行次数
//        CountDownLatch countDownLatch = new CountDownLatch(splitList.size());
//        for (List<ResearchInfo> splist : splitList) {
//            threadPoolExecutor.execute(new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    for(int i=0;i<splist.size();i++) {
//                        ResearchInfo r = splist.get(i);
//                        insertRecordJob(r.getId(),insertRecord);
//                    }
//                }
//            }));
//            // 任务个数 - 1, 直至为0时唤醒await()
//            countDownLatch.countDown();
//        }
//        try {
//            // 让当前线程处于阻塞状态，直到锁存器计数为零
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            throw new NullPointerException(e.getMessage());
//        }finally {
//            threadPoolExecutor.shutdown(); //线程使用完毕后需要关团
//        }
//
//        //当线程池完成的线程数等于线程池中的总线程数
//        boolean allThreadsIsDone = threadPoolExecutor.getTaskCount() == threadPoolExecutor.getCompletedTaskCount();
//        int u=0;
//        if (allThreadsIsDone) {
//            if (!insertRecord.isEmpty() && u==0){
//            patientRecordMapper.batchInsertPatientRecord(insertRecord);
//                resultStr = "操作成功,共插入"+insertRecord.size()+"条";
//                u++;
//            }
//        }
//        while (!allThreadsIsDone) {
//            allThreadsIsDone = threadPoolExecutor.getTaskCount() == threadPoolExecutor.getCompletedTaskCount();
//            if (allThreadsIsDone) {
//                if (!insertRecord.isEmpty() && u==0){
//            patientRecordMapper.batchInsertPatientRecord(insertRecord);
//                    resultStr = "操作成功,共插入"+insertRecord.size()+"条";
//                    u++;
//                }
//            }
//        }
//        return resultStr;
//    }

    public void insertRecordJob(Long researchId, List<PatientRecord> listTotal) {
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(researchId);
        if (researchInfo != null && "1".equals(researchInfo.getResearchStatus())) {
            long TimeNow = System.currentTimeMillis();
            Date now = new Date(TimeNow);
            PatientRecordTemplate pt = new PatientRecordTemplate();
            pt.setResearchId(researchId);
            List<PatientRecordTemplate> patientRecordTemplates = patientRecordTemplateMapper.selectPatientRecordTemplateList(pt);
            if (patientRecordTemplates.size() > 0) {
                List<PatientRecord> insertList = new ArrayList<>();
                patientRecordTemplates.sort(Comparator.comparing(PatientRecordTemplate::getFollowNum));
                Map<Long, PatientRecordTemplate> maps = patientRecordTemplates.stream().collect(Collectors.toMap(PatientRecordTemplate::getFollowId, Function.identity()));
                List<PatientRecord> patientRecords = patientRecordMapper.selectOnePatientMaxRecordByResearchId(researchInfo.getId());
                patientRecords.forEach(x -> {
                    if (!maps.containsKey(x.getFollowId())) {
                        return;
                    }
                    if (x.getLockVisitTime() == null) {
                        return;
                    }
                    PatientRecordTemplate recordTemplate = maps.get(x.getFollowId());
                    //说明是基线,会创建随访1的PatientRecord记录
                    if (recordTemplate.getFollowNum() == 0) {
                        handleBaseLine(listTotal,insertList, researchInfo, now, patientRecordTemplates, x, recordTemplate);
                    } else {
                        handleFollowUp(listTotal,insertList, researchInfo, now, patientRecordTemplates, x, recordTemplate);
                    }
                });
                if(insertList.size()>0){
                    patientRecordMapper.batchInsertPatientRecord(insertList);
                }
            }
        }
    }

    private void handleFollowUp(List<PatientRecord> listTotal, List<PatientRecord> insertList, ResearchInfo researchInfo, Date now,
                                List<PatientRecordTemplate> patientRecordTemplates, PatientRecord x,
                                PatientRecordTemplate recordTemplate) {
        if ("1".equals(recordTemplate.getFollowFrequency())) {//限制频次
            if (recordTemplate.getFollowTotal() == null || recordTemplate.getFollowTotal() < 1) {
                log.info("生成随访计划,队列ID{}随访规则ID{}followTotal没有值", researchInfo.getId(), recordTemplate.getFollowId());
                return;
            }
            x.setStageAddNum(x.getStageAddNum() + 1);
            if (x.getStageNum().intValue() == recordTemplate.getFollowTotal().intValue()) {//完成当前随访节点进入下一个随访节点
                int index = patientRecordTemplates.indexOf(recordTemplate);
                if (index + 1 < patientRecordTemplates.size()) {
                    if (isCreateRecord(now, x, patientRecordTemplates.get(index + 1))) {
                        listTotal.add(new PatientRecord(x, patientRecordTemplates.get(index + 1)));
                        insertList.add(new PatientRecord(x, patientRecordTemplates.get(index + 1)));
                    }
                } else {
                    //后续没有配置的随访计划
                    PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(x.getPatientId());
                    patientInfo.setIsEndCreateRecord("1");
                    patientInfoMapper.updatePatientInfo(patientInfo);
                }
            } else if (x.getStageNum().intValue() < recordTemplate.getFollowTotal().intValue()) {//未完成当前随访节点继续生成当前随访节点计划
                if (isCreateRecord(now, x, recordTemplate)) {
                    listTotal.add(new PatientRecord(x, recordTemplate));
                    insertList.add(new PatientRecord(x, recordTemplate));
                }
            }
        } else if ("2".equals(recordTemplate.getFollowFrequency())) {//不限频次
            x.setStageAddNum(x.getStageAddNum() + 1);
            //没设置次数且间隔为0天
            if (recordTemplate.getFollowTotal() == null && recordTemplate.getFollowInterval() == 0) {
                int index = patientRecordTemplates.indexOf(recordTemplate);
                if (index + 1 < patientRecordTemplates.size()) {
                    if (isCreateRecord(now, x, patientRecordTemplates.get(index + 1))) {
                        listTotal.add(new PatientRecord(x, patientRecordTemplates.get(index + 1)));
                        insertList.add(new PatientRecord(x, patientRecordTemplates.get(index + 1)));
                    }
                }
            } else {
                if (isCreateRecord(now, x, recordTemplate)) {
                    listTotal.add(new PatientRecord(x, recordTemplate));
                    insertList.add(new PatientRecord(x, recordTemplate));
                }
            }
        }
    }

    private void handleBaseLine(List<PatientRecord> listTotal,List<PatientRecord> insertList, ResearchInfo researchInfo, Date now,
                                List<PatientRecordTemplate> patientRecordTemplates, PatientRecord x,
                                PatientRecordTemplate recordTemplate) {
        if (patientRecordTemplates.size() > 1) {
            // 设置随访01
            recordTemplate = patientRecordTemplates.get(1);
        }
        x.setStageAddNum(1);
        if ("1".equals(recordTemplate.getFollowFrequency())) {
            //限制频次
            if (recordTemplate.getFollowTotal() == null || recordTemplate.getFollowTotal() < 1) {
                log.info("生成随访计划,队列ID{}随访规则ID{}followTotal没有值", researchInfo.getId(), recordTemplate.getFollowId());
                return;
            }
            if (isCreateRecord(now, x, recordTemplate)) {
                listTotal.add(new PatientRecord(x, recordTemplate));
                insertList.add(new PatientRecord(x, recordTemplate));
            }
        } else if ("2".equals(recordTemplate.getFollowFrequency())) {//不限频次
            if (isCreateRecord(now, x, recordTemplate)) {
                listTotal.add(new PatientRecord(x, recordTemplate));
                insertList.add(new PatientRecord(x, recordTemplate));
            }
        }
    }


    //判断生成的下一个随访时间是否在当前时间之前,如果是，可以生成
    public boolean isCreateRecord(Date now, PatientRecord patientRecord, PatientRecordTemplate prt) {
        if (patientRecord == null || prt == null || patientRecord.getLockVisitTime() == null) {
            return false;
        }
        Date nextDate = null;
        // 根据单位计算下次随访日期
        String unit = prt.getFollowIntervalUnit();
        int interval = prt.getFollowInterval() != null ? prt.getFollowInterval().intValue() : 0;

        if ("1".equals(unit)) {
            // 按天
            nextDate = DateUtils.addDays(patientRecord.getLockVisitTime(), interval);
        } else if ("2".equals(unit)) {
            // 按周
            nextDate = DateUtils.addWeeks(patientRecord.getLockVisitTime(), interval);
        } else if ("3".equals(unit)) {
            // 按月
            nextDate = DateUtils.addMonths(patientRecord.getLockVisitTime(), interval);
        } else {
            // 未知单位，直接返回 false
            return false;
        }

        if (nextDate == null) {
            return false;
        }

        // 如果今天是生成日或者已经过了生成日
        if (DateUtils.isSameDay(now, nextDate) || now.after(nextDate)) {
            return true;
        }

        return false;
    }

    public List<QuestionRecordVO> questionList(Long recordId) {
        List<QuestionRecord> questionRecords = questionRecordMapper.selectQuestionRecordByRecordId(recordId);
        if (questionRecords != null && !questionRecords.isEmpty()) {
            return questionRecords.stream().map(QuestionRecordVO::new).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void exportData(HttpServletResponse response, Long researchId, List<Long> patientIds) {
        dealExportData(response, researchId, patientIds);
    }

    public void edcExportData(HttpServletResponse response, Long researchId, List<Long> patientIds) {
        Workbook workbook = new SXSSFWorkbook(1000);
        try {

            // 创建sheet，填充数据
            edcMakeExcelSheetByFollowNode2(researchId, workbook, patientIds);
            // response处理
            buildFileResponse(response);
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            log.error("Download error:{}", e);
            throw new ServiceException("Download template fail");
        } finally {
            IOUtils.closeQuietly(workbook);
        }
    }

    private void dealExportData(HttpServletResponse response, Long researchId, List<Long> patientIds) {

        Workbook workbook = new SXSSFWorkbook(1000);
        try {

            // 创建sheet，填充数据
            makeExcelSheetByFollowNode2(researchId, workbook, patientIds);
            // response处理
            buildFileResponse(response);
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            log.error("Download error:{}", e);
            throw new ServiceException("Download template fail");
        } finally {
            IOUtils.closeQuietly(workbook);
        }
    }

    public void edcMakeExcelSheetByFollowNode2(Long researchId, Workbook workbook, List<Long> patientIds) {
        List<PatientRecordTemplate> patientRecordTemplateList =
                patientRecordTemplateMapper.selectPatientRecordTemplateByResearchId(researchId);
        if (CollectionUtils.isEmpty(patientRecordTemplateList)) {
            throw new ServiceException("该队列随访节点为空,请检查");
        }

        // 查询队列的表单值
        List<PatientMedicalPropertyResVO> resText = patientRecordTemplateMapper.selectPatientMedicalPropertyRes(researchId, "res_text_" + researchId, patientIds, null);
        List<PatientMedicalPropertyResVO> resTextExam = patientRecordTemplateMapper.selectPatientMedicalPropertyExamRes(researchId, "res_text_" + researchId, patientIds, null);
        List<PatientMedicalPropertyResVO> resLargeText = patientRecordTemplateMapper.selectPatientMedicalPropertyRes(researchId, "res_large_text_" + researchId, patientIds, null);
        List<PatientMedicalPropertyResVO> resDynamic = patientRecordTemplateMapper.selectPatientMedicalPropertyRes(researchId, "res_dynamic_" + researchId, patientIds, null);

        resText.addAll(resLargeText);
        resText.addAll(resDynamic);
        resText.addAll(resTextExam);


        Map<Long, List<PatientMedicalPropertyResVO>> followIdMap = resText.stream().filter(patientMedicalPropertyResVO -> {
            if ("0".equals(patientMedicalPropertyResVO.getFollowNum())) {
                return true;
            } else if ("4".equals(patientMedicalPropertyResVO.getEdcStatus())&&"2".equals(patientMedicalPropertyResVO.getType())) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.groupingBy(PatientMedicalPropertyResVO::getFollowId));

        log.info("基线和随访锁库的答案_followIdMap:{}", JSON.toJSONString(followIdMap));
//        Map<Long, List<PatientMedicalPropertyResVO>> followIdMap = resText.stream().collect(Collectors.groupingBy(PatientMedicalPropertyResVO::getFollowId));
//        Map<Long, List<PatientMedicalPropertyResVO>> followIdMap2 = resText.stream().collect(Collectors.groupingBy(PatientMedicalPropertyResVO::getRecordId));

        // 查询患者基础信息
        PatientInfo info = new PatientInfo();
        info.setResearchId(researchId);
        if (CollectionUtils.isNotEmpty(patientIds)) {
            info.setPatientIds(patientIds);
        }
        info.setEnrollmentHandlerStatus("3");
        List<PatientEnrollmentVo> list = patientInfoService.selectExcelEnrollmentPatientInfoList(info);
        Map<Long, PatientEnrollmentVo> patientInfoMap = list.stream().collect(Collectors.toMap(
                PatientEnrollmentVo::getPatientId, Function.identity(), (oldValue, newValue) -> newValue));

        //查询省市县字典项信息
        List<SysAreaVo> sysAreaVos = sysAreaMapper.selectAreaFullNameList();

        //第一层，循环周期模板
        for (PatientRecordTemplate patientRecordTemplate : patientRecordTemplateList) {

            // 获取表单信息+编导里的属性信息
            List<MedicalFormVO> formVOListFromDB = getMedicalFormVO(patientRecordTemplate);
            if (CollectionUtils.isEmpty(formVOListFromDB)) {
                return;
            }
            //过滤无效的表单
            formVOListFromDB = formVOListFromDB.stream()
                    .filter(e -> !(CollectionUtils.isEmpty(e.getChildrens()) && CollectionUtils.isEmpty(e.getPropertyVOS()) && CollectionUtils.isEmpty(e.getPropertyExamVOS())))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(formVOListFromDB)) {
                return;
            }
            if (patientRecordTemplate.getFollowNum()==0) {
                doItsExecl(workbook, followIdMap, patientInfoMap, sysAreaVos, patientRecordTemplate, "基线", formVOListFromDB);
                continue;
            }else {
                MedicalFormVO medicalFormVO = new MedicalFormVO();
                medicalFormVO.setMedicalCode("keyForm");
                medicalFormVO.setMedicalName("随访信息");
                medicalFormVO.setFormType("0");
                medicalFormVO.setDynamic("0");
                MedicalPropertyVO submitTimeVo = new MedicalPropertyVO();
                submitTimeVo.setId(-0001L);
                submitTimeVo.setPropertyCode(BaseInfoCodeNameEnum.SUBMIT_DATE.getCode());
                submitTimeVo.setPropertyName("随访提交时间");
                submitTimeVo.setPropertyType("Input");
                submitTimeVo.setAttribute("{\"propertyName\": \"随访提交时间\",\"tag\": \"el-input\",\"required\": true," +
                        "\"endType\": \"\"}");
                List<MedicalPropertyVO> propertyVOS = new ArrayList<>();
                propertyVOS.add(submitTimeVo);


                MedicalPropertyVO submitName = new MedicalPropertyVO();
                submitName.setId(-0002L);
                submitName.setPropertyCode(BaseInfoCodeNameEnum.FOLLOW_NAME.getCode());
                submitName.setPropertyName("随访名称");
                submitName.setPropertyType("Input");
                submitName.setAttribute("{\"propertyName\": \"随访名称\",\"tag\": \"el-input\",\"required\": true," +
                        "\"endType\": \"\"}");
                propertyVOS.add(submitName);
                medicalFormVO.setPropertyVOS(propertyVOS);
                formVOListFromDB.add(medicalFormVO);

            }
            Long followId = patientRecordTemplate.getFollowId();
            List<PatientMedicalPropertyResVO> resTextByFollowId = followIdMap.get(followId);
            if (org.springframework.util.CollectionUtils.isEmpty(resTextByFollowId)) {
                continue;
            }
            //同一随访聚合
            TreeMap<Long, List<PatientMedicalPropertyResVO>> followInstanceAnswerListMap = resTextByFollowId.stream()
                    .sorted(Comparator.comparing(PatientMedicalPropertyResVO::getRecordId))
                    //收集成treeMap
                    .collect(Collectors.groupingBy(PatientMedicalPropertyResVO::getRecordId, TreeMap::new, Collectors.toList()));

            Map<String, String> sheetMap = new HashMap<>();
            List<MedicalFormVO> finalFormVOListFromDB = formVOListFromDB;
            followInstanceAnswerListMap.forEach((recordId, followInstancesAnswerList) -> {

                String followName = followInstancesAnswerList.get(0).getFollowName();
                if (sheetMap.get(followName) != null) {
                    log.info("已经有患者创建了对应的sheet页, 当前患者只需加载数据即可!");
                }
                sheetMap.put(followName, followName);

                Map<Long, List<PatientMedicalPropertyResVO>> followIdMap2 = new HashMap<>();
                // 每一个人的一个随访 recordid
                followIdMap2.put(followId, followInstancesAnswerList);
                Map<Long, PatientEnrollmentVo> patientInfoMap2 = new HashMap<>();
                for (PatientMedicalPropertyResVO patientMedicalPropertyResVO : followInstancesAnswerList) {
                    PatientEnrollmentVo patientEnrollmentVo = patientInfoMap.get(patientMedicalPropertyResVO.getPatientId());
                    // 清理多余用户
                    patientInfoMap2.put(patientMedicalPropertyResVO.getPatientId(), patientEnrollmentVo);
                }
                doItsExecl(workbook, followIdMap2, patientInfoMap2, sysAreaVos, patientRecordTemplate, followName, finalFormVOListFromDB);
            });
        }
    }


    private void doItsExecl(Workbook workbook, Map<Long, List<PatientMedicalPropertyResVO>> followIdMap, Map<Long, PatientEnrollmentVo> patientInfoMap,
                            List<SysAreaVo> sysAreaVos, PatientRecordTemplate patientRecordTemplate, String followName, List<MedicalFormVO> formVOListFromDB) {
        //拼装第一列关键字段
        List<MedicalFormVO> allFormVOs = buildKeyCell();
        allFormVOs.addAll(formVOListFromDB);//allFormList 所有的表单数据

        Sheet sheet = workbook.getSheet(followName);
        boolean keng = true;
        if (Objects.isNull(sheet)) {
            keng = false;
            sheet = workbook.createSheet(followName);
        }

        CellStyle formNameCellStyle = createCellStyle(workbook, 0); //第一行
        //第三行绿色样式
        CellStyle greenCellStyle = createCellStyle(workbook, 2);
        //必填项样式
        CellStyle requiredStyle = createCellStyle(workbook, -1);
        //批注
        SXSSFDrawing drawing = (SXSSFDrawing) sheet.createDrawingPatriarch();
        //创建前三行
        Row firstLineRow = sheet.getRow(0);
        Row secondRow = sheet.getRow(1);
        Row thirdRow = sheet.getRow(2);
        if (Objects.isNull(firstLineRow)) {
            firstLineRow = sheet.createRow(0);
        }
        if (Objects.isNull(secondRow)) {
            secondRow = sheet.createRow(1);
        }
        if (Objects.isNull(thirdRow)) {
            thirdRow = sheet.createRow(2);
        }
        //行的索引
        int firstCellIndex = 0;
        int secondCellIndex = 0;
        // 列的索引 todo 是不是列的索引被多个用户重置了, 需要单用户测试相关的东西
        AtomicInteger cellIndexAtomic = new AtomicInteger(0);
            // 创建出excel模板，组装前三行（表单，子表单，属性行）
        assembleExcelInfo(allFormVOs, sheet, firstLineRow, secondRow, thirdRow, formNameCellStyle, firstCellIndex
                    , secondCellIndex, cellIndexAtomic, workbook, greenCellStyle, requiredStyle, drawing,keng);

        // 按列处理单元格格式，创建样式，还没用
        CellStyle dateStyle = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        dateStyle.setDataFormat(format.getFormat("yyyy-mm-dd"));
        CellStyle defaultStyle = workbook.createCellStyle();
        defaultStyle.setDataFormat(format.getFormat("@"));

        // Excel填值的cell进行格式化，方便数据按DB格式填写和存储到DB
        AtomicInteger sheetHiddenIndex = new AtomicInteger(1);
        formateExcelFillValueCell2(workbook, sysAreaVos, sheetHiddenIndex, allFormVOs, sheet, dateStyle, defaultStyle,
                followIdMap.get(patientRecordTemplate.getFollowId()), patientInfoMap);
    }


    public void makeExcelSheetByFollowNode2(Long researchId, Workbook workbook, List<Long> patientIds) {
        List<PatientRecordTemplate> patientRecordTemplateList =
                patientRecordTemplateMapper.selectPatientRecordTemplateByResearchId(researchId);

        if (CollectionUtils.isEmpty(patientRecordTemplateList)) {
            throw new ServiceException("该队列随访节点为空,请检查");
        }

        // 查询队列的表单值
        List<PatientMedicalPropertyResVO> resText = patientRecordTemplateMapper.selectPatientMedicalPropertyRes(researchId, "res_text_" + researchId, patientIds, null);
        List<PatientMedicalPropertyResVO> resLargeText = patientRecordTemplateMapper.selectPatientMedicalPropertyRes(researchId, "res_large_text_" + researchId, patientIds, null);
        List<PatientMedicalPropertyResVO> resDynamic = patientRecordTemplateMapper.selectPatientMedicalPropertyRes(researchId, "res_dynamic_" + researchId, patientIds, null);

        resText.addAll(resLargeText);
        resText.addAll(resDynamic);
        if (CollectionUtils.isNotEmpty(resText)) {
            throw new ServiceException("队列没有查询到随访表单值");
        }

        Map<Long, List<PatientMedicalPropertyResVO>> followIdMap = resText.stream().collect(Collectors.groupingBy(PatientMedicalPropertyResVO::getFollowId));

        // 查询患者基础信息
        PatientInfo info = new PatientInfo();
        info.setResearchId(researchId);
        List<PatientEnrollmentVo> list = patientInfoService.selectEnrollmentPatientInfoList(info);
        Map<Long, PatientEnrollmentVo> patientInfoMap = list.stream().collect(Collectors.toMap(
                PatientEnrollmentVo::getPatientId, Function.identity(), (oldValue, newValue) -> newValue));

        //查询省市县字典项信息
        List<SysAreaVo> sysAreaVos = sysAreaMapper.selectAreaFullNameList();

        //第一层，循环周期模板
        for (PatientRecordTemplate patientRecordTemplate : patientRecordTemplateList) {
            // 获取表单信息+编导里的属性信息
            List<MedicalFormVO> formVOListFromDB = getMedicalFormVO(patientRecordTemplate);
            // 基本信息加一列 所属中心
//            dealDept(formVOListFromDB);

            // 基本信息列要在每个sheet页中存在
            /*if (basicInfo == null || !basicInfo.getMedicalCode().equals("baseRegiter")) {
                basicInfo = buildBasicInfoForSheet(formVOListFromDB);
            }
            Set<String> stringSet = formVOListFromDB.stream().map(MedicalFormVO::getMedicalCode).collect(Collectors.toSet());
            if(!stringSet.contains("baseRegiter")){
                formVOListFromDB.add(0,basicInfo);
            }*/
            if (CollectionUtils.isEmpty(formVOListFromDB)) {
                return;
            }
            //拼装第一列关键字段
            List<MedicalFormVO> allFormVOs = buildKeyCell();
//            List<MedicalFormVO> allFormVOs = new ArrayList<>();
            allFormVOs.addAll(formVOListFromDB);//allFormList 所有的表单数据

            Sheet sheet = workbook.createSheet(patientRecordTemplate.getFollowName());


            CellStyle formNameCellStyle = createCellStyle(workbook, 0); //第一行
            //第三行绿色样式
            CellStyle greenCellStyle = createCellStyle(workbook, 2);
            //必填项样式
            CellStyle requiredStyle = createCellStyle(workbook, -1);
            //批注
            SXSSFDrawing drawing = (SXSSFDrawing) sheet.createDrawingPatriarch();

            //创建前三行
            Row firstLineRow = sheet.createRow(0);
            Row secondRow = sheet.createRow(1);
            Row thirdRow = sheet.createRow(2);
            //行的索引
            int firstCellIndex = 0;
            int secondCellIndex = 0;
            // 列的索引
            AtomicInteger cellIndexAtomic = new AtomicInteger(0);
            // 创建出excel模板，组装前三行（表单，子表单，属性行）
            assembleExcelInfo(allFormVOs, sheet, firstLineRow, secondRow, thirdRow, formNameCellStyle, firstCellIndex
                    , secondCellIndex, cellIndexAtomic, workbook, greenCellStyle, requiredStyle, drawing,false);

            // 按列处理单元格格式，创建样式，还没用
            CellStyle dateStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            dateStyle.setDataFormat(format.getFormat("yyyy-mm-dd"));
            CellStyle defaultStyle = workbook.createCellStyle();
            defaultStyle.setDataFormat(format.getFormat("@"));

            // Excel填值的cell进行格式化，方便数据按DB格式填写和存储到DB
            AtomicInteger sheetHiddenIndex = new AtomicInteger(1);
            formateExcelFillValueCell2(workbook, sysAreaVos, sheetHiddenIndex, allFormVOs, sheet, dateStyle, defaultStyle,
                    followIdMap.get(patientRecordTemplate.getFollowId()), patientInfoMap);
        }
    }

    private List<MedicalFormVO> buildKeyCell() {
        List<MedicalFormVO> ls = new ArrayList<>();
        MedicalFormVO medicalFormVO = new MedicalFormVO();
        medicalFormVO.setMedicalCode("keyForm");
        medicalFormVO.setMedicalName("患者基本信息");
        medicalFormVO.setFormType("0");
        medicalFormVO.setDynamic("0");

        MedicalPropertyVO nameMedicalPropertyVO = new MedicalPropertyVO();
        nameMedicalPropertyVO.setId(-1L);
        nameMedicalPropertyVO.setPropertyCode(BaseInfoCodeNameEnum.NAME.getCode());
        nameMedicalPropertyVO.setPropertyName("患者姓名");
        nameMedicalPropertyVO.setPropertyType("Input");
        nameMedicalPropertyVO.setAttribute("{\"propertyName\": \"患者姓名\",\"tag\": \"el-input\",\"required\": true," +
                "\"endType\": \"\"}");

        MedicalPropertyVO sexMedicalPropertyVO = new MedicalPropertyVO();
        sexMedicalPropertyVO.setId(-2L);
        sexMedicalPropertyVO.setPropertyCode(BaseInfoCodeNameEnum.SEX.getCode());
        sexMedicalPropertyVO.setPropertyName("患者性别");
        sexMedicalPropertyVO.setPropertyType("radio");
        sexMedicalPropertyVO.setAttribute("{\"propertyName\": \"患者性别\",\"tag\": \"el-input\",\"required\": true," +
                "\"endType\": \"\"}");

        MedicalPropertyVO phoneMedicalPropertyVO = new MedicalPropertyVO();
        phoneMedicalPropertyVO.setId(-3L);
        phoneMedicalPropertyVO.setPropertyCode(BaseInfoCodeNameEnum.MOBILE1.getCode());
        phoneMedicalPropertyVO.setPropertyName("手机号");
        phoneMedicalPropertyVO.setPropertyType("Input");
        phoneMedicalPropertyVO.setAttribute("{\"propertyName\": \"手机号\",\"tag\": \"el-input\",\"required\": true," +
                "\"endType\": \"\"}");

        MedicalPropertyVO idCardMedicalPropertyVO = new MedicalPropertyVO();
        idCardMedicalPropertyVO.setId(-4L);
        idCardMedicalPropertyVO.setPropertyCode(BaseInfoCodeNameEnum.ID_CARD.getCode());
        idCardMedicalPropertyVO.setPropertyName("身份证号");
        idCardMedicalPropertyVO.setPropertyType("Input");
        idCardMedicalPropertyVO.setAttribute("{\"propertyName\": \"身份证号\",\"tag\": \"el-input\",\"required\": true," +
                "\"endType\": \"\"}");


        MedicalPropertyVO birthdayMedicalPropertyVO = new MedicalPropertyVO();
        birthdayMedicalPropertyVO.setId(-5L);
        birthdayMedicalPropertyVO.setPropertyCode(BaseInfoCodeNameEnum.DATE_OF_BIRTH.getCode());
        birthdayMedicalPropertyVO.setPropertyName("出生日期");
        birthdayMedicalPropertyVO.setPropertyType("Input");
        birthdayMedicalPropertyVO.setAttribute("{\"propertyName\": \"出生日期\",\"tag\": \"el-input\",\"required\": true," +
                "\"endType\": \"\"}");
        MedicalPropertyVO personIdMedicalPropertyVO = new MedicalPropertyVO();
        personIdMedicalPropertyVO.setId(-6L);
        personIdMedicalPropertyVO.setPropertyCode(BaseInfoCodeNameEnum.PERSON_ID.getCode());
        personIdMedicalPropertyVO.setPropertyName("就诊号");
        personIdMedicalPropertyVO.setPropertyType("Input");
        personIdMedicalPropertyVO.setAttribute("{\"propertyName\": \"就诊号\",\"tag\": \"el-input\",\"required\": true," +
                "\"endType\": \"\"}");

        MedicalPropertyVO timeMedicalPropertyVO = new MedicalPropertyVO();
        timeMedicalPropertyVO.setId(-7L);
        timeMedicalPropertyVO.setPropertyCode(BaseInfoCodeNameEnum.ENROLLMENT_TIME.getCode());
        timeMedicalPropertyVO.setPropertyName("入组时间");
        timeMedicalPropertyVO.setPropertyType("Input");
        timeMedicalPropertyVO.setAttribute("{\"propertyName\": \"入组时间\",\"tag\": \"el-input\",\"required\": true," +
                "\"endType\": \"\"}");
        List<MedicalPropertyVO> propertyVOS = new ArrayList<>();
        propertyVOS.add(nameMedicalPropertyVO);
        propertyVOS.add(sexMedicalPropertyVO);
        propertyVOS.add(phoneMedicalPropertyVO);
        propertyVOS.add(idCardMedicalPropertyVO);
        propertyVOS.add(birthdayMedicalPropertyVO);
        propertyVOS.add(personIdMedicalPropertyVO);
        propertyVOS.add(timeMedicalPropertyVO);
        medicalFormVO.setPropertyVOS(propertyVOS);
        ls.add(medicalFormVO);
        return ls;
    }

    private void formateExcelFillValueCell2(Workbook workbook, List<SysAreaVo> sysAreaVos, AtomicInteger sheetHiddenIndex,
                                            List<MedicalFormVO> allFormVOs, Sheet sheet, CellStyle dateStyle,
                                            CellStyle defaultStyle, List<PatientMedicalPropertyResVO> patientMedicalPropertyResVOS,
                                            Map<Long, PatientEnrollmentVo> patientInfoMap) {
        Map map = new ConcurrentHashMap();

        List<String> areas = sysAreaVos.stream().map(SysAreaVo::getFullName).collect(Collectors.toList());
        String[] areaList = areas.toArray(new String[areas.size()]);

        Map<String, String> areaMap = sysAreaVos.stream().collect(Collectors.toMap(SysAreaVo::getAreaCode, SysAreaVo::getFullName));

        map.put("dateStyle", dateStyle);
        map.put("defaultStyle", defaultStyle);
        map.put("areaList", areaList);
        map.put("areaMap", areaMap);
        map.put("sheetHiddenIndex", sheetHiddenIndex);

        int rowIndex = 3;
        if (CollectionUtils.isEmpty(patientMedicalPropertyResVOS)) {
            patientMedicalPropertyResVOS = new ArrayList<>();
        }

        //构建500数据行，设置单元格格式，解决直接设置列格式不生效问题

        Row forthRow = sheet.getRow(rowIndex);
        while (!Objects.isNull(forthRow)) {
            rowIndex++;
            forthRow = sheet.getRow(rowIndex);
        }
        forthRow = sheet.createRow(rowIndex);
        AtomicInteger cellIndexAtomic = new AtomicInteger(0);

        int cellBegin = 0;
        //  同为第二层，填值部分数据格式化处理
        for (int i = 0; i < allFormVOs.size(); i++) {

            MedicalFormVO tempForm = allFormVOs.get(i);
            // 含有子表单的处理子表单
            List<MedicalFormVO> children = tempForm.getChildrens();
            if (i != 0) {
//                cellBegin = allFormVOs.get(i-1).getPropertyVOS().size();
                cellBegin = sheet.getRow(rowIndex).getLastCellNum();
            }
            if (CollectionUtils.isNotEmpty(children)) {
                for (MedicalFormVO childForm : children) {
                    buildFormatExcel2(workbook, sheet, cellIndexAtomic, childForm, rowIndex, forthRow, map, cellBegin, patientMedicalPropertyResVOS, patientInfoMap);
                }
            }
            // 表单属性处理
            buildFormatExcel2(workbook, sheet, cellIndexAtomic, tempForm, rowIndex, forthRow, map, cellBegin, patientMedicalPropertyResVOS, patientInfoMap);
        }
    }

    private void buildFormatExcel2(Workbook workbook, Sheet sheet, AtomicInteger cellIndexAtomic, MedicalFormVO formVO
            , int rowIndex, Row forthRow, Map map, int cellBegin, List<PatientMedicalPropertyResVO> patientMedicalPropertyResVOS,
                                   Map<Long, PatientEnrollmentVo> patientInfoMap) {
        List<MedicalPropertyVO> propertyVOS = formVO.getPropertyVOS();

        Map<String, List<MedicalPropertyVO>> dynamicTable = formVO.getDynamicTableNew();
        List<MedicalPropertyVO> tempPropertyVOS = null;

        if (Objects.nonNull(dynamicTable) && dynamicTable.size() > 0) {
            for (String key : dynamicTable.keySet()) {
                tempPropertyVOS = dynamicTable.get(key);
                this.formatCellStyle2(workbook, tempPropertyVOS, forthRow, cellIndexAtomic, sheet, map, cellBegin, rowIndex, patientMedicalPropertyResVOS, patientInfoMap);
            }
        } else {
            if (CollectionUtils.isEmpty(propertyVOS)) {
                return;
            }
            this.formatCellStyle2(workbook, propertyVOS, forthRow, cellIndexAtomic, sheet, map, cellBegin, rowIndex, patientMedicalPropertyResVOS, patientInfoMap);
        }

    }

    private void formatCellStyle2(Workbook workbook, List<MedicalPropertyVO> propertyVOS, Row forthRow,
                                  AtomicInteger cellIndexAtomic, Sheet sheet, Map map, int cellBegin, int rowIndex,
                                  List<PatientMedicalPropertyResVO> patientMedicalPropertyResVOS, Map<Long, PatientEnrollmentVo> patientInfoMap) {
        Map<String, List<PatientMedicalPropertyResVO>> patientIdMap = patientMedicalPropertyResVOS.stream().
                filter(patientMedicalPropertyResVO -> null != patientMedicalPropertyResVO.getPatientId()).
                collect(Collectors.groupingBy(patientMedicalPropertyResVO -> String.valueOf(patientMedicalPropertyResVO.getPatientId()) + "_" + patientMedicalPropertyResVO.getRecordId()));


        // 根据 recordId 分成 中心
        /*Map<Long, String> patientIdDeptNameMap = patientMedicalPropertyResVOS.stream().collect(
                Collectors.toMap(PatientMedicalPropertyResVO::getPatientId, PatientMedicalPropertyResVO::getDeptName, (k1, k2) -> k1));*/

//        Row row = sheet.createRow(3);

        short lastCellNum = sheet.getRow(rowIndex).getLastCellNum();
        cellBegin = lastCellNum == -1 ? 0 : lastCellNum;


        Set<Long> patientIdList = new HashSet<>();
        for (String key : patientIdMap.keySet()) {
            String[] keys = key.split("_");
            Long patientId = Long.valueOf(keys[0]);
            patientIdList.add(patientId);
            int begin = cellBegin;
            List<PatientMedicalPropertyResVO> patientMedicalPropertyResVOS1 = patientIdMap.get(key);

            // edc质检的数据这里处理有冲突, k1,k2->k1
            Map<String, PatientMedicalPropertyResVO> propertypeCodeMap = patientMedicalPropertyResVOS1.stream().
                    collect(Collectors.toMap(PatientMedicalPropertyResVO::getPropertyCode, Function.identity(), (k1, k2) -> k1));

            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }


            PatientEnrollmentVo patientEnrollmentVo = patientInfoMap.get(patientId);
            if (ObjUtil.isNotNull(patientEnrollmentVo)) {
                for (int j = 0; j < propertyVOS.size(); j++) {
                    MedicalPropertyVO property = propertyVOS.get(j);

                    String propertyType = property.getPropertyType();
                    if (PropertyType.UPLOAD.equalsIgnoreCase(propertyType)) {
                        continue; //不支持文件类型
                    }
                    Cell tCell = row.createCell(begin);
                    String propertyCode = property.getPropertyCode();
                    PatientMedicalPropertyResVO patientMedicalPropertyResVO = null != propertypeCodeMap.get(propertyCode) ?
                            propertypeCodeMap.get(propertyCode) : new PatientMedicalPropertyResVO();

                    if(propertyCode.equals(BaseInfoCodeNameEnum.NAME.getCode())){
                        tCell.setCellValue(patientEnrollmentVo.getPatientName());
                        begin++;
                        continue;
                    }

                    if (propertyCode.equals(BaseInfoCodeNameEnum.SEX.getCode())) {
                        tCell.setCellValue(PatientUtils.getSexName(patientEnrollmentVo.getSex()));
                        begin++;
                        continue;
                    }

                    if (propertyCode.equals(BaseInfoCodeNameEnum.MOBILE1.getCode())) {
                        tCell.setCellValue(patientEnrollmentVo.getMobile());
                        begin++;
                        continue;
                    }

                    if (propertyCode.equals(BaseInfoCodeNameEnum.ID_CARD.getCode())) {
                        tCell.setCellValue(patientEnrollmentVo.getIdCard());
                        begin++;
                        continue;
                    }

                    if (propertyCode.equals(BaseInfoCodeNameEnum.DATE_OF_BIRTH.getCode())) {
                        tCell.setCellValue(DateUtils.dateTime(patientEnrollmentVo.getBirthday()));
                        begin++;
                        continue;
                    }
                    if (propertyCode.equals(BaseInfoCodeNameEnum.PERSON_ID.getCode())) {
                        tCell.setCellValue(patientEnrollmentVo.getPersonId());
                        begin++;
                        continue;
                    }
                    if (propertyCode.equals(BaseInfoCodeNameEnum.ENROLLMENT_TIME.getCode())) {
                        tCell.setCellValue(DateUtils.dateTime(patientEnrollmentVo.getEnrollmentTime()));
                        begin++;
                        continue;
                    }
                    if (propertyCode.equals(BaseInfoCodeNameEnum.SUBMIT_DATE.getCode())) {
                        tCell.setCellValue(patientMedicalPropertyResVOS1.get(0).getFollowSubmitTime());
                        begin++;
                        continue;
                    }

                    if (propertyCode.equals(BaseInfoCodeNameEnum.FOLLOW_NAME.getCode())) {
                        tCell.setCellValue(patientMedicalPropertyResVOS1.get(0).getFollowName());
                        begin++;
                        continue;
                    }

                    // 把每一列变成文本格式
                    if ("Select,Checkbox,Radio".contains(propertyType)) {
                        String attribute = property.getAttribute();
                        JSONArray jsonArray = (JSONArray) JSONObject.parseObject(attribute).get("options");
                        List<String> values = new ArrayList();
                        if (jsonArray != null) {
                            for (Object obj : jsonArray) {
                                JSONObject json = (JSONObject) obj;
                                String label = json.get("label").toString();
                                values.add(label);
                                if (json.get("value").toString().equals(patientMedicalPropertyResVO.getPropertyValue())) {
                                    tCell.setCellValue(label);
                                }
                            }
//                        tCell.setCellValue(values.get(0));
                            //设置下拉单选
                            setDropDownBox(sheet, values.toArray(new String[values.size()]), 3, 500, begin, begin);
                        }
                        tCell.setCellStyle((CellStyle) map.get("defaultStyle"));
                    } else if ("Cascader".contains(propertyType)) {
                        List<String> resultOptionList = new ArrayList();
                        String attribute = property.getAttribute();
                        JSONArray jsonArray = (JSONArray) JSONObject.parseObject(attribute).get("options");

                        if (jsonArray != null) {
                            jsonArray.stream().forEach(optional -> {
                                List<String> values = new ArrayList();
                                Gson gson = new Gson();
                                JSONObject optionObj = (JSONObject) optional;
                                Option option = gson.fromJson(optionObj.toJSONString(), Option.class);
                                resultOptionList.addAll(binaryTreePaths(option, values));

                            });

                            if (CollectionUtils.isNotEmpty(resultOptionList)) {
                                tCell.setCellValue((null != patientMedicalPropertyResVO) ?
                                        patientMedicalPropertyResVO.getPropertyValue() : "");
                                //设置下拉单选
                                setDropDownBox(sheet, resultOptionList.toArray(new String[resultOptionList.size()]), 3, 500,
                                        begin, begin);
                            }
                        }
                        tCell.setCellStyle((CellStyle) map.get("defaultStyle"));
                    } else if ("Date".equalsIgnoreCase(propertyType)) {
                        sheet.setColumnWidth(begin, 3000);
                        tCell.setCellStyle((CellStyle) map.get("dateStyle"));
                        if (propertypeCodeMap.get(property.getPropertyCode()) != null) {
                            tCell.setCellValue(dateConvert(propertypeCodeMap.get(property.getPropertyCode()).getPropertyValue()));
                        }
                    /*tCell.setCellValue((null != patientMedicalPropertyResVO) ?
                            patientMedicalPropertyResVO.getPropertyValue() : "");*/
                    } else if ("Upload,ColorPicker".contains(propertyType)) {

                    } else if ("province".contains(propertyType)) {
//                tCell.setCellStyle((CellStyle) map.get("defaultStyle"));
                        Map<String, String> area = (Map<String, String>) map.get("areaMap");
                        String[] areaDictList = (String[]) map.get("areaList");
                        if (areaDictList.length > 0) {
                            AtomicInteger sheetHiddenIndex = (AtomicInteger) map.get("sheetHiddenIndex");

                            setLongHSSFValidation2((SXSSFWorkbook) workbook, areaDictList, (SXSSFSheet) sheet, 1,
                                    areaDictList.length, begin, sheetHiddenIndex);
                            String propertyValue = patientMedicalPropertyResVO.getPropertyValue();
                            if (com.dchealth.common.utils.StringUtils.isNotEmpty(propertyValue)) {
                                String[] areaFullCode = propertyValue.split(",");
                                tCell.setCellValue(area.get(areaFullCode[areaFullCode.length - 1]));
                            }
//                        sheetHiddenIndex.addAndGet(1);
                            map.put("sheetHiddenIndex", sheetHiddenIndex);
                        }
                    } else if ("timePicker".contains(propertyType)) {
                        tCell.setCellStyle((CellStyle) map.get("defaultStyle"));
                        tCell.setCellValue("10:00");
                    } else if ("YEARMONTHDAY".contains(propertyType)) {
                    /*tCell.setCellStyle((CellStyle) map.get("defaultStyle"));
                    String value = (null != patientMedicalPropertyResVO) ? patientMedicalPropertyResVO.getPropertyValue() : "";
                    String unit = PatientUtils.getSpecialNameFromType(patientMedicalPropertyResVO.getSpecialType());
                    tCell.setCellValue(value+unit);*/
                    } else {
                        tCell.setCellStyle((CellStyle) map.get("defaultStyle"));
                        tCell.setCellValue((null != patientMedicalPropertyResVO) ?
                                patientMedicalPropertyResVO.getPropertyValue() : "");
                    }
                    begin++;
                }
            }


            rowIndex++;
        }
        Set<Long> allPatientId = new HashSet<>(patientInfoMap.keySet());
        allPatientId.removeAll(patientIdList);
        //对患者基础信息做处理
        for (Long key : allPatientId) {
            int begin = cellBegin;
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }

            PatientEnrollmentVo patientEnrollmentVo = patientInfoMap.get(key);
            for (int j = 0; j < propertyVOS.size(); j++) {
                MedicalPropertyVO property = propertyVOS.get(j);

                String propertyType = property.getPropertyType();
                if (PropertyType.UPLOAD.equalsIgnoreCase(propertyType)) {
                    continue; //不支持文件类型
                }
                Cell tCell = row.createCell(begin);
                String propertyCode = property.getPropertyCode();


                if (propertyCode.equals(BaseInfoCodeNameEnum.NAME.getCode())) {
                    tCell.setCellValue(patientEnrollmentVo.getPatientName());
                    begin++;
                    continue;
                }

                if (propertyCode.equals(BaseInfoCodeNameEnum.SEX.getCode())) {
                    tCell.setCellValue(PatientUtils.getSexName(patientEnrollmentVo.getSex()));
                    begin++;
                    continue;
                }

                if (propertyCode.equals(BaseInfoCodeNameEnum.MOBILE1.getCode())) {
                    tCell.setCellValue(patientEnrollmentVo.getMobile());
                    begin++;
                    continue;
                }

                if (propertyCode.equals(BaseInfoCodeNameEnum.ID_CARD.getCode())) {
                    tCell.setCellValue(patientEnrollmentVo.getIdCard());
                    begin++;
                    continue;
                }

                if (propertyCode.equals(BaseInfoCodeNameEnum.DATE_OF_BIRTH.getCode())) {
                    tCell.setCellValue(DateUtils.dateTime(patientEnrollmentVo.getBirthday()));
                    begin++;
                    continue;
                }
                if (propertyCode.equals(BaseInfoCodeNameEnum.PERSON_ID.getCode())) {
                    tCell.setCellValue(patientEnrollmentVo.getPersonId());
                    begin++;
                    continue;
                }
                if (propertyCode.equals(BaseInfoCodeNameEnum.ENROLLMENT_TIME.getCode())) {
                    tCell.setCellValue(DateUtils.dateTime(patientEnrollmentVo.getEnrollmentTime()));
                    begin++;
                    continue;
                }
                begin++;
            }
            rowIndex++;
        }


    }

    /**
     * 目标:设置下拉选项，字典项：values 少于255行时使用
     * 添加数据有效性检查.
     *
     * @param sheet    要添加此检查的Sheet
     * @param firstRow 开始行
     * @param lastRow  结束行
     * @param firstCol 开始列
     * @param lastCol  结束列
     * @param values   有效性检查的下拉列表
     * @throws IllegalArgumentException 如果传入的行或者列小于0(< 0)或者结束行/列比开始行/列小
     */
    private static void setDropDownBox(Sheet sheet, String[] values, Integer firstRow, Integer lastRow,
                                       Integer firstCol, Integer lastCol) {
        if (firstRow < 0 || lastRow < 0 || firstCol < 0 || lastCol < 0 || lastRow < firstRow || lastCol < firstCol) {
            throw new IllegalArgumentException("Wrong Row or Column index : " + firstRow + ":" + lastRow + ":" + firstCol + ":" + lastCol);
        }
        if (sheet instanceof SXSSFSheet) {
            // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
            CellRangeAddressList regions = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
            // 数据有效性对象
            DataValidationHelper dataValidationHelper = sheet.getDataValidationHelper();
            DataValidationConstraint createExplicitListConstraint =
                    dataValidationHelper.createExplicitListConstraint(values);
            DataValidation createValidation = dataValidationHelper.createValidation(createExplicitListConstraint,
                    regions);
            if (createValidation instanceof XSSFDataValidation) {
                createValidation.setSuppressDropDownArrow(true);
                createValidation.setShowErrorBox(true);
            } else {
                createValidation.setSuppressDropDownArrow(false);
            }
            sheet.addValidationData(createValidation);
        } else if (sheet instanceof XSSFSheet) {
            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
            XSSFDataValidationConstraint dvConstraint =
                    (XSSFDataValidationConstraint) dvHelper.createExplicitListConstraint(values);
            CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
            XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
            validation.setSuppressDropDownArrow(true);
            validation.setShowErrorBox(true);
            sheet.addValidationData(validation);
        } else if (sheet instanceof HSSFSheet) {
            CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
            DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(values);
            DataValidation validation = new HSSFDataValidation(addressList, dvConstraint);
            validation.setSuppressDropDownArrow(true);
            validation.setShowErrorBox(true);
            sheet.addValidationData(validation);
        }

    }

    private void buildFileResponse(HttpServletResponse response) {

        response.reset();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = System.currentTimeMillis() + ".xlsx";
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    private List<MedicalFormVO> getMedicalFormVO(PatientRecordTemplate patientRecordTemplate) {
        List<MedicalFormVO> datas = medicalFormService.followForm(patientRecordTemplate.getFollowId(), (formVOS,
                                                                                                        propertyVOS,
                                                                                                        propertyTypes) -> {
            CompletableFuture<Void> fcf = CompletableFuture.runAsync(() -> {
                if (formVOS != null && !formVOS.isEmpty()) {
                    if (formVOS.stream().anyMatch(m -> "1".equals(m.getOpenMultiple()))) {

                        Map<Long, List<MedicalPropertyVO>> pMap =
                                propertyVOS.stream().collect(Collectors.groupingBy(MedicalPropertyVO::getMedicalFormId));

                        formVOS.stream().filter(f -> "1".equals(f.getOpenMultiple())).forEach(f -> {
                            if (pMap.containsKey(f.getMedicalFormId())) {
                                List<MedicalPropertyVO> ds = pMap.get(f.getMedicalFormId());
                                Map<String, List<MedicalPropertyVO>> map = new HashMap<>();
                                map.put("1", ds);
                                f.setDynamicTableNew(map);
                            }
                        });
                    }
                }
            }, threadPoolTaskExecutor);
            CompletableFuture.allOf(fcf).join();
        });
        return datas;
    }

    private CellStyle createCellStyle(Workbook wb, int rowIndex) {

        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        if (rowIndex == 2) {
            style.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font titleFont = wb.createFont();
            titleFont.setFontName("Arial");
            titleFont.setFontHeightInPoints((short) 12);
            titleFont.setBold(true);
            style.setFont(titleFont);
        } else if (rowIndex == -1) {
            //必填项，值
            style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font titleFont = wb.createFont();
            titleFont.setFontName("Arial");
            titleFont.setFontHeightInPoints((short) 12);
            titleFont.setBold(true);
            style.setFont(titleFont);
        } else {
            // 第一行
            Font titleFont = wb.createFont();
            titleFont.setFontName("Arial");
            titleFont.setFontHeightInPoints((short) 14);
            titleFont.setBold(true);
            style.setFont(titleFont);
        }
        return style;
    }

    /**
     * 创建前三行
     *
     * @param medicalFormVOList 每一个随访节点的表单-属性
     * @param sheet             sheet
     * @param firstRow          excel 第一行
     * @param secondRow         excel 第二行
     * @param thirdRow          excel 第三行
     * @param formNameCellStyle 表单行样式（第一行，第二行）
     * @param firstCellIndex
     * @param secondCellIndex   第二行列索引
     * @param cellIndexAtomic   列的索引 是否有影响?
     */
    private void assembleExcelInfo(List<MedicalFormVO> medicalFormVOList, Sheet sheet, Row firstRow, Row secondRow,
                                   Row thirdRow, CellStyle formNameCellStyle, int firstCellIndex, int secondCellIndex,
                                   AtomicInteger cellIndexAtomic, Workbook workbook, CellStyle greenCellStyle, CellStyle requiredStyle,
                                   SXSSFDrawing drawing,boolean keng) {
        //  第二层，遍历同一个周期配置的所有表单
        for (int i = 0; i < medicalFormVOList.size(); i++) {
            MedicalFormVO tempForm = medicalFormVOList.get(i);
            // 含有子表单的处理子表单
            List<MedicalFormVO> children = tempForm.getChildrens();
            if (CollectionUtils.isNotEmpty(children)) {
                //  第三层，遍历子表单
                for (MedicalFormVO childForm : children) {
                    //第二行：子表单处理
                    Cell cell = secondRow.getCell(secondCellIndex);
                    if (Objects.isNull(cell)) {
                         cell = secondRow.createCell(secondCellIndex);
                    }
                    cell.setCellValue(childForm.getMedicalName());
                    cell.setCellStyle(formNameCellStyle);
                    // 第三行：属性处理   &&  第四层，构建每个表单下的属性行
                    buildThirdLine(childForm, thirdRow, cellIndexAtomic, greenCellStyle, requiredStyle, drawing);
                    // 合并单元格
                    if (secondCellIndex < (cellIndexAtomic.get() - 1)) {
                        CellRangeAddress address = new CellRangeAddress(1, 1, secondCellIndex,
                                cellIndexAtomic.get() - 1);
                        // 合并单元格 todo 是否不用取所有的, 才可以合并??
                        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
                        if (CollectionUtils.isEmpty(mergedRegions) || (!mergedRegions.contains(address))) {
                            sheet.addMergedRegion(address);
                        }
                    }
                    secondCellIndex = cellIndexAtomic.get();
                }
            }
            // 第四层，构建每个表单下的属性行
            buildThirdLine(tempForm, thirdRow, cellIndexAtomic, greenCellStyle, requiredStyle, drawing);

            secondCellIndex = mergeCell(sheet, firstRow, formNameCellStyle, firstCellIndex, secondCellIndex,
                        cellIndexAtomic, tempForm);
            firstCellIndex = cellIndexAtomic.get();
        }
    }

    /**
     * 属性处理
     *
     * @param formVO          表单
     * @param thirdRow        行
     * @param cellIndexAtomic 列索引
     * @param greenCellStyle  可选择不填
     * @param requiredStyle   必填
     * @param drawing         提示信息
     */
    private void buildThirdLine(MedicalFormVO formVO, Row thirdRow, AtomicInteger cellIndexAtomic,
                                CellStyle greenCellStyle, CellStyle requiredStyle, SXSSFDrawing drawing) {
        List<MedicalPropertyVO> propertyVOS = formVO.getPropertyVOS();
        //  属性行构建
        buildAttributeLine(propertyVOS, thirdRow, cellIndexAtomic, greenCellStyle, requiredStyle, drawing);
        //todo 为啥把动态表单单独拎出来
        Map<String, List<MedicalPropertyVO>> dynamicTable = formVO.getDynamicTableNew();
        List<MedicalPropertyVO> tempPropertyVOS = null;
        if (Objects.nonNull(dynamicTable) && dynamicTable.size() > 0) {
            //第四层，构建每个表单下的属性行
            for (String key : dynamicTable.keySet()) {
                tempPropertyVOS = dynamicTable.get(key);
                buildAttributeLine(tempPropertyVOS, thirdRow, cellIndexAtomic, greenCellStyle, requiredStyle, drawing);
            }
        }
    }

    /**
     * 属性行构建:   按照属性创建列（单元格），加样式
     *
     * @param propertyVOS
     * @param thirdRow
     * @param cellIndexAtomic 列索引
     * @param greenCellStyle
     * @param requiredStyle
     * @param drawing
     */
    private void buildAttributeLine(List<MedicalPropertyVO> propertyVOS, Row thirdRow, AtomicInteger cellIndexAtomic,
                                    CellStyle greenCellStyle, CellStyle requiredStyle, SXSSFDrawing drawing) {
        if (CollectionUtils.isEmpty(propertyVOS)) {
            return;
        }
        //  第四层 遍历属性，构建第三行：属性行
        for (int j = 0; j < propertyVOS.size(); j++) {
            MedicalPropertyVO property = propertyVOS.get(j);
            if (PropertyType.UPLOAD.equalsIgnoreCase(property.getPropertyType())) {
                //附件类字段先不处理
                continue;
            }
            // 创建一个属性cell
            Cell tCell = thirdRow.getCell(cellIndexAtomic.get());
            if (Objects.isNull(tCell)) {
                tCell = thirdRow.createCell(cellIndexAtomic.get());
            }

            tCell.setCellValue(property.getPropertyName());
            String attribute = property.getAttribute();
            JSONObject json = JSONObject.parseObject(attribute);
            if (json != null && json.getBoolean("required")) {
                tCell.setCellStyle(requiredStyle);
            } else {
                tCell.setCellStyle(greenCellStyle);
            }
            if (Objects.isNull(tCell.getCellComment())) {
                    Comment comment = drawing.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, tCell.getColumnIndex(),
                            tCell.getRowIndex(), tCell.getColumnIndex() + 5, tCell.getRowIndex() + 6));
                    String commentStr = "{\"提示\":\"单元格格式请按示例格式化\",\"propertyId\":\"" + property.getId() + "\",\"medicalFormId"
                            + "\":\"" + property.getMedicalFormId() + "\",\"followId\":\"" + property.getFollowId() + "\"," +
                            "\"propertyType\":\"" + property.getPropertyType() + "\"}";
                    comment.setString(new XSSFRichTextString(commentStr));
                    tCell.setCellComment(comment);
            }
            cellIndexAtomic.addAndGet(1);
        }
    }

    /**
     * 合并单元格（表单和子表单行）
     *
     * @param sheet
     * @param firstRow
     * @param formNameCellStyle
     * @param firstCellIndex
     * @param secondCellIndex
     * @param cellIndexAtomic
     * @param tempForm
     * @return
     */
    private static int mergeCell(Sheet sheet, Row firstRow, CellStyle formNameCellStyle, int firstCellIndex,
                                 int secondCellIndex, AtomicInteger cellIndexAtomic, MedicalFormVO tempForm) {
        // 合并第二行（子表单）单元格
        if (secondCellIndex < (cellIndexAtomic.get() - 1)) {
            CellRangeAddress address = new CellRangeAddress(1, 1, secondCellIndex, cellIndexAtomic.get() - 1);
            List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
            if (CollectionUtils.isEmpty(mergedRegions)||(!mergedRegions.contains(address))) {
                sheet.addMergedRegion(address);
            }
        }
        secondCellIndex = cellIndexAtomic.get();
        //第一行：表单处理

        Cell cell = firstRow.getCell(firstCellIndex);
        if (Objects.isNull(cell)) {
            cell = firstRow.createCell(firstCellIndex);
            cell.setCellValue(tempForm.getMedicalName());
            cell.setCellStyle(formNameCellStyle);
        }
        // 合并第一行（表单）单元格
        if (firstCellIndex < (cellIndexAtomic.get() - 1)) {
            CellRangeAddress cellAddresses = new CellRangeAddress(0, 0, firstCellIndex, cellIndexAtomic.get() - 1);
            List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
            if (CollectionUtils.isEmpty(mergedRegions)||(!mergedRegions.contains(cellAddresses))) {
                    sheet.addMergedRegion(cellAddresses);
            }
        }
        return secondCellIndex;
    }

    public static List<String> binaryTreePaths(Option root, List<String> list) {
        if (root == null) {
            return list;
        }
        String path = root.getLabel();
        String values = root.getValue();
        backTracking(root.getChildren(), path, list, values);
        return list;
    }

    public static void backTracking(List<Option> children, String path, List<String> list, String values) {

        if (CollectionUtils.isEmpty(children)) {
//            list.add(path+"-" +values );
            list.add(path + "(" + values + ")");
            return;
        }

        for (Option child : children) {
            String newPath = path + "-" + child.getLabel();
            String newValues = values + "-" + child.getValue();
            backTracking(child.getChildren(), newPath, list, newValues);
        }
    }

    private Date dateConvert(String propertyValue) {
        if(com.dchealth.common.utils.StringUtils.isEmpty(propertyValue)){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(propertyValue);
        } catch (Exception e) {
            log.error("时间转换异常，请检查，date={}",propertyValue);
        }
        return date;
    }

    /**
     * 解决下拉框过长不显示问题，超过250行的下拉选项，一般用这个(隐藏sheet的方式)
     *
     * @param workbook
     * @param dictList   下拉数据数组
     * @param sheet
     * @param firstRow   开始行
     * @param endRow     结束行
     * @param cellNum    下拉框所在的列
     * @param sheetHiddenIndex 隐藏sheet名称
     */
    public static void setLongHSSFValidation2(SXSSFWorkbook workbook, String[] dictList, SXSSFSheet sheet,
                                              int firstRow, int endRow, int cellNum, AtomicInteger sheetHiddenIndex) {
        String hiddenName = "hidden" + cellNum;
        int sheetIndex = sheetHiddenIndex.get();
        if (workbook.getSheet(hiddenName) != null) {
            return;
        }
        //1.创建隐藏的sheet页。        起个名字吧！叫"hidden"！
        SXSSFSheet hidden = workbook.createSheet(hiddenName);
        //2.循环赋值（为了防止下拉框的行数与隐藏域的行数相对应，将隐藏域加到结束行之后）
        for (int i = 0, length = dictList.length; i < length; i++) {
            hidden.createRow(endRow + i).createCell(cellNum).setCellValue(dictList[i]);
        }
        Name category1Name = workbook.createName();
        category1Name.setNameName(hiddenName);
        //3 A1:A代表隐藏域创建第N列createCell(N)时。以A1列开始A行数据获取下拉数组
        category1Name.setRefersToFormula(hiddenName + "!A1:A" + (dictList.length + endRow));
        //
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createFormulaListConstraint(hiddenName);
        CellRangeAddressList addressList = new CellRangeAddressList(1, endRow, cellNum, cellNum);
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        if (dataValidation instanceof XSSFDataValidation) {
            // 数据校验
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }
        // 作用在目标sheet上
        sheet.addValidationData(dataValidation);
        // 设置hiddenSheet隐藏
        workbook.setSheetHidden(sheetIndex, true);
        sheetHiddenIndex.addAndGet(1);

    }

}
