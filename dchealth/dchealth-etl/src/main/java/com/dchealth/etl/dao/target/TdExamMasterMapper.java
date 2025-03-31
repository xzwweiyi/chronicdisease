package com.dchealth.etl.dao.target;

import com.dchealth.etl.domain.target.TdExamMaster;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 检查主记录表(TdExamMaster)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-29 18:04:02
 */
public interface TdExamMasterMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdExamMaster queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param tdExamMaster 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<TdExamMaster> queryAllByLimit(TdExamMaster tdExamMaster, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param tdExamMaster 查询条件
     * @return 总行数
     */
    long count(TdExamMaster tdExamMaster);

    /**
     * 新增数据
     *
     * @param tdExamMaster 实例对象
     * @return 影响行数
     */
    int insert(TdExamMaster tdExamMaster);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdExamMaster> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TdExamMaster> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdExamMaster> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TdExamMaster> entities);

    /**
     * 修改数据
     *
     * @param tdExamMaster 实例对象
     * @return 影响行数
     */
    int update(TdExamMaster tdExamMaster);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

