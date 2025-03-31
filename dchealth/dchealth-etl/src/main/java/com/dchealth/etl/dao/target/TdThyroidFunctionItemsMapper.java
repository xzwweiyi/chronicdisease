package com.dchealth.etl.dao.target;

import com.dchealth.etl.domain.target.TdThyroidFunctionItems;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;

/**
 * 甲状腺功能项目表(TdThyroidFunctionItems)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-03 09:37:38
 */
public interface TdThyroidFunctionItemsMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdThyroidFunctionItems queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param tdThyroidFunctionItems 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<TdThyroidFunctionItems> queryAllByLimit(TdThyroidFunctionItems tdThyroidFunctionItems, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param tdThyroidFunctionItems 查询条件
     * @return 总行数
     */
    long count(TdThyroidFunctionItems tdThyroidFunctionItems);

    /**
     * 新增数据
     *
     * @param tdThyroidFunctionItems 实例对象
     * @return 影响行数
     */
    int insert(TdThyroidFunctionItems tdThyroidFunctionItems);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdThyroidFunctionItems> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TdThyroidFunctionItems> entities);
    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdGeneralInspection> 实例对象列表
     * @return 影响行数
     */
    int insertBatchByMaps(@Param("entities") List<Map> entities);
    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdThyroidFunctionItems> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TdThyroidFunctionItems> entities);

    /**
     * 修改数据
     *
     * @param tdThyroidFunctionItems 实例对象
     * @return 影响行数
     */
    int update(TdThyroidFunctionItems tdThyroidFunctionItems);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

