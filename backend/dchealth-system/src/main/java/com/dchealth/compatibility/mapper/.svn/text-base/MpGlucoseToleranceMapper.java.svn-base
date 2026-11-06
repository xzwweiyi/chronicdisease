package com.dchealth.compatibility.mapper;

import com.dchealth.compatibility.domain.MpGlucoseTolerance;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 糖耐量试验Mapper接口
 * 
 * @author lchbu
 * @date 2024-01-22
 */
public interface MpGlucoseToleranceMapper 
{

    MpGlucoseTolerance selectByPersonIdAndVisitId(@Param("personId")String personId, @Param("visitOccurrenceId")String visitOccurrenceId);

    List<MpGlucoseTolerance> selectByPersonId(String personId);
}
