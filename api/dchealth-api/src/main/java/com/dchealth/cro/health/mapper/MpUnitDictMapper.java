package com.dchealth.cro.health.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dchealth.cro.health.domain.MpUnitDict;

/**
 * 单位字典表(MpUnitDict)表数据库访问层
 *
 */
@Repository
public interface MpUnitDictMapper {


    List<MpUnitDict> queryUnitDictByTableName(@Param("tableName") String tableName);
}

