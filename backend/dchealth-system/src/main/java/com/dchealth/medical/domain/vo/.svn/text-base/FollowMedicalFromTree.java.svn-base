package com.dchealth.medical.domain.vo;

import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.PatientRecordTemplate;
import com.dchealth.research.domain.vo.ResearchDiseasesVO;
import com.dchealth.template.domain.FMedicalForm;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 随访模板表单任务树
 */
@Data
public class FollowMedicalFromTree {

    private Long id;

    /**
     * 节点ID
     */
    private String nodeId;

    /**
     * 父节点ID
     */
    private String parentNodeId;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 0根节点(随访节点)1模板节点
     */
    private String nodeType;

    /**
     * 队列id
     */
    private Long researchId;

    /**
     * 处理数量
     */
    private Long patientId;

    /**
     * 随访节点id
     */
    private Long followId;

    /**
     * 子节点
     */
    private List<FollowMedicalFromTree> childrenList;

    public FollowMedicalFromTree(){}

    /**
     * edc工作台档案详情树结构
     * @param template
     * @param fMedicalForms
     * @param patientId
     */
    public FollowMedicalFromTree(PatientRecordTemplate template, Optional<List<FMedicalForm>> fMedicalForms, Long patientId) {
        //父节点-随访周期
        if (template != null){
            this.setId(template.getFollowId());
            this.setNodeId("fl_" + template.getFollowId());
            this.setParentNodeId("0");
            this.setNodeName(template.getFollowName());
            this.setFollowId(template.getFollowId());
            this.setPatientId(patientId);
            this.setNodeType("0");
            this.setResearchId(template.getResearchId());
            List<FollowMedicalFromTree> fromTrees = new ArrayList();
            //子节点-模板信息
            if(fMedicalForms.isPresent()){
                //获取根节点parentId=0的数据
                List<FMedicalForm> list = fMedicalForms.get();
                List<FMedicalForm> roots = list.stream().filter(f->f.getParentId().equals(0l)).collect(Collectors.toList());
                roots.forEach(fMedicalForm -> {
                    FollowMedicalFromTree fm = new FollowMedicalFromTree();
                    fm.setId(fMedicalForm.getMedicalFormId());
                    fm.setNodeId("mf_" + fMedicalForm.getMedicalFormId());
                    fm.setParentNodeId("fl_" + template.getFollowId());
                    fm.setNodeName(fMedicalForm.getMedicalName());
                    fm.setFollowId(template.getFollowId());
                    fm.setPatientId(patientId);
                    fm.setNodeType("1");
                    fm.setResearchId(template.getResearchId());
                    fromTrees.add(fm);
                });
            }
            this.childrenList = fromTrees;
        }
    }

}
