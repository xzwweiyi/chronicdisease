package com.dchealth.common.enums;

import lombok.Getter;

@Getter
public enum HealthManageEnum {

    DIET("饮食", 1),
    EXERCISE("运动", 2),
    SLEEP("睡眠", 3),
    MEDICINE("用药", 4),
    OTHER("其他", 9);

    private final String name;
    private final int code;

    HealthManageEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public static String getName(int code) {
        for (HealthManageEnum c : HealthManageEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return null;
    }
}
