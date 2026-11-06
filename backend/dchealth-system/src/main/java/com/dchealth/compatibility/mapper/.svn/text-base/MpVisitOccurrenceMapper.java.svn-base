package com.dchealth.compatibility.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dchealth.compatibility.domain.MpVisitOccurrence;
import com.dchealth.compatibility.from.HealthListSearchForm;

/**
 * 就诊信息Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
public interface MpVisitOccurrenceMapper 
{

    
    public  List<MpVisitOccurrence>  selectPatientHealthList(HealthListSearchForm  healthListSearchForm);
    public  MpVisitOccurrence  selectVisitByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);
}
