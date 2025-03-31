package com.dchealth.cro.medical.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.dchealth.cro.common.crypto.MD5Util;
import com.dchealth.cro.common.exception.base.ServiceException;
import com.dchealth.cro.health.domain.*;
import com.dchealth.cro.health.mapper.*;
import com.dchealth.cro.health.utils.ThreadLocalUtils;
import com.dchealth.cro.medical.contants.TableEnum;
import com.dchealth.cro.medical.domain.MpCommonCol;
import com.dchealth.cro.medical.mapper.MpCommonColMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.cache.RedisCache;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.exception.base.ServiceException;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.health.constants.HealthConstant;
import com.dchealth.cro.health.constants.HealthPatientCheckExamEnum;
import com.dchealth.cro.health.constants.HealthPatientCheckTestEnum;
import com.dchealth.cro.health.constants.HealthRecordCheckConstants;
import com.dchealth.cro.health.domain.MpBloodBiochemistry;
import com.dchealth.cro.health.domain.MpBloodGasAnalysis;
import com.dchealth.cro.health.domain.MpBloodRoutine;
import com.dchealth.cro.health.domain.MpCoagulationFunction;
import com.dchealth.cro.health.domain.MpCtExam;
import com.dchealth.cro.health.domain.MpDiagnosis;
import com.dchealth.cro.health.domain.MpElectrocardiogramExam;
import com.dchealth.cro.health.domain.MpGeneralInspection;
import com.dchealth.cro.health.domain.MpGlucoseTolerance;
import com.dchealth.cro.health.domain.MpHormoneItems;
import com.dchealth.cro.health.domain.MpInfectiousDiseases;
import com.dchealth.cro.health.domain.MpInternalMedicineExam;
import com.dchealth.cro.health.domain.MpNuclearMagnetismExam;
import com.dchealth.cro.health.domain.MpOphthalmologicalExam;
import com.dchealth.cro.health.domain.MpOtolaryngologyExam;
import com.dchealth.cro.health.domain.MpPerson;
import com.dchealth.cro.health.domain.MpStomatologyExam;
import com.dchealth.cro.health.domain.MpStoolRoutine;
import com.dchealth.cro.health.domain.MpSurgicalExam;
import com.dchealth.cro.health.domain.MpThyroidFunction;
import com.dchealth.cro.health.domain.MpTumorScreeningItems;
import com.dchealth.cro.health.domain.MpUltrasonicExam;
import com.dchealth.cro.health.domain.MpUnitDict;
import com.dchealth.cro.health.domain.MpUrineRoutine;
import com.dchealth.cro.health.domain.MpVisitOccurrence;
import com.dchealth.cro.health.domain.MpXRayExam;
import com.dchealth.cro.health.form.DetailForm;
import com.dchealth.cro.health.form.HealthListSearchForm;
import com.dchealth.cro.health.mapper.MpBloodBiochemistryMapper;
import com.dchealth.cro.health.mapper.MpBloodGasAnalysisMapper;
import com.dchealth.cro.health.mapper.MpBloodRoutineMapper;
import com.dchealth.cro.health.mapper.MpCoagulationFunctionMapper;
import com.dchealth.cro.health.mapper.MpCtExamMapper;
import com.dchealth.cro.health.mapper.MpDiagnosisMapper;
import com.dchealth.cro.health.mapper.MpElectrocardiogramExamMapper;
import com.dchealth.cro.health.mapper.MpGeneralInspectionMapper;
import com.dchealth.cro.health.mapper.MpGlucoseToleranceMapper;
import com.dchealth.cro.health.mapper.MpHormoneItemsMapper;
import com.dchealth.cro.health.mapper.MpInfectiousDiseasesMapper;
import com.dchealth.cro.health.mapper.MpInternalMedicineExamMapper;
import com.dchealth.cro.health.mapper.MpNuclearMagnetismExamMapper;
import com.dchealth.cro.health.mapper.MpOphthalmologicalExamMapper;
import com.dchealth.cro.health.mapper.MpOtolaryngologyExamMapper;
import com.dchealth.cro.health.mapper.MpPersonMapper;
import com.dchealth.cro.health.mapper.MpStomatologyExamMapper;
import com.dchealth.cro.health.mapper.MpStoolRoutineMapper;
import com.dchealth.cro.health.mapper.MpSurgicalExamMapper;
import com.dchealth.cro.health.mapper.MpThyroidFunctionMapper;
import com.dchealth.cro.health.mapper.MpTumorScreeningItemsMapper;
import com.dchealth.cro.health.mapper.MpUltrasonicExamMapper;
import com.dchealth.cro.health.mapper.MpUnitDictMapper;
import com.dchealth.cro.health.mapper.MpUrineRoutineMapper;
import com.dchealth.cro.health.mapper.MpVisitOccurrenceMapper;
import com.dchealth.cro.health.mapper.MpXRayExamMapper;
import com.dchealth.cro.health.utils.HealthUtils;
import com.dchealth.cro.health.utils.ThreadLocalUtils;
import com.dchealth.cro.health.vo.CatalogVO;
import com.dchealth.cro.health.vo.GeneralInspectionVO;
import com.dchealth.cro.health.vo.HealthDetailVO;
import com.dchealth.cro.health.vo.HealthItemVO;
import com.dchealth.cro.health.vo.ItemValueVO;
import com.dchealth.cro.health.vo.PatientHealthVisitVO;
import com.dchealth.cro.health.vo.VisitListVO;

@Service
public class HealthService {

	private static final Logger log = LoggerFactory.getLogger(HealthService.class);

	@Autowired
	private MpPersonMapper mpPersonMapper;
	@Autowired
	private MpVisitOccurrenceMapper mpVisitOccurrenceMapper;
	@Autowired
	private MpBloodBiochemistryMapper mpBloodBiochemistryMapper;
	@Autowired
	private MpBloodGasAnalysisMapper mpBloodGasAnalysisMapper;
	@Autowired
	private MpBloodRoutineMapper mpBloodRoutineMapper;
	@Autowired
	private MpCoagulationFunctionMapper mpCoagulationFunctionMapper;
	@Autowired
	private MpDiagnosisMapper mpDiagnosisMapper;
	@Autowired
	private MpGeneralInspectionMapper mpGeneralInspectionMapper;
	@Autowired
	private MpHormoneItemsMapper mpHormoneItemsMapper;
	@Autowired
	private MpInfectiousDiseasesMapper mpInfectiousDiseasesMapper;
	@Autowired
	private MpInternalMedicineExamMapper mpInternalMedicineExamMapper;
	@Autowired
	private MpOphthalmologicalExamMapper mpOphthalmologicalExamMapper;
	@Autowired
	private MpOtolaryngologyExamMapper mpOtolaryngologyExamMapper;
	@Autowired
	private MpStomatologyExamMapper mpStomatologyExamMapper;
	@Autowired
	private MpStoolRoutineMapper mpStoolRoutineMapper;
	@Autowired
	private MpSurgicalExamMapper mpSurgicalExamMapper;
	@Autowired
	private MpThyroidFunctionMapper mpThyroidFunctionMapper;
	@Autowired
	private MpTumorScreeningItemsMapper mpTumorScreeningItemsMapper;
	@Autowired
	private MpUrineRoutineMapper mpUrineRoutineMapper;
	@Autowired
	private MpUnitDictMapper mpUnitDictMapper;
	@Autowired
	private RedisCache redisCache;

	@Autowired
	private MpCtExamMapper mpCtExamMapper;

	@Autowired
	private MpNuclearMagnetismExamMapper mpNuclearMagnetismExamMapper;

	@Autowired
	private MpXRayExamMapper mpXRayExamMapper;

	@Autowired
	private MpUltrasonicExamMapper mpUltrasonicExamMapper;

	@Autowired
	private MpElectrocardiogramExamMapper mpElectrocardiogramExamMapper;

	@Autowired
	private MpGlucoseToleranceMapper mpGlucoseToleranceMapper;
	@Autowired
	private MpCommonExamMapper mpCommonExamMapper;
	@Autowired
	private MpCommonColMapper mpCommonColMapper;

	/**
	 * todo 检查, 通用核心方法
	 */
	public void check(){

	}
	/**
	 * todo 检验, 通用核心方法
	 */
	public void test(){

	}

	/**
	 * 健康档案和体检档案 入口
	 * @param healthListSearchForm
	 * @return
	 */
	public DchealthCloudResponseEntity<PatientHealthVisitVO> index(HealthListSearchForm  healthListSearchForm) {
		DchealthCloudResponseEntity<PatientHealthVisitVO> result = new DchealthCloudResponseEntity<PatientHealthVisitVO>();
		PatientHealthVisitVO  patientHealthVisitVO  = new PatientHealthVisitVO();
		patientHealthVisitVO.setPersonId(healthListSearchForm.getPersonId());
		int i = (healthListSearchForm.getPageNo()-1)*healthListSearchForm.getPageSize() +1;
		MpPerson person = mpPersonMapper.selectPersonByPersonId(healthListSearchForm.getPersonId());
		if(person != null ) {
			patientHealthVisitVO.setPatientName(person.getPersonName());
			List<VisitListVO>  vistitList = new ArrayList<VisitListVO>();
			List<MpVisitOccurrence>  list =  mpVisitOccurrenceMapper.selectPatientHealthList(healthListSearchForm);
			if(!CollectionUtils.isEmpty(list)) {
				for (MpVisitOccurrence mpVisitOccurrence : list) {
					VisitListVO  visitListVO  = new VisitListVO();
					if(mpVisitOccurrence.getVisitStartDate()!=null) {
						visitListVO.setCheckDate(DateUtils.parseDateToStrShort(mpVisitOccurrence.getVisitStartDate()));
					}
					String title = String.format("%02d",i );
					visitListVO.setItemName(HealthConstant.HEALTH_TYPE.get(healthListSearchForm.getType())+title);
					i++;
					visitListVO.setPersonId(healthListSearchForm.getPersonId());
					visitListVO.setVisitId(mpVisitOccurrence.getVisitOccurrenceId());
					if (healthListSearchForm.getType().equals("1")) {
						visitListVO.setVisitType(mpVisitOccurrence.getVisitType());
					}
					vistitList.add(visitListVO);
				}

			}
			patientHealthVisitVO.setList(vistitList);
		}
		result.setData(patientHealthVisitVO);
		return result;
	}
	/**
	 * 健康档案或体检档案详情页面
	 * @param detail
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DchealthCloudResponseEntity<HealthDetailVO> detail(DetailForm detail)  {
		DchealthCloudResponseEntity<HealthDetailVO>  result = new DchealthCloudResponseEntity<HealthDetailVO>();
		HealthDetailVO  healthDetailVO = new  HealthDetailVO();

		MpVisitOccurrence  mpVisitOccurrence  =  mpVisitOccurrenceMapper.selectVisitByPersonIdAndVisitId(detail.getPersonId(),detail.getVisitId());
		if(mpVisitOccurrence == null) {
			result.setCode(CroApiCodeConstant.ARCHIVE_NOT_EXIST.getCode());
			return result;
		}

		MpPerson person = mpPersonMapper.selectPersonByPersonId(detail.getPersonId());
		if(person == null) {
			result.setCode(CroApiCodeConstant.ARCHIVE_NOT_EXIST.getCode());
			return result;
		}

		String personId = detail.getPersonId();
		String visitId = detail.getVisitId();

		//一般检查& 基本信息
		GeneralInspectionVO  healthItemVO  =  getGeneralInspection(personId, visitId,person,mpVisitOccurrence,detail.getType());
		healthDetailVO.setGeneral(healthItemVO);
		//诊断
		if(HealthConstant.JK.equals(detail.getType())) {
			HealthItemVO   healthItemVO2 =  getDiagnosis(personId, visitId, null);
			healthDetailVO.setDiagnosis(healthItemVO2);
		}
		result.setData(healthDetailVO);
		return result;

	}

	/**
	 * 检查项目列表
	 *
	 * @param detail
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DchealthCloudResponseEntity<List<HealthItemVO>> checkExamItemList(DetailForm detail) {
		DchealthCloudResponseEntity<List<HealthItemVO>>  result = new DchealthCloudResponseEntity<List<HealthItemVO>>();
		String personId =detail.getPersonId();
		String visitId=detail.getVisitId();
		List<HealthItemVO> list = new ArrayList<HealthItemVO>();

		if(HealthConstant.TJ.equals(detail.getType())) {
			//内科检查
			HealthItemVO  healthItem1= getInternalMedicineExam(personId, visitId);
			if(healthItem1 != null ) {
				list.add(healthItem1);
			}
			//外科
			HealthItemVO  healthItem2= getSurgicalExam(personId, visitId);
			if(healthItem2 != null ) {
				list.add(healthItem2);
			}
			//耳鼻喉检查
			HealthItemVO  healthItem3= getOtolaryngology(personId, visitId);
			if(healthItem3 != null ) {
				list.add(healthItem3);
			}
			//眼科检查
			HealthItemVO  healthItem4= getOphthalmologicalExam(personId, visitId);
			if(healthItem4 != null ) {
				list.add(healthItem4);
			}
			//口腔科检查
			HealthItemVO  healthItem5= getOral(personId, visitId);
			if(healthItem5 != null ) {
				list.add(healthItem5);
			}

		}
		String visitType = "住院";
		if ("1".equals(detail.getType())) {
			visitType = "住院";
		}if ("2".equals(detail.getType())) {
			visitType = "体检";
		}
		// CT
//		HealthItemVO healthItem6= getCtExam(personId, visitId);
		HealthItemVO healthItem6= getZhuYuanCommonExam(personId, visitId, visitType,TableEnum.MP_CT_EXAM);
		if(healthItem6 != null ) {
			list.add(healthItem6);
		}
		// 磁共振
		HealthItemVO healthItem7= getZhuYuanCommonExam(personId, visitId, visitType,TableEnum.MP_NUCLEAR_MAGNETISM_EXAM);
		if(healthItem7 != null ) {
			list.add(healthItem7);
		}
		// X线
		HealthItemVO healthItem8= getZhuYuanCommonExam(personId, visitId, visitType,TableEnum.MP_X_RAY_EXAM);
		if(healthItem8 != null ) {
			list.add(healthItem8);
		}
		// 超声
		HealthItemVO healthItem9= getZhuYuanCommonExam(personId, visitId, visitType,TableEnum.MP_ULTRASONIC_EXAM);
		if(healthItem9 != null ) {
			list.add(healthItem9);
		}
		// 心电图
		HealthItemVO healthItem10= getZhuYuanCommonExam(personId, visitId, visitType,TableEnum.MP_ELECTROCARDIOGRAM_EXAM);
		if(healthItem10 != null ) {
			list.add(healthItem10);
		}

		result.setData(list);

		return result;
	}
	/**
	 * 血常规
	 *
	 * @param person
	 * @param id
	 * @param visitId
	 * @return
	 */
	private  List<HealthItemVO> getBloodRoutineCol(MpPerson person, String visitId, String visitType,TableEnum tableEnum) {
		List<HealthItemVO> listH = new ArrayList<>();
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		if (StringUtils.isEmpty(personId)) {
			return null;
		}
		MpCommonCol mpUrineRoutineCol = new MpCommonCol();

		mpUrineRoutineCol.setPersonId(personId);
		mpUrineRoutineCol.setVisitType(visitType);
		mpUrineRoutineCol.setVisitOccurrenceId(visitId);
		mpUrineRoutineCol.setTableName(tableEnum.getTableName());

		List<MpCommonCol> mpUrineRoutineCols = mpCommonColMapper.selectMpCommonColList(mpUrineRoutineCol);

		Map<String, List<MpCommonCol>> visitRecords = mpUrineRoutineCols.stream()
				.filter(x->!StringUtils.isEmpty(DateUtils.dateTime(x.getResultTime())))
				.collect(Collectors.groupingBy(key->DateUtils.parseDateToStrFull(key.getResultTime())));
		visitRecords.forEach((k,v)->{
			Map<String, List<MpCommonCol>> collect = v.stream().collect(Collectors.groupingBy(key -> key.getMeasurementType()));
			collect.forEach((type,ganOrShenObjs)->{
				HealthItemVO<List<ItemValueVO>> healthItemVO =new HealthItemVO<List<ItemValueVO>>();
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				healthItemVO.setItemId(HealthPatientCheckTestEnum.COAGULATION_FOUR.getCode());
				healthItemVO.setItemName(k.split(" ")[0] + " " + type);
				for (MpCommonCol urineRoutineCol : ganOrShenObjs) {
					ItemValueVO itemValueVO = new ItemValueVO();
					itemValueVO.setValue(urineRoutineCol.getResultValue());
					if (!StringUtils.isEmpty(urineRoutineCol.getMaxValue())
							&& urineRoutineCol.getMaxValue().length()>1
							&& urineRoutineCol.getMaxValue().startsWith("-")
					) {
						itemValueVO.setMaxValue(urineRoutineCol.getMaxValue().replace("-",""));
					}else{
						itemValueVO.setMaxValue(urineRoutineCol.getMaxValue());
					}
					itemValueVO.setMinValue(urineRoutineCol.getMinValue());
					itemValueVO.setUnit(urineRoutineCol.getUnit());
					itemValueVO.setName(urineRoutineCol.getMeasurementSourceValue());
					itemValueVO.setTip(urineRoutineCol.getTip());
					itemValueVO.setRangeValue(urineRoutineCol.getRangeValue());
					list.add(itemValueVO);
				}
				if (!CollectionUtils.isEmpty(list)) {
					healthItemVO.setObj(list);
					listH.add(healthItemVO);
				}
			});
		});
		return listH;
	}

	private HealthItemVO getZhuYuanCommonExam(String personId, String visitId, String visitType,TableEnum tableName) {
		List<MpCommonExam> mpUltrasonicExams = mpCommonExamMapper.selectMpCommonExamList(personId,visitId, tableName.getTableName(),visitType);
		if(!CollectionUtils.isEmpty(mpUltrasonicExams)) {
			HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
			healthItemVO.setItemId(HealthPatientCheckExamEnum.ULTRASONIC_EXAM.getCode());
			healthItemVO.setItemName(tableName.getType());
			List<CatalogVO> result = new ArrayList<>();
			Map<String, List<MpCommonExam>> mpCtExamList = mpUltrasonicExams.stream()
					.filter(x->!StringUtils.isEmpty(x.getObjectiveView())).filter(x->!StringUtils.isEmpty(x.getTestNo()))
					.collect(Collectors.groupingBy(MpCommonExam::getTestNo));
			List<MpCommonExam> twoList 	= new ArrayList<>();
			mpCtExamList.forEach((testNo,mpUltrasonicExam)->{
				MpCommonExam two = new MpCommonExam();
				MpCommonExam mpUltrasonicExam1 = mpUltrasonicExam.get(0);
				BeanUtils.copyProperties(mpUltrasonicExam1, two);

				String title = mpUltrasonicExam.stream().map(ct->ct.getCheckSourceType()).distinct().collect(Collectors.joining("、"));
				two.setTechnologyName(title);
				two.setCheckSourceType(title);
				if (!StringUtils.isEmpty(mpUltrasonicExam1.getObjectiveView()) || !StringUtils.isEmpty(mpUltrasonicExam1.getSubjectiveHints())) {
					two.setObjectiveViewMD5(MD5Util.encode(StringUtils.trim(mpUltrasonicExam1.getObjectiveView()) + StringUtils.isEmpty(mpUltrasonicExam1.getSubjectiveHints())));
				}
				twoList.add(two);
			});
			twoList.stream().filter(x->!StringUtils.isEmpty(x.getObjectiveViewMD5())).collect(Collectors.groupingBy(MpCommonExam::getObjectiveViewMD5)).forEach((viewMD5,mpUltrasonicExam)->{
				String title = mpUltrasonicExam.stream().map(x -> x.getCheckSourceType()).collect(Collectors.joining("、"));
				CatalogVO catalogVO = new CatalogVO();
				catalogVO.setItemTitle(DateUtils.dateTime(mpUltrasonicExam.get(0).getReportDate())+" "+ title);
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				setItemValueVO("检查项目", title,list);
//				setItemValueVO("身体部位",mpUltrasonicExam.getSctPosition(),list);
//				setItemValueVO("检查方式",mpUltrasonicExam.getTestMethod(),list);
				setItemValueVO("所见",mpUltrasonicExam.get(0).getObjectiveView(),list);
				setItemValueVO("诊断意见",mpUltrasonicExam.get(0).getSubjectiveHints(),list);
//				setItemValueVO("检查诊断",mpUltrasonicExam.getRelevantClinicDiagnosis(),list);
//				setItemValueVO("检查时间",DateUtils.dateTime(mpUltrasonicExam.getTechnologyDate()),list);
//				setItemValueVO("报告时间",DateUtils.dateTime(mpUltrasonicExam.getReportDate()),list);
				String deptName = mpUltrasonicExam.stream().map(x -> x.getDepartmentName()).distinct().collect(Collectors.joining("、"));
				setItemValueVO("开单科室",getRuleDeptName(deptName),list);
				catalogVO.setItemList(list);
				result.add(catalogVO);
			});
			healthItemVO.setArray(result);
			return healthItemVO;
		}
		return null;
	}

