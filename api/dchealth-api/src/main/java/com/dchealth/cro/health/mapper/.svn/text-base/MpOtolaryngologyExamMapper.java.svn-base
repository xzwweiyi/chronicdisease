package com.dchealth.cro.health.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dchealth.cro.health.domain.MpOtolaryngologyExam;

/**
 * 耳鼻喉科检查Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Repository
public interface MpOtolaryngologyExamMapper 
{
    /**
     * 查询耳鼻喉科检查
     * 
     * @param id 耳鼻喉科检查主键
     * @return 耳鼻喉科检查
     */
    public MpOtolaryngologyExam selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

   
}
