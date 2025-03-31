package com.dchealth.etl.domain.constant;


import java.util.HashMap;
import java.util.Map;

/**
 * 唐都体检档案 项目与系统表关系对照
 * @author zjl
 * @since 2023/4/3
 */
public class TdCheckConstant {

    /**
     * 一般检查
     */
    public static Map<String,String> generalKeys = new HashMap<String,String>(){
        {
            //身高(cm)
            put("010101","height");
            //体重(Kg)
            put("010102","weight");
            //收缩压(mmHg)
            put("010103","systolicBloodPressure");
            //舒张压(mmHg)
            put("010109","diastolicBloodPressure");
            /**
            //视力左
            put("200101","meanCorpusVolume");
            //视力右
            put("200102","meanCorpusHemoglobin");
             **/
            //腰围
            put("195045","waistline");
            //体脂肪率
            put("195016","bmi");
        }
    };

    /**
     * 内科检查
     */
    public static Map<String,String> internalMedicineExamKeys = new HashMap<String,String>(){
        {
            //胸部外形
            put("180407","chestContour");
            //胸部触诊
            put("180203","chestPalpation");
            //胸部叩诊
            put("180204","chestPercussion");
            //心界
            put("180408","cardiacBorder");
            //肺部听诊
            put("180205","pulmonaryAuscultation");
            //心音
            put("100409","cardiacRhythm");
            //心律
            put("180304","urineKetone");
            //心脏杂音
            put("180307","cardiacMurmur");
            //肝脏触诊
            put("180403","hepaticPalpation");
            //脾脏触诊
            put("180405","splenicPalpation");
            //腹部外形
            put("180410","abdominalContour");
            //腹部压痛
            put("108411","abdominalTenderness");
            //腹部叩诊
            put("180406","abdominalPercussion");
        }
    };
    /**
     * 外科检查
     */
    public static Map<String,String> surgicalExamKeys = new HashMap<String,String>(){
        {
            //皮肤
            put("190101","skin");
            //肛门
            put("190102","anus");
            //表浅淋巴结
            put("190103","superficialLymphNode");
            //甲状腺
            put("190203","thyroid");
            //脊柱
            put("190301","spine");
            //四肢关节
            put("190303","articulationOfLimbs");
        }
    };

    /**
     * 耳鼻喉检查
     */
    public static Map<String,String> otolaryngologyKeys = new HashMap<String,String>(){
        {
            //听力
            put("210101","hearing");
            //耳廓
            put("210103","auricle");
            //外耳道
            put("210104","externalAuditoryMeatus");
            //鼓膜
            put("210105","tympanicMembrane");
            //外鼻
            put("210201","externalNose");
            //鼻腔
            put("210207","nasalCavity");
            //口咽
            put("210301","oralPharynx");
            //喉咽
            put("210303","laryngopharynx");
            //扁桃体
            put("210411","tonsil");
        }
    };

    /**
     * 眼科检查
     */
    public static Map<String,String> ophthalmologicalExamKeys = new HashMap<String,String>(){
        {
            //视力（左）
            put("200101","visualAcuityLeft");
            //视力（右）
            put("200102","visualAcuityRight");
            //矫正视力左
            put("200104","correctedVisualAcuityLeft");
            //矫正视力右
            put("200105","correctedVisualAcuityRight");
            //泪器
            put("200302","lacrimalApparatus");
            //眼位
            put("200304","eyePosition");
            //眼球运动
            put("200318","eyeMovement");
            //眼睑
            put("200203","eyelid");
            //睑结膜 球结膜
            put("200604","conjunctivaOfEyelids");
            //球结膜
//            put("200604","");
            //眼压检查
            put("200611","intraocularPressureExam");
            //裂隙灯检查
            put("200602","slitLampExam");
            //眼底照相
            put("200616","fundusPhotography");
            //色觉检查
            put("200202","colorVisionExam");
        }
    };

