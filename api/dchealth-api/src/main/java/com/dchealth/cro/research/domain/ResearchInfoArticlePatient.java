package com.dchealth.cro.research.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.dchealth.cro.common.base.BaseEntity;

import lombok.Data;

/**
 * 患者完成患教量关系对象 research_info_article_patient
 *
 * @author lchbu
 * @date 2022-09-15
 */
@Data
public class ResearchInfoArticlePatient extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 账号id
     */
    private String accountId;

    private Long accountIds;

    /**
     * 患教id
     */
    private Long articleId;

    private String fillStatus;//0待填写1进行中2已完成

    /**
     * 队列id
     */
    private Long researchId;
    /**
     * 存活的队列ID
     */
    private String survivalResearchIds;

    private Integer pageNo =1;

    private Integer pageSize =10;

    /**
     * 患者填报记录id
     */
    private Long examRecordId;
    /**
     * true: 再来一次， false ：第一次填写
     * 用途是：这个问卷是不是复制出来的
     */
    @TableField(value = "is_fill_again")
    private Boolean isFillAgain = false;

    private String scores;
    private String evaluationResult;


    /**
     *队列名称
     */
    private Long projectName;

    private String infoType;
}
