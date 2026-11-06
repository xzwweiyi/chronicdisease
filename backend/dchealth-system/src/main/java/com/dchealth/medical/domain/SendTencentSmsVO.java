package com.dchealth.medical.domain;

import lombok.Data;

@Data
public class SendTencentSmsVO {
  private String appId;
  private String [] phones;
  private String templateId;
  private String [] templateParam;
  private TencentSmsVO tencentSmsVO;

  public SendTencentSmsVO(){

  }

  public SendTencentSmsVO(String appId, String [] phones, String templateId, String [] templateParam){
    this.appId = appId;
    this.phones = phones;
    this.templateId = templateId;
    this.templateParam = templateParam;
  }

}
