package com.dchealth.cro.medical.controller;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.utils.RsaUtil;
import com.dchealth.cro.medical.form.SatisfactionForm;
import com.dchealth.cro.medical.service.SatisfactionService;
import com.dchealth.cro.medical.vo.DoctorSatisfactionEvaluationVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 医生满意度
 */
@Controller
@RequestMapping(value = "/satisfaction")
public class DoctorSatisfactionController extends ApiBaseController {
	private static final Logger L = LoggerFactory.getLogger(DoctorSatisfactionController.class);
	@Autowired
	private SatisfactionService satisfactionService;
	@Autowired
	private RsaUtil rsaUtil;
	/**
	 * 新增医生满意度
	 */
	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public String list(@Validated DoctorSatisfactionEvaluationVo doctorSatisfactionEvaluationVo, BindingResult bind) {
		try {
			if (bind.hasErrors()) {
				return buildBindErrorParamMView(bind);
			}
			if(doctorSatisfactionEvaluationVo.getAccountId() !=null){
				doctorSatisfactionEvaluationVo.setAccountId(Long.valueOf(rsaUtil.decryptValue(doctorSatisfactionEvaluationVo.getAccountId().toString())));
			}
			return buildSuccessMView(satisfactionService.add(doctorSatisfactionEvaluationVo));
		} catch (Exception e) {
			L.info("新增医生满意度新增异常{}", JSON.toJSONString(doctorSatisfactionEvaluationVo), e);
			return buildExceptionMView();
		}
	}


	/**
	 * 查询医生满意度
	 */
	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public String list(@Validated SatisfactionForm satisfactionForm, BindingResult bind) {
		try {
			if (bind.hasErrors()) {
				return buildBindErrorParamMView(bind);
			}
			if(satisfactionForm.getAccountId() !=null){
				satisfactionForm.setAccountId(Long.valueOf(rsaUtil.decryptValue(satisfactionForm.getAccountId().toString())));
			}
			return buildSuccessMView(satisfactionService.list(satisfactionForm));
		} catch (Exception e) {
			L.info("综合查询资讯信息异常{}", JSON.toJSONString(satisfactionForm), e);
			return buildExceptionMView();
		}
	}

	
}
