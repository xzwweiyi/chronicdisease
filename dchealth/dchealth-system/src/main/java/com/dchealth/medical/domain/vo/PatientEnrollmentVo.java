package com.dchealth.medical.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class PatientEnrollmentVo {
    /**
     * 患者ID
     */
    private Long patientId;//patient_info表id

    private String tdPatientId;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 性别
     */
    private String sex;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    private Long planResearchId;//计划入组队列
    private Long researchId;//未入组队列

    private String registrySigns;//就诊标识

    private Long accountId;
    private Integer deptId;


    private String personId;
    private String baseMobile;

    /**
     * 1未入组 3已入组
     */
    private String enrollmentHandlerStatus;
    /**
     * 0未入组 1已入组
     */
    private String enrollmentStatus;
    /**
     * 入组情况描述
     */
    private String enrollmentResultContent;

    /**
     * 0未绑定 1已绑定
     */
    private String bindStatus;

    /*
     * 随访提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date followSubmitTime;

    private Integer visitType;

    /**
     * 就诊id
     */
    private Long visitOccurrenceId;

}
