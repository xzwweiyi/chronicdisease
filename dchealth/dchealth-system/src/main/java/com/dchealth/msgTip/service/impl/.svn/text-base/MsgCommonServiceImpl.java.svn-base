package com.dchealth.msgTip.service.impl;

import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.mapper.PatientInfoMapper;
import com.dchealth.medical.mapper.PatientRecordMapper;
import com.dchealth.msgTip.domain.SysMessage;
import com.dchealth.msgTip.mapper.SysMessageMapper;
import com.dchealth.msgTip.service.MsgCommonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author caihe
 * @since 2023/7/12
 * 消息处理通用类
 */
@Service
public class MsgCommonServiceImpl implements MsgCommonService {

    @Resource
    SysMessageMapper sysMessageMapper;
    @Resource
    PatientRecordMapper patientRecordMapper;
    @Resource
    PatientInfoMapper patientInfoMapper;
    /**
     * 保存消息信息
     * @param msg
     * @return
     */
    @Override
    public String saveMessage(SysMessage msg) {
        msg.setStatus(0);
        sysMessageMapper.insert(msg);
        return "success";
    }

    /**
     * @param msgType 消息类型： 1:随访前提醒;2:随访后提醒;3：超窗提醒;4:预警提醒；5：随访意见；6：质疑提醒；7入组提醒；8：质疑未处理；9预警未处理；10随访未提交；11：满意度评价；12：随访处理任务；13：超窗处理任务；13：预警处理任务
     * @param msgContent
     * @param patientRecord
     * @return
     */
    @Override
    public String saveMessageForDoctor(Integer msgType,String msgContent, PatientRecord patientRecord) {
        SysMessage msg = new SysMessage();
        if(msgType==7){
            //查询患者信息
            PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(patientRecord.getPatientId());
            patientRecord.setPatientId(patientInfo.getPatientId());
            patientRecord.setResearchId(patientInfo.getResearchId());
        }

        if(patientRecord.getRecordId()!=null && patientRecord.getPatientId()==null){
            patientRecord = patientRecordMapper.selectPatientRecordByRecordId(patientRecord.getRecordId());
        }
        msg.setResearchId(patientRecord.getResearchId());
        msg.setTipType(3);
        msg.setMsgContent(msgContent);
        msg.setMsgType(msgType);
        msg.setRecordId(patientRecord.getRecordId());
        msg.setUserType(2);
        msg.setUserId(patientRecord.getPatientId());
        msg.setStatus(0);
        sysMessageMapper.insert(msg);
        return "success";
    }
}
