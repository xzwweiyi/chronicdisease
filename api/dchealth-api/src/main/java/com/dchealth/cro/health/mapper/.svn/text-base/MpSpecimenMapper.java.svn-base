package com.dchealth.cro.health.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dchealth.cro.health.domain.MpSpecimen;

/**
 * 病理信息Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Repository
public interface MpSpecimenMapper 
{
    /**
     * 查询病理信息
     * 
     * @param id 病理信息主键
     * @return 病理信息
     */
    public MpSpecimen selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

}
