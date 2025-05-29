package com.dchealth.medical.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.system.mapper.SysUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.account.domain.AccountNotice;
import com.dchealth.account.mapper.AccountNoticeMapper;
import com.dchealth.common.config.DchealthConfig;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.http.HttpClientUtil;
import com.dchealth.common.utils.http.XfServiceHttpClientUtil;
import com.dchealth.medical.contants.MedicalContants;
import com.dchealth.medical.domain.MpPerson;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.PatientRecordOutbound;
import com.dchealth.medical.domain.PatientRecordTemplate;
import com.dchealth.medical.domain.form.TreeForm;
import com.dchealth.medical.domain.vo.OutboundTree;
import com.dchealth.medical.domain.vo.OutboundWorkListVO;
import com.dchealth.medical.mapper.MpPersonMapper;
import com.dchealth.medical.mapper.PatientInfoMapper;
import com.dchealth.medical.mapper.PatientRecordMapper;
import com.dchealth.medical.mapper.PatientRecordOutboundMapper;
import com.dchealth.medical.mapper.PatientRecordTemplateMapper;
import com.dchealth.medical.service.IPatientRecordOutboundService;
import com.dchealth.medical.vo.xunfei.BatchListReq;
import com.dchealth.medical.vo.xunfei.BatchListRes;
import com.dchealth.medical.vo.xunfei.Dweller;
import com.dchealth.medical.vo.xunfei.Recall;
import com.dchealth.medical.vo.xunfei.RecordDetailReq;
import com.dchealth.medical.vo.xunfei.RecordDetailRes;
import com.dchealth.medical.vo.xunfei.TaskRelationVo;
import com.dchealth.medical.vo.xunfei.Var;
import com.dchealth.medical.vo.xunfei.XunfeiReq;
import com.dchealth.medical.vo.xunfei.XunfeiRes;
import com.dchealth.research.domain.ResearchDiseases;
import com.dchealth.research.domain.ResearchInfo;
import com.dchealth.research.domain.ResearchOutbound;
import com.dchealth.research.mapper.ResearchDiseasesMapper;
import com.dchealth.research.mapper.ResearchInfoMapper;
import com.dchealth.research.mapper.ResearchOutboundMapper;
import com.google.common.collect.Lists;

/**
 * 患者参与队列信息Service业务层处理
 * 
 * @author lchbu
 * @date 2023-08-17
 */
@Service
public class PatientRecordOutboundServiceImpl implements IPatientRecordOutboundService{

    private static final Logger log = LoggerFactory.getLogger(PatientRecordOutboundServiceImpl.class);

    @Autowired
    private PatientRecordOutboundMapper patientRecordOutboundMapper;
    @Autowired
    private ResearchInfoMapper researchInfoMapper;

    @Autowired
    private PatientRecordTemplateMapper patientRecordTemplateMapper;
    @Autowired
    private ResearchOutboundMapper researchOutboundMapper;
    @Autowired
    private PatientRecordMapper patientRecordMapper;
    @Autowired
    private PatientInfoMapper patientInfoMapper;
    @Autowired
    private MpPersonMapper mpPersonMapper;
    @Autowired
    private ResearchDiseasesMapper researchDiseasesMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private AccountNoticeMapper accountNoticeMapper;

    @Value("${xunfei.requestUrl}")
    private String baseUrl ;

    /**
     * 查询患者参与队列信息
     * 
     * @param id 患者参与队列信息主键
     * @return 患者参与队列信息
     */
    @Override
    public PatientRecordOutbound selectPatientRecordOutboundById(Long id)
    {
        return patientRecordOutboundMapper.selectPatientRecordOutboundById(id);
    }

    /**
     * 查询患者参与队列信息列表
     * 
     * @param patientRecordOutbound 患者参与队列信息
     * @return 患者参与队列信息
     */
    @Override
    public List<PatientRecordOutbound> selectPatientRecordOutboundList(PatientRecordOutbound patientRecordOutbound){
        return patientRecordOutboundMapper.selectPatientRecordOutboundList(patientRecordOutbound);
    }

    @Override
    public List<OutboundWorkListVO> selectOutboundList(PatientRecordOutbound patientRecordOutbound){
        List<OutboundWorkListVO> list = patientRecordOutboundMapper.selectOutboundList(patientRecordOutbound);
        return list;
    }

    /**
     * 新增患者参与队列信息
     * 
     * @param patientRecordOutbound 患者参与队列信息
     * @return 结果
     */
    @Override
    public int insertPatientRecordOutbound(PatientRecordOutbound patientRecordOutbound)
    {
        patientRecordOutbound.setCreateTime(DateUtils.getNowDate());
        return patientRecordOutboundMapper.insertPatientRecordOutbound(patientRecordOutbound);
    }

    /**
     * 修改患者参与队列信息
     * 
     * @param patientRecordOutbound 患者参与队列信息
     * @return 结果
     */
    @Override
    public int updatePatientRecordOutbound(PatientRecordOutbound patientRecordOutbound)
    {
        patientRecordOutbound.setUpdateTime(DateUtils.getNowDate());
        return patientRecordOutboundMapper.updatePatientRecordOutbound(patientRecordOutbound);
    }

    /**
     * 批量删除患者参与队列信息
     * 
     * @param ids 需要删除的患者参与队列信息主键
     * @return 结果
     */
    @Override
    public int deletePatientRecordOutboundByIds(Long[] ids)
    {
        return patientRecordOutboundMapper.deletePatientRecordOutboundByIds(ids);
    }

    /**
     * 删除患者参与队列信息信息
     * 
     * @param id 患者参与队列信息主键
     * @return 结果
     */
    @Override
    public int deletePatientRecordOutboundById(Long id)
    {
        return patientRecordOutboundMapper.deletePatientRecordOutboundById(id);
    }

    @Override
    public OutboundTree outboundTree(Long userId) {
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        List<ResearchDiseases> researchDiseasesList = new ArrayList<>();
        if("1".equals(sysUser.getIsAlldeptResearch())){
            researchDiseasesList = researchDiseasesMapper.selectResearchDiseasesByAuth2(sysUser.getDeptId());
        }else{
            researchDiseasesList = researchDiseasesMapper.selectResearchDiseasesByUserId(userId);
        }
        TreeForm treeForm = new TreeForm();
        treeForm.setType("1");
        treeForm.setUserId(userId);
        List<ResearchInfo> researchInfos = researchInfoMapper.selectResearchInfoByUserId(treeForm);
        PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
        //随访
        patientRecordOutbound.setOutboundType("1");
        patientRecordOutbound.setRemindType("1");//1随访2超窗3预警
        patientRecordOutbound.setStatus("1");
        List<OutboundWorkListVO> follow = patientRecordOutboundMapper.selectOutboundList(patientRecordOutbound);
        //超窗
        patientRecordOutbound.setRemindType("2");//1随访2超窗3预警
        List<OutboundWorkListVO> over = patientRecordOutboundMapper.selectOutboundList(patientRecordOutbound);
        //预警
        patientRecordOutbound.setRemindType("3");//1随访2超窗3预警
        List<OutboundWorkListVO> warn = patientRecordOutboundMapper.selectOutboundList(patientRecordOutbound);
        //外呼随访

        Map<String,List<OutboundWorkListVO>> map = new HashMap<>();
        map.put("follow",follow);
        map.put("over",over);
        map.put("warn",warn);
        return new OutboundTree(researchDiseasesList, researchInfos, map);
    }




