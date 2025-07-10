package com.dchealth.health.vo.req;

import com.alibaba.excel.util.DateUtils;
import com.dchealth.cms.domain.CmsArticle;
import com.dchealth.common.enums.HealthManageEnum;
import com.dchealth.health.domain.PatientRecordHealth;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.research.domain.vo.ResearchDiseasesVO;
import com.dchealth.template.domain.FMedicalForm;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class RecordHealthTree implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;//0根节点1病种2队列3(随访、预警、患教问卷)4其它

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

    private Long patientId;

    private String personId;

    private Long recordId;
    /**
     * 队列id
     */
    private Long researchId;

    /**
     * 随访Id
     */
    private Long followId;

    /**
     * 患教id
     */
    private Long articleId;

    private Long healthId;

    /**
     * 模板Id
     */
    private Long medicalFormId;
    private String visitId;//就诊ID

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<RecordHealthTree> children;

    public RecordHealthTree() {
    }

    public RecordHealthTree(List<ResearchDiseasesVO> list, List<PatientRecord> patientRecords, List<CmsArticle> cmsArticles, Long patientId, List<FMedicalForm> fMedicalForms, List<PatientRecordHealth> patientRecordHealths) {
        this.type = "0";
        this.parentNodeId = "0";
        this.nodeId = "0";
        this.label = "健康管理";
        this.patientId = patientId;

        //按照病种分组
        Map<Long, List<ResearchDiseasesVO>> map = list.stream().filter(x -> x.getDiseaseId() != null).collect(Collectors.groupingBy(ResearchDiseasesVO::getDiseaseId));
        List<RecordHealthTree> dis = new ArrayList<>();
        if (map != null && !map.isEmpty()) {
            map.forEach((k, v) -> {
                if (v != null && !v.isEmpty()) {
                    //第一级节点，病种
                    ResearchDiseasesVO l = v.get(0);
                    RecordHealthTree level1Tree = new RecordHealthTree();
                    level1Tree.setId(l.getDiseaseId());
                    level1Tree.setNodeId("rd_" + l.getDiseaseId().longValue());
                    level1Tree.setParentNodeId("0");
                    level1Tree.setType("1");
                    level1Tree.setLabel(l.getDiseaseName());

                    List<RecordHealthTree> rs = new ArrayList<>();
                    for (ResearchDiseasesVO researchDiseasesVO : v) {
                        if (researchDiseasesVO.getResearchId() != null) {
                            //第二级节点,队列
                            RecordHealthTree level2Tree = new RecordHealthTree();
                            level2Tree.setId(researchDiseasesVO.getResearchId());
                            level2Tree.setNodeId("ri_" + researchDiseasesVO.getResearchId().longValue());
                            level2Tree.setParentNodeId("rd_" + l.getDiseaseId().longValue());
                            level2Tree.setType("2");
                            level2Tree.setLabel(researchDiseasesVO.getProjectName());
                            level2Tree.setResearchId(researchDiseasesVO.getResearchId());

                                //第四级节点
                                List<RecordHealthTree> diseasesResearchTreeList = buildLevel4Node(patientRecords, cmsArticles, 1, researchDiseasesVO, fMedicalForms, patientId, 1, patientRecordHealths);
                            level2Tree.setChildren(diseasesResearchTreeList);
                            rs.add(level2Tree);
                        }
                    }
                    //设置第一级的子节点
                    level1Tree.setChildren(rs);
                    dis.add(level1Tree);
                }
            });
        }
        this.children = dis;
    }


    /**
     * 构建第四层节点
     *
     * @param patientRecords
     * @param cmsArticles
     * @param key
     * @return
     */
    private List<RecordHealthTree> buildLevel4Node(List<PatientRecord> patientRecords, List<CmsArticle> cmsArticles,
                                                   Integer key, ResearchDiseasesVO researchDiseasesVO,
                                                   List<FMedicalForm> fMedicalForms, Long patientIdI, Integer keyValue, List<PatientRecordHealth> patientRecordHealths) {

        List<RecordHealthTree> tree = new ArrayList<>();
        if (key == 1 || key == 2) {
            List<PatientRecord> records = null;
            if (!CollectionUtils.isEmpty(patientRecords)) {
                records = patientRecords.stream().filter(patientRecord -> patientRecord.getResearchId().equals(researchDiseasesVO.getResearchId())).collect(Collectors.toList());
            }
            if (key == 1) {
                if (records != null && !records.isEmpty()) {
                    //随访
//                    List<PatientRecord> collect = records.stream().filter(record -> "0".equals(record.getWarnStatus())).collect(Collectors.toList());
//                    if (collect != null && !collect.isEmpty()) {
                        for (PatientRecord patientRecord : records) {
                            RecordHealthTree diseasesResearchTree = new RecordHealthTree();
                            diseasesResearchTree.setId(patientRecord.getRecordId());
                            diseasesResearchTree.setNodeId("pr_" + patientRecord.getRecordId());
                            diseasesResearchTree.setParentNodeId("fl_" + 0);
                            diseasesResearchTree.setType(String.valueOf(keyValue));
                            diseasesResearchTree.setLabel(patientRecord.getName());
                            diseasesResearchTree.setPatientId(patientRecord.getPatientId());
                            diseasesResearchTree.setResearchId(patientRecord.getResearchId());
                            diseasesResearchTree.setFollowId(patientRecord.getFollowId());
                            diseasesResearchTree.setRecordId(patientRecord.getRecordId());
                            List<RecordHealthTree> diseasesResearchTreeList = processRecord(fMedicalForms, patientRecord.getFollowId(), patientRecord.getRecordId(), patientRecord.getResearchId(), keyValue, patientRecordHealths);
                            if (!CollectionUtils.isEmpty(diseasesResearchTreeList)) {
//                                diseasesResearchTree.setChildren(diseasesResearchTreeList);
                                return diseasesResearchTreeList;
                            }

                            tree.add(diseasesResearchTree);
                        }
//                    }
                }
            } else {
                if (records != null && !records.isEmpty()) {
                    //预警
                    List<PatientRecord> collect1 = records.stream().filter(record -> !"0".equals(record.getWarnStatus())).collect(Collectors.toList());
                    if (collect1 != null && !collect1.isEmpty()) {
                        for (PatientRecord patientRecord : collect1) {
                            RecordHealthTree diseasesResearchTree = new RecordHealthTree();
                            diseasesResearchTree.setId(patientRecord.getRecordId());
                            diseasesResearchTree.setNodeId("pr_" + patientRecord.getRecordId());
                            diseasesResearchTree.setParentNodeId("fl_" + 1);
                            diseasesResearchTree.setType(String.valueOf(keyValue));
                            diseasesResearchTree.setLabel(patientRecord.getName());
                            diseasesResearchTree.setPatientId(patientRecord.getPatientId());
                            diseasesResearchTree.setResearchId(patientRecord.getResearchId());
                            diseasesResearchTree.setFollowId(patientRecord.getFollowId());
                            diseasesResearchTree.setRecordId(patientRecord.getRecordId());
                            List<RecordHealthTree> diseasesResearchTreeList = processRecord(fMedicalForms, patientRecord.getFollowId(), patientRecord.getRecordId(), patientRecord.getResearchId(), keyValue, patientRecordHealths);
                            if (!CollectionUtils.isEmpty(diseasesResearchTreeList)) {
                                diseasesResearchTree.setChildren(diseasesResearchTreeList);
                            }
                            tree.add(diseasesResearchTree);
                        }
                    }
                }
            }
        }

        if (key == 3) {
            //患教问卷
            if (cmsArticles != null && !cmsArticles.isEmpty()) {
                List<CmsArticle> articles = cmsArticles.stream().filter(cmsArticle -> cmsArticle.getResearchId().equals(researchDiseasesVO.getResearchId())).collect(Collectors.toList());
                if (articles != null && !articles.isEmpty()) {
                    for (CmsArticle cmsArticle : articles) {
                        RecordHealthTree diseasesResearchTree = new RecordHealthTree();
                        diseasesResearchTree.setId(cmsArticle.getId());
                        diseasesResearchTree.setNodeId("pr_" + cmsArticle.getId());
                        diseasesResearchTree.setParentNodeId("fl_" + 2);
                        diseasesResearchTree.setType(String.valueOf(keyValue));
                        diseasesResearchTree.setLabel(cmsArticle.getTitle());
                        diseasesResearchTree.setResearchId(cmsArticle.getResearchId());
                        diseasesResearchTree.setArticleId(cmsArticle.getId());
                        diseasesResearchTree.setPatientId(patientId);
                        tree.add(diseasesResearchTree);
                    }
                }
            }
        }
        return tree;
    }

    /**
     * 第二级节点
     *
     * @return
     */
    private Map<Integer, String> level2Nodes() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "随访");
