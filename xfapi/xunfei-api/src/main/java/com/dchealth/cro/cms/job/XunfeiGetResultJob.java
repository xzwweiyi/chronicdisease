package com.dchealth.cro.cms.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.cms.domain.PatientRecordOutbound;
import com.dchealth.cro.cms.domain.RecordOutboundHttpVO;
import com.dchealth.cro.cms.form.RecordOutbounUpdateForm;
import com.dchealth.cro.cms.utils.TdApiHttpClientUtil;
import com.dchealth.cro.cms.utils.XunfeiConstants;
import com.dchealth.cro.cms.vo.BatchListReq;
import com.dchealth.cro.cms.vo.BatchListRes;
import com.dchealth.cro.cms.vo.RecordDetailReq;
import com.dchealth.cro.cms.vo.RecordDetailRes;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.common.utils.XunfeiHttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@Slf4j
public class XunfeiGetResultJob {

    private static Logger logger = LoggerFactory.getLogger(XunfeiGetResultJob.class);

    @Value("${tdapi.baseUrl}")
    private String baseUrl ;


//    0 0/10 7/21 * * ?

     @Scheduled(cron = "0 0/10 7-21 * * ? ")
//     @Scheduled(cron = "0 45 17 * * ?")
    public void xunfeiGetResultJobHandler() throws Exception {
        logger.info("讯飞呼获取结果开始任务日期{}开始", DateUtils.getTime());
        Map<String, String> maps = new HashMap<String, String>();
//        maps.put("researchId", "2172");
        String httpResult = TdApiHttpClientUtil.getInstance().sendSzDefaultHttpPost(baseUrl+"transfer/recordOutbound/resultOutboundList", maps);
        com.alibaba.fastjson2.JSONObject jsonObject = JSON.parseObject(httpResult);
        String code = jsonObject.getString("code");
        if(httpResult!=null && "1000".equals(code)){
            RecordOutboundHttpVO recordOutboundHttpVO = JSONObject.parseObject(jsonObject.getString("data"),RecordOutboundHttpVO.class);
            List<PatientRecordOutbound> list = recordOutboundHttpVO.getRecordOutboundList();
            if(list.size()>0){
                Map<Long,List<PatientRecordOutbound>> recordOutboundMap = list.stream().
                        filter(x -> x.getResearchId()!=null).collect(Collectors.groupingBy(PatientRecordOutbound::getResearchId));
                for(Long researchId:recordOutboundMap.keySet()){
                    getResult(recordOutboundMap.get(researchId));
                }
            }
        }else{
            logger.info("飞呼获取结果任务失败");
        }
    }


