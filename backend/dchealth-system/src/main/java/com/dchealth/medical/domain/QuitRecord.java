package com.dchealth.medical.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @description: 退出记录
 * @author zhangxiao
 */
@Data
public class QuitRecord {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 队列id
     */
    private String researchId;
    /**
     * 患者id
     */
    private String personId;
    /**
     * 退出原因
     */
    private String quitReason;
    /**
     * 退出时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date quitTime;
    /**
     * 是否同意
     */
    private Boolean isAgree;
    /**
     * 同意时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date agreeTime;
    /**
     * 拒绝原因
     */
    private String rejectReason;
    /**
     * 拒绝时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rejectTime;
    /**
     * 换着姓名
     */
    private String patientName;
    /**
     * 患者id
     */
    private String patientId;
    /**
     * 记录id
     */
    private String recordId;

}
