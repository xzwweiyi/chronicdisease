package com.dchealth.cro.account.domain;

import java.util.Date;

import com.dchealth.cro.cms.vo.AccountNoticeNewVo;
import com.dchealth.cro.common.constant.Constants;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.medical.contants.MedicalContants;
import com.dchealth.cro.medical.domain.PatientRecord;

import lombok.Data;


@Data
public class AccountNotice{

    /** 主键 */
    private Long id;

    /** 用户ID */
    private Long accountId;

    private Long recordId;

    private String title;

    /*1:通知2:公告3:个人消息4:档案消息5:质疑消息6:患者随访提醒7:患者超窗提醒8:患者预警提醒9:患者随访意见提醒10:患者质疑提醒11:患者入组提醒
12:患者质疑未处理13:患者预警未处理14:患者随访未提交15:医生满意度评价16:医生随访处理任务提醒17:医生超窗任务提醒18:医生预警处理任务提醒
19:邀请患者入组提醒20:取消入组 21.拒绝入组   */
    /** 类型 */
    private String type;

    /** 读取状态 */
    private String isRead;

    private String extendJson;

    /** 通知内容 */
    private String content;

    /** 租户ID */
    private String tenantId;

   private Date createTime;
   private Date updateTime;

   //是否发送友盟推送0否1是
   private String isSendPush;
   
   private Long noticeId;

   private Long researchId;
   private Long patientId;

   private String name;
   private String followTipStatus;
   private Date visitTime;
   private Date recordCreateTime;
   private String sex;
   private String warnStatus;

   private String createTimeStr;

   private String typeMax;
   private String typeMin;

    public AccountNotice(){}
    public AccountNotice(PatientRecord patientRecord, String msgContent, String type){
        this.accountId = patientRecord.getAccountIds();
        this.recordId = patientRecord.getRecordId();
        this.type = type;
        this.setCreateTime(DateUtils.getNowDate());
        this.isRead = Constants.NO;
        if("8".equals(type) || "9".equals(type) || "10".equals(type) || "11".equals(type) || "12".equals(type)|| "13".equals(type)|| "14".equals(type)|| "21".equals(type)){
            //预警提醒,9患者随访意见提醒,10质疑提醒,11入组提醒
            this.title = MedicalContants.MESSAGE_TITLE.get(type);;
            this.content = msgContent;
            AccountNoticeNewVo accountNoticeVo = new AccountNoticeNewVo();
            accountNoticeVo.setRecordId(patientRecord.getRecordId());
            accountNoticeVo.setType(type);
            this.extendJson = com.alibaba.fastjson.JSON.toJSONString(accountNoticeVo);
        }
    }

    public AccountNotice(PatientRecord patientRecord, String msgContent, String type,Long accountId){
        this.accountId = accountId;
        this.recordId = patientRecord.getRecordId();
        this.type = type;
        this.setCreateTime(DateUtils.getNowDate());
        this.isRead = Constants.NO;
        if("16".equals(type) || "18".equals(type) ){
            //预警提醒
            this.title = MedicalContants.MESSAGE_TITLE.get(type);;
            this.content = msgContent;
            AccountNoticeNewVo accountNoticeVo = new AccountNoticeNewVo();
            accountNoticeVo.setRecordId(patientRecord.getRecordId());
            accountNoticeVo.setType(type);
            this.extendJson = com.alibaba.fastjson.JSON.toJSONString(accountNoticeVo);
        }
    }

}
