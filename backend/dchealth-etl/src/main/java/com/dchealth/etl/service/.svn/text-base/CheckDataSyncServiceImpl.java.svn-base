package com.dchealth.etl.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.json.JSONUtil;
import com.dchealth.etl.dao.ThredQuery;
import com.dchealth.etl.domain.check.Hyb;
import com.dchealth.etl.domain.check.Jcb;
import com.dchealth.etl.domain.check.Jcxx;
import com.dchealth.etl.domain.check.Jlb;
import com.dchealth.etl.domain.constant.TdCheckConstant;
import com.dchealth.etl.domain.target.*;
import com.dchealth.etl.service.check.*;
import com.dchealth.etl.service.target.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author zjl
 * @since 2023/3/27
 * 体检档案数据同步
 */
@Service("checkDataSyncService")
@Slf4j
public class CheckDataSyncServiceImpl implements CheckDataSyncService{

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
    TdPatientBaseCheckRelService tdPatientBaseCheckRelService;
    @Autowired
    TdInternalMedicineExamService tdInternalMedicineExamService;
    @Autowired
    TdSurgicalExamService tdSurgicalExamService;
    @Autowired
    TdOtolaryngologyService tdOtolaryngologyService;
    @Autowired
    TdOphthalmologicalExamService tdOphthalmologicalExamService;
    @Autowired
    TdOralService tdOralService;
    @Autowired
    TdLiverFunctionItemsService tdLiverFunctionItemsService;
    @Autowired
    TdBloodFatItemsService tdBloodFatItemsService;
    @Autowired
    TdCheckTestService tdCheckTestService;
    @Autowired
    TdCoagulationItemsService tdCoagulationItemsService;
    @Autowired
    TdHepatitisBItemsService tdHepatitisBItemsService;
    @Autowired
    TdRenalFunctionItemsService tdRenalFunctionItemsService;
    @Autowired
    TdAuxiliaryExaminationOtherService tdAuxiliaryExaminationOtherService;

    @Autowired
    HybService hybService;
    @Autowired
    JcxxService jcxxService;
    @Autowired
    JcbService jcbService;
    @Autowired
    JcmxxService jcmxxService;
    @Autowired
    JlbService jlbService;

    /**
     * 检查信息
     */
    private static boolean syncJcxxNext = true;
    private static boolean syncJcbNext = true;
    private static boolean syncHybNext = true;
    private static boolean syncJlbNext = true;

    public static String formatStrShort = "yyyy-MM-dd";
    public static String formatStrTime = "yyyy-MM-dd HH:mm:ss";

    private static double pageSize = 300.00;
    private static int saveSize = 300;

