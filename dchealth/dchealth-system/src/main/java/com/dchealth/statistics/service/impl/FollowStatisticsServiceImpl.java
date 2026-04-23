package com.dchealth.statistics.service.impl;

import cn.hutool.core.date.DateUtil;
import com.dchealth.common.core.domain.entity.SysDept;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.core.domain.model.LoginUser;
import com.dchealth.common.utils.*;
import com.dchealth.medical.domain.AppLoginRecord;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.mapper.AppLoginRecordMapper;
import com.dchealth.medical.mapper.PatientInfoMapper;
import com.dchealth.medical.mapper.PatientRecordMapper;
import com.dchealth.research.domain.ResearchDiseases;
import com.dchealth.research.domain.ResearchInfo;
import com.dchealth.research.domain.vo.ResearchDiseasesVO;
import com.dchealth.research.mapper.ResearchDiseasesMapper;
import com.dchealth.research.mapper.ResearchInfoMapper;
import com.dchealth.statistics.domain.*;
import com.dchealth.statistics.service.FollowStatisticsService;
import com.dchealth.system.domain.PatientAccount;
import com.dchealth.system.mapper.PatientAccountMapper;
import com.dchealth.system.mapper.SysDeptMapper;
import com.dchealth.system.mapper.SysUserMapper;
import com.dchealth.warn.domain.WarnEarlyRecord;
import com.dchealth.warn.mapper.WarnEarlyRecordMapper;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.dchealth.common.utils.DateUtils.getNowDate;


@Service
public class FollowStatisticsServiceImpl implements FollowStatisticsService {
    /**
     * 最近一周
     */
    private static final int DATA_AREA_RECENTLY_WEEK = 0;

    /**
     * 最近一月
     */
    private static final int DATA_AREA_RECENTLY_MONTH = 1;
    /**
     * 最近一年
     */
    private static final int DATA_AREA_RECENTLY_YEAR = 2;
    /**
     * 自定义
     */
    private static final int DATA_AREA_CUSTOM = 3;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private PatientRecordMapper patientRecordMapper;

    @Autowired
    private PatientInfoMapper patientInfoMapper;

    @Autowired
    private ResearchInfoMapper researchInfoMapper;
    @Autowired
    private ResearchDiseasesMapper researchDiseasesMapper;
    @Autowired
    private WarnEarlyRecordMapper warnEarlyRecordMapper;

    @Autowired
    private AppLoginRecordMapper appLoginRecordMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private PatientAccountMapper patientAccountMapper;


    @Override
    public Map<String, List<StatisticsVo>> followDistribute(StatisticsParam statisticsParam) {
        Map<String, List<StatisticsVo>> result = new HashMap<>();
        setQueryDate(statisticsParam);
        List<SysDept> sysDepts = sysDeptMapper.selectDeptList(new SysDept());

        List<SysDept> deptInUseList = sysDepts.stream().filter(de -> filterDept(de, statisticsParam)).collect(Collectors.toList());
        //  查询所有随访
        statisticsParam.setEnrollmentHandlerStatus("3");
        statisticsParam.setEnrollmentStatus("1");
        List<PatientRecord> patientRecords = patientRecordMapper.selectPatientRecordListForStatistics2(statisticsParam);

        PatientInfo pio = new PatientInfo();
        pio.setEnrollmentHandlerStatus("3");
        pio.setEnrollmentStatus("1");
        List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoListLess(pio);
        // 判断查询场景
        if (Objects.isNull(statisticsParam.getDeptId()) && Objects.isNull(statisticsParam.getDiseaseId()) && Objects.isNull(statisticsParam.getResearchId())) {
            queryAllDept(statisticsParam, result, patientRecords, deptInUseList, patientInfos);
        } else if (Objects.nonNull(statisticsParam.getDeptId()) && Objects.isNull(statisticsParam.getDiseaseId()) && Objects.isNull(statisticsParam.getResearchId())) {
            queryResearchByDeptId(statisticsParam, result, sysDepts, patientRecords, patientInfos);
        } else if (Objects.nonNull(statisticsParam.getDeptId()) && Objects.nonNull(statisticsParam.getDiseaseId()) && Objects.isNull(statisticsParam.getResearchId())) {
            queryResearchByDiseaseId(statisticsParam, result, sysDepts, patientRecords, patientInfos);
        } else if (Objects.nonNull(statisticsParam.getDeptId()) && Objects.nonNull(statisticsParam.getDiseaseId()) && Objects.nonNull(statisticsParam.getResearchId())) {
            queryRecordByResearch(statisticsParam, result, sysDepts, patientRecords, patientInfos);
        }
        return result;
    }

    private boolean filterDept(SysDept de, StatisticsParam statisticsParam) {
        return Objects.nonNull(statisticsParam.getRoleDeptId()) ?
                StringUtils.equals(de.getDelFlag(), "0") && StringUtils.equals(de.getDeptType(), "2")
                        && de.getDeptId().longValue() == statisticsParam.getRoleDeptId().longValue()
                : StringUtils.equals(de.getDelFlag(), "0") && StringUtils.equals(de.getDeptType(), "2");
    }

