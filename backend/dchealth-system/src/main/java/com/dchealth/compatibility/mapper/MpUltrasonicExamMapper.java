package com.dchealth.compatibility.mapper;


import com.dchealth.compatibility.domain.MpUltrasonicExam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 超声检查Mapper接口
 * 
 * @author lchbu
 * @date 2023-12-13
 */
public interface MpUltrasonicExamMapper 
{
    /**
     * 查询超声检查
     * 
     * @param id 超声检查主键
     * @return 超声检查
     */
    public MpUltrasonicExam selectMpUltrasonicExamById(Long id);

    List<MpUltrasonicExam> selectMpUltrasonicExamList(@Param("personId")String personId, @Param("visitOccurrenceId")String visitOccurrenceId,@Param("visitType")String visitType);

}
