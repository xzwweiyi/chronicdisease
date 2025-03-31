export default {
    /**
     * 基本信息
     */
    convertBaseInfo(type) {
        let baseInfo = [
            { sortNum: 1000, renderKey: "personName", renderName: "姓名" },
            { sortNum: 1002, renderKey: "personId", renderName: "患者id" },
            { sortNum: 1004, renderKey: "sex", renderName: "性别" },
            // { sortNum: 1006, renderKey: "genderSourceValue", renderName: "性别" },
            { sortNum: 1008, renderKey: "birthday", renderName: "出生日期" },
            { sortNum: 1010, renderKey: "age", renderName: "年龄" },
            { sortNum: 1012, renderKey: "visitTypeName", renderName: "证件类型" },
            { sortNum: 1014, renderKey: "cardId", renderName: "证件号" },
            { sortNum: 1016, renderKey: "address", renderName: "家庭住址" },
            { sortNum: 1018, renderKey: "phoneNumber", renderName: "联系电话" },
            { sortNum: 1020, renderKey: "checkDate", renderName: "就诊日期" },
            { sortNum: 1022, renderKey: "degreeOfEducation", renderName: "文化程度" },
            { sortNum: 1024, renderKey: "maritalStatus", renderName: "婚姻状况" },
            { sortNum: 1026, renderKey: "locationId", renderName: "现住址" },
            { sortNum: 1028, renderKey: "personCareer", renderName: "职业名称" },
            { sortNum: 1030, renderKey: "contactsName", renderName: "联系人" },
            { sortNum: 1032, renderKey: "contactsPhone", renderName: "联系人电话" },
            { sortNum: 1034, renderKey: "cardType", renderName: "证件类型" },
            { sortNum: 1036, renderKey: "careSiteId", renderName: "医疗机构" },
            // 一般
            { sortNum: 2010, renderKey: "height", renderName: "身高(cm )" },
            { sortNum: 2020, renderKey: "weight", renderName: "体重(Kg )" },
            { sortNum: 2022, renderKey: "vision", renderName: "视力" },
            { sortNum: 2030, renderKey: "systolicBloodPressure", renderName: "收缩压(mmHg )" },
            { sortNum: 2040, renderKey: "diastolicBloodPressure", renderName: "舒张压(mmHg )" },
            { sortNum: 2050, renderKey: "waist", renderName: "腰围" },
            { sortNum: 2060, renderKey: "bmi", renderName: "体脂肪率" },
            { sortNum: 2070, renderKey: "respiratoryRate", renderName: "脉搏" },
            { sortNum: 2080, renderKey: "bodyTemperature", renderName: "体温" },
            { sortNum: 2090, renderKey: "pulseRate", renderName: "呼吸频率" },
            { sortNum: 2100, renderKey: "urine", renderName: "尿量" },
            { sortNum: 2100, renderKey: "summaryDate", renderName: "结论日期/诊断日期" },
            { sortNum: 2100, renderKey: "examSummary", renderName: "终检结论" },
            { sortNum: 2100, renderKey: "healthGuide", renderName: "体检提示及建议" },
        ]
        return baseInfo
    },
    convertCheckInfo(type) {
        const arrayStemp = {

            /**
             * 一般检查
             */
            height: "身高(cm)", weight: "体重(Kg)", systolicBloodPressure: "收缩压(mmHg)",
            diastolicBloodPressure: "舒张压(mmHg)", waistline: "腰围", bmi: "体脂肪率", pulse: "脉搏",
            bodyTemperature: "体温", respiratoryRate: "呼吸频率",
            /**
             * 内科检查
             */
            chestContour: "胸部外形", chestPalpation: "胸部触诊", chestPercussion: "胸部叩诊",
            cardiacBorder: "心界", pulmonaryAuscultation: "肺部听诊", heartSound: "心音",
            cardiacRhythm: "心律", cardiacMurmur: "心脏杂音", hepaticPalpation: "肝脏触诊",
            splenicPalpation: "脾脏触诊", abdominalContour: "腹部外形", abdominalTenderness: "腹部压痛",
            abdominalPercussion: "腹部叩诊",
            /**
             * 外科检查
             */
            skin: "皮肤", anus: "肛门", superficialLymphNode: "表浅淋巴结",
            thyroid: "甲状腺", spine: "脊柱", articulationOfLimbs: "四肢关节",
            /**
             * 耳鼻喉检查
             */
            hearing: "听力", auricle: "耳廓", externalAuditoryMeatus: "外耳道",
            tympanicMembrane: "鼓膜", externalNose: "外鼻", nasalCavity: "鼻腔",
            oralPharynx: "口咽", laryngopharynx: "喉咽", tonsil: "扁桃体",
            /**
             * 眼科检查
             */
            visualAcuityLeft: "视力（左）", visualAcuityRight: "视力（右）", correctedVisualAcuityLeft: "矫正视力左",
            correctedVisualAcuityRight: "矫正视力右", lacrimalApparatus: "泪器", eyePosition: "眼位",
            eyeMovement: "眼球运动", eyelid: "眼睑", conjunctivaOfEyelids: "睑结膜", conjunctivaOfEyeball: '球结膜',
            intraocularPressureExam: "眼压检查", slitLampExam: "裂隙灯检查", fundusPhotography: "眼底照相",
            colorVisionExam: "色觉检查",
            /**
             * 口腔科检查
             */
            dentalCaries: "牙体组织疾病", periodontalDisease: "牙周组织疾病", oralMucosalDisease: "口腔黏膜疾病",
            oralMaxillofacialSurgery: "口腔颌面外科疾病", oralRestoration: "口腔修复科", orthodontics: "正畸科",
            /**
             * 辅助检查其他表
             */
            conventionalEnteroscope: "普通肠镜", painlessGastroscope: "无痛胃镜", painlessGastroenteroscopy: "无痛胃肠镜",
            superficialOrgan: "浅表器官（包块）", bone_mineralDensity: "骨密度测定（全身骨密）",
            c13BreathTest: "C13呼气试验检测", generalGastroscope: "普通胃镜",
            painlessColonoscopy: "无痛肠镜", painlessGastroscopyPlus: "无痛胃肠镜加",
            /**
             * 辅助检查-超声相关
             */
            carotidUltrasound: "颈部血管彩超", thyroidUltrasound: "甲状腺彩超", cardiacUltrasound: "心脏彩超",
            biRads: "乳腺彩超", "这个没有": "阴式彩超(女)", uterineAppendagesUltrasound: "子宫附件彩超",
            lgps: "肝胆胰脾", lgpsk: "肝胆胰脾肾", lgpskpb: "肝胆胰脾肾前列腺膀胱（男）",
            lgpskua: "肝胆胰脾肾子宫附件（女）", bilateralMammaryGland: "双侧乳腺(钼靶)",
            cervicalSpine: "颈椎正位片", cervicalLateralFilm: "颈椎侧位片", lumbarSpine: "腰椎正位片",
            lumbarSpineFilm: "腰椎侧位片", chestXray: "胸部正位片", leftKneeJointAp: "左膝关节正位",
            lkjlp: "左膝关节侧位", rightKneeJointAp: "右膝关节正位", rkjlp: "右膝关节侧位",
            /**
             * 辅助检查-ct相关
             */
            headPlainScanCt: "头颅平扫CT（轴位）", chestPlainScanCt: "胸部平扫CT（轴位+冠位）",
            clpCt: "胸部平扫CT+薄层扫描+肺结节", upperAbdominalCtPs: "上腹部CT平扫（肝胆胰脾）",
            lowerAbdominalCtPs: "下腹部CT平扫（轴位+冠位）", upperAbdominalCt: "上腹部CT增强",
            lowerAbdominalCt: "下腹部CT增强", ulAbdominalCt: "上腹部+下腹部CT增强", pelvicCt: "盆腔CT增强",
            petCt: "PET/CT全身", boneStaticImaging: "全身骨静态显像", petMr: "全身PET/MR",
            /**
             * 辅助检查-核磁相关
             */
            cranialVascularImaging: "头颅血管成像", cranialVenousMrv: "头颅静脉血管成像MRV",
            cranialArteryMra: "头颅动脉血管成像MRA", cardiacCoronaryAngiography: "心脏冠脉血管成像",
            headFlatScan15: "MR1.5T平扫头颅", csFlatScan15: "MR1.5T平扫颈椎", lsFlatScan15: "MR1.5T平扫腰椎",
            headFlatScan3: "MR3T平扫头颅", csFlatScan3: "MR3T平扫颈椎", lsFlatScan3: "MR3T平扫腰椎",
            dwi: "磁共振弥散成像（DWI）", asl: "磁共振功能成像（3D-ASL)", chestXray: "胸透",
            /**
             * 血常规 //电解质测定-钙（血）缺失
             */
            neutrophilPercentage: "中性粒细胞百分率", lymphocytePercentage: "淋巴细胞百分率",
            monocytePercentage: "单核细胞百分率", basophilPercentage: "嗜碱性粒细胞百分率",
            absNeutrophilCount: "中性粒细胞绝对值", absLymphocyteCount: "淋巴细胞绝对值",
            absMonocyteCount: "单核细胞绝对值", absEosinophilCount: "嗜酸性粒细胞绝对值",
            absBasophilCount: "嗜碱性粒细胞绝对值", redCellCount: "红细胞计数", hemoglobin: "血红蛋白",
            hematocrit: "红细胞压积", meanCorpusVolume: "平均红细胞体积", meanCorpusHemoglobin: "平均血红蛋白含量",
            meanCorpusHemConcent: "平均血红蛋白浓度", redCellDisWidthVariation: "红细胞分布宽度CV",
            redCellDisDeviation: "红细胞分布宽度SD", plateletCount: "血小板计数", plateletDisWidth: "血小板分布宽度",
            meanPlateletVolume: "平均血小板体积", largePlateletRatio: "大血小板比率", whiteCellCount: "白细胞计数",
            plateletSpecificVolume: "血小板比积", aboBlood: "ABO血型鉴定", rhBlood: "Rh(D)血型鉴定",
            esr: "红细胞沉降率", amylaseAssay: "淀粉酶测定（血）",
            /**
             * 尿常规
             */
            urinaryRbc: "尿红细胞定量", urineWhiteCells: "尿白细胞定量", urothelialCells: "尿上皮细胞定量",
            urinaryTubeType: "尿管型定量", urineWhiteCells: "尿白细胞", urineGlucose: "尿糖定性",
            urineBilirubin: "尿胆红素", specificGravityUrine: "尿比重", urineProtein: "尿蛋白",
            urineNitrite: "尿亚硝酸盐定性", urineKetone: "尿酮体", urineUrobilinogen: "尿胆原",
            urinePh: "尿PH值", urinaryPathologyType: "尿病理管型定量", urineCrystal: "尿结晶定量",
            urinarySperm: "尿精子定量", urineColor: "尿液颜色", urineOccultBlood: "尿潜血",
            urinaryRbcHpf: "红细胞（高倍视野）", urothelialCellsHbf: "上皮细胞（高倍视野）", urinaryWbcHbf: "白细胞（高倍视野）",
            urinaryTubeTypeLbf: "管型（低倍视野）", mucilaginousFilament: "粘液丝", turbidity: "浊度",
            leucocyteMass: "白细胞团", albuminCreatinineRatio: "白蛋白肌酐比", albumin: "白蛋白", urineAmylase: "尿淀粉酶",
            /**
             * 体检检验表 //C13呼气试验检测 //宫颈病理细胞学检测//宫颈薄层细胞检测（TCT）
             */
            amylase: "淀粉酶测定", fpg: "空腹血糖", pbg: "餐后2小时血糖", tox: "弓形虫", cytomegalovirus: "巨细胞病毒",
            herpesSimplexVirus: "单纯疱疹病毒", rubellaVirus: "风疹病毒", aso: "抗“O”检测  抗链球菌溶血素O",
            rheumatoidFactor: "类风湿因子", cReactiveProtein: "C-反应蛋白", calcium: "钙", magnesium: "镁",
            pb: "铅", copper: "铜", zinc: "锌", iron: "铁", covid19NucleicAcid: "新冠病毒核酸",
            boneDensityAnterior: "正位脊柱 L1-L4 骨密度", boneDensityOfFemur: "股骨骨密度",
            boneDensityForearmRadius: "前臂 桡骨33% 骨密度", helicobacterPylori: "幽门螺旋杆菌尿素酶抗体测定",
            humanPapillomavirusHpv: "人乳头瘤病毒（HPV）DNA基因分型检测", statinsMgTest: "(他汀类)化学药物用药指导基因检测",
            nitroglycerinMgTest: "(硝酸甘油、酒精)化学药物用药指导基因检测",
            folicMgTest: "(叶酸、甲氨喋呤)化学药物用药指导基因检测",
            apoeTest: "载脂蛋白E基因多态性检测 ApoE rs429358位点基因型,ApoE rs7412  位点基因型",

            // 心肌酶
            serumCreatineKinaseAssay: '血清肌酸激酶测定', hbdh: 'α-羟丁酸脱氢酶',
            serumCreatineKinaseMbAssay: '血清肌酸激酶-MB同工酶活性测定', serumLactateDehydrogenase: '血清乳酸脱氢酶',

            // 肿瘤十二项
            ferritin: '铁蛋白', carcinoembryonicAntigen: '癌胚抗原(CEA)', carbohydrateAntigen125: '癌抗原125(CA125)',
            hcg: '血清人绒毛膜促性腺激素', neuronSpecificEnolase: '神经元特异性稀醇化酶', alphaFetoprotein: '甲胎蛋白',
            carbohydrateAntigen199: '糖类抗原CA19-9', carbohydrateAntigen153: '糖类抗原CA153',
            carbohydrateAntigen50: '糖类抗原CA50', carbohydrateAntigen724: '糖类抗原CA724',
            squCellCarcinomaAntigen: '鳞状细胞癌相关抗原', cytokeratin19Fragment: '细胞角蛋白19片段',
            humanPididymisRotein: '人附睾蛋白4', vegf: '血管内皮生长因子检测', ft: ' F/T',
            psmAntigen: ' 前列腺特异性抗原（总）', freePsmAntigen: ' 前列腺特异性抗原（游离）',

            // 甲功五项
            thyroidStimulatingHormone: '促甲状腺激素', triiodothyronine: '三碘甲状腺原氨酸(T3)',
            freeThyroxine: '游离甲状腺素（FT4）', freeTriiodothyronine: '游离三碘甲状腺原氨酸(T3)',
            thyroxine: '甲状腺素（T4）', antithyroglobulinAntibody: '抗甲状腺球蛋白抗体',
            antithyroidPeroxidaseAntibody: '抗甲状腺过氧化物酶抗体', thyrotropinReceptorAntibody: '促甲状腺素受体抗体',

            // 激素六项
            follicleStimulatingHormone: '卵泡刺激素（FSH）', luteinizingHormone: '黄体生成素（LH）',
            estradiol: '雌二醇（E2）', progesterone: '孕酮（P）', testosterone: '睾酮（T）', prolactin: '催乳素（PRL）',

            // 肝功能
            totalBileAcid: '总胆汁酸', whiteBallRatio: '白球比', astAltRatio: 'AST/ALT', indirectBilirubin: '间接胆红素',
            alanineAminotransferase: '丙氨酸氨基转移酶', aspartateAminotransferase: '天冬氨酸氨基转移酶',
            totalProtein: '总蛋白', albumin: '白蛋白', globulin: '球蛋白', totalBilirubin: '总胆红素',
            directBilirubin: '直接胆红素',

            // 肾功能
            uricAcid: '尿酸', creatinine: '肌酐', bloodUreaNitrogen: '尿素', serumCystatinC: '血清胱抑素C',
            microglobulinβ2: 'β2-微球蛋白', carbonDioxide: '二氧化碳', directBilirubin: '直接胆红素',

            // 乙肝
            hbsag: '乙肝表面抗原定量', antiHbs: '乙肝表面抗体定量', hbeag: '乙肝E抗原定量', antiHbe: '乙肝E抗体定量',
            antiHbc: '乙肝核心抗体定量', hbvDna: '乙型肝炎病毒DNA(HBV-DNA)定量', hcvRna: '丙型肝炎病毒DNA(HCV-RNA)定量',
            hepatitisBSurfaceAntigen: '乙肝表面抗原', hepatitisBSurfaceAntibody: '乙肝表面抗体',
            hepatitisBEAntigen: '乙肝e抗原', hepatitisBENtibody: '乙肝e抗体', hepatitisBCoreAntibody: '乙肝核心抗体',
            hepatitisCAntibody: '丙肝抗体', hivAntibody: '艾滋病抗体', syphilisAntibody: '梅毒抗体',

            // 血脂
            totalCholesterol: '总胆固醇', triglycerides: '甘油三酯', hdlCholesterol: '高密度脂蛋白胆固醇',
            apolipoproteinA1: '载脂蛋白A-1', apolipoproteinB: '载脂蛋白B', ldlCholesterol: '低密度脂蛋白胆固醇',

            // 凝血项目
            prothrombinTime: '凝血酶原时间', aptt: '活化部分凝血活酶时间', fibrinogen: '纤维蛋白原含量', thrombinTime: '凝血酶时间',
            fdp: '纤维蛋白(原)降解产物', dDimerQuantitativeTest: 'D-二聚体定量', internationalNormRatio: '国际标准化比值',
            prothrombinActivity: '凝血酶原活动度',
            // * 心电图
            ecg: "心电图",
        }
        return arrayStemp[type]
    },
    convertRecordConstantInfo(type) {
        const arrayStemp = {
            // 血常规
            neutrophilPercentage: '中性粒细胞百分率', lymphocytePercentage: '淋巴细胞百分率',
            monocytePercentage: '单核细胞百分率', basophilPercentage: '嗜碱性粒细胞百分率',
            absNeutrophilCount: '中性粒细胞绝对值', absLymphocyteCount: '淋巴细胞绝对值',
            absMonocyteCount: '单核细胞绝对值', absEosinophilCount: '嗜酸性粒细胞绝对值',
            basophilPercentage: '嗜碱性粒细胞绝对值', redCellCount: '红细胞计数',
            hemoglobin: '血红蛋白', hematocrit: '红细胞压积', meanCorpusVolume: '平均红细胞体积',
            meanCorpusHemoglobin: '平均血红蛋白含量', meanCorpusHemConcent: '平均血红蛋白浓度',
            redCellDisWidthVariation: '红细胞分布宽度CV', redCellDisDeviation: '红细胞分布宽度SD',
            plateletCount: '血小板计数', plateletDisWidth: '血小板分布宽度',
            meanPlateletVolume: '平均血小板体积', largePlateletRatio: '大血小板比率',
            whiteCellCount: '白细胞计数', plateletSpecificVolume: '血小板比积', aboBlood: 'ABO血型鉴定',
            rhBlood: 'Rh(D)血型鉴定', esr: '红细胞沉降率', amylaseAssay: '淀粉酶测定（血）',
            // 尿常规
            specificGravityUrine: '尿比重', urinePh: '尿酸碱度', urineWhiteCells: '尿白细胞',
            urineNitrite: '尿亚硝酸盐', urineProtein: '尿蛋白质', urineGlucose: '尿糖', urineKetone: '尿酮体',
            urineUrobilinogen: '尿胆原', urineBilirubin: '尿胆红素', urineOccultBlood: '尿隐血',
            // 便常规
            stoolColor: '便颜色', stoolConsistency: '便性状', stoolRedCells: '便红细胞',
            stoolWhiteCells: '便白细胞', stoolParasiteEgg: '便虫卵', stoolParasiteEggB: '便虫卵B',
            stoolParasiteEggR: '便虫卵R', stoolParasiteEggT: '便虫卵T',
            // 血液生化
            totalProtein: '总蛋白', albumin: '白蛋白', globulin: '球蛋白', albuminGlobulinRatio: '白蛋白/球蛋白',
            aspartateAminotransferase: '天门冬氨酸氨基转移酶', alanineAminotransferase: '丙氨酸氨基转移酶',
            astAltRatio: 'AST/ALT比值', totalBileAcid: '总胆汁酸', totalBilirubin: '总胆红素', directBilirubin: '直接胆红素',
            indirectBilirubin: '间接胆红素', alkalinePhosphatase: '碱性磷酸酶', glutamicPyruvicTransaminase: 'ꝩ-谷氨酰转移酶',
            cholinesterase: '胆碱酯酶', lactateDehydrogenase: '乳酸脱氢酶', alphaHbDehydrogenase: 'α-羟丁酸脱氢酶',
            creatineKinase: '肌酸激酶', creatineKinaseIsoenzyme: '肌酸激酶同工酶', bloodUreaNitrogen: '尿素',
            creatinine: '肌酐', uricAcid: '尿酸', fastingBloodGlucose: '空腹血葡萄糖',
            glycosylatedHemoglobin: '糖化血红蛋白', bloodGlucose2h: '餐后小时血糖', potassium: '钾', sodium: '钠',
            chloride: '氯', calcium: '钙', phosphorus: '磷', magnesium: '镁', carbonDioxide: '二氧化碳',
            triglycerides: '甘油三酯', totalCholesterol: '总胆固醇', hdlCholesterol: '高密度脂蛋白胆固醇',
            ldlCholesterol: '低密度脂蛋白胆固醇', apolipoproteinA1: '载脂蛋白A-1', apolipoproteinB: '载脂蛋白B',
            apolipoproteinA1bRatio: '载脂蛋白A-1/B', homocysteine: '同型半胱氨酸', uacr: '尿白蛋白肌酐比值（UACR）',
            // 心肌酶
            serumLactateDehydrogenase: '血清乳酸脱氢酶', aspartateAminotransferase: '天门冬氨酸氨基转移酶',
            serumCreatineKinaseMbAssay: '血清肌酸激酶-MB同工酶活性测定', serumCreatineKinaseAssay: '血清肌酸激酶测定',
            // 肿瘤十二项
            alphaFetoprotein: '甲胎蛋白(AFP)', carcinoembryonicAntigen: '癌胚抗原(CEA)',
            carbohydrateAntigen199: '癌抗原199(CA199)', carbohydrateAntigen125: '癌抗原125(CA125)',
            carbohydrateAntigen242: '癌抗原242', carbohydrateAntigen153: '癌抗原153',
            neuronSpecificEnolase: '神经元特异性烯醇化酶NSE', carbohydrateAntigen724: '癌抗原72-4',
            cytokeratin19Fragment: '细胞角蛋白19片段(CYFRA21-2)', carbohydrateAntigen50: '癌抗原50',
            freePsmAntigen: '游离前列腺特异性抗原', psmAntigen: '前列腺特异性抗原',
            //  甲功五项
            triiodothyronine: '三碘甲状腺原氨酸(T3)', thyroxine: '甲状腺素（T4）',
            thyroidStimulatingHormone: '促甲状腺激素(TSH)', freeTriiodothyronine: '游离三碘甲状腺原氨酸(T3)',
            freeThyroxine: '游离甲状腺素（FT4）', antithyroidMicrosomeAntibody: '抗甲状腺微粒体抗体',
            thyroglobulin: '甲状腺球蛋白', ft3: '游离三碘甲状腺原氨酸',
            // 激素六项
            follicleStimulatingHormone: '卵泡刺激素（FSH）', luteinizingHormone: '黄体生成素（LH）',
            estradiol: '雌二醇（E2）', progesterone: '孕酮（P）', testosterone: '睾酮（T）', prolactin: '催乳素（PRL）',
            // 凝血项目
            prothrombinTime: '凝血酶原时间', aptt: '活化部分凝血活酶时间', fibrinogen: '纤维蛋白原含量',
            thrombinTime: '凝血酶时间', fdp: '纤维蛋白(原)降解产物', dDimerQuantitativeTest: 'D-二聚体定量',
            internationalNormRatio: '国际标准化比值', prothrombinActivity: '凝血酶原活动度',
            //    血气分析
            bloodGasPh: 'PH值', pco2: '二氧化碳分压', po2: '氧分压', hct: '红细胞压积', hb: '血红蛋白',
            be: '剩余碱', chco3: '标准碳酸根', actco3: '实际碳酸根', tco2: '总二氧化碳', so2: '血氧饱和度',
            p50act: '氧饱和度50%时氧分压', cto2: '氧含量', bo2: '氧容量',

        }
        return arrayStemp[type]
    }

}