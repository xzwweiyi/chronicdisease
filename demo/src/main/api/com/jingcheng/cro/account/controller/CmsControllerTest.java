package com.jingcheng.cro.account.controller;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.guanmi.common.utils.HttpClientUtil;
import org.junit.Test;

public class CmsControllerTest {

	@Test
	public void testList() {
		fail("Not yet implemented");
	}

	@Test
	public void testCatalog() {
		String url =  "article/catalog";
		Map<String, String> maps = new HashMap<String, String>();
//		maps.put("appType", "1");
		String result = HttpClientUtil.getInstance().sendSzDefaultHttpPost(url, maps);
		System.out.println(result);
	}

	@Test
	public void testDetail() {
		fail("Not yet implemented");
	}

	@Test
	public void testDetailComment() {
		fail("Not yet implemented");
	}

	@Test
	public void testSave() {
		fail("Not yet implemented");
	}

}
