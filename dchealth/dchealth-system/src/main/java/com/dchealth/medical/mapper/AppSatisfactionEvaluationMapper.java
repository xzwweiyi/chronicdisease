package com.dchealth.medical.mapper;

import com.dchealth.medical.domain.AppSatisfactionEvaluation;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * app 满意度评价(AppSatisfactionEvaluation)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-26 14:53:42
 */
public interface AppSatisfactionEvaluationMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AppSatisfactionEvaluation queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param appSatisfactionEvaluation 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<AppSatisfactionEvaluation> queryAllByLimit(@Param("appSatisfactionEvaluation")AppSatisfactionEvaluation appSatisfactionEvaluation, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param appSatisfactionEvaluation 查询条件
     * @return 总行数
     */
    long count(AppSatisfactionEvaluation appSatisfactionEvaluation);

    /**
     * 新增数据
     *
     * @param appSatisfactionEvaluation 实例对象
     * @return 影响行数
     */
    int insert(AppSatisfactionEvaluation appSatisfactionEvaluation);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppSatisfactionEvaluation> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AppSatisfactionEvaluation> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppSatisfactionEvaluation> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<AppSatisfactionEvaluation> entities);

    /**
     * 修改数据
     *
     * @param appSatisfactionEvaluation 实例对象
     * @return 影响行数
     */
    int update(AppSatisfactionEvaluation appSatisfactionEvaluation);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    public List<AppSatisfactionEvaluation> selectEvaluationList(AppSatisfactionEvaluation appSatisfactionEvaluation);

    public Map queryEvaluation(AppSatisfactionEvaluation appSatisfactionEvaluation);
}

