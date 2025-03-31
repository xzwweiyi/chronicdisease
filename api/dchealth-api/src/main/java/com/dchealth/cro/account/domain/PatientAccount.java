package com.dchealth.cro.account.domain;

import lombok.Data;

import java.util.Date;

/**
 * 患者账号表对象 patient_account
 * 
 * @author lchbu
 * @date 2022-09-13
 */
@Data
public class PatientAccount
{

    /** 患者账号id */
    private Long accountId;

    /** 注册手机号 */
    private String mobile;

    /** 0 正常 1 停用  */
    private String status;

    /**
     * 注册时间
     */
    private Date createTime;

    /** 就诊标识 */
    private String registrySigns;

    /** 身份证号 */
    private String idCard;

    /** 绑定状态0未绑定1已绑定 */
    private String bindStatus;
    
    private String openId;
}
