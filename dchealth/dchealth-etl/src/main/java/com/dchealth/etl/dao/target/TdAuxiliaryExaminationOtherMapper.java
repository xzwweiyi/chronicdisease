package com.dchealth.etl.dao.target;

import com.dchealth.etl.domain.target.TdAuxiliaryExaminationOther;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;

/**
 * 唐都-辅助检查-心电图等其他项目表(TdAuxiliaryExaminationOther)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-29 16:20:11
 */
public interface TdAuxiliaryExaminationOtherMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdAuxiliaryExaminationOther queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param tdAuxiliaryExaminationOther 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<TdAuxiliaryExaminationOther> queryAllByLimit(TdAuxiliaryExaminationOther tdAuxiliaryExaminationOther, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param tdAuxiliaryExaminationOther 查询条件
     * @return 总行数
     */
    long count(TdAuxiliaryExaminationOther tdAuxiliaryExaminationOther);

    /**
     * 新增数据
     *
     * @param tdAuxiliaryExaminationOther 实例对象
     * @return 影响行数
     */
    int insert(TdAuxiliaryExaminationOther tdAuxiliaryExaminationOther);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdAuxiliaryExaminationOther> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TdAuxiliaryExaminationOther> entities);
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
     * @param entities List<TdAuxiliaryExaminationOther> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TdAuxiliaryExaminationOther> entities);

    /**
     * 修改数据
     *
     * @param tdAuxiliaryExaminationOther 实例对象
     * @return 影响行数
     */
    int update(TdAuxiliaryExaminationOther tdAuxiliaryExaminationOther);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

