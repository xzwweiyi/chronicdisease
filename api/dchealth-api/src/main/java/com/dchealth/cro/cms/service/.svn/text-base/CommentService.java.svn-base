package com.dchealth.cro.cms.service;

import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dchealth.cro.cms.domain.CmsArticle;
import com.dchealth.cro.cms.domain.CmsComment;
import com.dchealth.cro.cms.domain.CmsCommentBlank;
import com.dchealth.cro.cms.domain.CmsCommentTip;
import com.dchealth.cro.cms.domain.CmsStat;
import com.dchealth.cro.cms.form.CmsCommentBlankForm;
import com.dchealth.cro.cms.form.CmsCommentTipForm;
import com.dchealth.cro.cms.form.CommentSaveForm;
import com.dchealth.cro.cms.mapper.CmsArticleMapper;
import com.dchealth.cro.cms.mapper.CmsCommentBlankMapper;
import com.dchealth.cro.cms.mapper.CmsCommentMapper;
import com.dchealth.cro.cms.mapper.CmsCommentTipMapper;
import com.dchealth.cro.cms.mapper.CmsStatMapper;
import com.dchealth.cro.cms.utils.WordFilter;
import com.dchealth.cro.common.base.CommonResult;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.Constants;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.common.utils.StringUtils;

@Service
public class CommentService {
	@Autowired
	private CmsCommentMapper cmsCommentMapper;
	@Autowired
	private CmsStatMapper cmsStatMapper;
	@Autowired
	private CmsArticleMapper cmsArticleMapper;
	@Autowired
	private CmsCommentTipMapper cmsCommentTipMapper;
	@Autowired
	private CmsCommentBlankMapper cmsCommentBlankMapper;

	@Transactional
	public DchealthCloudResponseEntity<CommonResult> save(CommentSaveForm commentSaveForm,String positionType) {
		DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
		CmsArticle cmsArticle = cmsArticleMapper.selectCmsArticleById(commentSaveForm.getArticleId());
		if (commentSaveForm.getAccountId() == null || commentSaveForm.getAccountId() <1 ){
			result.setCode(CroApiCodeConstant.USER_NOT_LOGIN.getCode());
			return result;
		}
		if (cmsArticle == null) {
			result.setCode(CroApiCodeConstant.ARTICLE_NOT_EXIST.getCode());
			return result;
		}
		
		if (Constants.NO.equals(cmsArticle.getAllowComment())) {
			result.setCode(CroApiCodeConstant.ARTICLE_NOT_ALLOWCOMMENT.getCode());
			return result;
		}
		CmsStat cmsStat = cmsStatMapper.selectCmsStatById(commentSaveForm.getArticleId());
		if (cmsStat == null) {
			result.setCode(CroApiCodeConstant.ARTICLE_NOT_EXIST.getCode());
			return result;
		}
		   WordFilter filter = new WordFilter("/sensitivewords/sms.dic");
		   Set<String> judge = filter.judge(commentSaveForm.getContent());
		   if(judge != null ) {
		        for (String word : judge) {
		        	if(StringUtils.isNotNullOrEmpty(word)) {
			        	result.setCode(CroApiCodeConstant.LLLEGAL_CONTENT.getCode());
						return result;
					}
		        }
	        }
		CmsComment cmsComment = new CmsComment();
		cmsComment.setAccountId(commentSaveForm.getAccountId());
		cmsComment.setArticleId(commentSaveForm.getArticleId());
		cmsComment.setContent(commentSaveForm.getContent());
		cmsComment.setCreateTime(DateUtils.getNowDate());
		cmsComment.setDelFlag(Constants.NO);
		cmsComment.setTenantId(commentSaveForm.getTenantId());
		int rs = cmsCommentMapper.insertCmsComment(cmsComment);
		CommonResult commonResult = new CommonResult();
		if (rs == 1) {
			int commentNum = cmsStat.getCommentNum() + 1;
			cmsStat.setCommentNum(commentNum);
			cmsStatMapper.updateCmsStat(cmsStat);
			commonResult.setResult(true);
		} else {
			commonResult.setResult(false);
		}
		result.setData(commonResult);
		return result;
	}

	public DchealthCloudResponseEntity<CommonResult> saveTip(CmsCommentTipForm cmsCommentTipForm) {
		DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
		if (cmsCommentTipForm.getAccountId() == null || cmsCommentTipForm.getAccountId() <1 ){
			result.setCode(CroApiCodeConstant.USER_NOT_LOGIN.getCode());
			return result;
		}
		CmsArticle cmsArticle = cmsArticleMapper.selectCmsArticleById(cmsCommentTipForm.getArticleId());
		if (cmsArticle == null) {
			result.setCode(CroApiCodeConstant.ARTICLE_NOT_EXIST.getCode());
			return result;
		}
		CmsComment cmsComment = cmsCommentMapper.selectCmsCommentById(cmsCommentTipForm.getCommentId());
		if(cmsComment == null) {
			result.setCode(CroApiCodeConstant.COMMENT_NOT_EXIST.getCode());
			return result;
		}
		CmsCommentTip  cmsCommentTip = new CmsCommentTip();
		BeanUtils.copyProperties(cmsCommentTipForm, cmsCommentTip);
		cmsCommentTip.setAuthStatus("1");
		cmsCommentTip.setCreateTime(DateUtils.getNowDate());
		int rs = cmsCommentTipMapper.insertCmsCommentTip(cmsCommentTip);
		CommonResult commonResult = new CommonResult();
		if(rs == 1) {
			commonResult.setResult(true);
		} else {
			commonResult.setResult(false);
		}
		result.setData(commonResult);
		return result;
	}

	public DchealthCloudResponseEntity<CommonResult> saveBlank(CmsCommentBlankForm cmsCommentBlankForm) {
		DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
		if (cmsCommentBlankForm.getAccountId() == null || cmsCommentBlankForm.getAccountId() <1 ){
			result.setCode(CroApiCodeConstant.USER_NOT_LOGIN.getCode());
			return result;
		}
		CmsArticle cmsArticle = cmsArticleMapper.selectCmsArticleById(cmsCommentBlankForm.getArticleId());
		if (cmsArticle == null) {
			result.setCode(CroApiCodeConstant.ARTICLE_NOT_EXIST.getCode());
			return result;
		}
		CmsComment cmsComment = cmsCommentMapper.selectCmsCommentById(cmsCommentBlankForm.getCommentId());
		if(cmsComment == null) {
			result.setCode(CroApiCodeConstant.COMMENT_NOT_EXIST.getCode());
			return result;
		}
		CmsCommentBlank  cmsCommentBlank = new CmsCommentBlank();
		BeanUtils.copyProperties(cmsCommentBlankForm, cmsCommentBlank);
		cmsCommentBlank.setCreateTime(DateUtils.getNowDate());
		int rs = cmsCommentBlankMapper.insertCmsCommentBlank(cmsCommentBlank);
		CommonResult commonResult = new CommonResult();
		if(rs == 1) {
			commonResult.setResult(true);
		} else {
			commonResult.setResult(false);
		}
		result.setData(commonResult);
		return result;
	}

}
