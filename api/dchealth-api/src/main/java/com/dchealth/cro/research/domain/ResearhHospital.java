package com.dchealth.cro.research.domain;

import lombok.Data;

/**
 * 队列参与中心信息对象 researh_hospital
 * 
 * @author genemax1107@aliyun.com
 * @date 2022-06-14
 */
@Data
public class ResearhHospital 
{

    private Long id;

    /** 队列ID */
    private Long researchId;

    /** 中心ID */
    private Long deptId;

    /** 目标患者数 */
    private Integer targetNum;

    
}