    @Override
    public String followRecordByResearchId(Long researchId) {
        String resultStr = "操作成功";
        List<PatientRecordOutbound> list = new ArrayList<>();
        insertFollowRecordJob(researchId,list);
        if (!list.isEmpty()){
            patientRecordOutboundMapper.batchInsertList(list);
            resultStr = "操作成功,共插入"+list.size()+"条-----------";
//            System.out.println(resultStr);
        }
        return resultStr;
    }

    public void insertFollowRecordJob(Long researchId,List<PatientRecordOutbound> insertList){
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(researchId);
        ResearchOutbound researchOutbound = researchOutboundMapper.selectByResearchIdAndType(researchId,"1");
        long TimeNow = System.currentTimeMillis();
        Date now = new Date(TimeNow);
        //随访外呼提前提醒
        PatientRecordTemplate pt = new PatientRecordTemplate();
        pt.setResearchId(researchId);
        List<PatientRecordTemplate> patientRecordTemplates = patientRecordTemplateMapper.selectPatientRecordTemplateList(pt);
        if(researchOutbound!=null && researchInfo!=null && "1".equals(researchInfo.getResearchStatus())){
            if(patientRecordTemplates.size()>0){
                patientRecordTemplates.sort(Comparator.comparing(PatientRecordTemplate :: getFollowNum));
                Map<Long, PatientRecordTemplate> maps = patientRecordTemplates.stream().collect(Collectors.toMap(PatientRecordTemplate :: getFollowId, Function.identity()));
                List<PatientRecord> patientRecords = patientRecordMapper.selectMaxRecordByResearchId(researchInfo.getId());
                patientRecords.forEach(x->{
                    if (!maps.containsKey(x.getFollowId())){
                        return;
                    }
                    if(x.getLockVisitTime()==null){
                        return;
                    }
                    PatientRecordOutbound pro = patientRecordOutboundMapper.selectByOutboundAndRemindType(x.getRecordId(),"1","1");
                    if(pro!=null){
                        return;
                    }
                    PatientRecordTemplate recordTemplate = maps.get(x.getFollowId());
                    if(recordTemplate.getFollowNum()==0){
                        //说明当前是基线,下次为第一次生成随访信息
                        int index = patientRecordTemplates.indexOf(recordTemplate);
                        x.setStageAddNum(1);
                        if (index+1 < patientRecordTemplates.size()){
                            if(isCreateFollowRecord(now, x, patientRecordTemplates.get(index + 1),researchOutbound.getFollowAdvanceDays())){
                                insertList.add(patientRecordOutboundFollow(x,researchOutbound,patientRecordTemplates.get(index + 1),"1"));
                            }
                        }
                    }else{
                        x.setStageAddNum(x.getStageAddNum()+1);
                        //限制频次
                        if("1".equals(recordTemplate.getFollowFrequency())){
                            if (recordTemplate.getFollowTotal() == null || recordTemplate.getFollowTotal() < 1){
                                log.info("生成随访计划,队列ID{}随访规则ID{}followTotal没有值", researchInfo.getId(), recordTemplate.getFollowId());
                                return;
                            }
                            if (x.getStageNum().intValue() == recordTemplate.getFollowTotal().intValue()) {//完成当前随访节点进入下一个随访节点
                                int index = patientRecordTemplates.indexOf(recordTemplate);
                                if (index+1 < patientRecordTemplates.size()){
                                    if(isCreateFollowRecord(now, x, patientRecordTemplates.get(index + 1),researchOutbound.getFollowAdvanceDays())){
                                        insertList.add(patientRecordOutboundFollow(x,researchOutbound,patientRecordTemplates.get(index + 1),"1"));
                                    }
                                }
                            }else if (x.getStageNum().intValue() < recordTemplate.getFollowTotal().intValue()){//未完成当前随访节点继续生成当前随访节点计划
                                if(isCreateFollowRecord(now, x, recordTemplate,researchOutbound.getFollowAdvanceDays())){
                                    insertList.add(patientRecordOutboundFollow(x,researchOutbound,recordTemplate,"1"));
                                }
                            }
                        }else if ("2".equals(recordTemplate.getFollowFrequency())){//不限频次
                            //没设置次数且间隔为0天
                            if(recordTemplate.getFollowTotal()==null && recordTemplate.getFollowInterval()==0){
                                int index = patientRecordTemplates.indexOf(recordTemplate);
                                if (index+1 < patientRecordTemplates.size()){
                                    if(isCreateFollowRecord(now, x, patientRecordTemplates.get(index + 1),researchOutbound.getFollowAdvanceDays())){
                                        insertList.add(patientRecordOutboundFollow(x,researchOutbound,patientRecordTemplates.get(index + 1),"1"));
                                    }
                                }
                            }else{
                                if(isCreateFollowRecord(now, x, recordTemplate,researchOutbound.getFollowAdvanceDays())){
                                    insertList.add(patientRecordOutboundFollow(x,researchOutbound,recordTemplate,"1"));
                                }
                            }
                        }
                    }
                });
            }
        }



    }


    //判断生成的下一个随访时间在提前提醒时间，是否在当前日期之前，如果是为true
    public boolean isCreateFollowRecord(Date now, PatientRecord patientRecord, PatientRecordTemplate prt,Integer i) {
        Date nextDate = null;
        if (StringUtils.equals("1", prt.getFollowIntervalUnit())) {
            nextDate = DateUtils.addDays(patientRecord.getLockVisitTime(), prt.getFollowInterval().intValue() - i);
        } else if ("2".equals(prt.getFollowIntervalUnit())) {
            nextDate = DateUtils.addDays(patientRecord.getLockVisitTime(), prt.getFollowInterval().intValue() * 7 - i);
        } else if ("3".equals(prt.getFollowIntervalUnit())) {
            nextDate = DateUtils.addDays(patientRecord.getLockVisitTime(), prt.getFollowInterval().intValue() * 30 - i);
        }
        if (DateUtils.dateTime(now).equals(DateUtils.dateTime(nextDate))) {
            return true;
        }
        return false;
    }