	/**
	 * 住院检验项目列表
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DchealthCloudResponseEntity<List<HealthItemVO>> queryCheckTestItemList(DetailForm detail) {
		DchealthCloudResponseEntity<List<HealthItemVO>>  result = new DchealthCloudResponseEntity<List<HealthItemVO>>();

		MpPerson person = mpPersonMapper.getPersonByPersonId(detail.getPersonId());

		String personId =detail.getPersonId();
		String visitId=detail.getVisitId();
		List<HealthItemVO> list = new ArrayList<HealthItemVO>();
		List<MpUnitDict> totalUnitDicts = mpUnitDictMapper.queryUnitDictByTableName(null);
		//查询单位字典表
		if (CollectionUtils.isEmpty(totalUnitDicts)) {
			return null;
		}
		//血常规
//		HealthItemVO  healthItem1= getBloodRoutine(person, visitId, totalUnitDicts);
//		if(healthItem1 != null ) {
//			list.add(healthItem1);
//		}
		String visitType = "1";
		if ("1".equals(detail.getType())) {
			visitType = "1";
		}if ("2".equals(detail.getType())) {
			visitType = "3"; //体检
		}if ("4".equals(detail.getType())) {
			visitType = "4";
		}
		//血常规
		List<HealthItemVO>  healthItem1= getBloodRoutineCol(person, visitId, visitType, TableEnum.MP_BLOOD_ROUTINE_COL);
		if(!CollectionUtils.isEmpty(healthItem1)) {
			Map<String, List<HealthItemVO>> collect2 = healthItem1.stream()
					.collect(Collectors.groupingBy(healthItemVO -> healthItemVO.getItemName().split("")[1]));
			collect2.forEach((k,v)->{
				list.addAll(reverseSortedHealthItemVOS(v));
			});
		}
		//尿常规
		List<HealthItemVO>  healthItem2= getBloodRoutineCol(person, visitId, visitType, TableEnum.MP_URINE_ROUTINE_COL);
		if(!CollectionUtils.isEmpty(healthItem2)) {
			Map<String, List<HealthItemVO>> collect2 = healthItem2.stream()
					.collect(Collectors.groupingBy(healthItemVO -> healthItemVO.getItemName().split("")[1]));
			collect2.forEach((k,v)->{
				list.addAll(reverseSortedHealthItemVOS(v));
			});
		}
		//便常规
		List<HealthItemVO>  healthItem3= getBloodRoutineCol(person, visitId, visitType, TableEnum.MP_STOOL_ROUTINE_COL);
		if(!CollectionUtils.isEmpty(healthItem3)) {
			Map<String, List<HealthItemVO>> collect2 = healthItem3.stream()
					.collect(Collectors.groupingBy(healthItemVO -> healthItemVO.getItemName().split("")[1]));
			collect2.forEach((k,v)->{
				list.addAll(reverseSortedHealthItemVOS(v));
			});
		}
		//常见传染病 乙肝 mp_infectious_diseases
		List<HealthItemVO>  healthItem6= getBloodRoutineCol(person, visitId, visitType, TableEnum.MP_THYROID_FUNCTION_COL);
		if(!CollectionUtils.isEmpty(healthItem6)) {
			Map<String, List<HealthItemVO>> collect2 = healthItem6.stream()
					.collect(Collectors.groupingBy(healthItemVO -> healthItemVO.getItemName().split("")[1]));
			collect2.forEach((k,v)->{
				list.addAll(reverseSortedHealthItemVOS(v));
			});
		}
		//凝血系列检查   mp_coagulation_function
		List<HealthItemVO>  healthItem7= getBloodRoutineCol(person, visitId, visitType, TableEnum.MP_COAGULATION_FUNCTION_COL2);
		if(!CollectionUtils.isEmpty(healthItem7)) {
			Map<String, List<HealthItemVO>> collect2 = healthItem7.stream()
					.collect(Collectors.groupingBy(healthItemVO -> healthItemVO.getItemName().split("")[1]));
			collect2.forEach((k,v)->{
				list.addAll(reverseSortedHealthItemVOS(v));
			});
		}
		//血液生化 mp_blood_biochemistry
		List<HealthItemVO>  healthItem8= getBloodRoutineCol(person, visitId, visitType, TableEnum.MP_BLOOD_BIOCHEMISTRY_COL);
		if(!CollectionUtils.isEmpty(healthItem8)) {
			Map<String, List<HealthItemVO>> collect2 = healthItem8.stream()
					.collect(Collectors.groupingBy(healthItemVO -> healthItemVO.getItemName().split("")[1]));
			collect2.forEach((k,v)->{
				list.addAll(reverseSortedHealthItemVOS(v));
			});
		}
		//甲状腺功能
//		HealthItemVO  healthItem10= getThyroidFunction(person, visitId, totalUnitDicts);
//		if(healthItem10 != null ) {
//			list.add(healthItem10);
//		}

		if(HealthConstant.JK.equals(detail.getType())) {
			//激素六项 mp_hormone_items
//			HealthItemVO  healthItem11= getHormoneItems(person, visitId, totalUnitDicts);
//			if(healthItem11 != null ) {
//				list.add(healthItem11);
//			}
			//肿瘤标志物
			List<HealthItemVO>  healthItem9= getBloodRoutineCol(person, visitId, visitType, TableEnum.MP_TUMOR_SCREENING_ITEMS_COL);
			if(!CollectionUtils.isEmpty(healthItem9)) {
				Map<String, List<HealthItemVO>> collect2 = healthItem9.stream()
						.collect(Collectors.groupingBy(healthItemVO -> healthItemVO.getItemName().split("")[1]));
				collect2.forEach((k,v)->{
					list.addAll(reverseSortedHealthItemVOS(v));
				});
			}
			//血气分析 mp_blood_biochemistry
//			HealthItemVO  healthItem12= getBloodGasAnalysis(person, visitId, totalUnitDicts);
//			if(healthItem12 != null ) {
//				list.add(healthItem12);
//			}

		}
		// 糖耐量试验
		List<HealthItemVO>  healthItem13= getBloodRoutineCol(person, visitId, visitType, TableEnum.MP_GLUCOSE_TOLERANCE_COL);
		if(!CollectionUtils.isEmpty(healthItem13)) {
			Map<String, List<HealthItemVO>> collect2 = healthItem13.stream()
					.collect(Collectors.groupingBy(healthItemVO -> healthItemVO.getItemName().split("")[1]));
			collect2.forEach((k,v)->{
				list.addAll(reverseSortedHealthItemVOS(v));
			});
		}

		result.setData(list);
		return result;
	}

	private HealthItemVO getMpGlucoseTolerance(MpPerson person, String visitId, List<MpUnitDict> totalUnitDicts) {

		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		MpGlucoseTolerance mpdb = mpGlucoseToleranceMapper.selectByPersonIdAndVisitId(personId, visitId);
		if(mpdb != null) {
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId( HealthPatientCheckTestEnum.GLUCOSE_TOLERANCE.getCode());
			healthItemVO.setItemName(DateUtils.dateTime(mpdb.getResultTime())+" "+HealthPatientCheckTestEnum.GLUCOSE_TOLERANCE.getValue());

			List<MpUnitDict> mpUnitDicts = totalUnitDicts.stream().filter(mpUnitDict ->
					mpUnitDict.getTableName().equals(HealthRecordCheckConstants.TAB_MP_GLUCOSE_TOLERANCE)).collect(Collectors.toList());
			Map<String, List<MpUnitDict>> collect = mpUnitDicts.stream().collect(Collectors.groupingBy(MpUnitDict::getCode));

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();

			ThreadLocalUtils.put("sex",sex);

			// 获取检验时候的年龄
			threadLocalSetAge(cardId, mpdb.getResultTime());

			ItemValueVO itemValueVO1 = setItemValueVO("孕周", mpdb.getGestationalWeeks(), HealthRecordCheckConstants.ATTR_GESTATIONAL_WEEKS, collect, list);
			ItemValueVO itemValueVO2 = setItemValueVO("身高", mpdb.getHeight(), HealthRecordCheckConstants.ATTR_HEIGHT, collect, list);
			ItemValueVO itemValueVO3 = setItemValueVO("体重", mpdb.getWeight(), HealthRecordCheckConstants.ATTR_WEIGHT, collect, list);
			ItemValueVO itemValueVO4 = setItemValueVO("血糖 0分", mpdb.getEatAfter(), HealthRecordCheckConstants.ATTR_EAT_AFTER, collect, list);
			ItemValueVO itemValueVO5 = setItemValueVO("血糖 30分", mpdb.getEatAfter30min(), HealthRecordCheckConstants.ATTR_EAT_AFTER_30MIN, collect, list);
			ItemValueVO itemValueVO6 = setItemValueVO("血糖 60分", mpdb.getEatAfter1h(), HealthRecordCheckConstants.ATTR_EAT_AFTER_1H, collect, list);
			ItemValueVO itemValueVO7 = setItemValueVO("血糖 120分", mpdb.getEatAfter2h(), HealthRecordCheckConstants.ATTR_EAT_AFTER_2H, collect, list);
			ItemValueVO itemValueVO8 = setItemValueVO("血糖 180分", mpdb.getEatAfter3h(), HealthRecordCheckConstants.ATTR_EAT_AFTER_3H, collect, list);
			ItemValueVO itemValueVO9 = setItemValueVO("血糖 240分", mpdb.getEatAfter4h(), HealthRecordCheckConstants.ATTR_EAT_AFTER_4H, collect, list);
			ItemValueVO itemValueVO10 = setItemValueVO("血糖 300分", mpdb.getEatAfter5h(), HealthRecordCheckConstants.ATTR_EAT_AFTER_5H, collect, list);
			healthItemVO.setObj(list);
			ThreadLocalUtils.release();
			return healthItemVO;
		}

		return null;
	}

	/**
	 * 常见传染病 乙肝
	 *
	 * @param person
	 * @param visitId
	 * @return
	 */
	private HealthItemVO<List<ItemValueVO>> getInfectiousDiseases(MpPerson person, String visitId) {
		String personId = person.getPersonId();
		MpInfectiousDiseases mpdb = mpInfectiousDiseasesMapper.selectByPersonIdAndVisitId(personId, visitId);
		if (mpdb != null) {
			HealthItemVO<List<ItemValueVO>> healthItemVO = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId(HealthPatientCheckTestEnum.HEPATITS_B_ITEMS.getCode());
			healthItemVO.setItemName(DateUtils.dateTime(mpdb.getResultTime())+" "+HealthPatientCheckTestEnum.HEPATITS_B_ITEMS.getValue());

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();
			ItemValueVO itemValueVO1 = setItemValueVO("乙肝表面抗原  ", mpdb.getHbvSag(), list);
			ItemValueVO itemValueVO2 = setItemValueVO("乙肝表面抗体", mpdb.getHbvSab(),list);
			ItemValueVO itemValueVO3 = setItemValueVO("乙肝e抗原", mpdb.getHbvEag(),list);
			ItemValueVO itemValueVO4 = setItemValueVO("乙肝e抗体", mpdb.getHbvEab(),list);
			ItemValueVO itemValueVO5 = setItemValueVO("乙肝核心抗体", mpdb.getHbvCab(),list);
			ItemValueVO itemValueVO6 = setItemValueVO("乙型肝炎病毒dna(hbv-dna)", mpdb.getHbvDna(),list);
			ItemValueVO itemValueVO7 = setItemValueVO("丙型肝炎病毒dna(hcv-rna)", mpdb.getHcvRna(),list);
			ItemValueVO itemValueVO8 = setItemValueVO("丙肝抗体", mpdb.getHcvAb(),list);
			ItemValueVO itemValueVO9 = setItemValueVO("艾滋病抗体", mpdb.getHivAg(),list);
			ItemValueVO itemValueVO10 = setItemValueVO("梅毒抗体", mpdb.getSyphilisAb(),list);
			healthItemVO.setObj(list);
			return healthItemVO;
		}

		return null;


	}
	/**
	 *  甲状腺功能
	 * @param person
	 * @param visitId
	 * @return
	 */
	private  HealthItemVO<List<ItemValueVO>> getThyroidFunction(MpPerson person, String visitId, List<MpUnitDict> totalUnitDicts) {
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		MpThyroidFunction  mpdb =  mpThyroidFunctionMapper.selectByPersonIdAndVisitId(personId, visitId);
		if(mpdb != null) {
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId( HealthPatientCheckTestEnum.THYROID_FUNCTION.getCode());
			healthItemVO.setItemName(DateUtils.dateTime(mpdb.getResultTime())+" "+HealthPatientCheckTestEnum.THYROID_FUNCTION.getValue());

			List<MpUnitDict> mpUnitDicts = totalUnitDicts.stream().filter(mpUnitDict ->
					mpUnitDict.getTableName().equals(HealthRecordCheckConstants.TAB_MP_THYROID_FUNCTION_ITEMS)).collect(Collectors.toList());
			Map<String, List<MpUnitDict>> collect = mpUnitDicts.stream().collect(Collectors.groupingBy(MpUnitDict::getCode));

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();

			ThreadLocalUtils.put("sex",sex);

			// 获取检验时候的年龄
			threadLocalSetAge(cardId, mpdb.getResultTime());

			ItemValueVO itemValueVO1 = setItemValueVO("三碘甲状腺原氨酸(t3) ",mpdb.getThyroidT3(), HealthRecordCheckConstants.ATTR_TRIIODOTHYRONINE, collect, list);
			ItemValueVO itemValueVO2 = setItemValueVO("甲状腺素（t4）",mpdb.getThyroidT4(), HealthRecordCheckConstants.ATTR_THYROXINE, collect, list);
			ItemValueVO itemValueVO3 = setItemValueVO("促甲状腺激素(tsh)",mpdb.getThyroidTsh(), HealthRecordCheckConstants.ATTR_THYROID_STIMULATING_HORMONE, collect, list);
			ItemValueVO itemValueVO4 = setItemValueVO("游离三碘甲状腺原氨酸(t3)",mpdb.getThyroidFt3(), HealthRecordCheckConstants.ATTR_FREE_TRIIODOTHYRONINE, collect, list);
			ItemValueVO itemValueVO5 = setItemValueVO("游离甲状腺素（ft4）",mpdb.getThyroidFt4(), HealthRecordCheckConstants.ATTR_FREE_THYROXINE, collect, list);
			ItemValueVO itemValueVO6 = setItemValueVO("抗甲状腺球蛋白抗体",mpdb.getThyroidTgab(), list);
			ItemValueVO itemValueVO7 = setItemValueVO("抗甲状腺过氧化物酶抗体",mpdb.getThyroidTpoAb(), list);
			ItemValueVO itemValueVO8 = setItemValueVO("促甲状腺素受体抗体",mpdb.getThyroidTrAb(), list);
			ItemValueVO itemValueVO9 = setItemValueVO("抗甲状腺微粒体抗体)",mpdb.getThyroidTmAb(), list);
			ItemValueVO itemValueVO10 = setItemValueVO("甲状腺球蛋白",mpdb.getThyroidGlobulin(), HealthRecordCheckConstants.ATTR_THYROIDGLOBULIN, collect, list);
			healthItemVO.setObj(list);
			ThreadLocalUtils.release();
			return healthItemVO;
		}
		return null;

	}

