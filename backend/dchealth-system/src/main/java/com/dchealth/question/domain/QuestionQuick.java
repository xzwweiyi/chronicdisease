package com.dchealth.question.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 质疑快捷回复对象 question_quick
 * 
 * @author lchbu
 * @date 2022-09-27
 */
@Data
public class QuestionQuick extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 快捷回复id */
    private Long questionQuickId;

    /** 快捷内容 */
    @Excel(name = "快捷内容")
    private String quickContent;

    /** 创建人 */
    @Excel(name = "创建人")
    private Long createUserId;

    /** 科室id */
    @Excel(name = "科室id")
    private Long deptId;

}
