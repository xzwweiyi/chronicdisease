package com.dchealth.etl.dao.record;

import com.dchealth.etl.domain.record.ExamMaster;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 检查主记录(ExamMaster)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-29 16:02:01
 */
public interface ExamMasterMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param examNo 主键
     * @return 实例对象
     */
    ExamMaster queryById(String examNo);

    /**
     * 查询指定行数据
     *
     * @param examMaster 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ExamMaster> queryAllByLimit(ExamMaster examMaster, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param examMaster 查询条件
     * @return 总行数
     */
    long count(ExamMaster examMaster);
    /**
     * 查询当天同步数据量
     * @param examMaster
     * @return
     */
    int querySyncInfoCount(ExamMaster examMaster);

    /**
     * 分页查询
     * @param examMaster
     * @param pageable
     * @return
     */
    List<ExamMaster> querySyncInfoByPage(@Param("examMaster") ExamMaster examMaster, @Param("pageable") Pageable pageable);
    /**
     * 新增数据
     *
     * @param examMaster 实例对象
     * @return 影响行数
     */
    int insert(ExamMaster examMaster);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ExamMaster> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ExamMaster> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ExamMaster> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ExamMaster> entities);

    /**
     * 修改数据
     *
     * @param examMaster 实例对象
     * @return 影响行数
     */
    int update(ExamMaster examMaster);

    /**
     * 通过主键删除数据
     *
     * @param examNo 主键
     * @return 影响行数
     */
    int deleteById(String examNo);

}

