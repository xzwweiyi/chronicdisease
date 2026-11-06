package com.dchealth.template.service.impl;

import com.dchealth.common.utils.StringUtils;
import com.dchealth.medical.domain.PatientExamRecord;
import com.dchealth.medical.mapper.PatientExamRecordMapper;
import com.dchealth.template.domain.FMedicalExamForm;
import com.dchealth.template.domain.FMedicalExamItem;
import com.dchealth.template.domain.FMedicalExamSubject;
import com.dchealth.template.domain.ResArticle;
import com.dchealth.template.domain.vo.ExamStatisticVo;
import com.dchealth.template.mapper.FMedicalExamFormMapper;
import com.dchealth.template.mapper.FMedicalExamItemMapper;
import com.dchealth.template.mapper.FMedicalExamSubjectMapper;
import com.dchealth.template.service.IFMedicalStatisticalService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FMedicalStatisticalServiceImpl implements IFMedicalStatisticalService {
    private static final String combinationStr = "->";

    @Autowired
    private FMedicalExamSubjectMapper fMedicalExamSubjectMapper;

    @Autowired
    private FMedicalExamItemMapper fMedicalExamItemMapper;

    @Autowired
    private PatientExamRecordMapper patientExamRecordMapper;

    @Autowired
    private FMedicalExamFormMapper fMedicalExamFormMapper;

    @Override
    public List<ExamStatisticVo> queryStatisticalData(FMedicalExamSubject fMedicalExamSubject) throws Exception {
        //获取medical_form_id
        FMedicalExamForm fMedicalExamForm = fMedicalExamFormMapper.selectFMedicalExamFormByArticleId(fMedicalExamSubject.getArticleId());
        List<ExamStatisticVo> result = new ArrayList<>();
        if (Objects.isNull(fMedicalExamForm)) {
            return result;
        }
        fMedicalExamSubject.setMedicalFormId(fMedicalExamForm.getMedicalFormId());

        //获取问卷填写表名
        PatientExamRecord patientExamRecord = new PatientExamRecord();
        patientExamRecord.setArticleId(fMedicalExamSubject.getArticleId());
        patientExamRecord.setMedicalFormId(fMedicalExamSubject.getMedicalFormId());
        List<PatientExamRecord> patientExamRecords = patientExamRecordMapper.selectPatientExamRecordList(patientExamRecord);
        if (CollectionUtils.isNotEmpty(patientExamRecords) && StringUtils.isNotEmpty(patientExamRecords.get(0).getExamTable())) {
            fMedicalExamSubject.setTableName(patientExamRecords.get(0).getExamTable());
        } else {
            throw new Exception("当前问卷未填写，无法统计");
        }
        List<FMedicalExamSubject> list = fMedicalExamSubjectMapper.queryStatisticalData(fMedicalExamSubject);
        Map<String, List<FMedicalExamSubject>> collect = list.stream().filter(con -> Objects.nonNull(con.getPropertyName())).collect(Collectors.groupingBy(FMedicalExamSubject::getPropertyName));
        List<FMedicalExamItem> examItems = fMedicalExamItemMapper.selectFMedicalExamItemByFromId(fMedicalExamForm.getMedicalFormId());
        List<String> xLine = new ArrayList<>();
        List<String> xLine3 = new ArrayList<>();
        List<Long> yLine = new ArrayList<>();
        List<Map<String, Object>> xLine4 = new ArrayList<>();
        List<Map<String, Object>> xLine6 = new ArrayList<>();
        List<Map<String, Object>> xLine7 = new ArrayList<>();
        for (String name : collect.keySet()) {
            List<FMedicalExamSubject> tableData = new ArrayList<>();
            ExamStatisticVo examStatisticVo = new ExamStatisticVo();
            List<FMedicalExamSubject> subjectList = collect.get(name);
            if (CollectionUtils.isNotEmpty(subjectList)) {
                examStatisticVo.setPropertyCode(subjectList.get(0).getPropertyCode());
                examStatisticVo.setPropertyType(subjectList.get(0).getPropertyType());
                examStatisticVo.setId(subjectList.get(0).getId());
            }
            List<FMedicalExamSubject> list1 = subjectList.stream().filter(a -> Objects.nonNull(a.getName())).collect(Collectors.toList());
            //查询字典
//            FMedicalExamItem dictData = new FMedicalExamItem();
//            dictData.setPropertyId(subjectList.get(0).getId());

            if (subjectList.size() > 0) {
                xLine = new ArrayList<>();
                yLine = new ArrayList<>();
                xLine4 = new ArrayList<>();
                xLine6 = new ArrayList<>();
                xLine7 = new ArrayList<>();
                List<FMedicalExamItem> dataList = examItems.stream()
                        .filter(data -> Objects.nonNull(data.getPropertyId()) && data.getPropertyId().longValue() == examStatisticVo.getId())
                        .collect(Collectors.toList());
                for (FMedicalExamItem data : dataList) {
                    FMedicalExamSubject examSubject = new FMedicalExamSubject();

                    examSubject.setName(data.getItemLabel());
                    Optional<FMedicalExamSubject> any = list.stream().filter(res -> data.getPropertyId().longValue() == res.getId().longValue() && StringUtils.equals(res.getName(), data.getItemValue())).findAny();
                    if (any.isPresent()) {
                        examSubject.setPropertyCode(any.get().getPropertyCode());
                        examSubject.setId(any.get().getId());
                        examSubject.setValue(any.get().getValue());
                    } else {
                        examSubject.setValue(0l);
                    }
//                    if (list1.size() == 0) {
//                        for (FMedicalExamSubject subject : list) {
//                            subjectList.remove(subject);
//                        }
//                    }
                    tableData.add(examSubject);
//                    subjectList.add(examSubject);
                }
                //                    折线图
//                Map<String, Object> res4 = new HashMap<>();
//                if (CollectionUtils.isNotEmpty(collect.keySet())){
//                    res4.put("name", collect.get(name).get(0).getPropertyName());
//                    res4.put("type", "line");
//                    res4.put("data", yLine);
//                    xLine4.add(res4);
//                    xLine3.add(collect.get(name).get(0).getPropertyName());
//                }


                for (FMedicalExamSubject subject : tableData) {
                    //查询所有选项内容
                    if (StringUtils.isNotEmpty(subject.getName())) {
                        xLine.add(subject.getName());
                        yLine.add(subject.getValue());
//                     饼图
                        Map<String, Object> res6 = new HashMap<>();
                        res6.put("name", subject.getName());
                        res6.put("value", subject.getValue());
                        xLine6.add(res6);

//                     环图
                        Map<String, Object> res7 = new HashMap<>();
                        res7.put("name", subject.getName());
                        res7.put("value", subject.getValue());
                        xLine7.add(res7);
                    }
                }
            }
            if (StringUtils.isNotEmpty(name)) {

                examStatisticVo.setPropertyName(name);
                examStatisticVo.setTableData(tableData);
                examStatisticVo.setXLine(xLine);
                examStatisticVo.setYLine(yLine);
//             折线图数据
                examStatisticVo.setXLine2(xLine);
                examStatisticVo.setXLine3(xLine3);
                examStatisticVo.setXLine4(xLine4);

//            饼图数据
                examStatisticVo.setXLine5(xLine);
                examStatisticVo.setXLine6(xLine6);

//            环形图
                examStatisticVo.setXLine7(xLine7);
                result.add(examStatisticVo);
            }
        }
        return result;
    }

    @Override
    public List<ExamStatisticVo> queryAnalysisData(long id, ExamStatisticVo examStatisticVo) {
        // 结果
        List<ExamStatisticVo> result = new ArrayList<>();

        // 查询问卷所有题干
        List<FMedicalExamSubject> fMedicalExamSubjects = fMedicalExamSubjectMapper.selectFMedicalExamSubjectByFromId(id);

        // 查询问卷所有选项
        FMedicalExamItem dictData = new FMedicalExamItem();
        dictData.setMedicalFormId(id);
        List<FMedicalExamItem> subjectAllOptions = fMedicalExamItemMapper.selectFMedicalExamItemList(dictData);

        // 获取答案
        PatientExamRecord patientExamRecord = new PatientExamRecord();
        patientExamRecord.setMedicalFormId(id);
        List<PatientExamRecord> patientExamRecords = patientExamRecordMapper.selectPatientExamRecordList(patientExamRecord);
        // 查询所有答案
        FMedicalExamSubject subject = new FMedicalExamSubject();
        if (CollectionUtils.size(patientExamRecords) > 0) {
            subject.setTableName(patientExamRecords.get(0).getExamTable());
        } else {
            return result;
        }

        List<ResArticle> resArticles = fMedicalExamSubjectMapper.selectFMedicalExamSubjectResArticle(subject);

        Map<Long, List<ResArticle>> resArticlesAns = resArticles.stream().filter(res -> Objects.nonNull(res.getExamRecordId())).collect(Collectors.groupingBy(ResArticle::getExamRecordId));

        // 遍历因变量，一组因变量代表一组要生成的数据
        for (long y : examStatisticVo.getYList()) {
            // 找出某一道题的所有选项
            List<FMedicalExamItem> collect = subjectAllOptions.stream().filter(option -> option.getPropertyId().longValue() == y).collect(Collectors.toList());

            // 定义结果对象,每个因变量都对应一组结果
            ExamStatisticVo examStatisticVo1 = new ExamStatisticVo();
            examStatisticVo1.setXList(examStatisticVo.getXList());
            examStatisticVo1.setYList(examStatisticVo.getYList());
            result.add(examStatisticVo1);

            List<String> tableFirst = new ArrayList<>();
            // 表格的首行第一列，固定格式
            tableFirst.add("x\\y");
            collect.stream().forEach(yl -> tableFirst.add(yl.getItemLabel()));
            examStatisticVo1.getTable().add(tableFirst);

            // 自变量对应的选项列表 的集合
            List<List<String>> xAllLabelList = new ArrayList<>();

            // 遍历自变量列表
            for (long x : examStatisticVo.getXList()) {
                // 找到自变量对应的题目
                Optional<FMedicalExamSubject> any1 = fMedicalExamSubjects.stream().filter(su -> su.getId() == x).findAny();
                if (!any1.isPresent()) {
                    continue;
                }
                // 找到自变量题目对应的所有选项
                xAllLabelList.add(subjectAllOptions.stream().filter(option -> option.getPropertyId().longValue() == x).map(FMedicalExamItem::getItemLabel).collect(Collectors.toList()));
            }

            buildTableRow(examStatisticVo1, tableFirst, xAllLabelList);

            buildTableData(examStatisticVo1, y, collect.stream().map(FMedicalExamItem::getItemLabel).collect(Collectors.toList()), resArticlesAns);

        }

        return result;
    }


    /**
     * 构建表数据
     *
     * @param examStatisticVo1 参数及结果对象
     * @param tableFirstRow    首行
     * @param xAllLabelList    所有列
     */
    public void buildTableRow(ExamStatisticVo examStatisticVo1, List<String> tableFirstRow, List<List<String>> xAllLabelList) {
        if (CollectionUtils.size(xAllLabelList) == 1) {
            xAllLabelList.get(0).stream().forEach(label -> {
                examStatisticVo1.getXLabelTempList().add(label);
//                examStatisticVo1.getSubjectTempList().add(String.valueOf(examStatisticVo1.getXList().get(0)));
                buildTableTemp(examStatisticVo1, tableFirstRow, label);
            });
        } else if (CollectionUtils.size(xAllLabelList) == 2) {
            List<String> combinations = combination(xAllLabelList.get(0), xAllLabelList.get(1));
            combinations.stream().forEach(label -> examStatisticVo1.getXLabelTempList().add(label));
            for (String re : combinations) {
                buildTableTemp(examStatisticVo1, tableFirstRow, re);
            }
        } else if (CollectionUtils.size(xAllLabelList) == 3) {
            List<List<String>> temp = new ArrayList<>();
            for (int i = 0; i < CollectionUtils.size(xAllLabelList) - 1; i++) {
                List<String> ls1 = xAllLabelList.get(i);
                List<String> ls2 = xAllLabelList.get(i + 1);
                temp.add(combination(ls1, ls2));
            }
            List<String> res = new LinkedList<>();
            for (int i = 0; i < CollectionUtils.size(temp) - 1; i++) {
                res.addAll(merge(temp.get(i), temp.get(i + 1)));
            }
            for (String re : res) {
                examStatisticVo1.getXLabelTempList().add(re);
                buildTableTemp(examStatisticVo1, tableFirstRow, re);
            }
        }
    }

    public void buildTableData(ExamStatisticVo examStatisticVo1, long ySubjectId, List<String> yLabelList, Map<Long, List<ResArticle>> userExamAnswer) {

        for (int tbIndex = 0; tbIndex < CollectionUtils.size(yLabelList); tbIndex++) {
            List<String> xLabelTempList = examStatisticVo1.getXLabelTempList();
            // 折线图，map
            Map<String, Object> zline4 = new HashMap<>();
            zline4.put("name", yLabelList.get(tbIndex));
            zline4.put("type", "line");
            List<Long> line4 = new ArrayList<>();
            zline4.put("data", line4);

            Map<String, Object> barLine = new HashMap<>();
            barLine.put("name", yLabelList.get(tbIndex));
            barLine.put("type", "bar");
            List<Long> barData = new ArrayList<>();
            barLine.put("data", barData);

            for (int i = 0; i < CollectionUtils.size(xLabelTempList); i++) {
                StringBuilder xyName = new StringBuilder();
                xyName.append(xLabelTempList.get(i));
                boolean firstYLabel = true;

                String[] labels = xLabelTempList.get(i).split(combinationStr);
                long initValue = 0;
                Set<Long> records = userExamAnswer.keySet();
                for (long recordId : records) {
                    List<ResArticle> fMedicalExamAnswers1 = userExamAnswer.get(recordId);
                    boolean goodAnswer = true;  // 默认问卷符合自变量
                    for (int j = 0; j < labels.length; j++) {
                        long xSubjectId = examStatisticVo1.getXList().get(j);
                        int finalJ = j;
                        Optional<ResArticle> any2 =
                                fMedicalExamAnswers1.stream().filter(ans -> ans.getPropertyId().longValue() == xSubjectId &&
                                        StringUtils.equals(ans.getPropertyValue(), labels[finalJ])).findAny();
                        if (!any2.isPresent()) {
                            examStatisticVo1.getTable().get(i + 1).set(tbIndex + 1, "0");
                            goodAnswer = false;
                            break;
                        }
                    }
                    int finalTbIndex = tbIndex;
                    Optional<ResArticle> any3 =
                            fMedicalExamAnswers1.stream().filter(ans -> ans.getPropertyId().longValue() == ySubjectId &&
                                    StringUtils.equals(ans.getPropertyValue(), yLabelList.get(finalTbIndex))).findAny();
                    if (any3.isPresent() && goodAnswer) {
                        initValue++;
                    }
                    if (firstYLabel) {
                        xyName.append(combinationStr).append(yLabelList.get(finalTbIndex));
                        firstYLabel = false;
                    }
                }
                line4.add(initValue);
                barData.add(initValue);
                examStatisticVo1.getTable().get(i + 1).set(tbIndex + 1, String.valueOf(initValue));
                // 环图
                Map<String, Object> map = new HashMap<>();
                map.put("name", xyName.toString());
                map.put("value", initValue);
                examStatisticVo1.getXLine7().add(map);

                // 饼图
                Map<String, Object> map2 = new HashMap<>();
                map2.put("name", xyName.toString());
                map2.put("value", initValue);
                examStatisticVo1.getXLine5().add(xyName.toString());
                examStatisticVo1.getXLine6().add(map2);

//                    柱状图
//                examStatisticVo1.getXLine().add(xyName.toString());
//                examStatisticVo1.getYLine().add(initValue);
            }
            // 柱状图
            examStatisticVo1.getYLine2().add(barLine);
            // 折线图
            examStatisticVo1.getXLine4().add(zline4);
        }
        // 折线图
        examStatisticVo1.getXLine2().addAll(examStatisticVo1.getXLabelTempList());
        examStatisticVo1.getXLine3().addAll(yLabelList);
        // 柱状图
        examStatisticVo1.getXLine().addAll(examStatisticVo1.getXLabelTempList());
        examStatisticVo1.getYLine3().addAll(yLabelList);
    }

    private void buildTableTemp(ExamStatisticVo examStatisticVo1, List<String> tableFirstRow, String re) {
        List<String> row = new LinkedList<>();
        row.add(re);
        // 构建默认行
        initYData(row, CollectionUtils.size(tableFirstRow) - 1);
        examStatisticVo1.getTable().add(row);
    }


    private List<String> combination(List<String> ls1, List<String> ls2) {
        List<String> res = new ArrayList<>();
        for (String s1 : ls1) {
            for (String s2 : ls2) {
                StringBuilder sb = new StringBuilder();
                sb.append(s1).append(combinationStr).append(s2);
                res.add(sb.toString());
            }
        }
        return res;
    }

    private List<String> merge(List<String> ls1, List<String> ls2) {
        List<String> res = new ArrayList<>();
        for (String s1 : ls1) {
            for (String s2 : ls2) {
                String[] s1arr = s1.split(combinationStr);
                String[] s2arr = s2.split(combinationStr);
                if (StringUtils.equals(s1arr[s1arr.length - 1], s2arr[0])) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(s1arr[0]).append(combinationStr).append(s2arr[0]).append(combinationStr);
                    sb.append(s2arr[1]);
                    res.add(sb.toString());
                }
            }
        }
        return res;
    }

    private void initYData(List<String> tableY, int size) {
        for (int i = 0; i < size; i++) {
            tableY.add("0");
        }
    }
}
