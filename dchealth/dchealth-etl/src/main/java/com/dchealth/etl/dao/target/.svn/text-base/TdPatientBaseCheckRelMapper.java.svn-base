package com.dchealth.etl.dao.target;

import com.dchealth.etl.domain.target.TdPatientBaseCheckRel;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 基础信息表和体检关系表(TdPatientBaseCheckRel)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-06 17:24:12
 */
public interface TdPatientBaseCheckRelMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdPatientBaseCheckRel queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param tdPatientBaseCheckRel 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<TdPatientBaseCheckRel> queryAllByLimit(TdPatientBaseCheckRel tdPatientBaseCheckRel, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param tdPatientBaseCheckRel 查询条件
     * @return 总行数
     */
    long count(TdPatientBaseCheckRel tdPatientBaseCheckRel);

    /**
     * 新增数据
     *
     * @param tdPatientBaseCheckRel 实例对象
     * @return 影响行数
     */
    int insert(TdPatientBaseCheckRel tdPatientBaseCheckRel);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdPatientBaseCheckRel> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TdPatientBaseCheckRel> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdPatientBaseCheckRel> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TdPatientBaseCheckRel> entities);

    /**
     * 修改数据
     *
     * @param tdPatientBaseCheckRel 实例对象
     * @return 影响行数
     */
    int update(TdPatientBaseCheckRel tdPatientBaseCheckRel);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

