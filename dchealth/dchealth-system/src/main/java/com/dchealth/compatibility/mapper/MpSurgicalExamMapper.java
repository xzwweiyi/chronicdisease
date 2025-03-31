package com.dchealth.compatibility.mapper;

import org.apache.ibatis.annotations.Param;

import com.dchealth.compatibility.domain.MpSurgicalExam;

/**
 * 外科检查Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
public interface MpSurgicalExamMapper 
{
    /**
     * 查询外科检查
     * 
     * @param id 外科检查主键
     * @return 外科检查
     */
    public MpSurgicalExam selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

}
