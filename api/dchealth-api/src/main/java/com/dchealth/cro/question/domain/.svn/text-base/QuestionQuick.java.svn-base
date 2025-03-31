package com.dchealth.cro.question.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 质疑快捷回复对象 question_quick
 * 
 * @author lchbu
 * @date 2022-09-27
 */
@Data
public class QuestionQuick
{
    private static final long serialVersionUID = 1L;

    /** 快捷回复id */
    private Long questionQuickId;

    /** 快捷内容 */
    private String quickContent;

    /** 创建人 */
    private Long createUserId;

    /** 科室id */
    private Long deptId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
}
