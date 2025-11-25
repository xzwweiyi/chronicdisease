package com.dchealth.medical.domain.form;


import lombok.Data;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/11/24 17:12
 * @Version: 1.0
 */
@Data
public class SelfAssessmentForm {

    private String medicalFormId;

    /**
     * 分数
     */
    private String scores;

    private String code;

    private String name;

    /**
     * 描述
     */
    private String evaluationResult;

}
