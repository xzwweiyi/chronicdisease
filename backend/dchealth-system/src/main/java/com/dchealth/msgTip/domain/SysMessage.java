package com.dchealth.msgTip.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息表-包括push，消息，提醒等数据(SysMessage)实体类
 *
 * @author makejava
 * @since 2023-07-11 13:50:24
 */
public class SysMessage implements Serializable {
    private static final long serialVersionUID = -71010928818214993L;
    
    private Integer id;
    
    private Long userId;
    /**
     * 1:医生；2：患者
     */
    private Integer userType;
    /**
     * 随访记录id
     */
    private Long recordId;
    /**
     * 队列id
     */
    private Long researchId;
    /**
     * 0未读，1：已读
     */
    private Integer status;
    /**
     *  消息类型： 1:随访前提醒;2:随访后提醒;3：超窗提醒;4:预警提醒；5：随访意见；6：质疑提醒；7入组提醒；8：质疑未处理；9预警未处理；10随访未提交；11：满意度评价；12：随访处理任务；13：超窗处理任务；13：预警处理任务
     */
    private Integer msgType;
    /**
     * 消息内容
     */
    private String msgContent;
    /**
     * 接收地址
     */
    private String toAddress;
    /**
     * 接收人
     */
    private String toUserName;
    /**
     * 发送时间
     */
    private Date sendTime;
    /**
     * 发送状态 1：成功，2：失败
     */
    private Integer sendState;/**
     * 提醒方式：1智能外呼2短信提醒3App站内提醒消息
     */
    private Integer tipType;
    
    private Date createTime;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Integer getTipType() {
        return tipType;
    }

    public void setTipType(Integer tipType) {
        this.tipType = tipType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getSendState() {
        return sendState;
    }

    public void setSendState(Integer sendState) {
        this.sendState = sendState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Long getResearchId() {
        return researchId;
    }

    public void setResearchId(Long researchId) {
        this.researchId = researchId;
    }
}

