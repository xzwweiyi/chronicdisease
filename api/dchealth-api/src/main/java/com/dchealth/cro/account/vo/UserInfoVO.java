package com.dchealth.cro.account.vo;

import java.io.Serializable;
import java.util.List;

import com.dchealth.cro.common.annoation.Desensitize;
import com.dchealth.cro.common.constant.DesensitizeType;
import com.dchealth.cro.common.serializers.DesensitizeSerializer;
import com.dchealth.cro.core.vo.DictVO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
public class UserInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long accountId;// 主键
	@Desensitize(type = DesensitizeType.PHONE)
	@JsonSerialize(using = DesensitizeSerializer.class)
	private String mobile;// 手机号或邮箱
	private String email;// 邮箱
	private String sex;// 性别0 男 1 女 2未知
	private List<DictVO> sexDict;
	private String nickName;// 昵称
	@Desensitize(type = DesensitizeType.ID_CARD)
	@JsonSerialize(using = DesensitizeSerializer.class)
	private String idCardNo;// 身份证号

	private Long deptId;//部门id

	private String userType;

	/** 账号 */
	private String userName;
	/** 用户头像 */
	private String userHead;

	private String privateKey;

	private String status;//账号状态 0正常  1停用

	private String title;//职称
	private List<DictVO> titleDict;
	private String hospitalName;//
	private Integer hospitalId;
	private String workYear;// 行业时间
	private String calWorkYear;//计算年数
	private String expert;// 专长
	private String personalIntro;// 个人介绍
	private String figureImage;// 形象照
	private List<String> honorImages;// 荣誉展示
	private String education;// 学历
	private List<DictVO> educationDict;
	private String authStatus;//审核状态 1 未提交 2 待审核 3 审核通过  4 未通过
    private String authContext;//审核内容
	private String birthday;//生日
	private Long provinceId;//省
	private String provinceName;
	private Long cityId;//市
	private String cityName;
	private Long areaId;//县
	private String areaName;
	private String detailAddress;//详细地址
	private String deptName;//部门

}
