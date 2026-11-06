/*
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

*/
/**
 * 随访工作站任务树
 *//*

@Data
public class FollowWorkTree {

    */
/**
     * 节点ID
     *//*

    private String nodeId;

    */
/**
     * 父节点ID
     *//*

    private String parentNodeId;

    */
/**
     * 节点名称
     *//*

    private String nodeName;

    */
/**
     * 0根节点1病种/待分配患者2队列/任务类型状态3任务类型4任务类型状态
     *//*

    private String nodeLevel;

    */
/**
     * 层级类型
     * 0根节点1病种2待分配患者
     *//*

    private String nodeLevelType;

    */
/**
     * 节点类型
     * 0无类型1患者入组审核2随访处理3随访超窗4预警处理
     *//*

    private String nodeType;

    */
/**
     * 0无状态1待处理2进行中3已完成4已评价5未完成（基线未填写，查询转换为0）
     *//*

    private String nodeTypeStatus;

    */
/**
     * 队列id
     *//*

    private Long researchId;

    */
/**
     * 处理数量
     *//*

    private Integer handleNum;

    */
/**
     * 子节点
     *//*

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


    */
/**
     * 构造函数
     *
     * @param parentNodeId
     * @param nodeLevel
     * @param nodeType
     *//*

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

    */
/**
     * 任务类型
     *
     * @param parentNodeId
     * @return
     *//*

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

    */
/**
     * 任务类型状态
     *
     * @param parentNodeId
     * @param nodeLevel
     * @param nodeLevelType
     * @return
     *//*

    private List<FollowWorkTree> createNodeLevel4(String parentNodeId, String nodeLevel, String nodeLevelType, String nodeType, String[] nodeTypeStatus, Long researchId) {
        List<FollowWorkTree> followWorkTrees = new ArrayList<FollowWorkTree>();
        for (String x : nodeTypeStatus) {
            followWorkTrees.add(new FollowWorkTree(parentNodeId, nodeLevel, nodeLevelType, nodeType, x, researchId, null));
        }
        return followWorkTrees;
    }


}
*/

