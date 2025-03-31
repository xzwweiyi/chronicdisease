package com.dchealth.cro.health.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dchealth.cro.health.domain.MpBloodRoutine;

import java.util.List;

/**
 * 血常规Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Repository
public interface MpBloodRoutineMapper 
{
    /**
     * 查询血常规
     * 
     * @param id 血常规主键
     * @return 血常规
     */
    public MpBloodRoutine selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

    List<MpBloodRoutine> selectByPersonId(@Param("personId")String personId);
}
