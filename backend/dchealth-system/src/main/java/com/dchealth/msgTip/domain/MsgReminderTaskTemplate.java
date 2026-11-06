package com.dchealth.msgTip.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * 随访消息提醒模板(MsgReminderTaskTemplate)实体类
 *
 * @author makejava
 * @since 2023-06-06 10:05:21
 */

public class MsgReminderTaskTemplate implements Serializable {
    private static final long serialVersionUID = -18423936132979916L;
    /**
     * 随访提醒模板主键
     */
    private Long id;
    /**
     * 随访提醒配置ID
     */
    private Long reminderTaskId;
    /**
     * 提醒方式：1智能外呼2短信提醒3App站内提醒消息
     */
    private Integer tipType;
    /**
     * 短信提醒模板
     */
    private String msgTipTemplate;
    /**
     * 电话提醒模板
     */
    private String phoneTipTemplate;
    /**
     * 重播次数：0不重播1重播1次2重播2次
     */
    private Integer replayTime;
    /**
     * 与上次提醒间隔时间数
     */
    private Integer intervalTime;
    /**
     * 间隔单位：1:min 2:seconds 3hour
     */
    private Integer intervalUnit;
    
    private Date createTime;
    
    private Date updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReminderTaskId() {
        return reminderTaskId;
    }

    public void setReminderTaskId(Long reminderTaskId) {
        this.reminderTaskId = reminderTaskId;
    }

    public Integer getTipType() {
        return tipType;
    }

    public void setTipType(Integer tipType) {
        this.tipType = tipType;
    }

    public String getMsgTipTemplate() {
        return msgTipTemplate;
    }

    public void setMsgTipTemplate(String msgTipTemplate) {
        this.msgTipTemplate = msgTipTemplate;
    }

    public String getPhoneTipTemplate() {
        return phoneTipTemplate;
    }

    public void setPhoneTipTemplate(String phoneTipTemplate) {
        this.phoneTipTemplate = phoneTipTemplate;
    }

    public Integer getReplayTime() {
        return replayTime;
    }

    public void setReplayTime(Integer replayTime) {
        this.replayTime = replayTime;
    }

    public Integer getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Integer intervalTime) {
        this.intervalTime = intervalTime;
    }

    public Integer getIntervalUnit() {
        return intervalUnit;
    }

    public void setIntervalUnit(Integer intervalUnit) {
        this.intervalUnit = intervalUnit;
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

}

