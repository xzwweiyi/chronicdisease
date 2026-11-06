package com.dchealth.statistics.domain;

import lombok.Data;

import java.util.Date;

@Data
public class PatientDistribute {

    /**
     * 省份ID
     */
    private Long provinceId;

    private Date date;
}
