package com.jingcheng.cro.account.controller;

import java.util.HashMap;
import java.util.Map;

import org.guanmi.common.utils.AESUtils;
import org.guanmi.common.utils.Constants;
import org.guanmi.common.utils.HttpClientUtil;
import org.junit.Test;

public class AccountControllerTest {
	@Test
	public void testResearch() {
		String url = "research/research_standard";
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("researchId", "1");
		String result = HttpClientUtil.getInstance().sendSzDefaultHttpPost(url, maps);
		System.out.println(result);
		
	}
	
	@Test
    public void sendMobileSmsVerifyCode() {
        String url = "user/sendAccountVerifyCode";
        Map<String, String> maps = new HashMap<String, String>();
        String aesMobile = AESUtils.encAESCode("18392183795", Constants.AES_KEY);
        maps.put("mobile", aesMobile);
        maps.put("bussinessType", "1");
        String result = HttpClientUtil.getInstance().sendSzDefaultHttpPost(url, maps);
        System.out.println(result);
    }

}
