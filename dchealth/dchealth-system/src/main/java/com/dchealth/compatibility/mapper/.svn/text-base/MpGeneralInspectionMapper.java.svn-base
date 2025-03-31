package com.dchealth.compatibility.mapper;

import org.apache.ibatis.annotations.Param;

import com.dchealth.compatibility.domain.MpGeneralInspection;

import java.util.List;

/**
 * 一般检查Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
public interface MpGeneralInspectionMapper 
{
    /**
     * 查询一般检查
     * 
     * @param id 一般检查主键
     * @return 一般检查
     */
    public MpGeneralInspection selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);
    public List<MpGeneralInspection>  selectByPersonIdAndVisitList(@Param("personId")String personId, @Param("visitOccurrenceId")String visitOccurrenceId);

   
}
