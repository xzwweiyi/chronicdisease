package com.jingcheng.cro.account.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.guanmi.common.crypto.Sm4Util;
import org.guanmi.common.utils.AESUtils;
import org.guanmi.common.utils.Constants;
import org.guanmi.common.utils.HttpClientUtil;
import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

public class UserControllerTest {
	public static void main(String[] args) {
		  String aesMobile = null;
			try {
				aesMobile = Sm4Util.encryptEcb(Constants.AES_KEY,"18515574691");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(aesMobile);
	}
    @Test
    public void sendMobileSmsVerifyCode() {
        String url = "user/sendAccountVerifyCode";
        Map<String, String> maps = new HashMap<String, String>();
        String aesMobile = null;
		try {
			aesMobile = Sm4Util.encryptEcb(Constants.AES_KEY,"18515574691");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        maps.put("mobile", aesMobile);
        maps.put("bussinessType", "4");
        String result = HttpClientUtil.getInstance().sendSzDefaultHttpPost(url, maps);
        System.out.println(result);
        Assert.assertTrue(StringUtils.equals("SUCCESS", JSONObject.parseObject(result).getString("msg")));
    }

    @Test
    public void checkAccount() {
        String url = "user/checkAccount";
        Map<String, String> maps = new HashMap<String, String>();
        String aesMobile = null;
		try {
			aesMobile = Sm4Util.encryptEcb(Constants.AES_KEY,"18515574691");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
        maps.put("phone", aesMobile);
        String result = HttpClientUtil.getInstance().sendSzDefaultHttpPost(url, maps);
        System.out.println(result);
    }

    @Test
    public void checkVerifyCode() throws Exception {
        String url = "user/checkVerifyCode";
        Map<String, String> maps = new HashMap<String, String>();
        String aesMobile = Sm4Util.encryptEcb(Constants.AES_KEY,"18515574691");
        maps.put("mobile", aesMobile);
        maps.put("verifyCode", "823080");
        String result = HttpClientUtil.getInstance().sendSzDefaultHttpPost(url, maps);
        System.out.println(result);
    }

    @Test
    public void updatePasswordByAccount() throws Exception {
        String url = "user/updatePasswordByAccount";
        Map<String, String> maps = new HashMap<String, String>();
        String aesMobile = Sm4Util.encryptEcb(Constants.AES_KEY,"18515574691");
        maps.put("account", "18392183795");
        maps.put("password", "123456");
        String result = HttpClientUtil.getInstance().sendSzDefaultHttpPost(url, maps);
        System.out.println(result);
        Assert.assertTrue(StringUtils.equals("SUCCESS", JSONObject.parseObject(result).getString("msg")));
    }

    @Test
    public void existSysUser() {
        String url = "user/existSysUser";
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("account", "18392183795");
        String result = HttpClientUtil.getInstance().sendSzDefaultHttpPost(url, maps);
        System.out.println(result);
        Assert.assertTrue(StringUtils.equals("SUCCESS", JSONObject.parseObject(result).getString("msg")));
    }

    @Test
    public void detailDoctor() {
        String url = "user/detailDoctor";
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("doctorId", "102");
        String result = HttpClientUtil.getInstance().sendSzDefaultHttpPost(url, maps);
        System.out.println(result);
        Assert.assertTrue(StringUtils.equals("SUCCESS", JSONObject.parseObject(result).getString("msg")));
    }

    @Test
    public void loginByMobileSms() throws Exception {
        String url = "user/loginByMobileSms";
        Map<String, String> maps = new HashMap<String, String>();
        String aesMobile = Sm4Util.encryptEcb(Constants.AES_KEY,"18515574691");
        maps.put("mobile", aesMobile);
        maps.put("verifyCode", "840080");
        String result = HttpClientUtil.getInstance().sendSzDefaultHttpPost(url, maps);
        System.out.println(result);
        Assert.assertTrue(StringUtils.equals("SUCCESS", JSONObject.parseObject(result).getString("msg")));
    }


    @Test
    public void loginByAccount() throws Exception {
        String url = "user/loginByAccount";
        Map<String, String> maps = new HashMap<String, String>();
        String aesMobile = Sm4Util.encryptEcb(Constants.AES_KEY,"18515574691");
        maps.put("loginName", aesMobile);
        String aespwd = AESUtils.encAESCode("123456", Constants.AES_KEY);
        maps.put("password", aespwd);
        String result = HttpClientUtil.getInstance().sendSzDefaultHttpPost(url, maps);
        System.out.println(result);
        Assert.assertTrue(StringUtils.equals("SUCCESS", JSONObject.parseObject(result).getString("msg")));
    }

    @Test
    public void login() throws Exception {
        String url = "user/login";
        Map<String, String> maps = new HashMap<String, String>();
        String aesMobile = Sm4Util.encryptEcb(Constants.AES_KEY,"18515574691");
        maps.put("mobile", aesMobile);
        String result = HttpClientUtil.getInstance().sendSzDefaultHttpPost(url, maps);
        System.out.println(result);
        Assert.assertTrue(StringUtils.equals("SUCCESS", JSONObject.parseObject(result).getString("msg")));
    }

}
