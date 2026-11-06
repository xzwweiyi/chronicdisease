package com.dchealth.compatibility.mapper;


import com.dchealth.compatibility.domain.MpCtExam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * CT检查Mapper接口
 * 
 * @author lchbu
 * @date 2023-12-13
 */
public interface MpCtExamMapper 
{
    /**
     * 查询CT检查
     * 
     * @param id CT检查主键
     * @return CT检查
     */
    public MpCtExam selectMpCtExamById(Long id);

    List<MpCtExam> selectMpCtExamList(@Param("personId")String personId, @Param("visitOccurrenceId")String visitOccurrenceId, @Param("visitType")String visitType);
}
