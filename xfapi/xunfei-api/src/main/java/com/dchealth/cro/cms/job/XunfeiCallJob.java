package com.dchealth.cro.cms.job;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.cms.domain.PatientRecordOutbound;
import com.dchealth.cro.cms.domain.RecordOutboundHttpVO;
import com.dchealth.cro.cms.domain.RecordOutboundVO;
import com.dchealth.cro.cms.domain.ResearchOutbound;
import com.dchealth.cro.cms.form.RecordOutbounUpdateForm;
import com.dchealth.cro.cms.utils.TdApiHttpClientUtil;
import com.dchealth.cro.cms.utils.XunfeiConstants;
import com.dchealth.cro.cms.vo.*;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.common.utils.XunfeiHttpClientUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @Auther: zdd
 */
@Component
public class XunfeiCallJob {

    private static Logger logger = LoggerFactory.getLogger(XunfeiCallJob.class);

    @Value("${tdapi.baseUrl}")
    private String baseUrl ;


    //每分钟执行一次
//     @Scheduled(cron = "0 40 17 * * ?")
    @Scheduled(cron = "0 0 7-21 * * ?")
    public void xunfeiCallJobHandler() throws Exception {
        logger.info("讯飞呼叫随访或超窗开始任务日期{}开始", DateUtils.getTime());
        Map<String, String> maps = new HashMap<String, String>();
//        maps.put("researchId", "2172");
        String httpResult = TdApiHttpClientUtil.getInstance().sendSzDefaultHttpPost(baseUrl+"transfer/recordOutbound/recordOutbound", maps);
        com.alibaba.fastjson2.JSONObject jsonObject = JSON.parseObject(httpResult);
        String code = jsonObject.getString("code");
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(httpResult!=null && "1000".equals(code)){
            RecordOutboundVO recordOutboundVO = JSONObject.parseObject(jsonObject.getString("data"),RecordOutboundVO.class);
            List<RecordOutboundHttpVO> list = recordOutboundVO.getRecordOutboundHttpVO();
            if(list.size()>0){
                for(RecordOutboundHttpVO p:list){
                    ResearchOutbound researchOutbound = p.getResearchOutbound();
                    List<PatientRecordOutbound> patientRecordOutboundList = p.getRecordOutboundList();
                    List<PatientRecordOutbound> followList = patientRecordOutboundList.stream().filter(f-> "1".equals(f.getRemindType())).collect(Collectors.toList());
                    List<PatientRecordOutbound> overList = patientRecordOutboundList.stream().filter(f-> "2".equals(f.getRemindType())).collect(Collectors.toList());
                    List<PatientRecordOutbound> warnList = patientRecordOutboundList.stream().filter(f-> "3".equals(f.getRemindType())).collect(Collectors.toList());

                    //获取当天需要呼叫的数据，随访，超窗和预警
                    if(followList.size()>0){
                        List<List<PatientRecordOutbound>> followPartition = Lists.partition(followList,500);
                        for(List<PatientRecordOutbound> pros:followPartition){
                            if(researchOutbound!=null && getTimeAccess(researchOutbound.getFollowAdvanceTime(),hour)){
                                sendXunfeiCallFollow(pros,researchOutbound);
                            }
                        }
                    }
                    //超窗
                    if(overList.size()>0){
                        List<List<PatientRecordOutbound>> overPartition = Lists.partition(overList,500);
                        for(List<PatientRecordOutbound> pros:overPartition){
                            if(researchOutbound!=null && getTimeAccess(researchOutbound.getOverAdvanceTime(),hour)){
                                sendXunfeiCallOver(pros,researchOutbound);
                            }
                        }
                    }
                    //预警
                    if(warnList.size()>0){
                        for(PatientRecordOutbound pro:warnList){
                            sendXunfeiCallWarn(pro,researchOutbound);
                        }
                    }

                }
            }
        }else{
            logger.info("讯飞呼叫随访或超窗任务失败");
        }


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
        logger.info("讯飞呼叫随访请求参数{}", com.alibaba.fastjson.JSONObject.toJSONString(xunfeiReq));
        String result = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(XunfeiConstants.XUNFEI_CALL_TASK, xunfeiReq);
//        String result = "{\"code\":1000,\"data\":{\"batch\":[\"1111222233334444\"],\"filterDwellers\":[],\"taskRelation\":[{\"relationId\":\"13887\",\"taskId\":\"222233334444\"}]},\"msg\":\"SUCCESS\"}";

        logger.info("讯飞呼叫随访返回结果{}",result);
        com.alibaba.fastjson2.JSONObject jsonObject = JSON.parseObject(result);
        String code = jsonObject.getString("code");
        if(result!=null && ("1000".equals(code) || "0".equals(code))){
            XunfeiRes xunfeiRes = com.alibaba.fastjson2.JSONObject.parseObject(jsonObject.getString("data"), XunfeiRes.class);
            String batch = xunfeiRes.getBatch()[0];
            List<TaskRelationVo> taskRelationVoList = xunfeiRes.getTaskRelation();
            Map<String, TaskRelationVo> taskRelationVoMap = new HashMap<>();
            if(taskRelationVoList.size()>0){
                taskRelationVoMap = taskRelationVoList.stream().collect(Collectors.toMap(TaskRelationVo::getRelationId, Function.identity(), (key1, key2) -> key2));
            }
            for(PatientRecordOutbound p:outboundList){
                p.setStatus("2");//执行中
                p.setBatch(batch);
                if(taskRelationVoMap.containsKey(String.valueOf(p.getRecordId()))){
                    TaskRelationVo relationVo = taskRelationVoMap.get(String.valueOf(p.getRecordId()));
                    p.setTaskId(relationVo.getTaskId());
                }
                p.setUpdateTime(DateUtils.getNowDate());
            }
            //获取到的结果需要调用tdapi修改数据库，修改批次号，任务id和status
            RecordOutbounUpdateForm recordOutbounUpdateForm = new RecordOutbounUpdateForm();
            recordOutbounUpdateForm.setPatientRecordOutboundList(outboundList);
            String updResult = TdApiHttpClientUtil.getInstance().sendSzDefaultHttpPostJson(baseUrl+"transfer/recordOutbound/recordOutboundUpdate", recordOutbounUpdateForm);
            logger.info("transfer/recordOutbound/recordOutboundUpdate随访操作结果{}", updResult);
        }
    }

