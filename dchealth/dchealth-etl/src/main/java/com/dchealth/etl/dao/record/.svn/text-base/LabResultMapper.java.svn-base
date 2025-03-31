package com.dchealth.etl.dao.record;

import com.dchealth.etl.domain.record.LabResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 检验结果(LabResult)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-31 17:13:05
 */
public interface LabResultMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param  testNo
     * @return 实例对象
     */
    LabResult queryById(String testNo);

    /**
     * 查询指定行数据
     *
     * @param labResult 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<LabResult> queryAllByLimit(LabResult labResult, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param labResult 查询条件
     * @return 总行数
     */
    long count(LabResult labResult);

    /**
     *查询当天同步数据量
     * @param labResult
     * @return
     */
    int querySyncInfoCount(LabResult labResult);
    /**
     * 查询指定行数据
     *
     * @param labResult 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<LabResult> querySyncInfoByPage(@Param("labResult") LabResult labResult, @Param("pageable") Pageable pageable);
    /**
     * 新增数据
     *
     * @param labResult 实例对象
     * @return 影响行数
     */
    int insert(LabResult labResult);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<LabResult> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<LabResult> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<LabResult> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<LabResult> entities);

    /**
     * 修改数据
     *
     * @param labResult 实例对象
     * @return 影响行数
     */
    int update(LabResult labResult);

    /**
     * 通过主键删除数据
     *
     * @param  testNo
     * @return 影响行数
     */
    int deleteById(String testNo);

}

