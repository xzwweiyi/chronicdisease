package com.dchealth.cro.medical.domain;

import com.dchealth.cro.common.base.BaseEntity;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper=false)
public class PatientRecord extends BaseEntity {
    /*
     * 档案id
     */
    private Long recordId;

    /*
     * 患者ID
     */
    private Long patientId;

    /*
     * 研究项目ID
     */
    private Long researchId;

    /*
     * 随访节点id
     */
    private Long followId;

    /*
     *
     */
    private String name;

    /*
     * 同一个随访周期配置的随访次数
     */
    private Integer stageNum;
    /*
     * 同一个队列的随访总次数
     */
    private Integer stageAddNum;
    /*
     * 质疑状态0无质疑1待处理2进行中3已完成
     */
    private String questionStatus;

    /*
     * 预警状态0无预警1待处理2进行中3已完成
     */
    private String warnStatus;

    /*
     * 超窗状态0无超窗1待处理(超窗)2完成
     */
    private String overTimeStatus;

    /*
     * 超窗处理状态0无1更新随访时间2忽略本次3失访
     */
    private String overTimeHandleStatus;

    /*
     * 更新人
     */
    private String updateBy;

    /*
     * 创建人
     */
    private String createBy;

    /*
     * 生成随访时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /*
     * 最新更新时间
     */
    private Date updateTime;

    /*
     * 填写时间
     */
    private Date fillTime;

    /*
     * 患者填写状态0未填写1已填写
     */
    private String fillStatus;

    /*
     * 随访时间(可更新)
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date visitTime;

    /*
     * 锁定的随访时间不可更新
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lockVisitTime;

    /*
     * 评价状态0无状态1待评价2已完成
     */
    private String evaluateStatus;


    /**
     * 健康管理填写按钮控制
     */
    private Boolean healthEditable=false;


    //随访后台评价状态0无状态1待评价2已完成
    private String evaluateStatusBack;
    //后台回复时效1-5颗星
    private String  evaluatePrescriptionBack;
    //后台回复质量1-5颗星
    private String  evaluateQualityBack;
    //后台回复评价
    private String  evaluateReplyBack;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date  evaluateReplyBackTime;

    //告警后台评价状态0无状态1待评价2已完成
    private String warnEvaluateStatusBack;
    //后台回复时效1-5颗星
    private String  warnEvaluatePrescriptionBack;
    //后台回复质量1-5颗星
    private String  warnEvaluateQualityBack;
    //后台回复评价
    private String  warnEvaluateReplyBack;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date  warnEvaluateReplyBackTime;


    //超窗后台评价状态0无状态1待评价2已完成
    private String overEvaluateStatusBack;
    //后台回复时效1-5颗星
    private String  overEvaluatePrescriptionBack;
    //后台回复质量1-5颗星
    private String  overEvaluateQualityBack;
    //后台回复评价
    private String  overEvaluateReplyBack;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date  overEvaluateReplyBackTime;


    private Date followCompleteTime;

    /*
     * edc状态0无状态1待处理2进行中3已完成
     */
    private String edcStatus;
    /**
     * followTipStatus 随访状态记录
     */
    private Integer followTipStatus;
    /**
     * 随访状态描述
     */
    private String followTipStatusDesc;
    /**
     * isSeeDetail 随访状态按钮状态：0置灰 1显示 2消失
     */
    private Integer isSeeDetail;

    /*
     * 随访意见
     */
    private String followOpinion;

    private Long followOpinionUserId;

    /*
     * 随访状态0无状态1待处理2进行中3已完成
     */
    private String followStatus;


    /**
     * 入组时间
     */
    private Date enrollmentTime;


    /**
     * 患者名
     */
    private String patientName;

    /**
     * 随访名
     */
    private String followName;
    /**
     * 患者状态
     */
    private String patientStatus;
    /**
     * 回复时效1-5颗星
     */
    private String evaluatePrescription;
    /**
     * 回复质量1-5颗星
     */
    private String evaluateQuality;
    /**
     * 回复评价
     */
    private String evaluateReply;

    //回复评价时间
    private Date evaluateReplyTime;

    /*
     * 超窗处理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date overTimeHandleTime;

    /**
     * 失访状态 0不再参与 1失联 2其他
     */
    private String loseType;
    /**
     * 失访原因
     */
    private String loseReason;

    private Integer recordNum;//1基线2随访

    private Integer followNum;//1基线2随访
    private Integer overTimeNum;//超窗状态0无超窗1待处理(超窗)2完成
    private Integer warnNum;//超窗状态0无超窗1待处理(超窗)2完成
    /**
     * 忽略本次的原因 0 患者本次不随访 1 患者已经完成随访
     */
    private String ignoreType;

    /**
     * 0 保存  1 提交
     */
    private Integer isSubmit;
    /**
     * 随访提交时间
     */
    private Date followSubmitTime;

//    详情接口增加字段：isEditable 【true：可编辑，false：只读]
    private Boolean isEditable=true;

    private String accountId;

    private Long accountIds;

    private Double formComplentRate;

    public PatientRecord() {
    }

    /**
     * 生成计划随访使用
     *
     * @param old
     * @param recordTemplate
     */
    public PatientRecord(PatientRecord old, PatientRecordTemplate recordTemplate){
        this.followTipStatus = 2;
        this.researchId = old.getResearchId();
        this.patientId = old.getPatientId();
        if (old.getFollowId().longValue() != recordTemplate.getFollowId().longValue()){
            this.stageNum = 1;
        }else{
            this.stageNum = old.getStageNum() + 1;
        }
        this.followId = recordTemplate.getFollowId();
        if ("1".equals(recordTemplate.getFollowIntervalUnit())){
            this.visitTime = DateUtils.addDays(old.getLockVisitTime(),recordTemplate.getFollowInterval().intValue());
        }else if ("2".equals(recordTemplate.getFollowIntervalUnit())){
            this.visitTime = DateUtils.addDays(old.getLockVisitTime(),recordTemplate.getFollowInterval().intValue() * 7);
        }else if ("3".equals(recordTemplate.getFollowIntervalUnit())){
            this.visitTime = DateUtils.addDays(old.getLockVisitTime(),recordTemplate.getFollowInterval().intValue() * 30);
        }
        this.lockVisitTime = this.visitTime;
        this.setCreateTime(new Date());
        String name = StringUtils.isNotBlankAndNotEmpty(recordTemplate.getFollowName()) ? recordTemplate.getFollowName() : "随访";
        this.name = name + String.format("%02d", this.stageNum);
    }

    /**
     * 生成基线
     * @param patientInfo
     * @param recordTemplate
     */
    public PatientRecord(PatientInfo patientInfo, PatientRecordTemplate recordTemplate){
        this.researchId = patientInfo.getResearchId();
        this.patientId = patientInfo.getPatientId();
        this.stageNum = 1;
        this.visitTime = patientInfo.getEnrollmentTime();
        this.lockVisitTime = this.visitTime;
        this.setCreateTime(new Date());
        String name = StringUtils.isNotBlankAndNotEmpty(recordTemplate.getFollowName()) ? recordTemplate.getFollowName() : "基线";
        this.name = name;
        this.followId = recordTemplate.getFollowId();
        this.followTipStatus =0;
    }

}
