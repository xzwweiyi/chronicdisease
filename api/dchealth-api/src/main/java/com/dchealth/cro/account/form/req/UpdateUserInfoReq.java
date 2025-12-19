package com.dchealth.cro.account.form.req;


import lombok.Data;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/12/4 17:55
 * @Version: 1.0
 */
@Data
public class UpdateUserInfoReq {

    /**
     * 账号ID
     */
    private Long accountId;

    /**
     * 患者i d
     */
    private String personId;

    /**
     * 登录手机
     */
    private String loginMobile;

    /**
     * 身份证号
     */
    private String idCard;
}
