package com.dchealth.statistics.domain;


import lombok.Data;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2026/1/22 15:06
 * @Version: 1.0
 */
@Data
public class StatisticsRateResultVo {

    private String followRate;

    private String followNum;

    private String notFollowRateName;

    private String notFollowRate;

    private String followRateName;

    private String totalNum;
}
