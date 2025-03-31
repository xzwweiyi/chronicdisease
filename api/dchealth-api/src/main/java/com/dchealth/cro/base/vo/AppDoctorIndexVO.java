package com.dchealth.cro.base.vo;

import java.util.List;

import com.dchealth.cro.cms.vo.ArticleListVO;

import lombok.Data;

/**
 * 医生端首页
 *
 */
@Data
public class AppDoctorIndexVO {
	private List<AppBannerVO>  banners;
	
	private List<DoctorIndexVO> doctors;
	
	private List<ArticleListVO> articles;
	

}