    /**
     * 检查信息表
     * @param studyId
     * @return
     */
    @Override
    public String syncJcxx(String studyId, String startDate, String endDate,String endStudyId) throws Exception {
        log.info("syncJcxx同步开始 studyId={},startDate={},endDate={}",studyId,startDate,endDate);
        //记录异常信息
        TdSyncErrorInfo tdSyncErrorInfo = new TdSyncErrorInfo();
        String tableName = "jcxx";
        try{

            //先从同步信息表中查询出已经同步的最新数据
            Map checkNextInfo = this.checkNextInfo(tableName,startDate,endDate);
            if("false".equals(checkNextInfo.get("nextFlag"))){
                log.error("参数错误，无数据同步开始或结束日期，tableName={}",tableName);
                return "参数错误，无数据同步开始或结束日期";
            }

            TdSyncDataInfo tdSyncDataInfo = (TdSyncDataInfo)checkNextInfo.get("tdSyncDataInfo");
            //取数据同步记录表中记录的下一天日期
            String recordDateStr = tdSyncDataInfo.getRecordDate();
            log.info("syncJcxx同步进行中 tdSyncDataInfo={}", JSONUtil.toJsonStr(tdSyncDataInfo));

            //如果传入的studyId不为空，则以传入参数为准
            if(StringUtils.isNotBlank(studyId)){
                tdSyncDataInfo.setDataRecords(studyId);
            }

            //分页查询检查信息
            Jcxx jcxx = null;
            while (syncJcxxNext) {

                //判断是否结束同步
                if(StringUtils.isNotBlank(endStudyId) &&
                        Integer.parseInt(tdSyncDataInfo.getDataRecords())>Integer.parseInt(endStudyId)){
                    syncJcxxNext = false;
                    continue;
                }

                jcxx = new Jcxx();
                jcxx.setId(tdSyncDataInfo.getDataRecords());
                PageRequest pageRequest = PageRequest.of(0,(int)pageSize);
                List<Jcxx> list = jcxxService.querySyncInfoByPage(jcxx,pageRequest);

                List<PatientBaseInfo> pbList = new ArrayList<>();
                List<TdPatientBaseCheckRel> pbrList = new ArrayList<>();
                if(list != null && list.size()>0){
                    for(Jcxx jc : list){
                        PatientBaseInfo patientBaseInfo = patientBaseInfoService.queryByIdCard(jc.getSfzh());
                        if(patientBaseInfo ==null){
                            //新增
                            patientBaseInfo = new PatientBaseInfo();
                            patientBaseInfo.setName(jc.getXm());
                            //年龄，根据身份证号计算
                            patientBaseInfo.setBirthday(IdcardUtil.getBirthDate(jc.getSfzh()).toString(formatStrShort));
                            patientBaseInfo.setAge(IdcardUtil.getAgeByIdCard(jc.getSfzh()));
                            if("男".equals(jc.getXb())){
                                patientBaseInfo.setSex(1);
                            }else if("女".equals(jc.getXb())){
                                patientBaseInfo.setSex(2);
                            }else{
                                patientBaseInfo.setSex(0);
                            }
                            patientBaseInfo.setIdCard(jc.getSfzh());
                            patientBaseInfo.setMobile(jc.getLxdh());
                            patientBaseInfo.setNation(jc.getMz());

                            if("已婚".equals(jc.getHf())){
                                patientBaseInfo.setMaritalStatus("1");
                            }else if("未婚".equals(jc.getHf())){
                                patientBaseInfo.setMaritalStatus("2");
                            }else{
                                patientBaseInfo.setMaritalStatus("0");
                            }

                            patientBaseInfo.setCreateTime(new Date());
                            pbList.add(patientBaseInfo);

                        }

                        //处理中间表信息
                        TdPatientBaseCheckRel pbr = new TdPatientBaseCheckRel();
                        pbr.setStudyId(jc.getId());
                        pbr.setIdCardNo(jc.getSfzh());
                        pbr.setStudyDate(jc.getCyrq());
                        pbr.setCheckDate(jc.getCyrq());
                        pbr.setCreateTime(new Date());
                        pbrList.add(pbr);

                        //记录当期同步的数据
                        tdSyncDataInfo.setDataRecords(jc.getId());

                    }

                    if(pbList!=null && pbList.size()>0){
                        //保存病人信息
                        this.batchSaveInfos(patientBaseInfoService,"insertBatch",pbList);
                    }
                    if(pbrList!=null && pbrList.size()>0){
                        //保存关联关系表
                        this.batchSaveInfos(tdPatientBaseCheckRelService,"insertBatch",pbrList);
                    }

                }else{
                    //无数据跳出循环
                    syncJcxxNext = false;
                }
            }

            //插入之后保存同步记录
            if(tdSyncDataInfo.getId()==null){
                tdSyncDataInfoService.insert(tdSyncDataInfo);
            }else{
                tdSyncDataInfo.setUpdateTime(new Date());
                tdSyncDataInfoService.update(tdSyncDataInfo);
            }

        }catch (Exception e){
            log.error(e.getMessage());
            tdSyncErrorInfo.setTableName(tableName);
            tdSyncErrorInfo.setErrorInfo(e.getMessage());
            tdSyncErrorInfo.setCreateTime(new Date());
            tdSyncErrorInfo.setUpdateTime(new Date());
            tdSyncErrorInfoService.insert(tdSyncErrorInfo);
            throw e;
        }finally {
            syncJcxxNext = true;
        }

        return "同步成功";
    }


    private Map checkNextInfo(String tableName,String startDate,String endDate){
        Map rtMap = new HashMap();
        TdSyncDataInfo tdSyncDataInfo = tdSyncDataInfoService.queryByTableName(tableName);
        if(tdSyncDataInfo==null || StringUtils.isBlank(tdSyncDataInfo.getRecordDate())){
            //如果不存在表的记录，则创建并保存
            tdSyncDataInfo = new TdSyncDataInfo();
            //为空默认从0开始
            tdSyncDataInfo.setDataRecords("0");
            tdSyncDataInfo.setRecordDate("2001-01-01");
            tdSyncDataInfo.setTableName(tableName);
            tdSyncDataInfo.setCreateTime(new Date());
        }
        if(StringUtils.isNotBlank(startDate)){
            tdSyncDataInfo.setRecordDate(startDate);
        }
        rtMap.put("tdSyncDataInfo",tdSyncDataInfo);

        if(StringUtils.isBlank(tdSyncDataInfo.getRecordDate()) || StringUtils.isBlank(endDate)){
            log.info("参数错误，无数据同步开始或结束日期，tableName={}",tableName);
            rtMap.put("nextFlag","false");
        }

        if(endDate.length()!=10){
            log.info("参数错误，开始或结束日期格式不正确，tableName={}",tableName);
            rtMap.put("nextFlag","false");
        }

        return rtMap;
    }

