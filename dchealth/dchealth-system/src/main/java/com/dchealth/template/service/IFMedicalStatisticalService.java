package com.dchealth.template.service;

import com.dchealth.template.domain.FMedicalExamSubject;
import com.dchealth.template.domain.vo.ExamStatisticVo;

import java.util.List;


public interface IFMedicalStatisticalService {

   public List<ExamStatisticVo> queryStatisticalData(FMedicalExamSubject fMedicalExamSubject) throws Exception;


    List<ExamStatisticVo> queryAnalysisData(long id, ExamStatisticVo examStatisticVo);
}
