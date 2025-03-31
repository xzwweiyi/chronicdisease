package com.dchealth.compatibility.mapper;

import org.apache.ibatis.annotations.Param;

import com.dchealth.compatibility.domain.MpThyroidFunction;

import java.util.List;

/**
 * 甲状腺功能Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
public interface MpThyroidFunctionMapper 
{
    /**
     * 查询甲状腺功能
     * 
     * @param id 甲状腺功能主键
     * @return 甲状腺功能
     */
    public MpThyroidFunction selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

    List<MpThyroidFunction> selectByPersonId(String personId);

}
