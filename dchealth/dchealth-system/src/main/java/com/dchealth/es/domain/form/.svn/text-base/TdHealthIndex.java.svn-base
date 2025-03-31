package com.dchealth.es.domain.form;

import com.dchealth.elasticsearch.annotation.ESIndices;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zjl
 * @since 2023/4/24
 * 唐都健康档案ES索引
 */
@ESIndices(indexName = "td_health_index")
@Data
public class TdHealthIndex implements Serializable {
    private static final long serialVersionUID = -87859961824273526L;

    /**
     * 病历数
     */
    private long docNum;

    private String id;
    /**
     * es中数据类型
     * 1：诊断信息；2：病人体征记录/一般检查；3：检查信息；4：检验信息；5：医嘱信息；6：处方信息；7：个人史，家族史
     */
    private String esDataType;
    private String baseId;
    /**
     * 登陆账号id
     */
    private Long accountId;
    /**
     * 就诊标识
     */
    private String registrySigns;
    /**
     * 病案号
     */
    private Long medicalRecordNo;
    /**
     * 患者姓名
     */
    private String name;
    /**
     * 出生日期
     */
    private String birthday;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 性别（0:未知，1：男，2：女）
     */
    private String sex;
    /**
     * 婚姻状态
     */
    private String maritalStatus;
    /**
     * 职业
     */
    private String profession;
    /**
     * 民族
     */
    private String nation;
    /**
     * 国籍
     */
    private String nationality;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 移动电话
     */
    private String mobile;
    /**
     * 现住址
     */
    private String presentAddress;
    /**
     * 户口地址
     */
    private String permanentAddress;
    /**
     * 文化程度 1=研究生；2=大学本科；3=大学专科和专科学校；4=中等专业学校；5=技工学校；6=高中；7=初中；8=小学；9=文盲或半文盲；10=不详
     */
    private String degreeOfEducation;
    /**
     * 血型 1=A型；2=B型；3=O型、4=AB型；5=不详/1=阴性；2=阳性；3=不详
     */
    private String abo;
    /**
     * 联系人
     */
    private String linkMan;
    /**
     * 关系
     */
    private String relationship;
    /**
     * 联系人住址
     */
    private String linkManAddress;
    /**
     * 入院途径
     */
    private String admissionHospitalWay;
    /**
     * 联系人电话
     */
    private String linkManMobile;
    /**
     * 入院日期
     */
    private Date admissionHospitalDate;
    /**
     * 入院科室
     */
    private String admissionHospitalDepartment;
    /**
     * 患者状态0失效1生效
     */
    private String patientStatus;
    /**
     * 患者Id
     */
    private String patientId;
    /**
     * 出院科室
     */
    private String deptDischargeFrom;
    /**
     * 出院时间
     */
    private Date dischargeDate;
    /**
     * 上次就诊日期examResultTime
     */
    private Date lastVisitDate;
    /**
     * 门诊时间
     */
    private Date patientClinicTime;
    // 一般检查
    /**
     * 记录日期
     */
    private Date generalInspectionDate;
    /**
     * 身高
     */
    private Float height;
    /**
     * 体重
     */
    private Float weight;
    /**
     * 腰围
     */
    private Float waistline;
    /**
     * 脉搏
     */
    private Float pulse;
    /**
     * 体质指数（BMI）
     */
    private String bmi;
    /**
     * 呼吸频率
     */
    private String respiratoryRate;
    /**
     * 脉率
     */
    private Float pulseRate;
    /**
     * 收缩压
     */
    private Float systolicBloodPressure;
    /**
     * 舒张压
     */
    private Float diastolicBloodPressure;
    /**
     * 体温
     */
    private Float bodyTemperature;
    // 诊断记录
    /**
     * 诊断类别
     */
    private String diagnosisType;
    /**
     * 诊断序号
     */
    private Integer diagnosisNo;
    /**
     * 诊断描述、终检结论
     */
    private String diagnosisDesc;
    /**
     * 诊断日期、终检日期
     */
    private Date diagnosisDate;
    /**
     * 终检医生
     */
    private String finalCheckDoctor;
    /**
     * 检查结果
     */
    private String checkDesc;
    /**
     * 结论医生
     */
    private String checkDoctor;
    /**
     * 结论日期
     */
    private Date checkDate;
    /**
     * 保健建议
     */
    private String healthAdvice;
    /**
     * 饮食建议
     */
    private String dietaryAdvice;
    /**
     * 运动建议
     */
    private String sportsAdvice;
    /**
     * 治疗建议
     */
    private String treatmentRecommendations;

