package com.dchealth.medical.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;
import java.util.Date;

@Data
public class PatientRecordOutbound extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 档案id */
    @Excel(name = "档案id")
    private Long recordId;

    /** 研究ID */
    @Excel(name = "研究ID")
    private Long researchId;

    /** 外呼设置ID */
    @Excel(name = "外呼设置ID")
    private Long outboundId;

    /** 电话模板ID */
    @Excel(name = "电话模板ID")
    private String speechId;

    /** 随访节点名称 */
    @Excel(name = "随访节点名称")
    private String name;

    /** 随访次数 */
    @Excel(name = "随访次数")
    private Integer stageNum;

    /** 外呼类型0无状态1智能外呼2短信提醒3app站内信 */
    @Excel(name = "外呼类型0无状态1智能外呼2短信提醒3app站内信")
    private String outboundType;

    /** 提醒类型0无1随访2超窗3预警 */
    @Excel(name = "提醒类型0无1随访2超窗3预警")
    private String remindType;

    /** 提醒类型0无1待呼叫2已呼3已返回 */
    @Excel(name = "提醒类型0无1待呼叫2已呼3已返回")
    private String status;

//    @Excel(name = "呼叫时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date outboundTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planVisitTime;//计划随访时间

    /** 批次号 */
    @Excel(name = "批次号")
    private String batch;

    private String taskId;

    //讯飞记录id
    private String xunfeiRecordId;

    /** 讯飞模板id */
    private String xunfeiSpeechId;

    /** 电话拨打时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date xunfeiCallTime;

    /** 任务开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date xunfeiTaskTime;

    /** 方案名称 */
    private String xunfeiPlanName;

    /** 电话模版名称 */
    private String xunfeiSpeechName;

    /** 通话时长秒 */
    private Integer xunfeiCalltimeLength;

    /** 电话任务状态描述 */
    private String xunfeiCallResult;

    /** 任务状态枚举值说明0成功2通话中3无法接通4关机5用户正忙6空号7号码错误9停机15客户接听后并主动挂机20用户未接22来电提醒转来电提醒23呼入限制26网络故障28线路故障30呼叫失败300任务执行失败 */
    private String xunfeiResultCode;

    /** 接通状态枚举值说明1正常回答2自动留言3接听不便4居民已死亡5家属不能代答6不愿配合7号码错误8中断 */
    private String xunfeiEndNode;

    /** 音频地址 */
    private String xunfeiRecordUrl;

    /** 短信模板名称 */
    private String xunfeiSmsName;

    /** 短信内容 */
    private String xunfeiSmsContent;

    /** 对话内容json字符串,resultCode为0或15时才会有值 */
    private String xunfeiDialog;

    private String patientName;
    private Long patientId;
    private String patientCode;
    private String idCard;

    //查询批次汇总数据时筛选当日数据用
    private String outboundCreateTime;

    private String mobile;

    //是否下载语音文件到本地0否1是
    private String isDownloadSound;

    //本地语音文件
    private String localRecordUrl;

    private String projectName;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("recordId", getRecordId())
            .append("researchId", getResearchId())
            .append("outboundId", getOutboundId())
            .append("speechId", getSpeechId())
            .append("outboundType", getOutboundType())
            .append("remindType", getRemindType())
            .append("status", getStatus())
            .append("batch", getBatch())
            .append("updateBy", getUpdateBy())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
