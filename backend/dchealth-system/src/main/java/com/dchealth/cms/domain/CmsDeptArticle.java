package com.dchealth.cms.domain;

import com.dchealth.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 科室资讯实体
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class CmsDeptArticle extends BaseEntity
{

	private static final long serialVersionUID = 1L;

    /** 科室ID */
    private Long deptId;

    /** 资讯ID */
    private Long articleId;

    /** 分类ID **/
    private Long categoryId;

    private String delFlag;

    /**
     * 分类名称
     */
    private String categoryName;

  
}
