package com.dchealth.compatibility.mapper;

import org.apache.ibatis.annotations.Param;

import com.dchealth.compatibility.domain.MpDrugExposure;

/**
 * 用药信息Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
public interface MpDrugExposureMapper 
{
    /**
     * 查询用药信息
     * 
     * @param id 用药信息主键
     * @return 用药信息
     */
    public MpDrugExposure selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

}
