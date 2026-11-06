package com.dchealth.etl.dao.record;

import com.dchealth.etl.domain.record.ClinicMaster;
import com.dchealth.etl.domain.record.Orders;
import com.dchealth.etl.domain.record.OutpMedrec;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 就诊记录(ClinicMaster)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-18 15:34:47
 */
public interface ClinicMasterMapper {


    /**
     * 查询指定行数据
     *
     * @param clinicMaster 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ClinicMaster> queryAllByLimit(ClinicMaster clinicMaster, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param clinicMaster 查询条件
     * @return 总行数
     */
    long count(ClinicMaster clinicMaster);

    /**
     * 新增数据
     *
     * @param clinicMaster 实例对象
     * @return 影响行数
     */
    int insert(ClinicMaster clinicMaster);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ClinicMaster> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ClinicMaster> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ClinicMaster> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ClinicMaster> entities);

    /**
     * 修改数据
     *
     * @param clinicMaster 实例对象
     * @return 影响行数
     */
    int update(ClinicMaster clinicMaster);

    /**
     *查询当天同步数据量
     * @param clinicMaster
     * @return
     */
    int querySyncInfoCount(ClinicMaster clinicMaster);
    /**
     * 查询指定行数据
     *
     * @param clinicMaster 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ClinicMaster> querySyncInfoByPage(@Param("clinicMaster") ClinicMaster clinicMaster, @Param("pageable") Pageable pageable);
}

