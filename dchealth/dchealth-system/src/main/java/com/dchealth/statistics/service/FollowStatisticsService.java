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
}
