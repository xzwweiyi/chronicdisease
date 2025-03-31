package com.dchealth.etl.dao.record;

import com.dchealth.etl.domain.record.DrugPrescDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * (DrugPrescDetail)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-24 17:54:22
 */
public interface DrugPrescDetailMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    DrugPrescDetail queryById( );

    /**
     * 查询指定行数据
     *
     * @param drugPrescDetail 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<DrugPrescDetail> queryAllByLimit(DrugPrescDetail drugPrescDetail, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param drugPrescDetail 查询条件
     * @return 总行数
     */
    long count(DrugPrescDetail drugPrescDetail);
    /**
     *查询当天同步数据量
     * @param drugPrescDetail
     * @return
     */
    int querySyncInfoCount(DrugPrescDetail drugPrescDetail);
    /**
     * 查询指定行数据
     *
     * @param drugPrescDetail 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<DrugPrescDetail> querySyncInfoByPage(@Param("drugPrescDetail") DrugPrescDetail drugPrescDetail, @Param("pageable") Pageable pageable);
    /**
     * 新增数据
     *
     * @param drugPrescDetail 实例对象
     * @return 影响行数
     */
    int insert(DrugPrescDetail drugPrescDetail);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DrugPrescDetail> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DrugPrescDetail> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DrugPrescDetail> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<DrugPrescDetail> entities);

    /**
     * 修改数据
     *
     * @param drugPrescDetail 实例对象
     * @return 影响行数
     */
    int update(DrugPrescDetail drugPrescDetail);

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 影响行数
     */
    int deleteById( );

}

