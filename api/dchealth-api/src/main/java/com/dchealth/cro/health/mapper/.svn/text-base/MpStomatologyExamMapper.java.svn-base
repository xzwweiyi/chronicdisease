package com.dchealth.cro.health.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dchealth.cro.health.domain.MpStomatologyExam;

/**
 * 口腔科检查Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Repository
public interface MpStomatologyExamMapper 
{
    /**
     * 查询口腔科检查
     * 
     * @param id 口腔科检查主键
     * @return 口腔科检查
     */
    public MpStomatologyExam selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

}
