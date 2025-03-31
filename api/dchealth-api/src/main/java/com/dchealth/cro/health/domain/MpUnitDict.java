package com.dchealth.cro.health.domain;

import java.util.Date;

import lombok.Data;


@Data
public class MpUnitDict  {
    
    private Integer id;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 对应表中字段
     */
    private String code;
    /**
     * 对应表字段中文名称
     */
    private String name;
    /**
     * 单位
     */
    private String value;
    /**
     * 参考值
     */
    private String defValue;
    /**
     * 是否启用 1：启用 0：禁用 默认1
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 最小值
     */
    private String minValue;
    /**
     * 最大值
     */
    private String maxValue;
    /**
     * 类型：男，女，小孩
     */
    private String type;
}

