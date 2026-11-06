package com.dchealth.medical.domain.vo;

import com.dchealth.research.domain.ResearchDiseases;
import com.dchealth.research.domain.ResearchInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 评价管理
 */
@Data
public class EvaluateTree {

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
     * 1随访处理评价/3 超窗 4预警处理评价
     */
    private String nodeType;

    /**
     * * 1质效随访处理评价 2 app患者评价 /3质效超窗 4质效预警处理评价
     */
    private String belongType;

    /**
     * 1待评价2已完成
     */
    private String statusType;

    /**
     * 队列id
     */
    private Long researchId;

    /**
     * 处理数量
     */
    private Integer handleNum;

    private List<EvaluateTree> childrenList;

    public EvaluateTree(){}

    private EvaluateTree(String nodeId, String parentNodeId, String belongType, String nodeName,
                         String nodeType, String statusType, Long researchId, Integer handleNum){

        this.nodeId = nodeId;
        this.parentNodeId = parentNodeId;
        this.belongType = belongType;
        this.nodeName = nodeName;
        this.nodeType = nodeType;
        this.statusType = statusType;
        this.researchId = researchId;
        this.handleNum = handleNum;
    }

    private EvaluateTree(String parentNodeId, ResearchDiseases diseases, String belongType){
        this.nodeId = parentNodeId + "_" + diseases.getDiseaseId().longValue();
        this.parentNodeId = parentNodeId;
        this.nodeName = diseases.getDiseaseName();
        this.nodeType = "3";
        this.belongType = belongType;
    }

//    private EvaluateTree(String parentNodeId, ResearchInfo researchInfo, String belongType){
//        this.nodeId = parentNodeId + "_" + researchInfo.getId().longValue();
//        this.parentNodeId = parentNodeId;
//        this.nodeName = researchInfo.getProjectName();
//        this.nodeType = "3";
//        this.researchId = researchInfo.getId();
//        this.belongType = belongType;
//        if ("1".equals(belongType)){
//            this.handleNum = researchInfo.getEvaluateBackPendingNum() + researchInfo.getWarnEvaluateBackPendingNum() + researchInfo.getOverEvaluateBackPendingNum();
//        }else if ("2".equals(belongType)){
//            this.handleNum = researchInfo.getEvaluatePendingNum();
//        }
//
//    }

    private EvaluateTree(String parentNodeId, ResearchInfo researchInfo, String belongType,String type){
        this.nodeId = parentNodeId + "_" + researchInfo.getId().longValue();
        this.parentNodeId = parentNodeId;
        this.nodeName = researchInfo.getProjectName();
        this.nodeType = "3";
        this.researchId = researchInfo.getId();
        this.belongType = belongType;
        if ("1".equals(belongType)){
            if ("1".equals(type)){
                this.handleNum = researchInfo.getEvaluateBackPendingNum() + researchInfo.getWarnEvaluateBackPendingNum() + researchInfo.getOverEvaluateBackPendingNum();
            }
            if ("2".equals(type)){
                this.handleNum = researchInfo.getEvaluatePendingNum();
            }

        }else if ("2".equals(belongType)){
            this.handleNum = researchInfo.getEvaluatePendingNum();
        }

    }


    public EvaluateTree(List<ResearchDiseases> researchDiseases, List<ResearchInfo> researchInfos,String type){
        if (researchDiseases == null || researchDiseases.isEmpty()){
            return;
        }

        Map<Long, List<ResearchInfo>> researchMap = null;
        if (researchInfos != null && !researchInfos.isEmpty()){
            researchMap = researchInfos.stream().collect(Collectors.groupingBy(ResearchInfo :: getDiseaseTypeId));
        }
        final Map<Long, List<ResearchInfo>> map = researchMap;

        List<EvaluateTree> level3 = researchDiseases.stream().map(x->{
            EvaluateTree evaluateTree = new EvaluateTree("0", x, type);
            List<ResearchInfo> researchInfoList = null;
            if (map != null){
                researchInfoList = map.get(x.getDiseaseId());
                if (researchInfoList != null && !researchInfoList.isEmpty()){
                    List<EvaluateTree> children = researchInfoList.stream().map(y->{
                        EvaluateTree tree = new EvaluateTree(evaluateTree.getNodeId(), y, evaluateTree.getBelongType(),type);
                        if (tree.getNodeName().contains("唐氏高血压")){
                            System.out.println(tree.getNodeName());
                        }
                        if ("1".equals(type)) {
                            tree.setChildrenList(level2(tree.getNodeId(), y, evaluateTree.getBelongType()));
                        }
                        if ("2".equals(type)){
                            tree.setChildrenList(level5(tree.getNodeId(), y, evaluateTree.getBelongType()));
                        }
                        return tree;
                    }).collect(Collectors.toList());
                    evaluateTree.setChildrenList(children);
                    int sum = children.stream().mapToInt(EvaluateTree :: getHandleNum).sum();
                    evaluateTree.setHandleNum(sum);
                }
            }
            return evaluateTree;
        }).collect(Collectors.toList());

        this.childrenList = level3;

    }




    private List<EvaluateTree> level5(String parentNodeId, ResearchInfo researchInfo, String belongType){
        List<EvaluateTree> evaluateTrees = new ArrayList<>();
        EvaluateTree evaluateTree1 = new EvaluateTree(parentNodeId + "_1", parentNodeId, belongType, "待评价", "1", "5", researchInfo.getId(), "1".equals(belongType) ? researchInfo.getEvaluateBackPendingNum() : researchInfo.getEvaluatePendingNum());
        EvaluateTree evaluateTree2 = new EvaluateTree(parentNodeId + "_2", parentNodeId, belongType, "已评价", "2", "5", researchInfo.getId(), "1".equals(belongType) ? researchInfo.getEvaluateBackCompletedNum() : researchInfo.getEvaluateCompletedNum());
        evaluateTrees.add(evaluateTree1);
        evaluateTrees.add(evaluateTree2);
        return evaluateTrees;
    }


    private List<EvaluateTree> level2(String parentNodeId, ResearchInfo researchInfo, String belongType){
        List<EvaluateTree> evaluateTrees = new ArrayList<>();
        EvaluateTree evaluateTree1 = new EvaluateTree(parentNodeId + "_1", parentNodeId, belongType, "随访处理", "1", "5",  researchInfo.getId(),  researchInfo.getEvaluateBackPendingNum() != null ? researchInfo.getEvaluateBackPendingNum() : 0);
        evaluateTree1.setChildrenList(level5(evaluateTree1.getNodeId(), researchInfo, belongType));
        EvaluateTree evaluateTree2 = new EvaluateTree(parentNodeId + "_2", parentNodeId, belongType, "随访超窗", "3", "5",  researchInfo.getId(),  researchInfo.getOverEvaluateBackPendingNum()  != null ? researchInfo.getOverEvaluateBackPendingNum() : 0);
        evaluateTree2.setChildrenList(level5(evaluateTree2.getNodeId(), researchInfo,belongType));
        EvaluateTree evaluateTree3 = new EvaluateTree(parentNodeId + "_3", parentNodeId, belongType, "预警处理", "4", "5",  researchInfo.getId(),  researchInfo.getWarnEvaluateBackPendingNum()  != null ? researchInfo.getWarnEvaluateBackPendingNum(): 0);
        evaluateTree3.setChildrenList(level5(evaluateTree3.getNodeId(), researchInfo, belongType));
        evaluateTrees.add(evaluateTree1);
        evaluateTrees.add(evaluateTree2);
        evaluateTrees.add(evaluateTree3);
        return evaluateTrees;
    }








}
