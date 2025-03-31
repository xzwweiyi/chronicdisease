package com.dchealth.cro.health.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dchealth.cro.health.domain.MpBloodGasAnalysis;

import java.util.List;

/**
 * 血气分析Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Repository
public interface MpBloodGasAnalysisMapper 
{
    /**
     * 查询血气分析
     * 
     * @param id 血气分析主键
     * @return 血气分析
     */
    public MpBloodGasAnalysis selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

    List<MpBloodGasAnalysis> selectByPersonId(String personId);
}
