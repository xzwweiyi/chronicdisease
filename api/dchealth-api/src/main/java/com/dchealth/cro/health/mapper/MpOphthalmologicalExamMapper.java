package com.dchealth.cro.health.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dchealth.cro.health.domain.MpOphthalmologicalExam;

/**
 * 眼科检查Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Repository
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