    //外呼短信随访提前提醒
    public PatientRecordOutbound patientRecordOutboundFollow(PatientRecord patientRecord,ResearchOutbound researchOutbound,PatientRecordTemplate prt,String outboundType){
        PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
        patientRecordOutbound.setRecordId(patientRecord.getRecordId());
        patientRecordOutbound.setResearchId(patientRecord.getResearchId());
        patientRecordOutbound.setOutboundId(researchOutbound.getId());
        patientRecordOutbound.setSpeechId(researchOutbound.getFollowSpeechId());
        patientRecordOutbound.setOutboundType(outboundType);//1智能外呼2短信提醒3app站内信
        patientRecordOutbound.setRemindType("1");
        patientRecordOutbound.setStatus("1");
        patientRecordOutbound.setCreateTime(DateUtils.getNowDate());
        patientRecordOutbound.setName("随访"+String.format("%02d", patientRecord.getStageAddNum()));
        patientRecordOutbound.setStageNum(patientRecord.getStageAddNum());

        Date nextDate = null;
        Date planeDate = null;
        if (StringUtils.equals("1", prt.getFollowIntervalUnit())) {
            nextDate = DateUtils.addDays(patientRecord.getLockVisitTime(), prt.getFollowInterval().intValue() - researchOutbound.getFollowAdvanceDays());
            planeDate = DateUtils.addDays(patientRecord.getLockVisitTime(), prt.getFollowInterval().intValue());
        } else if ("2".equals(prt.getFollowIntervalUnit())) {
            nextDate = DateUtils.addDays(patientRecord.getLockVisitTime(), prt.getFollowInterval().intValue() * 7 - researchOutbound.getFollowAdvanceDays());
            planeDate = DateUtils.addDays(patientRecord.getLockVisitTime(), prt.getFollowInterval().intValue() * 7);
        } else if ("3".equals(prt.getFollowIntervalUnit())) {
            nextDate = DateUtils.addDays(patientRecord.getLockVisitTime(), prt.getFollowInterval().intValue() * 30 - researchOutbound.getFollowAdvanceDays());
            planeDate = DateUtils.addDays(patientRecord.getLockVisitTime(), prt.getFollowInterval().intValue() * 30);
        }

        String date1 = DateUtils.dateTime(nextDate);
        if(researchOutbound.getFollowAdvanceTime()!=null){
            patientRecordOutbound.setOutboundTime(DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS,date1+" "+
                    MedicalContants.GET_HOUR_TYPE.get(researchOutbound.getFollowAdvanceTime())+":00:00"));
            patientRecordOutbound.setPlanVisitTime(planeDate);
        }else{
            patientRecordOutbound.setOutboundTime(nextDate);
        }
        return patientRecordOutbound;
    }


    //外呼超窗1天后未填写的提醒
    public PatientRecordOutbound patientRecordOutboundOver(PatientRecord patientRecord,ResearchOutbound researchOutbound,Date now,String outboundType){
        PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
        patientRecordOutbound.setRecordId(patientRecord.getRecordId());
        patientRecordOutbound.setResearchId(patientRecord.getResearchId());
        patientRecordOutbound.setOutboundId(researchOutbound.getId());
        patientRecordOutbound.setSpeechId(researchOutbound.getOverSpeechId());
        patientRecordOutbound.setOutboundType(outboundType);//外呼类型0无状态1智能外呼2短信提醒3app站内信
        patientRecordOutbound.setRemindType("2");//提醒类型0无1随访2超窗3预警
        patientRecordOutbound.setStatus("1");
        patientRecordOutbound.setCreateTime(DateUtils.getNowDate());
        patientRecordOutbound.setName(patientRecord.getName());
        patientRecordOutbound.setStageNum(patientRecord.getStageAddNum());
        patientRecordOutbound.setPlanVisitTime(patientRecord.getVisitTime());
        Date boundTime = getDate(now,researchOutbound.getOverAdvanceTime());
        patientRecordOutbound.setOutboundTime(boundTime);
        return patientRecordOutbound;
    }

    //拼接第一个日期年月日，第二个日期时分秒
    public Date getDate(Date date1,String type){
        String dateStr1 = DateUtils.parseDateToStrFull(date1);
        String dateStrStart =  dateStr1.substring(0,10);
        Date result = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS,dateStrStart+" "+MedicalContants.GET_HOUR_TYPE.get(type)+":00:00");
        return result;
    }




    @Override
    public String appNoticeByResearch(Long researchId) {
        String resultStr = "操作成功";
        List<AccountNotice> list = new ArrayList<>();
        insertRecordAccountNotice(researchId,list);
        if (!list.isEmpty()){
            accountNoticeMapper.batchInsertList(list);
            resultStr = "操作成功,共插入"+list.size()+"条-----------";
            System.out.println(resultStr);

            for(AccountNotice a:list){
                sendPush(2,a);
            }
        }
        return resultStr;
    }

    public void sendPush(Integer appType,AccountNotice accountNotice){
        if (accountNotice.getAccountId() != null){
//            umpushService.sendCustomizedcastMessage(appType,String.valueOf(accountNotice.getAccountId()),accountNotice.getContent(),
//                    accountNotice.getExtendJson());
        }
    }


    //app站内信，随访提醒和超窗提醒
    public void insertRecordAccountNotice(Long researchId,List<AccountNotice> insertList){
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(researchId);
        ResearchOutbound researchOutbound = researchOutboundMapper.selectByResearchIdAndType(researchId,"3");
        long TimeNow = System.currentTimeMillis();
        Date now = new Date(TimeNow);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(researchOutbound!=null && researchInfo!=null && "1".equals(researchInfo.getResearchStatus()) && getTimeAccess(researchOutbound.getFollowAdvanceTime(),hour)){
            PatientRecordTemplate pt = new PatientRecordTemplate();
            pt.setResearchId(researchId);
            List<PatientRecordTemplate> patientRecordTemplates = patientRecordTemplateMapper.selectPatientRecordTemplateList(pt);
            if(patientRecordTemplates.size()>0){
                patientRecordTemplates.sort(Comparator.comparing(PatientRecordTemplate :: getFollowNum));
                Map<Long, PatientRecordTemplate> maps = patientRecordTemplates.stream().collect(Collectors.toMap(PatientRecordTemplate :: getFollowId, Function.identity()));
                List<PatientRecord> patientRecords = patientRecordMapper.selectMaxRecordByResearchId(researchInfo.getId());
                patientRecords.forEach(x->{
                    if (!maps.containsKey(x.getFollowId())){
                        return;
                    }
                    if(x.getLockVisitTime()==null){
                        return;
                    }
                    PatientRecordTemplate recordTemplate = maps.get(x.getFollowId());
                    if(recordTemplate.getFollowNum()==0){
                        //说明当前是基线,下次为第一次生成随访信息
                        int index = patientRecordTemplates.indexOf(recordTemplate);
                        if (index+1 < patientRecordTemplates.size()){
                            if(isCreateFollowRecord(now, x, patientRecordTemplates.get(index + 1),researchOutbound.getFollowAdvanceDays())){
                                insertList.add(new AccountNotice(x,researchOutbound,"6"));
                            }
                        }
                    }else{
                        //限制频次
                        if("1".equals(recordTemplate.getFollowFrequency())){
                            if (recordTemplate.getFollowTotal() == null || recordTemplate.getFollowTotal() < 1){
                                log.info("app站内信提醒,队列ID{}随访规则ID{}followTotal没有值", researchInfo.getId(), recordTemplate.getFollowId());
                                return;
                            }
                            if (x.getStageNum().intValue() == recordTemplate.getFollowTotal().intValue()) {//完成当前随访节点进入下一个随访节点
                                int index = patientRecordTemplates.indexOf(recordTemplate);
                                if (index+1 < patientRecordTemplates.size()){
                                    if(isCreateFollowRecord(now, x, patientRecordTemplates.get(index + 1),researchOutbound.getFollowAdvanceDays())){
                                        insertList.add(new AccountNotice(x,researchOutbound,"6"));
                                    }
                                }
                            }else if (x.getStageNum().intValue() < recordTemplate.getFollowTotal().intValue()){//未完成当前随访节点继续生成当前随访节点计划
                                if(isCreateFollowRecord(now, x, recordTemplate,researchOutbound.getFollowAdvanceDays())){
                                    insertList.add(new AccountNotice(x,researchOutbound,"6"));
                                }
                            }
                        }else if ("2".equals(recordTemplate.getFollowFrequency())){//不限频次
                            //没设置次数且间隔为0天
                            if(recordTemplate.getFollowTotal()==null && recordTemplate.getFollowInterval()==0){
                                int index = patientRecordTemplates.indexOf(recordTemplate);
                                if (index+1 < patientRecordTemplates.size()){
                                    if(isCreateFollowRecord(now, x, patientRecordTemplates.get(index + 1),researchOutbound.getFollowAdvanceDays())){
                                        insertList.add(new AccountNotice(x,researchOutbound,"6"));
                                    }
                                }
                            }else{
                                if(isCreateFollowRecord(now, x, recordTemplate,researchOutbound.getFollowAdvanceDays())){
                                    insertList.add(new AccountNotice(x,researchOutbound,"6"));
                                }
                            }
                        }
                    }
                });
            }
        }

        if(researchOutbound!=null && researchInfo!=null && "1".equals(researchInfo.getResearchStatus()) && getTimeAccess(researchOutbound.getOverAdvanceTime(),hour)){
            //超窗提醒,超过随访时间1天要提醒
            PatientRecord patientRecord = new PatientRecord();
            patientRecord.setResearchId(researchId);
            patientRecord.setFillStatus("0");
            Date overDay = DateUtils.addDays(new Date(), -researchOutbound.getOverAdvanceDays());
            String overDayStr = DateUtils.dateTime(overDay);
            patientRecord.setOverDay(overDayStr);
            List<PatientRecord> patientRecordList = patientRecordMapper.selectListByOverTime(patientRecord);
            if(patientRecordList.size()>0){
                for(PatientRecord p:patientRecordList){
                    insertList.add(new AccountNotice(p,researchOutbound,"7"));
                }
            }
        }
    }