    // 辅助检查
    /**
     * 健康档案检查申请号
     */
    private String examNo;
    /**
     * 报告时间
     */
    private Date examResultTime;
    /**
     * CT检查
     */
    private String ct;
    /**
     * PET—CT检查
     */
    private String petCt;
    /**
     * MRI检查
     */
    private String mri;
    /**
     * 心电图
     */
    private String ecg;
    /**
     * 胸片报告
     */
    private String cr;
    /**
     * X线（胸部以外）
     */
    private String crOther;
    /**
     * 心脏超声
     */
    private String cardiacUltrasound;
    /**
     * 腹部超声
     */
    private String abdominalUltrasound;
    /**
     * 专科超声男
     */
    private String specializedUltrasoundMale;
    /**
     * 专科超声女
     */
    private String specializedUltrasoundFemale;
    /**
     * 甲状腺超声
     */
    private String thyroidUltrasound;
    /**
     * 颈动脉超声
     */
    private String carotidUltrasound;
    /**
     * 血管多普勒超声
     */
    private String vascularDopplerUltrasound;
    /**
     * 胸腔镜
     */
    private String thoracoscope;
    /**
     * 支气管镜
     */
    private String bronchoscope;
    /**
     * 纵膈镜
     */
    private String mediastinoscopy;
    /**
     *肾小球滤过率
     */
    private String gfr;
    /**
     * 动脉硬化检测
     */
    private String arteriosclerosisDetection;
    /**
     * 肺功能检查
     */
    private String pulmonaryFunctionTest;
    /**
     * 血管造影
     */
    private String angiography;
    /**
     * 肺穿刺病理检查
     */
    private String pathologicalExamination;

