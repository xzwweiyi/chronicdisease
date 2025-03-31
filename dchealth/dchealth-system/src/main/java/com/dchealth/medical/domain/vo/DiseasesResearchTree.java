package com.dchealth.medical.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.dchealth.cms.domain.CmsArticle;
import com.dchealth.common.core.domain.entity.SysDictData;
import com.dchealth.compatibility.vo.VisitListVO;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.ResearchPatient;
import com.dchealth.research.domain.vo.ResearchDiseasesVO;
import com.dchealth.template.domain.FMedicalForm;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class DiseasesResearchTree implements Serializable {

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

    /**
     * 模板Id
     */
    private Long medicalFormId;
    private String visitId;//就诊ID

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DiseasesResearchTree> children;

    public DiseasesResearchTree() {
    }

    public DiseasesResearchTree(List<ResearchDiseasesVO> list, List<PatientRecord> patientRecords, List<CmsArticle> cmsArticles, Long patientId, List<FMedicalForm> fMedicalForms) {
        this.type = "0";
        this.parentNodeId = "0";
        this.nodeId = "0";
        this.label = "队列";
        this.patientId = patientId;

        //按照病种分组
        Map<Long, List<ResearchDiseasesVO>> map = list.stream().filter(x -> x.getDiseaseId() != null).collect(Collectors.groupingBy(ResearchDiseasesVO::getDiseaseId));
        List<DiseasesResearchTree> dis = new ArrayList<>();
        if (map != null && !map.isEmpty()) {
            map.forEach((k, v) -> {
                if (v != null && !v.isEmpty()) {
                    //第一级节点，病种
                    ResearchDiseasesVO l = v.get(0);
                    DiseasesResearchTree level1Tree = new DiseasesResearchTree();
                    level1Tree.setId(l.getDiseaseId());
                    level1Tree.setNodeId("rd_" + l.getDiseaseId().longValue());
                    level1Tree.setParentNodeId("0");
                    level1Tree.setType("1");
                    level1Tree.setLabel(l.getDiseaseName());

                    List<DiseasesResearchTree> rs = new ArrayList<>();
                    for (ResearchDiseasesVO researchDiseasesVO : v) {
                        if (researchDiseasesVO.getResearchId() != null) {
                            //第二级节点,队列
                            DiseasesResearchTree level2Tree = new DiseasesResearchTree();
                            level2Tree.setId(researchDiseasesVO.getResearchId());
                            level2Tree.setNodeId("ri_" + researchDiseasesVO.getResearchId().longValue());
                            level2Tree.setParentNodeId("rd_" + l.getDiseaseId().longValue());
                            level2Tree.setType("2");
                            level2Tree.setLabel(researchDiseasesVO.getProjectName());
                            level2Tree.setResearchId(researchDiseasesVO.getResearchId());


                            List<DiseasesResearchTree> child = new ArrayList<>();
                            Map<Integer, String> level3NodeMap = level2Nodes();
                            level3NodeMap.forEach((key, value) -> {
                                //第三级节点
                                DiseasesResearchTree level3Tree = new DiseasesResearchTree();
                                level3Tree.setId(Long.valueOf(key));
                                level3Tree.setNodeId("fl_" + 0);
                                level3Tree.setParentNodeId("ri_" + researchDiseasesVO.getResearchId().longValue());
                                level3Tree.setType(String.valueOf(key));
                                level3Tree.setLabel(value);
                                level3Tree.setResearchId(researchDiseasesVO.getResearchId());

                                //第四级节点
                                List<DiseasesResearchTree> diseasesResearchTreeList = buildLevel4Node(patientRecords, cmsArticles, key, researchDiseasesVO, fMedicalForms, patientId, key);
                                if (!CollectionUtils.isEmpty(diseasesResearchTreeList)) {
                                    level3Tree.setChildren(diseasesResearchTreeList);
                                }
                                child.add(level3Tree);
                            });

                            level2Tree.setChildren(child);
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

    public DiseasesResearchTree(List<ResearchPatient> researchPatients, List<VisitListVO> patientHealthRecord, String personId, List<SysDictData> sysDictData) {
        this.setNodeId("0");
        this.setParentNodeId("0");
        this.setLabel("患者健康档案");
        this.setType("0");
        this.setPersonId(personId);
        List<DiseasesResearchTree> pht = new ArrayList();
        if (patientHealthRecord != null && patientHealthRecord.size() > 0) {
            for (VisitListVO pr : patientHealthRecord) {
                DiseasesResearchTree ptree = new DiseasesResearchTree();
//                ptree.setId(pr.get);
//                ptree.setNodeId("rd_" + pr.getRecordId());
                ptree.setParentNodeId(this.nodeId);
//                ptree.setLabel(DateUtils.parseDateToStrShort(pr.getCreateTime()));
                ptree.setType("1");
                ptree.setPersonId(personId);
                List<DiseasesResearchTree> tree = new ArrayList();
                for (SysDictData sy : sysDictData) {
                    //获取疾病类型
                    DiseasesResearchTree tr = new DiseasesResearchTree();
                    tr.setId(sy.getDictCode());
                    tr.setNodeId("ri_" + sy.getDictCode());
//                    tr.setParentNodeId("rd_" + pr.getRecordId());
                    tr.setLabel(sy.getDictLabel());
                    tr.setType("2");
                    tr.setPersonId(personId);
//                    tr.setRecordId(pr.getRecordId());
                    tree.add(tr);
                }
                ptree.setChildren(tree);
                pht.add(ptree);
            }
        }
        this.children = pht;
    }


    /**
     * 构建第四层节点
     *
     * @param patientRecords
     * @param cmsArticles
     * @param key
     * @return
     */
    private List<DiseasesResearchTree> buildLevel4Node(List<PatientRecord> patientRecords, List<CmsArticle> cmsArticles,
                                                       Integer key, ResearchDiseasesVO researchDiseasesVO,
                                                       List<FMedicalForm> fMedicalForms, Long patientIdI, Integer keyValue) {

        List<DiseasesResearchTree> tree = new ArrayList<>();
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
                            DiseasesResearchTree diseasesResearchTree = new DiseasesResearchTree();
                            diseasesResearchTree.setId(patientRecord.getRecordId());
                            diseasesResearchTree.setNodeId("pr_" + patientRecord.getRecordId());
                            diseasesResearchTree.setParentNodeId("fl_" + 0);
                            diseasesResearchTree.setType(String.valueOf(keyValue));
                            diseasesResearchTree.setLabel(patientRecord.getName());
                            diseasesResearchTree.setPatientId(patientRecord.getPatientId());
                            diseasesResearchTree.setResearchId(patientRecord.getResearchId());
                            diseasesResearchTree.setFollowId(patientRecord.getFollowId());
                            diseasesResearchTree.setRecordId(patientRecord.getRecordId());
                            List<DiseasesResearchTree> diseasesResearchTreeList = processRecord(fMedicalForms, patientRecord.getFollowId(), patientRecord.getRecordId(), patientRecord.getResearchId(), keyValue);
                            if (!CollectionUtils.isEmpty(diseasesResearchTreeList)) {
                                diseasesResearchTree.setChildren(diseasesResearchTreeList);
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
                            DiseasesResearchTree diseasesResearchTree = new DiseasesResearchTree();
                            diseasesResearchTree.setId(patientRecord.getRecordId());
                            diseasesResearchTree.setNodeId("pr_" + patientRecord.getRecordId());
                            diseasesResearchTree.setParentNodeId("fl_" + 1);
                            diseasesResearchTree.setType(String.valueOf(keyValue));
                            diseasesResearchTree.setLabel(patientRecord.getName());
                            diseasesResearchTree.setPatientId(patientRecord.getPatientId());
                            diseasesResearchTree.setResearchId(patientRecord.getResearchId());
                            diseasesResearchTree.setFollowId(patientRecord.getFollowId());
                            diseasesResearchTree.setRecordId(patientRecord.getRecordId());
                            List<DiseasesResearchTree> diseasesResearchTreeList = processRecord(fMedicalForms, patientRecord.getFollowId(), patientRecord.getRecordId(), patientRecord.getResearchId(), keyValue);
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
                        DiseasesResearchTree diseasesResearchTree = new DiseasesResearchTree();
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

    private List<DiseasesResearchTree> processRecord(List<FMedicalForm> fMedicalForms, Long followId, Long recordId, Long researchId, Integer keyValue) {

        List<DiseasesResearchTree> tree = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fMedicalForms)) {
            List<FMedicalForm> list = fMedicalForms.stream().filter(item -> item.getFollowId().equals(followId)).collect(Collectors.toList());

            //通过followId 查询表单
            if (!CollectionUtils.isEmpty(list)) {
                for (int i = 0; i < list.size(); i++) {
                    FMedicalForm fMedicalForm = list.get(i);
                    DiseasesResearchTree diseasesResearchTree = new DiseasesResearchTree();
                    diseasesResearchTree.setId(Long.valueOf(i));
                    diseasesResearchTree.setNodeId("pr_" + i);
                    diseasesResearchTree.setParentNodeId("fl_" + 4);
                    diseasesResearchTree.setType(String.valueOf(keyValue));
                    diseasesResearchTree.setLabel(fMedicalForm.getMedicalName());
                    diseasesResearchTree.setFollowId(fMedicalForm.getFollowId());
                    diseasesResearchTree.setMedicalFormId(fMedicalForm.getMedicalFormId());
                    diseasesResearchTree.setRecordId(recordId);
                    diseasesResearchTree.setResearchId(researchId);
                    tree.add(diseasesResearchTree);
                }
            }
        }
        return tree;
    }
}
