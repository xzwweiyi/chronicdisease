package com.dchealth.redis.service.impl;

import com.alibaba.fastjson2.JSON;
import com.dchealth.listener.FollowTreeEvent;
import com.dchealth.system.domain.PatientCardInfo;
import com.dchealth.system.service.IPatientCardInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Component
public class RedisMessageListener implements MessageListener {

    @Resource(name = "redisTemplateSelect1")
    private RedisTemplate redisTemplate;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    public IPatientCardInfoService patientCardInfoService;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        // 接收的topic
        log.info("channel:" + new String(pattern));

        //序列化对象（特别注意：发布的时候需要设置序列化；订阅方也需要设置序列化）
        Object deserialize = redisTemplate.getValueSerializer().deserialize(message.getBody());
        log.info("序列化对象{}",deserialize);
//        System.out.println("JSON.toJSONString(deserialize) = " + JSON.toJSONString(deserialize));
//        log.info(messageDto.getData()+","+messageDto.getContent());

        if ("followTree".equals(new String(pattern))) {
            //  deserialize队列所有者id + admin
            if (Long.valueOf(deserialize.toString()).equals(1L)) {
                applicationContext.publishEvent(new FollowTreeEvent(JSON.toJSONString(deserialize), 1L, Boolean.FALSE));
            } else {
                applicationContext.publishEvent(new FollowTreeEvent(JSON.toJSONString(deserialize), 1L,Boolean.FALSE));
                applicationContext.publishEvent(new FollowTreeEvent(JSON.toJSONString(deserialize), Long.valueOf(deserialize.toString()), Boolean.FALSE));
            }
        } else if ("patientCard".equals(new String(pattern))) {
            PatientCardInfo patientCardInfo = JSON.parseObject((String) deserialize, PatientCardInfo.class);
            patientCardInfoService.updateMasterNumber(patientCardInfo);
            Map<String, Integer> stringIntegerMap = patientCardInfoService.updatePatientAllPersonId();
            log.info("刷新患者主号id{}",JSON.toJSONString(stringIntegerMap));
        }

    }

}

