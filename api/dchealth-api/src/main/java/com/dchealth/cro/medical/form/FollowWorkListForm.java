package com.dchealth.cro.medical.form;

import lombok.Data;

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
     * 随访状态0无状态1待处理2进行中3已完成
     */
    private String followStatus;

    /**
     * 预警状态0无预警1待处理2进行中3已完成
     */
    private String warnStatus;

    /**
     * 评价状态0无状态1待评价2已完成
     */
    private String  evaluateStatus;

    /**
     * 超窗状态0无超窗1待处理(超窗)2完成
     */
    private String overTimeStatus;

    /**
     * 入组处理状态1待处理3已完成
     */
    private String enrollmentHandlerStatus;

    /**
     * 是否待分配0否1是
     */
    private String allocatedStatus;

    /**
     * 入组处理状态0无状态1待处理3已完成
     */
    private String allocatedHandlerStatus;

    private String accountId;
    private Long accountIds;
    private Integer pageNo =1;//页码
    private Integer pageSize =10;//每页记录数
    private String searchKey;//搜索值

}
