package com.dchealth.etl.dao.target;

import com.dchealth.etl.domain.target.TdOrders;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 医嘱(TdOrders)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-24 17:41:59
 */
public interface TdOrdersMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdOrders queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param tdOrders 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<TdOrders> queryAllByLimit(TdOrders tdOrders, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param tdOrders 查询条件
     * @return 总行数
     */
    long count(TdOrders tdOrders);

    /**
     * 新增数据
     *
     * @param tdOrders 实例对象
     * @return 影响行数
     */
    int insert(TdOrders tdOrders);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdOrders> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TdOrders> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdOrders> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TdOrders> entities);

    /**
     * 修改数据
     *
     * @param tdOrders 实例对象
     * @return 影响行数
     */
    int update(TdOrders tdOrders);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

