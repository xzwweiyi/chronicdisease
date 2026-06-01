package com.dchealth.cro.account.form;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 账户信息
 */
@Data
public class AccountForm {
    /** 主键 */
    @NotNull(message = "用户ID不能为空")
    private String accountId;
    private Long accountIds;

    private String headImage;//头像

    /** 真实姓名 */
    private String realName;

    /** 渠道 */
    private String tenantId;
    private String positionType ="1";//位置1医生端轮播  2 患者端轮播  3小程序
}
