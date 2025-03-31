package com.dchealth.cro.health.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dchealth.cro.health.domain.MpElectrocardiogramExam;

/**
 * 心电图检查Mapper接口
 * 
 * @author lchbu
 * @date 2023-12-13
 */
public interface MpElectrocardiogramExamMapper 
{
    /**
     * 查询心电图检查
     *
     * @param id 心电图检查主键
     * @return 心电图检查
     */
    public MpElectrocardiogramExam selectMpElectrocardiogramExamById(Long id);

    List<MpElectrocardiogramExam> selectMpElectrocardiogramExamList(@Param("personId")String personId, @Param("visitOccurrenceId")String visitOccurrenceId,@Param("visitType")String visitType);

}
