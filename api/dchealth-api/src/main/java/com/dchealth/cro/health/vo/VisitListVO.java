package com.dchealth.cro.health.vo;

import lombok.Data;

@Data
public class VisitListVO {
	private String personId;//患者ID

    private String itemName;//体检报告或健康报告
    
    private String checkDate;//就诊日期
    
    private String visitId;//就诊ID

    private String visitType;//就诊类型

}
