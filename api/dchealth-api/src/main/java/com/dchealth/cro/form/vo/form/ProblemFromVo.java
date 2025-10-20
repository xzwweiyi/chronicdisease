package com.dchealth.cro.form.vo.form;


import lombok.Data;

import java.util.List;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/10/16 12:13
 * @Version: 1.0
 */
@Data
public class ProblemFromVo {

    private String layout;
    private Boolean isShowDom;
    private String tagIcon;
    private String labelType;
    private String type;
    private Boolean isShowSubTitle;
    private String subTitle;
    private String justify;
    private String align;
    private String formCode;
    private String formName;
    private Boolean isDoctorShow;
    private Boolean isPatientShow;
    private String openImage;
    private String openMultiple;
    private String imageAction;
    private Integer span;
    private List<FormItemVo> children;
    private String document;
    private String renderKey;
    private Object subjectScores;
    private Integer gutter;
    private Object defaultValue;
    private String id;
    private String propertyCode;
    private String propertyName;
    private String parentId;
    private String propertyType;
    private String dictType;
    private Integer medicalFormId;
    private String openProperty;
    private Integer sortNum;
    private String ifScore;
    private Object examSubjects;
    private Object propertyValues;
    private Object imageUrls;
    private Object articleId;
    private Object name;
    private Object value;
    private Object tableName;
    private Object propertyValue;
    private Object other;
    private Integer pageNum;
    private Integer pageSize;
    private Object createBy;
    private Object followId;

}
