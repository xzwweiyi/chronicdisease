package com.dchealth.etl.dao.record;

import com.dchealth.etl.domain.record.DrugPrescMaster;
import com.dchealth.etl.domain.record.Orders;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 药品处方主记录(DrugPrescMaster)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-24 17:53:50
 */
public interface DrugPrescMasterMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param
     * @return 实例对象
     */
    DrugPrescMaster queryById( );

    /**
     * 查询指定行数据
     *
     * @param drugPrescMaster 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<DrugPrescMaster> queryAllByLimit(DrugPrescMaster drugPrescMaster, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param drugPrescMaster 查询条件
     * @return 总行数
     */
    long count(DrugPrescMaster drugPrescMaster);
    /**
     *查询当天同步数据量
     * @param drugPrescMaster
     * @return
     */
    int querySyncInfoCount(DrugPrescMaster drugPrescMaster);
    /**
     * 查询指定行数据
     *
     * @param drugPrescMaster 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<DrugPrescMaster> querySyncInfoByPage(@Param("drugPrescMaster") DrugPrescMaster drugPrescMaster, @Param("pageable") Pageable pageable);
    /**
     * 新增数据
     *
     * @param drugPrescMaster 实例对象
     * @return 影响行数
     */
    int insert(DrugPrescMaster drugPrescMaster);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DrugPrescMaster> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DrugPrescMaster> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DrugPrescMaster> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<DrugPrescMaster> entities);

    /**
     * 修改数据
     *
     * @param drugPrescMaster 实例对象
     * @return 影响行数
     */
    int update(DrugPrescMaster drugPrescMaster);

    /**
     * 通过主键删除数据
     *
     * @param
     * @return 影响行数
     */
    int deleteById( );

}