    /**
     * 口腔科检查
     */
    public static Map<String,String> oralKeys = new HashMap<String,String>(){
        {
            //牙体组织疾病
            put("230221","dentalCaries");
            //牙周组织疾病
            put("230222","periodontalDisease");
            //口腔黏膜疾病
            put("23223","oralMucosalDisease");
            //口腔颌面外科疾病
            put("230224","oralMaxillofacialSurgery");
            //口腔修复科
            put("230225","oralRestoration");
            //正畸科
            put("230226","orthodontics");
        }
    };

    /**
     * 辅助检查其他表
     */
    public static Map<String,String> auxiliaryExamOtherKeys = new HashMap<String,String>(){
        {
            //普通肠镜
            put("9007","conventionalEnteroscope");
            //无痛胃镜
            put("9003","painlessGastroscope");
            //无痛胃肠镜
            put("9005","painlessGastroenteroscopy");
            //浅表器官（包块）
            put("1041","superficialOrgan");
            //骨密度测定（全身骨密）
            put("1006"," bone_mineralDensity");
            //C13呼气试验检测
            put("1007","c13BreathTest");
            //普通胃镜
            put("9006","generalGastroscope");
            //无痛肠镜
            put("9004","painlessColonoscopy");
            //无痛胃肠镜加
            put("9008","painlessGastroscopyPlus");
        }
    };

    /**
     * 辅助检查-超声相关
     */
    public static Map<String,String> examUltrasoundKeys = new HashMap<String,String>(){
        {
            //颈部血管彩超
            put("1042","carotidUltrasound");
            //甲状腺彩超
            put("1049","thyroidUltrasound");
            //心脏彩超
            put("1043","cardiacUltrasound");
            //乳腺彩超
            put("1048","biRads");
            //阴式彩超(女)
            put("1044","");
            //子宫附件彩超
            put("1045","uterineAppendagesUltrasound");
            //肝胆胰脾
            put("1047","lgps");
            //肝胆胰脾肾
            put("1046","lgpsk");
            //肝胆胰脾肾前列腺膀胱（男）
            put("1051","lgpskpb");
            //肝胆胰脾肾子宫附件（女）
            put("1050","lgpskua");
            //双侧乳腺(钼靶)
            put("1039","bilateralMammaryGland");

            //颈椎正位片
            put("3006","cervicalSpine");
            //颈椎侧位片
            put("3007","cervicalLateralFilm");
            //腰椎正位片
            put("3008","lumbarSpine");
            //腰椎侧位片
            put("3009","lumbarSpineFilm");
            //胸部正位片
            put("3001","chestXray");
            //左膝关节正位
            put("3010","leftKneeJointAp");
            //左膝关节侧位
            put("3011","lkjlp");
            //右膝关节正位
            put("3012","rightKneeJointAp");
            //右膝关节侧位
            put("3013","rkjlp");
        }
    };
    /**
     * 辅助检查-ct相关
     */
    public static Map<String,String> examCTKeys = new HashMap<String,String>(){
        {
            //头颅平扫CT（轴位）
            put("1027","headPlainScanCt");
            //胸部平扫CT（轴位+冠位）
            put("1028","chestPlainScanCt");
            //胸部平扫CT+薄层扫描+肺结节
            put("1029","clpCt");
            //上腹部CT平扫（肝胆胰脾）
            put("1030","upperAbdominalCtPs");
            //下腹部CT平扫（轴位+冠位）
            put("1031","lowerAbdominalCtPs");
            //上腹部CT增强
            put("1032","upperAbdominalCt");
            //下腹部CT增强
            put("1033","lowerAbdominalCt");
            //上腹部+下腹部CT增强
            put("1034","ulAbdominalCt");
            //盆腔CT增强
            put("1035","pelvicCt");
            //PET/CT全身
            put("1021","petCt");
            //全身骨静态显像
            put("1019","boneStaticImaging");
            //全身PET/MR
            put("1020","petMr");
        }
    };

