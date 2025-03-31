package com.dchealth.etl.dao.target;

import com.dchealth.etl.domain.target.TdAuxiliaryExaminationUltrasound;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;

/**
 * 唐都-辅助检查-超声项目相关表(TdAuxiliaryExaminationUltrasound)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-29 16:20:37
 */
public interface TdAuxiliaryExaminationUltrasoundMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdAuxiliaryExaminationUltrasound queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param tdAuxiliaryExaminationUltrasound 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<TdAuxiliaryExaminationUltrasound> queryAllByLimit(TdAuxiliaryExaminationUltrasound tdAuxiliaryExaminationUltrasound, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param tdAuxiliaryExaminationUltrasound 查询条件
     * @return 总行数
     */
    long count(TdAuxiliaryExaminationUltrasound tdAuxiliaryExaminationUltrasound);

    /**
     * 新增数据
     *
     * @param tdAuxiliaryExaminationUltrasound 实例对象
     * @return 影响行数
     */
    int insert(TdAuxiliaryExaminationUltrasound tdAuxiliaryExaminationUltrasound);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdAuxiliaryExaminationUltrasound> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TdAuxiliaryExaminationUltrasound> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdAuxiliaryExaminationUltrasound> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TdAuxiliaryExaminationUltrasound> entities);

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
     * @param tdAuxiliaryExaminationUltrasound 实例对象
     * @return 影响行数
     */
    int update(TdAuxiliaryExaminationUltrasound tdAuxiliaryExaminationUltrasound);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

