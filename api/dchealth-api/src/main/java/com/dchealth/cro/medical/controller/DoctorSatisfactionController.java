package com.dchealth.cro.medical.controller;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.common.base.ApiBaseController;
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
			return buildSuccessMView(satisfactionService.list(satisfactionForm));
		} catch (Exception e) {
			L.info("综合查询资讯信息异常{}", JSON.toJSONString(satisfactionForm), e);
			return buildExceptionMView();
		}
	}

	
}