    /**
     * 辅助检查-核磁相关
     */
    public static Map<String,String> examNMRKeys = new HashMap<String,String>(){
        {
            //头颅血管成像
            put("1036","cranialVascularImaging");
            //头颅静脉血管成像MRV
            put("3005","cranialVenousMrv");
            //头颅动脉血管成像MRA
            put("3005","cranialArteryMra");
            //心脏冠脉血管成像
            put("1037","cardiacCoronaryAngiography");
            //MR1.5T平扫头颅
            put("1022","headFlatScan15");
            //MR1.5T平扫颈椎
            put("3002","csFlatScan15");
            //MR1.5T平扫腰椎
            put("3003","lsFlatScan15");
            //MR3T平扫头颅
            put("1023","headFlatScan3");
            //MR3T平扫颈椎
            put("1025","csFlatScan3");
            //MR3T平扫腰椎
            put("1101","lsFlatScan3");
            //磁共振弥散成像（DWI）
            put("1024","dwi");
            //磁共振功能成像（3D-ASL)
            put("1100","asl");
            //胸透
            put("1038","chestXray");
        }
    };

    /**
     * 血常规
     */
    public static Map<String,String> bloodRoutineKeys = new HashMap<String,String>(){
        {
            //中性粒细胞百分率
            put("881169","neutrophilPercentage");
//            put("885898","neutrophilPercentage");
//            put("886341","neutrophilPercentage");
            //淋巴细胞百分率
//            put("885897","lymphocytePercentage");
//            put("886342","lymphocytePercentage");
            put("881170","lymphocytePercentage");
            //单核细胞百分率
            put("881171","monocytePercentage");
//            put("885896","monocytePercentage");
            //嗜碱性粒细胞百分率
//            put("885899","basophilPercentage");
            put("881172","basophilPercentage");
//            put("886343","basophilPercentage");
            //中性粒细胞绝对值
            put("881174","absNeutrophilCount");
            //淋巴细胞绝对值
            put("881175","absLymphocyteCount");
            //单核细胞绝对值
            put("881176","absMonocyteCount");
            //嗜酸性粒细胞绝对值
            put("881177","absEosinophilCount");
            //嗜碱性粒细胞绝对值
            put("881178","basophilPercentage");
            //红细胞计数
            put("881179","redCellCount");
            //血红蛋白
            put("881180","hemoglobin");
            //红细胞压积
            put("881181","hematocrit");
//            put("885902","hematocrit");
//            put("887009","hematocrit");
            //平均红细胞体积
            put("881182","meanCorpusVolume");
//            put("885903","meanCorpusVolume");
            //平均血红蛋白含量
            put("881183","meanCorpusHemoglobin");
//            put("885904","meanCorpusHemoglobin");
            //平均血红蛋白浓度
//            put("885905","meanCorpusHemConcent");
            put("881184","meanCorpusHemConcent");
            //红细胞分布宽度CV
            put("881185","redCellDisWidthVariation");
            //红细胞分布宽度SD
            put("881186","redCellDisDeviation");
            //血小板计数
            put("881187","plateletCount");
            //血小板分布宽度
//            put("885908","plateletDisWidth");
            put("881188","plateletDisWidth");
            //平均血小板体积
            put("881189","meanPlateletVolume");
//            put("885907","meanPlateletVolume");
            //大血小板比率
//            put("885910","largePlateletRatio");
            put("881190","largePlateletRatio");
            //白细胞计数
            put("881192","whiteCellCount");
            //血小板比积
            put("885083","plateletSpecificVolume");
            //ABO血型鉴定
            put("885178","aboBlood");
            //Rh(D)血型鉴定
            put("885179","rhBlood");
            //红细胞沉降率
            put("881203","esr");
            //电解质测定-钙（血）
//            put("","");
            //淀粉酶测定（血）
            put("882019","amylaseAssay");
        }
    };