    public String getResult(List<PatientRecordOutbound> list) {
        if(list.size()>0){
            Map<String,List<PatientRecordOutbound>> listMap = list.stream().filter(x -> x.getBatch()!=null).collect(Collectors.groupingBy(PatientRecordOutbound::getBatch));
            for(String entry:listMap.keySet()){
                List<PatientRecordOutbound> outboundList = listMap.get(entry);
                Map<Long,PatientRecordOutbound> outboundMap = outboundList.stream().collect(Collectors.toMap(PatientRecordOutbound::getRecordId, Function.identity(), (key1, key2) -> key2));

                //查询批次汇总数据接口返回讯飞recordId
                //在通过查询电话记录详情获取外呼结果
                BatchListReq batchListReq = new BatchListReq();
                batchListReq.setBatch(entry);
                String result = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(XunfeiConstants.XUNFEI_BATCH_LIST, batchListReq);
//                String result = "{\"code\": 0,\"message\": \"成功\",\"data\": [{\"actualCallCount\": 1,\"smsFlag\": false,\"resultCode\": \"0\",\"telephone\": \"15836372038\",\"relationId\": \"13887\",\"endNode\": \"1\",\"recordId\": \"333444555\",\"dwellerName\": \"张兮兮\",\"callTimeLength\": 32,\"callTime\": 1698043277000}]}";
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(result);
                String code = jsonObject.getString("code");
                List<PatientRecordOutbound> updateList = new ArrayList<>();

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
                                String detailResult = XunfeiHttpClientUtil.getInstance().sendDefaultHttpPost(XunfeiConstants.XUNFEI_RECORD_DETAIL, recordDetailReq);

//                                String detailResult = "{\"code\": 0,\"message\": \"成功\",\"data\": {\"speechId\": \"15303181554565121\",\"speechName\": \"预警提醒\",\"recordUrl\": \"https://www.xfzyzl.com/yetavoice/ant/2/ivr/40/2023/10/23/0/180555918-E0A3A789E1EFFC90C4BA40B9670C69C0.wav\",\"resultCode\": \"0\",\"planName\": \"一键随访_20231023144116\",\"telephone\": \"17600093418\",\"endNode\": \"1\",\"recordId\": \"208ecaf05cae4b4296754b15789d242c\",\"dialog\": \"[{\\\"node\\\":\\\"自我介绍与居民确认\\\",\\\"result\\\":\\\"对。\\\",\\\"robot\\\":\\\"您好，我是唐都医院随访工作人员，请问您是张哥吗？\\\"},{\\\"node\\\":\\\"通知\\\",\\\"result\\\":\\\"好。\\\",\\\"robot\\\":\\\"好的，我们这边看到，您10-23在唐都慢病管理APP填报的随访信息结果异常，触发了预警，请您及时登录唐都慢病管理APP并查看预警指导。\\\"},{\\\"node\\\":\\\"通知完成\\\",\\\"result\\\":\\\"再见。\\\",\\\"robot\\\":\\\"好的，感谢您的接听，祝您身体健康，生活愉快，再见！\\\"}]\",\"dwellerName\": \"张哥\",\"callTimeLength\": 32,\"callResult\": \"成功\",\"callTime\": 1698043277000,\"taskTime\": \"2023-10-23 14:41:16\"}}";

                                com.alibaba.fastjson.JSONObject detailJsonObject = com.alibaba.fastjson.JSON.parseObject(detailResult);
                                log.info("讯飞返回结果：========>{}", detailJsonObject);
                                RecordDetailRes recordDetailRes = com.alibaba.fastjson.JSONObject.parseObject(detailJsonObject.getString("data"),RecordDetailRes.class);
                                pro.setStatus("3");
                                pro.setXunfeiRecordId(br.getRecordId());
                                pro.setXunfeiSpeechId(recordDetailRes.getSpeechId());

                                pro.setXunfeiTaskTime(DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS,recordDetailRes.getTaskTime()));
                                pro.setXunfeiPlanName(recordDetailRes.getPlanName());
                                pro.setXunfeiSpeechName(recordDetailRes.getSpeechName());
                                pro.setXunfeiCalltimeLength(recordDetailRes.getCallTimeLength());
                                pro.setXunfeiCallResult(recordDetailRes.getCallResult());
                                pro.setXunfeiResultCode(recordDetailRes.getResultCode());
                                pro.setXunfeiEndNode(recordDetailRes.getEndNode());
                                //讯飞音频文件地址
                                if(recordDetailRes.getResultCode()!=null && ( "0".equals(recordDetailRes.getResultCode()) || "15".equals(recordDetailRes.getResultCode()) )){
                                    Date callDate = new Date();
                                    callDate.setTime(recordDetailRes.getCallTime());
                                    pro.setXunfeiCallTime(callDate);
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
                                updateList.add(pro);
                            }
                        }
                    }
                }else{
                    String msg = jsonObject.getString("msg");
                    for(PatientRecordOutbound p:outboundList){
                        p.setXunfeiResultCode(code);
                        p.setXunfeiCallResult(msg);
                        p.setStatus("3");
                        updateList.add(p);
                    }
                }

                //获取到的结果需要调用tdapi修改数据库，修改xunfei字段
                RecordOutbounUpdateForm recordOutbounUpdateForm = new RecordOutbounUpdateForm();
                recordOutbounUpdateForm.setPatientRecordOutboundList(updateList);
                String updResult = TdApiHttpClientUtil.getInstance().sendSzDefaultHttpPostJson(baseUrl+"transfer/recordOutbound/recordOutboundUpdate", recordOutbounUpdateForm);
                logger.info("transfer/recordOutbound/recordOutboundUpdate获取结果操作结果{}", updResult);

            }
        }
        return "操作成功";
    }

}
