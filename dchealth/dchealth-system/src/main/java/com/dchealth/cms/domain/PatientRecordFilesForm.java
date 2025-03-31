package com.dchealth.cms.domain;

import lombok.Data;

import java.util.List;

/**
 * 档案上传文件
 */
@Data
public class PatientRecordFilesForm {

    /** 图片地址 */
    private List<String> imageUrl;

    /** medical_form的主键id */
    private Long medicalFormId;

    private String propertyCode;

    /**
     * 属性ID
     */
    private Long propertyId;
    /**
     * 问卷上传ID  【只针对问卷】
     */
    private Long id;

}
