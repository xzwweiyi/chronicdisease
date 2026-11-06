package com.dchealth.compatibility.mapper;

import org.apache.ibatis.annotations.Param;

import com.dchealth.compatibility.domain.MpStoolRoutine;

import java.util.List;

/**
 * 便常规Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
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
