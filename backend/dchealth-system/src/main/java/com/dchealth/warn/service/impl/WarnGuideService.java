package com.dchealth.warn.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.medical.domain.MpPerson;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.PatientRecordOperLog;
import com.dchealth.medical.mapper.MpPersonMapper;
import com.dchealth.medical.mapper.PatientRecordMapper;
import com.dchealth.medical.mapper.PatientRecordOperLogMapper;
import com.dchealth.system.mapper.SysUserMapper;
import com.dchealth.warn.domain.WarnLeaveMessage;
import com.dchealth.warn.domain.form.WarnGuideMsgForm;
import com.dchealth.warn.domain.vo.WarnGuideMsgListVO;
import com.dchealth.warn.mapper.WarnLeaveMessageMapper;
import com.dchealth.warn.service.IWarnGuideService;

@Service
public class WarnGuideService implements IWarnGuideService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private MpPersonMapper mpPersonMapper;
    @Autowired
    private PatientRecordMapper patientRecordMapper;
    @Autowired
    private WarnLeaveMessageMapper warnLeaveMessageMapper;
    @Autowired
    private PatientRecordOperLogMapper patientRecordOperLogMapper;


    @Override
    public List<WarnGuideMsgListVO> guideList(Long recordId) {
        List<WarnGuideMsgListVO> warnGuideMsgs = new ArrayList<>();

        WarnLeaveMessage warnLeaveMessage = new WarnLeaveMessage();
        warnLeaveMessage.setRecordId(recordId);
        List<WarnLeaveMessage> list = warnLeaveMessageMapper.selectWarnLeaveMessageList(warnLeaveMessage);
        if (list != null && !list.isEmpty()){
            list.forEach(x->{
                WarnGuideMsgListVO wvo = new WarnGuideMsgListVO();
                wvo.setMsgType(x.getContentType());
                wvo.setMsgTime(DateUtils.parseDateToStrFull(x.getCreateTime()));
                wvo.setMsgText(x.getContent());
                wvo.setUserType(x.getUserType());
                if ("1".equals(x.getUserType())){
                    SysUser sysUser = sysUserMapper.selectUserById(x.getAccountId());
                    if (sysUser != null){
                        wvo.setUserName(sysUser.getNickName());
                        wvo.setHeadImage(sysUser.getHeadImage());
                    }
                }else if ("2".equals(x.getUserType())){
                	MpPerson mpPerson = mpPersonMapper.selectPersonByAccountId(x.getAccountId());
                    if (mpPerson != null){
                        wvo.setUserName(mpPerson.getPersonName());
//                        wvo.setHeadImage(co);
                    }
                }
                warnGuideMsgs.add(wvo);
            });
        }
        return warnGuideMsgs;
    }


    @Override
    @Transactional
    public Long sendMsg(WarnGuideMsgForm warnGuideMsgForm) {
        WarnLeaveMessage warnLeaveMessage = new WarnLeaveMessage();
        warnLeaveMessage.setRecordId(warnGuideMsgForm.getRecordId());
        warnLeaveMessage.setAccountId(warnGuideMsgForm.getFromUserId());
        warnLeaveMessage.setUserType("1");
        warnLeaveMessage.setContentType(warnGuideMsgForm.getMsgType());
        if("1".equals(warnGuideMsgForm.getMsgType())){
            //文本
            warnLeaveMessage.setContent(warnGuideMsgForm.getMsgText());
        }else if("2".equals(warnGuideMsgForm.getMsgType())){
            //图片
            warnLeaveMessage.setContent(warnGuideMsgForm.getMsgImageUrl());
        }
        warnLeaveMessage.setCreateTime(new Date());
        warnLeaveMessageMapper.insertWarnLeaveMessage(warnLeaveMessage);
        PatientRecord patientRecord = new PatientRecord();
        patientRecord.setRecordId(warnGuideMsgForm.getRecordId());
        patientRecord.setWarnStatus("2");
        patientRecordMapper.updatePatientRecord(patientRecord);
        return warnLeaveMessage.getId();
    }

    @Override
    public int end(Long recordId,Long userId) {
        PatientRecord patientRecord = new PatientRecord();
        patientRecord.setRecordId(recordId);
        patientRecord.setWarnStatus("3");
        patientRecord.setWarnEvaluateStatusBack("1");
        patientRecordOperLogMapper.insertPatientRecordOperLog(new PatientRecordOperLog(userId,recordId,"2",DateUtils.getNowDate()));
        return patientRecordMapper.updatePatientRecord(patientRecord);
    }
}
