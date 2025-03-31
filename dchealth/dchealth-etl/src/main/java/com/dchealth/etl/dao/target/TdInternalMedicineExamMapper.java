package com.dchealth.etl.dao.target;

import com.dchealth.etl.domain.target.TdInternalMedicineExam;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;

/**
 * 内科检查表(TdInternalMedicineExam)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-07 14:08:44
 */
public interface TdInternalMedicineExamMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdInternalMedicineExam queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param tdInternalMedicineExam 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<TdInternalMedicineExam> queryAllByLimit(TdInternalMedicineExam tdInternalMedicineExam, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param tdInternalMedicineExam 查询条件
     * @return 总行数
     */
    long count(TdInternalMedicineExam tdInternalMedicineExam);

    /**
     * 新增数据
     *
     * @param tdInternalMedicineExam 实例对象
     * @return 影响行数
     */
    int insert(TdInternalMedicineExam tdInternalMedicineExam);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdInternalMedicineExam> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TdInternalMedicineExam> entities);
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
     * @param entities List<TdInternalMedicineExam> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TdInternalMedicineExam> entities);

    /**
     * 修改数据
     *
     * @param tdInternalMedicineExam 实例对象
     * @return 影响行数
     */
    int update(TdInternalMedicineExam tdInternalMedicineExam);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

}

