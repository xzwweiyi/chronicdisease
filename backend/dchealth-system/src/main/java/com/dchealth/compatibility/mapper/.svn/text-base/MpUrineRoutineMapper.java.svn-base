package com.dchealth.compatibility.mapper;

import org.apache.ibatis.annotations.Param;

import com.dchealth.compatibility.domain.MpUrineRoutine;

import java.util.List;

/**
 * 尿常规Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
public interface MpUrineRoutineMapper 
{
    /**
     * 查询尿常规
     * 
     * @param id 尿常规主键
     * @return 尿常规
     */
    public MpUrineRoutine selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);;

    List<MpUrineRoutine> selectByPersonId(@Param("personId")String personId);

}