    /**
     * 尿常规
     */
    public static Map<String,String> urineRoutineKeys = new HashMap<String,String>(){
        {
            //尿红细胞定量
            put("881138","urinaryRbc");
            //尿白细胞定量
            put("881139","urineWhiteCells");
            //尿上皮细胞定量
            put("881140","urothelialCells");
            //尿管型定量
            put("881141","urinaryTubeType");
            //尿白细胞
            put("881144","urineWhiteCells");
            //尿糖定性
            put("881145","urineGlucose");
            //尿胆红素
            put("881146","urineBilirubin");
            //尿比重
            put("881147","specificGravityUrine");
            //尿蛋白
            put("881148","urineProtein");
            //尿亚硝酸盐定性
            put("881149","urineNitrite");
            //尿酮体
            put("881150","urineKetone");
            //尿胆原
            put("881151","urineUrobilinogen");
            //尿PH值
            put("881152","urinePh");
            //尿病理管型定量
            put("881154","urinaryPathologyType");
            //尿结晶定量
            put("881155","urineCrystal");
            //尿精子定量
            put("881157","urinarySperm");
            //尿液颜色
            put("881209","urineColor");
            //尿潜血
            put("885082","urineOccultBlood");
            //红细胞（高倍视野）
            put("885125","urinaryRbcHpf");
            //上皮细胞（高倍视野）
            put("885126","urothelialCellsHbf");
            //白细胞（高倍视野）
            put("885131","urinaryWbcHbf");
            //管型（低倍视野）
            put("885134","urinaryTubeTypeLbf");
            //粘液丝
            put("885403","mucilaginousFilament");
            //浊度
            put("885545","turbidity");
            put("885242","turbidity");
            //白细胞团
            put("885669","leucocyteMass");
            //白蛋白肌酐比
            put("885672","albuminCreatinineRatio");
            //白蛋白
            put("885433","albumin");
            //尿淀粉酶
            put("882028","urineAmylase");
        }
    };

    /**
     * 体检检验表
     */
    public static Map<String,String> checkTestKeys = new HashMap<String,String>(){
        {
            //淀粉酶测定
            put("882019","amylase");
            //空腹血糖
            put("885873","fpg");
            //餐后2小时血糖
            put("885019","pbg");
            //弓形虫
            put("886022","tox");
            //巨细胞病毒
            put("886245","cytomegalovirus");
            //单纯疱疹病毒
            put("885351","herpesSimplexVirus");
            put("885386","herpesSimplexVirus");
            //风疹病毒
            put("885393","rubellaVirus");
            //抗“O”检测  抗链球菌溶血素O
            put("884021","aso");
            //类风湿因子
            put("884022","rheumatoidFactor");
            //C-反应蛋白
            put("885982","cReactiveProtein");
            //钙
            put("882030","calcium");
            //镁
            put("882085","magnesium");
            //铅
            put("885190","pb");
            //铜
            put("885244","copper");
            //锌
            put("885245","zinc");
            //铁
            put("885246","iron");
            //新冠病毒核酸
            put("885735","covid19NucleicAcid");
            put("885763","covid19NucleicAcid");
            //正位脊柱 L1-L4 骨密度
            put("520001","boneDensityAnterior");
            //股骨骨密度
            put("520002","boneDensityOfFemur");
            //前臂 桡骨33% 骨密度
            put("520003","boneDensityForearmRadius");
            //C13呼气试验检测
//            put("","");
            //幽门螺旋杆菌尿素酶抗体测定
            put("885381","helicobacterPylori");
            //宫颈病理细胞学检测
//            put("","");
            //宫颈薄层细胞检测（TCT）
//            put("","");
            //人乳头瘤病毒（HPV）DNA基因分型检测
            put("885609","humanPapillomavirusHpv");
            //(他汀类)化学药物用药指导基因检测
            put("885511","statinsMgTest");
            //(硝酸甘油、酒精)化学药物用药指导基因检测
            put("885484","nitroglycerinMgTest");
            //(叶酸、甲氨喋呤)化学药物用药指导基因检测
            put("885465","folicMgTest");
            //载脂蛋白E基因多态性检测 ApoE rs429358位点基因型,ApoE rs7412  位点基因型
            put("885615","apoeTest");
            put("885616","apoeTest");
        }
    };

