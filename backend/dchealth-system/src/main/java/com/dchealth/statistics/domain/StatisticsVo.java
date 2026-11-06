package com.dchealth.statistics.domain;

import lombok.Data;

import java.util.List;

@Data
public class StatisticsVo {
    private String name;
    /**
     * 随访人次
     */
    private Long followNums;
    /**
     * 随访人次占比
     */
    private String followRate;
    /**
     * 随访人次占比
     */
    private String dropRate;
    /**
     * 脱落人次
     */
    private Long dropNums;
    /**
     * 随访总人次
     */
    private Long total;
    /**
     * 子级数据（如病区）
     */
    private List<StatisticsVo> children;
}