    // 检验
    /**
     * 健康档案检验申请号
     */
    private String testNo;
    /**
     * 报告时间
     */
    private Date testResultTime;
    /*血液生化*/
    /**
     * 总蛋白
     */
    private String totalProtein;
    /**
     * 白蛋白
     */
    private String albumin;
    /**
     * 球蛋白
     */
    private String globulin;
    /**
     * 白蛋白/球蛋白
     * 白球比
     */
    private String albuminGlobulinRatio;
    /**
     * 天门冬氨酸氨基转移酶
     */
    private String aspartateAminotransferase;
    /**
     * 丙氨酸氨基转移酶
     */
    private String alanineAminotransferase;
    /**
     * AST/ALT比值
     */
    private String astAltRatio;
    /**
     * 总胆汁酸
     */
    private String totalBileAcid;
    /**
     * 总胆红素
     */
    private String totalBilirubin;
    /**
     * 直接胆红素
     */
    private String directBilirubin;
    /**
     * 间接胆红素
     */
    private String indirectBilirubin;
    /**
     * 碱性磷酸酶
     */
    private String alkalinePhosphatase;
    /**
     * 谷氨酰转移酶
     */
    private String glutamicPyruvicTransaminase;
    /**
     * 胆碱酯酶
     */
    private String cholinesterase;
    /**
     * 乳酸脱氢酶
     */
    private String lactateDehydrogenase;
    /**
     * α-羟丁酸脱氢酶
     */
    private String alphaHbDehydrogenase;
    /**
     * 肌酸激酶
     */
    private String creatineKinase;
    /**
     * 肌酸激酶同工酶
     */
    private String creatineKinaseIsoenzyme;
    /**
     * 尿素
     */
    private String bloodUreaNitrogen;
    /**
     * 肌酐
     */
    private String creatinine;
    /**
     * 尿酸
     */
    private String uricAcid;
    /**
     * 空腹血葡萄糖
     */
    private String fastingBloodGlucose;
    /**
     * 钾
     */
    private String potassium;
    /**
     * 钠
     */
    private String sodium;
    /**
     * 氯
     */
    private String chloride;
    /**
     * 钙
     */
    private String calcium;
    /**
     * 磷
     */
    private String phosphorus;
    /**
     * 镁
     */
    private String magnesium;
    /**
     * 二氧化碳
     */
    private String carbonDioxide;
    /**
     * 甘油三酯
     */
    private String triglycerides;
    /**
     * 总胆固醇
     */
    private String totalCholesterol;
    /**
     * 高密度脂蛋白胆固醇
     */
    private String hdlCholesterol;
    /**
     * 低密度脂蛋白胆固醇
     */
    private String ldlCholesterol;
    /**
     * 载脂蛋白A-1
     */
    private String apolipoproteinA1;
    /**
     * 载脂蛋白B
     */
    private String apolipoproteinB;
    /**
     * 载脂蛋白A-1/B
     */
    private String apolipoproteinA1bRatio;
    /**
     * 白球比 与上面的白蛋白/球蛋白 是一个东西，使用上面的 albuminGlobulinRatio
     */
    private String whiteBallRatio;
    /**
     * 血清胱抑素C
     */
    private String serumCystatinC;
    /**
     * 血糖
     */
    private String bloodSugar;
    /**
     * 餐后2小时血糖
     */
    private String bloodGlucose2h;
    /**
     * 糖化血红蛋白
     */
    private String glycosylatedHemoglobin;
    /**
     * OGTT  2小时血糖
     */
    private String ogttBloodGlucose;
    /**
     * 同型半胱氨酸
     */
    private String homocysteine;
    /**
     * 尿白蛋白肌酐比值（UACR）
     */
    private String uacr;
    /*血常规*/
//    /**
//     * 铅
//     */
//    private String pb;
//    /**
//     * 铜
//     */
//    private String copper;
//    /**
//     * 锌
//     */
//    private String zinc;
//    /**
//     * 铁
//     */
//    private String iron;
    /*血常规*/
    /**
     * 白细胞计数
     */
    private String whiteCellCount;
    /**
     * 红细胞计数
     */
    private String redCellCount;
    /**
     * 血红蛋白
     */
    private String hemoglobin;
    /**
     * 红细胞比容
     */
    private String hematocrit;
    /**
     * 平均红细胞体积
     */
    private String meanCorpusVolume;
    /**
     * 平均红细胞血红蛋白含量
     */
    private String meanCorpusHemoglobin;
    /**
     * 平均红细胞血红蛋白浓度
     */
    private String meanCorpusHemConcent;
    /**
     * 红细胞分布宽度-变异系数
     */
    private String redCellDisWidthVariation;
    /**
     * 血小板计数
     */
    private String plateletCount;
    /**
     * 平均血小板体积
     */
    private String meanPlateletVolume;
    /**
     * 血小板分布宽度
     */
    private String plateletDisWidth;
    /**
     * 淋巴细胞百分比
     */
    private String lymphocytePercentage;
    /**
     * 中性粒细胞百分比
     */
    private String neutrophilPercentage;
    /**
     * 淋巴细胞绝对值
     */
    private String absLymphocyteCount;
    /**
     * 中性粒细胞绝对值
     */
    private String absNeutrophilCount;
    /**
     * 红细胞分布宽度-标准差
     */
    private String redCellDisDeviation;
    /**
     * 血小板压积
     */
    private String plateletcrit;
    /**
     * 单核细胞百分比
     */
    private String monocytePercentage;
    /**
     * 单核细胞绝对值
     */
    private String absMonocyteCount;
    /**
     * 嗜酸性细胞百分比
     */
    private String eosinophilPercentage;
    /**
     * 嗜酸性细胞绝对值
     */
    private String absEosinophilCount;
    /**
     * 嗜碱性细胞百分比
     */
    private String basophilPercentage;
    /**
     * 嗜碱性细胞绝对值
     */
    private String absBasophilCount;

