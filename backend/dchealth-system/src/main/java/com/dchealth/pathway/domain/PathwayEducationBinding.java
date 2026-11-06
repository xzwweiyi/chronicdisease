package com.dchealth.pathway.domain;

import com.dchealth.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 路径宣教内容绑定对象 pathway_education_binding
 *
 * @author xuzw
 * @date 2026-06-30
 */
@Data
public class PathwayEducationBinding extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 路径ID */
    private Long researchId;

    /** 节点模板ID（null表示路径全局绑定） */
    private Long templateId;

    /** 患教文章ID */
    private Long articleId;

    /** 绑定类型：1-节点绑定，2-路径全局 */
    private String bindingType;

    /** 推送时机：1-入组时，2-节点开始前，3-节点完成后 */
    private String pushTiming;

    /** 提前推送天数 */
    private Integer pushAdvanceDays;

    /** 排序 */
    private Integer sortOrder;
}
