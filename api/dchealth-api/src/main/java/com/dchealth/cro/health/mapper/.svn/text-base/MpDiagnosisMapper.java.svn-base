package com.dchealth.cro.health.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dchealth.cro.health.domain.MpDiagnosis;

/**
 * 诊断记录Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Repository
public interface MpDiagnosisMapper 
{
    /**
     * 查询诊断记录
     * 
     * @param id 诊断记录主键
     * @return 诊断记录
     */
    public List<MpDiagnosis> selectByPersonIdAndVisitId(@Param("personId")String personId,
                                                        @Param("visitOccurrenceId")String visitOccurrenceId,
                                                        @Param("visitType")String visitType);

}
