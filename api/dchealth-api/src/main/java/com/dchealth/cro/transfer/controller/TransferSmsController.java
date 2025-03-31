package com.dchealth.cro.transfer.controller;

import cn.hutool.core.date.DateTime;
import com.dchealth.cro.account.form.SendVerfiyCodeForm;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.constant.Constants;
import com.dchealth.cro.common.constant.PatternConstant;
import com.dchealth.cro.common.crypto.MD5Util;
import com.dchealth.cro.common.crypto.Sm4Util;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.transfer.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

@RestController
@RequestMapping(value = "/transfer/sms")
public class TransferSmsController extends ApiBaseController {
	private static final Logger L = LoggerFactory.getLogger(TransferSmsController.class);

	@Autowired
	private TransferService transferService;

	/**
	 *  获取医生端短信
	 */
	@ResponseBody
	@RequestMapping(value = "doctorTencentSmsVO", method = RequestMethod.POST)
	public String doctorTencentSmsVO(@Validated SendVerfiyCodeForm sendVerfiyCodeForm, BindingResult bind) {
		if (bind.hasErrors()) {
			return buildBindErrorParamMView(bind);
		}
		sendVerfiyCodeForm.setAppId("1");
		String mobile = "";
		try {
			mobile = Sm4Util.decryptEcb(Constants.SM4_KEY, sendVerfiyCodeForm.getMobile());
		} catch (Exception e) {
			L.error("手机号有误:{}", sendVerfiyCodeForm.getMobile());
			return buildErrorParamMView();
		}
		sendVerfiyCodeForm.setMobile(mobile);
		if (sendVerfiyCodeForm.getMobile().matches(PatternConstant.PHONE) ) {
			return buildSuccessMView(transferService.getDoctorTencentSmsVO(sendVerfiyCodeForm));
		}
		return buildErrorParamMView();
	}


	/**
	 *  获取患者短信
	 */
	@ResponseBody
	@RequestMapping(value = "patientTencentSmsVO", method = RequestMethod.POST)
	public String patientTencentSmsVO(@Validated SendVerfiyCodeForm sendVerfiyCodeForm, BindingResult bind) {
		if (bind.hasErrors()) {
			return buildBindErrorParamMView(bind);
		}
		sendVerfiyCodeForm.setAppId("1");
		String mobile = "";
		try {
			mobile = Sm4Util.decryptEcb(Constants.SM4_KEY, sendVerfiyCodeForm.getMobile());
		} catch (Exception e) {
			L.error("手机号有误:{}", sendVerfiyCodeForm.getMobile());
			return buildErrorParamMView();
		}
		sendVerfiyCodeForm.setMobile(mobile);
		if (sendVerfiyCodeForm.getMobile().matches(PatternConstant.PHONE) ) {
			return buildSuccessMView(transferService.getPatientTencentSmsVO(sendVerfiyCodeForm));
		}
		return buildErrorParamMView();
	}


	/**
	 *  获取小程序患者短信
	 */
	@ResponseBody
	@RequestMapping(value = "patientTencentMinSmsVO", method = RequestMethod.POST)
	public String patientTencentMinSmsVO(@Validated SendVerfiyCodeForm sendVerfiyCodeForm, BindingResult bind) {
		if (bind.hasErrors()) {
			return buildBindErrorParamMView(bind);
		}
		sendVerfiyCodeForm.setAppId("1");
		if (sendVerfiyCodeForm.getMobile().matches(PatternConstant.PHONE) ) {
			return buildSuccessMView(transferService.getPatientTencentSmsVO(sendVerfiyCodeForm));
		}
		return buildErrorParamMView();
	}

	/**
	 *  部署程序
	 */
	@ResponseBody
	@RequestMapping(value = "publish", method = RequestMethod.POST)
	public String publish(MultipartFile file,String token,String sign) {
		L.info("上传文件={}",file.getOriginalFilename());
//		String path = "C:/tmp/upload_temp/tdapi/";
		String path = "/usr/local/server/mirco-server/upload_temp/tdapi/";
		Date date = new Date();
		String s = DateUtils.dateTime(date);
		String encode = MD5Util.encode(s);
		if (!encode.equals(token)) {
			return buildExceptionMView();
		}
		File file1 = new File(path);
		if (!file1.exists()) {
			file1.mkdir();
		}
        try {
            file.transferTo(new File(path+file.getOriginalFilename()));
			FileOutputStream fileOutputStream = new FileOutputStream(new File(
					path + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("_"))+ "_sign.txt"));
			fileOutputStream.write(sign.getBytes());
			fileOutputStream.close();
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
        return buildSuccessMView(Boolean.TRUE);
	}

//	restTemplate.getForEntity("/transfer/sms/{id}", String.class, 1)
	/**
	 *  部署程序
	 */
	@ResponseBody
	@RequestMapping(value = "option", method = RequestMethod.POST)
	public String publish2(String cmd, String token) {
		Date date = new Date();
		String s = DateUtils.dateTime(date);
		String encode = MD5Util.encode(s);
		if (!encode.equals(token)) {
			return buildExceptionMView();
		}
		if ("restart".equals(cmd)) {
			//重启
			ProcessBuilder pb = new ProcessBuilder();
			pb.command("sh","/usr/local/server/mirco-server/restartTD.sh");
            try {
				Process start = pb.start();
				BufferedReader reader = new BufferedReader(new InputStreamReader(start.getInputStream()));
				String version = reader.readLine();
				L.info(version);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		if ("public".equals(cmd)) {
			//发布更新
			//链接, 分发, 执行命令
			ProcessBuilder pb = new ProcessBuilder();
			pb.command("sh","/usr/local/server/mirco-server/publicTD.sh");
			try {
				Process start = pb.start();
				BufferedReader reader = new BufferedReader(new InputStreamReader(start.getInputStream()));
				String version = reader.readLine();
				L.info(version);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return buildSuccessMView(cmd);
	}
}
