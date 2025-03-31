package com.dchealth.cro.health.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dchealth.cro.health.domain.MpVisitOccurrence;
import com.dchealth.cro.health.form.HealthListSearchForm;

/**
 * 就诊信息Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Repository
public interface MpVisitOccurrenceMapper 
{

    
    public  List<MpVisitOccurrence>  selectPatientHealthList(HealthListSearchForm  healthListSearchForm);
    public  MpVisitOccurrence  selectVisitByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);
}
