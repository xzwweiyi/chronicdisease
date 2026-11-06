package com.dchealth.pathway.service;

import com.dchealth.medical.domain.PatientRecordTemplate;

import java.util.List;

/**
 * 管理路径节点服务接口
 *
 * @author xuzw
 * @date 2026-06-30
 */
public interface IPathwayNodeService
{
    /**
     * 查询路径的所有节点
     */
    List<PatientRecordTemplate> selectNodesByPathwayId(Long researchId);

    /**
     * 新增节点
     */
    int insertNode(PatientRecordTemplate node);

    /**
     * 修改节点
     */
    int updateNode(PatientRecordTemplate node);

    /**
     * 删除节点
     */
    int deleteNode(Long followId);

    /**
     * 节点排序
     */
    int reorderNodes(Long researchId, List<Long> orderedFollowIds);

    /**
     * 保存节点的CRF表单绑定
     */
    int saveNodeForms(Long followId, String requiredForms, String optionalForms);

    /**
     * 获取基线节点
     */
    PatientRecordTemplate selectBaselineNode(Long researchId);
}
