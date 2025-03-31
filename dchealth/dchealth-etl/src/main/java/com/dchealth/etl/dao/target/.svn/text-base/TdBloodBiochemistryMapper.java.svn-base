package com.dchealth.etl.dao.target;

import com.dchealth.etl.domain.target.TdBloodBiochemistry;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 唐都-血液生化表(TdBloodBiochemistry)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-03 09:28:49
 */
public interface TdBloodBiochemistryMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdBloodBiochemistry queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param tdBloodBiochemistry 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<TdBloodBiochemistry> queryAllByLimit(TdBloodBiochemistry tdBloodBiochemistry, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param tdBloodBiochemistry 查询条件
     * @return 总行数
     */
    long count(TdBloodBiochemistry tdBloodBiochemistry);

    /**
     * 新增数据
     *
     * @param tdBloodBiochemistry 实例对象
     * @return 影响行数
     */
    int insert(TdBloodBiochemistry tdBloodBiochemistry);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdBloodBiochemistry> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TdBloodBiochemistry> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdBloodBiochemistry> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TdBloodBiochemistry> entities);

    /**
     * 修改数据
     *
     * @param tdBloodBiochemistry 实例对象
     * @return 影响行数
     */
    int update(TdBloodBiochemistry tdBloodBiochemistry);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

