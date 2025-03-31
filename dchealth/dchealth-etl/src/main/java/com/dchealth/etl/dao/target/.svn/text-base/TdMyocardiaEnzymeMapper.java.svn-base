package com.dchealth.etl.dao.target;

import com.dchealth.etl.domain.target.TdMyocardiaEnzyme;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;

/**
 * 唐都-心肌酶表(TdMyocardiaEnzyme)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-03 09:35:31
 */
public interface TdMyocardiaEnzymeMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdMyocardiaEnzyme queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param tdMyocardiaEnzyme 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<TdMyocardiaEnzyme> queryAllByLimit(TdMyocardiaEnzyme tdMyocardiaEnzyme, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param tdMyocardiaEnzyme 查询条件
     * @return 总行数
     */
    long count(TdMyocardiaEnzyme tdMyocardiaEnzyme);

    /**
     * 新增数据
     *
     * @param tdMyocardiaEnzyme 实例对象
     * @return 影响行数
     */
    int insert(TdMyocardiaEnzyme tdMyocardiaEnzyme);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdMyocardiaEnzyme> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TdMyocardiaEnzyme> entities);
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
     * @param entities List<TdMyocardiaEnzyme> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TdMyocardiaEnzyme> entities);

    /**
     * 修改数据
     *
     * @param tdMyocardiaEnzyme 实例对象
     * @return 影响行数
     */
    int update(TdMyocardiaEnzyme tdMyocardiaEnzyme);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

