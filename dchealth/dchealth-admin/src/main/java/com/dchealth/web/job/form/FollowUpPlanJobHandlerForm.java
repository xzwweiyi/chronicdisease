package com.dchealth.web.job.form;

import lombok.Data;

import java.util.List;

/**
 * 单次执行随访计划form
 */
@Data
public class FollowUpPlanJobHandlerForm {

    /**
     * 队列ids
     */
    private List<Long> researchIds;

    /**
     * 执行的日期
     */
    private String handleDate;
}
