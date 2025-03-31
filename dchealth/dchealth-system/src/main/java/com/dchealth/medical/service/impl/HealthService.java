package com.dchealth.medical.service.impl;


import com.dchealth.common.ThreadLocalUtils;
import com.dchealth.common.core.redis.RedisCache;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.MD5Util;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.compatibility.constants.HealthConstant;
import com.dchealth.compatibility.constants.HealthPatientCheckExamEnum;
import com.dchealth.compatibility.constants.HealthPatientCheckTestEnum;
import com.dchealth.compatibility.constants.HealthRecordCheckConstants;
import com.dchealth.compatibility.domain.*;
import com.dchealth.compatibility.from.DetailForm;
import com.dchealth.compatibility.from.HealthListSearchForm;
import com.dchealth.compatibility.from.MobileForm;
import com.dchealth.compatibility.mapper.*;
import com.dchealth.compatibility.utils.HealthUtils;
import com.dchealth.compatibility.utils.ZtreeNodeUtils;
import com.dchealth.compatibility.vo.*;
import com.dchealth.medical.domain.MpPerson;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.mapper.MpPersonMapper;
import com.dchealth.medical.mapper.PatientInfoMapper;
import com.dchealth.system.domain.*;
import com.dchealth.system.mapper.*;
import com.dchealth.system.service.IMpUrineRoutineColService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author lzx
 * @Date 2023/3/27
 * @Description:
 */
@Service
@Slf4j
public class HealthService  {

	@Autowired
	private MpPersonMapper mpPersonMapper;
	@Autowired
	private MpVisitOccurrenceMapper mpVisitOccurrenceMapper;
	@Autowired
	private MpAuxiliaryExaminationOtherMapper mpAuxiliaryExaminationOtherMapper;
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
	private MpUrineRoutineColMapper mpUrineRoutineColMapper;
	@Autowired
	private MpStoolRoutineColMapper mpStoolRoutineColMapper;
	@Autowired
	private MpUnitDictMapper mpUnitDictMapper;

	@Autowired
	private PatientInfoMapper patientInfoMapper;

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
	private IMpUrineRoutineColService mpUrineRoutineColService;
	@Autowired
	private MpBloodRoutineColMapper mpBloodRoutineColMapper;
	@Autowired
	private MpBloodBiochemistryColMapper mpBloodBiochemistryColMapper;
	@Autowired
	private MpThyroidFunctionColMapper mpThyroidFunctionColMapper;
	@Autowired
	private MpCoagulationFunctionColMapper mpCoagulationFunctionColMapper;
	@Autowired
	private MpTumorScreeningItemsColMapper mpTumorScreeningItemsColMapper;
	@Autowired
	private MpGlucoseToleranceColMapper mpGlucoseToleranceColMapper;


	public List<HealthItemVO> mzTestItemList(DetailForm detail) {
		// 通过patientId去查询personId
		if (StringUtils.isEmpty(detail.getPersonId())) {
			PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(Long.parseLong(detail.getPatientId()));
			if(patientInfo == null || StringUtils.isEmpty(patientInfo.getPersonId())){
				log.info("patientId = {} 在 patient_info 表查不到信息或者personId为空");
				return null;
			}
			detail.setPersonId(patientInfo.getPersonId());
		}

		MpPerson person = mpPersonMapper.getPersonByPersonId(detail.getPersonId());

		List<HealthItemVO> data = new ArrayList<HealthItemVO>();
		List<MpUnitDict> totalUnitDicts = mpUnitDictMapper.queryUnitDictByTableName(null);

		AtomicInteger itemId = new AtomicInteger(1);
		//血常规
//		getBloodRoutine(person, totalUnitDicts, data, itemId);
		getBloodRoutineCol(person, data, itemId,detail.getVisitType());

		//尿常规
//		getUrineRoutine(person, totalUnitDicts, data, itemId);
		getUrineRoutineCol(person, totalUnitDicts, data, itemId,detail.getVisitType());

		//便常规
//		getStoolRoutine(person, data, itemId);
		getStoolRoutineCol(person, data, itemId, detail.getVisitType());


		//常见传染病 乙肝 mp_infectious_diseases
//		getInfectiousDiseases(person, data, itemId);

		//凝血系列检查   mp_coagulation_function

		getCoagulationFunctionCol(person, data, itemId, detail.getVisitType());
//		getCoagulationFunction(person, totalUnitDicts, data, itemId);

		//  肝or肾
		//血液生化 mp_blood_biochemistry
//		getBloodBiochemistry(person, totalUnitDicts, data, itemId);
		getBloodGanOrShenCol(person, data, itemId,detail.getVisitType());

		//甲状腺功能
//		getThyroidFunction(person, totalUnitDicts, data, itemId); 血脂
		getBloodJiaCol(person, data, itemId,detail.getVisitType());

		//激素六项 mp_hormone_items
		getHormoneItems(person, totalUnitDicts, data, itemId);

		//肿瘤标志物
//		getTumorScreeningItems(person, totalUnitDicts, data, itemId);
		getTumorScreeningItemsCol(person, data, itemId, detail.getVisitType());
		//血气分析 mp_blood_biochemistry
//		getBloodGasAnalysis(person, totalUnitDicts, data, itemId);

		// 血糖成分
		getGlucoseToleranceCol(person, data, itemId, detail.getVisitType());
		// 糖耐量试验
//		getGlucoseTolerance(person, totalUnitDicts, data, itemId);

		return data;
	}

	/**
	 * 健康档案和体检档案 入口
	 * @param healthListSearchForm
	 * @return
	 */
	public HealthNodeVO index(HealthListSearchForm  healthListSearchForm) {
		// 通过patientId去查询personId
		if (StringUtils.isEmpty(healthListSearchForm.getPersonId())) {
			//主键查询
			PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(Long.parseLong(healthListSearchForm.getPatientId()));
			if(patientInfo == null || StringUtils.isEmpty(patientInfo.getPersonId())){
				log.info("patientId = {} 在 patient_info 表查不到信息或者personId为空");
				return null;
			}
			healthListSearchForm.setPersonId(patientInfo.getPersonId());
		}

		List<VisitListVO>  vistitList = new ArrayList<VisitListVO>();
		List<MpVisitOccurrence>  list =  mpVisitOccurrenceMapper.selectPatientHealthList(healthListSearchForm);
		if(!CollectionUtils.isEmpty(list)) {
			for (MpVisitOccurrence mpVisitOccurrence : list) {
				VisitListVO  visitListVO  = new VisitListVO();
				if(mpVisitOccurrence.getVisitStartDate()!=null) {
					visitListVO.setCheckDate(DateUtils.parseDateToStrShort(mpVisitOccurrence.getVisitStartDate()));
				}
				visitListVO.setItemName(HealthConstant.HEALTH_TYPE.get(healthListSearchForm.getType()));
				visitListVO.setPersonId(healthListSearchForm.getPersonId());
				visitListVO.setVisitId(mpVisitOccurrence.getVisitOccurrenceId());
				vistitList.add(visitListVO);
			}
		}

		String type = healthListSearchForm.getType();
		HealthNodeVO rootNode = ZtreeNodeUtils.getRootNode(type, healthListSearchForm.getPersonId());

		ZtreeNodeUtils.getVisitNode(rootNode,vistitList,type);
		return rootNode;
	}

