package com.dchealth.statistics.service;

import com.dchealth.statistics.domain.*;

import java.util.List;
import java.util.Map;

public interface FollowStatisticsService {

    Map<String, List<StatisticsVo>> followDistribute(StatisticsParam statisticsParam);

    Map<String, List<StatisticsVo>> patientDistribute(StatisticsParam statisticsParam);

    Map<String, Object> hospitalView();

    List<ResearchStatusOfNumStatisticsVo> researchStatusNum(StatisticsParam statisticsParam);

    Map<String, Object> queryPatientDistribute(PatientDistribute distributeParam);

    StatisticsRateResultVo queryFollowRate(StatisticsFollowRateVo statisticsParam);

    /**
     * 查询当年12个月的月活跃趋势
     * @return 月活跃趋势数据
     */
    Map<String, Object> queryMonthlyActivityTrend(Integer yearStat);

    /**
     * 年龄分布统计（新分段逻辑）
     * @param statisticsParam 统计参数
     * @return 年龄分布数据
     */
    List<StatisticsVo> queryAgeDistribution(StatisticsParam statisticsParam);


}
