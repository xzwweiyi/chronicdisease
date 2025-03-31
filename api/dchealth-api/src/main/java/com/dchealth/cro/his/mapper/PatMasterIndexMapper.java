package com.dchealth.cro.his.mapper;

import org.apache.ibatis.annotations.Param;

import com.dchealth.cro.his.domain.PatMasterIndex;

/**
 * 病人主索引患者信息表](PatMasterIndex)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-27 13:44:51
 */
public interface PatMasterIndexMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    PatMasterIndex selectByIdNoAndPatientId(@Param("idNo")String idNo,@Param("patientId")String patientId);

  

}