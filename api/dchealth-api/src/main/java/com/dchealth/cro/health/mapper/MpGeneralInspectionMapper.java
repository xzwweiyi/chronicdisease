package com.dchealth.cro.health.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dchealth.cro.health.domain.MpGeneralInspection;

/**
 * 一般检查Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Repository
public interface MpGeneralInspectionMapper 
{
    /**
     * 查询一般检查
     * 
     * @param id 一般检查主键
     * @return 一般检查
     */
    public MpGeneralInspection selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

   
}
