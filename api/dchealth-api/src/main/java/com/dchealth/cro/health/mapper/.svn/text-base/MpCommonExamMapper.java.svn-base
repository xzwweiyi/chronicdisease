package com.dchealth.cro.health.mapper;


import com.dchealth.cro.health.domain.MpCommonExam;
import com.dchealth.cro.health.domain.MpCtExam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * CT检查Mapper接口
 * 
 * @author lchbu
 * @date 2023-12-13
 */
public interface MpCommonExamMapper
{
    List<MpCommonExam> selectMpCommonExamList(@Param("personId")String personId, @Param("visitOccurrenceId")String visitOccurrenceId, @Param("tableName")String tableName, @Param("visitType")String visitType);
}
