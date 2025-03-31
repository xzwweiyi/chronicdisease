package com.dchealth.cro.health.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dchealth.cro.health.domain.MpStoolRoutine;

import java.util.List;

/**
 * 便常规Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Repository
public interface MpStoolRoutineMapper 
{
    /**
     * 查询便常规
     * 
     * @param id 便常规主键
     * @return 便常规
     */
    public MpStoolRoutine selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

    List<MpStoolRoutine> selectByPersonId(@Param("personId")String personId);
}
