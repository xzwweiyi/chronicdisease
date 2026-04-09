package com.dchealth.cro.common.base;

import java.util.List;
import java.util.Map;

import com.dchealth.cro.common.exception.base.ServiceException;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.common.constant.CroApiCodeConstant;

/**
 * 控制器支持类
 *
 */
public class ApiBaseController {
	
	/**
	 * 构造成功json视图
	 *
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> String buildSuccessMView(T obj) {
		DchealthCloudResponseEntity<T> result = null ;
		if(obj instanceof DchealthCloudResponseEntity) {
			result  =(DchealthCloudResponseEntity<T>)  obj;
			if( result.getCode()!=CroApiCodeConstant.SUCCESS.getCode() ) {
				result.setMsg(CroApiCodeConstant.getMsg(result.getCode()));
			}
			return JSON.toJSONString(result);
		}
		result = new DchealthCloudResponseEntity<T>();
		result.setData(obj);
		return JSON.toJSONString(result);
	}

	/**
	 * 构造成功json视图
	 *
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> String buildSuccessMessageView(T obj) {
		DchealthCloudResponseEntity<T> result = null ;
		if(obj instanceof DchealthCloudResponseEntity) {
			return JSON.toJSONString(obj);
		}
		result = new DchealthCloudResponseEntity<T>();
		result.setData(obj);
		return JSON.toJSONString(result);
	}

	
	
	/**
	 * 构造成功json视图
	 *
	 * @param
	 * @return
	 */
	
	public  <T> String  buildErrorParamMView() {
		DchealthCloudResponseEntity<T> result = new DchealthCloudResponseEntity<T>();
		result.setMsg(CroApiCodeConstant.ERROR_PARAM.getMsg());	
		result.setCode(CroApiCodeConstant.ERROR_PARAM.getCode());	
		return JSON.toJSONString(result);
	}

	public  <T> String  buildErrorParamMView(Exception e) {
		DchealthCloudResponseEntity<T> result = new DchealthCloudResponseEntity<T>();
		if(e instanceof ServiceException) {
			ServiceException e1 = (ServiceException) e;
			result.setMsg(e1.getMsg());
			result.setCode(e1.getCode());
		}else {
			result.setMsg(CroApiCodeConstant.SYSTEM_ERROR.getMsg());
			result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());
		}
		return JSON.toJSONString(result);
	}

	/**
	 * 构造成功json视图
	 *
	 * @param
	 * @return
	 */
	
	public  <T> String  buildErrorMView(int code) {
		DchealthCloudResponseEntity<T> result = new DchealthCloudResponseEntity<T>();
		result.setMsg(CroApiCodeConstant.getMsg(code));	
		result.setCode(code);	
		return JSON.toJSONString(result);
	}
	
	/**
	 * 构造成功json视图
	 *
	 * @param
	 * @return
	 */
	
	public  <T> String  buildExceptionMView() {
		DchealthCloudResponseEntity<T> result = new DchealthCloudResponseEntity<T>();
		result.setMsg(CroApiCodeConstant.SYSTEM_ERROR.getMsg());	
		result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());	
		return JSON.toJSONString(result);
	}
	/**
	 *  这里是从文件取还是从数据库取？
	 * @param bind
	 * @param langCode
	 * @return
	 */
	public String  buildBindErrorParamMView(BindingResult bind) {
		List<ObjectError> list = bind.getAllErrors();
		for (ObjectError error : list) {
			DchealthCloudResponseEntity<?> result = new DchealthCloudResponseEntity<>();
			result.setMsg(error.getDefaultMessage());	
		    result.setCode(CroApiCodeConstant.ERROR_PARAM.getCode());	
			return JSON.toJSONString(result);
		}
		return null;
	}


	/**
	 * 必须参数验证
	 *
	 * @param params
	 */
	@SuppressWarnings("rawtypes")
	public boolean notEmpty(Object... params) {
		if (ArrayUtils.isNotEmpty(params)) {
			for (Object param : params) {
				if (param == null) {
					return false;
				}
				if (param instanceof List && ((List) param).isEmpty()) {
					return false;
				}
				if (param instanceof Map && ((Map) param).isEmpty()) {
					return false;
				}
				if (param instanceof String && ((String) param).length() == 0) {
					return false;
				}

			}
			return true;
		}
		return false;
	}



	

}
