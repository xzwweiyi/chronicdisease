package com.dchealth.cro.cms.controller;

import com.alibaba.fastjson.JSONObject;
import com.dchealth.cro.cms.service.XunfeiService;
import com.dchealth.cro.cms.vo.*;
import com.dchealth.cro.common.base.ApiBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

/**
 * 讯飞
 */
@Controller
@RequestMapping(value = "/xunfei")
public class XunfeiController extends ApiBaseController {
	private static final Logger L = LoggerFactory.getLogger(XunfeiController.class);
	@Autowired
	private XunfeiService xunfeiService;

	/**
	 * 查询讯飞模板
	 */
	@ResponseBody
	@RequestMapping(value = "speechList", method = RequestMethod.POST)
	public String list() {
		try {
			return buildSuccessMView(xunfeiService.list());
		} catch (Exception e) {
			L.info("查询讯飞模板{}", e);
			return buildExceptionMView();
		}
	}

	/**
	 * 根据speedId查询电话模板自定义变量
	 */
	@ResponseBody
	@RequestMapping(value = "speechVariable", method = RequestMethod.POST)
	public String speechVariable(@Validated SpeechVariableRes speechVariable) {
		L.info("--------------根据speedId查询电话模板自定义变量--------------:{}",speechVariable);
		try {
			return buildSuccessMView(xunfeiService.getVarList(speechVariable));
		} catch (Exception e) {
			L.info("根据speedId查询电话模板自定义变量异常{}", e);
			return buildExceptionMView();
		}
	}

	/**
	 * 调用外呼开放接口（用电话模版）呼叫
	 */
	@ResponseBody
	@RequestMapping(value = "callTask", method = RequestMethod.POST)
	public String callTask(HttpServletRequest request) {
		String body="";
		try(BufferedReader reader=request.getReader()){
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line=reader.readLine()) != null){
				stringBuilder.append(line);
			}
			body=stringBuilder.toString();
			XunfeiReq xunfeiReq = JSONObject.parseObject(body,XunfeiReq.class);
			return buildSuccessMView(xunfeiService.getCallTask(xunfeiReq));
		}catch (Exception e){
			L.info("查询讯飞模板{}", e);
			return buildExceptionMView();
		}
	}


	/**
	 * 调用外呼开放接口（用短信）呼叫
	 */
	@ResponseBody
	@RequestMapping(value = "smsTask", method = RequestMethod.POST)
	public String smsTask(HttpServletRequest request) {
		String body="";
		try(BufferedReader reader=request.getReader()){
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line=reader.readLine()) != null){
				stringBuilder.append(line);
			}
			body=stringBuilder.toString();
			XunfeiReq xunfeiReq = JSONObject.parseObject(body,XunfeiReq.class);
			return buildSuccessMView(xunfeiService.smsTask(xunfeiReq));
		}catch (Exception e){
			L.info("查询讯飞模板{}", e);
			return buildExceptionMView();
		}
	}



	/**
	 * 查询批次汇总数据接口返回讯飞recordId
	 */
	@ResponseBody
	@RequestMapping(value = "batchList", method = RequestMethod.POST)
	public String batchList(@Validated BatchListReq batchListReq) {
		L.info("--------------查询批次汇总数据接口返回讯飞recordId--------------:{}",batchListReq);
		try {
			return buildSuccessMView(xunfeiService.getBatchList(batchListReq));
		} catch (Exception e) {
			L.info("根据speedId查询电话模板自定义变量异常{}", e);
			return buildExceptionMView();
		}
	}


	/**
	 * 开始调用查询电话记录详情，用上个接口返回的recordId(讯飞recordId)
	 */
	@ResponseBody
	@RequestMapping(value = "recordDetail", method = RequestMethod.POST)
	public String recordDetail(@Validated RecordDetailReq recordDetailReq) {
		L.info("--------------根据讯飞recordId查询具体单个随访任务的记录详情--------------:{}",recordDetailReq);
		try {
			return buildSuccessMView(xunfeiService.recordDetail(recordDetailReq));
		} catch (Exception e) {
			L.info("根据讯飞recordId查询具体单个随访任务的记录详情异常{}", e);
			return buildExceptionMView();
		}
	}




	/**
	 * 下载本地语音文件
	 */
	@ResponseBody
	@RequestMapping(value = "uploadImFile", method = RequestMethod.POST)
	public String uploadImFile(@Validated DownloadImFileVo downloadImFileVo) {
		L.info("--------------下载本地语音文件--------------:{}",downloadImFileVo);
		try {
			return buildSuccessMView(xunfeiService.uploadImFile(downloadImFileVo));
		} catch (Exception e) {
			L.info("根据speedId查询电话模板自定义变量异常{}", e);
			return buildExceptionMView();
		}
	}


	/**
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "sendVerifyCode", method = RequestMethod.POST)
	public String sendVerifyCode(HttpServletRequest request) {
		String body="";
		try(BufferedReader reader=request.getReader()){
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line=reader.readLine()) != null){
				stringBuilder.append(line);
			}
			body=stringBuilder.toString();
			SendTencentSmsVO sendTencentSmsVO = JSONObject.parseObject(body,SendTencentSmsVO.class);
			L.info("--------------发送短信验证码参数--------------:{}",sendTencentSmsVO);
			return buildSuccessMView(xunfeiService.sendVerifyCode(sendTencentSmsVO));
		}catch (Exception e){
			L.info("发送短信验证码异常{}", e);
			return buildExceptionMView();
		}
	}

	

}
