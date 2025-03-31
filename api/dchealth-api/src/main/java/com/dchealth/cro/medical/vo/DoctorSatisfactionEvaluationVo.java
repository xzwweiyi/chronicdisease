package com.dchealth.cro.medical.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DoctorSatisfactionEvaluationVo {


    @NotNull(message = "用户ID不能为空")
    private Long accountId;//用户Id

    /** 分值 */
    private Integer score;

    /** 意见 */
    private String suggestion;





}
