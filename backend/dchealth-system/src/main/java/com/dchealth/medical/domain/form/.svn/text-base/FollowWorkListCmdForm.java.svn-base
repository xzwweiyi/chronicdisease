package com.dchealth.medical.domain.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

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
