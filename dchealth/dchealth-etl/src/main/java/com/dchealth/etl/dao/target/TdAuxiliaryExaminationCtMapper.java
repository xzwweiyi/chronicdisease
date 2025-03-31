package com.dchealth.etl.dao.target;

import com.dchealth.etl.domain.target.TdAuxiliaryExaminationCt;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;

/**
 * 唐都-辅助检查-ct项目相关表(TdAuxiliaryExaminationCt)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-29 16:14:32
 */
public interface TdAuxiliaryExaminationCtMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdAuxiliaryExaminationCt queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param tdAuxiliaryExaminationCt 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<TdAuxiliaryExaminationCt> queryAllByLimit(TdAuxiliaryExaminationCt tdAuxiliaryExaminationCt, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param tdAuxiliaryExaminationCt 查询条件
     * @return 总行数
     */
    long count(TdAuxiliaryExaminationCt tdAuxiliaryExaminationCt);

    /**
     * 新增数据
     *
     * @param tdAuxiliaryExaminationCt 实例对象
     * @return 影响行数
     */
    int insert(TdAuxiliaryExaminationCt tdAuxiliaryExaminationCt);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdAuxiliaryExaminationCt> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TdAuxiliaryExaminationCt> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdAuxiliaryExaminationCt> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TdAuxiliaryExaminationCt> entities);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdGeneralInspection> 实例对象列表
     * @return 影响行数
     */
    int insertBatchByMaps(@Param("entities") List<Map> entities);

    /**
     * 修改数据
     *
     * @param tdAuxiliaryExaminationCt 实例对象
     * @return 影响行数
     */
    int update(TdAuxiliaryExaminationCt tdAuxiliaryExaminationCt);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

