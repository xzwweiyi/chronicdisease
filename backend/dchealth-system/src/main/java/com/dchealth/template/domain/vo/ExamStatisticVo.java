package com.dchealth.template.domain.vo;


import com.dchealth.common.core.domain.BaseEntity;
import com.dchealth.template.domain.FMedicalExamSubject;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 属性题目对象 f_medical_exam_subject
 *
 * @author genemax1107@aliyun.com
 * @date 2022-11-07
 */
@Data
public class ExamStatisticVo extends BaseEntity {


    private long id;
    private long medicalFormId;

    private static final long serialVersionUID = 1L;

    /**
     * 请求参数自变量
     */
    private List<Long> xList = new ArrayList<>();
    /**
     * 请求参数因变量
     */
    private List<Long> yList = new ArrayList<>();

    /**
     * 属性名称
     */
    private String propertyName;

    private String propertyCode;

    private String propertyType;

    private List<FMedicalExamSubject> tableData = new LinkedList<>();
    /**
     * 结果表数据
     */
    private List<List<String>> table = new LinkedList<>();
    /**
     * 自变量选项对应题目id
     */
    private List<String> subjectTempList = new ArrayList<>();

    /**
     * 自变量临时存储(自变量的全组合)
     */
    private List<String> xLabelTempList = new ArrayList<>();

    /**
     * 柱状图
     */
//     x轴
    private List<String> xLine = new ArrayList<>();
    private List<Long> yLine = new ArrayList<>();
    private List<Map<String, Object>> yLine2 = new ArrayList<>();
    //    柱状图示例(legend)
    private List<String> yLine3 = new ArrayList<>();


    /**
     * 折线图
     */
    private List<String> xLine2 = new ArrayList<>();
    private List<String> xLine3 = new ArrayList<>();
    private List<Map<String, Object>> xLine4 = new ArrayList<>();

    /**
     * 饼图
     */
    private List<String> xLine5 = new ArrayList<>();
    private List<Map<String, Object>> xLine6 = new ArrayList<>();


    /**
     * 环形图
     */
    private List<Map<String, Object>> xLine7 = new ArrayList<>();

}