    /**
     * 心肌酶
     */
    public static Map<String,String> myocardiaKeys = new HashMap<String,String>(){
        {
            //血清肌酸激酶测定
            put("882006","serumCreatineKinaseAssay");
            //α-羟丁酸脱氢酶
            put("882020","hbdh");
            //血清肌酸激酶-MB同工酶活性测定
            put("882021","serumCreatineKinaseMbAssay");
//            put("884168","serumCreatineKinaseMbAssay");
            //血清乳酸脱氢酶
            put("882088","serumLactateDehydrogenase");
        }
    };

    /**
     * 肿瘤十二项
     */
    public static Map<String,String> tumorKeys = new HashMap<String,String>(){
        {
            //铁蛋白
            put("884035","ferritin");
            //癌胚抗原(CEA)
            put("884121","carcinoembryonicAntigen");
            //癌抗原125(CA125)
            put("884123","carbohydrateAntigen125");
            //血清人绒毛膜促性腺激素
            put("884125","hcg");
            //神经元特异性稀醇化酶
            put("884132","neuronSpecificEnolase");
            //甲胎蛋白
            put("884157","alphaFetoprotein");
            //糖类抗原CA19-9
            put("884159","carbohydrateAntigen199");
            //糖类抗原CA153
            put("885016","carbohydrateAntigen153");
            //糖类抗原CA50
            put("885049","carbohydrateAntigen50");
            //糖类抗原CA724
            put("885051","carbohydrateAntigen724");
            //鳞状细胞癌相关抗原
            put("885070","squCellCarcinomaAntigen");
            //细胞角蛋白19片段
            put("885392","cytokeratin19Fragment");
            //人附睾蛋白4
            put("885587","humanPididymisRotein");
            //血管内皮生长因子检测
            put("885777","vegf");
            // F/T
            put("884102","ft");
            // 前列腺特异性抗原（总）
            put("884100","psmAntigen");
            // 前列腺特异性抗原（游离）
            put("884101","freePsmAntigen");
            //异常糖链蛋白（TAP）测定
//            put("","");
        }
    };

