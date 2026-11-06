package com.dchealth.etl.dao.check;

import com.dchealth.etl.domain.check.Jcxx;
import com.dchealth.etl.domain.check.ViewJcxx;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 患者基本信息(ViewJcxx)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-06 11:34:37
 */
public interface ViewJcxxMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ViewJcxx queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param viewJcxx 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ViewJcxx> queryAllByLimit(ViewJcxx viewJcxx, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param viewJcxx 查询条件
     * @return 总行数
     */
    long count(ViewJcxx viewJcxx);
    /**
     * 查询指定行数据
     *
     * @param jcxx 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ViewJcxx> querySyncInfoByPage(@Param("jcxx") ViewJcxx jcxx, @Param("pageable") Pageable pageable);
    /**
     * 新增数据
     *
     * @param viewJcxx 实例对象
     * @return 影响行数
     */
    int insert(ViewJcxx viewJcxx);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ViewJcxx> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ViewJcxx> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ViewJcxx> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ViewJcxx> entities);

    /**
     * 修改数据
     *
     * @param viewJcxx 实例对象
     * @return 影响行数
     */
    int update(ViewJcxx viewJcxx);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

}