//    public AccountNotice getAccoutnNotice(PatientRecord patientRecord,ResearchOutbound researchOutbound,String type){
//        AccountNotice accountNotice = new AccountNotice();
//        accountNotice.setAccountId(patientRecord.getAccountId());
//        accountNotice.setRecordId(patientRecord.getRecordId());
//        accountNotice.setType(type);
//        accountNotice.setCreateTime(DateUtils.getNowDate());
//        accountNotice.setIsRead(Constants.YES);
//        if("6".equals(type)){
//            accountNotice.setTitle(MedicalContants.MESSAGE_TITLE.get("6"));
//            accountNotice.setContent(researchOutbound.getFollowMailContent());
//            AccountNoticeVo accountNoticeVo = new AccountNoticeVo();
//            accountNoticeVo.setRecordId(accountNotice.getRecordId());
//            accountNoticeVo.setType("6");
//            accountNoticeVo.setResearchId(patientRecord.getResearchId());
//            accountNoticeVo.setPatientId(patientRecord.getPatientId());
//            accountNotice.setExtendJson(com.alibaba.fastjson.JSON.toJSONString(accountNoticeVo));
//        }else if("7".equals(type)){
//            accountNotice.setTitle(MedicalContants.MESSAGE_TITLE.get("7"));
//            accountNotice.setContent(researchOutbound.getOverMailContent());
//            AccountNoticeVo accountNoticeVo = new AccountNoticeVo();
//            accountNoticeVo.setRecordId(accountNotice.getRecordId());
//            accountNoticeVo.setType("7");
//            accountNoticeVo.setStageName(patientRecord.getName());
//            accountNotice.setExtendJson(com.alibaba.fastjson.JSON.toJSONString(accountNoticeVo));
//        }
//        return accountNotice;
//    }

    //根据App站内信提醒时间和当前小时判断是否在符合时间内
    public boolean getTimeAccess(String type,int hour){
        if(hour<7 && hour>20){
            return false;
        }
        if("1".equals(type)){
            if(hour>=7 && hour <8){
                return true;
            }
        }else if("2".equals(type)){
            if(hour>=8 && hour <9){
                return true;
            }
        }else if("3".equals(type)){
            if(hour>=9 && hour <10){
                return true;
            }
        }else if("4".equals(type)){
            if(hour>=10 && hour <11){
                return true;
            }
        }else if("5".equals(type)){
            if(hour>=11 && hour <12){
                return true;
            }
        }else if("6".equals(type)){
            if(hour>=12 && hour <13){
                return true;
            }
        }else if("7".equals(type)){
            if(hour>=13 && hour <14){
                return true;
            }
        }else if("8".equals(type)){
            if(hour>=14 && hour <15){
                return true;
            }
        }else if("9".equals(type)){
            if(hour>=15 && hour <16){
                return true;
            }
        }else if("10".equals(type)){
            if(hour>=16 && hour <17){
                return true;
            }
        }else if("11".equals(type)){
            if(hour>=17 && hour <18){
                return true;
            }
        }else if("12".equals(type)){
            if(hour>=18 && hour <19){
                return true;
            }
        }else if("13".equals(type)){
            if(hour>=19 && hour <=20){
                return true;
            }
        }else if("14".equals(type)){
            if(hour>20 && hour <=23){
                return true;
            }
        }
        return false;
    }


    //单次队列呼叫
    @Override
    public String callbackByResearchId(Long researchId) {
        String resultStr = "操作成功";
        callbackByRecordJob(researchId);
        return resultStr;
    }

    public void callbackByRecordJob(Long researchId){
        ResearchOutbound researchOutbound = researchOutboundMapper.selectByResearchIdAndType(researchId,"1");
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(researchOutbound!=null && getTimeAccess(researchOutbound.getFollowAdvanceTime(),hour)){
            PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
            patientRecordOutbound.setResearchId(researchId);
            patientRecordOutbound.setStatus("1");
            patientRecordOutbound.setOutboundType("1");//外呼类型0无状态1智能外呼2短信提醒3app站内信
            patientRecordOutbound.setRemindType("1");//提醒类型0无1随访2超窗3预警
            patientRecordOutbound.setOutboundCreateTime(DateUtils.dateNowTime());
            List<PatientRecordOutbound> outboundList = patientRecordOutboundMapper.selectPatientRecordOutboundList(patientRecordOutbound);
            if(outboundList.size()>0){
                List<List<PatientRecordOutbound>> partition = Lists.partition(outboundList,500);
                for(List<PatientRecordOutbound> pros:partition){
                    sendXunfeiCallFollow(pros,researchOutbound);
                }
            }
        }
//        if(researchOutbound!=null && getTimeAccess(researchOutbound.getOverAdvanceTime(),hour)){
//            //超窗
//            PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
//            patientRecordOutbound.setResearchId(researchId);
//            patientRecordOutbound.setStatus("1");
//            patientRecordOutbound.setOutboundType("1");//外呼类型0无状态1智能外呼2短信提醒3app站内信
//            patientRecordOutbound.setRemindType("2");//提醒类型0无1随访2超窗3预警
//            patientRecordOutbound.setOutboundCreateTime(DateUtils.dateNowTime());
//            List<PatientRecordOutbound> outboundOverList = patientRecordOutboundMapper.selectPatientRecordOutboundList(patientRecordOutbound);
//            if(outboundOverList.size()>0){
//                List<List<PatientRecordOutbound>> partitionOver = Lists.partition(outboundOverList,500);
//                for(List<PatientRecordOutbound> pros:partitionOver){
//                    sendXunfeiCallOver(pros,researchOutbound);
//                }
//            }
//        }
        //随访短信提醒
//        ResearchOutbound researchOutboundSms = researchOutboundMapper.selectByResearchIdAndType(researchId,"2");
//        if(researchOutboundSms!=null && getTimeAccess(researchOutboundSms.getFollowAdvanceTime(),hour)){
//            PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
//            patientRecordOutbound.setResearchId(researchId);
//            patientRecordOutbound.setStatus("1");
//            patientRecordOutbound.setOutboundType("2");//外呼类型0无状态1智能外呼2短信提醒3app站内信
//            patientRecordOutbound.setRemindType("1");//提醒类型0无1随访2超窗3预警
//            patientRecordOutbound.setOutboundCreateTime(DateUtils.dateNowTime());
//            List<PatientRecordOutbound> outboundList = patientRecordOutboundMapper.selectPatientRecordOutboundList(patientRecordOutbound);
//            if(outboundList.size()>0){
//                List<List<PatientRecordOutbound>> partition = Lists.partition(outboundList,500);
//                for(List<PatientRecordOutbound> pros:partition){
//                    for(PatientRecordOutbound pb:pros){
//                        //随访短信提醒3,超窗2
//                        sendXunfeiSms(pb,"3");
//                    }
//                }
//            }
//        }
//        //超窗短信提醒
//        if(researchOutboundSms!=null && getTimeAccess(researchOutboundSms.getOverAdvanceTime(),hour)){
//            PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
//            patientRecordOutbound.setResearchId(researchId);
//            patientRecordOutbound.setStatus("1");
//            patientRecordOutbound.setOutboundType("2");//外呼类型0无状态1智能外呼2短信提醒3app站内信
//            patientRecordOutbound.setRemindType("2");//提醒类型0无1随访2超窗3预警
//            patientRecordOutbound.setOutboundCreateTime(DateUtils.dateNowTime());
//            List<PatientRecordOutbound> outboundList = patientRecordOutboundMapper.selectPatientRecordOutboundList(patientRecordOutbound);
//            if(outboundList.size()>0){
//                List<List<PatientRecordOutbound>> partition = Lists.partition(outboundList,500);
//                for(List<PatientRecordOutbound> pros:partition){
//                    for(PatientRecordOutbound pb:pros){
//                        sendXunfeiSms(pb,"2");
//                    }
//                }
//            }
//        }


    }

    //随访外呼提前提醒
    public void sendXunfeiCallFollow(List<PatientRecordOutbound> outboundList,ResearchOutbound researchOutbound){
        XunfeiReq xunfeiReq = new XunfeiReq();
        List<Dweller> dwellerList = new ArrayList<>();
        for(PatientRecordOutbound pro:outboundList){
            //居民信息
            Dweller dweller = new Dweller();
            dweller.setDwellerName(pro.getPatientName());
            dweller.setTelephone(pro.getMobile());
            dweller.setIdCard(pro.getIdCard());
            dweller.setRelationId(pro.getRecordId()+"");
            dwellerList.add(dweller);
        }
        xunfeiReq.setDwellers(dwellerList);
        xunfeiReq.setSpeechId(Long.parseLong(researchOutbound.getFollowSpeechId()));
//                xunfeiReq.setPlanName("");//任务名称，非必填
        xunfeiReq.setType(1);//1即呼
        if(!"0".equals(researchOutbound.getFollowReplay())){
            Recall recall = new Recall(Integer.parseInt(researchOutbound.getFollowReplay()),researchOutbound.getFollowReplaySpaceMinute());
            xunfeiReq.setRecall(recall);
        }
        List<Var> vars = getVarList(researchOutbound.getFollowSpeechId(),outboundList.get(0));
        xunfeiReq.setVars(vars);
        log.info("讯飞呼叫随访请求参数{}", com.alibaba.fastjson.JSONObject.toJSONString(xunfeiReq));
//        String result = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(Constants.XUNFEI_CALL_TASK,xunfeiReq);
        String result = XfServiceHttpClientUtil.getInstance().sendSzDefaultHttpPostJson(baseUrl+"xunfei/callTask",xunfeiReq);
        log.info("讯飞呼叫随访返回结果{}",result);
        JSONObject jsonObject = JSON.parseObject(result);
        String code = jsonObject.getString("code");
        if(result!=null && ("1000".equals(code) || "0".equals(code))){
            XunfeiRes xunfeiRes = JSONObject.parseObject(jsonObject.getString("data"),XunfeiRes.class);
            String batch = xunfeiRes.getBatch()[0];
            List<TaskRelationVo> taskRelationVoList = xunfeiRes.getTaskRelation();
            Map<String,TaskRelationVo> taskRelationVoMap = new HashMap<>();
            if(taskRelationVoList.size()>0){
                taskRelationVoMap = taskRelationVoList.stream().collect(Collectors.toMap(TaskRelationVo::getRelationId,Function.identity(), (key1, key2) -> key2));
            }
            for(PatientRecordOutbound p:outboundList){
                p.setStatus("2");//执行中
                p.setBatch(batch);
                if(taskRelationVoMap.containsKey(String.valueOf(p.getRecordId()))){
                    TaskRelationVo relationVo = taskRelationVoMap.get(String.valueOf(p.getRecordId()));
                    p.setTaskId(relationVo.getTaskId());
                }
                p.setUpdateTime(DateUtils.getNowDate());
                patientRecordOutboundMapper.updatePatientRecordOutbound(p);
            }
        }
    }

    //超窗外呼提醒
    public void sendXunfeiCallOver(List<PatientRecordOutbound> outboundList,ResearchOutbound researchOutbound){
        XunfeiReq xunfeiReq = new XunfeiReq();
        List<Dweller> dwellerList = new ArrayList<>();
        for(PatientRecordOutbound pro:outboundList){
            //居民信息
            Dweller dweller = new Dweller();
            dweller.setDwellerName(pro.getPatientName());
            dweller.setTelephone(pro.getMobile());
            dweller.setIdCard(pro.getIdCard());
            dweller.setRelationId(pro.getRecordId()+"");
            dwellerList.add(dweller);
        }
        xunfeiReq.setDwellers(dwellerList);
        xunfeiReq.setSpeechId(Long.parseLong(researchOutbound.getOverSpeechId()));
//                xunfeiReq.setPlanName("");//任务名称，非必填
        xunfeiReq.setType(1);//1即呼
        if(!"0".equals(researchOutbound.getOverReplay())){
            Recall recall = new Recall(Integer.parseInt(researchOutbound.getOverReplay()),researchOutbound.getOverReplaySpaceMinute());
            xunfeiReq.setRecall(recall);
        }
        List<Var> vars = getVarList(researchOutbound.getOverSpeechId(),outboundList.get(0));
        xunfeiReq.setVars(vars);
        System.out.println(com.alibaba.fastjson.JSONObject.toJSONString(xunfeiReq));
//        String result = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(Constants.XUNFEI_CALL_TASK,xunfeiReq);
        String result = XfServiceHttpClientUtil.getInstance().sendSzDefaultHttpPostJson(baseUrl+"xunfei/callTask",xunfeiReq);
        JSONObject jsonObject = JSON.parseObject(result);
        String code = jsonObject.getString("code");
        if(result!=null && ("1000".equals(code) || "0".equals(code))){
            XunfeiRes xunfeiRes = JSONObject.parseObject(jsonObject.getString("data"),XunfeiRes.class);
            String batch = xunfeiRes.getBatch()[0];
            List<TaskRelationVo> taskRelationVoList = xunfeiRes.getTaskRelation();
            Map<String,TaskRelationVo> taskRelationVoMap = new HashMap<>();
            if(taskRelationVoList.size()>0){
                taskRelationVoMap = taskRelationVoList.stream().collect(Collectors.toMap(TaskRelationVo::getRelationId,Function.identity(), (key1, key2) -> key2));
            }
            for(PatientRecordOutbound p:outboundList){
                p.setStatus("2");//执行中
                p.setBatch(batch);
                if(taskRelationVoMap.containsKey(String.valueOf(p.getRecordId()))){
                    TaskRelationVo relationVo = taskRelationVoMap.get(String.valueOf(p.getRecordId()));
                    p.setTaskId(relationVo.getTaskId());
                }
                p.setUpdateTime(DateUtils.getNowDate());
                patientRecordOutboundMapper.updatePatientRecordOutbound(p);
            }
        }

    }

    //随访短信提醒3
    public void sendXunfeiSms(PatientRecordOutbound pb,String smsType){
        XunfeiReq xunfeiReq = new XunfeiReq();
        List<Dweller> dwellerList = new ArrayList<>();
        //居民信息
        Dweller dweller = new Dweller();
        dweller.setDwellerName(pb.getPatientName());
        dweller.setTelephone(pb.getMobile());
        dweller.setIdCard(pb.getIdCard());
        dweller.setRelationId(pb.getRecordId()+"");
        dwellerList.add(dweller);
        xunfeiReq.setDwellers(dwellerList);
        xunfeiReq.setSmsId(MedicalContants.XUNFEI_MESSAGE_TEMPLATE.get(smsType));
        xunfeiReq.setType(1);//1即呼
        List<Var> vars = getSmsVarList(MedicalContants.XUNFEI_MESSAGE_TEMPLATE.get(smsType),pb);
        xunfeiReq.setVars(vars);
        log.info("讯飞短信提醒随访请求参数{}", com.alibaba.fastjson.JSONObject.toJSONString(xunfeiReq));
//        String result = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(Constants.XUNFEI_SMS_TASK,xunfeiReq);
        String result = XfServiceHttpClientUtil.getInstance().sendSzDefaultHttpPostJson(baseUrl+"xunfei/smsTask",xunfeiReq);
        log.info("讯飞短信提醒随访返回结果{}",result);
        JSONObject jsonObject = JSON.parseObject(result);
        String code = jsonObject.getString("code");
        if(result!=null && ("1000".equals(code) || "0".equals(code))){
            XunfeiRes xunfeiRes = JSONObject.parseObject(jsonObject.getString("data"),XunfeiRes.class);
            String batch = xunfeiRes.getBatch()[0];
            List<TaskRelationVo> taskRelationVoList = xunfeiRes.getTaskRelation();
            Map<String,TaskRelationVo> taskRelationVoMap = new HashMap<>();
            if(taskRelationVoList.size()>0){
                taskRelationVoMap = taskRelationVoList.stream().collect(Collectors.toMap(TaskRelationVo::getRelationId,Function.identity(), (key1, key2) -> key2));
            }
            pb.setStatus("2");//执行中
            pb.setBatch(batch);
            if(taskRelationVoMap.containsKey(String.valueOf(pb.getRecordId()))){
                TaskRelationVo relationVo = taskRelationVoMap.get(String.valueOf(pb.getRecordId()));
                pb.setTaskId(relationVo.getTaskId());
            }
            pb.setUpdateTime(DateUtils.getNowDate());
            patientRecordOutboundMapper.updatePatientRecordOutbound(pb);
        }else{
//            XunfeiReesult xunfeiReesult = JSONObject.parseObject(result,XunfeiReesult.class);

        }
    }

    //随访超窗提醒2