////lys
//package com.dchealth.medical.domain.vo;
//
//import com.dchealth.research.domain.ResearchDiseases;
//import com.dchealth.research.domain.ResearchInfo;
//import lombok.Data;
//
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//import java.util.function.Function;
//import java.util.function.Predicate;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
///**
// * 随访工作站任务树
// */
//@Data
//public class FollowWorkTree {
//
//    /**
//     * 节点ID
//     */
//    private String nodeId;
//
//    /**
//     * 父节点ID
//     */
//    private String parentNodeId;
//
//    /**
//     * 节点名称
//     */
//    private String nodeName;
//
//    /**
//     * 0根节点1病种/待分配患者2队列/任务类型状态3任务类型4任务类型状态
//     */
//    private String nodeLevel;
//
//    /**
//     * 层级类型
//     * 0根节点1病种2待分配患者
//     */
//    private String nodeLevelType;
//
//    /**
//     * 节点类型
//     * 0无类型1患者入组审核2随访处理3随访超窗4预警处理
//     */
//    private String nodeType;
//
//    /**
//     * 0无状态1待处理2进行中3已完成4已评价5未完成（基线未填写，查询转换为0）
//     */
//    private String nodeTypeStatus;
//
//    /**
//     * 队列id
//     */
//    private Long researchId;
//
//    /**
//     * 处理数量
//     */
//    private Integer handleNum;
//
//    /**
//     * 子节点
//     */
//    private List<FollowWorkTree> childrenList;
//
//    /**
//     * 是否叶子节点
//     */
//    private Boolean leaf;
//
//    /**
//     * 是否已加载子节点（用于前端判断）
//     */
//    private Boolean childrenLoaded = false;
//
//    public FollowWorkTree() {
//    }
//
//    public FollowWorkTree(String parentNodeId, ResearchDiseases researchDiseases) {
//        this.nodeId = parentNodeId + "_" + researchDiseases.getDiseaseId().longValue();
//        this.parentNodeId = parentNodeId;
//        this.nodeName = researchDiseases.getDiseaseName();
//        this.nodeLevel = "1";
//        this.nodeLevelType = "1";
//        this.nodeType = "0";
//        this.nodeTypeStatus = "0";
//        this.leaf = false; // 疾病节点有子节点
//    }
//
//    public FollowWorkTree(String parentNodeId, ResearchInfo researchInfo, Map<String, List<FollowWorkListVO>> mapInfo) {
//        this.nodeId = parentNodeId + "_" + researchInfo.getId().longValue();
//        this.parentNodeId = parentNodeId;
//        this.nodeName = researchInfo.getProjectName();
//        this.nodeLevel = "2";
//        this.nodeLevelType = "1";
//        this.nodeType = "0";
//        this.nodeTypeStatus = "0";
//
//        if (mapInfo != null && !mapInfo.isEmpty()) {
//            List<FollowWorkTree> nodeLevel3 = createNodeLevel3(this.nodeId, researchInfo.getId(), mapInfo, researchInfo.getResearchStatus());
//            this.childrenList = nodeLevel3;
//            int sum = nodeLevel3.stream().filter(vo -> vo.getHandleNum() != null).mapToInt(FollowWorkTree::getHandleNum).sum();
//            this.setHandleNum(sum);
//            this.leaf = false; // 有任务类型子节点
//        } else {
//            // 懒加载模式，先不设置子节点
//            this.childrenList = new ArrayList<>();
//            this.leaf = false; // 队列节点可能有子节点，需要展开时加载
//        }
//    }
//
//    // 懒加载构造函数 - 只构建结构，不加载详细数据
//    public FollowWorkTree(List<ResearchDiseases> researchDiseases,
//                          List<ResearchInfo> researchInfos,
//                          Map<String, List<FollowWorkListVO>> mapInfo,
//                          boolean lazyLoad) {
//
//        // 基础属性设置
//        this.setNodeId("0");
//        this.setParentNodeId("0");
//        this.setNodeName("随访任务");
//        this.setNodeLevel("0");
//        this.setNodeLevelType("0");
//        this.setNodeType("0");
//        this.setNodeTypeStatus("0");
//        this.setLeaf(false); // 根节点不是叶子节点
//
//        if (lazyLoad) {
//            // 懒加载模式，只设置基础结构，不加载详细数据
//            this.childrenList = new ArrayList<>();
//        } else {
//            // 原有逻辑 - 完整加载
//            final Map<Long, List<ResearchInfo>> researchMap = researchInfos.stream()
//                    .filter(Objects::nonNull)
//                    .collect(Collectors.groupingByConcurrent(ResearchInfo::getDiseaseTypeId));
//
//            this.childrenList = researchDiseases.stream()
//                    .filter(Objects::nonNull)
//                    .map(disease -> createDiseaseNode(disease, researchMap, mapInfo))
//                    .filter(Objects::nonNull)
//                    .collect(Collectors.toList());
//        }
//    }
//
//    // 原有构造函数 - 保持兼容性
//    public FollowWorkTree(List<ResearchDiseases> researchDiseases,
//                          List<ResearchInfo> researchInfos,
//                          Map<String, List<FollowWorkListVO>> mapInfo) {
//        this(researchDiseases, researchInfos, mapInfo, false); // 默认非懒加载
//    }
//
//    // 优化疾病节点创建方法
//    private FollowWorkTree createDiseaseNode(ResearchDiseases disease,
//                                             Map<Long, List<ResearchInfo>> researchMap,
//                                             Map<String, List<FollowWorkListVO>> mapInfo) {
//
//        FollowWorkTree diseaseNode = new FollowWorkTree(this.getNodeId(), disease);
//
//        List<ResearchInfo> researchInfoList = researchMap.getOrDefault(disease.getDiseaseId(), Collections.emptyList());
//
//        if (!researchInfoList.isEmpty()) {
//            // 根据数据量决定是否使用并行流
//            Stream<ResearchInfo> infoStream = researchInfoList.size() > 100 ?
//                    researchInfoList.parallelStream() : researchInfoList.stream();
//
//            List<FollowWorkTree> children = infoStream
//                    .map(info -> new FollowWorkTree(diseaseNode.getNodeId(), info, mapInfo))
//                    .filter(Objects::nonNull)
//                    .collect(Collectors.toList());
//
//            diseaseNode.setChildrenList(children);
//
//            // 使用更高效的方式计算总和
//            int handleNum = 0;
//            for (FollowWorkTree node : children) {
//                if (node.getHandleNum() != null) {
//                    handleNum += node.getHandleNum();
//                }
//            }
//            diseaseNode.setHandleNum(handleNum);
//        }
//        return diseaseNode;
//    }
//
//
//    /**
//     * 构造函数 - 用于任务类型和状态节点
//     *
//     * @param parentNodeId
//     * @param nodeLevel
//     * @param nodeType
//     */
//    public FollowWorkTree(String parentNodeId, String nodeLevel, String nodeLevelType, String nodeType, String nodeTypeStatus, Long researchId, Map<String, List<FollowWorkListVO>> mapInfo) {
//        if ("3".equals(nodeLevel)) {
//            this.nodeId = parentNodeId + "_" + nodeType;
//        } else if ("2".equals(nodeLevel) || "4".equals(nodeLevel)) {
//            this.nodeId = parentNodeId + "_" + nodeTypeStatus;
//        }
//        this.parentNodeId = parentNodeId;
//        this.nodeLevel = nodeLevel;
//        this.nodeLevelType = nodeLevelType;
//        this.nodeType = nodeType;
//        this.researchId = researchId;
//
//        // 设置叶子节点属性
//        this.leaf = "4".equals(nodeLevel); // 状态节点是叶子节点
//
//        if ("3".equals(nodeLevel)) {
//            List<FollowWorkListVO> vos = null;
//            if ("1".equals(nodeType)) {
//                this.nodeName = "患者入组审核";
//                vos = mapInfo.get("follow");
//            } else if ("2".equals(nodeType)) {
//                this.nodeName = "随访处理";
//                vos = mapInfo.get("handle");
//            } else if ("3".equals(nodeType)) {
//                this.nodeName = "随访超窗";
//                vos = mapInfo.get("over");
//            } else if ("4".equals(nodeType)) {
//                this.nodeName = "预警处理";
//                vos = mapInfo.get("warn");
//            } else if ("5".equals(nodeType)) {
//                this.nodeName = "基线";
//                vos = mapInfo.get("baseline");
//            } else if ("6".equals(nodeType)) {
//                this.nodeName = "患者退出列表审核";
//                vos = mapInfo.get("logoutAudit");
//            }
//            this.nodeTypeStatus = "0";
//            if (vos != null && !vos.isEmpty()) {
//                long count = vos.stream().filter(vo -> String.valueOf(vo.getResearchId()).equals(String.valueOf(researchId))).count();
//                this.handleNum = Integer.valueOf(String.valueOf(count));
//            }
//
//            // 任务类型节点有子节点
//            if (mapInfo != null) {
//                this.childrenList = createNodeLevel4(this.nodeId, "4", "1", this.nodeType, getStatusArrayByNodeType(this.nodeType), this.researchId);
//            } else {
//                this.childrenList = new ArrayList<>();
//            }
//        } else if ("4".equals(nodeLevel) || "2".equals(nodeLevel)) {
//            this.nodeTypeStatus = nodeTypeStatus;
//            if ("1".equals(nodeTypeStatus)) {
//                this.nodeName = "待处理";
//            } else if ("2".equals(nodeTypeStatus)) {
//                this.nodeName = "进行中";
//            } else if ("3".equals(nodeTypeStatus)) {
//                this.nodeName = "已完成";
//            } else if ("4".equals(nodeTypeStatus)) {
//                this.nodeName = "已评价";
//            }
//
//            //基线状态特殊处理
//            if ("5".equals(nodeType)) {
//                if ("5".equals(nodeTypeStatus)) {
//                    this.nodeName = "未完成";
//                } else if ("1".equals(nodeTypeStatus)) {
//                    this.nodeName = "已完成";
//                }
//            }
//
//            // 状态节点没有子节点
//            this.childrenList = null;
//        }
//    }
//
//    public FollowWorkTree setChildrenList(List<FollowWorkTree> childrenList) {
//        this.childrenList = childrenList;
//        return this;
//    }
//
//    public FollowWorkTree setChildrenListData(String[] nodeTypeStatus) {
//        this.childrenList = createNodeLevel4(this.nodeId, "4", "1", this.nodeType, nodeTypeStatus, this.researchId);
//        return this;
//    }
//
//    /**
//     * 任务类型
//     *
//     * @param parentNodeId
//     * @return
//     */
//    private List<FollowWorkTree> createNodeLevel3(String parentNodeId, Long researchId, Map<String, List<FollowWorkListVO>> mapInfo, String researchStatus) {
//        List<FollowWorkTree> followWorkTrees = new ArrayList<FollowWorkTree>();
//        if ("1".equals(researchStatus)) {
//            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "1", "0", researchId, mapInfo));
//            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "5", "0", researchId, mapInfo));
//            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "2", "0", researchId, mapInfo));
//            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "3", "0", researchId, mapInfo));
//            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "4", "0", researchId, mapInfo));
//            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "6", "0", researchId, mapInfo));
//        } else {
//            //未发布随访  允许进行患者入组审核
//            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "1", "0", researchId, mapInfo));
//            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "5", "0", researchId, mapInfo));
//        }
//
//        return followWorkTrees;
//    }
//
//    /**
//     * 任务类型状态
//     *
//     * @param parentNodeId
//     * @param nodeLevel
//     * @param nodeLevelType
//     * @return
//     */
//    private List<FollowWorkTree> createNodeLevel4(String parentNodeId, String nodeLevel, String nodeLevelType, String nodeType, String[] nodeTypeStatus, Long researchId) {
//        List<FollowWorkTree> followWorkTrees = new ArrayList<FollowWorkTree>();
//        for (String x : nodeTypeStatus) {
//            FollowWorkTree node = new FollowWorkTree(parentNodeId, nodeLevel, nodeLevelType, nodeType, x, researchId, null);
//            node.setLeaf(true); // 状态节点是叶子节点
//            followWorkTrees.add(node);
//        }
//        return followWorkTrees;
//    }
//
//    /**
//     * 根据任务类型返回对应的状态数组
//     */
//    private String[] getStatusArrayByNodeType(String nodeType) {
//        switch (nodeType) {
//            case "1": return new String[]{"1", "2", "3"};
//            case "2": return new String[]{"1", "2", "3", "4"};
//            case "3": return new String[]{"1", "3", "4"};
//            case "4": return new String[]{"1", "2", "3", "4"};
//            case "5": return new String[]{"5", "1"};
//            case "6": return new String[]{"1", "3"};
//            default: return new String[]{};
//        }
//    }
//
//    /**
//     * 创建疾病节点的子节点（用于懒加载）
//     */
//    public static List<FollowWorkTree> createDiseaseChildren(String parentNodeId, List<ResearchInfo> researchInfos) {
//        return researchInfos.stream()
//                .map(info -> {
//                    FollowWorkTree node = new FollowWorkTree();
//                    node.setNodeId(parentNodeId + "_" + info.getId().longValue());
//                    node.setParentNodeId(parentNodeId);
//                    node.setNodeName(info.getProjectName());
//                    node.setNodeLevel("2");
//                    node.setNodeLevelType("1");
//                    node.setNodeType("0");
//                    node.setNodeTypeStatus("0");
//                    node.setResearchId(info.getId());
//                    node.setLeaf(false); // 队列节点有子节点
//                    node.setChildrenList(new ArrayList<>()); // 初始为空，需要时加载
//                    return node;
//                })
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * 创建队列节点的子节点（用于懒加载）
//     */
//    public static List<FollowWorkTree> createResearchChildren(String parentNodeId, Long researchId, String researchStatus, Map<String, List<FollowWorkListVO>> mapInfo) {
//        List<FollowWorkTree> children = new ArrayList<>();
//
//        if ("1".equals(researchStatus)) {
//            children.add(new FollowWorkTree(parentNodeId, "3", "1", "1", "0", researchId, mapInfo));
//            children.add(new FollowWorkTree(parentNodeId, "3", "1", "5", "0", researchId, mapInfo));
//            children.add(new FollowWorkTree(parentNodeId, "3", "1", "2", "0", researchId, mapInfo));
//            children.add(new FollowWorkTree(parentNodeId, "3", "1", "3", "0", researchId, mapInfo));
//            children.add(new FollowWorkTree(parentNodeId, "3", "1", "4", "0", researchId, mapInfo));
//            children.add(new FollowWorkTree(parentNodeId, "3", "1", "6", "0", researchId, mapInfo));
//        } else {
//            // 未发布随访，只允许患者入组审核和基线
//            children.add(new FollowWorkTree(parentNodeId, "3", "1", "1", "0", researchId, mapInfo));
//            children.add(new FollowWorkTree(parentNodeId, "3", "1", "5", "0", researchId, mapInfo));
//        }
//
//        return children;
//    }
//
//    /**
//     * 创建任务类型节点的子节点（用于懒加载）
//     */
//    public static List<FollowWorkTree> createTaskTypeChildren(String parentNodeId, String nodeType, Long researchId) {
//        String[] statusArray;
//        switch (nodeType) {
//            case "1": statusArray = new String[]{"1", "2", "3"}; break;
//            case "2": statusArray = new String[]{"1", "2", "3", "4"}; break;
//            case "3": statusArray = new String[]{"1", "3", "4"}; break;
//            case "4": statusArray = new String[]{"1", "2", "3", "4"}; break;
//            case "5": statusArray = new String[]{"5", "1"}; break;
//            case "6": statusArray = new String[]{"1", "3"}; break;
//            default: statusArray = new String[]{};
//        }
//
//        List<FollowWorkTree> children = new ArrayList<>();
//        for (String status : statusArray) {
//            FollowWorkTree node = new FollowWorkTree();
//            node.setNodeId(parentNodeId + "_" + status);
//            node.setParentNodeId(parentNodeId);
//            node.setNodeLevel("4");
//            node.setNodeLevelType("1");
//            node.setNodeType(nodeType);
//            node.setNodeTypeStatus(status);
//            node.setResearchId(researchId);
//            node.setLeaf(true); // 状态节点是叶子节点
//
//            // 设置节点名称
//            if ("1".equals(status) && !"5".equals(nodeType)) {
//                node.setNodeName("待处理");
//            } else if ("2".equals(status)) {
//                node.setNodeName("进行中");
//            } else if ("3".equals(status)) {
//                node.setNodeName("已完成");
//            } else if ("4".equals(status)) {
//                node.setNodeName("已评价");
//            } else if ("5".equals(status) && "5".equals(nodeType)) {
//                node.setNodeName("未完成");
//            } else if ("1".equals(status) && "5".equals(nodeType)) {
//                node.setNodeName("已完成");
//            }
//
//            children.add(node);
//        }
//
//        return children;
//    }
//}
////lys

