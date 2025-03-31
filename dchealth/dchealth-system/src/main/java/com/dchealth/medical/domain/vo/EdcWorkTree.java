package com.dchealth.medical.domain.vo;

import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.research.domain.vo.ResearchDiseasesVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 随访工作站任务树
 */
@Data
public class EdcWorkTree {

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
     * 0根节点1病种2队列3任务状态
     */
    private String nodeType;

    /**
     * 0无状态1待处理2进行中3已完成
     */
    private String nodeTypeStatus;

    /**
     * 队列id
     */
    private Long researchId;

    /**
     * 处理数量
     */
    private Integer handleNum;

    /**
     * 子节点
     */
    private List<EdcWorkTree> childrenList;

    public EdcWorkTree(){}

    /**
     * edc工作台任务列表树结构
     * @param researchDiseasesList
     * @param patientRecords
     */
    public EdcWorkTree(List<ResearchDiseasesVO> researchDiseasesList, List<PatientRecord> patientRecords) {
        this.setNodeId("0");
        this.setParentNodeId("0");
        this.setNodeName("任务列表");
        this.setNodeType("0");
        this.setNodeTypeStatus("0");
        List<EdcWorkTree> dis = new ArrayList();
        Map<Long, List<ResearchDiseasesVO>> map = researchDiseasesList.stream().filter(researchDiseases -> researchDiseases.getDiseaseId() != null).collect(Collectors.groupingBy(ResearchDiseasesVO::getDiseaseId));
        if(map != null && !map.isEmpty()){
            map.forEach((k,v) -> {
                if(v != null && !v.isEmpty()){
                    //病种类
                    ResearchDiseasesVO rd = v.get(0);
                    EdcWorkTree rdTree = new EdcWorkTree();
                    rdTree.setId(rd.getDiseaseId());
                    rdTree.setNodeId("rd_" + rd.getDiseaseId());
                    rdTree.setParentNodeId(this.nodeId);
                    rdTree.setNodeName(rd.getDiseaseName());
                    rdTree.setNodeType("1");
                    rdTree.setNodeTypeStatus("0");
                    List<EdcWorkTree> researchList = new ArrayList();
                    for (ResearchDiseasesVO rds : v) {
                        if(rds != null){
                            //队列类
                            EdcWorkTree reTree = new EdcWorkTree();
                            reTree.setId(rds.getResearchId());
                            reTree.setNodeId("ri_" + rds.getResearchId());
                            reTree.setParentNodeId("rd_" + rds.getDiseaseId());
                            reTree.setNodeName(rds.getProjectName());
                            reTree.setNodeType("2");
                            reTree.setNodeTypeStatus("0");
                            //待处理任务数量统计
                            if(patientRecords != null && !patientRecords.isEmpty()){
                                Map<Long, List<PatientRecord>> reMap = patientRecords.stream().filter(patientRecord -> patientRecord.getResearchId() != null).collect(Collectors.groupingBy(PatientRecord::getResearchId));
                                if(reMap != null && reMap.containsKey(rds.getResearchId())){
                                    List<PatientRecord> records = reMap.get(rds.getResearchId());
                                    reTree.setHandleNum(records.size());
                                }
                            }
                            //任务三个状态（1待处理、2进行中、3已完成）
                            reTree.setChildrenList(statusList(rds.getResearchId()));
                            researchList.add(reTree);
                        }
                    }
                    int sum = researchList.stream().filter(list -> list.getHandleNum() != null).mapToInt(EdcWorkTree::getHandleNum).sum();
                    rdTree.setHandleNum(sum);
                    rdTree.setChildrenList(researchList);
                    dis.add(rdTree);
                }
            });
        }
        this.childrenList = dis;
    }

    /**
     * 构建edc任务树队列任务四个状态（固定展示）
     * @param researchId
     * @return
     */
    public List<EdcWorkTree> statusList(Long researchId){
        List<EdcWorkTree> treeList = new ArrayList();
        for (int i = 1; i < 5; i++) {
            EdcWorkTree tree = new EdcWorkTree();
            tree.setId(Long.parseLong(i + ""));
            tree.setNodeId("st_" + researchId);
            tree.setParentNodeId("ri_" + researchId);
            switch (i){
                case 1 :
                    tree.setNodeName("待提交");
                    break;
                case 2 :
                    tree.setNodeName("待质检");
                    break;
                case 3 :
                    tree.setNodeName("质疑待回复");
                    break;
                case 4 :
                    tree.setNodeName("已锁库");
                    break;
                default :
                    break;
            }
            tree.setNodeType("3");
            tree.setNodeTypeStatus(i + "");
            tree.setResearchId(researchId);
            treeList.add(tree);
        }
        return treeList;
    }

}
