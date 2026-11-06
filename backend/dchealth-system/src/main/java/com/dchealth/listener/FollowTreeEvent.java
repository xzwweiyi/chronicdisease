package com.dchealth.listener;

import org.springframework.context.ApplicationEvent;

/**
 * 缓存事件
 */
public class FollowTreeEvent extends ApplicationEvent {
    private Long userId;
    private Long diseasesId;
    private Boolean flag;

    private Integer num;

    public FollowTreeEvent(Object source, Long userId, Boolean flag) {
        super(source);
        this.userId = userId;
        this.flag = flag;
    }

    public FollowTreeEvent(Object source, Long userId, Long diseasesId, Integer num, Boolean  flag) {
        super(source);
        this.userId = userId;
        this.diseasesId = diseasesId;
        this.flag = flag;
        this.num = num;
    }


    public Long getUserId() {
        return userId;
    }

    public Long getDiseasesId() {
        return diseasesId;
    }

    public Boolean getFlag() {
        return flag;
    }

    public Integer getNum() {
        return num;
    }
}
