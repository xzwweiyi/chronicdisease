package com.dchealth.cro.health.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dchealth.cro.health.domain.MpAuxiliaryExaminationOther;

/**
 * 唐都-辅助检查-心电图等其他项目Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Repository
public interface MpAuxiliaryExaminationOtherMapper 
{
    /**
     * 查询唐都-辅助检查-心电图等其他项目
     * 
     * @param id 唐都-辅助检查-心电图等其他项目主键
     * @return 唐都-辅助检查-心电图等其他项目
     */
    public MpAuxiliaryExaminationOther selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

   
}
