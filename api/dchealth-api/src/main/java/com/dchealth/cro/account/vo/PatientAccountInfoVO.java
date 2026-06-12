package com.dchealth.cro.account.vo;

import com.dchealth.cro.common.annoation.Desensitize;
import com.dchealth.cro.common.constant.DesensitizeType;
import com.dchealth.cro.common.serializers.DesensitizeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    @Desensitize(type = DesensitizeType.ID_CARD)
    @JsonSerialize(using = DesensitizeSerializer.class)
    private String idCard;

    private String idCardOrg;
    /**
         * 入组处理状态1待处理3已完成
     */
    private String enrollmentHandlerStatus;

    /** 入组状态：0待分配1已入组  */
    private String enrollmentStatus;
    /**
     * 随访手机号
     */
    @Desensitize(type = DesensitizeType.PHONE)
    @JsonSerialize(using = DesensitizeSerializer.class)
    private String mobile;

    private String mobileOrg;
    /**
     * 登录手机号
     */
    @Desensitize(type = DesensitizeType.PHONE)
    @JsonSerialize(using = DesensitizeSerializer.class)
    private String loginMobile;

    private String loginMobileOrg;

}