    @Override
    public Map<String, List<StatisticsVo>> patientDistribute(StatisticsParam statisticsParam) {
        Map<String, List<StatisticsVo>> result = new HashMap<>();

        PatientInfo patientInfoQuery = new PatientInfo();
        if (Objects.nonNull(statisticsParam.getDeptId())) {
            patientInfoQuery.setDeptId(statisticsParam.getDeptId());
        }
        if (Objects.nonNull(statisticsParam.getDiseaseId())) {
            patientInfoQuery.setDiseaseId(statisticsParam.getDiseaseId());
        }
        if (Objects.nonNull(statisticsParam.getUserId())) {
            patientInfoQuery.setUserId(statisticsParam.getUserId());
        }
        patientInfoQuery.setEnrollmentHandlerStatus("3");
        patientInfoQuery.setEnrollmentStatus("1");
        patientInfoQuery.setNotPatientStatus("6");//排除已退出队列患者
        patientInfoQuery.setNotResearchStatus("0");//排除为发布队列患者
        List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoList(patientInfoQuery);

        List<Long> deptPatientIds = patientInfos.stream().map(PatientInfo::getPatientId).collect(Collectors.toList());


//        List<PatientRecord> patientRecords = patientRecordMapper.selectPatientRecordListForStatistics(statisticsParam);
        List<ResearchDiseases> researchDiseases = researchDiseasesMapper.selectResearchDiseasesByAuth();
        if (Objects.nonNull(statisticsParam.getDeptId())) {
            researchDiseases = researchDiseases.stream().filter(rd -> Objects.nonNull(rd.getDeptId()) && statisticsParam.getDeptId().longValue() == rd.getDeptId()).collect(Collectors.toList());
        }

        if (Objects.nonNull(statisticsParam.getDiseaseId())) {
            researchDiseases.stream().filter(rd -> Objects.nonNull(rd.getDiseaseId()) && rd.getDiseaseId().longValue() == statisticsParam.getDiseaseId().longValue()).collect(Collectors.toList());
        }

        ResearchInfo researchInfoQuery = new ResearchInfo();
        if (Objects.nonNull(statisticsParam.getDeptId())) {
            researchInfoQuery.setDeptId(statisticsParam.getDeptId());
        }
        if (Objects.nonNull(statisticsParam.getDiseaseId())) {
            researchInfoQuery.setDiseaseTypeId(statisticsParam.getDiseaseId());
        }
        List<ResearchInfo> researchInfos = researchInfoMapper.selectResearchInfoList(researchInfoQuery);

        Map<String, List<ResearchDiseases>> disGroup = researchDiseases.stream().filter(rd -> Objects.nonNull(rd.getDiseaseName())).collect(Collectors.groupingBy(ResearchDiseases::getDiseaseName));
        List<StatisticsVo> diseaseList = disGroup.keySet().stream().map(diseaseName -> {
            Set<Long> diseaseNameIds = disGroup.get(diseaseName).stream().map(ResearchDiseases::getDiseaseId).collect(Collectors.toSet());
            List<Long> diseaseResearchIds = researchInfos.stream().filter(researchInfo -> Objects.nonNull(researchInfo.getDiseaseTypeId()) && diseaseNameIds.contains(researchInfo.getDiseaseTypeId())
                    ).map(ResearchInfo::getId).collect(Collectors.toList());
            long count = patientInfos.stream().filter(pr -> deptPatientIds.contains(pr.getPatientId()) && diseaseResearchIds.contains(pr.getResearchId())).count();
            if (count == 0)
                return null;
            StatisticsVo statisticsVo = new StatisticsVo();
            statisticsVo.setName(diseaseName);
            statisticsVo.setTotal(count);
            return statisticsVo;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        Long diseaseSum = 0L;
        for (StatisticsVo vo : diseaseList) {
            diseaseSum += vo.getTotal();
        }
        StatisticsVo statisticsVoDisOther = new StatisticsVo();
        statisticsVoDisOther.setName("其他");
        statisticsVoDisOther.setTotal(patientInfos.size() - diseaseSum);
//        if(statisticsVoDisOther.getTotal() > 0 )diseaseList.add(statisticsVoDisOther);
        result.put("disease", diseaseList);

//        List<Long> patientBaseIds = patientInfos.stream().map(PatientInfo::getBaseId).collect(Collectors.toList());
//        List<PatientBaseInfo> patientBaseInfos = patientBaseInfoMapper.selectBasePatientInfoList(new PatientBaseInfo());
//        List<PatientBaseInfo> deptBaseList = patientBaseInfos.stream().filter(base -> patientBaseIds.contains(base.getId())).collect(Collectors.toList());


        List<StatisticsVo> sexList = new ArrayList<>();
        Integer manNum = 0;
        Integer girlNum = 0;
        StatisticsVo manVo = new StatisticsVo();
        StatisticsVo girlVo = new StatisticsVo();
        StatisticsVo genderOtherVo = new StatisticsVo();
        for(PatientInfo pi:patientInfos){
            if(pi.getIdCard()!=null && pi.getIdCard().length()==18){
                Integer sex = Integer.parseInt(CommunalUtils.getSex(pi.getIdCard()));
                if (sex == 1) {
                    manNum++;
                } else {
                    girlNum++;
                }
            }

        }
        manVo.setTotal(Long.valueOf(manNum));
        manVo.setName("男");
        sexList.add(manVo);
        girlVo.setTotal(Long.valueOf(girlNum));
        girlVo.setName("女");
        sexList.add(girlVo);
        genderOtherVo.setTotal(patientInfos.size() - Long.valueOf(girlNum) - Long.valueOf(manNum));
        genderOtherVo.setName("未知");
        if(genderOtherVo.getTotal() > 0)sexList.add(genderOtherVo);
        result.put("patientSex", sexList);
        result.put("sex", sexList);

//        StatisticsVo statisticsVo = new StatisticsVo();
//        statisticsVo.setName("北京市");
//        statisticsVo.setTotal(Long.parseLong(String.valueOf(CollectionUtils.size(deptBaseList))));
//        result.put("city", Arrays.asList(statisticsVo));

        // 年龄分布
        List<StatisticsVo> ageList = new ArrayList<>();
        Set<Long> researchIds = researchInfos.stream().map(ResearchInfo::getId).collect(Collectors.toSet());
        List<PatientInfo> diseasePatientList = patientInfos.stream().filter(p -> Objects.nonNull(p.getResearchId()) && researchIds.contains(p.getResearchId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(diseasePatientList)) {
            ageList.add(filterAgeDay(0, 28, patientInfos));
            ageList.add(filterAgeDay(28, 365, patientInfos));
            ageList.add(filterAgeYear(1, 3, patientInfos));
            ageList.add(filterAgeYear(3, 6, patientInfos));
            ageList.add(filterAgeYear(6, 12, patientInfos));
            ageList.add(filterAgeYear(12, 18, patientInfos));
            ageList.add(filterAgeYear(18, 29, patientInfos));
            ageList.add(filterAgeYear(29, 39, patientInfos));
            ageList.add(filterAgeYear(39, 49, patientInfos));
            ageList.add(filterAgeYear(49, 59, patientInfos));
            ageList.add(filterAgeYear(59, 69, patientInfos));
            ageList.add(filterAgeYear(69, 80, patientInfos));
            ageList.add(filterAgeYear(80, 89, patientInfos));
            ageList.add(filterAgeYear(89, 99, patientInfos));
            ageList.add(filterAgeYear(99, 150, patientInfos));
        }
        Long ageListSum = 0L;
        for (StatisticsVo vo : ageList) {
            ageListSum += vo.getTotal();
        }
        StatisticsVo statisticsVoAgeOther = new StatisticsVo();
        statisticsVoAgeOther.setName("未知");
        statisticsVoAgeOther.setTotal(patientInfos.size() - ageListSum);
        if(statisticsVoAgeOther.getTotal() > 0)ageList.add(statisticsVoAgeOther);
        result.put("age", ageList);
        return result;
    }

    @Override
    public Map<String, Object> hospitalView() {
        Map<String, Object> result = new HashMap<>();
        // 今日随访
        PatientRecord patientRecord = new PatientRecord();
        patientRecord.setEnrollmentHandlerStatus("3");
        List<PatientRecord> patientRecords = patientRecordMapper.selectPatientRecordListNoBase(patientRecord);
        List<PatientRecord> todayFollowPatientList = patientRecords.stream().filter(pr -> Objects.nonNull(pr.getCreateTime()) && DateUtils.isSameDay(pr.getCreateTime(), getNowDate())).collect(Collectors.toList());
        result.put("todayFollowPatientNum", CollectionUtils.size(todayFollowPatientList));
        // 今日入组
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setEnrollmentHandlerStatus("3");

        List<PatientInfo> patientInfoList = patientInfoMapper.selectPatientInfoListLess(patientInfo);
        List<PatientInfo> todayEnrollmentPatientList = patientInfoList.stream().filter(p -> Objects.nonNull(p.getEnrollmentTime()) && DateUtils.isSameDay(p.getEnrollmentTime(), getNowDate())).collect(Collectors.toList());
        result.put("todayEnrollmentPatientNum", CollectionUtils.size(todayEnrollmentPatientList));
        // 查询已发布的队列
        ResearchInfo publishedQuery = new ResearchInfo();
        publishedQuery.setResearchStatus("1"); // 1-进行中（已发布）
        List<ResearchInfo> publishedResearchList = researchInfoMapper.selectResearchInfoList(publishedQuery);
        Set<Long> publishedDeptAllIds = publishedResearchList.stream()
                .filter(r -> Objects.nonNull(r.getDeptId()))
                .map(ResearchInfo::getDeptId)
                .collect(Collectors.toSet());
        
        // 查询所有部门信息
        List<SysDept> sysDepts = sysDeptMapper.selectDeptList(new SysDept());
        Map<Long, SysDept> deptMap = sysDepts.stream().collect(Collectors.toMap(SysDept::getDeptId, d -> d));
        
        // 一次遍历同时统计科室（ancestors层级为2）和病区（ancestors层级为3）
        Set<Long> publishedDeptIds = new HashSet<>();
        Set<Long> publishedWardIds = new HashSet<>();
        for (Long deptId : publishedDeptAllIds) {
            SysDept dept = deptMap.get(deptId);
            if (dept != null && dept.getAncestors() != null) {
                int level = dept.getAncestors().split(",").length;
                if (level == 2) {
                    // 科室层级（ancestors="0,100"）
                    publishedDeptIds.add(deptId);
                } else if (level == 3) {
                    // 病区层级（ancestors="0,100,102"）
                    publishedWardIds.add(deptId);
                }
            }
        }
        result.put("todayFollowDeptNum", CollectionUtils.size(publishedDeptIds));
        result.put("todayFollowWardNum", CollectionUtils.size(publishedWardIds));

        // 随访队列
        Set<Long> researchIds = patientRecords.stream().filter(p -> Objects.nonNull(p.getResearchId())).map(PatientRecord::getResearchId).collect(Collectors.toSet());
        result.put("todayFollowResearchNum", CollectionUtils.size(researchIds));
        // 随访病种
        List<ResearchInfo> researchInfos = researchInfoMapper.selectResearchInfoList(new ResearchInfo());
        Set<Long> diseaseIds = researchInfos.stream().filter(rd -> researchIds.contains(rd.getId())).map(ResearchInfo::getDiseaseTypeId).collect(Collectors.toSet());
        result.put("todayFollowDiseaseNum", CollectionUtils.size(diseaseIds));

        // 累计随访人数
        result.put("totalFollowPatientNum", CollectionUtils.size(patientRecords));

        // 医生端用户数（正常状态且未删除）
        SysUser doctorQuery = new SysUser();
        doctorQuery.setStatus("0"); // 0-正常
        List<SysUser> doctorList = sysUserMapper.selectUserList(doctorQuery);
        result.put("doctorUserNum", CollectionUtils.size(doctorList));

        // 患者端用户数（正常状态）
        PatientAccount patientQuery = new PatientAccount();
        patientQuery.setStatus("0"); // 0-正常
        List<PatientAccount> patientAccountList = patientAccountMapper.selectPatientAccountList(patientQuery);
        result.put("patientUserNum", CollectionUtils.size(patientAccountList));

//        PatientInfo patientInfoVo = new PatientInfo();
//        patientInfoVo.setEnrollmentHandlerStatus("3");
//        List<PatientInfo> patientInfoList = patientInfoMapper.selectPatientInfoList(patientInfoVo);

        // 慢病管理患者
        result.put("totalPatientNum", CollectionUtils.size(patientInfoList));
        // // 科室慢病管理情况
        // List<SysDept> sysDepts = sysDeptMapper.selectDeptList(new SysDept());
        // List<StatisticsVo> deptList = sysDepts.stream().map(de -> {
        //     long count = patientInfoList.stream().filter(p -> Objects.nonNull(p.getDeptId()) && p.getDeptId().longValue() == de.getDeptId().longValue()).count();
        //     if (count == 0) {
        //         return null;
        //     }
        //     StatisticsVo statisticsVo = new StatisticsVo();
        //     statisticsVo.setTotal(count);
        //     statisticsVo.setName(de.getDeptName());
        //     return statisticsVo;
        // }).filter(Objects::nonNull)
        //         .sorted((vo1, vo2) -> Long.compare(vo2.getTotal(), vo1.getTotal()))
        //         .limit(10)
        //         .collect(Collectors.toList());

        // 科室慢病管理情况（只统计有已发布队列的科室和病区）
        // 按父科室ID分组有已发布队列的病区
        Map<Long, List<SysDept>> wardsByParentId = sysDepts.stream()
                .filter(w -> publishedWardIds.contains(w.getDeptId()))
                .filter(w -> Objects.nonNull(w.getParentId()))
                .collect(Collectors.groupingBy(SysDept::getParentId));

        // 统计有已发布队列的科室及其病区数据
        List<StatisticsVo> deptList = sysDepts.stream()
                .filter(dept -> 
                    publishedDeptIds.contains(dept.getDeptId()) ||      // 科室自己有已发布队列
                    wardsByParentId.containsKey(dept.getDeptId())       // 或科室下有病区有已发布队列
                ) // 统计有已发布队列的科室（包括通过病区间接拥有队列的科室）
                .map(dept -> {
                    // 统计科室直属患者
                    long deptCount = patientInfoList.stream()
                            .filter(p -> Objects.nonNull(p.getDeptId()) && p.getDeptId().equals(dept.getDeptId()))
                            .count();

                    // 统计该科室下有已发布队列的病区数据
                    List<StatisticsVo> wardList = null;
                    if (wardsByParentId.containsKey(dept.getDeptId())) {
                        wardList = wardsByParentId.get(dept.getDeptId()).stream().map(ward -> {
                            long wardCount = patientInfoList.stream()
                                    .filter(p -> Objects.nonNull(p.getDeptId()) && p.getDeptId().equals(ward.getDeptId()))
                                    .count();
                            if (wardCount == 0) {
                                return null;
                            }
                            StatisticsVo wardVo = new StatisticsVo();
                            wardVo.setName(ward.getDeptName());
                            wardVo.setTotal(wardCount);
                            return wardVo;
                        }).filter(Objects::nonNull)
                        .sorted((vo1, vo2) -> Long.compare(vo2.getTotal(), vo1.getTotal()))
                        .collect(Collectors.toList());

                        // 累加病区患者数到科室总数
                        if (wardList != null && !wardList.isEmpty()) {
                            deptCount += wardList.stream().mapToLong(StatisticsVo::getTotal).sum();
                        }
                    }

                    if (deptCount == 0) {
                        return null;
                    }

                    StatisticsVo deptVo = new StatisticsVo();
                    deptVo.setName(dept.getDeptName());
                    deptVo.setTotal(deptCount);
                    deptVo.setChildren(wardList);
                    return deptVo;
                }).filter(Objects::nonNull)
                .sorted((vo1, vo2) -> Long.compare(vo2.getTotal(), vo1.getTotal()))
                .limit(10)
                .collect(Collectors.toList());
        result.put("patientDept", deptList);

        // 慢病种种统计
        List<ResearchDiseases> researchDiseases = researchDiseasesMapper.selectResearchDiseasesByAuthLess();
        List<StatisticsVo> patientDiseaseList = researchDiseases.stream().map(rd -> {
            Set<Long> diseaseResearchIds = researchInfos.stream().filter(re -> Objects.nonNull(re.getDiseaseTypeId()) && re.getDiseaseTypeId().longValue() == rd.getDiseaseId().longValue()).map(ResearchInfo::getId).collect(Collectors.toSet());
            long count = patientInfoList.stream().filter(pr -> diseaseResearchIds.contains(pr.getResearchId())).count();
            if (count == 0) {
                return null;
            }
            StatisticsVo statisticsVo = new StatisticsVo();
            statisticsVo.setName(rd.getDiseaseName());
            statisticsVo.setTotal(count);
            return statisticsVo;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        result.put("patientDisease", patientDiseaseList);

        // 性别
//        Map<Integer, List<PatientBaseInfo>> sexGropp = patientBaseInfos.stream().filter(db -> Objects.nonNull(db.getSex()) && db.getSex().intValue() != 0).collect(Collectors.groupingBy(PatientBaseInfo::getSex));
//        List<StatisticsVo> sexList = sexGropp.keySet().stream().map(sex -> {
//            StatisticsVo statisticsVo = new StatisticsVo();
//            if (sex == 1) {
//                statisticsVo.setName("男");
//            } else {
//                statisticsVo.setName("女");
//            }
//            statisticsVo.setTotal(Long.parseLong(String.valueOf(CollectionUtils.size(sexGropp.get(sex)))));
//            return statisticsVo;
//        }).collect(Collectors.toList());

        List<StatisticsVo> sexList = new ArrayList<>();
        Integer manNum = 0;
        Integer girlNum = 0;
        StatisticsVo manVo = new StatisticsVo();
        StatisticsVo girlVo = new StatisticsVo();
        for(PatientInfo pi:patientInfoList){
            if(pi.getIdCard()!=null && pi.getIdCard().length()==18){
                Integer sex = Integer.parseInt(CommunalUtils.getSex(pi.getIdCard()));
                if (sex == 1) {
                    manNum++;
                } else {
                    girlNum++;
                }
            }
        }
        manVo.setTotal(Long.valueOf(manNum));
        manVo.setName("男");
        sexList.add(manVo);
        girlVo.setTotal(Long.valueOf(girlNum));
        girlVo.setName("女");
        sexList.add(girlVo);
        result.put("patientSex", sexList);

        //统计月活跃次数患者数和月活跃数
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1;
        AppLoginRecord appLoginRecord = new AppLoginRecord();
        appLoginRecord.setYearStat(year);
        appLoginRecord.setMonthStat(month);
        List<AppLoginRecord> appLoginRecordList = appLoginRecordMapper.selectAppLoginRecordList(appLoginRecord);
        List<String> apploginRecordGroupBy = appLoginRecordList.stream().map(AppLoginRecord::getDeviceToken).distinct().collect(Collectors.toList());
        //月活跃患者
        result.put("monthlyActivityPatients", apploginRecordGroupBy.size());
        //月活跃数
        result.put("monthlyActivityNumber", appLoginRecordList.size());
        return result;
    }

    /**
     * 查询当年12个月的月活跃趋势
     * @return 月活跃趋势数据
     */
    @Override
    public Map<String, Object> queryMonthlyActivityTrend(Integer yearStat) {
        Map<String, Object> result = new HashMap<>();

        // 如果年份为空 获取当前年份
        if (Objects.isNull(yearStat)) {
            Calendar date = Calendar.getInstance();
            yearStat = date.get(Calendar.YEAR);
        }

        // 一次性查询当前年份的所有登录记录
        AppLoginRecord appLoginRecord = new AppLoginRecord();
        appLoginRecord.setYearStat(yearStat);
        List<AppLoginRecord> appLoginRecordList = appLoginRecordMapper.selectAppLoginRecordList(appLoginRecord);

        // 按月份分组
        Map<Integer, List<AppLoginRecord>> monthGroupMap = appLoginRecordList.stream()
                .filter(record -> Objects.nonNull(record.getMonthStat()))
                .collect(Collectors.groupingBy(AppLoginRecord::getMonthStat));

        // 初始化12个月的数据
        List<String> months = new ArrayList<>();
        List<Integer> monthlyActivityPatients = new ArrayList<>();
        List<Integer> monthlyActivityNumber = new ArrayList<>();

        // 遍历12个月，统计每月数据
        for (int month = 1; month <= 12; month++) {
            months.add(month + "月");

            List<AppLoginRecord> monthRecords = monthGroupMap.getOrDefault(month, new ArrayList<>());

            // 计算月活跃患者数（去重deviceToken）
            long distinctPatients = monthRecords.stream()
                    .map(AppLoginRecord::getDeviceToken)
                    .distinct()
                    .count();
            monthlyActivityPatients.add((int) distinctPatients);

            // 计算月活跃数（总登录次数）
            monthlyActivityNumber.add(monthRecords.size());
        }

        result.put("months", months);
        result.put("monthlyActivityPatients", monthlyActivityPatients);
        result.put("monthlyActivityNumber", monthlyActivityNumber);

        return result;
    }

    /**
     * 年龄分布统计（新分段逻辑）
     * @param statisticsParam 统计参数
     * @return 年龄分布数据
     */
    @Override
    public List<StatisticsVo> queryAgeDistribution(StatisticsParam statisticsParam) {
        // 查询患者信息
        PatientInfo patientInfoQuery = new PatientInfo();
        patientInfoQuery.setEnrollmentHandlerStatus("3");
        patientInfoQuery.setEnrollmentStatus("1");
        patientInfoQuery.setNotPatientStatus("6"); // 排除已退出队列患者
        patientInfoQuery.setNotResearchStatus("0"); // 排除未发布队列患者
        List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoList(patientInfoQuery);

        // 按新的年龄分段统计
        List<StatisticsVo> ageList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(patientInfos)) {
            ageList.add(filterAgeByUnit(0, 15, patientInfos, "0-14岁", Calendar.YEAR));
            ageList.add(filterAgeByUnit(15, 45, patientInfos, "15-44岁", Calendar.YEAR));
            ageList.add(filterAgeByUnit(45, 60, patientInfos, "45-59岁", Calendar.YEAR));
            ageList.add(filterAgeByUnit(60, 75, patientInfos, "60-74岁", Calendar.YEAR));
            ageList.add(filterAgeByUnit(75, 90, patientInfos, "75-89岁", Calendar.YEAR));
            ageList.add(filterAgeByUnit(90, 150, patientInfos, "90岁及以上", Calendar.YEAR));
        }

        // 计算未知年龄数量
        Long ageListSum = ageList.stream().mapToLong(StatisticsVo::getTotal).sum();
        StatisticsVo statisticsVoAgeOther = new StatisticsVo();
        statisticsVoAgeOther.setName("未知");
        statisticsVoAgeOther.setTotal(patientInfos.size() - ageListSum);
        if (statisticsVoAgeOther.getTotal() > 0) {
            ageList.add(statisticsVoAgeOther);
        }

        return ageList;
    }


    @Override
    public List<ResearchStatusOfNumStatisticsVo> researchStatusNum(StatisticsParam statisticsParam) {
        Long userId = statisticsParam.getUserId();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<ResearchDiseasesVO> researchDiseasesVOS;
        // 中心医院
        if (loginUser.getDeptId() == 100) {
            if (statisticsParam.getDeptId() == null) {
                // 查看所有科室
                researchDiseasesVOS = researchDiseasesMapper.selectStatusNumOfDept(userId);
            } else {
                // 查看科室的所有病种
                researchDiseasesVOS = researchDiseasesMapper.selectStatusNumOfDiseaseName(statisticsParam.getDeptId(), userId);
            }
        } else {
            // 其他科室可以查看当前科室的病种
            researchDiseasesVOS = researchDiseasesMapper.selectStatusNumOfDiseaseName(statisticsParam.getDeptId(), userId);
        }
        Map<Long, ResearchStatusOfNumStatisticsVo> map = new HashMap<>();

        for (ResearchDiseasesVO researchDiseasesVO : researchDiseasesVOS) {
            Long deptId = researchDiseasesVO.getDeptId();
            Long diseaseId = researchDiseasesVO.getDiseaseId();
            ResearchStatusOfNumStatisticsVo researchStatusOfNumStatisticsVo = map.get(deptId == null ? diseaseId :deptId);
            if (null == researchStatusOfNumStatisticsVo) {
                researchStatusOfNumStatisticsVo = new ResearchStatusOfNumStatisticsVo();
                researchStatusOfNumStatisticsVo.setName((loginUser.getDeptId() == 100 && statisticsParam.getDeptId() == null) ?
                        researchDiseasesVO.getDeptName() :
                        researchDiseasesVO.getDiseaseName());
            }
            dealNum(researchStatusOfNumStatisticsVo, researchDiseasesVO); // 设置关闭和进行数量
            map.put(deptId == null ? diseaseId :deptId, researchStatusOfNumStatisticsVo);
        }
        return map.values().stream().collect(Collectors.toList());
    }

    @Override
    public  Map<String, Object> queryPatientDistribute(PatientDistribute distributeParam) {
        //查询所有患者分布
        List<PatientDistributeVo> researchProCityStatisticsVos = patientInfoMapper.queryPatientDistributeNew(new PatientDistribute());
        Map<Long, List<PatientDistributeVo>> map = researchProCityStatisticsVos.stream().collect(Collectors.groupingBy(PatientDistributeVo::getProvinceId));
        //患者来源省区分布
        List<ProvinceVo> provinceVos = new ArrayList<>();
        map.forEach((aLong, patientDistributeVos) -> {
            ProvinceVo provinceVo = new ProvinceVo();
            provinceVo.setProvinceId(aLong);
            provinceVo.setProvinceName(patientDistributeVos.get(0).getProvinceName());
            provinceVo.setPatientCount(patientDistributeVos.stream().mapToLong(PatientDistributeVo::getPatientCount).sum());
            provinceVos.add(provinceVo);
        });

        // 陕西省 行政区划代码
        Long provinceId = 610000L;
        //计算省内和省外患者数量
        long inProvinceCount = 0L;
        long outProvinceCount = 0L;
        for (Map.Entry<Long, List<PatientDistributeVo>> entry : map.entrySet()) {
            long count = entry.getValue().stream().mapToLong(PatientDistributeVo::getPatientCount).sum();
            if (entry.getKey().equals(provinceId)) {
                inProvinceCount = count;
            } else {
                outProvinceCount += count;
            }
        }

        //省内慢病患者分布
        PatientDistribute patientDistribute = new PatientDistribute();
        patientDistribute.setProvinceId(distributeParam.getProvinceId() == null ? provinceId:distributeParam.getProvinceId());
        List<PatientDistributeVo> provincePatientList = patientInfoMapper.queryPatientDistributeNew(patientDistribute);
        Map<Long, List<PatientDistributeVo>> provincePatientMap = provincePatientList.stream().collect(Collectors.groupingBy(PatientDistributeVo::getCityId));
        //省内当日新增慢病患者分布
        patientDistribute.setDate(new Date());
        List<PatientDistributeVo> provinceNewPatientList = patientInfoMapper.queryPatientDistributeNew(patientDistribute);
        Map<Long, List<PatientDistributeVo>> provinceNewPatientMap = provinceNewPatientList.stream().collect(Collectors.groupingBy(PatientDistributeVo::getCityId));

        //患者来源省区分布
        List<CityVisitVo> cityVisitVos = new ArrayList<>();
        provincePatientMap.forEach((aLong, patientDistributeVos) -> {
            CityVisitVo cityVisitVo = new CityVisitVo();
            cityVisitVo.setCityId(aLong);
            cityVisitVo.setLongitude(patientDistributeVos.get(0).getLongitude());
            cityVisitVo.setLatitude(patientDistributeVos.get(0).getLatitude());
            cityVisitVo.setCityName(patientDistributeVos.get(0).getCityName());
            cityVisitVo.setPatientCount(patientDistributeVos.stream().mapToLong(PatientDistributeVo::getPatientCount).sum());
            List<PatientDistributeVo> newPatientDistributeVos = provinceNewPatientMap.get(aLong);
            if (CollectionUtils.isNotEmpty(newPatientDistributeVos)){
                long newPatientCount = newPatientDistributeVos.stream().mapToLong(PatientDistributeVo::getPatientCount).sum();
                cityVisitVo.setNewPatientCount(newPatientCount);
            }else {
                cityVisitVo.setNewPatientCount(0L);
            }
            Map<String, Long> visitStatisticsMap = patientDistributeVos.stream().collect(Collectors.groupingBy(PatientDistributeVo::getVisitName, Collectors.summingLong(PatientDistributeVo::getPatientCount)));

            if (!visitStatisticsMap.keySet().contains("hospitalPatient")) {
                visitStatisticsMap.put("hospitalPatient",0l);
            }
            if (!visitStatisticsMap.keySet().contains("outPatient")) {
                visitStatisticsMap.put("outPatient",0l);
            }
            if (!visitStatisticsMap.keySet().contains("emergencyPatient")) {
                visitStatisticsMap.put("emergencyPatient",0l);
            }
            if (!visitStatisticsMap.keySet().contains("testPatient")) {
                visitStatisticsMap.put("testPatient",0l);
            }

            cityVisitVo.setVisitStatistics(visitStatisticsMap);
            cityVisitVos.add(cityVisitVo);
        });
        Map<String, Object> proCityStatisticsMap = new HashMap<>();
        proCityStatisticsMap.put("provinceVos",provinceVos);
        proCityStatisticsMap.put("cityVisitVos",cityVisitVos);
        proCityStatisticsMap.put("inProvinceCount",inProvinceCount);
        proCityStatisticsMap.put("outProvinceCount",outProvinceCount);
        return proCityStatisticsMap;
    }

    @Override
    public StatisticsRateResultVo queryFollowRate(StatisticsFollowRateVo statisticsParam) {

        // 入队列时间和随访时间都为空的时候展示所有的随访率
        // 如果入队列时间不为空，则计算队列在指定时间内入队列患者和参加随访的患者随访率

        // 如果全部不为空，则计算队列在指定时间内入队列患者和参加随访的患者随访率
        List<StatisticsFollowRateVo> patientEnrollmentList = patientInfoMapper.selectFollowEnrollmentList(statisticsParam);
        // 未入组的手动人数
        List<StatisticsFollowRateVo> noGeneratedManually = patientEnrollmentList.stream()
                .filter(patientEnrollment -> patientEnrollment.getGeneratedManually() == 0)
                .collect(Collectors.toList());
        // 手动入组的手动人数,默认从1开始，但是本身0也算一次
        List<StatisticsFollowRateVo> generatedManually = patientEnrollmentList.stream()
                .filter(patientEnrollment -> patientEnrollment.getGeneratedManually() != 0)
                .collect(Collectors.toList());
        int generatedManuallySum = patientEnrollmentList.stream()
                .filter(patientEnrollment -> patientEnrollment.getGeneratedManually() != 0)
                .mapToInt(StatisticsFollowRateVo::getGeneratedManually)
                .sum();
        int enrollmentNum = noGeneratedManually.size() + generatedManuallySum+generatedManually.size();

        List<StatisticsFollowRateVo> patientRecordList = patientRecordMapper.selectFollowListBySuccess(statisticsParam);

        int followNum = patientRecordList.size();

        return getStatisticsRateResultVo(followNum, enrollmentNum);
    }

    private static StatisticsRateResultVo getStatisticsRateResultVo(int followNum, int enrollmentNum) {
        double followRate;
        if (enrollmentNum == 0) {
            followRate = 0.0; // 当没有入队列患者时，随访率为0
        } else {
            followRate = (double) followNum / enrollmentNum;
        }

        double notFollowRate = 1 - followRate;
        // 保留两位小数
        DecimalFormat df = new DecimalFormat("0.00%");
        String followRateStr = df.format(followRate);
        String notFollowRateStr = df.format(notFollowRate);

        StatisticsRateResultVo statisticsRateResultVo = new StatisticsRateResultVo();
        statisticsRateResultVo.setFollowRate(followRateStr);
        statisticsRateResultVo.setFollowNum(String.valueOf(followNum));
        statisticsRateResultVo.setTotalNum(String.valueOf(enrollmentNum));
        statisticsRateResultVo.setFollowRateName("随访率");
        statisticsRateResultVo.setNotFollowRate(notFollowRateStr);
        statisticsRateResultVo.setNotFollowRateName("未随访率");
        return statisticsRateResultVo;
    }

    private void dealNum(ResearchStatusOfNumStatisticsVo researchStatusOfNumStatisticsVo, ResearchDiseasesVO researchDiseasesVO) {
        String researchStatus = researchDiseasesVO.getResearchStatus();
        if (researchStatus.equals("1")) { // 进行中
            researchStatusOfNumStatisticsVo.setGoingNum(researchDiseasesVO.getNum());
        } else if (researchStatus.equals("2")) { // 已关闭
            researchStatusOfNumStatisticsVo.setCloseNum(researchDiseasesVO.getNum());
        }
    }

    private void queryRecordByResearch(StatisticsParam statisticsParam, Map<String, List<StatisticsVo>> result, List<SysDept> sysDepts, List<PatientRecord> patientRecords, List<PatientInfo> patientInfos) {
        Optional<SysDept> any = sysDepts.stream().filter(dept -> Objects.nonNull(dept.getDeptId()) && dept.getDeptId().longValue() == statisticsParam.getDeptId().longValue()).findAny();
        List<ResearchDiseases> researchDiseases = researchDiseasesMapper.selectResearchDiseasesByAuth();
        Optional<ResearchDiseases> any2 = researchDiseases.stream().filter(rd -> Objects.nonNull(rd.getDiseaseId()) && rd.getDiseaseId().longValue() == statisticsParam.getDiseaseId().longValue()).findAny();
        List<ResearchInfo> researchInfos = researchInfoMapper.selectResearchInfoList(new ResearchInfo());
        Optional<ResearchInfo> any1 = researchInfos.stream().filter(research -> Objects.nonNull(research.getId()) && research.getId().longValue() == statisticsParam.getResearchId().longValue()).findAny();

        if (any.isPresent() && any2.isPresent() && any1.isPresent()) {
            // 依从性
            List<PatientInfo> deptPtatients = patientInfos.stream().filter(p -> p.getResearchId().equals(statisticsParam.getResearchId())).collect(Collectors.toList());
//            List<PatientInfo> deptPtatients = patientInfos.stream().filter(p -> Objects.nonNull(p.getDeptId()) && p.getDeptId().longValue() == any.get().getDeptId().longValue() && p.getResearchId() == statisticsParam.getResearchId()).collect(Collectors.toList());
            List<PatientRecord> researchRecords = patientRecords.stream().filter(p -> Objects.nonNull(p.getResearchId()) && p.getResearchId().longValue() == any1.get().getId().longValue()).collect(Collectors.toList());
            long patientCount = researchRecords.stream().map(PatientRecord::getPatientId).collect(Collectors.toSet()).size();
            if (patientCount == 0) {
                result.put("rely", Collections.EMPTY_LIST);
            } else {
                StatisticsVo statisticsVo = new StatisticsVo();
                statisticsVo.setName(any1.get().getProjectName());
                statisticsVo.setTotal(patientCount);
                long loseCount = researchRecords.stream().filter(depR -> filterPatientResearch(depR, deptPtatients)).map(PatientRecord::getPatientId).collect(Collectors.toSet()).size();
                statisticsVo.setDropNums(loseCount);
                statisticsVo.setFollowNums(patientCount - loseCount);
                statisticsVo.setFollowRate(getPercent(patientCount - loseCount, patientCount));
                statisticsVo.setDropRate(getPercent(loseCount, patientCount));
                result.put("rely", Arrays.asList(statisticsVo));
            }

            Integer recordCount = patientRecordMapper.selectRecordCount(statisticsParam);
            Integer warnCount = warnEarlyRecordMapper.selectWarnCount(statisticsParam);
            setOverDate(statisticsParam);//超窗时间需比随访开始时间退后一天
            Integer overCount = patientRecordMapper.selectOverRecordCount(statisticsParam);
            // 告警
            if(warnCount.equals(0)) {
                result.put("warn", Collections.EMPTY_LIST);
            } else {
                result.put("warn", buildStatics(1, warnCount, recordCount));
            }

            // 超窗
            if(overCount.equals(0)) {
                result.put("overTime", Collections.EMPTY_LIST);
            } else {
                result.put("overTime", buildStatics(2, overCount, recordCount - overCount));
            }

//            // 告警
//            List<WarnEarlyRecord> warnEarlyRecords = warnEarlyRecordMapper.selectWarnEarlyRecordList(new WarnEarlyRecord());
//            Set<Long> queryTimeWarnIds = warnEarlyRecords.stream().filter(warnR -> DateUtils.isTimeBetweenRange(warnR.getCreateTime(), statisticsParam.getStartTime(), statisticsParam.getEndTime())).map(WarnEarlyRecord::getRecordId).collect(Collectors.toSet());
//            List<PatientRecord> diseaseRecords = researchRecords.stream().filter(record -> queryTimeWarnIds.contains(record.getRecordId()) && filterPatientDeptDiseaseResearch2(record, any1.get(), deptPtatients)).collect(Collectors.toList());
//            long warnTotalCount = diseaseRecords.stream().filter(record -> !StringUtils.equals(record.getWarnStatus(), "0")).count();
//            if (warnTotalCount == 0) {
//                result.put("warn", Collections.EMPTY_LIST);
//            } else {
//                StatisticsVo statisticsVo2 = new StatisticsVo();
//                statisticsVo2.setTotal(warnTotalCount);
//                statisticsVo2.setName(any1.get().getProjectName());
//                List<PatientRecord> patientRecordList = diseaseRecords.stream().filter(record -> !StringUtils.equals(record.getWarnStatus(), "0") && record.getResearchId().longValue() == any1.get().getId().longValue()
//                        && !"基线".equals(record.getName())).collect(Collectors.toList());
//                long count = diseaseRecords.stream().filter(record -> !StringUtils.equals(record.getWarnStatus(), "0") && record.getResearchId().longValue() == any1.get().getId().longValue()
//                        && !"基线".equals(record.getName())).count();
//                statisticsVo2.setFollowNums(count);
//                statisticsVo2.setFollowRate(getPercent(count, warnTotalCount));
//                result.put("warn", Arrays.asList(statisticsVo2));
//            }
//
//            // 超窗
//            List<PatientRecord> overTimeRecords = researchRecords.stream().filter(re -> DateUtils.isTimeBetweenRange(re.getOverTimeCreateTime(), statisticsParam.getStartTime(), statisticsParam.getEndTime())
//                    && !StringUtils.equals(re.getOverTimeStatus(), "0") && "0".equals(re.getFollowStatus()) && !"基线".equals(re.getName())).collect(Collectors.toList());
//            long overTimeCount = overTimeRecords.stream().count();
//            if (overTimeCount == 0) {
//                result.put("overTime", Collections.EMPTY_LIST);
//            } else {
//                StatisticsVo statisticsVo3 = new StatisticsVo();
//                long researchOverTimeNum = overTimeRecords.stream().filter(record -> record.getResearchId().longValue() == any1.get().getId()).count();
//                statisticsVo3.setName(any1.get().getProjectName());
//                statisticsVo3.setFollowNums(researchOverTimeNum);
//                statisticsVo3.setFollowRate(getPercent(researchOverTimeNum, overTimeCount));
//                statisticsVo3.setTotal(overTimeCount);
//                result.put("overTime", Arrays.asList(statisticsVo3));
//            }

        }
    }

    private void queryResearchByDiseaseId(StatisticsParam statisticsParam, Map<String, List<StatisticsVo>> result, List<SysDept> sysDepts, List<PatientRecord> patientRecords, List<PatientInfo> patientInfos) {
        Optional<SysDept> any = sysDepts.stream().filter(dept -> dept.getDeptId().longValue() == statisticsParam.getDeptId().longValue()).findAny();
        List<ResearchDiseases> researchDiseases = researchDiseasesMapper.selectResearchDiseasesByAuth();
        Optional<ResearchDiseases> any2 = researchDiseases.stream().filter(rd -> rd.getDiseaseId().longValue() == statisticsParam.getDiseaseId().longValue()).findAny();
        List<ResearchInfo> researchInfos = researchInfoMapper.selectResearchInfoList(new ResearchInfo());

        if (any.isPresent() && any2.isPresent()) {
            // 依从性
            List<ResearchInfo> deptDiseaseResearchInfos = researchInfos.stream().filter(ri -> Objects.nonNull(ri.getDiseaseTypeId()) && Objects.nonNull(ri.getDeptId()) && ri.getDeptId().longValue() == statisticsParam.getDeptId().longValue() && ri.getDiseaseTypeId().longValue() == any2.get().getDiseaseId()).collect(Collectors.toList());
            List<Long> deptDiseaseResearchIds = deptDiseaseResearchInfos.stream().map(ResearchInfo::getId).collect(Collectors.toList());
            List<PatientInfo> deptPtatients = patientInfos.stream().filter(p -> Objects.nonNull(p.getDeptId()) && p.getDeptId().longValue() == any.get().getDeptId().longValue()).collect(Collectors.toList());

            List<StatisticsVo> rely = deptDiseaseResearchInfos.stream().map(ddri -> {
                StatisticsVo statisticsVo = new StatisticsVo();
                if ("2".equals(ddri.getResearchStatus())) {
                    statisticsVo.setName(ddri.getProjectName() + "（已关闭）");
                }else {
                    statisticsVo.setName(ddri.getProjectName());
                }
                List<PatientRecord> deptResearchRecords = patientRecords.stream().filter(p -> filterDeptDiseaseResearch(p, deptPtatients, ddri)).collect(Collectors.toList());
                long researchCount = deptResearchRecords.stream().map(PatientRecord::getPatientId).collect(Collectors.toSet()).size();
                if (researchCount == 0) {
                    return null;
                }
                statisticsVo.setTotal(researchCount);
                long loseCount = deptResearchRecords.stream().filter(depR -> filterPatientResearch(depR, deptPtatients)).map(PatientRecord::getPatientId).collect(Collectors.toSet()).size();
                statisticsVo.setDropNums(loseCount);
                statisticsVo.setFollowNums(researchCount - loseCount);
                statisticsVo.setFollowRate(getPercent(researchCount - loseCount, researchCount));
                statisticsVo.setDropRate(getPercent(loseCount, researchCount));
                return statisticsVo;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            result.put("rely", rely);

            Integer recordCount = patientRecordMapper.selectRecordCount(statisticsParam);
            Integer warnCount = warnEarlyRecordMapper.selectWarnCount(statisticsParam);
            setOverDate(statisticsParam);//超窗时间需比随访开始时间退后一天
            Integer overCount = patientRecordMapper.selectOverRecordCount(statisticsParam);
            // 告警
            if(warnCount.equals(0)) {
                result.put("warn", Collections.EMPTY_LIST);
            } else {
                result.put("warn", buildStatics(1, warnCount, recordCount));
            }

            // 超窗
            if(overCount.equals(0)) {
                result.put("overTime", Collections.EMPTY_LIST);
            } else {
                result.put("overTime", buildStatics(2, overCount, recordCount - overCount));
            }

//            // 告警
//            List<WarnEarlyRecord> warnEarlyRecords = warnEarlyRecordMapper.selectWarnEarlyRecordList(new WarnEarlyRecord());
//            Set<Long> queryTimeWarnIds = warnEarlyRecords.stream().filter(warnR -> DateUtils.isTimeBetweenRange(warnR.getCreateTime(), statisticsParam.getStartTime(), statisticsParam.getEndTime())).map(WarnEarlyRecord::getRecordId).collect(Collectors.toSet());
//            List<PatientRecord> diseaseRecords = patientRecords.stream().filter(record -> filterPatientDeptDiseaseResearch(record, researchInfos, deptPtatients, any2.get()) && deptDiseaseResearchIds.contains(record.getResearchId())).collect(Collectors.toList());
//            List<PatientRecord> diseaseWarnRecords = diseaseRecords.stream().filter(record ->  queryTimeWarnIds.contains(record.getRecordId())  && !"基线".equals(record.getName())).collect(Collectors.toList());
//            long warnTotalCount = diseaseWarnRecords.stream().filter(record -> !StringUtils.equals(record.getWarnStatus(), "0")).count();
//            if (warnTotalCount == 0) {
//                result.put("warn", Collections.EMPTY_LIST);
//            } else {
//                List<StatisticsVo> warn = deptDiseaseResearchInfos.stream().map(dr -> {
//                    long count = diseaseWarnRecords.stream().filter(record -> !StringUtils.equals(record.getWarnStatus(), "0")
//                            && record.getResearchId().longValue() == dr.getId().longValue()).count();
//                    if (count == 0) {
//                        return null;
//                    }
//                    StatisticsVo statisticsVo = new StatisticsVo();
//                    statisticsVo.setTotal(warnTotalCount);
//                    statisticsVo.setName(dr.getProjectName());
//                    statisticsVo.setFollowNums(count);
//                    statisticsVo.setFollowRate(getPercent(count, warnTotalCount));
//                    return statisticsVo;
//                }).filter(Objects::nonNull).collect(Collectors.toList());
//                result.put("warn", warn);
//            }
//
//
//            // 超窗
//            List<PatientRecord> overTimeRecords = diseaseRecords.stream().filter(re -> DateUtils.isTimeBetweenRange(re.getOverTimeCreateTime(), statisticsParam.getStartTime(), statisticsParam.getEndTime())
//                    && !StringUtils.equals(re.getOverTimeStatus(), "0") && "0".equals(re.getFollowStatus()) && !"基线".equals(re.getName())).collect(Collectors.toList());
//            long overTimeCount = overTimeRecords.stream().count();
//            if (overTimeCount == 0) {
//                result.put("overTime", Collections.EMPTY_LIST);
//            } else {
//                List<StatisticsVo> overTimes = deptDiseaseResearchInfos.stream().map(dr -> {
//                    long researchOverTimeNum = overTimeRecords.stream().filter(record -> Objects.nonNull(record.getResearchId()) && record.getResearchId().longValue() == dr.getId()).count();
//                    if (researchOverTimeNum == 0)
//                        return null;
//                    StatisticsVo statisticsVo = new StatisticsVo();
//                    statisticsVo.setName(dr.getProjectName());
//                    statisticsVo.setFollowNums(researchOverTimeNum);
//                    statisticsVo.setFollowRate(getPercent(researchOverTimeNum, overTimeCount));
//                    statisticsVo.setTotal(overTimeCount);
//                    return statisticsVo;
//                }).filter(Objects::nonNull).collect(Collectors.toList());
//                result.put("overTime", overTimes);
//            }
        }
    }

    private boolean filterPatientDeptDiseaseResearch(PatientRecord record, List<ResearchInfo> researchInfos, List<PatientInfo> deptPtatients, ResearchDiseases researchDiseases) {
        Optional<ResearchInfo> any = researchInfos.stream().filter(research -> Objects.nonNull(record.getResearchId()) && record.getResearchId().longValue() == research.getId().longValue()).findAny();
        Optional<PatientInfo> any1 = deptPtatients.stream().filter(p -> Objects.nonNull(record.getPatientId()) && record.getPatientId().longValue() == p.getPatientId().longValue()).findAny();
        return any.isPresent() && any1.isPresent() &&
                any.get().getDiseaseTypeId().longValue() == researchDiseases.getDiseaseId().longValue();
    }

    private boolean filterPatientDeptDiseaseResearch2(PatientRecord record, ResearchInfo researchInfo, List<PatientInfo> deptPtatients) {
        Optional<PatientInfo> any1 = deptPtatients.stream().filter(p -> Objects.nonNull(record.getPatientId()) && record.getPatientId().longValue() == p.getPatientId().longValue()).findAny();
        return any1.isPresent() && record.getResearchId().longValue() == researchInfo.getId().longValue();
    }

    private boolean filterDeptDiseaseResearch(PatientRecord p, List<PatientInfo> deptPtatients, ResearchInfo ddri) {
        Optional<PatientInfo> any = deptPtatients.stream().filter(patientInfo -> Objects.nonNull(p.getPatientId()) && Objects.nonNull(patientInfo.getPatientId()) && patientInfo.getPatientId().longValue() == p.getPatientId().longValue()).findAny();
        return any.isPresent() && Objects.nonNull(p.getResearchId()) && p.getResearchId().longValue() == ddri.getId().longValue();
    }

    private void queryResearchByDeptId(StatisticsParam statisticsParam, Map<String, List<StatisticsVo>> result, List<SysDept> sysDepts, List<PatientRecord> patientRecords, List<PatientInfo> patientInfos) {
        Optional<SysDept> any = sysDepts.stream().filter(dept -> Objects.nonNull(dept.getDeptId()) && dept.getDeptId().longValue() == statisticsParam.getDeptId().longValue()).findAny();
        if (any.isPresent()) {
            // 依从性
            List<PatientInfo> deptPtatients = patientInfos.stream().filter(p -> Objects.nonNull(p.getDeptId()) && p.getDeptId().longValue() == any.get().getDeptId().longValue()).collect(Collectors.toList());
            List<ResearchDiseases> researchDiseases = researchDiseasesMapper.selectResearchDiseasesByAuth();
            List<ResearchInfo> researchInfos = researchInfoMapper.selectResearchInfoList(new ResearchInfo());
            List<ResearchDiseases> deptResearchs = researchDiseases.stream().filter(research -> Objects.nonNull(research.getDeptId()) && research.getDeptId().longValue() == statisticsParam.getDeptId().longValue()).collect(Collectors.toList());
            List<PatientRecord> deptPatientRecords = new ArrayList<>();

            List<PatientRecord> finalDeptPatientRecords = deptPatientRecords;
            List<StatisticsVo> rely = deptResearchs.stream().map(re -> {
                List<PatientRecord> deptResearchRecords = patientRecords.stream().filter(p -> filterDeptResearch(p, deptPtatients, researchInfos, re)).collect(Collectors.toList());
                finalDeptPatientRecords.addAll(deptResearchRecords);
                long researchCount = deptResearchRecords.stream().map(PatientRecord::getPatientId).collect(Collectors.toSet()).size();
                if (researchCount == 0) {
                    return null;
                }
                StatisticsVo statisticsVo = new StatisticsVo();
                statisticsVo.setName(re.getDiseaseName());
                statisticsVo.setTotal(researchCount);
                long loseCount = deptResearchRecords.stream().filter(depR -> filterPatientResearch(depR, deptPtatients)).map(PatientRecord::getPatientId).collect(Collectors.toSet()).size();
                statisticsVo.setDropNums(loseCount);
                statisticsVo.setFollowNums(researchCount - loseCount);
                statisticsVo.setFollowRate(getPercent(researchCount - loseCount, researchCount));
                statisticsVo.setDropRate(getPercent(loseCount, researchCount));
                return statisticsVo;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            result.put("rely", rely);
//            deptPatientRecords = finalDeptPatientRecords.stream().distinct().collect(Collectors.toList());

            Integer recordCount = patientRecordMapper.selectRecordCount(statisticsParam);
            Integer warnCount = warnEarlyRecordMapper.selectWarnCount(statisticsParam);
            setOverDate(statisticsParam);//超窗时间需比随访开始时间退后一天
            Integer overCount = patientRecordMapper.selectOverRecordCount(statisticsParam);
            // 告警
            if(warnCount.equals(0)) {
                result.put("warn", Collections.EMPTY_LIST);
            } else {
                result.put("warn", buildStatics(1, warnCount, recordCount));
            }

            // 超窗
            if(overCount.equals(0)) {
                result.put("overTime", Collections.EMPTY_LIST);
            } else {
                result.put("overTime", buildStatics(2, overCount, recordCount - overCount));
            }


//            List<WarnEarlyRecord> warnEarlyRecords = warnEarlyRecordMapper.selectWarnEarlyRecordList(new WarnEarlyRecord());
//            Set<Long> queryTimeWarnIds = warnEarlyRecords.stream().filter(warnR -> DateUtils.isTimeBetweenRange(warnR.getCreateTime(), statisticsParam.getStartTime(), statisticsParam.getEndTime())).map(WarnEarlyRecord::getRecordId).collect(Collectors.toSet());
////            List<PatientRecord> deptRecords = patientRecords.stream().filter(record -> filterPatientDeptResearch(record, researchInfos, deptPtatients, deptResearchs)).collect(Collectors.toList());
//            List<PatientRecord> deptWarnRecords = deptPatientRecords.stream().filter(record -> queryTimeWarnIds.contains(record.getRecordId())  && !"基线".equals(record.getName())).collect(Collectors.toList());
//            long warnTotalCount = deptWarnRecords.stream().filter(record -> !StringUtils.equals(record.getWarnStatus(), "0")).count();
//            if (warnTotalCount == 0) {
//                result.put("warn", Collections.EMPTY_LIST);
//            } else {
//                List<StatisticsVo> warn = deptResearchs.stream().map(dr -> {
//                    StatisticsVo statisticsVo = new StatisticsVo();
//                    statisticsVo.setTotal(warnTotalCount);
//                    statisticsVo.setName(dr.getDiseaseName());
//                    List<ResearchInfo> any1 = researchInfos.stream().filter(research -> Objects.nonNull(research.getDeptId()) && research.getDeptId().longValue() == statisticsParam.getDeptId().longValue() && research.getDiseaseTypeId().longValue() == dr.getDiseaseId().longValue()).collect(Collectors.toList());
//                    List<Long> researchIds = any1.stream().map(ResearchInfo::getId).collect(Collectors.toList());
//                    long count = deptWarnRecords.stream().filter(record -> Objects.nonNull(any1) && !StringUtils.equals(record.getWarnStatus(), "0")
//                            && researchIds.contains(record.getResearchId())).count();
//                    if(count == 0){
//                        return null;
//                    }
//                    statisticsVo.setFollowNums(count);
//                    statisticsVo.setFollowRate(getPercent(count, warnTotalCount));
//                    return statisticsVo;
//                }).filter(Objects::nonNull).collect(Collectors.toList());
//                result.put("warn", warn);
//            }

            // 超窗
//            List<PatientRecord> overTimeRecords = deptPatientRecords.stream().filter(re -> DateUtils.isTimeBetweenRange(re.getOverTimeCreateTime(), statisticsParam.getStartTime(), statisticsParam.getEndTime())
//                    && !StringUtils.equals(re.getOverTimeStatus(), "0") && "0".equals(re.getFollowStatus()) && !"基线".equals(re.getName())).collect(Collectors.toList());
//            long overTimeCount = overTimeRecords.stream().count();
//            if (overTimeCount == 0) {
//                result.put("overTime", Collections.EMPTY_LIST);
//            } else {
//                List<StatisticsVo> overTimes = deptResearchs.stream().map(dr -> {
//                    StatisticsVo statisticsVo = new StatisticsVo();
//
//                    Set<Long> researchIds = researchInfos.stream().filter(research -> Objects.nonNull(research.getDeptId()) && research.getDeptId().longValue() == statisticsParam.getDeptId().longValue() && research.getDiseaseTypeId().longValue() == dr.getDiseaseId().longValue()).map(ResearchInfo::getId).collect(Collectors.toSet());
//                    System.out.println(researchIds);
//                    overTimeRecords.stream().forEach(overTimeRecord -> System.out.println("==" + overTimeRecord.getRecordId()));
//                    long researchOverTimeNum = overTimeRecords.stream().filter(record -> CollectionUtils.isNotEmpty(researchIds) && researchIds.contains(record.getResearchId().longValue())).count();
//                    if(researchOverTimeNum == 0){
//                        return null;
//                    }
//                    statisticsVo.setName(dr.getDiseaseName());
//                    statisticsVo.setFollowNums(researchOverTimeNum);
//                    statisticsVo.setFollowRate(getPercent(researchOverTimeNum, overTimeCount));
//                    statisticsVo.setTotal(overTimeCount);
//                    return statisticsVo;
//                }).filter(Objects::nonNull).collect(Collectors.toList());
//                result.put("overTime", overTimes);
//            }

        }
    }

    private List<StatisticsVo> buildStatics(Integer type, Integer count, Integer recordCount) {
        List<StatisticsVo> list = new ArrayList<StatisticsVo>();
        StatisticsVo statisticsVo = new StatisticsVo();
        statisticsVo.setName("随访人次");
        statisticsVo.setFollowNums(Long.valueOf(recordCount));
        statisticsVo.setFollowRate(getPercent(recordCount, recordCount + count));
        StatisticsVo statisticsVo1 = new StatisticsVo();
        if(type.equals(1)) {
            statisticsVo1.setName("预警人次");
        } else if(type.equals(2)) {
            statisticsVo1.setName("超窗人次");
        }
        statisticsVo1.setFollowNums(Long.valueOf(count));
        statisticsVo1.setFollowRate(getPercent(count, recordCount + count));
        list.add(statisticsVo);
        list.add(statisticsVo1);
        return list;
    }

    private boolean filterPatientDeptResearch(PatientRecord record, List<ResearchInfo> researchInfos, List<PatientInfo> deptPtatients, List<ResearchDiseases> deptResearchs) {
        Optional<ResearchInfo> any = researchInfos.stream().filter(researchInfo -> record.getResearchId().longValue() == record.getResearchId().longValue()).findAny();

        Optional<PatientInfo> any1 = deptPtatients.stream().filter(p -> p.getPatientId().longValue() == record.getPatientId().longValue()).findAny();
        Optional<ResearchDiseases> any2 = deptResearchs.stream().filter(dr -> any.isPresent() && dr.getDiseaseId().longValue() == any.get().getDiseaseTypeId().longValue()).findAny();
        return any.isPresent() && any1.isPresent() && any2.isPresent();
    }

    private boolean filterPatientResearch(PatientRecord depR, List<PatientInfo> patientInfos) {
        Optional<PatientInfo> any = patientInfos.stream().filter(p -> Objects.nonNull(p.getDeptId()) && p.getPatientId().longValue() == depR.getPatientId().longValue()).findAny();
        return any.isPresent() && StringUtils.equals(any.get().getPatientStatus(), "4");
    }

    private boolean filterDeptResearch(PatientRecord p, List<PatientInfo> deptPatients, List<ResearchInfo> researchInfos, ResearchDiseases researchDiseases) {
        Optional<PatientInfo> any = deptPatients.stream().filter(dp -> Objects.nonNull(dp) && Objects.nonNull(p) && Objects.nonNull(p.getPatientId()) && Objects.nonNull(dp.getPatientId()) && dp.getPatientId().longValue() == p.getPatientId().longValue()).findAny();
        Optional<ResearchInfo> any1 = researchInfos.stream().filter(researchInfo -> Objects.nonNull(p.getResearchId()) && researchInfo.getId().longValue() == p.getResearchId().longValue()).findAny();
        return any.isPresent() && any1.isPresent() && Objects.nonNull(any1.get().getDiseaseTypeId()) && Objects.nonNull(researchDiseases.getDiseaseId()) && any1.get().getDiseaseTypeId().longValue() == researchDiseases.getDiseaseId().longValue();
    }

    /**
     * 查询全部科室
     *
     * @param result
     */
    private void queryAllDept(StatisticsParam statisticsParam, Map<String, List<StatisticsVo>> result, List<PatientRecord> patientRecords, List<SysDept> deptInUseList, List<PatientInfo> patientInfos) {
        List<PatientRecord> allPatientRecords = patientRecords.stream().filter(patientRecord -> Objects.nonNull(patientRecord.getPatientId())).collect(Collectors.toList());
        List<ResearchDiseases> allResearchDiseases = researchDiseasesMapper.selectResearchDiseasesList(new ResearchDiseases());
        List<ResearchInfo> allResearchInfos = researchInfoMapper.selectResearchInfoList(new ResearchInfo());
        List<Long> allDeptPatientIds = new ArrayList<>();
        // 依从性统计
        List<StatisticsVo> relyList = deptInUseList.stream().map(dept -> {
            StatisticsVo statisticsVo = new StatisticsVo();
            statisticsVo.setName(dept.getDeptName());
            Set<Long> deptPatientIds = patientInfos.stream().filter(patientInfo -> Objects.nonNull(patientInfo.getDeptId()) && patientInfo.getDeptId().longValue() == dept.getDeptId().longValue()).map(PatientInfo::getPatientId).collect(Collectors.toSet());
            allDeptPatientIds.addAll(deptPatientIds);
            List<Long> deptPatientCurrents = allPatientRecords.stream().filter(re -> deptPatientIds.contains(re.getPatientId())).map(PatientRecord::getPatientId).collect(Collectors.toList());
            long deptRecordsNum = new HashSet<>(deptPatientCurrents).size();
            if (deptRecordsNum == 0)
                return null;
            statisticsVo.setTotal(deptRecordsNum);
            long losePatientNum = patientInfos.stream().filter(info -> Objects.nonNull(info.getDeptId()) && deptPatientCurrents.contains(info.getPatientId()) && StringUtils.equals(info.getPatientStatus(), "4")).map(PatientInfo::getPatientId).collect(Collectors.toSet()).size();
            statisticsVo.setDropNums(losePatientNum);
            statisticsVo.setDropRate(getPercent(losePatientNum, deptRecordsNum));
            long followNums = deptRecordsNum - losePatientNum;
            statisticsVo.setFollowNums(followNums);
            statisticsVo.setFollowRate(getPercent(followNums, deptRecordsNum));
            return statisticsVo;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        result.put("rely", relyList);

        // 预警分布
        List<WarnEarlyRecord> warnEarlyRecords = warnEarlyRecordMapper.selectWarnEarlyRecordList(new WarnEarlyRecord());
        Set<Long> queryTimeWarnIds = warnEarlyRecords.stream().filter(warnR -> DateUtils.isTimeBetweenRange(warnR.getCreateTime(), statisticsParam.getStartTime(), statisticsParam.getEndTime())).map(WarnEarlyRecord::getRecordId).collect(Collectors.toSet());
        long count = allPatientRecords.stream().filter(re -> queryTimeWarnIds.contains(re.getRecordId()) && !StringUtils.equals(re.getWarnStatus(), "0")
                && !"基线".equals(re.getName()) && allDeptPatientIds.contains(re.getPatientId())).count();
        if (count == 0) {
            result.put("warn", Collections.EMPTY_LIST);
        } else {
            List<PatientRecord> warnQueryPatientRecord = allPatientRecords.stream().filter(pr -> queryTimeWarnIds.contains(pr.getRecordId()) && !"基线".equals(pr.getName())).collect(Collectors.toList());
            List<StatisticsVo> warnList = deptInUseList.stream().map(dept -> {
                StatisticsVo statisticsVo = new StatisticsVo();
                Set<Long> deptDiseases = allResearchDiseases.stream().filter(disease -> Objects.nonNull(disease.getDeptId()) && disease.getDeptId().longValue() == dept.getDeptId().longValue() && "0".equals(disease.getIsDeleted())).map(ResearchDiseases::getDiseaseId).collect(Collectors.toSet());
                Set<Long> deptResearchIds = allResearchInfos.stream().filter(researchInfo -> Objects.nonNull(researchInfo.getDiseaseTypeId()) && deptDiseases.contains(researchInfo.getDiseaseTypeId())).map(ResearchInfo::getId).collect(Collectors.toSet());
                Set<Long> deptPatientIds = patientInfos.stream().filter(patientInfo -> deptResearchIds.contains(patientInfo.getResearchId())).map(PatientInfo::getPatientId).collect(Collectors.toSet());
//                Set<Long> deptPatientIds = patientInfos.stream().filter(patientInfo -> Objects.nonNull(patientInfo.getDeptId()) && patientInfo.getDeptId().longValue() == dept.getDeptId().longValue()).map(PatientInfo::getPatientId).collect(Collectors.toSet());
                long deptRecordsWanNum = warnQueryPatientRecord.stream().filter(re -> deptPatientIds.contains(re.getPatientId())
                        && !StringUtils.equals(re.getWarnStatus(), "0")).count();
                if (deptRecordsWanNum == 0)
                    return null;
                statisticsVo.setName(dept.getDeptName());
                statisticsVo.setFollowNums(deptRecordsWanNum);
                statisticsVo.setFollowRate(getPercent(deptRecordsWanNum, count));
                statisticsVo.setTotal(count);
                return statisticsVo;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            result.put("warn", warnList);
        }


        // 超窗
        List<PatientRecord> overTimeQueryPatientRecords = patientRecords.stream().filter(pr -> DateUtils.isTimeBetweenRange(pr.getOverTimeCreateTime(), statisticsParam.getStartTime(), statisticsParam.getEndTime())
                && "0".equals(pr.getFollowStatus()) && !"基线".equals(pr.getName())).collect(Collectors.toList());
        long overTimeCount = overTimeQueryPatientRecords.stream().filter(re -> !StringUtils.equals(re.getOverTimeStatus(), "0")).count();
        if (overTimeCount == 0) {
            result.put("overTime", Collections.EMPTY_LIST);
        } else {
            List<StatisticsVo> overTimes = deptInUseList.stream().map(dept -> {
                Set<Long> deptPatientIds = patientInfos.stream().filter(patientInfo -> Objects.nonNull(patientInfo.getDeptId()) && patientInfo.getDeptId().longValue() == dept.getDeptId().longValue()).map(PatientInfo::getPatientId).collect(Collectors.toSet());
                long deptRecordsOverTimeNum = overTimeQueryPatientRecords.stream().filter(re -> deptPatientIds.contains(re.getPatientId()) && !StringUtils.equals(re.getOverTimeStatus(), "0")).count();
                if (deptRecordsOverTimeNum == 0)
                    return null;
                StatisticsVo statisticsVo = new StatisticsVo();
                statisticsVo.setName(dept.getDeptName());
                statisticsVo.setFollowNums(deptRecordsOverTimeNum);
                statisticsVo.setFollowRate(getPercent(deptRecordsOverTimeNum, overTimeCount));
                statisticsVo.setTotal(overTimeCount);
                return statisticsVo;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            result.put("overTime", overTimes);
        }

    }

    private StatisticsVo filterAgeYear(int start, int end, List<PatientInfo> collect) {
        StatisticsVo statisticsVo = new StatisticsVo();
        if (start == 99) {
            statisticsVo.setName("99岁以上");
        } else {
            statisticsVo.setName(start + "-" + end + "岁");
        }
        statisticsVo.setTotal(collect.stream().filter(p -> ageInArea(start, end, p, Calendar.YEAR)).count());
        return statisticsVo;
    }

    /**
     * 按新分段逻辑过滤年龄（支持天和岁混合）
     * @param start 起始值
     * @param end 结束值
     * @param collect 患者列表
     * @param label 显示标签
     * @param unit 单位（Calendar.DATE表示天，Calendar.YEAR表示岁）
     * @return 统计结果
     */
    private StatisticsVo filterAgeByUnit(int start, int end, List<PatientInfo> collect, String label, int unit) {
        StatisticsVo statisticsVo = new StatisticsVo();
        statisticsVo.setName(label);
        statisticsVo.setTotal(collect.stream().filter(p -> ageInArea(start, end, p, unit)).count());
        return statisticsVo;
    }

    private StatisticsVo filterAgeDay(int start, int end, List<PatientInfo> collect) {
        StatisticsVo statisticsVo = new StatisticsVo();
        if (end == 365) {
            statisticsVo.setName(start + "天-1岁");
        } else {
            statisticsVo.setName(start + "-" + end + "天");
        }

        statisticsVo.setTotal(collect.stream().filter(p -> ageInArea(start, end, p, Calendar.DATE)).count());
        return statisticsVo;
    }

    private boolean ageInArea(int start, int end, PatientInfo p, int dateUnit) {
        try {
            if (dateUnit == Calendar.YEAR) {
                if (StringUtils.isNotEmpty(p.getIdCard()) && p.getIdCard().length()==18) {
                    return AgeUtil.getAgeYearByBirthday(DateUtils.parseDate(CommunalUtils.getBirthday(p.getIdCard()))) >= start
                            && AgeUtil.getAgeYearByBirthday(DateUtils.parseDate(CommunalUtils.getBirthday(p.getIdCard()))) < end;
                }
                if (StringUtils.isNotEmpty(p.getIdCard()) && p.getIdCard().length()==18) {
                    return AgeUtil.getAgeYearByIdCard(p.getIdCard()) >= start
                            && AgeUtil.getAgeYearByIdCard(p.getIdCard()) < end;
                }
            } else {
                if (StringUtils.isNotEmpty(p.getIdCard()) && p.getIdCard().length()==18) {
                    return AgeUtil.getAgeDayByBirthday(DateUtils.parseDate(CommunalUtils.getBirthday(p.getIdCard()))) >= start
                            && AgeUtil.getAgeDayByBirthday(DateUtils.parseDate(CommunalUtils.getBirthday(p.getIdCard()))) < end;
                }
                if (StringUtils.isNotEmpty(p.getIdCard()) && p.getIdCard().length()==18) {
                    return AgeUtil.getAgeDayByIdCard(p.getIdCard()) >= start
                            && AgeUtil.getAgeDayByIdCard(p.getIdCard()) < end;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private void setQueryDate(StatisticsParam statisticsParam) {
        if (statisticsParam.getTimeRegion() == DATA_AREA_CUSTOM) {
            Date startDate = Objects.isNull(statisticsParam.getStartTime()) ? getNowDate() : statisticsParam.getStartTime();
            Date lastDate = Objects.isNull(statisticsParam.getEndTime()) ? getNowDate() : statisticsParam.getEndTime();
            if (startDate.after(lastDate)) {
                statisticsParam.setStartTime(DateUtil.beginOfDay(lastDate));
                statisticsParam.setEndTime(DateUtil.endOfDay(startDate));
            } else {
                statisticsParam.setStartTime(DateUtil.beginOfDay(startDate));
                statisticsParam.setEndTime(DateUtil.endOfDay(lastDate));
            }
        } else {
            int dateNum = getDateNum(statisticsParam.getTimeRegion());
            statisticsParam.setStartTime(DateUtil.beginOfDay(DateUtils.getPreDate(dateNum)));
            statisticsParam.setEndTime(DateUtil.endOfDay(getNowDate()));
        }
    }

    /**
     * 设置超窗开始时间（超窗时间得推迟随访时间一天）
     * @param statisticsParam
     */
    private void setOverDate(StatisticsParam statisticsParam) {
        statisticsParam.setStartTime(DateUtil.beginOfDay(DateUtil.offsetDay(statisticsParam.getStartTime(), 1)));
    }

    private int getDateNum(int dateArea) {
        int dateNum;
        if (dateArea == DATA_AREA_RECENTLY_WEEK) {
            dateNum = 7;
        } else if (dateArea == DATA_AREA_RECENTLY_MONTH) {
            dateNum = 30;
        } else if (dateArea == DATA_AREA_RECENTLY_YEAR) {
            dateNum = 365;
        } else {
            dateNum = 7;
        }
        return dateNum;
    }

    private String getPercent(long cu, long total) {
        if (total == 0 || cu == 0) {
            return "0.00%";
        }
        DecimalFormat format = new DecimalFormat("#0.00%");
        return format.format((cu * 1.0) / (total * 1.0));
    }



    @Data
    class ProvinceVo {
        /**
         * 省份名称
         */
        private String provinceName;
        /**
         * 省份ID
         */
        private Long provinceId;
        /**
         * 总患者数量
         */
        private Long patientCount;
    }

    @Data
    class CityVisitVo {
        /**
         * 城市名称
         */
        private String cityName;

        /**
         * 城市ID
         */
        private Long cityId;

        /**
         * 新增患者
         */
        private Long newPatientCount;

        /**
         * 就诊统计
         */
        private Map<String, Long> visitStatistics;


        /**
         * 患者数量
         */
        private Long patientCount;


        /**
         * 经度
         */
        private String longitude;

        /**
         * 纬度
         */
        private String latitude;
    }
}

