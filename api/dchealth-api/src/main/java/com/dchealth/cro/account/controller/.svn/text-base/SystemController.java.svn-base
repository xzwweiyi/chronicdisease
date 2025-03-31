package com.dchealth.cro.account.controller;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dchealth.cro.account.service.SystemService;
import com.dchealth.cro.common.base.ApiBaseController;

/**
 *
 */
@Controller
@RequestMapping(value = "/system")
public class SystemController extends ApiBaseController {
	private static final Logger L = LoggerFactory.getLogger(SystemService.class);
	@Autowired
	private SystemService systemService;

	/**
	 * 查询最新APP版本信息
	 * type  安卓 苹果  类型 1Android 2:IOS
	 * appType 医生端 患者端
	 */
	@ResponseBody
	@RequestMapping(value = "findLastAppVersion", method = RequestMethod.POST)
	public String findLastAppVersion(String type, String appVersion,@RequestAttribute("channel") String channel,@RequestParam(value = "appType", defaultValue = "1", required = true) String appType) {
		try {
			boolean b = notEmpty(type, appVersion);
			if (!b) {
				return buildErrorParamMView();
			}
			String version = appVersion.toUpperCase().replace(".", "").replace("V", "");
			if (!b && !NumberUtils.isNumber(version)) {
				return buildErrorParamMView();
			}
			return buildSuccessMView(systemService.findLastAppVersion(type, version,channel,appType));
		} catch (Exception e) {
			L.error("查询APP版本信息异常{}", type, e);
			return buildExceptionMView();
		}
	}

	/**
	 * 查询地区信息
	 */
	@ResponseBody
	@RequestMapping(value = "findAreaList", method = RequestMethod.POST)
	public String findAreaList(
			@RequestParam(value = "parentId", required = true, defaultValue = "1") Integer parentId) {
		try {
			return buildSuccessMView(systemService.findAreaList(parentId));
		} catch (Exception e) {
			L.error("查询地区信息异常{}", parentId, e);
			return buildExceptionMView();
		}
	}
	
	/**
	 * 端首页
	 */
	@ResponseBody
	@RequestMapping(value = "findAppIndex", method = RequestMethod.POST)
	public String findAppIndex(@RequestAttribute("channel") String channel ) {
		try {
			return buildSuccessMView(systemService.findAppIndex(channel));
		} catch (Exception e) {
			L.error("查询首页信息异常{}", e);
			return buildExceptionMView();
		}
	}

}
