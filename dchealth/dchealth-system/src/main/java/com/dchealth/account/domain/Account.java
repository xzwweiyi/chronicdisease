package com.dchealth.account.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 account
 * 
 * @author ruoyi
 * @date 2022-03-28
 */
public class Account extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 昵称 */
    @Excel(name = "昵称")
    private String nickName;

    /** 头像地址 */
    @Excel(name = "头像地址")
    private String headImage;

    /** 账号编码 */
    @Excel(name = "账号编码")
    private String accountNo;

    /** 账号 */
    @Excel(name = "账号")
    private String accountName;

    /** 真实姓名 */
    @Excel(name = "真实姓名")
    private String realName;

    /** 拼音简写 */
    @Excel(name = "拼音简写")
    private String letterName;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idCardNo;

    /** 注册手机号 */
    @Excel(name = "注册手机号")
    private String mobile;

    /** 注册邮箱 */
    @Excel(name = "注册邮箱")
    private String email;

    /** 性别0男 1女 2未知 */
    @Excel(name = "性别0男 1女 2未知")
    private String sex;

    /** 登录密码 */
    @Excel(name = "登录密码")
    private String loginPasswd;

    /** 支付密码 */
    @Excel(name = "支付密码")
    private String payPasswd;

    /** 0 正常 1 停用  */
    @Excel(name = "0 正常 1 停用 ")
    private String status;

    /** 盐值 */
    @Excel(name = "盐值")
    private String saltVal;

    /** 加密码 */
    @Excel(name = "加密码")
    private String userUniqueCode;

    /** 用户类型 1 管理员  4 医生 */
    @Excel(name = "用户类型 1 管理员  4 医生")
    private String accountType;

    /**  小程序显示类型 1 问诊 2 专科列表 */
    @Excel(name = " 小程序显示类型 1 问诊 2 专科列表")
    private String showType;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String tenantId;

    /** 微信OPENID */
    @Excel(name = "微信OPENID")
    private String openId;

    /** 账号来源1医生端  2 患者端  3小程序 */
    @Excel(name = "账号来源1医生端  2 患者端  3小程序")
    private String dataSources;

    /** 位置1医生端轮播  2 患者端轮播  3小程序 */
    @Excel(name = "位置1医生端轮播  2 患者端轮播  3小程序")
    private String positionType;

    /** 籍贯 */
    @Excel(name = "籍贯")
    private String nativePlace;

    /** 微信用户标识 */
    @Excel(name = "微信用户标识")
    private String unionid;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setNickName(String nickName) 
    {
        this.nickName = nickName;
    }

    public String getNickName() 
    {
        return nickName;
    }
    public void setHeadImage(String headImage) 
    {
        this.headImage = headImage;
    }

    public String getHeadImage() 
    {
        return headImage;
    }
    public void setAccountNo(String accountNo) 
    {
        this.accountNo = accountNo;
    }

    public String getAccountNo() 
    {
        return accountNo;
    }
    public void setAccountName(String accountName) 
    {
        this.accountName = accountName;
    }

    public String getAccountName() 
    {
        return accountName;
    }
    public void setRealName(String realName) 
    {
        this.realName = realName;
    }

    public String getRealName() 
    {
        return realName;
    }
    public void setLetterName(String letterName) 
    {
        this.letterName = letterName;
    }

    public String getLetterName() 
    {
        return letterName;
    }
    public void setIdCardNo(String idCardNo) 
    {
        this.idCardNo = idCardNo;
    }

    public String getIdCardNo() 
    {
        return idCardNo;
    }
    public void setMobile(String mobile) 
    {
        this.mobile = mobile;
    }

    public String getMobile() 
    {
        return mobile;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }
    public void setSex(String sex) 
    {
        this.sex = sex;
    }

    public String getSex() 
    {
        return sex;
    }
    public void setLoginPasswd(String loginPasswd) 
    {
        this.loginPasswd = loginPasswd;
    }

    public String getLoginPasswd() 
    {
        return loginPasswd;
    }
    public void setPayPasswd(String payPasswd) 
    {
        this.payPasswd = payPasswd;
    }

    public String getPayPasswd() 
    {
        return payPasswd;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setSaltVal(String saltVal) 
    {
        this.saltVal = saltVal;
    }

    public String getSaltVal() 
    {
        return saltVal;
    }
    public void setUserUniqueCode(String userUniqueCode) 
    {
        this.userUniqueCode = userUniqueCode;
    }

    public String getUserUniqueCode() 
    {
        return userUniqueCode;
    }
    public void setAccountType(String accountType) 
    {
        this.accountType = accountType;
    }

    public String getAccountType() 
    {
        return accountType;
    }
    public void setShowType(String showType) 
    {
        this.showType = showType;
    }

    public String getShowType() 
    {
        return showType;
    }
    public void setTenantId(String tenantId) 
    {
        this.tenantId = tenantId;
    }

    public String getTenantId() 
    {
        return tenantId;
    }
    public void setOpenId(String openId) 
    {
        this.openId = openId;
    }

    public String getOpenId() 
    {
        return openId;
    }
    public void setDataSources(String dataSources) 
    {
        this.dataSources = dataSources;
    }

    public String getDataSources() 
    {
        return dataSources;
    }
    public void setPositionType(String positionType) 
    {
        this.positionType = positionType;
    }

    public String getPositionType() 
    {
        return positionType;
    }
    public void setNativePlace(String nativePlace) 
    {
        this.nativePlace = nativePlace;
    }

    public String getNativePlace() 
    {
        return nativePlace;
    }
    public void setUnionid(String unionid) 
    {
        this.unionid = unionid;
    }

    public String getUnionid() 
    {
        return unionid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("nickName", getNickName())
            .append("headImage", getHeadImage())
            .append("accountNo", getAccountNo())
            .append("accountName", getAccountName())
            .append("realName", getRealName())
            .append("letterName", getLetterName())
            .append("idCardNo", getIdCardNo())
            .append("mobile", getMobile())
            .append("email", getEmail())
            .append("sex", getSex())
            .append("loginPasswd", getLoginPasswd())
            .append("payPasswd", getPayPasswd())
            .append("status", getStatus())
            .append("saltVal", getSaltVal())
            .append("userUniqueCode", getUserUniqueCode())
            .append("accountType", getAccountType())
            .append("showType", getShowType())
            .append("tenantId", getTenantId())
            .append("openId", getOpenId())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("dataSources", getDataSources())
            .append("positionType", getPositionType())
            .append("nativePlace", getNativePlace())
            .append("unionid", getUnionid())
            .toString();
    }
}
