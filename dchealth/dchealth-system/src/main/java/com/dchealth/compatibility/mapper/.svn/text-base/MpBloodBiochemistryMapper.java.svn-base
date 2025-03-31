package com.dchealth.compatibility.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dchealth.compatibility.domain.MpBloodBiochemistry;

/**
 * 血液生化Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
public interface MpBloodBiochemistryMapper 
{
    public MpBloodBiochemistry selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

    List<MpBloodBiochemistry> selectByPersonId(String personId);

}
