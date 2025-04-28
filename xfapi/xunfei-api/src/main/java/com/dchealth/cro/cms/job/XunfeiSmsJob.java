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
public class XunfeiSmsJob {

    private static Logger logger = LoggerFactory.getLogger(XunfeiSmsJob.class);

    @Value("${tdapi.baseUrl}")
    private String baseUrl ;


//    @Autowired
//    private UmpushService umpushService;

    //启动延期5秒
//     @Scheduled(fixedDelay =5000)
    //每分钟执行一次
     @Scheduled(cron = "0 0/20 * * * ?")
//     @Scheduled(cron = "0 20 17 * * ?")
    public void xunfeiSmsJobHandler() throws Exception {
        logger.info("讯飞短信开始任务日期{}开始", DateUtils.getTime());
        Map<String, String> maps = new HashMap<String, String>();
//        maps.put("researchId", "2172");
        String httpResult = TdApiHttpClientUtil.getInstance().sendSzDefaultHttpPost(baseUrl+"transfer/recordOutbound/getSendSmsList", maps);
        com.alibaba.fastjson2.JSONObject jsonObject = JSON.parseObject(httpResult);
        String code = jsonObject.getString("code");
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(httpResult!=null && "1000".equals(code)){
            RecordOutboundHttpVO recordOutboundHttpVO = JSONObject.parseObject(jsonObject.getString("data"),RecordOutboundHttpVO.class);
            List<PatientRecordOutbound> patientRecordOutboundList = recordOutboundHttpVO.getRecordOutboundList();
            if(patientRecordOutboundList.size()>0){
                //remindType提醒类型0无1随访2超窗3预警
                List<PatientRecordOutbound> followList = patientRecordOutboundList.stream().filter(f-> "1".equals(f.getRemindType())).collect(Collectors.toList());
                List<PatientRecordOutbound> overList = patientRecordOutboundList.stream().filter(f-> "2".equals(f.getRemindType())).collect(Collectors.toList());
                List<PatientRecordOutbound> warnList = patientRecordOutboundList.stream().filter(f-> "3".equals(f.getRemindType())).collect(Collectors.toList());

                //获取当天需要发送短信的数据，随访，超窗和预警
                if(followList.size()>0){
                    for(PatientRecordOutbound pro:followList){
                        if(pro.getFollowAdvanceTime()!=null && getTimeAccess(pro.getFollowAdvanceTime(),hour)){
                            sendXunfeiSms(pro,"3");
                        }
                    }
                }
                //超窗
                if(overList.size()>0){
                    for(PatientRecordOutbound pro:overList){
                        if(pro.getOverAdvanceTime()!=null && getTimeAccess(pro.getOverAdvanceTime(),hour)){
                                sendXunfeiSms(pro,"2");
                        }
                    }
                }
                //预警
                if(warnList.size()>0){
                    for(PatientRecordOutbound pro:warnList){
                            sendXunfeiSms(pro,"4");
                    }
                }
            }
        }else{
            logger.info("讯飞呼叫随访超窗预警短信任务失败");
        }
         logger.info("讯飞短信开始任务日期{}结束", DateUtils.getTime());

    }


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

        xunfeiReq.setSmsId(XunfeiConstants.XUNFEI_MESSAGE_TEMPLATE.get(smsType));
        xunfeiReq.setType(1);//1即呼
        List<Var> vars = getSmsVarList(XunfeiConstants.XUNFEI_MESSAGE_TEMPLATE.get(smsType),pb);
        xunfeiReq.setVars(vars);
        logger.info("---------------------xunfeicalljob短信开始呼叫，参数为:{}----------------------------", com.alibaba.fastjson.JSONObject.toJSONString(xunfeiReq));
        String result = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(XunfeiConstants.XUNFEI_SMS_TASK, xunfeiReq);
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
            pb.setStatus("2");//执行中
            pb.setBatch(batch);
            if(taskRelationVoMap.containsKey(String.valueOf(pb.getRecordId()))){
                TaskRelationVo relationVo = taskRelationVoMap.get(String.valueOf(pb.getRecordId()));
                pb.setTaskId(relationVo.getTaskId());
            }
            pb.setUpdateTime(DateUtils.getNowDate());
            //获取到的结果需要调用tdapi修改数据库，修改批次号，任务id和status
            List<PatientRecordOutbound> patientRecordOutboundList = new ArrayList<>();
            patientRecordOutboundList.add(pb);
            RecordOutbounUpdateForm recordOutbounUpdateForm = new RecordOutbounUpdateForm();
            recordOutbounUpdateForm.setPatientRecordOutboundList(patientRecordOutboundList);
            String updResult = TdApiHttpClientUtil.getInstance().sendSzDefaultHttpPostJson(baseUrl+"transfer/recordOutbound/recordOutboundUpdate", recordOutbounUpdateForm);
            logger.info("transfer/recordOutbound/recordOutboundUpdate超窗操作结果{}", updResult);
        }
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
        }else if("16607".equals(followSpeechId)){
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
