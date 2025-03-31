package com.dchealth.compatibility.mapper;

import com.dchealth.compatibility.domain.MpXRayExam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * X线检查Mapper接口
 * 
 * @author lchbu
 * @date 2023-12-13
 */
public interface MpXRayExamMapper 
{
    /**
     * 查询X线检查
     * 
     * @param id X线检查主键
     * @return X线检查
     */
    public MpXRayExam selectMpXRayExamById(Long id);


    List<MpXRayExam> selectMpXRayExamList(@Param("personId")String personId, @Param("visitOccurrenceId")String visitOccurrenceId,@Param("visitType")String visitType);

}
