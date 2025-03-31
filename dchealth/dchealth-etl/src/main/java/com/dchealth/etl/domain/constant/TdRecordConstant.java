package com.dchealth.etl.domain.constant;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 唐都健康档案检验项目与系统表对照类
 * @author zjl
 * @since 2023/4/3
 */
public class TdRecordConstant {

    /**
     * 血常规
     */
    public static Map<String,String> bloodRoutineKeys = new HashMap<String,String>(){
        {
            //白细胞计数
            put("1192","whiteCellCount");
            //红细胞计数
            put("1179","redCellCount");
            put("1180","hemoglobin");
            put("1181","hematocrit");
            put("1182","meanCorpusVolume");
            put("1183","meanCorpusHemoglobin");
            put("1184","meanCorpusHemConcent");
            put("1185","redCellDisWidthVariation");
            put("1187","plateletCount");
            put("1189","meanPlateletVolume");
            put("1188","plateletDisWidth");
            put("1170","lymphocytePercentage");
            put("1169","neutrophilPercentage");
            put("1175","absLymphocyteCount");
            put("1174","absNeutrophilCount");
            put("1186","redCellDisDeviation");
            put("5909","plateletcrit");
            put("1171","monocytePercentage");
            put("1176","absMonocyteCount");
            put("1172","eosinophilPercentage");
            put("1177","absEosinophilCount");
            put("1173","basophilPercentage");
            put("1178","absBasophilCount");
        }
    };

    /**
     * 尿常规
     */
    public static Map<String,String> urineRoutineKeys = new HashMap<String,String>(){
        {
            //尿比重
            put("1147","specificGravityUrine");
            //尿酸碱度
            put("2016","urinePh");
            //尿白细胞
            put("1144","urineWhiteCells");
            //尿亚硝酸盐
            put("1149","urineNitrite");
            //尿蛋白质
            put("2065","urineProtein");
            //尿糖
            put("1145","urineGlucose");
            //尿酮体
            put("1150","urineKetone");
            //尿胆原
            put("1151","urineUrobilinogen");
            //尿胆红素
            put("1146","urineBilirubin");
            //尿隐血
            put("5082","urineOccultBlood");
        }
    };
    /**
     * 便常规
     */
    public static Map<String,String> stoolRoutineKeys = new HashMap<String,String>(){
        {
            //便颜色
            put("1205","stoolColor");
            //便性状
            put("1085","stoolConsistency");
            //便红细胞
            put("1087","stoolRedCells");
            //便白细胞
            put("1088","stoolWhiteCells");
            //便虫卵 1090，1091,1092，1094
            put("1090","stoolParasiteEgg");
            put("1091","stoolParasiteEggB");
            put("1092","stoolParasiteEggR");
            put("1094","stoolParasiteEggT");

        }
    };

    /**
     * 血液生化
     */
    public static Map<String,String> bloodBiochemistryKeys = new HashMap<String,String>(){
        {
            //总蛋白
            put("2056","totalProtein");
            //白蛋白
            put("2057","albumin");
            //球蛋白
            put("2059","globulin");
            //白蛋白/球蛋白
            put("2004","albuminGlobulinRatio");
            //天门冬氨酸氨基转移酶
            put("2055","aspartateAminotransferase");
            //丙氨酸氨基转移酶
            put("2054","alanineAminotransferase");
            //AST/ALT比值
            put("2005","astAltRatio");
            //总胆汁酸
            put("2003","totalBileAcid");
            //总胆红素
            put("2060","totalBilirubin");
            //直接胆红素
            put("2061","directBilirubin");
            //间接胆红素
            put("2046","indirectBilirubin");
            //碱性磷酸酶
            put("2001","alkalinePhosphatase");
            //ꝩ-谷氨酰转移酶
            put("2002","glutamicPyruvicTransaminase");
            //胆碱酯酶
            put("2049","cholinesterase");
            //乳酸脱氢酶
            put("2088","lactateDehydrogenase");
            //α-羟丁酸脱氢酶
            put("2020","alphaHbDehydrogenase");
            //肌酸激酶
            put("2006","creatineKinase");
            //肌酸激酶同工酶
            put("2021","creatineKinaseIsoenzyme");
            //尿素
            put("2026","bloodUreaNitrogen");
            //肌酐
            put("2017","creatinine");
            //尿酸
            put("2016","uricAcid");
            //空腹血葡萄糖
            put("5238","fastingBloodGlucose");

            //糖化血红蛋白
            put("5001","glycosylatedHemoglobin");
            //餐后小时血糖
            put("5019","bloodGlucose2h");

            //钾
            put("2011","potassium");
            //钠
            put("2012","sodium");
            //氯
            put("2013","chloride");
            //钙
            put("2030","calcium");
            //磷
            put("2045","phosphorus");
            //镁
            put("2085","magnesium");
            //二氧化碳
            put("2014","carbonDioxide");
            //甘油三酯
            put("2008","triglycerides");
            //总胆固醇
            put("2007","totalCholesterol");
            //高密度脂蛋白胆固醇
            put("2010","hdlCholesterol");
            //低密度脂蛋白胆固醇
            put("2051","ldlCholesterol");
            //载脂蛋白A-1
            put("2023","apolipoproteinA1");
            //载脂蛋白B
            put("2024","apolipoproteinB");
            //载脂蛋白A-1/B
//            put("A-1/B","apolipoproteinA1bRatio");
            //同型半胱氨酸
            put("5695","homocysteine");
            //尿白蛋白肌酐比值（UACR）
            put("5682","uacr");
        }
    };

