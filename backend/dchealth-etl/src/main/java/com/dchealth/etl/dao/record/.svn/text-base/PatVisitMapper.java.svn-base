package com.dchealth.etl.dao.record;

import com.dchealth.etl.domain.record.PatVisit;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 病人住院主记录(PatVisit)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-13 09:26:57
 */
public interface PatVisitMapper {

    /**
     * 查询记录
     * @param patientId
     * @return
     */
    List<PatVisit> queryPatVisit(String patientId);
    /**
     * 查询指定行数据
     *
     * @param patVisit 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<PatVisit> queryAllByLimit(PatVisit patVisit, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param patVisit 查询条件
     * @return 总行数
     */
    long count(PatVisit patVisit);

    /**
     * 新增数据
     *
     * @param patVisit 实例对象
     * @return 影响行数
     */
    int insert(PatVisit patVisit);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<PatVisit> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<PatVisit> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<PatVisit> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<PatVisit> entities);

    /**
     * 修改数据
     *
     * @param patVisit 实例对象
     * @return 影响行数
     */
    int update(PatVisit patVisit);

    int querySyncInfoCount(PatVisit patVisit);
    /**
     * 查询指定行数据
     *
     * @param patVisit 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<PatVisit> querySyncInfoByPage(@Param("patVisit") PatVisit patVisit, @Param("pageable") Pageable pageable);

}

