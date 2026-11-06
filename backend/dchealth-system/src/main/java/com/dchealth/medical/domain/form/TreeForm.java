package com.dchealth.medical.domain.form;

import lombok.Data;

@Data
public class TreeForm {

    /**
     * 1随访工作站2评价
     */
    private String type;

    private Long deptId;

    private Long userId;

    private Long diseaseId;
}