    /**
     * 检查信息表
     * @param studyId
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public String syncJcb(String studyId, String startDate, String endDate) throws Exception {

        log.info("syncJcb同步开始 studyId={},startDate={},endDate={}",studyId,startDate,endDate);
        //记录异常信息
        TdSyncErrorInfo tdSyncErrorInfo = new TdSyncErrorInfo();
        String tableName = "jcb";
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
            log.info("syncJcb同步进行中 tdSyncDataInfo={}", JSONUtil.toJsonStr(tdSyncDataInfo));

            //如果传入的studyId不为空，则以传入参数为准
            if (StringUtils.isNotBlank(studyId)) {
                tdSyncDataInfo.setDataRecords(studyId);
            }

            while (syncJcbNext) {
                //查询当天数据总数
                Jcb jcb = new Jcb();
                Map<String,Object> params = new HashMap<>();
                //分页查询检查信息
                params.put("recordingDate",recordDateStr);
                jcb.setParams(params);

                Date rDate = DateUtil.parse(recordDateStr,formatStrShort);
                if(DateUtil.compare(rDate,new Date(),formatStrShort)>=0 || recordDateStr.equals(endDate)){
                    //如果rDate大于等于当前时间，则不再进行循环
                    syncJcbNext = false;
                    continue;
                }

                long count = jcbService.querySyncInfoCount(jcb);
                if(count==0){
                    log.info("syncJcb当天无数据jcb={}",JSONUtil.toJsonStr(jcb));
                    //循环进行下一天的数据
                    recordDateStr = getDateStr(recordDateStr,1);
                    continue;
                }
                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();
                //存放对应表数据
                //循环分页查询recordDateStr当天的数据
                for (int i = 0; i < totalPage; i++) {
                    List<Jcb> list = jcbService.querySyncInfoByPage(jcb,PageRequest.of(i, (int) pageSize), count).getContent();
                    if(list!=null && list.size()>0){
                        for(Jcb jc : list){
                            this.getJcbInfos(jc);
                        }
                    }
                }
                log.info("当天的数据处理完成recordDateStr={}",recordDateStr);
                //保存数据
                log.info("当天的数据处理完成generaMap.size={}",generaMap.size());
                this.batchSaveInfos(tdGeneralInspectionService,"insertBatchByMaps",generaMap.values().stream().collect(Collectors.toList()));
                log.info("当天的数据处理完成internalMedicineExamMap.size={}",internalMedicineExamMap.size());
                this.batchSaveInfos(tdInternalMedicineExamService,"insertBatchByMaps",internalMedicineExamMap.values().stream().collect(Collectors.toList()));
                log.info("当天的数据处理完成surgicalExamMap.size={}",surgicalExamMap.size());
                this.batchSaveInfos(tdSurgicalExamService,"insertBatchByMaps",surgicalExamMap.values().stream().collect(Collectors.toList()));
                log.info("当天的数据处理完成otolaryngologyMap.size={}",otolaryngologyMap.size());
                this.batchSaveInfos(tdOtolaryngologyService,"insertBatchByMaps",otolaryngologyMap.values().stream().collect(Collectors.toList()));
                log.info("当天的数据处理完成ophthalmologicalExamMap.size={}",ophthalmologicalExamMap.size());
                this.batchSaveInfos(tdOphthalmologicalExamService,"insertBatchByMaps",ophthalmologicalExamMap.values().stream().collect(Collectors.toList()));
                log.info("当天的数据处理完成oralMap.size={}",oralMap.size());
                this.batchSaveInfos(tdOralService,"insertBatchByMaps",oralMap.values().stream().collect(Collectors.toList()));
                log.info("当天的数据处理完成auxiliaryExamOtherMap.size={}",auxiliaryExamOtherMap.size());
                this.batchSaveInfos(tdAuxiliaryExaminationOtherService,"insertBatchByMaps",auxiliaryExamOtherMap.values().stream().collect(Collectors.toList()));
                //------------------------------------------------------------//

                //记录当期同步的数据
                tdSyncDataInfo.setRecordDate(recordDateStr);

                //循环进行下一天的数据
                recordDateStr = getDateStr(recordDateStr,1);

                //当天数据处理结束后，清空对应map集合的数据
                generaMap.clear();
                internalMedicineExamMap.clear();
                surgicalExamMap.clear();
                otolaryngologyMap.clear();
                ophthalmologicalExamMap.clear();
                oralMap.clear();
            }

            //插入之后保存同步记录
            if(tdSyncDataInfo.getId()==null){
                tdSyncDataInfoService.insert(tdSyncDataInfo);
            }else{
                tdSyncDataInfo.setUpdateTime(new Date());
                tdSyncDataInfoService.update(tdSyncDataInfo);
            }

        }catch (Exception e){
            log.error(e.getMessage());
            tdSyncErrorInfo.setTableName(tableName);
            tdSyncErrorInfo.setErrorInfo(e.getMessage());
            tdSyncErrorInfo.setCreateTime(new Date());
            tdSyncErrorInfo.setUpdateTime(new Date());
            tdSyncErrorInfoService.insert(tdSyncErrorInfo);
            throw e;
        }finally {
            syncJcbNext = true;
        }

        return "同步成功";
    }

    /**
     * 一般检查
     */
    static Map<String,Map> generaMap = new HashMap<>();
    /**
     * 内科检查
     */
    static Map<String,Map> internalMedicineExamMap = new HashMap<>();
    /**
     * 外科检查
     */
    static Map<String,Map> surgicalExamMap = new HashMap<>();
    /**
     * 耳鼻喉检查
     */
    static Map<String,Map> otolaryngologyMap = new HashMap<>();
    /**
     * 眼科检查
     */
    static Map<String,Map> ophthalmologicalExamMap = new HashMap<>();
    /**
     * 口腔科检查
     */
    static Map<String,Map> oralMap = new HashMap<>();    /**
     * 辅助检查
     */
    static Map<String,Map> auxiliaryExamOtherMap = new HashMap<>();
    /**
     * 获取检查表的信息并进行数据组装
     * @param jcb
     */
    private void getJcbInfos(Jcb jcb){
        String key = jcb.getStudyid();
        if(TdCheckConstant.generalKeys.containsKey(jcb.getXxdm())){
            //一般检查
            Map map = this.getKeysInfo(jcb,generaMap.get(key),TdCheckConstant.generalKeys);
            generaMap.put(key,map);
        }else if(TdCheckConstant.internalMedicineExamKeys.containsKey(jcb.getXxdm())){
            //内科检查
            Map map = this.getKeysInfo(jcb,internalMedicineExamMap.get(key),TdCheckConstant.internalMedicineExamKeys);
            internalMedicineExamMap.put(key,map);
        }else if(TdCheckConstant.surgicalExamKeys.containsKey(jcb.getXxdm())){
            //外科检查
            Map map = this.getKeysInfo(jcb,surgicalExamMap.get(key),TdCheckConstant.surgicalExamKeys);
            surgicalExamMap.put(key,map);
        }else if(TdCheckConstant.otolaryngologyKeys.containsKey(jcb.getXxdm())){
            //耳鼻喉检查
            Map map = this.getKeysInfo(jcb,otolaryngologyMap.get(key),TdCheckConstant.otolaryngologyKeys);
            otolaryngologyMap.put(key,map);
        }else if(TdCheckConstant.ophthalmologicalExamKeys.containsKey(jcb.getXxdm())){
            //眼科检查
            Map map = this.getKeysInfo(jcb,ophthalmologicalExamMap.get(key),TdCheckConstant.ophthalmologicalExamKeys);
            ophthalmologicalExamMap.put(key,map);
        }else if(TdCheckConstant.oralKeys.containsKey(jcb.getXxdm())){
            //口腔科检查
            Map map = this.getKeysInfo(jcb,oralMap.get(key),TdCheckConstant.oralKeys);
            oralMap.put(key,map);
        }else if(TdCheckConstant.auxiliaryExamOtherKeys.containsKey(jcb.getXxdm())){
            //其他检查
            Map map = this.getKeysInfo(jcb,auxiliaryExamOtherMap.get(key),TdCheckConstant.auxiliaryExamOtherKeys);
            auxiliaryExamOtherMap.put(key,map);
        }
    }