	/**
	 *肿瘤标志物
	 * @return
	 */
	private HealthItemVO<List<ItemValueVO>> getTumorScreeningItems(MpPerson person, String visitId, List<MpUnitDict> totalUnitDicts){
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		MpTumorScreeningItems mpdb = mpTumorScreeningItemsMapper.selectByPersonIdAndVisitId(personId, visitId);
		if(mpdb != null) {
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId( HealthPatientCheckTestEnum.TUMOR_SCREENING_MEAL.getCode());
			healthItemVO.setItemName(DateUtils.dateTime(mpdb.getResultTime())+" "+HealthPatientCheckTestEnum.TUMOR_SCREENING_MEAL.getValue());

			List<MpUnitDict> mpUnitDicts = totalUnitDicts.stream().filter(mpUnitDict ->
					mpUnitDict.getTableName().equals(HealthRecordCheckConstants.TAB_MP_TUMOR_SCREENING_ITEMS)).collect(Collectors.toList());
			Map<String, List<MpUnitDict>> collect = mpUnitDicts.stream().collect(Collectors.groupingBy(MpUnitDict::getCode));

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();

			ThreadLocalUtils.put("sex",sex);

			// 获取检验时候的年龄
			threadLocalSetAge(cardId, mpdb.getResultTime());

			ItemValueVO itemValueVO1 = setItemValueVO("甲胎蛋白(afp) ",mpdb.getTumorAfp(), HealthRecordCheckConstants.ATTR_ALPHA_FETOPROTEIN, collect, list);
			ItemValueVO itemValueVO2 = setItemValueVO("癌胚抗原(cea)",mpdb.getTumorCea(), HealthRecordCheckConstants.ATTR_CARCINOEMBRYONIC_ANTIGEN, collect, list);
			ItemValueVO itemValueVO3 = setItemValueVO("癌抗原199(ca199) ",mpdb.getTumorCa199(), HealthRecordCheckConstants.ATTR_CARBOHYDRATE_ANTIGEN199, collect, list);
			ItemValueVO itemValueVO4 = setItemValueVO("癌抗原125(ca125)",mpdb.getTumorCa125(), HealthRecordCheckConstants.ATTR_CARBOHYDRATE_ANTIGEN125, collect, list);
			ItemValueVO itemValueVO5 = setItemValueVO("癌抗原242",mpdb.getTumorCa242(), HealthRecordCheckConstants.ATTR_CARBOHYDRATE_ANTIGEN242, collect, list);
			ItemValueVO itemValueVO6 = setItemValueVO("癌抗原153",mpdb.getTumorCa153(), HealthRecordCheckConstants.ATTR_CARBOHYDRATE_ANTIGEN153, collect, list);
			ItemValueVO itemValueVO7 = setItemValueVO("神经元特异性烯醇化酶nse",mpdb.getTumorNse(), HealthRecordCheckConstants.ATTR_NEURON_SPECIFIC_ENOLASE, collect, list);
			ItemValueVO itemValueVO8 = setItemValueVO("癌抗原72-4",mpdb.getTumorCa724(), HealthRecordCheckConstants.ATTR_CARBOHYDRATE_ANTIGEN724, collect, list);
			ItemValueVO itemValueVO9 = setItemValueVO("细胞角蛋白19片段(cyfra21-2)",mpdb.getTumorCyfra21(), HealthRecordCheckConstants.ATTR_CYTOKERATIN19_FRAGMENT, collect, list);
			ItemValueVO itemValueVO10 = setItemValueVO("癌抗原50",mpdb.getTumorCa50(), HealthRecordCheckConstants.ATTR_CARBOHYDRATE_ANTIGEN50, collect, list);
			ItemValueVO itemValueVO11 = setItemValueVO("游离前列腺特异性抗原",mpdb.getTumorFpsa(), HealthRecordCheckConstants.ATTR_FREE_PSM_ANTIGEN, collect, list);
			ItemValueVO itemValueVO12 = setItemValueVO("前列腺特异性抗原",mpdb.getTumorTpsa(), HealthRecordCheckConstants.ATTR_PSM_ANTIGEN, collect, list);
			ItemValueVO itemValueVO13 = setItemValueVO("人附睾蛋白4",mpdb.getTumorHe4(), HealthRecordCheckConstants.ATTR_HUMAN_PIDIDYMIS_ROTEIN, collect, list);
			ItemValueVO itemValueVO14 = setItemValueVO("鳞状细胞癌抗原（scc）",mpdb.getTumorScc(), HealthRecordCheckConstants.ATTR_SCC, collect, list);
			healthItemVO.setObj(list);
			ThreadLocalUtils.release();
			return healthItemVO;
		}

		return null;
	}
	/**
	 *血生化
	 * @return
	 */
	private HealthItemVO<List<ItemValueVO>> getBloodBiochemistry(MpPerson person, String visitId, List<MpUnitDict> totalUnitDicts){
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		MpBloodBiochemistry mpdb = mpBloodBiochemistryMapper.selectByPersonIdAndVisitId(personId, visitId);
		if(mpdb != null) {
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId( HealthPatientCheckTestEnum.BLOOD_BIOCHEMISTRY.getCode());
			healthItemVO.setItemName(DateUtils.dateTime(mpdb.getResultTime())+" "+HealthPatientCheckTestEnum.BLOOD_BIOCHEMISTRY.getValue());

			List<MpUnitDict> mpUnitDicts = totalUnitDicts.stream().filter(mpUnitDict ->
					mpUnitDict.getTableName().equals(HealthRecordCheckConstants.TAB_MP_BLOOD_BIOCHEMISTRY)).collect(Collectors.toList());
			Map<String, List<MpUnitDict>> collect = mpUnitDicts.stream().collect(Collectors.groupingBy(MpUnitDict::getCode));

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();

			ThreadLocalUtils.put("sex",sex);

			// 获取检验时候的年龄
			threadLocalSetAge(cardId, mpdb.getResultTime());

			ItemValueVO itemValueVO1 = setItemValueVO("总蛋白",mpdb.getBloodTp(), HealthRecordCheckConstants.ATTR_TOTAL_PROTEIN, collect, list);
			ItemValueVO itemValueVO2 = setItemValueVO("白蛋白",mpdb.getBloodAlb(), HealthRecordCheckConstants.ATTR_ALBUMIN2, collect, list);
			ItemValueVO itemValueVO3 = setItemValueVO("球蛋白",mpdb.getBloodGlubolin(), HealthRecordCheckConstants.ATTR_GLOBULIN, collect, list);
			ItemValueVO itemValueVO4 = setItemValueVO("白蛋白/球蛋白",mpdb.getBloodAToG(), HealthRecordCheckConstants.ATTR_ALBUMIN_GLOBULIN_RATIO, collect, list);
			ItemValueVO itemValueVO5 = setItemValueVO("天门冬氨酸氨基转移酶",mpdb.getBloodAst(), HealthRecordCheckConstants.ATTR_ASPARTATE_AMINOTRANSFERASE, collect, list);
			ItemValueVO itemValueVO6 = setItemValueVO("丙氨酸氨基转移酶",mpdb.getBloodAlt(), HealthRecordCheckConstants.ATTR_ALANINE_AMINOTRANSFERASE, collect, list);
			ItemValueVO itemValueVO7 = setItemValueVO("ast/alt比值",mpdb.getAstToAlt(), HealthRecordCheckConstants.ATTR_AST_ALT_RATIO, collect, list);
			ItemValueVO itemValueVO8 = setItemValueVO("总胆汁酸",mpdb.getBloodTba(), HealthRecordCheckConstants.ATTR_TOTAL_BILE_ACID, collect, list);
			ItemValueVO itemValueVO9 = setItemValueVO("总胆红素",mpdb.getBloodTbil(), HealthRecordCheckConstants.ATTR_TOTAL_BILIRUBIN, collect, list);
			ItemValueVO itemValueVO10 = setItemValueVO("直接胆红素",mpdb.getBloodDbil(), HealthRecordCheckConstants.ATTR_DIRECT_BILIRUBIN, collect, list);
			ItemValueVO itemValueVO11 = setItemValueVO("间接胆红素",mpdb.getBloodIbil(), HealthRecordCheckConstants.ATTR_INDIRECT_BILIRUBIN, collect, list);
			ItemValueVO itemValueVO12 = setItemValueVO("碱性磷酸酶",mpdb.getBloodAlp(), HealthRecordCheckConstants.ATTR_ALKALINE_PHOSPHATASE, collect, list);
			ItemValueVO itemValueVO13 = setItemValueVO("ꝩ-谷氨酰转移酶",mpdb.getBloodYGt(), HealthRecordCheckConstants.ATTR_GLUTAMIC_PYRUVIC_TRANSAMINASE, collect, list);
			ItemValueVO itemValueVO14 = setItemValueVO("胆碱酯酶",mpdb.getBloodChe(), HealthRecordCheckConstants.ATTR_CHOLINESTERASE, collect, list);
			ItemValueVO itemValueVO15 = setItemValueVO("乳酸脱氢酶",mpdb.getBloodLdh(), HealthRecordCheckConstants.ATTR_LACTATE_DEHYDROGENASE, collect, list);
			ItemValueVO itemValueVO16 = setItemValueVO("α-羟丁酸脱氢酶",mpdb.getBloodHbdh(), HealthRecordCheckConstants.ATTR_ALPHA_HB_DEHYDROGENASE, collect, list);
			ItemValueVO itemValueVO17 = setItemValueVO("肌酸激酶",mpdb.getBloodCk(), HealthRecordCheckConstants.ATTR_CREATINE_KINASE, collect, list);
			ItemValueVO itemValueVO18 = setItemValueVO("肌酸激酶同工酶",mpdb.getBloodCkMb(), HealthRecordCheckConstants.ATTR_CREATINE_KINASE_ISOENZYME, collect, list);
			ItemValueVO itemValueVO19 = setItemValueVO("尿素",mpdb.getBloodBun(), HealthRecordCheckConstants.ATTR_BLOOD_UREA_NITROGEN, collect, list);
			ItemValueVO itemValueVO20 = setItemValueVO("肌酐",mpdb.getBloodCr(), HealthRecordCheckConstants.ATTR_CREATININE, collect, list);
			ItemValueVO itemValueVO21 = setItemValueVO("尿酸",mpdb.getBloodUa(), HealthRecordCheckConstants.ATTR_URIC_ACID, collect, list);
			ItemValueVO itemValueVO22 = setItemValueVO("空腹血葡萄糖",mpdb.getBloodGlucose(), HealthRecordCheckConstants.ATTR_FASTING_BLOOD_GLUCOSE, collect, list);
			ItemValueVO itemValueVO23 = setItemValueVO("血清胱抑素c",mpdb.getBloodCpi(), HealthRecordCheckConstants.ATTR_SERUM_CYSTATIN_C, collect, list);
			ItemValueVO itemValueVO24 = setItemValueVO("β2-微球蛋白",mpdb.getBloodΒ2Mg(), HealthRecordCheckConstants.ATTR_BLOOD_B2_MG, collect, list);
			ItemValueVO itemValueVO25 = setItemValueVO("钾",mpdb.getBloodK(), HealthRecordCheckConstants.ATTR_POTASSIUM, collect, list);
			ItemValueVO itemValueVO26 = setItemValueVO("钠",mpdb.getBloodNa(), HealthRecordCheckConstants.ATTR_SODIUM, collect, list);
			ItemValueVO itemValueVO27 = setItemValueVO("氯",mpdb.getBloodCl(), HealthRecordCheckConstants.ATTR_CHLORIDE, collect, list);
			ItemValueVO itemValueVO28 = setItemValueVO("钙",mpdb.getBloodCa(), HealthRecordCheckConstants.ATTR_CALCIUM, collect, list);
			ItemValueVO itemValueVO29 = setItemValueVO("磷",mpdb.getBloodIp(), HealthRecordCheckConstants.ATTR_PHOSPHORUS, collect, list);
			ItemValueVO itemValueVO30 = setItemValueVO("镁",mpdb.getBloodMg(), HealthRecordCheckConstants.ATTR_MAGNESIUM, collect, list);
			ItemValueVO itemValueVO31 = setItemValueVO("总二氧化碳总量",mpdb.getBloodTco2(), HealthRecordCheckConstants.ATTR_CARBON_DIOXIDE, collect, list);
			ItemValueVO itemValueVO32 = setItemValueVO("甘油三酯",mpdb.getBloodTg(), HealthRecordCheckConstants.ATTR_TRIGLYCERIDES, collect, list);
			ItemValueVO itemValueVO33 = setItemValueVO("总胆固醇",mpdb.getBloodCho(), HealthRecordCheckConstants.ATTR_TOTAL_CHOLESTEROL, collect, list);
			ItemValueVO itemValueVO34 = setItemValueVO("高密度脂蛋白胆固醇",mpdb.getBloodHdlC(), HealthRecordCheckConstants.ATTR_HDL_CHOLESTEROL, collect, list);
			ItemValueVO itemValueVO35 = setItemValueVO("低密度脂蛋白胆固醇",mpdb.getBloodLdlC(), HealthRecordCheckConstants.ATTR_LDL_CHOLESTEROL, collect, list);
			ItemValueVO itemValueVO36 = setItemValueVO("载脂蛋白a-1",mpdb.getBloodApoA1(), HealthRecordCheckConstants.ATTR_APOLIPOPROTEIN_A1, collect, list);
			ItemValueVO itemValueVO37 = setItemValueVO("载脂蛋白b",mpdb.getBloodApoB(), HealthRecordCheckConstants.ATTR_APOLIPOPROTEIN_B, collect, list);
			ItemValueVO itemValueVO38 = setItemValueVO("载脂蛋白a-1/b",mpdb.getBloodApoA1ToB(), HealthRecordCheckConstants.ATTR_APOLIPOPROTEIN_A1B_RATIO, collect, list);
			ItemValueVO itemValueVO39 = setItemValueVO("脂蛋白a ",mpdb.getBloodLpa(), HealthRecordCheckConstants.ATTR_BLOOD_LPA, collect, list);
			healthItemVO.setObj(list);
			ThreadLocalUtils.release();
			return healthItemVO;
		}

		return null;
	}
	/**
	 *凝血系列检查
	 * @return
	 */
	private HealthItemVO<List<ItemValueVO>> getCoagulationFunction(MpPerson person, String visitId, List<MpUnitDict> totalUnitDicts){
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		MpCoagulationFunction  mpdb =  mpCoagulationFunctionMapper.selectByPersonIdAndVisitId(personId, visitId);
		if(mpdb != null) {
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId( HealthPatientCheckTestEnum.COAGULATION_FOUR.getCode());
			healthItemVO.setItemName(DateUtils.dateTime(mpdb.getResultTime())+" "+HealthPatientCheckTestEnum.COAGULATION_FOUR.getValue());

			List<MpUnitDict> mpUnitDicts = totalUnitDicts.stream().filter(mpUnitDict ->
					mpUnitDict.getTableName().equals(HealthRecordCheckConstants.TAB_MP_COAGULATION_ITEMS)).collect(Collectors.toList());
			Map<String, List<MpUnitDict>> collect = mpUnitDicts.stream().collect(Collectors.groupingBy(MpUnitDict::getCode));

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();

			ThreadLocalUtils.put("sex",sex);

			// 获取检验时候的年龄
			threadLocalSetAge(cardId, mpdb.getResultTime());

			ItemValueVO itemValueVO1 = setItemValueVO("凝血酶原时间",mpdb.getCoagulationPt(), HealthRecordCheckConstants.ATTR_PROTHROMBIN_TIME, collect, list);
			ItemValueVO itemValueVO2 = setItemValueVO("活化部分凝血活酶时间",mpdb.getCoagulationAptt(), HealthRecordCheckConstants.ATTR_APTT, collect, list);
			ItemValueVO itemValueVO3 = setItemValueVO("纤维蛋白原含量",mpdb.getCoagulationFib(), HealthRecordCheckConstants.ATTR_FIBRINOGEN, collect, list);
			ItemValueVO itemValueVO4 = setItemValueVO("凝血酶时间",mpdb.getCoagulationTt(), HealthRecordCheckConstants.ATTR_THROMBIN_TIME, collect, list);
			ItemValueVO itemValueVO5 = setItemValueVO("纤维蛋白(原)降解产物",mpdb.getCoagulationFdp(), HealthRecordCheckConstants.ATTR_FDP, collect, list);
			ItemValueVO itemValueVO6 = setItemValueVO("d-二聚体定量",mpdb.getCoagulationDdimer(), HealthRecordCheckConstants.ATTR_D_DIMER_QUANTITATIVE_TEST, collect, list);
			ItemValueVO itemValueVO7 = setItemValueVO("国际标准化比值",mpdb.getCoagulationInr(), HealthRecordCheckConstants.ATTR_INTERNATIONAL_NORM_RATIO, collect, list);
			ItemValueVO itemValueVO8 = setItemValueVO("凝血酶原活动度",mpdb.getCoagulationPta(), HealthRecordCheckConstants.ATTR_PROTHROMBIN_ACTIVITY, collect, list);

			healthItemVO.setObj(list);
			ThreadLocalUtils.release();
			return healthItemVO;
		}
		return null;
	}
	/**
	 *血气分析
	 * @return
	 */
	private HealthItemVO<List<ItemValueVO>> getBloodGasAnalysis(MpPerson person, String visitId, List<MpUnitDict> totalUnitDicts){
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		MpBloodGasAnalysis  mpdb =  mpBloodGasAnalysisMapper.selectByPersonIdAndVisitId(personId, visitId);
		if(mpdb != null) {
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId( HealthPatientCheckTestEnum.BLOOD_GAS_ANALYSIS.getCode());
			healthItemVO.setItemName(DateUtils.dateTime(mpdb.getResultTime())+" "+HealthPatientCheckTestEnum.BLOOD_GAS_ANALYSIS.getValue());

			List<MpUnitDict> mpUnitDicts = totalUnitDicts.stream().filter(mpUnitDict ->
					mpUnitDict.getTableName().equals(HealthRecordCheckConstants.TAB_MP_BLOOD_GAS_ANALYSIS)).collect(Collectors.toList());
			Map<String, List<MpUnitDict>> collect = mpUnitDicts.stream().collect(Collectors.groupingBy(MpUnitDict::getCode));

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();

			ThreadLocalUtils.put("sex",sex);

			// 获取检验时候的年龄
			threadLocalSetAge(cardId, mpdb.getResultTime());

			ItemValueVO itemValueVO1 = setItemValueVO("ph值",mpdb.getBloodPh(), HealthRecordCheckConstants.ATTR_BLOOD_PH, collect, list);
			ItemValueVO itemValueVO2 = setItemValueVO("动脉血二氧化碳分压",mpdb.getBloodPco2(), HealthRecordCheckConstants.ATTR_BLOOD_PCO2, collect, list);
			ItemValueVO itemValueVO3 = setItemValueVO("动脉血氧分压",mpdb.getBloodPo2(), HealthRecordCheckConstants.ATTR_BLOOD_PO2, collect, list);
			ItemValueVO itemValueVO4 = setItemValueVO("红细胞压积",mpdb.getBloodHct(), HealthRecordCheckConstants.ATTR_BLOOD_HCT, collect, list);
			ItemValueVO itemValueVO5 = setItemValueVO("血红蛋白",mpdb.getBloodHb(), HealthRecordCheckConstants.ATTR_BLOOD_HB, collect, list);
			ItemValueVO itemValueVO6 = setItemValueVO("剩余碱",mpdb.getBloodBe(), HealthRecordCheckConstants.ATTR_BLOOD_BE, collect, list);
			ItemValueVO itemValueVO7 = setItemValueVO("标准碳酸根",mpdb.getBloodSb(), HealthRecordCheckConstants.ATTR_BLOOD_SB, collect, list);
			ItemValueVO itemValueVO8 = setItemValueVO("实际碳酸根",mpdb.getBloodAb(), HealthRecordCheckConstants.ATTR_BLOOD_AB, collect, list);
			ItemValueVO itemValueVO9 = setItemValueVO("总二氧化碳总量",mpdb.getBloodTco2(), HealthRecordCheckConstants.ATTR_BLOOD_TCO2, collect, list);
			ItemValueVO itemValueVO10 = setItemValueVO("氧饱和度50%时氧分压",mpdb.getBloodPo2P50(), HealthRecordCheckConstants.ATTR_BLOOD_PO2_P50, collect, list);
			ItemValueVO itemValueVO11 = setItemValueVO("血氧饱和度",mpdb.getBloodSato2(), HealthRecordCheckConstants.ATTR_BLOOD_SATO2, collect, list);
			ItemValueVO itemValueVO12 = setItemValueVO("血氧含量",mpdb.getBloodTo2(), HealthRecordCheckConstants.ATTR_BLOOD_TO2, collect, list);
			ItemValueVO itemValueVO13 = setItemValueVO("氧容量",mpdb.getBloodO2Cap(), HealthRecordCheckConstants.ATTR_BLOOD_O2_CAP, collect, list);
			healthItemVO.setObj(list);
			ThreadLocalUtils.release();
			return healthItemVO;
		}

		return null;
	}
	/**
	 *激素六项
	 * @return
	 */
	private HealthItemVO<List<ItemValueVO>> getHormoneItems(MpPerson person, String visitId, List<MpUnitDict> totalUnitDicts){
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		MpHormoneItems mpdb = mpHormoneItemsMapper.selectByPersonIdAndVisitId(personId, visitId);
		if(mpdb != null) {
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId( HealthPatientCheckTestEnum.HORMONE_ITEMS.getCode());
			healthItemVO.setItemName(DateUtils.dateTime(mpdb.getResultTime())+" "+HealthPatientCheckTestEnum.HORMONE_ITEMS.getValue());

			List<MpUnitDict> mpUnitDicts = totalUnitDicts.stream().filter(mpUnitDict ->
					mpUnitDict.getTableName().equals(HealthRecordCheckConstants.TAB_MP_HORMONE_ITEMS)).collect(Collectors.toList());
			Map<String, List<MpUnitDict>> collect = mpUnitDicts.stream().collect(Collectors.groupingBy(MpUnitDict::getCode));

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();

			ThreadLocalUtils.put("sex",sex);

			// 获取检验时候的年龄
			threadLocalSetAge(cardId, mpdb.getResultTime());

			ItemValueVO itemValueVO1= setItemValueVO("卵泡刺激素(fsh)", mpdb.getHormoneFsh(), HealthRecordCheckConstants.ATTR_FOLLICLE_STIMULATING_HORMONE, collect, list);
			ItemValueVO itemValueVO2= setItemValueVO("黄体生成素（LH）", mpdb.getHormoneLh(), HealthRecordCheckConstants.ATTR_LUTEINIZING_HORMONE, collect, list);
			ItemValueVO itemValueVO3= setItemValueVO("雌二醇（e2）", mpdb.getHormoneE2(), HealthRecordCheckConstants.ATTR_ESTRADIOL, collect, list);
			ItemValueVO itemValueVO4= setItemValueVO("孕酮（p）", mpdb.getHormoneP(), HealthRecordCheckConstants.ATTR_PROGESTERONE, collect, list);
			ItemValueVO itemValueVO5= setItemValueVO("睾酮（t）", mpdb.getHormoneT(), HealthRecordCheckConstants.ATTR_TESTOSTERONE, collect, list);
			ItemValueVO itemValueVO6= setItemValueVO("催乳素（prl）", mpdb.getHormonePrl(), HealthRecordCheckConstants.ATTR_PROLACTIN, collect, list);
			healthItemVO.setObj(list);
			ThreadLocalUtils.release();
			return healthItemVO;
		}
		return null;
	}
	/**
	 *  便常规
	 * @param person
	 * @param visitId
	 * @return
	 */
	private  HealthItemVO<List<ItemValueVO>> getStoolRoutine(MpPerson person, String visitId) {
		String personId = person.getPersonId();
		MpStoolRoutine mpdb = mpStoolRoutineMapper.selectByPersonIdAndVisitId(personId, visitId);
		if(mpdb != null) {
			HealthItemVO<List<ItemValueVO>> healthItemVO = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId( HealthPatientCheckTestEnum.STOOL_ROUTINE.getCode());
			healthItemVO.setItemName(DateUtils.dateTime(mpdb.getResultTime())+" "+HealthPatientCheckTestEnum.STOOL_ROUTINE.getValue());

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();
			ItemValueVO itemValueVO1= setItemValueVO("大便颜色", mpdb.getFecesColour(),list);
			ItemValueVO itemValueVO2= setItemValueVO("大便性状", mpdb.getFecesCharacter(),list);
			ItemValueVO itemValueVO3= setItemValueVO("便红细胞", mpdb.getFecesRbc(), list);
			ItemValueVO itemValueVO4= setItemValueVO("便白细胞", mpdb.getFecesWbc(), list);
			ItemValueVO itemValueVO5= setItemValueVO("便隐血", mpdb.getFecesOb(), list);
			ItemValueVO itemValueVO6= setItemValueVO("蛔虫卵", mpdb.getFecesAscarid(), list);
			ItemValueVO itemValueVO7= setItemValueVO("鞭虫卵", mpdb.getFecesWhipworm(), list);
			ItemValueVO itemValueVO8= setItemValueVO("蛲虫卵", mpdb.getFecesPinworm(), list);
			ItemValueVO itemValueVO9= setItemValueVO("绦虫卵", mpdb.getFecesTapeworm(), list);
			healthItemVO.setObj(list);
			return healthItemVO;
		}
		return null;
	}
	/**
	 *  尿常规
	 * @param person
	 * @param visitId
	 * @return
	 */
	private  HealthItemVO<List<ItemValueVO>> getUrineRoutine(MpPerson person, String visitId, List<MpUnitDict> totalUnitDicts) {
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		MpUrineRoutine mpdb = mpUrineRoutineMapper.selectByPersonIdAndVisitId(personId, visitId);
		if(mpdb != null) {
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId( HealthPatientCheckTestEnum.URINE_ROUTINE.getCode());
			healthItemVO.setItemName(DateUtils.dateTime(mpdb.getResultTime())+" "+HealthPatientCheckTestEnum.URINE_ROUTINE.getValue());

			List<MpUnitDict> mpUnitDicts = totalUnitDicts.stream().filter(mpUnitDict ->
					mpUnitDict.getTableName().equals(HealthRecordCheckConstants.TAB_MP_URINE_ROUTINE)).collect(Collectors.toList());
			Map<String, List<MpUnitDict>> collect = mpUnitDicts.stream().collect(Collectors.groupingBy(MpUnitDict::getCode));

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();

			ThreadLocalUtils.put("sex",sex);

			// 获取检验时候的年龄
			threadLocalSetAge(cardId, mpdb.getResultTime());

			ItemValueVO itemValueVO1= setItemValueVO("尿比重", mpdb.getUrineSg(), HealthRecordCheckConstants.ATTR_SPECIFIC_GRAVITY_URINE, collect, list);
			ItemValueVO itemValueVO2= setItemValueVO("尿酸碱度", mpdb.getUrinePh(), HealthRecordCheckConstants.ATTR_URINE_PH, collect, list);
			ItemValueVO itemValueVO3= setItemValueVO("尿白细胞", mpdb.getUrineWbcCount(), HealthRecordCheckConstants.ATTR_URINE_WHITE_CELLS, collect, list);
			ItemValueVO itemValueVO4= setItemValueVO("尿红细胞", mpdb.getUrineRbcCount(), HealthRecordCheckConstants.ATTR_URINE_RED_CELLS, collect, list);
//			ItemValueVO itemValueVO5= setItemValueVO("尿亚硝酸盐", mpdb.getUrineNit(), HealthRecordCheckConstants.ATTR_URINE_NITRITE, collect, list);
			ItemValueVO itemValueVO5= setItemValueVO("尿亚硝酸盐",mpdb.getUrineNit(), list);
			ItemValueVO itemValueVO6= setItemValueVO("尿蛋白质", mpdb.getUrinePro(),list);
			ItemValueVO itemValueVO7= setItemValueVO("尿糖", mpdb.getUrineGlu(),  list);
			ItemValueVO itemValueVO8= setItemValueVO("尿酮体", mpdb.getUrineKet(), list);
			ItemValueVO itemValueVO9= setItemValueVO("尿胆原", mpdb.getUrineUro(), list);
			ItemValueVO itemValueVO10= setItemValueVO("胆红素", mpdb.getUrineBil(), list);
			ItemValueVO itemValueVO11= setItemValueVO("尿隐血", mpdb.getUrineBlo(), list);
			healthItemVO.setObj(list);
			ThreadLocalUtils.release();
			return healthItemVO;
		}
		return null;

	}
	/**
	 *  血常规
	 * @param personId
	 * @param visitId
	 * @return
	 */
	private  HealthItemVO<List<ItemValueVO>> getBloodRoutine(MpPerson person, String visitId, List<MpUnitDict> totalUnitDicts) {
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		MpBloodRoutine mpdb = mpBloodRoutineMapper.selectByPersonIdAndVisitId(personId, visitId);
		if(mpdb != null) {
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId( HealthPatientCheckTestEnum.BLOOD_ROUTINE.getCode());
			healthItemVO.setItemName(DateUtils.dateTime(mpdb.getResultTime())+" "+HealthPatientCheckTestEnum.BLOOD_ROUTINE.getValue());

			List<MpUnitDict> mpUnitDicts = totalUnitDicts.stream().filter(mpUnitDict ->
					mpUnitDict.getTableName().equals(HealthRecordCheckConstants.TAB_MP_BLOOD_ROUTINE)).collect(Collectors.toList());
			Map<String, List<MpUnitDict>> collect = mpUnitDicts.stream().collect(Collectors.groupingBy(MpUnitDict::getCode));

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();

			ThreadLocalUtils.put("sex",sex);

			// 获取检验时候的年龄
			threadLocalSetAge(cardId, mpdb.getResultTime());

			//白细胞计数
			ItemValueVO itemValueVO1= setItemValueVO("白细胞计数", mpdb.getWbcCount(), HealthRecordCheckConstants.ATTR_WHITE_CELL_COUNT, collect, list);
			ItemValueVO itemValueVO2= setItemValueVO("红细胞计数", mpdb.getRbcCount(), HealthRecordCheckConstants.ATTR_RED_CELL_COUNT, collect, list);
			ItemValueVO itemValueVO3= setItemValueVO("血红蛋白", mpdb.getBloodHb(), HealthRecordCheckConstants.ATTR_HEMOGLOBIN, collect, list);
			ItemValueVO itemValueVO4= setItemValueVO("红细胞压积", mpdb.getBloodHct(), HealthRecordCheckConstants.ATTR_HEMATOCRIT, collect, list);
			ItemValueVO itemValueVO5= setItemValueVO("平均红细胞体积", mpdb.getBloodMcv(), HealthRecordCheckConstants.ATTR_MEAN_CORPUS_VOLUME, collect, list);
			ItemValueVO itemValueVO6= setItemValueVO("平均红细胞血红蛋白含量", mpdb.getBloodMch(), HealthRecordCheckConstants.ATTR_MEAN_CORPUS_VOLUME, collect, list);
			ItemValueVO itemValueVO7= setItemValueVO("平均红细胞血红蛋白浓度", mpdb.getBloodMchc(), HealthRecordCheckConstants.ATTR_MEAN_CORPUS_HEM_CONCENT, collect, list);
			ItemValueVO itemValueVO8= setItemValueVO("红细胞分布宽度-变异系数", mpdb.getBloodRdwCv(), HealthRecordCheckConstants.ATTR_RED_CELL_DIS_WIDTH_VARIATION, collect, list);
			ItemValueVO itemValueVO9= setItemValueVO("红细胞分布宽度-标准差", mpdb.getBloodRdwSd(), HealthRecordCheckConstants.ATTR_RED_CELL_DIS_DEVIATION, collect, list);
			ItemValueVO itemValueVO10= setItemValueVO("血小板计数", mpdb.getBloodPltCount(), HealthRecordCheckConstants.ATTR_PLATELET_COUNT, collect, list);
			ItemValueVO itemValueVO11= setItemValueVO("平均血小板体积", mpdb.getBloodMpv(), HealthRecordCheckConstants.ATTR_MEAN_PLATELET_VOLUME, collect, list);
			ItemValueVO itemValueVO12= setItemValueVO("血小板压积", mpdb.getBloodPct(), HealthRecordCheckConstants.ATTR_PLATELETCRIT, collect, list);
			ItemValueVO itemValueVO13= setItemValueVO("血小板分布宽度", mpdb.getBloodPdw(), HealthRecordCheckConstants.ATTR_PLATELET_DIS_WIDTH, collect, list);
			ItemValueVO itemValueVO14= setItemValueVO("淋巴细胞绝对值", mpdb.getBloodLyCount(), HealthRecordCheckConstants.ATTR_ABS_LYMPHOCYTE_COUNT, collect, list);
			ItemValueVO itemValueVO15= setItemValueVO("淋巴细胞百分比", mpdb.getBloodLyPercentage(), HealthRecordCheckConstants.ATTR_LYMPHOCYTE_PERCENTAGE, collect, list);
			ItemValueVO itemValueVO16= setItemValueVO("中性粒细胞绝对值", mpdb.getBloodNeCount(), HealthRecordCheckConstants.ATTR_ABS_NEUTROPHIL_COUNT, collect, list);
			ItemValueVO itemValueVO17= setItemValueVO("中性粒细胞百分比", mpdb.getBloodNePercentage(), HealthRecordCheckConstants.ATTR_NEUTROPHIL_PERCENTAGE, collect, list);
			ItemValueVO itemValueVO18= setItemValueVO("单核细胞绝对值", mpdb.getBloodMoCount(), HealthRecordCheckConstants.ATTR_ABS_MONOCYTE_COUNT, collect, list);
			ItemValueVO itemValueVO19= setItemValueVO("单核细胞百分比", mpdb.getBloodMoPercentage(), HealthRecordCheckConstants.ATTR_MONOCYTE_PERCENTAGE, collect, list);
			ItemValueVO itemValueVO20= setItemValueVO("嗜酸性细胞绝对值", mpdb.getBloodEoCount(), HealthRecordCheckConstants.ATTR_ABS_EOSINOPHIL_COUNT, collect, list);
			ItemValueVO itemValueVO21= setItemValueVO("嗜酸性细胞百分比", mpdb.getBloodEoPercentage(), HealthRecordCheckConstants.ATTR_EOSINOPHIL_PERCENTAGE, collect, list);
			ItemValueVO itemValueVO22= setItemValueVO("嗜碱性细胞绝对值", mpdb.getBloodBaCount(), HealthRecordCheckConstants.ATTR_ABS_BASOPHIL_COUNT, collect, list);
			ItemValueVO itemValueVO23= setItemValueVO("嗜碱性细胞百分比", mpdb.getBloodBaPercentage(), HealthRecordCheckConstants.ATTR_BASOPHIL_PERCENTAGE, collect, list);
			healthItemVO.setObj(list);
			ThreadLocalUtils.release();
			return healthItemVO;
		}
		return null;

	}