////lys2
//package com.dchealth.medical.domain.vo;
//
//import com.dchealth.research.domain.ResearchDiseases;
//import com.dchealth.research.domain.ResearchInfo;
//import lombok.Data;
//
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//import java.util.function.Function;
//import java.util.function.Predicate;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
///**
// * 随访工作站任务树
// */
//@Data
//public class FollowWorkTree {
//
//    /**
//     * 节点ID
//     */
//    private String nodeId;
//
//    /**
//     * 父节点ID
//     */
//    private String parentNodeId;
//
//    /**
//     * 节点名称
//     */
//    private String nodeName;
//
//    /**
//     * 0根节点1病种/待分配患者2队列/任务类型状态3任务类型4任务类型状态
//     */
//    private String nodeLevel;
//
//    /**
//     * 层级类型
//     * 0根节点1病种2待分配患者
//     */
//    private String nodeLevelType;
//
//    /**
//     * 节点类型
//     * 0无类型1患者入组审核2随访处理3随访超窗4预警处理
//     */
//    private String nodeType;
//
//    /**
//     * 0无状态1待处理2进行中3已完成4已评价5未完成（基线未填写，查询转换为0）
//     */
//    private String nodeTypeStatus;
//
//    /**
//     * 队列id
//     */
//    private Long researchId;
//
//    /**
//     * 处理数量
//     */
//    private Integer handleNum;
//
//    /**
//     * 子节点
//     */
//    private List<FollowWorkTree> childrenList;
//
//    public FollowWorkTree() {
//    }
//
//    public FollowWorkTree(String parentNodeId, ResearchDiseases researchDiseases) {
//        this.nodeId = parentNodeId + "_" + researchDiseases.getDiseaseId().longValue();
//        this.parentNodeId = parentNodeId;
//        this.nodeName = researchDiseases.getDiseaseName();
//        this.nodeLevel = "1";
//        this.nodeLevelType = "1";
//        this.nodeType = "0";
//        this.nodeTypeStatus = "0";
//    }
//
//    public FollowWorkTree(String parentNodeId, ResearchInfo researchInfo, Map<String, List<FollowWorkListVO>> mapInfo) {
//        this.nodeId = parentNodeId + "_" + researchInfo.getId().longValue();
//        this.parentNodeId = parentNodeId;
//        this.nodeName = researchInfo.getProjectName();
//        this.nodeLevel = "2";
//        this.nodeLevelType = "1";
//        this.nodeType = "0";
//        this.nodeTypeStatus = "0";
//        List<FollowWorkTree> nodeLevel3 = createNodeLevel3(this.nodeId, researchInfo.getId(), mapInfo, researchInfo.getResearchStatus());
//        this.childrenList = nodeLevel3;
//        int sum = nodeLevel3.stream().filter(vo -> vo.getHandleNum() != null).mapToInt(FollowWorkTree::getHandleNum).sum();
//        this.setHandleNum(sum);
//    }
//
////    public FollowWorkTree(List<ResearchDiseases> researchDiseases, List<ResearchInfo> researchInfos, Map<String,List<FollowWorkListVO>> mapInfo){
////        this.setNodeId("0");
////        this.setParentNodeId("0");
////        this.setNodeName("随访任务");
////        this.setNodeLevel("0");
////        this.setNodeLevelType("0");
////        this.setNodeType("0");
////        this.setNodeTypeStatus("0");
////
////        Map<Long, List<ResearchInfo>> researchMap = null;
////        if (researchInfos != null && !researchInfos.isEmpty()){
////            researchMap = researchInfos.stream().collect(Collectors.groupingBy(ResearchInfo :: getDiseaseTypeId));
////        }
////        final Map<Long, List<ResearchInfo>> map = researchMap;
////        if (researchDiseases != null && !researchDiseases.isEmpty()){
////            List<FollowWorkTree> level2 = researchDiseases.stream().map(x->{
////                FollowWorkTree followWorkTree = new FollowWorkTree(this.getNodeId(), x);
////                List<ResearchInfo> researchInfoList = null;
////                if (map != null){
////                    researchInfoList = map.get(x.getDiseaseId());
////                    if (researchInfoList != null && !researchInfoList.isEmpty()){
////                        List<FollowWorkTree> children = researchInfoList.stream().map(y->new FollowWorkTree(followWorkTree.getNodeId(), y, mapInfo)).collect(Collectors.toList());
////                        followWorkTree.setChildrenList(children);
////                        int sum = children.stream().filter(vo ->vo.getHandleNum() != null).mapToInt(FollowWorkTree::getHandleNum).sum();
////                        followWorkTree.setHandleNum(sum);
////                    }
////                }
////                return followWorkTree;
////            }).collect(Collectors.toList());
////            this.childrenList = level2;
////        }
//
//    /// /        FollowWorkTree pend = new FollowWorkTree();
//    /// /        pend.setNodeId(this.nodeId+"_01");
//    /// /        pend.setParentNodeId(this.nodeId);
//    /// /        pend.setNodeName("待分配患者");
//    /// /        pend.setNodeLevel("1");
//    /// /        pend.setNodeLevelType("2");
//    /// /        pend.setNodeType("0");
//    /// /        pend.setNodeTypeStatus("0");
//    /// /        pend.setChildrenList(createNodeLevel4(pend.getNodeId(),"2", "2", "0", new String[]{"1","3"}, null));
//    /// /        if (this.childrenList == null){
//    /// /            this.childrenList = new ArrayList<>();
//    /// /        }
//    /// /        //待分配患者数量-待处理
//    /// /        List<FollowWorkListVO> allocated = mapInfo.get("allocated");
//    /// /        pend.setHandleNum(allocated != null ? allocated.size() : null);
//    /// /        this.childrenList.add(pend);
////    }
//
//    // 优化树构建的构造函数
//    public FollowWorkTree(List<ResearchDiseases> researchDiseases,
//                          List<ResearchInfo> researchInfos,
//                          Map<String, List<FollowWorkListVO>> mapInfo) {
//
//        // 基础属性设置
//        this.setNodeId("0");
//        this.setParentNodeId("0");
//        this.setNodeName("随访任务");
//        this.setNodeLevel("0");
//        this.setNodeLevelType("0");
//        this.setNodeType("0");
//        this.setNodeTypeStatus("0");
//
//        // 提前处理researchInfos分组，使用ConcurrentHashMap提高并发性能
//        final Map<Long, List<ResearchInfo>> researchMap = researchInfos.stream()
//                .filter(Objects::nonNull)
//                .collect(Collectors.groupingByConcurrent(ResearchInfo::getDiseaseTypeId));
//
//        // 优化疾病节点处理
//        this.childrenList = researchDiseases.stream()
//                .filter(Objects::nonNull)
//                .map(disease -> createDiseaseNode(disease, researchMap, mapInfo))
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//    }
//
//    // 优化疾病节点创建方法
//    private FollowWorkTree createDiseaseNode(ResearchDiseases disease,
//                                             Map<Long, List<ResearchInfo>> researchMap,
//                                             Map<String, List<FollowWorkListVO>> mapInfo) {
//
//        FollowWorkTree diseaseNode = new FollowWorkTree(this.getNodeId(), disease);
//
//        List<ResearchInfo> researchInfoList = researchMap.getOrDefault(disease.getDiseaseId(), Collections.emptyList());
//
//        if (!researchInfoList.isEmpty()) {
//            // 根据数据量决定是否使用并行流
//            Stream<ResearchInfo> infoStream = researchInfoList.size() > 100 ?
//                    researchInfoList.parallelStream() : researchInfoList.stream();
//
//            List<FollowWorkTree> children = infoStream
//                    .map(info -> new FollowWorkTree(diseaseNode.getNodeId(), info, mapInfo))
//                    .filter(Objects::nonNull)
//                    .collect(Collectors.toList());
//
//            diseaseNode.setChildrenList(children);
//
//            // 使用更高效的方式计算总和
//            int handleNum = 0;
//            for (FollowWorkTree node : children) {
//                if (node.getHandleNum() != null) {
//                    handleNum += node.getHandleNum();
//                }
//            }
//            diseaseNode.setHandleNum(handleNum);
//        }
//        return diseaseNode;
//    }
//
//
//    /**
//     * 构造函数
//     *
//     * @param parentNodeId
//     * @param nodeLevel
//     * @param nodeType
//     */
//    public FollowWorkTree(String parentNodeId, String nodeLevel, String nodeLevelType, String nodeType, String nodeTypeStatus, Long researchId, Map<String, List<FollowWorkListVO>> mapInfo) {
//        if ("3".equals(nodeLevel)) {
//            this.nodeId = parentNodeId + "_" + nodeType;
//        } else if ("2".equals(nodeLevel) || "4".equals(nodeLevel)) {
//            this.nodeId = parentNodeId + "_" + nodeTypeStatus;
//        }
//        this.parentNodeId = parentNodeId;
//        this.nodeLevel = nodeLevel;
//        this.nodeLevelType = nodeLevelType;
//        this.nodeType = nodeType;
//        this.researchId = researchId;
//        if ("3".equals(nodeLevel)) {
//            List<FollowWorkListVO> vos = null;
//            if ("1".equals(nodeType)) {
//                this.nodeName = "患者入组审核";
//                vos = mapInfo.get("follow");
//            } else if ("2".equals(nodeType)) {
//                this.nodeName = "随访处理";
//                vos = mapInfo.get("handle");
//            } else if ("3".equals(nodeType)) {
//                this.nodeName = "随访超窗";
//                vos = mapInfo.get("over");
//            } else if ("4".equals(nodeType)) {
//                this.nodeName = "预警处理";
//                vos = mapInfo.get("warn");
//            } else if ("5".equals(nodeType)) {
//                this.nodeName = "基线";
//                vos = mapInfo.get("baseline");
//            } else if ("6".equals(nodeType)) {
//                this.nodeName = "患者退出列表审核";
//                vos = mapInfo.get("logoutAudit");
//            }
//            this.nodeTypeStatus = "0";
//            if (vos != null && !vos.isEmpty()) {
//                long count = vos.stream().filter(vo -> String.valueOf(vo.getResearchId()).equals(String.valueOf(researchId))).count();
//                this.handleNum = Integer.valueOf(String.valueOf(count));
//            }
//        } else if ("4".equals(nodeLevel) || "2".equals(nodeLevel)) {
//            this.nodeTypeStatus = nodeTypeStatus;
//            if ("1".equals(nodeTypeStatus)) {
//                this.nodeName = "待处理";
//            } else if ("2".equals(nodeTypeStatus)) {
//                this.nodeName = "进行中";
//            } else if ("3".equals(nodeTypeStatus)) {
//                this.nodeName = "已完成";
//            } else if ("4".equals(nodeTypeStatus)) {
//                this.nodeName = "已评价";
//            }
//
//            //基线状态特殊处理
//            if ("5".equals(nodeType)) {
//                if ("5".equals(nodeTypeStatus)) {
//                    this.nodeName = "未完成";
//                } else if ("1".equals(nodeTypeStatus)) {
//                    this.nodeName = "已完成";
//                }
//            }
//        }
//    }
//
//    public FollowWorkTree setChildrenList(List<FollowWorkTree> childrenList) {
//        this.childrenList = childrenList;
//        return this;
//    }
//
//    public FollowWorkTree setChildrenListData(String[] nodeTypeStatus) {
//        this.childrenList = createNodeLevel4(this.nodeId, "4", "1", this.nodeType, nodeTypeStatus, this.researchId);
//        return this;
//    }
//
//    /**
//     * 任务类型
//     *
//     * @param parentNodeId
//     * @return
//     */
//    private List<FollowWorkTree> createNodeLevel3(String parentNodeId, Long researchId, Map<String, List<FollowWorkListVO>> mapInfo, String researchStatus) {
//        List<FollowWorkTree> followWorkTrees = new ArrayList<FollowWorkTree>();
//        if ("1".equals(researchStatus)) {
//            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "1", "0", researchId, mapInfo)
//                    .setChildrenListData(new String[]{"1", "2", "3"}));
//            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "5", "0", researchId, mapInfo)
//                    .setChildrenListData(new String[]{"5", "1"}));
//            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "2", "0", researchId, mapInfo)
//                    .setChildrenListData(new String[]{"1", "2", "3", "4"}));
//            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "3", "0", researchId, mapInfo)
//                    .setChildrenListData(new String[]{"1", "3", "4"}));
//            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "4", "0", researchId, mapInfo)
//                    .setChildrenListData(new String[]{"1", "2", "3", "4"}));
//            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "6", "0", researchId, mapInfo)
//                    .setChildrenListData(new String[]{"1", "3"}));
//        } else {
//            //未发布随访  允许进行患者入组审核
//            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "1", "0", researchId, mapInfo)
//                    .setChildrenListData(new String[]{"1", "2", "3"}));
//            followWorkTrees.add(new FollowWorkTree(parentNodeId, "3", "1", "5", "0", researchId, mapInfo)
//                    .setChildrenListData(new String[]{"5", "1"}));
//        }
//
//        return followWorkTrees;
//    }
//
//    /**
//     * 任务类型状态
//     *
//     * @param parentNodeId
//     * @param nodeLevel
//     * @param nodeLevelType
//     * @return
//     */
//    private List<FollowWorkTree> createNodeLevel4(String parentNodeId, String nodeLevel, String nodeLevelType, String nodeType, String[] nodeTypeStatus, Long researchId) {
//        List<FollowWorkTree> followWorkTrees = new ArrayList<FollowWorkTree>();
//        for (String x : nodeTypeStatus) {
//            followWorkTrees.add(new FollowWorkTree(parentNodeId, nodeLevel, nodeLevelType, nodeType, x, researchId, null));
//        }
//        return followWorkTrees;
//    }
//
//
//}

