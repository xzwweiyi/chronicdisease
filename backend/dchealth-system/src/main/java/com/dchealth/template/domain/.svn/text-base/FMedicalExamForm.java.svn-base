package com.dchealth.template.domain;

import com.dchealth.cms.domain.PatientRecordFilesForm;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 单模板-问卷对象 f_medical_exam_form
 * 
 * @author lchbu
 * @date 2022-11-04
 */
@Data
public class FMedicalExamForm extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 表单id */
    private Long medicalFormId;

    /** 随访表单id */
    @Excel(name = "随访表单id")
    private Long followId;

    /** 表单编码 */
    @Excel(name = "表单编码")
    private String medicalCode;

    /** 表单名称 */
    @Excel(name = "表单名称")
    private String medicalName;

    /** 父id */
    @Excel(name = "父id")
    private Long parentId;

    /** 是否开启APP端图片上传 0未开启 1 开启  */
    @Excel(name = "是否开启APP端图片上传 0未开启 1 开启 ")
    private String openImage;

    /** 是否开启APP端属性填写 0未开启 1 开启  */
    @Excel(name = "是否开启APP端属性填写 0未开启 1 开启 ")
    private String openProperty;

    /** 是否开启动态添加 0未开启 1 开启  */
    @Excel(name = "是否开启动态添加 0未开启 1 开启 ")
    private String openMultiple;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String attribute;

    /** 版本号 */
    @Excel(name = "版本号")
    private String versionCode;

    /** 0模块1标准项2拍照项3自评量表4预警填报 */
    @Excel(name = "0模块1标准项2拍照项3自评量表4预警填报")
    private String formType;

    /** 是否动态表格0否1是 */
    @Excel(name = "是否动态表格0否1是")
    private String dynamic;

    /** 表单来源 */
    @Excel(name = "表单来源")
    private String formSource;

    /** 栏目编号 */
    private Long categoryId;

    /**
     * 问卷信息
     */
    private List<FMedicalExamSubject> subjectList;

    /** cms关联id */
    private Long articleId;


    private Long createId;

    //科室id集合
    private String deptIds;

    private Long deptId;

    /** 浏览图 */
    private String imageUrl;

    //描述、摘要、说明
    private String description;


    private Long patientId;

    /** 患者填报记录id */
    private Long examRecordId;

    /** 分数 */
    private Long scores;
    /** 评价 */
    private String evaluation;

    /** 是否提交；true提交，false保存 */
    private boolean flag;

    private Long researchId;

    private String status;//0待填写 1保存 2提交

    /**
     * 是否再来一次
     *  用途是  这个问卷是不是复制出来的
     */
    private Boolean isFillAgain = false;

    /**
     * 用途是控制是否展示“再来一次”按钮
     * true：展示，false：隐藏
     */
    private Boolean isShowAgain = true;
    /**
     * 上传图片
     */
    private List<PatientRecordFilesForm> imageUrls;
    private String imageUrlsJson;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("medicalFormId", getMedicalFormId())
            .append("followId", getFollowId())
            .append("medicalCode", getMedicalCode())
            .append("medicalName", getMedicalName())
            .append("parentId", getParentId())
            .append("openImage", getOpenImage())
            .append("openProperty", getOpenProperty())
            .append("openMultiple", getOpenMultiple())
            .append("attribute", getAttribute())
            .append("versionCode", getVersionCode())
            .append("formType", getFormType())
            .append("dynamic", getDynamic())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("formSource", getFormSource())
            .toString();
    }
}