	/**
	 *  无痛胃肠镜painless_gastroenteroscopy
	 * @return
	 */
	private   HealthItemVO<List<ItemValueVO>>    getPainlessGastroenteroscopy(String v) {
		if(StringUtils.isNotBlankAndNotEmpty(v)) {
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId( HealthPatientCheckExamEnum.PAINLESS_GASTROENTEROSCOPY.getCode());
			healthItemVO.setItemName(HealthPatientCheckExamEnum.PAINLESS_GASTROENTEROSCOPY.getValue());
			List<ItemValueVO>  list = new ArrayList<ItemValueVO>();
			ItemValueVO itemValueVO = new ItemValueVO();
			itemValueVO.setValue(v);
			list.add(itemValueVO);
			healthItemVO.setObj(list);
			return healthItemVO;
		}

		return null;

	}
	/**
	 *  无痛胃镜painless_gastroscope
	 * @return
	 */
	private   HealthItemVO<List<ItemValueVO>>   getPainlessGastroscope(String v) {
		if(StringUtils.isNotBlankAndNotEmpty(v)) {
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId( HealthPatientCheckExamEnum.PAINLESS_GASTROSCOPE.getCode());
			healthItemVO.setItemName(HealthPatientCheckExamEnum.PAINLESS_GASTROSCOPE.getValue());
			List<ItemValueVO>  list = new ArrayList<ItemValueVO>();
			ItemValueVO itemValueVO = new ItemValueVO();
			itemValueVO.setValue(v);
			list.add(itemValueVO);
			healthItemVO.setObj(list);
			return healthItemVO;
		}

		return null;

	}
	/**
	 *  普通肠镜
	 * @return
	 */
	private   HealthItemVO<List<ItemValueVO>>    getConventionalEnteroscope(String v) {
		if(StringUtils.isNotBlankAndNotEmpty(v)) {
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId( HealthPatientCheckExamEnum.CONVENTIONAL_ENTEROSCOPE.getCode());
			healthItemVO.setItemName(HealthPatientCheckExamEnum.CONVENTIONAL_ENTEROSCOPE.getValue());
			List<ItemValueVO>  list = new ArrayList<ItemValueVO>();
			ItemValueVO itemValueVO = new ItemValueVO();
			itemValueVO.setValue(v);
			list.add(itemValueVO);
			healthItemVO.setObj(list);
			return healthItemVO;
		}

		return null;

	}
	/**
	 *  心电图
	 * @return
	 */
	private  HealthItemVO<List<ItemValueVO>>   getEcg(String v) {
		if(StringUtils.isNotBlankAndNotEmpty(v)) {
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId( HealthPatientCheckExamEnum.ECG.getCode());
			healthItemVO.setItemName(HealthPatientCheckExamEnum.ECG.getValue());
			List<ItemValueVO>  list = new ArrayList<ItemValueVO>();
			ItemValueVO itemValueVO = new ItemValueVO();
			itemValueVO.setValue(v);
			list.add(itemValueVO);
			healthItemVO.setObj(list);
			return healthItemVO;
		}

		return null;

	}
	/**
	 *  口腔科检查
	 * @param personId
	 * @param visitId
	 * @return
	 */
	private  HealthItemVO<List<ItemValueVO>>   getOral(String personId,String visitId) {
		MpStomatologyExam   mpdb = mpStomatologyExamMapper.selectByPersonIdAndVisitId(personId, visitId);
		if(mpdb != null) {
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId( HealthPatientCheckExamEnum.ORAL.getCode());
			healthItemVO.setItemName(HealthPatientCheckExamEnum.ORAL.getValue());
			List<ItemValueVO>  list = new ArrayList<ItemValueVO>();
			ItemValueVO itemValueVO1 = setItemValueVO("牙体组织疾病",mpdb.getDentalTissue(),list);
			ItemValueVO itemValueVO2 = setItemValueVO("牙周组织疾病",mpdb.getDentalPeriTissue(), list);
			ItemValueVO itemValueVO3 = setItemValueVO("口腔黏膜疾病",mpdb.getOralMucosa(), list);
			ItemValueVO itemValueVO4 = setItemValueVO("口腔颌面外科疾病 ",mpdb.getOralMaxillofacial(), list);
			ItemValueVO itemValueVO5 = setItemValueVO("口腔修复科",mpdb.getProsthodontics(), list);
			ItemValueVO itemValueVO6 = setItemValueVO("正畸科",mpdb.getOrthodontics(), list);
			String healthExamDate = null;
			if(mpdb.getHealthExamDate() != null) {
				healthExamDate =DateUtils.parseDateToStrShort(mpdb.getHealthExamDate());
			}
			ItemValueVO itemValueVO14 = setItemValueVO("体检日期",healthExamDate, list);
			String resultDate = null;
			if(mpdb.getResultDate() != null) {
				resultDate =DateUtils.parseDateToStrShort(mpdb.getResultDate());
			}
			ItemValueVO itemValueVO15 = setItemValueVO("结果日期",resultDate, list);
			healthItemVO.setObj(list);
			return healthItemVO;
		}
		return null;

	}

	private HealthItemVO getCtExam(String personId, String visitId) {
		List<MpCtExam> mpCtExams = mpCtExamMapper.selectMpCtExamList(personId, visitId, null);
		if(!CollectionUtils.isEmpty(mpCtExams)) {
			HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
			healthItemVO.setItemId(HealthPatientCheckExamEnum.CT_EXAM.getCode());
			healthItemVO.setItemName(HealthPatientCheckExamEnum.CT_EXAM.getValue());
			List<CatalogVO> result = new ArrayList<>();

			for(MpCtExam mpCtExam : mpCtExams){
				CatalogVO catalogVO = new CatalogVO();
				catalogVO.setItemTitle(mpCtExam.getTechnologyName());
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				setItemValueVO("检查项目",mpCtExam.getTechnologyName(),list);
				setItemValueVO("身体部位",mpCtExam.getSctPosition(),list);
				setItemValueVO("检查方式",mpCtExam.getTestMethod(),list);
				setItemValueVO("检查报告结果--客观所见",mpCtExam.getObjectiveView(),list);
				setItemValueVO("检查报告结果--主观提示",mpCtExam.getSubjectiveHints(),list);
				setItemValueVO("检查诊断",mpCtExam.getRelevantClinicDiagnosis(),list);
				setItemValueVO("检查时间",DateUtils.dateTime(mpCtExam.getTechnologyDate()),list);
				setItemValueVO("报告时间",DateUtils.dateTime(mpCtExam.getReportDate()),list);
				setItemValueVO("开单科室",mpCtExam.getDepartmentName(),list);
				catalogVO.setItemList(list);
				result.add(catalogVO);
			}
			healthItemVO.setArray(result);
			return healthItemVO;

		}
		return null;
	}

	private HealthItemVO getNuclearMagnetismExam(String personId, String visitId) {
		List<MpNuclearMagnetismExam> mpNuclearMagnetismExams = mpNuclearMagnetismExamMapper.selectMpNuclearMagnetismExamList(personId, visitId, null);
		if(!CollectionUtils.isEmpty(mpNuclearMagnetismExams)) {
			HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
			healthItemVO.setItemId(HealthPatientCheckExamEnum.NUCLEAR_MAGNETISM_EXAM.getCode());
			healthItemVO.setItemName(HealthPatientCheckExamEnum.NUCLEAR_MAGNETISM_EXAM.getValue());
			List<CatalogVO> result = new ArrayList<>();
			for(MpNuclearMagnetismExam mpNuclearMagnetismExam : mpNuclearMagnetismExams){
				CatalogVO catalogVO = new CatalogVO();
				catalogVO.setItemTitle(mpNuclearMagnetismExam.getTechnologyName());
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				setItemValueVO("检查项目",mpNuclearMagnetismExam.getTechnologyName(),list);
				setItemValueVO("身体部位",mpNuclearMagnetismExam.getSctPosition(),list);
				setItemValueVO("检查方式",mpNuclearMagnetismExam.getTestMethod(),list);
				setItemValueVO("检查报告结果--客观所见",mpNuclearMagnetismExam.getObjectiveView(),list);
				setItemValueVO("检查报告结果--主观提示",mpNuclearMagnetismExam.getSubjectiveHints(),list);
				setItemValueVO("检查诊断",mpNuclearMagnetismExam.getRelevantClinicDiagnosis(),list);
				setItemValueVO("检查时间",DateUtils.dateTime(mpNuclearMagnetismExam.getTechnologyDate()),list);
				setItemValueVO("报告时间",DateUtils.dateTime(mpNuclearMagnetismExam.getReportDate()),list);
				setItemValueVO("开单科室",mpNuclearMagnetismExam.getDepartmentName(),list);
				catalogVO.setItemList(list);
				result.add(catalogVO);
			}
			healthItemVO.setArray(result);
			return healthItemVO;
		}
		return null;
	}

	private HealthItemVO getXRayExam(String personId, String visitId) {
		List<MpXRayExam> mpXRayExams = mpXRayExamMapper.selectMpXRayExamList(personId, visitId,null);
		if(!CollectionUtils.isEmpty(mpXRayExams)) {
			HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
			healthItemVO.setItemId(HealthPatientCheckExamEnum.X_RAY_EXAM.getCode());
			healthItemVO.setItemName(HealthPatientCheckExamEnum.X_RAY_EXAM.getValue());
			List<CatalogVO> result = new ArrayList<>();
			for(MpXRayExam mpXRayExam : mpXRayExams){
				CatalogVO catalogVO = new CatalogVO();
				catalogVO.setItemTitle(mpXRayExam.getTechnologyName());
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				setItemValueVO("检查项目",mpXRayExam.getTechnologyName(),list);
				setItemValueVO("身体部位",mpXRayExam.getSctPosition(),list);
				setItemValueVO("检查方式",mpXRayExam.getTestMethod(),list);
				setItemValueVO("检查报告结果--客观所见",mpXRayExam.getObjectiveView(),list);
				setItemValueVO("检查报告结果--主观提示",mpXRayExam.getSubjectiveHints(),list);
				setItemValueVO("检查诊断",mpXRayExam.getRelevantClinicDiagnosis(),list);
				setItemValueVO("检查时间",DateUtils.dateTime(mpXRayExam.getTechnologyDate()),list);
				setItemValueVO("报告时间",DateUtils.dateTime(mpXRayExam.getReportDate()),list);
				setItemValueVO("开单科室",mpXRayExam.getDepartmentName(),list);
				catalogVO.setItemList(list);
				result.add(catalogVO);
			}
			healthItemVO.setArray(result);
			return healthItemVO;
		}
		return null;
	}

	private HealthItemVO getUltrasonicExam(String personId, String visitId) {
		List<MpUltrasonicExam> mpUltrasonicExams = mpUltrasonicExamMapper.selectMpUltrasonicExamList(personId, visitId, null);
		if(!CollectionUtils.isEmpty(mpUltrasonicExams)) {
			HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
			healthItemVO.setItemId(HealthPatientCheckExamEnum.ULTRASONIC_EXAM.getCode());
			healthItemVO.setItemName(HealthPatientCheckExamEnum.ULTRASONIC_EXAM.getValue());
			List<CatalogVO> result = new ArrayList<>();
			for(MpUltrasonicExam mpUltrasonicExam : mpUltrasonicExams){
				CatalogVO catalogVO = new CatalogVO();
				catalogVO.setItemTitle(mpUltrasonicExam.getTechnologyName());
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				setItemValueVO("检查项目",mpUltrasonicExam.getTechnologyName(),list);
				setItemValueVO("身体部位",mpUltrasonicExam.getSctPosition(),list);
				setItemValueVO("检查方式",mpUltrasonicExam.getTestMethod(),list);
				setItemValueVO("检查报告结果--客观所见",mpUltrasonicExam.getObjectiveView(),list);
				setItemValueVO("检查报告结果--主观提示",mpUltrasonicExam.getSubjectiveHints(),list);
				setItemValueVO("检查诊断",mpUltrasonicExam.getRelevantClinicDiagnosis(),list);
				setItemValueVO("检查时间",DateUtils.dateTime(mpUltrasonicExam.getTechnologyDate()),list);
				setItemValueVO("报告时间",DateUtils.dateTime(mpUltrasonicExam.getReportDate()),list);
				setItemValueVO("开单科室",mpUltrasonicExam.getDepartmentName(),list);
				catalogVO.setItemList(list);
				result.add(catalogVO);
			}
			healthItemVO.setArray(result);
			return healthItemVO;
		}
		return null;
	}

	private HealthItemVO getElectrocardiogramExam(String personId, String visitId) {
		List<MpElectrocardiogramExam> mpElectrocardiogramExams = mpElectrocardiogramExamMapper.selectMpElectrocardiogramExamList(personId, visitId, null);
		if(!CollectionUtils.isEmpty(mpElectrocardiogramExams)) {
			HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
			healthItemVO.setItemId(HealthPatientCheckExamEnum.ECG.getCode());
			healthItemVO.setItemName(HealthPatientCheckExamEnum.ECG.getValue());
			List<CatalogVO> result = new ArrayList<>();
			for(MpElectrocardiogramExam mpElectrocardiogramExam : mpElectrocardiogramExams){
				CatalogVO catalogVO = new CatalogVO();
				catalogVO.setItemTitle(mpElectrocardiogramExam.getTechnologyName());
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				setItemValueVO("检查项目",mpElectrocardiogramExam.getTechnologyName(),list);
				setItemValueVO("身体部位",mpElectrocardiogramExam.getSctPosition(),list);
				setItemValueVO("检查方式",mpElectrocardiogramExam.getTestMethod(),list);
				setItemValueVO("检查报告结果--客观所见",mpElectrocardiogramExam.getObjectiveView(),list);
				setItemValueVO("检查报告结果--主观提示",mpElectrocardiogramExam.getSubjectiveHints(),list);
				setItemValueVO("检查诊断",mpElectrocardiogramExam.getRelevantClinicDiagnosis(),list);
				setItemValueVO("心电图报告",mpElectrocardiogramExam.getPdfReport(),list);
				setItemValueVO("检查时间",DateUtils.dateTime(mpElectrocardiogramExam.getTechnologyDate()),list);
				setItemValueVO("报告时间",DateUtils.dateTime(mpElectrocardiogramExam.getReportDate()),list);
				setItemValueVO("开单科室",mpElectrocardiogramExam.getDepartmentName(),list);
				catalogVO.setItemList(list);
				result.add(catalogVO);
			}
			healthItemVO.setArray(result);
			return healthItemVO;
		}
		return null;
	}

	/**
	 *  眼科检查表
	 * @param personId
	 * @param visitId
	 * @return
	 */
	private  HealthItemVO<List<ItemValueVO>>   getOphthalmologicalExam(String personId,String visitId) {
		MpOphthalmologicalExam   mpdb =  mpOphthalmologicalExamMapper.selectByPersonIdAndVisitId(personId, visitId);
		if(mpdb != null) {
			HealthItemVO< List<ItemValueVO>>  healthItemVO  = new HealthItemVO< List<ItemValueVO>>();
			healthItemVO.setItemId( HealthPatientCheckExamEnum.OPHTHALMOLOGICAL_EXAM.getCode());
			healthItemVO.setItemName(HealthPatientCheckExamEnum.OPHTHALMOLOGICAL_EXAM.getValue());
			List<ItemValueVO>  list = new ArrayList<ItemValueVO>();
			ItemValueVO itemValueVO1 = setItemValueVO("视力（左）",mpdb.getVisionLeft(), list);
			ItemValueVO itemValueVO2 = setItemValueVO("视力（右）",mpdb.getVisionRight(), list);
			ItemValueVO itemValueVO3 = setItemValueVO("矫正视力左",mpdb.getVisionLeftRevise(), list);
			ItemValueVO itemValueVO4 = setItemValueVO("矫正视力右",mpdb.getVisionRightRevise(), list);
			ItemValueVO itemValueVO5 = setItemValueVO("眼位",mpdb.getEyePosition(), list);
			ItemValueVO itemValueVO6 = setItemValueVO("眼球运动",mpdb.getEyeMovement(), list);
			ItemValueVO itemValueVO7 = setItemValueVO("眼睑",mpdb.getEyelid(), list);
			ItemValueVO itemValueVO8 = setItemValueVO("睑结膜",mpdb.getEyeConjunctiva(), list);
			ItemValueVO itemValueVO9 = setItemValueVO("球结膜 ",mpdb.getEyeballConjunctiva(), list);
			ItemValueVO itemValueVO10 = setItemValueVO("眼压检查 ",mpdb.getEyePressure(), list);
			ItemValueVO itemValueVO11 = setItemValueVO("眼底照相",mpdb.getEyePhoto(), list);
			ItemValueVO itemValueVO12 = setItemValueVO("色觉检查",mpdb.getVisionColour(), list);
			ItemValueVO itemValueVO13 = setItemValueVO("泪器",mpdb.getLacrimal(), list);

			String healthExamDate = null;
			if(mpdb.getHealthExamDate() != null) {
				healthExamDate =DateUtils.parseDateToStrShort(mpdb.getHealthExamDate());
			}
			ItemValueVO itemValueVO14 = setItemValueVO("体检日期",healthExamDate, list);
			String resultDate = null;
			if(mpdb.getResultDate() != null) {
				resultDate =DateUtils.parseDateToStrShort(mpdb.getResultDate());
			}
			ItemValueVO itemValueVO15 = setItemValueVO("结果日期",resultDate, list);
			healthItemVO.setObj(list);

			return healthItemVO;
		}
		return null;

	}
	/**
	 *  耳鼻喉检查
	 * @param personId
	 * @param visitId
	 * @return
	 */
	private  HealthItemVO<List<ItemValueVO>>   getOtolaryngology(String personId,String visitId) {
		MpOtolaryngologyExam   mpdb =  mpOtolaryngologyExamMapper.selectByPersonIdAndVisitId(personId, visitId);
		if(mpdb != null) {
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId( HealthPatientCheckExamEnum.OTOLARYNGOLOGY.getCode());
			healthItemVO.setItemName(HealthPatientCheckExamEnum.OTOLARYNGOLOGY.getValue());
			List<ItemValueVO>  list = new ArrayList<ItemValueVO>();
			ItemValueVO itemValueVO1 = setItemValueVO("听力",mpdb.getHearing(), list);
			ItemValueVO itemValueVO2 = setItemValueVO("耳廓",mpdb.getAuricle(), list);
			ItemValueVO itemValueVO3 = setItemValueVO("外耳道",mpdb.getEarExternalMeatus(), list);
			ItemValueVO itemValueVO4 = setItemValueVO("鼓膜",mpdb.getEarDrum(), list);
			ItemValueVO itemValueVO5 = setItemValueVO("外鼻",mpdb.getNoseExternal(), list);
			ItemValueVO itemValueVO6 = setItemValueVO("鼻腔",mpdb.getNoseCavity(), list);
			ItemValueVO itemValueVO7 = setItemValueVO("口咽",mpdb.getOropharynx(), list);
			ItemValueVO itemValueVO8 = setItemValueVO("喉咽 ",mpdb.getThroat(), list);
			ItemValueVO itemValueVO9 = setItemValueVO("扁桃体",mpdb.getTonsil(), list);

			String healthExamDate = null;
			if(mpdb.getHealthExamDate() != null) {
				healthExamDate =DateUtils.parseDateToStrShort(mpdb.getHealthExamDate());
			}
			ItemValueVO itemValueVO14 = setItemValueVO("体检日期",healthExamDate, list);
			String resultDate = null;
			if(mpdb.getResultDate() != null) {
				resultDate =DateUtils.parseDateToStrShort(mpdb.getResultDate());
			}
			ItemValueVO itemValueVO15 = setItemValueVO("结果日期",resultDate, list);
			healthItemVO.setObj(list);

			return healthItemVO;
		}
		return null;

	}

	/**
	 * 外科检查
	 * @param personId
	 * @param visitId
	 * @return
	 */
	private  HealthItemVO< List<ItemValueVO>>   getSurgicalExam(String personId,String visitId) {
		MpSurgicalExam   mpdb =  mpSurgicalExamMapper.selectByPersonIdAndVisitId(personId, visitId);
		if(mpdb != null) {
			HealthItemVO< List<ItemValueVO>>  healthItemVO  = new HealthItemVO< List<ItemValueVO>>();
			healthItemVO.setItemId( HealthPatientCheckExamEnum.SURGICAL_EXAM.getCode());
			healthItemVO.setItemName(HealthPatientCheckExamEnum.SURGICAL_EXAM.getValue());
			List<ItemValueVO> list = new ArrayList<ItemValueVO>();
			ItemValueVO itemValueVO1 = setItemValueVO("皮肤",mpdb.getSkin(), list);
			ItemValueVO itemValueVO2 = setItemValueVO("肛门",mpdb.getAnus(), list);;
			ItemValueVO itemValueVO3 = setItemValueVO("表浅淋巴结",mpdb.getSuperficialLynph(), list);
			ItemValueVO itemValueVO4 = setItemValueVO("甲状腺",mpdb.getThyroid(), list);
			ItemValueVO itemValueVO5 = setItemValueVO("脊柱",mpdb.getSpine(), list);
			ItemValueVO itemValueVO6 = setItemValueVO("四肢关节",mpdb.getLimbJoint(), list);

			String healthExamDate = null;
			if(mpdb.getHealthExamDate() != null) {
				healthExamDate =DateUtils.parseDateToStrShort(mpdb.getHealthExamDate());
			}
			ItemValueVO itemValueVO14 = setItemValueVO("体检日期",healthExamDate, list);
			String resultDate = null;
			if(mpdb.getResultDate() != null) {
				resultDate =DateUtils.parseDateToStrShort(mpdb.getResultDate());
			}
			ItemValueVO itemValueVO15 = setItemValueVO("结果日期",resultDate, list);
			healthItemVO.setObj(list);
			return healthItemVO;
		}
		return null;

	}

