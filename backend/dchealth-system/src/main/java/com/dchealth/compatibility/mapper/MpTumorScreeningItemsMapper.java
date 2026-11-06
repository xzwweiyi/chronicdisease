package com.dchealth.compatibility.mapper;

import org.apache.ibatis.annotations.Param;

import com.dchealth.compatibility.domain.MpTumorScreeningItems;

import java.util.List;

/**
 * 肿瘤标志物Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
public interface MpTumorScreeningItemsMapper 
{
    /**
     * 查询肿瘤标志物
     * 
     * @param id 肿瘤标志物主键
     * @return 肿瘤标志物
     */
    public MpTumorScreeningItems selectByPersonIdAndVisitId(@Param("personId")String personId,@Param("visitOccurrenceId")String visitOccurrenceId);

    List<MpTumorScreeningItems> selectByPersonId(String personId);

}
