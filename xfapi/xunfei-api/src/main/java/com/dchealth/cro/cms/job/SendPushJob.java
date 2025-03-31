package com.dchealth.cro.cms.job;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.cms.service.DoctorAccountService;
import com.dchealth.cro.cms.utils.TdApiHttpClientUtil;
import com.dchealth.cro.cms.vo.*;
import com.dchealth.cro.common.base.CommonResult;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.core.umpush.UmpushService;
import com.dchealth.cro.enums.NoticeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @Auther: zdd
 */
@Component
public class SendPushJob {

    private static Logger logger = LoggerFactory.getLogger(SendPushJob.class);

    @Value("${tdapi.baseUrl}")
    private String baseUrl ;

    @Autowired
    private UmpushService umpushService;

    @Autowired
    private DoctorAccountService doctorAccountService;

//    0 0/10 7/21 * * ?

//     * 0/10 * * * ?
//     @Scheduled(cron = "0 0/1 * * * ?")
//     @Scheduled(cron = "0 08 17 * * ?")
    @Scheduled(cron = "0 0/3 7-21 * * ?")
    //@Scheduled(cron = "0 */5 * * * ?")
    public void sendPushJobHandler() throws Exception {
        logger.info("友盟推送开始任务日期{}开始", DateUtils.getTime());
        Map<String, String> maps = new HashMap<String, String>();
        String httpResult = TdApiHttpClientUtil.getInstance().sendSzDefaultHttpPost(baseUrl+"transfer/accountNotice/getList", maps);
        com.alibaba.fastjson2.JSONObject jsonObject = JSON.parseObject(httpResult);
        String code = jsonObject.getString("code");
        if(httpResult!=null && "1000".equals(code)){
            AccountNoticePushVo accountNoticePushVo = JSONObject.parseObject(jsonObject.getString("data"),AccountNoticePushVo.class);
            List<AccountNotice> list = accountNoticePushVo.getAccountNoticeList();

            // 短信
            List<AccountNotice> smsList = list.stream().filter(x-> NoticeEnum.SMS.getCode().equals(x.getType())).collect(Collectors.toList());
            for(AccountNotice a:smsList){
                if (a.getAccountId() != null){
                    if(a.getExtendJson()!=null){
                        Long accountId = a.getAccountId();
                        String extendJson = a.getExtendJson();
                        AccountExtendJsonVo accountExtendJsonVo = JSONObject.parseObject(extendJson,AccountExtendJsonVo.class);
                        SendTencentSmsVO sendTencentSmsVO = accountExtendJsonVo.getSendTencentSmsVO();
                        DchealthCloudResponseEntity<CommonResult> commonResultDchealthCloudResponseEntity = doctorAccountService.sendPatientJoinResearch(sendTencentSmsVO,!accountId.equals(0),accountExtendJsonVo.getResearchName());
                        logger.info("短信发送结果{}", commonResultDchealthCloudResponseEntity);
                    }
                }
            }
            // 站内信
            List<AccountNotice> pushList = list.stream().filter(x-> !(NoticeEnum.SMS.getCode().equals(x.getType()))).collect(Collectors.toList());
            for(AccountNotice a:pushList){
                if (a.getAccountId() != null){
                    if(a.getExtendJson()!=null){
                        String extendJson = a.getExtendJson();
                        AccountExtendJsonVo accountExtendJsonVo = JSONObject.parseObject(extendJson,AccountExtendJsonVo.class);
                        accountExtendJsonVo.setId(a.getId());
                        if("15".equals(a.getType()) || "16".equals(a.getType()) || "17".equals(a.getType()) || "18".equals(a.getType())){
                            umpushService.sendCustomizedcastMessage(accountNoticePushVo.getDoctorConfig(),String.valueOf(a.getAccountId()),a.getContent(),
                                    JSON.toJSONString(accountExtendJsonVo));
                        }else{
                            umpushService.sendCustomizedcastMessage(accountNoticePushVo.getPatientConfig(),String.valueOf(a.getAccountId()),a.getContent(),
                                    JSON.toJSONString(accountExtendJsonVo));
                        }
                    }

                }
            }
        }else{
            logger.info("友盟推送开始任务失败");
        }
        logger.info("友盟推送开始任务日期{}结束", DateUtils.getTime());
    }
}
