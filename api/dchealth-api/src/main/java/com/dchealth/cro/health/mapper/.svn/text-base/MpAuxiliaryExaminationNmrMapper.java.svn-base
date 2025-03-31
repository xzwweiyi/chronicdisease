package com.dchealth.cro.health.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dchealth.cro.health.domain.MpAuxiliaryExaminationNmr;

/**
 * 唐都-辅助检查-核磁项目相关Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Repository
public interface MpAuxiliaryExaminationNmrMapper 
{
    /**
     * 查询唐都-辅助检查-核磁项目相关
     * 
     * @param id 唐都-辅助检查-核磁项目相关主键
     * @return 唐都-辅助检查-核磁项目相关
     */
    public MpAuxiliaryExaminationNmr selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

}
