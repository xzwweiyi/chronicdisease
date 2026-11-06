package com.dchealth.system.domain;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * 患者账号表对象 patient_account
 *
 * @author lchbu
 * @date 2022-09-14
 */
@Data
public class PatientAccount extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 患者账号id
     */
    @Excel(name = "id")
    private Long accountId;

    /**
     * 患者基础信息id
     */
    private Long id;

    /**
     * 注册手机号
     */
    @Excel(name = "手机号")
    private String mobile;

    /**
     * 0 正常 1 停用
     */
    @Excel(name = "用户状态 0 正常 1 停用 ")
    private String status;

    /**
     * 就诊标识
     */
   // @Excel(name = "就诊标识")
    private String registrySigns;

    /**
     * 身份证号
     */
  //  @Excel(name = "身份证号")
    private String idCard;

    @Excel(name = "姓名")
    private String name;

    @Excel(name = "性别(0:未知，1：男，2：女)")
    private Integer sex;

    /**
     * 绑定状态0未绑定1已绑定
     */
    //@Excel(name = "绑定状态0未绑定1已绑定")
    private String bindStatus;

    /**
     * 公众号 openId
     */
    private String openId;

    //科室id
    private Long[] deptIds;
}
