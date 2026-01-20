package com.dchealth.research.domain.vo;

import com.dchealth.common.core.domain.entity.SysDept;
import com.dchealth.research.domain.ResearchDiseases;
import com.dchealth.research.domain.ResearchInfo;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResearchDiseasesTreeSelect implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;//0根节点1医院2科室3病种4队列

    private int count;

    private String parentNodeId;

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 节点名称
     */
    private String label;

    private Long id;

    /**
     * 阶段（0-简介，1-随访周期，2-CRF，3-患教，4-外呼，5-随访患者，6-随访医生，7-队列状态）
     */
    private String stage;

    /**
     * 队列状态0待发布1进行中3已关闭
     */
    private String researchStatus;

    private Integer orderNum;
    //队列创建人
    private String researchCreateBy;

    /**
     * 子节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonManagedReference
    private List<ResearchDiseasesTreeSelect> children;

    @JsonBackReference
    private ResearchDiseasesTreeSelect parent;

    public ResearchDiseasesTreeSelect() {
    }

    public ResearchDiseasesTreeSelect(SysDept sysDept) {
        this.id = sysDept.getDeptId();
        this.nodeId = "d_" + sysDept.getDeptId().longValue();
        this.parentNodeId = "d_" + sysDept.getParentId().longValue();
        if ("0".equals(sysDept.getDeptType())) {
            this.type = "0";
        } else if ("1".equals(sysDept.getDeptType())) {
            this.type = "1";
        } else if ("2".equals(sysDept.getDeptType())) {
            this.type = "2";
        }
        this.label = sysDept.getDeptName();
    }

    public ResearchDiseasesTreeSelect(ResearchDiseases researchDiseases) {
        this.id = researchDiseases.getDiseaseId();
        this.nodeId = "rd_" + researchDiseases.getDiseaseId().longValue();
        this.parentNodeId = "d_" + researchDiseases.getDeptId().longValue();
        this.type = "3";
        this.label = researchDiseases.getDiseaseName();
    }

    public ResearchDiseasesTreeSelect(ResearchInfo researchInfo) {
        this.id = researchInfo.getId();
        this.nodeId = "ri_" + researchInfo.getId().longValue();
        this.parentNodeId = "rd_" + researchInfo.getDiseaseTypeId().longValue();
        this.type = "4";
        this.label = researchInfo.getProjectName();
        this.stage = researchInfo.getStage();
    }

    public ResearchDiseasesTreeSelect(List<ResearchDiseasesVO> list) {
        ResearchDiseasesVO diseasesVO = list.get(0);
        this.id = diseasesVO.getDeptId();
        this.nodeId = "d_" + diseasesVO.getDeptId().longValue();
        this.parentNodeId = "0";
        this.type = "2";
        this.label = diseasesVO.getDeptName();
        this.orderNum = diseasesVO.getOrderNum();

        Map<Long, List<ResearchDiseasesVO>> map = list.stream().filter(x -> x.getDiseaseId() != null).collect(Collectors.groupingBy(ResearchDiseasesVO::getDiseaseId));
        List<ResearchDiseasesTreeSelect> dis = new ArrayList<>();
        if (map != null && !map.isEmpty()) {
            map.forEach((k, v) -> {
                if (v != null && !v.isEmpty()) {
                    ResearchDiseasesVO l = v.get(0);
                    ResearchDiseasesTreeSelect treeSelect = new ResearchDiseasesTreeSelect();
                    treeSelect.setId(l.getDiseaseId());
                    treeSelect.setNodeId("rd_" + l.getDiseaseId().longValue());
                    treeSelect.setParentNodeId("d_" + diseasesVO.getDeptId().longValue());
                    treeSelect.setType("3");
                    treeSelect.setLabel(l.getDiseaseName());
                    List<ResearchDiseasesTreeSelect> rs = new ArrayList<>();
                    for (ResearchDiseasesVO researchDiseasesVO : v) {
                        if (researchDiseasesVO.getResearchId() != null) {
                            ResearchDiseasesTreeSelect research = new ResearchDiseasesTreeSelect();
                            research.setId(researchDiseasesVO.getResearchId());
                            research.setNodeId("ri_" + researchDiseasesVO.getResearchId().longValue());
                            research.setParentNodeId("rd_" + l.getDiseaseId().longValue());
                            research.setType("4");
                            research.setLabel(researchDiseasesVO.getProjectName());
                            research.setStage(researchDiseasesVO.getStage());
                            research.setResearchStatus(researchDiseasesVO.getResearchStatus());
                            research.setResearchCreateBy(researchDiseasesVO.getCreateBy());
                            rs.add(research);
                        }
                    }
                    treeSelect.setCount(rs.size());
                    treeSelect.setChildren(rs);
                    dis.add(treeSelect);
                }
            });
        }
        this.children = dis;
        this.count = dis.size();
    }


    public ResearchDiseasesTreeSelect(List<ResearchDiseasesVO> list, Boolean flag, List<ResearchDiseasesVO> parentVos, SysDept partentDept) {

        // 外面包一层科室节点
//        ResearchDiseasesVO diseasesVO = list.get(0);
        this.id = partentDept.getDeptId();
        this.nodeId = "d_" + partentDept.getDeptId();
        this.parentNodeId = "0";
        this.type = "2";
        this.label = partentDept.getDeptName();
        this.orderNum = Integer.parseInt(partentDept.getOrderNum());

        List<ResearchDiseasesTreeSelect> children = new ArrayList<>();

        if (flag) {
            if (parentVos != null && !parentVos.isEmpty()) {
                // 一级层级下也有病种
                ResearchDiseasesVO researchDiseasesVO = parentVos.get(0);
                Map<Long, List<ResearchDiseasesVO>> map2 = parentVos.stream().filter(x -> x.getDiseaseId() != null).collect(Collectors.groupingBy(ResearchDiseasesVO::getDiseaseId));
                List<ResearchDiseasesTreeSelect> dis2 = getResearchDiseasesTreeSelects(map2, researchDiseasesVO);
//                this.setChildren(dis2);
                this.setCount(dis2.size());
                children.addAll(dis2);
            }

        }

        Map<Long, List<ResearchDiseasesVO>> deptMap = list.stream().collect(Collectors.groupingBy(ResearchDiseasesVO::getDeptId));

        deptMap.forEach((k, v) -> {


//        List<ResearchDiseasesTreeSelect> researchDiseasesTreeSelects = new ArrayList<>();

            ResearchDiseasesTreeSelect researchDiseasesTreeSelect = new ResearchDiseasesTreeSelect();
            ResearchDiseasesVO diseasesVO = v.get(0);
            researchDiseasesTreeSelect.setId(diseasesVO.getDeptId());
            researchDiseasesTreeSelect.setNodeId("d_" + diseasesVO.getDeptId());
            researchDiseasesTreeSelect.setParentNodeId("0");
            researchDiseasesTreeSelect.setType("2");
            researchDiseasesTreeSelect.setLabel(diseasesVO.getDeptName());
            researchDiseasesTreeSelect.setOrderNum(diseasesVO.getOrderNum());


            Map<Long, List<ResearchDiseasesVO>> map = v.stream().filter(x -> x.getDiseaseId() != null).collect(Collectors.groupingBy(ResearchDiseasesVO::getDiseaseId));
            List<ResearchDiseasesTreeSelect> dis = getResearchDiseasesTreeSelects(map, diseasesVO);
            researchDiseasesTreeSelect.setChildren(dis);
            researchDiseasesTreeSelect.setCount(dis.size());
            children.add(researchDiseasesTreeSelect);
//            researchDiseasesTreeSelects.add(researchDiseasesTreeSelect);
        });


//        children.addAll(collect);

        this.children = children;

    }

    private static List<ResearchDiseasesTreeSelect> getResearchDiseasesTreeSelects(Map<Long, List<ResearchDiseasesVO>> map, ResearchDiseasesVO diseasesVO) {
        List<ResearchDiseasesTreeSelect> dis = new ArrayList<>();
        if (map != null && !map.isEmpty()) {
            map.forEach((k, v) -> {
                if (v != null && !v.isEmpty()) {
                    ResearchDiseasesVO l = v.get(0);
                    ResearchDiseasesTreeSelect treeSelect = new ResearchDiseasesTreeSelect();
                    treeSelect.setId(l.getDiseaseId());
                    treeSelect.setNodeId("rd_" + l.getDiseaseId().longValue());
                    treeSelect.setParentNodeId("d_" + diseasesVO.getDeptId().longValue());
                    treeSelect.setType("3");
                    treeSelect.setLabel(l.getDiseaseName());
                    List<ResearchDiseasesTreeSelect> rs = new ArrayList<>();
                    for (ResearchDiseasesVO researchDiseasesVO : v) {
                        if (researchDiseasesVO.getResearchId() != null) {
                            ResearchDiseasesTreeSelect research = new ResearchDiseasesTreeSelect();
                            research.setId(researchDiseasesVO.getResearchId());
                            research.setNodeId("ri_" + researchDiseasesVO.getResearchId().longValue());
                            research.setParentNodeId("rd_" + l.getDiseaseId().longValue());
                            research.setType("4");
                            research.setLabel(researchDiseasesVO.getProjectName());
                            research.setStage(researchDiseasesVO.getStage());
                            research.setResearchStatus(researchDiseasesVO.getResearchStatus());
                            research.setResearchCreateBy(researchDiseasesVO.getCreateBy());
                            rs.add(research);
                        }
                    }
                    treeSelect.setCount(rs.size());
                    treeSelect.setChildren(rs);
                    dis.add(treeSelect);
                }
            });
        }
        return dis;
    }


    /**
     * 树结构
     *
     * @param treeSelects
     * @param isCondition
     * @return
     */
    public static List<ResearchDiseasesTreeSelect> tree(List<ResearchDiseasesTreeSelect> treeSelects, boolean isCondition) {
        ResearchDiseasesTreeSelect researchDiseasesTreeSelect = treeSelects.stream().filter(x -> "d_0".equals(x.getParentNodeId())).findFirst().get();//根节点
        if (isCondition) {
            return filter(generateTree(researchDiseasesTreeSelect.getNodeId(), treeSelects));
        } else {
            return generateTree(researchDiseasesTreeSelect.getNodeId(), treeSelects).getChildren();
        }
    }

    /**
     * 过滤
     *
     * @param treeSelect
     * @return
     */
    private static List<ResearchDiseasesTreeSelect> filter(ResearchDiseasesTreeSelect treeSelect) {
        if (isRemoveNode(treeSelect)) {
            return new ArrayList<>();
        }
        Iterator<ResearchDiseasesTreeSelect> iterator = treeSelect.getChildren().iterator();
        while (iterator.hasNext()) {
            ResearchDiseasesTreeSelect item = iterator.next();
            deleteNode(item, iterator);
        }
        return treeSelect.getChildren();
    }

    /**
     * 删除节点
     *
     * @param treeSelect
     * @param iterator
     */
    private static void deleteNode(ResearchDiseasesTreeSelect treeSelect, Iterator<ResearchDiseasesTreeSelect> iterator) {
        if (isRemoveNode(treeSelect)) {
            iterator.remove();
            return;
        }

        Iterator<ResearchDiseasesTreeSelect> children = treeSelect.getChildren().iterator();
        while (children.hasNext()) {
            ResearchDiseasesTreeSelect child = children.next();
            deleteNode(child, children);
        }
    }

    /**
     * 判断是否需要删除节点
     * 有一个子节点类型等于4返回FALSE
     *
     * @param treeSelect
     * @return
     */
    private static boolean isRemoveNode(ResearchDiseasesTreeSelect treeSelect) {
        if ("4".equals(treeSelect.getType())) {
            return false;
        }
        boolean isRemove = true;
        if (treeSelect.getChildren() != null && treeSelect.getChildren().isEmpty()) {
            for (ResearchDiseasesTreeSelect child : treeSelect.getChildren()) {
                if (!isRemoveNode(treeSelect)) {
                    isRemove = false;
                    break;
                }
            }
        }
        return isRemove;
    }

    /**
     * 数结构
     *
     * @param nodeId
     * @param list
     * @return
     */
    private static ResearchDiseasesTreeSelect generateTree(String nodeId, List<ResearchDiseasesTreeSelect> list) {
        ResearchDiseasesTreeSelect root = new ResearchDiseasesTreeSelect();
        //查询根
        for (ResearchDiseasesTreeSelect menu : list) {
            if (menu.getNodeId().equals(nodeId)) {
                root = menu;
            }
        }
        //查询子集
        List<ResearchDiseasesTreeSelect> childList = new ArrayList<>();
        for (ResearchDiseasesTreeSelect node : list) {
            if (node.getParentNodeId().equals(root.getNodeId())) {
                childList.add(node);
            }
        }
        root.setCount(childList.size());
        //子集查询子集
        if (childList != null && childList.size() > 0) {
            for (ResearchDiseasesTreeSelect child : childList) {
                ResearchDiseasesTreeSelect childRoot = generateTree(child.getNodeId(), list);
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

    public List<ResearchDiseasesTreeSelect> getChildren() {
        return children;
    }

    public void setChildren(List<ResearchDiseasesTreeSelect> children) {
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getResearchCreateBy() {
        return researchCreateBy;
    }

    public void setResearchCreateBy(String researchCreateBy) {
        this.researchCreateBy = researchCreateBy;
    }
}
