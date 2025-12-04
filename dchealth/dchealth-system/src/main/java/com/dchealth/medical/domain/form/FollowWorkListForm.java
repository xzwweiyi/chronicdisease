package com.dchealth.medical.domain.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 随访工作站列表表单
 */
@Data
public class FollowWorkListForm {

    /**
     * 队列ID
     */
    private Long researchId;

    /**
     * 随访状态0无状态1待处理1进行中2已完成
     */
    private Integer followStatus;



    /**
     * 预警状态0无预警1待处理2进行中3已完成
     */
    private Integer warnStatus;

    /**
     * 超窗状态0无超窗1待处理(超窗)2完成
     */
    private Integer overTimeStatus;
    /**
     * 超窗状态集合
     */
    private List<Integer> overTimeStates;

    /**
     * 入组处理状态1待处理3已完成
     */
    private Integer enrollmentHandlerStatus;

    /**
     * 入组状态 0未入组1已入组
     */
    private Integer enrollmentStatus;
    /**
     * 入组开始时间
     */
    private String enrollmentStartTime;
    /**
     * 入组结束时间
     */
    private String enrollmentEndTime;

    /**
     * 是否待分配0否1是
     */
    private Integer allocatedStatus;

    /**
     * 入组处理状态0无状态1待处理3已完成
     */
    private String allocatedHandlerStatus;

    /**
     * 填写状态 0未填写1已填写
     */
    private Integer fillStatus;

    //随访质疑时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date questionTime;

    //随访完成时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date followCompleteTime;

    //渠道来源1后台2扫码
    private String channelType;

    private String patientName;
    private Long patientId;
    private Long personId;

    private Long deptId;

    /**
     * 0未绑定 1已绑定
     */
    private String bindStatus;

    /**
     * 患者评价状态0无状态1待评价2已完成
     */
    private Integer evaluateStatus;

    /**
     * 患者所在随访阶段 基线、随访01、随访02等
     */
    private String stageName;
    /**
     * 完成状态的查询sql
     */
    private String followStatusSql;
    /**
     * 患者状态 1查询患者状态
     */
    private String patientStatus;

    /**
     * 疾病id
     */
    private Long diseaseTypeId;
}
