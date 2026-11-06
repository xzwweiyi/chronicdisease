package com.dchealth.msgTip.domain;

import java.util.Date;
import java.io.Serializable;

/**
 * 随访消息提醒配置表(MsgReminderTask)实体类
 *
 * @author makejava
 * @since 2023-06-06 10:04:53
 */
public class MsgReminderTask implements Serializable {
    private static final long serialVersionUID = -23125351913964732L;
    /**
     * 随访提醒任务主键
     */
    private Long id;
    /**
     * 队列ID
     */
    private Long researchId;
    /**
     * 队列名称
     */
    private String name;
    /**
     * 提前提醒天数,单位：天
     */
    private Integer earlierTime;
    /**
     * 超窗天数设置
     */
    private Integer ultraWindowTime;
    /**
     * 提醒时间
     */
    private Date reminderTime;
    /**
     * 站内消息内容
     */
    private String stationMessage;
    /**
     * 随访方式：1随访2超窗
     */
    private Integer followType;
    /**
     * 提醒方式：1智能外呼2短信提醒3App站内提醒消息
     */
    private Integer tipType;
    /**
     * 是否删除该提醒方式：0否 1是
     */
    private Integer isDelete;
    
    private Date createTime;
    
    private Date updateTime;

    private Integer afterTime;
    /**
     * 电话或短信模板
     */
    private String template;
    /**
     * 重拨次数
     */
    private Integer redialNum;
    /**
     * 重拨间隔
     */
    private Integer redialInterval;
    /**
     * 重拨间隔单位
     */
    private Integer redialIntervalUnit;
    /**
     * 提醒区间开始时间
     */
    private String reminderStartTime;
    /**
     * 提醒区间结束时间
     */
    private String reminderEndTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResearchId() {
        return researchId;
    }

    public void setResearchId(Long researchId) {
        this.researchId = researchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEarlierTime() {
        return earlierTime;
    }

    public void setEarlierTime(Integer earlierTime) {
        this.earlierTime = earlierTime;
    }

    public Integer getUltraWindowTime() {
        return ultraWindowTime;
    }

    public void setUltraWindowTime(Integer ultraWindowTime) {
        this.ultraWindowTime = ultraWindowTime;
    }

    public Date getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(Date reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getStationMessage() {
        return stationMessage;
    }

    public void setStationMessage(String stationMessage) {
        this.stationMessage = stationMessage;
    }

    public Integer getFollowType() {
        return followType;
    }

    public void setFollowType(Integer followType) {
        this.followType = followType;
    }

    public Integer getTipType() {
        return tipType;
    }

    public void setTipType(Integer tipType) {
        this.tipType = tipType;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getAfterTime() {
        return afterTime;
    }

    public void setAfterTime(Integer afterTime) {
        this.afterTime = afterTime;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Integer getRedialNum() {
        return redialNum;
    }

    public void setRedialNum(Integer redialNum) {
        this.redialNum = redialNum;
    }

    public Integer getRedialInterval() {
        return redialInterval;
    }

    public void setRedialInterval(Integer redialInterval) {
        this.redialInterval = redialInterval;
    }

    public Integer getRedialIntervalUnit() {
        return redialIntervalUnit;
    }

    public void setRedialIntervalUnit(Integer redialIntervalUnit) {
        this.redialIntervalUnit = redialIntervalUnit;
    }

    public String getReminderStartTime() {
        return reminderStartTime;
    }

    public void setReminderStartTime(String reminderStartTime) {
        this.reminderStartTime = reminderStartTime;
    }

    public String getReminderEndTime() {
        return reminderEndTime;
    }

    public void setReminderEndTime(String reminderEndTime) {
        this.reminderEndTime = reminderEndTime;
    }
}

