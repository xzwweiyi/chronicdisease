package com.dchealth.cro.health.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dchealth.cro.health.domain.MpAuxiliaryExaminationUltrasound;

/**
 * 唐都-辅助检查-超声项目相关Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Repository
public interface MpAuxiliaryExaminationUltrasoundMapper 
{
    /**
     * 查询唐都-辅助检查-超声项目相关
     * 
     * @param id 唐都-辅助检查-超声项目相关主键
     * @return 唐都-辅助检查-超声项目相关
     */
    public MpAuxiliaryExaminationUltrasound selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

}
