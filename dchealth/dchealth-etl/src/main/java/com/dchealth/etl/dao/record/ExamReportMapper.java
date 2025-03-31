package com.dchealth.etl.dao.record;

import com.dchealth.etl.domain.record.ExamReport;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 检查报告表
此表描述检查报告内容，由检查执行科室生成。
主键：申请序号。(ExamReport)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-30 17:27:22
 */
public interface ExamReportMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param examNo 主键
     * @return 实例对象
     */
    ExamReport queryById(String examNo);

    /**
     * 查询指定行数据
     *
     * @param examReport 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ExamReport> queryAllByLimit(ExamReport examReport, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param examReport 查询条件
     * @return 总行数
     */
    long count(ExamReport examReport);
    /**
     * 查询指定行数据
     *
     * @param examReport 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ExamReport> querySyncInfoByPage(@Param("examReport") ExamReport examReport, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param examReport 查询条件
     * @return 总行数
     */
    int querySyncInfoCount(ExamReport examReport);
    /**
     * 新增数据
     *
     * @param examReport 实例对象
     * @return 影响行数
     */
    int insert(ExamReport examReport);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ExamReport> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ExamReport> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ExamReport> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ExamReport> entities);

    /**
     * 修改数据
     *
     * @param examReport 实例对象
     * @return 影响行数
     */
    int update(ExamReport examReport);

    /**
     * 通过主键删除数据
     *
     * @param examNo 主键
     * @return 影响行数
     */
    int deleteById(String examNo);

}

