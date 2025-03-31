package com.dchealth.cro.research.controller;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.account.form.MessageSearchForm;
import com.dchealth.cro.medical.form.ApplyResearchForm;
import com.dchealth.cro.medical.service.MedicalPatientService;
import com.dchealth.cro.research.domain.ReSearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.exception.base.BaseException;
import com.dchealth.cro.common.exception.base.ServiceException;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.medical.vo.AgreeVO;
import com.dchealth.cro.research.service.ResearchInfoService;

/**
 * 研究项目
 *
 */
@RestController
@RequestMapping(value = "/research")
public class ResearchController extends ApiBaseController {
	private static final Logger L = LoggerFactory.getLogger(ResearchController.class);
	@Autowired
	private ResearchInfoService researchInfoService;



	/**
	 * 用户协议
	 */
	@ResponseBody
	@RequestMapping(value = "patient_agree", method = RequestMethod.POST)
	public String patientAgree(@RequestParam(value = "researchId", required = true) Long  researchId) {
		try {
			
			DchealthCloudResponseEntity<AgreeVO> result = new  DchealthCloudResponseEntity<AgreeVO>();
			AgreeVO  agreeVO = new AgreeVO();
			agreeVO.setAgreeUrl("https://download.giantcro.com/user_agree/td_patient_agree.html");
			agreeVO.setCreateTime(DateUtils.getGBKTime());
			result.setData(agreeVO);
			return buildSuccessMView(result);
		} catch (ServiceException e) {
			return buildErrorMView(CroApiCodeConstant.IMAGE_NOT_EXIST.getCode());
		}catch (Exception e) {
			return buildExceptionMView();
		}
		
	}
	/**
	 * 项目介绍
	 */
	@ResponseBody
	@RequestMapping(value = "research_intro", method = RequestMethod.POST)
	public String researchIntro(@RequestParam(value = "researchId", required = true) Long  researchId) {
		try {
			DchealthCloudResponseEntity<AgreeVO> result = new  DchealthCloudResponseEntity<AgreeVO>();
			AgreeVO  agreeVO = new AgreeVO();
			agreeVO.setAgreeUrl("https://download.giantcro.com/h5/dchealth.html?researchId="+researchId);
			agreeVO.setCreateTime(DateUtils.getGBKTime());
			result.setData(agreeVO);
			return buildSuccessMView(result);
		} catch (Exception e) {
			L.info("查询项目介绍异常{}", researchId, e);
			return buildExceptionMView();
		}
		
	}




}
