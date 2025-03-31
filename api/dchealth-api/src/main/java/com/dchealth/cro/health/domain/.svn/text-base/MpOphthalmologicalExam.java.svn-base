package com.dchealth.cro.health.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 眼科检查对象 mp_ophthalmological_exam
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpOphthalmologicalExam {

    /** 自增主键 */
    private Long id;

    /** 医疗机构id */
    private String careSiteId;

    /** 患者id */
    private String personId;

    /** 就诊id */
    private String visitOccurrenceId;

    private String visitType;

    /** 视力（左） */
    private String visionLeft;

    /** 视力（右） */
    private String visionRight;

    /** 矫正视力左 */
    private String visionLeftRevise;

    /** 矫正视力右 */
    private String visionRightRevise;

    /** 眼位 */
    private String eyePosition;

    /** 眼球运动 */
    private String eyeMovement;

    /** 眼睑 */
    private String eyelid;

    /** 睑结膜 */
    private String eyeConjunctiva;

    /** 球结膜 */
    private String eyeballConjunctiva;

    /** 眼压检查 */
    private String eyePressure;

    /** 眼底照相 */
    private String eyePhoto;

    /** 色觉检查 */
    private String visionColour;

    /** 泪器 */
    private String lacrimal;

    /** 体检日期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date healthExamDate;

    /** 结果日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date resultDate;

    
    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;
}
