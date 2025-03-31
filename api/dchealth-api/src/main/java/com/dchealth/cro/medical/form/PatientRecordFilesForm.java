package com.dchealth.cro.medical.form;

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

    /**
     * 属性ID
     */
    private Long propertyId;

    private String propertyCode;
    /**
     * 问卷上传ID  【只针对问卷】
     */
    private Long id;

}
