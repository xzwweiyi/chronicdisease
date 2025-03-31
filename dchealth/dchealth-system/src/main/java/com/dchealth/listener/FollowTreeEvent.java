package com.dchealth.listener;

import org.springframework.context.ApplicationEvent;

/**
 * 缓存事件
 */
public class FollowTreeEvent extends ApplicationEvent {
    private Long userId;
    public FollowTreeEvent(Object source,Long userId) {
        super(source);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
