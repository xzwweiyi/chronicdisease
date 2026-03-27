package com.dchealth.cro.transfer.controller;

import com.alibaba.fastjson.JSONObject;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.transfer.service.TransferRecordOutbounService;
import com.dchealth.cro.transfer.vo.RecordOutbounForm;
import com.dchealth.cro.transfer.vo.RecordOutbounUpdateForm;
import com.dchealth.cro.transfer.vo.UploadToLocalWavVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

@RestController
@RequestMapping(value = "/transfer/recordOutbound")
public class TransferRecordOutboundController extends ApiBaseController {
	private static final Logger L = LoggerFactory.getLogger(TransferRecordOutboundController.class);

	@Autowired
	private TransferRecordOutbounService transferRecordOutbounService;

	/**
	 *  获取需要外呼的数据
	 */
	@ResponseBody
	@RequestMapping(value = "recordOutbound", method = RequestMethod.POST)
	public String recordOutbound(@Validated RecordOutbounForm recordOutbounForm, BindingResult bind) {
		if (bind.hasErrors()) {
			return buildBindErrorParamMView(bind);
		}
		return buildSuccessMView(transferRecordOutbounService.getRecordOutboundVO(recordOutbounForm));
	}

	/**
	 *  获取需要外呼的数据
	 */
	@ResponseBody
	@RequestMapping(value = "recordOutboundUpdate", method = RequestMethod.POST)
	public String recordOutboundUpdate(HttpServletRequest request) {
		String body="";
		try(BufferedReader reader=request.getReader()){
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line=reader.readLine()) != null){
				stringBuilder.append(line);
			}
			body=stringBuilder.toString();
			RecordOutbounUpdateForm recordOutbounUpdateForm = JSONObject.parseObject(body,RecordOutbounUpdateForm.class);
			return buildSuccessMView(transferRecordOutbounService.recordOutboundUpdate(recordOutbounUpdateForm));
		}catch (Exception e){
			L.info("准备更新讯飞批次号和任务id异常{}", e);
			return buildExceptionMView();
		}
	}


	/**
	 *  获取需要查询结果的数据
	 */
	@ResponseBody
	@RequestMapping(value = "resultOutboundList", method = RequestMethod.POST)
	public String resultOutboundList(@Validated RecordOutbounForm recordOutbounForm, BindingResult bind) {
		if (bind.hasErrors()) {
			return buildBindErrorParamMView(bind);
		}
		return buildSuccessMView(transferRecordOutbounService.getResultOutboundList(recordOutbounForm));
	}

	/**
	 *  获取短信的数据
	 */
	@ResponseBody
	@RequestMapping(value = "getSendSmsList", method = RequestMethod.POST)
	public String getSendSmsList(@Validated RecordOutbounForm recordOutbounForm, BindingResult bind) {
		if (bind.hasErrors()) {
			return buildBindErrorParamMView(bind);
		}
		return buildSuccessMView(transferRecordOutbounService.getSendSmsList(recordOutbounForm));
	}


	/**
	 *  获取需要下载到本地的语音文件列表
	 */
	@ResponseBody
	@RequestMapping(value = "getDounloadList", method = RequestMethod.POST)
	public String getDounloadList(@Validated RecordOutbounForm recordOutbounForm, BindingResult bind) {
		if (bind.hasErrors()) {
			return buildBindErrorParamMView(bind);
		}
		return buildSuccessMView(transferRecordOutbounService.getDounloadList(recordOutbounForm));
	}
	@ResponseBody
	@RequestMapping(value = "getHistoryDounloadList", method = RequestMethod.POST)
	public String getHistoryDounloadList(@Validated RecordOutbounForm recordOutbounForm, BindingResult bind) {
		if (bind.hasErrors()) {
			return buildBindErrorParamMView(bind);
		}
		return buildSuccessMView(transferRecordOutbounService.getHistoryDounloadList(recordOutbounForm));
	}

	/**
	 * 中转服务器查询到需要下载语音文件到本地数据后，调用此方法上传文件
	 */
	@ResponseBody
	@RequestMapping(value = "uploadToLocalWavVo", method = RequestMethod.POST)
	public String uploadToLocalWavVo(@Validated UploadToLocalWavVo uploadToLocalWavVo, BindingResult bind, MultipartFile file) {
		if (bind.hasErrors()) {
			return buildBindErrorParamMView(bind);
		}
		return buildSuccessMView(transferRecordOutbounService.uploadToLocalWavVo(uploadToLocalWavVo,file));
	}



//	@ResponseBody
//	@RequestMapping(value = "uploadToLocalWavVo", method = RequestMethod.POST)
//	public String uploadToLocalWavVo(HttpServletRequest request) {
//		String body="";
//		try(BufferedReader reader=request.getReader()){
//			StringBuilder stringBuilder = new StringBuilder();
//			String line;
//			while ((line=reader.readLine()) != null){
//				stringBuilder.append(line);
//			}
//			body=stringBuilder.toString();
//			UploadToLocalWavVo uploadToLocalWavVo = JSONObject.parseObject(body,UploadToLocalWavVo.class);
//			L.info("---------------uploadToLocalWavVo参数:{}--------------", JSON.toJSONString(uploadToLocalWavVo));
//			return buildSuccessMView(transferRecordOutbounService.uploadToLocalWavVo(uploadToLocalWavVo));
//		}catch (Exception e){
//			L.info("准备更新讯飞批次号和任务id异常{}", e);
//			return buildExceptionMView();
//		}
//	}



}
