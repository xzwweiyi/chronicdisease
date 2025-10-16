package com.dchealth.cro.form.vo.form;


import lombok.Data;

import java.util.List;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/10/16 12:14
 * @Version: 1.0
 */
@Data
public class FormItemVo {
    private String layout;
    private String propertyName;
    private Boolean isShowDom;
    private String tag;
    private String labelType;
    private Boolean isFlex;
    private Boolean isEditOption;
    private Boolean isShowSubTitle;
    private String subTitle;
    private String tagIcon;
    private Integer span;
    private String optionType;
    private String size;
    private Boolean disabled;
    private Boolean required;
    private Boolean isDoctorShow;
    private Boolean isPatientShow;
    private List<ProblemOptionVo> options;
    private String document;
    private Long renderKey;
    private String vModel;
    private String propertyCode;
    private String propertyType;
    private String placeholder;
    private Integer labelWidth;
    private Boolean clearable;
    private Boolean filterable;
    private Boolean multiple;
    private List<Object> regValue;

    private String propertyValue;

}
