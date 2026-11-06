package com.dchealth.compatibility.mapper;

import org.apache.ibatis.annotations.Param;

import com.dchealth.compatibility.domain.MpCoagulationFunction;

import java.util.List;

/**
 * 凝血功能Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
public interface MpCoagulationFunctionMapper 
{
    /**
     * 查询凝血功能
     * 
     * @param id 凝血功能主键
     * @return 凝血功能
     */
    public MpCoagulationFunction selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

    List<MpCoagulationFunction> selectByPersonId(@Param("personId")String personId);

}
