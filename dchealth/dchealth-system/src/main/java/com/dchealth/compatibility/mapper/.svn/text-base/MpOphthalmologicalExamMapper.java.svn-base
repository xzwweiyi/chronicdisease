package com.dchealth.compatibility.mapper;

import org.apache.ibatis.annotations.Param;

import com.dchealth.compatibility.domain.MpOphthalmologicalExam;

/**
 * 眼科检查Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
public interface MpOphthalmologicalExamMapper 
{
    /**
     * 查询眼科检查
     * 
     * @param id 眼科检查主键
     * @return 眼科检查
     */
    public MpOphthalmologicalExam selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

}
