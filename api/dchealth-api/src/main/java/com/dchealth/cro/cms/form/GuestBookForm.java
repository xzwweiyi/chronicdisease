package com.dchealth.cro.cms.form;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class GuestBookForm {
	@Pattern(regexp = "1[3|4|5|6|7|8|9][0-9]\\d{8}")
	@NotNull(message = "手机号不能为空")
	private String mobile;// 手机号
	private String userName;// 姓名
	@NotBlank(message = "意见不能为空")
	private String content;// 内容
	@NotBlank(message = "用户ID不能为空")
	private String accountId;// 用户ID
	private Long accountIds;// 用户ID
	private String tenantId;// 租户ID
	private List<String> list;//文件地址
	private String imageListJson;//小程序上传
}
