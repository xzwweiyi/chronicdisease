package com.dchealth.pathway.service.impl;

import com.dchealth.common.utils.DateUtils;
import com.dchealth.medical.domain.PatientRecordTemplate;
import com.dchealth.medical.mapper.PatientRecordTemplateMapper;
import com.dchealth.pathway.service.IPathwayNodeService;
import com.dchealth.pathway.service.IPathwayService;
import com.dchealth.research.domain.ResearchInfo;
import com.dchealth.research.mapper.ResearchInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 管理路径Service业务层处理
 *
 * @author xuzw
 * @date 2026-06-30
 */
@Service
public class PathwayServiceImpl implements IPathwayService
{
    @Autowired
    private ResearchInfoMapper researchInfoMapper;

    @Autowired
    private PatientRecordTemplateMapper patientRecordTemplateMapper;

    @Autowired
    private IPathwayNodeService pathwayNodeService;

    /**
     * 查询路径列表
     */
    @Override
    public List<ResearchInfo> selectPathwayList(ResearchInfo pathway)
    {
        if (pathway == null) {
            pathway = new ResearchInfo();
        }
        pathway.setSystemType("2");
        return researchInfoMapper.selectResearchInfoList(pathway);
    }

    /**
     * 根据ID查询路径详情
     */
    @Override
    public ResearchInfo selectPathwayById(Long id)
    {
        ResearchInfo pathway = researchInfoMapper.selectResearchInfoById(id);
        if (pathway != null && !"2".equals(pathway.getSystemType())) {
            return null;
        }
        return pathway;
    }

    /**
     * 新增路径
     */
    @Override
    @Transactional
    public Long insertPathway(ResearchInfo pathway)
    {
        pathway.setSystemType("2");
        pathway.setPathwayStatus("0"); // 草稿
        pathway.setDelFlag("0");
        pathway.setPatientNum(0L);
        pathway.setHospitalNum(0L);
        pathway.setStage("0");
        pathway.setCreateTime(new Date());
        researchInfoMapper.insertResearchInfo(pathway);

        Long researchId = pathway.getId();

        // 创建基线节点
        PatientRecordTemplate baseline = new PatientRecordTemplate();
        baseline.setResearchId(researchId);
        baseline.setFollowNum(0);
        baseline.setFollowName("基线");
        baseline.setNodeType("follow_up");
        baseline.setIsBaseline(1);
        baseline.setNodeOrder(0);
        baseline.setFollowInterval(0);
        baseline.setFollowIntervalUnit("1");
        baseline.setFollowFrequency("1");
        baseline.setFollowTotal(1);
        baseline.setCreateTime(new Date());
        pathwayNodeService.insertNode(baseline);

        return researchId;
    }

    /**
     * 修改路径
     */
    @Override
    public int updatePathway(ResearchInfo pathway)
    {
        pathway.setSystemType("2");
        pathway.setUpdateTime(new Date());
        return researchInfoMapper.updateResearchInfo(pathway);
    }

    /**
     * 删除路径（逻辑删除）
     */
    @Override
    public int deletePathwayById(Long id)
    {
        ResearchInfo pathway = new ResearchInfo();
        pathway.setId(id);
        pathway.setDelFlag("1");
        pathway.setUpdateTime(new Date());
        return researchInfoMapper.updateResearchInfo(pathway);
    }

    /**
     * 更新路径状态
     */
    @Override
    public int updatePathwayStatus(Long id, String pathwayStatus)
    {
        ResearchInfo pathway = new ResearchInfo();
        pathway.setId(id);
        pathway.setPathwayStatus(pathwayStatus);
        pathway.setUpdateTime(new Date());
        if ("1".equals(pathwayStatus)) {
            pathway.setPublicTime(new Date());
        }
        return researchInfoMapper.updateResearchInfo(pathway);
    }

    /**
     * 复制路径
     */
    @Override
    @Transactional
    public Long copyPathway(Long sourceId)
    {
        ResearchInfo source = selectPathwayById(sourceId);
        if (source == null) {
            return null;
        }

        // 复制路径基本信息
        ResearchInfo copy = new ResearchInfo();
        copy.setProjectName(source.getProjectName() + "（副本）");
        copy.setResearchSynopsis(source.getResearchSynopsis());
        copy.setDiseaseTypeId(source.getDiseaseTypeId());
        copy.setDeptId(source.getDeptId());
        copy.setDoctorId(source.getDoctorId());
        copy.setImageUrl(source.getImageUrl());
        copy.setSystemType("2");
        copy.setPathwayStatus("0"); // 草稿
        copy.setPathwayStageConfig(source.getPathwayStageConfig());
        copy.setBaselineConfig(source.getBaselineConfig());
        copy.setAssessmentConfig(source.getAssessmentConfig());
        copy.setExitCriteria(source.getExitCriteria());
        copy.setTargetPatientNum(source.getTargetPatientNum());
        copy.setDelFlag("0");
        copy.setPatientNum(0L);
        copy.setHospitalNum(0L);
        copy.setStage("0");
        copy.setCreateTime(new Date());
        researchInfoMapper.insertResearchInfo(copy);

        Long newId = copy.getId();

        // 复制节点
        List<PatientRecordTemplate> nodes = pathwayNodeService.selectNodesByPathwayId(sourceId);
        if (nodes != null) {
            for (PatientRecordTemplate node : nodes) {
                PatientRecordTemplate newNode = new PatientRecordTemplate();
                newNode.setResearchId(newId);
                newNode.setFollowNum(node.getFollowNum());
                newNode.setFollowName(node.getFollowName());
                newNode.setNodeType(node.getNodeType());
                newNode.setNodeOrder(node.getNodeOrder());
                newNode.setIsBaseline(node.getIsBaseline());
                newNode.setFollowInterval(node.getFollowInterval());
                newNode.setFollowIntervalUnit(node.getFollowIntervalUnit());
                newNode.setFollowFrequency(node.getFollowFrequency());
                newNode.setFollowTotal(node.getFollowTotal());
                newNode.setNodeConfig(node.getNodeConfig());
                newNode.setRequiredForms(node.getRequiredForms());
                newNode.setOptionalForms(node.getOptionalForms());
                newNode.setReminderAdvanceDays(node.getReminderAdvanceDays());
                newNode.setReminderTemplateId(node.getReminderTemplateId());
                newNode.setCreateTime(new Date());
                pathwayNodeService.insertNode(newNode);
            }
        }

        return newId;
    }
}
