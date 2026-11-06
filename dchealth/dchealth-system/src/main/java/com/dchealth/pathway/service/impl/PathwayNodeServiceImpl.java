package com.dchealth.pathway.service.impl;

import com.dchealth.medical.domain.PatientRecordTemplate;
import com.dchealth.medical.mapper.PatientRecordTemplateMapper;
import com.dchealth.pathway.service.IPathwayNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 管理路径节点Service业务层处理
 *
 * @author xuzw
 * @date 2026-06-30
 */
@Service
public class PathwayNodeServiceImpl implements IPathwayNodeService
{
    @Autowired
    private PatientRecordTemplateMapper patientRecordTemplateMapper;

    /**
     * 查询路径的所有节点（按排序号升序）
     */
    @Override
    public List<PatientRecordTemplate> selectNodesByPathwayId(Long researchId)
    {
        return patientRecordTemplateMapper.selectPatientRecordTemplateByResearchId(researchId);
    }

    /**
     * 新增节点
     */
    @Override
    public int insertNode(PatientRecordTemplate node)
    {
        node.setCreateTime(new Date());
        patientRecordTemplateMapper.insertPatientRecordTemplate(node);
        return 1;
    }

    /**
     * 修改节点
     */
    @Override
    public int updateNode(PatientRecordTemplate node)
    {
        node.setUpdateTime(new Date());
        patientRecordTemplateMapper.updatePatientRecordTemplate(node);
        return 1;
    }

    /**
     * 删除节点
     */
    @Override
    @Transactional
    public int deleteNode(Long followId)
    {
        PatientRecordTemplate node = new PatientRecordTemplate();
        node.setFollowId(followId);
        patientRecordTemplateMapper.deletePatientRecordTemplateByFollowId(followId);
        return 1;
    }

    /**
     * 节点排序
     */
    @Override
    @Transactional
    public int reorderNodes(Long researchId, List<Long> orderedFollowIds)
    {
        if (orderedFollowIds == null || orderedFollowIds.isEmpty()) {
            return 0;
        }
        for (int i = 0; i < orderedFollowIds.size(); i++) {
            PatientRecordTemplate node = new PatientRecordTemplate();
            node.setFollowId(orderedFollowIds.get(i));
            node.setNodeOrder(i + 1);
            node.setUpdateTime(new Date());
            patientRecordTemplateMapper.updatePatientRecordTemplate(node);
        }
        return orderedFollowIds.size();
    }

    /**
     * 保存节点的CRF表单绑定
     */
    @Override
    public int saveNodeForms(Long followId, String requiredForms, String optionalForms)
    {
        PatientRecordTemplate node = new PatientRecordTemplate();
        node.setFollowId(followId);
        node.setRequiredForms(requiredForms);
        node.setOptionalForms(optionalForms);
        node.setUpdateTime(new Date());
        patientRecordTemplateMapper.updatePatientRecordTemplate(node);
        return 1;
    }

    /**
     * 获取基线节点
     */
    @Override
    public PatientRecordTemplate selectBaselineNode(Long researchId)
    {
        List<PatientRecordTemplate> nodes = patientRecordTemplateMapper.selectPatientRecordTemplateByResearchId(researchId);
        if (nodes != null) {
            for (PatientRecordTemplate node : nodes) {
                if (node.getIsBaseline() != null && node.getIsBaseline() == 1) {
                    return node;
                }
            }
        }
        return null;
    }
}
