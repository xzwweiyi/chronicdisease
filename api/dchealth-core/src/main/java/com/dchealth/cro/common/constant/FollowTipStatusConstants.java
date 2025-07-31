package com.dchealth.cro.common.constant;

/**
 * @Auther: cuitao
 * @Date: 2023/4/6 - 04 - 06 - 9:46
 * @Description: com.dchealth.common.constant
 */
public class FollowTipStatusConstants {

    /**
     * 无状态
     */
    public static final Integer NO_STATUS = -1;

    /**
     * 基线创建提醒
     */
    public static final Integer BASELINE_REMINDER = 0;
    /**
     * 随访时间提醒
     */
    public static final Integer FOLLOW_UP_TIME_REMINDER = 1;
    /**
     * 随访到期提醒
     */
    public static final Integer FOLLOW_UP_VISIT_DUE_REMINDER = 2;
    /**
     * 随访提交提醒
     */
    public static final Integer FOLLOW_UP_VISIT_SUBMISSION_REMINDER = 3;
    /**
     * 随访质疑提醒
     */
    public static final Integer FOLLOW_UP_QUESTIONING_REMINDER = 4;
    /**
     * 随访意见提醒
     */
    public static final Integer FOLLOW_UP_OPINION_REMINDER = 5;
    /**
     * 变更队列提醒
     */
    public static final Integer CHANGE_QUEUE_ALERT = 6;
    /**
     * 失访提醒
     */
    public static final Integer MISSING_VISIT_REMINDER = 7;
    /**
     * 变更随访时间提醒
     */
    public static final Integer CHANGE_OF_FOLLOW_UP_TIME_REMINDER = 8;
    /**
     * 忽略本次随访提醒
     */
    public static final Integer IGNORE_THIS_FOLLOW_UP_REMINDER = 9;
    /**
     * 您已完成所有随访
     */
    public static final Integer REMINDER_OF_ALL_FOLLOW_UP_VISITS_COMPLETED = 10;
}
