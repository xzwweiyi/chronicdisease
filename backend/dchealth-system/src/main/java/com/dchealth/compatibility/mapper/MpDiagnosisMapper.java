package com.dchealth.compatibility.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dchealth.compatibility.domain.MpDiagnosis;

/**
 * 诊断记录Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
public interface MpDiagnosisMapper 
{
    /**
     * 查询诊断记录
     * 
     * @param id 诊断记录主键
     * @return 诊断记录
     */
    public List<MpDiagnosis> selectByPersonIdAndVisitId(@Param("personId")String personId,
                                                        @Param("visitOccurrenceId")String visitOccurrenceId,
                                                        @Param("visitType")String visitType);

    /**
     * 按诊断名称查询患者ID列表（用于疾病筛选）
     */
    List<String> selectPersonIdsByConditionName(@Param("conditionName") String conditionName);

    /**
     * 查询合并症患者（同时患有2种及以上慢病）的 person_id 列表
     */
    List<String> selectPersonIdsWithComorbidity();

    /**
     * 查询患者的诊断记录列表
     */
    List<MpDiagnosis> selectByPersonId(@Param("personId") String personId);

}
