package com.dchealth.medical.domain.vo;

import com.dchealth.research.domain.ResearchDiseases;
import com.dchealth.research.domain.ResearchInfo;
import lombok.Data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 随访工作站任务树
 */
@Data
public class FollowWorkTree {

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
     * 0根节点1病种/待分配患者2队列/任务类型状态3任务类型4任务类型状态
     */
    private String nodeLevel;

    /**
     * 层级类型
     * 0根节点1病种2待分配患者
     */
    private String nodeLevelType;

    /**
     * 节点类型
     * 0无类型1患者入组审核2随访处理3随访超窗4预警处理
     */
    private String nodeType;

    /**
     * 0无状态1待处理2进行中3已完成4已评价5未完成（基线未填写，查询转换为0）
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
    private List<FollowWorkTree> childrenList;

    public FollowWorkTree() {
    }

    public FollowWorkTree(String parentNodeId, ResearchDiseases researchDiseases) {
        this.nodeId = parentNodeId + "_" + researchDiseases.getDiseaseId().longValue();
        this.parentNodeId = parentNodeId;
        this.nodeName = researchDiseases.getDiseaseName();
        this.nodeLevel = "1";
        this.nodeLevelType = "1";
        this.nodeType = "0";
        this.nodeTypeStatus = "0";
    }

    public FollowWorkTree(String parentNodeId, ResearchInfo researchInfo, Map<String, List<FollowWorkListVO>> mapInfo) {
        this.nodeId = parentNodeId + "_" + researchInfo.getId().longValue();
        this.parentNodeId = parentNodeId;
        this.nodeName = researchInfo.getProjectName();
        this.nodeLevel = "2";
        this.nodeLevelType = "1";
        this.nodeType = "0";
        this.nodeTypeStatus = "0";
        List<FollowWorkTree> nodeLevel3 = createNodeLevel3(this.nodeId, researchInfo.getId(), mapInfo, researchInfo.getResearchStatus());
        this.childrenList = nodeLevel3;
        int sum = nodeLevel3.stream().filter(vo -> vo.getHandleNum() != null).mapToInt(FollowWorkTree::getHandleNum).sum();
        this.setHandleNum(sum);
    }

//    public FollowWorkTree(List<ResearchDiseases> researchDiseases, List<ResearchInfo> researchInfos, Map<String,List<FollowWorkListVO>> mapInfo){
//        this.setNodeId("0");
//        this.setParentNodeId("0");
//        this.setNodeName("随访任务");
//        this.setNodeLevel("0");
//        this.setNodeLevelType("0");
//        this.setNodeType("0");
//        this.setNodeTypeStatus("0");
//
//        Map<Long, List<ResearchInfo>> researchMap = null;
//        if (researchInfos != null && !researchInfos.isEmpty()){
//            researchMap = researchInfos.stream().collect(Collectors.groupingBy(ResearchInfo :: getDiseaseTypeId));
//        }
//        final Map<Long, List<ResearchInfo>> map = researchMap;
//        if (researchDiseases != null && !researchDiseases.isEmpty()){
//            List<FollowWorkTree> level2 = researchDiseases.stream().map(x->{
//                FollowWorkTree followWorkTree = new FollowWorkTree(this.getNodeId(), x);
//                List<ResearchInfo> researchInfoList = null;
//                if (map != null){
//                    researchInfoList = map.get(x.getDiseaseId());
//                    if (researchInfoList != null && !researchInfoList.isEmpty()){
//                        List<FollowWorkTree> children = researchInfoList.stream().map(y->new FollowWorkTree(followWorkTree.getNodeId(), y, mapInfo)).collect(Collectors.toList());
//                        followWorkTree.setChildrenList(children);
//                        int sum = children.stream().filter(vo ->vo.getHandleNum() != null).mapToInt(FollowWorkTree::getHandleNum).sum();
//                        followWorkTree.setHandleNum(sum);
//                    }
//                }
//                return followWorkTree;
//            }).collect(Collectors.toList());
//            this.childrenList = level2;
//        }

    /// /        FollowWorkTree pend = new FollowWorkTree();
    /// /        pend.setNodeId(this.nodeId+"_01");
    /// /        pend.setParentNodeId(this.nodeId);
    /// /        pend.setNodeName("待分配患者");
    /// /        pend.setNodeLevel("1");
    /// /        pend.setNodeLevelType("2");
    /// /        pend.setNodeType("0");
    /// /        pend.setNodeTypeStatus("0");
    /// /        pend.setChildrenList(createNodeLevel4(pend.getNodeId(),"2", "2", "0", new String[]{"1","3"}, null));
    /// /        if (this.childrenList == null){
    /// /            this.childrenList = new ArrayList<>();
    /// /        }
    /// /        //待分配患者数量-待处理
    /// /        List<FollowWorkListVO> allocated = mapInfo.get("allocated");
    /// /        pend.setHandleNum(allocated != null ? allocated.size() : null);
    /// /        this.childrenList.add(pend);
//    }

// 优化树构建的构造函数
    public FollowWorkTree(List<ResearchDiseases> researchDiseases,
                          List<ResearchInfo> researchInfos,
                          Map<String, List<FollowWorkListVO>> mapInfo) {

        // 基础属性设置
        this.setNodeId("0");
        this.setParentNodeId("0");
        this.setNodeName("随访任务");
        this.setNodeLevel("0");
        this.setNodeLevelType("0");
        this.setNodeType("0");
        this.setNodeTypeStatus("0");

        // 提前处理researchInfos分组，使用ConcurrentHashMap提高并发性能
        final Map<Long, List<ResearchInfo>> researchMap = researchInfos.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingByConcurrent(ResearchInfo::getDiseaseTypeId));

        // 优化疾病节点处理
        this.childrenList = researchDiseases.stream()
                .filter(Objects::nonNull)
                .map(disease -> createDiseaseNode(disease, researchMap, mapInfo))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // 优化疾病节点创建方法
    private FollowWorkTree createDiseaseNode(ResearchDiseases disease,
                                             Map<Long, List<ResearchInfo>> researchMap,
                                             Map<String, List<FollowWorkListVO>> mapInfo) {

        FollowWorkTree diseaseNode = new FollowWorkTree(this.getNodeId(), disease);

        List<ResearchInfo> researchInfoList = researchMap.getOrDefault(disease.getDiseaseId(), Collections.emptyList());

        if (!researchInfoList.isEmpty()) {
            // 根据数据量决定是否使用并行流
            Stream<ResearchInfo> infoStream = researchInfoList.size() > 100 ?
                    researchInfoList.parallelStream() : researchInfoList.stream();

            List<FollowWorkTree> children = infoStream
                    .map(info -> new FollowWorkTree(diseaseNode.getNodeId(), info, mapInfo))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            diseaseNode.setChildrenList(children);

            // 使用更高效的方式计算总和
            int handleNum = 0;
            for (FollowWorkTree node : children) {
                if (node.getHandleNum() != null) {
                    handleNum += node.getHandleNum();
                }
            }
            diseaseNode.setHandleNum(handleNum);
        }
        return diseaseNode;
    }


    /**
     * 构造函数
     *
     * @param parentNodeId
     * @param nodeLevel
     * @param nodeType
     */
    public FollowWorkTree(String parentNodeId, String nodeLevel, String nodeLevelType, String nodeType, String nodeTypeStatus, Long researchId, Map<String, List<FollowWorkListVO>> mapInfo) {
        if ("3".equals(nodeLevel)) {
            this.nodeId = parentNodeId + "_" + nodeType;
        } else if ("2".equals(nodeLevel) || "4".equals(nodeLevel)) {
            this.nodeId = parentNodeId + "_" + nodeTypeStatus;
        }
        this.parentNodeId = parentNodeId;
        this.nodeLevel = nodeLevel;
        this.nodeLevelType = nodeLevelType;
        this.nodeType = nodeType;
        this.researchId = researchId;
        if ("3".equals(nodeLevel)) {
            List<FollowWorkListVO> vos = null;
            if ("1".equals(nodeType)) {
                this.nodeName = "患者入组审核";
                vos = mapInfo.get("follow");
            } else if ("2".equals(nodeType)) {
                this.nodeName = "随访处理";
                vos = mapInfo.get("handle");
            } else if ("3".equals(nodeType)) {
                this.nodeName = "随访超窗";
                vos = mapInfo.get("over");
            } else if ("4".equals(nodeType)) {
                this.nodeName = "预警处理";
                vos = mapInfo.get("warn");
            } else if ("5".equals(nodeType)) {
                this.nodeName = "基线";
                vos = mapInfo.get("baseline");
            } else if ("6".equals(nodeType)) {
                this.nodeName = "患者退出列表审核";
                vos = mapInfo.get("logoutAudit");
            }
            this.nodeTypeStatus = "0";
            if (vos != null && !vos.isEmpty()) {
                long count = vos.stream().filter(vo -> String.valueOf(vo.getResearchId()).equals(String.valueOf(researchId))).count();
                this.handleNum = Integer.valueOf(String.valueOf(count));
            }
        } else if ("4".equals(nodeLevel) || "2".equals(nodeLevel)) {
            this.nodeTypeStatus = nodeTypeStatus;
            if ("1".equals(nodeTypeStatus)) {
                this.nodeName = "待处理";
            } else if ("2".equals(nodeTypeStatus)) {
                this.nodeName = "进行中";
            } else if ("3".equals(nodeTypeStatus)) {
                this.nodeName = "已完成";
            } else if ("4".equals(nodeTypeStatus)) {
                this.nodeName = "已评价";
            }

            //基线状态特殊处理
            if ("5".equals(nodeType)) {
                if ("5".equals(nodeTypeStatus)) {
                    this.nodeName = "未完成";
                } else if ("1".equals(nodeTypeStatus)) {
                    this.nodeName = "已完成";
                }
            }
        }
    }

