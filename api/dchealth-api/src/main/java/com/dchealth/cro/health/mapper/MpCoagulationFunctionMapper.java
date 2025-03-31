package com.dchealth.cro.health.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dchealth.cro.health.domain.MpCoagulationFunction;

import java.util.List;

/**
 * 凝血功能Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Repository
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
