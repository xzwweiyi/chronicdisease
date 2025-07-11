package com.dchealth.cro.medical.form;

import com.dchealth.cro.form.domain.form.FMedicalPropertyForm;
import com.dchealth.cro.question.form.QuestionRecordForm;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 患者档案保存更新信息实体
 */
@Data
public class PatientRecordSaveUpdateInfoForm {

    public PatientRecordSaveUpdateInfoForm(){}

    /**
     * 档案ID
     */
    @NotNull(message="档案不能为空")
    private Long recordId;

    /**
     * 健康档案id
     */
    private Long healthId;

    /**
     *表单id
     */
    private Long medicalFormId;

    private Integer healthManageType;

    /**
     * 患者ID
     */
    //private Long patientId;

    /**
     * 队列ID
     */
    //private Long researchId;

    /**
     * 随访ID
     */
    //private Long followId;

    /**
     * 来源(1-患者，2-EDC)
     */
    //private String type;

    /**
     * 属性值集合
     */
    private List<FMedicalPropertyForm> propertyForms;

//    @NotBlank(message = "请填写表单数据")
    private String propertyFormsJson;



    /**
     * 上传图片
     */
    private List<PatientRecordFilesForm> imageUrls;

    private String imageUrlsJson;

    /**
     * 质疑
     */
    private String questionRecordJson;

    private QuestionRecordForm questionRecordForm;
    /**
     * 0 保存  1 提交
     */
    private Integer isSubmit;

    /**
     * 表单完成比率
     */
    private Double formComplentRate;


}
