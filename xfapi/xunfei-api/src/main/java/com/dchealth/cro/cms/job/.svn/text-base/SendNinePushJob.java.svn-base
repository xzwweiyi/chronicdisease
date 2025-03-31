package com.dchealth.cro.cms.job;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.cms.utils.TdApiHttpClientUtil;
import com.dchealth.cro.cms.vo.AccountExtendJsonVo;
import com.dchealth.cro.cms.vo.AccountNotice;
import com.dchealth.cro.cms.vo.AccountNoticePushVo;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.core.umpush.UmpushService;
import com.dchealth.cro.enums.NoticeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @Auther: zdd
 */
@Component
public class SendNinePushJob {

    private static Logger logger = LoggerFactory.getLogger(SendNinePushJob.class);

    @Value("${tdapi.baseUrl}")
    private String baseUrl ;

    @Autowired
    private UmpushService umpushService;



    //每天9点执行
     @Scheduled(cron = "0 0 9 * * ?")
    public void sendPushJobHandler() throws Exception {
        logger.info("友盟9点定时推送开始任务日期{}开始", DateUtils.getTime());
        Map<String, String> maps = new HashMap<String, String>();
        String httpResult = TdApiHttpClientUtil.getInstance().sendSzDefaultHttpPost(baseUrl+"transfer/accountNotice/getNoticeNineList", maps);
        com.alibaba.fastjson2.JSONObject jsonObject = JSON.parseObject(httpResult);
        String code = jsonObject.getString("code");
        if(httpResult!=null && "1000".equals(code)){
            AccountNoticePushVo accountNoticePushVo = JSONObject.parseObject(jsonObject.getString("data"),AccountNoticePushVo.class);
            List<AccountNotice> list = accountNoticePushVo.getAccountNoticeList();
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
            logger.info("友盟9点定时推送开始任务失败");
        }
        logger.info("友盟9点定时推送开始任务日期{}结束", DateUtils.getTime());
    }
}
