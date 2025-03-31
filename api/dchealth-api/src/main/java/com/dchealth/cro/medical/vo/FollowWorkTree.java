package com.dchealth.cro.medical.vo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dchealth.cro.research.domain.ResearchDiseases;
import com.dchealth.cro.research.domain.ResearchInfo;

import lombok.Data;

/**
 * 随访工作站任务树
 */
@Data
public class FollowWorkTree {

    private Long nodeId;

    private String nodeName;

    /**
     * 待处理任务
     */
    private Integer pendingTasksNum;

    /**
     * 进行中任务
     */
    private Integer ongoingTasksNum;

    /**
     * 已完成任务
     */
    private Integer completedTasksNum;


    /**
     * 已完成评价任务
     */
    private Integer evaluatedNum;


    /**
     * 队列
     */
    private List<FollowWorkTree> childrenList;

    public FollowWorkTree(){}

    public FollowWorkTree(ResearchDiseases researchDiseases){
        this.nodeId = researchDiseases.getDiseaseId();
        this.completedTasksNum = researchDiseases.getCompletedTasksNum();
        this.pendingTasksNum = researchDiseases.getPendingTasksNum();
        this.ongoingTasksNum = researchDiseases.getOngoingTasksNum();
        this.evaluatedNum  = researchDiseases.getEvaluatedNum();
        this.nodeName = researchDiseases.getDiseaseName();
    }

    public FollowWorkTree(ResearchInfo researchInfo){
        this.nodeId = researchInfo.getId();
        this.pendingTasksNum = researchInfo.getPendingTasksNum();
        this.completedTasksNum = researchInfo.getCompletedTasksNum();
        this.ongoingTasksNum = researchInfo.getOngoingTasksNum();
        this.evaluatedNum  = researchInfo.getEvaluatedNum();
        this.nodeName = researchInfo.getProjectName();
    }

    public static List<FollowWorkTree> trees(List<ResearchDiseases> researchDiseases, List<ResearchInfo> researchInfos){
        Map<Long, List<ResearchInfo>> researchMap = null;
        if (researchInfos != null && !researchInfos.isEmpty()){
            researchMap = researchInfos.stream().collect(Collectors.groupingBy(ResearchInfo :: getDiseaseTypeId));
        }
        final Map<Long, List<ResearchInfo>> map = researchMap;
        if (researchDiseases != null && !researchDiseases.isEmpty()){
            List<FollowWorkTree> ds = researchDiseases.stream().map(x->{
                FollowWorkTree followWorkTree = new FollowWorkTree(x);
                List<ResearchInfo> researchInfoList = null;
                if (map != null){
                    researchInfoList = map.get(x.getDiseaseId());
                    if (researchInfoList != null && !researchInfoList.isEmpty()){
                        List<FollowWorkTree> children = researchInfoList.stream().map(y->new FollowWorkTree(y)).collect(Collectors.toList());
                        followWorkTree.setChildrenList(children);
                        //已评价任务数量
                        int evaluatedNum = children.stream().mapToInt(tree -> tree.getEvaluatedNum()).sum();
                        followWorkTree.setEvaluatedNum(evaluatedNum);

                        int pendingTasksNum = children.stream().mapToInt(tree -> tree.getPendingTasksNum()).sum();
                        followWorkTree.setPendingTasksNum(pendingTasksNum);
                        int ongoingTasksNum = children.stream().mapToInt(ch -> ch.getOngoingTasksNum()).sum();
                        followWorkTree.setOngoingTasksNum(ongoingTasksNum);
                        int completedTasksNum = children.stream().mapToInt(ch -> ch.getCompletedTasksNum()).sum();
                        followWorkTree.setCompletedTasksNum(completedTasksNum);
                        return followWorkTree;
                    }
                }
                return null;
            }).collect(Collectors.toList());
            return ds;
        }
        return null;
    }
}