//lys2
package com.dchealth.medical.domain.vo;

import com.dchealth.research.domain.ResearchDiseases;
import com.dchealth.research.domain.ResearchInfo;
import lombok.Data;

import java.util.*;
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

    /**
     * 是否有子节点（用于懒加载）
     */
    private Boolean hasChildren = false;

    /**
     * 病种id
     * @return
     */
    private Long diseaseId;

    // 添加getter和setter
    public Boolean getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public FollowWorkTree() {
    }

    // 修改构造函数，为病种节点设置hasChildren属性
    public FollowWorkTree(String parentNodeId, ResearchDiseases researchDiseases) {
        this.nodeId = parentNodeId + "_" + researchDiseases.getDiseaseId().longValue();
        this.parentNodeId = parentNodeId;
        this.nodeName = researchDiseases.getDiseaseName();
        this.nodeLevel = "1";
        this.nodeLevelType = "1";
        this.nodeType = "0";
        this.nodeTypeStatus = "0";
        this.hasChildren = true; // 病种节点有子节点需要懒加载
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

    public FollowWorkTree(List<ResearchDiseases> researchDiseases,
                          List<ResearchInfo> researchInfos,
                          Map<String, List<FollowWorkListVO>> mapInfo, String m) {

//
//        // 提前处理researchInfos分组，使用ConcurrentHashMap提高并发性能
////        final Map<Long, List<ResearchInfo>> researchMap = researchInfos.stream()
////                .filter(Objects::nonNull)
////                .collect(Collectors.groupingByConcurrent(ResearchInfo::getDiseaseTypeId));
//
//        // 优化疾病节点处理
//        this.childrenList = researchDiseases.stream()
//                .filter(Objects::nonNull)
//                .map(disease -> createDiseaseNode(disease, researchMap, mapInfo))
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//
        List<FollowWorkTree> children = researchInfos.stream()
                .map(info -> new FollowWorkTree(m, info, mapInfo))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        this.handleNum = children.stream().mapToInt(FollowWorkTree::getHandleNum).sum();

        this.childrenList = children;

    }

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
            //未发布随访  仅基线节点
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