    //尿常规
    /**
     * 尿比重
     */
    private String specificGravityUrine;
    /**
     * 尿酸碱度
     */
    private String urinePh;
    /**
     * 尿白细胞
     */
    private String urineWhiteCells;
    /**
     * 尿亚硝酸盐
     */
    private String urineNitrite;
    /**
     * 尿蛋白质
     */
    private String urineProtein;
    /**
     * 尿糖
     */
    private String urineGlucose;
    /**
     * 尿酮体
     */
    private String urineKetone;
    /**
     * 尿胆原
     */
    private String urineUrobilinogen;
    /**
     * 尿胆红素
     */
    private String urineBilirubin;
    /**
     * 尿隐血
     */
    private String urineOccultBlood;

    /* 便常规*/
    /**
     * 便颜色
     */
    private String stoolColor;
    /**
     * 便性状
     */
    private String stoolConsistency;
    /**
     * 便红细胞
     */
    private String stoolRedCells;
    /**
     * 便白细胞
     */
    private String stoolWhiteCells;
    /**
     * 蛔虫卵
     */
    private String stoolParasiteEgg;
    /**
     * 鞭虫卵
     */
    private String stoolParasiteEggB;
    /**
     * 蛲虫卵
     */
    private String stoolParasiteEggR;
    /**
     * 绦虫卵
     */
    private String stoolParasiteEggT;
    /*心肌酶*/
    /**
     * 血清乳酸脱氢酶
     */
    private String serumLactateDehydrogenase;
    /**
     * 天门冬氨酸氨基转移酶 血液生化已存在
     */
//    private String aspartateAminotransferase;
    /**
     * 血清肌酸激酶-MB同工酶活性测定
     */
    private String serumCreatineKinaseMbAssay;
    /**
     * 血清肌酸激酶测定
     */
    private String serumCreatineKinaseAssay;
    /**
     * α-羟丁酸脱氢酶
     */
    private String hbdh;
    /* 肿瘤十二项 */
    /**
     * 甲胎蛋白(AFP)
     */
    private String alphaFetoprotein;
    /**
     * 癌胚抗原(CEA)
     */
    private String carcinoembryonicAntigen;
    /**
     * 癌抗原199(CA199)
     */
    private String carbohydrateAntigen199;
    /**
     * 癌抗原125
     */
    private String carbohydrateAntigen125;
    /**
     * 癌抗原242
     */
    private String carbohydrateAntigen242;
    /**
     * 癌抗原153
     */
    private String carbohydrateAntigen153;
    /**
     * 神经元特异性烯醇化酶NSE
     */
    private String neuronSpecificEnolase;
    /**
     * 癌抗原72-4
     */
    private String carbohydrateAntigen724;
    /**
     * 细胞角蛋白19片段(CYFRA21-2)
     */
    private String cytokeratin19Fragment;
    /**
     * 癌抗原50
     */
    private String carbohydrateAntigen50;
    /**
     * 游离前列腺特异性抗原
     */
    private String freePsmAntigen;
    /**
     * 前列腺特异性抗原
     */
    private String psmAntigen;

