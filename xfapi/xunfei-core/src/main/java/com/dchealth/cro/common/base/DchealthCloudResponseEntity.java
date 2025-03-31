package com.dchealth.cro.common.base;

import java.io.Serializable;

public class DchealthCloudResponseEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private int code = 1000;
	private String msg = "SUCCESS";
	private T data;




	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}





	
	
	

}
