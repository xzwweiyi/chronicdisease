package com.dchealth.cro.research.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReSearchForm {
    private Integer pageNo = 1;//页码
    private Integer pageSize = 10;//每页记录数
    @NotNull(message = "用户ID不能为空")
    private Long accountId;//用户Id
    @NotNull(message = "队列ID不能为空")
    private Long researchId;
    private String tenantId;//租户ID


    private String isDoctor;
    private String isPatient;


}
