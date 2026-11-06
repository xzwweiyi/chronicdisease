package com.dchealth.research.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

@Data
public class ResearchOutbound extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    private Long researchId;

    /** 类型0无状态1智能外呼2短信提醒3App站内信 */
    @Excel(name = "类型0无状态1智能外呼2短信提醒3App站内信")
    private String type;

    /** 随访提前提醒天数 */
    @Excel(name = "随访提前提醒天数")
    private Integer followAdvanceDays;

    /** 随访提醒时间 */
    @Excel(name = "随访提醒时间")
    private String followAdvanceTime;

    /** 随访电话模板id */
    @Excel(name = "随访电话模板id")
    private String followSpeechId;

    /** 随访重拨次数0不重拨 */
    @Excel(name = "随访重拨次数0不重拨")
    private String followReplay;

    /** 随访重拨间隔分钟 */
    @Excel(name = "随访重拨间隔分钟")
    private Integer followReplaySpaceMinute;

    /** 随访站内信内容 */
    @Excel(name = "随访站内信内容")
    private String followMailContent;

    /** 超窗提前提醒天数 */
    @Excel(name = "超窗提前提醒天数")
    private Integer overAdvanceDays;

    /** 超窗提醒时间 */
    @Excel(name = "超窗提醒时间")
    private String overAdvanceTime;

    /** 超窗电话模板id */
    @Excel(name = "超窗电话模板id")
    private String overSpeechId;

    /** 超窗重拨次数0不重拨 */
    @Excel(name = "超窗重拨次数0不重拨")
    private String overReplay;

    /** 超窗重拨间隔分钟 */
    @Excel(name = "超窗重拨间隔分钟")
    private Integer overReplaySpaceMinute;

    /** 随访站内信内容 */
    @Excel(name = "随访站内信内容")
    private String overMailContent;

    /** 预警电话模板id */
    @Excel(name = "预警电话模板id")
    private String warnSpeechId;

    /** 预警重拨次数0不重拨 */
    @Excel(name = "预警重拨次数0不重拨")
    private String warnReplay;

    /** 预警重拨间隔分钟 */
    @Excel(name = "预警重拨间隔分钟")
    private Integer warnReplaySpaceMinute;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("type", getType())
            .append("followAdvanceDays", getFollowAdvanceDays())
            .append("followAdvanceTime", getFollowAdvanceTime())
            .append("followSpeechId", getFollowSpeechId())
            .append("followReplay", getFollowReplay())
            .append("followReplaySpaceMinute", getFollowReplaySpaceMinute())
            .append("followMailContent", getFollowMailContent())
            .append("overAdvanceDays", getOverAdvanceDays())
            .append("overAdvanceTime", getOverAdvanceTime())
            .append("overSpeechId", getOverSpeechId())
            .append("overReplay", getOverReplay())
            .append("overReplaySpaceMinute", getOverReplaySpaceMinute())
            .append("overMailContent", getOverMailContent())
            .append("warnSpeechId", getWarnSpeechId())
            .append("warnReplay", getWarnReplay())
            .append("warnReplaySpaceMinute", getWarnReplaySpaceMinute())
            .append("delFlag", getDelFlag())
            .append("updateBy", getUpdateBy())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
