package com.dchealth.compatibility.vo;

import lombok.Data;

/**
 * 眼科检查表(TdOphthalmologicalExam)实体类
 *
 */
@Data
public class OphthalmologicalExamVO  {
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
    private String healthExamDate;

    /** 结果日期 */
    private String resultDate;
}

