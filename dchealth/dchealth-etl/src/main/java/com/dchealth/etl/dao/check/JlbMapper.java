package com.dchealth.etl.dao.check;

import com.dchealth.etl.domain.check.Jlb;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 结论表(Jlb)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-22 17:46:18
 */
@Repository
public interface JlbMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    Jlb queryById( );

    /**
     * 查询指定行数据
     *
     * @param jlb 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<Jlb> queryAllByLimit(Jlb jlb, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param jlb 查询条件
     * @return 总行数
     */
    long count(Jlb jlb);
    /**
     * 统计总行数
     *
     * @param jlb 查询条件
     * @return 总行数
     */
    long querySyncInfoCount(Jlb jlb);

    /**
     * 查询指定行数据
     *
     * @param jlb      查询条件
     * @param pageable 分页对象
     * @return 对象列表
     */
    List<Jlb> querySyncInfoByPage(@Param("jlb") Jlb jlb, @Param("pageable") Pageable pageable);

    /**
     * 查询指定行数据
     *
     * @param params      查询条件
     * @return 对象列表
     */
    List<Jlb> querySyncInfosByPage(@Param("params") Map params);
    /**
     * 新增数据
     *
     * @param jlb 实例对象
     * @return 影响行数
     */
    int insert(Jlb jlb);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Jlb> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Jlb> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Jlb> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Jlb> entities);

    /**
     * 修改数据
     *
     * @param jlb 实例对象
     * @return 影响行数
     */
    int update(Jlb jlb);

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 影响行数
     */
    int deleteById( );

}

