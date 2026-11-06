package com.dchealth.medical.domain.form;

import lombok.Data;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 详情
 */
@Data
public class FollowWorkDetailsForm {

    /**
     * 患者ID
     */
    private Long patientId;

    /**
     * 档案ID
     */
    private Long recordId;

    /**
     * 详情类型
     * 1患者入组审核2随访处理3随访超窗4预警处理5待分配
     */
    private String detailType;

    /**
     * 队列id
     */
    private Long researchId;

    /**
     * 随访id
     */
    private Long followId;
}
