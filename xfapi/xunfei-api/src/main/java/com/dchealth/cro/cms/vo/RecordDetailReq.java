package com.dchealth.cro.cms.vo;

import lombok.Data;


/**
 *
 */
@Data
public class RecordDetailReq {


    /**
     *
     */
    private String recordId;

    //暂时不用传，传会报错
    private String relationId;


}
