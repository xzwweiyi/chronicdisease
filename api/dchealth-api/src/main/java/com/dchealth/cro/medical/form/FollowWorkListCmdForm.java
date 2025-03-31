package com.dchealth.cro.medical.form;

import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * 随访工作站列表表单
 */
@Data
public class FollowWorkListCmdForm {

    /**
     * 队列ID
     */
    @NotNull
    private Long researchId;

    /**
     * 随访模版id
     */
    @NotNull
    private Long followId;

    @NotNull
    private Long patientId;

}
