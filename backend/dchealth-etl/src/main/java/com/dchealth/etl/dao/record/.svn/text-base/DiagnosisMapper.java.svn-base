package com.dchealth.etl.dao.record;

import com.dchealth.etl.domain.record.Diagnosis;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 诊断记录(Diagnosis)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-04 16:23:44
 */
public interface DiagnosisMapper {


    /**
     * 查询指定行数据
     *
     * @param diagnosis 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<Diagnosis> queryAllByLimit(Diagnosis diagnosis, @Param("pageable") Pageable pageable);

    int querySyncInfoCount(Diagnosis diagnosis);
    /**
     * 查询指定行数据
     *
     * @param diagnosis 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<Diagnosis> querySyncInfoByPage(@Param("diagnosis") Diagnosis diagnosis, @Param("pageable") Pageable pageable);
    /**
     * 统计总行数
     *
     * @param diagnosis 查询条件
     * @return 总行数
     */
    long count(Diagnosis diagnosis);

    /**
     * 新增数据
     *
     * @param diagnosis 实例对象
     * @return 影响行数
     */
    int insert(Diagnosis diagnosis);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Diagnosis> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Diagnosis> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Diagnosis> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Diagnosis> entities);

    /**
     * 修改数据
     *
     * @param diagnosis 实例对象
     * @return 影响行数
     */
    int update(Diagnosis diagnosis);


}

