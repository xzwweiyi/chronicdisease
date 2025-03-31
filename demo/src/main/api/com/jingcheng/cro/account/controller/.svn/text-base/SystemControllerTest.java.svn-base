package com.jingcheng.cro.account.controller;

import java.util.HashMap;
import java.util.Map;

import org.guanmi.common.utils.HttpClientUtil;
import org.junit.Test;

public class SystemControllerTest {
	@Test
	public void findAppIndex() {
		String url ="system/findAppIndex";
		Map<String, String> maps = new HashMap<String, String>();
		String result = HttpClientUtil.getInstance().sendSzDefaultHttpPost(url, maps);
		System.out.println(result);
	}
	
}
