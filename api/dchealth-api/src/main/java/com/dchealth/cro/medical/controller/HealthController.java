package com.dchealth.cro.medical.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.health.form.DetailForm;
import com.dchealth.cro.health.form.HealthListSearchForm;
import com.dchealth.cro.medical.service.HealthService;

@Controller
@RequestMapping(value = "/health")
public class HealthController  extends ApiBaseController{
	
	 private static final Logger L = LoggerFactory.getLogger(HealthController.class);
	  @Autowired
	  private HealthService healthService;
	  
	  
	  /**
	     * 查询患者健康档案和体检报告列表
	     *
	     * @param patientId       患者Id，再患者基础信息表中自动生成的ID
	     * @param type   1：健康档案 2：体检报告
	     * @param pageNo
	     * @param pageSize
	     * @return
	     */
	    @ResponseBody
	    @RequestMapping(value = "/index", method = RequestMethod.POST)
	    public String index( @Validated HealthListSearchForm  healthListSearchForm,BindingResult bind) {
	        try {
	        	if (bind.hasErrors()) {
	    			return buildBindErrorParamMView(bind);
	    		}
	            return buildSuccessMView(healthService.index(healthListSearchForm));
	        	
	        } catch (Exception e) {
	            L.error("查询患者健康档案和体检报告列表异常：{},{}", JSON.toJSONString(healthListSearchForm),e);
	        }
	        return buildErrorParamMView();
	    }
	   
	    
	  /**
     * 查询患者基础信息和诊断 ，转诊信息
     *
     * @param patentId        患者基础信息表中自动生成的ID
     * @param type    1：健康档案 2：体检报告
     * @param checkDate 检查日期
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public String queryPatientInfo(@Validated DetailForm detail,BindingResult bind) {
        try {
        	if (bind.hasErrors()) {
    			return buildBindErrorParamMView(bind);
    		}
            return buildSuccessMView(healthService.detail(detail));
        } catch (Exception e) {
        	 L.error("查询患者基本信息，诊断信息异常：{}", e);
        }
        return buildErrorParamMView();
    }
    
    
    /**
     * 检查项目列表--住院
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkExamItemList", method = RequestMethod.POST)
    public String checkExamItemList(@Validated DetailForm detail,BindingResult bind) {
        try {
        	if (bind.hasErrors()) {
    			return buildBindErrorParamMView(bind);
    		}
            return  buildSuccessMView(healthService.checkExamItemList(detail));
        } catch (Exception e) {
        	 L.info("查询检查项目列表异常：{}", e);
        }
        return buildErrorParamMView();
    }
  
    /**
     * 检验项目列表--住院
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkTestItemList", method = RequestMethod.POST)
    public String queryTestItemList(@Validated DetailForm detail,BindingResult bind) {
        String result;
        try {
        	if (bind.hasErrors()) {
    			return buildBindErrorParamMView(bind);
    		}
            result = buildSuccessMView(healthService.queryCheckTestItemList(detail));
        } catch (Exception e) {
        	 L.info("查询检验项目列表异常：{}", e);
            result = buildExceptionMView();
        }
        return result;
    }

	/**
	 * 检验项目列表---门诊
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/mzTestItemList", method = RequestMethod.POST)
	public String mzTestItemList(DetailForm detail) {
		String result;
		try {
			result = buildSuccessMView(healthService.mzTestItemList(detail));
		} catch (Exception e) {
			L.info("查询门诊检验项目列表异常：{}", e);
			result = buildExceptionMView();
		}
		return result;
	}

	/**
	 * 检查项目列表--门诊
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/mzExamItemList", method = RequestMethod.POST)
	public String mzExamItemList(DetailForm detail) {
		try {
			return  buildSuccessMView(healthService.mzExamItemList(detail));
		} catch (Exception e) {
			L.info("查询门诊检查项目列表异常：{}", e);
		}
		return buildErrorParamMView();
	}

	/**
	 * 门诊诊断
	 *
	 * @param detail
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryMzDiagnosis", method = RequestMethod.POST)
	public String queryMzDiagnosis(DetailForm detail) {
		try {
			return buildSuccessMView(healthService.queryMzDiagnosis(detail));
		} catch (Exception e) {
			L.error("查询患者基本信息，诊断信息异常：{}", e);
		}
		return buildErrorParamMView();
	}
}
