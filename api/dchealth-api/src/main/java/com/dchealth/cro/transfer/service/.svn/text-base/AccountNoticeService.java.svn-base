package com.dchealth.cro.transfer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dchealth.cro.account.domain.AccountNotice;
import com.dchealth.cro.account.mapper.AccountNoticeMapper;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.core.config.UmConfig;
import com.dchealth.cro.core.umpush.UmpushService;
import com.dchealth.cro.transfer.vo.AccountNoticeForm;
import com.dchealth.cro.transfer.vo.AccountNoticePushVo;

@Service
public class AccountNoticeService {
    private static final Logger L = LoggerFactory.getLogger(AccountNoticeService.class);

    @Autowired
    private AccountNoticeMapper accountNoticeMapper;

    @Autowired
    private UmpushService umpushService;


    public DchealthCloudResponseEntity<AccountNoticePushVo> getNoticeList(AccountNoticeForm accountNoticeForm) {
        DchealthCloudResponseEntity<AccountNoticePushVo> result = new DchealthCloudResponseEntity<AccountNoticePushVo>();
        AccountNoticePushVo accountNoticePushVo = new AccountNoticePushVo();
        List<AccountNotice> list = new ArrayList<>();

        AccountNotice accountNotice = new AccountNotice();
        accountNotice.setIsSendPush("0");
        accountNotice.setCreateTimeStr(DateUtils.dateNowTime());
        List<AccountNotice> list1 = accountNoticeMapper.selectAccountNoticeList(accountNotice);
        if(list1.size()>0){
            List<AccountNotice> list2 = list1.stream().filter(x ->
                    !"12".equals(x.getType()) && !"13".equals(x.getType()) && !"14".equals(x.getType()) && !"17".equals(x.getType())).collect(Collectors.toList());
            if(list2.size()>0){
                list.addAll(list2);
            }
        }
        accountNoticePushVo.setAccountNoticeList(list);
        if(list.size()>0){
            for(AccountNotice a:list){
                a.setIsSendPush("1");
                accountNoticeMapper.updateAccountNotice(a);
            }
        }
        //appType 1医生2患者
        UmConfig doctorConfig = umpushService.getUmConfigInstance(1);
        UmConfig patientConfig = umpushService.getUmConfigInstance(2);
        accountNoticePushVo.setPatientConfig(patientConfig);
        accountNoticePushVo.setDoctorConfig(doctorConfig);
        result.setData(accountNoticePushVo);
        return result;
    }



    public DchealthCloudResponseEntity<AccountNoticePushVo> getNoticeNineList(AccountNoticeForm accountNoticeForm) {
        DchealthCloudResponseEntity<AccountNoticePushVo> result = new DchealthCloudResponseEntity<AccountNoticePushVo>();
        AccountNoticePushVo accountNoticePushVo = new AccountNoticePushVo();
        List<AccountNotice> list = new ArrayList<>();

        AccountNotice accountNotice = new AccountNotice();
        accountNotice.setIsSendPush("0");
        accountNotice.setCreateTimeStr(DateUtils.dateNowTime());
        accountNotice.setType("17");
        List<AccountNotice> list1 = accountNoticeMapper.selectAccountNoticeList(accountNotice);

        accountNotice.setType(null);
        accountNotice.setTypeMin("12");
        accountNotice.setTypeMax("14");
        List<AccountNotice> list2 = accountNoticeMapper.selectAccountNoticeList(accountNotice);

        list.addAll(list1);
        list.addAll(list2);
        accountNoticePushVo.setAccountNoticeList(list);
        if(list.size()>0){
            for(AccountNotice a:list){
                a.setIsSendPush("1");
                accountNoticeMapper.updateAccountNotice(a);
            }
        }
        //appType 1医生2患者
        UmConfig doctorConfig = umpushService.getUmConfigInstance(1);
        UmConfig patientConfig = umpushService.getUmConfigInstance(2);
        accountNoticePushVo.setPatientConfig(patientConfig);
        accountNoticePushVo.setDoctorConfig(doctorConfig);
        result.setData(accountNoticePushVo);
        return result;
    }

}
