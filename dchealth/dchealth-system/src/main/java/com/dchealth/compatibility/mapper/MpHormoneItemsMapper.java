package com.dchealth.compatibility.mapper;

import org.apache.ibatis.annotations.Param;

import com.dchealth.compatibility.domain.MpHormoneItems;

import java.util.List;

/**
 * 激素测定Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
public interface MpHormoneItemsMapper 
{
    /**
     * 查询激素测定
     * 
     * @param id 激素测定主键
     * @return 激素测定
     */
    public MpHormoneItems selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

    List<MpHormoneItems> selectByPersonId(String personId);

}
