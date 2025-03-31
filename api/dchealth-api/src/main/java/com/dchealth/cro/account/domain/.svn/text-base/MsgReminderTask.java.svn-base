package com.dchealth.cro.account.domain;

import java.util.Date;
import java.io.Serializable;

/**
 * 随访消息提醒配置表(MsgReminderTask)实体类
 *
 * @author makejava
 * @since 2023-06-06 09:38:21
 */
public class MsgReminderTask implements Serializable {
    private static final long serialVersionUID = 970182646218323019L;
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

}

