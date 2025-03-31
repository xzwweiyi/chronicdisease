package com.dchealth.cro.health.mapper;

import com.dchealth.cro.health.domain.MpNuclearMagnetismExam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 磁共振检查Mapper接口
 * 
 * @author lchbu
 * @date 2023-12-13
 */
public interface MpNuclearMagnetismExamMapper 
{
    /**
     * 查询磁共振检查
     * 
     * @param id 磁共振检查主键
     * @return 磁共振检查
     */
    public MpNuclearMagnetismExam selectMpNuclearMagnetismExamById(Long id);

    List<MpNuclearMagnetismExam> selectMpNuclearMagnetismExamList(@Param("personId")String personId, @Param("visitOccurrenceId")String visitOccurrenceId, @Param("visitType")String visitType);

}
