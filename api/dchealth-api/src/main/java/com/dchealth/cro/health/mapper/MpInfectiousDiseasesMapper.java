package com.dchealth.cro.health.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dchealth.cro.health.domain.MpInfectiousDiseases;

import java.util.List;

/**
 * 常见传染病（代替乙肝）Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Repository
public interface MpInfectiousDiseasesMapper 
{
    /**
     * 查询常见传染病（代替乙肝）
     * 
     * @param id 常见传染病（代替乙肝）主键
     * @return 常见传染病（代替乙肝）
     */
    public MpInfectiousDiseases selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

    List<MpInfectiousDiseases> selectByPersonId(@Param("personId")String personId);
}