	/**
	 * 內科检查
	 * @param personId
	 * @param visitId
	 * @return
	 */
	private  HealthItemVO<?>   getInternalMedicineExam(String personId,String visitId) {
		MpInternalMedicineExam  mpInternalMedicineExam =  mpInternalMedicineExamMapper.selectByPersonIdAndVisitId(personId, visitId);
		if(mpInternalMedicineExam != null) {
			HealthItemVO< List<ItemValueVO> >  healthItemVO  = new HealthItemVO<>();
			healthItemVO.setItemId( HealthPatientCheckExamEnum.INTERNAL_MEDICINE_EXAM.getCode());
			healthItemVO.setItemName(HealthPatientCheckExamEnum.INTERNAL_MEDICINE_EXAM.getValue());
			List<ItemValueVO>  list = new ArrayList<ItemValueVO>();
			ItemValueVO itemValueVO1 = setItemValueVO("胸部外形",mpInternalMedicineExam.getChestShape(), list);
			ItemValueVO itemValueVO2 = setItemValueVO("胸部触诊",mpInternalMedicineExam.getChestPalpation(), list);
			ItemValueVO itemValueVO3 = setItemValueVO("胸部叩诊",mpInternalMedicineExam.getChestPercussion(), list);
			ItemValueVO itemValueVO4 = setItemValueVO("心界",mpInternalMedicineExam.getHeartBorder(), list);
			ItemValueVO itemValueVO5 = setItemValueVO("肺部听诊",mpInternalMedicineExam.getLungAuscultation(), list);
			ItemValueVO itemValueVO6 = setItemValueVO("心音",mpInternalMedicineExam.getHeartSound(), list);
			ItemValueVO itemValueVO7 = setItemValueVO("心律",mpInternalMedicineExam.getHeartRhythm(), list);
			ItemValueVO itemValueVO8 = setItemValueVO("心脏杂音",mpInternalMedicineExam.getHeartNoise(), list);
			ItemValueVO itemValueVO9 = setItemValueVO("肝脏触诊",mpInternalMedicineExam.getLiverPalpation(), list);
			ItemValueVO itemValueVO10 = setItemValueVO("脾脏触诊",mpInternalMedicineExam.getSpleenPalpation(), list);
			ItemValueVO itemValueVO11 = setItemValueVO("腹部外形",mpInternalMedicineExam.getAbdomShape(), list);
			ItemValueVO itemValueVO12 = setItemValueVO("腹部压痛 ",mpInternalMedicineExam.getAbdomPressurePain(), list);
			ItemValueVO itemValueVO13 = setItemValueVO("腹部叩诊",mpInternalMedicineExam.getAbdomPercussion(), list);
			/** 体检日期时间 */
			String healthExamDate = null;
			if(mpInternalMedicineExam.getHealthExamDate() != null) {
				healthExamDate =DateUtils.parseDateToStrShort(mpInternalMedicineExam.getHealthExamDate());
			}
			ItemValueVO itemValueVO14 = setItemValueVO("体检日期时",healthExamDate, list);
			String resultDate = null;
			if(mpInternalMedicineExam.getResultDate() != null) {
				resultDate =DateUtils.parseDateToStrShort(mpInternalMedicineExam.getResultDate());
			}
			ItemValueVO itemValueVO15 = setItemValueVO("结果日期",resultDate, list);
			healthItemVO.setObj(list);
			return healthItemVO;
		}
		return null;

	}


	/**
	 * 获取诊断信息
	 *
	 * @param personId
	 * @param visitId
	 * @return
	 */
	private HealthItemVO<List<CatalogVO>> getDiagnosis(String personId, String visitId, String visitType) {
		List<MpDiagnosis> disgnosisList = mpDiagnosisMapper.selectByPersonIdAndVisitId(personId, visitId, visitType);
		if (!CollectionUtils.isEmpty(disgnosisList)) {
			int i=1;
			List<CatalogVO>  result  = new ArrayList<CatalogVO>();
			HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
			healthItemVO.setItemId( HealthPatientCheckExamEnum.DIAGNOSI.getCode());
			healthItemVO.setItemName(HealthPatientCheckExamEnum.DIAGNOSI.getValue());
			for (MpDiagnosis mpDiagnosis : disgnosisList) {
				CatalogVO  catalogVO = new CatalogVO();
				List<ItemValueVO> list = new ArrayList<>();
				ItemValueVO itemValueVO1 = setItemValueVO("诊断类别",mpDiagnosis.getDiagnosisType(), list);
//				ItemValueVO itemValueVO2 = setItemValueVO("诊断序号",mpDiagnosis.getDiagnosisNo(), list);
				ItemValueVO itemValueVO3 = setItemValueVO("终检结论",mpDiagnosis.getDiagnosisDesc(), list);
				String diagnosisDate = null;
				if(mpDiagnosis.getDiagnosisDate() != null) {
					diagnosisDate =DateUtils.parseDateToStrShort(mpDiagnosis.getDiagnosisDate());
				}
//				String title = String.format("%02d",i );
				catalogVO.setItemTitle(diagnosisDate+" "+"诊断");
				i++;
				ItemValueVO itemValueVO4 = setItemValueVO("诊断日期",diagnosisDate, list);
				ItemValueVO itemValueVO5 = setItemValueVO("终检医生",mpDiagnosis.getFinalCheckDoctor(), list);
				ItemValueVO itemValueVO6 = setItemValueVO("检查结果",mpDiagnosis.getCheckDesc(), list);
				ItemValueVO itemValueVO7 = setItemValueVO("结论医生",mpDiagnosis.getCheckDoctor(), list);
				String checkDate = null;
				if(mpDiagnosis.getCheckDate() != null) {
					checkDate =DateUtils.parseDateToStrShort(mpDiagnosis.getCheckDate());
				}
				ItemValueVO itemValueVO8 = setItemValueVO("结论日期",checkDate, list);
				String resultDate = null;
				if(mpDiagnosis.getResultDate() != null) {
					resultDate =DateUtils.parseDateToStrShort(mpDiagnosis.getResultDate());
				}
				ItemValueVO itemValueVO10 = setItemValueVO("诊断日期",resultDate, list);
				ItemValueVO itemValueVO13 = setItemValueVO("诊断名称",mpDiagnosis.getConditionName(), list);
				ItemValueVO itemValueVO14 = setItemValueVO("诊断类别",mpDiagnosis.getConditionClass(), list);
				ItemValueVO itemValueVO15 = setItemValueVO("诊断科室名称",mpDiagnosis.getDepartmentName(), list);
				ItemValueVO itemValueVO16 = setItemValueVO("是否新发肿瘤",mpDiagnosis.getIsNewTumor(), list);
				ItemValueVO itemValueVO17 = setItemValueVO("是否实施过临床路径",mpDiagnosis.getIsClinicalPath(), list);
				ItemValueVO itemValueVO18 = setItemValueVO("肿瘤分期等级",mpDiagnosis.getTumourStageLevel(), list);
				ItemValueVO itemValueVO19 = setItemValueVO("肿瘤分期类型",mpDiagnosis.getTumourStageType(), list);
				ItemValueVO itemValueVO20 = setItemValueVO("t分期",mpDiagnosis.getTStages(), list);
				ItemValueVO itemValueVO21 = setItemValueVO("n分期",mpDiagnosis.getNStages(), list);
				ItemValueVO itemValueVO22 = setItemValueVO("m分期",mpDiagnosis.getMStages(), list);
				ItemValueVO itemValueVO23 = setItemValueVO("tnm综合分期",mpDiagnosis.getTnmStages(), list);
				ItemValueVO itemValueVO24 = setItemValueVO("是否手术病人",mpDiagnosis.getIsOperationPerson(), list);
				ItemValueVO itemValueVO25 = setItemValueVO("是否微创手术病例",mpDiagnosis.getIsOperationMini(), list);
				catalogVO.setItemList(list);
				result.add(catalogVO);
			}
			healthItemVO.setObj(result);
			return healthItemVO;
		}
		return null;
	}

	/**
	 * 一般检查
	 * @param personId
	 * @param visitId
	 * @return
	 */
	private  GeneralInspectionVO  getGeneralInspection(String personId,String visitId,MpPerson person,MpVisitOccurrence  mpVisitOccurrence ,String type) {
		GeneralInspectionVO generalInspectionVO = new GeneralInspectionVO();
		String idCard = person.getCardId();
		generalInspectionVO.setPersonId(person.getPersonId());
		generalInspectionVO.setVisitId(visitId);
		if(mpVisitOccurrence.getVisitStartDate()!=null) {
			generalInspectionVO.setCheckDate(DateUtils.parseDateToStrShort(mpVisitOccurrence.getVisitStartDate()));
		}
		generalInspectionVO.setAddress(person.getAddress());
		generalInspectionVO.setContactsName(person.getContactsName());
		generalInspectionVO.setContactsPhone(person.getContactsPhone());
		generalInspectionVO.setDegreeOfEducation(HealthConstant.DEGREE_OF_EDUCATION.get(person.getDegreeOfEducation()));
		generalInspectionVO.setMaritalStatus(HealthConstant.MARITAL_STATUS.get(person.getMaritalStatus()));
		generalInspectionVO.setPhoneNumber(person.getPhoneNumber());
		generalInspectionVO.setSex(HealthUtils.getSexName(person.getCardId()));
		generalInspectionVO.setPersonName(person.getPersonName());
		generalInspectionVO.setIdCard(person.getCardId());
		generalInspectionVO.setBirthDay(HealthUtils.getBirthFromIdCard(person.getCardId()));
		if(HealthConstant.TJ.equals(type)) {
			if(mpVisitOccurrence.getSummaryDate()!=null) {
				generalInspectionVO.setSummaryDate(DateUtils.parseDateToStrShort(mpVisitOccurrence.getSummaryDate()));
			}
			generalInspectionVO.setExamSummary(mpVisitOccurrence.getExamSummary());
			generalInspectionVO.setHealthGuide(mpVisitOccurrence.getHealthGuide());
		}
		MpGeneralInspection mpGeneralInspection  = mpGeneralInspectionMapper.selectByPersonIdAndVisitId(personId, visitId);

		if(mpGeneralInspection != null) {

			BeanUtils.copyProperties(mpGeneralInspection, generalInspectionVO);
			generalInspectionVO.setPersonName(person.getPersonName());
			if(mpGeneralInspection.getResultTime() != null) {
				generalInspectionVO.setResultDate(DateUtils.parseDateToStrShort(mpGeneralInspection.getResultTime()));
				generalInspectionVO.setCheckDate(DateUtils.parseDateToStrShort(mpGeneralInspection.getResultTime()));
				if(StringUtils.isNotBlankAndNotEmpty(idCard)) {
					String brithDay = HealthUtils.getBirthFromIdCard(idCard);
					int age =  HealthUtils.getAge(mpGeneralInspection.getResultTime(), brithDay);
					if(age >0 ||age <120) {
						generalInspectionVO.setAge(String.valueOf(age));
					}else {
						generalInspectionVO.setAge("-");
					}
				}
			}
			if(mpGeneralInspection.getResultTime() != null) {
				generalInspectionVO.setResultTime(DateUtils.parseDateToStrShort(mpGeneralInspection.getResultTime()));
			}
			generalInspectionVO.setVisitTypeName(mpGeneralInspection.getVisitType());
			if(!StringUtils.isNotBlankAndNotEmpty(generalInspectionVO.getAge())) {
				generalInspectionVO.setAge("-");
			}

			if(HealthConstant.TJ.equals(type)) {
				generalInspectionVO.setVision(mpGeneralInspection.getVision());
				generalInspectionVO.setWaist(mpGeneralInspection.getWaist());
			}
		}
		return generalInspectionVO;
	}


	/**
	 * 设置每个子项
	 *
	 * @param n
	 * @param v
	 * @param list
	 * @return
	 */
	/*private ItemValueVO  setItemValueVO(String n,String v) {
		ItemValueVO  itemValueVO = new ItemValueVO();
		itemValueVO.setName(n);
		itemValueVO.setValue(v);
		return itemValueVO;
	}*/
	private ItemValueVO  setItemValueVO(String n, String v, List<ItemValueVO> list) {
		if(StringUtils.isEmpty(v)){
			return null;
		}
		ItemValueVO  itemValueVO = new ItemValueVO();
		itemValueVO.setName(n);
		itemValueVO.setValue(v);
		list.add(itemValueVO);
		return itemValueVO;
	}

	/**
	 * 获取每个子项的内容
	 * @param name
	 * @param key
	 * @param v
	 * @param minValueMap
	 * @param maxValueMap
	 * @param unitMap
	 * @return
	 */
    /*private ItemValueVO  setItemValueVO(String name,String v,String key, Map<String, String> minValueMap, Map<String, String> maxValueMap, Map<String, String> unitMap) {
		 ItemValueVO  itemValueVO = new ItemValueVO();
		 itemValueVO.setName(name);
		 itemValueVO.setValue(v);
		 itemValueVO.setMaxValue(maxValueMap.get(key));
		 itemValueVO.setMinValue(minValueMap.get(key));
		 itemValueVO.setUnit(unitMap.get(key));
		 return itemValueVO;

	 }*/

	private ItemValueVO setItemValueVO(String name, String v, String key, Map<String, List<MpUnitDict>> unitDictMap, List<ItemValueVO> list) {
		if(StringUtils.isEmpty(v)){
			return null;
		}
		ItemValueVO  itemValueVO = new ItemValueVO();
		itemValueVO.setName(name);
		itemValueVO.setValue(v);

		String sex = ThreadLocalUtils.get("sex");
		String ageType = ThreadLocalUtils.get("ageType");

		List<MpUnitDict> mpUnitDicts = unitDictMap.get(key);
		MpUnitDict mpUnitDict;

		if(mpUnitDicts.size() == 1 || StringUtils.isEmpty(sex)){ // 不分男女，也不分年龄
			mpUnitDict = mpUnitDicts.get(0);
		}else {
			Map<String, MpUnitDict> collect = mpUnitDicts.stream().collect(Collectors.toMap(MpUnitDict::getType, Function.identity()));
			// 先取年龄，再取性别，取不到取默认第一条
			MpUnitDict ageUnitDict = collect.get(ageType);
			MpUnitDict sexUnitDict = collect.get(sex);
			if(ageUnitDict != null){
				mpUnitDict = ageUnitDict;
			} else if (sexUnitDict != null) {
				mpUnitDict = sexUnitDict;
			}else {
				mpUnitDict = mpUnitDicts.get(0);
			}
		}

		itemValueVO.setMaxValue(mpUnitDict.getMaxValue());
		itemValueVO.setMinValue(mpUnitDict.getMinValue());
		itemValueVO.setUnit(mpUnitDict.getValue());
		list.add(itemValueVO);
		return itemValueVO;
	}