    /**
     * 通过枚举类获取对应表的数据
     * @param jcb
     * @param map 要保存的对应表的map
     * @param keys 对应表的映射map
     * @return
     */
    private Map getKeysInfo(Jcb jcb,Map map,Map keys){
        if(map == null){
            map = new HashMap();
            map.put("createTime",new Date());
            map.put("studyId",jcb.getStudyid());
        }
        String value = jcb.getCvalue();
        if(StringUtils.isBlank(value)){
            value = StringUtils.isNotBlank(jcb.getNvalue())?jcb.getNvalue():jcb.getMvalue();
        }
        map.put(keys.get(jcb.getXxdm()),value);
        map.put("studyDate",jcb.getStudydate());
        return map;
    }

    /**
     * 获取日期偏移后日期字符串
     * @param recordDateStr yyyy-MM-dd
     * @param offset
     * @return
     */
    private String getDateStr(String recordDateStr,int offset){
        Date rDate = DateUtil.parse(recordDateStr, formatStrShort);
        return DateUtil.format(DateUtil.offsetDay(rDate, offset), formatStrShort);
    }

    /**
     * 同步化验表
     * @param studyId
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public String syncHyb(String studyId, String startDate, String endDate) throws Exception {
        log.info("syncHyb同步开始 studyId={},startDate={},endDate={}",studyId,startDate,endDate);
        //记录异常信息
        TdSyncErrorInfo tdSyncErrorInfo = new TdSyncErrorInfo();
        String tableName = "hyb";
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
            log.info("syncHyb同步进行中 tdSyncDataInfo={}", JSONUtil.toJsonStr(tdSyncDataInfo));

            //如果传入的studyId不为空，则以传入参数为准
            if (StringUtils.isNotBlank(studyId)) {
                tdSyncDataInfo.setDataRecords(studyId);
            }

            Map<String, Object> params = new HashMap<>();
            Hyb hyb = new Hyb();
            //分页查询化验表信息
            while (syncHybNext) {
                //查询当天数据总数
                params.put("recordingDate", recordDateStr);
                hyb.setParams(params);

                Date rDate = DateUtil.parse(recordDateStr, formatStrShort);
                if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || recordDateStr.equals(endDate)) {
                    //如果rDate大于等于当前时间，则不再进行循环
                    syncHybNext = false;
                    continue;
                }

                long count = hybService.querySyncInfoCount(hyb);
                if (count == 0) {
                    log.info("syncHyb当天无数据jcb={}", JSONUtil.toJsonStr(hyb));
                    //循环进行下一天的数据
                    recordDateStr = getDateStr(recordDateStr, 1);
                    continue;
                }

                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();
                //存放对应表数据
                //循环分页查询recordDateStr当天的数据
                for (int i = 0; i < totalPage; i++) {
                    List<Hyb> list = hybService.querySyncInfoByPage(hyb,PageRequest.of(i, (int) pageSize), count).getContent();
                    list.stream().forEach(hy -> {
                        this.getHybInfos(hy);
                    });
                }

                log.info("saveHybInfos 当天的数据处理完成recordDateStr={}",recordDateStr);
                //保存处理好的数据
                this.saveHybInfos();

                //记录当期同步的数据
                tdSyncDataInfo.setRecordDate(recordDateStr);

                //循环进行下一天的数据
                recordDateStr = getDateStr(recordDateStr,1);

            }
            //插入之后保存同步记录
            if(tdSyncDataInfo.getId()==null){
                tdSyncDataInfoService.insert(tdSyncDataInfo);
            }else{
                tdSyncDataInfo.setUpdateTime(new Date());
                tdSyncDataInfoService.update(tdSyncDataInfo);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            tdSyncErrorInfo.setTableName(tableName);
            tdSyncErrorInfo.setErrorInfo(e.getMessage());
            tdSyncErrorInfo.setCreateTime(new Date());
            tdSyncErrorInfo.setUpdateTime(new Date());
            tdSyncErrorInfoService.insert(tdSyncErrorInfo);
            throw e;
        }finally {
            syncHybNext = true;
        }

        return "同步成功";
    }

    /**
     * 保存化验表
     */
    private void saveHybInfos() throws Exception{
        log.info("保存血常规信息 size={}",bloodRoutineMap.size());
        if(bloodRoutineMap.size()!=0){
            this.batchSaveInfos(tdBloodRoutineService,"insertBatchByMaps",bloodRoutineMap.values().stream().collect(Collectors.toList()));
        }
        log.info("保存尿常规信息 size={}",urineRoutineMap.size());
        if(urineRoutineMap.size()!=0){
            this.batchSaveInfos(tdUrineRoutineService,"insertBatchByMaps",urineRoutineMap.values().stream().collect(Collectors.toList()));
        }
        log.info("保存甲状腺信息 size={}",thyroidMap.size());
        if(thyroidMap.size()!=0){
            this.batchSaveInfos(tdThyroidFunctionItemsService,"insertBatchByMaps",thyroidMap.values().stream().collect(Collectors.toList()));
        }
        log.info("保存激素六项信息 size={}",hormoneMap.size());
        if(hormoneMap.size()!=0){
            this.batchSaveInfos(tdHormoneItemsService,"insertBatchByMaps",hormoneMap.values().stream().collect(Collectors.toList()));
        }
        log.info("保存肿瘤信息 size={}",tumorMap.size());
        if(tumorMap.size()!=0){
            this.batchSaveInfos(tdTumorScreeningItemsService,"insertBatchByMaps",tumorMap.values().stream().collect(Collectors.toList()));
        }
        log.info("保存心肌酶信息 size={}",myocardiaMap.size());
        if(myocardiaMap.size()!=0){
            this.batchSaveInfos(tdMyocardiaEnzymeService,"insertBatchByMaps",myocardiaMap.values().stream().collect(Collectors.toList()));
        }
        log.info("保存肝功能信息 size={}",liverMap.size());
        if(liverMap.size()!=0){
            this.batchSaveInfos(tdLiverFunctionItemsService,"insertBatchByMaps",liverMap.values().stream().collect(Collectors.toList()));
        }
        log.info("保存肾功能信息 size={}",renalMap.size());
        if(renalMap.size()!=0){
            this.batchSaveInfos(tdRenalFunctionItemsService,"insertBatchByMaps",renalMap.values().stream().collect(Collectors.toList()));
        }
        log.info("保存乙肝信息 size={}",hepatitisMap.size());
        if(hepatitisMap.size()!=0){
            this.batchSaveInfos(tdHepatitisBItemsService,"insertBatchByMaps",hepatitisMap.values().stream().collect(Collectors.toList()));
        }
        log.info("保存血脂信息 size={}",bloodFatMap.size());
        if(bloodFatMap.size()!=0){
            this.batchSaveInfos(tdBloodFatItemsService,"insertBatchByMaps",bloodFatMap.values().stream().collect(Collectors.toList()));
        }
        log.info("保存凝血项目信息 size={}",coagulationMap.size());
        if(coagulationMap.size()!=0){
            this.batchSaveInfos(tdCoagulationItemsService,"insertBatchByMaps",coagulationMap.values().stream().collect(Collectors.toList()));
        }
        log.info("保存体检检验表信息 size={}",checkTestMap.size());
        if(checkTestMap.size()!=0){
            this.batchSaveInfos(tdCheckTestService,"insertBatchByMaps",checkTestMap.values().stream().collect(Collectors.toList()));
        }

        //当天数据处理结束后，清空对应map集合的数据
        bloodRoutineMap.clear();
        urineRoutineMap.clear();
        thyroidMap.clear();
        hormoneMap.clear();
        tumorMap.clear();
        myocardiaMap.clear();
        liverMap.clear();
        renalMap.clear();
        hepatitisMap.clear();
        bloodFatMap.clear();
        coagulationMap.clear();
        checkTestMap.clear();
    }

