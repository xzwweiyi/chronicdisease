package com.dchealth.medical.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class EvaluateVO {

    /**
     * 患者ID
     */
    private Long patientId;

    /**
     * 患者名称
     */
    private String patientName;

    /**
     * 阶段名称
     */
    private String name;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 队列
     */
    private String projectName;

    /**
     * 档案ID
     */
    private Long recordId;

    //后台回复评价时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date evaluateReplyBackTime;

    //患者评价时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date evaluateReplyTime;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date  overEvaluateReplyBackTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date  warnEvaluateReplyBackTime;
}