	/**
	 * 获取检验时候的年龄
	 *
	 * @param cardId 身份证号
	 * @param resultTime 报告时间
	 */
	private static void threadLocalSetAge(String cardId, Date resultTime) {
		try {
			if (resultTime != null && cardId.length() == 18) {
				String year = cardId.substring(6).substring(0, 4);// 得到年份
				String month = cardId.substring(10).substring(0, 2);// 得到月份
				String day = cardId.substring(12).substring(0, 2);// 得到日
				// 获取检验时候的年龄

				// 将Date对象转换为LocalDateTime
				LocalDateTime localDateTime = resultTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

				// 提取LocalDate部分
				LocalDate resultDate = localDateTime.toLocalDate();

				LocalDate birthDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day)); // 假设出生日期为1990年1月1日

				int age = Period.between(birthDate, resultDate).getYears(); // 计算年龄
				int monthAge = Period.between(birthDate, resultDate.withDayOfMonth(1).plusMonths(1)).getMonths(); // 计算月龄
				int dayAge = Period.between(birthDate, resultDate.withDayOfMonth(1)).getDays(); // 计算天数

				String ageType = "";
				// 判断是 新生儿（0-28天）、婴儿（0-1岁）、幼儿（1-3岁）、儿童（小于14岁），成人
				if(age == 0 && monthAge == 1 && dayAge <= 28){
					ageType = "新生儿";
				}else if(age == 1){
					ageType = "婴儿";
				}else if(age > 1 && age <=3 ){
					ageType = "幼儿";
				}else if(age <= 14){
					ageType = "儿童";
				}else {
					ageType = "成人";
				}
				ThreadLocalUtils.put("ageType",ageType);
			}
		} catch (Exception e) {
			log.error("threadLocalSetAge error is {}",e.getMessage());
		}
	}


	public DchealthCloudResponseEntity<List<HealthItemVO>> mzTestItemList(DetailForm detail) {
		DchealthCloudResponseEntity<List<HealthItemVO>> result = new DchealthCloudResponseEntity<List<HealthItemVO>>();

		MpPerson person = mpPersonMapper.getPersonByPersonId(detail.getPersonId());
		if(person == null){
			return result;
		}

		List<HealthItemVO> data = new ArrayList<HealthItemVO>();
		List<MpUnitDict> totalUnitDicts = mpUnitDictMapper.queryUnitDictByTableName(null);

		AtomicInteger itemId = new AtomicInteger(1);

		// 1.住院 2.门诊 4.预祝院
		String visitType = "2";
		if ("4".equals(detail.getType())) {
			visitType = "4";
		}
		//
		getCommonCol(person,data,itemId, visitType, TableEnum.MP_BLOOD_ROUTINE_COL.getTableName());
//		getBloodRoutine(person, totalUnitDicts, data, itemId);
		//血常规
//		getBloodRoutine(person, totalUnitDicts, data, itemId);

		//尿常规
		getCommonCol(person,data,itemId, visitType, TableEnum.MP_URINE_ROUTINE_COL.getTableName());
//		getUrineRoutine(person, totalUnitDicts, data, itemId);

		//便常规
//		getStoolRoutine(person, data, itemId);
		getCommonCol(person,data,itemId, visitType, TableEnum.MP_STOOL_ROUTINE_COL.getTableName());

		//常见传染病 乙肝 mp_infectious_diseases
//		getInfectiousDiseases(person, data, itemId);
		getCommonCol(person,data,itemId, visitType, TableEnum.MP_THYROID_FUNCTION_COL.getTableName());

		//凝血系列检查   mp_coagulation_function
//		getCoagulationFunction(person, totalUnitDicts, data, itemId);
		getCommonCol(person,data,itemId, visitType, TableEnum.MP_COAGULATION_FUNCTION_COL2.getTableName());

		//血液生化 mp_blood_biochemistry
//		getBloodBiochemistry(person, totalUnitDicts, data, itemId);

		//甲状腺功能
//		getThyroidFunction(person, totalUnitDicts, data, itemId);

		//激素六项 mp_hormone_items
//		getHormoneItems(person, totalUnitDicts, data, itemId);

		//肿瘤标志物
//		getTumorScreeningItems(person, totalUnitDicts, data, itemId);
		getCommonCol(person,data,itemId, visitType, TableEnum.MP_TUMOR_SCREENING_ITEMS_COL.getTableName());

		//血气分析 mp_blood_biochemistry
//		getBloodGasAnalysis(person, totalUnitDicts, data, itemId);
		getCommonCol(person,data,itemId, visitType, TableEnum.MP_BLOOD_BIOCHEMISTRY_COL.getTableName());
		// 糖耐量试验
//		getGlucoseTolerance(person, totalUnitDicts, data, itemId);
		getCommonCol(person,data,itemId, visitType, TableEnum.MP_GLUCOSE_TOLERANCE_COL.getTableName());

		result.setData(data);
		return result;
	}

	private void getCommonCol(MpPerson person, List<HealthItemVO> data, AtomicInteger itemId, String visitType,String tableName) {
		List<HealthItemVO> localList = new ArrayList<>();
		String personId = person.getPersonId();
		MpCommonCol mpBloodRoutineCol = new MpCommonCol();
		if (StringUtils.isEmpty(personId)) {
			return;
		}
		mpBloodRoutineCol.setPersonId(personId);
		mpBloodRoutineCol.setVisitType(visitType);
		mpBloodRoutineCol.setTableName(tableName);
		// 1. 查出肝or肾
		List<MpCommonCol> mpBloodRoutineCols = mpCommonColMapper.selectMpCommonColList(mpBloodRoutineCol);
		if (CollectionUtils.isEmpty(mpBloodRoutineCols)) {
			log.info("personid={},查询血常规为空",personId);
			return;
		}
		Map<String, List<MpCommonCol>> visitRecords = mpBloodRoutineCols.stream()
				.filter(obj->!StringUtils.isEmpty(DateUtils.dateTime(obj.getResultTime())))
				.collect(Collectors.groupingBy(key -> DateUtils.parseDateToStrFull(key.getResultTime())));
		visitRecords.forEach((k,v)->{

			Map<String, List<MpCommonCol>> collect = v.stream().collect(Collectors.groupingBy(MpCommonCol::getMeasurementType));
			collect.forEach((measurementType,ganOrShenObjs)->{
				// 一个检验项
				HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
				healthItemVO.setItemId(itemId.get());
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				for (MpCommonCol urineRoutineCol : ganOrShenObjs) {
					//todo title 放出来
					healthItemVO.setItemName(DateUtils.dateTime(urineRoutineCol.getResultTime()) +" "+urineRoutineCol.getMeasurementType());
					ItemValueVO itemValueVO = new ItemValueVO();
					itemValueVO.setValue(urineRoutineCol.getResultValue());
					if (!StringUtils.isEmpty(urineRoutineCol.getMaxValue())
							&& urineRoutineCol.getMaxValue().length()>1
							&& urineRoutineCol.getMaxValue().startsWith("-")
					) {
						itemValueVO.setMaxValue(urineRoutineCol.getMaxValue().replace("-",""));
					}else{
						itemValueVO.setMaxValue(urineRoutineCol.getMaxValue());
					}
					itemValueVO.setMinValue(urineRoutineCol.getMinValue());
					itemValueVO.setUnit(urineRoutineCol.getUnit());
					itemValueVO.setName(urineRoutineCol.getMeasurementSourceValue());
					itemValueVO.setTip(urineRoutineCol.getTip());
					itemValueVO.setRangeValue(urineRoutineCol.getRangeValue());
					list.add(itemValueVO);
				}
				healthItemVO.setObj(list);
				localList.add(healthItemVO);
			});
		});
		Map<String, List<HealthItemVO>> collect2 = localList.stream()
				.collect(Collectors.groupingBy(healthItemVO -> healthItemVO.getItemName().split(" ")[1]));
		collect2.forEach((k,v)->{
			data.addAll(reverseSortedHealthItemVOS(v));
		});
	}

	/**
	 * 按日期倒序排序
	 * @param healthItem
	 */
	private List<HealthItemVO>  reverseSortedHealthItemVOS(List<HealthItemVO> healthItem) {
		List<HealthItemVO> healthItemCopy = healthItem.stream().sorted(Comparator.comparing(HealthItemVO::getItemName)).collect(Collectors.toList());
		Collections.reverse(healthItemCopy);
		return healthItemCopy;
	}
	private void getGlucoseTolerance(MpPerson person, List<MpUnitDict> totalUnitDicts, List<HealthItemVO> data, AtomicInteger itemId) {
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		List<MpGlucoseTolerance> mpdbs = mpGlucoseToleranceMapper.selectByPersonId(personId);
		List<String> resultTimes = mpdbs.stream().map(v -> DateUtils.dateTime(v.getResultTime())).collect(Collectors.toList());

		Map<String, MpGlucoseTolerance> mpdbsMap = mpdbs.stream().collect(Collectors.toMap(key->DateUtils.dateTime(key.getResultTime()), Function.identity()));
		for(String resultTime: resultTimes) {
			MpGlucoseTolerance mpdb = mpdbsMap.get(resultTime);
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId(itemId.get());
			healthItemVO.setItemName(resultTime +" "+HealthPatientCheckTestEnum.GLUCOSE_TOLERANCE.getValue());

			List<MpUnitDict> mpUnitDicts = totalUnitDicts.stream().filter(mpUnitDict ->
					mpUnitDict.getTableName().equals(HealthRecordCheckConstants.TAB_MP_GLUCOSE_TOLERANCE)).collect(Collectors.toList());
			Map<String, List<MpUnitDict>> collect = mpUnitDicts.stream().collect(Collectors.groupingBy(MpUnitDict::getCode));

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();

			ThreadLocalUtils.put("sex",sex);

			// 获取检验时候的年龄
			threadLocalSetAge(cardId, mpdb.getResultTime());

			ItemValueVO itemValueVO1 = setItemValueVO("孕周", mpdb.getGestationalWeeks(), HealthRecordCheckConstants.ATTR_GESTATIONAL_WEEKS, collect, list);
			ItemValueVO itemValueVO2 = setItemValueVO("身高", mpdb.getHeight(), HealthRecordCheckConstants.ATTR_HEIGHT, collect, list);
			ItemValueVO itemValueVO3 = setItemValueVO("体重", mpdb.getWeight(), HealthRecordCheckConstants.ATTR_WEIGHT, collect, list);
			ItemValueVO itemValueVO4 = setItemValueVO("血糖 0分", mpdb.getEatAfter(), HealthRecordCheckConstants.ATTR_EAT_AFTER, collect, list);
			ItemValueVO itemValueVO5 = setItemValueVO("血糖 30分", mpdb.getEatAfter30min(), HealthRecordCheckConstants.ATTR_EAT_AFTER_30MIN, collect, list);
			ItemValueVO itemValueVO6 = setItemValueVO("血糖 60分", mpdb.getEatAfter1h(), HealthRecordCheckConstants.ATTR_EAT_AFTER_1H, collect, list);
			ItemValueVO itemValueVO7 = setItemValueVO("血糖 120分", mpdb.getEatAfter2h(), HealthRecordCheckConstants.ATTR_EAT_AFTER_2H, collect, list);
			ItemValueVO itemValueVO8 = setItemValueVO("血糖 180分", mpdb.getEatAfter3h(), HealthRecordCheckConstants.ATTR_EAT_AFTER_3H, collect, list);
			ItemValueVO itemValueVO9 = setItemValueVO("血糖 240分", mpdb.getEatAfter4h(), HealthRecordCheckConstants.ATTR_EAT_AFTER_4H, collect, list);
			ItemValueVO itemValueVO10 = setItemValueVO("血糖 300分", mpdb.getEatAfter5h(), HealthRecordCheckConstants.ATTR_EAT_AFTER_5H, collect, list);
			healthItemVO.setObj(list);
			data.add(healthItemVO);

			ThreadLocalUtils.release();
			itemId.incrementAndGet();
		}
	}

	private void getBloodGasAnalysis(MpPerson person, List<MpUnitDict> totalUnitDicts, List<HealthItemVO> data, AtomicInteger itemId) {
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		List<MpBloodGasAnalysis> mpdbs = mpBloodGasAnalysisMapper.selectByPersonId(personId);
		List<String> resultTimes = mpdbs.stream().map(v -> DateUtils.dateTime(v.getResultTime())).collect(Collectors.toList());

		Map<String, MpBloodGasAnalysis> mpdbsMap = mpdbs.stream().collect(Collectors.toMap(key->DateUtils.dateTime(key.getResultTime()), Function.identity()));
		for(String resultTime: resultTimes) {
			MpBloodGasAnalysis mpdb = mpdbsMap.get(resultTime);
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId(itemId.get());
			healthItemVO.setItemName(resultTime +" "+HealthPatientCheckTestEnum.BLOOD_GAS_ANALYSIS.getValue());

			List<MpUnitDict> mpUnitDicts = totalUnitDicts.stream().filter(mpUnitDict ->
					mpUnitDict.getTableName().equals(HealthRecordCheckConstants.TAB_MP_BLOOD_GAS_ANALYSIS)).collect(Collectors.toList());
			Map<String, List<MpUnitDict>> collect = mpUnitDicts.stream().collect(Collectors.groupingBy(MpUnitDict::getCode));

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();

			ThreadLocalUtils.put("sex",sex);

			// 获取检验时候的年龄
			threadLocalSetAge(cardId, mpdb.getResultTime());

			ItemValueVO itemValueVO1 = setItemValueVO("ph值",mpdb.getBloodPh(), HealthRecordCheckConstants.ATTR_BLOOD_PH, collect, list);
			ItemValueVO itemValueVO2 = setItemValueVO("动脉血二氧化碳分压",mpdb.getBloodPco2(), HealthRecordCheckConstants.ATTR_BLOOD_PCO2, collect, list);
			ItemValueVO itemValueVO3 = setItemValueVO("动脉血氧分压",mpdb.getBloodPo2(), HealthRecordCheckConstants.ATTR_BLOOD_PO2, collect, list);
			ItemValueVO itemValueVO4 = setItemValueVO("红细胞压积",mpdb.getBloodHct(), HealthRecordCheckConstants.ATTR_BLOOD_HCT, collect, list);
			ItemValueVO itemValueVO5 = setItemValueVO("血红蛋白",mpdb.getBloodHb(), HealthRecordCheckConstants.ATTR_BLOOD_HB, collect, list);
			ItemValueVO itemValueVO6 = setItemValueVO("剩余碱",mpdb.getBloodBe(), HealthRecordCheckConstants.ATTR_BLOOD_BE, collect, list);
			ItemValueVO itemValueVO7 = setItemValueVO("标准碳酸根",mpdb.getBloodSb(), HealthRecordCheckConstants.ATTR_BLOOD_SB, collect, list);
			ItemValueVO itemValueVO8 = setItemValueVO("实际碳酸根",mpdb.getBloodAb(), HealthRecordCheckConstants.ATTR_BLOOD_AB, collect, list);
			ItemValueVO itemValueVO9 = setItemValueVO("总二氧化碳总量",mpdb.getBloodTco2(), HealthRecordCheckConstants.ATTR_BLOOD_TCO2, collect, list);
			ItemValueVO itemValueVO10 = setItemValueVO("氧饱和度50%时氧分压",mpdb.getBloodPo2P50(), HealthRecordCheckConstants.ATTR_BLOOD_PO2_P50, collect, list);
			ItemValueVO itemValueVO11 = setItemValueVO("血氧饱和度",mpdb.getBloodSato2(), HealthRecordCheckConstants.ATTR_BLOOD_SATO2, collect, list);
			ItemValueVO itemValueVO12 = setItemValueVO("血氧含量",mpdb.getBloodTo2(), HealthRecordCheckConstants.ATTR_BLOOD_TO2, collect, list);
			ItemValueVO itemValueVO13 = setItemValueVO("氧容量",mpdb.getBloodO2Cap(), HealthRecordCheckConstants.ATTR_BLOOD_O2_CAP, collect, list);
			healthItemVO.setObj(list);
			data.add(healthItemVO);

			ThreadLocalUtils.release();
			itemId.incrementAndGet();
		}
	}

	private void getTumorScreeningItems(MpPerson person, List<MpUnitDict> totalUnitDicts, List<HealthItemVO> data, AtomicInteger itemId) {
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		List<MpTumorScreeningItems> mpdbs = mpTumorScreeningItemsMapper.selectByPersonId(personId);
		List<String> resultTimes = mpdbs.stream().map(v -> DateUtils.dateTime(v.getResultTime())).collect(Collectors.toList());

		Map<String, MpTumorScreeningItems> mpdbsMap = mpdbs.stream().collect(Collectors.toMap(key->DateUtils.dateTime(key.getResultTime()), Function.identity()));
		for(String resultTime: resultTimes) {
			MpTumorScreeningItems mpdb = mpdbsMap.get(resultTime);
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId(itemId.get());
			healthItemVO.setItemName(resultTime +" "+HealthPatientCheckTestEnum.TUMOR_SCREENING_MEAL.getValue());

			List<MpUnitDict> mpUnitDicts = totalUnitDicts.stream().filter(mpUnitDict ->
					mpUnitDict.getTableName().equals(HealthRecordCheckConstants.TAB_MP_TUMOR_SCREENING_ITEMS)).collect(Collectors.toList());
			Map<String, List<MpUnitDict>> collect = mpUnitDicts.stream().collect(Collectors.groupingBy(MpUnitDict::getCode));

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();

			ThreadLocalUtils.put("sex",sex);

			// 获取检验时候的年龄
			threadLocalSetAge(cardId, mpdb.getResultTime());

			ItemValueVO itemValueVO1 = setItemValueVO("甲胎蛋白(afp) ",mpdb.getTumorAfp(), HealthRecordCheckConstants.ATTR_ALPHA_FETOPROTEIN, collect, list);
			ItemValueVO itemValueVO2 = setItemValueVO("癌胚抗原(cea)",mpdb.getTumorCea(), HealthRecordCheckConstants.ATTR_CARCINOEMBRYONIC_ANTIGEN, collect, list);
			ItemValueVO itemValueVO3 = setItemValueVO("癌抗原199(ca199) ",mpdb.getTumorCa199(), HealthRecordCheckConstants.ATTR_CARBOHYDRATE_ANTIGEN199, collect, list);
			ItemValueVO itemValueVO4 = setItemValueVO("癌抗原125(ca125)",mpdb.getTumorCa125(), HealthRecordCheckConstants.ATTR_CARBOHYDRATE_ANTIGEN125, collect, list);
			ItemValueVO itemValueVO5 = setItemValueVO("癌抗原242",mpdb.getTumorCa242(), HealthRecordCheckConstants.ATTR_CARBOHYDRATE_ANTIGEN242, collect, list);
			ItemValueVO itemValueVO6 = setItemValueVO("癌抗原153",mpdb.getTumorCa153(), HealthRecordCheckConstants.ATTR_CARBOHYDRATE_ANTIGEN153, collect, list);
			ItemValueVO itemValueVO7 = setItemValueVO("神经元特异性烯醇化酶nse",mpdb.getTumorNse(), HealthRecordCheckConstants.ATTR_NEURON_SPECIFIC_ENOLASE, collect, list);
			ItemValueVO itemValueVO8 = setItemValueVO("癌抗原72-4",mpdb.getTumorCa724(), HealthRecordCheckConstants.ATTR_CARBOHYDRATE_ANTIGEN724, collect, list);
			ItemValueVO itemValueVO9 = setItemValueVO("细胞角蛋白19片段(cyfra21-2)",mpdb.getTumorCyfra21(), HealthRecordCheckConstants.ATTR_CYTOKERATIN19_FRAGMENT, collect, list);
			ItemValueVO itemValueVO10 = setItemValueVO("癌抗原50",mpdb.getTumorCa50(), HealthRecordCheckConstants.ATTR_CARBOHYDRATE_ANTIGEN50, collect, list);
			ItemValueVO itemValueVO11 = setItemValueVO("游离前列腺特异性抗原",mpdb.getTumorFpsa(), HealthRecordCheckConstants.ATTR_FREE_PSM_ANTIGEN, collect, list);
			ItemValueVO itemValueVO12 = setItemValueVO("前列腺特异性抗原",mpdb.getTumorTpsa(), HealthRecordCheckConstants.ATTR_PSM_ANTIGEN, collect, list);
			ItemValueVO itemValueVO13 = setItemValueVO("人附睾蛋白4",mpdb.getTumorHe4(), HealthRecordCheckConstants.ATTR_HUMAN_PIDIDYMIS_ROTEIN, collect, list);
			ItemValueVO itemValueVO14 = setItemValueVO("鳞状细胞癌抗原（scc）",mpdb.getTumorScc(), HealthRecordCheckConstants.ATTR_SCC, collect, list);
			healthItemVO.setObj(list);
			data.add(healthItemVO);

			ThreadLocalUtils.release();
			itemId.incrementAndGet();
		}
	}

	private void getHormoneItems(MpPerson person, List<MpUnitDict> totalUnitDicts, List<HealthItemVO> data, AtomicInteger itemId) {
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		List<MpHormoneItems> mpdbs= mpHormoneItemsMapper.selectByPersonId(personId);
		List<String> resultTimes = mpdbs.stream().map(v -> DateUtils.dateTime(v.getResultTime())).collect(Collectors.toList());

		Map<String, MpHormoneItems> mpdbsMap = mpdbs.stream().collect(Collectors.toMap(key->DateUtils.dateTime(key.getResultTime()), Function.identity()));
		for(String resultTime: resultTimes) {
			MpHormoneItems mpdb = mpdbsMap.get(resultTime);
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId(itemId.get());
			healthItemVO.setItemName(resultTime +" "+HealthPatientCheckTestEnum.HORMONE_ITEMS.getValue());

			List<MpUnitDict> mpUnitDicts = totalUnitDicts.stream().filter(mpUnitDict ->
					mpUnitDict.getTableName().equals(HealthRecordCheckConstants.TAB_MP_HORMONE_ITEMS)).collect(Collectors.toList());
			Map<String, List<MpUnitDict>> collect = mpUnitDicts.stream().collect(Collectors.groupingBy(MpUnitDict::getCode));

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();

			ThreadLocalUtils.put("sex",sex);

			// 获取检验时候的年龄
			threadLocalSetAge(cardId, mpdb.getResultTime());

			ItemValueVO itemValueVO1= setItemValueVO("卵泡刺激素(fsh)", mpdb.getHormoneFsh(), HealthRecordCheckConstants.ATTR_FOLLICLE_STIMULATING_HORMONE, collect, list);
			ItemValueVO itemValueVO2= setItemValueVO("黄体生成素（LH）", mpdb.getHormoneLh(), HealthRecordCheckConstants.ATTR_LUTEINIZING_HORMONE, collect, list);
			ItemValueVO itemValueVO3= setItemValueVO("雌二醇（e2）", mpdb.getHormoneE2(), HealthRecordCheckConstants.ATTR_ESTRADIOL, collect, list);
			ItemValueVO itemValueVO4= setItemValueVO("孕酮（p）", mpdb.getHormoneP(), HealthRecordCheckConstants.ATTR_PROGESTERONE, collect, list);
			ItemValueVO itemValueVO5= setItemValueVO("睾酮（t）", mpdb.getHormoneT(), HealthRecordCheckConstants.ATTR_TESTOSTERONE, collect, list);
			ItemValueVO itemValueVO6= setItemValueVO("催乳素（prl）", mpdb.getHormonePrl(), HealthRecordCheckConstants.ATTR_PROLACTIN, collect, list);
			healthItemVO.setObj(list);
			data.add(healthItemVO);

			ThreadLocalUtils.release();
			itemId.incrementAndGet();
		}
	}

	private void getThyroidFunction(MpPerson person, List<MpUnitDict> totalUnitDicts, List<HealthItemVO> data, AtomicInteger itemId) {
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		List<MpThyroidFunction> mpdbs = mpThyroidFunctionMapper.selectByPersonId(personId);
		List<String> resultTimes = mpdbs.stream().map(v -> DateUtils.dateTime(v.getResultTime())).collect(Collectors.toList());

		Map<String, MpThyroidFunction> mpdbsMap = mpdbs.stream().collect(Collectors.toMap(key->DateUtils.dateTime(key.getResultTime()), Function.identity()));
		for(String resultTime: resultTimes) {
			MpThyroidFunction mpdb = mpdbsMap.get(resultTime);
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId(itemId.get());
			healthItemVO.setItemName(resultTime +" "+HealthPatientCheckTestEnum.THYROID_FUNCTION.getValue());

			List<MpUnitDict> mpUnitDicts = totalUnitDicts.stream().filter(mpUnitDict ->
					mpUnitDict.getTableName().equals(HealthRecordCheckConstants.TAB_MP_THYROID_FUNCTION_ITEMS)).collect(Collectors.toList());
			Map<String, List<MpUnitDict>> collect = mpUnitDicts.stream().collect(Collectors.groupingBy(MpUnitDict::getCode));

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();

			ThreadLocalUtils.put("sex",sex);

			// 获取检验时候的年龄
			threadLocalSetAge(cardId, mpdb.getResultTime());

			ItemValueVO itemValueVO1 = setItemValueVO("三碘甲状腺原氨酸(t3) ",mpdb.getThyroidT3(), HealthRecordCheckConstants.ATTR_TRIIODOTHYRONINE, collect, list);
			ItemValueVO itemValueVO2 = setItemValueVO("甲状腺素（t4）",mpdb.getThyroidT4(), HealthRecordCheckConstants.ATTR_THYROXINE, collect, list);
			ItemValueVO itemValueVO3 = setItemValueVO("促甲状腺激素(tsh)",mpdb.getThyroidTsh(), HealthRecordCheckConstants.ATTR_THYROID_STIMULATING_HORMONE, collect, list);
			ItemValueVO itemValueVO4 = setItemValueVO("游离三碘甲状腺原氨酸(t3)",mpdb.getThyroidFt3(), HealthRecordCheckConstants.ATTR_FREE_TRIIODOTHYRONINE, collect, list);
			ItemValueVO itemValueVO5 = setItemValueVO("游离甲状腺素（ft4）",mpdb.getThyroidFt4(), HealthRecordCheckConstants.ATTR_FREE_THYROXINE, collect, list);
			ItemValueVO itemValueVO6 = setItemValueVO("抗甲状腺球蛋白抗体",mpdb.getThyroidTgab(), list);
			ItemValueVO itemValueVO7 = setItemValueVO("抗甲状腺过氧化物酶抗体",mpdb.getThyroidTpoAb(), list);
			ItemValueVO itemValueVO8 = setItemValueVO("促甲状腺素受体抗体",mpdb.getThyroidTrAb(), list);
			ItemValueVO itemValueVO9 = setItemValueVO("抗甲状腺微粒体抗体)",mpdb.getThyroidTmAb(), list);
			ItemValueVO itemValueVO10 = setItemValueVO("甲状腺球蛋白",mpdb.getThyroidGlobulin(), HealthRecordCheckConstants.ATTR_THYROIDGLOBULIN, collect, list);
			healthItemVO.setObj(list);
			data.add(healthItemVO);

			ThreadLocalUtils.release();
			itemId.incrementAndGet();
		}
	}

	private void getBloodBiochemistry(MpPerson person, List<MpUnitDict> totalUnitDicts, List<HealthItemVO> data, AtomicInteger itemId) {
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		List<MpBloodBiochemistry> mpdbs = mpBloodBiochemistryMapper.selectByPersonId(personId);
		List<String> resultTimes = mpdbs.stream().map(v -> DateUtils.dateTime(v.getResultTime())).collect(Collectors.toList());

		Map<String, MpBloodBiochemistry> mpdbsMap = mpdbs.stream().collect(Collectors.toMap(key->DateUtils.dateTime(key.getResultTime()), Function.identity()));
		for(String resultTime: resultTimes) {
			MpBloodBiochemistry mpdb = mpdbsMap.get(resultTime);
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId(itemId.get());
			healthItemVO.setItemName(resultTime +" "+HealthPatientCheckTestEnum.BLOOD_BIOCHEMISTRY.getValue());

			List<MpUnitDict> mpUnitDicts = totalUnitDicts.stream().filter(mpUnitDict ->
					mpUnitDict.getTableName().equals(HealthRecordCheckConstants.TAB_MP_BLOOD_BIOCHEMISTRY)).collect(Collectors.toList());
			Map<String, List<MpUnitDict>> collect = mpUnitDicts.stream().collect(Collectors.groupingBy(MpUnitDict::getCode));

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();

			ThreadLocalUtils.put("sex",sex);

			// 获取检验时候的年龄
			threadLocalSetAge(cardId, mpdb.getResultTime());

			ItemValueVO itemValueVO1 = setItemValueVO("总蛋白",mpdb.getBloodTp(), HealthRecordCheckConstants.ATTR_TOTAL_PROTEIN, collect, list);
			ItemValueVO itemValueVO2 = setItemValueVO("白蛋白",mpdb.getBloodAlb(), HealthRecordCheckConstants.ATTR_ALBUMIN2, collect, list);
			ItemValueVO itemValueVO3 = setItemValueVO("球蛋白",mpdb.getBloodGlubolin(), HealthRecordCheckConstants.ATTR_GLOBULIN, collect, list);
			ItemValueVO itemValueVO4 = setItemValueVO("白蛋白/球蛋白",mpdb.getBloodAToG(), HealthRecordCheckConstants.ATTR_ALBUMIN_GLOBULIN_RATIO, collect, list);
			ItemValueVO itemValueVO5 = setItemValueVO("天门冬氨酸氨基转移酶",mpdb.getBloodAst(), HealthRecordCheckConstants.ATTR_ASPARTATE_AMINOTRANSFERASE, collect, list);
			ItemValueVO itemValueVO6 = setItemValueVO("丙氨酸氨基转移酶",mpdb.getBloodAlt(), HealthRecordCheckConstants.ATTR_ALANINE_AMINOTRANSFERASE, collect, list);
			ItemValueVO itemValueVO7 = setItemValueVO("ast/alt比值",mpdb.getAstToAlt(), HealthRecordCheckConstants.ATTR_AST_ALT_RATIO, collect, list);
			ItemValueVO itemValueVO8 = setItemValueVO("总胆汁酸",mpdb.getBloodTba(), HealthRecordCheckConstants.ATTR_TOTAL_BILE_ACID, collect, list);
			ItemValueVO itemValueVO9 = setItemValueVO("总胆红素",mpdb.getBloodTbil(), HealthRecordCheckConstants.ATTR_TOTAL_BILIRUBIN, collect, list);
			ItemValueVO itemValueVO10 = setItemValueVO("直接胆红素",mpdb.getBloodDbil(), HealthRecordCheckConstants.ATTR_DIRECT_BILIRUBIN, collect, list);
			ItemValueVO itemValueVO11 = setItemValueVO("间接胆红素",mpdb.getBloodIbil(), HealthRecordCheckConstants.ATTR_INDIRECT_BILIRUBIN, collect, list);
			ItemValueVO itemValueVO12 = setItemValueVO("碱性磷酸酶",mpdb.getBloodAlp(), HealthRecordCheckConstants.ATTR_ALKALINE_PHOSPHATASE, collect, list);
			ItemValueVO itemValueVO13 = setItemValueVO("ꝩ-谷氨酰转移酶",mpdb.getBloodYGt(), HealthRecordCheckConstants.ATTR_GLUTAMIC_PYRUVIC_TRANSAMINASE, collect, list);
			ItemValueVO itemValueVO14 = setItemValueVO("胆碱酯酶",mpdb.getBloodChe(), HealthRecordCheckConstants.ATTR_CHOLINESTERASE, collect, list);
			ItemValueVO itemValueVO15 = setItemValueVO("乳酸脱氢酶",mpdb.getBloodLdh(), HealthRecordCheckConstants.ATTR_LACTATE_DEHYDROGENASE, collect, list);
			ItemValueVO itemValueVO16 = setItemValueVO("α-羟丁酸脱氢酶",mpdb.getBloodHbdh(), HealthRecordCheckConstants.ATTR_ALPHA_HB_DEHYDROGENASE, collect, list);
			ItemValueVO itemValueVO17 = setItemValueVO("肌酸激酶",mpdb.getBloodCk(), HealthRecordCheckConstants.ATTR_CREATINE_KINASE, collect, list);
			ItemValueVO itemValueVO18 = setItemValueVO("肌酸激酶同工酶",mpdb.getBloodCkMb(), HealthRecordCheckConstants.ATTR_CREATINE_KINASE_ISOENZYME, collect, list);
			ItemValueVO itemValueVO19 = setItemValueVO("尿素",mpdb.getBloodBun(), HealthRecordCheckConstants.ATTR_BLOOD_UREA_NITROGEN, collect, list);
			ItemValueVO itemValueVO20 = setItemValueVO("肌酐",mpdb.getBloodCr(), HealthRecordCheckConstants.ATTR_CREATININE, collect, list);
			ItemValueVO itemValueVO21 = setItemValueVO("尿酸",mpdb.getBloodUa(), HealthRecordCheckConstants.ATTR_URIC_ACID, collect, list);
			ItemValueVO itemValueVO22 = setItemValueVO("空腹血葡萄糖",mpdb.getBloodGlucose(), HealthRecordCheckConstants.ATTR_FASTING_BLOOD_GLUCOSE, collect, list);
			ItemValueVO itemValueVO23 = setItemValueVO("血清胱抑素c",mpdb.getBloodCpi(), HealthRecordCheckConstants.ATTR_SERUM_CYSTATIN_C, collect, list);
			ItemValueVO itemValueVO24 = setItemValueVO("β2-微球蛋白",mpdb.getBloodΒ2Mg(), HealthRecordCheckConstants.ATTR_BLOOD_B2_MG, collect, list);
			ItemValueVO itemValueVO25 = setItemValueVO("钾",mpdb.getBloodK(), HealthRecordCheckConstants.ATTR_POTASSIUM, collect, list);
			ItemValueVO itemValueVO26 = setItemValueVO("钠",mpdb.getBloodNa(), HealthRecordCheckConstants.ATTR_SODIUM, collect, list);
			ItemValueVO itemValueVO27 = setItemValueVO("氯",mpdb.getBloodCl(), HealthRecordCheckConstants.ATTR_CHLORIDE, collect, list);
			ItemValueVO itemValueVO28 = setItemValueVO("钙",mpdb.getBloodCa(), HealthRecordCheckConstants.ATTR_CALCIUM, collect, list);
			ItemValueVO itemValueVO29 = setItemValueVO("磷",mpdb.getBloodIp(), HealthRecordCheckConstants.ATTR_PHOSPHORUS, collect, list);
			ItemValueVO itemValueVO30 = setItemValueVO("镁",mpdb.getBloodMg(), HealthRecordCheckConstants.ATTR_MAGNESIUM, collect, list);
			ItemValueVO itemValueVO31 = setItemValueVO("总二氧化碳总量",mpdb.getBloodTco2(), HealthRecordCheckConstants.ATTR_CARBON_DIOXIDE, collect, list);
			ItemValueVO itemValueVO32 = setItemValueVO("甘油三酯",mpdb.getBloodTg(), HealthRecordCheckConstants.ATTR_TRIGLYCERIDES, collect, list);
			ItemValueVO itemValueVO33 = setItemValueVO("总胆固醇",mpdb.getBloodCho(), HealthRecordCheckConstants.ATTR_TOTAL_CHOLESTEROL, collect, list);
			ItemValueVO itemValueVO34 = setItemValueVO("高密度脂蛋白胆固醇",mpdb.getBloodHdlC(), HealthRecordCheckConstants.ATTR_HDL_CHOLESTEROL, collect, list);
			ItemValueVO itemValueVO35 = setItemValueVO("低密度脂蛋白胆固醇",mpdb.getBloodLdlC(), HealthRecordCheckConstants.ATTR_LDL_CHOLESTEROL, collect, list);
			ItemValueVO itemValueVO36 = setItemValueVO("载脂蛋白a-1",mpdb.getBloodApoA1(), HealthRecordCheckConstants.ATTR_APOLIPOPROTEIN_A1, collect, list);
			ItemValueVO itemValueVO37 = setItemValueVO("载脂蛋白b",mpdb.getBloodApoB(), HealthRecordCheckConstants.ATTR_APOLIPOPROTEIN_B, collect, list);
			ItemValueVO itemValueVO38 = setItemValueVO("载脂蛋白a-1/b",mpdb.getBloodApoA1ToB(), HealthRecordCheckConstants.ATTR_APOLIPOPROTEIN_A1B_RATIO, collect, list);
			ItemValueVO itemValueVO39 = setItemValueVO("脂蛋白a ",mpdb.getBloodLpa(), HealthRecordCheckConstants.ATTR_BLOOD_LPA, collect, list);
			healthItemVO.setObj(list);
			data.add(healthItemVO);

			ThreadLocalUtils.release();
			itemId.incrementAndGet();
		}
	}

	private void getCoagulationFunction(MpPerson person, List<MpUnitDict> totalUnitDicts, List<HealthItemVO> data, AtomicInteger itemId) {
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		List<MpCoagulationFunction> mpdbs = mpCoagulationFunctionMapper.selectByPersonId(personId);
		List<String> resultTimes = mpdbs.stream().map(v -> DateUtils.dateTime(v.getResultTime())).collect(Collectors.toList());

		Map<String, MpCoagulationFunction> mpdbsMap = mpdbs.stream().collect(Collectors.toMap(key->DateUtils.dateTime(key.getResultTime()), Function.identity()));
		for(String resultTime: resultTimes) {
			MpCoagulationFunction mpdb = mpdbsMap.get(resultTime);

			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId(itemId.get());
			healthItemVO.setItemName(resultTime +" "+HealthPatientCheckTestEnum.COAGULATION_FOUR.getValue());

			List<MpUnitDict> mpUnitDicts = totalUnitDicts.stream().filter(mpUnitDict ->
					mpUnitDict.getTableName().equals(HealthRecordCheckConstants.TAB_MP_COAGULATION_ITEMS)).collect(Collectors.toList());
			Map<String, List<MpUnitDict>> collect = mpUnitDicts.stream().collect(Collectors.groupingBy(MpUnitDict::getCode));

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();

			ThreadLocalUtils.put("sex",sex);

			// 获取检验时候的年龄
			threadLocalSetAge(cardId, mpdb.getResultTime());

			ItemValueVO itemValueVO1 = setItemValueVO("凝血酶原时间",mpdb.getCoagulationPt(), HealthRecordCheckConstants.ATTR_PROTHROMBIN_TIME, collect, list);
			ItemValueVO itemValueVO2 = setItemValueVO("活化部分凝血活酶时间",mpdb.getCoagulationAptt(), HealthRecordCheckConstants.ATTR_APTT, collect, list);
			ItemValueVO itemValueVO3 = setItemValueVO("纤维蛋白原含量",mpdb.getCoagulationFib(), HealthRecordCheckConstants.ATTR_FIBRINOGEN, collect, list);
			ItemValueVO itemValueVO4 = setItemValueVO("凝血酶时间",mpdb.getCoagulationTt(), HealthRecordCheckConstants.ATTR_THROMBIN_TIME, collect, list);
			ItemValueVO itemValueVO5 = setItemValueVO("纤维蛋白(原)降解产物",mpdb.getCoagulationFdp(), HealthRecordCheckConstants.ATTR_FDP, collect, list);
			ItemValueVO itemValueVO6 = setItemValueVO("d-二聚体定量",mpdb.getCoagulationDdimer(), HealthRecordCheckConstants.ATTR_D_DIMER_QUANTITATIVE_TEST, collect, list);
			ItemValueVO itemValueVO7 = setItemValueVO("国际标准化比值",mpdb.getCoagulationInr(), HealthRecordCheckConstants.ATTR_INTERNATIONAL_NORM_RATIO, collect, list);
			ItemValueVO itemValueVO8 = setItemValueVO("凝血酶原活动度",mpdb.getCoagulationPta(), HealthRecordCheckConstants.ATTR_PROTHROMBIN_ACTIVITY, collect, list);
			healthItemVO.setObj(list);
			data.add(healthItemVO);

			ThreadLocalUtils.release();
			itemId.incrementAndGet();
		}
	}

	private void getInfectiousDiseases(MpPerson person, List<HealthItemVO> data, AtomicInteger itemId) {
		String personId = person.getPersonId();
		List<MpInfectiousDiseases> mpdbs = mpInfectiousDiseasesMapper.selectByPersonId(personId);
		List<String> resultTimes = mpdbs.stream().map(v -> DateUtils.dateTime(v.getResultTime())).collect(Collectors.toList());

		Map<String, MpInfectiousDiseases> mpdbsMap = mpdbs.stream().collect(Collectors.toMap(key->DateUtils.dateTime(key.getResultTime()), Function.identity()));
		for(String resultTime: resultTimes) {
			MpInfectiousDiseases mpdb = mpdbsMap.get(resultTime);

			HealthItemVO<List<ItemValueVO>> healthItemVO = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId(itemId.get());
			healthItemVO.setItemName(resultTime +" "+HealthPatientCheckTestEnum.HEPATITS_B_ITEMS.getValue());

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();
			ItemValueVO itemValueVO1 = setItemValueVO("乙肝表面抗原  ", mpdb.getHbvSag(), list);
			ItemValueVO itemValueVO2 = setItemValueVO("乙肝表面抗体", mpdb.getHbvSab(),list);
			ItemValueVO itemValueVO3 = setItemValueVO("乙肝e抗原", mpdb.getHbvEag(),list);
			ItemValueVO itemValueVO4 = setItemValueVO("乙肝e抗体", mpdb.getHbvEab(),list);
			ItemValueVO itemValueVO5 = setItemValueVO("乙肝核心抗体", mpdb.getHbvCab(),list);
			ItemValueVO itemValueVO6 = setItemValueVO("乙型肝炎病毒dna(hbv-dna)", mpdb.getHbvDna(),list);
			ItemValueVO itemValueVO7 = setItemValueVO("丙型肝炎病毒dna(hcv-rna)", mpdb.getHcvRna(),list);
			ItemValueVO itemValueVO8 = setItemValueVO("丙肝抗体", mpdb.getHcvAb(),list);
			ItemValueVO itemValueVO9 = setItemValueVO("艾滋病抗体", mpdb.getHivAg(),list);
			ItemValueVO itemValueVO10 = setItemValueVO("梅毒抗体", mpdb.getSyphilisAb(),list);
			healthItemVO.setObj(list);

			data.add(healthItemVO);

			itemId.incrementAndGet();
		}
	}

	private void getStoolRoutine(MpPerson person, List<HealthItemVO> data, AtomicInteger itemId) {
		String personId = person.getPersonId();
		List<MpStoolRoutine> mpdbs = mpStoolRoutineMapper.selectByPersonId(personId);
		List<String> resultTimes = mpdbs.stream().map(v -> DateUtils.dateTime(v.getResultTime())).collect(Collectors.toList());

		Map<String, MpStoolRoutine> mpdbsMap = mpdbs.stream().collect(Collectors.toMap(key->DateUtils.dateTime(key.getResultTime()), Function.identity()));
		for(String resultTime: resultTimes) {
			MpStoolRoutine mpdb = mpdbsMap.get(resultTime);

			HealthItemVO<List<ItemValueVO>> healthItemVO = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId(itemId.get());
			healthItemVO.setItemName(resultTime +" "+HealthPatientCheckTestEnum.STOOL_ROUTINE.getValue());

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();
			ItemValueVO itemValueVO1= setItemValueVO("大便颜色", mpdb.getFecesColour(),list);
			ItemValueVO itemValueVO2= setItemValueVO("大便性状", mpdb.getFecesCharacter(),list);
			ItemValueVO itemValueVO3= setItemValueVO("便红细胞", mpdb.getFecesRbc(), list);
			ItemValueVO itemValueVO4= setItemValueVO("便白细胞", mpdb.getFecesWbc(), list);
			ItemValueVO itemValueVO5= setItemValueVO("便隐血", mpdb.getFecesOb(), list);
			ItemValueVO itemValueVO6= setItemValueVO("蛔虫卵", mpdb.getFecesAscarid(), list);
			ItemValueVO itemValueVO7= setItemValueVO("鞭虫卵", mpdb.getFecesWhipworm(), list);
			ItemValueVO itemValueVO8= setItemValueVO("蛲虫卵", mpdb.getFecesPinworm(), list);
			ItemValueVO itemValueVO9= setItemValueVO("绦虫卵", mpdb.getFecesTapeworm(), list);
			healthItemVO.setObj(list);

			data.add(healthItemVO);

			itemId.incrementAndGet();
		}
	}

	private void getUrineRoutine(MpPerson person, List<MpUnitDict> totalUnitDicts, List<HealthItemVO> data, AtomicInteger itemId) {
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		List<MpUrineRoutine> mpdbs = mpUrineRoutineMapper.selectByPersonId(personId);
		List<String> resultTimes = mpdbs.stream().map(v -> DateUtils.dateTime(v.getResultTime())).collect(Collectors.toList());

		Map<String, MpUrineRoutine> mpdbsMap = mpdbs.stream().collect(Collectors.toMap(key->DateUtils.dateTime(key.getResultTime()), Function.identity()));
		for(String resultTime: resultTimes) {
			MpUrineRoutine mpdb = mpdbsMap.get(resultTime);
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId(itemId.get());
			healthItemVO.setItemName(resultTime +" "+HealthPatientCheckTestEnum.URINE_ROUTINE.getValue());

			List<MpUnitDict> mpUnitDicts = totalUnitDicts.stream().filter(mpUnitDict ->
					mpUnitDict.getTableName().equals(HealthRecordCheckConstants.TAB_MP_URINE_ROUTINE)).collect(Collectors.toList());
			Map<String, List<MpUnitDict>> collect = mpUnitDicts.stream().collect(Collectors.groupingBy(MpUnitDict::getCode));

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();

			ThreadLocalUtils.put("sex",sex);

			// 获取检验时候的年龄
			threadLocalSetAge(cardId, mpdb.getResultTime());

			ItemValueVO itemValueVO1= setItemValueVO("尿比重", mpdb.getUrineSg(), HealthRecordCheckConstants.ATTR_SPECIFIC_GRAVITY_URINE, collect, list);
			ItemValueVO itemValueVO2= setItemValueVO("尿酸碱度", mpdb.getUrinePh(), HealthRecordCheckConstants.ATTR_URINE_PH, collect, list);
			ItemValueVO itemValueVO3= setItemValueVO("尿白细胞", mpdb.getUrineWbcCount(), HealthRecordCheckConstants.ATTR_URINE_WHITE_CELLS, collect, list);
			ItemValueVO itemValueVO4= setItemValueVO("尿红细胞", mpdb.getUrineRbcCount(), HealthRecordCheckConstants.ATTR_URINE_RED_CELLS, collect, list);
//			ItemValueVO itemValueVO5= setItemValueVO("尿亚硝酸盐", mpdb.getUrineNit(), HealthRecordCheckConstants.ATTR_URINE_NITRITE, collect, list);
			ItemValueVO itemValueVO5= setItemValueVO("尿亚硝酸盐",mpdb.getUrineNit(), list);
			ItemValueVO itemValueVO6= setItemValueVO("尿蛋白质", mpdb.getUrinePro(),list);
			ItemValueVO itemValueVO7= setItemValueVO("尿糖", mpdb.getUrineGlu(),  list);
			ItemValueVO itemValueVO8= setItemValueVO("尿酮体", mpdb.getUrineKet(), list);
			ItemValueVO itemValueVO9= setItemValueVO("尿胆原", mpdb.getUrineUro(), list);
			ItemValueVO itemValueVO10= setItemValueVO("胆红素", mpdb.getUrineBil(), list);
			ItemValueVO itemValueVO11= setItemValueVO("尿隐血", mpdb.getUrineBlo(), list);
			healthItemVO.setObj(list);
			data.add(healthItemVO);

			ThreadLocalUtils.release();
			itemId.incrementAndGet();
		}
	}

	private void getBloodRoutine(MpPerson person, List<MpUnitDict> totalUnitDicts, List<HealthItemVO> data, AtomicInteger itemId) {
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		List<MpBloodRoutine> mpdbs = mpBloodRoutineMapper.selectByPersonId(personId);
		List<String> resultTimes = mpdbs.stream().map(v -> DateUtils.dateTime(v.getResultTime())).collect(Collectors.toList());

		Map<String, MpBloodRoutine> mpdbsMap = mpdbs.stream().collect(Collectors.toMap(key->DateUtils.dateTime(key.getResultTime()), Function.identity()));
		for(String resultTime: resultTimes){
			MpBloodRoutine mpdb = mpdbsMap.get(resultTime);
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId(itemId.get());
			healthItemVO.setItemName(resultTime +" "+HealthPatientCheckTestEnum.BLOOD_ROUTINE.getValue());

			List<MpUnitDict> mpUnitDicts = totalUnitDicts.stream().filter(mpUnitDict ->
					mpUnitDict.getTableName().equals(HealthRecordCheckConstants.TAB_MP_BLOOD_ROUTINE)).collect(Collectors.toList());
			Map<String, List<MpUnitDict>> collect = mpUnitDicts.stream().collect(Collectors.groupingBy(MpUnitDict::getCode));

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();

			ThreadLocalUtils.put("sex",sex);

			// 获取检验时候的年龄
			threadLocalSetAge(cardId, mpdb.getResultTime());

			//白细胞计数
			ItemValueVO itemValueVO1= setItemValueVO("白细胞计数", mpdb.getWbcCount(), HealthRecordCheckConstants.ATTR_WHITE_CELL_COUNT, collect, list);
			ItemValueVO itemValueVO2= setItemValueVO("红细胞计数", mpdb.getRbcCount(), HealthRecordCheckConstants.ATTR_RED_CELL_COUNT, collect, list);
			ItemValueVO itemValueVO3= setItemValueVO("血红蛋白", mpdb.getBloodHb(), HealthRecordCheckConstants.ATTR_HEMOGLOBIN, collect, list);
			ItemValueVO itemValueVO4= setItemValueVO("红细胞压积", mpdb.getBloodHct(), HealthRecordCheckConstants.ATTR_HEMATOCRIT, collect, list);
			ItemValueVO itemValueVO5= setItemValueVO("平均红细胞体积", mpdb.getBloodMcv(), HealthRecordCheckConstants.ATTR_MEAN_CORPUS_VOLUME, collect, list);
			ItemValueVO itemValueVO6= setItemValueVO("平均红细胞血红蛋白含量", mpdb.getBloodMch(), HealthRecordCheckConstants.ATTR_MEAN_CORPUS_VOLUME, collect, list);
			ItemValueVO itemValueVO7= setItemValueVO("平均红细胞血红蛋白浓度", mpdb.getBloodMchc(), HealthRecordCheckConstants.ATTR_MEAN_CORPUS_HEM_CONCENT, collect, list);
			ItemValueVO itemValueVO8= setItemValueVO("红细胞分布宽度-变异系数", mpdb.getBloodRdwCv(), HealthRecordCheckConstants.ATTR_RED_CELL_DIS_WIDTH_VARIATION, collect, list);
			ItemValueVO itemValueVO9= setItemValueVO("红细胞分布宽度-标准差", mpdb.getBloodRdwSd(), HealthRecordCheckConstants.ATTR_RED_CELL_DIS_DEVIATION, collect, list);
			ItemValueVO itemValueVO10= setItemValueVO("血小板计数", mpdb.getBloodPltCount(), HealthRecordCheckConstants.ATTR_PLATELET_COUNT, collect, list);
			ItemValueVO itemValueVO11= setItemValueVO("平均血小板体积", mpdb.getBloodMpv(), HealthRecordCheckConstants.ATTR_MEAN_PLATELET_VOLUME, collect, list);
			ItemValueVO itemValueVO12= setItemValueVO("血小板压积", mpdb.getBloodPct(), HealthRecordCheckConstants.ATTR_PLATELETCRIT, collect, list);
			ItemValueVO itemValueVO13= setItemValueVO("血小板分布宽度", mpdb.getBloodPdw(), HealthRecordCheckConstants.ATTR_PLATELET_DIS_WIDTH, collect, list);
			ItemValueVO itemValueVO14= setItemValueVO("淋巴细胞绝对值", mpdb.getBloodLyCount(), HealthRecordCheckConstants.ATTR_ABS_LYMPHOCYTE_COUNT, collect, list);
			ItemValueVO itemValueVO15= setItemValueVO("淋巴细胞百分比", mpdb.getBloodLyPercentage(), HealthRecordCheckConstants.ATTR_LYMPHOCYTE_PERCENTAGE, collect, list);
			ItemValueVO itemValueVO16= setItemValueVO("中性粒细胞绝对值", mpdb.getBloodNeCount(), HealthRecordCheckConstants.ATTR_ABS_NEUTROPHIL_COUNT, collect, list);
			ItemValueVO itemValueVO17= setItemValueVO("中性粒细胞百分比", mpdb.getBloodNePercentage(), HealthRecordCheckConstants.ATTR_NEUTROPHIL_PERCENTAGE, collect, list);
			ItemValueVO itemValueVO18= setItemValueVO("单核细胞绝对值", mpdb.getBloodMoCount(), HealthRecordCheckConstants.ATTR_ABS_MONOCYTE_COUNT, collect, list);
			ItemValueVO itemValueVO19= setItemValueVO("单核细胞百分比", mpdb.getBloodMoPercentage(), HealthRecordCheckConstants.ATTR_MONOCYTE_PERCENTAGE, collect, list);
			ItemValueVO itemValueVO20= setItemValueVO("嗜酸性细胞绝对值", mpdb.getBloodEoCount(), HealthRecordCheckConstants.ATTR_ABS_EOSINOPHIL_COUNT, collect, list);
			ItemValueVO itemValueVO21= setItemValueVO("嗜酸性细胞百分比", mpdb.getBloodEoPercentage(), HealthRecordCheckConstants.ATTR_EOSINOPHIL_PERCENTAGE, collect, list);
			ItemValueVO itemValueVO22= setItemValueVO("嗜碱性细胞绝对值", mpdb.getBloodBaCount(), HealthRecordCheckConstants.ATTR_ABS_BASOPHIL_COUNT, collect, list);
			ItemValueVO itemValueVO23= setItemValueVO("嗜碱性细胞百分比", mpdb.getBloodBaPercentage(), HealthRecordCheckConstants.ATTR_BASOPHIL_PERCENTAGE, collect, list);
			healthItemVO.setObj(list);
			data.add(healthItemVO);

			ThreadLocalUtils.release();
			itemId.incrementAndGet();
		}
	}

	public DchealthCloudResponseEntity<List<HealthItemVO>> mzExamItemList(DetailForm detail) {
		DchealthCloudResponseEntity<List<HealthItemVO>>  result = new DchealthCloudResponseEntity<List<HealthItemVO>>();
		String personId =detail.getPersonId();
		if (StringUtils.isEmpty(personId)) {
			throw new ServiceException("personId不能为空");
		}
		List<HealthItemVO> data = new ArrayList<HealthItemVO>();

		AtomicInteger itemId = new AtomicInteger(1);
		String visitType = "门诊";
		if ("1".equals(detail.getType())) {
			//住院
		}if ("2".equals(detail.getType())) {
			//体检
		}if ("4".equals(detail.getType())) {
			//预住院
			visitType = "预住院";
		}

		getExamOf(personId, data, itemId, TableEnum.MP_CT_EXAM,visitType);
		getExamOf(personId, data, itemId, TableEnum.MP_NUCLEAR_MAGNETISM_EXAM,visitType);
		getExamOf(personId, data, itemId, TableEnum.MP_X_RAY_EXAM,visitType);
		getExamOf(personId, data, itemId, TableEnum.MP_ULTRASONIC_EXAM,visitType);
		getExamOf(personId, data, itemId, TableEnum.MP_ELECTROCARDIOGRAM_EXAM,visitType);
		// CT
//		getMzCtExam(personId, data, itemId);
//		// 磁共振
//		getMzNuclearMagnetismExam(personId, data, itemId);
//
//		// X线
//		getMzXRayExam(personId, data, itemId);
//
//		// 超声
//		getMzUltrasonicExam(personId, data, itemId);
//
//		// 心电图
//		getMzElectrocardiogramExam(personId, data, itemId);

		result.setData(data);

		return result;
	}
	/**
	 * 合并同类项: 将同testNo, 不同testNo同天同内容的合并
	 * @param personId
	 * @param data
	 * @param itemId
	 * @param visitType
	 */
	private void getExamOf(String personId, List<HealthItemVO> data, AtomicInteger itemId,TableEnum tableEnum, String visitType) {
		List<MpCommonExam> totalCtExams = mpCommonExamMapper.selectMpCommonExamList(personId,null, tableEnum.getTableName(),visitType);
		List<String> reportDates = totalCtExams.stream().map(v -> DateUtils.dateTime(v.getReportDate())).distinct().collect(Collectors.toList());

		Map<String, List<MpCommonExam>> mpCtExamMap = totalCtExams.stream().collect(Collectors.groupingBy(key -> DateUtils.dateTime(key.getReportDate())));

		HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
		healthItemVO.setItemId(itemId.get());
		healthItemVO.setItemName(tableEnum.getType());
		List<CatalogVO> result = new ArrayList<>();
		for(String reportDate: reportDates){
			List<MpCommonExam> mpCtExams = mpCtExamMap.get(reportDate);

			//合并 按testNo做完一遍, 再来一遍内容, 同级~
			Map<String, List<MpCommonExam>> collect = mpCtExams.stream()
					.filter(x->!StringUtils.isEmpty(x.getTestNo()))
					.collect(Collectors.groupingBy(MpCommonExam::getTestNo));
			List<MpCommonExam> twoMpCtExams = new ArrayList<>();
			// 不同号按内容合并MpCommonExam
			collect.forEach((testNo,mpctExamList)->{
				MpCommonExam mpCtExam = new MpCommonExam();
				String ctTitle = mpctExamList.stream().map(MpCommonExam::getCheckSourceType).distinct().collect(Collectors.joining("、"));
				mpCtExam.setCheckSourceType(ctTitle);
//				mpctExamList.stream().filter(x->!StringUtils.isEmpty(x.getObjectiveView())).findFirst().orElse(mpCtExam)
				mpCtExam.setDepartmentName(mpctExamList.get(0).getDepartmentName());
				mpCtExam.setTestNo(testNo);
				String objectiveView = mpctExamList.get(0).getObjectiveView();
				mpCtExam.setObjectiveView(objectiveView);
				String subjectiveHints = mpctExamList.get(0).getSubjectiveHints();
				mpCtExam.setSubjectiveHints(subjectiveHints);
				//md5 主观客观任意有值参与计算
				if (!StringUtils.isEmpty(objectiveView)||!StringUtils.isEmpty(subjectiveHints)){
					String objectiveMD5 = MD5Util.encode(StringUtils.trim(objectiveView)+StringUtils.trim(subjectiveHints));
					mpCtExam.setObjectiveViewMD5(objectiveMD5);
				}
				mpCtExam.setReportDate(mpctExamList.get(0).getReportDate());
				twoMpCtExams.add(mpCtExam);
			});
			Map<String, List<MpCommonExam>> viewCtList = twoMpCtExams.stream()
					.filter(x->!StringUtils.isEmpty(x.getObjectiveViewMD5()))
					.collect(Collectors.groupingBy(MpCommonExam::getObjectiveViewMD5));
			viewCtList.forEach((view,ctlist)->{
				CatalogVO catalogVO = new CatalogVO();
				String ctTitle = ctlist.stream().map(MpCommonExam::getCheckSourceType).distinct().collect(Collectors.joining("、"));
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				setItemValueVO("检查项目",ctTitle,list);
//				setItemValueVO("检查方式",mpCtExam.getTestMethod(),list);
//				setItemValueVO("身体部位",mpCtExam.getSctPosition(),list);
				setItemValueVO("所见",ctlist.get(0).getObjectiveView(),list);
				setItemValueVO("诊断意见",ctlist.get(0).getSubjectiveHints(),list);
//				setItemValueVO("诊断意见",mpCtExam.getRelevantClinicDiagnosis(),list);
//				setItemValueVO("检查时间",DateUtils.dateTime(mpCtExam.getTechnologyDate()),list);
//				setItemValueVO("报告时间",DateUtils.dateTime(mpCtExam.getReportDate()),list);
				String deptName = ctlist.stream().map(MpCommonExam::getDepartmentName).distinct().collect(Collectors.joining("、"));
				setItemValueVO("开单科室",getRuleDeptName(deptName),list);
				catalogVO.setItemList(list);
				catalogVO.setItemTitle(reportDate+" "+ctTitle);
				result.add(catalogVO);
			});
			itemId.incrementAndGet();
		}
		healthItemVO.setObj(result);
		if (!CollectionUtils.isEmpty(totalCtExams)) {
			data.add(healthItemVO);
		}
	}

	/**
	 * 规则化部门名称
	 * @param dept
	 * @return
	 */
	private String getRuleDeptName(String dept){
		if (StringUtils.isEmpty(dept)||"null".equals(dept)) {
			return "-";
		}
		return dept;
	}


	private void getMzCtExam(String personId, List<HealthItemVO> data, AtomicInteger itemId) {
		List<MpCtExam> totalCtExams = mpCtExamMapper.selectMpCtExamList(personId,null, "门诊");
		List<String> reportDates = totalCtExams.stream().map(v -> DateUtils.dateTime(v.getReportDate())).distinct().collect(Collectors.toList());

		Map<String, List<MpCtExam>> mpCtExamMap = totalCtExams.stream().collect(Collectors.groupingBy(key -> DateUtils.dateTime(key.getReportDate())));

		for(String reportDate: reportDates){
			List<MpCtExam> mpCtExams = mpCtExamMap.get(reportDate);
			HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
			healthItemVO.setItemId(itemId.get());
			healthItemVO.setItemName(reportDate+" "+HealthPatientCheckExamEnum.CT_EXAM.getValue());
			List<CatalogVO> result = new ArrayList<>();
			for(MpCtExam mpCtExam : mpCtExams){
				CatalogVO catalogVO = new CatalogVO();
				catalogVO.setItemTitle(mpCtExam.getTechnologyName());
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				setItemValueVO("检查项目",mpCtExam.getTechnologyName(),list);
				setItemValueVO("身体部位",mpCtExam.getSctPosition(),list);
				setItemValueVO("检查方式",mpCtExam.getTestMethod(),list);
				setItemValueVO("检查报告结果--客观所见",mpCtExam.getObjectiveView(),list);
				setItemValueVO("检查报告结果--主观提示",mpCtExam.getSubjectiveHints(),list);
				setItemValueVO("检查诊断",mpCtExam.getRelevantClinicDiagnosis(),list);
				setItemValueVO("检查时间",DateUtils.dateTime(mpCtExam.getTechnologyDate()),list);
				setItemValueVO("报告时间",DateUtils.dateTime(mpCtExam.getReportDate()),list);
				setItemValueVO("开单科室",mpCtExam.getDepartmentName(),list);
				catalogVO.setItemList(list);
				result.add(catalogVO);
			}
			healthItemVO.setObj(result);
			data.add(healthItemVO);
			itemId.incrementAndGet();
		}
	}

	private void getMzNuclearMagnetismExam(String personId, List<HealthItemVO> data, AtomicInteger itemId) {
		List<MpNuclearMagnetismExam> totalNuclearMagnetismExams = mpNuclearMagnetismExamMapper.selectMpNuclearMagnetismExamList(personId,null, "门诊");
		List<String> reportDates = totalNuclearMagnetismExams.stream().map(v -> DateUtils.dateTime(v.getReportDate())).distinct().collect(Collectors.toList());

		Map<String, List<MpNuclearMagnetismExam>> mpNuclearMagnetismExamMap = totalNuclearMagnetismExams.stream().collect(Collectors.groupingBy(key -> DateUtils.dateTime(key.getReportDate())));

		for(String reportDate: reportDates){
			List<MpNuclearMagnetismExam> mpNuclearMagnetismExams = mpNuclearMagnetismExamMap.get(reportDate);
			HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
			healthItemVO.setItemId(itemId.get());
			healthItemVO.setItemName(reportDate+" "+HealthPatientCheckExamEnum.NUCLEAR_MAGNETISM_EXAM.getValue());
			List<CatalogVO> result = new ArrayList<>();
			for(MpNuclearMagnetismExam mpNuclearMagnetismExam : mpNuclearMagnetismExams){
				CatalogVO catalogVO = new CatalogVO();
				catalogVO.setItemTitle(mpNuclearMagnetismExam.getTechnologyName());
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				setItemValueVO("检查项目",mpNuclearMagnetismExam.getTechnologyName(),list);
				setItemValueVO("身体部位",mpNuclearMagnetismExam.getSctPosition(),list);
				setItemValueVO("检查方式",mpNuclearMagnetismExam.getTestMethod(),list);
				setItemValueVO("检查报告结果--客观所见",mpNuclearMagnetismExam.getObjectiveView(),list);
				setItemValueVO("检查报告结果--主观提示",mpNuclearMagnetismExam.getSubjectiveHints(),list);
				setItemValueVO("检查诊断",mpNuclearMagnetismExam.getRelevantClinicDiagnosis(),list);
				setItemValueVO("检查时间",DateUtils.dateTime(mpNuclearMagnetismExam.getTechnologyDate()),list);
				setItemValueVO("报告时间",DateUtils.dateTime(mpNuclearMagnetismExam.getReportDate()),list);
				setItemValueVO("开单科室",mpNuclearMagnetismExam.getDepartmentName(),list);
				catalogVO.setItemList(list);
				result.add(catalogVO);
			}
			healthItemVO.setObj(result);
			data.add(healthItemVO);
			itemId.incrementAndGet();
		}
	}

	private void getMzXRayExam(String personId, List<HealthItemVO> data, AtomicInteger itemId) {
		List<MpXRayExam> totalXRayExams = mpXRayExamMapper.selectMpXRayExamList(personId,null, "门诊");
		List<String> reportDates = totalXRayExams.stream().map(v -> DateUtils.dateTime(v.getReportDate())).distinct().collect(Collectors.toList());

		Map<String, List<MpXRayExam>> XRayExamMap = totalXRayExams.stream().collect(Collectors.groupingBy(key -> DateUtils.dateTime(key.getReportDate())));

		for(String reportDate: reportDates){
			List<MpXRayExam> mpXRayExams = XRayExamMap.get(reportDate);
			HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
			healthItemVO.setItemId(itemId.get());
			healthItemVO.setItemName(reportDate+" "+HealthPatientCheckExamEnum.X_RAY_EXAM.getValue());
			List<CatalogVO> result = new ArrayList<>();

			for(MpXRayExam mpXRayExam : mpXRayExams){
				CatalogVO catalogVO = new CatalogVO();
				catalogVO.setItemTitle(mpXRayExam.getTechnologyName());
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				setItemValueVO("检查项目",mpXRayExam.getTechnologyName(),list);
				setItemValueVO("身体部位",mpXRayExam.getSctPosition(),list);
				setItemValueVO("检查方式",mpXRayExam.getTestMethod(),list);
				setItemValueVO("检查报告结果--客观所见",mpXRayExam.getObjectiveView(),list);
				setItemValueVO("检查报告结果--主观提示",mpXRayExam.getSubjectiveHints(),list);
				setItemValueVO("检查诊断",mpXRayExam.getRelevantClinicDiagnosis(),list);
				setItemValueVO("检查时间",DateUtils.dateTime(mpXRayExam.getTechnologyDate()),list);
				setItemValueVO("报告时间",DateUtils.dateTime(mpXRayExam.getReportDate()),list);
				setItemValueVO("开单科室",mpXRayExam.getDepartmentName(),list);
				catalogVO.setItemList(list);
				result.add(catalogVO);
			}
			healthItemVO.setObj(result);
			data.add(healthItemVO);
			itemId.incrementAndGet();
		}
	}

	private void getMzUltrasonicExam(String personId, List<HealthItemVO> data, AtomicInteger itemId) {
		List<MpUltrasonicExam> totalMpUltrasonicExams = mpUltrasonicExamMapper.selectMpUltrasonicExamList(personId,null, "门诊");
		List<String> reportDates = totalMpUltrasonicExams.stream().map(v -> DateUtils.dateTime(v.getReportDate())).distinct().collect(Collectors.toList());

		Map<String, List<MpUltrasonicExam>> mpUltrasonicExamsMap = totalMpUltrasonicExams.stream().collect(Collectors.groupingBy(key -> DateUtils.dateTime(key.getReportDate())));

		for(String reportDate: reportDates){
			List<MpUltrasonicExam> mpUltrasonicExams = mpUltrasonicExamsMap.get(reportDate);
			HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
			healthItemVO.setItemId(itemId.get());
			healthItemVO.setItemName(reportDate+" "+HealthPatientCheckExamEnum.ULTRASONIC_EXAM.getValue());
			List<CatalogVO> result = new ArrayList<>();

			for(MpUltrasonicExam mpUltrasonicExam : mpUltrasonicExams){
				CatalogVO catalogVO = new CatalogVO();
				catalogVO.setItemTitle(mpUltrasonicExam.getTechnologyName());
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				setItemValueVO("检查项目",mpUltrasonicExam.getTechnologyName(),list);
				setItemValueVO("身体部位",mpUltrasonicExam.getSctPosition(),list);
				setItemValueVO("检查方式",mpUltrasonicExam.getTestMethod(),list);
				setItemValueVO("检查报告结果--客观所见",mpUltrasonicExam.getObjectiveView(),list);
				setItemValueVO("检查报告结果--主观提示",mpUltrasonicExam.getSubjectiveHints(),list);
				setItemValueVO("检查诊断",mpUltrasonicExam.getRelevantClinicDiagnosis(),list);
				setItemValueVO("检查时间",DateUtils.dateTime(mpUltrasonicExam.getTechnologyDate()),list);
				setItemValueVO("报告时间",DateUtils.dateTime(mpUltrasonicExam.getReportDate()),list);
				setItemValueVO("开单科室",mpUltrasonicExam.getDepartmentName(),list);
				catalogVO.setItemList(list);
				result.add(catalogVO);
			}
			healthItemVO.setObj(result);
			data.add(healthItemVO);
			itemId.incrementAndGet();
		}
	}

	private void getMzElectrocardiogramExam(String personId, List<HealthItemVO> data, AtomicInteger itemId) {
		List<MpElectrocardiogramExam> totalElectrocardiogramExams = mpElectrocardiogramExamMapper.selectMpElectrocardiogramExamList(personId,null, "门诊");
		List<String> reportDates = totalElectrocardiogramExams.stream().map(v -> DateUtils.dateTime(v.getReportDate())).distinct().collect(Collectors.toList());

		Map<String, List<MpElectrocardiogramExam>> electrocardiogramExamsMap = totalElectrocardiogramExams.stream().collect(Collectors.groupingBy(key -> DateUtils.dateTime(key.getReportDate())));

		for(String reportDate: reportDates){
			List<MpElectrocardiogramExam> mpElectrocardiogramExams = electrocardiogramExamsMap.get(reportDate);
			HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
			healthItemVO.setItemId(itemId.get());
			healthItemVO.setItemName(reportDate+" "+HealthPatientCheckExamEnum.ECG.getValue());
			List<CatalogVO> result = new ArrayList<>();

			for(MpElectrocardiogramExam mpElectrocardiogramExam : mpElectrocardiogramExams){
				CatalogVO catalogVO = new CatalogVO();
				catalogVO.setItemTitle(mpElectrocardiogramExam.getTechnologyName());
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				setItemValueVO("检查项目",mpElectrocardiogramExam.getTechnologyName(),list);
				setItemValueVO("身体部位",mpElectrocardiogramExam.getSctPosition(),list);
				setItemValueVO("检查方式",mpElectrocardiogramExam.getTestMethod(),list);
				setItemValueVO("检查报告结果--客观所见",mpElectrocardiogramExam.getObjectiveView(),list);
				setItemValueVO("检查报告结果--主观提示",mpElectrocardiogramExam.getSubjectiveHints(),list);
				setItemValueVO("检查诊断",mpElectrocardiogramExam.getRelevantClinicDiagnosis(),list);
				setItemValueVO("心电图报告",mpElectrocardiogramExam.getPdfReport(),list);
				setItemValueVO("检查时间",DateUtils.dateTime(mpElectrocardiogramExam.getTechnologyDate()),list);
				setItemValueVO("报告时间",DateUtils.dateTime(mpElectrocardiogramExam.getReportDate()),list);
				setItemValueVO("开单科室",mpElectrocardiogramExam.getDepartmentName(),list);
				catalogVO.setItemList(list);
				result.add(catalogVO);
			}
			healthItemVO.setObj(result);
			data.add(healthItemVO);
			itemId.incrementAndGet();
		}
	}


	private void getMzSurgicalExam(String personId, List<HealthItemVO> data, AtomicInteger itemId) {
		List<MpSurgicalExam> mpdbs = mpSurgicalExamMapper.selectByPersonId(personId);
		Map<String, MpSurgicalExam> mpInternalMedicineExamMaps = mpdbs.stream().collect(Collectors.toMap(key->DateUtils.dateTime(key.getResultDate()), Function.identity()));

		for(String resultDate: mpInternalMedicineExamMaps.keySet()){
			MpSurgicalExam mpdb = mpInternalMedicineExamMaps.get(resultDate);

			HealthItemVO< List<ItemValueVO>>  healthItemVO  = new HealthItemVO< List<ItemValueVO>>();
			healthItemVO.setItemId(itemId.get());
			healthItemVO.setItemName(resultDate+" "+HealthPatientCheckExamEnum.SURGICAL_EXAM.getValue());
			List<ItemValueVO> list = new ArrayList<ItemValueVO>();
			ItemValueVO itemValueVO1 = setItemValueVO("皮肤",mpdb.getSkin(), list);
			ItemValueVO itemValueVO2 = setItemValueVO("肛门",mpdb.getAnus(), list);;
			ItemValueVO itemValueVO3 = setItemValueVO("表浅淋巴结",mpdb.getSuperficialLynph(), list);
			ItemValueVO itemValueVO4 = setItemValueVO("甲状腺",mpdb.getThyroid(), list);
			ItemValueVO itemValueVO5 = setItemValueVO("脊柱",mpdb.getSpine(), list);
			ItemValueVO itemValueVO6 = setItemValueVO("四肢关节",mpdb.getLimbJoint(), list);
			healthItemVO.setObj(list);
			data.add(healthItemVO);
			itemId.incrementAndGet();
		}
	}

	private void getMzInternalMedicineExam(String personId, List<HealthItemVO> data, AtomicInteger itemId) {
		List<MpInternalMedicineExam> mpInternalMedicineExams = mpInternalMedicineExamMapper.selectByPersonId(personId);
		Map<String, MpInternalMedicineExam> mpInternalMedicineExamMaps = mpInternalMedicineExams.stream().collect(Collectors.toMap(key->DateUtils.dateTime(key.getResultDate()), Function.identity()));

		for(String resultDate: mpInternalMedicineExamMaps.keySet()){
			MpInternalMedicineExam mpInternalMedicineExam = mpInternalMedicineExamMaps.get(resultDate);

			HealthItemVO< List<ItemValueVO> >  healthItemVO  = new HealthItemVO<>();
			healthItemVO.setItemId(itemId.get());
			healthItemVO.setItemName(resultDate +" "+HealthPatientCheckExamEnum.INTERNAL_MEDICINE_EXAM.getValue());
			List<ItemValueVO>  list = new ArrayList<ItemValueVO>();
			ItemValueVO itemValueVO1 = setItemValueVO("胸部外形",mpInternalMedicineExam.getChestShape(), list);
			ItemValueVO itemValueVO2 = setItemValueVO("胸部触诊",mpInternalMedicineExam.getChestPalpation(), list);
			ItemValueVO itemValueVO3 = setItemValueVO("胸部叩诊",mpInternalMedicineExam.getChestPercussion(), list);
			ItemValueVO itemValueVO4 = setItemValueVO("心界",mpInternalMedicineExam.getHeartBorder(), list);
			ItemValueVO itemValueVO5 = setItemValueVO("肺部听诊",mpInternalMedicineExam.getLungAuscultation(), list);
			ItemValueVO itemValueVO6 = setItemValueVO("心音",mpInternalMedicineExam.getHeartSound(), list);
			ItemValueVO itemValueVO7 = setItemValueVO("心律",mpInternalMedicineExam.getHeartRhythm(), list);
			ItemValueVO itemValueVO8 = setItemValueVO("心脏杂音",mpInternalMedicineExam.getHeartNoise(), list);
			ItemValueVO itemValueVO9 = setItemValueVO("肝脏触诊",mpInternalMedicineExam.getLiverPalpation(), list);
			ItemValueVO itemValueVO10 = setItemValueVO("脾脏触诊",mpInternalMedicineExam.getSpleenPalpation(), list);
			ItemValueVO itemValueVO11 = setItemValueVO("腹部外形",mpInternalMedicineExam.getAbdomShape(), list);
			ItemValueVO itemValueVO12 = setItemValueVO("腹部压痛 ",mpInternalMedicineExam.getAbdomPressurePain(), list);
			ItemValueVO itemValueVO13 = setItemValueVO("腹部叩诊",mpInternalMedicineExam.getAbdomPercussion(), list);
			healthItemVO.setObj(list);
			data.add(healthItemVO);
			itemId.incrementAndGet();
		}
	}

	public DchealthCloudResponseEntity<HealthItemVO<List<CatalogVO>>> queryMzDiagnosis(DetailForm detail)  {
		DchealthCloudResponseEntity<HealthItemVO<List<CatalogVO>>> result = new DchealthCloudResponseEntity<HealthItemVO<List<CatalogVO>>>();
		MpPerson person = mpPersonMapper.selectPersonByPersonId(detail.getPersonId());
		if(person == null) {
			result.setCode(CroApiCodeConstant.ARCHIVE_NOT_EXIST.getCode());
			return result;
		}

		String personId = detail.getPersonId();

		//诊断
		HealthItemVO<List<CatalogVO>> diagnosis = getDiagnosis(personId, null, "2");
		result.setData(diagnosis);
		return result;

	}
}
