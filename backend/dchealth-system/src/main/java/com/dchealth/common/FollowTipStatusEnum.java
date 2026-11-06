package com.dchealth.common;


import com.dchealth.common.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Objects;

/**
 * @Auther: cuitao
 * @Date: 2023/4/6 - 04 - 06 - 15:00
 * @Description: com.dchealth.cro.core
 */
public enum FollowTipStatusEnum {

    NO_STATUS(-1, "无状态"),
    BASELINE_REMINDER(0, "基线创建提醒：您已加入本队列，基础档案已为您建立，可点击填写"),
    FOLLOW_UP_TIME_REMINDER(1, "随访时间提醒：下次随访将在%s开始，请尽量来院随访，如无法来院可直接点击【线上随访】"),
    FOLLOW_UP_VISIT_DUE_REMINDER(2, "随访到期提醒：您有一个随访已到时间，请尽快完成随访"),
    FOLLOW_UP_VISIT_SUBMISSION_REMINDER(3, "随访提交提醒：已提交随访，医生将给出随访意见，请耐心等候"),
    FOLLOW_UP_QUESTIONING_REMINDER(4, "随访质疑提醒：医生向您发起了质疑，请尽快查看并回复"),
    FOLLOW_UP_OPINION_REMINDER(5, "随访意见提醒：医生已给出随访意见，请给出本次随访评价"),
    CHANGE_QUEUE_ALERT(6, "变更队列提醒：医生变更队列到%s，请点击随访查看具体意见"),
    MISSING_VISIT_REMINDER(7, "失访提醒：%s"),
    CHANGE_OF_FOLLOW_UP_TIME_REMINDER(8, "变更随访时间提醒：本次随访时间已更新，请按时完成随访"),
    IGNORE_THIS_FOLLOW_UP_REMINDER(9, "忽略本次随访提醒：已与您沟通本次暂不随访"),
    REMINDER_OF_ALL_FOLLOW_UP_VISITS_COMPLETED(10, "所有随访完成后提示：您已完成所有随访，感谢您的配合，祝您身体健康");

    private Integer code;
    private String name;

    private FollowTipStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getStatusDesc(Integer code,String other) {
        for (FollowTipStatusEnum followTipStatusEnum : FollowTipStatusEnum.values()) {
            if (followTipStatusEnum.getIndex() == code) {
                if(Objects.equals(FollowTipStatusEnum.FOLLOW_UP_TIME_REMINDER.getIndex(),code)){
                    return getNextFollowTime(code, DateUtils.parseDate(other));
                }
                if(Objects.equals(FollowTipStatusEnum.MISSING_VISIT_REMINDER.getIndex(),code)){
                    return getReplaceResult(code,other);
                }
                if(Objects.equals(FollowTipStatusEnum.CHANGE_QUEUE_ALERT.getIndex(),code)){
                    return getReplaceResult(code,other);
                }
                if(Objects.equals(FollowTipStatusEnum.MISSING_VISIT_REMINDER.getIndex(),code)){
                    return getReplaceResult(code,other);
                }

                return followTipStatusEnum.name;
            }
        }
        return null;
    }
    public static String getStatusDesc(Integer code) {
        for (FollowTipStatusEnum followTipStatusEnum : FollowTipStatusEnum.values()) {
            if (followTipStatusEnum.getIndex() == code) {
                return followTipStatusEnum.name;
            }
        }
        return null;
    }

    private static String getReplaceResult(Integer code, String other) {
        String statusDesc = FollowTipStatusEnum.getStatusDesc(code);
        if (StringUtils.isNotEmpty(statusDesc)) {
            if(StringUtils.isEmpty(other)){other ="" ;}
            return statusDesc.replace("%s", other);
        }
        return statusDesc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return code;
    }

    public void setIndex(Integer index) {
        this.code = index;
    }
    private  static String getNextFollowTime(Integer code,Date date) {
        DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, date);
        String statusDesc = FollowTipStatusEnum.getStatusDesc(code);
        if (StringUtils.isNotEmpty(statusDesc)) {
            return statusDesc.replace("%s", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, date));
        }
        return statusDesc;
    }

}
