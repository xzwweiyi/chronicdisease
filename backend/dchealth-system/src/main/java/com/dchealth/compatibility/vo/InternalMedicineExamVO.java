package com.dchealth.compatibility.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 内科检查对象 mp_internal_medicine_exam
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class InternalMedicineExamVO
{


    /** 胸部外形 */
    private String chestShape;

    /** 胸部触诊 */
    private String chestPalpation;

    /** 胸部叩诊 */
    private String chestPercussion;

    /** 心界 */
    private String heartBorder;

    /** 肺部听诊 */
    private String lungAuscultation;

    /** 心音 */
    private String heartSound;

    /** 心律 */
    private String heartRhythm;

    /** 心脏杂音 */
    private String heartNoise;

    /** 肝脏触诊 */
    private String liverPalpation;

    /** 脾脏触诊 */
    private String spleenPalpation;

    /** 腹部外形 */
    private String abdomShape;

    /** 腹部压痛 */
    private String abdomPressurePain;

    /** 腹部叩诊 */
    private String abdomPercussion;

    /** 体检日期时间 */
    private String healthExamDate;

    /** 结果日期 */
    private String resultDate;

}