//        map.put(2, "预警");
//        map.put(3, "患教问卷");
        return map;
    }

    private List<RecordHealthTree> processRecord(List<FMedicalForm> fMedicalForms, Long followId, Long recordId, Long researchId, Integer keyValue, List<PatientRecordHealth> patientRecordHealths) {

        List<RecordHealthTree> tree = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fMedicalForms)) {
            List<FMedicalForm> list = fMedicalForms.stream().filter(item -> item.getFollowId().equals(followId)).collect(Collectors.toList());

            //通过followId 查询表单
            if (!CollectionUtils.isEmpty(list)) {
                for (int i = 0; i < list.size(); i++) {
                    FMedicalForm fMedicalForm = list.get(i);
                    RecordHealthTree diseasesResearchTree = new RecordHealthTree();
                    diseasesResearchTree.setId(Long.valueOf(i));
                    diseasesResearchTree.setNodeId("pr_" + i);
                    diseasesResearchTree.setParentNodeId("fl_" + 4);
                    diseasesResearchTree.setType(String.valueOf(keyValue));
                    diseasesResearchTree.setLabel(fMedicalForm.getMedicalName());
                    diseasesResearchTree.setFollowId(fMedicalForm.getFollowId());
                    diseasesResearchTree.setMedicalFormId(fMedicalForm.getMedicalFormId());
                    diseasesResearchTree.setRecordId(recordId);
                    diseasesResearchTree.setResearchId(researchId);

                    List<RecordHealthTree> recordHealthTrees = recordHealth(recordId, researchId, keyValue, patientRecordHealths, fMedicalForm.getHealthManageType());
                    if (!CollectionUtils.isEmpty(recordHealthTrees)) {
                        diseasesResearchTree.setChildren(recordHealthTrees);
                    }
                    tree.add(diseasesResearchTree);
                }
            }
        }
        return tree;
    }



    private List<RecordHealthTree> recordHealth( Long recordId, Long researchId, Integer keyValue, List<PatientRecordHealth> patientRecordHealths, Integer healthManageType) {

        List<RecordHealthTree> tree = new ArrayList<>();

        for (HealthManageEnum value : HealthManageEnum.values()) {

            for (int i = 0; i < patientRecordHealths.size(); i++) {

                if (value.getCode() == patientRecordHealths.get(i).getHealthManageType() && value.getCode() == healthManageType) {
                    PatientRecordHealth patientRecordHealth = patientRecordHealths.get(i);
                    RecordHealthTree diseasesResearchTree = new RecordHealthTree();

                    Date submitTime = patientRecordHealth.getSubmitTime();
                    String time = submitTime != null ? DateUtils.format(submitTime, "yyyy-MM-dd HH:mm:ss") : "";
                    diseasesResearchTree.setId((long) i);
                    diseasesResearchTree.setNodeId("pr_" + i);
                    diseasesResearchTree.setParentNodeId("fl_" + 4);
                    diseasesResearchTree.setType(String.valueOf(keyValue));
                    diseasesResearchTree.setLabel(HealthManageEnum.getName(patientRecordHealth.getHealthManageType()) + " " + time);
                    diseasesResearchTree.setFollowId(patientRecordHealths.get(i).getFollowId());
//                    diseasesResearchTree.setMedicalFormId(patientRecordHealths.get(i).getMedicalFormId());
                    diseasesResearchTree.setHealthId(patientRecordHealths.get(i).getHealthId());
                    diseasesResearchTree.setRecordId(recordId);
                    diseasesResearchTree.setResearchId(researchId);
                    tree.add(diseasesResearchTree);
                }

            }
        }
        return tree;
    }
}
