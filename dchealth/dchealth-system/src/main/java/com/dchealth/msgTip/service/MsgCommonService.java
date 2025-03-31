package com.dchealth.msgTip.service;

import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.msgTip.domain.SysMessage;

/**
 * @author caihe
 * @since 2023/7/12
 * 消息处理通用类
 */
public interface MsgCommonService {

    /**
     * 保存消息信息
     * @param msg
     * @return
     */
    String saveMessage(SysMessage msg);
    /**
     * 保存消息信息
     * @param msgType 消息类型： 1:随访前提醒;2:随访后提醒;3：超窗提醒;4:预警提醒；5：随访意见；6：质疑提醒；7入组提醒；8：质疑未处理；9预警未处理；10随访未提交；11：满意度评价；12：随访处理任务；13：超窗处理任务；13：预警处理任务
     * @param msgContent
     * @param patientRecord
     * @return
     */
    String saveMessageForDoctor(Integer msgType,String msgContent,PatientRecord patientRecord);

}
