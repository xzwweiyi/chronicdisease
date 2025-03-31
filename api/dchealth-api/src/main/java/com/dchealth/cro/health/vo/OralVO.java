package com.dchealth.cro.health.vo;

import lombok.Data;

/**
 * 口腔科相关表(TdOral)实体类
 *
 */
@Data
public class OralVO  {
    /** 牙体组织疾病 */
    private String dentalTissue;

    /** 牙周组织疾病 */
    private String dentalPeriTissue;

    /** 口腔黏膜疾病 */
    private String oralMucosa;

    /** 口腔颌面外科疾病 */
    private String oralMaxillofacial;

    /** 口腔修复科 */
    private String prosthodontics;

    /** 正畸科 */
    private String orthodontics;

    /** 体检日期时间 */
    private String healthExamDate;

    /** 结果日期 */
    private String resultDate;

}

