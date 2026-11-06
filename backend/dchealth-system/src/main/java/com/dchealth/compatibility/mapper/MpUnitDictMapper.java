package com.dchealth.compatibility.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dchealth.compatibility.domain.MpUnitDict;

/**
 * 单位字典表(MpUnitDict)表数据库访问层
 *
 */
public interface MpUnitDictMapper {


    List<MpUnitDict> queryUnitDictByTableName(@Param("tableName") String tableName);
}

