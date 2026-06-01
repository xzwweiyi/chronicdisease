package com.dchealth.cro.medical.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dchealth.cro.common.base.CommonResult;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.medical.domain.DoctorSatisfactionEvaluation;
import com.dchealth.cro.medical.form.SatisfactionForm;
import com.dchealth.cro.medical.mapper.DoctorSatisfactionEvaluationMapper;
import com.dchealth.cro.medical.vo.DoctorSatisfactionEvaluationVo;

@Service
public class SatisfactionService {
	@Autowired
	private DoctorSatisfactionEvaluationMapper doctorSatisfactionEvaluationMapper;


	@Transactional
	public DchealthCloudResponseEntity<CommonResult> add(DoctorSatisfactionEvaluationVo doctorSatisfactionEvaluationVo){
		DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
		CommonResult commonResult = new CommonResult();
		DoctorSatisfactionEvaluation doctorSatisfactionEvaluation = new DoctorSatisfactionEvaluation();
		doctorSatisfactionEvaluation.setAccountId(doctorSatisfactionEvaluationVo.getAccountIds());
		doctorSatisfactionEvaluation.setScore(doctorSatisfactionEvaluationVo.getScore());
		doctorSatisfactionEvaluation.setSuggestion(doctorSatisfactionEvaluationVo.getSuggestion());
		doctorSatisfactionEvaluation.setCreateTime(DateUtils.getNowDate());
		doctorSatisfactionEvaluationMapper.insertDoctorSatisfactionEvaluation(doctorSatisfactionEvaluation);
		commonResult.setResult(true);
		result.setData(commonResult);
		return result;
	}


	public DchealthCloudResponseEntity<List<DoctorSatisfactionEvaluation>> list(SatisfactionForm satisfactionForm) {
		DchealthCloudResponseEntity<List<DoctorSatisfactionEvaluation>> result = new DchealthCloudResponseEntity<List<DoctorSatisfactionEvaluation>>();
		List<DoctorSatisfactionEvaluation> list = doctorSatisfactionEvaluationMapper.selectListByForm(satisfactionForm);
		if (list != null && list.size() > 0) {
			for (DoctorSatisfactionEvaluation accountNotice : list) {
				DoctorSatisfactionEvaluation doctorSatisfactionEvaluation = new DoctorSatisfactionEvaluation();
				doctorSatisfactionEvaluation.setCreateTimeStr(DateUtils.dateTime(accountNotice.getCreateTime()));
			}
		}
		result.setData(list);
		return result;
	}

}