    /**
     * 心肌酶
     */
    public static Map<String,String> myocardiaKeys = new HashMap<String,String>(){
        {
            //血清乳酸脱氢酶
            put("2088","serumLactateDehydrogenase");
            //天门冬氨酸氨基转移酶
            put("2055","aspartateAminotransferase");
            //血清肌酸激酶-MB同工酶活性测定
            put("4168","serumCreatineKinaseMbAssay");
            //血清肌酸激酶测定
            put("2006","serumCreatineKinaseAssay");
        }
    };

    /**
     * 肿瘤十二项
     */
    public static Map<String,String> tumorKeys = new HashMap<String,String>(){
        {
            //甲胎蛋白(AFP)
            put("4157","alphaFetoprotein");
            //癌胚抗原(CEA)
            put("4121","carcinoembryonicAntigen");
            //癌抗原199(CA199)
            put("4159","carbohydrateAntigen199");
            //癌抗原125(CA125)
            put("4123","carbohydrateAntigen125");
            //癌抗原242
            put("5050","carbohydrateAntigen242");
            //癌抗原153
            put("5016","carbohydrateAntigen153");
            //神经元特异性烯醇化酶NSE
            put("4132","neuronSpecificEnolase");
            //癌抗原72-4
            put("5051","carbohydrateAntigen724");
            //细胞角蛋白19片段(CYFRA21-2)
            put("5392","cytokeratin19Fragment");
            //癌抗原50
            put("5049","carbohydrateAntigen50");
            //游离前列腺特异性抗原
            put("4101","freePsmAntigen");
            //前列腺特异性抗原
            put("4100","psmAntigen");
        }
    };

    /**
     * 甲功五项
     */
    public static Map<String,String> thyroidKeys = new HashMap<String,String>(){
        {
            //三碘甲状腺原氨酸(T3)
            put("5470","triiodothyronine");
            //甲状腺素（T4）
            put("5480","thyroxine");
            //促甲状腺激素(TSH)
            put("5066","thyroidStimulatingHormone");
            //游离三碘甲状腺原氨酸(T3)
            put("5479","freeTriiodothyronine");
            //游离甲状腺素（FT4）
            put("5472","freeThyroxine");
            //抗甲状腺微粒体抗体
            put("5796","antithyroidMicrosomeAntibody");
            //甲状腺球蛋白
            put("5472","thyroglobulin");
            //游离三碘甲状腺原氨酸
            put("5791","ft3");
        }
    };
    /**
     * 激素六项
     */
    public static Map<String,String> hormoneKeys = new HashMap<String,String>(){
        {
            //卵泡刺激素（FSH）
            put("5009","follicleStimulatingHormone");
            //黄体生成素（LH）
            put("5008","luteinizingHormone");
            //雌二醇（E2）
            put("5005","estradiol");
            //孕酮（P）
            put("5006","progesterone");
            //睾酮（T）
            put("5478","testosterone");
            //催乳素（PRL）
            put("5477","prolactin");
        }
    };

    /**
     * 凝血项目
     */
    public static Map<String,String> coagulationKeys = new HashMap<String,String>(){
        {
            //凝血酶原时间
            put("1001","prothrombinTime");
            //活化部分凝血活酶时间
            put("1112","aptt");
            //纤维蛋白原含量
            put("1134","fibrinogen");
            //凝血酶时间
            put("1129","thrombinTime");
//            //纤维蛋白(原)降解产物
//            put("884040","fdp");
            //D-二聚体定量
            put("5432","dDimerQuantitativeTest");
            //国际标准化比值
            put("1054","internationalNormRatio");
//            //凝血酶原活动度
//            put("881058","prothrombinActivity");
        }
    };

    /**
     * 血气分析
     */
    public static Map<String,String> bloodGasKeys = new HashMap<String,String>(){
        {
            //PH值
            put("6189","bloodGasPh");
            //二氧化碳分压
            put("6190","pco2");
            //氧分压
            put("6192","po2");
            //红细胞压积
            put("6211","hct");
            //血红蛋白
            put("6201","hb");
            //剩余碱
            put("6206","be");
            //标准碳酸根
//            put("","chco3");
            //实际碳酸根
            put("6224","actco3");
            //总二氧化碳
            put("6229","tco2");
            //血氧饱和度
            put("6202","so2");
            //氧饱和度50%时氧分压
            put("6210","p50act");
            //氧含量
//            put("","cto2");
//            //氧容量
//            put("","bo2");

        }
    };
}
