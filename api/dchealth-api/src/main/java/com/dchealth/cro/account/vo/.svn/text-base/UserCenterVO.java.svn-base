package com.dchealth.cro.account.vo;

import java.io.Serializable;
import java.util.List;

import com.dchealth.cro.core.vo.DictVO;

import lombok.Data;

@Data
public class UserCenterVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long accountId;// 主键
	private String mobile;// 手机号或邮箱
	private String email;// 邮箱
	private String sex;// 性别0 男 1 女 2未知
	private List<DictVO> sexDict;
	private String nickName;// 昵称
	private String accountType;// 用户类型 1 一级专家(真专家) 2 二级专家 3 普通医生 10 患者

	/** 账号 */
	private String userName;
	private String companyName;
	/** 用户头像 */
	private String userHead;


	private String realName;
	private String title;
	private List<DictVO> titleDict;
	private Integer departId;
	private List<DictVO> departDict;
	private String hospitalName;//
	private Integer hospitalId;
	private String workYear;// 行业时间
	private String calWorkYear;//计算年数
	private String expert;// 专长
	private String personalIntro;// 个人介绍
	private String education;// 学历
	private List<DictVO> educationDict;
	private String consulationNum;//会诊数
    private String consOpenStatus;
	private String authStatus;//审核状态 1 未提交 2 待审核 3 审核通过  4 未通过
    private String consFee;
    
    /** 积分余额 */
    private String scoreBalance;

    /** 积分总收入 */
    private String scoreTotalIncome;

    /** 积分总消费 */
    private String scoreTotalCost;

    /** 人民币余额 */
    private String walletBalance;

    /** 人民币总收入 */
    private String walletTotalIncome;

    /** 人民币消费总额 */
    private String walletTotalCost;


}
