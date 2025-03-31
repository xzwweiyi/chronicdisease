package com.dchealth.medical.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 队列角度患者vo
 */
@Data
public class ResearchPatientVO {

    /**
     * 患者ID
     */
    private Long patientId;

    /**
     * 档案ID
     */
    private Long recordId;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 阶段名称
     */
    private String stageName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 队列名称
     */
    private String projectName;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 入组处理结果0无状态1待分配2变更3确认入组
     */
    private String enrollmentResult;

    /**
     * 入组结果文本
     */
    private String enrollmentResultContent;

    /** 队列id */
    private Long researchId;

    /**
     * 入组时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enrollmentTime;


    //随访质疑时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date questionTime;

    //随访完成时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date followCompleteTime;

    //随访状态0无状态1待处理2进行中3已完成
    private String followStatus;

    //渠道来源1后台2扫码
    private String channelType;
}
