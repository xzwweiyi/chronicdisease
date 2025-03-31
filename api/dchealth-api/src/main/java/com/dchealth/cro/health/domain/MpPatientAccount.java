package com.dchealth.cro.health.domain;

import java.util.Date;

import lombok.Data;

/**
 * 患者用户关联对象 mp_patient_account
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpPatientAccount {
	  /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;

    /** 就诊ID */
    private String personId;

    /** 用户ID */
    private Long accountId;

   
}
