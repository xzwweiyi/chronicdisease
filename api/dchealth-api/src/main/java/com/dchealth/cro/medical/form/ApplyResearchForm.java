package com.dchealth.cro.medical.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 申请队列接口
 */
@Data
public class ApplyResearchForm {

    @NotNull(message = "账号ID不能为空")
    private Long accountId;

    @NotNull(message = "队列ID不能为空")
    private Long researchId;
    
    private String personId;

    @NotNull(message = "科室ID不能为空")
    private Long deptId;

    @NotNull(message = "疾病ID不能为空")
    private Long diseaseId;

}