    /* 甲功八项 */
    /**
     * 三碘甲状腺原氨酸(T3)
     */
    private String triiodothyronine;
    /**
     * 甲状腺素（T4）
     */
    private String thyroxine;
    /**
     * 促甲状腺激素(TSH)
     */
    private String thyroidStimulatingHormone;
    /**
     * 游离三碘甲状腺原氨酸(T3)
     */
    private String freeTriiodothyronine;
    /**
     * 游离甲状腺素（FT4）
     */
    private String freeThyroxine;
    /**
     * 抗甲状腺球蛋白抗体
     */
    private String antithyroglobulinAntibody;
    /**
     * 抗甲状腺过氧化物酶抗体
     */
    private String antithyroidPeroxidaseAntibody;
    /**
     * 促甲状腺素受体抗体
     */
    private String thyrotropinReceptorAntibody;
    /**
     * 抗甲状腺微粒体抗体
     */
    private String antithyroidMicrosomeAntibody;
    /**
     * 甲状腺球蛋白
     */
    private String thyroglobulin;
    /**
     * 游离三碘甲状腺原氨酸
     */
    private String ft3;
    /* 激素六项 */
    /**
     * 卵泡刺激素（FSH）
     */
    private String follicleStimulatingHormone;
    /**
     * 黄体生成素（LH）
     */
    private String luteinizingHormone;
    /**
     * 雌二醇（E2）
     */
    private String estradiol;
    /**
     * 孕酮（P）
     */
    private String progesterone;
    /**
     * 睾酮（T）
     */
    private String testosterone;
    /**
     * 催乳素（PRL）
     */
    private String prolactin;

    /*凝血项目*/
    /**
     * 凝血酶原时间
     */
    private String prothrombinTime;
    /**
     * 活化部分凝血活酶时间
     */
    private String aptt;
    /**
     * 凝血酶时间
     */
    private String thrombinTime;
    /**
     * 纤维蛋白原含量
     */
    private String fibrinogen;
    /**
     * 纤维蛋白(原)降解产物
     */
    private String fdp;
    /**
     * D-二聚体定量
     */
    private String dDimerQuantitativeTest;
    /**
     * 国际标准化比值
     */
    private String internationalNormRatio;
    /**
     * 凝血酶原活动度
     */
    private String prothrombinActivity;
    /*血气分析*/
    /**
     * ph值
     */
    private String bloodGasPh;
    /**
     * 二氧化碳分压
     */
    private String pco2;
    /**
     * 氧分压
     */
    private String po2;
    /**
     * 红细胞压积
     */
    private String hct;
    /**
     * 血红蛋白
     */
    private String hb;
    /**
     * 剩余碱
     */
    private String be;
    /**
     * 标准碳酸根
     */
    private String chco3;
    /**
     * 实际碳酸根
     */
    private String actco3;
    /**
     * 总二氧化碳
     */
    private String tco2;
    /**
     * 氧饱和度50%时氧分压
     */
    private String p50act;
    /**
     * 血氧饱和度
     */
    private String so2;
    /**
     * 氧含量
     */
    private String cto2;
    /**
     * 氧容量
     */
    private String bo2;
    /* 医嘱*/
    /**
     * 病人本次住院标识
     */
    private String orderVisitId;
    /**
     * 医嘱序号
     */
    private Integer orderNo;
    /**
     * 医嘱子序号
     */
    private Integer orderSubNo;
    /**
     * 长期医嘱标志
     */
    private Integer repeatIndicator;
    /**
     * 医嘱类别
     */
    private String orderClass;
    /**
     * 医嘱正文
     */
    private String orderText;
    /**
     * 开医嘱时间
     */
    private Date enterDateTime;
    /*药品处方*/
    /**
     * 处方日期
     */
    private Date prescDate;
    /**
     * 处方号
     */
    private String prescNo;
    /**
     * 项目序号
     */
    private Integer itemNo;
    /**
     * 药品代码
     */
    private String drugCode;
    /**
     * 药品规格
     */
    private String drugSpec;
    /**
     * 药品名称
     */
    private String drugName;
    /*个人史，家族史*/
    private String personalHistory;
    private String familyHistory;

    private String personId;

    private String cardId;
    private String personName;

    private String phoneNumber;
    private String address;
    /**
     * 诊断日期-格式化YY_MM_DD
     */
    private Date resultDate;
    /**
     * 入院时间
     */
    private Date visitStartDate;
    /**
     * 出院时间
     */
    private Date visitEndDate;

    private String careSiteId;
}
