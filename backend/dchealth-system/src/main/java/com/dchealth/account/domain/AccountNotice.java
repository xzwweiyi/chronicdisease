package com.dchealth.account.domain;

import com.dchealth.common.constant.Constants;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.medical.contants.MedicalContants;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.vo.AccountNoticeVo;
import com.dchealth.research.domain.ResearchOutbound;
import com.dchealth.research.domain.ResearchUserRole;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import java.util.Date;

@Data
public class AccountNotice extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long accountId;

    /** 档案ID */
    @Excel(name = "档案ID")
    private Long recordId;

    private String title;

    /** 类型 1通知 2 公告 3 个人消息 4 档案消息 5 质疑消息 */
    @Excel(name = "类型 1通知 2 公告 3 个人消息 4 档案消息 5 质疑消息")
    private String type;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String tenantId;

    /** 是否已读 */
    @Excel(name = "是否已读")
    private String isRead;

    /** 通知内容 */
    @Excel(name = "通知内容")
    private String content;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String extendJson;



    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("accountId", getAccountId())
            .append("recordId", getRecordId())
            .append("type", getType())
            .append("tenantId", getTenantId())
            .append("isRead", getIsRead())
            .append("content", getContent())
            .append("extendJson", getExtendJson())
            .append("updateTime", getUpdateTime())
            .append("createTime", getCreateTime())
            .toString();
    }

    public AccountNotice(){}
    public AccountNotice(PatientRecord patientRecord, ResearchOutbound researchOutbound, String type){
        this.accountId = patientRecord.getAccountId();
        this.recordId = patientRecord.getRecordId();
        this.type = type;
        this.setCreateTime(DateUtils.getNowDate());
        this.isRead = Constants.NO;
        if("6".equals(type)){
            //随访提醒
            this.title = MedicalContants.MESSAGE_TITLE.get("6");
            this.content = researchOutbound.getFollowMailContent();
            AccountNoticeVo accountNoticeVo = new AccountNoticeVo();
            accountNoticeVo.setRecordId(patientRecord.getRecordId());
            accountNoticeVo.setType("6");
            accountNoticeVo.setResearchId(patientRecord.getResearchId());
            accountNoticeVo.setPatientId(patientRecord.getPatientId());
            this.extendJson = com.alibaba.fastjson.JSON.toJSONString(accountNoticeVo);
        }else if("7".equals(type)){
            //超窗提醒
            this.title = MedicalContants.MESSAGE_TITLE.get("7");
            this.content = researchOutbound.getOverMailContent();
            AccountNoticeVo accountNoticeVo = new AccountNoticeVo();
            accountNoticeVo.setRecordId(patientRecord.getRecordId());
            accountNoticeVo.setType("7");
//            accountNoticeVo.setStageName(patientRecord.getName());
            this.extendJson = com.alibaba.fastjson.JSON.toJSONString(accountNoticeVo);
        }
    }

    public AccountNotice(PatientRecord patientRecord, String msgContent, String type){
        this.accountId = patientRecord.getAccountId();
        this.recordId = patientRecord.getRecordId();
        this.type = type;
        this.setCreateTime(DateUtils.getNowDate());
        this.isRead = Constants.NO;
        if("9".equals(type) || "10".equals(type) || "11".equals(type) || "12".equals(type)|| "13".equals(type)|| "14".equals(type) || "19".equals(type)|| "20".equals(type)){
            //9患者随访意见提醒,10质疑提醒,11入组提醒, 19.邀请入组
            this.title = MedicalContants.MESSAGE_TITLE.get(type);
            this.content = msgContent;
            AccountNoticeVo accountNoticeVo = new AccountNoticeVo();
            accountNoticeVo.setRecordId(patientRecord.getRecordId());
            accountNoticeVo.setType(type);
            this.extendJson = com.alibaba.fastjson.JSON.toJSONString(accountNoticeVo);
        }
    }

//    public AccountNotice(PatientRecord patientRecord, String msgContent, String type,Long accountId){
//        this.accountId = accountId;
//        this.recordId = patientRecord.getRecordId();
//        this.type = type;
//        this.setCreateTime(DateUtils.getNowDate());
//        this.isRead = Constants.NO;
//        if("18".equals(type)){
//            //预警提醒
//            this.title = "预警处理任务提醒";
//            this.content = msgContent;
//            AccountNoticeVo accountNoticeVo = new AccountNoticeVo();
//            accountNoticeVo.setRecordId(patientRecord.getRecordId());
//            accountNoticeVo.setType(type);
//            this.extendJson = com.alibaba.fastjson.JSON.toJSONString(accountNoticeVo);
//        }
//    }

    public AccountNotice(SysUser sysUser, String msgContent, String type) {
        this.accountId = sysUser.getUserId();
        this.type = type;
        this.setCreateTime(DateUtils.getNowDate());
        this.isRead = Constants.NO;
        if ("15".equals(type)) {
            //9患者随访意见提醒,10质疑提醒,11入组提醒
            this.title = MedicalContants.MESSAGE_TITLE.get(type);
            this.content = msgContent;
            this.isRead = "N";
            AccountNoticeVo accountNoticeVo = new AccountNoticeVo();
            accountNoticeVo.setType(type);
            this.extendJson = com.alibaba.fastjson.JSON.toJSONString(accountNoticeVo);
        }
    }

    public AccountNotice(ResearchUserRole sysUser, PatientRecord patientRecord, String msgContent, String type) {
        this.accountId = sysUser.getUserId();
        this.recordId = patientRecord.getRecordId();
        this.type = type;
        this.setCreateTime(DateUtils.getNowDate());
        this.isRead = Constants.NO;
        if ("17".equals(type)) {
            this.title = MedicalContants.MESSAGE_TITLE.get(type);
            this.content = msgContent;
            AccountNoticeVo accountNoticeVo = new AccountNoticeVo();
            accountNoticeVo.setType(type);
            accountNoticeVo.setRecordId(patientRecord.getRecordId());
            this.extendJson = com.alibaba.fastjson.JSON.toJSONString(accountNoticeVo);
        }
    }

    public AccountNotice(Long userId, PatientRecord patientRecord, String msgContent, String type) {
        this.accountId = userId;
        this.recordId = patientRecord.getRecordId();
        this.type = type;
        this.setCreateTime(DateUtils.getNowDate());
        this.isRead = Constants.NO;
        if ("17".equals(type)) {
            this.title = MedicalContants.MESSAGE_TITLE.get(type);
            this.content = msgContent;
            AccountNoticeVo accountNoticeVo = new AccountNoticeVo();
            accountNoticeVo.setType(type);
            accountNoticeVo.setRecordId(patientRecord.getRecordId());
            this.extendJson = com.alibaba.fastjson.JSON.toJSONString(accountNoticeVo);
        }
    }

}