    public FollowWorkTree setChildrenList(List<FollowWorkTree> childrenList) {
        this.childrenList = childrenList;
        return this;
    }

    public FollowWorkTree setChildrenListData(String[] nodeTypeStatus) {
        this.childrenList = createNodeLevel4(this.nodeId, "4", "1", this.nodeType, nodeTypeStatus, this.researchId);
        return this;
    }

    /**
     * 任务类型
     *
     * @param parentNodeId
     * @return
     */
    private List<FollowWorkTree> createNodeLevel3(String parentNodeId, Long researchId, Map<String, List<FollowWorkListVO>> mapInfo, String researchStatus) {
        List<FollowWorkTree> followWorkTrees = new ArrayList<FollowWorkTree>();
        if ("1".equals(researchStatus)) {
            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "1", "0", researchId, mapInfo)
                    .setChildrenListData(new String[]{"1", "2", "3"}));
            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "5", "0", researchId, mapInfo)
                    .setChildrenListData(new String[]{"5", "1"}));
            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "2", "0", researchId, mapInfo)
                    .setChildrenListData(new String[]{"1", "2", "3", "4"}));
            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "3", "0", researchId, mapInfo)
                    .setChildrenListData(new String[]{"1", "3", "4"}));
            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "4", "0", researchId, mapInfo)
                    .setChildrenListData(new String[]{"1", "2", "3", "4"}));
            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "6", "0", researchId, mapInfo)
                    .setChildrenListData(new String[]{"1", "3"}));
        } else {
            //未发布随访  允许进行患者入组审核
            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "1", "0", researchId, mapInfo)
                    .setChildrenListData(new String[]{"1", "2", "3"}));
            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "5", "0", researchId, mapInfo)
                    .setChildrenListData(new String[]{"5", "1"}));
        }

        return followWorkTrees;
    }

    /**
     * 任务类型状态
     *
     * @param parentNodeId
     * @param nodeLevel
     * @param nodeLevelType
     * @return
     */
    private List<FollowWorkTree> createNodeLevel4(String parentNodeId, String nodeLevel, String nodeLevelType, String nodeType, String[] nodeTypeStatus, Long researchId) {
        List<FollowWorkTree> followWorkTrees = new ArrayList<FollowWorkTree>();
        for (String x : nodeTypeStatus) {
            followWorkTrees.add(new FollowWorkTree(parentNodeId, nodeLevel, nodeLevelType, nodeType, x, researchId, null));
        }
        return followWorkTrees;
    }


}
