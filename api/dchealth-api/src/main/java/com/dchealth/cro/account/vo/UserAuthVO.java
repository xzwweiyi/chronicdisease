package com.dchealth.cro.account.vo;

import java.io.Serializable;

public class UserAuthVO  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long accountId;//用户ID
	
	private String facePhoto;//正面照
	private String backPhoto;//反面照
	private String handPhoto;//手持照
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public String getFacePhoto() {
		return facePhoto;
	}
	public void setFacePhoto(String facePhoto) {
		this.facePhoto = facePhoto;
	}
	public String getBackPhoto() {
		return backPhoto;
	}
	public void setBackPhoto(String backPhoto) {
		this.backPhoto = backPhoto;
	}
	public String getHandPhoto() {
		return handPhoto;
	}
	public void setHandPhoto(String handPhoto) {
		this.handPhoto = handPhoto;
	}
	
	

}
