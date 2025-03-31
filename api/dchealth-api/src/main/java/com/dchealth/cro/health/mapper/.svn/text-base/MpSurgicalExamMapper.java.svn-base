package com.dchealth.cro.health.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dchealth.cro.health.domain.MpSurgicalExam;

import java.util.List;

/**
 * 外科检查Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Repository
public interface MpSurgicalExamMapper 
{
    /**
     * 查询外科检查
     * 
     * @param id 外科检查主键
     * @return 外科检查
     */
    public MpSurgicalExam selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

    List<MpSurgicalExam> selectByPersonId(String personId);
}
