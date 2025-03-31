package com.dchealth.cro.common.exception.base;

/**
 * 基础异常
 * 
 * @author ruoyi
 */
public class ServiceException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * 所属模块
     */
    private String msg;

    /**
     * 错误码
     */
    private int code;

    public ServiceException() {
	}
    
    
    
	public ServiceException(String msg, int code) {
		super();
		this.msg = msg;
		this.code = code;
	}



	public ServiceException(int code) {
		super();
		this.code = code;
	}



	public ServiceException(String msg) {
		super();
		this.msg = msg;
	}



	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

   
}
