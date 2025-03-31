package com.dchealth.cro.cms.vo;

import lombok.Data;


/**
 *
 */
@Data
public class BatchListRes {


    /**
     *
     */
    private String recordId;

    private String dwellerName;

    private String telephone;

    private String relationId;

    private String resultCode;

    private String endNode;

    private Long callTime;

    private String actualCallCount;

    private Integer callTimeLength;


}
