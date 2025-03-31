package com.dchealth.etl.dao.target;

import com.dchealth.etl.domain.target.TdDrugPrescDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 药品处方明细记录(TdDrugPrescDetail)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-24 17:43:08
 */
public interface TdDrugPrescDetailMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdDrugPrescDetail queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param tdDrugPrescDetail 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<TdDrugPrescDetail> queryAllByLimit(TdDrugPrescDetail tdDrugPrescDetail, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param tdDrugPrescDetail 查询条件
     * @return 总行数
     */
    long count(TdDrugPrescDetail tdDrugPrescDetail);

    /**
     * 新增数据
     *
     * @param tdDrugPrescDetail 实例对象
     * @return 影响行数
     */
    int insert(TdDrugPrescDetail tdDrugPrescDetail);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdDrugPrescDetail> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TdDrugPrescDetail> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdDrugPrescDetail> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TdDrugPrescDetail> entities);

    /**
     * 修改数据
     *
     * @param tdDrugPrescDetail 实例对象
     * @return 影响行数
     */
    int update(TdDrugPrescDetail tdDrugPrescDetail);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