    //超窗
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
        logger.info("---------------------xunfeicalljob超窗开始呼叫，参数为:{}----------------------------", com.alibaba.fastjson.JSONObject.toJSONString(xunfeiReq));
        String result = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(XunfeiConstants.XUNFEI_CALL_TASK, xunfeiReq);
        logger.info("讯飞超窗呼叫返回结果{}",result);
        com.alibaba.fastjson2.JSONObject jsonObject = JSON.parseObject(result);
        String code = jsonObject.getString("code");
        if(result!=null && ("1000".equals(code) || "0".equals(code))){
            XunfeiRes xunfeiRes = com.alibaba.fastjson2.JSONObject.parseObject(jsonObject.getString("data"),XunfeiRes.class);
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
            }
            //获取到的结果需要调用tdapi修改数据库，修改批次号，任务id和status
            RecordOutbounUpdateForm recordOutbounUpdateForm = new RecordOutbounUpdateForm();
            recordOutbounUpdateForm.setPatientRecordOutboundList(outboundList);
            String updResult = TdApiHttpClientUtil.getInstance().sendSzDefaultHttpPostJson(baseUrl+"transfer/recordOutbound/recordOutboundUpdate", recordOutbounUpdateForm);
            logger.info("transfer/recordOutbound/recordOutboundUpdate超窗操作结果{}", updResult);
        }
    }

    //预警
    public void sendXunfeiCallWarn(PatientRecordOutbound patientRecordOutbound,ResearchOutbound researchOutbound){
        XunfeiReq xunfeiReq = new XunfeiReq();
        List<Dweller> dwellerList = new ArrayList<>();
        //居民信息
        Dweller dweller = new Dweller();
        dweller.setDwellerName(patientRecordOutbound.getPatientName());
        dweller.setTelephone(patientRecordOutbound.getMobile());
        dweller.setIdCard(patientRecordOutbound.getIdCard());
        dweller.setRelationId(patientRecordOutbound.getRecordId()+"");
        dwellerList.add(dweller);
        xunfeiReq.setDwellers(dwellerList);
        xunfeiReq.setSpeechId(Long.parseLong(researchOutbound.getWarnSpeechId()));
        xunfeiReq.setType(1);//1即呼
        if(!"0".equals(researchOutbound.getWarnReplay()) && researchOutbound.getWarnReplay()!=null){
            Recall recall = new Recall(Integer.parseInt(researchOutbound.getWarnReplay()),researchOutbound.getWarnReplaySpaceMinute());
            xunfeiReq.setRecall(recall);
        }
        List<Var> vars = getVarList(researchOutbound.getWarnSpeechId(),patientRecordOutbound);
        xunfeiReq.setVars(vars);
        logger.info("---------------------xunfeicalljob预警开始呼叫，参数为:{}----------------------------", com.alibaba.fastjson.JSONObject.toJSONString(xunfeiReq));

        String result = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(XunfeiConstants.XUNFEI_CALL_TASK, xunfeiReq);
        logger.info("讯飞预警呼叫返回结果{}",result);
        com.alibaba.fastjson2.JSONObject jsonObject = JSON.parseObject(result);
        String code = jsonObject.getString("code");
        if(result!=null && ("1000".equals(code) || "0".equals(code))){
            XunfeiRes xunfeiRes = com.alibaba.fastjson2.JSONObject.parseObject(jsonObject.getString("data"),XunfeiRes.class);
            String batch = xunfeiRes.getBatch()[0];
            List<TaskRelationVo> taskRelationVoList = xunfeiRes.getTaskRelation();
            Map<String,TaskRelationVo> taskRelationVoMap = new HashMap<>();
            if(taskRelationVoList.size()>0){
                taskRelationVoMap = taskRelationVoList.stream().collect(Collectors.toMap(TaskRelationVo::getRelationId,Function.identity(), (key1, key2) -> key2));
            }

            patientRecordOutbound.setStatus("2");//执行中
            patientRecordOutbound.setBatch(batch);
            if(taskRelationVoMap.containsKey(String.valueOf(patientRecordOutbound.getRecordId()))){
                TaskRelationVo relationVo = taskRelationVoMap.get(String.valueOf(patientRecordOutbound.getRecordId()));
                patientRecordOutbound.setTaskId(relationVo.getTaskId());
            }
            patientRecordOutbound.setUpdateTime(DateUtils.getNowDate());

            List<PatientRecordOutbound> outboundList = new ArrayList<>();
            outboundList.add(patientRecordOutbound);
            //获取到的结果需要调用tdapi修改数据库，修改批次号，任务id和status
            RecordOutbounUpdateForm recordOutbounUpdateForm = new RecordOutbounUpdateForm();
            recordOutbounUpdateForm.setPatientRecordOutboundList(outboundList);
            String updResult = TdApiHttpClientUtil.getInstance().sendSzDefaultHttpPostJson(baseUrl+"transfer/recordOutbound/recordOutboundUpdate", recordOutbounUpdateForm);
            logger.info("transfer/recordOutbound/recordOutboundUpdate预警操作结果{}", updResult);
        }

    }

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
}
