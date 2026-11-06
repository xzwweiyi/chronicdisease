package com.dchealth.etl.dao.record;

import com.dchealth.etl.domain.record.PatMasterIndex;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 病人主索引患者信息表](PatMasterIndex)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-27 13:44:51
 */
public interface PatMasterIndexMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    PatMasterIndex queryById(String patientId);

    /**
     * 查询指定行数据
     *
     * @param patMasterIndex 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<PatMasterIndex> queryAllByLimit(PatMasterIndex patMasterIndex, @Param("pageable") Pageable pageable);
    /**
     * 查询指定行数据
     *
     * @param patMasterIndex 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<PatMasterIndex> querySyncInfoByPage(@Param("patMasterIndex") PatMasterIndex patMasterIndex, @Param("pageable") Pageable pageable);
    /**
     * 统计总行数
     *
     * @param patMasterIndex 查询条件
     * @return 总行数
     */
    long count(PatMasterIndex patMasterIndex);

    /**
     * 新增数据
     *
     * @param patMasterIndex 实例对象
     * @return 影响行数
     */
    int insert(PatMasterIndex patMasterIndex);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<PatMasterIndex> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<PatMasterIndex> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<PatMasterIndex> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<PatMasterIndex> entities);

    /**
     * 修改数据
     *
     * @param patMasterIndex 实例对象
     * @return 影响行数
     */
    int update(PatMasterIndex patMasterIndex);

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 影响行数
     */
    int deleteById(String patientId);

}

