package com.dchealth.cro.cms.form;

import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class ActicleListSearchForm {
	  private String title;//标题
	  private Integer pageNo =1;//页码
	  private Integer pageSize =10;//每页记录数
	  private Long categoryId;//分类
	  @Pattern(regexp = "[123]", message = "类型有误")
	  private String type;//1:首页轮播  2 首页列表 3 综合检索
	  private String tenantId;//租户ID
	  private String positionType ="1";//  显示位置类型 1 医生端 2 患者端 3小程序
	  
	
	  
	  
	  

}
