package com.dchealth.medical.domain.vo;

import com.dchealth.research.domain.ResearchDiseases;
import com.dchealth.research.domain.ResearchInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 随访工作站任务树
 */
@Data
public class OutboundTree {

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
     * 节点类型
     * 0无类型1随访提醒2超窗提醒
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
    private List<OutboundTree> childrenList;

    public OutboundTree(){}

    public OutboundTree(String parentNodeId, ResearchDiseases researchDiseases){
        this.nodeId = parentNodeId + "_" + researchDiseases.getDiseaseId().longValue();
        this.parentNodeId = parentNodeId;
        this.nodeName = researchDiseases.getDiseaseName();
        this.nodeLevel = "1";
        this.nodeType = "0";
        this.nodeTypeStatus = "0";
    }

    public OutboundTree(String parentNodeId, ResearchInfo researchInfo, Map<String,List<OutboundWorkListVO>> mapInfo){
        this.nodeId = parentNodeId + "_" + researchInfo.getId().longValue();
        this.parentNodeId = parentNodeId;
        this.nodeName = researchInfo.getProjectName();
        this.nodeLevel = "2";
        this.nodeType = "0";
        this.nodeTypeStatus = "0";
        List<OutboundTree> nodeLevel3 = createNodeLevel3(this.nodeId, researchInfo.getId(), mapInfo);
        this.childrenList = nodeLevel3;
        int sum = nodeLevel3.stream().filter(vo -> vo.getHandleNum() != null).mapToInt(OutboundTree::getHandleNum).sum();
        this.setHandleNum(sum);
    }

    public OutboundTree(List<ResearchDiseases> researchDiseases, List<ResearchInfo> researchInfos, Map<String,List<OutboundWorkListVO>> mapInfo){
        this.setNodeId("0");
        this.setParentNodeId("0");
        this.setNodeName("外呼任务");
        this.setNodeLevel("0");
        this.setNodeType("0");
        this.setNodeTypeStatus("0");

        Map<Long, List<ResearchInfo>> researchMap = null;
        if (researchInfos != null && !researchInfos.isEmpty()){
            researchMap = researchInfos.stream().collect(Collectors.groupingBy(ResearchInfo :: getDiseaseTypeId));
        }
        final Map<Long, List<ResearchInfo>> map = researchMap;
        if (researchDiseases != null && !researchDiseases.isEmpty()){
            List<OutboundTree> level2 = researchDiseases.stream().map(x->{
                OutboundTree followWorkTree = new OutboundTree(this.getNodeId(), x);
                List<ResearchInfo> researchInfoList = null;
                if (map != null){
                    researchInfoList = map.get(x.getDiseaseId());
                    if (researchInfoList != null && !researchInfoList.isEmpty()){
                        List<OutboundTree> children = researchInfoList.stream().map(y->new OutboundTree(followWorkTree.getNodeId(), y, mapInfo)).collect(Collectors.toList());
                        followWorkTree.setChildrenList(children);
                        int sum = children.stream().filter(vo ->vo.getHandleNum() != null).mapToInt(OutboundTree::getHandleNum).sum();
                        followWorkTree.setHandleNum(sum);
                    }
                }
                return followWorkTree;
            }).collect(Collectors.toList());
            this.childrenList = level2;
        }
    }

    /**
     * 构造函数
     * @param parentNodeId
     * @param nodeLevel
     * @param nodeType
     */
    public OutboundTree(String parentNodeId, String nodeLevel, String nodeType, String nodeTypeStatus, Long researchId, Map<String,List<OutboundWorkListVO>> mapInfo){
        if ("3".equals(nodeLevel)){
            this.nodeId = parentNodeId + "_" + nodeType;
        }else if ("2".equals(nodeLevel) || "4".equals(nodeLevel)){
            this.nodeId = parentNodeId + "_" + nodeTypeStatus;
        }
        this.parentNodeId = parentNodeId;
        this.nodeLevel = nodeLevel;
        this.nodeType = nodeType;
        this.researchId = researchId;
        if ("3".equals(nodeLevel)){
            List<OutboundWorkListVO> vos = null;
            if ("1".equals(nodeType)){
                this.nodeName = "随访提醒";
                vos = mapInfo.get("follow");
            }else if ("2".equals(nodeType)){
                this.nodeName = "超窗提醒";
                vos = mapInfo.get("over");
            }else if ("3".equals(nodeType)){
                this.nodeName = "预警提醒";
                vos = mapInfo.get("warn");
            }
            this.nodeTypeStatus = "0";
            if(vos != null && !vos.isEmpty()){
                long count = vos.stream().filter(vo -> String.valueOf(vo.getResearchId()).equals(String.valueOf(researchId))).count();
                this.handleNum = Integer.valueOf(String.valueOf(count));
            }
        }else if ("4".equals(nodeLevel) || "2".equals(nodeLevel)){
            this.nodeTypeStatus = nodeTypeStatus;
            if ("1".equals(nodeTypeStatus)){
                this.nodeName = "待拨打";
            }else if ("2".equals(nodeTypeStatus)){
                this.nodeName = "执行中";
            }else if ("3".equals(nodeTypeStatus)){
                this.nodeName = "已完成";
            }
        }
    }

    public OutboundTree setChildrenList(List<OutboundTree> childrenList){
        this.childrenList = childrenList;
        return this;
    }
    public OutboundTree setChildrenListData(String[] nodeTypeStatus){
        this.childrenList = createNodeLevel4(this.nodeId,"4", this.nodeType, nodeTypeStatus, this.researchId);
        return this;
    }

    /**
     * 任务类型
     * @param parentNodeId
     * @return
     */
    private List<OutboundTree> createNodeLevel3(String parentNodeId, Long researchId , Map<String,List<OutboundWorkListVO>> mapInfo){
        List<OutboundTree> followWorkTrees = new ArrayList<OutboundTree>();
        followWorkTrees.add(new OutboundTree(parentNodeId, "3", "1", "0", researchId, mapInfo)
                .setChildrenListData(new String[]{"1","2","3"}));
        followWorkTrees.add(new OutboundTree(parentNodeId, "3", "2", "0", researchId, mapInfo)
                .setChildrenListData(new String[]{"1","2","3"}));
        followWorkTrees.add(new OutboundTree(parentNodeId, "3", "3", "0", researchId, mapInfo)
                .setChildrenListData(new String[]{"1","2","3"}));
        return followWorkTrees;
    }

    /**
     * 任务类型状态
     * @param parentNodeId
     * @param nodeLevel
     * @return
     */
    private List<OutboundTree> createNodeLevel4(String parentNodeId, String nodeLevel, String nodeType, String[] nodeTypeStatus, Long researchId) {
        List<OutboundTree> followWorkTrees = new ArrayList<OutboundTree>();
        for (String x : nodeTypeStatus) {
            followWorkTrees.add(new OutboundTree(parentNodeId, nodeLevel, nodeType, x,researchId, null));
        }
        return followWorkTrees;
    }


}
