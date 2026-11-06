package com.dchealth.compatibility.mapper;

import org.apache.ibatis.annotations.Param;

import com.dchealth.compatibility.domain.MpProcedureOccurrence;

/**
 * 手术信息Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
public interface MpProcedureOccurrenceMapper 
{
    /**
     * 查询手术信息
     * 
     * @param id 手术信息主键
     * @return 手术信息
     */
    public MpProcedureOccurrence selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

}