    /**
     * 血常规
     */
    static Map<String,Map> bloodRoutineMap = new HashMap<>();
    /**
     * 尿常规
     */
    static Map<String,Map> urineRoutineMap = new HashMap<>();
    /**
     * 甲状腺
     */
    static Map<String,Map> thyroidMap = new HashMap<>();
    /**
     * 激素六项
     */
    static Map<String,Map> hormoneMap = new HashMap<>();
    /**
     * 肿瘤
     */
    static Map<String,Map> tumorMap = new HashMap<>();
    /**
     * 心肌酶
     */
    static Map<String,Map> myocardiaMap = new HashMap<>();
    /**
     * 肝功能
     */
    static Map<String,Map> liverMap = new HashMap<>();
    /**
     * 肾功能
     */
    static Map<String,Map> renalMap = new HashMap<>();
    /**
     * 乙肝
     */
    static Map<String,Map> hepatitisMap = new HashMap<>();
    /**
     * 血脂
     */
    static Map<String,Map> bloodFatMap = new HashMap<>();
    /**
     * 凝血项目
     */
    static Map<String,Map> coagulationMap = new HashMap<>();
    /**
     * 体检检验表
     */
    static Map<String,Map> checkTestMap = new HashMap<>();

    /**
     * 获取化验表的信息并进行数据组装
     * @param hyb
     */
    private void getHybInfos(Hyb hyb){
        String key = hyb.getStudyid();
        if(TdCheckConstant.bloodRoutineKeys.containsKey(hyb.getXxdm())){
            //血常规
            Map map = this.getHybKeysInfo(hyb,bloodRoutineMap.get(key),TdCheckConstant.bloodRoutineKeys);
            bloodRoutineMap.put(key,map);
        }else if(TdCheckConstant.urineRoutineKeys.containsKey(hyb.getXxdm())){
            //尿常规
            Map map = this.getHybKeysInfo(hyb,urineRoutineMap.get(key),TdCheckConstant.urineRoutineKeys);
            urineRoutineMap.put(key,map);
        }else if(TdCheckConstant.thyroidKeys.containsKey(hyb.getXxdm())){
            //甲状腺
            Map map = this.getHybKeysInfo(hyb,thyroidMap.get(key),TdCheckConstant.thyroidKeys);
            thyroidMap.put(key,map);
        }else if(TdCheckConstant.hormoneKeys.containsKey(hyb.getXxdm())){
            //激素六项
            Map map = this.getHybKeysInfo(hyb,hormoneMap.get(key),TdCheckConstant.hormoneKeys);
            hormoneMap.put(key,map);
        }else if(TdCheckConstant.tumorKeys.containsKey(hyb.getXxdm())){
            //肿瘤
            Map map = this.getHybKeysInfo(hyb,tumorMap.get(key),TdCheckConstant.tumorKeys);
            tumorMap.put(key,map);
        }else if(TdCheckConstant.myocardiaKeys.containsKey(hyb.getXxdm())){
            //心肌酶
            Map map = this.getHybKeysInfo(hyb,myocardiaMap.get(key),TdCheckConstant.myocardiaKeys);
            myocardiaMap.put(key,map);
        }else if(TdCheckConstant.liverKeys.containsKey(hyb.getXxdm())){
            //肝功能
            Map map = this.getHybKeysInfo(hyb,liverMap.get(key),TdCheckConstant.liverKeys);
            liverMap.put(key,map);
        }else if(TdCheckConstant.renalKeys.containsKey(hyb.getXxdm())){
            //肾功能
            Map map = this.getHybKeysInfo(hyb,renalMap.get(key),TdCheckConstant.renalKeys);
            renalMap.put(key,map);
        }else if(TdCheckConstant.hepatitisKeys.containsKey(hyb.getXxdm())){
            //乙肝
            Map map = this.getHybKeysInfo(hyb,hepatitisMap.get(key),TdCheckConstant.hepatitisKeys);
            hepatitisMap.put(key,map);
        }else if(TdCheckConstant.bloodFatKeys.containsKey(hyb.getXxdm())){
            //血脂
            Map map = this.getHybKeysInfo(hyb,bloodFatMap.get(key),TdCheckConstant.bloodFatKeys);
            bloodFatMap.put(key,map);
        }else if(TdCheckConstant.coagulationKeys.containsKey(hyb.getXxdm())){
            //凝血项目
            Map map = this.getHybKeysInfo(hyb,coagulationMap.get(key),TdCheckConstant.coagulationKeys);
            coagulationMap.put(key,map);
        }else if(TdCheckConstant.checkTestKeys.containsKey(hyb.getXxdm())){
            //体检检验表
            Map map = this.getHybKeysInfo(hyb,checkTestMap.get(key),TdCheckConstant.checkTestKeys);
            checkTestMap.put(key,map);
        }

    }
    /**
     * 通过枚举类获取对应表的数据
     * @param hyb
     * @param map 要保存的对应表的map
     * @param keys 对应表的映射map
     * @return
     */
    private Map getHybKeysInfo(Hyb hyb,Map map,Map keys){
        if(map == null){
            map = new HashMap();
            map.put("createTime",new Date());
            map.put("studyId",hyb.getStudyid());
        }
        map.put(keys.get(hyb.getXxdm()),hyb.getItemresult());
        if("1".equals(hyb.getFlag())){
            //指标低
            String lowVals = String.valueOf(map.get("abnormalIndicatorLow"));
            if(StringUtils.isBlank(lowVals)){
                map.put("abnormalIndicatorLow",keys.get(hyb.getXxdm()));
            }else{
                map.put("abnormalIndicatorLow",keys.get(hyb.getXxdm())+","+ lowVals);
            }
        }else if("2".equals(hyb.getFlag())){
            //指标高
            String highVals = String.valueOf(map.get("abnormalIndicatorHigh"));
            if(StringUtils.isBlank(highVals)){
                map.put("abnormalIndicatorHigh",keys.get(hyb.getXxdm()));
            }else{
                map.put("abnormalIndicatorHigh",keys.get(hyb.getXxdm())+","+ highVals);
            }
        }

        String checkDate = hyb.getCheckdate();
        String checkTime = hyb.getChecktime();
        if(map.get("resultTime")==null && StringUtils.isNotBlank(checkDate)
                && StringUtils.isNotBlank(checkTime)
            && checkDate.length()== 10 && checkTime.length() == 8 ){
            Date resultTime = DateUtil.parse(checkDate+" "+checkTime);
            map.put("resultTime",resultTime);
        }
        return map;
    }

