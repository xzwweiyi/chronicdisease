package com.dchealth.cro.health.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dchealth.cro.health.domain.MpInternalMedicineExam;

import java.util.List;

/**
 * 内科检查Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Repository
public interface MpInternalMedicineExamMapper 
{
    /**
     * 查询内科检查
     * 
     * @param id 内科检查主键
     * @return 内科检查
     */
    public MpInternalMedicineExam selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

    List<MpInternalMedicineExam> selectByPersonId(String personId);
}
