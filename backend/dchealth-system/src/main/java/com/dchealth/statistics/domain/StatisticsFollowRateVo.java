package com.dchealth.statistics.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2026/1/22 11:37
 * @Version: 1.0
 */
@Data
public class StatisticsFollowRateVo {

    private Long patientId;

    private Long deptId;

    private Long roleDeptId;

    private Long recordId;

    private Long userId;

    private String personId;

    private String followName;

    private Long researchId;

    private Long diseaseTypeId;

    private String diseaseName;

    private Long diseaseId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date followSubmitTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date enrollmentTime;


    // 以下参数为计算随访率添加
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date inputStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date inputEndTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date followStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date followEndTime;
    /**
     * 手动入组次数
     */
    private int generatedManually;
}