    /**
     * 甲功五项
     */
    public static Map<String,String> thyroidKeys = new HashMap<String,String>(){
        {
            //促甲状腺激素
            put("885066","thyroidStimulatingHormone");
            put("885794","thyroidStimulatingHormone");
            put("885990","thyroidStimulatingHormone");
            //三碘甲状腺原氨酸
            put("885470","freeTriiodothyronine");
//            put("885626","freeTriiodothyronine");
            //游离甲状腺素（FT4）
            put("885472","freeThyroxine");
            //游离三碘甲状腺原氨酸(T3)
//            put("885791","freeTriiodothyronine");
            put("885479","freeTriiodothyronine");
            //甲状腺素（T4）
//            put("885789","thyroxine");
            put("885480","thyroxine");
            //抗甲状腺球蛋白抗体
            put("885481","antithyroglobulinAntibody");
            //抗甲状腺过氧化物酶抗体
//            put("885798","antithyroidPeroxidaseAntibody");
            put("885482","antithyroidPeroxidaseAntibody");
            //促甲状腺素受体抗体
            put("885483","thyrotropinReceptorAntibody");

        }
    };
    /**
     * 激素六项
     */
    public static Map<String,String> hormoneKeys = new HashMap<String,String>(){
        {
            //卵泡刺激素（FSH）
            put("885805","follicleStimulatingHormone");
            //黄体生成素（LH）
            put("885804","luteinizingHormone");
            //雌二醇（E2）
            put("885807","estradiol");
//            put("885005","estradiol");
            //孕酮（P）
            put("885808","progesterone");
//            put("885006","progesterone");
            //睾酮（T）
            put("885806","testosterone");
//            put("885478","testosterone");
            //催乳素（PRL）
//            put("885477","prolactin");
            put("885803","prolactin");
        }
    };
    /**
     * 肝功能
     */
    public static Map<String,String> liverKeys = new HashMap<String,String>(){
        {
            //总胆汁酸
            put("882003","totalBileAcid");
            //白球比
            put("882004","whiteBallRatio");
            //AST/ALT
            put("882005","astAltRatio");
            //间接胆红素
            put("882046","indirectBilirubin");
            //丙氨酸氨基转移酶
            put("882054","alanineAminotransferase");
            //天冬氨酸氨基转移酶
            put("882055","aspartateAminotransferase");
            //总蛋白
            put("882056","totalProtein");
            //白蛋白
            put("882057","albumin");
            //球蛋白
            put("882059","globulin");
            //总胆红素
            put("882060","totalBilirubin");
            //直接胆红素
            put("882061","directBilirubin");
        }
    };
    /**
     * 肾功能
     */
    public static Map<String,String> renalKeys = new HashMap<String,String>(){
        {
            //尿酸
            put("882016","uricAcid");
            //肌酐
            put("882017","creatinine");
            //尿素
            put("882026","bloodUreaNitrogen");
            //血清胱抑素C
            put("885461","serumCystatinC");
            //β2-微球蛋白
            put("884036","microglobulinβ2");
            //二氧化碳
            put("882014","carbonDioxide");
//            //直接胆红素
//            put("882061","directBilirubin");
        }
    };
    /**
     * 乙肝
     */
    public static Map<String,String> hepatitisKeys = new HashMap<String,String>(){
        {
            //乙肝表面抗原定量
            put("884053","hbsag");
            //乙肝表面抗体定量
            put("884054","antiHbs");
            //乙肝E抗原定量
            put("884055","hbeag");
            //乙肝E抗体定量
            put("884056","antiHbe");
            //乙肝核心抗体定量
            put("884057","antiHbc");
            //乙型肝炎病毒DNA(HBV-DNA)定量
            put("886001","hbvDna");
            put("886030","hbvDna");
            //丙型肝炎病毒DNA(HCV-RNA)定量
            put("886031","hcvRna");
            put("886002","hcvRna");

            //乙肝表面抗原
            put("884052","hepatitisBSurfaceAntigen");
            //乙肝表面抗体
            put("884058","hepatitisBSurfaceAntibody");
            //乙肝e抗原
            put("884059","hepatitisBEAntigen");
            //乙肝e抗体
            put("884060","hepatitisBENtibody");
            //乙肝核心抗体
            put("884061","hepatitisBCoreAntibody");
            //丙肝抗体
            put("884063","hepatitisCAntibody");
            put("884064","hepatitisCAntibody");
            //艾滋病抗体
            put("885647","hivAntibody");
            //梅毒抗体
            put("885648","syphilisAntibody");
        }
    };

    /**
     * 血脂
     */
    public static Map<String,String> bloodFatKeys = new HashMap<String,String>(){
        {
            //总胆固醇
            put("882007","totalCholesterol");
            //甘油三酯
            put("882008","triglycerides");
            //高密度脂蛋白胆固醇
            put("882010","hdlCholesterol");
            //载脂蛋白A-1
            put("882023","apolipoproteinA1");
            //载脂蛋白B
            put("882024","apolipoproteinB");
            //低密度脂蛋白胆固醇
            put("882051","ldlCholesterol");
        }
    };
    /**
     * 凝血项目
     */
    public static Map<String,String> coagulationKeys = new HashMap<String,String>(){
        {
            //凝血酶原时间
            put("881001","prothrombinTime");
            //活化部分凝血活酶时间
            put("881112","aptt");
            //纤维蛋白原含量
            put("881134","fibrinogen");
            //凝血酶时间
            put("881129","thrombinTime");
            //纤维蛋白(原)降解产物
            put("884040","fdp");
            //D-二聚体定量
            put("885432","dDimerQuantitativeTest");
            //国际标准化比值
            put("881054","internationalNormRatio");
            //凝血酶原活动度
            put("881058","prothrombinActivity");
        }
    };
}
