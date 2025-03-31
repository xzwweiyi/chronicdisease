package com.dchealth.cro.account.vo;

import lombok.Data;

/**
 * 患者账号信息接口
 */
@Data
public class PatientAccountInfoVO {

    private Long accountId;

    /** 患者姓名 */
    private String personName;

    /**
     * 患者基础信息表id
     */
    private String personId;

    /**
     * 私钥
     */
    private String privateKey;

    /** 绑定状态0未绑定1已绑定 */
    private String bindStatus;

    private Integer sex;
    private String idCard;

    /**
         * 入组处理状态1待处理3已完成
     */
    private String enrollmentHandlerStatus;

    /** 入组状态：0待分配1已入组  */
    private String enrollmentStatus;
    /**
     * 手机号
     */
    private String mobile;
}
