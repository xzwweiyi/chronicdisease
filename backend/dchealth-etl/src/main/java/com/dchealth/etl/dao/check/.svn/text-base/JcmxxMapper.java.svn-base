package com.dchealth.etl.dao.check;

import com.dchealth.etl.domain.check.Jcmxx;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 查明细项目(Jcmxx)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-22 17:39:49
 */

@Repository
public interface JcmxxMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    Jcmxx queryById( );

    /**
     * 查询指定行数据
     *
     * @param jcmxx 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<Jcmxx> queryAllByLimit(Jcmxx jcmxx, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param jcmxx 查询条件
     * @return 总行数
     */
    long count(Jcmxx jcmxx);

    /**
     * 新增数据
     *
     * @param jcmxx 实例对象
     * @return 影响行数
     */
    int insert(Jcmxx jcmxx);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Jcmxx> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Jcmxx> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Jcmxx> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Jcmxx> entities);

    /**
     * 修改数据
     *
     * @param jcmxx 实例对象
     * @return 影响行数
     */
    int update(Jcmxx jcmxx);

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 影响行数
     */
    int deleteById( );

}