	/**
	 * 健康档案或体检档案详情页面
	 * @param detail
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HealthDetailVO detail(DetailForm detail)  {
		HealthDetailVO  healthDetailVO = new  HealthDetailVO();

		MpVisitOccurrence  mpVisitOccurrence  =  mpVisitOccurrenceMapper.selectVisitByPersonIdAndVisitId(detail.getPersonId(),detail.getVisitId());
		if(mpVisitOccurrence == null) {
			return null;
		}

		MpPerson person = mpPersonMapper.getPersonByPersonId(detail.getPersonId());
		if(person == null) {
			return null;
		}


		String personId = detail.getPersonId();
		String visitId = detail.getVisitId();

		//一般检查& 基本信息
		GeneralInspectionVO  healthItemVO  =  getGeneralInspection(personId, visitId,person,mpVisitOccurrence,detail.getType());
		healthDetailVO.setGeneral(healthItemVO);
		//诊断
		if(HealthConstant.JK.equals(detail.getType())) {
			HealthItemVO   healthItemVO2 =  getDiagnosis(personId, visitId,null);
			healthDetailVO.setDiagnosis(healthItemVO2);
		}

		return healthDetailVO;

	}

	/**
	 * 检查项目列表
	 *
	 * @param detail
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<HealthItemVO> checkExamItemList(DetailForm detail) {
		// 通过patientId去查询personId
		if (StringUtils.isEmpty(detail.getPersonId())) {
			PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(Long.parseLong(detail.getPatientId()));
			if(patientInfo == null || StringUtils.isEmpty(patientInfo.getPersonId())){
				log.info("patientId = {} 在 patient_info 表查不到信息或者personId为空");
				return null;
			}
			detail.setPersonId(patientInfo.getPersonId());
		}

		String visitType = detail.getVisitType();
		if (StringUtils.isEmpty(visitType)) {
			visitType = getCheckVisitType(detail.getType());
		}

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

		// CT
		HealthItemVO healthItem6= getCtExam(personId, visitId,visitType);
		if(healthItem6 != null ) {
			list.add(healthItem6);
		}
		// 磁共振
		HealthItemVO healthItem7= getNuclearMagnetismExam(personId, visitId,visitType);
		if(healthItem7 != null ) {
			list.add(healthItem7);
		}
		// X线
		HealthItemVO healthItem8= getXRayExam(personId, visitId,visitType);
		if(healthItem8 != null ) {
			list.add(healthItem8);
		}
		// 超声
		HealthItemVO healthItem9= getUltrasonicExam(personId, visitId,visitType);
		if(healthItem9 != null ) {
			list.add(healthItem9);
		}
		// 心电图
		HealthItemVO healthItem10= getElectrocardiogramExam(personId, visitId,visitType);
		if(healthItem10 != null ) {
			list.add(healthItem10);
		}

		return list;
	}

	private String getCheckVisitType(String type) {
		if (StringUtils.isEmpty(type)) {
			return null;
		}
		if ("1".equals(type)) {
			//住院
			return "住院";
		}
		if ("2".equals(type)) {
			//体检
			return "体检";
		}
		//门诊
		return "门诊";
	}

	/**
	 * 检验项目列表
	 * @param personId
	 * @param visitId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<HealthItemVO> queryCheckTestItemList(DetailForm detail) {
		// 通过patientId去查询personId
		if (StringUtils.isEmpty(detail.getPersonId())) {
			PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(Long.parseLong(detail.getPatientId()));
			if(patientInfo == null || StringUtils.isEmpty(patientInfo.getPersonId())){
				log.info("patientId = {} 在 patient_info 表查不到信息或者personId为空");
				return null;
			}
			detail.setPersonId(patientInfo.getPersonId());
		}

		MpPerson person = mpPersonMapper.getPersonByPersonId(detail.getPersonId());

		String personId =detail.getPersonId();
		String visitId=detail.getVisitId();
		String type = detail.getType();
		String visitType = "2";
		if ("1".equals(type)) {
			//住院
			visitType = "1";
		}
		if ("2".equals(type)) {
			//体检
			visitType = "3";
		}
		List<HealthItemVO> list = new ArrayList<HealthItemVO>();
		List<MpUnitDict> totalUnitDicts = mpUnitDictMapper.queryUnitDictByTableName(null);
		//查询单位字典表
		if (CollectionUtils.isEmpty(totalUnitDicts)) {
			return null;
		}
		//血常规
		List<HealthItemVO>  healthItem1= getBloodRoutineCol(person, visitId, visitType, detail.getCheckDate());
		//排序
		if(!CollectionUtils.isEmpty(healthItem1)) {
			list.addAll(reverseSortedHealthItemVOS(healthItem1));
		}
		//尿常规
//		HealthItemVO  healthItem2= getUrineRoutine(person, visitId, totalUnitDicts);
		List<HealthItemVO>  healthItem2= getUrineRoutineCol(person, visitId,visitType,detail.getCheckDate());
		if(!CollectionUtils.isEmpty(healthItem2)) {
			list.addAll(reverseSortedHealthItemVOS(healthItem2));
		}
		//便常规
		List<HealthItemVO>  healthItem3= getStoolRoutineCol(person, visitId,visitType,detail.getCheckDate());
//		HealthItemVO  healthItem3= getStoolRoutine(person, visitId);
		if(!CollectionUtils.isEmpty(healthItem3)) {
			list.addAll(reverseSortedHealthItemVOS(healthItem3));
		}
		//常见传染病 乙肝 mp_infectious_diseases
//		HealthItemVO  healthItem6= getInfectiousDiseases(person, visitId);
//		if(healthItem6 != null ) {
//			list.add(healthItem6);
//		}
		//凝血系列检查   mp_coagulation_function
//		HealthItemVO  healthItem7= getCoagulationFunction(person, visitId, totalUnitDicts);
		List<HealthItemVO>  healthItem7= getCoagulationFunctionCol(person, visitId,visitType,detail.getCheckDate());
		if(!CollectionUtils.isEmpty(healthItem7)) {
			Map<String, List<HealthItemVO>> collect2 = healthItem7.stream()
					.collect(Collectors.groupingBy(healthItemVO -> healthItemVO.getItemName().split(" ")[1]));
			collect2.forEach((k,v)->{
				list.addAll(reverseSortedHealthItemVOS(v));
			});
		}
		//血液生化 mp_blood_biochemistry
//		HealthItemVO  healthItem8= getBloodBiochemistry(person, visitId, totalUnitDicts);
		List<HealthItemVO>  healthItem8= getBloodGanOrShenCol(person, visitId,visitType,detail.getCheckDate());
		if(!CollectionUtils.isEmpty(healthItem8)) {
			Map<String, List<HealthItemVO>> collect2 = healthItem8.stream()
					.collect(Collectors.groupingBy(healthItemVO -> healthItemVO.getItemName().split(" ")[1]));
			collect2.forEach((k,v)->{
				list.addAll(reverseSortedHealthItemVOS(v));
			});
		}
		//甲状腺功能
		List<HealthItemVO>  healthItem10= getBloodJiaCol(person, visitId,visitType,detail.getCheckDate());
		if(!CollectionUtils.isEmpty(healthItem10)) {
			Map<String, List<HealthItemVO>> collect2 = healthItem10.stream()
					.collect(Collectors.groupingBy(healthItemVO -> healthItemVO.getItemName().split(" ")[1]));
			collect2.forEach((k,v)->{
				list.addAll(reverseSortedHealthItemVOS(v));
			});
		}
//		HealthItemVO  healthItem10= getThyroidFunction(person, visitId, totalUnitDicts);
//		if(healthItem10 != null ) {
//			list.add(healthItem10);
//		}

		if(HealthConstant.JK.equals(detail.getType())) {
			//激素六项 mp_hormone_items
			HealthItemVO  healthItem11= getHormoneItems(person, visitId, totalUnitDicts);
			if(healthItem11 != null ) {
				list.add(healthItem11);
			}
			//肿瘤标志物
//			HealthItemVO  healthItem9= getTumorScreeningItems(person, visitId, totalUnitDicts);
//			if(healthItem9 != null ) {
//				list.add(healthItem9);
//			}
			List<HealthItemVO>  healthItem9= getTumorScreeningItemsCol(person, visitId,visitType,detail.getCheckDate());
			if(!CollectionUtils.isEmpty(healthItem9)) {
				Map<String, List<HealthItemVO>> collect2 = healthItem9.stream()
						.collect(Collectors.groupingBy(healthItemVO -> healthItemVO.getItemName().split(" ")[1]));
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

		//血糖
		List<HealthItemVO>  healthItem13= getMpGlucoseToleranceCol(person, visitId,visitType,detail.getCheckDate());
		if(!CollectionUtils.isEmpty(healthItem13)) {
			Map<String, List<HealthItemVO>> collect2 = healthItem13.stream()
					.collect(Collectors.groupingBy(healthItemVO -> healthItemVO.getItemName().split(" ")[1]));
			collect2.forEach((k,v)->{
				list.addAll(reverseSortedHealthItemVOS(v));
			});
		}

//		// 糖耐量试验
//		HealthItemVO  healthItem13= getMpGlucoseTolerance(person, visitId, totalUnitDicts);
//		if(healthItem13 != null ) {
//			list.add(healthItem13);
//		}

		//排序
//		List<HealthItemVO> collect = list.stream().sorted(Comparator.comparing(HealthItemVO::getItemName)).collect(Collectors.toList());
//		Collections.reverse(collect);

		return list;
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

	private String dealType(MpPerson person) {
		try {
			// 男，女，成人，儿童，新生儿，幼儿，4-14岁，婴儿
			String cardId = person.getCardId();
			String birthday = cardId.substring(6, 14);
			return null;
		} catch (Exception e) {
			log.error("e {}",e);
			return null;
		}
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
	 * 甲状腺功能
	 *
	 * @param person
	 * @param visitId
	 * @param totalUnitDicts
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
	private HealthItemVO<List<ItemValueVO>>  getBloodGasAnalysis(MpPerson person, String visitId, List<MpUnitDict> totalUnitDicts){
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
	 * 便常规
	 *
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
	 * 尿常规
	 *
	 * @param person
	 * @param visitId
	 * @param totalUnitDicts
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
	 * 尿常规
	 *
	 * @param person
	 * @param id
	 * @param visitId
	 * @return
	 */
	private  List<HealthItemVO> getUrineRoutineCol(MpPerson person, String visitId, String visitType,Date checkDate) {
		List<HealthItemVO> listH = new ArrayList<>();
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		if (StringUtils.isEmpty(personId)) {
			return null;
		}
		MpUrineRoutineCol mpUrineRoutineCol = new MpUrineRoutineCol();

		mpUrineRoutineCol.setPersonId(personId);
		mpUrineRoutineCol.setVisitType(visitType);
		mpUrineRoutineCol.setVisitOccurrenceId(visitId);
		// 1. 查出尿常规数据
		List<MpUrineRoutineCol> mpUrineRoutineCols = mpUrineRoutineColMapper.selectMpUrineRoutineColList(mpUrineRoutineCol);
//		if (Objects.nonNull(checkDate)) {
//			mpUrineRoutineCols = mpUrineRoutineCols.stream().filter(e->{
//				String s2 = DateUtils.dateTime(e.getResultTime());
//				if (DateUtils.dateTime(checkDate).equals(s2)) {
//					return true;
//				}
//				return false;=
//			}).collect(Collectors.toList());
//		}
		Map<String, List<MpUrineRoutineCol>> visitRecords = mpUrineRoutineCols.stream()
				.filter(key->!StringUtils.isEmpty(DateUtils.dateTime(key.getResultTime())))
				.collect(Collectors.groupingBy(key->DateUtils.dateTime(key.getResultTime())));
		visitRecords.forEach((k,v)->{
			HealthItemVO<List<ItemValueVO>> healthItemVO = new HealthItemVO<List<ItemValueVO>>();
			List<ItemValueVO> list = new ArrayList<ItemValueVO>();
			healthItemVO.setItemId(HealthPatientCheckTestEnum.URINE_ROUTINE.getCode());
			healthItemVO.setItemName(k + " " + HealthPatientCheckTestEnum.URINE_ROUTINE.getValue());
			for (MpUrineRoutineCol urineRoutineCol : v) {
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
		return listH;
	}
/**
	 * 血常规
	 *
	 * @param person
	 * @param id
	 * @param visitId
	 * @return
	 */
	private  List<HealthItemVO> getBloodRoutineCol(MpPerson person, String visitId, String visitType,Date checkDate) {
		List<HealthItemVO> listH = new ArrayList<>();
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		if (StringUtils.isEmpty(personId)) {
			return null;
		}
		MpBloodRoutineCol mpUrineRoutineCol = new MpBloodRoutineCol();

		mpUrineRoutineCol.setPersonId(personId);
		mpUrineRoutineCol.setVisitType(visitType);
		mpUrineRoutineCol.setVisitOccurrenceId(visitId);
		// 1. 查出尿常规数据
		List<MpBloodRoutineCol> mpUrineRoutineCols = mpBloodRoutineColMapper.selectMpBloodRoutineColList(mpUrineRoutineCol);
		Map<String, List<MpBloodRoutineCol>> visitRecords = mpUrineRoutineCols.stream().collect(Collectors.groupingBy(key->DateUtils.parseDateToStrFull(key.getResultTime())));
		visitRecords.forEach((k,v)->{
			HealthItemVO<List<ItemValueVO>> healthItemVO = new HealthItemVO<List<ItemValueVO>>();
			List<ItemValueVO> list = new ArrayList<ItemValueVO>();
			healthItemVO.setItemId(HealthPatientCheckTestEnum.BLOOD_ROUTINE.getCode());
			healthItemVO.setItemName(k.split(" ")[0] + " " + HealthPatientCheckTestEnum.BLOOD_ROUTINE.getValue());
			for (MpBloodRoutineCol urineRoutineCol : v) {
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
		return listH;
	}
/**
	 * 肝or肾
	 *
	 * @param person
	 * @param id
	 * @param visitId
	 * @return
	 */
	private  List<HealthItemVO> getBloodGanOrShenCol(MpPerson person, String visitId, String visitType,Date checkDate) {
		List<HealthItemVO> listH = new ArrayList<>();
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		if (StringUtils.isEmpty(personId)) {
			return null;
		}
		MpBloodBiochemistryCol mpUrineRoutineCol = new MpBloodBiochemistryCol();

		mpUrineRoutineCol.setPersonId(personId);
		mpUrineRoutineCol.setVisitType(visitType);
		mpUrineRoutineCol.setVisitOccurrenceId(visitId);
		// 1. 查出肝肾
		List<MpBloodBiochemistryCol> mpUrineRoutineCols = mpBloodBiochemistryColMapper.selectMpBloodBiochemistryColList(mpUrineRoutineCol);
		Map<String, List<MpBloodBiochemistryCol>> visitRecords = mpUrineRoutineCols.stream()
				.collect(Collectors.groupingBy(key->DateUtils.dateTime(key.getResultTime())));
		visitRecords.forEach((k,v)->{
			Map<String, List<MpBloodBiochemistryCol>> collect = v.stream().collect(Collectors.groupingBy(key -> key.getMeasurementType()));
			collect.forEach((type,ganOrShenObjs)->{
				HealthItemVO<List<ItemValueVO>> healthItemVO = new HealthItemVO<List<ItemValueVO>>();
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				healthItemVO.setItemId(HealthPatientCheckTestEnum.BLOOD_BIOCHEMISTRY.getCode());
				healthItemVO.setItemName(k + " " + type);
				for (MpBloodBiochemistryCol urineRoutineCol : ganOrShenObjs) {
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

/**
	 * 甲功_or其他数据?
	 *
	 * @param person
	 * @param id
	 * @param visitId
	 * @return
	 */
	private  List<HealthItemVO> getBloodJiaCol(MpPerson person, String visitId, String visitType,Date checkDate) {
		List<HealthItemVO> listH = new ArrayList<>();
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		if (StringUtils.isEmpty(personId)) {
			return null;
		}
		MpThyroidFunctionCol mpUrineRoutineCol = new MpThyroidFunctionCol();

		mpUrineRoutineCol.setPersonId(personId);
		mpUrineRoutineCol.setVisitType(visitType);
		mpUrineRoutineCol.setVisitOccurrenceId(visitId);
		// 1. 查出肝肾
		List<MpThyroidFunctionCol> mpUrineRoutineCols = mpThyroidFunctionColMapper.selectMpThyroidFunctionColList(mpUrineRoutineCol);
		Map<String, List<MpThyroidFunctionCol>> visitRecords = mpUrineRoutineCols.stream()
				.collect(Collectors.groupingBy(key->DateUtils.dateTime(key.getResultTime())));
		visitRecords.forEach((k,v)->{
			// 甲功 -- [...]
			// 感染八项 -- [...]
			Map<String, List<MpThyroidFunctionCol>> collect = v.stream().collect(Collectors.groupingBy(key -> key.getMeasurementType()));
			collect.forEach((type,ganOrShenObjs)->{
				HealthItemVO<List<ItemValueVO>> healthItemVO = new HealthItemVO<List<ItemValueVO>>();
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				healthItemVO.setItemId(HealthPatientCheckTestEnum.THYROID_FUNCTION.getCode());
				healthItemVO.setItemName(k + " " + type);
				for (MpThyroidFunctionCol urineRoutineCol : ganOrShenObjs) {
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
				if (type.contains("感染八项")) {
					List<ItemValueVO> listCopy = new ArrayList<ItemValueVO>();
					Map<String, List<ItemValueVO>> name = list.stream().collect(Collectors.groupingBy(ItemValueVO::getName));
					listCopy.addAll(name.get("乙肝表面抗原定量"));
					listCopy.addAll(name.get("乙肝表面抗体定量"));
					listCopy.addAll(name.get("乙肝e抗原定量"));
					listCopy.addAll(name.get("乙肝e抗体定量"));
					listCopy.addAll(name.get("乙肝核心抗体定量"));
					listCopy.addAll(name.get("丙型肝炎抗体定量"));
					listCopy.addAll(name.get("人免疫缺陷病毒抗原抗体"));
					listCopy.addAll(name.get("梅毒抗体定量"));
					if (!CollectionUtils.isEmpty(listCopy)) {
						healthItemVO.setObj(listCopy);
						listH.add(healthItemVO);
					}
				}else {
					if (!CollectionUtils.isEmpty(list)) {
						healthItemVO.setObj(list);
						listH.add(healthItemVO);
					}
				}
			});
		});
		return listH;
	}

	/**
	 * 便常规 健康&体检
	 *
	 * @param person
	 * @param id
	 * @param visitId
	 * @return
	 */
	private  List<HealthItemVO> getStoolRoutineCol(MpPerson person, String visitId, String visitType,Date checkDate) {
		List<HealthItemVO> listH = new ArrayList<>();
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		if (StringUtils.isEmpty(personId)) {
			return null;
		}
		MpStoolRoutineCol mpStoolRoutineCol = new MpStoolRoutineCol();

		mpStoolRoutineCol.setPersonId(personId);
		mpStoolRoutineCol.setVisitType(visitType);
		mpStoolRoutineCol.setVisitOccurrenceId(visitId);
		// 1. 查出尿常规数据
		List<MpStoolRoutineCol> mpUrineRoutineCols = mpStoolRoutineColMapper.selectMpStoolRoutineColList(mpStoolRoutineCol);
//		if (Objects.nonNull(checkDate)) {
//			mpUrineRoutineCols = mpUrineRoutineCols.stream().filter(e->{
//				String s2 = DateUtils.dateTime(e.getResultTime());
//				if (DateUtils.dateTime(checkDate).equals(s2)) {
//					return true;
//				}
//				return false;
//			}).collect(Collectors.toList());
//		}
		Map<String, List<MpStoolRoutineCol>> visitRecords = mpUrineRoutineCols.stream().collect(Collectors.groupingBy(key->DateUtils.dateTime(key.getResultTime())));
		visitRecords.forEach((k,v)->{
			HealthItemVO<List<ItemValueVO>> healthItemVO =new HealthItemVO<List<ItemValueVO>>();
			List<ItemValueVO> list = new ArrayList<ItemValueVO>();
			healthItemVO.setItemId(HealthPatientCheckTestEnum.STOOL_ROUTINE.getCode());
			healthItemVO.setItemName(k + " " + HealthPatientCheckTestEnum.STOOL_ROUTINE.getValue());
			for (MpStoolRoutineCol urineRoutineCol : v) {
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
		return listH;
	}

	/**
	 * 凝血 健康&体检
	 *
	 * @param person
	 * @param id
	 * @param visitId
	 * @return
	 */
	private  List<HealthItemVO> getCoagulationFunctionCol(MpPerson person, String visitId, String visitType,Date checkDate) {
		List<HealthItemVO> listH = new ArrayList<>();
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		if (StringUtils.isEmpty(personId)) {
			return null;
		}
		MpCoagulationFunctionCol mpStoolRoutineCol = new MpCoagulationFunctionCol();

		mpStoolRoutineCol.setPersonId(personId);
		mpStoolRoutineCol.setVisitType(visitType);
		mpStoolRoutineCol.setVisitOccurrenceId(visitId);
		// 1. 查出尿常规数据
		List<MpCoagulationFunctionCol> mpUrineRoutineCols = mpCoagulationFunctionColMapper.selectMpCoagulationFunctionColList(mpStoolRoutineCol);
//		if (Objects.nonNull(checkDate)) {
//			mpUrineRoutineCols = mpUrineRoutineCols.stream().filter(e->{
//				String s2 = DateUtils.dateTime(e.getResultTime());
//				if (DateUtils.dateTime(checkDate).equals(s2)) {
//					return true;
//				}
//				return false;
//			}).collect(Collectors.toList());
//		}
		Map<String, List<MpCoagulationFunctionCol>> visitRecords = mpUrineRoutineCols.stream()
				.filter(x->!StringUtils.isEmpty(DateUtils.dateTime(x.getResultTime())))
				.collect(Collectors.groupingBy(key->DateUtils.parseDateToStrFull(key.getResultTime())));
		visitRecords.forEach((k,v)->{
					Map<String, List<MpCoagulationFunctionCol>> collect = v.stream().collect(Collectors.groupingBy(key -> key.getMeasurementType()));
					collect.forEach((type,ganOrShenObjs)->{
			HealthItemVO<List<ItemValueVO>> healthItemVO =new HealthItemVO<List<ItemValueVO>>();
			List<ItemValueVO> list = new ArrayList<ItemValueVO>();
			healthItemVO.setItemId(HealthPatientCheckTestEnum.COAGULATION_FOUR.getCode());
			healthItemVO.setItemName(k.split(" ")[0] + " " + type);
			for (MpCoagulationFunctionCol urineRoutineCol : ganOrShenObjs) {
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
	/**
	 * 凝血 健康&体检
	 *
	 * @param person
	 * @param id
	 * @param visitId
	 * @return
	 */
	private  List<HealthItemVO> getMpGlucoseToleranceCol(MpPerson person, String visitId, String visitType,Date checkDate) {
		List<HealthItemVO> listH = new ArrayList<>();
		String personId = person.getPersonId();
		if (StringUtils.isEmpty(personId)) {
			return null;
		}
		MpGlucoseToleranceCol mpStoolRoutineCol = new MpGlucoseToleranceCol();

		mpStoolRoutineCol.setPersonId(personId);
		mpStoolRoutineCol.setVisitType(visitType);
		mpStoolRoutineCol.setVisitOccurrenceId(visitId);
		// 1. 查出尿常规数据
		List<MpGlucoseToleranceCol> mpUrineRoutineCols = mpGlucoseToleranceColMapper.selectMpGlucoseToleranceColList(mpStoolRoutineCol);
//		if (Objects.nonNull(checkDate)) {
//			mpUrineRoutineCols = mpUrineRoutineCols.stream().filter(e->{
//				String s2 = DateUtils.dateTime(e.getResultTime());
//				if (DateUtils.dateTime(checkDate).equals(s2)) {
//					return true;
//				}
//				return false;
//			}).collect(Collectors.toList());
//		}
		Map<String, List<MpGlucoseToleranceCol>> visitRecords = mpUrineRoutineCols.stream()
				.filter(x->!StringUtils.isEmpty(DateUtils.dateTime(x.getResultTime())))
				.collect(Collectors.groupingBy(key->DateUtils.parseDateToStrFull(key.getResultTime())));
		visitRecords.forEach((k/*时间*/,v)->{
					Map<String, List<MpGlucoseToleranceCol>> collect = v.stream().collect(Collectors.groupingBy(key -> key.getMeasurementType()));
					collect.forEach((type,ganOrShenObjs)->{
			HealthItemVO<List<ItemValueVO>> healthItemVO =new HealthItemVO<List<ItemValueVO>>();
			List<ItemValueVO> list = new ArrayList<ItemValueVO>();
			healthItemVO.setItemId(HealthPatientCheckTestEnum.COAGULATION_FOUR.getCode());
			healthItemVO.setItemName(k.split(" ")[0] + " " + type);
			for (MpGlucoseToleranceCol urineRoutineCol : ganOrShenObjs) {
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

	/**
	 * 肿瘤 健康&体检
	 *
	 * @param person
	 * @param id
	 * @param visitId
	 * @return
	 */
	private  List<HealthItemVO> getTumorScreeningItemsCol(MpPerson person, String visitId, String visitType,Date checkDate) {
		List<HealthItemVO> listH = new ArrayList<>();
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		if (StringUtils.isEmpty(personId)) {
			return null;
		}
		MpTumorScreeningItemsCol mpStoolRoutineCol = new MpTumorScreeningItemsCol();

		mpStoolRoutineCol.setPersonId(personId);
		mpStoolRoutineCol.setVisitType(visitType);
		mpStoolRoutineCol.setVisitOccurrenceId(visitId);
		// 1. 查出尿常规数据
		List<MpTumorScreeningItemsCol> mpUrineRoutineCols = mpTumorScreeningItemsColMapper.selectMpTumorScreeningItemsColList(mpStoolRoutineCol);
//		if (Objects.nonNull(checkDate)) {
//			mpUrineRoutineCols = mpUrineRoutineCols.stream().filter(e->{
//				String s2 = DateUtils.dateTime(e.getResultTime());
//				if (DateUtils.dateTime(checkDate).equals(s2)) {
//					return true;
//				}
//				return false;
//			}).collect(Collectors.toList());
//		}
		Map<String, List<MpTumorScreeningItemsCol>> visitRecords = mpUrineRoutineCols.stream().collect(Collectors.groupingBy(key->DateUtils.dateTime(key.getResultTime())));
		visitRecords.forEach((k,v)->{
					Map<String, List<MpTumorScreeningItemsCol>> collect = v.stream().collect(Collectors.groupingBy(key -> key.getMeasurementType()));
					collect.forEach((type,ganOrShenObjs)->{
			HealthItemVO<List<ItemValueVO>> healthItemVO =new HealthItemVO<List<ItemValueVO>>();
			List<ItemValueVO> list = new ArrayList<ItemValueVO>();
			healthItemVO.setItemId(HealthPatientCheckTestEnum.TUMOR_SCREENING_MEAL.getCode());
			healthItemVO.setItemName(k + " " + type);
			for (MpTumorScreeningItemsCol urineRoutineCol : ganOrShenObjs) {
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
		});});
		return listH;
	}

	/**
	 * 血常规
	 *
	 * @param person
	 * @param visitId
	 * @param totalUnitDicts
	 * @return
	 */
	private  HealthItemVO<List<ItemValueVO>> getBloodRoutine(MpPerson person, String visitId, List<MpUnitDict> totalUnitDicts) {
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		MpBloodRoutine mpdb = mpBloodRoutineMapper.selectByPersonIdAndVisitId(personId, visitId);
		if(mpdb != null) {
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId(HealthPatientCheckTestEnum.BLOOD_ROUTINE.getCode());
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
			ItemValueVO itemValueVO14 = setItemValueVO("体检日期时",healthExamDate, list);
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
			ItemValueVO itemValueVO14 = setItemValueVO("体检日期时",healthExamDate, list);
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
			ItemValueVO itemValueVO14 = setItemValueVO("体检日期时",healthExamDate, list);
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
			ItemValueVO itemValueVO14 = setItemValueVO("体检日期时",healthExamDate, list);
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
   private GeneralInspectionVO getGeneralInspection(String personId, String visitId, MpPerson person, MpVisitOccurrence mpVisitOccurrence, String type) {
	   GeneralInspectionVO   generalInspectionVO  = new GeneralInspectionVO();
	   String  idCard = person.getCardId();
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
//	   MpGeneralInspection  mpGeneralInspection  = mpGeneralInspectionMapper.selectByPersonIdAndVisitList(personId, visitId);
	   List<MpGeneralInspection> mpGeneralInspectionList  = mpGeneralInspectionMapper.selectByPersonIdAndVisitList(personId, visitId);

	   if(!CollectionUtils.isEmpty(mpGeneralInspectionList)) {
			// mpGeneralInspection != null 获取第一条
		   MpGeneralInspection mpGeneralInspection = mpGeneralInspectionList.stream().sorted(Comparator.comparing(MpGeneralInspection::getResultTime).reversed()).findFirst().get();

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


		   return generalInspectionVO;

	   }
       return null;
   }


	/**
    * 设置每个子项
    * @param n
    * @param v
    * @return
    */
  /* private ItemValueVO  setItemValueVO(String n,String v) {
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

	private ItemValueVO  setItemValueVOHref(String n, String href, List<ItemValueVO> list) {
		if(StringUtils.isEmpty(href)){
			return null;
		}
		ItemValueVO  itemValueVO = new ItemValueVO();
		itemValueVO.setName(n);
		itemValueVO.setHref(href);
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
   /*private ItemValueVO  setItemValueVO(String name,String v,String key, Map<String, String> minValueMap, Map<String, String> maxValueMap, Map<String, String> unitMap, list) {
	   ItemValueVO  itemValueVO = new ItemValueVO();
	   itemValueVO.setName(name);
	   itemValueVO.setValue(v);
	   itemValueVO.setMaxValue(maxValueMap.get(key));
	   itemValueVO.setMinValue(minValueMap.get(key));
	   itemValueVO.setUnit(unitMap.get(key));
	   return itemValueVO;

   }*/
   public MpPerson queryBaseInfo(DetailForm detail) {
	   // 通过patientId去查询personId
	   if (StringUtils.isEmpty(detail.getPersonId())) {
		   PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(Long.parseLong(detail.getPatientId()));
		   if(patientInfo == null || StringUtils.isEmpty(patientInfo.getPersonId())){
			   log.info("patientId = {} 在 patient_info 表查不到信息或者personId为空");
			   return null;
		   }
		   detail.setPersonId(patientInfo.getPersonId());
	   }

	   MpPerson person = mpPersonMapper.getPersonByPersonId(detail.getPersonId());
	   if(person == null) {
		   return null;
	   }
	   person.setSex(HealthUtils.getSexName(person.getCardId()));
	   return person;
   }

	public GeneralInspectionVO queryGeneralInfo(DetailForm detail) {
		// 通过patientId去查询personId
		if (StringUtils.isEmpty(detail.getPersonId())) {
			PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(Long.parseLong(detail.getPatientId()));
			if(patientInfo == null || StringUtils.isEmpty(patientInfo.getPersonId())){
				log.info("patientId = {} 在 patient_info 表查不到信息或者personId为空");
				return null;
			}
			detail.setPersonId(patientInfo.getPersonId());
		}

		MpVisitOccurrence  mpVisitOccurrence  =  mpVisitOccurrenceMapper.selectVisitByPersonIdAndVisitId(detail.getPersonId(),detail.getVisitId());
		if(mpVisitOccurrence == null) {
			return null;
		}

		MpPerson person = mpPersonMapper.getPersonByPersonId(detail.getPersonId());
		if(person == null) {
			return null;
		}

		String personId = detail.getPersonId();
		String visitId = detail.getVisitId();

		//一般检查& 基本信息
		GeneralInspectionVO healthItemVO = getGeneralInspection(personId, visitId,person,mpVisitOccurrence,detail.getType());
		return healthItemVO;
	}

	public HealthItemVO queryDiagnosis(DetailForm detail) {
		// 通过patientId去查询personId
		if (StringUtils.isEmpty(detail.getPersonId())) {
			PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(Long.parseLong(detail.getPatientId()));
			if(patientInfo == null || StringUtils.isEmpty(patientInfo.getPersonId())){
				log.info("patientId = {} 在 patient_info 表查不到信息或者personId为空");
				return null;
			}
			detail.setPersonId(patientInfo.getPersonId());
		}
		//诊断
		if(HealthConstant.JK.equals(detail.getType())) {
			HealthItemVO healthItemVO =  getDiagnosis(detail.getPersonId(), detail.getVisitId(),null);
			return healthItemVO;
		}
		log.warn("非健康档案没有诊断");
		return null;
	}

	/*private ItemValueVO  setItemValueVO(String name,String v,String key, Map<String, String> minValueMap, Map<String, String> maxValueMap, Map<String, String> unitMap, List<ItemValueVO> list) {
        if(StringUtils.isEmpty(v)){
            return null;
        }
        ItemValueVO  itemValueVO = new ItemValueVO();
        itemValueVO.setName(name);
        itemValueVO.setValue(v);
        itemValueVO.setMaxValue(maxValueMap.get(key));
        itemValueVO.setMinValue(minValueMap.get(key));
        itemValueVO.setUnit(unitMap.get(key));
        list.add(itemValueVO);
        return itemValueVO;

    }*/
	private ItemValueVO  setItemValueVO(String name, String v, String key, Map<String, List<MpUnitDict>> unitDictMap, List<ItemValueVO> list) {
		if(StringUtils.isEmpty(v)){
			return null;
		}
		ItemValueVO  itemValueVO = new ItemValueVO();
		itemValueVO.setName(name);
		itemValueVO.setValue(v);

		String sex = ThreadLocalUtils.get("sex");
		String ageType = ThreadLocalUtils.get("ageType");

		List<MpUnitDict> mpUnitDicts = unitDictMap.get(key); //todo 大血小板比例
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

	private HealthItemVO getCtExam(String personId, String visitId, String visitType) {
		List<MpCtExam> mpCtExams = mpCtExamMapper.selectMpCtExamList(personId, visitId, visitType);
		if(!CollectionUtils.isEmpty(mpCtExams)) {
			HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
			healthItemVO.setItemId(HealthPatientCheckExamEnum.CT_EXAM.getCode());
			healthItemVO.setItemName("CT");
			List<CatalogVO> result = new ArrayList<>();
			Map<String, List<MpCtExam>> mpCtExamList = mpCtExams.stream().collect(Collectors.groupingBy(ct->DateUtils.dateTime(ct.getReportDate())));
			mpCtExamList.forEach((reportDate,ctList)->{
				Map<String, List<MpCtExam>> testNoMpCtExamList = ctList.stream().filter(x->!StringUtils.isEmpty(x.getTestNo()))
						.collect(Collectors.groupingBy(MpCtExam::getTestNo));
				List<MpCtExam> twoCtList = new ArrayList<>();
				// 同天同号
				testNoMpCtExamList.forEach((testNo,mpCtExam)->{
					MpCtExam ctExam = new MpCtExam();
					String title = mpCtExam.stream().map(ct->ct.getCheckSourceType()).distinct().collect(Collectors.joining("、"));
					ctExam.setCheckSourceType(title);
					ctExam.setSubjectiveHints(mpCtExam.get(0).getSubjectiveHints());
					ctExam.setObjectiveView(mpCtExam.get(0).getObjectiveView());

					ctExam.setDepartmentName(mpCtExam.get(0).getDepartmentName());
					ctExam.setTestNo(testNo);
					String objectiveView = mpCtExam.get(0).getObjectiveView();
					String subjectiveHints = mpCtExam.get(0).getSubjectiveHints();
					//md5 主观客观任意有值参与计算
					if (!StringUtils.isEmpty(objectiveView)||!StringUtils.isEmpty(subjectiveHints)){
						String objectiveMD5 = MD5Util.encode(StringUtils.trim(objectiveView)+StringUtils.trim(subjectiveHints));
						ctExam.setObjectiveViewMD5(objectiveMD5);
					}
					ctExam.setReportDate(mpCtExam.get(0).getReportDate());
					twoCtList.add(ctExam);
				});
				// 同天同内容, 最终处理
				Map<String, List<MpCtExam>> viewCtList = twoCtList.stream()
						.filter(x -> !StringUtils.isEmpty(x.getObjectiveViewMD5()))
						.collect(Collectors.groupingBy(MpCtExam::getObjectiveViewMD5));
				viewCtList.forEach((md5,mpCtExam)->{
					String title = mpCtExam.stream().map(MpCtExam::getCheckSourceType).distinct().collect(Collectors.joining("、"));
					CatalogVO catalogVO = new CatalogVO();
					List<ItemValueVO> list = new ArrayList<ItemValueVO>();
					setItemValueVO("检查项目",title,list);
//				setItemValueVO("身体部位",mpCtExam.getSctPosition(),list);
//				setItemValueVO("检查方式",mpCtExam.getTestMethod(),list);
					setItemValueVO("所见",mpCtExam.get(0).getObjectiveView(),list);
					setItemValueVO("诊断意见",mpCtExam.get(0).getSubjectiveHints(),list);
//				setItemValueVO("检查诊断",mpCtExam.getRelevantClinicDiagnosis(),list);
//				setItemValueVO("检查时间",DateUtils.dateTime(mpCtExam.getTechnologyDate()),list);
//				setItemValueVO("报告时间",DateUtils.dateTime(mpCtExam.getReportDate()),list);
					String deptName = mpCtExam.stream().map(MpCtExam::getDepartmentName).distinct().collect(Collectors.joining("、"));
					setItemValueVO("开单科室",getRuleDeptName(deptName), list);
					catalogVO.setItemTitle(DateUtils.dateTime(mpCtExam.get(0).getReportDate())+" "+title);
					catalogVO.setItemList(list);
					result.add(catalogVO);
				});
//				twoCtList.stream().filter(x->StringUtils.isEmpty(x.getObjectiveViewMD5())).forEach(ct->{
//					CatalogVO catalogVO = new CatalogVO();
//					List<ItemValueVO> list = new ArrayList<ItemValueVO>();
//					setItemValueVO("检查项目",ct.getCheckSourceType(),list);
//					setItemValueVO("所见",ct.getObjectiveView(),list);
//					setItemValueVO("诊断意见",ct.getSubjectiveHints(),list);
//					setItemValueVO("开单科室",ct.getDepartmentName(),list);
//					catalogVO.setItemList(list);
//					catalogVO.setItemTitle(DateUtils.dateTime(ct.getReportDate())+" "+ct.getCheckSourceType());
//					result.add(catalogVO);
//				});
			});
			healthItemVO.setArray(result);
			return healthItemVO;
		}
		return null;
	}

	private HealthItemVO getNuclearMagnetismExam(String personId, String visitId, String visitType) {
		List<MpNuclearMagnetismExam> mpNuclearMagnetismExams = mpNuclearMagnetismExamMapper.selectMpNuclearMagnetismExamList(personId, visitId, visitType);
		if(!CollectionUtils.isEmpty(mpNuclearMagnetismExams)) {
			HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
			healthItemVO.setItemId(HealthPatientCheckExamEnum.NUCLEAR_MAGNETISM_EXAM.getCode());
			healthItemVO.setItemName("磁共振");
			List<CatalogVO> result = new ArrayList<>();
			Map<String, List<MpNuclearMagnetismExam>> mpCtExamList = mpNuclearMagnetismExams.stream().filter(x->!StringUtils.isEmpty(x.getTestNo())).collect(Collectors.groupingBy(MpNuclearMagnetismExam::getTestNo));
			List<MpNuclearMagnetismExam> twoList = new ArrayList<>();
			// 降维
			mpCtExamList.forEach((testNo,mpNuclearMagnetismExamList)->{
				MpNuclearMagnetismExam two = new MpNuclearMagnetismExam();
				MpNuclearMagnetismExam mpNuclearMagnetismExam = mpNuclearMagnetismExamList.get(0);
				BeanUtils.copyProperties(mpNuclearMagnetismExam, two);

				String title = mpNuclearMagnetismExamList.stream().map(MpNuclearMagnetismExam::getCheckSourceType)
						.distinct().collect(Collectors.joining("、"));
				two.setCheckSourceType(title);
				two.setTechnologyName(title);
				String objectiveView = mpNuclearMagnetismExam.getObjectiveView();
				String subjectiveHints = mpNuclearMagnetismExam.getSubjectiveHints();
				if (!StringUtils.isEmpty(objectiveView) || !StringUtils.isEmpty(subjectiveHints)) {
					String encode = MD5Util.encode(StringUtils.trim(objectiveView) + StringUtils.isEmpty(subjectiveHints));
					two.setViewMD5(encode);
				}
				twoList.add(two);
			});
			Map<String, List<MpNuclearMagnetismExam>> collect = twoList.stream()
					.filter(x -> !StringUtils.isEmpty(x.getViewMD5()))
					.collect(Collectors.groupingBy(MpNuclearMagnetismExam::getViewMD5));
			collect.forEach((md5,mpNuclearMagnetismExamList)->{
				MpNuclearMagnetismExam mpNuclearMagnetismExam = mpNuclearMagnetismExamList.get(0);
				String title = mpNuclearMagnetismExamList.stream().map(MpNuclearMagnetismExam::getCheckSourceType)
						.collect(Collectors.joining("、"));
				CatalogVO catalogVO = new CatalogVO();
				catalogVO.setItemTitle(DateUtils.dateTime(mpNuclearMagnetismExam.getReportDate())+" "+title);
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				setItemValueVO("检查项目",title,list);
//				setItemValueVO("身体部位",mpNuclearMagnetismExam.getSctPosition(),list);
//				setItemValueVO("检查方式",mpNuclearMagnetismExam.getTestMethod(),list);
				setItemValueVO("所见", mpNuclearMagnetismExam.getObjectiveView(),list);
				setItemValueVO("诊断意见", mpNuclearMagnetismExam.getSubjectiveHints(),list);
//				setItemValueVO("检查诊断",mpNuclearMagnetismExam.getRelevantClinicDiagnosis(),list);
//				setItemValueVO("检查时间",DateUtils.dateTime(mpNuclearMagnetismExam.getTechnologyDate()),list);
//				setItemValueVO("报告时间",DateUtils.dateTime(mpNuclearMagnetismExam.getReportDate()),list);
				String deptName = mpNuclearMagnetismExamList.stream().map(x -> x.getDepartmentName()).distinct().collect(Collectors.joining("、"));
				setItemValueVO("开单科室",getRuleDeptName(deptName),list);
				catalogVO.setItemList(list);
				result.add(catalogVO);
			});
			healthItemVO.setArray(result);
			return healthItemVO;
		}
		return null;
	}

	private HealthItemVO getXRayExam(String personId, String visitId, String visitType) {
		List<MpXRayExam> mpXRayExams = mpXRayExamMapper.selectMpXRayExamList(personId, visitId,visitType);
		if(!CollectionUtils.isEmpty(mpXRayExams)) {
			HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
			healthItemVO.setItemId(HealthPatientCheckExamEnum.X_RAY_EXAM.getCode());
//			healthItemVO.setItemName(HealthPatientCheckExamEnum.X_RAY_EXAM.getValue());
			healthItemVO.setItemName("X片");

			Map<String, List<MpXRayExam>> collect = mpXRayExams.stream().distinct().filter(x->!StringUtils.isEmpty(x.getTestNo())).collect(Collectors.groupingBy(MpXRayExam::getTestNo));
			List<MpXRayExam> twoList = new ArrayList<>();
			collect.forEach((testNo,mpXRayExamList)->{
				MpXRayExam two = new MpXRayExam();
				MpXRayExam mpXRayExam = mpXRayExamList.get(0);
				BeanUtils.copyProperties(mpXRayExam, two);
				String title = mpXRayExamList.stream().map(MpXRayExam::getCheckSourceType)
						.distinct().collect(Collectors.joining("、"));
				two.setCheckSourceType(title);
				two.setTechnologyName(title);
				String objectiveView = mpXRayExam.getObjectiveView();
				String subjectiveHints = mpXRayExam.getSubjectiveHints();
				if (!StringUtils.isEmpty(objectiveView)|| !StringUtils.isEmpty(subjectiveHints)) {
					String encode = MD5Util.encode(StringUtils.trim(objectiveView) + StringUtils.trim(subjectiveHints));
					two.setViewMd5(encode);
				}
				twoList.add(two);
		});
			Map<String, List<MpXRayExam>> collect1 = twoList.stream()
					.filter(x -> !StringUtils.isEmpty(x.getViewMd5()))
					.collect(Collectors.groupingBy(MpXRayExam::getViewMd5));
			List<CatalogVO> result = new ArrayList<>();
			collect1.forEach((md5,mpXRayExamList)->{
				MpXRayExam mpXRayExam = mpXRayExamList.get(0);
				String title = mpXRayExamList.stream().map(MpXRayExam::getCheckSourceType).distinct()
						.collect(Collectors.joining("、"));
				CatalogVO catalogVO = new CatalogVO();
				catalogVO.setItemTitle(DateUtils.dateTime(mpXRayExam.getReportDate())+" "+title);
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				setItemValueVO("检查项目",title,list);
//				setItemValueVO("身体部位",mpXRayExam.getSctPosition(),list);
//				setItemValueVO("检查方式",mpXRayExam.getTestMethod(),list);
				setItemValueVO("所见",mpXRayExam.getObjectiveView(),list);
				setItemValueVO("诊断意见",mpXRayExam.getSubjectiveHints(),list);
//				setItemValueVO("检查诊断",mpXRayExam.getRelevantClinicDiagnosis(),list);
//				setItemValueVO("检查时间",DateUtils.dateTime(mpXRayExam.getTechnologyDate()),list);
//				setItemValueVO("报告时间",DateUtils.dateTime(mpXRayExam.getReportDate()),list);
				String deptName = mpXRayExamList.stream().map(x -> x.getDepartmentName()).distinct().collect(Collectors.joining("、"));
				// dept.equals("null") ? distinct的转义bug??
				setItemValueVO("开单科室", getRuleDeptName(deptName), list);
				catalogVO.setItemList(list);
				result.add(catalogVO);
			});

			healthItemVO.setArray(result);
			return healthItemVO;
		}
		return null;
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

	private HealthItemVO getUltrasonicExam(String personId, String visitId, String visitType) {
		List<MpUltrasonicExam> mpUltrasonicExams = mpUltrasonicExamMapper.selectMpUltrasonicExamList(personId, visitId, visitType);
		if(!CollectionUtils.isEmpty(mpUltrasonicExams)) {
			HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
			healthItemVO.setItemId(HealthPatientCheckExamEnum.ULTRASONIC_EXAM.getCode());
			healthItemVO.setItemName("超声");
			List<CatalogVO> result = new ArrayList<>();
			Map<String, List<MpUltrasonicExam>> mpCtExamList = mpUltrasonicExams.stream()
					.filter(x->!StringUtils.isEmpty(x.getObjectiveView())).filter(x->!StringUtils.isEmpty(x.getTestNo()))
					.collect(Collectors.groupingBy(MpUltrasonicExam::getTestNo));
			List<MpUltrasonicExam> twoList 	= new ArrayList<>();
			mpCtExamList.forEach((testNo,mpUltrasonicExam)->{
				MpUltrasonicExam two = new MpUltrasonicExam();
				MpUltrasonicExam mpUltrasonicExam1 = mpUltrasonicExam.get(0);
				BeanUtils.copyProperties(mpUltrasonicExam1, two);

				String title = mpUltrasonicExam.stream().map(ct->ct.getCheckSourceType()).distinct().collect(Collectors.joining("、"));
				two.setTechnologyName(title);
				two.setCheckSourceType(title);
				if (!StringUtils.isEmpty(mpUltrasonicExam1.getObjectiveView()) || !StringUtils.isEmpty(mpUltrasonicExam1.getSubjectiveHints())) {
					two.setViewMD5(MD5Util.encode(StringUtils.trim(mpUltrasonicExam1.getObjectiveView()) + StringUtils.isEmpty(mpUltrasonicExam1.getSubjectiveHints())));
				}
				twoList.add(two);
			});
			twoList.stream().filter(x->!StringUtils.isEmpty(x.getViewMD5())).collect(Collectors.groupingBy(MpUltrasonicExam::getViewMD5)).forEach((viewMD5,mpUltrasonicExam)->{
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

	private HealthItemVO getElectrocardiogramExam(String personId, String visitId, String visitType) {
		List<MpElectrocardiogramExam> mpElectrocardiogramExams = mpElectrocardiogramExamMapper.selectMpElectrocardiogramExamList(personId, visitId, visitType);
		if(!CollectionUtils.isEmpty(mpElectrocardiogramExams)) {
			HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
			healthItemVO.setItemId(HealthPatientCheckExamEnum.ECG.getCode());
//			healthItemVO.setItemName(HealthPatientCheckExamEnum.ECG.getValue());
			healthItemVO.setItemName("心电图");

			Map<String, List<MpElectrocardiogramExam>> collect = mpElectrocardiogramExams.stream().distinct().filter(x->!StringUtils.isEmpty(x.getTestNo()))
					.collect(Collectors.groupingBy(MpElectrocardiogramExam::getTestNo));
			List<MpElectrocardiogramExam> twoList = new ArrayList<>();
			collect.forEach((testNo,mpElectrocardiogramExam)->{
				MpElectrocardiogramExam two = new MpElectrocardiogramExam();
				MpElectrocardiogramExam mpElectrocardiogramExam1 = mpElectrocardiogramExam.get(0);
				BeanUtils.copyProperties(mpElectrocardiogramExam1, two);
				if (!StringUtils.isEmpty(mpElectrocardiogramExam1.getObjectiveView()) || !StringUtils.isEmpty(mpElectrocardiogramExam1.getSubjectiveHints())) {
					two.setViewMd5(MD5Util.encode(StringUtils.trim(mpElectrocardiogramExam1.getObjectiveView()) + StringUtils.isEmpty(mpElectrocardiogramExam1.getSubjectiveHints())));
				}
				two.setCheckSourceType(mpElectrocardiogramExam.stream().map(x -> x.getCheckSourceType()).distinct().collect(Collectors.joining("、")));
				two.setTechnologyName(mpElectrocardiogramExam.stream().map(x -> x.getTechnologyName()).distinct().collect(Collectors.joining("、")));
				twoList.add(two);
			});
			List<CatalogVO> result = new ArrayList<>();
			twoList.stream().filter(x->!StringUtils.isEmpty(x.getViewMd5()))
					.collect(Collectors.groupingBy(MpElectrocardiogramExam::getViewMd5))
					.forEach((viewMd5,mpElectrocardiogramExam)->{
						String title = mpElectrocardiogramExam.stream().map(x -> x.getCheckSourceType()).collect(Collectors.joining("、"));
						MpElectrocardiogramExam mpElectrocardiogramExam1 = mpElectrocardiogramExam.get(0);

						CatalogVO catalogVO = new CatalogVO();
				catalogVO.setItemTitle(DateUtils.dateTime(mpElectrocardiogramExam1.getReportDate())+" "+title);
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				setItemValueVO("检查项目",title,list);
//				setItemValueVO("身体部位",mpElectrocardiogramExam.getSctPosition(),list);
//				setItemValueVO("检查方式",mpElectrocardiogramExam.getTestMethod(),list);
				setItemValueVO("所见",mpElectrocardiogramExam1.getObjectiveView(),list);
				setItemValueVO("诊断意见",mpElectrocardiogramExam1.getSubjectiveHints(),list);
//				setItemValueVO("检查诊断",mpElectrocardiogramExam.getRelevantClinicDiagnosis(),list);
				setItemValueVOHref("心电图报告",mpElectrocardiogramExam1.getPdfReport(),list);
//				setItemValueVO("检查时间",DateUtils.dateTime(mpElectrocardiogramExam.getTechnologyDate()),list);
//				setItemValueVO("报告时间",DateUtils.dateTime(mpElectrocardiogramExam.getReportDate()),list);
						String deptName = mpElectrocardiogramExam.stream().map(x -> x.getDepartmentName()).distinct().collect(Collectors.joining("、"));
							setItemValueVO("开单科室",getRuleDeptName(deptName),list);
				catalogVO.setItemList(list);
				result.add(catalogVO);
			});
			healthItemVO.setArray(result);
			return healthItemVO;
		}
		return null;
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

	/**
	 * 获取_尿常规
	 *
	 * @param person
	 * @param totalUnitDicts
	 * @param data
	 * @param itemId
	 * @param visitType
	 */
	private void getUrineRoutineCol(MpPerson person, List<MpUnitDict> totalUnitDicts, List<HealthItemVO> data, AtomicInteger itemId, String visitType) {
		List<HealthItemVO> localList = new ArrayList<>();
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		MpUrineRoutineCol mpUrineRoutineCol = new MpUrineRoutineCol();
		if (StringUtils.isEmpty(personId)) {
			return;
		}
		mpUrineRoutineCol.setPersonId(personId);
		mpUrineRoutineCol.setVisitType(visitType);
		// 1. 查出尿常规数据
		List<MpUrineRoutineCol> mpUrineRoutineCols = mpUrineRoutineColMapper.selectMpUrineRoutineColList(mpUrineRoutineCol);
		if (CollectionUtils.isEmpty(mpUrineRoutineCols)) {
			log.info("personid={},查询尿常规为空",personId);
			return;
		}
//		Map<String, List<MpUrineRoutineCol>> visitRecords = mpUrineRoutineCols.stream().collect(Collectors.groupingBy(MpUrineRoutineCol::getVisitOccurrenceId));
		Map<String, List<MpUrineRoutineCol>> visitRecords = mpUrineRoutineCols.stream()
				.filter(obj->!StringUtils.isEmpty(DateUtils.dateTime(obj.getResultTime())))
				.collect(Collectors.groupingBy(key->DateUtils.dateTime(key.getResultTime())));
		visitRecords.forEach((k,v)->{
			List<ItemValueVO> list = new ArrayList<ItemValueVO>();
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId(itemId.get());
			for (MpUrineRoutineCol urineRoutineCol : v) {
				healthItemVO.setItemName(DateUtils.dateTime(urineRoutineCol.getResultTime()) +" "+HealthPatientCheckTestEnum.URINE_ROUTINE.getValue());
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
		data.addAll(reverseSortedHealthItemVOS(localList));
		// 2. 通过visit聚合成一条数据, 一条数据展示出来 通过日期搞出来
//		List<MpUrineRoutine> mpdbs = mpUrineRoutineMapper.selectByPersonId(personId);
//
//		List<String> resultTimes = mpdbs.stream().map(v -> DateUtils.dateTime(v.getResultTime())).collect(Collectors.toList());
//
//		Map<String, MpUrineRoutine> mpdbsMap = mpdbs.stream().collect(Collectors.toMap(key->DateUtils.dateTime(key.getResultTime()), Function.identity()));
//		for(String resultTime: resultTimes) {
//			MpUrineRoutine mpdb = mpdbsMap.get(resultTime);
//			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
//			healthItemVO.setItemId(itemId.get());
//			healthItemVO.setItemName(resultTime +" "+HealthPatientCheckTestEnum.URINE_ROUTINE.getValue());
//
//			List<MpUnitDict> mpUnitDicts = totalUnitDicts.stream().filter(mpUnitDict ->
//					mpUnitDict.getTableName().equals(HealthRecordCheckConstants.TAB_MP_URINE_ROUTINE)).collect(Collectors.toList());
//			Map<String, List<MpUnitDict>> collect = mpUnitDicts.stream().collect(Collectors.groupingBy(MpUnitDict::getCode));
//
//			List<ItemValueVO> list = new ArrayList<ItemValueVO>();
//
//			ThreadLocalUtils.put("sex",sex);
//
//			// 获取检验时候的年龄
//			threadLocalSetAge(cardId, mpdb.getResultTime());
//
//			ItemValueVO itemValueVO1= setItemValueVO("尿比重", mpdb.getUrineSg(), HealthRecordCheckConstants.ATTR_SPECIFIC_GRAVITY_URINE, collect, list);
//			ItemValueVO itemValueVO2= setItemValueVO("尿酸碱度", mpdb.getUrinePh(), HealthRecordCheckConstants.ATTR_URINE_PH, collect, list);
//			ItemValueVO itemValueVO3= setItemValueVO("尿白细胞", mpdb.getUrineWbcCount(), HealthRecordCheckConstants.ATTR_URINE_WHITE_CELLS, collect, list);
//			ItemValueVO itemValueVO4= setItemValueVO("尿红细胞", mpdb.getUrineRbcCount(), HealthRecordCheckConstants.ATTR_URINE_RED_CELLS, collect, list);
////			ItemValueVO itemValueVO5= setItemValueVO("尿亚硝酸盐", mpdb.getUrineNit(), HealthRecordCheckConstants.ATTR_URINE_NITRITE, collect, list);
//			ItemValueVO itemValueVO5= setItemValueVO("尿亚硝酸盐",mpdb.getUrineNit(), list);
//			ItemValueVO itemValueVO6= setItemValueVO("尿蛋白质", mpdb.getUrinePro(),list);
//			ItemValueVO itemValueVO7= setItemValueVO("尿糖", mpdb.getUrineGlu(),  list);
//			ItemValueVO itemValueVO8= setItemValueVO("尿酮体", mpdb.getUrineKet(), list);
//			ItemValueVO itemValueVO9= setItemValueVO("尿胆原", mpdb.getUrineUro(), list);
//			ItemValueVO itemValueVO10= setItemValueVO("胆红素", mpdb.getUrineBil(), list);
//			ItemValueVO itemValueVO11= setItemValueVO("尿隐血", mpdb.getUrineBlo(), list);
//			healthItemVO.setObj(list);
//			data.add(healthItemVO);
//
//			ThreadLocalUtils.release();
//			itemId.incrementAndGet();
//		}
	}

	/**
	 * 获取_血常规
	 *
	 * @param person
	 * @param data
	 * @param itemId
	 * @param visitType
	 */
	private void getBloodRoutineCol(MpPerson person, List<HealthItemVO> data, AtomicInteger itemId, String visitType) {
		List<HealthItemVO> localList = new ArrayList<>();
		String personId = person.getPersonId();
		MpBloodRoutineCol mpBloodRoutineCol = new MpBloodRoutineCol();
		if (StringUtils.isEmpty(personId)) {
			return;
		}
		mpBloodRoutineCol.setPersonId(personId);
		mpBloodRoutineCol.setVisitType(visitType);
		// 1. 查出血常规数据
		List<MpBloodRoutineCol> mpBloodRoutineCols = mpBloodRoutineColMapper.selectMpBloodRoutineColList(mpBloodRoutineCol);
		if (CollectionUtils.isEmpty(mpBloodRoutineCols)) {
			log.info("personid={},查询血常规为空",personId);
			return;
		}
		Map<String, List<MpBloodRoutineCol>> visitRecords = mpBloodRoutineCols.stream()
				.filter(obj->!StringUtils.isEmpty(DateUtils.dateTime(obj.getResultTime())))
				.collect(Collectors.groupingBy(key -> DateUtils.parseDateToStrFull(key.getResultTime())));
		visitRecords.forEach((k,v)->{
			List<ItemValueVO> list = new ArrayList<ItemValueVO>();
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId(itemId.get());
			for (MpBloodRoutineCol urineRoutineCol : v) {
				healthItemVO.setItemName(DateUtils.dateTime(urineRoutineCol.getResultTime()) +" "+HealthPatientCheckTestEnum.BLOOD_ROUTINE.getValue());
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
		data.addAll(reverseSortedHealthItemVOS(localList));
	}

	/**
	 * 获取_肝or肾
	 *
	 * @param person
	 * @param data
	 * @param itemId
	 * @param visitType
	 */
	private void getBloodGanOrShenCol(MpPerson person, List<HealthItemVO> data, AtomicInteger itemId, String visitType) {
		List<HealthItemVO> localList = new ArrayList<>();
		String personId = person.getPersonId();
		MpBloodBiochemistryCol mpBloodRoutineCol = new MpBloodBiochemistryCol();
		if (StringUtils.isEmpty(personId)) {
			return;
		}
		mpBloodRoutineCol.setPersonId(personId);
		mpBloodRoutineCol.setVisitType(visitType);
		// 1. 查出肝or肾
		List<MpBloodBiochemistryCol> mpBloodRoutineCols = mpBloodBiochemistryColMapper.selectMpBloodBiochemistryColList(mpBloodRoutineCol);
		if (CollectionUtils.isEmpty(mpBloodRoutineCols)) {
			log.info("personid={},查询血常规为空",personId);
			return;
		}
		Map<String, List<MpBloodBiochemistryCol>> visitRecords = mpBloodRoutineCols.stream()
				.filter(obj->!StringUtils.isEmpty(DateUtils.dateTime(obj.getResultTime())))
				.collect(Collectors.groupingBy(key -> DateUtils.dateTime(key.getResultTime())));
		visitRecords.forEach((k,v)->{

			Map<String, List<MpBloodBiochemistryCol>> collect = v.stream().collect(Collectors.groupingBy(MpBloodBiochemistryCol::getMeasurementType));
			collect.forEach((measurementType,ganOrShenObjs)->{
				// 一个检验项
				HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
				healthItemVO.setItemId(itemId.get());
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				for (MpBloodBiochemistryCol urineRoutineCol : ganOrShenObjs) {
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
	 * 获取_甲功_量少可做通用?
	 *
	 * @param person
	 * @param data
	 * @param itemId
	 * @param visitType
	 */
	private void getBloodJiaCol(MpPerson person, List<HealthItemVO> data, AtomicInteger itemId, String visitType) {

		List<HealthItemVO> localList = new ArrayList<>();
		String personId = person.getPersonId();
		MpThyroidFunctionCol mpBloodRoutineCol = new MpThyroidFunctionCol();
		if (StringUtils.isEmpty(personId)) {
			return;
		}
		mpBloodRoutineCol.setPersonId(personId);
		mpBloodRoutineCol.setVisitType(visitType);
		// 1. 查出肝or肾
		List<MpThyroidFunctionCol> mpBloodRoutineCols = mpThyroidFunctionColMapper.selectMpThyroidFunctionColList(mpBloodRoutineCol);
		if (CollectionUtils.isEmpty(mpBloodRoutineCols)) {
			log.info("personid={},查询血常规为空",personId);
			return;
		}
		Map<String, List<MpThyroidFunctionCol>> visitRecords = mpBloodRoutineCols.stream()
				.filter(obj->!StringUtils.isEmpty(DateUtils.dateTime(obj.getResultTime())))
				.collect(Collectors.groupingBy(key -> DateUtils.dateTime(key.getResultTime())));
		visitRecords.forEach((k,v)->{

			Map<String, List<MpThyroidFunctionCol>> collect = v.stream().collect(Collectors.groupingBy(MpThyroidFunctionCol::getMeasurementType));
			collect.forEach((measurementType,ganOrShenObjs)->{
				// 一个检验项
				HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
				healthItemVO.setItemId(itemId.get());
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				for (MpThyroidFunctionCol urineRoutineCol : ganOrShenObjs) {
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
				if (measurementType.contains("感染八项")) {
					List<ItemValueVO> listCopy = new ArrayList<ItemValueVO>();
					Map<String, List<ItemValueVO>> name = list.stream().collect(Collectors.groupingBy(ItemValueVO::getName));
					listCopy.addAll(name.get("乙肝表面抗原定量"));
					listCopy.addAll(name.get("乙肝表面抗体定量"));
					listCopy.addAll(name.get("乙肝e抗原定量"));
					listCopy.addAll(name.get("乙肝e抗体定量"));
					listCopy.addAll(name.get("乙肝核心抗体定量"));
					listCopy.addAll(name.get("丙型肝炎抗体定量"));
					listCopy.addAll(name.get("人免疫缺陷病毒抗原抗体"));
					listCopy.addAll(name.get("梅毒抗体定量"));
					healthItemVO.setObj(listCopy);
				}else {
					healthItemVO.setObj(list);
				}
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
	 * 获取_尿常规 门诊
	 *
	 * @param totalUnitDicts
	 * @param person
	 * @param data
	 * @param itemId
	 * @param visitType
	 */
	private void getStoolRoutineCol(MpPerson person, List<HealthItemVO> data, AtomicInteger itemId, String visitType) {
		List<HealthItemVO> localList = new ArrayList<>();
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		MpStoolRoutineCol mpStool = new MpStoolRoutineCol();
		if (StringUtils.isEmpty(personId)) {
			return;
		}
		mpStool.setPersonId(personId);
		mpStool.setVisitType(visitType);
		// 1. 查出尿常规数据
		List<MpStoolRoutineCol> mpStoolRoutineCols = mpStoolRoutineColMapper.selectMpStoolRoutineColList(mpStool);
		if (CollectionUtils.isEmpty(mpStoolRoutineCols)) {
			log.info("personid={},查询便常规为空",personId);
			return;
		}
		Map<String, List<MpStoolRoutineCol>> visitRecords = mpStoolRoutineCols.stream().collect(Collectors.groupingBy(key->DateUtils.dateTime(key.getResultTime())));
		visitRecords.forEach((k,v)->{
			List<ItemValueVO> list = new ArrayList<ItemValueVO>();
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId(itemId.get());
			for (MpStoolRoutineCol urineRoutineCol : v) {
				healthItemVO.setItemName(DateUtils.dateTime(urineRoutineCol.getResultTime()) +" "+HealthPatientCheckTestEnum.STOOL_ROUTINE.getValue());
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
		data.addAll(reverseSortedHealthItemVOS(localList));
	}

	/**
	 * 凝血 门诊
	 *
	 * @param totalUnitDicts
	 * @param person
	 * @param data
	 * @param itemId
	 * @param visitType
	 */
	private void getCoagulationFunctionCol(MpPerson person, List<HealthItemVO> data, AtomicInteger itemId, String visitType) {
		List<HealthItemVO> localList = new ArrayList<>();
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		MpCoagulationFunctionCol mpStool = new MpCoagulationFunctionCol();
		if (StringUtils.isEmpty(personId)) {
			return;
		}
		mpStool.setPersonId(personId);
		mpStool.setVisitType(visitType);
		// 1. 查出尿常规数据
		List<MpCoagulationFunctionCol> mpStoolRoutineCols = mpCoagulationFunctionColMapper.selectMpCoagulationFunctionColList(mpStool);
		if (CollectionUtils.isEmpty(mpStoolRoutineCols)) {
			log.info("personid={},查询便常规为空",personId);
			return;
		}
		Map<String, List<MpCoagulationFunctionCol>> visitRecords = mpStoolRoutineCols.stream()
				.filter(key->!StringUtils.isEmpty(DateUtils.dateTime(key.getResultTime())))
				.collect(Collectors.groupingBy(key->DateUtils.parseDateToStrFull(key.getResultTime())));
		visitRecords.forEach((k,v)->{
			List<ItemValueVO> list = new ArrayList<ItemValueVO>();
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId(itemId.get());
			for (MpCoagulationFunctionCol urineRoutineCol : v) {
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
		data.addAll(reverseSortedHealthItemVOS(localList));
	}
	/**
	 * 肿瘤标着物 门诊
	 *
	 * @param totalUnitDicts
	 * @param person
	 * @param data
	 * @param itemId
	 * @param visitType
	 */
	private void getTumorScreeningItemsCol(MpPerson person, List<HealthItemVO> data, AtomicInteger itemId, String visitType) {
		List<HealthItemVO> localList = new ArrayList<>();
		String personId = person.getPersonId();
		String sex = person.getGenderSourceValue();
		String cardId = person.getCardId();
		MpTumorScreeningItemsCol mpStool = new MpTumorScreeningItemsCol();
		if (StringUtils.isEmpty(personId)) {
			return;
		}
		mpStool.setPersonId(personId);
		mpStool.setVisitType(visitType);
		// 1. 查出尿常规数据
		List<MpTumorScreeningItemsCol> mpStoolRoutineCols = mpTumorScreeningItemsColMapper.selectMpTumorScreeningItemsColList(mpStool);
		if (CollectionUtils.isEmpty(mpStoolRoutineCols)) {
			log.info("personid={},查询便常规为空",personId);
			return;
		}
		Map<String, List<MpTumorScreeningItemsCol>> visitRecords = mpStoolRoutineCols.stream()
				.filter(obj->!StringUtils.isEmpty(DateUtils.dateTime(obj.getResultTime())))
				.collect(Collectors.groupingBy(key->DateUtils.dateTime(key.getResultTime())));
		visitRecords.forEach((k,v)->{
			List<ItemValueVO> list = new ArrayList<ItemValueVO>();
			HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
			healthItemVO.setItemId(itemId.get());
			for (MpTumorScreeningItemsCol urineRoutineCol : v) {
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
		data.addAll(reverseSortedHealthItemVOS(localList));
	}
	/**
	 * 血糖 门诊
	 *
	 * @param totalUnitDicts
	 * @param person
	 * @param data
	 * @param itemId
	 * @param visitType
	 */
	private void getGlucoseToleranceCol(MpPerson person, List<HealthItemVO> data, AtomicInteger itemId, String visitType) {
		List<HealthItemVO> localList = new ArrayList<>();
		String personId = person.getPersonId();
		MpGlucoseToleranceCol mpStool = new MpGlucoseToleranceCol();
		if (StringUtils.isEmpty(personId)) {
			return;
		}
		mpStool.setPersonId(personId);
		mpStool.setVisitType(visitType);
		// 1. 查出尿常规数据
		List<MpGlucoseToleranceCol> mpStoolRoutineCols = mpGlucoseToleranceColMapper.selectMpGlucoseToleranceColList(mpStool);
		if (CollectionUtils.isEmpty(mpStoolRoutineCols)) {
			log.info("personid={},查询便常规为空",personId);
			return;
		}
		Map<String, List<MpGlucoseToleranceCol>> visitRecords = mpStoolRoutineCols.stream()
				.filter(obj->!StringUtils.isEmpty(DateUtils.dateTime(obj.getResultTime())))
				.collect(Collectors.groupingBy(key->DateUtils.parseDateToStrFull(key.getResultTime())));
		visitRecords.forEach((k/*时间*/,v/*所有类型血糖*/)->{
			Map<String, List<MpGlucoseToleranceCol>> collect = v.stream().collect(Collectors.groupingBy(MpGlucoseToleranceCol::getMeasurementType));
			collect.forEach((k1/*类型*/,v1/*所有血糖*/)->{

				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				HealthItemVO<List<ItemValueVO>>  healthItemVO  = new HealthItemVO<List<ItemValueVO>>();
				healthItemVO.setItemId(itemId.get());
				for (MpGlucoseToleranceCol urineRoutineCol : v1) {
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
		data.addAll(reverseSortedHealthItemVOS(localList));
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


			List<MpUnitDict> mpUnitDicts = totalUnitDicts.stream().filter(mpUnitDict ->
					mpUnitDict.getTableName().equals(HealthRecordCheckConstants.TAB_MP_BLOOD_ROUTINE)).collect(Collectors.toList());
			Map<String, List<MpUnitDict>> collect = mpUnitDicts.stream().collect(Collectors.groupingBy(MpUnitDict::getCode));

			List<ItemValueVO> list = new ArrayList<ItemValueVO>();

			ThreadLocalUtils.put("sex",sex);

			// 获取检验时候的年龄
			threadLocalSetAge(cardId, mpdb.getResultTime());

			//白细胞计数
			ItemValueVO itemValueVO17= setItemValueVO("中性粒细胞百分率", mpdb.getBloodNePercentage(), HealthRecordCheckConstants.ATTR_NEUTROPHIL_PERCENTAGE, collect, list);
			ItemValueVO itemValueVO15= setItemValueVO("淋巴细胞百分率", mpdb.getBloodLyPercentage(), HealthRecordCheckConstants.ATTR_LYMPHOCYTE_PERCENTAGE, collect, list);
			ItemValueVO itemValueVO19= setItemValueVO("单核细胞百分率", mpdb.getBloodMoPercentage(), HealthRecordCheckConstants.ATTR_MONOCYTE_PERCENTAGE, collect, list);
			//todo 嗜酸性粒细胞百分率 =? 嗜酸性细胞百分率
			ItemValueVO itemValueVO26= setItemValueVO("嗜酸性粒细胞百分率", mpdb.getBloodEoPercentage(), HealthRecordCheckConstants.ATTR_EOSINOPHIL_PERCENTAGE, collect, list);
			//todo 嗜酸性粒细胞百分率 =? 嗜酸性细胞百分率
			ItemValueVO itemValueVO25= setItemValueVO("嗜碱性粒细胞百分率", mpdb.getBloodBaPercentage(), HealthRecordCheckConstants.ATTR_BASOPHIL_PERCENTAGE, collect, list);
			ItemValueVO itemValueVO16= setItemValueVO("中性粒细胞绝对值", mpdb.getBloodNeCount(), HealthRecordCheckConstants.ATTR_ABS_NEUTROPHIL_COUNT, collect, list);
			ItemValueVO itemValueVO14= setItemValueVO("淋巴细胞绝对值", mpdb.getBloodLyCount(), HealthRecordCheckConstants.ATTR_ABS_LYMPHOCYTE_COUNT, collect, list);
			ItemValueVO itemValueVO18= setItemValueVO("单核细胞绝对值", mpdb.getBloodMoCount(), HealthRecordCheckConstants.ATTR_ABS_MONOCYTE_COUNT, collect, list);
			ItemValueVO itemValueVO20= setItemValueVO("嗜酸性细胞绝对值", mpdb.getBloodEoCount(), HealthRecordCheckConstants.ATTR_ABS_EOSINOPHIL_COUNT, collect, list);
			ItemValueVO itemValueVO22= setItemValueVO("嗜碱性细胞绝对值", mpdb.getBloodBaCount(), HealthRecordCheckConstants.ATTR_ABS_BASOPHIL_COUNT, collect, list);
			ItemValueVO itemValueVO2= setItemValueVO("红细胞计数", mpdb.getRbcCount(), HealthRecordCheckConstants.ATTR_RED_CELL_COUNT, collect, list);
			ItemValueVO itemValueVO3= setItemValueVO("血红蛋白", mpdb.getBloodHb(), HealthRecordCheckConstants.ATTR_HEMOGLOBIN, collect, list);
			ItemValueVO itemValueVO4= setItemValueVO("红细胞压积", mpdb.getBloodHct(), HealthRecordCheckConstants.ATTR_HEMATOCRIT, collect, list);
			ItemValueVO itemValueVO5 = setItemValueVO("平均红细胞体积", mpdb.getBloodMcv(), HealthRecordCheckConstants.ATTR_MEAN_CORPUS_VOLUME, collect, list);
			// 平均血红蛋白含量 (平均红细胞血红蛋白含量)
			ItemValueVO itemValueVO6= setItemValueVO("平均血红蛋白含量", mpdb.getBloodMch(), HealthRecordCheckConstants.ATTR_MEAN_CORPUS_VOLUME, collect, list);
			ItemValueVO itemValueVO7= setItemValueVO("平均血红蛋白浓度", mpdb.getBloodMchc(), HealthRecordCheckConstants.ATTR_MEAN_CORPUS_HEM_CONCENT, collect, list);
			//红细胞分布宽度-变异系数 红细胞分布宽度CV
			ItemValueVO itemValueVO8= setItemValueVO("红细胞分布宽度CV", mpdb.getBloodRdwCv(), HealthRecordCheckConstants.ATTR_RED_CELL_DIS_WIDTH_VARIATION, collect, list);
			//红细胞分布宽度-标准差 红细胞分布宽度SD
			ItemValueVO itemValueVO9= setItemValueVO("红细胞分布宽度SD", mpdb.getBloodRdwSd(), HealthRecordCheckConstants.ATTR_RED_CELL_DIS_DEVIATION, collect, list);
			ItemValueVO itemValueVO10= setItemValueVO("血小板计数", mpdb.getBloodPltCount(), HealthRecordCheckConstants.ATTR_PLATELET_COUNT, collect, list);
			ItemValueVO itemValueVO13= setItemValueVO("血小板分布宽度", mpdb.getBloodPdw(), HealthRecordCheckConstants.ATTR_PLATELET_DIS_WIDTH, collect, list);
			ItemValueVO itemValueVO11= setItemValueVO("平均血小板体积", mpdb.getBloodMpv(), HealthRecordCheckConstants.ATTR_MEAN_PLATELET_VOLUME, collect, list);
			//todo 大板
			ItemValueVO itemValueVO1= setItemValueVO("大血小板比率", mpdb.getLargePlateletRatio(), HealthRecordCheckConstants.ATTR_LARGE_PLATELET_RATIO, collect, list);
			ItemValueVO itemValueVO= setItemValueVO("白细胞计数", mpdb.getWbcCount(), HealthRecordCheckConstants.ATTR_WHITE_CELL_COUNT, collect, list);
			//血小板压积 --> 血小板比积
			ItemValueVO itemValueVO12= setItemValueVO("血小板比积", mpdb.getBloodPct(), HealthRecordCheckConstants.ATTR_PLATELETCRIT, collect, list);

//			ItemValueVO itemValueVO21= setItemValueVO("嗜酸性细胞百分比", mpdb.getBloodEoPercentage(), HealthRecordCheckConstants.ATTR_EOSINOPHIL_PERCENTAGE, collect, list);
//			ItemValueVO itemValueVO23= setItemValueVO("嗜碱性细胞百分比", mpdb.getBloodBaPercentage(), HealthRecordCheckConstants.ATTR_BASOPHIL_PERCENTAGE, collect, list);
			healthItemVO.setObj(list);
//			int itemCount = list.size();
			healthItemVO.setItemId(itemId.get());
			healthItemVO.setItemName(resultTime +" "+HealthPatientCheckTestEnum.BLOOD_ROUTINE.getValue());
			data.add(healthItemVO);

			ThreadLocalUtils.release();
			itemId.incrementAndGet();
		}
	}

	public List<HealthItemVO> mzExamItemList(DetailForm detail) {
//		String s = MDC.get("traceId");

		// 通过patientId去查询personId
		if (StringUtils.isEmpty(detail.getPersonId())) {
			PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(Long.parseLong(detail.getPatientId()));
			if(patientInfo == null || StringUtils.isEmpty(patientInfo.getPersonId())){
				log.info("patientId = {} 在 patient_info 表查不到信息或者personId为空");
				return null;
			}
			detail.setPersonId(patientInfo.getPersonId());
		}
		String personId =detail.getPersonId();
		List<HealthItemVO> data = new ArrayList<HealthItemVO>();

		AtomicInteger itemId = new AtomicInteger(1);

		String visitType = detail.getVisitType();
		if ("2".equals(visitType)) {
			visitType = "门诊";
		}
		if ("4".equals(visitType)) {
			visitType = "预住院";
		}

		// CT
		getMzCtExam(personId, data, itemId,visitType);

		// 磁共振
		getMzNuclearMagnetismExam(personId, data, itemId,visitType);

		// X线
		getMzXRayExam(personId, data, itemId,visitType);

		// 超声
		getMzUltrasonicExam(personId, data, itemId,visitType);

		// 心电图
		getMzElectrocardiogramExam(personId, data, itemId,visitType);

		return data;
	}

	/**
	 * 合并同类项: 将同testNo, 不同testNo同天同内容的合并
	 * @param personId
	 * @param data
	 * @param itemId
	 * @param visitType
	 */
	private void getMzCtExam(String personId, List<HealthItemVO> data, AtomicInteger itemId, String visitType) {
		List<MpCtExam> totalCtExams = mpCtExamMapper.selectMpCtExamList(personId,null, visitType);
		List<String> reportDates = totalCtExams.stream().map(v -> DateUtils.dateTime(v.getReportDate())).distinct().collect(Collectors.toList());

		Map<String, List<MpCtExam>> mpCtExamMap = totalCtExams.stream().collect(Collectors.groupingBy(key -> DateUtils.dateTime(key.getReportDate())));

		HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
		healthItemVO.setItemId(itemId.get());
		healthItemVO.setItemName("CT");
		List<CatalogVO> result = new ArrayList<>();
		for(String reportDate: reportDates){
			List<MpCtExam> mpCtExams = mpCtExamMap.get(reportDate);

			//合并 按testNo做完一遍, 再来一遍内容, 同级~
			Map<String, List<MpCtExam>> collect = mpCtExams.stream()
					.filter(x->!StringUtils.isEmpty(x.getTestNo()))
					.collect(Collectors.groupingBy(MpCtExam::getTestNo));
			List<MpCtExam> twoMpCtExams = new ArrayList<>();
			// 不同号按内容合并
			collect.forEach((testNo,mpctExamList)->{
				MpCtExam mpCtExam = new MpCtExam();
				String ctTitle = mpctExamList.stream().map(MpCtExam::getCheckSourceType).distinct().collect(Collectors.joining("、"));
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
			Map<String, List<MpCtExam>> viewCtList = twoMpCtExams.stream()
					.filter(x->!StringUtils.isEmpty(x.getObjectiveViewMD5()))
					.collect(Collectors.groupingBy(MpCtExam::getObjectiveViewMD5));
			viewCtList.forEach((view,ctlist)->{
				CatalogVO catalogVO = new CatalogVO();
				String ctTitle = ctlist.stream().map(MpCtExam::getCheckSourceType).distinct().collect(Collectors.joining("、"));
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				setItemValueVO("检查项目",ctTitle,list);
//				setItemValueVO("检查方式",mpCtExam.getTestMethod(),list);
//				setItemValueVO("身体部位",mpCtExam.getSctPosition(),list);
				setItemValueVO("所见",ctlist.get(0).getObjectiveView(),list);
				setItemValueVO("诊断意见",ctlist.get(0).getSubjectiveHints(),list);
//				setItemValueVO("诊断意见",mpCtExam.getRelevantClinicDiagnosis(),list);
//				setItemValueVO("检查时间",DateUtils.dateTime(mpCtExam.getTechnologyDate()),list);
//				setItemValueVO("报告时间",DateUtils.dateTime(mpCtExam.getReportDate()),list);
				String deptName = ctlist.stream().map(MpCtExam::getDepartmentName).distinct().collect(Collectors.joining("、"));
				setItemValueVO("开单科室",getRuleDeptName(deptName),list);
				catalogVO.setItemList(list);
				catalogVO.setItemTitle(reportDate+" "+ctTitle);
				result.add(catalogVO);
			});
//			twoMpCtExams.stream()
//					.filter(x -> StringUtils.isEmpty(x.getObjectiveViewMD5()))
//					.forEach(ct->{
//						CatalogVO catalogVO = new CatalogVO();
//						List<ItemValueVO> list = new ArrayList<ItemValueVO>();
//						setItemValueVO("检查项目",ct.getCheckSourceType(),list);
//						setItemValueVO("所见",ct.getObjectiveView(),list);
//						setItemValueVO("诊断意见",ct.getSubjectiveHints(),list);
//						setItemValueVO("开单科室",ct.getDepartmentName(),list);
//						catalogVO.setItemList(list);
//						catalogVO.setItemTitle(reportDate+" "+ct.getCheckSourceType());
//						result.add(catalogVO);
//					});
			itemId.incrementAndGet();
		}
		healthItemVO.setObj(result);
		if (!CollectionUtils.isEmpty(totalCtExams)) {
			data.add(healthItemVO);
		}
	}

	private void getMzNuclearMagnetismExam(String personId, List<HealthItemVO> data, AtomicInteger itemId, String visitType) {
		List<MpNuclearMagnetismExam> totalNuclearMagnetismExams = mpNuclearMagnetismExamMapper.selectMpNuclearMagnetismExamList(personId,null, visitType);
		List<String> reportDates = totalNuclearMagnetismExams.stream().map(v -> DateUtils.dateTime(v.getReportDate())).distinct().collect(Collectors.toList());

		Map<String, List<MpNuclearMagnetismExam>> mpNuclearMagnetismExamMap = totalNuclearMagnetismExams.stream().collect(Collectors.groupingBy(key -> DateUtils.dateTime(key.getReportDate())));
		HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
		healthItemVO.setItemId(itemId.get());
		healthItemVO.setItemName("磁共振");
		List<CatalogVO> result = new ArrayList<>();

		for(String reportDate: reportDates){
			List<MpNuclearMagnetismExam> mpNuclearMagnetismExams = mpNuclearMagnetismExamMap.get(reportDate);
			Map<String, List<MpNuclearMagnetismExam>> collect = mpNuclearMagnetismExams.stream().filter(x->!StringUtils.isEmpty(x.getTestNo()))
					.collect(Collectors.groupingBy(MpNuclearMagnetismExam::getTestNo));
			List<MpNuclearMagnetismExam> twoList = new ArrayList<>();
			collect.forEach((testNo,mpctExamList)->{
				MpNuclearMagnetismExam two = new MpNuclearMagnetismExam();
				MpNuclearMagnetismExam mpNuclearMagnetismExam = mpctExamList.get(0);
				BeanUtils.copyProperties(mpNuclearMagnetismExam,two);
				String objectiveView = mpNuclearMagnetismExam.getObjectiveView();
				String subjectiveHints = mpNuclearMagnetismExam.getSubjectiveHints();
				if (!StringUtils.isEmpty(objectiveView) || !StringUtils.isEmpty(subjectiveHints)) {
					String encode = MD5Util.encode(StringUtils.trim(objectiveView) + StringUtils.trim(subjectiveHints));
					two.setViewMD5(encode);
				}
				twoList.add(two);
				String ctTitle = mpctExamList.stream().map(MpNuclearMagnetismExam::getCheckSourceType).distinct().collect(Collectors.joining("、"));
				two.setCheckSourceType(ctTitle);
				two.setTechnologyName(ctTitle);
			});
			Map<String, List<MpNuclearMagnetismExam>> listMap = twoList.stream().filter(x -> !StringUtils.isEmpty(x.getViewMD5())).collect(Collectors.groupingBy(MpNuclearMagnetismExam::getViewMD5));
			listMap.forEach((view, ctlist) -> {
				String ctTitle = ctlist.stream().map(MpNuclearMagnetismExam::getCheckSourceType).distinct().collect(Collectors.joining("、"));
				CatalogVO catalogVO = new CatalogVO();
				catalogVO.setItemTitle(reportDate+" "+ctTitle);
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				setItemValueVO("检查项目",ctTitle,list);
//				setItemValueVO("检查方式",mpCtExam.getTestMethod(),list);
//				setItemValueVO("身体部位",mpCtExam.getSctPosition(),list);
				setItemValueVO("所见",ctlist.get(0).getObjectiveView(),list);
				setItemValueVO("诊断意见",ctlist.get(0).getSubjectiveHints(),list);
//				setItemValueVO("诊断意见",mpCtExam.getRelevantClinicDiagnosis(),list);
//				setItemValueVO("检查时间",DateUtils.dateTime(mpCtExam.getTechnologyDate()),list);
//				setItemValueVO("报告时间",DateUtils.dateTime(mpCtExam.getReportDate()),list);
				String deptName = ctlist.stream().map(MpNuclearMagnetismExam::getDepartmentName).distinct().collect(Collectors.joining("、"));
				setItemValueVO("开单科室",getRuleDeptName(deptName),list);
				catalogVO.setItemList(list);
				result.add(catalogVO);
			});

//			for(MpNuclearMagnetismExam mpNuclearMagnetismExam : mpNuclearMagnetismExams){
//				CatalogVO catalogVO = new CatalogVO();
//				catalogVO.setItemTitle(mpNuclearMagnetismExam.getTechnologyName());
//				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
//				setItemValueVO("检查项目",mpNuclearMagnetismExam.getTechnologyName(),list);
////				setItemValueVO("身体部位",mpNuclearMagnetismExam.getSctPosition(),list);
////				setItemValueVO("检查方式",mpNuclearMagnetismExam.getTestMethod(),list);
//				setItemValueVO("所见",mpNuclearMagnetismExam.getObjectiveView(),list);
//				setItemValueVO("诊断意见",mpNuclearMagnetismExam.getSubjectiveHints(),list);
////				setItemValueVO("检查诊断",mpNuclearMagnetismExam.getRelevantClinicDiagnosis(),list);
////				setItemValueVO("检查时间",DateUtils.dateTime(mpNuclearMagnetismExam.getTechnologyDate()),list);
////				setItemValueVO("报告时间",DateUtils.dateTime(mpNuclearMagnetismExam.getReportDate()),list);
//				setItemValueVO("开单科室",mpNuclearMagnetismExam.getDepartmentName(),list);
//				catalogVO.setItemList(list);
//				result.add(catalogVO);
//
////				Map<String, List<MpCtExam>> collect = mpCtExams.stream().collect(Collectors.groupingBy(MpCtExam::getTestNo));
////
////				collect.forEach((testNo,mpctExamList)->{
////					String ctTitle = mpctExamList.stream().map(MpCtExam::getCheckSourceType).collect(Collectors.joining("、"));
////
////					CatalogVO catalogVO = new CatalogVO();
//////				catalogVO.setItemTitle(ctTitle);
////					List<ItemValueVO> list = new ArrayList<ItemValueVO>();
////					setItemValueVO("检查项目",ctTitle,list);
//////				setItemValueVO("检查方式",mpCtExam.getTestMethod(),list);
//////				setItemValueVO("身体部位",mpCtExam.getSctPosition(),list);
////					setItemValueVO("所见",mpctExamList.get(0).getObjectiveView(),list);
////					setItemValueVO("诊断意见",mpctExamList.get(0).getSubjectiveHints(),list);
//////				setItemValueVO("诊断意见",mpCtExam.getRelevantClinicDiagnosis(),list);
//////				setItemValueVO("检查时间",DateUtils.dateTime(mpCtExam.getTechnologyDate()),list);
//////				setItemValueVO("报告时间",DateUtils.dateTime(mpCtExam.getReportDate()),list);
////					setItemValueVO("开单科室",mpctExamList.get(0).getDepartmentName(),list);
////					catalogVO.setItemList(list);
////					result.add(catalogVO);
////				});
//			}

		}
		healthItemVO.setObj(result);
		if (!CollectionUtils.isEmpty(totalNuclearMagnetismExams)) {
			data.add(healthItemVO);
		}
		itemId.incrementAndGet();
	}

	private void getMzXRayExam(String personId, List<HealthItemVO> data, AtomicInteger itemId, String visitType) {
		List<MpXRayExam> totalXRayExams = mpXRayExamMapper.selectMpXRayExamList(personId,null, visitType);
		List<String> reportDates = totalXRayExams.stream().map(v -> DateUtils.dateTime(v.getReportDate())).distinct().collect(Collectors.toList());

		Map<String, List<MpXRayExam>> XRayExamMap = totalXRayExams.stream().collect(Collectors.groupingBy(key -> DateUtils.dateTime(key.getReportDate())));

		HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
		healthItemVO.setItemId(itemId.get());
//			healthItemVO.setItemName(reportDate+" "+HealthPatientCheckExamEnum.X_RAY_EXAM.getValue());
		healthItemVO.setItemName("X片");
		List<CatalogVO> result = new ArrayList<>();

		for(String reportDate: reportDates){
			List<MpXRayExam> mpXRayExams = XRayExamMap.get(reportDate);
			Map<String, List<MpXRayExam>> collect = mpXRayExams.stream().distinct().filter(x->!StringUtils.isEmpty(x.getTestNo()))
					.collect(Collectors.groupingBy(MpXRayExam::getTestNo));
			List<MpXRayExam> twoList = new ArrayList<>();
			collect.forEach((testNo,mpXRayExamList)->{
				MpXRayExam two = new MpXRayExam();
				MpXRayExam mpXRayExam = mpXRayExamList.get(0);
				BeanUtils.copyProperties(mpXRayExam, two);
				String title = mpXRayExamList.stream().map(x -> x.getCheckSourceType()).distinct().collect(Collectors.joining("、"));
				two.setCheckSourceType(title);
				two.setTechnologyName(title);
				String objectiveView = mpXRayExam.getObjectiveView();
				String subjectiveHints = mpXRayExam.getSubjectiveHints();
				if (!StringUtils.isEmpty(objectiveView) || !StringUtils.isEmpty(subjectiveHints)) {
					String encode = MD5Util.encode(StringUtils.trim(objectiveView) + StringUtils.trim(subjectiveHints));
					two.setViewMd5(encode);
				}
				twoList.add(two);
			});
			Map<String, List<MpXRayExam>> collect1 = twoList.stream().distinct().collect(Collectors.groupingBy(MpXRayExam::getViewMd5));
			collect1.forEach((viewMd5,mpXRayExamList)->{
				MpXRayExam mpXRayExam = mpXRayExamList.get(0);
				String title = mpXRayExamList.stream().map(MpXRayExam::getCheckSourceType).distinct().collect(Collectors.joining("、"));
				CatalogVO catalogVO = new CatalogVO();
				catalogVO.setItemTitle(reportDate+" "+title);
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				setItemValueVO("检查项目",title,list);
//				setItemValueVO("身体部位",mpXRayExam.getSctPosition(),list);
//				setItemValueVO("检查方式",mpXRayExam.getTestMethod(),list);
				setItemValueVO("所见",mpXRayExam.getObjectiveView(),list);
				setItemValueVO("诊断意见",mpXRayExam.getSubjectiveHints(),list);
//				setItemValueVO("检查诊断",mpXRayExam.getRelevantClinicDiagnosis(),list);
//				setItemValueVO("检查时间",DateUtils.dateTime(mpXRayExam.getTechnologyDate()),list);
//				setItemValueVO("报告时间",DateUtils.dateTime(mpXRayExam.getReportDate()),list);
				String deptName = mpXRayExamList.stream().map(MpXRayExam::getDepartmentName).distinct().collect(Collectors.joining("、"));
					setItemValueVO("开单科室",getRuleDeptName(deptName),list);
				catalogVO.setItemList(list);
				result.add(catalogVO);
			});
		}
		healthItemVO.setObj(result);
		if (!CollectionUtils.isEmpty(totalXRayExams)) {
			data.add(healthItemVO);
		}
		itemId.incrementAndGet();
	}

	/**
	 * 超声
	 *
	 * @param personId
	 * @param data
	 * @param itemId
	 * @param visitType
	 */
	private void getMzUltrasonicExam(String personId, List<HealthItemVO> data, AtomicInteger itemId, String visitType) {
		List<MpUltrasonicExam> totalMpUltrasonicExams = mpUltrasonicExamMapper.selectMpUltrasonicExamList(personId,null, visitType);
		List<String> reportDates = totalMpUltrasonicExams.stream().map(v -> DateUtils.dateTime(v.getReportDate())).distinct().collect(Collectors.toList());

		Map<String, List<MpUltrasonicExam>> mpUltrasonicExamsMap = totalMpUltrasonicExams.stream().collect(Collectors.groupingBy(key -> DateUtils.dateTime(key.getReportDate())));

//		for(String reportDate: reportDates){
//			List<MpUltrasonicExam> mpUltrasonicExams = mpUltrasonicExamsMap.get(reportDate);
//			// todo 空指针检查
//			Map<String, List<MpUltrasonicExam>> mpUltrasonicExams2 = mpUltrasonicExams.stream().collect(Collectors.groupingBy(MpUltrasonicExam::getTestNo));
//			List<MpUltrasonicExam> mpUltrasonicExams1 = new ArrayList<>();
//			mpUltrasonicExams2.forEach((k,v) -> {
//				mpUltrasonicExams1.add(v.stream().findFirst().get());
//			});
//			HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
//			healthItemVO.setItemId(itemId.get());
//			healthItemVO.setItemName(reportDate+" "+HealthPatientCheckExamEnum.ULTRASONIC_EXAM.getValue());
//			List<CatalogVO> result = new ArrayList<>();
//
////			Map<String, List<MpCtExam>> collect = mpCtExams.stream().collect(Collectors.groupingBy(MpCtExam::getTestNo));
//
//			for(MpUltrasonicExam mpUltrasonicExam : mpUltrasonicExams1){
//				CatalogVO catalogVO = new CatalogVO();
//				catalogVO.setItemTitle(mpUltrasonicExam.getTechnologySourceName());
//				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
//				setItemValueVO("检查项目",mpUltrasonicExam.getTechnologySourceName(),list);
////				setItemValueVO("检查部位",mpUltrasonicExam.getPosition(),list);
////				setItemValueVO("检查方式",mpUltrasonicExam.getTestMethod(),list);
//				setItemValueVO("所见",mpUltrasonicExam.getObjectiveView(),list);
//				setItemValueVO("诊断意见",mpUltrasonicExam.getSubjectiveHints(),list);
////				setItemValueVO("检查诊断",mpUltrasonicExam.getRelevantClinicDiagnosis(),list);
////				setItemValueVO("检查时间",DateUtils.dateTime(mpUltrasonicExam.getTechnologyDate()),list);
////				setItemValueVO("报告时间",DateUtils.dateTime(mpUltrasonicExam.getReportDate()),list);
//				setItemValueVO("开单科室",mpUltrasonicExam.getDepartmentName(),list);
//				catalogVO.setItemList(list);
//				result.add(catalogVO);
//			}
//			healthItemVO.setObj(result);
//			data.add(healthItemVO);
//			itemId.incrementAndGet();
//		}
		HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
		healthItemVO.setItemId(itemId.get());
		healthItemVO.setItemName("超声");
		List<CatalogVO> result = new ArrayList<>();

		for(String reportDate: reportDates){
			List<MpUltrasonicExam> mpUltrasonicExams = mpUltrasonicExamsMap.get(reportDate);

			Map<String, List<MpUltrasonicExam>> collect = mpUltrasonicExams.stream()
					.filter(x->!StringUtils.isEmpty(x.getObjectiveView())).filter(x->!StringUtils.isEmpty(x.getTestNo()))
					.collect(Collectors.groupingBy(MpUltrasonicExam::getTestNo));
			List<MpUltrasonicExam> towList = new ArrayList<>();
			collect.forEach((testNo,mpctExamList)->{
				MpUltrasonicExam two = new MpUltrasonicExam();
				MpUltrasonicExam mpUltrasonicExam = mpctExamList.get(0);
				BeanUtils.copyProperties(mpUltrasonicExam,two);

				String ctTitle = mpctExamList.stream().map(MpUltrasonicExam::getCheckSourceType).distinct().collect(Collectors.joining("、"));
				two.setCheckSourceType(ctTitle);
				two.setTechnologyName(ctTitle);
				if (!StringUtils.isEmpty(mpUltrasonicExam.getObjectiveView()) || !StringUtils.isEmpty(mpUltrasonicExam.getSubjectiveHints())) {
					String encode = MD5Util.encode(StringUtils.trim(mpUltrasonicExam.getObjectiveView()) + StringUtils.trim(mpUltrasonicExam.getSubjectiveHints()));
					two.setViewMD5(encode);
				}
				towList.add(two);

			});
			towList.stream().filter(x->!StringUtils.isEmpty(x.getViewMD5())).collect(Collectors.groupingBy(MpUltrasonicExam::getViewMD5)).forEach((viewMD5,mpctExamList)->{

				String ctTitle = mpctExamList.stream().map(MpUltrasonicExam::getCheckSourceType).distinct().collect(Collectors.joining("、"));
				CatalogVO catalogVO = new CatalogVO();
				catalogVO.setItemTitle(reportDate+" "+ctTitle);
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				setItemValueVO("检查项目",ctTitle,list);
//				setItemValueVO("检查方式",mpCtExam.getTestMethod(),list);
//				setItemValueVO("身体部位",mpCtExam.getSctPosition(),list);
				setItemValueVO("所见",mpctExamList.get(0).getObjectiveView(),list);
				setItemValueVO("诊断意见",mpctExamList.get(0).getSubjectiveHints(),list);
//				setItemValueVO("诊断意见",mpCtExam.getRelevantClinicDiagnosis(),list);
//				setItemValueVO("检查时间",DateUtils.dateTime(mpCtExam.getTechnologyDate()),list);
//				setItemValueVO("报告时间",DateUtils.dateTime(mpCtExam.getReportDate()),list);
				String deptName = mpctExamList.stream().map(MpUltrasonicExam::getDepartmentName).distinct().collect(Collectors.joining("、"));
					setItemValueVO("开单科室",getRuleDeptName(deptName),list);
				catalogVO.setItemList(list);
				result.add(catalogVO);
		});
//			for(MpCtExam mpCtExam : mpCtExams){
//				CatalogVO catalogVO = new CatalogVO();
//				catalogVO.setItemTitle(mpCtExam.getTechnologyName());
//				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
//				setItemValueVO("检查项目",mpCtExam.getTechnologyName(),list);
////				setItemValueVO("检查方式",mpCtExam.getTestMethod(),list);
////				setItemValueVO("身体部位",mpCtExam.getSctPosition(),list);
//				setItemValueVO("所见",mpCtExam.getObjectiveView(),list);
//				setItemValueVO("诊断意见",mpCtExam.getSubjectiveHints(),list);
////				setItemValueVO("诊断意见",mpCtExam.getRelevantClinicDiagnosis(),list);
////				setItemValueVO("检查时间",DateUtils.dateTime(mpCtExam.getTechnologyDate()),list);
////				setItemValueVO("报告时间",DateUtils.dateTime(mpCtExam.getReportDate()),list);
//				setItemValueVO("开单科室",mpCtExam.getDepartmentName(),list);
//				catalogVO.setItemList(list);
//				result.add(catalogVO);
//			}
		}
		healthItemVO.setObj(result);
		if (!CollectionUtils.isEmpty(totalMpUltrasonicExams)) {
			data.add(healthItemVO);
		}
		itemId.incrementAndGet();
	}

	private void getMzElectrocardiogramExam(String personId, List<HealthItemVO> data, AtomicInteger itemId, String visitType) {
		List<MpElectrocardiogramExam> totalElectrocardiogramExams = mpElectrocardiogramExamMapper.selectMpElectrocardiogramExamList(personId,null, visitType);
		List<String> reportDates = totalElectrocardiogramExams.stream().map(v -> DateUtils.dateTime(v.getReportDate())).distinct().collect(Collectors.toList());

		Map<String, List<MpElectrocardiogramExam>> electrocardiogramExamsMap = totalElectrocardiogramExams.stream().collect(Collectors.groupingBy(key -> DateUtils.dateTime(key.getReportDate())));

		HealthItemVO<List<CatalogVO>> healthItemVO = new HealthItemVO<List<CatalogVO>>();
		healthItemVO.setItemId(itemId.get());
		healthItemVO.setItemName("心电图");
//		healthItemVO.setItemName(reportDate+" "+HealthPatientCheckExamEnum.ECG.getValue());
		List<CatalogVO> result = new ArrayList<>();
		for(String reportDate: reportDates){
			List<MpElectrocardiogramExam> mpElectrocardiogramExams = electrocardiogramExamsMap.get(reportDate);
			Map<String, List<MpElectrocardiogramExam>> collect = mpElectrocardiogramExams.stream().distinct().filter(x->!StringUtils.isEmpty(x.getTestNo()))
					.collect(Collectors.groupingBy(MpElectrocardiogramExam::getTestNo));
			List<MpElectrocardiogramExam> towList = new ArrayList<>();
			collect.forEach((testNo,mpctExamList)->{
				MpElectrocardiogramExam two = new MpElectrocardiogramExam();
				MpElectrocardiogramExam mpElectrocardiogramExam = mpctExamList.get(0);
				BeanUtils.copyProperties(mpElectrocardiogramExam,two);
				String title = mpctExamList.stream().map(MpElectrocardiogramExam::getCheckSourceType).distinct().collect(Collectors.joining("、"));
				two.setCheckSourceType(title);
				two.setTechnologyName(title);
				if (!StringUtils.isEmpty(mpElectrocardiogramExam.getObjectiveView()) || !StringUtils.isEmpty(mpElectrocardiogramExam.getSubjectiveHints())) {
					String encode = MD5Util.encode(StringUtils.trim(mpElectrocardiogramExam.getObjectiveView()) + StringUtils.trim(mpElectrocardiogramExam.getSubjectiveHints()));
					two.setViewMd5(encode);
				}
				towList.add(two);
			});
			towList.stream().filter(x->!StringUtils.isEmpty(x.getViewMd5())).collect(Collectors.groupingBy(MpElectrocardiogramExam::getViewMd5)).forEach((viewMD5,mpctExamList)->{
				CatalogVO catalogVO = new CatalogVO();
				String title = mpctExamList.stream().map(MpElectrocardiogramExam::getCheckSourceType).distinct().collect(Collectors.joining("、"));
				catalogVO.setItemTitle(reportDate+" "+title);
				List<ItemValueVO> list = new ArrayList<ItemValueVO>();
				setItemValueVO("检查项目",title,list);
//				setItemValueVO("身体部位",mpElectrocardiogramExam.getSctPosition(),list);
//				setItemValueVO("检查方式",mpElectrocardiogramExam.getTestMethod(),list);
				setItemValueVO("所见",mpctExamList.get(0).getObjectiveView(),list);
				setItemValueVO("诊断意见",mpctExamList.get(0).getSubjectiveHints(),list);
//				setItemValueVO("检查诊断",mpElectrocardiogramExam.getRelevantClinicDiagnosis(),list);
				setItemValueVOHref("心电图报告",mpctExamList.get(0).getPdfReport(),list);
//				setItemValueVO("检查时间",DateUtils.dateTime(mpElectrocardiogramExam.getTechnologyDate()),list);
//				setItemValueVO("报告时间",DateUtils.dateTime(mpElectrocardiogramExam.getReportDate()),list);
				String deptName = mpctExamList.stream().map(MpElectrocardiogramExam::getDepartmentName).distinct().collect(Collectors.joining("、"));
					setItemValueVO("开单科室",getRuleDeptName(deptName),list);
				catalogVO.setItemList(list);
				result.add(catalogVO);
			});
		}
		healthItemVO.setObj(result);
		if (!CollectionUtils.isEmpty(totalElectrocardiogramExams)) {
			data.add(healthItemVO);
		}
		itemId.incrementAndGet();
	}

	public void previewArea(FTPClient ftpClient, HttpServletResponse response, String pdfReport) {
		// 获取远程文件的输入流
		try (OutputStream outputStream = response.getOutputStream();
			 InputStream inputStream = ftpClient.retrieveFileStream(pdfReport.substring(18))) {
			// 如果远程文件不存在，返回
			if (inputStream == null) {
				return;
			}
			// 将远程文件的内容写入HTTP响应中
			byte[] buffer = new byte[1024];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HealthItemVO queryMzDiagnosis(DetailForm detail) {
		// 通过patientId去查询personId
		if (StringUtils.isEmpty(detail.getPersonId())) {
			PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(Long.parseLong(detail.getPatientId()));
			if(patientInfo == null || StringUtils.isEmpty(patientInfo.getPersonId())){
				log.info("patientId = {} 在 patient_info 表查不到信息或者personId为空");
				return null;
			}
			detail.setPersonId(patientInfo.getPersonId());
		}
		//诊断
		HealthItemVO healthItemVO = getDiagnosis(detail.getPersonId(), null,"2");
		return healthItemVO;
	}

	/**
	 * 	更新患者手机号
	 * @param form 表单信息
	 * @return 更新结果
	 */
	@Transactional(rollbackFor = Exception.class)
	public String updateMobile(MobileForm form) {
	    // 获取表单中的personId
	    String personId = form.getPersonId();
	    // 如果personId为空，根据patientId查询患者信息
	    if (StringUtils.isEmpty(personId)) {
	        // 根据患者ID查询患者信息
	        PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(Long.parseLong(form.getPatientId()));
	        // 如果患者信息不存在或personId为空，记录日志并返回null
	        if (patientInfo == null || StringUtils.isEmpty(patientInfo.getPersonId())) {
	            log.info("patientId = {} 在 patient_info 表查不到信息或者personId为空", form.getPatientId());
	            return null;
	        }
	        // 获取患者信息中的personId
	        personId = patientInfo.getPersonId();
	    }
	    // 更新患者手机号
	    patientInfoMapper.updatePatientMobile(personId, form.getMobile());
	    mpPersonMapper.updateMobile(personId, form.getMobile());
	    // 返回更新成功消息
	    return "更新患者手机成功";
	}
}