    /**
     * 同步结论表
     * @param studyId
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public String syncJlb(String studyId, String startDate, String endDate) throws Exception {
        log.info("syncJlb同步开始 studyId={},startDate={},endDate={}",studyId,startDate,endDate);
        //记录异常信息
        TdSyncErrorInfo tdSyncErrorInfo = new TdSyncErrorInfo();
        String tableName = "jlb";
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
            log.info("syncJlb同步进行中 tdSyncDataInfo={}", JSONUtil.toJsonStr(tdSyncDataInfo));

            //如果传入的studyId不为空，则以传入参数为准
            if (StringUtils.isNotBlank(studyId)) {
                tdSyncDataInfo.setDataRecords(studyId);
            }

            //分页查询结论表信息
            while (syncJlbNext) {
                Map<String, Object> params = new HashMap<>();
                Jlb jlb = new Jlb();
                //查询当天数据总数
                params.put("recordingDate", recordDateStr);
                jlb.setParams(params);

                Date rDate = DateUtil.parse(recordDateStr, formatStrShort);
                if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || recordDateStr.equals(endDate)) {
                    //如果rDate大于等于当前时间，则不再进行循环
                    syncJlbNext = false;
                    continue;
                }

                long count = jlbService.querySyncInfoCount(jlb);
                if (count == 0) {
                    log.info("syncJlb当天无数据jcb={}", JSONUtil.toJsonStr(jlb));
                    //循环进行下一天的数据
                    recordDateStr = getDateStr(recordDateStr, 1);
                    continue;
                }

                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();
                //存放对应表数据
                List<TdDiagnosis> insertList = new ArrayList<>();
                for (int i = 0; i < totalPage; i++) {
                    List<Jlb> list = jlbService.querySyncInfoByPage(jlb,PageRequest.of(i, (int) pageSize), count).getContent();
                    list.stream().forEach(jl -> {
                        TdDiagnosis td = new TdDiagnosis();
                        td.setStudyId(jl.getStudyid());
                        td.setResultDate(jl.getZjrq());
                        td.setDiagnosisDesc(jl.getZjjl());
                        td.setDiagnosisDate(jl.getZjrq());
                        td.setFinalCheckDoctor(jl.getZjys());
                        td.setCheckDesc(jl.getJcjg());
                        td.setCheckDate(jl.getJlrq());
                        td.setCheckDoctor(jl.getJlys());
                        td.setHealthAdvice(jl.getYlbj());
                        td.setDietaryAdvice(jl.getJcys());
                        td.setSportsAdvice(jl.getTydl());
                        td.setTreatmentRecommendations(jl.getZljy());
                        td.setCreateTime(new Date());
                        insertList.add(td);
                        tdSyncDataInfo.setDataRecords(jl.getStudyid());
                    });
                }
                //保存体检结论信息
                if(insertList.size()>0){
                    this.batchSaveInfos(tdDiagnosisService,"insertBatch",insertList);
                }

                //记录当期同步的数据
                tdSyncDataInfo.setRecordDate(recordDateStr);

                //循环进行下一天的数据
                recordDateStr = getDateStr(recordDateStr,1);

            }

            //插入之后保存同步记录
            if(tdSyncDataInfo.getId()==null){
                tdSyncDataInfoService.insert(tdSyncDataInfo);
            }else{
                tdSyncDataInfo.setUpdateTime(new Date());
                tdSyncDataInfoService.update(tdSyncDataInfo);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            tdSyncErrorInfo.setTableName(tableName);
            tdSyncErrorInfo.setErrorInfo(e.getMessage());
            tdSyncErrorInfo.setCreateTime(new Date());
            tdSyncErrorInfo.setUpdateTime(new Date());
            tdSyncErrorInfoService.insert(tdSyncErrorInfo);
            throw e;
        }finally {
            syncJlbNext = true;
        }

        return "同步成功";
    }

    /**
     * 按固定长度条数批量保存数据
     * @param obj 调用的类
     * @param methodName 调用方法
     * @param list 保存的数据集
     * @throws Exception
     */
    private void batchSaveInfos(Object obj,String methodName,List list) throws Exception{
        try{
            log.info("batchSaveInfos 保存数据：obj={},list.size()={}",obj.getClass().getName(),list.size());
            if(list!=null && list.size()>0){
                Method method = obj.getClass().getDeclaredMethod(methodName,List.class);
                List<List> partition = Lists.partition(list , saveSize);
                for(List ls : partition){
                    method.invoke(obj,ls);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }

    }

    private ExecutorService executor = Executors.newFixedThreadPool(5);
    //newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。

    @Override
    public String syncJlbByThred(String studyId, String startDate, String endDate) throws Exception {
        log.info("syncJlb同步开始 studyId={},startDate={},endDate={}", studyId, startDate, endDate);
        //记录异常信息
        TdSyncErrorInfo tdSyncErrorInfo = new TdSyncErrorInfo();
        String tableName = "jlb";
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
            log.info("syncJlb同步进行中 tdSyncDataInfo={}", JSONUtil.toJsonStr(tdSyncDataInfo));

            //如果传入的studyId不为空，则以传入参数为准
            if (StringUtils.isNotBlank(studyId)) {
                tdSyncDataInfo.setDataRecords(studyId);
            }

            //分页查询结论表信息
            while (syncJlbNext) {
                Map<String, Object> params = new HashMap<>();
                Jlb jlb = new Jlb();
                //查询当天数据总数
                params.put("recordingDate", recordDateStr);
                jlb.setParams(params);

                Date rDate = DateUtil.parse(recordDateStr, formatStrShort);
                if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || recordDateStr.equals(endDate)) {
                    //如果rDate大于等于当前时间，则不再进行循环
                    syncJlbNext = false;
                    continue;
                }

                long count = jlbService.querySyncInfoCount(jlb);
                if (count == 0) {
                    log.info("syncJlb当天无数据jcb={}", JSONUtil.toJsonStr(jlb));
                    //循环进行下一天的数据
                    recordDateStr = getDateStr(recordDateStr, 1);
                    continue;
                }
                pageSize = 2;
                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();

                //-----多线程查询--start--
                List<Callable<List>> tasks = new ArrayList<>();
                int offset = 0;
                for (int i = 0; i < totalPage; i++) {
                    Map pms = new HashMap();
                    pms.put("recordingDate",recordDateStr);
                    pms.put("pageSize",(int)pageSize);
                    pms.put("total",(int)count);
                    pms.put("pageNum",i);
                    pms.put("offset",offset);
                    Callable<List> qfe = new ThredQuery(jlbService, "querySyncInfoByPage", pms, offset, (int) pageSize);
                    tasks.add(qfe);
                    offset = offset + num.intValue();
                }
                //-----多线程查询--end--
                List<Future<List>> futures = executor.invokeAll(tasks);
                /******处理线程返回结果******/
                //存放对应表数据
                List<TdDiagnosis> insertList = new ArrayList<>();
                if (futures != null && futures.size() > 0) {
                    for (Future<List> future : futures) {
                        List<Jlb> list = future.get();
                        list.stream().forEach(jl -> {
                            TdDiagnosis td = new TdDiagnosis();
                            td.setStudyId(jl.getStudyid());
                            td.setResultDate(jl.getZjrq());
                            td.setDiagnosisDesc(jl.getZjjl());
                            td.setDiagnosisDate(jl.getZjrq());
                            td.setFinalCheckDoctor(jl.getZjys());
                            td.setCheckDesc(jl.getJcjg());
                            td.setCheckDate(jl.getJlrq());
                            td.setCheckDoctor(jl.getJlys());
                            td.setHealthAdvice(jl.getYlbj());
                            td.setDietaryAdvice(jl.getJcys());
                            td.setSportsAdvice(jl.getTydl());
                            td.setTreatmentRecommendations(jl.getZljy());
                            td.setCreateTime(new Date());
                            insertList.add(td);
                            tdSyncDataInfo.setDataRecords(jl.getStudyid());
                        });
                    }
                }
                //保存体检结论信息
                if(insertList.size()>0){
                    this.batchSaveInfos(tdDiagnosisService,"insertBatch",insertList);
                }

                //记录当期同步的数据
                tdSyncDataInfo.setRecordDate(recordDateStr);
                //循环进行下一天的数据
                recordDateStr = getDateStr(recordDateStr,1);
            }

            //插入之后保存同步记录
            if(tdSyncDataInfo.getId()==null){
                tdSyncDataInfoService.insert(tdSyncDataInfo);
            }else{
                tdSyncDataInfo.setUpdateTime(new Date());
                tdSyncDataInfoService.update(tdSyncDataInfo);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            tdSyncErrorInfo.setTableName(tableName);
            tdSyncErrorInfo.setErrorInfo(e.getMessage());
            tdSyncErrorInfo.setCreateTime(new Date());
            tdSyncErrorInfo.setUpdateTime(new Date());
            tdSyncErrorInfoService.insert(tdSyncErrorInfo);
            throw e;
        }finally {
            syncJlbNext = true;
        }

        return "同步成功";
    }


}
