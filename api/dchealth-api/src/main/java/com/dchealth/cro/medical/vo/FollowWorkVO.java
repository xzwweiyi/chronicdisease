package com.dchealth.cro.medical.vo;

import java.util.List;

import lombok.Data;

/**
 * 随访工作站列表数据vo
 */
@Data
public class FollowWorkVO {
    /**
     * 随访list
     */
    private List<FollowWorkListVO> FollowWorkListVO;
    /**
     * 数量
     */
    private int recoedSum;
}
