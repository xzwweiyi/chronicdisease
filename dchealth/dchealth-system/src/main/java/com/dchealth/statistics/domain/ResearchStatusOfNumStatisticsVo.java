package com.dchealth.statistics.domain;

import lombok.Data;

@Data
public class ResearchStatusOfNumStatisticsVo {

    /**
     * 横坐标（科室名或者病种名）
     */
    private String name;

    /**
     * 进行中的队列数量
     */
    private int goingNum = 0;

    /**
     * 已关闭的队列数量
     */
    private int closeNum = 0;
}
