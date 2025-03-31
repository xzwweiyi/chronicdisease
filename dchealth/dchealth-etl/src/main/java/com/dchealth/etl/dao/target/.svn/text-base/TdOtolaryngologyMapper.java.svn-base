package com.dchealth.etl.dao.target;

import com.dchealth.etl.domain.target.TdOtolaryngology;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;

/**
 * 唐都-耳鼻喉相关表(TdOtolaryngology)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-07 14:32:35
 */
public interface TdOtolaryngologyMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdOtolaryngology queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param tdOtolaryngology 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<TdOtolaryngology> queryAllByLimit(TdOtolaryngology tdOtolaryngology, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param tdOtolaryngology 查询条件
     * @return 总行数
     */
    long count(TdOtolaryngology tdOtolaryngology);

    /**
     * 新增数据
     *
     * @param tdOtolaryngology 实例对象
     * @return 影响行数
     */
    int insert(TdOtolaryngology tdOtolaryngology);
    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdGeneralInspection> 实例对象列表
     * @return 影响行数
     */
    int insertBatchByMaps(@Param("entities") List<Map> entities);
    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdOtolaryngology> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TdOtolaryngology> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdOtolaryngology> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TdOtolaryngology> entities);

    /**
     * 修改数据
     *
     * @param tdOtolaryngology 实例对象
     * @return 影响行数
     */
    int update(TdOtolaryngology tdOtolaryngology);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

