package com.dchealth.dynamic.domain;

import lombok.Data;

@Data
public class DynamicColumn {
    /**
     * 列名称
     */
    private String columnName;
    /**
     * 列类型
     */
    private String columnType;

    /**
     * 是否允许为null
     */
    private boolean isAllowedNull;

    /**
     * 是否为主键
     */
    private boolean isPrimaryKey;

    /**
     * 是否自增长
     */
    private boolean isAutoIncrease;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 注释
     */
    private String comment;

    /**
     * 是否创建索引
     */
    private boolean isCreateIndex;
}
