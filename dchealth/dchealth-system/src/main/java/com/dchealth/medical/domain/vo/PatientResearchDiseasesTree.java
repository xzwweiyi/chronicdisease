package com.dchealth.medical.domain.vo;

import com.dchealth.common.core.domain.entity.SysDept;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.research.domain.ResearchDiseases;
import com.dchealth.research.domain.ResearchInfo;
import com.dchealth.research.domain.vo.ResearchDiseasesVO;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 患者管理-科室-病种-队列树状图实体
 */
public class PatientResearchDiseasesTree implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;//0根节点1医院2科室3病种4队列

    private int count;

    private String parentNodeId;

    /**
     * 节点id
     */
    private String nodeId;

    /** 节点名称 */
    private String label;

    private Long id;

    /** 阶段（0-简介，1-随访周期，2-CRF，3-患教，4-外呼，5-随访患者，6-随访医生，7-队列状态） */
    private String stage;
    /** 总患者数 */
    private int totalNum;

    /** 入组患者数 */
    private int enrollmentNum;

    /** 基线患者数 */
    private int baseNum;

    /** 随访患者数 */
    private int followNum;

    /** 完成患者数 */
    private int completeNum;

    /** 失访患者数 */
    private int lostFollowNum;

    /** 退出患者数 */
    private int exitNum;

    /** 队列状态0待发布1进行中3已关闭 */
    private String researchStatus;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<PatientResearchDiseasesTree> children;

    public PatientResearchDiseasesTree(){}

    public PatientResearchDiseasesTree(SysDept sysDept){
        this.id = sysDept.getDeptId();
        this.nodeId = "d_"+sysDept.getDeptId().longValue();
        this.parentNodeId = "d_"+sysDept.getParentId().longValue();
        if ("0".equals(sysDept.getDeptType())){
            this.type = "0";
        }else if ("1".equals(sysDept.getDeptType())){
            this.type = "1";
        }else if ("2".equals(sysDept.getDeptType())){
            this.type = "2";
        }
        this.label = sysDept.getDeptName();
    }

    public PatientResearchDiseasesTree(ResearchDiseases researchDiseases){
        this.id = researchDiseases.getDiseaseId();
        this.nodeId = "rd_"+researchDiseases.getDiseaseId().longValue();
        this.parentNodeId = "d_"+researchDiseases.getDeptId().longValue();
        this.type = "3";
        this.label = researchDiseases.getDiseaseName();
    }

    public PatientResearchDiseasesTree(ResearchInfo researchInfo){
        this.id = researchInfo.getId();
        this.nodeId = "ri_"+researchInfo.getId().longValue();
        this.parentNodeId = "rd_"+researchInfo.getDiseaseTypeId().longValue();
        this.type = "4";
        this.label = researchInfo.getProjectName();
        this.stage = researchInfo.getStage();
    }

    public PatientResearchDiseasesTree(List<ResearchDiseasesVO> list, List<PatientInfo> patientInfos){
        ResearchDiseasesVO diseasesVO = list.get(0);
        this.id = diseasesVO.getDeptId();
        this.nodeId = "d_"+diseasesVO.getDeptId().longValue();
        this.parentNodeId = "0";
        this.type = "2";
        this.label=diseasesVO.getDeptName();

        AtomicInteger totalNum = new AtomicInteger();
        AtomicInteger totalEnrollmentNum = new AtomicInteger();
        AtomicInteger totalBaseNum = new AtomicInteger();
        AtomicInteger totalFollowNum = new AtomicInteger();
        AtomicInteger totalCompleteNum = new AtomicInteger();
        AtomicInteger totalLostFollowNum = new AtomicInteger();
        AtomicInteger totalExitNum = new AtomicInteger();

        Map<Long, List<ResearchDiseasesVO>> map = list.stream().filter(x->x.getDiseaseId() != null).collect(Collectors.groupingBy(ResearchDiseasesVO :: getDiseaseId));
        List<PatientResearchDiseasesTree> dis = new ArrayList<>();
        if (map != null && !map.isEmpty()){
            map.forEach((k,v)->{
                if (v != null && !v.isEmpty()){
                    ResearchDiseasesVO l = v.get(0);
                    PatientResearchDiseasesTree treeSelect = new PatientResearchDiseasesTree();
                    treeSelect.setId(l.getDiseaseId());
                    treeSelect.setNodeId("rd_"+l.getDiseaseId().longValue());
                    treeSelect.setParentNodeId("d_"+diseasesVO.getDeptId().longValue());
                    treeSelect.setType("3");
                    treeSelect.setLabel(l.getDiseaseName());

                    AtomicInteger childNum = new AtomicInteger();
                    AtomicInteger childEnrollmentNum = new AtomicInteger();
                    AtomicInteger childBaseNum = new AtomicInteger();
                    AtomicInteger childFollowNum = new AtomicInteger();
                    AtomicInteger childCompleteNum = new AtomicInteger();
                    AtomicInteger childLostFollowNum = new AtomicInteger();
                    AtomicInteger childExitNum = new AtomicInteger();

                    List<PatientResearchDiseasesTree> rs = new ArrayList<>();
                    for (ResearchDiseasesVO researchDiseasesVO : v) {
                        if (researchDiseasesVO.getResearchId() != null){
                            PatientResearchDiseasesTree research = new PatientResearchDiseasesTree();
                            research.setId(researchDiseasesVO.getResearchId());
                            research.setNodeId("ri_"+researchDiseasesVO.getResearchId().longValue());
                            research.setParentNodeId("rd_"+l.getDiseaseId().longValue());
                            research.setType("4");
                            research.setLabel(researchDiseasesVO.getProjectName());
                            research.setStage(researchDiseasesVO.getStage());
                            research.setResearchStatus(researchDiseasesVO.getResearchStatus());
                            if(patientInfos != null && !patientInfos.isEmpty()){
                                List<PatientInfo> patientInfoList = patientInfos.stream().filter(patientInfo -> patientInfo.getResearchId().equals(researchDiseasesVO.getResearchId()))
                                        .distinct().collect(Collectors.toList());
                                if(patientInfoList != null){
                                    //队列患者总数
                                    Integer num = patientInfoList.size();
                                    research.setTotalNum(num);

                                    //已完成的患者数
                                    List<PatientInfo> finishedPatientList = patientInfoList.stream().filter(patientInfo -> "3".equals(patientInfo.getEnrollmentHandlerStatus())).collect(Collectors.toList());
                                    research.setCount(num);

                                    //入组患者数
                                    List<PatientInfo> enrollmentPatientList = finishedPatientList.stream().filter(patientInfo -> "1".equals(patientInfo.getEnrollmentStatus())).collect(Collectors.toList());
                                    Integer enrollmentNum = enrollmentPatientList.size();
                                    research.setEnrollmentNum(enrollmentNum);
                                    List<PatientInfo> currentPatientList = new ArrayList<>();
                                    currentPatientList.addAll(enrollmentPatientList);
                                    //退出患者数
                                    //因为状态标识较多，此处为避免退出患者与其他状态患者出现交集，从总入组患者中排除已退出患者
                                    List<PatientInfo> exitPatientList = enrollmentPatientList.stream().filter(patientInfo -> "6".equals(patientInfo.getPatientStatus())).collect(Collectors.toList());
                                    Integer exitNum = exitPatientList.size();
                                    enrollmentPatientList.removeAll(exitPatientList);
                                    research.setExitNum(exitNum);

                                    //基线患者数
                                    List<PatientInfo> basePatientList = currentPatientList.stream().filter(patientInfo -> "1".equals(patientInfo.getEnrollmentStatus()) &&
                                            (patientInfo.getIsSubmitBase() == 1)).collect(Collectors.toList());
                                    Integer baseNum = basePatientList.size();
                                    research.setBaseNum(baseNum);

                                    //完成患者数
                                    List<PatientInfo> completePatientList = enrollmentPatientList.stream().filter(patientInfo -> "1".equals(patientInfo.getEnrollmentStatus()) &&
                                            1 == patientInfo.getIsCompleteFollowup() &&  1 ==patientInfo.getExistsCompleteFollowup()).collect(Collectors.toList());
                                    Integer completeNum = completePatientList.size();
                                    enrollmentPatientList.removeAll(completePatientList);
                                    research.setCompleteNum(completeNum);

                                    //失访患者数
                                    List<PatientInfo> lostFollowPatientList = enrollmentPatientList.stream().filter(patientInfo -> "4".equals(patientInfo.getPatientStatus())).collect(Collectors.toList());
                                    Integer lostFollowNum = lostFollowPatientList.size();
                                    enrollmentPatientList.removeAll(lostFollowPatientList);
                                    research.setLostFollowNum(lostFollowNum);

                                    //随访患者数
                                    Integer followNum = enrollmentPatientList.size();
                                    research.setFollowNum(followNum.intValue());


                                    //未发布的项目，随访、完成、失访都应为0
                                    if(research.getResearchStatus().equals("0")){
                                        //完成基线，但未发布无随访的完成状态，归为随访状态
                                        followNum = followNum+completeNum;
                                        completeNum = 0;
                                        lostFollowNum = 0;
                                        research.setFollowNum(followNum);
                                        research.setCompleteNum(0);
                                        research.setLostFollowNum(0);
                                    }

                                    totalNum.addAndGet(num);
                                    totalEnrollmentNum.addAndGet(enrollmentNum);
                                    totalBaseNum.addAndGet(baseNum);
                                    totalFollowNum.addAndGet(followNum);
                                    totalCompleteNum.addAndGet(completeNum);
                                    totalLostFollowNum.addAndGet(lostFollowNum);
                                    totalExitNum.addAndGet(exitNum);

                                    childNum.addAndGet(num);
                                    childEnrollmentNum.addAndGet(enrollmentNum);
                                    childBaseNum.addAndGet(baseNum);
                                    childFollowNum.addAndGet(followNum);
                                    childCompleteNum.addAndGet(completeNum);
                                    childLostFollowNum.addAndGet(lostFollowNum);
                                    childExitNum.addAndGet(exitNum);

                                }
                            }
                            rs.add(research);
                        }
                    }
                    treeSelect.setCount(rs.size());
                    treeSelect.setChildren(rs);

                    treeSelect.setTotalNum(childNum.get());
                    treeSelect.setEnrollmentNum(childEnrollmentNum.get());
                    treeSelect.setBaseNum(childBaseNum.get());
                    treeSelect.setFollowNum(childFollowNum.get());
                    treeSelect.setCompleteNum(childCompleteNum.get());
                    treeSelect.setLostFollowNum(childLostFollowNum.get());
                    treeSelect.setExitNum(childExitNum.get());
                    dis.add(treeSelect);
                }
            });
        }
        this.children = dis;
        this.count = dis.size();

        this.totalNum = totalNum.get();
        this.enrollmentNum = totalEnrollmentNum.get();
        this.baseNum = totalBaseNum.get();
        this.followNum = totalFollowNum.get();
        this.completeNum = totalCompleteNum.get();
        this.lostFollowNum = totalLostFollowNum.get();
        this.exitNum = totalExitNum.get();
    }


    /**
     * 树结构
     * @param treeSelects
     * @param isCondition
     * @return
     */
    public static List<PatientResearchDiseasesTree> tree(List<PatientResearchDiseasesTree> treeSelects, boolean isCondition){
        PatientResearchDiseasesTree researchDiseasesTreeSelect = treeSelects.stream().filter(x->"d_0".equals(x.getParentNodeId())).findFirst().get();//根节点
        if (isCondition){
            return filter(generateTree(researchDiseasesTreeSelect.getNodeId(), treeSelects));
        }else{
            return generateTree(researchDiseasesTreeSelect.getNodeId(), treeSelects).getChildren();
        }
    }

    /**
     * 过滤
     * @param treeSelect
     * @return
     */
    private static List<PatientResearchDiseasesTree> filter(PatientResearchDiseasesTree treeSelect){
        if (isRemoveNode(treeSelect)){
            return new ArrayList<>();
        }
        Iterator<PatientResearchDiseasesTree> iterator = treeSelect.getChildren().iterator();
        while (iterator.hasNext()){
            PatientResearchDiseasesTree item = iterator.next();
            deleteNode(item, iterator);
        }
        return treeSelect.getChildren();
    }

    /**
     * 删除节点
     * @param treeSelect
     * @param iterator
     */
    private static void deleteNode(PatientResearchDiseasesTree treeSelect, Iterator<PatientResearchDiseasesTree> iterator){
        if (isRemoveNode(treeSelect)){
            iterator.remove();
            return;
        }

        Iterator<PatientResearchDiseasesTree> children = treeSelect.getChildren().iterator();
        while (children.hasNext()){
            PatientResearchDiseasesTree child = children.next();
            deleteNode(child,children);
        }
    }

    /**
     * 判断是否需要删除节点
     * 有一个子节点类型等于4返回FALSE
     * @param treeSelect
     * @return
     */
    private static boolean isRemoveNode(PatientResearchDiseasesTree treeSelect){
        if ("4".equals(treeSelect.getType())){
            return false;
        }
        boolean isRemove = true;
        if (treeSelect.getChildren() != null && treeSelect.getChildren().isEmpty()){
            for (PatientResearchDiseasesTree child : treeSelect.getChildren()) {
                if (!isRemoveNode(treeSelect)){
                    isRemove = false;
                    break;
                }
            }
        }
        return isRemove;
    }

    /**
     * 数结构
     * @param nodeId
     * @param list
     * @return
     */
    private static PatientResearchDiseasesTree generateTree(String nodeId, List<PatientResearchDiseasesTree> list) {
        PatientResearchDiseasesTree root = new PatientResearchDiseasesTree();
        //查询根
        for (PatientResearchDiseasesTree menu : list) {
            if (menu.getNodeId().equals(nodeId)) {
                root = menu;
            }
        }
        //查询子集
        List<PatientResearchDiseasesTree> childList = new ArrayList<>();
        for (PatientResearchDiseasesTree node : list) {
            if (node.getParentNodeId().equals(root.getNodeId())) {
                childList.add(node);
            }
        }
        root.setCount(childList.size());
        //子集查询子集
        if (childList != null && childList.size() > 0) {
            for (PatientResearchDiseasesTree child : childList) {
                PatientResearchDiseasesTree childRoot = generateTree(child.getNodeId(), list);
                if (root.getChildren() == null) {
                    root.setChildren(new ArrayList<>());
                }
                root.getChildren().add(childRoot);
            }
        }
        return root;
    }

    public String getResearchStatus() {
        return researchStatus;
    }

    public void setResearchStatus(String researchStatus) {
        this.researchStatus = researchStatus;
    }

    public int getEnrollmentNum() {
        return enrollmentNum;
    }

    public void setEnrollmentNum(int enrollmentNum) {
        this.enrollmentNum = enrollmentNum;
    }

    public int getBaseNum() {
        return baseNum;
    }

    public void setBaseNum(int baseNum) {
        this.baseNum = baseNum;
    }

    public int getFollowNum() {
        return followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public int getCompleteNum() {
        return completeNum;
    }

    public void setCompleteNum(int completeNum) {
        this.completeNum = completeNum;
    }

    public int getLostFollowNum() {
        return lostFollowNum;
    }

    public void setLostFollowNum(int lostFollowNum) {
        this.lostFollowNum = lostFollowNum;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getParentNodeId() {
        return parentNodeId;
    }

    public void setParentNodeId(String parentNodeId) {
        this.parentNodeId = parentNodeId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<PatientResearchDiseasesTree> getChildren() {
        return children;
    }

    public void setChildren(List<PatientResearchDiseasesTree> children) {
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getExitNum() {
        return exitNum;
    }

    public void setExitNum(int exitNum) {
        this.exitNum = exitNum;
    }
}
