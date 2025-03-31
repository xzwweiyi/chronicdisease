package com.dchealth.cro.health.vo;

import lombok.Data;

/**
 * 耳鼻喉相关表(Otolaryngology)实体类
 *
 */
@Data
public class OtolaryngologyVO{
  

    /** 听力 */
    private String hearing;

    /** 耳廓 */
    private String auricle;

    /** 外耳道 */
    private String earExternalMeatus;

    /** 鼓膜 */
    private String earDrum;

    /** 外鼻 */
    private String noseExternal;

    /** 鼻腔 */
    private String noseCavity;

    /** 口咽 */
    private String oropharynx;

    /** 喉咽 */
    private String throat;

    /** 扁桃体 */
    private String tonsil;

    /** 体检日期时间 */
    private String healthExamDate;

    /** 结果日期 */
    private String resultDate;
}

