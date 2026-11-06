package com.dchealth.pathway.service;

import com.dchealth.research.domain.ResearchInfo;

import java.util.List;

/**
 * 管理路径服务接口
 *
 * @author xuzw
 * @date 2026-06-30
 */
public interface IPathwayService
{
    /**
     * 查询路径列表
     */
    List<ResearchInfo> selectPathwayList(ResearchInfo pathway);

    /**
     * 根据ID查询路径详情
     */
    ResearchInfo selectPathwayById(Long id);

    /**
     * 新增路径
     */
    Long insertPathway(ResearchInfo pathway);

    /**
     * 修改路径
     */
    int updatePathway(ResearchInfo pathway);

    /**
     * 删除路径（逻辑删除）
     */
    int deletePathwayById(Long id);

    /**
     * 更新路径状态（发布/关闭/草稿）
     */
    int updatePathwayStatus(Long id, String pathwayStatus);

    /**
     * 复制路径
     */
    Long copyPathway(Long sourceId);
}
