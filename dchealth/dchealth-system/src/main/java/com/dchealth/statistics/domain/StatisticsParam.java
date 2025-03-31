package com.dchealth.statistics.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class StatisticsParam {

    /**
     * 科室id
     */
    private Long deptId;
    /**
     * 病种id
     */
    private Long diseaseId;
    /**
     * 队列id
     */
    private Long researchId;

    /**
     * 时间区域 0（近7天）、1（近30天）、2（近365天）、3（自定义）
     */
    private int timeRegion;

    /**
     * 自定义开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 自定义结束
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 当前登录角色的中心
     */
    private Long roleDeptId;

    private String enrollmentHandlerStatus;
    private String enrollmentStatus;

    private Long userId;
}
