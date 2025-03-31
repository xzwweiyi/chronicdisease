package com.dchealth.research.domain.vo;

import lombok.Data;

/**
 * 队列随访节点vo
 */
@Data
public class ResearchFollowVO {

    /**
     * 随访节点id
     */
    private Long followId;

    /**
     * 随访节点名称
     */
    private String followName;

    /**
     *
     */
    private boolean isCheck;

}
