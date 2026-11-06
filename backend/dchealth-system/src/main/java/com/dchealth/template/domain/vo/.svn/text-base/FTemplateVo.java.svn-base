package com.dchealth.template.domain.vo;

import com.dchealth.common.core.domain.BaseEntity;
import com.dchealth.template.domain.FTemplateForm;
import lombok.Data;

import java.util.List;

/**
 * 模板vo类
 */
@Data
public class FTemplateVo extends BaseEntity {

    /**
     * 模板id
     */
    private String templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板类型-单个组件、整个组建
     */
    private String templateType;

    /**
     * 是否删除-0：未删除、1：已删除
     */
    private String isDeleted;

    /**
     * 共享类型-1：私有库、2：公有库
     */
    private String shareType;

    /**
     * 表单对象s
     */
    private List<FTemplateForm> fTemplateForms;

}
