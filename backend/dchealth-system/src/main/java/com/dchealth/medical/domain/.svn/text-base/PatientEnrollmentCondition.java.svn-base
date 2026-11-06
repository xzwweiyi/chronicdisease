package com.dchealth.medical.domain;

import com.dchealth.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class PatientEnrollmentCondition extends BaseEntity {
    /*
     *
     */
    private Long id;

    /**
     * 队列id
     */
    private Long researchId;

    /*
     * 纳排时间类型 默认、门诊时间、入院时间 、出院时间
     */
    private String enrollmentTimeType;

    /*
     * 纳排开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date enrollmentTimeStart;

    /*
     * 纳排结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date enrollmentTimeEnd;

    /*
     * 科室id列表
     */
    private String deptIdArr;

    /*
     * 检索条件类型 包含 不包含
     */
    private String deptType;

    /*
     * 性别 男 女 不限
     */
    private String sex;

    /*
     * 性别检索条件类型 包含 不包含
     */
    private String sexType;

    /*
     * 年龄检索条件类型 区间 等于 大于 大于等于 小于 小于等于
     */
    private String ageType;

    /*
     * 年龄开始
     */
    private Integer ageStart;

    /*
     * 年龄结束
     */
    private Integer ageEnd;

    /*
     * 年龄检索条件类型 区间 等于 大于 大于等于 小于 小于等于
     */
    private String clinicDateType;

    /*
     * 门诊开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date clinicDateStart;

    /*
     * 门诊结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date clinicDateEnd;

    /*
     * 诊断条件类型 包含 不包含
     */
    private String diagnosisType;

    /*
     * 诊断内容
     */
    private String diagnosis;

    @DateTimeFormat(pattern = "hh:mm:ss.SSSZ", iso = DateTimeFormat.ISO.TIME)
    private Time importTime;

    private String importTimes;

    /**
     * 分页查询-每页数量
     */
    private Integer pageSize;

    /**
     * 查询页数
     */
    private Integer pageNum;
    /**
     * 动态条件
     */
    private String conditions;
    private List<Map<String,String>> paramList;
}
