package com.dchealth.cro.cms.vo;

import com.dchealth.cro.core.tencent.TencentSmsVO;
import lombok.Data;

@Data
public class SendTencentSmsVO {

  private String appId;
  private String [] phones;
  private String templateId;
  private String [] templateParam;
  private TencentSmsVO tencentSmsVO;

}