//    public void sendXunfeiSmsOver(PatientRecordOutbound pb,String smsType){
//        XunfeiReq xunfeiReq = new XunfeiReq();
//        List<Dweller> dwellerList = new ArrayList<>();
//
//        //居民信息
//        Dweller dweller = new Dweller();
//        dweller.setDwellerName(pb.getPatientName());
//        dweller.setTelephone(pb.getMobile());
//        dweller.setIdCard(pb.getIdCard());
//        dweller.setRelationId(pb.getRecordId()+"");
//        dwellerList.add(dweller);
//
//        xunfeiReq.setDwellers(dwellerList);
//        xunfeiReq.setSmsId(MedicalContants.XUNFEI_MESSAGE_TEMPLATE.get(smsType));
//        xunfeiReq.setType(1);//1即呼
//        List<Var> vars = getSmsVarList(MedicalContants.XUNFEI_MESSAGE_TEMPLATE.get(smsType),pb);
//        xunfeiReq.setVars(vars);
//        log.info("讯飞短信提醒随访请求参数{}", com.alibaba.fastjson.JSONObject.toJSONString(xunfeiReq));
////        String result = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(Constants.XUNFEI_SMS_TASK,xunfeiReq);
//        String result = XfServiceHttpClientUtil.getInstance().sendSzDefaultHttpPostJson(baseUrl+"xunfei/smsTask",xunfeiReq);
//        log.info("讯飞短信提醒随访返回结果{}",result);
//        JSONObject jsonObject = JSON.parseObject(result);
//        String code = jsonObject.getString("code");
//        if(result!=null && ("1000".equals(code) || "0".equals(code))){
//            XunfeiRes xunfeiRes = JSONObject.parseObject(jsonObject.getString("data"),XunfeiRes.class);
//            String batch = xunfeiRes.getBatch()[0];
//            List<TaskRelationVo> taskRelationVoList = xunfeiRes.getTaskRelation();
//            Map<String,TaskRelationVo> taskRelationVoMap = new HashMap<>();
//            if(taskRelationVoList.size()>0){
//                taskRelationVoMap = taskRelationVoList.stream().collect(Collectors.toMap(TaskRelationVo::getRelationId,Function.identity(), (key1, key2) -> key2));
//            }
//            pb.setStatus("2");//执行中
//            pb.setBatch(batch);
//            if(taskRelationVoMap.containsKey(String.valueOf(pb.getRecordId()))){
//                TaskRelationVo relationVo = taskRelationVoMap.get(String.valueOf(pb.getRecordId()));
//                pb.setTaskId(relationVo.getTaskId());
//            }
//            pb.setUpdateTime(DateUtils.getNowDate());
//            patientRecordOutboundMapper.updatePatientRecordOutbound(pb);
//        }else{
////            XunfeiReesult xunfeiReesult = JSONObject.parseObject(result,XunfeiReesult.class);
//
//        }
//    }


    public List<Var> getVarList(String followSpeechId,PatientRecordOutbound pro){
        List<Var> varList = new ArrayList<>();
        if("772939646279680".equals(followSpeechId)){
            Var var1 = new Var("主治医生","唐都医院");
            Var var2 = new Var("单位名称","唐都医院");
            Var var3 = new Var("医院名称","唐都医院");
            varList.add(var1);
            varList.add(var2);
            varList.add(var3);
        }else if("216442646339584".equals(followSpeechId)){
            Var var1 = new Var("期间","住院");
            Var var2 = new Var("单位名称","唐都医院");
            varList.add(var1);
            varList.add(var2);
        }else if("214084533133312".equals(followSpeechId)){
            Var var1 = new Var("自我介绍","唐都医院");
            varList.add(var1);
        }else if("74459".equals(followSpeechId)){
            Var var1 = new Var("体检单位","唐都医院");
            Var var2 = new Var("体检项目","身高、体重、视力和血常规等检查");
            Var var3 = new Var("携带物品","背包");
            Var var4 = new Var("体检时间","每周一至周五全天");
            Var var5 = new Var("自我介绍","唐都医院");
            varList.add(var1);
            varList.add(var2);
            varList.add(var3);
            varList.add(var4);
            varList.add(var5);
        }else if("213577374679040".equals(followSpeechId)){
            Var var1 = new Var("自我介绍","唐都医院");
            Var var2 = new Var("未体检宣教","未体检宣教");
            Var var3 = new Var("体检类型","体检");
            varList.add(var1);
            varList.add(var2);
            varList.add(var3);
        }else if("1530318155456512".equals(followSpeechId)){
            //医院-随访提醒
            Var var1 = new Var("队列名称",pro.getProjectName());
            Var var2 = new Var("具体日期",DateUtils.dateTimeDays(pro.getPlanVisitTime()));
            Var var3 = new Var("外呼机构","唐都医院");
            varList.add(var1);
            varList.add(var2);
            varList.add(var3);
        }else if("1530319925452800".equals(followSpeechId)){
            //医院-超窗提醒
            Var var1 = new Var("外呼机构","唐都医院");
            Var var2 = new Var("队列名称",pro.getProjectName());
            varList.add(var1);
            varList.add(var2);
        }else if("1530309037039616".equals(followSpeechId)){
            //预警提醒
            Var var1 = new Var("外呼机构","唐都医院");
            Var var2 = new Var("具体日期",DateUtils.dateTimeDays(DateUtils.getNowDate()));
            varList.add(var1);
            varList.add(var2);
        }
        return varList;
    }

    public List<Var> getSmsVarList(String followSpeechId,PatientRecordOutbound patientRecordOutbound){
        List<Var> varList = new ArrayList<>();
        if("12107".equals(followSpeechId)){
            //测试时候用的模板
            Var var1 = new Var("dwellerName",patientRecordOutbound.getPatientName());
            Var var2 = new Var("content","国庆快乐");
            Var var3 = new Var("center","唐都医院");
            varList.add(var1);
            varList.add(var2);
            varList.add(var3);
        }else if("12383".equals(followSpeechId)){
            //
            Var var1 = new Var("dwellerName","尊敬的"+patientRecordOutbound.getPatientName());
            Var var2 = new Var("content","您在唐都随访患者管理小程序加入的"+patientRecordOutbound.getProjectName()+"队列未按期进行随访，请您及时登录唐都随访患者管理小程序并尽快填报随访信息，便于医生团队及时掌握您的健康状况");
            Var var3 = new Var("center","唐都医院");
            varList.add(var1);
            varList.add(var2);
            varList.add(var3);
        }else if("10427".equals(followSpeechId)){
            //
            Var var1 = new Var("name","尊敬的"+patientRecordOutbound.getPatientName()+"，");
            Var var2 = new Var("content","您在唐都随访患者管理小程序加入的"+patientRecordOutbound.getProjectName()+"队列未按期进行随访，请您及时登录唐都随访患者管理小程序并尽快填报随访信息，便于医生团队及时掌握您的健康状况");
            varList.add(var1);
            varList.add(var2);
        }else if("19786".equals(followSpeechId)){
            //唐都医院随访短信提醒
            Var var1 = new Var("dwellerName",patientRecordOutbound.getPatientName());
            Var var2 = new Var("content1",patientRecordOutbound.getProjectName()+"队列");
            Var var3 = new Var("content2","将于"+DateUtils.dateTimeDays(patientRecordOutbound.getPlanVisitTime()));
            Var var4 = new Var("center","唐都医院");
            varList.add(var1);
            varList.add(var2);
            varList.add(var3);
            varList.add(var4);
        }else if("16606".equals(followSpeechId)){
            //唐都医院预警提醒
            Var var1 = new Var("dwellerName",patientRecordOutbound.getPatientName());
            Var var2 = new Var("content",DateUtils.dateTimeDays(DateUtils.getNowDate()));
            Var var3 = new Var("center","唐都医院");
            varList.add(var1);
            varList.add(var2);
            varList.add(var3);
        }
        return varList;
    }


    @Override
    public String getResult(Long researchId) {
        PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
        patientRecordOutbound.setStatus("2");
        patientRecordOutbound.setOutboundType("1");//外呼类型0无状态1智能外呼2短信提醒3app站内信
        patientRecordOutbound.setResearchId(researchId);
        patientRecordOutbound.setOutboundCreateTime(DateUtils.dateNowTime());
        List<PatientRecordOutbound> list = patientRecordOutboundMapper.selectPatientRecordOutboundList(patientRecordOutbound);
        if(list.size()>0){
            Map<String,List<PatientRecordOutbound>> listMap = list.stream().filter(x -> x.getBatch()!=null).collect(Collectors.groupingBy(PatientRecordOutbound::getBatch));
            for(String entry:listMap.keySet()){
                List<PatientRecordOutbound> outboundList = listMap.get(entry);
                Map<Long,PatientRecordOutbound> outboundMap = outboundList.stream().collect(Collectors.toMap(PatientRecordOutbound::getRecordId, Function.identity(), (key1, key2) -> key2));

                //查询批次汇总数据接口返回讯飞recordId
                //在通过查询电话记录详情获取外呼结果
                BatchListReq batchListReq = new BatchListReq();
                batchListReq.setBatch(entry);
//                String result = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(Constants.XUNFEI_BATCH_LIST,batchListReq);
                String result = XfServiceHttpClientUtil.getInstance().sendDefaultHttpPost(baseUrl+"xunfei/batchList",batchListReq);
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(result);
                String code = jsonObject.getString("code");
                if(result!=null && ("1000".equals(code) || "0".equals(code))){
                    List<BatchListRes> batchListResList = JSONArray.parseArray(jsonObject.getString("data"),BatchListRes.class);
                    if(batchListResList.size()>0){
                        for(BatchListRes br:batchListResList){
                            //讯飞返回结果relationId为周期唐都周期id
                            if(outboundMap.containsKey(Long.parseLong(br.getRelationId()))){
                                PatientRecordOutbound pro = outboundMap.get(Long.parseLong(br.getRelationId()));
                                //开始调用查询电话记录详情，用上个接口返回的recordId(讯飞recordId)
                                RecordDetailReq recordDetailReq = new RecordDetailReq();
                                recordDetailReq.setRecordId(br.getRecordId());
//                                String detailResult = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(Constants.XUNFEI_RECORD_DETAIL, recordDetailReq);
                                String detailResult = XfServiceHttpClientUtil.getInstance().sendDefaultHttpPost(baseUrl+"xunfei/recordDetail",recordDetailReq);
                                com.alibaba.fastjson.JSONObject detailJsonObject = com.alibaba.fastjson.JSON.parseObject(detailResult);
                                RecordDetailRes recordDetailRes = com.alibaba.fastjson.JSONObject.parseObject(detailJsonObject.getString("data"),RecordDetailRes.class);
                                pro.setStatus("3");
                                pro.setXunfeiRecordId(br.getRecordId());
                                pro.setXunfeiSpeechId(recordDetailRes.getSpeechId());
                                Date callDate = new Date();
                                callDate.setTime(recordDetailRes.getCallTime());
                                pro.setXunfeiCallTime(callDate);
                                pro.setXunfeiTaskTime(DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS,recordDetailRes.getTaskTime()));
                                pro.setXunfeiPlanName(recordDetailRes.getPlanName());
                                pro.setXunfeiSpeechName(recordDetailRes.getSpeechName());
                                pro.setXunfeiCalltimeLength(recordDetailRes.getCallTimeLength());
                                pro.setXunfeiCallResult(recordDetailRes.getCallResult());
                                pro.setXunfeiResultCode(recordDetailRes.getResultCode());
                                pro.setXunfeiEndNode(recordDetailRes.getEndNode());
                                //讯飞音频文件地址
                                if(recordDetailRes.getResultCode()!=null && ( "0".equals(recordDetailRes.getResultCode()) || "15".equals(recordDetailRes.getResultCode()) )){
                                    String soundUrl = recordDetailRes.getRecordUrl();
                                    if(StringUtils.isNotEmpty(soundUrl) && soundUrl.indexOf(".wav")!=-1){
                                        Integer position = soundUrl.indexOf(".wav");
                                        String newSounUrl = soundUrl.substring(0,position+4);
                                        pro.setXunfeiRecordUrl(newSounUrl);
                                        pro.setXunfeiSmsName(recordDetailRes.getSmsName());
                                        pro.setXunfeiSmsContent(recordDetailRes.getSmsContent());
                                        pro.setXunfeiDialog(recordDetailRes.getDialog());
                                    }else{
                                        pro.setStatus("2");
                                    }
                                }
                                patientRecordOutboundMapper.updatePatientRecordOutbound(pro);
                            }
                        }
                    }
                }else{
                    String msg = jsonObject.getString("msg");
                    for(PatientRecordOutbound p:outboundList){
                        p.setXunfeiResultCode(code);
                        p.setXunfeiCallResult(msg);
                        p.setStatus("3");
                        patientRecordOutboundMapper.updatePatientRecordOutbound(p);
                    }

                }
            }
        }
        return "操作成功";
    }

    @Override
    public String callByRecordId(Long recordId) {
        PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByRecordId(recordId);
        PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(patientRecord.getPatientId());
       MpPerson mpPerson =  mpPersonMapper.selectPersonByPersonId(patientInfo.getPersonId());
        ResearchOutbound researchOutbound = researchOutboundMapper.selectByResearchIdAndType(patientRecord.getResearchId(),"1");
        if(researchOutbound!=null && mpPerson!=null){
            ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(patientInfo.getResearchId());
            PatientRecordOutbound patientRecordOutbound = patientRecordOutboundWarn(patientRecord,researchOutbound);
            patientRecordOutbound.setProjectName(researchInfo.getProjectName());
            sendXunfeiCallWarn(patientRecordOutbound,patientRecord,researchOutbound,mpPerson);
        }
        return "操作成功";
    }


    public PatientRecordOutbound patientRecordOutboundWarn(PatientRecord patientRecord,ResearchOutbound researchOutbound){
        PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
        patientRecordOutbound.setRecordId(patientRecord.getRecordId());
        patientRecordOutbound.setResearchId(patientRecord.getResearchId());
        patientRecordOutbound.setOutboundId(researchOutbound.getId());
        patientRecordOutbound.setSpeechId(researchOutbound.getWarnSpeechId());
        patientRecordOutbound.setOutboundType("1");//1智能外呼2短信提醒3app站内信
        patientRecordOutbound.setRemindType("3");//预警
        patientRecordOutbound.setStatus("1");
        patientRecordOutbound.setCreateTime(DateUtils.getNowDate());
        patientRecordOutbound.setName("随访"+String.format("%02d", patientRecord.getStageAddNum()));
        patientRecordOutbound.setStageNum(patientRecord.getStageAddNum());
        patientRecordOutbound.setOutboundTime(DateUtils.getNowDate());
        return patientRecordOutbound;
    }

    public void sendXunfeiCallWarn(PatientRecordOutbound patientRecordOutbound,PatientRecord patientRecord,ResearchOutbound researchOutbound,MpPerson mpPerson){
        XunfeiReq xunfeiReq = new XunfeiReq();
        List<Dweller> dwellerList = new ArrayList<>();

        //居民信息
        Dweller dweller = new Dweller();
        dweller.setDwellerName(mpPerson.getPersonName());
        dweller.setTelephone(mpPerson.getContactsPhone());
//        dweller.setTelephone("15836372038");
        dweller.setIdCard(mpPerson.getCardId());
        dweller.setRelationId(patientRecord.getRecordId()+"");
        dwellerList.add(dweller);

        xunfeiReq.setDwellers(dwellerList);
        xunfeiReq.setSpeechId(Long.parseLong(researchOutbound.getWarnSpeechId()));
//                xunfeiReq.setPlanName("");//任务名称，非必填
        xunfeiReq.setType(1);//1即呼
        if(!"0".equals(researchOutbound.getWarnReplay()) && researchOutbound.getWarnReplay()!=null){
            Recall recall = new Recall(Integer.parseInt(researchOutbound.getWarnReplay()),researchOutbound.getWarnReplaySpaceMinute());
            xunfeiReq.setRecall(recall);
        }
        List<Var> vars = getVarList(researchOutbound.getWarnSpeechId(),patientRecordOutbound);
        xunfeiReq.setVars(vars);
        System.out.println(com.alibaba.fastjson.JSONObject.toJSONString(xunfeiReq)+"--------------------------");
//        String result = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(Constants.XUNFEI_CALL_TASK,xunfeiReq);
        String result = XfServiceHttpClientUtil.getInstance().sendSzDefaultHttpPostJson(baseUrl+"xunfei/callTask",xunfeiReq);
        System.out.println(result+"--------------结果------------");
        JSONObject jsonObject = JSON.parseObject(result);
        String code = jsonObject.getString("code");
        if(result!=null && ("1000".equals(code) || "0".equals(code))){
            XunfeiRes xunfeiRes = JSONObject.parseObject(jsonObject.getString("data"),XunfeiRes.class);
            String batch = xunfeiRes.getBatch()[0];
            List<TaskRelationVo> taskRelationVoList = xunfeiRes.getTaskRelation();
            Map<String,TaskRelationVo> taskRelationVoMap = new HashMap<>();
            if(taskRelationVoList.size()>0){
                taskRelationVoMap = taskRelationVoList.stream().collect(Collectors.toMap(TaskRelationVo::getRelationId,Function.identity(), (key1, key2) -> key2));
            }

            patientRecordOutbound.setStatus("2");//执行中
            patientRecordOutbound.setBatch(batch);
            if(taskRelationVoMap.containsKey(String.valueOf(patientRecord.getRecordId()))){
                TaskRelationVo relationVo = taskRelationVoMap.get(String.valueOf(patientRecord.getRecordId()));
                patientRecordOutbound.setTaskId(relationVo.getTaskId());
            }
            patientRecordOutbound.setUpdateTime(DateUtils.getNowDate());
            patientRecordOutboundMapper.insertPatientRecordOutbound(patientRecordOutbound);
        }
    }

    @Override
    public String downloadWav(Long id) {
        PatientRecordOutbound patientRecordOutbound = patientRecordOutboundMapper.selectPatientRecordOutboundById(id);
        String fileName = getUuidFileName("wav");
        try {
            String localUrl = uploadImFile(patientRecordOutbound.getXunfeiRecordUrl(),fileName);
            patientRecordOutbound.setIsDownloadSound("1");
            patientRecordOutbound.setLocalRecordUrl(localUrl);
            patientRecordOutboundMapper.updatePatientRecordOutbound(patientRecordOutbound);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "操作成功";
    }

    @Override
    public String selectSevenDayList(Long researchId) {
        PatientRecord patientRecord = new PatientRecord();
        patientRecord.setResearchId(researchId);
        patientRecord.setQuestionStatus("1");
        Date overDay = DateUtils.addDays(new Date(), -7);
        String overDayStr = DateUtils.dateTime(overDay);
        patientRecord.setQuestionTimeOver(overDayStr);
        List<PatientRecord> patientRecordList = patientRecordMapper.selectListByOverTime(patientRecord);
        if(patientRecordList.size()>0){
            for(PatientRecord p:patientRecordList){
                String msgContent = "医生向您发起的质疑超过7天未处理，请尽快查";
                AccountNotice accountNotice = new AccountNotice(p,msgContent,"12");
                accountNoticeMapper.insertAccountNotice(accountNotice);
//                umpushService.sendCustomizedcastMessage(2,String.valueOf(p.getAccountId()),msgContent,accountNotice.getExtendJson());

            }
        }
        return "操作成功";
    }

    public static String getUuidFileName(String imageFormat){
        String fileName = "";
        if (!StringUtils.isNotEmpty(fileName)){
            String uuid = UUID.randomUUID().toString();
            fileName = uuid.replace("-", "");
        }
        fileName += "."+ imageFormat;
        return fileName;
    }

    private String uploadImFile(String fileUrl,String fileName) throws IOException {
        InputStream is = null;
        try{
            URL url = new URL(fileUrl);
            URLConnection con = url.openConnection();
            is = con.getInputStream();
            if (is == null){
                return "";
            }
            log.info("uploadImFile------------------"+fileName);
            String httpUrl = DchealthConfig.getFastPath() + "ngupload/single";
            String filesResult = HttpClientUtil.getInstance().sendHttpPostWav(httpUrl, is,fileName);
            if (StringUtils.isNotEmpty(filesResult)) {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(filesResult);
                if (jsonObject != null) {
                    int  code = jsonObject.getInteger("code");
                    if(code ==1000) {
                        return jsonObject.getString("data");
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (is != null){
                is.close();
            }
        }
        return "";
    }
}
