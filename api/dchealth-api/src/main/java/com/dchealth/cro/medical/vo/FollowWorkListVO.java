package com.dchealth.cro.medical.vo;

import lombok.Data;

import java.util.Date;

/**
 * 随访工作站列表数据vo
 */
@Data
public class FollowWorkListVO {

    /**
     * 患者ID
     */
    private Long patientId;

    /**
     * 就诊ID
     */
    private String  personId;

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
    private String overTimeHandleStatus;
    private String loseType;
    private String loseReason;
    private Date visitTime;

    /** 预警等级 0无状态 1异常 2危机 */
    private String earlyWarnLevel;

    private  String warnStatus;

    /** 手动入组次数 */
    private int generatedManually;
}
